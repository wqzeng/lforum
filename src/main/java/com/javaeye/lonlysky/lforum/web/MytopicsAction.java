package com.javaeye.lonlysky.lforum.web;

import java.util.List;

import org.apache.struts2.config.ParentPackage;
import org.springframework.beans.factory.annotation.Autowired;

import com.javaeye.lonlysky.lforum.ForumBaseAction;
import com.javaeye.lonlysky.lforum.comm.LForumRequest;
import com.javaeye.lonlysky.lforum.comm.utils.ForumUtils;
import com.javaeye.lonlysky.lforum.entity.forum.ShowforumPageTopicInfo;
import com.javaeye.lonlysky.lforum.entity.forum.Users;
import com.javaeye.lonlysky.lforum.service.TopicManager;

/**
 * 我的主题
 * 
 * @author 黄磊
 *
 */
@ParentPackage("default")
public class MytopicsAction extends ForumBaseAction {

	private static final long serialVersionUID = 7858194195861114686L;

	/**
	 * 主题列表
	 */
	private List<ShowforumPageTopicInfo> topics;

	/**
	 * 当前页码
	 */
	private int pageid;
	/**
	 * 总页数
	 */
	private int pagecount;

	/**
	 * 主题总数
	 */
	private int topiccount;

	/**
	 * 分页页码链接
	 */
	private String pagenumbers;
	/**
	 * 当前登录的用户信息
	 */
	private Users user = new Users();

	private int pagesize = 16;

	@Autowired
	private TopicManager topicManager;

	@Override
	public String execute() throws Exception {
		pagetitle = "用户控制面板";

		if (userid == -1) {
			reqcfg.addErrLine("你尚未登录");
			return SUCCESS;
		}

		user = userManager.getUserInfo(userid);
		//得到当前用户请求的页数
		pageid = LForumRequest.getParamIntValue("page", 1);
		//获取主题总数
		topiccount = topicManager.getTopicsCountbyUserId(userid);
		//获取总页数
		pagecount = topiccount % pagesize == 0 ? topiccount / pagesize : topiccount / pagesize + 1;
		if (pagecount == 0) {
			pagecount = 1;
		}
		//修正请求页数中可能的错误
		if (pageid < 1) {
			pageid = 1;
		}
		if (pageid > pagecount) {
			pageid = pagecount;
		}

		topics = topicManager.getTopicsByUserId(userid, pageid, pagesize, 600, config.getHottopic());

		pagenumbers = ForumUtils.getPageNumbers(pageid, pagecount, "mytopics.action", 8);
		return SUCCESS;
	}

	public List<ShowforumPageTopicInfo> getTopics() {
		return topics;
	}

	public int getPageid() {
		return pageid;
	}

	public int getPagecount() {
		return pagecount;
	}

	public int getTopiccount() {
		return topiccount;
	}

	public String getPagenumbers() {
		return pagenumbers;
	}

	public Users getUser() {
		return user;
	}

	public int getPagesize() {
		return pagesize;
	}
}
