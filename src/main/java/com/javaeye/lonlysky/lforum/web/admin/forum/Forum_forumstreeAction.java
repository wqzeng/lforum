package com.javaeye.lonlysky.lforum.web.admin.forum;

import java.util.List;

import org.apache.struts2.config.ParentPackage;
import org.springframework.beans.factory.annotation.Autowired;

import com.javaeye.lonlysky.lforum.AdminBaseAction;
import com.javaeye.lonlysky.lforum.comm.LForumRequest;
import com.javaeye.lonlysky.lforum.comm.utils.Utils;
import com.javaeye.lonlysky.lforum.entity.forum.Forums;
import com.javaeye.lonlysky.lforum.service.ForumManager;
import com.javaeye.lonlysky.lforum.service.admin.AdminForumManager;

/**
 * 论坛版块托动
 * 
 * @author 黄磊
 *
 */
@ParentPackage("default")
public class Forum_forumstreeAction extends AdminBaseAction {

	private static final long serialVersionUID = 3187964039684462690L;

	@Autowired
	private AdminForumManager adminForumManager;

	@Autowired
	private ForumManager forumManager;

	// 图标信息变量声明
	private String t_rootpic = "<img src=../images/lines/tplus.gif align=absmiddle>";
	private String l_rootpic = "<img src=../images/lines/lplus.gif align=absmiddle>";
	private String l_TOP_rootpic = "<img src=../images/lines/rplus.gif align=absmiddle>";
	private String i_rootpic = "<img src=../images/lines/dashplus.gif align=absmiddle>";
	private String t_nodepic = "<img src=../images/lines/tminus.gif align=absmiddle>";
	private String l_nodepic = "<img src=../images/lines/lminus.gif align=absmiddle>";
	private String i_nodepic = "<img src=../images/lines/i.gif align=absmiddle>";
	private String no_nodepic = "<img src=../images/lines/noexpand.gif align=absmiddle>";

	private String treeStr = "";
	private int noPicCount = 0;

	@Override
	public String execute() throws Exception {
		if (!LForumRequest.getParamValue("currentfid").equals("")) {
			if (!adminForumManager.movingForumsPos(LForumRequest.getParamIntValue("currentfid", 0), LForumRequest
					.getParamIntValue("targetfid", 0), LForumRequest.getParamIntValue("isaschildnode", 0) == 1 ? true
					: false)) {
				registerStartupScript("",
						"<script>alert('当前版块下面有子版块,因此无法移动!');window.location.href='forum_forumstree.action';</script>");
			}
			adminVistLogManager.insertLog(user, username, usergroup, grouptitle, ip, "移动论坛版块", "移动论坛版块ID:"
					+ LForumRequest.getParamValue("currentfid") + "到ID:" + LForumRequest.getParamValue("targetfid"));
		}
		if (!LForumRequest.isPost()) {
			List<Forums> forumList = forumManager.getForumList();
			if (forumList.size() == 0) {
				//如果版块表中没有任何版块, 则跳转到"添加第一个版块"页面. 
				registerStartupScript("",
						"<script>alert('当前没有任何板块,请添加板块!');window.location.href='forum_addfirstforum.action';</script>");
			} else {
				addTree(0, forumManager.getForumList("layer=0 and forums.fid=0"), "");

				treeStr = "<script type=\"text/javascript\">\r\n  var obj = [" + treeStr;
				treeStr = treeStr.substring(0, treeStr.length() - 3);
				treeStr += "];\r\n var newtree = new tree(\"newtree\",obj,\"reSetTree\");";
				treeStr += "</script>";
			}
		}
		return SUCCESS;
	}

	private void addTree(int layer, List<Forums> forumList, String currentnodestr) {
		if (layer == 0) {
			// 作为根结点

			for (int n = 0; n < forumList.size(); n++) {
				String mystr = "";
				if (forumList.size() == 1) {
					mystr += i_rootpic; //
					currentnodestr = no_nodepic;
				} else {
					if (n == 0) {
						mystr += l_TOP_rootpic; //
						currentnodestr = i_nodepic;
					} else {
						if ((n > 0) && (n < (forumList.size() - 1))) {
							mystr += t_rootpic; //
							currentnodestr = i_nodepic;
						} else {
							mystr += l_rootpic;
							currentnodestr = no_nodepic;
						}
					}
				}

				treeStr += "{fid:"
						+ forumList.get(n).getFid()
						+ ",name:\""
						+ Utils.cleanHtmlTag(forumList.get(n).getName().trim().replace("\\", "\\\\ "))
						+ "\",subject:\" "
						+ mystr
						+ " <img src=../images/folders.gif align=\\\"absmiddle\\\" > <a href=\\\"../../showforum.action?forumid="
						+ forumList.get(n).getFid() + "\\\" target=\\\"_blank\\\">"
						+ Utils.cleanHtmlTag(forumList.get(n).getName().trim().replace("\\", "\\\\ "))
						+ "</a>\",linetitle:\"" + mystr + "\",parentidlist:0,layer:" + forumList.get(n).getLayer()
						+ ",subforumcount:" + forumList.get(n).getSubforumcount() + ",istrade:"
						+ getIsTrade(forumList.get(n).getIstrade()) + "},\r\n";

				if (forumList.get(n).getSubforumcount() > 0) {
					int mylayer = forumList.get(n).getLayer();
					String selectstr = "layer=" + (++mylayer) + " and forums.fid=" + forumList.get(n).getFid();
					addTree(mylayer, forumManager.getForumList(selectstr), currentnodestr);
				}
			}
		} else {
			// 作为版块

			for (int n = 0; n < forumList.size(); n++) {
				String mystr = "";
				mystr += currentnodestr;
				String temp = currentnodestr;

				if ((n >= 0) && (n < (forumList.size() - 1))) {
					mystr += t_nodepic; //
					temp += t_nodepic;
				} else {
					mystr += l_nodepic;
					noPicCount++;
					temp += no_nodepic;
				}

				treeStr += "{fid:"
						+ forumList.get(n).getFid()
						+ ",name:\""
						+ Utils.cleanHtmlTag(forumList.get(n).getName().trim().replace("\\", "\\\\ "))
						+ "\",subject:\" "
						+ mystr
						+ " <img src=../images/folder.gif align=\\\"absmiddle\\\" > <a href=\\\"../../showforum.action?forumid="
						+ forumList.get(n).getFid() + "\\\" target=\\\"_blank\\\">"
						+ Utils.cleanHtmlTag(forumList.get(n).getName().trim().replace("\\", "\\\\ "))
						+ "</a>\",linetitle:\"" + mystr + "\",parentidlist:\""
						+ forumList.get(n).getParentidlist().trim() + "\",layer:" + forumList.get(n).getLayer()
						+ ",subforumcount:" + forumList.get(n).getSubforumcount() + ",istrade:"
						+ getIsTrade(forumList.get(n).getIstrade()) + "},\r\n";

				if (forumList.get(n).getSubforumcount() > 0) {
					int mylayer = forumList.get(n).getLayer();
					String selectstr = "layer=" + (++mylayer) + " and forums.fid=" + forumList.get(n).getFid();
					addTree(mylayer, forumManager.getForumList(selectstr), temp);
				}
			}
		}

	}

	private int getIsTrade(Integer istrade) {
		if (istrade == 0)
			return 0;
		return 0;
	}

	public String getTreeStr() {
		return treeStr;
	}

}
