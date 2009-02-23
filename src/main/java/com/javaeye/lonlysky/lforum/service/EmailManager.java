package com.javaeye.lonlysky.lforum.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.javaeye.lonlysky.lforum.comm.utils.EmailUtil;
import com.javaeye.lonlysky.lforum.config.impl.ConfigLoader;
import com.javaeye.lonlysky.lforum.config.impl.EmailConfigLoader;
import com.javaeye.lonlysky.lforum.entity.global.EmailConfigInfo;

/**
 * 邮件发送类的调用封装类
 * 
 * @author 黄磊
 *
 */
@Service
@Transactional
public class EmailManager {

	private static final Logger logger = LoggerFactory.getLogger(EmailManager.class);

	/**
	 * 发送用户注册信息
	 * 
	 * @param email
	 * @return
	 */
	public boolean sendRegMessage(String email) {
		EmailConfigInfo emailInfo = EmailConfigLoader.getInstance().getEmailConfigInfo();
		String forumurl = ConfigLoader.getConfig().getForumurl();

		String body = emailInfo.getEmailcontent().replace("WEBTITLE", ConfigLoader.getConfig().getWebtitle());
		body = body.replace("WEBURL", "<a href=" + ConfigLoader.getConfig().getWeburl() + ">"
				+ ConfigLoader.getConfig().getWeburl() + "</a>");
		body = body.replace("FORUMURL", "<a href=" + forumurl + ">" + forumurl + "</a>");
		body = body.replace("FORUMTITLE", ConfigLoader.getConfig().getForumtitle());
		body = "<pre style=\"width:100%;word-wrap:break-word\">" + body + "</pre>";
		String subject = "已成功创建你的 " + ConfigLoader.getConfig().getForumtitle() + "帐户,请查收.";

		String[] mailto = { email };
		try {
			EmailUtil.sendMail(emailInfo.getSmtp(), emailInfo.getSysemail(), emailInfo.getPort() + "", emailInfo
					.getUsername(), emailInfo.getPassword(), mailto, 1, subject, body);
			if (logger.isDebugEnabled()) {
				logger.debug("发送用户{}注册Email成功", email);
			}
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * 发送用户注册信息
	 * 
	 * @param email
	 * @return
	 */
	public boolean sendRegMessage(String email, String authstr) {
		EmailConfigInfo emailInfo = EmailConfigLoader.getInstance().getEmailConfigInfo();
		String forumurl = ConfigLoader.getConfig().getForumurl();

		String body = emailInfo.getEmailcontent().replace("WEBTITLE", ConfigLoader.getConfig().getWebtitle());
		body = body.replace("WEBURL", "<a href=" + ConfigLoader.getConfig().getWeburl() + ">"
				+ ConfigLoader.getConfig().getWeburl() + "</a>");
		body = body.replace("FORUMURL", "<a href=" + forumurl + ">" + forumurl + "</a>");
		body = body.replace("FORUMTITLE", ConfigLoader.getConfig().getForumtitle());
		body = "<pre style=\"width:100%;word-wrap:break-word\">" + body + "\r\n\r\n" + "激活您帐户的链接为:<a href=" + forumurl
				+ "activationuser.action?authstr=" + authstr.trim() + "  target=_blank>" + forumurl
				+ "activationuser.actopm?authstr=" + authstr.trim() + "</a></pre>";
		String subject = "已成功创建你的 " + ConfigLoader.getConfig().getForumtitle() + "帐户,请查收.";

		String[] mailto = { email };
		try {
			EmailUtil.sendMail(emailInfo.getSmtp(), emailInfo.getSysemail(), emailInfo.getPort() + "", emailInfo
					.getUsername(), emailInfo.getPassword(), mailto, 1, subject, body);
			if (logger.isDebugEnabled()) {
				logger.debug("发送用户{}注册Email成功，并发送激活码{}", email, authstr);
			}
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * 邮件通知服务
	 * @param email email地址
	 * @param title 邮件的标题
	 * @param body 邮件内容
	 * @return
	 */
	public boolean sendEmailNotify(String email, String title, String body) {
		EmailConfigInfo emailInfo = EmailConfigLoader.getInstance().getEmailConfigInfo();
		body = "<pre style=\"width:100%;word-wrap:break-word\">" + body + "</pre>";
		String[] mailto = { email };
		try {
			EmailUtil.sendMail(emailInfo.getSmtp(), emailInfo.getSysemail(), emailInfo.getPort() + "", emailInfo
					.getUsername(), emailInfo.getPassword(), mailto, 1, title, body);
			if (logger.isDebugEnabled()) {
				logger.debug("邮件通知服务{}", email);
			}
			return true;
		} catch (Exception e) {
			return false;
		}
	}
}
