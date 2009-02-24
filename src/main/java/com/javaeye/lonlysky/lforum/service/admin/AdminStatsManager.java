package com.javaeye.lonlysky.lforum.service.admin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springside.modules.orm.hibernate.SimpleHibernateTemplate;

import com.javaeye.lonlysky.lforum.comm.utils.Utils;
import com.javaeye.lonlysky.lforum.entity.forum.ForumStatistics;
import com.javaeye.lonlysky.lforum.entity.forum.Forums;
import com.javaeye.lonlysky.lforum.entity.forum.Topics;
import com.javaeye.lonlysky.lforum.entity.forum.Users;
import com.javaeye.lonlysky.lforum.service.ForumManager;
import com.javaeye.lonlysky.lforum.service.PostManager;
import com.javaeye.lonlysky.lforum.service.StatisticManager;
import com.javaeye.lonlysky.lforum.service.TopicManager;
import com.javaeye.lonlysky.lforum.service.UserManager;

/**
 * 后台论坛统计功能类
 * 
 * @author 黄磊
 *
 */
@Service
@Transactional
public class AdminStatsManager {

	public enum ForumStats {
		topiccount, postcount, lasttid, lasttitle, lastpost, lastposterid, lastposter, todaypostcount
	}

	private static final long serialVersionUID = 8549350528434641912L;
	private static final Logger logger = LoggerFactory.getLogger(AdminStatsManager.class);
	private SimpleHibernateTemplate<Topics, Integer> topicDAO;

	@Autowired
	private TopicManager topicManager;

	@Autowired
	private PostManager postManager;

	@Autowired
	private ForumManager forumManager;

	@Autowired
	private UserManager userManager;

