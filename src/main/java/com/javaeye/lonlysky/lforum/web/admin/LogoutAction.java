package com.javaeye.lonlysky.lforum.web.admin;

import org.apache.struts2.config.ParentPackage;
import org.springframework.beans.factory.annotation.Autowired;

import com.javaeye.lonlysky.lforum.BaseAction;
import com.javaeye.lonlysky.lforum.comm.utils.ForumUtils;
import com.javaeye.lonlysky.lforum.entity.forum.Online;
import com.javaeye.lonlysky.lforum.service.OnlineUserManager;
import com.javaeye.lonlysky.lforum.service.admin.GroupManager;

/**
 * 退出管理登录
 * 
 * @author 黄磊
 *
 */
@ParentPackage("default")
public class LogoutAction extends BaseAction {

	private static final long serialVersionUID = 5415592989240544311L;

	@Autowired
	private OnlineUserManager onlineUserManager;

	@Autowired
	private GroupManager groupManager;

	@Override
	public String execute() throws Exception {
		//获取当前用户的在线信息
		Online oluserinfo = onlineUserManager.updateInfo(config.getPasswordkey(), config.getOnlinetimeout());
		if (groupManager.adminGetUserGroupInfo(oluserinfo.getUsergroups().getGroupid()).getAdmingroups().getAdmingid() != 1) {
			response.sendRedirect("../");
			return null;
		}
		int olid = oluserinfo.getOlid();
		onlineUserManager.deleteRows(olid);

		//清除Cookie
		ForumUtils.clearUserCookie();
		return SUCCESS;
	}
}
