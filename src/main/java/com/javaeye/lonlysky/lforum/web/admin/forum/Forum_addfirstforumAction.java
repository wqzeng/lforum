package com.javaeye.lonlysky.lforum.web.admin.forum;

import java.util.List;

import org.apache.struts2.config.ParentPackage;
import org.springframework.beans.factory.annotation.Autowired;

import com.javaeye.lonlysky.lforum.AdminBaseAction;
import com.javaeye.lonlysky.lforum.comm.LForumRequest;
import com.javaeye.lonlysky.lforum.entity.forum.Attachtypes;
import com.javaeye.lonlysky.lforum.entity.forum.Forumfields;
import com.javaeye.lonlysky.lforum.entity.forum.Forums;
import com.javaeye.lonlysky.lforum.entity.forum.Topics;
import com.javaeye.lonlysky.lforum.entity.forum.Usergroups;
import com.javaeye.lonlysky.lforum.entity.forum.Users;
import com.javaeye.lonlysky.lforum.service.AttachmentManager;
import com.javaeye.lonlysky.lforum.service.ForumManager;
import com.javaeye.lonlysky.lforum.service.UserGroupManager;
import com.javaeye.lonlysky.lforum.service.admin.AdminForumManager;

/**
 * 添加第一个分类页
 * 说明: 当论坛版块表中没有记录时,则会运行该页面
 * 
 * @author 黄磊
 *
 */
@ParentPackage("default")
public class Forum_addfirstforumAction extends AdminBaseAction {

	private static final long serialVersionUID = 2293681661253334707L;

	private List<Usergroups> groupList;
	private List<Attachtypes> attachtypeList;
	private Forums foruminfo = new Forums();

	@Autowired
	private UserGroupManager userGroupManager;
	
	@Autowired
	private AttachmentManager attachmentManager;

	@Autowired
	private AdminForumManager adminForumManager;

	@Autowired
	private ForumManager forumManager;

	@Override
	public String execute() throws Exception {
		groupList = userGroupManager.getUserGroupList();
		attachtypeList = attachmentManager.getAttachmentType();
		if (LForumRequest.isPost()) { // 保存论坛信息
			if (LForumRequest.getParamValue("name").equals("")) {
				registerStartupScript("", "<script>alert('论坛名称不能为空');</script>");
				return SUCCESS;
			}
			int maxdisplayorder = forumManager.getForumsMaxDisplayOrder();
			Forums parentForum = new Forums();
			parentForum.setFid(0);
			insertForum(parentForum, 0, "0", 0, maxdisplayorder);
		}
		return SUCCESS;
	}

