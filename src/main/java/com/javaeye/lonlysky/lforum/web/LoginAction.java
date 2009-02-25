package com.javaeye.lonlysky.lforum.web;

import org.apache.struts2.config.ParentPackage;
import org.springframework.beans.factory.annotation.Autowired;

import com.javaeye.lonlysky.lforum.ForumBaseAction;
import com.javaeye.lonlysky.lforum.comm.ForumAction;
import com.javaeye.lonlysky.lforum.comm.LForumRequest;
import com.javaeye.lonlysky.lforum.comm.utils.ForumUtils;
import com.javaeye.lonlysky.lforum.comm.utils.Utils;
import com.javaeye.lonlysky.lforum.comm.utils.des.DES;
import com.javaeye.lonlysky.lforum.entity.forum.Users;
import com.javaeye.lonlysky.lforum.service.LoginLogManager;

/**
 * 论坛登录
 * 
 * @author 黄磊 
 *
 */
@ParentPackage("default")
public class LoginAction extends ForumBaseAction {

	private static final long serialVersionUID = 1079279456637041488L;

	/**
	 * 登录所使用的用户名
	 */
	private String postusername;

	/**
	 * 登陆时的密码验证信息
	 */
	private String loginauth;

	/**
	 * 登陆时提交的密码
	 */
	private String postpassword;

	/**
	 * 登陆成功后跳转的链接
	 */
	private String referer;

	/**
	 * 是否跨页面提交
	 */
	private boolean loginsubmit;

	@Autowired
	private LoginLogManager loginLogManager;

