package com.javaeye.lonlysky.lforum.service.admin;

import java.util.HashMap;
import java.util.Map;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springside.modules.orm.hibernate.SimpleHibernateTemplate;

import com.javaeye.lonlysky.lforum.comm.utils.Utils;
import com.javaeye.lonlysky.lforum.entity.forum.Forums;
import com.javaeye.lonlysky.lforum.entity.forum.Topics;
import com.javaeye.lonlysky.lforum.entity.forum.Users;
import com.javaeye.lonlysky.lforum.service.ForumManager;
import com.javaeye.lonlysky.lforum.service.PostManager;
import com.javaeye.lonlysky.lforum.service.TopicManager;

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
	private SimpleHibernateTemplate<Topics, Integer> topicDAO;

	@Autowired
	private TopicManager topicManager;

	@Autowired
	private PostManager postManager;

	@Autowired
	private ForumManager forumManager;

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
}
