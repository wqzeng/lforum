package com.javaeye.lonlysky.lforum.web;

import java.io.IOException;

import org.apache.struts2.config.ParentPackage;
import org.springframework.beans.factory.annotation.Autowired;

import com.javaeye.lonlysky.lforum.ForumBaseAction;
import com.javaeye.lonlysky.lforum.comm.LForumRequest;
import com.javaeye.lonlysky.lforum.comm.utils.ForumUtils;
import com.javaeye.lonlysky.lforum.comm.utils.Utils;
import com.javaeye.lonlysky.lforum.config.impl.ConfigLoader;
import com.javaeye.lonlysky.lforum.entity.forum.AttachmentInfo;
import com.javaeye.lonlysky.lforum.entity.forum.Forums;
import com.javaeye.lonlysky.lforum.entity.forum.Topics;
import com.javaeye.lonlysky.lforum.service.AttachmentManager;
import com.javaeye.lonlysky.lforum.service.ForumManager;
import com.javaeye.lonlysky.lforum.service.ModeratorManager;
import com.javaeye.lonlysky.lforum.service.ScoresetManager;
import com.javaeye.lonlysky.lforum.service.TopicManager;
import com.javaeye.lonlysky.lforum.service.UserCreditManager;

/**
 * 附件Action
 * 
 * @author 黄磊
 *
 */
@ParentPackage("default")
public class AttachmentAction extends ForumBaseAction {

	private static final long serialVersionUID = 5773528389827513652L;

	/**
	 * 附件所属主题信息
	 */
	private Topics topic;

	/**
	 * 附件信息
	 */
	private AttachmentInfo attachmentinfo;

	/**
	 * 附件所属版块Id
	 */
	private int forumid;

	/**
	 * 附件所属版块名称
	 */
	private String forumname;

	/**
	 * 附件所属主题Id
	 */
	private int topicid;

	/**
	 * 附件Id
	 */
	private int attachmentid;

	/**
	 * 附件所属主题标题
	 */
	private String topictitle;

	/**
	 * 论坛导航信息
	 */
	private String forumnav;

	/**
	 * 是否需要登录后进行下载
	 */
	private boolean needlogin = false;

	@Autowired
	private ScoresetManager scoresetManager;

	@Autowired
	private AttachmentManager attachmentManager;

	@Autowired
	private TopicManager topicManager;

	@Autowired
	private ForumManager forumManager;

	@Autowired
	private ModeratorManager moderatorManager;

	@Autowired
	private UserCreditManager userCreditManager;