	@Override
	public String execute() throws Exception {
		loginsubmit = LForumRequest.getParamValue("loginsubmit").equals("true") ? true : false;
		referer = LForumRequest.getParamValue("referer");
		loginauth = LForumRequest.getParamValue("loginauth");

		pagetitle = "用户登录";

		postusername = Utils.urlDecode(LForumRequest.getParamValue("postusername")).trim();

		// 判断用户是否已经登录
		if (userid != -1) {
			reqcfg.setUrl(config.getForumurl()).setMetaRefresh().setShowBackLink(false).addErrLine("您已经登录，无须重复登录");
			ispost = true;
			setLeftMenuRefresh();
		}

		if (loginLogManager.updateLoginLog(LForumRequest.getIp(), false) >= 5) {
			reqcfg.addErrLine("您已经多次输入密码错误, 请15分钟后再登录");
			loginsubmit = false;
			return SUCCESS;
		}

		// 未提交或跨页提交时
		if (!LForumRequest.isPost() || !referer.equals("")) {
			String r = "";
			if (!referer.equals("")) {
				r = referer;
			} else {
				if (LForumRequest.getUrlReferrer().equals("") || LForumRequest.getUrlReferrer().indexOf("login") > -1
						|| LForumRequest.getUrlReferrer().indexOf("logout") > -1) {
					r = "main.action";
				} else {
					r = LForumRequest.getUrlReferrer();
				}
			}
			ForumUtils.writeCookie("reurl", (LForumRequest.getParamValue("reurl").equals("") || LForumRequest
					.getParamValue("reurl").indexOf("login") > -1) ? r : LForumRequest.getParamValue("reurl"));
		}

		// 如果表单提交
		if (LForumRequest.isPost()) {
			reqcfg.setBackLink("login.action?postusername=" + LForumRequest.getParamValue("username"));

			//如果没输入验证码就要求用户填写
			if (isseccode && LForumRequest.getParamValue("vcode").equals("")) {
				postusername = LForumRequest.getParamValue("username");
				DES des = new DES(config.getPasswordkey());
				loginauth = des.getEncString(LForumRequest.getParamValue("password")).replace("+", "[");
				loginsubmit = true;
				return SUCCESS;
			}

			if (!userManager.exists(LForumRequest.getParamValue("username"))) {
				reqcfg.addErrLine("用户不存在");
			}
			if (LForumRequest.getParamValue("password").equals("")
					&& LForumRequest.getParamValue("loginauth").equals("")) {
				reqcfg.addErrLine("密码不能为空");
			}
			if (reqcfg.isErr()) {
				return SUCCESS;
			}
			if (!loginauth.equals("")) {
				DES des = new DES(config.getPasswordkey());
				postpassword = des.getDesString(loginauth.replace("[", "+"));
			} else {
				postpassword = LForumRequest.getParamValue("password");
			}

			if (postusername.equals("")) {
				postusername = LForumRequest.getParamValue("username");
			}
			int uid = -1;

			// 检测用户密码
			if (config.getSecques() == 1 && (!loginauth.equals("") || !loginsubmit)) {
				uid = userManager.checkPasswordAndSecques(postusername, postpassword, true, LForumRequest
						.getParamIntValue("question", 0), LForumRequest.getParamValue("answer"));
			} else {
				uid = userManager.checkPassword(postusername, postpassword, true);
			}
			if (uid != -1) {
				Users userinfo = userManager.getUserInfo(uid);
				if (userinfo.getUsergroups().getGroupid() == 8) {
					reqcfg.addErrLine("抱歉, 您的用户身份尚未得到验证");
					if (config.getRegverify() == 1) {
						reqcfg.addErrLine("请您到您的邮箱中点击激活链接来激活您的帐号");
					}
					if (config.getRegverify() == 2) {
						reqcfg.addErrLine("您需要等待一些时间, 待系统管理员审核您的帐户后才可登录使用");
					}
					loginsubmit = false;
				} else {
					if (!userinfo.getSecques().trim().equals("") && loginsubmit
							&& LForumRequest.getParamValue("loginauth").equals("")) {
						DES des = new DES(config.getPasswordkey());
						loginauth = des.getEncString(LForumRequest.getParamValue("password")).replace("+", "[");
					} else {
						loginLogManager.deleteLoginLog(LForumRequest.getIp());

						// 此处更新积分

						// 写用户cookie
						ForumUtils.writeUserCookie(userinfo, LForumRequest.getParamIntValue("expires", 0), config
								.getPasswordkey(), LForumRequest.getParamIntValue("templateid", 0), LForumRequest
								.getParamIntValue("loginmode", -1));
						// 更新动作
						onlineUserManager.updateAction(olid, ForumAction.Login.ACTION_ID, 0);
						// 更新在线信息
						onlineUserManager.updateInfo(config.getPasswordkey(), config.getOnlinetimeout());
						olid = oluserinfo.getOlid();
						// 更新用户最后登录时间
						userManager.updateUserLastvisit(uid, LForumRequest.getIp());

						String reurl = Utils.urlDecode(ForumUtils.getReUrl());
						if (reurl.indexOf("register.action") < 0) {
							reqcfg.setUrl(reurl);
						} else {
							reqcfg.setUrl("main.action");
						}

						reqcfg.addMsgLine("登录成功, 返回登录前页面");
						username = LForumRequest.getParamValue("username");
						userid = uid;
						usergroupinfo = userGroupManager.getUsergroup(userinfo.getUsergroups().getGroupid());
						useradminid = usergroupinfo.getAdmingroups().getAdmingid();

						reqcfg.setMetaRefresh().setShowBackLink(false);
						setLeftMenuRefresh();
						loginsubmit = false;
					}
				}
			} else {
				int errcount = loginLogManager.updateLoginLog(LForumRequest.getIp(), true);
				if (errcount > 5) {
					reqcfg.addErrLine("您已经输入密码5次错误, 请15分钟后再试");
				} else {
					reqcfg.addErrLine("密码或安全提问第" + errcount + "次错误, 您最多有5次机会重试");
				}
			}
		}

		return SUCCESS;
	}

	/**
	 * 刷新左边菜单栏
	 */
	private void setLeftMenuRefresh() {
		StringBuilder script = new StringBuilder();
		script.append("if (top.document.getElementById('leftmenu')){");
		script.append("		top.frames['leftmenu'].location.reload();");
		script.append("}");
		reqcfg.addScript(script.toString());
	}

	public String getPostusername() {
		return postusername;
	}

	public void setPostusername(String postusername) {
		this.postusername = postusername;
	}

	public String getLoginauth() {
		return loginauth;
	}

	public void setLoginauth(String loginauth) {
		this.loginauth = loginauth;
	}

	public String getPostpassword() {
		return postpassword;
	}

	public void setPostpassword(String postpassword) {
		this.postpassword = postpassword;
	}

	public String getReferer() {
		return referer;
	}

	public void setReferer(String referer) {
		this.referer = referer;
	}

	public boolean getLoginsubmit() {
		return loginsubmit;
	}
}
