package com.javaeye.lonlysky.lforum.web.tools;

import org.apache.struts2.config.ParentPackage;
import org.springframework.beans.factory.annotation.Autowired;

import com.javaeye.lonlysky.lforum.ForumBaseAction;
import com.javaeye.lonlysky.lforum.comm.LForumRequest;
import com.javaeye.lonlysky.lforum.entity.forum.Forums;
import com.javaeye.lonlysky.lforum.service.FeedManager;
import com.javaeye.lonlysky.lforum.service.ForumManager;

/**
 * RSS页面类
 *  
 * @author 黄磊
 *
 */
@ParentPackage("default")
public class RssAction extends ForumBaseAction {

	private static final long serialVersionUID = 556390719585806889L;

	@Autowired
	private FeedManager feedManager;

	@Autowired
	private ForumManager forumManager;

	@Override
	public String execute() throws Exception {
		if (config.getRssstatus() == 1) {
			int forumid = LForumRequest.getParamIntValue("forumid", -1);
			if (forumid == -1) {
				responseXML(feedManager.getRssXml(config.getRssttl()), false);
				return null;
			} else {
				Forums forum = forumManager.getForumInfo(forumid);
				if (forum != null) {
					if (forum.getAllowrss() == 1) {
						responseXML(feedManager.getForumRssXml(config.getRssttl(), forumid), false);
						return null;
					}
				}
			}
		}
		responseXML("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n<Rss>Error</Rss>\r\n", false);
		return null;
	}
}
