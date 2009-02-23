package com.javaeye.lonlysky.lforum.service;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.time.DateUtils;
import org.dom4j.Document;
import org.dom4j.Element;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springside.modules.orm.hibernate.Page;
import org.springside.modules.orm.hibernate.SimpleHibernateTemplate;

import com.javaeye.lonlysky.lforum.comm.utils.ForumUtils;
import com.javaeye.lonlysky.lforum.comm.utils.Utils;
import com.javaeye.lonlysky.lforum.comm.utils.XmlElementUtil;
import com.javaeye.lonlysky.lforum.config.impl.ConfigLoader;
import com.javaeye.lonlysky.lforum.entity.forum.Debates;
import com.javaeye.lonlysky.lforum.entity.forum.ForumStatistics;
import com.javaeye.lonlysky.lforum.entity.forum.Forums;
import com.javaeye.lonlysky.lforum.entity.forum.Myposts;
import com.javaeye.lonlysky.lforum.entity.forum.Mytopics;
import com.javaeye.lonlysky.lforum.entity.forum.Postid;
import com.javaeye.lonlysky.lforum.entity.forum.ShowforumPageTopicInfo;
import com.javaeye.lonlysky.lforum.entity.forum.Topics;
import com.javaeye.lonlysky.lforum.entity.forum.Users;

/**
 * 主题操作类
 * 
 * @author 黄磊
 *
 */
@Service
@Transactional
public class TopicManager {
	private static final Logger logger = LoggerFactory.getLogger(TopicManager.class);

	private SimpleHibernateTemplate<Topics, Integer> topicDAO;
	private SimpleHibernateTemplate<Debates, Integer> debateDAO;
	private SimpleHibernateTemplate<Mytopics, Integer> mytopicDAO;
	private SimpleHibernateTemplate<Myposts, Integer> mypostDAO;

	@Autowired
	private ForumManager forumManager;

	@Autowired
	private CachesManager cachesManager;

	@Autowired
	private StatisticManager statisticManager;

