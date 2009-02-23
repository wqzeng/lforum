package com.javaeye.lonlysky.lforum.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.javaeye.lonlysky.lforum.config.impl.ConfigLoader;
import com.javaeye.lonlysky.lforum.entity.forum.TopicView;

/**
 * 帖子状态操作
 * 
 * @author 黄磊
 *
 */
@Service
public class TopicStatManager {

	@Autowired
	private TopicManager topicManager;

	static int queuedAllowCount = 20;
	static TopicViewCollection<TopicView> queuedStatsList = null;
	
	/**
	 * 设置队列长度
	 */
	public void setQueueCount() {
		if (ConfigLoader.getConfig().getTopicqueuestatscount() > 20
				&& ConfigLoader.getConfig().getTopicqueuestatscount() <= 1000) {
			queuedAllowCount = ConfigLoader.getConfig().getTopicqueuestatscount();
		}
		if (queuedStatsList == null) {

			queuedStatsList = new TopicViewCollection<TopicView>();
		}
	}

	/**
	 * 获取指定帖子浏览数
	 * @param tid 帖子ID 
	 * @return
	 */
	public int getStoredTopicViewCount(int tid) {
		for (TopicView curtv : queuedStatsList) {
			if (curtv.getTopicID() == tid) {
				return curtv.getViewCount();
			}
		}
		return 0;
	}

	/**
	 * 追踪浏览量
	 * @param tv 主题浏览数对象
	 * @return 成功返回true
	 */
	public boolean track(TopicView tv) {
		if (tv == null) {
			return false;
		}
		if (queuedStatsList == null) {
			setQueueCount();
		}
		if (ConfigLoader.getConfig().getTopicqueuestats() == 1) {
			return addQuedStats(tv);
		} else {
			return trackTopic(tv);
		}
	}

	/**
	 * 追踪浏览量
	 * @param tid 主题id
	 * @param viewcount 浏览量
	 * @return 成功返回true
	 */
	public boolean track(int tid, int viewcount) {
		if (tid < 1 || viewcount < 1)
			return false;
		TopicView tv = new TopicView();
		tv.setTopicID(tid);
		tv.setViewCount(viewcount);
		return track(tv);
	}

	/**
	 * 向队列中增加统计对象
	 * @param tv 主题浏览数对象
	 * @return 成功返回true
	 */
	public boolean addQuedStats(TopicView tv) {
		if (tv == null)
			return false;
		if (queuedAllowCount != ConfigLoader.getConfig().getTopicqueuestatscount() || queuedStatsList == null) {
			setQueueCount();
		}

		//Check for the limit
		if (queuedStatsList.getViewCount() >= queuedAllowCount || queuedStatsList.size() >= 5) {
			//aquire the lock 
			synchronized (queuedStatsList) {
				//make sure the pool queue was not cleared during a wait for the lock
				if (queuedStatsList.getViewCount() >= queuedAllowCount || queuedStatsList.size() >= 5) {

					clearTrackTopicQueue(queuedStatsList);

					queuedStatsList.clear();
					queuedStatsList.setViewCount(0);
				}
			}
		}

		boolean inArray = false;
		for (TopicView curtv : queuedStatsList) {
			if (curtv.getTopicID() == tv.getTopicID()) {
				curtv.setViewCount(curtv.getViewCount() + tv.getViewCount());
				inArray = true;
				break;
			}
		}

		if (!inArray)
			queuedStatsList.add(tv);
		queuedStatsList.setViewCount(queuedStatsList.getViewCount() + 1);
		return true;
	}

	/**
	 * 清除追踪主题的队列
	 * @param tvc 主题浏览集合
	 * @return 成功返回true
	 */
	private boolean clearTrackTopicQueue(TopicViewCollection<TopicView> tvc) {
		ProcessStats processStats = new ProcessStats(tvc);
		processStats.run();
		return true;
	}

	/**
	 * 追踪主题
	 * @param tvc 主题浏览集合
	 * @return 成功返回true
	 */
	public boolean trackTopic(TopicViewCollection<TopicView> tvc) {
		if (tvc == null)
			return false;

		for (TopicView tv : tvc) {
			updateTopicViewCount(tv.getTopicID(), tv.getViewCount());
		}
		return true;
	}

	/**
	 * 追踪主题
	 * @param tvc 主题浏览对象
	 * @return 成功返回true
	 */
	public boolean trackTopic(TopicView tv) {
		return updateTopicViewCount(tv.getTopicID(), tv.getViewCount()) == 1;
	}

	/**
	 * 更新主题浏览量
	 * @param tid 主题id
	 * @param viewcount 浏览量
	 * @return 成功返回1，否则返回0
	 */
	public int updateTopicViewCount(int tid, int viewcount) {
		return topicManager.updateTopicViewCount(tid, viewcount);
	}
}

class ProcessStats implements Runnable {

	@Autowired
	protected TopicStatManager topicStatManager;

	protected TopicViewCollection<TopicView> tvc;

	public ProcessStats(TopicViewCollection<TopicView> tvc) {
		this.tvc = tvc;
	}

	/**
	 * 处理当前操作
	 */
	public void run() {
		topicStatManager.trackTopic(tvc);
	}
}

class TopicViewCollection<T> extends ArrayList<T> {

	public TopicViewCollection() {
		super();
	}

	private static final long serialVersionUID = 2166523128287574945L;

	private int viewCount;

	public int getViewCount() {
		return viewCount;
	}

	public void setViewCount(int viewCount) {
		this.viewCount = viewCount;
	}

}
