package com.javaeye.lonlysky.lforum.web.admin.global;

import java.util.HashMap;
import java.util.Map;

import org.apache.struts2.config.ParentPackage;

import com.javaeye.lonlysky.lforum.AdminBaseAction;
import com.javaeye.lonlysky.lforum.comm.LForumRequest;
import com.javaeye.lonlysky.lforum.comm.utils.Utils;
import com.javaeye.lonlysky.lforum.config.impl.ConfigLoader;

/**
 * 时间段设置
 *  
 * @author 黄磊
 *
 */
@ParentPackage("default")
public class Global_timespanAction extends AdminBaseAction {

	private static final long serialVersionUID = 8015243525791188041L;

	private String visitbanperiods_posttextarea;
	private String postmodperiods_posttextarea;
	private String searchbanperiods_posttextarea;
	private String postbanperiods_posttextarea;
	private String attachbanperiods_posttextarea;

	@Override
	public String execute() throws Exception {
		if (LForumRequest.isPost()) {
			Map<String, String> timeMap = new HashMap<String, String>();
			timeMap.put("禁止访问时间段", visitbanperiods_posttextarea);
			timeMap.put("禁止发帖时间段", postbanperiods_posttextarea);
			timeMap.put("发帖审核时间段", postmodperiods_posttextarea);
			timeMap.put("禁止下载附件时间段", attachbanperiods_posttextarea);
			timeMap.put("禁止全文搜索时间段", searchbanperiods_posttextarea);
			Map<Integer, String> keyMap = new HashMap<Integer, String>();
			if (!Utils.isRuleTip(timeMap, "timesect", keyMap)) {
				registerStartupScript("erro", "<script>alert('" + keyMap.get(0) + ",时间格式错误');</script>");
				return SUCCESS;
			}

			config.setVisitbanperiods(visitbanperiods_posttextarea);
			config.setPostbanperiods(postbanperiods_posttextarea);
			config.setPostmodperiods(postmodperiods_posttextarea);
			config.setSearchbanperiods(searchbanperiods_posttextarea);
			config.setAttachbanperiods(attachbanperiods_posttextarea);

			ConfigLoader configLoader = new ConfigLoader(config.getWebpath());
			configLoader.saveConfig(config);
			adminVistLogManager.insertLog(user, username, usergroup, grouptitle, ip, "时间段设置", "");
			registerStartupScript("PAGE", "window.location.href='global_timespan.action';");
		}
		return SUCCESS;
	}

	public String getVisitbanperiods_posttextarea() {
		return visitbanperiods_posttextarea;
	}

	public void setVisitbanperiods_posttextarea(String visitbanperiods_posttextarea) {
		this.visitbanperiods_posttextarea = visitbanperiods_posttextarea;
	}

	public String getPostmodperiods_posttextarea() {
		return postmodperiods_posttextarea;
	}

	public void setPostmodperiods_posttextarea(String postmodperiods_posttextarea) {
		this.postmodperiods_posttextarea = postmodperiods_posttextarea;
	}

	public String getSearchbanperiods_posttextarea() {
		return searchbanperiods_posttextarea;
	}

	public void setSearchbanperiods_posttextarea(String searchbanperiods_posttextarea) {
		this.searchbanperiods_posttextarea = searchbanperiods_posttextarea;
	}

	public String getPostbanperiods_posttextarea() {
		return postbanperiods_posttextarea;
	}

	public void setPostbanperiods_posttextarea(String postbanperiods_posttextarea) {
		this.postbanperiods_posttextarea = postbanperiods_posttextarea;
	}

	public String getAttachbanperiods_posttextarea() {
		return attachbanperiods_posttextarea;
	}

	public void setAttachbanperiods_posttextarea(String attachbanperiods_posttextarea) {
		this.attachbanperiods_posttextarea = attachbanperiods_posttextarea;
	}

}
