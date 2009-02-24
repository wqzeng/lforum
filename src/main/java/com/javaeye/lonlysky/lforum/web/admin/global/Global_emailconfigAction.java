package com.javaeye.lonlysky.lforum.web.admin.global;

import org.apache.struts2.config.ParentPackage;
import org.springframework.beans.factory.annotation.Autowired;

import com.javaeye.lonlysky.lforum.AdminBaseAction;
import com.javaeye.lonlysky.lforum.comm.LForumRequest;
import com.javaeye.lonlysky.lforum.config.impl.EmailConfigLoader;
import com.javaeye.lonlysky.lforum.entity.global.EmailConfigInfo;
import com.javaeye.lonlysky.lforum.service.EmailManager;

/**
 * 编辑邮件配置
 * 
 * @author 黄磊
 *
 */
@ParentPackage("default")
public class Global_emailconfigAction extends AdminBaseAction {

	private static final long serialVersionUID = 1237422749735800977L;

	private EmailConfigInfo emailInfo;

	@Autowired
	private EmailManager emailManager;

	@Override
	public String execute() throws Exception {
		emailInfo = EmailConfigLoader.getInstance().getEmailConfigInfo();
		if (LForumRequest.isPost()) {
			String submitMethod = LForumRequest.getParamValue("submitMethod");
			if (!submitMethod.equals("")) {
				submitMethod = submitMethod.substring(submitMethod.indexOf(":") + 1);
				if (submitMethod.equals("testEmail")) {
					testEmail();
					return SUCCESS;
				}
			}
			emailInfo.setPassword(LForumRequest.getParamValue("password"));
			emailInfo.setPort(LForumRequest.getParamIntValue("port", 25));
			emailInfo.setSmtp(LForumRequest.getParamValue("smtp"));
			emailInfo.setSysemail(LForumRequest.getParamValue("sysemail"));
			emailInfo.setUsername(LForumRequest.getParamValue("username"));
			EmailConfigLoader.getInstance().saveEmailConfig(emailInfo);
			registerStartupScript("PAGE", "window.location.href='global_emailconfig.action';");
		}
		return SUCCESS;
	}

	/**
	 * 发送测试邮件
	 */
	private void testEmail() {
		if (!LForumRequest.getParamValue("testEmail").equals("")) {
			emailManager.sendEmailNotify(LForumRequest.getParamValue("testEmail"), "测试邮件", "这是一封LForum邮箱设置页面发送的测试邮件!");
			registerStartupScript("PAGE", "window.location.href='global_emailconfig.action';");
		} else {
			registerStartupScript("", "<script>alert('请输入测试发送EMAIL地址!');</script>");
		}

	}

	public EmailConfigInfo getEmailInfo() {
		return emailInfo;
	}

}