	@Autowired
	private TopicAdminManager topicAdminManager;

	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		mypostDAO = new SimpleHibernateTemplate<Myposts, Integer>(sessionFactory, Myposts.class);
		topicDAO = new SimpleHibernateTemplate<Topics, Integer>(sessionFactory, Topics.class);
		debateDAO = new SimpleHibernateTemplate<Debates, Integer>(sessionFactory, Debates.class);
		mytopicDAO = new SimpleHibernateTemplate<Mytopics, Integer>(sessionFactory, Mytopics.class);
	}

	/**
	 * 得到当前版块内正常(未关闭)主题总数
	 * @param fid 版块ID
	 * @return 主题总数
	 */
	public int getTopicCount(int fid) {
		int count = Utils.null2Int(topicDAO.findUnique("select curtopics from Forums where fid=?", fid), 0);
		if (logger.isDebugEnabled()) {
			logger.debug("得到板块{}主题总数{}", fid, count);
		}
		return count;
	}

	/**
	 * 得到符合条件的主题总数
	 * @param condition
	 * @return
	 */
	public int getTopicCount(String condition) {
		int count = Utils.null2Int(topicDAO
				.findUnique("select count(tid) from Topics where displayorder>-1 and closed<=1 " + condition), 0);
		return count;
	}

	/**
	 * 得到当前版块内(包括子版)正常(未关闭)主题总数
	 * @param fid 版块ID
	 * @return 主题总数
	 */
	public int getAllTopicCount(int fid) {
		int count = Utils
				.null2Int(
						topicDAO
								.findUnique(
										"select count(tid) from Topics where (forums.fid=? or forums.fid in (  select fid  from Forums  where  charindex(',' + trim(?) + ',', ',' + trim(parentidlist) + ',') > 0)) and displayorder>=0",
										fid, fid), 0);
		if (logger.isDebugEnabled()) {
			logger.debug("得到当前版块内(包括子版){}正常(未关闭)主题总数{}", fid, count);
		}
		return count;
	}

	/**
	 * 得到当前版块内主题总数
	 * @param fid 版块ID
	 * @param condition 条件
	 * @param includeclosedtopic 
	 * @return 主题总数
	 */
	public int getTopicCount(int fid, String condition, boolean includeclosedtopic) {
		int count = 0;
		int state = includeclosedtopic ? -1 : 0;
		if (state == -1) {
			count = Utils.null2Int(topicDAO.findUnique(
					"select count(tid) from Topics where fid=? and displayorder>-1 and closed<=1" + condition, fid), 0);
		} else {
			count = Utils.null2Int(topicDAO.findUnique(
					"select count(tid) from Topics where fid=? and displayorder>-1 and closed=? and closed<=1"
							+ condition, fid, state), 0);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("得到板块{}主题总数{},查询条件：" + condition, fid, count);
		}
		return count;
	}

	/**
	 * 得到当前版块内主题总数
	 * @param fid 版块ID
	 * @param condition 条件
	 * @return 主题总数
	 */
	public int getTopicCount(int fid, String condition) {
		return getTopicCount(fid, condition, false);
	}

	/**
	 * 创建新主题
	 * @param topics 主题信息
	 */
	public void createTopic(Topics topics) {
		Postid postid = new Postid();
		postid.setPid(0);
		Users users = new Users();
		users.setUid(0);

		topics.setUsersByLastposterid(users);
		topics.setPostid(postid);
		topics.setIdentify(0);
		topics.setPoll(0);
		topicDAO.save(topics);
		if (topics.getDisplayorder() == 0) {
			// 更新论坛状态
			ForumStatistics statistics = statisticManager.getStatistic();
			statistics.setTotaltopic(statistics.getTotaltopic() + 1);
			statisticManager.updateStatistics(statistics);

			// 更新论坛板块信息
			Forums forums = forumManager.getForumInfo(topics.getForums().getFid());
			forums.setTopics_1(forums.getTopics_1() + 1);
			forums.setCurtopics(forums.getCurtopics() + 1);
			forumManager.updateForum(forums);

			if (topics.getUsersByPosterid().getUid() != -1) {
				// 更新会员信息
				Mytopics mytopics = new Mytopics();
				mytopics.setTopics(topics);
				mytopics.setUsers(topics.getUsersByPosterid());
				mytopics.setDateline(topics.getPostdatetime());
				mytopicDAO.save(mytopics);
			}
		}
		if (logger.isDebugEnabled()) {
			logger.debug("创建新主题{}成功", topics.getTid());
		}
	}

	/**
	 * 增加父版块的主题数
	 * @param fpidlist 父板块id列表
	 * @param topics 父板块id列表
	 * @param posts 贴子数
	 */
	public void addParentForumTopics(String fpidlist, int topics, int posts) {
		if (fpidlist != "") {
			String[] fids = fpidlist.split(",");
			for (String fid : fids) {
				Forums forum = forumManager.getForumInfo(Utils.null2Int(fid));
				forum.setTopics_1(forum.getTopics_1() + topics);
				forumManager.updateForum(forum);
			}
		}
	}

	/**
	 * 获得主题信息
	 * @param tid 要获得的主题ID
	 * @param fid 版块ID
	 * @param mode 模式选择, 0=当前主题, 1=上一主题, 2=下一主题
	 * @return
	 */
	public Topics getTopicInfo(int tid, int fid, int mode) {
		Topics topic = new Topics();
		try {
			switch (mode) {
			case 1: // 上一主题
				topic = (Topics) topicDAO.find(
						"from Topics where forums.fid=? and tid<? and displayorder>=0 order by tid desc", fid, tid)
						.get(0);
				break;
			case 2: // 下一主题
				topic = (Topics) topicDAO.find(
						"from Topics where forums.fid=? and tid<? and displayorder>=0 order by tid desc", fid, tid)
						.get(0);
				break;
			default:
				break;
			}
		} catch (IndexOutOfBoundsException e) {
			// 抓取数组下标越界异常
			return null;
		}
		return topic;
	}

	/**
	 * 获得主题信息
	 * @param tid 要获得的主题ID
	 * @return
	 */
	public Topics getTopicInfo(int tid) {
		return topicDAO.get(tid);
	}

	/**
	 * 更新主题信息
	 * @param topics
	 */
	public void updateTopicInfo(Topics topics) {
		topicDAO.save(topics);
	}

	/**
	 * 得到置顶主题信息
	 * 
	 * @param fid 版块ID
	 * @return 置顶主题
	 */
	public Element getTopTopicListID(int fid) {
		if (logger.isDebugEnabled()) {
			logger.debug("获取板块 {} 的置顶主题", fid);
		}
		Element element = null;
		String xmlpath = ConfigLoader.getConfig().getWebpath() + "cache/topic/" + fid + ".xml";
		if (Utils.fileExists(xmlpath)) {
			Document document = XmlElementUtil.readXML(xmlpath);
			element = XmlElementUtil.findElement("topic", document.getRootElement());
			if (element != null) {
				String tids = XmlElementUtil.findElement("tid", element).getText();
				if (!tids.trim().equals("")) {
					if (tids.substring(0, 1).equals(",")) {
						element.element("tid").setText(tids.substring(1));
					}
				}
			}
		}
		return element;
	}

	/**
	 * 获得置顶主题信息列表
	 * 
	 * @param fid 版块ID
	 * @param pagesize 每页显示主题数
	 * @param pageindex 当前页数
	 * @param tids 主题id列表
	 * @param autoclose
	 * @param topictypeprefix
	 * @return 主题信息列表
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 * @throws ParseException 
	 */
	public List<ShowforumPageTopicInfo> getTopTopicList(int fid, int pagesize, int pageindex, String tids,
			int autoclose, int topictypeprefix) throws IllegalAccessException, InvocationTargetException,
			ParseException {
		if (logger.isDebugEnabled()) {
			logger.debug("获得置顶主题信息列表：tids - " + tids);
		}
		List<ShowforumPageTopicInfo> col = new ArrayList<ShowforumPageTopicInfo>();
		if (pageindex <= 0) {
			return col;
		}

		Page<Topics> page = new Page<Topics>(pagesize);
		page.setPageNo(pageindex);
		page = topicDAO.find(page, "from Topics where displayorder>0 and charindex(','+rtrim(tid)+',' , '," + tids
				+ ",')>0  order by lastpost desc");

		Map<Integer, Object> topictypeMap = cachesManager.getTopicTypeArray();
		StringBuilder closeid = new StringBuilder();
		Object topicTypeName = null;
		for (Topics topic : page.getResult()) {
			ShowforumPageTopicInfo info = new ShowforumPageTopicInfo();
			BeanUtils.copyProperties(info, topic);
			// 处理关闭标记
			if (info.getClosed() == 0) {
				if (Utils.strDateDiffHours(info.getPostdatetime(), (autoclose * 24)) > 0) {
					info.setClosed(1);
					if (closeid.length() > 0) {
						closeid.append(",");
					}
					closeid.append(info.getTid().toString());
					info.setFolder("closed");
				}
			} else {
				info.setFolder("closed");
				if (info.getClosed() > 1) {
					info.setTid(info.getClosed());
					info.setFolder("move");
				}
			}//end if
			// 高亮标题
			if (info.getHighlight() != "") {
				info.setTitle("<span style=\"" + info.getHighlight() + "\">" + info.getTitle() + "</span>");
			}
			// 扩展属性
			if (topictypeprefix > 0 && info.getTopictypes().getTypeid() > 0) {
				// 设置主题类别
				topicTypeName = topictypeMap.get(info.getTopictypes().getTypeid());
				if (topicTypeName != null && Utils.null2String(topicTypeName) != "") {
					info.setTopictypename(topicTypeName.toString().trim());
				}
			} else {
				info.setTopictypename("");
			}

			col.add(info);
		}//end for
		if (closeid.length() > 0) {
			// 设置主题关闭状态
			topicAdminManager.setClose(closeid.toString(), 1);
		}
		return col;
	}

	/**
	 * 获得一般主题信息列表
	 * @param fid 版块ID
	 * @param pagesize 每页显示主题数
	 * @param pageindex 当前页数
	 * @param startnum
	 * @param newmin 最近多少分钟内的主题认为是新主题
	 * @param hot 多少个帖子认为是热门主题
	 * @param autoclose
	 * @param topictypeprefix
	 * @param condition 条件
	 * @return 主题信息列表
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 * @throws ParseException 
	 */
	public List<ShowforumPageTopicInfo> getTopicList(int fid, int pagesize, int pageindex, int startnum, int newmin,
			int hot, int autoclose, int topictypeprefix, String condition) throws IllegalAccessException,
			InvocationTargetException, ParseException {
		return getTopicList(fid, pagesize, pageindex, startnum, newmin, hot, autoclose, topictypeprefix, condition,
				null, 1);
	}

	/**
	 * 获得一般主题信息列表
	 * @param fid 版块ID
	 * @param pagesize 每页显示主题数
	 * @param pageindex 当前页数
	 * @param startnum
	 * @param newmin 最近多少分钟内的主题认为是新主题
	 * @param hot 多少个帖子认为是热门主题
	 * @param autoclose
	 * @param topictypeprefix
	 * @param condition 条件
	 * @return 主题信息列表
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 * @throws ParseException 
	 */
	public List<ShowforumPageTopicInfo> getTopicList(int fid, int pagesize, int pageindex, int startnum, int newmin,
			int hot, int autoclose, int topictypeprefix, String condition, String orderby, int ascdesc)
			throws IllegalAccessException, InvocationTargetException, ParseException {
		List<ShowforumPageTopicInfo> col = new ArrayList<ShowforumPageTopicInfo>();
		if (pageindex <= 0) {
			return col;
		}

		Page<Topics> page = new Page<Topics>(pagesize);
		page.setPageNo(pageindex);
		String order = " order by ";
		if (orderby != null) {
			order += orderby;
		} else {
			order += "postid.pid";
		}
		if (ascdesc == 0) {
			order += " asc ";
		} else {
			order += " desc ";
		}

		page.setAutoCount(true);
		page = topicDAO.find(page, "from Topics where forums.fid=? and displayorder=0" + condition + order, fid);

		Map<Integer, Object> topictypeMap = cachesManager.getTopicTypeArray();
		StringBuilder closeid = new StringBuilder();
		Object topicTypeName = null;
		for (Topics topic : page.getResult()) {
			ShowforumPageTopicInfo info = new ShowforumPageTopicInfo();
			BeanUtils.copyProperties(info, topic);
			// 处理关闭标记
			if (info.getClosed() == 0) {
				String oldtopic = ForumUtils.getCookie("oldtopic") + "D";
				if (oldtopic.indexOf("D" + info.getTid().toString() + "D") == -1
						&& DateUtils.addMinutes(new Date(), -1 * newmin).before(Utils.parseDate(info.getLastpost()))) {
					info.setFolder("new");
				} else {
					info.setFolder("old");
				}
				System.out.println("当前的回复数：" + info.getReplies() + ",热门数：" + hot);
				if (info.getReplies() >= hot) {
					info.setFolder(info.getFolder() + "hot");
					System.out.println("设置热门：" + info.getFolder());
				}
				if (autoclose > 0) {
					if (Utils.strDateDiffHours(info.getPostdatetime(), (autoclose * 24)) > 0) {
						info.setClosed(1);
						if (closeid.length() > 0) {
							closeid.append(",");
						}
						closeid.append(info.getTid().toString());
						info.setFolder("closed");
					}
				}
			} else {
				info.setFolder("closed");
				if (info.getClosed() > 1) {
					info.setTid(info.getClosed());
					info.setFolder("move");
				}
			}//end if
			// 高亮标题
			if (info.getHighlight() != "") {
				info.setTitle("<span style=\"" + info.getHighlight() + "\">" + info.getTitle() + "</span>");
			}
			// 扩展属性
			if (topictypeprefix > 0 && info.getTopictypes().getTypeid() > 0) {
				// 设置主题类别
				topicTypeName = topictypeMap.get(info.getTopictypes().getTypeid());
				if (topicTypeName != null && Utils.null2String(topicTypeName) != "") {
					info.setTopictypename(topicTypeName.toString().trim());
				}
			} else {
				info.setTopictypename("");
			}
			System.out.println("当前图标：" + info.getFolder());
			col.add(info);
		}//end for
		if (closeid.length() > 0) {
			// 设置主题关闭状态
			topicAdminManager.setClose(closeid.toString(), 1);
		}
		return col;
	}

	/**
	 * 将得到的主题列表中加入主题类型名称字段
	 * 
	 * @param topicList
	 * @return
	 */
	public List<ShowforumPageTopicInfo> getTopicTypeName(List<ShowforumPageTopicInfo> topicList) {
		Map<Integer, Object> map = cachesManager.getTopicTypeArray();
		Object topictypeName = null;
		for (ShowforumPageTopicInfo info : topicList) {
			topictypeName = map.get(info.getTopictypes().getTypeid());
			info.setTopictypename((topictypeName != null && topictypeName.toString().trim() != "") ? "["
					+ topictypeName.toString().trim() + "]" : "");
		}
		return topicList;
	}

	/**
	 * 输出htmltitle
	 * 
	 * @param htmltitle HTML标题
	 * @param topicid 帖子ID
	 */
	public void writeHtmlTitleFile(String htmltitle, int topicid) {
		StringBuilder dir = new StringBuilder();
		dir.append(ConfigLoader.getConfig().getWebpath());
		dir.append("cache/topic/magic/");
		dir.append((topicid / 1000 + 1));
		dir.append("/");

		String filename = dir.toString() + topicid + "_htmltitle.config";
		try {
			FileUtils.writeStringToFile(new File(filename), htmltitle, "UTF-8");
			if (logger.isDebugEnabled()) {
				logger.debug("输出HTML标题{}", filename);
			}
		} catch (IOException e) {
			logger.error("输出HTML标题失败：" + e.getMessage());
		}
	}

	/**
	 * 增加辩论主题扩展信息
	 * 
	 * @param debates
	 */
	public void addDebateTopic(Debates debates) {
		debateDAO.save(debates);
		if (logger.isDebugEnabled()) {
			logger.debug("增加辩论主题扩展信息{}成功", debates.getTid());
		}
	}

	/**
	 * 更新主题浏览量
	 * @param tid 主题id
	 * @param viewcount 浏览量
	 */
	public int updateTopicViewCount(int tid, int viewcount) {
		Topics topics = getTopicInfo(tid);
		topics.setViews(topics.getViews() + viewcount);
		updateTopicInfo(topics);
		return 1;
	}

	/**
	 * 更新主题附件类型
	 * @param pid 帖子
	 * @param attType 附件类型
	 */
	public void updateAttachment(int tid, int attType) {
		Topics topics = getTopicInfo(tid);
		topics.setAttachment(attType);
		updateTopicInfo(topics);
	}

	/**
	 * 将主题设置为隐藏主题
	 * @param tid 主题ID
	 */
	public void updateTopicHide(int tid) {
		Topics topics = getTopicInfo(tid);
		topics.setHide(1);
		updateTopicInfo(topics);
	}

	/**
	 * 列新主题的回复数
	 * @param tid 主题ID
	 */
	public void updateTopicReplies(int tid) {
		int tmp = Utils.null2Int(topicDAO.findUnique("select count(pid) from Posts where topics.tid=? and invisible=0",
				tid), 0);
		topicDAO.createQuery("update Topics set replies=" + tmp + "-1 where displayorder>=0 and tid=?", tid)
				.executeUpdate();
	}

	/**
	 * 删除主题的相关主题记录
	 * @param topicid
	 */
	public void deleteRelatedTopics(int topicid) {
	}

	/**
	 * 更新主题
	 * @param topic 主题信息
	 */
	public void updateTopic(Topics topic) {
		topicDAO.save(topic);
	}

	/**
	 * 获得指定的主题列表 
	 * @param topiclist 主题ID列表
	 * @return 主题列表
	 */
	public List<Topics> getTopicList(String topiclist) {
		return getTopicList(topiclist, -10);
	}

	/**
	 * 获得指定的主题列表 
	 * @param topiclist 主题ID列表
	 * @param displayorder order的下限( where displayorder>此值)
	 * @return 主题列表
	 */
	@SuppressWarnings("unchecked")
	public List<Topics> getTopicList(String topiclist, int displayorder) {
		if (!Utils.isIntArray(topiclist.split(","))) {
			return null;
		}
		return topicDAO.find("from Topics where displayorder>? and tid in(" + topiclist + ")", displayorder);
	}

	/**
	 * 判断帖子列表是否都在当前板块
	 * @param topiclist 主题序列
	 * @param forumid 主题序列
	 * @return
	 */
	public boolean inSameForum(String topiclist, int forumid) {
		if (!Utils.isIntArray(topiclist.split(","))) {
			return false;
		}
		return topiclist.split(",").length == Utils.null2Int(topicDAO.findUnique(
				"select count(tid) from Topics where forums.fid=? and tid in(" + topiclist + ")", forumid));
	}

	/**
	 * 获取指定用户的主题总数
	 * @param userid
	 * @return
	 */
	public int getTopicsCountbyUserId(int userid) {
		return Utils.null2Int(topicDAO.findUnique("select count(tid) from Mytopics where users.uid=?", userid), 0);
	}

	/**
	 * 按照用户Id获取主题列表
	 * @param userid
	 * @param pageid
	 * @param pagesize
	 * @param newmin
	 * @param hottopic
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws ParseException
	 */
	public List<ShowforumPageTopicInfo> getTopicsByUserId(int userid, int pageid, int pagesize, int newmin, int hottopic)
			throws IllegalAccessException, InvocationTargetException, ParseException {
		List<ShowforumPageTopicInfo> topicList = new ArrayList<ShowforumPageTopicInfo>();
		Page<Mytopics> page = new Page<Mytopics>(pagesize);
		page.setPageNo(pageid);
		try {
			page = mytopicDAO.find(page, "from Mytopics where users.uid=? order by topics.tid desc", userid);
		} catch (ObjectNotFoundException e) {
			return topicList;
		}
		for (Mytopics mytopics : page.getResult()) {
			mytopics.getTopics().getForums().getName();
			ShowforumPageTopicInfo topic = new ShowforumPageTopicInfo();
			BeanUtils.copyProperties(topic, mytopics.getTopics());
			if (topic.getClosed() == 0) {
				String oldtopic = ForumUtils.getCookie("oldtopic") + "D";
				if (oldtopic.indexOf("D" + topic.getTid() + "D") == -1
						&& DateUtils.addMinutes(new Date(), -1 * newmin).before(
								Utils.parseDate(topic.getLastpost().trim()))) {
					topic.setFolder("new");
				} else {
					topic.setFolder("old");
				}

				if (topic.getReplies() >= hottopic) {
					topic.setFolder(topic.getFolder() + "hot");
				}
			} else {
				topic.setFolder("closed");
				if (topic.getClosed() > 1) {
					topic.setTid(topic.getClosed());
					topic.setFolder("move");
				}
			}
			if (!topic.getHighlight().equals("")) {
				topic.setTitle("<span style=\"" + topic.getHighlight() + "\">" + topic.getTitle() + "</span>");
			}
			topicList.add(topic);
		}
		return topicList;
	}

	/**
	 * 按照用户Id获取其回复过的主题总数
	 * @param userid
	 * @return
	 */
	public int getTopicsCountbyReplyUserId(int userid) {
		return Utils.null2Int(topicDAO.findUnique("select count(distinct topics.tid) from Myposts where users.uid=?",
				userid), 0);
	}

	/**
	 * 按照用户Id获取其回复过的主题列表
	 * @param userid
	 * @param pageid
	 * @param pagesize
	 * @param newmin
	 * @param hottopic
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws ParseException
	 */
	public List<ShowforumPageTopicInfo> getTopicsByReplyUserId(int userid, int pageid, int pagesize, int newmin,
			int hottopic) throws IllegalAccessException, InvocationTargetException, ParseException {
		List<ShowforumPageTopicInfo> topicList = new ArrayList<ShowforumPageTopicInfo>();
		Page<Myposts> page = new Page<Myposts>(pagesize);
		page.setPageNo(pageid);
		try {
			page = mypostDAO.find(page, "from Myposts where users.uid=? order by topics.tid desc", userid);
		} catch (ObjectNotFoundException e) {
			return topicList;
		}
		for (Myposts myposts : page.getResult()) {
			myposts.getTopics().getForums().getName();
			ShowforumPageTopicInfo topic = new ShowforumPageTopicInfo();
			BeanUtils.copyProperties(topic, myposts.getTopics());
			if (topic.getClosed() == 0) {
				String oldtopic = ForumUtils.getCookie("oldtopic") + "D";
				if (oldtopic.indexOf("D" + topic.getTid() + "D") == -1
						&& DateUtils.addMinutes(new Date(), -1 * newmin).before(
								Utils.parseDate(topic.getLastpost().trim()))) {
					topic.setFolder("new");
				} else {
					topic.setFolder("old");
				}

				if (topic.getReplies() >= hottopic) {
					topic.setFolder(topic.getFolder() + "hot");
				}
			} else {
				topic.setFolder("closed");
				if (topic.getClosed() > 1) {
					topic.setTid(topic.getClosed());
					topic.setFolder("move");
				}
			}
			if (!topic.getHighlight().equals("")) {
				topic.setTitle("<span style=\"" + topic.getHighlight() + "\">" + topic.getTitle() + "</span>");
			}
			topicList.add(topic);
		}
		return topicList;
	}

	/**
	 * 对符合新贴,精华贴的页面显示进行查询的函数
	 * @param pagesize 每个分页的主题数
	 * @param pageindex 分页页数
	 * @param startnum 置顶帖数量
	 * @param newmin 最近多少分钟内的主题认为是新主题
	 * @param hot 多少个帖子认为是热门主题
	 * @param autoclose 条件
	 * @param topictypeprefix
	 * @param condition
	 * @param ascdesc
	 * @return 主题列表
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 * @throws ParseException 
	 */
	public List<ShowforumPageTopicInfo> getTopicListByType(int pagesize, int pageindex, int startnum, int newmin,
			int hot, int autoclose, int topictypeprefix, String condition, int ascdesc) throws IllegalAccessException,
			InvocationTargetException, ParseException {
		List<ShowforumPageTopicInfo> coll = new ArrayList<ShowforumPageTopicInfo>();

		if (pageindex <= 0) {
			return coll;
		}
		String sortstr = "asc";
		if (ascdesc != 0) {
			sortstr = "desc";
		}
		Page<Topics> page = new Page<Topics>(pagesize);
		page.setPageNo(pageindex);
		page = topicDAO.find(page, "from Topics where displayorder>=0 " + condition + " order by tid " + sortstr
				+ " ,postid.pid " + sortstr);
		if (page != null) {

			Map<Integer, Object> topicTypeArray = cachesManager.getTopicTypeArray();
			StringBuilder closeid = new StringBuilder();
			Object topicTypeName = null;

			for (Topics topic : page.getResult()) {
				ShowforumPageTopicInfo info = new ShowforumPageTopicInfo();
				BeanUtils.copyProperties(info, topic);
				//处理关闭标记
				if (info.getClosed() == 0) {
					String oldtopic = ForumUtils.getCookie("oldtopic") + "D";
					if (oldtopic.indexOf("D" + info.getTid() + "D") == -1
							&& DateUtils.addMinutes(new Date(), -1 * newmin)
									.before(Utils.parseDate(info.getLastpost()))) {
						info.setFolder("new");
					} else {
						info.setFolder("old");
					}

					if (info.getReplies() >= hot) {
						info.setFolder(info.getFolder() + "hot");
					}

					if (autoclose > 0) {
						if (Utils.strDateDiffHours(info.getPostdatetime(), autoclose * 24) > 0) {
							info.setClosed(1);
							if (closeid.length() > 0) {
								closeid.append(",");
							}
							closeid.append(info.getTid());
							info.setFolder("closed");
						}
					}
				} else {
					info.setFolder("closed");
					if (info.getClosed() > 1) {
						info.setTid(info.getClosed());
						info.setFolder("move");
					}
				}

				if (!info.getHighlight().equals("")) {
					info.setTitle("<span style=\"" + info.getHighlight() + "\">" + info.getTitle() + "</span>");
				}

				//扩展属性
				if (topictypeprefix > 0 && info.getTopictypes().getTypeid() > 0) {
					topicTypeName = topicTypeArray.get(info.getTopictypes().getTypeid());
					if (!Utils.null2String(topicTypeName).equals("")) {
						info.setTopictypename(topicTypeName.toString().trim());
					}
				} else {
					info.setTopictypename("");
				}
				//

				coll.add(info);
			}

			if (closeid.length() > 0) {
				topicAdminManager.setClose(closeid + "", 1);
			}
		}
		return coll;
	}

	public List<ShowforumPageTopicInfo> getTopicListByTypeDate(int pagesize, int pageindex, int startnum, int newmin,
			int hot, int autoclose, int topictypeprefix, String condition, String orderby, int ascdesc)
			throws IllegalAccessException, InvocationTargetException, ParseException {
		List<ShowforumPageTopicInfo> coll = new ArrayList<ShowforumPageTopicInfo>();

		if (pageindex <= 0) {
			return coll;
		}

		String sortstr = "asc";
		if (ascdesc != 0) {
			sortstr = "desc";
		}
		Page<Topics> page = new Page<Topics>(pagesize);
		page.setPageNo(pageindex);
		page = topicDAO.find(page, "from Topics where displayorder>=0 " + condition + " order by " + orderby + " "
				+ sortstr);
		if (page != null) {

			Map<Integer, Object> topicTypeArray = cachesManager.getTopicTypeArray();
			StringBuilder closeid = new StringBuilder();
			Object topicTypeName = null;

			for (Topics topic : page.getResult()) {
				ShowforumPageTopicInfo info = new ShowforumPageTopicInfo();
				BeanUtils.copyProperties(info, topic);
				//处理关闭标记
				if (info.getClosed() == 0) {
					String oldtopic = ForumUtils.getCookie("oldtopic") + "D";
					if (oldtopic.indexOf("D" + info.getTid() + "D") == -1
							&& DateUtils.addMinutes(new Date(), -1 * newmin)
									.before(Utils.parseDate(info.getLastpost()))) {
						info.setFolder("new");
					} else {
						info.setFolder("old");
					}

					if (info.getReplies() >= hot) {
						info.setFolder(info.getFolder() + "hot");
					}

					if (autoclose > 0) {
						if (Utils.strDateDiffHours(info.getPostdatetime(), autoclose * 24) > 0) {
							info.setClosed(1);
							if (closeid.length() > 0) {
								closeid.append(",");
							}
							closeid.append(info.getTid());
							info.setFolder("closed");
						}
					}
				} else {
					info.setFolder("closed");
					if (info.getClosed() > 1) {
						info.setTid(info.getClosed());
						info.setFolder("move");
					}
				}

				if (!info.getHighlight().equals("")) {
					info.setTitle("<span style=\"" + info.getHighlight() + "\">" + info.getTitle() + "</span>");
				}

				//扩展属性
				if (topictypeprefix > 0 && info.getTopictypes().getTypeid() > 0) {
					topicTypeName = topicTypeArray.get(info.getTopictypes().getTypeid());
					if (!Utils.null2String(topicTypeName).equals("")) {
						info.setTopictypename(topicTypeName.toString().trim());
					}
				} else {
					info.setTopictypename("");
				}
				//

				coll.add(info);
			}

			if (closeid.length() > 0) {
				topicAdminManager.setClose(closeid + "", 1);
			}
		}
		return coll;
	}

	/**
	 * 获得相应的magic值
	 * @param magic 数据库中magic值
	 * @param magicType magic类型
	 * @return
	 */
	public int getMagicValue(int magic, int magicType) {
		if (magic == 0)
			return 0;
		String m = magic + "";
		if (magicType == 1) {
			if (m.length() >= 2) {
				return Utils.null2Int(m.substring(1, 1));
			}
		} else if (magicType == 2) {
			if (m.length() >= 5) {
				return Utils.null2Int(m.substring(2, 3));
			}
		} else if (magicType == 3) {
			if (m.length() >= 6) {
				return Utils.null2Int(m.substring(5, 6));
			}
		}
		return 0;

	}

	/**
	 * 更新主题为已被管理
	 * @param topiclist 主题id列表
	 * @param operationid 管理操作id
	 * @return  成功返回1，否则返回0
	 */
	public int updateTopicModerated(String topiclist, int moderated) {
		if (!Utils.isIntArray(topiclist.split(","))) {
			return 0;
		}

		return topicDAO.createQuery("update Topics set moderated=? where tid in(" + topiclist + ")", moderated)
				.executeUpdate();
	}

	/**
	 * 读取指定帖子的html标题
	 * @param topicid
	 * @return
	 * @throws IOException
	 */
	public String getHtmlTitle(int topicid) throws IOException {
		StringBuilder dir = new StringBuilder();
		dir.append(ConfigLoader.getConfig().getWebpath());
		dir.append("cache/topic/magic/");
		dir.append((topicid / 1000 + 1));
		dir.append("/");
		String filename = dir.toString() + topicid + "_htmltitle.config";
		if (!Utils.fileExists(filename))
			return "";

		return FileUtils.readFileToString(new File(filename), "UTF-8");
	}
}
