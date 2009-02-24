package com.javaeye.lonlysky.lforum.web.admin.forum;

import org.apache.struts2.config.ParentPackage;
import org.springframework.beans.factory.annotation.Autowired;

import com.javaeye.lonlysky.lforum.AdminBaseAction;
import com.javaeye.lonlysky.lforum.comm.LForumRequest;
import com.javaeye.lonlysky.lforum.entity.forum.Forums;
import com.javaeye.lonlysky.lforum.service.ForumManager;
import com.javaeye.lonlysky.lforum.service.admin.AdminCacheManager;
import com.javaeye.lonlysky.lforum.service.admin.AdminForumManager;
import com.javaeye.lonlysky.lforum.service.admin.AdminStatsManager;

/**
 * 更新论坛统计
 * 
 * @author 黄磊
 *
 */
@ParentPackage("default")
public class Forum_updateforumstaticAction extends AdminBaseAction {

	private static final long serialVersionUID = 7872660851772237398L;

	@Autowired
	private AdminStatsManager adminStatsManager;

	@Autowired
	private AdminForumManager adminForumManager;

	@Autowired
	private ForumManager forumManager;

	@Override
	public String execute() throws Exception {
		if (LForumRequest.isPost()) {
			String submitMethod = LForumRequest.getParamValue("submitMethod");
			if (!submitMethod.equals("")) {
				submitMethod = submitMethod.substring(submitMethod.indexOf(":") + 1);
				if (submitMethod.equals("clearFlag")) {
					clearFlag();
				} else if (submitMethod.equals("reSetStatistic")) {
					reSetStatistic();
				} else if (submitMethod.equals("systeAutoSet")) {
					systeAutoSet();
				} else if (submitMethod.equals("updateCurTopics")) {
					updateCurTopics();
				} else if (submitMethod.equals("updateForumLastPost")) {
					updateForumLastPost();
				}
			}
		}
		return SUCCESS;
	}

	/**
	 * 更新所有版块最后发帖
	 */
	private void updateForumLastPost() {
		for (Forums foruminfo : forumManager.getForumList()) {
			forumManager.updateLastPost(foruminfo);
		}

	}

	/**
	 * 更新所有版块的当前帖数
	 */
	private void updateCurTopics() {
		for (int id : forumManager.getForumIdList()) {
			forumManager.setRealCurrentTopics(id);
		}
	}

	/**
	 * 系统调整论坛版块
	 */
	private void systeAutoSet() {
		adminForumManager.setForumslayer();
		adminForumManager.setForumsSubForumCountAndDispalyorder();
		adminForumManager.setForumsPathList();
		//adminForumManager.setForumsStatus();
		AdminCacheManager.reSetForumLinkList();
		AdminCacheManager.reSetForumList();
		AdminCacheManager.reSetForumListBoxOptions();
		registerStartupScript("", "<script language=javascript>clearflag();</script>");
	}

	/**
	 * 重建论坛统计数据
	 */
	private void reSetStatistic() {
		adminStatsManager.reSetStatistic();
		AdminCacheManager.reSetStatistics();
		registerStartupScript("", "<script language=javascript>clearflag();</script>");
	}

	/**
	 * 清理移动标记
	 */
	private void clearFlag() {
		adminStatsManager.reSetClearMove();
		registerStartupScript("", "<script language=javascript>clearflag();</script>");
	}

}
