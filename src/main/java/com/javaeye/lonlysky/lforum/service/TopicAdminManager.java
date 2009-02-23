package com.javaeye.lonlysky.lforum.service;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springside.modules.orm.hibernate.SimpleHibernateTemplate;

import com.javaeye.lonlysky.lforum.comm.LForumRequest;
import com.javaeye.lonlysky.lforum.comm.utils.UBBUtils;
import com.javaeye.lonlysky.lforum.comm.utils.Utils;
import com.javaeye.lonlysky.lforum.comm.utils.XmlElementUtil;
import com.javaeye.lonlysky.lforum.config.impl.ConfigLoader;
import com.javaeye.lonlysky.lforum.entity.forum.Forums;
import com.javaeye.lonlysky.lforum.entity.forum.Postid;
import com.javaeye.lonlysky.lforum.entity.forum.Posts;
import com.javaeye.lonlysky.lforum.entity.forum.Ratelog;
import com.javaeye.lonlysky.lforum.entity.forum.Topics;
import com.javaeye.lonlysky.lforum.entity.global.Config;
import com.javaeye.lonlysky.lforum.service.admin.AdminModeratorLogMag;
import com.javaeye.lonlysky.lforum.service.admin.AdminRateLogManager;
import com.javaeye.lonlysky.lforum.service.admin.AdminStatsManager;

/**
 * 主题主题管理类
 * 
 * @author 黄磊
 *
 */
@Service
@Transactional
public class TopicAdminManager {

	private static final Logger logger = LoggerFactory.getLogger(TopicAdminManager.class);
	private SimpleHibernateTemplate<Topics, Integer> topicDAO;
	private SimpleHibernateTemplate<Posts, Integer> postDAO;
	private SimpleHibernateTemplate<Postid, Integer> postidDAO;

	@Autowired
	private UserManager userManager;

	@Autowired
	private TopicManager topicManager;

	@Autowired
	private UserCreditManager userCreditManager;

	@Autowired
	private PostManager postManager;

	@Autowired
	private AttachmentManager attachmentManager;

	@Autowired
	private AdminStatsManager adminStatsManager;

	@Autowired
	private ForumManager forumManager;

	@Autowired
	private AdminRateLogManager adminRateLogManager;

	@Autowired
	private ScoresetManager scoresetManager;

