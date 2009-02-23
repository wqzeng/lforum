package com.javaeye.lonlysky.lforum.web;

import java.io.IOException;
import java.text.ParseException;

import org.apache.struts2.config.ParentPackage;
import org.springframework.beans.factory.annotation.Autowired;

import com.javaeye.lonlysky.lforum.ForumBaseAction;
import com.javaeye.lonlysky.lforum.comm.LForumRequest;
import com.javaeye.lonlysky.lforum.comm.utils.Utils;
import com.javaeye.lonlysky.lforum.entity.forum.Admingroups;
import com.javaeye.lonlysky.lforum.entity.forum.Forums;
import com.javaeye.lonlysky.lforum.entity.forum.Posts;
import com.javaeye.lonlysky.lforum.entity.forum.Topics;
import com.javaeye.lonlysky.lforum.service.AdminGroupManager;
import com.javaeye.lonlysky.lforum.service.ForumManager;
import com.javaeye.lonlysky.lforum.service.ForumTagManager;
import com.javaeye.lonlysky.lforum.service.ModeratorManager;
import com.javaeye.lonlysky.lforum.service.PostManager;
import com.javaeye.lonlysky.lforum.service.TopicAdminManager;
import com.javaeye.lonlysky.lforum.service.TopicManager;
import com.javaeye.lonlysky.lforum.service.UserCreditManager;

/**
 * 删除帖子页
 * 
 * @author 黄磊
 *
 */
@ParentPackage("default")
public class DelpostAction extends ForumBaseAction {

	private static final long serialVersionUID = 7434141474039324999L;

	/**
	 * 帖子Id
	 */
	private int postid;

	/**
	 * 帖子信息
	 */
	private Posts post;

	/**
	 * 所属主题信息
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
	 * 所属主题Id
	 */
	private int topicid;

	/**
	 * 所属主题标题
	 */
	private String topictitle;

	/**
	 * 论坛导航信息
	 */
	private String forumnav;

	/**
	 * 版块信息
	 */
	private Forums forum;
	private boolean ismoder = false;

	// 是否允许删除帖子, 初始false为不允许
	boolean allowdelpost = false;

	@Autowired
	private TopicManager topicManager;

	@Autowired
	private PostManager postManager;

	@Autowired
	private UserCreditManager userCreditManager;

	@Autowired
	private ForumManager forumManager;

	@Autowired
	private TopicAdminManager topicAdminManager;

	@Autowired
	private ForumTagManager forumTagManager;

	@Autowired
	private ModeratorManager moderatorManager;

	@Autowired
	private AdminGroupManager adminGroupManager;

