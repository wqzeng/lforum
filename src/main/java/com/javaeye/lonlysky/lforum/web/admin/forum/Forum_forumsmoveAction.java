package com.javaeye.lonlysky.lforum.web.admin.forum;

import java.util.List;

import org.apache.struts2.config.ParentPackage;
import org.springframework.beans.factory.annotation.Autowired;

import com.javaeye.lonlysky.lforum.AdminBaseAction;
import com.javaeye.lonlysky.lforum.comm.LForumRequest;
import com.javaeye.lonlysky.lforum.comm.utils.Utils;
import com.javaeye.lonlysky.lforum.service.admin.AdminForumManager;

/**
 * 版块移动
 * 
 * @author 黄磊
 *
 */
@ParentPackage("default")
public class Forum_forumsmoveAction extends AdminBaseAction {

	private static final long serialVersionUID = -1051452691038942477L;

	private String sourceforum;
	private String targetforum;

	@Autowired
	private AdminForumManager adminForumManager;

	@Override
	public String execute() throws Exception {
		if (LForumRequest.getParamValue("currentfid").equals("")) {
			registerStartupScript("", "<script>window.location.href='forum_forumstree.action';</script>");
			return SUCCESS;
		} else {
			int currentfid = LForumRequest.getParamIntValue("currentfid", 0);
			List<Object[]> objList = adminForumManager.getForumTree();
			for (Object[] objects : objList) {
				if (currentfid == Utils.null2Int(objects[0])) {
					sourceforum += "<option value=\"" + objects[0] + "\" selected=\"selected\">" + objects[1]
							+ "</option>";
				} else {
					sourceforum += "<option value=\"" + objects[0] + "\">" + objects[1] + "</option>";
				}
				targetforum += "<option value=\"" + objects[0] + "\">" + objects[1] + "</option>";
			}
		}
		if (LForumRequest.isPost()) { //如果提交表单
			int sourceforumid = LForumRequest.getParamIntValue("sourceforumid", 0);
			int targetforumid = LForumRequest.getParamIntValue("targetforumid", 0);
			if (sourceforumid == targetforumid) {
				registerStartupScript("", "<script>alert('您所要移动的版块与目标版块相同, 因此无法提交!');</script>");
				return SUCCESS;
			}

			boolean aschild = LForumRequest.getParamIntValue("movetype", 0) == 1 ? true : false;
			if (!adminForumManager.movingForumsPos(sourceforumid, targetforumid, aschild)) {
				registerStartupScript("", "<script>alert('当前源版块移动失败!');</script>");
				return SUCCESS;
			}

			adminVistLogManager.insertLog(user, username, usergroup, grouptitle, ip, "移动论坛版块", "移动论坛版块ID:"
					+ sourceforumid + "到ID:" + targetforumid);
			registerStartupScript("PAGE", "window.location.href='forum_forumstree.action';");
		}
		return SUCCESS;
	}

	public String getSourceforum() {
		return sourceforum;
	}

	public String getTargetforum() {
		return targetforum;
	}

}
