package com.javaeye.lonlysky.lforum.web;

import java.util.ArrayList;
import java.util.List;

import org.apache.struts2.config.ParentPackage;
import org.springframework.beans.factory.annotation.Autowired;

import com.javaeye.lonlysky.lforum.ForumBaseAction;
import com.javaeye.lonlysky.lforum.comm.LForumRequest;
import com.javaeye.lonlysky.lforum.comm.utils.ForumUtils;
import com.javaeye.lonlysky.lforum.comm.utils.Utils;
import com.javaeye.lonlysky.lforum.entity.forum.Pms;
import com.javaeye.lonlysky.lforum.entity.forum.Users;
import com.javaeye.lonlysky.lforum.service.MessageManager;

/**
 * 草稿箱页面
 * 
 * @author 黄磊
 *
 */
@ParentPackage("default")
public class UsercpdraftboxAction extends ForumBaseAction {

	private static final long serialVersionUID = 8527697356233735565L;

	/**
	 * 短消息列表
	 */
	private List<Pms> pmlist = new ArrayList<Pms>();

	/**
	 * 当前页码
	 */
	private int pageid;

	/**
	 * 短消息数
	 */
	public int pmcount;

	/**
	 * 分页页数
	 */
	private int pagecount;

	/**
	 * 分页页码链接
	 */
	private String pagenumbers;

	/**
	 * 用户最大短消息数
	 */
	private int maxmsg;

	/**
	 * 已使用的短消息数
	 */
	private int usedmsgcount;

	/**
	 * 已使用的短消息条宽度
	 */
	private int usedmsgbarwidth;

	/**
	 * 未使用的短消息条宽度
	 */
	private int unusedmsgbarwidth;

	/**
	 * 当前用户信息
	 */
	private Users user = new Users();

	@Autowired
	private MessageManager messageManager;

	@Override
	public String execute() throws Exception {
		pagetitle = "短消息草稿箱";

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
			if (LForumRequest.getParamValues("pmitemid") == null) {
				reqcfg.addErrLine("您未选中任何短消息，当前操作失败！");
				return SUCCESS;
			}

			if (!Utils.isIntArray(LForumRequest.getParamValues("pmitemid"))) {
				reqcfg.addErrLine("参数信息错误！");
				return SUCCESS;
			}

			String[] pmitemid = LForumRequest.getParamValues("pmitemid");

			int retval = messageManager.deletePrivateMessage(userid, pmitemid);

			if (retval == -1) {
				reqcfg.addErrLine("参数无效");
				return SUCCESS;
			}

			reqcfg.setShowBackLink(false).addMsgLine("删除完毕");
		} else {
			bindItems();
		}
		return SUCCESS;
	}

	/**
	 * 加载用户当前请求页数的短消息列表并装入List
	 */
	private void bindItems() {
		//得到当前用户请求的页数
		pageid = LForumRequest.getParamIntValue("page", 1);
		//获取主题总数
		pmcount = messageManager.getPrivateMessageCount(userid, 0);
		//获取总页数
		pagecount = pmcount % 16 == 0 ? pmcount / 16 : pmcount / 16 + 1;
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

		usedmsgcount = messageManager.getPrivateMessageCount(userid, -1);
		maxmsg = usergroupinfo.getMaxpmnum();

		if (maxmsg <= 0) {
			usedmsgbarwidth = 0;
			unusedmsgbarwidth = 0;
		} else {
			usedmsgbarwidth = usedmsgcount * 100 / maxmsg;
			unusedmsgbarwidth = 100 - usedmsgbarwidth;
		}

		pmlist = messageManager.getPrivateMessageList(userid, 2, 16, pageid, 2);
		pagenumbers = ForumUtils.getPageNumbers(pageid, pagecount, "usercpdraftbox.action", 8);
	}

	public List<Pms> getPmlist() {
		return pmlist;
	}

	public int getPageid() {
		return pageid;
	}

	public int getPmcount() {
		return pmcount;
	}

	public int getPagecount() {
		return pagecount;
	}

	public String getPagenumbers() {
		return pagenumbers;
	}

	public int getMaxmsg() {
		return maxmsg;
	}

	public int getUsedmsgcount() {
		return usedmsgcount;
	}

	public int getUsedmsgbarwidth() {
		return usedmsgbarwidth;
	}

	public int getUnusedmsgbarwidth() {
		return unusedmsgbarwidth;
	}

	public Users getUser() {
		return user;
	}
}
