package com.javaeye.lonlysky.lforum.web;

import org.apache.struts2.config.ParentPackage;
import org.springframework.beans.factory.annotation.Autowired;

import com.javaeye.lonlysky.lforum.ForumBaseAction;
import com.javaeye.lonlysky.lforum.comm.LForumRequest;
import com.javaeye.lonlysky.lforum.comm.utils.ForumUtils;
import com.javaeye.lonlysky.lforum.comm.utils.Utils;
import com.javaeye.lonlysky.lforum.service.EmailManager;

/**
 * 找回密码页
 * 
 * @author 黄磊
 *
 */
@ParentPackage("default")
public class GetpasswordAction extends ForumBaseAction {

	private static final long serialVersionUID = 2126792657657287191L;

	@Autowired
	private EmailManager emailManager;

	@Override
	public String execute() throws Exception {
		pagetitle = "密码找回";
		username = Utils.cleanHtmlTag(LForumRequest.getParamValue("username"));

		//如果提交...
		if (LForumRequest.isPost()) {
			if (ForumUtils.isCrossSitePost()) {
				reqcfg
						.addErrLine("您的请求来路不正确，无法提交。如果您安装了某种默认屏蔽来路信息的个人防火墙软件(如 Norton Internet Security)，请设置其不要禁止来路信息后再试。");
				return SUCCESS;
			}

			reqcfg.setBackLink("getpassword.action?username=" + username);

			if (!userManager.exists(username)) {
				reqcfg.addErrLine("用户不存在");
				return SUCCESS;
			}

			if (LForumRequest.getParamValue("email").equals("")) {
				reqcfg.addErrLine("电子邮件不能为空");
				return SUCCESS;
			}

			if (reqcfg.isErr()) {
				return SUCCESS;
			}

			int uid = userManager.checkEmailAndSecques(username, LForumRequest.getParamValue("email"), LForumRequest
					.getParamIntValue("question", 0), LForumRequest.getParamValue("answer"));
			if (uid != -1) {
				String Authstr = Utils.getRandomString(20);
				userManager.updateAuthStr(uid, Authstr, 2);

				String title = config.getForumtitle() + " 取回密码说明";
				StringBuilder body = new StringBuilder();
				body.append(username);
				body.append("您好!<br />这封信是由 ");
				body.append(config.getForumtitle());
				body.append(" 发送的.<br /><br />您收到这封邮件,是因为在我们的论坛上这个邮箱地址被登记为用户邮箱,且该用户请求使用 Email 密码重置功能所致.");
				body.append("<br /><br />----------------------------------------------------------------------");
				body.append("<br />重要！");
				body.append("<br /><br />----------------------------------------------------------------------");
				body.append("<br /><br />如果您没有提交密码重置的请求或不是我们论坛的注册用户,请立即忽略并删除这封邮件.只在您确认需要重置密码的情况下,才继续阅读下面的内容.");
				body.append("<br /><br />----------------------------------------------------------------------");
				body.append("<br />密码重置说明");
				body.append("<br /><br />----------------------------------------------------------------------");
				body.append("<br /><br />您只需在提交请求后的三天之内,通过点击下面的链接重置您的密码:");
				body.append("<br /><br /><a href=" + config.getForumurl() + "setnewpassword.action?uid=" + uid + "&id="
						+ Authstr + " target=_blank>");
				body.append(config.getForumurl());
				body.append("setnewpassword.action?uid=");
				body.append(uid);
				body.append("&id=");
				body.append(Authstr);
				body.append("</a>");
				body.append("<br /><br />(如果上面不是链接形式,请将地址手工粘贴到浏览器地址栏再访问)");
				body.append("<br /><br />上面的页面打开后,输入新的密码后提交,之后您即可使用新的密码登录论坛了.您可以在用户控制面板中随时修改您的密码.");
				body.append("<br /><br />本请求提交者的 IP 为 ");
				body.append(LForumRequest.getIp());
				body.append("<br /><br /><br /><br />");
				body.append("<br />此致 <br /><br />");
				body.append(config.getForumtitle());
				body.append(" 管理团队.");
				body.append("<br />");
				body.append(config.getForumurl());
				body.append("<br /><br />");

				emailManager.sendEmailNotify(LForumRequest.getParamValue("email"), title, body.toString());

				reqcfg.setUrl(forumurl).setMetaRefresh(5).setShowBackLink(false).addMsgLine(
						"取回密码的方法已经通过 Email 发送到您的信箱中,<br />请在 3 天之内到论坛修改您的密码.");
			} else {
				reqcfg.addErrLine("用户名,Email 地址或安全提问不匹配,请返回修改.");
			}
		}
		return SUCCESS;
	}

}
