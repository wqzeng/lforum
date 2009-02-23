package com.javaeye.lonlysky.lforum.web;

import java.text.ParseException;

import org.apache.struts2.config.ParentPackage;
import org.springframework.beans.factory.annotation.Autowired;

import com.javaeye.lonlysky.lforum.ForumBaseAction;
import com.javaeye.lonlysky.lforum.GlobalsKeys;
import com.javaeye.lonlysky.lforum.comm.LForumRequest;
import com.javaeye.lonlysky.lforum.comm.utils.ForumUtils;
import com.javaeye.lonlysky.lforum.comm.utils.Utils;
import com.javaeye.lonlysky.lforum.entity.forum.Admingroups;
import com.javaeye.lonlysky.lforum.entity.forum.CreditsOperationType;
import com.javaeye.lonlysky.lforum.entity.forum.Pms;
import com.javaeye.lonlysky.lforum.entity.forum.Users;
import com.javaeye.lonlysky.lforum.service.AdminGroupManager;
import com.javaeye.lonlysky.lforum.service.EmailManager;
import com.javaeye.lonlysky.lforum.service.MessageManager;
import com.javaeye.lonlysky.lforum.service.UserCreditManager;

/**
 * 撰写短消息页面
 * 
 * @author 黄磊
 *
 */
@ParentPackage("default")
public class UsercppostpmAction extends ForumBaseAction {

	private static final long serialVersionUID = -2313184347134995631L;

	/**
	 * 短消息收件人
	 */
	private String msgto;

	/**
	 * 短消息标题
	 */
	private String subject;

	/**
	 * 短消息内容
	 */
	private String message;

	/**
	 * 短消息收件人Id
	 */
	private int msgtoid = 0;

	/**
	 * 当前用户信息
	 */
	private Users user = new Users();

	@Autowired
	private MessageManager messageManager;

	@Autowired
	private UserCreditManager userCreditManager;

	@Autowired
	private AdminGroupManager adminGroupManager;

	@Autowired
	private EmailManager emailManager;

