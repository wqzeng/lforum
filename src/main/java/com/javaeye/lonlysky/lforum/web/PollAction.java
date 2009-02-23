package com.javaeye.lonlysky.lforum.web;

import java.util.Date;

import org.apache.struts2.config.ParentPackage;
import org.springframework.beans.factory.annotation.Autowired;

import com.javaeye.lonlysky.lforum.ForumBaseAction;
import com.javaeye.lonlysky.lforum.comm.LForumRequest;
import com.javaeye.lonlysky.lforum.comm.utils.ForumUtils;
import com.javaeye.lonlysky.lforum.comm.utils.Utils;
import com.javaeye.lonlysky.lforum.entity.forum.Forums;
import com.javaeye.lonlysky.lforum.entity.forum.Topics;
import com.javaeye.lonlysky.lforum.service.ForumManager;
import com.javaeye.lonlysky.lforum.service.PollManager;
import com.javaeye.lonlysky.lforum.service.TopicManager;
import com.javaeye.lonlysky.lforum.service.UserCreditManager;

/**
 * 投票页面
 * 
 * @author 黄磊
 *
 */
@ParentPackage("default")
public class PollAction extends ForumBaseAction {

	private static final long serialVersionUID = 2588112441113379298L;

	/**
	 * 主题信息
	 */
	private Topics topic;

	/**
	 * 所属版块Id
	 */
	private int forumid;

	/**
	 * 所属版块名称
	 */
	private String forumname;

	/**
	 * 论坛导航信息
	 */
	private String forumnav;

	/**
	 * 主题Id
	 */
	private int topicid;

	/**
	 * 主题标题
	 */
	private String topictitle;

	private final String POLLED_COOKIENAME = "lforum_polled";

	@Autowired
	private TopicManager topicManager;

	@Autowired
	private ForumManager forumManager;

	@Autowired
	private PollManager pollManager;

	@Autowired
	private UserCreditManager userCreditManager;

	@Override
	public String execute() throws Exception {
		// 获取主题ID
		topicid = LForumRequest.getParamIntValue("topicid", -1);

		forumnav = "";

		// 如果主题ID非数字
		if (topicid == -1) {
			reqcfg.addErrLine("无效的主题ID");
			return SUCCESS;
		}

		// 获取该主题的信息
		topic = topicManager.getTopicInfo(topicid);
		// 如果该主题不存在
		if (topic == null) {
			reqcfg.addErrLine("不存在的主题ID");
			return SUCCESS;
		}

		topictitle = topic.getTitle().trim();
		forumid = topic.getForums().getFid();
		Forums forum = forumManager.getForumInfo(forumid);
		forumname = forum.getName();
		pagetitle = Utils.cleanHtmlTag(forum.getName());
		forumnav = forum.getPathlist().trim();

		if (topic.getSpecial() != 1) {
			reqcfg.addErrLine("不存在的投票ID");
			return SUCCESS;
		}

		if (usergroupinfo.getAllowvote() != 1) {
			reqcfg.addErrLine("您当前的身份 \"" + usergroupinfo.getGrouptitle() + "\" 没有投票的权限");
			return SUCCESS;
		}

		if (Utils.parseDate(pollManager.getPollEnddatetime(topic.getTid())).before(new Date())) {
			reqcfg.addErrLine("投票已经过期");
			return SUCCESS;
		}
		String polled = "";
		if (userid != -1) {
			if (!pollManager.allowVote(topicid, username)) {
				reqcfg.addErrLine("你已经投过票");
				return SUCCESS;
			}
		} else {
			//写cookie
			polled = ForumUtils.getCookie(POLLED_COOKIENAME);
			if (Utils.inArray(topic.getTid().toString(), polled)) {
				reqcfg.addErrLine("你已经投过票");
				return SUCCESS;
			}
		}

		//当未选择任何投票项时
		if (LForumRequest.getParamValues("pollitemid", ",").equals("")) {
			reqcfg.addErrLine("您未选择任何投票项！");
			return SUCCESS;
		}

		if (pollManager.updatePoll(topicid, LForumRequest.getParamValues("pollitemid", ","),
				userid == -1 ? usergroupinfo.getGrouptitle() + " [" + LForumRequest.getIp() + "]" : username) < 0) {
			reqcfg.addErrLine("提交投票信息中包括非法内容");
			return SUCCESS;
		}

		if (userid == -1) {
			ForumUtils.writeCookie(POLLED_COOKIENAME, polled + "," + topic.getTid());
		}

		reqcfg.setUrl("showtopic.action?topicid=" + topicid).setMetaRefresh().setShowBackLink(false);
		if (userid != -1) {
			userCreditManager.updateUserCreditsByVotepoll(userid);
		}
		reqcfg.addMsgLine("投票成功, 返回主题");
		return SUCCESS;
	}

	public Topics getTopic() {
		return topic;
	}

	public int getForumid() {
		return forumid;
	}

	public String getForumname() {
		return forumname;
	}

	public String getForumnav() {
		return forumnav;
	}

	public int getTopicid() {
		return topicid;
	}

	public String getTopictitle() {
		return topictitle;
	}
}
