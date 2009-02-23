package com.javaeye.lonlysky.lforum.web;

import org.apache.struts2.config.ParentPackage;

import com.javaeye.lonlysky.lforum.ForumBaseAction;
import com.javaeye.lonlysky.lforum.comm.LForumRequest;
import com.javaeye.lonlysky.lforum.comm.MD5;
import com.javaeye.lonlysky.lforum.comm.utils.ForumUtils;
import com.javaeye.lonlysky.lforum.entity.forum.Users;

/**
 * 重置密码页面
 * 
 * @author 黄磊
 *
 */
@ParentPackage("default")
public class UsercpnewpasswordAction extends ForumBaseAction {

	private static final long serialVersionUID = -3721028673169072551L;

	/**
	 * 当前用户信息
	 */
	private Users user = new Users();

	@Override
	public String execute() throws Exception {
		pagetitle = "用户控制面板";

		if (userid == -1) {
			reqcfg.addErrLine("你尚未登录");
			return SUCCESS;
		}
		user = userManager.getUserInfo(userid);

		if (LForumRequest.isPost()) {
			String oldpassword = LForumRequest.getParamValue("oldpassword");
			String newpassword = LForumRequest.getParamValue("newpassword");
			String newpassword2 = LForumRequest.getParamValue("newpassword2");

			if (userManager.checkPassword(userid, oldpassword, true) == -1) {
				reqcfg.addErrLine("你的原密码错误");
			}
			if (!newpassword.equals(newpassword2)) {
				reqcfg.addErrLine("新密码两次输入不一致");
			}

			if (newpassword.equals("")) {
				newpassword = oldpassword;
			}

			if (newpassword.length() < 6) {
				reqcfg.addErrLine("密码不得少于6个字符");
			}

			if (reqcfg.isErr()) {
				return SUCCESS;
			} else {
				//判断是否需要修改安全提问
				userManager.updateUserPassword(userid, newpassword);
				if (LForumRequest.getParamValue("changesecques") != "") {
					userManager.updateUserSecques(userid, LForumRequest.getParamIntValue("question", 0), LForumRequest
							.getParamValue("answer"));
				}
				ForumUtils.writeCookie("password", ForumUtils.setCookiePassword(MD5.encode(newpassword), config
						.getPasswordkey()));
				onlineUserManager.updatePassword(olid, MD5.encode(newpassword));

				reqcfg.setUrl("usercpnewpassword.action").setMetaRefresh().setShowBackLink(true).addMsgLine(
						"修改密码完毕, 同时已经更新了您的登录信息");
			}
		}
		return SUCCESS;
	}

	public Users getUser() {
		return user;
	}

}
