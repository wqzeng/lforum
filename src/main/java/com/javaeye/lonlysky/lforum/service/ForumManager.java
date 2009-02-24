package com.javaeye.lonlysky.lforum.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springside.modules.orm.hibernate.SimpleHibernateTemplate;

import com.javaeye.lonlysky.lforum.cache.LForumCache;
import com.javaeye.lonlysky.lforum.comm.utils.ForumUtils;
import com.javaeye.lonlysky.lforum.comm.utils.Utils;
import com.javaeye.lonlysky.lforum.entity.forum.Forums;
import com.javaeye.lonlysky.lforum.entity.forum.IndexPageForumInfo;
import com.javaeye.lonlysky.lforum.entity.forum.Posts;
import com.javaeye.lonlysky.lforum.entity.forum.Topics;
import com.javaeye.lonlysky.lforum.entity.forum.Users;
import com.javaeye.lonlysky.lforum.exception.ServiceException;

/**
 * 板块业务操作
 * 
 * @author 黄磊
 *
 */
@Service
@Transactional
public class ForumManager {

	private static final Logger logger = LoggerFactory.getLogger(ForumManager.class);
	private SimpleHibernateTemplate<Forums, Integer> forumDAO;

	@Autowired
	private UserGroupManager userGroupManager;

	@Autowired
	private PostManager postManager;

	/**
	 * 主题统计
	 */
	public static final String TOPIC_COUNT = "topiccount";

	/**
	 * 帖子统计
	 */
	public static final String POST_COUNT = "postcount";