	@Autowired
	private StatisticManager statisticManager;

	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		topicDAO = new SimpleHibernateTemplate<Topics, Integer>(sessionFactory, Topics.class);
	}

	/**
	 * 重建指定论坛帖数
	 * @param fid
	 * @param forumSatts
	 */
	public void reSetFourmTopicAPost(int fid, Map<ForumStats, Object> map) {
		map.put(ForumStats.todaypostcount, 0);
		map.put(ForumStats.postcount, 0);
		map.put(ForumStats.lasttid, 0);
		map.put(ForumStats.lasttitle, "");
		map.put(ForumStats.lastpost, "");
		map.put(ForumStats.lastposterid, 0);
		map.put(ForumStats.lastposter, "");
		map.put(ForumStats.todaypostcount, 0);
		if (fid < 1) {
			return;
		}
		map.put(ForumStats.topiccount, topicManager.getAllTopicCount(fid));
		map.put(ForumStats.postcount, getPostsCountByFid(fid, map));

		Object[] objects = postManager.getLastPostByFid(fid);
		if (objects != null) {
			map.put(ForumStats.lasttid, objects[0]);
			map.put(ForumStats.lasttitle, topicManager.getTopicInfo(Utils.null2Int(objects[0])).getTitle().trim());
			map.put(ForumStats.lastpost, objects[2]);
			map.put(ForumStats.lastposterid, objects[3]);
			map.put(ForumStats.lastposter, objects[4]);
		}

	}

	/**
	 * 得到论坛中帖子总数
	 * @param fid 版块ID
	 * @param map 输出参数,根据返回指定版块今日发帖总数todaypostcount
	 * @return 帖子总数
	 */
	public int getPostsCountByFid(int fid, Map<ForumStats, Object> map) {
		int postcount = 0;
		map.put(ForumStats.todaypostcount, 0);

		///得到指定版块的最大和最小主题ID
		Object obj = topicDAO
				.createQuery(
						"select max(tid),min(tid) from Topics where fid in (select fid from Forums where fid=? or charindex(',' + trim(?) + ',', ',' + trim(parentidlist) + ',')>0)",
						fid, fid).uniqueResult();
		int maxtid = 0;
		int mintid = 0;
		if (obj != null) {
			Object[] objs = (Object[]) obj;
			maxtid = Utils.null2Int(objs[0], 0);
			mintid = Utils.null2Int(objs[1], 0);
		}

		if (mintid + maxtid == 0) {
			postcount = postManager.getPostCount(fid);
			map.put(ForumStats.todaypostcount, postManager.getTodayPostCount(fid));
		} else {
			postcount = postcount + postManager.getPostCount(fid);
			map.put(ForumStats.todaypostcount, (Utils.null2Int(map.get(ForumStats.todaypostcount)) + postManager
					.getTodayPostCount(fid)));
		}
		return postcount;
	}

	/**
	 * 重建指定论坛帖数
	 */
	public void reSetFourmTopicAPost(int fid) {
		if (fid < 1) {
			return;
		}

		int topiccount = 0;
		int postcount = 0;
		int lasttid = 0;
		String lasttitle = "";
		String lastpost = "1900-1-1";
		int lastposterid = 0;
		String lastposter = "";
		int todaypostcount = 0;
		topiccount = topicManager.getAllTopicCount(fid);

		Map<ForumStats, Object> map = new HashMap<ForumStats, Object>();
		postcount = getPostsCountByFid(fid, map);
		todaypostcount = Utils.null2Int(map.get(ForumStats.todaypostcount), 0);

		Object[] objects = postManager.getLastPostByFid(fid);
		if (objects != null) {
			lasttid = Utils.null2Int(objects[0], 0);
			lasttitle = topicManager.getTopicInfo(Utils.null2Int(objects[0])).getTitle().trim();
			lastpost = Utils.null2String(objects[2]);
			lastposterid = Utils.null2Int(objects[3], 0);
			lastposter = Utils.null2String(objects[4]);
		}
		Forums forum = forumManager.getForumInfo(fid);
		forum.setTopics_1(topiccount);
		forum.setPosts(postcount);
		Topics topics = new Topics();
		topics.setTid(lasttid);
		forum.setTopics(topics);
		forum.setLasttitle(lasttitle);
		forum.setLastpost(lastpost);
		Users users = new Users();
		users.setUid(lastposterid);
		forum.setUsers(users);
		forum.setLastposter(lastposter);
		forum.setTodayposts(todaypostcount);
		forumManager.updateForum(forum);
	}

	/**
	 * 重建论坛帖数
	 * @param statcount 要设置的版块数量
	 * @param lastfid 最后一个版块ID
	 * @return 最后一个版块ID
	 */
	public int reSetFourmTopicAPost(int statcount, int lastfid) {
		if (statcount < 1) {
			return -1;
		}
		forumManager.setRealCurrentTopics(lastfid);
		List<Integer> forumfids = forumManager.getTopForumFids(lastfid, statcount);
		lastfid = -1;
		if (forumfids != null) {
			int topiccount = 0;
			int postcount = 0;
			int todaypostcount = 0;
			for (Integer integer : forumfids) {
				lastfid = integer;
				topiccount = topicManager.getAllTopicCount(lastfid);
				Map<ForumStats, Object> map = new HashMap<ForumStats, Object>();
				postcount = getPostsCountByFid(lastfid, map);
				todaypostcount = Utils.null2Int(map.get(ForumStats.todaypostcount), 0);
				int lasttid = 0;
				String lasttitle = "";
				String lastpost = "1900-1-1";
				int lastposterid = 0;
				String lastposter = "";
				Object[] forumLastPost = forumManager.getForumLastPost(lastfid, topiccount, postcount, 0, "",
						"1900-1-1", 0, "", todaypostcount);
				if (forumLastPost != null) {
					lasttid = Utils.null2Int(forumLastPost[0], 0);
					lasttitle = topicManager.getTopicInfo(lasttid).getTitle();
					lastpost = forumLastPost[2].toString();
					lastposterid = Utils.null2Int(forumLastPost[3], 0);
					lastposter = forumLastPost[4].toString();
				}
				Forums forums = forumManager.getForumInfo(lastfid);
				forums.setTopics_1(topiccount);
				forums.setPosts(postcount);
				forums.setTopics(new Topics(lasttid));
				forums.setLasttitle(lasttitle);
				forums.setLastpost(lastpost);
				forums.setUsers(new Users(lastposterid));
				forums.setLastposter(lastposter);
				forums.setTodayposts(todaypostcount);
				forumManager.updateForum(forums);
				//				forumManager.updateForum(lastfid, topiccount, postcount, lasttid, lasttitle, lastpost, lastposterid,
				//						lastposter, todaypostcount);
			}
		}
		logger.info("重建论坛帖数,statcount:{},lastfid{}", statcount, lastfid);
		return lastfid;
	}

	/**
	 * 重设置用户精华帖数
	 * @param statcount 要设置的用户数量
	 * @param lastuid 最后一个用户ID
	 * @return 最后一个用户ID
	 */
	public int reSetUserDigestPosts(int statcount, int lastuid) {
		if (statcount < 1) {
			return -1;
		}
		List<Integer> useridList = userManager.getTopUsers(statcount, lastuid);
		lastuid = -1;
		if (useridList != null) {
			for (Integer integer : useridList) {
				lastuid = integer;
				userManager.resetUserDigestPosts(lastuid);
			}
		}
		logger.info("重设置用户精华帖数,statcount:{},lastuid{}", statcount, lastuid);
		return lastuid;

	}

	/**
	 * 重设置用户发帖数
	 * @param statcount 要设置的用户数量
	 * @param lastuid 最后一个用户ID
	 * @return 最后一个用户ID
	 */
	public int reSetUserPosts(int statcount, int lastuid) {
		if (statcount < 1) {
			return -1;
		}

		List<Integer> useridList = userManager.getTopUsers(statcount, lastuid);
		lastuid = -1;
		if (useridList != null) {
			for (Integer integer : useridList) {
				lastuid = integer;
				int postcount = getPostsCountByUid(lastuid);
				userManager.updateUserPostCount(postcount, lastuid);
			}
		}
		logger.info("重设置用户发帖数,statcount:{},lastuid{}", statcount, lastuid);
		return lastuid;
	}

	/**
	 * 返回指定版块的发帖总数
	 * @param uid 指定用户id
	 * @return 
	 */
	public int getPostsCountByUid(int uid) {
		Map<ForumStats, Integer> map = new HashMap<ForumStats, Integer>();
		return getPostsCountByUid(uid, map);
	}

	/**
	 * 得到论坛中帖子总数
	 * @param uid 用户ID
	 * @param map 根据返回指定版块今日发帖总数
	 * @return 帖子总数
	 */
	public int getPostsCountByUid(int uid, Map<ForumStats, Integer> map) {
		int postcount = 0;
		map.put(ForumStats.todaypostcount, 0);

		///得到指定版块的最大和最小主题ID
		int maxtid = 0;
		int mintid = 0;
		Object[] maxandminid = topicManager.getMaxAndMinTidByUid(uid);
		if (maxandminid != null) {
			maxtid = Utils.null2Int(maxandminid[0], 0);
			mintid = Utils.null2Int(maxandminid[1], 0);

		}

		if (mintid + maxtid == 0) {
			postcount = postManager.getPostCountByUid(uid);
			map.put(ForumStats.todaypostcount, postManager.getTodayPostCountByUid(uid));
		} else {
			postcount = postcount + postManager.getPostCountByUid(uid);
			map.put(ForumStats.todaypostcount, map.get(ForumStats.todaypostcount)
					+ postManager.getTodayPostCountByUid(uid));
		}
		return postcount;
	}

	/**
	 * 重建主题帖数
	 * 
	 * @param statcount 要设置的主题数量
	 * @param lasttid 最后一个主题ID
	 * @return 最后一个主题ID
	 */
	public int reSetTopicPosts(int statcount, int lasttid) {
		if (statcount < 1) {
			return -1;
		}
		List<Integer> tids = topicManager.getTopicTids(statcount, lasttid);
		lasttid = -1;

		if (tids != null) {
			int postcount = 0;
			for (Integer integer : tids) {
				lasttid = integer;
				postcount = postManager.getPostCountByTid(lasttid);
				Object[] lastpost = postManager.getLastPost(lasttid);
				if (lastpost != null) {
					if (Utils.null2Int(lastpost[0], 0) != 0) {
						topicManager.updateTopic(lasttid, postcount, Utils.null2Int(lastpost[0], 0), lastpost[1]
								.toString(), Utils.null2Int(lastpost[2], 0), lastpost[3].toString());
					} else {
						topicManager.updateTopicLastPosterId(lasttid);
					}
				}
			}
		}
		logger.info("重建主题帖数,statcount:{},lasttid{}", statcount, lasttid);
		return lasttid;
	}

	/**
	 * 除主题里面已经移走的主题
	 */
	public void reSetClearMove() {
		// TODO Auto-generated method stub

	}

	/**
	 * 重设论坛统计数据
	 */
	public void reSetStatistic() {
		int userCount = userManager.getUserCount();
		int topicsCount = topicManager.getTopicCount();
		int postCount = postManager.getPostCount();
		int lastuserid = 0;
		String lastusername = "";
		Map<Integer, String> map = new HashMap<Integer, String>();
		if (getLastUserInfo(map)) {
			for (Entry<Integer, String> entry : map.entrySet()) {
				lastuserid = entry.getKey();
				lastusername = entry.getValue();
			}
		}
		ForumStatistics statistics = statisticManager.getStatistic();
		statistics.setTotalpost(postCount);
		statistics.setLastusername(lastusername);
		statistics.setTotaltopic(topicsCount);
		statistics.setTotalusers(userCount);
		statistics.setUsers(new Users(lastuserid));
		statisticManager.updateStatistics(statistics);
	}

	/**
	 * 得到论坛中最后注册的用户ID和用户名
	 * @param map
	 * @return
	 */
	public boolean getLastUserInfo(Map<Integer, String> map) {
		Object[] objects = (Object[]) topicDAO.createQuery("select uid,username from Users order by uid desc")
				.setMaxResults(1).uniqueResult();
		if (objects != null) {
			map.put(Utils.null2Int(objects[0]), objects[1].toString());
			return true;
		}
		return false;
	}
}
