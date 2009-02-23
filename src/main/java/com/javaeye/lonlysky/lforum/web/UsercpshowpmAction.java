package com.javaeye.lonlysky.lforum.web;

import org.apache.struts2.config.ParentPackage;
import org.springframework.beans.factory.annotation.Autowired;

import com.javaeye.lonlysky.lforum.ForumBaseAction;
import com.javaeye.lonlysky.lforum.comm.LForumRequest;
import com.javaeye.lonlysky.lforum.entity.forum.CreditsOperationType;
import com.javaeye.lonlysky.lforum.entity.forum.Pms;
import com.javaeye.lonlysky.lforum.entity.forum.Users;
import com.javaeye.lonlysky.lforum.service.MessageManager;
import com.javaeye.lonlysky.lforum.service.UserCreditManager;

/**
 * 显示短消息页面
 * 
 * @author 黄磊
 *
 */
@ParentPackage("default")
public class UsercpshowpmAction extends ForumBaseAction {

	private static final long serialVersionUID = -4832586673664301055L;

	/**
	 * 短消息发件人
	 */
	public String msgfrom = "";

	/**
	 * 短消息标题
	 */
	public String subject = "";

	/**
	 * 短消息内容
	 */
	public String message = "";

	/**
	 * 短消息回复标题
	 */
	public String resubject = "";

	/**
	 * 短消息回复内容
	 */
	public String remessage = "";

	/**
	 * 短消息发送时间
	 */
	public String postdatetime = "";

	/**
	 * 短消息Id
	 */
	public int pmid = 0;

	/**
	 * 是否能够回复短消息
	 */
	public boolean canreplypm = true;

	/**
	 * 当前用户信息
	 */
	public Users user = new Users();

	@Autowired
	private UserCreditManager userCreditManager;

	@Autowired
	private MessageManager messageManager;

	@Override
	public String execute() throws Exception {
		if (userid == -1) {
			reqcfg.addErrLine("你尚未登录");
			return SUCCESS;
		}

		pagetitle = "查看短消息";
		user = userManager.getUserInfo(userid);

		pmid = LForumRequest.getParamIntValue("pmid", -1);
		if (pmid <= 0) {
			reqcfg.addErrLine("参数无效");
			return SUCCESS;
		}

		if (!userCreditManager.checkUserCreditsIsEnough(userid, 1, CreditsOperationType.SENDMESSAGE, -1)) {
			canreplypm = false;
		}

		Pms messageinfo = messageManager.getPrivateMessageInfo(pmid);

		if (messageinfo == null) {
			reqcfg.addErrLine("无效的短消息ID");
			return SUCCESS;
		}
		if (messageinfo != null) {
			//判断当前用户是否有权阅读此消息
			if (messageinfo.getUsersByMsgtoid().getUid() == userid
					|| messageinfo.getUsersByMsgfromid().getUid() == userid) {
				String action = LForumRequest.getParamValue("action");
				if (action.compareTo("delete") == 0) {
					ispost = true;
					int retval = messageManager.deletePrivateMessage(userid, pmid);
					if (retval < 1) {
						reqcfg.addErrLine("消息未找到,可能已被删除");
						return SUCCESS;
					} else {
						reqcfg.addMsgLine("指定消息成功删除,现在将转入消息列表").setUrl("usercpinbox.action").setMetaRefresh();
						return SUCCESS;
					}
				}
				if (action.compareTo("noread") == 0) {
					messageManager.setPrivateMessageState(pmid, 1); //将短消息的状态置 1 表示未读
					ispost = true;
					userManager.decreaseNewPMCount(userid, -1); //将用户的未读短信息数据加 1
					reqcfg.addMsgLine("指定消息已被置成未读状态,现在将转入消息列表").setUrl("usercpinbox.action").setMetaRefresh();
					return SUCCESS;
				} else {
					messageManager.setPrivateMessageState(pmid, 0); //将短消息的状态置 0 表示已读
					userManager.decreaseNewPMCount(userid); //将用户的未读短信息数据减 1
				}

				msgfrom = messageinfo.getMsgfrom();
				subject = messageinfo.getSubject();
				message = messageinfo.getMessage().replace("\n", "<br/>");
				postdatetime = messageinfo.getPostdatetime();
				resubject = "re:" + messageinfo.getSubject();
				remessage = ">" + messageinfo.getMessage().replace("\n", "\n> ") + "\r\n\r\n";
				return SUCCESS;
			}
		}
		reqcfg.addErrLine("对不起, 短消息不存在或已被删除.");
		return SUCCESS;
	}

	public String getMsgfrom() {
		return msgfrom;
	}

	public String getSubject() {
		return subject;
	}

	public String getMessage() {
		return message;
	}

	public String getResubject() {
		return resubject;
	}

	public String getRemessage() {
		return remessage;
	}

	public String getPostdatetime() {
		return postdatetime;
	}

	public int getPmid() {
		return pmid;
	}

	public boolean isCanreplypm() {
		return canreplypm;
	}

	public Users getUser() {
		return user;
	}

}
