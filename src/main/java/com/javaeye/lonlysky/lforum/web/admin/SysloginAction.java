package com.javaeye.lonlysky.lforum.web.admin;

import java.io.IOException;
import java.util.Date;

import org.apache.commons.lang.time.DateUtils;
import org.apache.struts2.config.ParentPackage;
import org.springframework.beans.factory.annotation.Autowired;

import com.javaeye.lonlysky.lforum.BaseAction;
import com.javaeye.lonlysky.lforum.comm.LForumRequest;
import com.javaeye.lonlysky.lforum.comm.MD5;
import com.javaeye.lonlysky.lforum.comm.utils.ForumUtils;
import com.javaeye.lonlysky.lforum.comm.utils.Utils;
import com.javaeye.lonlysky.lforum.entity.forum.Online;
import com.javaeye.lonlysky.lforum.entity.forum.Usergroups;
import com.javaeye.lonlysky.lforum.entity.forum.Users;
import com.javaeye.lonlysky.lforum.entity.global.Config;
import com.javaeye.lonlysky.lforum.service.OnlineUserManager;
import com.javaeye.lonlysky.lforum.service.UserGroupManager;
import com.javaeye.lonlysky.lforum.service.UserManager;
import com.javaeye.lonlysky.lforum.service.admin.AdminVistLogManager;

/**
 * 后台管理登录
 * 
 * @author 黄磊
 *
 */
@ParentPackage("default")
public class SysloginAction extends BaseAction {

	private static final long serialVersionUID = 2171433792871211012L;

	/**
	 * 当前登陆用户的在线ID
	 */
	private int olid;

	/**
	 * 页面尾部信息
	 */
	private String footer = "";

	private String name = null;

	private String msg = "";

	@Autowired
	private OnlineUserManager onlineUserManager;

	@Autowired
	private UserGroupManager userGroupManager;

	@Autowired
	private UserManager userManager;

	@Autowired
	private AdminVistLogManager adminVistLogManager;

	@Override
	public String execute() throws Exception {
		footer = "<div align=\"center\" style=\" padding-top:60px;font-size:11px; font-family: Arial\">";
		footer += "<hr style=\"height:1; width:600; height:1; color:#CCCCCC\" />Powered by ";
		footer += "<a style=\"COLOR: #000000\" href=\"http://lonlysk.javaeye.com\" target=\"_blank\">LForum For Java";
		footer += "</a> &nbsp;&copy; 2001-";
		footer += Utils.dateFormat(new Date(), "yyyy");
		footer += ", <a style=\"COLOR: #000000;font-weight:bold\" href=\"http://lonlysk.javaeye.com\" target=\"_blank\">Lonlysky Blog.</a></div>";

		Online oluserinfo = onlineUserManager.updateInfo(config.getPasswordkey(), config.getOnlinetimeout());

		olid = oluserinfo.getOlid();

		if (!LForumRequest.isPost()) {
			if (!config.getAdminipaccess().trim().equals("")) {
				String[] regctrl = config.getAdminipaccess().trim().split("\n");
				if (!Utils.inIPArray(LForumRequest.getIp(), regctrl)) {
					StringBuilder sb = new StringBuilder();
					sb
							.append("<br /><br /><div style=\"width:100%\" align=\"center\"><div align=\"center\" style=\"width:600px; border:1px dotted #FF6600; background-color:#FFFCEC; margin:auto; padding:20px;\">");
					sb
							.append("<img src=\"images/hint.gif\" border=\"0\" alt=\"提示:\" align=\"absmiddle\" />&nbsp; 您的IP地址不在系统允许的范围之内</div></div>");
					renderHtml(sb.toString());
					return null;
				}
			}

			Usergroups usergroupinfo = userGroupManager.getUsergroup(oluserinfo.getUsergroups().getGroupid());
			if (oluserinfo.getUsers().getUid() <= 0 || usergroupinfo.getAdmingroups().getAdmingid() != 1) {
				String message = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">";
				message += "<html xmlns=\"http://www.w3.org/1999/xhtml\"><head><title>无法确认您的身份</title><meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\">";
				message += "<link href=\"styles/default.css\" type=\"text/css\" rel=\"stylesheet\"></head><script type=\"text/javascript\">if(top.location!=self.location){top.location.href = \"syslogin.aspx\";}</script><body><br /><br /><div style=\"width:100%\" align=\"center\">";
				message += "<div align=\"center\" style=\"width:600px; border:1px dotted #FF6600; background-color:#FFFCEC; margin:auto; padding:20px;\"><img src=\"images/hint.gif\" border=\"0\" alt=\"提示:\" align=\"absmiddle\" width=\"11\" height=\"13\" /> &nbsp;";
				message += "无法确认您的身份, 请<a href=\"../login.action\">登录</a></div></div></body></html>";
				renderHtml(message);
				return null;
			}

			// 显示相关页面登陆提交信息

			if (ForumUtils.getCookie("lforumkey").equals("")
					|| !ForumUtils
							.getCookiePassword(ForumUtils.getCookie("lforumkey"), config.getPasswordkey())
							.equals(
									(oluserinfo.getPassword()
											+ userManager.getUserInfo(oluserinfo.getUsers().getUid()).getSecques() + oluserinfo
											.getUsers().getUid()))) {
				msg = "<IMG alt=\"提示:\" src=\"images/warning.gif\" align=\"absMiddle\" border=\"0\" width=\"16\" height=\"16\">请重新进行管理员登录";
			}

			if (oluserinfo.getUsers().getUid() > 0 && usergroupinfo.getAdmingroups().getAdmingid() == 1
					&& !oluserinfo.getUsername().trim().equals("")) {
				name = oluserinfo.getUsername();
			}

			if (LForumRequest.getParamValue("result").equals("1")) {
				msg = "<IMG alt=\"提示:\" src=\"images/warning.gif\" align=\"absMiddle\" border=\"0\" width=\"16\" height=\"16\"><font color=\"red\">用户不存在或密码错误</font>";
				return SUCCESS;
			}

			if (LForumRequest.getParamValue("result").equals("2")) {
				msg = "<IMG alt=\"提示:\" src=\"images/warning.gif\" align=\"absMiddle\" border=\"0\" width=\"16\" height=\"16\"><font color=\"red\">用户不是管理员身分,因此无法登陆后台</font>";
				return SUCCESS;
			}

			if (LForumRequest.getParamValue("result").equals("3")) {
				msg = "<IMG alt=\"提示:\" src=\"images/warning.gif\" align=\"absMiddle\" border=\"0\" width=\"16\" height=\"16\"><font color=\"red\">验证码错误,请重新输入</font>";
				return SUCCESS;
			}

			if (LForumRequest.getParamValue("result").equals("4")) {
				return SUCCESS;
			}
		}

		if (LForumRequest.isPost()) {
			//对提供的信息进行验证
			verifyLoginInf();
		} else {
			response.sendRedirect("syslogin.action?result=4");
		}
		return SUCCESS;
	}

