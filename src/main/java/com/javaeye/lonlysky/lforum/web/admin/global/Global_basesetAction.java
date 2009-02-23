package com.javaeye.lonlysky.lforum.web.admin.global;

import org.apache.struts2.config.ParentPackage;
import org.springframework.beans.factory.annotation.Autowired;

import com.javaeye.lonlysky.lforum.AdminBaseAction;
import com.javaeye.lonlysky.lforum.cache.LForumCache;
import com.javaeye.lonlysky.lforum.comm.LForumRequest;
import com.javaeye.lonlysky.lforum.config.impl.ConfigLoader;
import com.javaeye.lonlysky.lforum.service.TopicStatManager;

/**
 * 后台基本设置
 * 
 * @author 黄磊
 *
 */
@ParentPackage("default")
public class Global_basesetAction extends AdminBaseAction {

	private static final long serialVersionUID = -2879642345315462691L;

	@Autowired
	private TopicStatManager topicStatManager;

	@Override
	public String execute() throws Exception {
		if (LForumRequest.isPost()) {
			config.setForumtitle(LForumRequest.getParamValue("forumtitle"));
			config.setForumurl(LForumRequest.getParamValue("forumurl"));
			config.setWebtitle(LForumRequest.getParamValue("webtitle"));
			config.setWeburl(LForumRequest.getParamValue("weburl"));
			config.setClosed(LForumRequest.getParamIntValue("closed", 0));
			config.setClosedreason(LForumRequest.getParamValue("closedreason_posttextarea"));
			config.setIcp(LForumRequest.getParamValue("icp"));
			config.setRssttl(LForumRequest.getParamIntValue("rssttl", 60));
			config.setSitemapttl(LForumRequest.getParamIntValue("sitemapttl", 12));
			config.setNocacheheaders(LForumRequest.getParamIntValue("nocacheheaders", 0));
			config.setMaxmodworksmonths(3);
			config.setRssstatus(LForumRequest.getParamIntValue("rssstatus", 0));
			config.setSitemapstatus(LForumRequest.getParamIntValue("sitemapstatus", 0));
			config.setCookiedomain(LForumRequest.getParamValue("CookieDomain"));
			config.setMemliststatus(LForumRequest.getParamIntValue("memliststatus", 0));
			config.setLinktext(LForumRequest.getParamValue("Linktext_posttextarea"));
			config.setStatcode(LForumRequest.getParamValue("Statcode_posttextarea"));

			ConfigLoader configLoader = new ConfigLoader(config.getWebpath());
			configLoader.saveConfig(config);
			LForumCache.getInstance().removeCache("ForumList");
			topicStatManager.setQueueCount();
			adminVistLogManager.insertLog(user, username, usergroup, grouptitle, ip, "基本设置", "");
			registerStartupScript("page", "window.location.href='global_baseset.action';");
		}
		return SUCCESS;
	}
}
