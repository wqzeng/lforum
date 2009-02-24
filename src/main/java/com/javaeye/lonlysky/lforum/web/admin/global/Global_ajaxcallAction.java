package com.javaeye.lonlysky.lforum.web.admin.global;

import org.springframework.beans.factory.annotation.Autowired;

import com.javaeye.lonlysky.lforum.AdminBaseAction;
import com.javaeye.lonlysky.lforum.comm.LForumRequest;
import com.javaeye.lonlysky.lforum.service.admin.AdminStatsManager;

/**
 * 进行论坛统计
 * 
 * @author 黄磊
 *
 */
public class Global_ajaxcallAction extends AdminBaseAction {

	private static final long serialVersionUID = 3605574993476355965L;

	@Autowired
	private AdminStatsManager adminStatsManager;

	@Override
	public String execute() throws Exception {
		int pertask = LForumRequest.getParamIntValue("pertask", 0);
		int lastnumber = LForumRequest.getParamIntValue("lastnumber", 0);
		int startvalue = LForumRequest.getParamIntValue("startvalue", 0);
		int endvalue = LForumRequest.getParamIntValue("endvalue", 0);
		String resultmessage = "";
		String opname = LForumRequest.getParamValue("opname");
		if (opname.equals("ReSetFourmTopicAPost")) {
			lastnumber = adminStatsManager.reSetFourmTopicAPost(pertask, lastnumber);
			resultmessage = lastnumber + "";
		} else if (opname.equals("ReSetUserDigestPosts")) {
			lastnumber = adminStatsManager.reSetUserDigestPosts(pertask, lastnumber);
			resultmessage = lastnumber + "";
		} else if (opname.equals("ReSetUserPosts")) {
			lastnumber = adminStatsManager.reSetUserPosts(pertask, lastnumber);
			resultmessage = lastnumber + "";
		} else if (opname.equals("ReSetTopicPosts")) {
			lastnumber = adminStatsManager.reSetTopicPosts(pertask, lastnumber);
			resultmessage = lastnumber + "";
		} else if (opname.equals("ReSetFourmTopicAPost_StartEnd")) {
			adminStatsManager.reSetFourmTopicAPost(startvalue, endvalue);
			resultmessage = "1";
		} else if (opname.equals("ReSetUserDigestPosts_StartEnd")) {
			adminStatsManager.reSetUserDigestPosts(startvalue, endvalue);
			resultmessage = "1";
		}else if (opname.equals("ReSetUserPosts_StartEnd")) {
			adminStatsManager.reSetUserPosts(startvalue, endvalue);
            resultmessage = "1";
		}else if (opname.equals("ReSetTopicPosts_StartEnd")) {
			adminStatsManager.reSetTopicPosts(startvalue, endvalue);
            resultmessage = "1";
		}
		renderText(resultmessage);
		return null;
	}
}
