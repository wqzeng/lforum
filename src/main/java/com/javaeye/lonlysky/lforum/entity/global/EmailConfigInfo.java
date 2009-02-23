package com.javaeye.lonlysky.lforum.entity.global;

import java.io.Serializable;

/**
 * Email配置信息类
 * 
 * @author 黄磊
 *
 */
public class EmailConfigInfo implements Serializable {

	private static final long serialVersionUID = -569810459461570503L;

	private String smtp; //smtp 地址

	private int port = 25; //端口号

	private String sysemail; //系统邮件地址

	private String username; //邮件帐号

	private String password; //邮件密码

	private String emailcontent; //邮件内容

	public String getSmtp() {
		return smtp;
	}

	public void setSmtp(String smtp) {
		this.smtp = smtp;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getSysemail() {
		return sysemail;
	}

	public void setSysemail(String sysemail) {
		this.sysemail = sysemail;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmailcontent() {
		return emailcontent;
	}

	public void setEmailcontent(String emailcontent) {
		this.emailcontent = emailcontent;
	}
}
