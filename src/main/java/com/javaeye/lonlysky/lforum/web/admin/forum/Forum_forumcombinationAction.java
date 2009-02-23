package com.javaeye.lonlysky.lforum.web.admin.forum;

import java.util.List;

import org.apache.struts2.config.ParentPackage;
import org.springframework.beans.factory.annotation.Autowired;

import com.javaeye.lonlysky.lforum.AdminBaseAction;
import com.javaeye.lonlysky.lforum.comm.LForumRequest;
import com.javaeye.lonlysky.lforum.service.admin.AdminForumManager;

/**
 * 论坛版块合并
 * 
 * @author 黄磊
 *
 */
@ParentPackage("default")
public class Forum_forumcombinationAction extends AdminBaseAction {

	private static final long serialVersionUID = 5524150349801336756L;

	private String sourceforum;
	private String targetforum;

	@Autowired
	private AdminForumManager adminForumManager;

	@Override
	public String execute() throws Exception {
		List<Object[]> objList = adminForumManager.getForumTree();
		for (Object[] objects : objList) {
			sourceforum += "<option value=\"" + objects[0] + "\">" + objects[1] + "</option>";
			targetforum += "<option value=\"" + objects[0] + "\">" + objects[1] + "</option>";
		}
		if (LForumRequest.isPost()) { //如果提交表单
			int sourceforumid = LForumRequest.getParamIntValue("sourceforumid", 0);
			int targetforumid = LForumRequest.getParamIntValue("targetforumid", 0);
			if (sourceforumid == 0) {
				registerStartupScript("", "<script>alert('请选择相应的源论坛!');</script>");
				return SUCCESS;
			}

			if (targetforumid == 0) {
				registerStartupScript("", "<script>alert('请选择相应的目标论坛!');</script>");
				return SUCCESS;
			}

			if (adminForumManager.getTopForum(targetforumid) != -1) {
				registerStartupScript("", "<script>alert('您所选择的目标论坛是\"论坛分类\"而不是\"论坛版块\",因此合并无效!');</script>");
				return SUCCESS;
			}

			String result;
			if (!adminForumManager.combinationForums(sourceforumid, targetforumid)) {
				result = "<script>alert('当前节点下面有子结点,因此合并无效!');window.location.href='forum_forumcombination.action';</script>";
				registerStartupScript("", result);
				return SUCCESS;
			} else {
				adminVistLogManager.insertLog(user, username, usergroup, grouptitle, ip, "合并论坛版块", "合并论坛版块"
						+ sourceforumid + "到" + targetforumid);

				registerStartupScript("PAGE", "window.location.href='forum_forumstree.action';");
				return SUCCESS;
			}

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