	/**
	 * 今日帖子统计
	 */
	public static final String TODAY_COUNT = "todaycount";

	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		forumDAO = new SimpleHibernateTemplate<Forums, Integer>(sessionFactory, Forums.class);
	}

	/**
	 * 获得分类和版块列表
	 * @param fid 版块id
	 * @param hideprivate 是否显示无权限的版块
	 * @param usergroupid 用户组id
	 * @param moderstyle 版主显示样式
	 * @param countMap 返回统计集合
	 * @return 板块列表
	 */
	@SuppressWarnings("unchecked")
	public List<Forums> getForumIndexList(int fid, int hideprivate, int usergroupid, int moderstyle,
			Map<String, Integer> countMap) {
		if (logger.isDebugEnabled()) {
			logger.debug("获得分类和版块列表：fid - " + fid + ",hideprivate - " + hideprivate + ",usergroupid - " + usergroupid
					+ ",moderstyle - " + moderstyle);
		}
		int topiccount = 0, postcount = 0, todaycount = 0;
		List<Forums> forumList = new ArrayList<Forums>();
		if (fid > 0) { // 获取指定板块列表
			forumList = getForumList(fid);
		} else { // 获取首页列表
			forumList = forumDAO
					.find("from Forums where forums.fid not in(select fid from Forums where status<1 and layer=0) and status>0 and layer<=1 order by displayorder");
		}

		int status = 0, colcount = 1;
		for (Forums forums : forumList) {
			forumDAO.getSession().evict(forums);
			if (forums.getStatus() > 0) {
				if (forums.getForums().getFid() == 0 && forums.getSubforumcount() > 0) {
					colcount = forums.getColcount();
					status = colcount;
					forums.setStatus(status + 1);
				} else {
					status++;
					forums.setStatus(status);
					forums.setColcount(colcount);
				}
			}//end if

			if (hideprivate == 1 && !forums.getForumfields().getViewperm().equals("")
					&& !Utils.inArray(usergroupid + "", forums.getForumfields().getPostperm())) {
				forumList.remove(forums);
				continue;
			}

			// 版主列表
			String[] moderatorslist = forums.getForumfields().getModerators().split(",");
			StringBuilder sb = new StringBuilder();
			// 判断版主显示方式并组装HTML语句
			for (String string : moderatorslist) {
				if (moderstyle == 0) {
					if (!string.equals("")) {
						if (!sb.toString().equals("")) {
							sb.append(",");
						}
						sb.append("<a href=\"userinfo.action?username=");
						sb.append(Utils.urlEncode(string.trim()));
						sb.append("\" target=\"_blank\">");
						sb.append(string.trim());
						sb.append("</a>");
					}
				} else {
					if (!string.equals("")) {
						sb.append("<option value=\"");
						sb.append(string.trim());
						sb.append("\">");
						sb.append(string.trim());
						sb.append("</option>");
					}
				}//end if
			}//end for

			if (!sb.toString().equals("") && moderstyle == 1) {
				sb
						.insert(0,
								"<select style=\"width: 100px;\" onchange=\"window.open('userinfo.action?username=' + escape(this.value));\">");
				sb.append("</select>");
			}
			forums.getForumfields().setModerators(sb.toString());

			// 设置今日发帖数
			if (forums.getLastpost().trim().equals("")) {
				forums.setTodayposts(0);
			} else {
				try {
					Date date1 = new SimpleDateFormat(Utils.SHORT_DATEFORMAT).parse(forums.getLastpost());
					Date date2 = new SimpleDateFormat(Utils.SHORT_DATEFORMAT).parse(Utils.getNowTime());
					if (!Utils.dateFormat(date1, Utils.SHORT_DATEFORMAT).equals(
							Utils.dateFormat(date2, Utils.SHORT_DATEFORMAT))) {
						// 如果最后发帖日期不等于当前日期
						forums.setTodayposts(0);
					}
				} catch (ParseException e) {
					throw new ServiceException("转换发帖时间出错");
				}

			}//end if

			// 获取统计
			if (forums.getLayer() > 0) {
				topiccount = topiccount + forums.getTopics_1();
				postcount = postcount + forums.getPosts();
				todaycount = todaycount + forums.getTodayposts();
			}
		}
		if (countMap != null) {
			countMap.put(TOPIC_COUNT, topiccount);
			countMap.put(POST_COUNT, postcount);
			countMap.put(TODAY_COUNT, todaycount);
			if (logger.isDebugEnabled()) {
				logger.debug("主题统计：" + countMap.get(TOPIC_COUNT) + "，帖子统计：" + countMap.get(POST_COUNT) + "，今日帖子："
						+ countMap.get(TODAY_COUNT));
			}
		}
		return forumList;
	}

	/**
	 * 获得版块下的子版块列表
	 * @param fid 版块id
	 * @return 子版块列表
	 */
	@SuppressWarnings("unchecked")
	public List<Forums> getForumList(int fid) {
		List<Forums> forumList = LForumCache.getInstance().getListCache("ForumList" + fid, Forums.class);
		if (forumList == null) {
			forumList = new ArrayList<Forums>();
			if (fid < 0) {
				return forumList;
			}
			forumList = forumDAO.find("from Forums where forums.fid=? and status>0 order by displayorder", fid);

			int status = 0, colcount = 1;
			for (Forums forums : forumList) {
				forumDAO.getSession().evict(forums);
				if (forums.getStatus() > 0) {
					if (colcount > 1) {
						status++;
						forums.setStatus(status);
						forums.setColcount(colcount);
					} else if (forums.getSubforumcount() > 0 && forums.getColcount() > 0) {
						colcount = forums.getColcount();
						status = colcount;
						forums.setStatus(status + 1);
					}//end if
				}
			}//end for
			if (logger.isDebugEnabled()) {
				logger.debug("获取父论坛ID为{}的论坛{}个", fid, forumList.size());
			}
			LForumCache.getInstance().addCache("ForumList" + fid, forumList);
		}
		return forumList;
	}

	/**
	 * 返回用户所在的用户组是否有权在该版块发主题或恢复
	 * @param perm 用户组
	 * @param usergroupid 用户所在组别
	 * @return boolean
	 */
	public boolean hasPerm(String perm, int usergroupid) {
		if (perm == null || perm.trim().equals("")) {
			return true;
		}
		return Utils.inArray(usergroupid + "", perm);
	}

	/**
	 * 返回用户所在的用户组是否有权浏览该版块
	 * @param viewperm 查看权限的用户组id列表
	 * @param usergroupid 用户组id
	 * @return boolean
	 */
	public boolean allowView(String viewperm, int usergroupid) {
		return hasPerm(viewperm, usergroupid);
	}

	/**
	 * 返回用户所在的用户组是否有权在该版块发主题
	 * @param viewperm 用户组
	 * @param usergroupid 用户组id
	 * @return boolean
	 */
	public boolean allowPost(String postperm, int usergroupid) {
		return hasPerm(postperm, usergroupid);
	}

	/**
	 * 返回用户所在的用户组是否有权在该版块发回复
	 * @param viewperm 用户组
	 * @param usergroupid 用户组id
	 * @return boolean
	 */
	public boolean allowReply(String replyperm, int usergroupid) {
		return hasPerm(replyperm, usergroupid);
	}

	/**
	 * 返回用户所在的用户组是否有权在该版块下载附件
	 * @param viewperm 允许下载附件的用户组id列表
	 * @param usergroupid 用户组id
	 * @return boolean
	 */
	public boolean allowGetAttach(String getattachperm, int usergroupid) {
		return hasPerm(getattachperm, usergroupid);
	}

	/**
	 * 返回用户所在的用户组是否有权在该版块上传附件
	 * @param viewperm 允许上传附件的用户组id列表
	 * @param usergroupid 用户组id
	 * @return boolean
	 */
	public boolean allowPostAttach(String postattachperm, int usergroupid) {
		return hasPerm(postattachperm, usergroupid);
	}

	/**
	 * 返回用户是否有权浏览该版块
	 * @param permuserlist 查看当前版块的相关权限
	 * @param userid 查看权限的用户id
	 * @return 是否有权限
	 */
	public boolean allowViewByUserID(String permuserlist, int userid) {
		if (!permuserlist.trim().equals("")) {
			int power = getForumSpecialUserPower(permuserlist, userid);
			if (((int) (power & ForumSpecialUserPower.VIEWBYUSER)) > 0) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 返回用户是否有权在该版块发主题
	 * @param permuserlist 查看当前版块的相关权限
	 * @param userid 查看权限的用户id
	 * @return
	 */
	public boolean allowPostByUserID(String permuserlist, int userid) {
		if (!Utils.null2String(permuserlist).equals("")) {
			int forumspecialuserpower = getForumSpecialUserPower(permuserlist, userid);
			if (((int) (forumspecialuserpower & ForumSpecialUserPower.POSTBYUSER)) > 0) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 返回用户是否有权在该版块上传附件
	 * @param permuserlist 查看当前版块的相关权限
	 * @param userid 查看权限的用户id
	 * @return
	 */
	public boolean allowPostAttachByUserID(String permuserlist, int userid) {
		if (!Utils.null2String(permuserlist).equals("")) {
			int forumspecialuserpower = getForumSpecialUserPower(permuserlist, userid);
			if (((int) (forumspecialuserpower & ForumSpecialUserPower.POSTATTACHBYUSER)) > 0) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 判断指定的主题分类是否属于本版块可用的范围之内
	 * @param typeid 主题分类Id
	 * @param topictypes 主题分类Id
	 * @return 是否属于
	 */
	public boolean isCurrentForumTopicType(String typeid, String topictypes) {
		if (Utils.null2String(topictypes).equals("")) {
			return true;
		}
		for (String topictype : topictypes.split("|")) {
			if (!topictype.trim().equals("")) {
				if (typeid.trim().equals(topictype.split(",")[0].trim())) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 获取指定版块特殊用户的权限
	 * @param permuserlist
	 * @param userid 用户ID
	 * @return
	 */
	private int getForumSpecialUserPower(String permuserlist, int userid) {
		for (String currentinf : permuserlist.split("|")) {
			if (!currentinf.trim().equals("")) {
				if (currentinf.split(",")[1].equals(userid + "")) {
					return Utils.null2Int(currentinf.split(",")[2]);
				}
			}
		}
		return 0;
	}

	/**
	 * 返回全部版块列表
	 * @return 全部版块列表
	 */
	@SuppressWarnings("unchecked")
	public List<Forums> getForumList() {
		if (logger.isDebugEnabled()) {
			logger.debug("获取所有板块列表");
		}
		return forumDAO.find("from Forums order by displayorder asc");
	}

	/**
	 * 返回指定条件块列表
	 * @return 
	 */
	@SuppressWarnings("unchecked")
	public List<Forums> getForumList(String hql) {
		if (logger.isDebugEnabled()) {
			logger.debug("获取指定条件 {} 板块列表", hql);
		}
		return forumDAO.find("from Forums where " + hql + " order by displayorder asc");
	}

	/**
	 * 获得指定的分类或版块信息
	 * @param fid 分类或版块ID
	 * @return 返回分类或版块的信息
	 */
	public Forums getForumInfo(int fid) {
		try {
			Forums forums = forumDAO.get(fid);
			forums.setPathlist(forums.getPathlist().replace("a><a", "a> &raquo; <a"));
			return forums;
		} catch (Exception e) {
			return null;
		}
		//		List<Forums> forumList = getForumList();
		//		if (forumList == null) {
		//			return new Forums();
		//		}
		//		for (Forums forums : forumList) {
		//			if (forums.getFid() == fid) {
		//				forums.setPathlist(forums.getPathlist().replace("a><a", "a> &raquo; <a"));
		//				return forums;
		//			}
		//		}
		//		return null;
	}

	/**
	 * 设置当前版块主题数(不含子版块)
	 * @param fid 版块id
	 * @return 主题数
	 */
	public int setRealCurrentTopics(int fid) {
		return forumDAO
				.createQuery(
						"update Forums set curtopics = (select count(tid) from Topics where displayorder >= 0 and forums.fid=?) where fid=?",
						fid, fid).executeUpdate();
	}

	/**
	 * 获取首页版块列表集合	
	 * @param hideprivate 是否显示无权限的版块
	 * @param usergroupid 用户组id
	 * @param moderstyle 版主显示样式
	 * @param countMap 返回统计集合
	 * @return 板块列表
	 * @return
	 */
	public List<IndexPageForumInfo> getIndexPageForum(int hideprivate, int usergroupid, int moderstyle,
			Map<String, Integer> countMap) {
		List<IndexPageForumInfo> indexForumList = new ArrayList<IndexPageForumInfo>();
		List<Forums> forumList = getForumIndexList(0, hideprivate, usergroupid, moderstyle, countMap);

		for (Forums forums : forumList) {
			IndexPageForumInfo forumInfo = new IndexPageForumInfo();
			try {
				BeanUtils.copyProperties(forumInfo, forums);
			} catch (Exception e) {
				throw new ServiceException("获取首页板块属性出错");
			}
			try {
				// 是否有新帖
				if (Utils.howLong("m", forumInfo.getLastpost(), Utils.getNowTime()) < 600) {
					forumInfo.setHavenew("new");
				} else {
					forumInfo.setHavenew("old");
				}

				// 判断是否收起
				if (forumInfo.getLayer() == 0
						&& ForumUtils.getCookie("lforum_collapse").indexOf("fid=" + forumInfo.getFid()) > -1) {
					forumInfo.setCollapse("display: none;");
				} else {
					forumInfo.setCollapse("");
				}
			} catch (ParseException e) {
				throw new ServiceException("获取首页板块列表失败");
			}
			indexForumList.add(forumInfo);
		}
		return indexForumList;
	}

	/**
	 * 获取获得子版块列表
	 * @param hideprivate 是否显示无权限的版块
	 * @param usergroupid 用户组id
	 * @param moderstyle 版主显示样式
	 * @param countMap 返回统计集合
	 * @return 板块列表
	 * @return
	 */
	public List<IndexPageForumInfo> getSubForumList(int fid, int colcount, int hideprivate, int usergroupid,
			int moderstyle) {
		List<IndexPageForumInfo> indexForumList = new ArrayList<IndexPageForumInfo>();
		List<Forums> forumList = getForumIndexList(fid, hideprivate, usergroupid, moderstyle, null);

		for (Forums forums : forumList) {
			IndexPageForumInfo forumInfo = new IndexPageForumInfo();
			try {
				BeanUtils.copyProperties(forumInfo, forums);
			} catch (Exception e) {
				throw new ServiceException("获取板块" + fid + "的子板块属性出错");
			}
			try {
				// 是否有新帖
				if (Utils.howLong("m", forumInfo.getLastpost(), Utils.getNowTime()) < 600) {
					forumInfo.setHavenew("new");
				} else {
					forumInfo.setHavenew("old");
				}

				// 判断是否收起
				if (forumInfo.getLayer() == 0
						&& ForumUtils.getCookie("lforum_collapse").indexOf("fid=" + forumInfo.getFid()) > -1) {
					forumInfo.setCollapse("display: none;");
				} else {
					forumInfo.setCollapse("");
				}
			} catch (ParseException e) {
				throw new ServiceException("获取板块" + fid + "的子板块列表失败");
			}
			indexForumList.add(forumInfo);
		}
		return indexForumList;
	}

	/**
	 * 得到当前版块的主题类型选项
	 * @param fid 板块ID
	 * @param topictypes
	 * @return 主题类型字符串
	 */
	public String getCurrentTopicTypesOption(int fid, String topictypes) {
		//判断当前版块没有相应主题分类时
		if (topictypes.equals("") || topictypes.equals("|")) {
			return "";
		}

		String topictypeoptions = LForumCache.getInstance().getCache("CurrentTopicTypesOption" + fid, String.class);
		if (topictypeoptions != null) {
			return topictypeoptions;
		}
		StringBuilder builder = new StringBuilder();
		builder.append("<option value=\"0\"></option>");
		for (String topictype : topictypes.split("|")) {
			if (topictype.trim() != "") {
				builder.append("<option value=\"");
				builder.append(topictype.split(",")[0]);
				builder.append("\">");
				builder.append(topictype.split(",")[1]);
				builder.append("</option>");
			}
		}
		topictypeoptions = builder.toString();
		LForumCache.getInstance().addCache("CurrentTopicTypesOption" + fid, topictypeoptions);
		return topictypeoptions;
	}

	/**
	 * 得到当前版块的主题类型链接串 
	 * @param fid 板块ID
	 * @param topictypes
	 * @param fullpagename
	 * @return 当前版块的主题类型链接串
	 */
	public String getCurrentTopicTypesLink(int fid, String topictypes, String fullpagename) {
		if ((topictypes == null) || (topictypes.equals(""))) {
			return "";
		}
		String topictypelinks = LForumCache.getInstance().getCache("CurrentTopicTypesLink" + fid, String.class);
		if (topictypelinks != null) {
			return topictypelinks;
		}
		StringBuilder builder = new StringBuilder();
		StringBuilder dropbuilder = new StringBuilder();
		for (String topictype : topictypes.split("|")) {
			if (topictype.trim() != "") {
				if (topictype.split(",")[2].equals("0")) //平版模式
				{
					builder.append("<a href=\"");
					builder.append(fullpagename + "?forumid=" + fid);
					builder.append("&typeid=");
					builder.append(topictype.split(",")[0]);
					builder.append("\">");
					builder.append(topictype.split(",")[1]);
					builder.append("</a>&nbsp;&nbsp;");
				} else //下拉类型
				{
					dropbuilder.append("<p><a href=\"");
					dropbuilder.append(fullpagename + "?forumid=" + fid);
					dropbuilder.append("&typeid=");
					dropbuilder.append(topictype.split(",")[0]);
					dropbuilder.append("\">");
					dropbuilder.append(topictype.split(",")[1]);
					dropbuilder.append("</a></p>");
				}
			}
		}//end for

		if (dropbuilder.toString() != "") {
			builder
					.append("<span id=\"topictypedrop\" onmouseover=\"showMenu(this.id, true);\" style=\"CURSOR:pointer\"><a href=\"###\">更多分类 ...</a></span>");
			builder
					.append("<div class=\"popupmenu_popup popupmenu_topictype\" id=\"topictypedrop_menu\" style=\"DISPLAY: none\">");
			builder.append("<div class=\"popupmenu_topictypeoption\">");
			builder.append(dropbuilder.toString().trim());
			builder.append("</div>");
			builder.append("</div>");
		}
		topictypelinks = builder.toString();
		LForumCache.getInstance().addCache("CurrentTopicTypesLink" + fid, topictypelinks);
		return topictypelinks;
	}

	/**
	 * 更新板块信息
	 * 
	 * @param forum 板块 
	 */
	public void updateForum(Forums forum) {
		forumDAO.save(forum);
	}

	/**
	 * 返回访问过的论坛的列表html
	 * @param count 最大显示条数
	 * @return 列表html
	 */
	public String getVisitedForumsOptions(int count) {
		if (ForumUtils.getCookie("visitedforums").equals("")) {
			return "";
		}
		StringBuilder sb = new StringBuilder();
		String[] strfid = ForumUtils.getCookie("visitedforums").split(",");
		for (int fidIndex = 0; fidIndex < strfid.length; fidIndex++) {
			Forums foruminfo = getForumInfo(Utils.null2Int(strfid[fidIndex], 0));
			if (foruminfo != null) {
				sb.append("<option value=\"");
				sb.append(strfid[fidIndex]);
				sb.append("\">");
				sb.append(foruminfo.getName());
				sb.append("</option>");
			}
		}
		return sb.toString();
	}

	/**
	 * 返回用户是否有权在该版块发回复
	 * @param Permuserlist 查看当前版块的相关权限
	 * @param userid 查看权限的用户id
	 * @return
	 */
	public boolean allowReplyByUserID(String permuserlist, int userid) {
		if (!Utils.null2String(permuserlist).equals("")) {
			int forumspecialuserpower = getForumSpecialUserPower(permuserlist, userid);
			if (((int) (forumspecialuserpower & ForumSpecialUserPower.REPLYBYUSER)) > 0) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 返回用户是否有权在该版块下载附件
	 * @param permuserlist 查看当前版块的相关权限
	 * @param userid 查看当前版块的相关权限
	 * @return
	 */
	public boolean allowGetAttachByUserID(String permuserlist, int userid) {
		if (!Utils.null2String(permuserlist).equals("")) {
			int forumspecialuserpower = getForumSpecialUserPower(permuserlist, userid);
			if (((int) (forumspecialuserpower & ForumSpecialUserPower.DOWNLOADATTACHBYUSER)) > 0) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 获取指定父ID的最大显示层
	 * @param parentid 父ID
	 * @return
	 */
	public int getForumsMaxDisplayOrder(int parentid) {
		return Utils.null2Int(forumDAO.findUnique("select max(displayorder) from Forums where forums.fid=?", parentid),
				-1);
	}

	/**
	 * 获取最大显示层
	 * @param parentid 父ID
	 * @return
	 */
	public int getForumsMaxDisplayOrder() {
		return Utils.null2Int(forumDAO.findUnique("select max(displayorder) from Forums"), -1);
	}

	/**
	 * 板块用户权限
	 * 
	 * @author 黄磊
	 *
	 */
	public interface ForumSpecialUserPower {
		public final int VIEWBYUSER = 1;
		public final int POSTBYUSER = 2;
		public final int REPLYBYUSER = 4;
		public final int DOWNLOADATTACHBYUSER = 8;
		public final int POSTATTACHBYUSER = 16;
	}

	/**
	 * 更新指定版块的最新发帖数信息
	 * @param forum
	 */
	public void updateLastPost(Forums foruminfo) {
		Posts postinfo;
		int tid = postManager.getLastPostTid(foruminfo, getVisibleForum());
		if (tid > 0) {
			postinfo = postManager.getLastPostByTid(tid);
			if (postinfo == null) {
				postinfo = new Posts();
				postinfo.setPid(0);
				postinfo.setTopics(new Topics(0));
				postinfo.setTitle("从未");
				postinfo.setPostdatetime("1900-1-1 00:00:00");
				postinfo.setPoster("");
				postinfo.setUsers(new Users(0));

			}
		} else {
			postinfo = new Posts();
			postinfo.setPid(0);
			Topics topics = new Topics();
			topics.setTid(0);
			postinfo.setTopics(topics);
			postinfo.setTitle("从未");
			postinfo.setPostdatetime("1900-1-1 00:00:00");
			postinfo.setPoster("");
			Users users = new Users();
			users.setUid(0);
			postinfo.setUsers(users);
		}
		updateLastPost(foruminfo, postinfo);
		if (foruminfo.getLayer() > 0) { //递归调用并更新相应父版块信息
			foruminfo = getForumInfo(foruminfo.getForums().getFid());
			updateLastPost(foruminfo);
		}
	}

	/**
	 * 更新指定板块最后发帖
	 * @param foruminfo
	 * @param postinfo
	 */
	private void updateLastPost(Forums foruminfo, Posts postinfo) {
		forumDAO.createQuery(
				"update Forums set topics.tid=?, lasttitle=?, lastpost=?,users.uid=?,lastposter=? where fid=? or fid in ("
						+ foruminfo.getParentidlist() + ")", postinfo.getTopics().getTid(),
				postinfo.getTopics().getTitle(), postinfo.getPostdatetime(), postinfo.getUsers().getUid(),
				postinfo.getPoster(), foruminfo.getFid()).executeUpdate();

	}

	/**
	 * 获得可见的板块列表
	 * @return 返回值是以英文逗号分割的板块ID
	 */
	public String getVisibleForum() {
		StringBuilder result = new StringBuilder();
		List<Forums> forumlist = getForumList();
		if (forumlist == null) {
			return "";
		}

		for (Forums foruminfo : forumlist) {

			if (foruminfo.getStatus() > 0) {
				if (Utils.null2String(foruminfo.getForumfields().getViewperm()).equals("")) { //当板块权限为空时，按照用户组权限
					if (userGroupManager.getUsergroup(7).getAllowvisit() != 1) {
						continue;
					}
				} else {//当板块权限不为空，按照板块权限
					if (!allowView(Utils.null2String(foruminfo.getForumfields().getViewperm()), 7)) {
						continue;
					}
				}
				result.append("," + foruminfo.getFid());
			}
		}

		if (result.length() > 0)
			return result.delete(0, 1).toString();
		else
			return "";
	}

	/**
	 * 获取板块ID列表
	 * @param lastfid
	 * @param statcount
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Integer> getTopForumFids(int lastfid, int statcount) {
		return forumDAO.createQuery("select fid from Forums where fid>?", lastfid).setMaxResults(statcount).list();
	}

	/**
	 * 获取板块最后回复帖子
	 * @param lastfid
	 * @param topiccount
	 * @param postcount
	 * @param lasttid
	 * @param lasttitle
	 * @param lastpost
	 * @param lastposterid
	 * @param lastposter
	 * @param todaypostcount
	 * @return
	 */
	public Object[] getForumLastPost(int lastfid, int topiccount, int postcount, int lasttid, String lasttitle,
			String lastpost, int lastposterid, String lastposter, int todaypostcount) {
		return (Object[]) forumDAO
				.createQuery(
						"select topics.tid,title,postdatetime,users.uid,poster From Posts where forums.fid=? order by pid desc",
						lastfid).setMaxResults(1).uniqueResult();
	}

	/**
	 * 获取分类
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Forums> getMainForum() {
		return forumDAO.find("from Forums where layer=0 order by displayorder asc");
	}

	@SuppressWarnings("unchecked")
	public List<Integer> getForumIdList() {
		return forumDAO.find("select fid from Forums");
	}
}
