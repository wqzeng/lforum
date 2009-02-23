package com.javaeye.lonlysky.lforum.cache;

import java.net.URL;
import java.util.List;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * LForum缓存管理
 * 
 * @author 黄磊
 *
 */
public class LForumCache {

	private static final Logger logger = LoggerFactory.getLogger(LForumCache.class);
	private static Object lockObject = new Object();
	private static LForumCache instance = null;

	public static final String CACHE_NAME = "LForumCaches";

	private LForumCache() {
	}

	/**
	 * 获取换此类实例
	 * 
	 * @return LForumCache实例
	 */
	public static LForumCache getInstance() {
		if (instance == null) {
			synchronized (lockObject) {
				logger.info("创建新的缓存类实例");
				instance = new LForumCache();
			}
		}
		return instance;
	}

	/**
	 * 获取缓存管理类
	 * 
	 * @return 缓存管理类
	 */
	public CacheManager getCacheManager() {
		URL url = getClass().getResource("/ehcache-hibernate.xml");
		return CacheManager.create(url);
	}

	/**
	 * 获取论坛缓存
	 * 
	 * @return 缓存
	 */
	public Cache getCache() {
		return getCacheManager().getCache(CACHE_NAME);
	}

	/**
	 * 添加缓存对象
	 * 
	 * @param key 缓存KEY
	 * @param cacheObj 缓存对象
	 */
	public void addCache(String key, Object cacheObj) {
		getCache().put(new Element(key, cacheObj));
		if (logger.isDebugEnabled()) {
			logger.debug("添加KEY为{}的缓存对象,当前缓存{}个", key, getCache().getSize());
		}
	}

	/**
	 * 获取缓存对象
	 * 
	 * @param key 缓存KEY
	 * @return 缓存对象
	 */
	public Object getCache(String key) {
		Element element = getCache().get(key);
		if (element == null) {
			return null;
		}
		if (logger.isDebugEnabled()) {
			logger.debug("获取KEY为{}的缓存对象", key);
		}
		return (Object) element.getValue();

	}

	/**
	 * 获取指定类型缓存对象
	 * 
	 * @param <T> 指定类型
	 * @param key  缓存KEY
	 * @param classz 类型Class
	 * @return 指定类型缓存对象
	 */
	@SuppressWarnings("unchecked")
	public <T> T getCache(String key, Class<T> classz) {
		if (getCache(key) == null) {
			return null;
		}
		if (logger.isDebugEnabled()) {
			logger.debug("获取KEY为{}且类型为{}的缓存对象", key, classz.getName());
		}
		return (T) getCache(key);
	}

	/**
	 * 获取指定类型LIST缓存
	 * 
	 * @param <T>  指定类型
	 * @param key 缓存KEY
	 * @param classz 类型Class
	 * @return 指定类型LIST缓存
	 */
	@SuppressWarnings("unchecked")
	public <T> List<T> getListCache(String key, Class<T> classz) {
		if (getCache(key) == null) {
			return null;
		}
		if (logger.isDebugEnabled()) {
			logger.debug("获取KEY为{}且类型为{}的LIST缓存", key, classz.getName());
		}
		return (List<T>) getCache(key);
	}

	/**
	 * 移除指定KEY缓存对象
	 * 
	 * @param key 缓存KEY
	 */
	public void removeCache(String key) {
		if (getCache(key) != null) {
			getCache().remove(key);
			if (logger.isDebugEnabled()) {
				logger.debug("移除取KEY为{}的缓存对象", key);
			}
		}
	}
}
