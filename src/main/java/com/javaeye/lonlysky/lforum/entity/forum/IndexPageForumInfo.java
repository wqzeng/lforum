package com.javaeye.lonlysky.lforum.entity.forum;

/**
 * 论坛首页显示板块<br>
 * 加入是否有新主题和是否收起的属性
 * 
 * @author 黄磊
 *
 */
public class IndexPageForumInfo extends Forums {

	private static final long serialVersionUID = -8589015507515500911L;
	private String havenew;
	private String collapse;

	/**
	 * 获取是否是新主题
	 */
	public String getHavenew() {
		return havenew;
	}

	/**
	 * 设置是否是新主题
	 */
	public void setHavenew(String havenew) {
		this.havenew = havenew;
	}

	/**
	 * 获取是否收起(如果是则输出'display: none;')
	 */
	public String getCollapse() {
		return collapse;
	}

	/**
	 * 设置是否收起
	 */
	public void setCollapse(String collapse) {
		this.collapse = collapse;
	}

}
