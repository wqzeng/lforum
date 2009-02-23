package com.javaeye.lonlysky.lforum.web;

import org.apache.struts2.config.ParentPackage;
import org.springframework.beans.factory.annotation.Autowired;

import com.javaeye.lonlysky.lforum.ForumBaseAction;
import com.javaeye.lonlysky.lforum.comm.LForumRequest;
import com.javaeye.lonlysky.lforum.comm.utils.Utils;
import com.javaeye.lonlysky.lforum.entity.forum.Admingroups;
import com.javaeye.lonlysky.lforum.entity.forum.Usergroups;
import com.javaeye.lonlysky.lforum.entity.forum.Users;
import com.javaeye.lonlysky.lforum.service.AdminGroupManager;
import com.javaeye.lonlysky.lforum.service.ScoresetManager;

/**
 * 查看用户信息页
 * 
 * @author 黄磊
 *
 */
@ParentPackage("default")
public class UserinfoAction extends ForumBaseAction {

	private static final long serialVersionUID = -442043270090320887L;

	/**
	 * 当前用户信息
	 */
	private Users user;

	/**
	 * 当前用户用户组信息
	 */
	private Usergroups group;

	/**
	 * 当前用户管理组信息
	 */
	private Admingroups admininfo;

	/**
	 * 可用的扩展积分名称列表
	 */
	private String[] score;

	/**
	 * 是否需要快速登录
	 */
	public boolean needlogin = false;

	@Autowired
	private AdminGroupManager adminGroupManager;

	@Autowired
	private ScoresetManager scoresetManager;

	@Override
	public String execute() throws Exception {

		pagetitle = "查看用户信息";

		if (usergroupinfo.getAllowviewpro() != 1) {
			reqcfg.addErrLine("您当前的身份 \"" + usergroupinfo.getGrouptitle() + "\" 没有查看用户资料的权限");
			if (userid < 1)
				needlogin = true;
			return SUCCESS;
		}

		if (LForumRequest.getParamValue("username").equals("") && LForumRequest.getParamValue("userid").equals("")) {
			reqcfg.addErrLine("错误的URL链接");
			return SUCCESS;
		}

		int id = LForumRequest.getParamIntValue("userid", -1);

		if (id == -1) {
			id = userManager.getUserId(Utils.urlDecode(LForumRequest.getParamValue("username")));
		}

		if (id == -1) {
			reqcfg.addErrLine("该用户不存在");
			return SUCCESS;
		}

		user = userManager.getUserInfo(id);
		if (user == null) {
			reqcfg.addErrLine("该用户不存在");
			return SUCCESS;
		}

		//用户设定Email保密时，清空用户的Email属性以避免被显示
		if (user.getShowemail() != 1) {
			user.setEmail("");
		}
		//获取积分机制和用户组信息，底层有缓存
		score = scoresetManager.getValidScoreName();
		group = userGroupManager.getUsergroup(user.getUsergroups().getGroupid());
		admininfo = adminGroupManager.getAdminGroup(group.getAdmingroups().getAdmingid());
		return SUCCESS;
	}

	public Users getUser() {
		return user;
	}

	public Usergroups getGroup() {
		return group;
	}

	public Admingroups getAdmininfo() {
		return admininfo;
	}

	public String[] getScore() {
		return score;
	}
}
