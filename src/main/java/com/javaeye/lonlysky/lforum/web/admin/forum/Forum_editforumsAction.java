package com.javaeye.lonlysky.lforum.web.admin.forum;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.struts2.config.ParentPackage;
import org.springframework.beans.factory.annotation.Autowired;

import com.javaeye.lonlysky.lforum.AdminBaseAction;
import com.javaeye.lonlysky.lforum.comm.LForumRequest;
import com.javaeye.lonlysky.lforum.comm.utils.Utils;
import com.javaeye.lonlysky.lforum.entity.forum.Attachtypes;
import com.javaeye.lonlysky.lforum.entity.forum.Forums;
import com.javaeye.lonlysky.lforum.entity.forum.Templates;
import com.javaeye.lonlysky.lforum.entity.forum.Usergroups;
import com.javaeye.lonlysky.lforum.service.AttachmentManager;
import com.javaeye.lonlysky.lforum.service.ForumManager;
import com.javaeye.lonlysky.lforum.service.TemplateManager;
import com.javaeye.lonlysky.lforum.service.UserGroupManager;
import com.javaeye.lonlysky.lforum.service.admin.AdminForumManager;
import com.javaeye.lonlysky.lforum.service.admin.AdminStatsManager;
import com.javaeye.lonlysky.lforum.service.admin.AdminStatsManager.ForumStats;

/**
 * 编辑论坛版块信息
 * 
 * @author 黄磊
 *
 */
@ParentPackage("default")
public class Forum_editforumsAction extends AdminBaseAction {

	private static final long serialVersionUID = 3211707378265122323L;

	private List<Attachtypes> attachtypeList;

	/**
	 * 当前编辑论坛信息
	 */
	private Forums forum;

	private String templatestr = "";
	private int fid;
	private String runforumsstatic = "";
	private String powerset = "";
	private String tabDef = "tabPage1";
	private int[] attachexts;

	@Autowired
	private UserGroupManager userGroupManager;

	@Autowired
	private AttachmentManager attachmentManager;

	@Autowired
	private ForumManager forumManager;

	@Autowired
	private TemplateManager templateManager;

	@Autowired
	private AdminForumManager adminForumManager;

	@Autowired
	private AdminStatsManager adminStatsManager;

