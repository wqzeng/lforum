package com.javaeye.lonlysky.lforum.web;

import org.apache.struts2.config.ParentPackage;
import org.springframework.beans.factory.annotation.Autowired;

import com.javaeye.lonlysky.lforum.ForumBaseAction;
import com.javaeye.lonlysky.lforum.entity.forum.Users;
import com.javaeye.lonlysky.lforum.service.ScoresetManager;

/**
 * 用户中心
 * 
 * @author 黄磊
 *
 */
@ParentPackage("default")
public class UsercpAction extends ForumBaseAction {

	private static final long serialVersionUID = 2325261708049324509L;

	/**
	 * 当前登录的用户信息
	 */
	private Users user = new Users();

	/**
	 * 可用的积分名称
	 */
	private String[] score;

	/**
	 * 头像
	 */
	private String avatar;

	/**
	 * 头像地址
	 */
	private String avatarurl;

	/**
	 * 头像类型
	 */
	private int avatartype;

	/**
	 * 头像宽度
	 */
	private int avatarwidth;

	/**
	 * 头像高度
	 */
	private int avatarheight;

	@Autowired
	private ScoresetManager scoresetManager;

	@Override
	public String execute() throws Exception {
		pagetitle = "用户控制面板";

		if (userid == -1) {
			reqcfg.addErrLine("你尚未登录");

			return SUCCESS;
		}
		user = userManager.getUserInfo(userid);

		if (!reqcfg.isErr()) {
			score = scoresetManager.getValidScoreName();
			avatar = user.getUserfields().getAvatar().trim();
			avatarurl = "";
			avatartype = 1;
			avatarwidth = 0;
			avatarheight = 0;
			if (avatar.indexOf("avatars\\common\\") != -1) {
				avatartype = 0;
			} else if (avatar.substring(0, 7).toLowerCase().equals("http://")) {
				avatarurl = avatar;
				avatartype = 2;
				avatarwidth = user.getUserfields().getAvatarwidth();
				avatarheight = user.getUserfields().getAvatarwidth();
			}

		}
		return SUCCESS;
	}

	public Users getUser() {
		return user;
	}

	public String[] getScore() {
		return score;
	}

	public String getAvatar() {
		return avatar;
	}

	public String getAvatarurl() {
		return avatarurl;
	}

	public int getAvatartype() {
		return avatartype;
	}

	public int getAvatarwidth() {
		return avatarwidth;
	}

	public int getAvatarheight() {
		return avatarheight;
	}
}
