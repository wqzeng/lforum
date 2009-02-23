package com.javaeye.lonlysky.lforum.web;

import org.apache.struts2.config.ParentPackage;
import org.springframework.beans.factory.annotation.Autowired;

import com.javaeye.lonlysky.lforum.ForumBaseAction;
import com.javaeye.lonlysky.lforum.comm.LForumRequest;
import com.javaeye.lonlysky.lforum.comm.utils.ForumUtils;
import com.javaeye.lonlysky.lforum.comm.utils.Utils;
import com.javaeye.lonlysky.lforum.entity.forum.Forums;
import com.javaeye.lonlysky.lforum.entity.forum.Topics;
import com.javaeye.lonlysky.lforum.service.FavoriteManager;
import com.javaeye.lonlysky.lforum.service.ForumManager;
import com.javaeye.lonlysky.lforum.service.TopicManager;

/**
 * 添加收藏页
 * 
 * @author 黄磊
 *
 */
@ParentPackage("default")
public class FavoritesAction extends ForumBaseAction {

	private static final long serialVersionUID = 1302503055238336601L;

	/**
	 * 将要收藏的主题信息
	 */
	private Topics topic;

	/**
	 * 主题所属版块
	 */
	private int forumid;

	/**
	 * 主题所属版块名称
	 */
	private String forumname;

	/**
	 * 主题Id
	 */
	private int topicid;
	/**
	 * 主题标题
	 */
	private String topictitle;

	/**
	 * 论坛导航信息
	 */
	private String forumnav;

	/**
	 * 主题所属版块
	 */
	private Forums forum;

	@Autowired
	private TopicManager topicManager;

	@Autowired
	private FavoriteManager favoriteManager;

	@Autowired
	private ForumManager forumManager;

	@Override
	public String execute() throws Exception {
		if (userid == -1) {
			reqcfg.addErrLine("你尚未登录");
			return SUCCESS;
		}

		String referer = ForumUtils.getCookie("referer");

		// 获取主题ID
		topicid = LForumRequest.getParamIntValue("topicid", -1);

		if (topicid != -1)//收藏的是主题
		{
			// 获取该主题的信息
			Topics topic = topicManager.getTopicInfo(topicid);
			// 如果该主题不存在
			if (topic == null) {
				reqcfg.addErrLine("不存在的主题ID");
				return SUCCESS;
			}

			topictitle = topic.getTitle();
			forumid = topic.getForums().getFid();
			forum = forumManager.getForumInfo(forumid);
			forumname = forum.getName();
			pagetitle = Utils.cleanHtmlTag(forum.getName());
			forumnav = forum.getPathlist();

			// 检查用户是否拥有足够权限                
			if (config.getMaxfavorites() <= favoriteManager.getFavoritesCount(userid)) {
				reqcfg.addErrLine("您收藏的主题数目已经达到系统设置的数目上限");
				return SUCCESS;
			}

			if (favoriteManager.checkFavoritesIsIN(userid, topicid) != 0) {
				reqcfg.addErrLine("您过去已经收藏过这个主题,请返回");
				return SUCCESS;
			}

			favoriteManager.createFavorites(userid, topicid);
			reqcfg.addMsgLine("指定主题已成功添加到收藏夹中,现在将回到上一页").setUrl(referer).setMetaRefresh().setShowBackLink(false);

		}

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
}
