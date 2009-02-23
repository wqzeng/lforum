package com.javaeye.lonlysky.lforum.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springside.modules.test.SpringTransactionalTestCase;

import com.javaeye.lonlysky.lforum.entity.forum.ForumStatistics;

/**
 * StatisticManager集成测试
 * 
 * @author 黄磊
 *
 */
public class StatisticManagerTest extends SpringTransactionalTestCase {

	@Autowired
	private StatisticManager statisticManager;

	public void test() {
		ForumStatistics statistics = statisticManager.getStatistic();
		assertNotNull(statistics);

		statistics.setHighestonlineusercount(10);
		statisticManager.updateStatistics(statistics);
		flush();

		Map<String, Integer> countMap = new HashMap<String, Integer>();
		statisticManager.getPostCountFromForum(0, countMap);

	}
}
