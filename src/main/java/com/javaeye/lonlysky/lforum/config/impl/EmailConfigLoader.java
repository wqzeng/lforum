package com.javaeye.lonlysky.lforum.config.impl;

import org.dom4j.Document;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.javaeye.lonlysky.lforum.cache.LForumCache;
import com.javaeye.lonlysky.lforum.comm.utils.Utils;
import com.javaeye.lonlysky.lforum.comm.utils.XmlElementUtil;
import com.javaeye.lonlysky.lforum.entity.global.EmailConfigInfo;

/**
 * Email信息加载类
 * 
 * @author 黄磊
 *
 */
public class EmailConfigLoader {

	private static final Logger logger = LoggerFactory.getLogger(EmailConfigLoader.class);
	private static EmailConfigLoader emailConfigLoader;
	private String appPath; // 系统路径

	private EmailConfigLoader() {
		appPath = ConfigLoader.getConfig().getWebpath();
	}

	/**
	 * 获取本类实例
	 * @return
	 */
	public static EmailConfigLoader getInstance() {
		if (emailConfigLoader == null) {
			synchronized (logger) {
				emailConfigLoader = new EmailConfigLoader();
				if (logger.isDebugEnabled()) {
					logger.debug("EmailConfigLoader实例初始化");
				}
			}
		}
		return emailConfigLoader;
	}

	/**
	 * 获取Email配置信息类
	 * 
	 * @return Email配置信息类
	 */
	public EmailConfigInfo getEmailConfigInfo() {
		EmailConfigInfo emailConfigInfo = LForumCache.getInstance().getCache("EmailConfigInfo", EmailConfigInfo.class);
		if (emailConfigInfo == null) {
			emailConfigInfo = new EmailConfigInfo();
			Document document = XmlElementUtil.readXML(appPath + "/WEB-INF/config/email.xml");
			Element root = document.getRootElement();
			emailConfigInfo.setEmailcontent(XmlElementUtil.findElement("Emailcontent", root).getTextTrim());
			emailConfigInfo.setPassword(XmlElementUtil.findElement("Password", root).getTextTrim());
			emailConfigInfo.setPort(Utils.null2Int(XmlElementUtil.findElement("Port", root).getTextTrim()));
			emailConfigInfo.setSmtp(XmlElementUtil.findElement("Smtp", root).getTextTrim());
			emailConfigInfo.setSysemail(XmlElementUtil.findElement("Sysemail", root).getTextTrim());
			emailConfigInfo.setUsername(XmlElementUtil.findElement("Username", root).getTextTrim());
			LForumCache.getInstance().addCache("EmailConfigInfo", emailConfigInfo);
			if (logger.isDebugEnabled()) {
				logger.debug("获取Email配置信息类");
			}
		}
		return emailConfigInfo;
	}

	/**
	 * 保存Email配置信息
	 * 
	 * @param emailConfigInfo Email配置信息
	 */
	public void saveEmailConfig(EmailConfigInfo emailConfigInfo) {
		Document document = XmlElementUtil.readXML(appPath + "/WEB-INF/config/email.xml");
		Element root = document.getRootElement();
		XmlElementUtil.findElement("Emailcontent", root).setText(emailConfigInfo.getEmailcontent());
		XmlElementUtil.findElement("Password", root).setText(emailConfigInfo.getPassword());
		XmlElementUtil.findElement("Port", root).setText(Utils.null2String(emailConfigInfo.getPort()));
		XmlElementUtil.findElement("Smtp", root).setText(emailConfigInfo.getSmtp());
		XmlElementUtil.findElement("Sysemail", root).setText(emailConfigInfo.getSysemail());
		XmlElementUtil.findElement("Username", root).setText(emailConfigInfo.getUsername());
		XmlElementUtil.saveXML(appPath + "/WEB-INF/config/email.xml", document);
		if (logger.isDebugEnabled()) {
			logger.debug("保存Email配置信息");
		}
	}
}
