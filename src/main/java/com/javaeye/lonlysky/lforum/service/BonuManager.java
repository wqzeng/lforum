package com.javaeye.lonlysky.lforum.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springside.modules.orm.hibernate.SimpleHibernateTemplate;

import com.javaeye.lonlysky.lforum.comm.utils.Utils;
import com.javaeye.lonlysky.lforum.entity.forum.Bonuslog;
import com.javaeye.lonlysky.lforum.entity.forum.Postid;
import com.javaeye.lonlysky.lforum.entity.forum.Topics;
import com.javaeye.lonlysky.lforum.entity.forum.Users;

/**
 * 悬赏操作类
 * 
 * @author 黄磊
 *
 */
@Service
@Transactional
public class BonuManager {

	private static final Logger logger = LoggerFactory.getLogger(BonuManager.class);

	private SimpleHibernateTemplate<Bonuslog, Integer> bonuslogDAO;

	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		bonuslogDAO = new SimpleHibernateTemplate<Bonuslog, Integer>(sessionFactory, Bonuslog.class);
	}

	@Autowired
	private ScoresetManager scoresetManager;

	@Autowired
	private TopicManager topicManager;

	@Autowired
	private UserManager userManager;

	/**
	 * 添加悬赏日志
	 * @param topics 主题
	 * @param author 悬赏者
	 * @param winer 获奖者
	 * @param winnerName 获奖者用户名
	 * @param post 帖子	
	 * @param bonus 奖励积分
	 * @param isbest 是否是最佳答案，0=不是,1=是较好的答案,2=最佳答案
	 */
	public void addLog(Topics topics, Users author, Users winer, String winnerName, Postid post, int bonus, int isbest) {
		Bonuslog bonuslog = new Bonuslog();
		bonuslog.setAnswername("");
		bonuslog.setBonus(bonus);
		bonuslog.setExtid(scoresetManager.getCreditsTrans());
		bonuslog.setIsbest(isbest);
		bonuslog.setPostid(post);
		bonuslog.setDateline(Utils.getNowTime());
		bonuslog.setTopics(topics);
		bonuslog.setUsersByAuthorid(author);

		bonuslogDAO.save(bonuslog);
		if (logger.isDebugEnabled()) {
			logger.debug("添加悬赏日志{}成功", bonuslog.getId());
		}
	}

	/**
	 * 结束悬赏并给分
	 * @param topicinfo 主题信息
	 * @param userid 当前执行此操作的用户Id
	 * @param postIdArray 帖子Id数组
	 * @param winerIdArray 获奖者Id数组
	 * @param winnerNameArray 获奖者的用户名数组
	 * @param costBonusArray 奖励积分数组
	 * @param valuableAnswerArray 有价值答案的pid数组
	 * @param bestAnswer 最佳答案的pid
	 */
	public void closeBonus(Topics topicinfo, int userid, int[] postIdArray, int[] winerIdArray,
			String[] winnerNameArray, String[] costBonusArray, String[] valuableAnswerArray, int bestAnswer) {
		topicinfo.setSpecial(3);
		topicManager.updateTopic(topicinfo);//更新标志位为已结帖状态

		//开始给分和记录
		int winerId = 0, isbest = 0, postid = 0, bonus = 0;
		String winnerName = "";
		for (int i = 0; i < winerIdArray.length; i++) {
			winerId = winerIdArray[i];
			bonus = Utils.null2Int(costBonusArray[i], 0);
			winnerName = winnerNameArray[i];
			postid = postIdArray[i];

			if (winerId > 0 && bonus > 0) {
				userManager.updateUserExtCredits(winerId, scoresetManager.getCreditsTrans(), bonus);
			}
			if (Utils.inArray(postid + "", valuableAnswerArray)) {
				isbest = 1;
			}
			if (postid == bestAnswer) {
				isbest = 2;
			}
			Users users = new Users();
			users.setUid(winerId);

			Postid pid = new Postid();
			pid.setPid(postid);
			addLog(topicinfo, topicinfo.getUsersByPosterid(), users, winnerName, pid, bonus, isbest);
		}
	}

	/**
	 * 获取指定主题的给分记录
	 * @param tid 主题ID
	 * @return 悬赏日志集合
	 */
	@SuppressWarnings("unchecked")
	public List<Bonuslog> getLogs(int tid) {
		List<Object[]> objlist = bonuslogDAO
				.find(
						"select topics.tid,usersByAuthorid.uid,usersByAnswerid.uid,answername,extid,sum(bonus) as bonus form Bonuslog where topics.tid=? group by usersByAnswerid.uid,usersByAuthorid.uid,tid,answername,extid",
						tid);
		List<Bonuslog> bonuslogList = new ArrayList<Bonuslog>();

		for (Object[] objs : objlist) {
			// 封装日志对象
			Topics topics = new Topics();
			topics.setTid(Utils.null2Int(objs[0], 0));

			Users usersByAuthorid = new Users();
			usersByAuthorid.setUid(Utils.null2Int(objs[1], 0));

			Users usersByAnswerid = new Users();
			usersByAnswerid.setUid(Utils.null2Int(objs[2], 0));

			Bonuslog bonuslog = new Bonuslog();
			bonuslog.setTopics(topics);
			bonuslog.setUsersByAuthorid(usersByAuthorid);
			bonuslog.setUsersByAnswerid(usersByAnswerid);
			bonuslog.setAnswername(Utils.null2String(objs[3]));
			bonuslog.setExtid(Utils.null2Int(objs[4]));
			bonuslog.setBonus(Utils.null2Int(objs[5], 0));

			bonuslogList.add(bonuslog);
		}
		return bonuslogList;
	}

	/**
	 * 获取指定主题的给分记录
	 * @param tid 主题Id
	 * @return 指定主题的给分记录
	 */
	public Map<Integer, Bonuslog> getLogsForEachPost(int tid) {
		List<Bonuslog> bList = bonuslogDAO.findByProperty("topics.tid", tid);
		Map<Integer, Bonuslog> blis = new HashMap<Integer, Bonuslog>();
		for (Bonuslog blog : bList) {
			blis.put(blog.getPostid().getPid(), blog);
		}
		return blis;
	}
}
