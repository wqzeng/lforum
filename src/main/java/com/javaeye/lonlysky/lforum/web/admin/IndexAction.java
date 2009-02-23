package com.javaeye.lonlysky.lforum.web.admin;

import java.util.Date;

import org.apache.commons.lang.time.DateUtils;
import org.apache.struts2.config.ParentPackage;
import org.springframework.beans.factory.annotation.Autowired;

import com.javaeye.lonlysky.lforum.BaseAction;
import com.javaeye.lonlysky.lforum.comm.LForumRequest;
import com.javaeye.lonlysky.lforum.comm.utils.ForumUtils;
import com.javaeye.lonlysky.lforum.comm.utils.Utils;
import com.javaeye.lonlysky.lforum.entity.forum.Online;
import com.javaeye.lonlysky.lforum.entity.forum.Usergroups;
import com.javaeye.lonlysky.lforum.entity.global.Config;
import com.javaeye.lonlysky.lforum.service.OnlineUserManager;
import com.javaeye.lonlysky.lforum.service.UserManager;
import com.javaeye.lonlysky.lforum.service.admin.GroupManager;

/**
 * 后台首页
 * 
 * @author 黄磊
 *
 */
@ParentPackage("default")
public class IndexAction extends BaseAction {

	private static final long serialVersionUID = 6645730033205913468L;

	private int olid;

	@Autowired
	private OnlineUserManager onlineUserManager;

	@Autowired
	private GroupManager groupManager;

	@Autowired
	private UserManager userManager;

	@Override
	public String execute() throws Exception {

		// 如果IP访问列表有设置则进行判断
		if (!config.getAdminipaccess().trim().equals("")) {
			String[] regctrl = config.getAdminipaccess().trim().split("\n");
			if (!Utils.inIPArray(LForumRequest.getIp(), regctrl)) {
				response.sendRedirect("syslogin.action");
				return null;
			}
		}

		//获取当前用户的在线信息
		Online oluserinfo = onlineUserManager.updateInfo(config.getPasswordkey(), config.getOnlinetimeout());
		olid = oluserinfo.getOlid();

		Usergroups usergroupinfo = groupManager.adminGetUserGroupInfo(oluserinfo.getUsergroups().getGroupid());
		if (oluserinfo.getUsers().getUid() <= 0 || usergroupinfo.getAdmingroups().getAdmingid() != 1) {
			response.sendRedirect("syslogin.action");
			return null;
		}

		String secques = userManager.getUserInfo(oluserinfo.getUsers().getUid()).getSecques();
		// 管理员身份验证
		if (ForumUtils.getCookie("lforumkey").equals("")
				|| !ForumUtils.getCookiePassword(ForumUtils.getCookie("lforumkey"), config.getPasswordkey()).equals(
						(oluserinfo.getPassword() + secques + oluserinfo.getUsers().getUid()))) {
			response.sendRedirect("syslogin.action");
			return null;
		} else {
			ForumUtils.writeCookie("lforumkey", ForumUtils.setCookiePassword(oluserinfo.getPassword() + secques
					+ oluserinfo.getUsers().getUid(), config.getPasswordkey()), Utils.null2Int(DateUtils.addMinutes(
					new Date(), 30).getTime()));
		}
		return SUCCESS;
	}

	public Config getConfig() {
		return config;
	}

	public int getOlid() {
		return olid;
	}

}
