package com.javaeye.lonlysky.lforum.web;

import java.util.List;

import org.apache.struts2.config.ParentPackage;
import org.springframework.beans.factory.annotation.Autowired;

import com.javaeye.lonlysky.lforum.ForumBaseAction;
import com.javaeye.lonlysky.lforum.comm.LForumRequest;
import com.javaeye.lonlysky.lforum.comm.utils.ForumUtils;
import com.javaeye.lonlysky.lforum.comm.utils.Utils;
import com.javaeye.lonlysky.lforum.entity.forum.Favorites;
import com.javaeye.lonlysky.lforum.entity.forum.Users;
import com.javaeye.lonlysky.lforum.service.FavoriteManager;

/**
 * 查看主题订阅页面
 * 
 * @author 黄磊
 *
 */
@ParentPackage("default")
public class UsercpsubscribeAction extends ForumBaseAction {

	private static final long serialVersionUID = -1936464762288038113L;

	/**
	 * 收藏列表
	 */
	private List<Favorites> favoriteslist;

	/**
	 * 当前页码
	 */
	private int pageid;

	/**
	 * 分页页数
	 */
	private int pagecount;

	/**
	 * 分页页码链接
	 */
	private String pagenumbers;

	/**
	 * 收藏的主题数量
	 */
	private int topiccount;

	/**
	 * 当前用户信息
	 */
	private Users user = new Users();

	@Autowired
	private FavoriteManager favoriteManager;

	@Override
	public String execute() throws Exception {
		pagetitle = "用户控制面板";

		if (userid == -1) {
			reqcfg.addErrLine("你尚未登录");
			return SUCCESS;
		}
		user = userManager.getUserInfo(userid);

		if (LForumRequest.isPost()) {
			if (ForumUtils.isCrossSitePost()) {
				reqcfg
						.addErrLine("您的请求来路不正确，无法提交。如果您安装了某种默认屏蔽来路信息的个人防火墙软件(如 Norton Internet Security)，请设置其不要禁止来路信息后再试。");
				return SUCCESS;
			}

			if (LForumRequest.getParamValues("titemid") == null) {
				reqcfg.addErrLine("您未选中任何数据信息，当前操作失败！");
				return SUCCESS;
			}

			if (!Utils.isIntArray(LForumRequest.getParamValues("titemid"))) {
				reqcfg.addErrLine("参数无效");
				return SUCCESS;
			}
			int retval = favoriteManager.deleteFavorites(userid, LForumRequest.getParamValues("titemid"), 0);

			if (retval == -1) {
				reqcfg.addErrLine("参数无效");
				return SUCCESS;
			}

			reqcfg.setShowBackLink(false).addMsgLine("删除完毕");
		} else {
			//得到当前用户请求的页数
			pageid = LForumRequest.getParamIntValue("page", 1);
			//获取主题总数
			topiccount = favoriteManager.getFavoritesCount(userid);
			//获取总页数
			pagecount = topiccount % 16 == 0 ? topiccount / 16 : topiccount / 16 + 1;
			if (pagecount == 0) {
				pagecount = 1;
			}
			//修正请求页数中可能的错误
			if (pageid < 1) {
				pageid = 1;
			}
			if (pageid > pagecount) {
				pageid = pagecount;
			}

			favoriteslist = favoriteManager.getFavoritesList(userid, 16, pageid);

			pagenumbers = ForumUtils.getPageNumbers(pageid, pagecount, "usercpsubscribe.action", 8);
		}
		return SUCCESS;
	}

	public List<Favorites> getFavoriteslist() {
		return favoriteslist;
	}

	public int getPageid() {
		return pageid;
	}

	public int getPagecount() {
		return pagecount;
	}

	public String getPagenumbers() {
		return pagenumbers;
	}

	public Users getUser() {
		return user;
	}

	public int getTopiccount() {
		return topiccount;
	}
}
