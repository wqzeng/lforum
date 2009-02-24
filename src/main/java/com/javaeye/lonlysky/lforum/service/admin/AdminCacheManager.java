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

	public static void reSetStatistics() {
		// TODO Auto-generated method stub

	}

	/**
	 * 重新设置友情链接列表
	 */
	public static void reSetForumLinkList() {
		LForumCache.getInstance().removeCache("ForumLinkList");
	}

	/**
	 * 重置论坛列表
	 */
	public static void reSetForumList() {
		LForumCache.getInstance().removeCache("ForumList");
	}

	/**
	 * 重新设置版块下拉列表
	 */
	public static void reSetForumListBoxOptions() {
		LForumCache.getInstance().removeCache("ForumListBoxOptions");
	}
}
