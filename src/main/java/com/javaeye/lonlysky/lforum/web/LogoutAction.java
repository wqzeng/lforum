package com.javaeye.lonlysky.lforum.web;

import org.apache.struts2.config.ParentPackage;

import com.javaeye.lonlysky.lforum.ForumBaseAction;
import com.javaeye.lonlysky.lforum.comm.LForumRequest;
import com.javaeye.lonlysky.lforum.comm.utils.ForumUtils;
import com.javaeye.lonlysky.lforum.comm.utils.Utils;

/**
 * 退出登录页面
 * 
 * @author 黄磊
 *
 */
@ParentPackage("default")
public class LogoutAction extends ForumBaseAction {

	private static final long serialVersionUID = -9220014845583959329L;

	@Override
	public String execute() throws Exception {
		pagetitle = "用户退出";
		username = "游客";
		userid = -1;

		// 用于刷新分栏菜单
		StringBuilder script = new StringBuilder();
		script.append("if (top.document.getElementById('leftmenu')){");
		script.append("		top.frames['leftmenu'].location.reload();");
		script.append("}");
		reqcfg.addScript(script.toString());

		// 设置回退页面
		String referer = LForumRequest.getParamValue("reurl");
		if (!LForumRequest.isPost() || referer != "") {
			String r = "";
			if (referer != "") {
				r = referer;
			} else {
				if ((LForumRequest.getUrlReferrer() == "") || (LForumRequest.getUrlReferrer().indexOf("login") > -1)
						|| LForumRequest.getUrlReferrer().indexOf("logout") > -1) {
					r = "main.action";
				} else {
					r = LForumRequest.getUrlReferrer();
				}
			}
			ForumUtils.writeCookie("reurl", (referer == "" || referer.indexOf("login.action") > -1) ? r : referer);
		}
		reqcfg.setUrl(Utils.urlDecode(ForumUtils.getReUrl())).setMetaRefresh().setShowBackLink(false);

		if (LForumRequest.getParamValue("userkey").equals(userkey)) {
			reqcfg.addMsgLine("已经清除了您的登录信息, 稍后您将以游客身份返回首页");

			onlineUserManager.deleteRows(olid);
			ForumUtils.clearUserCookie();
			ForumUtils.writeCookie("templateid", "", -999999);

		} else {
			reqcfg.addMsgLine("无法确定您的身份, 稍后返回首页");
		}
		return SUCCESS;
	}
}