	@Override
	public String execute() throws Exception {
		// 获取帖子ID
		topicid = LForumRequest.getParamIntValue("topicid", -1);
		postid = LForumRequest.getParamIntValue("postid", -1);
		// 如果主题ID非数字
		if (postid == -1) {
			reqcfg.addErrLine("无效的帖子ID");
			return SUCCESS;
		}

		// 获取该帖子的信息
		post = postManager.getPostInfo(postid);
		// 如果该帖子不存在
		if (post == null) {
			reqcfg.addErrLine("不存在的主题ID");
			return SUCCESS;
		}

		// 获取该主题的信息
		topic = topicManager.getTopicInfo(topicid);

		// 如果该主题不存在
		if (topic == null) {
			reqcfg.addErrLine("不存在的主题ID");
			return SUCCESS;
		}

		if (topicid != post.getTopics().getTid()) {
			reqcfg.addErrLine("主题ID无效");
			return SUCCESS;
		}

		topictitle = topic.getTitle();
		forumid = topic.getForums().getFid();
		forum = forumManager.getForumInfo(forumid);
		forumname = forum.getName();
		pagetitle = "删除" + post.getTitle();
		forumnav = forum.getPathlist().trim();
		int opinion = LForumRequest.getParamIntValue("opinion", -1);
		if (!checkPermission(post, opinion))
			return SUCCESS;

		if (!allowdelpost) {
			reqcfg.addErrLine("当前不允许删帖");
			return SUCCESS;
		}

		int losslessdel = Utils.strDateDiffHours(post.getPostdatetime(), config.getLosslessdel() * 24);

		// 通过验证的用户可以删除帖子，如果是主题贴则另处理
		if (post.getLayer() == 0) {
			topicAdminManager.deleteTopics(topicid + "", forum.getRecyclebin(), false);
			//重新统计论坛帖数
			forumManager.setRealCurrentTopics(forum.getFid());

			forumTagManager.deleteTopicTags(topicid);
		} else {
			int reval;
			if (topic.getSpecial() == 4) {
				String opiniontext = "";

				if (opinion != 1 && opinion != 2) {
					reqcfg.addErrLine("参数错误");
					return SUCCESS;
				}
				reval = postManager.deletePost(postid, false, true);
				switch (opinion) {
				case 1:
					opiniontext = "positivediggs";
					break;
				case 2:
					opiniontext = "negativediggs";
					break;

				}
				postManager.deleteDebatePost(topicid, opiniontext, postid);

			} else {
				reval = postManager.deletePost(postid, false, true);

			}

			//再次确保回复数精确
			topicManager.updateTopicReplies(topic.getTid());

			//更新指定版块的最新发帖数信息
			forumManager.updateLastPost(forum);

			if (reval > 0 && losslessdel < 0) {
				userCreditManager.updateUserCreditsByPosts(post.getUsers().getUid(), -1);
			}
		}

		reqcfg.setUrl("showtopic.action?topicid=" + post.getTopics().getTid());
		if (post.getLayer() == 0) {
			reqcfg.setUrl("showforum.action?forumid=" + post.getForums().getFid());
		}
		reqcfg.setMetaRefresh().setShowBackLink(false).addMsgLine("删除帖子成功, 返回主题");
		return SUCCESS;
	}

	private boolean checkPermission(Posts post, int opinion) throws IOException, ParseException {
		ismoder = moderatorManager.isModer(useradminid, userid, forumid);
		if (userid == post.getUsers().getUid() && !ismoder) {
			if (post.getLayer() < 1 && topic.getReplies() > 0) {
				reqcfg.addErrLine("已经被回复过的主帖不能被删除");
				return false;
			}
			if (Utils.strDateDiffMinutes(post.getPostdatetime(), config.getEdittimelimit()) > 0
					|| post.getUsers().getUid() != userid) { //不是作者或者超过编辑时限
				reqcfg.addErrLine("已经超过了编辑帖子时限,不能删除帖子");
				return false;
			} else {
				allowdelpost = true;
			}

		} else {
			Admingroups admininfo = adminGroupManager.getAdminGroup(useradminid);
			if (admininfo != null) {
				// 如果所属管理组有删帖的管理权限
				if (admininfo.getAllowdelpost() == 1) {
					// 如果是管理员或总版主
					if (moderatorManager.isModer(useradminid, userid, forumid)) {
						allowdelpost = true;
						if (post.getLayer() == 0) { //管理者跳转至删除主题
							response.sendRedirect("topicadmin.action?action=moderate&operat=delete&forumid="
									+ post.getForums().getFid() + "&topicid=" + post.getTopics().getTid());
							return false;
						} else { //跳转至批量删帖
							response.sendRedirect("topicadmin.action?action=moderate&operat=delposts&forumid="
									+ post.getForums().getFid() + "&topicid=" + post.getTopics().getTid() + "&postid="
									+ post.getPid() + "&opinion=" + opinion);
							return false;
						}
					}
				}
			} else {
				allowdelpost = false;
			}

		}

		return true;
	}

	public int getPostid() {
		return postid;
	}

	public Posts getPost() {
		return post;
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

	public int getTopicid() {
		return topicid;
	}

	public String getTopictitle() {
		return topictitle;
	}

	public String getForumnav() {
		return forumnav;
	}

	public Forums getForum() {
		return forum;
	}

	public boolean isIsmoder() {
		return ismoder;
	}

}