	@Override
	public String execute() throws Exception {
		pagetitle = "撰写短消息";

		if (userid == -1) {
			reqcfg.addErrLine("你尚未登录");

			return SUCCESS;
		}
		user = userManager.getUserInfo(userid);

		if (!checkPermission()) {
			return SUCCESS;
		}

		if (LForumRequest.isPost()) {
			if (!checkPermissionAfterPost()) {
				return SUCCESS;
			}

			// 创建并发送短消息

			Pms pm = new Pms();

			String curdatetime = Utils.getNowTime();
			// 收件箱
			if (useradminid == 1) {
				pm.setMessage(LForumRequest.getParamValue("message"));
				pm.setSubject(LForumRequest.getParamValue("subject"));
			} else {
				pm.setMessage(cachesManager.banWordFilter(LForumRequest.getParamValue("message")));
				pm.setSubject(cachesManager.banWordFilter(LForumRequest.getParamValue("subject")));
			}

			if (cachesManager.hasBannedWord(pm.getMessage()) || cachesManager.hasBannedWord(pm.getSubject())) {
				//HasBannedWord 指定的字符串中是否含有禁止词汇
				reqcfg.addErrLine("对不起, 您提交的内容包含不良信息, 因此无法提交, 请返回修改!");
				return SUCCESS;
			}
			pm.setMessage(cachesManager.banWordFilter(pm.getMessage()));
			pm.setSubject(cachesManager.banWordFilter(pm.getSubject()));

			pm.setMsgto(LForumRequest.getParamValue("msgto"));
			Users msgtoUser = new Users();
			msgtoUser.setUid(msgtoid);
			pm.setUsersByMsgtoid(msgtoUser);
			pm.setMsgfrom(username);
			pm.setUsersByMsgfromid(user);
			pm.setNew_(1);
			pm.setPostdatetime(curdatetime);

			if (!LForumRequest.getParamValue("savetousercpdraftbox").equals("")) {
				// 检查发送人的短消息是否已超过发送人用户组的上限
				if (messageManager.getPrivateMessageCount(userid, -1) >= usergroupinfo.getMaxpmnum()) {
					reqcfg.addErrLine("抱歉,您的短消息已达到上限,无法保存到草稿箱");
					return SUCCESS;
				}
				// 只将消息保存到草稿箱
				pm.setFolder(2);
				if (userCreditManager.updateUserCreditsBySendpms(userid) == -1) {
					reqcfg.addErrLine("您的积分不足, 不能发送短消息");
					return subject;
				}
				messageManager.createPrivateMessage(pm, 0);

				//发送邮件通知
				if (LForumRequest.getParamValue("emailnotify") == "on") {
					//SendNotifyEmail(Users.GetUserInfo(msgtoid).Email.Trim(), pm);
				}

				reqcfg.setUrl("usercpdraftbox.action").setMetaRefresh().setShowBackLink(true).addMsgLine("已将消息保存到草稿箱");
			} else if (!LForumRequest.getParamValue("savetosentbox").equals("")) {
				// 检查接收人的短消息是否已超过接收人用户组的上限
				Users touser = userManager.getUserInfo(msgtoid);
				//管理组不受接收人短消息上限限制
				int radminId = userGroupManager.getUsergroup(usergroupid).getAdmingroups().getAdmingid();
				if (!(radminId > 0 && radminId <= 3)
						&& messageManager.getPrivateMessageCount(msgtoid, -1) >= userGroupManager.getUsergroup(
								touser.getUsergroups().getGroupid()).getMaxpmnum()) {
					reqcfg.addErrLine("抱歉,接收人的短消息已达到上限,无法接收");
					return SUCCESS;
				}

				if (!Utils.inArray(touser.getNewsletter().toString(), "2,3,6,7")) {
					reqcfg.addErrLine("抱歉,接收人拒绝接收短消息");
					return SUCCESS;
				}
				// 检查发送人的短消息是否已超过发送人用户组的上限
				if (messageManager.getPrivateMessageCount(userid, -1) >= usergroupinfo.getMaxpmnum()) {
					reqcfg.addErrLine("抱歉,您的短消息已达到上限,无法保存到发件箱");
					return SUCCESS;
				}
				// 发送消息且保存到发件箱
				pm.setFolder(0);
				if (userCreditManager.updateUserCreditsBySendpms(userid) == -1) {
					reqcfg.addErrLine("您的积分不足, 不能发送短消息");
					return SUCCESS;
				}
				messageManager.createPrivateMessage(pm, 1);

				//发送邮件通知
				if (LForumRequest.getParamValue("emailnotify") == "on") {
					//SendNotifyEmail(touser.Email.Trim(), pm);
				}

				// 更新在线表中的用户最后发帖时间
				onlineUserManager.updatePostPMTime(olid);

				reqcfg.setUrl("usercpsentbox.action").setMetaRefresh().setShowBackLink(true).addMsgLine(
						"发送完毕, 且已将消息保存到发件箱");
			} else {
				Users touser = userManager.getUserInfo(msgtoid);
				// 检查接收人的短消息是否已超过接收人用户组的上限,管理组不受接收人短消息上限限制
				int radminId = userGroupManager.getUsergroup(usergroupid).getAdmingroups().getAdmingid();
				if (!(radminId > 0 && radminId <= 3)
						&& messageManager.getPrivateMessageCount(msgtoid, -1) >= userGroupManager.getUsergroup(
								touser.getUsergroups().getGroupid()).getMaxpmnum()) {
					reqcfg.addErrLine("抱歉,接收人的短消息已达到上限,无法接收");
					return SUCCESS;
				}
				if (!Utils.inArray(touser.getNewsletter().toString(), "2,3,6,7")) {
					reqcfg.addErrLine("抱歉,接收人拒绝接收短消息");
					return SUCCESS;
				}

				// 发送消息但不保存到发件箱
				pm.setFolder(0);
				if (userCreditManager.updateUserCreditsBySendpms(userid) == -1) {
					reqcfg.addErrLine("您的积分不足, 不能发送短消息");
					return SUCCESS;
				}
				messageManager.createPrivateMessage(pm, 0);

				//发送邮件通知
				if (LForumRequest.getParamValue("emailnotify") == "on") {
					//SendNotifyEmail(touser.Email.Trim(), pm);
				}

				reqcfg.setUrl("usercpinbox.action").setMetaRefresh().setShowBackLink(true).addMsgLine("发送完毕");
			}
		}

		msgto = LForumRequest.getParamValue("msgto");

		msgtoid = LForumRequest.getParamIntValue("msgtoid", 0);
		if (msgtoid > 0) {
			msgto = userManager.getUserName(msgtoid).trim();
		}

		subject = LForumRequest.getParamValue("subject");
		message = LForumRequest.getParamValue("message");

		String action = LForumRequest.getParamValue("action").toLowerCase();
		if (action.compareTo("re") == 0 || action.compareTo("fw") == 0) //回复或者转发
		{
			int pmid = LForumRequest.getParamIntValue("pmid", -1);
			if (pmid != -1) {
				Pms pm = messageManager.getPrivateMessageInfo(pmid);
				if (pm != null) {
					if (pm.getUsersByMsgtoid().getUid() == userid || pm.getUsersByMsgfromid().getUid() == userid) {
						if (action.compareTo("re") == 0) {
							msgto = pm.getMsgfrom();
						} else {
							msgto = "";
						}
						subject = action + ":" + pm.getSubject();
						message = "> " + pm.getMessage().replace("\n", "\n> ") + "\r\n\r\n";
					}
				}
			}
		}
		return SUCCESS;
	}

