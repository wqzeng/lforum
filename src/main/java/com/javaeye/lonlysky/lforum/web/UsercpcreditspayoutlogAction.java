package com.javaeye.lonlysky.lforum.web;

import java.util.ArrayList;
import java.util.List;

import org.apache.struts2.config.ParentPackage;
import org.springframework.beans.factory.annotation.Autowired;

import com.javaeye.lonlysky.lforum.ForumBaseAction;
import com.javaeye.lonlysky.lforum.comm.LForumRequest;
import com.javaeye.lonlysky.lforum.comm.utils.ForumUtils;
import com.javaeye.lonlysky.lforum.entity.forum.Paymentlog;
import com.javaeye.lonlysky.lforum.entity.forum.Users;
import com.javaeye.lonlysky.lforum.service.PaymentLogManager;

/**
 * 收入记录
 * 
 * @author 黄磊
 *
 */
@ParentPackage("default")
public class UsercpcreditspayoutlogAction extends ForumBaseAction {

	private static final long serialVersionUID = -4383505224558518539L;

	/**
	 * 当前页码
	 */
	private int pageid;

	/**
	 * 积分收入日志数
	 */
	private int payoutlogcount;

	/**
	 * 分页页数
	 */
	private int pagecount;

	/**
	 * 分页页码链接
	 */
	private String pagenumbers;

	/**
	 * 积分收入日志列表
	 */
	private List<Paymentlog> payloglist = new ArrayList<Paymentlog>();

	/**
	 * 当前用户信息
	 */
	private Users user = new Users();

	private int pagesize = 16;

	@Autowired
	private PaymentLogManager paymentLogManager;

	@Override
	public String execute() throws Exception {
		pagetitle = "用户控制面板";

		if (userid == -1) {
			reqcfg.addErrLine("你尚未登录");

			return SUCCESS;
		}

		user = userManager.getUserInfo(userid);
		//得到当前用户请求的页数
		pageid = LForumRequest.getParamIntValue("page", 1);
		//获取主题总数
		payoutlogcount = paymentLogManager.getPaymentLogOutRecordCount(userid);
		//获取总页数
		pagecount = payoutlogcount % pagesize == 0 ? payoutlogcount / pagesize : payoutlogcount / pagesize + 1;
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

		//获取收入记录并分页显示
		payloglist = paymentLogManager.getPayLogOutList(pagesize, pageid, userid);

		pagenumbers = ForumUtils.getPageNumbers(pageid, pagecount, "usercpcreditspayoutlog.action", 8);
		return SUCCESS;
	}

	public int getPageid() {
		return pageid;
	}

	public int getPayoutlogcount() {
		return payoutlogcount;
	}

	public int getPagecount() {
		return pagecount;
	}

	public String getPagenumbers() {
		return pagenumbers;
	}

	public List<Paymentlog> getPayloglist() {
		return payloglist;
	}

	public Users getUser() {
		return user;
	}

	public int getPagesize() {
		return pagesize;
	}
}
