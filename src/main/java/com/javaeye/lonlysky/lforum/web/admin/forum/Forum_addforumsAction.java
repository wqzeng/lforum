package com.javaeye.lonlysky.lforum.web.admin.forum;

import java.util.List;

import org.apache.struts2.config.ParentPackage;
import org.springframework.beans.factory.annotation.Autowired;

import com.javaeye.lonlysky.lforum.AdminBaseAction;
import com.javaeye.lonlysky.lforum.comm.LForumRequest;
import com.javaeye.lonlysky.lforum.comm.utils.Utils;
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
 * 添加版块
 * 
 * @author 黄磊
 *
 */
@ParentPackage("default")
public class Forum_addforumsAction extends AdminBaseAction {

	private static final long serialVersionUID = 959835554442024770L;

	private List<Usergroups> groupList;
	private List<Attachtypes> attachtypeList;
	private String forumTree;
	private Forums foruminfo = new Forums();
	private int fid;

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
		List<Object[]> objList = adminForumManager.getForumTree();
		if (objList.size() == 0) {
			//如果版块表中没有任何版块, 则跳转到"添加第一个版块"页面. 
			registerStartupScript("",
					"<script>alert('当前没有任何板块,请添加板块!');window.location.href='forum_addfirstforum.action';</script>");
			return SUCCESS;
		}
		int fid = LForumRequest.getParamIntValue("fid", -1);
		for (Object[] objects : objList) {
			if (fid != -1 && fid == Utils.null2Int(objects[0])) {
				forumTree += "<option value=\"" + objects[0] + "\" selected=\"selected\">" + objects[1] + "</option>";
			} else {
				forumTree += "<option value=\"" + objects[0] + "\">" + objects[1] + "</option>";
			}
		}
		if (LForumRequest.isPost()) { // 保存论坛信息
			if (LForumRequest.getParamValue("name").equals("")) {
				registerStartupScript("", "<script>alert('论坛名称不能为空');</script>");
				return SUCCESS;
			}
			if (LForumRequest.getParamIntValue("addtype", 0) == 0) {
				submitSameAfter();
			} else {
				if (LForumRequest.getParamIntValue("forumid", 0) == 0) {
					registerStartupScript("", "<script>alert('请选择所属论坛版块');</script>");
					return SUCCESS;
				}
				submitAddChild();
			}

		}

		return SUCCESS;
	}

	private void submitAddChild() {
		if (LForumRequest.getParamIntValue("forumid", 0) != 0) {

			//添加与当前论坛同级的论坛
			Forums forums = forumManager.getForumInfo(LForumRequest.getParamIntValue("forumid", 0));

			//找出当前要插入的记录所用的FID
			String parentidlist = null;
			if (forums.getParentidlist().trim().equals("0")) {
				parentidlist = forums.getFid().toString();
			} else {
				parentidlist = forums.getParentidlist().trim() + "," + forums.getFid();
			}

			int maxdisplayorder = forumManager.getForumsMaxDisplayOrder(forums.getFid());
			if (maxdisplayorder == -1) {
				maxdisplayorder = forums.getDisplayorder();
			}

			insertForum(forums, forums.getLayer() + 1, parentidlist, 0, setAfterDisplayOrder(maxdisplayorder));

			setSubForumCount(forums);
		} else {
			// 按根论坛插入
			int maxdisplayorder = forumManager.getForumsMaxDisplayOrder();
			Forums parentForum = new Forums();
			parentForum.setFid(0);
			insertForum(parentForum, 0, "0", 0, maxdisplayorder);
		}

	}

	private void setSubForumCount(Forums forums) {
		forums.setSubforumcount(forums.getSubforumcount() + 1);
		forumManager.updateForum(forums);
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

		foruminfo.setTemplateid(LForumRequest.getParamIntValue("templateid", 1));
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
		if (LForumRequest.getParamIntValue("colcount", 0) == 1) //传统模式[默认]
		{
			foruminfo.setColcount(1);
		} else {
			if (LForumRequest.getParamIntValue("colcountnumber", 0) < 1
					|| LForumRequest.getParamIntValue("colcountnumber", 0) > 9) {
				registerStartupScript("", "<script>alert('列值必须在2~9范围内');</script>");
				return;
			}
			foruminfo.setColcount(LForumRequest.getParamIntValue("colcountnumber", 0));
		}
		foruminfo.getForumfields().setViewperm(LForumRequest.getParamValues("viewperm", ","));
		foruminfo.getForumfields().setPostperm(LForumRequest.getParamValues("postperm", ","));
		foruminfo.getForumfields().setReplyperm(LForumRequest.getParamValues("replyperm", ","));
		foruminfo.getForumfields().setGetattachperm(LForumRequest.getParamValues("getattachperm", ","));
		foruminfo.getForumfields().setPostattachperm(LForumRequest.getParamValues("postattachperm", ","));

		String result = adminForumManager.insertForumsInf(foruminfo).replace("'", "’");

		adminVistLogManager.insertLog(user, username, usergroup, grouptitle, ip, "添加论坛版块", "添加论坛版块,名称为:"
				+ foruminfo.getName());

		if (result.equals(""))
			registerStartupScript("PAGE", "self.location.href='forum_forumstree.action';");
		else
			registerStartupScript("PAGE", "alert('用户:" + result
					+ "不存在,因为无法设为版主');self.location.href='forum_forumstree.action';");

	}

	/**
	 * 在当前节点之后加入同级论坛时的displayorder字段值
	 * @param maxdisplayorder
	 * @return
	 */
	private int setAfterDisplayOrder(int currentdisplayorder) {
		adminForumManager.updateForumsDisplayOrder(currentdisplayorder);
		return currentdisplayorder + 1;
	}

	private void submitSameAfter() {
		int maxdisplayorder = forumManager.getForumsMaxDisplayOrder();
		Forums parentForum = new Forums();
		parentForum.setFid(0);
		insertForum(parentForum, 0, "0", 0, maxdisplayorder);
	}

	public List<Usergroups> getGroupList() {
		return groupList;
	}

	public List<Attachtypes> getAttachtypeList() {
		return attachtypeList;
	}

	public String getForumTree() {
		return forumTree;
	}

	public int getFid() {
		return fid;
	}

}
