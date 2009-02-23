package com.javaeye.lonlysky.lforum.web;

import org.apache.struts2.config.ParentPackage;
import org.springframework.beans.factory.annotation.Autowired;

import com.javaeye.lonlysky.lforum.ForumBaseAction;
import com.javaeye.lonlysky.lforum.comm.ForumAction;
import com.javaeye.lonlysky.lforum.entity.forum.Users;
import com.javaeye.lonlysky.lforum.service.ScoresetManager;

/**
 * 版块列表(分栏模式)
 * 
 * @author 黄磊
 *
 */
@ParentPackage("default")
public class ForumlistAction extends ForumBaseAction {

	private static final long serialVersionUID = 4946063983332837937L;

	/**
	 * 当前登录的用户信息
	 */
	private Users userinfo;

	/**
	 * 总在线数
	 */
	private int totalonline;

	/**
	 * 总在线注册用户数
	 */
	private int totalonlineuser;

	/**
	 * 可用的扩展积分显示名称
	 */
	private String[] score;

	@Autowired
	private ScoresetManager scoresetManager;

	@Override
	public String execute() throws Exception {

		pagetitle = "版块列表";

		if (config.getRssstatus() == 1) {
			reqcfg.addLinkRss("tools/rss.action", config.getForumtitle() + "最新主题");
		}
		userinfo = new Users();
		if (userid != -1) {
			userinfo = userManager.getUserInfo(userid);
			if (userinfo.getNewpm() == 0) {
				newpmcount = 0;
			}
		}

		onlineUserManager.updateAction(olid, ForumAction.IndexShow.ACTION_ID, 0, config.getOnlinetimeout());
		// 获得统计信息
		totalonline = onlineusercount;
		totalonlineuser = onlineUserManager.getOnlineUserCount();

		score = scoresetManager.getValidScoreName();
		return SUCCESS;
	}

	public Users getUserinfo() {
		return userinfo;
	}

	public int getTotalonline() {
		return totalonline;
	}

	public int getTotalonlineuser() {
		return totalonlineuser;
	}

	public String[] getScore() {
		return score;
	}

}