	@Override
	public String execute() throws Exception {
		fid = LForumRequest.getParamIntValue("fid", 0);
		if (!LForumRequest.isPost()) {
			if (fid == 0) {
				registerStartupScript("PAGE", "alert('无效板块ID！');self.location.href='forum_forumstree.action';");
				return SUCCESS;
			}
			bindData();
		} else {
			String submitMethod = LForumRequest.getParamValue("submitMethod");
			if (!submitMethod.equals("")) {
				submitMethod = submitMethod.substring(submitMethod.indexOf(":") + 1);
				if (submitMethod.equals("bindPower")) {
					return bindPower();
				} else if (submitMethod.equals("delPower")) {
					return delPower();
				} else if (submitMethod.equals("runForumStatic")) {
					return runForumStatic();
				}
			}
			if (fid != 0) {
				forum = forumManager.getForumInfo(fid);
				forum.setName(LForumRequest.getParamValue("name"));
				forum.setStatus(LForumRequest.getParamIntValue("status", 1));

				if (LForumRequest.getParamIntValue("colcount", 0) == 1) //传统模式[默认]
				{
					forum.setColcount(1);
				} else {
					if (LForumRequest.getParamIntValue("colcountnumber", 0) < 1
							|| LForumRequest.getParamIntValue("colcountnumber", 0) > 9) {
						registerStartupScript("", "<script>alert('列值必须在2~9范围内');</script>");
						return SUCCESS;
					}
					forum.setColcount(LForumRequest.getParamIntValue("colcountnumber", 0));
				}
				forum.setTemplateid(LForumRequest.getParamIntValue("templateid", 1) == config.getTemplateid() ? 0
						: LForumRequest.getParamIntValue("templateid", 1));
				forum.setAllowsmilies(LForumRequest.getParamIntValue("Allowsmilies", 0));
				forum.setAllowrss(LForumRequest.getParamIntValue("Allowrss", 0));
				forum.setAllowbbcode(LForumRequest.getParamIntValue("Allowbbcode", 0));
				forum.setAllowimgcode(LForumRequest.getParamIntValue("Allowimgcode", 0));
				forum.setRecyclebin(LForumRequest.getParamIntValue("Recyclebin", 0));
				forum.setModnewposts(LForumRequest.getParamIntValue("Modnewposts", 0));
				forum.setJammer(LForumRequest.getParamIntValue("Jammer", 0));
				forum.setDisablewatermark(LForumRequest.getParamIntValue("Disablewatermark ", 0));
				forum.setInheritedmod(LForumRequest.getParamIntValue("Inheritedmod ", 0));
				forum.setAllowthumbnail(LForumRequest.getParamIntValue("Allowthumbnail", 0));
				forum.setAllowtag(LForumRequest.getParamIntValue("Allowtag", 0));

				int temppostspecial = 0;
				temppostspecial = LForumRequest.getParamIntValue("allowpoll", 0) == 1 ? temppostspecial | 1
						: temppostspecial & ~1;
				temppostspecial = LForumRequest.getParamIntValue("allowbianl", 0) == 1 ? temppostspecial | 16
						: temppostspecial & ~16;
				temppostspecial = LForumRequest.getParamIntValue("allowxuans", 0) == 1 ? temppostspecial | 4
						: temppostspecial & ~4;
				forum.setAllowpostspecial(temppostspecial);
				forum.setAllowspecialonly(LForumRequest.getParamIntValue("allowspecialonly", 0));

				if (LForumRequest.getParamIntValue("autocloseoption", 0) == 0)
					forum.setAutoclose(0);
				else
					forum.setAutoclose(LForumRequest.getParamIntValue("autocloseday", 4));

				forum.getForumfields().setDescription(LForumRequest.getParamValue("description_posttextarea"));
				forum.getForumfields().setPassword(LForumRequest.getParamValue("password"));
				forum.getForumfields().setIcon(LForumRequest.getParamValue("icon"));
				forum.getForumfields().setRedirect(LForumRequest.getParamValue("redirect"));
				forum.getForumfields().setAttachextensions(LForumRequest.getParamValues("attachextensions", ","));

				adminForumManager.compareOldAndNewModerator(forum.getForumfields().getModerators(), LForumRequest
						.getParamValue("moderators_posttextarea"), fid);
				forum.getForumfields().setModerators(LForumRequest.getParamValue("moderators_posttextarea"));
				forum.getForumfields().setRules(LForumRequest.getParamValue("rules"));
				forum.getForumfields().setTopictypes(LForumRequest.getParamValue("topictypes"));

				forum.getForumfields().setViewperm(LForumRequest.getParamValues("viewperm", ","));
				forum.getForumfields().setPostperm(LForumRequest.getParamValues("postperm", ","));
				forum.getForumfields().setReplyperm(LForumRequest.getParamValues("replyperm", ","));
				forum.getForumfields().setGetattachperm(LForumRequest.getParamValues("getattachperm", ","));
				forum.getForumfields().setPostattachperm(LForumRequest.getParamValues("postattachperm", ","));

				String result = adminForumManager.saveForumsInf(forum).replace("'", "’");
				adminVistLogManager.insertLog(user, username, usergroup, grouptitle, ip, "编辑论坛版块", "编辑论坛版块,名称为:"
						+ forum.getName().trim());

				if (result.equals("")) {
					response.sendRedirect("forum_forumstree.action");
					return null;
				} else {
					renderHtml("<script>alert('用户:"
							+ result
							+ "不存在或因为它们所属组为\"游客\",\"等待验证会员\",因为无法设为版主');window.location.href='forum_forumstree.action';</script>");
					return null;
				}
			}
		}
		return SUCCESS;
	}

	/**
	 * 绑定页面数据
	 */
	private void bindData() {
		forum = forumManager.getForumInfo(fid);
		// 权限
		List<Usergroups> groupList = userGroupManager.getUserGroupList();
		for (int i = 1; i <= groupList.size(); i++) {
			String classStr = "";
			if (i % 2 == 1)
				classStr = "td_alternating_item1";
			else
				classStr = "td_alternating_item2";
			powerset += "<tr><td class='" + classStr + "'><input type='checkbox' id='r" + i + "' onclick='selectRow("
					+ i + ",this.checked)'></td><td class='" + classStr + "'><label for='r" + i + "'>"
					+ groupList.get(i - 1).getGrouptitle() + "</lable></td>";
			powerset += getPowerStr("viewperm", forum.getForumfields().getViewperm().trim(), groupList.get(i - 1)
					.getGroupid(), i);
			powerset += getPowerStr("postperm", forum.getForumfields().getPostperm().trim(), groupList.get(i - 1)
					.getGroupid(), i);
			powerset += getPowerStr("replyperm", forum.getForumfields().getReplyperm().trim(), groupList.get(i - 1)
					.getGroupid(), i);
			powerset += getPowerStr("getattachperm", forum.getForumfields().getGetattachperm().trim(), groupList.get(
					i - 1).getGroupid(), i);
			powerset += getPowerStr("postattachperm", forum.getForumfields().getPostattachperm().trim(), groupList.get(
					i - 1).getGroupid(), i)
					+ "</tr>";
		}

		// 允许附件
		attachtypeList = attachmentManager.getAttachmentType();
		if (!forum.getForumfields().getAttachextensions().trim().equals("")) {
			String[] strs = forum.getForumfields().getAttachextensions().trim().split(",");
			attachexts = new int[strs.length];
			for (int i = 0; i < strs.length; i++) {
				attachexts[i] = Utils.null2Int(strs[i]);
			}
		} else {
			attachexts = new int[0];
		}

		// 模板
		List<Templates> templateList = templateManager.getValidTemplateList();
		for (Templates template : templateList) {
			if (forum.getTemplateid() == template.getTemplateid().intValue()) {
				templatestr += "<option selected=\"selected\" value=\"" + template.getTemplateid() + "\">"
						+ template.getName() + "</option>";
			} else {
				templatestr += "<option value=\"" + template.getTemplateid() + "\">" + template.getName() + "</option>";
			}
		}
	}