	/*
	 * 添加新论坛
	 */
	private void insertForum(Forums parentForum, int layer, String parentidlist, int subforumcount,
			int systemdisplayorder) {
		Forumfields forumfields = new Forumfields();
		forumfields.setApplytopictype(0);
		forumfields.setPostbytopictype(0);
		forumfields.setPermuserlist("");
		forumfields.setTopictypeprefix(0);
		forumfields.setViewbytopictype(0);
		foruminfo.setForumfields(forumfields);
		foruminfo.setForums(parentForum);
		foruminfo.setLayer(layer);
		foruminfo.setParentidlist(parentidlist);
		foruminfo.setSubforumcount(subforumcount);
		foruminfo.setName(LForumRequest.getParamValue("name"));

		foruminfo.setStatus(LForumRequest.getParamIntValue("status", 1));

		foruminfo.setDisplayorder(systemdisplayorder);

		foruminfo.setTemplateid(LForumRequest.getParamIntValue("templateid", config.getTemplateid()));
		foruminfo.setAllowsmilies(LForumRequest.getParamIntValue("Allowsmilies", 0));
		foruminfo.setAllowrss(LForumRequest.getParamIntValue("Allowrss", 0));
		foruminfo.setAllowhtml(0);
		foruminfo.setAllowbbcode(LForumRequest.getParamIntValue("Allowbbcode", 0));
		foruminfo.setAllowimgcode(LForumRequest.getParamIntValue("Allowimgcode", 0));
		foruminfo.setAllowblog(0);
		foruminfo.setIstrade(0);

		foruminfo.setAlloweditrules(0);
		foruminfo.setRecyclebin(LForumRequest.getParamIntValue("Recyclebin", 0));
		foruminfo.setModnewposts(LForumRequest.getParamIntValue("Modnewposts", 0));
		foruminfo.setJammer(LForumRequest.getParamIntValue("Jammer", 0));
		foruminfo.setDisablewatermark(LForumRequest.getParamIntValue("Disablewatermark ", 0));
		foruminfo.setInheritedmod(LForumRequest.getParamIntValue("Inheritedmod ", 0));
		foruminfo.setAllowthumbnail(LForumRequest.getParamIntValue("Allowthumbnail", 0));
		foruminfo.setAllowtag(LForumRequest.getParamIntValue("Allowtag", 0));
		int temppostspecial = 0;
		temppostspecial = LForumRequest.getParamIntValue("allowpoll", 0) == 1 ? temppostspecial | 1 : temppostspecial
				& ~1;
		temppostspecial = LForumRequest.getParamIntValue("allowbianl", 0) == 1 ? temppostspecial | 16 : temppostspecial
				& ~16;
		temppostspecial = LForumRequest.getParamIntValue("allowxuans", 0) == 1 ? temppostspecial | 4 : temppostspecial
				& ~4;
		foruminfo.setAllowpostspecial(temppostspecial);
		foruminfo.setAllowspecialonly(LForumRequest.getParamIntValue("allowspecialonly", 0));

		if (LForumRequest.getParamIntValue("autocloseoption", 0) == 0)
			foruminfo.setAutoclose(0);
		else
			foruminfo.setAutoclose(LForumRequest.getParamIntValue("autocloseday", 4));

		foruminfo.getForumfields().setDescription(LForumRequest.getParamValue("description_posttextarea"));
		foruminfo.getForumfields().setPassword(LForumRequest.getParamValue("password"));
		foruminfo.getForumfields().setIcon(LForumRequest.getParamValue("icon"));
		foruminfo.getForumfields().setPostcredits("");
		foruminfo.getForumfields().setReplycredits("");
		foruminfo.getForumfields().setRedirect(LForumRequest.getParamValue("redirect"));
		foruminfo.getForumfields().setAttachextensions(LForumRequest.getParamValues("attachextensions", ","));
		foruminfo.getForumfields().setModerators(LForumRequest.getParamValue("moderators_posttextarea"));
		foruminfo.getForumfields().setRules(LForumRequest.getParamValue("rules"));
		foruminfo.getForumfields().setTopictypes(LForumRequest.getParamValue("topictypes"));
		foruminfo.setCurtopics(0);
		foruminfo.setLastpost("1999-01-01 00:00:00");
		foruminfo.setLastposter("");
		foruminfo.setLasttitle("");
		foruminfo.setPosts(0);
		foruminfo.setTodayposts(0);
		Topics topics = new Topics();
		topics.setTid(0);
		foruminfo.setTopics(topics);
		foruminfo.setTopics_1(0);
		Users users = new Users();
		users.setUid(0);
		foruminfo.setUsers(users);
		foruminfo.setPathlist("");
		foruminfo.setColcount(1);
		foruminfo.getForumfields().setViewperm(LForumRequest.getParamValues("viewperm", ","));
		foruminfo.getForumfields().setPostperm(LForumRequest.getParamValues("postperm", ","));
		foruminfo.getForumfields().setReplyperm(LForumRequest.getParamValues("replyperm", ","));
		foruminfo.getForumfields().setGetattachperm(LForumRequest.getParamValues("getattachperm", ","));
		foruminfo.getForumfields().setPostattachperm(LForumRequest.getParamValues("postattachperm", ","));

		String result = adminForumManager.insertForumsInf(foruminfo).replace("'", "’");

		adminVistLogManager.insertLog(user, username, usergroup, grouptitle, ip, "添加论坛版块", "添加论坛版块,名称为:"
				+ foruminfo.getName());

		if (result == "")
			registerStartupScript("PAGE", "self.location.href='forum_forumstree.action';");
		else
			registerStartupScript("PAGE", "alert('用户:" + result
					+ "不存在,因为无法设为版主');self.location.href='forum_forumstree.action';");

	}

	public List<Attachtypes> getAttachtypeList() {
		return attachtypeList;
	}
	
	public List<Usergroups> getGroupList() {
		return groupList;
	}

}
