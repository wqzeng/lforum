package com.javaeye.lonlysky.lforum.web;

import org.apache.struts2.config.ParentPackage;

import com.javaeye.lonlysky.lforum.ForumBaseAction;
import com.javaeye.lonlysky.lforum.comm.LForumRequest;
import com.javaeye.lonlysky.lforum.comm.utils.ForumUtils;
import com.javaeye.lonlysky.lforum.comm.utils.Utils;
import com.javaeye.lonlysky.lforum.entity.forum.Users;

/**
 * 短消息基本设置页面
 * 
 * @author 黄磊
 *
 */
@ParentPackage("default")
public class UsercppmsetAction extends ForumBaseAction {

	private static final long serialVersionUID = -6731526086916629066L;

	/**
	 * 当前用户信息
	 */
	private Users user = new Users();

	/**
	 * 短消息接收设置
	 */
	private int receivepmsetting;

	@Override
	public String execute() throws Exception {
		pagetitle = "用户控制面板";

		if (userid == -1) {
			reqcfg.addErrLine("你尚未登录");
			return SUCCESS;
		}
		user = userManager.getUserInfo(userid);
		receivepmsetting = user.getNewsletter();

		if (LForumRequest.isPost()) {
			user.setPmsound(LForumRequest.getParamIntValue("pmsound", 0));

			receivepmsetting = 1;
			for (String rpms : LForumRequest.getParamValues("receivesetting")) {
				if (!rpms.trim().equals("")) {
					int tmp = Utils.null2Int(rpms);
					receivepmsetting = receivepmsetting | tmp;
				}
			}
			user.setNewsletter(receivepmsetting);

			userManager.updateUserInfo(user);

			ForumUtils.writeCookie("pmsound", user.getPmsound().toString());

			reqcfg.setUrl("usercppmset.action").setMetaRefresh().setShowBackLink(true).addMsgLine("短消息设置已成功更新");
		}
		return SUCCESS;
	}

	public Users getUser() {
		return user;
	}

	public int getReceivepmsetting() {
		return receivepmsetting;
	}
}