	@Autowired
	private AdminModeratorLogMag adminModeratorLogMag;

	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		postDAO = new SimpleHibernateTemplate<Posts, Integer>(sessionFactory, Posts.class);
		topicDAO = new SimpleHibernateTemplate<Topics, Integer>(sessionFactory, Topics.class);
		postidDAO = new SimpleHibernateTemplate<Postid, Integer>(sessionFactory, Postid.class);
	}

	/**
	 * 设置主题指定字段的属性值
	 * @param topiclist 要设置的主题列表
	 * @param field 要设置的主题列表
	 * @param intValue 属性值
	 * @return 属性值
	 */
	public int setTopicStatus(String topiclist, String field, int intValue) {
		if (logger.isDebugEnabled()) {
			logger.debug("设置主题指定字段的属性值,topiclist:{},field:{},intValue:" + intValue, topiclist, field);
		}
		if (!Utils.inArray(field.toLowerCase().trim(), "displayorder,highlight,digest")) {
			return -1;
		}

		if (!Utils.isIntArray(topiclist.split(","))) {
			return -1;
		}

		return topicDAO.createQuery("update Topics set " + field + "=? where tid in(" + topiclist + ")", intValue)
				.executeUpdate();
	}

	/**
	 * 设置主题指定字段的属性值
	 * @param topiclist 要设置的主题列表
	 * @param field 要设置的主题列表
	 * @param value 属性值
	 * @return 属性值
	 */
	public int setTopicStatus(String topiclist, String field, String value) {
		if (logger.isDebugEnabled()) {
			logger.debug("设置主题指定字段的属性值,topiclist:{},field:{},value:" + value, topiclist, field);
		}
		if ((",displayorder,highlight,digest,").indexOf("," + field.toLowerCase().trim() + ",") < 0) {
			return -1;
		}

		if (!Utils.isIntArray(topiclist.split(","))) {
			return -1;
		}

		return topicDAO.createQuery("update Topics set " + field + "=? where tid in(" + topiclist + ")", value)
				.executeUpdate();
	}

	/**
	 * 将主题置顶/解除置顶
	 * @param fid 要设置的主题列表
	 * @param topiclist 
	 * @param intValue 置顶级别( 0 为解除置顶)
	 * @return 置顶级别( 0 为解除置顶)
	 * @throws IOException 
	 */
	public int setTopTopicList(int fid, String topiclist, int intValue) throws IOException {
		if (logger.isDebugEnabled()) {
			logger.debug("将主题置顶/解除置顶,fid:{},topiclist:{},intValue:" + intValue, fid, topiclist);
		}
		if (setTopicStatus(topiclist, "displayorder", intValue) > 0) {
			if (resetTopTopicList(fid) == 1) {
				return 1;
			}
		}
		if (Utils.fileExists(ConfigLoader.getConfig().getWebpath() + "cache/topic/" + fid + ".xml")) {
			FileUtils.forceDelete(new File(ConfigLoader.getConfig().getWebpath() + "cache/topic/" + fid + ".xml"));
		}
		return -1;
	}

	public String resetTopTopicListHQL(int layer, String fid, String parentidlist) {

		String filterexpress = "";

		switch (layer) {
		case 0:
			filterexpress = "fid<>" + fid + " and (',' + trim(parentidlist) + ',' like '%," + fid + ",%')";
			break;
		case 1:
			filterexpress = parentidlist.trim();
			if (!filterexpress.equals("")) {
				filterexpress = "fid<>" + fid + " and (fid=" + filterexpress
						+ " or (',' + trim(parentidlist) + ',' like '%," + filterexpress + ",%'))";
			} else {
				filterexpress = "fid<>" + fid + " and (',' + trim(parentidlist) + ',' like '%," + filterexpress
						+ ",%')";
			}
			break;
		default:
			filterexpress = parentidlist.trim();
			if (!filterexpress.equals("")) {
				filterexpress = filterexpress.substring(0, filterexpress.indexOf(","));
				filterexpress = "fid<>" + fid + " and (fid=" + filterexpress
						+ " or (',' + trim(parentidlist) + ',' like '%," + filterexpress + ",%'))";
			} else {
				filterexpress = "fid<>" + fid + " and (',' + trim(parentidlist) + ',' like '%," + filterexpress
						+ ",%')";
			}
			break;
		}

		return filterexpress;
	}

	/**
	 * 重新生成置顶主题
	 * @param fid 主题ID
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public int resetTopTopicList(int fid) {
		List<Object[]> objList = topicDAO
				.find("select tid,displayorder,forums.fid from Topics where displayorder>0 order by fid");
		if (objList != null) {
			List<Object[]> topList = topicDAO
					.find("select fid,forums.fid,parentidlist,layer from Forums order by fid desc");
			int[] fidIndex = null;
			if (topList != null) {

				fidIndex = new int[Utils.null2Int(topList.get(0)[0], 0) + 1];
				int index = 0;
				for (Object[] objects : topList) {
					Object[] obj2 = new Object[10];
					System.arraycopy(objects, 0, obj2, 0, objects.length);
					objects = obj2;
					fidIndex[Utils.null2Int(objects[0], 0)] = index;
					topList.set(index, objects);
					index++;
				}
			}

			int tidCount = 0;
			int tid0Count = 0;
			int tid1Count = 0;
			int tid2Count = 0;
			int tid3Count = 0;

			StringBuilder sbTop3 = new StringBuilder();
			for (Object[] objects : objList) {
				if (Utils.null2Int(objects[1], 0) == 3) {
					if (sbTop3.length() > 0) {
						sbTop3.append(",");
					}

					if (fidIndex != null && fidIndex.length >= Utils.null2Int(objects[2], 0)) {
						sbTop3.append(objects[0]);
						tidCount++;
						//tid3count
						topList.get(fidIndex[Utils.null2Int(objects[2], 0)])[9] = Utils.null2Int(topList
								.get(fidIndex[Utils.null2Int(objects[2], 0)])[9], 0) + 1;
					}

				} else {
					if (fidIndex != null && fidIndex.length >= Utils.null2Int(objects[2], 0)) {
						if (Utils.null2Int(objects[1], 0) != 2) {
							//tidlist
							topList.get(fidIndex[Utils.null2Int(objects[2], 0)])[6] = Utils.null2String(topList
									.get(fidIndex[Utils.null2Int(objects[2], 0)])[6])
									+ "," + objects[0];
							//tidcount
							topList.get(fidIndex[Utils.null2Int(objects[2], 0)])[7] = Utils.null2Int(topList
									.get(fidIndex[Utils.null2Int(objects[2], 0)])[7], 0) + 1;
						} else {
							//tid2list
							topList.get(fidIndex[Utils.null2Int(objects[2], 0)])[5] = Utils.null2String(topList
									.get(fidIndex[Utils.null2Int(objects[2], 0)])[5])
									+ "," + objects[0];
							//tid2count
							topList.get(fidIndex[Utils.null2Int(objects[2], 0)])[8] = Utils.null2Int(topList
									.get(fidIndex[Utils.null2Int(objects[2], 0)])[8], 0) + 1;
						}
					}
				}
			}

			if (topList != null) {
				for (Object[] objects : topList) {
					//temptidlist
					objects[4] = sbTop3.toString() + Utils.null2String(objects[6]) + Utils.null2String(objects[5]);

					tid1Count = Utils.null2Int(objects[7], 0);
					tid2Count = Utils.null2Int(objects[8], 0);
					tid3Count = Utils.null2Int(objects[9], 0);

					tid0Count = tid1Count + tid2Count + tid3Count;

					objects[7] = tid1Count + tidCount + Utils.null2Int(objects[8], 0);

					String filterexpress = resetTopTopicListHQL(Utils.null2Int(objects[3], 0), objects[0].toString()
							.trim(), objects[2].toString().trim());
					List<Object[]> tmpList = topicDAO
							.find("select fid,forums.fid,parentidlist,layer from Forums where " + filterexpress
									+ " order by fid desc");
					for (int i = 0; i < tmpList.size(); i++) {
						for (Object[] obj : topList) {
							if (obj[0].equals(tmpList.get(0)[0])) {
								if (!Utils.null2String(obj[5]).equals("")) {
									objects[4] = Utils.null2String(objects[4]) + Utils.null2String(obj[5]);
									objects[7] = Utils.null2Int(obj[8], 0) + Utils.null2Int(objects[7], 0);
									tid2Count = tid2Count + Utils.null2Int(obj[8], 0);
								}
							}
						}

					}

					tid0Count = Utils.null2Int(objects[7], 0) - tid0Count;
					Document document = DocumentHelper.createDocument();
					Element root = document.addElement("topics");
					Element topic = root.addElement("topic");
					topic.addElement("tid").setText(Utils.null2String(objects[4]));
					topic.addElement("tidCount").setText(Utils.null2String(objects[7]));
					topic.addElement("tid0Count").setText(Utils.null2String(tid0Count));
					topic.addElement("tid1Count").setText(Utils.null2String(tid1Count));
					topic.addElement("tid2Count").setText(Utils.null2String(tid2Count));
					topic.addElement("tid3Count").setText(Utils.null2String(tid3Count));

					XmlElementUtil.saveXML(
							ConfigLoader.getConfig().getWebpath() + "cache/topic/" + objects[0] + ".xml", document);
				}
				return 1;
			}
		}
		return 0;
	}

	/**
	 * 将主题高亮显示
	 * @param topiclist 要设置的主题列表
	 * @param intValue 高亮样式及颜色( 0 为解除高亮显示)
	 * @return 更新主题个数
	 */
	public int setHighlight(String topiclist, String intValue) {
		return setTopicStatus(topiclist, "highlight", intValue);
	}

	/**
	 * 根据得到给定主题的用户列表
	 * @param topiclist 主题列表
	 * @param op 操作源(0:精华,1:删除)
	 * @return 用户列表
	 */
	@SuppressWarnings("unchecked")
	private String getUserListWithTopiclist(String topiclist, int op) {
		if (!Utils.isIntArray(topiclist.split(","))) {
			return "";
		}
		StringBuilder useridlist = new StringBuilder();

		Config configinfo = ConfigLoader.getConfig();
		List<Integer> posteridList = null;
		if (op == 1) {
			if (configinfo.getLosslessdel() != 0) {
				posteridList = topicDAO.find(
						"select usersByPosterid.uid from Topics where day(?)-day(postdatetime)<? and tid in("
								+ topiclist + ")", Utils.getNowTime(), configinfo.getLosslessdel());
			} else {
				posteridList = topicDAO.find("select usersByPosterid.uid from Topics where tid in(" + topiclist + ")");
			}
		} else {
			posteridList = topicDAO.find("select usersByPosterid.uid from Topics where tid in(" + topiclist + ")");
		}

		if (posteridList != null) {
			for (Integer integer : posteridList) {
				if (!useridlist.toString().equals("")) {
					useridlist.append(",");
				}
				useridlist.append(integer);
			}
		}
		return useridlist.toString();
	}

	/**
	 * 将主题设置精华/解除精华
	 * @param topiclist 要设置的主题列表
	 * @param intValue 精华级别( 0 为解除精华)
	 * @return 更新主题个数
	 */
	public int setDigest(String topiclist, int intValue) {
		int mount = setTopicStatus(topiclist, "digest", intValue);
		String useridlist = getUserListWithTopiclist(topiclist, 0);
		if (mount > 0) {
			userManager.updateUserDigest(useridlist);
			if (intValue > 0 && !useridlist.equals("")) {
				userCreditManager.updateUserCreditsByDigest(useridlist, mount);
			}
		}
		return mount;
	}

	/**
	 * 将主题设置关闭/打开
	 * @param topiclist 要设置的主题列表
	 * @param intValue 关闭/打开标志( 0 为打开,1 为关闭)
	 * @return 更新主题个数
	 */
	public int setClose(String topiclist, int intValue) {
		if (!Utils.isIntArray(topiclist.split(","))) {
			return -1;
		}

		return topicDAO.createQuery("update Topics set closed=? where tid in(" + topiclist + ") and closed in(0,1)",
				intValue).executeUpdate();
	}

	/**
	 * 获得主题指定字段的属性值
	 * @param topiclist 主题列表
	 * @param field 要获得值的字段
	 * @return 主题指定字段的状态
	 */
	public int getTopicStatus(String topiclist, String field) {
		if (logger.isDebugEnabled()) {
			logger.debug("获得主题指定字段的属性值,topiclist:{},field:{}", topiclist, field);
		}
		if ((",displayorder,digest,").indexOf("," + field.toLowerCase().trim() + ",") < 0) {
			return -1;
		}

		if (!Utils.isIntArray(topiclist.split(","))) {
			return -1;
		}

		return Utils.null2Int(topicDAO.findUnique("select sum(" + field + ") from Topics where tid in(" + topiclist
				+ ")"), 0);
	}

	/**
	 * 获得主题置顶状态
	 * @param topiclist 主题列表
	 * @return 置顶状态(单个主题返回真实状态,多个主题返回状态值累计)
	 */
	public int getDisplayorder(String topiclist) {
		return getTopicStatus(topiclist, "displayorder");
	}

	/**
	 * 获得高亮样式及颜色
	 * @param topiclist
	 * @return
	 */
	public int getHighlight(String topiclist) {
		return 0;
	}

	/**
	 * 获得主题精华状态
	 * @param topiclist 主题列表
	 * @return 精华状态(单个主题返回真实状态,多个主题返回状态值累计)
	 */
	public int getDigest(String topiclist) {
		return getTopicStatus(topiclist, "digest");
	}

	/**
	 * 在数据库中删除指定主题
	 * @param topiclist 主题列表
	 * @param subtractCredits 是否减少用户积分(0不减少,1减少)
	 * @param reserveAttach 删除个数
	 * @return
	 * @throws IOException 
	 * @throws ParseException 
	 */
	public int deleteTopics(String topiclist, int subtractCredits, boolean reserveAttach) throws IOException,
			ParseException {
		if (!Utils.isIntArray(topiclist.split(","))) {
			return -1;
		}

		Config configinfo = ConfigLoader.getConfig();

		List<Topics> topicList = topicManager.getTopicList(topiclist);
		if (topicList == null) {
			return -1;
		}
		for (Topics topic : topicList) {
			if (topic.getDigest() > 0) {
				userCreditManager.updateUserCreditsByDigest(topic.getUsersByPosterid().getUid(), -1);
			}

		}

		List<Posts> postList = postManager.getPostList(topiclist);
		if (postList != null) {
			int i = 0;
			int[] tuidlist = new int[postList.size()];
			int[] auidlist = new int[postList.size()];
			//int fid = -1;
			for (Posts post : postList) {
				if (Utils.strDateDiffHours(post.getPostdatetime(), configinfo.getLosslessdel() * 24) < 0) {
					if (post.getLayer() == 0) {
						tuidlist[i] = post.getUsers().getUid();
					} else {
						auidlist[i] = attachmentManager.getAttachmentCountByPid(post.getPid());
					}
				} else {
					tuidlist[i] = 0;
					auidlist[i] = 0;
				}
			}
			userCreditManager.updateUserCreditsByDeleteTopic(tuidlist, auidlist, -1);
		}

		int reval = 0;

		reval = deleteTopicByTidList(topiclist, true);
		if (reval > 0 && !reserveAttach) {
			attachmentManager.deleteAttachmentByTid(topiclist);
		}
		return reval;

	}

	/**
	 * 删除指定主题
	 * @param topiclist 要删除的主题ID列表
	 * @param chanageposts 删除帖时是否要减版块帖数
	 * @return
	 * @throws ParseException 
	 */
	@SuppressWarnings("unchecked")
	public int deleteTopicByTidList(String topiclist, boolean chanageposts) throws ParseException {
		if (topiclist.trim().equals("")) {
			return 0;
		}
		String fid = "";
		String tempfidlist = "";
		int postcount = 0; // 帖子计数
		int topiccount = 0; // 主题计数
		int todaycount = 0; // 今日计数
		List<Object[]> objList = postDAO
				.find("select forums.fid,users.uid,layer,postdatetime from Posts where topics.tid in(" + topiclist
						+ ")");
		if (objList != null) {
			for (Object[] objects : objList) {
				postcount += 1;
				if (Utils.null2Int(objects[2]) == 0) {
					topiccount += 1;
				}
				if (Utils.howLong("d", Utils.null2String(objects[3]), Utils.getNowTime()) == 0) {
					todaycount += 1;
				}
				if ((fid + ",").indexOf("," + objects[0] + ",") < 0) {
					tempfidlist = Utils.null2String(postDAO.findUnique("select parentidlist from Forums where fid=?",
							objects[0]));
					if (!tempfidlist.equals("")) {
						fid = fid.trim() + "," + tempfidlist.trim() + "," + objects[0];
					} else {
						fid = fid.trim() + "," + objects[0];
					}
				}//end if
				if (chanageposts) {
					// 更新用户帖子数量
					postDAO.createQuery("update Users set posts=posts-1 where uid=?", objects[1]).executeUpdate();
				}
			}
		}
		System.out.println(fid);
		if (fid.trim().length() > 0) {
			fid = fid.substring(1);
			System.out.println(fid);
			if (chanageposts) {
				// 更新论坛信息
				postDAO.createQuery("update ForumStatistics set totaltopic=totaltopic-?,totalpost=totalpost-?",
						topiccount, postcount).executeUpdate();

				// 更新板块信息
				postDAO.createQuery(
						"update Forums set posts=posts-?,topics_1=topics_1-?,todayposts=todayposts-? where fid in("
								+ fid + ")", postcount, topiccount, todaycount).executeUpdate();
			}
			// 删除收藏夹
			postDAO.createQuery("delete from Favorites where topics.tid in (" + topiclist + ") and typeid=0")
					.executeUpdate();

			// 删除投票
			postDAO.createQuery("delete from Polls where topics.tid in (" + topiclist + ")").executeUpdate();

			// 删除帖子
			postDAO.createQuery("delete from Posts where topics.tid in (" + topiclist + ")").executeUpdate();

			// 删除我的帖子
			postDAO.createQuery("delete from Myposts where topics.tid in (" + topiclist + ")").executeUpdate();
		}
		// 删除主题
		postDAO.createQuery("delete from Topics where closed in(" + topiclist + ") or tid in (" + topiclist + ")")
				.executeUpdate();

		//		// 更新标签
		//		postDAO.createQuery(
		//				"update Tags set count=count-1,fcount=fcount-1 where tagid in(select tagid from Topictags where topics.tid in("
		//						+ topiclist + "))").executeUpdate();
		//
		//		// 删除标签
		//		postDAO.createQuery("delete from Topictags where topics.tid in(" + topiclist + ")").executeUpdate();
		//
		//		// 删除标签缓存
		//		postDAO.createQuery(
		//				"delete from Topictagcaches where tid in(" + topiclist + ") or linktid in(" + topiclist + ")")
		//				.executeUpdate();
		return 1;
	}

	/**
	 * 不更新积分删除主题
	 * @param topiclist
	 * @param reserveAttach
	 * @return
	 * @throws ParseException
	 * @throws IOException
	 */
	public int deleteTopicsWithoutChangingCredits(String topiclist, boolean reserveAttach) throws ParseException,
			IOException {
		if (!Utils.isIntArray(topiclist.split(","))) {
			return -1;
		}
		int reval = -1;
		reval = deleteTopicByTidList(topiclist, false);
		if (reval > 0 && !reserveAttach) {
			attachmentManager.deleteAttachmentByTid(topiclist);
		}
		return reval;
	}

	/**
	 * 在数据库中删除指定主题
	 * @param topiclist 主题列表
	 * @param reserveAttach
	 * @return 删除个数
	 * @throws IOException
	 * @throws ParseException
	 */
	public int deleteTopics(String topiclist, boolean reserveAttach) throws IOException, ParseException {
		return deleteTopics(topiclist, 1, reserveAttach);
	}

	/**
	 * 在删除指定的主题
	 * @param topiclist 主题列表
	 * @param toDustbin 指定主题删除形式(0：直接从数据库中删除,并删除与之关联的信息  1：只将其从论坛列表中删除(将displayorder字段置为-1)将其放入回收站中
	 * @param reserveAttach 删除个数
	 * @return
	 * @throws ParseException 
	 * @throws IOException 
	 */
	public int deleteTopics(String topiclist, byte toDustbin, boolean reserveAttach) throws IOException, ParseException {
		return toDustbin == 0 ? deleteTopics(topiclist, reserveAttach) : setTopicStatus(topiclist, "displayorder", -1);
	}

	/**
	 * 恢复回收站中的主题。
	 * @param topiclist 主题列表
	 * @return 恢复个数
	 */
	public int restoreTopics(String topiclist) {
		return setTopicStatus(topiclist, "displayorder", 0);
	}

	public int moveTopics(String topiclist, int fid, int oldfid, boolean savelink) {
		if (topiclist.trim().equals("")) {
			return -1;
		}
		String[] tidlist = topiclist.split(",");
		int intDelTidCount = 0;
		for (String tid : tidlist) {
			if (!Utils.isInt(tid)) {
				return -1;
			}
		}
		intDelTidCount += deleteClosedTopics(fid, topiclist);

		//转移帖子
		//
		moveTopics(topiclist, fid, oldfid);

		//如果保存链接则复制一条记录到原版块
		if (savelink) {
			if (copyTopicLink(oldfid, topiclist) <= 0) {
				return -2;
			}

			adminStatsManager.reSetFourmTopicAPost(oldfid);
			forumManager.setRealCurrentTopics(oldfid);
		}
		return 1;
	}

	public int copyTopicLink(int oldfid, String topiclist) {
		// TODO Auto-generated method stub
		return 0;
	}

	/**
	 * 移动主题到指定版块
	 * @param topiclist 要移动的主题列表
	 * @param fid 转到的版块ID
	 * @param oldfid 
	 * @return 更新记录数
	 */
	public int moveTopics(String topiclist, int fid, int oldfid) {
		if (!Utils.isIntArray(topiclist.split(","))) {
			return -1;
		}
		//更新帖子
		postDAO.createQuery("upodate Posts set forums.fid=? where topics.tid in (" + topiclist + ")", fid)
				.executeUpdate();

		//更新主题
		int reval = topicDAO.createQuery("update Topics set forums.fid=?, typeid=0 where tid in(" + topiclist + ")",
				fid).executeUpdate();
		if (reval > 0) {
			adminStatsManager.reSetFourmTopicAPost(fid);
			adminStatsManager.reSetFourmTopicAPost(oldfid);
			forumManager.setRealCurrentTopics(fid);
			forumManager.setRealCurrentTopics(oldfid);
		}
		//生成置顶帖
		resetTopTopicList(fid);
		resetTopTopicList(oldfid);
		return reval;
	}

	/**
	 * 删除关闭帖子
	 * @param fid
	 * @param topiclist
	 * @return
	 */
	private int deleteClosedTopics(int fid, String topiclist) {
		return topicDAO.createQuery("delete from Topics where forums.fid=? and closed in (" + topiclist + ")", fid)
				.executeUpdate();
	}

	/**
	 * 复制主题
	 * @param topiclist 主题id列表
	 * @param fid 目标版块id
	 * @return 更新记录数
	 * @throws ParseException
	 */
	public int copyTopics(String topiclist, int fid) throws ParseException {
		if (!Utils.isIntArray(topiclist.split(","))) {
			return -1;
		}
		Forums forums = new Forums();
		forums.setFid(fid);
		int reval = 0;
		Topics topicinfo = null;
		for (String topicid : topiclist.split(",")) {
			topicinfo = topicManager.getTopicInfo(Utils.null2Int(topicid, 0));
			if (topicinfo != null) {
				topicinfo.setTid(0);
				topicinfo.setForums(forums);
				topicinfo.setReadperm(0);
				topicinfo.setPrice(0);
				topicinfo.setPostdatetime(Utils.getNowTime());
				topicinfo.setLastpost(Utils.getNowTime());
				topicinfo.setLastposter("");
				topicinfo.setViews(0);
				topicinfo.setReplies(0);
				topicinfo.setDisplayorder(0);
				topicinfo.setHighlight("");
				topicinfo.setDigest(0);
				topicinfo.setRate(0);
				topicinfo.setHide(0);
				topicinfo.setSpecial(0);
				topicinfo.setAttachment(0);
				topicinfo.setModerated(0);
				topicinfo.setClosed(0);
				topicManager.createTopic(topicinfo);
				Posts postinfo = postManager.getPostInfo(postManager.getFirstPostId(Utils.null2Int(topicid, 0)));
				postinfo.setPid(0);
				postinfo.setForums(topicinfo.getForums());
				postinfo.setTopics(topicinfo);
				Postid postidByParentid = new Postid();
				postidByParentid.setPid(0);
				postinfo.setPostidByParentid(postidByParentid);
				postinfo.setLayer(0);
				postinfo.setPostdatetime(Utils.getNowTime());
				postinfo.setInvisible(0);
				postinfo.setAttachment(0);
				postinfo.setRate(0);
				postinfo.setRatetimes(0);
				postinfo.setMessage(UBBUtils.clearAttachUBB(postinfo.getMessage()));
				postinfo.setTitle(topicinfo.getTitle());
				postManager.createPost(postinfo);
				if (postinfo.getPid() > 0) {
					reval++;
				}
			}
		}
		return reval;
	}

	/**
	 * 给指定帖子评分
	 * @param tid
	 * @param postidlist 帖子列表
	 * @param score 要加／减的分值列表
	 * @param extcredits 对应的扩展积分列表
	 * @param userid
	 * @param username
	 * @param reason
	 * @return 更新数量
	 */
	public int ratePosts(int tid, String postidlist, String score, String extcredits, int userid, String username,
			String reason) {
		if (!Utils.isIntArray(postidlist.split(","))) {
			return 0;
		}
		double[] extcreditslist = new double[] { 0, 0, 0, 0, 0, 0, 0, 0 };
		String[] tmpScorelist = score.split(",");
		String[] tmpExtcreditslist = extcredits.split(",");
		int tempExtc = 0;
		for (int i = 0; i < tmpExtcreditslist.length; i++) {
			tempExtc = Utils.null2Int(tmpExtcreditslist[i], -1);
			if (tempExtc > 0 && tempExtc < extcreditslist.length) {
				extcreditslist[tempExtc - 1] = Utils.null2Int(tmpScorelist[i], 0);
				adminRateLogManager.insertLog(postidlist, userid, username, tempExtc, Utils
						.null2Int(tmpScorelist[i], 0), reason);

				//更新相应帖子的积分数
				for (String pid : postidlist.split(",")) {
					if (!pid.trim().equals("")) {
						setPostRate(Utils.null2Int(pid, 0), Utils.null2Int(tmpExtcreditslist[i], 0), Utils.null2Float(
								tmpScorelist[i], (float) 0), true);
					}
				}
				//}
			}
		}

		String useridlist = getUserListWithPostlist(tid, postidlist);

		return userCreditManager.updateUserCreditsByRate(useridlist, extcreditslist);

	}

	/**
	 * 根据得到给定帖子id的用户列表
	 * @param tid
	 * @param postlist 帖子列表
	 * @return 用户列表
	 */
	@SuppressWarnings("unchecked")
	public String getUserListWithPostlist(int tid, String postlist) {
		if (!Utils.isIntArray(postlist.split(","))) {
			return "";
		}
		StringBuilder useridlist = new StringBuilder();
		List<Integer> posterids = postDAO.find("select users.uid from Posts where pid in(" + postlist + ")");
		if (posterids != null) {
			for (Integer integer : posterids) {
				if (!useridlist.toString().equals("")) {
					useridlist.append(",");
				}
				useridlist.append(integer);
			}
		}
		return useridlist.toString();
	}

	/**
	 * 用当前的评分值通过一定兑换比例换算成积分后，更新相应贴子中的rate字段
	 * @param postid 帖子ID
	 * @param extid 扩展积分ID
	 * @param score 分数
	 * @param israte true为评分，false为撤消评分
	 */
	public void setPostRate(int postid, int extid, float score, boolean israte) {
		if (score == 0) {
			return;
		}

		List<Object[]> scorePaySet = scoresetManager.getRateScoreSet();
		if (scorePaySet.size() > 0) {
			if (!Utils.null2String(scorePaySet.get(extid - 1)[1]).equals("")) {
				Float rate = Utils.null2Float(scorePaySet.get(extid - 1)[2], (float) 0);
				if (rate != 0) {
					rate = (rate * score);
				}

				//当是撤消积分
				if (!israte) {
					rate = -1 * rate;
				}

				postDAO.createQuery("update Posts set rate=rate+? where pid in(" + postid + ")", rate.intValue())
						.executeUpdate();

				Posts post = postManager.getPostInfo(postid);
				if (post != null) {
					if (post.getLayer() == 0) {
						setTopicStatus(post.getTopics().getTid().toString(), "rate", post.getRate());
					}
				}

			}
		}
	}

	/**
	 * 主题鉴定
	 * @param topiclist
	 * @param identify
	 */
	public void identifyTopic(String topiclist, int identify) {
		if (!Utils.isIntArray(topiclist.split(",")))
			return;
		topicDAO.createQuery("update Topics set identify=? where tid in(" + topiclist + ")", identify).executeUpdate();
	}

	/**
	 * 提升下沉主题
	 * @param topiclist
	 * @param bumptype
	 */
	public void bumpTopics(String topiclist, int bumptype) {
		if (bumptype == 1) {
			String[] tidlist = topiclist.split(",");
			for (String tid : tidlist) {
				Postid postid = new Postid();
				postid.setPostdatetime(Utils.getNowTime());
				postidDAO.save(postid);
				topicDAO.createQuery("update Topics set postid.pid=? where tid=" + tid, postid.getPid())
						.executeUpdate();
			}
		} else {
			topicDAO.createQuery("update Topics set postid.pid=0-postid.pid where tid in(" + topiclist + ")")
					.executeUpdate();
		}

	}

	/**
	 * 检查评分状态
	 * @param postidlist 检查评分状态
	 * @param userid 用户id
	 * @return 被评分的帖子id字符串
	 */
	public String checkRateState(String postidlist, int userid) {
		String reval = "";
		String tempreval = "";
		for (String pid : postidlist.split(",")) {
			tempreval = Utils.null2String(postDAO.createQuery(
					"select postid.pid from Ratelog where  users.uid=? and postid.pid=" + pid, userid).setMaxResults(1)
					.uniqueResult());
			if (!tempreval.equals("")) {
				if (!reval.equals("")) {
					reval = reval + ",";
				}
				reval = reval + tempreval;
			}

		}
		return reval;
	}

	/**
	 * 撤消评分
	 */
	public void cancelRatePosts(String ratelogidlist, int tid, String pid, int userid, String username, int groupid,
			String grouptitle, int forumid, String forumname, String reason) {
		if (!Utils.isInt(pid)) {
			return;
		}

		List<Ratelog> ratelogList = adminRateLogManager.logList(ratelogidlist.split(",").length, 1, "id in("
				+ ratelogidlist + ")");//得到要删除的评分日志列表

		int rateduserid = postManager.getPostInfo(Utils.null2Int(pid, 0)).getUsers().getUid(); //被评分的用户的UID

		if (rateduserid <= 0) {
			return;
		}

		if (ratelogList.size() > 0) {
			for (Ratelog ratelog : ratelogList) {
				setPostRate(Utils.null2Int(pid, 0), ratelog.getExtcredits(), ratelog.getScore(), false);
				//乘-1是要进行分值的反向操作
				postDAO.createQuery(
						"update Users set extcredits" + ratelog.getExtcredits() + "=extcredits"
								+ ratelog.getExtcredits() + "+" + ((-1) * ratelog.getScore()) + " where uid=?",
						rateduserid).executeUpdate();
			}
		}

		adminRateLogManager.deleteRateLog("id in(" + ratelogidlist + ")");

		//当贴子已无评分记录时，则清空贴子相关的评分信息字段(rate,ratetimes)
		if (adminRateLogManager.logList(1, 1, "pid = " + pid).size() == 0) {
			postDAO.createQuery("update Posts set rate=0,ratetimes=0 where pid in(" + pid + ")").executeUpdate();
		}

		Topics topicinfo = topicManager.getTopicInfo(tid);
		String topictitle = "暂无标题";
		if (topicinfo != null) {
			topictitle = topicinfo.getTitle().trim();
		}

		adminModeratorLogMag.insertLog(userid, username, groupid, grouptitle, LForumRequest.getIp(),
				Utils.getNowTime(), forumid, forumname, tid, topictitle, "撤消评分", reason);

	}

	/**
	 * 返回指定主题的最后一次操作
	 * @param topicid 主题id
	 * @return 管理日志内容
	 */
	public String getTopicListModeratorLog(int topicid) {
		String str = "";
		Object[] objects = (Object[]) topicDAO
				.createQuery(
						"select grouptitle,moderatorname,postdatetime,actions from Moderatormanagelog where topics.tid=? order by id desc",
						topicid).setMaxResults(1).uniqueResult();
		if (objects != null) {
			str = "本主题由 " + objects[0] + " " + objects[1] + " 于 " + objects[2] + " 执行 " + objects[3] + " 操作";
		}
		return str;
	}
}