package com.javaeye.lonlysky.lforum.web;

import java.util.List;

import org.apache.struts2.config.ParentPackage;
import org.springframework.beans.factory.annotation.Autowired;

import com.javaeye.lonlysky.lforum.ForumBaseAction;
import com.javaeye.lonlysky.lforum.comm.LForumRequest;
import com.javaeye.lonlysky.lforum.comm.utils.ForumUtils;
import com.javaeye.lonlysky.lforum.entity.forum.Myattachments;
import com.javaeye.lonlysky.lforum.entity.forum.Users;
import com.javaeye.lonlysky.lforum.entity.global.AttachmentType;
import com.javaeye.lonlysky.lforum.service.AttachmentManager;

/**
 * 我的附件
 * 
 * @author 黄磊
 *
 */
@ParentPackage("default")
public class MyattachmentAction extends ForumBaseAction {

	private static final long serialVersionUID = -2635729075143906294L;

	/**
	 * 帖子所属的主题列表
	 */
	private List<Myattachments> myattachmentlist;

	private List<AttachmentType> typelist;

	/**
	 * 当前页码
	 */
	private int pageid;

	/**
	 * 总页数
	 */
	private int pagecount;

	/**
	 * 附件总数
	 */
	private int attachmentcount;

	/**
	 * 分页页码链接
	 */
	private String pagenumbers;

	/**
	 * 当前登录的用户信息
	 */
	private Users user = new Users();

	/**
	 * 文件类型
	 */
	private int typeid = 0;

	private int pagesize = 16;

	@Autowired
	private AttachmentManager attachmentManager;

	@Override
	public String execute() throws Exception {
		String linkurl = "";
		pagetitle = "用户控制面板";

		if (userid == -1) {
			reqcfg.addErrLine("你尚未登录");
			return SUCCESS;
		}

		user = userManager.getUserInfo(userid);
		pageid = LForumRequest.getParamIntValue("page", 1);
		typeid = LForumRequest.getParamIntValue("typeid", 0);

		typelist = attachmentManager.attachTypeList();

		if (typeid > 0) {
			attachmentcount = attachmentManager.getUserAttachmentCount(userid, typeid);
			linkurl = "myattachment.action?typeid=" + typeid;
		} else {
			attachmentcount = attachmentManager.getUserAttachmentCount(userid);
			linkurl = "myattachment.aspx";
		}
		pagecount = attachmentcount % pagesize == 0 ? attachmentcount / pagesize : attachmentcount / pagesize + 1;
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
		myattachmentlist = attachmentManager.getAttachmentByUid(userid, typeid, pageid, pagesize);

		pagenumbers = ForumUtils.getPageNumbers(pageid, pagecount, linkurl, 10);
		return SUCCESS;
	}

	public List<Myattachments> getMyattachmentlist() {
		return myattachmentlist;
	}

	public List<AttachmentType> getTypelist() {
		return typelist;
	}

	public int getPageid() {
		return pageid;
	}

	public int getPagecount() {
		return pagecount;
	}

	public int getAttachmentcount() {
		return attachmentcount;
	}

	public String getPagenumbers() {
		return pagenumbers;
	}

	public Users getUser() {
		return user;
	}

	public int getTypeid() {
		return typeid;
	}

	public int getPagesize() {
		return pagesize;
	}
}
