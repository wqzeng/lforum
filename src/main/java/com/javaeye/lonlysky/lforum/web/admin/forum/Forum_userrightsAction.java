package com.javaeye.lonlysky.lforum.web.admin.forum;

import java.util.List;

import org.apache.struts2.config.ParentPackage;
import org.springframework.beans.factory.annotation.Autowired;

import com.javaeye.lonlysky.lforum.AdminBaseAction;
import com.javaeye.lonlysky.lforum.comm.LForumRequest;
import com.javaeye.lonlysky.lforum.comm.utils.Utils;
import com.javaeye.lonlysky.lforum.config.impl.ConfigLoader;
import com.javaeye.lonlysky.lforum.entity.forum.Usergroups;
import com.javaeye.lonlysky.lforum.service.UserGroupManager;

/**
 * 用户权限设置(全局)
 * 
 * @author 黄磊
 *
 */
@ParentPackage("default")
public class Forum_userrightsAction extends AdminBaseAction {

	private static final long serialVersionUID = -3649647955984209507L;

	private String userGroupTab = "";

	@Autowired
	private UserGroupManager userGroupManager;

	@Override
	public String execute() throws Exception {
		List<Usergroups> groupList = userGroupManager.getUserGroupList();
		int i = 0;
		for (Usergroups group : groupList) {
			if (i == 0) {
				userGroupTab += "<tr>";
			}
			userGroupTab += "<td><input value=\"" + group.getGroupid() + "\" type=\"checkbox\" name=\"userGroup\"";
			for (String groupid : config.getHtmltitleusergroup().split(",")) {
				if (Utils.null2Int(groupid) == group.getGroupid()) {
					userGroupTab += " checked=\"checked\"";
					break;
				}
			}
			userGroupTab += " /><label\">" + group.getGrouptitle() + "</label></td>";
			if (i == 1) {
				userGroupTab += "</tr>";
				i = 0;
			} else {
				i++;
			}
		}

		// 如果提交
		if (LForumRequest.isPost()) {
			int minpostsize = LForumRequest.getParamIntValue("minpostsize", config.getMinpostsize());
			int maxpostsize = LForumRequest.getParamIntValue("maxpostsize", config.getMaxpostsize());
			int maxfavorites = LForumRequest.getParamIntValue("maxfavorites", config.getMaxfavorites());
			int maxavatarsize = LForumRequest.getParamIntValue("maxavatarsize", config.getMaxavatarsize());
			int maxavatarwidth = LForumRequest.getParamIntValue("maxavatarwidth", config.getMaxavatarwidth());
			int maxavatarheight = LForumRequest.getParamIntValue("maxavatarheight", config.getMaxavatarheight());
			int maxpolloptions = LForumRequest.getParamIntValue("maxpolloptions", config.getMaxpolloptions());
			int maxattachments = LForumRequest.getParamIntValue("maxattachments", config.getMaxattachments());
			int karmaratelimit = LForumRequest.getParamIntValue("karmaratelimit", config.getKarmaratelimit());

			if (minpostsize > 9999999 || (minpostsize < 0)) {
				registerStartupScript("",
						"<script>alert('帖子最小字数只能在0-9999999之间');window.location.href='forum_userrights.action';</script>");
				return SUCCESS;
			}

			if (maxpostsize > 9999999 || (maxpostsize < 0)) {
				registerStartupScript("",
						"<script>alert('帖子最大字数只能在0-9999999之间');window.location.href='forum_userrights.action';</script>");
				return SUCCESS;
			}

			if (maxfavorites > 9999999 || (maxfavorites < 0)) {
				registerStartupScript("",
						"<script>alert('收藏夹容量只能在0-9999999之间');window.location.href='forum_userrights.action';</script>");
				return SUCCESS;
			}

			if (maxavatarsize > 9999999 || (maxavatarsize < 0)) {
				registerStartupScript("",
						"<script>alert('头像最大尺寸只能在0-9999999之间');window.location.href='forum_userrights.action';</script>");
				return SUCCESS;
			}

			if (maxavatarwidth > 9999999 || (maxavatarwidth < 0)) {
				registerStartupScript("",
						"<script>alert('头像最大宽度只能在165-9999999之间');window.location.href='forum_userrights.action';</script>");
				return SUCCESS;
			}

			if (maxavatarheight > 9999999 || (maxavatarheight < 0)) {
				registerStartupScript("",
						"<script>alert('头像最大高度只能在0-9999999之间');window.location.href='forum_userrights.action';</script>");
				return SUCCESS;
			}

			if (maxpolloptions > 9999999 || (maxpolloptions < 0)) {
				registerStartupScript("",
						"<script>alert('最多投票项只能在0-9999999之间');window.location.href='forum_userrights.action';</script>");
				return SUCCESS;
			}

			if (maxattachments > 9999999 || (maxattachments < 0)) {
				registerStartupScript("",
						"<script>alert('最多附件数只能在0-9999999之间');window.location.href='forum_userrights.action';</script>");
				return SUCCESS;
			}
			if (karmaratelimit > 9999 || (karmaratelimit < 0)) {
				registerStartupScript("",
						"<script>alert('评分时间限制只能在0-9999之间');window.location.href='forum_userrights.action';</script>");
				return SUCCESS;
			}

			config.setDupkarmarate(LForumRequest.getParamIntValue("dupkarmarate", config.getDupkarmarate()));
			config.setMinpostsize(minpostsize);
			config.setMaxpostsize(maxpostsize);
			config.setMaxfavorites(maxfavorites);
			config.setMaxavatarsize(maxavatarsize);
			config.setMaxavatarwidth(maxavatarwidth);
			config.setMaxavatarheight(maxavatarheight);
			config.setMaxpolloptions(maxpolloptions);
			config.setMaxattachments(maxattachments);
			config.setKarmaratelimit(karmaratelimit);
			config.setModeractions(LForumRequest.getParamIntValue("moderactions", config.getModeractions()));
			config.setHtmltitleusergroup(LForumRequest.getParamValues("userGroup", ","));

			ConfigLoader configLoader = new ConfigLoader(config.getWebpath());
			configLoader.saveConfig(config);

			adminVistLogManager.insertLog(user, username, usergroup, grouptitle, ip, "用户权限设置", "");
			registerStartupScript("PAGE", "window.location.href='forum_userrights.action';");
		}
		return SUCCESS;
	}

	public String getUserGroupTab() {
		return userGroupTab;
	}
}