	@Override
	public String execute() throws Exception {
		pagetitle = "附件下载";

		//　如果当前用户非管理员并且论坛设定了禁止下载附件时间段，当前时间如果在其中的一个时间段内，则不允许用户下载附件
		if (useradminid != 1 && usergroupinfo.getDisableperiodctrl() != 1) {
			String visittime = scoresetManager.betweenTime(config.getAttachbanperiods());
			if (!visittime.equals("")) {
				reqcfg.addErrLine("在此时间段( " + visittime + " )内用户不可以下载附件");
				return SUCCESS;
			}
		}

		// 获取附件ID
		attachmentid = LForumRequest.getParamIntValue("attachmentid", -1);
		// 如果附件ID非数字
		if (attachmentid == -1) {
			reqcfg.addErrLine("无效的附件ID");
			return SUCCESS;
		}

		// 获取该附件的信息
		attachmentinfo = attachmentManager.getAttachmentInfo(attachmentid);
		// 如果该附件不存在
		if (attachmentinfo == null) {
			reqcfg.addErrLine("不存在的附件ID");
			return SUCCESS;
		}
		topicid = attachmentinfo.getTopics().getTid();

		// 获取该主题的信息
		topic = topicManager.getTopicInfo(topicid);
		// 如果该主题不存在
		if (topic == null) {
			reqcfg.addErrLine("不存在的主题ID");
			return SUCCESS;
		}

		topictitle = topic.getTitle();
		forumid = topic.getForums().getFid();
		Forums forum = forumManager.getForumInfo(forumid);
		forumname = forum.getName();

		pagetitle = Utils.cleanHtmlTag(forum.getName());
		forumnav = forum.getPathlist().trim();

		//添加判断特殊用户的代码
		if (!forumManager.allowViewByUserID(forum.getForumfields().getPermuserlist(), userid)) {
			if (!forumManager.allowView(forum.getForumfields().getViewperm(), usergroupid)) {
				reqcfg.addErrLine("您没有浏览该版块的权限");
				if (userid == -1) {
					needlogin = true;
				}
				return SUCCESS;
			}
		}

		//添加判断特殊用户的代码
		if (!forumManager.allowGetAttachByUserID(forum.getForumfields().getPermuserlist(), userid)) {
			if (Utils.null2String(forum.getForumfields().getGetattachperm()).equals("")) {
				// 验证用户是否有下载附件的权限
				if (usergroupinfo.getAllowgetattach() != 1) {
					reqcfg.addErrLine("您当前的身份 \"" + usergroupinfo.getGrouptitle() + "\" 没有下载或查看附件的权限");
					if (userid == -1) {
						needlogin = true;
					}
					return SUCCESS;
				}
			} else {
				if (!forumManager.allowGetAttach(forum.getForumfields().getGetattachperm(), usergroupid)) {
					reqcfg.addErrLine("您没有在该版块下载附件的权限");
					if (userid == -1) {
						needlogin = true;
					}
					return SUCCESS;
				}
			}
		}

		// 检查用户是否拥有足够的阅读权限
		if ((attachmentinfo.getReadperm() > usergroupinfo.getReadaccess())
				&& (attachmentinfo.getUsers().getUid() != userid)
				&& (!moderatorManager.isModer(useradminid, userid, forumid))) {
			reqcfg.addErrLine("您的阅读权限不够");
			if (userid == -1) {
				needlogin = true;
			}
			return SUCCESS;
		}
		//如果图片是不直接显示(作为附件显示) 并且不是作者本人下载都会扣分
		if (config.getShowimages() != 1 || !Utils.isImgFilename(attachmentinfo.getFilename().trim())
				&& userid != attachmentinfo.getUsers().getUid()) {
			if (userCreditManager.updateUserCreditsByDownloadAttachment(userid) == -1) {
				reqcfg.addErrLine("您的积分不足");
				return SUCCESS;
			}

		}

		if (attachmentinfo.getFilename().indexOf("http") < 0) {
			if (!Utils.fileExists(ConfigLoader.getConfig().getWebpath() + "upload/" + attachmentinfo.getFilename())) {
				reqcfg.addErrLine("该附件文件不存在或已被删除");
				return SUCCESS;
			}
		}

		attachmentManager.updateAttachmentDownloads(attachmentid);

		try {
			if (attachmentinfo.getFilename().indexOf("http") < 0) {
				ForumUtils.responseFile(ConfigLoader.getConfig().getWebpath() + "upload/"
						+ attachmentinfo.getFilename(), attachmentinfo.getAttachment(), attachmentinfo.getFiletype());
			} else {
				responseFile(attachmentinfo.getFilename().trim(), attachmentinfo.getFiletype());
			}
		} catch (Exception e) {
		}

		return SUCCESS;
	}

	public void responseFile(String url, String filetype) throws IOException {
		response.sendRedirect(url);
	}

	public Topics getTopic() {
		return topic;
	}

	public AttachmentInfo getAttachmentinfo() {
		return attachmentinfo;
	}

	public int getForumid() {
		return forumid;
	}

	public String getForumname() {
		return forumname;
	}

	public int getTopicid() {
		return topicid;
	}

	public int getAttachmentid() {
		return attachmentid;
	}

	public String getTopictitle() {
		return topictitle;
	}

	public String getForumnav() {
		return forumnav;
	}

	public boolean isNeedlogin() {
		return needlogin;
	}
}
