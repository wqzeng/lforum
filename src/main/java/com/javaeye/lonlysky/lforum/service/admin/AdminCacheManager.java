package com.javaeye.lonlysky.lforum.service.admin;

import com.javaeye.lonlysky.lforum.cache.LForumCache;

/**
 * 后台缓存管理类
 * 
 * @author 黄磊
 *
 */
public class AdminCacheManager {

	/**
	 * 重新设置版主信息
	 */
	public static void reSetModeratorList() {
		LForumCache.getInstance().removeCache("ModeratorList");
	}
}