	/**
	 * 提交后的权限检查
	 * @return
	 */
	private boolean checkPermissionAfterPost() {
		if (ForumUtils.isCrossSitePost()) {
			reqcfg.addErrLine("您的请求来路不正确，无法提交。如果您安装了某种默认屏蔽来路信息的个人防火墙软件(如 Norton Internet Security)，请设置其不要禁止来路信息后再试。");
			return false;
		}

		if (LForumRequest.getParamValue("message").equals("")) {
			reqcfg.addErrLine("内容不能为空");

			return false;
		}

		if (LForumRequest.getParamValue("message").length() > 3000) {
			reqcfg.addErrLine("内容不能超过3000字");

			return false;
		}

		if (LForumRequest.getParamValue("msgto").equals("")) {
			reqcfg.addErrLine("接收人不能为空");

			return false;
		}

		if (LForumRequest.getParamValue("subject").equals("")) {
			reqcfg.addErrLine("标题不能为空");

			return false;
		}

		if (LForumRequest.getParamValue("subject").length() > 60) {
			reqcfg.addErrLine("标题不能超过60字");

			return false;
		}

		// 不能给负责发送新用户注册欢迎信件的用户名称发送消息
		if (LForumRequest.getParamValue("msgto").equals(GlobalsKeys.SYSTEM_USERNAME)) {
			reqcfg.addErrLine("不能给系统发送消息");
			return false;
		}

		msgtoid = userManager.getUserId(LForumRequest.getParamValue("msgto"));
		if (msgtoid == -1) {
			reqcfg.addErrLine("接收人不是注册用户");
			return false;
		}

		return true;
	}

	/**
	 * 不论是否提交都有的权限检查
	 * @return
	 * @throws ParseException 
	 */
	private boolean checkPermission() throws ParseException {
		// 如果是受灌水限制用户, 则判断是否是灌水
		Admingroups admininfo = adminGroupManager.getAdminGroup(useradminid);
		if (admininfo == null || admininfo.getDisablepostctrl() != 1) {
			int interval = Utils.strDateDiffSeconds(lastpostpmtime, config.getPostinterval() * 2);
			if (interval < 0) {
				reqcfg.addErrLine("系统规定发帖或发短消息间隔为" + (config.getPostinterval() * 2) + "秒, 您还需要等待 " + (interval * -1)
						+ " 秒");
				return false;
			}
		}

		if (!userCreditManager.checkUserCreditsIsEnough(userid, 1, CreditsOperationType.SENDMESSAGE, -1)) {
			reqcfg.addErrLine("您的积分不足, 不能发送短消息");
			return false;
		}
		return true;
	}

	/**
	 * 发送邮件通知
	 * @param email 接收人邮箱
	 * @param pm 短消息对象
	 */
	public void sendNotifyEmail(String email, Pms pm) {
		String jumpurl = config.getForumurl() + "usercpshowpm.action?pmid=" + pm.getPmid();
		StringBuilder sb_body = new StringBuilder("# 论坛短消息: <a href=\"" + jumpurl + "\" target=\"_blank\">"
				+ pm.getSubject() + "</a>");
		//发送人邮箱
		String cur_email = userManager.getUserInfo(userid).getEmail().trim();
		sb_body.append("\r\n");
		sb_body.append("\r\n");
		sb_body.append(pm.getMessage());
		sb_body.append("\r\n<hr/>");
		sb_body.append("作 者:" + pm.getMsgfrom());
		sb_body.append("\r\n");
		sb_body.append("Email:<a href=\"mailto:" + cur_email + "\" target=\"_blank\">" + cur_email + "</a>");
		sb_body.append("\r\n");
		sb_body.append("URL:<a href=\"" + jumpurl + "\" target=\"_blank\">" + jumpurl + "</a>");
		sb_body.append("\r\n");
		sb_body.append("时 间:" + pm.getPostdatetime());
		emailManager.sendEmailNotify(email, "[" + config.getForumtitle() + "短消息通知]" + pm.getSubject(), sb_body
				.toString());
	}

	public String getMsgto() {
		return msgto;
	}

	public String getSubject() {
		return subject;
	}

	public String getMessage() {
		return message;
	}

	public int getMsgtoid() {
		return msgtoid;
	}

	public Users getUser() {
		return user;
	}
}
