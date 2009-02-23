package com.javaeye.lonlysky.lforum.web.tools;

import org.apache.struts2.config.ParentPackage;
import org.springframework.beans.factory.annotation.Autowired;

import com.javaeye.lonlysky.lforum.BaseAction;
import com.javaeye.lonlysky.lforum.config.impl.ConfigLoader;
import com.javaeye.lonlysky.lforum.service.FeedManager;

/**
 * 百度协议页面类
 * 
 * @author 黄磊
 *
 */
@ParentPackage("default")
public class SitemapAction extends BaseAction {

	private static final long serialVersionUID = -5452063914594644627L;

	@Autowired
	private FeedManager feedManager;

	@Override
	public String execute() throws Exception {
		if (ConfigLoader.getConfig().getSitemapstatus() == 1) {
			renderXML(feedManager.getBaiduSitemap(ConfigLoader.getConfig().getSitemapttl()));
			return null;
		}
		String string = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n<Document>Sitemap is forbidden</Document>\r\n";
		renderXML(string);
		return null;
	}
}
