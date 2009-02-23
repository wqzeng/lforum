package com.javaeye.lonlysky.lforum.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.hibernate.ObjectNotFoundException;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springside.modules.orm.hibernate.SimpleHibernateTemplate;

import com.javaeye.lonlysky.lforum.comm.LForumRequest;
import com.javaeye.lonlysky.lforum.comm.utils.Utils;
import com.javaeye.lonlysky.lforum.entity.forum.Searchcaches;
import com.javaeye.lonlysky.lforum.entity.forum.Usergroups;
import com.javaeye.lonlysky.lforum.entity.forum.Users;
import com.javaeye.lonlysky.lforum.entity.global.SearchType;

/**
 * 搜索操作类
 * 
 * @author 黄磊
 *
 */
@Service
@Transactional
public class SearchManager {

	private static final Logger logger = LoggerFactory.getLogger(SearchManager.class);

	private SimpleHibernateTemplate<Searchcaches, Integer> searchcacheDAO;

	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		searchcacheDAO = new SimpleHibernateTemplate<Searchcaches, Integer>(sessionFactory, Searchcaches.class);
	}

	private Pattern regexForumTopics = Pattern.compile("<ForumTopics>([\\s\\S]+?)</ForumTopics>");

	/**
	 * 删除超过３０分钟的缓存记录
	 */
	public void deleteExpriedSearchCache() {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MINUTE, -30);
		int result = searchcacheDAO.createQuery("delete from Searchcaches where expiration<?",
				Utils.dateFormat(calendar.getTime(), Utils.FULL_DATEFORMAT)).executeUpdate();
		if (logger.isDebugEnabled()) {
			logger.debug("删除超过３０分钟的缓存记录：{}", result);
		}
	}

	private StringBuilder getSearchByPosterResult(List<Object[]> objList) {
		StringBuilder strTids = new StringBuilder("<ForumTopics>");
		if (objList != null) {
			for (Object[] objects : objList) {
				strTids.append(objects[0] + ",");
			}
		}
		if (strTids.toString().endsWith(",")) {
			strTids.setLength(strTids.length() - 1);
		}

		strTids.append("</ForumTopics>");
		if (logger.isDebugEnabled()) {
			logger.debug("组装按照用户查询结果：" + strTids.toString());
		}
		return strTids;
	}

	/**
	 * 根据指定条件进行搜索
	 * @param userid 用户id
	 * @param usergroupid 用户组id
	 * @param keyword 关键字
	 * @param posterid 发帖者id
	 * @param type 搜索类型
	 * @param searchforumid 搜索版块id
	 * @param keywordtype 关键字类型
	 * @param searchtime 搜索时间
	 * @param searchtimetype 搜索时间类型
	 * @param resultorder 结果排序方式
	 * @param resultordertype 结果类型类型
	 * @return 如果成功则返回searchid, 否则返回-1
	 */
	@SuppressWarnings("unchecked")
	public int search(int userid, int usergroupid, String keyword, int posterid, String type, String searchforumid,
			int keywordtype, int searchtime, int searchtimetype, int resultorder, int resultordertype) {
		// 超过30分钟的缓存纪录将被删除
		deleteExpriedSearchCache();
		String hql = "";
		StringBuilder strTids = new StringBuilder();
		SearchType searchType = SearchType.TopicTitle;

		switch (keywordtype) {
		case 0:
			searchType = SearchType.PostTitle;
			if (type.equals("digest")) {
				searchType = SearchType.DigestTopic;
			}
			break;
		case 1:
			searchType = SearchType.PostContent;
			break;
		case 8:
			searchType = SearchType.ByPoster;
			break;
		}
		if (searchType == SearchType.DigestTopic) {
			hql = getSearchTopicTitleHQL(posterid, searchforumid, resultorder, resultordertype, 1, keyword);
		} else if (searchType == SearchType.TopicTitle || searchType == SearchType.PostTitle) {
			hql = getSearchTopicTitleHQL(posterid, searchforumid, resultorder, resultordertype, 0, keyword);
		} else if (searchType == SearchType.PostContent) {
			hql = getSearchPostContentHQL(posterid, searchforumid, resultorder, resultordertype, searchtime,
					searchtimetype, new StringBuilder(keyword));
		} else if (searchType == SearchType.ByPoster) {
			hql = getSearchByPosterHQL(posterid);
		} else {
			hql = getSearchTopicTitleHQL(posterid, searchforumid, resultorder, resultordertype, 0, keyword);
		}

		if (hql.equals("")) {
			return -1;
		}
		int searchid = Utils.null2Int(searchcacheDAO.createQuery(
				"select searchid from Searchcaches where searchstring=? and usergroups.groupid=?", hql, usergroupid)
				.setMaxResults(1).uniqueResult());

		if (searchid > -1) {
			return searchid;
		}

		List objList = searchcacheDAO.find(hql);
		int rowcount = 0;
		if (objList != null) {
			if (searchType == SearchType.ByPoster) {
				strTids = getSearchByPosterResult(objList);
				Searchcaches cacheinfo = new Searchcaches();
				cacheinfo.setKeywords(keyword);
				cacheinfo.setSearchstring(hql);
				cacheinfo.setPostdatetime(Utils.getNowTime());
				cacheinfo.setTopics(rowcount);
				cacheinfo.setTids(strTids.toString());
				Users users = new Users();
				users.setUid(userid);
				cacheinfo.setUsers(users);
				Usergroups usergroups = new Usergroups();
				usergroups.setGroupid(usergroupid);
				cacheinfo.setUsergroups(usergroups);
				cacheinfo.setIp(LForumRequest.getIp());
				cacheinfo.setExpiration(Utils.getNowTime());

				return createSearchCache(cacheinfo);
			} else {
				strTids.append("<ForumTopics>");
			}

			for (Object objects : objList) {
				if (objects instanceof Object[]) {
					Object[] obj = (Object[]) objects;
					strTids.append(obj[0]);
				} else {
					strTids.append(objects);
				}
				strTids.append(",");
				rowcount++;
			}

			if (rowcount > 0) {
				strTids.deleteCharAt(strTids.length() - 1);
				strTids.append("</ForumTopics>");
				Searchcaches cacheinfo = new Searchcaches();
				cacheinfo.setKeywords(keyword);
				cacheinfo.setSearchstring(hql);
				cacheinfo.setPostdatetime(Utils.getNowTime());
				cacheinfo.setTopics(rowcount);
				cacheinfo.setTids(strTids.toString());
				Users users = new Users();
				users.setUid(userid);
				cacheinfo.setUsers(users);
				Usergroups usergroups = new Usergroups();
				usergroups.setGroupid(usergroupid);
				cacheinfo.setUsergroups(usergroups);
				cacheinfo.setIp(LForumRequest.getIp());
				cacheinfo.setExpiration(Utils.getNowTime());

				return createSearchCache(cacheinfo);
			}
		}
		return -1;
	}

	/**
	 * 创建搜索缓存
	 * @param cacheinfo 创建搜索缓存
	 * @return 创建搜索缓存
	 */
	public int createSearchCache(Searchcaches cacheinfo) {
		searchcacheDAO.save(cacheinfo);
		return cacheinfo.getSearchid();
	}

	private String getSearchByPosterHQL(int posterid) {
		if (posterid > 0) {
			String hql = "select distinct topics.tid, 'forum' as datafrom from Posts where users.uid=" + posterid
					+ " and topics.tid not in (select tid from Topics where usersByPosterid.uid=" + posterid
					+ " and displayorder<0)";
			return hql;
		}
		return "";
	}

	private String getSearchPostContentHQL(int posterid, String searchforumid, int resultorder, int resultordertype,
			int searchtime, int searchtimetype, StringBuilder strKeyWord) {
		StringBuilder strHQL = new StringBuilder();

		String orderfield = "lastpost";
		switch (resultorder) {
		case 1:
			orderfield = "tid";
			break;
		case 2:
			orderfield = "replies";
			break;
		case 3:
			orderfield = "views";
			break;
		default:
			orderfield = "lastpost";
			break;
		}

		strHQL.append("select distinct topics.tid,topics." + orderfield
				+ " from Posts where topics.displayorder>=0 and ");

		if (!searchforumid.equals("")) {
			strHQL.append("forums.fid in (");
			strHQL.append(searchforumid);
			strHQL.append(") and ");
		}

		if (posterid != -1) {
			strHQL.append("users.uid=");
			strHQL.append(posterid);
			strHQL.append(" and ");
		}

		if (searchtime != 0) {
			strHQL.append("postdatetime");
			if (searchtimetype == 1) {
				strHQL.append("<'");
			} else {
				strHQL.append(">'");
			}
			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.DAY_OF_MONTH, searchtime);
			strHQL.append(Utils.dateFormat(calendar.getTime(), Utils.SHORT_DATEFORMAT) + " 00:00:00");
			strHQL.append("'and ");
		}

		String[] keywordlist = strKeyWord.toString().split(" ");
		strKeyWord = new StringBuilder();
		for (int i = 0; i < keywordlist.length; i++) {
			strKeyWord.append(" or ");
			strKeyWord.append("message like '%");
			strKeyWord.append(keywordlist[i]);
			strKeyWord.append("%' ");
		}

		strHQL.append(strKeyWord.toString().substring(3));
		strHQL.append("order by topics.");

		switch (resultorder) {
		case 1:
			strHQL.append("tid");
			break;
		case 2:
			strHQL.append("replies");
			break;
		case 3:
			strHQL.append("views");
			break;
		default:
			strHQL.append("lastpost");
			break;
		}
		if (resultordertype == 1) {
			strHQL.append(" asc");
		} else {
			strHQL.append(" desc");
		}

		return strHQL.toString();
	}

	/**
	 * 获取按照帖子标题查询的HQL语句
	 * @param posterid
	 * @param searchforumid
	 * @param resultorder
	 * @param resultordertype
	 * @param digest
	 * @param keyword
	 * @return
	 */
	private String getSearchTopicTitleHQL(int posterid, String searchforumid, int resultorder, int resultordertype,
			int digest, String keyword) {
		keyword = keyword.replaceAll("--|;|'|\"", "");

		// 替换转义字符
		keyword.replace("'", "''");
		StringBuilder strKeyWord = new StringBuilder(keyword);
		StringBuilder strHQL = new StringBuilder();
		strHQL.append("select tid from Topics where displayorder>=0");

		if (posterid > 0) {
			strHQL.append(" and posterid=");
			strHQL.append(posterid);
		}

		if (digest > 0) {
			strHQL.append(" and digest>0 ");
		}

		if (!searchforumid.equals("")) {
			strHQL.append(" and forums.fid in (");
			strHQL.append(searchforumid);
			strHQL.append(")");
		}

		String[] keywordlist = strKeyWord.toString().split(" ");
		strKeyWord = new StringBuilder();

		if (keyword.length() > 0) {
			strKeyWord.append(" and (1=0 ");
			for (int i = 0; i < keywordlist.length; i++) {
				strKeyWord.append(" or title ");

				strKeyWord.append("like '%");
				strKeyWord.append(keywordlist[i]);
				strKeyWord.append("%' ");
			}
			strKeyWord.append(")");
		}

		strHQL.append(strKeyWord.toString());

		strHQL.append(" order by ");
		switch (resultorder) {
		case 1:
			strHQL.append("tid");
			break;
		case 2:
			strHQL.append("replies");
			break;
		case 3:
			strHQL.append("views");
			break;
		default:
			strHQL.append("postdatetime");
			break;
		}

		if (resultordertype == 1) {
			strHQL.append(" asc");
		} else {
			strHQL.append(" desc");
		}

		return strHQL.toString();
	}

	/**
	 * 获指定的搜索缓存的集合
	 * @param searchid 搜索缓存的searchid
	 * @param pagesize 每页的记录数
	 * @param pageindex 当前页码
	 * @param topiccountmap  主题记录数
	 * @param type 搜索类型
	 * @return 搜索缓存的集合
	 */
	@SuppressWarnings("unchecked")
	public List getSearchCacheList(int searchid, int pagesize, int pageindex, Map<Integer, Integer> topiccountmap,
			String type) {
		if (logger.isDebugEnabled()) {
			logger.debug("搜索缓存的集合,ID：{},TYPE：{}", searchid, type);
		}
		topiccountmap.put(0, 0);
		String cachedidlist;
		try {
			Searchcaches caches = searchcacheDAO.get(searchid);
			cachedidlist = caches.getTids();
		} catch (ObjectNotFoundException e) {
			return new ArrayList();
		}

		Matcher m;

		m = regexForumTopics.matcher(cachedidlist);

		if (m.find()) {
			String tids = getCurrentPageTids(m.group(1), topiccountmap, pagesize, pageindex);

			if (tids.equals("")) {
				return new ArrayList();
			}

			if (type.equals("digest")) {
				return searchcacheDAO
						.createQuery(
								"select tid, title, poster, usersByPosterid.uid, postdatetime, replies, views, lastpost,lastposter,usersByLastposterid.uid, forums.fid,forums.name from Topics as topic where tid in("
										+ tids + ")").setMaxResults(pagesize).list();
			}
			if (type.equals("post")) {
				return searchcacheDAO
						.createQuery(
								"select topics.tid, title, poster, users.uid, postdatetime,lastedit, rate, ratetimes,topics.usersByLastposterid.uid forums.fid,forums.name from Posts as post where pid in("
										+ tids + ")").setMaxResults(pagesize).list();
			} else {
				return searchcacheDAO
						.createQuery(
								"select tid, title, poster, usersByPosterid.uid, postdatetime, replies, views, lastpost,lastposter,usersByLastposterid.uid, forums.fid,forums.name from Topics as topic where tid in("
										+ tids + ")").setMaxResults(pagesize).list();
			}
		}

		return new ArrayList();
	}

	/**
	 * 获得当前页的Tid列表
	 * @param tids 全部Tid列表
	 * @param topiccountmap
	 * @param pagesize
	 * @param pageindex
	 * @return
	 */
	private String getCurrentPageTids(String tids, Map<Integer, Integer> topiccountmap, int pagesize, int pageindex) {
		String[] tid = tids.split(",");
		topiccountmap.put(0, tid.length);
		int pagecount = tid.length % pagesize == 0 ? tid.length / pagesize : tid.length / pagesize + 1;
		if (pagecount < 1) {
			pagecount = 1;
		}
		if (pageindex > pagecount) {
			pageindex = pagecount;
		}
		int startindex = pagesize * (pageindex - 1);
		StringBuilder strTids = new StringBuilder();
		for (int i = startindex; i < tid.length; i++) {
			if (i > startindex + pagesize) {
				break;
			} else {
				strTids.append(tid[i]);
				strTids.append(",");
			}
		}
		strTids.deleteCharAt(strTids.length() - 1);

		return strTids.toString();
	}
}
