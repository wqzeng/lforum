package com.javaeye.lonlysky.lforum.web;

import java.util.ArrayList;
import java.util.List;

import org.apache.struts2.config.ParentPackage;

import com.javaeye.lonlysky.lforum.ForumBaseAction;
import com.javaeye.lonlysky.lforum.comm.LForumRequest;
import com.javaeye.lonlysky.lforum.comm.utils.ForumUtils;
import com.javaeye.lonlysky.lforum.comm.utils.Utils;

/**
 * 用户列表页面
 * 
 * @author 黄磊
 *
 */
@ParentPackage("default")
public class ShowuserAction extends ForumBaseAction {

	private static final long serialVersionUID = -5279325169212466010L;

	/**
	 * 用户列表
	 */
	private List<Object[]> userlist;

	/**
	 * 当前页码
	 */
	private int pageid;

	/**
	 * 总用户数
	 */
	private int totalusers;

	/**
	 * 分页页数
	 */
	private int pagecount;

	/**
	 * 分页页码链接
	 */
	private String pagenumbers;

	@Override
	public String execute() throws Exception {
		pagetitle = "用户";

		userlist = new ArrayList<Object[]>();
		;
		if (config.getMemliststatus() != 1) {
			reqcfg.addErrLine("系统不允许查看用户列表");
			return SUCCESS;
		}

		String orderby = LForumRequest.getParamValue("orderby");
		//进行参数过滤
		if (!orderby.equals("") && !Utils.inArray(orderby, "uid,username,credits,posts,admin,joindate,lastactivity")) {
			orderby = "uid";
		}

		String ordertype = LForumRequest.getParamValue("ordertype");
		//进行参数过滤      
		if (!ordertype.equals("desc") && !ordertype.equals("asc")) {
			ordertype = "desc";
		}

		//得到当前用户请求的页数
		pageid = LForumRequest.getParamIntValue("page", 1);
		if (LForumRequest.getParamValue("orderby").equals("admin")) {
			//获管理组用户总数
			totalusers = userManager.getUserCountByAdmin();
		} else {
			//获取用户总数
			totalusers = userManager.getUserCount();
		}
		//获取总页数
		pagecount = totalusers % 20 == 0 ? totalusers / 20 : totalusers / 20 + 1;
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

		//获取当前页主题列表
		userlist = userManager.getUserList(20, pageid, orderby, ordertype);
		//得到页码链接
		pagenumbers = ForumUtils.getPageNumbers(pageid, pagecount, "showuser.action?orderby=" + orderby + "&ordertype="
				+ ordertype, 8);
		return SUCCESS;
	}

	public List<Object[]> getUserlist() {
		return userlist;
	}

	public int getPageid() {
		return pageid;
	}

	public int getTotalusers() {
		return totalusers;
	}

	public int getPagecount() {
		return pagecount;
	}

	public String getPagenumbers() {
		return pagenumbers;
	}

}
