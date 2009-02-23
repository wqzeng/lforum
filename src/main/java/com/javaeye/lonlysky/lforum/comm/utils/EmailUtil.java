package com.javaeye.lonlysky.lforum.comm.utils;

import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.Message.RecipientType;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sun.misc.BASE64Encoder;

import com.javaeye.lonlysky.lforum.exception.WebException;

/**
 * 本类用于发送邮件<br>
 * 本类主要封装了javaMail中的一些API
 * 
 * @author 黄磊
 * 
 */
public class EmailUtil {

	private static final Logger logger = LoggerFactory.getLogger(EmailUtil.class);

	/**
	 * 发送邮件
	 * 
	 * @param server
	 *            邮件服务器(smtp.163.com)
	 * @param port
	 *            邮件发送端口
	 * @param user
	 *            用户名
	 * @param pwd
	 *            用户密码
	 * @param mailTo
	 *            接收人
	 * @param mailType
	 *            发送类型(0--普通，1--抄送，2---暗送)
	 * @param subject
	 *            主题
	 * @param enclosure
	 *            附件
	 * @param content
	 *            内容
	 * @param hasSure
	 *            是否有附件
	 */
	private static void sendMail(String server, String from, String port, String user, String pwd, String[] mailTo,
			int mailType, String subject, String[] enclosure, String content, boolean hasSure) {
		try {
			Properties props = new Properties();
			props.put("mail.smtp.auth", "true");
			props.put("mail.transport.protoco1", "smtp");
			props.put("mail.smtp.host", server);
			props.put("mail.smtp.prot", port);
			Session session = Session.getDefaultInstance(props, null);
			Message msg = new MimeMessage(session);
			msg.setFrom(new InternetAddress(from));
			RecipientType type;
			switch (mailType) { // 设置邮件类型
			case 0:
				type = Message.RecipientType.TO;
				break;
			case 1:
				type = Message.RecipientType.CC;
				break;
			case 2:
				type = Message.RecipientType.BCC;
				break;
			default:
				type = Message.RecipientType.TO;
				break;
			}
			for (String element : mailTo) { // 循环加入收件人
				msg.addRecipient(type, new InternetAddress(element));
			}
			msg.setSubject(subject);
			Multipart part = new MimeMultipart();
			BodyPart body = new MimeBodyPart();
			String bodyType = "text/html; charset=\"GB2312\"";
			body.setContent(new String(content.getBytes("gb2312")), bodyType);
			part.addBodyPart(body);
			if (hasSure) { // 如果有附件
				for (String element : enclosure) {
					String fileName = element.substring(element.indexOf("//") + 1, element.length());
					BASE64Encoder base64 = new BASE64Encoder();
					String gbkName = base64.encode(fileName.getBytes()); // 获取转码后的文件名
					body = new MimeBodyPart();
					DataSource source = new FileDataSource(element);
					body.setDataHandler(new DataHandler(source));
					body.setFileName("=?GBK?B?" + gbkName + "?=");
					part.addBodyPart(body);
				}
			}
			msg.setContent(part);
			Transport transport = session.getTransport("smtp");

			// 分别使用两种方式登录邮箱,邮箱为用户名和独立用户名
			try {
				transport.connect(server, server, pwd); // 登录邮箱
			} catch (MessagingException e) {
				try {
					transport.connect(server, user, pwd); // 登录邮箱
				} catch (MessagingException e1) {
					logger.error("登录邮箱服务器" + server + "异常", e1);
					throw new WebException("登录邮箱服务器" + server + "失败", e1);
				}
			}

			// 发送邮件
			transport.sendMessage(msg, msg.getAllRecipients());
			transport.close();
			if (logger.isDebugEnabled()) {
				for (String string : mailTo) {
					logger.debug("成功发送邮件到：" + string);
				}
			}
		} catch (Exception ex) {
			logger.error("发送邮件异常", ex);
			throw new WebException("发送邮件失败,请联系管理员", ex);
		}
	}

	/**
	 * 发送不带附件的邮件
	 * 
	 * @param server
	 *            邮件服务器(mail.163.com)
	 * @param port
	 *            邮件发送端口
	 * @param user
	 *            用户名
	 * @param pwd
	 *            用户密码
	 * @param mailTo
	 *            接收人
	 * @param mailType
	 *            发送类型(0--普通，1--抄送，2---暗送)
	 * @param subject
	 *            主题
	 * @param content
	 *            内容
	 */
	public static void sendMail(String server, String from, String port, String user, String pwd, String[] mailTo,
			int mailType, String subject, String content) {
		sendMail(server, from, port, user, pwd, mailTo, mailType, subject, null, content, false);
	}

	/**
	 * 发送待附件的邮件
	 * 
	 * @param server
	 *            邮件服务器(mail.163.com)
	 * @param port
	 *            邮件发送端口
	 * @param user
	 *            用户名
	 * @param pwd
	 *            用户密码
	 * @param mailTo
	 *            接收人
	 * @param mailType
	 *            发送类型(0--普通，1--抄送，2---暗送)
	 * @param subject
	 *            主题
	 * @param enclosure
	 *            附件
	 * @param content
	 *            内容
	 */
	public static void sendMail(String server, String from, String port, String user, String pwd, String[] mailTo,
			int mailType, String subject, String[] enclosure, String content) {
		sendMail(server, from, port, user, pwd, mailTo, mailType, subject, enclosure, content, true);
	}

}
