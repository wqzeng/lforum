package com.javaeye.lonlysky.lforum.web;

import org.apache.struts2.config.ParentPackage;

import com.javaeye.lonlysky.lforum.ForumBaseAction;
import com.javaeye.lonlysky.lforum.comm.LForumRequest;
import com.javaeye.lonlysky.lforum.comm.utils.ForumUtils;
import com.javaeye.lonlysky.lforum.comm.utils.Utils;

/**
 * 分栏框架页
 * 
 * @author 黄磊
 *
 */
@ParentPackage("default")
public class FrameAction extends ForumBaseAction {

	private static final long serialVersionUID = 8457934635051918628L;

	@Override
	public String execute() throws Exception {
		pagetitle = "分栏";

		int toframe = LForumRequest.getParamIntValue("f", 1);
		if (toframe == 1) {
			ForumUtils.writeCookie("isframe", 1);
		} else {
			toframe = Utils.null2Int(ForumUtils.getCookie("isframe"));
			if (toframe == -1) {
				toframe = config.getIsframeshow();
			}
		}

		if (toframe == 0) {
			response.sendRedirect("main.action");
		}
		return SUCCESS;
	}

}
