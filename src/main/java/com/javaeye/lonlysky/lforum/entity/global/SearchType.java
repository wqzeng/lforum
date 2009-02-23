package com.javaeye.lonlysky.lforum.entity.global;

/**
 * 搜索类型
 * 
 * @author 黄磊
 *
 */
public enum SearchType {
	
	/**
     * 全部搜索
     */
    All,
    
    /**
     * 搜索精华主题
     */
    DigestTopic,
    
    /**
     * 搜索主题标题
     */
    TopicTitle,
    
    /**
     * 搜索帖子标题
     */
    PostTitle,
    
    /**
     * 帖子全文检索
     */
    PostContent,
    
    /**
     * 仅按作者搜索,搜索论坛
     */
    ByPoster
}
