package com.javaeye.lonlysky.lforum.web;

import org.apache.struts2.config.ParentPackage;
import org.springframework.beans.factory.annotation.Autowired;

import com.javaeye.lonlysky.lforum.ForumBaseAction;
import com.javaeye.lonlysky.lforum.comm.LForumRequest;
import com.javaeye.lonlysky.lforum.comm.utils.ForumUtils;
import com.javaeye.lonlysky.lforum.comm.utils.UBBUtils;
import com.javaeye.lonlysky.lforum.entity.forum.PostpramsInfo;
import com.javaeye.lonlysky.lforum.entity.forum.Users;
import com.javaeye.lonlysky.lforum.service.SmilieManager;

/**
 * 论坛设置
 * 
 * @author 黄磊
 *
 */
@ParentPackage("default")
public class UsercpforumsettingAction extends ForumBaseAction {

	private static final long serialVersionUID = 1784261022176127144L;

	/**
	 * 当前用户信息
	 */
	private Users user = new Users();

	@Autowired
	private SmilieManager smilieManager;

	@Override
	public String execute() throws Exception {
		pagetitle = "用户控制面板";

		if (userid == -1) {
			reqcfg.addErrLine("你尚未登录");
			return SUCCESS;
		}
		user = userManager.getUserInfo(userid);

		if (LForumRequest.isPost()) {
			reqcfg.setBackLink("usercpforumsetting.action");
			if (ForumUtils.isCrossSitePost()) {
				reqcfg
						.addErrLine("您的请求来路不正确，无法提交。如果您安装了某种默认屏蔽来路信息的个人防火墙软件(如 Norton Internet Security)，请设置其不要禁止来路信息后再试。");
				return SUCCESS;
			}
			user.setTpp(LForumRequest.getParamIntValue("tpp", 0));
			user.setPpp(LForumRequest.getParamIntValue("ppp", 0));
			user.setPmsound(LForumRequest.getParamIntValue("pmsound", 0));
			user.setInvisible(LForumRequest.getParamIntValue("invisible", 0));
			user.getUserfields().setCustomstatus(
					cachesManager.banWordFilter(LForumRequest.getParamValue("customstatus")));
			//获取提交的内容并进行脏字和Html处理
			String signature = cachesManager.banWordFilter(LForumRequest.getParamValue("signature"));

			int sigstatus = LForumRequest.getParamIntValue("sigstatus", 0);
			//错误参数值纠正
			if (sigstatus != 0) {
				sigstatus = 1;
			}

			PostpramsInfo postpramsinfo = new PostpramsInfo();
			postpramsinfo.setUsergroupid(usergroupid);
			postpramsinfo.setAttachimgpost(config.getAttachimgpost());
			postpramsinfo.setShowattachmentpath(config.getShowattachmentpath());
			postpramsinfo.setHide(0);
			postpramsinfo.setPrice(0);
			postpramsinfo.setSdetail(signature);
			postpramsinfo.setSmileyoff(1);
			postpramsinfo.setBbcodeoff(1 - usergroupinfo.getAllowsigbbcode());
			postpramsinfo.setParseurloff(1);
			postpramsinfo.setShowimages(usergroupinfo.getAllowsigimgcode());
			postpramsinfo.setAllowhtml(0);
			postpramsinfo.setSignature(1);
			postpramsinfo.setSmiliesinfo(smilieManager.getSmiliesListWithInfo());
			postpramsinfo.setCustomeditorbuttoninfo(null);
			postpramsinfo.setSmiliesmax(config.getSmiliesmax());
			postpramsinfo.setSignature(1);

			String sightml = UBBUtils.uBBToHTML(postpramsinfo);

			if (LForumRequest.getParamValue("signature").length() > usergroupinfo.getMaxsigsize()) {
				reqcfg.addErrLine("您的签名长度超过 " + usergroupinfo.getMaxsigsize() + " 字符的限制，请返回修改。");
				return SUCCESS;
			}

			if (sightml.length() >= 1000) {
				reqcfg.addErrLine("您的签名转换后超出系统最大长度， 请返回修改");
				return SUCCESS;
			}

			user.setSigstatus(sigstatus);
			user.getUserfields().setSignature(signature);
			user.getUserfields().setSightml(sightml);

			userManager.updateUserInfo(user);
			onlineUserManager.updateInvisible(olid, user.getInvisible());

			ForumUtils.writeCookie("sigstatus", sigstatus);
			ForumUtils.writeCookie("tpp", user.getTpp().toString());
			ForumUtils.writeCookie("ppp", user.getPpp().toString());
			ForumUtils.writeCookie("pmsound", user.getPmsound().toString());

			reqcfg.setUrl("usercpforumsetting.action").setMetaRefresh().setShowBackLink(true).addMsgLine("修改论坛设置完毕");
		}
		return SUCCESS;

	}

	public Users getUser() {
		return user;
	}
}
