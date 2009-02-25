package com.javaeye.lonlysky.lforum.web.admin.global;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.struts2.config.ParentPackage;
import org.springframework.beans.factory.annotation.Autowired;

import com.javaeye.lonlysky.lforum.AdminBaseAction;
import com.javaeye.lonlysky.lforum.comm.LForumRequest;
import com.javaeye.lonlysky.lforum.comm.utils.Utils;
import com.javaeye.lonlysky.lforum.config.impl.ConfigLoader;
import com.javaeye.lonlysky.lforum.entity.forum.Templates;
import com.javaeye.lonlysky.lforum.service.TemplateManager;

/**
 * 界面与显示方式设置
 * 
 * @author 黄磊
 *
 */
@ParentPackage("default")
public class Global_uiandshowstyleAction extends AdminBaseAction {

	private static final long serialVersionUID = -4231433629975966026L;

	private String templatelist = "";
	private String templatepath = "";
	private int timeout = 0;

	@Autowired
	private TemplateManager templateManager;

	@Override
	public String execute() throws Exception {
		timeout = Math.abs(config.getOnlinetimeout());
		List<Templates> templates = templateManager.getValidTemplateList();
		String scriptstr = "<script type=\"text/javascript\">\r\n";
		scriptstr += "images = new Array();\r\n";
		int i = 0;
		for (Templates template : templates) {
			if (template.getTemplateid() == config.getTemplateid()) {
				templatepath = template.getDirectory();
				templatelist += "<option selected=\"selected\" value=\"" + template.getTemplateid() + "\">"
						+ template.getName() + "</option>";
			} else {
				templatelist += "<option value=\"" + template.getTemplateid() + "\">" + template.getName()
						+ "</option>";
			}
			scriptstr += "images[" + i + "]=\"../../templates/" + template.getDirectory() + "/about.png\";\r\n";
			i++;
		}
		scriptstr += "</script>";
		registerStartupScript("", scriptstr);

		if (LForumRequest.isPost()) {
			String onlinetimeout = LForumRequest.getParamValue("onlinetimeout");
			String maxsigrows = LForumRequest.getParamValue("maxsigrows");
			String visitedforums = LForumRequest.getParamValue("visitedforums");
			String smiliesmax = LForumRequest.getParamValue("smiliesmax");
			Map<String, String> checkMap = new HashMap<String, String>();
			checkMap.put("无动作离线时间", onlinetimeout);
			checkMap.put("最大签名高度", maxsigrows);
			checkMap.put("显示最近访问论坛数量", visitedforums);
			checkMap.put("帖子中同一表情符出现的最大次数", smiliesmax);
			for (Entry<String, String> entry : checkMap.entrySet()) {
				if (!Utils.isInt(entry.getValue())) {
					registerStartupScript("", "<script>alert('输入错误:" + entry.getKey()
							+ ",只能是0或者正整数');window.location.href='global_uiandshowstyle.action';</script>");
					return SUCCESS;
				}
			}

			if (Utils.null2Int(onlinetimeout) <= 0) {
				registerStartupScript("", "<script>alert('无动作离线时间必须大于0');</script>");
				return SUCCESS;
			}
			if (Utils.null2Int(maxsigrows) > 9999 || (Utils.null2Int(maxsigrows) < 0)) {
				registerStartupScript("",
						"<script>alert('最大签名高度只能在0-9999之间');window.location.href='global_uiandshowstyle.action';</script>");
				return SUCCESS;
			}

			if (Utils.null2Int(visitedforums) > 9999 || (Utils.null2Int(visitedforums) < 0)) {
				registerStartupScript("",
						"<script>alert('显示最近访问论坛数量只能在0-9999之间');window.location.href='global_uiandshowstyle.action';</script>");
				return SUCCESS;
			}

			if (Utils.null2Int(smiliesmax) > 1000 || (Utils.null2Int(smiliesmax) < 0)) {
				registerStartupScript("",
						"<script>alert('帖子中同一表情符出现的最大次数只能在0-1000之间');window.location.href='global_uiandshowstyle.action';</script>");
				return SUCCESS;
			}
			int viewnewtopicminute = LForumRequest.getParamIntValue("viewnewtopicminute", 0);
			if (viewnewtopicminute > 14400 || viewnewtopicminute < 5) {
				registerStartupScript("",
						"<script>alert('查看新帖的设置必须在5-14400之间');window.location.href='global_uiandshowstyle.action';</script>");
				return SUCCESS;
			}

			config.setTemplateid(LForumRequest.getParamIntValue("templateid", config.getTemplateid()));
			config.setSubforumsindex(1);
			config.setStylejump(LForumRequest.getParamIntValue("stylejump", config.getStylejump()));
			config.setIsframeshow(LForumRequest.getParamIntValue("isframeshow", config.getIsframeshow()));
			config
					.setWhosonlinestatus(LForumRequest.getParamIntValue("whosonlinestatus", config
							.getWhosonlinestatus()));

			if (LForumRequest.getParamIntValue("showauthorstatusinpost", 1) == 1)
				config.setOnlinetimeout(0 - Utils.null2Int(onlinetimeout));
			else
				config.setOnlinetimeout(Utils.null2Int(onlinetimeout));
			config.setMaxonlinelist(LForumRequest.getParamIntValue("maxonlinelist", config.getMaxonlinelist()));
			config.setForumjump(LForumRequest.getParamIntValue("forumjump", config.getForumjump()));
			config.setSmileyinsert(LForumRequest.getParamIntValue("smileyinsert", config.getSmileyinsert()));
			config.setVisitedforums(Utils.null2Int(visitedforums));
			config.setModdisplay(LForumRequest.getParamIntValue("moddisplay", config.getModdisplay()));
			config.setShowavatars(LForumRequest.getParamIntValue("showavatars", config.getShowavatars()));
			config.setShowsignatures(LForumRequest.getParamIntValue("showsignatures", config.getShowsignatures()));
			config.setShowimages(LForumRequest.getParamIntValue("showimages", config.getShowimages()));
			config.setSmiliesmax(LForumRequest.getParamIntValue("smiliesmax", config.getSmiliesmax()));
			config.setMaxsigrows(LForumRequest.getParamIntValue("maxsigrows", config.getMaxsigrows()));
			config.setViewnewtopicminute(LForumRequest.getParamIntValue("viewnewtopicminute", config
					.getViewnewtopicminute()));
			config.setWhosonlinecontract(LForumRequest.getParamIntValue("whosonlinecontract", config
					.getWhosonlinecontract()));

			ConfigLoader configLoader = new ConfigLoader(config.getWebpath());
			configLoader.saveConfig(config);
			adminVistLogManager.insertLog(user, username, usergroup, grouptitle, ip, "界面与显示方式设置", "");
			registerStartupScript("PAGE", "window.location.href='global_uiandshowstyle.action';");
		}
		return SUCCESS;
	}

	public int getTimeout() {
		return timeout;
	}

	public String getTemplatepath() {
		return templatepath;
	}

	public String getTemplatelist() {
		return templatelist;
	}
}
