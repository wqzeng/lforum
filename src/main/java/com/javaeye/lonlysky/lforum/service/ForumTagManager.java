package com.javaeye.lonlysky.lforum.service;

import java.util.List;

import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springside.modules.orm.hibernate.Page;
import org.springside.modules.orm.hibernate.SimpleHibernateTemplate;

import com.javaeye.lonlysky.lforum.cache.LForumCache;
import com.javaeye.lonlysky.lforum.config.impl.ConfigLoader;
import com.javaeye.lonlysky.lforum.entity.global.Tags;

/**
 * 论坛标签(Tag)操作类
 * 
 * @author 黄磊
 *
 */
@Service
@Transactional
public class ForumTagManager {

	private static final Logger logger = LoggerFactory.getLogger(ForumTagManager.class);
	private SimpleHibernateTemplate<Tags, Integer> tagDAO;

	@Autowired
	private TagManager tagManager;

	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		tagDAO = new SimpleHibernateTemplate<Tags, Integer>(sessionFactory, Tags.class);
	}

	/**
	 * 论坛热门标签缓存文件(json格式)文件路径
	 */
	private String forumHotTagJSONCacheFileName = "cache\\tag\\hottags_forum_cache_json.txt";

	/**
	 * 论坛热门标签缓存文件(jsonp格式)文件路径
	 */
	private String forumHotTagJSONPCacheFileName = "cache\\tag\\hottags_forum_cache_jsonp.txt";

	/**
	 * 写入热门标签缓存文件(json格式)
	 * @param count 数量
	 */
	public void writeHotTagsListForForumCacheFile(int count) {
		String filename = ConfigLoader.getConfig().getWebpath() + forumHotTagJSONCacheFileName;
		List<Tags> tags = getHotTagsListForForum(count);
		tagManager.writeTagsCacheFile(filename, tags, "", true);
		if (logger.isDebugEnabled()) {
			logger.debug("写入热门标签缓存文件(json格式) count:{}", count);
		}
	}

	/**
	 * 写入热门标签缓存文件(jsonp格式)
	 * @param count
	 */
	public void writeHotTagsListForForumJSONPCacheFile(int count) {
		String filename = ConfigLoader.getConfig().getWebpath() + forumHotTagJSONPCacheFileName;
		List<Tags> tags = getHotTagsListForForum(count);
		tagManager.writeTagsCacheFile(filename, tags, "forumhottag_callback", true);
		if (logger.isDebugEnabled()) {
			logger.debug("写入热门标签缓存文件(jsonp格式) count:{}", count);
		}
	}

	/**
	 * 获取论坛热门标签
	 * @param count 数量
	 * @return List
	 */
	public List<Tags> getHotTagsListForForum(int count) {
		Page<Tags> page = new Page<Tags>(count);
		page.setPageNo(1);
		return tagDAO.find(page, "from Tags where fcount>0 and orderid>-1 order by orderid,fcount desc").getResult();
	}

	/**
	 * 获取60个论坛热门标签
	 * @return List
	 */
	public List<Tags> getHotTagsListForForum() {
		return getHotTagsListForForum(60);
	}

	/**
	 * 写入主题标签缓存文件
	 * @param topicid 主题Id
	 */
	public void writeTopicTagsCacheFile(int topicid) {
		StringBuilder dir = new StringBuilder();
		dir.append(ConfigLoader.getConfig().getWebpath());
		dir.append("cache/topic/magic/");
		dir.append((topicid / 1000 + 1));
		dir.append("/");
		String filename = dir.toString() + topicid + "_tags.config";
		List<Tags> tags = getTagsListByTopic(topicid);
		tagManager.writeTagsCacheFile(filename, tags, "", false);
		if (logger.isDebugEnabled()) {
			logger.debug("写入主题标签缓存文件 topicid:{}", topicid);
		}
	}

	/**
	 * 获取主题所包含的Tag
	 * @param topicid 主题Id
	 * @return List
	 */
	@SuppressWarnings("unchecked")
	public List<Tags> getTagsListByTopic(int topicid) {
		return tagDAO
				.find(
						"from Tags as tag,Topictags as ttag where ttag.tagid=tag.tagid and ttag.topics.tid=? order by tag.orderid",
						topicid);
	}

	public void createTopicTags(String[] tagArray, int topicid, int userid, String curdatetime) {
	}

	/**
	 * 缓存的热门标签
	 * @param count 标签数
	 * @return
	 */
	public List<Tags> getCachedHotForumTags(int count) {
		List<Tags> tags = LForumCache.getInstance().getListCache("Hot-" + count, Tags.class);
		if (tags != null) {
			return tags;
		}
		tags = getHotTagsListForForum(count);
		LForumCache.getInstance().addCache("Hot-" + count, tags);
		return tags;
	}

	/**
	 * 删除主题标签
	 * @param topicid
	 */
	public void deleteTopicTags(int topicid) {
		tagDAO
				.createQuery(
						"update Tags set count=count-1,fcount=fcount-1 where exists(select tagid from Topictags where topics.tid=? and tagid=Tags.tagid)",
						topicid).executeUpdate();
		tagDAO.createQuery("delete from Topictags where topics.tid=?", topicid);
	}
}