	/**
	 * 增加特殊用户
	 */
	public String bindPower() throws Exception {
		System.out.println("增加特殊用户");
		registerStartupScript("PAGE", "alert('增加特殊用户!');self.location.href='forum_forumstree.action';");
		return SUCCESS;
	}

	/**
	 * 删除特殊用户
	 */
	public String delPower() throws Exception {
		System.out.println("删除特殊用户");
		registerStartupScript("PAGE", "alert('删除特殊用户!');self.location.href='forum_forumstree.action';");
		return SUCCESS;
	}

	/**
	 * 统计最新信息
	 */
	public String runForumStatic() throws Exception {
		fid = LForumRequest.getParamIntValue("fid", 0);
		if (fid != 0) {
			forum = forumManager.getForumInfo(fid);
		} else {
			registerStartupScript("PAGE", "alert('无效板块ID！');self.location.href='forum_forumstree.action';");
			return SUCCESS;
		}
		Map<ForumStats, Object> map = new HashMap<ForumStats, Object>();
		adminStatsManager.reSetFourmTopicAPost(fid, map);
		int topiccount = Utils.null2Int(map.get(ForumStats.topiccount), 0);
		int postcount = Utils.null2Int(map.get(ForumStats.postcount), 0);
		//int lasttid = Utils.null2Int(map.get(ForumStats.lasttid), 0);
		//String lasttitle = Utils.null2String(map.get(ForumStats.lasttitle));
		String lastpost = Utils.null2String(map.get(ForumStats.lastpost));
		//int lastposterid = Utils.null2Int(map.get(ForumStats.lastposterid), 0);
		//String lastposter = Utils.null2String(map.get(ForumStats.lastposter));
		int replypost = Utils.null2Int(map.get(ForumStats.todaypostcount), 0);
		runforumsstatic = "<br /><br />运行结果<hr style=\"height:1px; width:600; color:#CCCCCC; background:#CCCCCC; border: 0; \" align=\"left\" />主题总数:"
				+ topiccount + "<br />帖子总数:" + postcount + "<br />今日回帖数总数:" + replypost + "<br />最后提交日期:" + lastpost;
		if ((forum.getTopics_1() == topiccount) && (forum.getPosts() == postcount)
				&& (forum.getTodayposts() == replypost) && (forum.getLastpost().trim().equals(lastpost))) {
			runforumsstatic += "<br /><br /><br />结果一致";
		} else {
			runforumsstatic += "<br /><br /><br />比较<hr style=\"height:1px; width:600; color:#CCCCCC; background:#CCCCCC; border: 0; \" align=\"left\" />";
			if (forum.getTopics_1() != topiccount) {
				runforumsstatic += "主题总数有差异<br />";
			}
			if (forum.getPosts() != postcount) {
				runforumsstatic += "帖子总数有差异<br />";
			}
			if (forum.getTodayposts() != replypost) {
				runforumsstatic += "今日回帖数总数有差异<br />";
			}
			if (!forum.getLastpost().equals(lastpost)) {
				runforumsstatic += "最后提交日期有差异<br />";
			}
		}
		bindData();
		tabDef = "tabPage6";
		return SUCCESS;
	}

	/*
	 * 生成组权限控制项
	 */
	private String getPowerStr(String strPerfix, String groupList, int groupId, int ctlId) {
		String classStr = "";
		if (ctlId % 2 == 1)
			classStr = "td_alternating_item1";
		else
			classStr = "td_alternating_item2";
		groupList = "," + groupList + ",";
		String strTd = "<td class='" + classStr + "'><input type='checkbox' name='" + strPerfix + "' id='" + strPerfix
				+ ctlId + "' value='" + groupId + "' "
				+ (groupList.indexOf("," + groupId + ",") == -1 ? "" : "checked='checked'") + "></td>";

		return strTd;
	}

	public String getPowerset() {
		return powerset;
	}

	public List<Attachtypes> getAttachtypeList() {
		return attachtypeList;
	}

	public Forums getForum() {
		return forum;
	}

	public String getTemplatestr() {
		return templatestr;
	}

	public int getFid() {
		return fid;
	}

	public String getRunforumsstatic() {
		return runforumsstatic;
	}

	public String getTabDef() {
		return tabDef;
	}

	public int[] getAttachexts() {
		return attachexts;
	}

}
