package com.javaeye.lonlysky.lforum.web;

import org.apache.struts2.config.ParentPackage;
import org.springframework.beans.factory.annotation.Autowired;

import com.javaeye.lonlysky.lforum.ForumBaseAction;
import com.javaeye.lonlysky.lforum.comm.ForumAction;
import com.javaeye.lonlysky.lforum.comm.LForumRequest;
import com.javaeye.lonlysky.lforum.comm.utils.ForumUtils;
import com.javaeye.lonlysky.lforum.entity.forum.Users;
import com.javaeye.lonlysky.lforum.service.UserCreditManager;

/**
 * 激活用户页面
 * 
 * @author 黄磊
 *
 */
@ParentPackage("default")
public class ActivationuserAction extends ForumBaseAction {

	private static final long serialVersionUID = 2610306392734314186L;

	@Autowired
	private UserCreditManager userCreditManager;

	@Override
	public String execute() throws Exception {
		pagetitle = "用户帐号激活";

		reqcfg.setUrl("main.action").setMetaRefresh().setShowBackLink(false);
		String authstr = LForumRequest.getParamValue("authstr").trim().replace("'", "''");

		if (authstr != null && authstr != "") {
			int uid = userManager.getUserIdByAuthStr(authstr);
			if (uid != -1) {
				Users users = userManager.getUserInfo(uid);
				//将用户调整到相应的用户组
				if (userCreditManager.getCreditsUserGroupID(0) != null) {
					//添加注册用户审核机制后需要修改
					users.setUsergroups(userCreditManager.getCreditsUserGroupID(0));
				}

				//更新激活字段
				users.getUserfields().setAuthstr("");
				users.getUserfields().setAuthflag(0);
				userManager.updateUserInfo(users);
				reqcfg.addMsgLine("您当前的帐号已经激活,稍后您将以相应身份返回首页");

				ForumUtils.writeUserCookie(users, LForumRequest.getParamIntValue("expires", -1), config
						.getPasswordkey());
				onlineUserManager
						.updateAction(olid, ForumAction.ActivationUser.ACTION_ID, 0, config.getOnlinetimeout());

			} else {
				reqcfg.addMsgLine("您当前的激活链接无效,稍后您将以游客身份返回首页");
				onlineUserManager.deleteRows(olid);
				ForumUtils.clearUserCookie();
			}
		} else {
			reqcfg.addMsgLine("您当前的激活链接无效,稍后您将以游客身份返回首页");
			onlineUserManager.deleteRows(olid);
			ForumUtils.clearUserCookie();
		}
		return SUCCESS;
	}

}
