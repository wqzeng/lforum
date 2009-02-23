package com.javaeye.lonlysky.lforum.service;

import java.text.ParseException;
import java.util.Map;

import org.apache.commons.lang.time.DateUtils;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springside.modules.orm.hibernate.SimpleHibernateTemplate;

import com.javaeye.lonlysky.lforum.cache.LForumCache;
import com.javaeye.lonlysky.lforum.comm.utils.Utils;
import com.javaeye.lonlysky.lforum.entity.forum.ForumStatistics;
import com.javaeye.lonlysky.lforum.entity.forum.Forums;
import com.javaeye.lonlysky.lforum.exception.ServiceException;

/**
 * 论坛统计业务处理
 * 
 * @author 黄磊
 *
 */
@Service
@Transactional
public class StatisticManager {

	private static final Logger logger = LoggerFactory.getLogger(StatisticManager.class);
	private SimpleHibernateTemplate<ForumStatistics, Integer> statisticsDAO;
	private SimpleHibernateTemplate<Forums, Integer> forumDAO;

	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		statisticsDAO = new SimpleHibernateTemplate<ForumStatistics, Integer>(sessionFactory, ForumStatistics.class);
		forumDAO = new SimpleHibernateTemplate<Forums, Integer>(sessionFactory, Forums.class);
	}

	/**
	 * 主题统计
	 */
	public static final String TOPIC_COUNT = "topiccount";

	/**
	 * 帖子统计
	 */
	public static final String POST_COUNT = "postcount";

	/**
	 * 近日帖子统计
	 */
	public static final String TODAYPOST_COUNT = "todaypostcount";

	/**
	 * 获取论坛统计信息
	 * 
	 * @return 论坛统计信息
	 */
	public ForumStatistics getStatistic() {
		return statisticsDAO.get(1);
	}

	/**
	 * 获取指定版块中的主题帖子统计数据
	 * @param fid 板块ID
	 * @param countMap 存放统计的集合
	 */
	public void getPostCountFromForum(int fid, Map<String, Integer> countMap) {
		if (countMap == null) {
			throw new ServiceException("统计集合不能为空");
		}
		int topiccount = 0, postcount = 0, todaypostcount = 0;
		if (fid == 0) { //统计所有板块
			// 主题统计
			topiccount = Utils.null2Int(forumDAO.createCriteria(Property.forName("layer").eq(1)).setProjection(
					Projections.sum("topics_1")).uniqueResult(), 0);
			// 帖子统计
			postcount = Utils.null2Int(forumDAO.createCriteria(Property.forName("layer").eq(1)).setProjection(
					Projections.sum("posts")).uniqueResult(), 0);
			// 今日帖子统计
			todaypostcount = Utils.null2Int(forumDAO.createCriteria(Property.forName("layer").eq(1)).setProjection(
					Projections.sum("todayposts")), 0)
					- Utils.null2Int(forumDAO.createCriteria(Property.forName("lastpost").lt(Utils.getNowTime())).add(
							Property.forName("layer").eq(1)).setProjection(Projections.sum("todayposts")), 0);
		} else {
			// 主题统计
			topiccount = Utils.null2Int(forumDAO.createCriteria(Property.forName("layer").eq(1)).add(
					Property.forName("fid").eq(fid)).setProjection(Projections.sum("topics_1")).uniqueResult(), 0);
			// 帖子统计
			postcount = Utils.null2Int(forumDAO.createCriteria(Property.forName("layer").eq(1)).add(
					Property.forName("fid").eq(fid)).setProjection(Projections.sum("posts")).uniqueResult(), 0);
			// 今日帖子统计
			todaypostcount = Utils.null2Int(forumDAO.createCriteria(Property.forName("layer").eq(1)).add(
					Property.forName("fid").eq(fid)).setProjection(Projections.sum("todayposts")), 0)
					- Utils.null2Int(forumDAO.createCriteria(Property.forName("lastpost").lt(Utils.getNowTime())).add(
							Property.forName("layer").eq(1)).add(Property.forName("fid").eq(fid)).setProjection(
							Projections.sum("todayposts")), 0);
		}
		if (logger.isDebugEnabled()) {
			logger.debug("主题统计：" + topiccount + ",帖子统计：" + postcount + ",今日帖子统计：" + todaypostcount);
		}
		countMap.put(TOPIC_COUNT, topiccount);
		countMap.put(POST_COUNT, postcount);
		countMap.put(TODAYPOST_COUNT, todaypostcount);
	}

	/**
	 * 更新论坛统计信息
	 */
	public void updateStatistics(ForumStatistics statistics) {
		statisticsDAO.save(statistics);
	}

	/**
	 * 得到上一次执行搜索操作的时间
	 * @return
	 */
	public String getStatisticsSearchtime() {
		String searchtime = LForumCache.getInstance().getCache("StatisticsSearchtime", String.class);
		if (searchtime == null) {
			searchtime = Utils.getNowTime();
			LForumCache.getInstance().addCache("StatisticsSearchtime", searchtime);
		}
		return searchtime;
	}

	/**
	 * 得到用户在一分钟内搜索的次数
	 * @return
	 */
	public int getStatisticsSearchcount() {
		int searchcount = Utils.null2Int(LForumCache.getInstance().getCache("StatisticsSearchcount"), 0);
		if (searchcount == 0) {
			searchcount = 1;
			LForumCache.getInstance().addCache("StatisticsSearchcount", searchcount);
		}
		return searchcount;
	}

	/**
	 * 重新设置用户上一次执行搜索操作的时间
	 * @param searchtime 操作时间
	 */
	public void setStatisticsSearchtime(String searchtime) {
		LForumCache.getInstance().removeCache("StatisticsSearchtime");
		LForumCache.getInstance().addCache("StatisticsSearchtime", searchtime);
	}

	/**
	 * 设置用户在一分钟内搜索的次数为初始值。
	 * @param searchcount 初始值
	 */
	public void setStatisticsSearchcount(int searchcount) {
		LForumCache.getInstance().removeCache("StatisticsSearchcount");
		LForumCache.getInstance().addCache("StatisticsSearchcount", searchcount);
	}

	/**
	 * 检查并更新60秒内统计的数量
	 * @param maxspm 60秒内允许的最大搜索次数
	 * @return 没有超过最大搜索次数返回true
	 * @throws ParseException
	 */
	public boolean checkSearchCount(int maxspm) throws ParseException {
		if (maxspm == 0)
			return true;
		String searchtime = getStatisticsSearchtime();
		int searchcount = getStatisticsSearchcount();
		if (Utils.strDateDiffSeconds(searchtime, 60) > 0) {
			setStatisticsSearchtime(Utils.getNowTime());
			setStatisticsSearchcount(1);
		}

		if (searchcount > maxspm) {
			return false;
		}

		setStatisticsSearchcount(searchcount + 1);
		return true;

	}

	/**
	 * 更新昨日发帖
	 */
	public void updateYesterdayPosts() {
		int yesterdayposts = Utils.null2Int(statisticsDAO.findUnique(
				"select count(pid) from Posts where postdatetime<? and postdatetime>? and invisible=0", Utils
						.getNowShortDate(), Utils.dateFormat(DateUtils.addDays(Utils.getNowDate(), -1),
						Utils.SHORT_DATEFORMAT)), 0);

		ForumStatistics statistics = getStatistic();
		int highestposts = statistics.getHighestposts();
		statistics.setYesterdayposts(yesterdayposts);
		if (yesterdayposts > highestposts) {
			statistics.setHighestposts(yesterdayposts);
			statistics.setHighestpostsdate(Utils.dateFormat(DateUtils.addDays(Utils.getNowDate(), -1),
					Utils.SHORT_DATEFORMAT));
		}
		updateStatistics(statistics);
	}
}