	/**
	 * 对提供的信息进行验证
	 * @throws IOException 
	 */
	private void verifyLoginInf() throws IOException {
		if (!onlineUserManager.checkUserVerifyCode(olid, LForumRequest.getParamValue("vcode"))) {
			response.sendRedirect("syslogin.action?result=3");
			return;
		}
		int uid = userManager.checkPassword(LForumRequest.getParamValue("UserName"), LForumRequest
				.getParamValue("PassWord"), true);
		Users userinfo = userManager.getUserInfo(uid);

		if (userinfo != null) {
			Usergroups usergroupinfo = userGroupManager.getUsergroup(userinfo.getUsergroups().getGroupid());

			if (usergroupinfo.getAdmingroups().getAdmingid() == 1) {
				ForumUtils.writeUserCookie(userinfo, 1440, config.getPasswordkey());

				String secques = userinfo.getSecques();
				String ip = LForumRequest.getIp();

				Usergroups tmpusergroupinfo = userGroupManager.getUsergroup(userinfo.getUsergroups().getGroupid());

				String grouptitle = tmpusergroupinfo.getGrouptitle();

				ForumUtils.writeCookie("lforumkey", ForumUtils.setCookiePassword(MD5.encode(LForumRequest
						.getParamValue("PassWord"))
						+ secques + userinfo.getUid(), config.getPasswordkey()), Utils.null2Int(DateUtils.addMinutes(
						new Date(), 30).getTime()));
				adminVistLogManager.insertLog(userinfo, userinfo.getUsername(), tmpusergroupinfo, grouptitle, ip,
						"后台管理员登陆", "");

				response.sendRedirect("index.action");
				return;
			} else {
				response.sendRedirect("syslogin.action?result=2");
				return;
			}
		} else {
			response.sendRedirect("syslogin.action?result=1");
			return;
		}
	}

	public int getOlid() {
		return olid;
	}

	public Config getConfig() {
		return config;
	}

	public String getFooter() {
		return footer;
	}

	public String getName() {
		return name;
	}

	public String getMsg() {
		return msg;
	}
}
