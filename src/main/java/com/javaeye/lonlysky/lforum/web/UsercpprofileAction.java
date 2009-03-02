package com.javaeye.lonlysky.lforum.web;

import java.util.regex.Pattern;

import org.apache.struts2.config.ParentPackage;

import com.javaeye.lonlysky.lforum.ForumBaseAction;
import com.javaeye.lonlysky.lforum.comm.LForumRequest;
import com.javaeye.lonlysky.lforum.comm.utils.ForumUtils;
import com.javaeye.lonlysky.lforum.comm.utils.Utils;
import com.javaeye.lonlysky.lforum.entity.forum.Users;

/**
 * 更新用户档案页面
 * 
 * @author 黄磊
 *
 */
@ParentPackage("default")
public class UsercpprofileAction extends ForumBaseAction {

	private static final long serialVersionUID = -2426094017624165014L;

	/**
	 * 当前用户信息
	 */
	private Users user = new Users();

	@Override
	public String execute() throws Exception {
		pagetitle = "用户控制面板";

		if (userid == -1) {
			reqcfg.addErrLine("你尚未登录");

			return SUCCESS;
		}
		user = userManager.getUserInfo(userid);

		if (LForumRequest.isPost()) {
			if (ForumUtils.isCrossSitePost()) {
				reqcfg
						.addErrLine("您的请求来路不正确，无法提交。如果您安装了某种默认屏蔽来路信息的个人防火墙软件(如 Norton Internet Security)，请设置其不要禁止来路信息后再试。");
				return SUCCESS;
			}
			//实名验证
			if (config.getRealnamesystem() == 1) {
				if (LForumRequest.getParamValue("realname").trim().equals("")) {
					reqcfg.addErrLine("真实姓名不能为空");
				} else if (LForumRequest.getParamValue("realname").trim().length() > 10) {
					reqcfg.addErrLine("真实姓名不能大于10个字符");
				}
				if (LForumRequest.getParamValue("idcard").trim().equals("")) {
					reqcfg.addErrLine("身份证号码不能为空");
				} else if (LForumRequest.getParamValue("idcard").trim().length() > 20) {
					reqcfg.addErrLine("身份证号码不能大于20个字符");
				}
				if (LForumRequest.getParamValue("mobile").trim().equals("")
						&& LForumRequest.getParamValue("phone").trim().equals("")) {
					reqcfg.addErrLine("移动电话号码和是固定电话号码必须填写其中一项");
				}
				if (LForumRequest.getParamValue("mobile").trim().length() > 20) {
					reqcfg.addErrLine("移动电话号码不能大于20个字符");
				}
				if (LForumRequest.getParamValue("phone").trim().length() > 20) {
					reqcfg.addErrLine("固定电话号码不能大于20个字符");
				}
			}

			if (!LForumRequest.getParamValue("idcard").trim().equals("")
					&& !Pattern.compile("^[\\x20-\\x80]+$").matcher(LForumRequest.getParamValue("idcard").trim())
							.find()) {
				reqcfg.addErrLine("身份证号码中含有非法字符");
			}

			if (LForumRequest.getParamValue("mobile").trim() != ""
					&& !Pattern.compile("^[\\d|-]+$").matcher(LForumRequest.getParamValue("mobile").trim()).find()) {
				reqcfg.addErrLine("移动电话号码中含有非法字符");
			}
			if (LForumRequest.getParamValue("phone").trim() != ""
					&& !Pattern.compile("^[\\d|-]+$").matcher(LForumRequest.getParamValue("phone").trim()).find()) {
				reqcfg.addErrLine("固定电话号码中含有非法字符");
			}

			String email = LForumRequest.getParamValue("email").trim().toLowerCase();

			if (email.equals("")) {
				reqcfg.addErrLine("Email不能为空");
				return SUCCESS;
			}

			else if (!Utils.checkEmail(email)) {
				reqcfg.addErrLine("Email格式不正确");
				return SUCCESS;
			} else {
				int tmpUserid = userManager.findUserByEmail(email);
				if (tmpUserid != userid && tmpUserid != -1 && config.getDoublee() == 0) {
					reqcfg.addErrLine("Email: \"" + email + "\" 已经被其它用户注册使用");
					return SUCCESS;
				}

				String emailhost = Utils.getEmailHostName(email);
				// 允许名单规则优先于禁止名单规则
				if (!config.getAccessemail().trim().equals("")) {
					// 如果email后缀 不属于 允许名单
					if (!Utils.inArray(emailhost, config.getAccessemail().replace("\r\n", "\n"), "\n")) {
						reqcfg.addErrLine("Email: \"" + email + "\" 不在本论坛允许范围之类, 本论坛只允许用户使用这些Email地址注册: "
								+ config.getAccessemail().replace("\n", ",&nbsp;"));
						return SUCCESS;
					}
				} else if (!config.getCensoremail().trim().equals("")) {
					// 如果email后缀 属于 禁止名单
					if (Utils.inArray(emailhost, config.getCensoremail().replace("\r\n", "\n"), "\n")) {
						reqcfg.addErrLine("Email: \"" + email + "\" 不允许在本论坛使用, 本论坛不允许用户使用的Email地址包括: "
								+ config.getCensoremail().replace("\n", ",&nbsp;"));
						return SUCCESS;
					}
				}
				if (LForumRequest.getParamValue("bio").length() > 500) {
					//如果自我介绍超过500...
					reqcfg.addErrLine("自我介绍不得超过500个字符");
					return SUCCESS;
				}
				if (LForumRequest.getParamValue("signature").length() > 500) {
					//如果签名超过500...
					reqcfg.addErrLine("签名不得超过500个字符");
					return SUCCESS;
				}
			}

			if (reqcfg.getPage_err() == 0) {
				user.setNickname(LForumRequest.getParamValue("nickname"));
				user.setGender(LForumRequest.getParamIntValue("gender", 0));
				user.getUserfields().setRealname(LForumRequest.getParamValue("realname"));
				user.getUserfields().setIdcard(LForumRequest.getParamValue("idcard"));
				user.getUserfields().setMobile(LForumRequest.getParamValue("mobile"));
				user.getUserfields().setPhone(LForumRequest.getParamValue("phone"));
				user.setEmail(email);
				user.setBday(LForumRequest.getParamValue("bday"));
				user.setShowemail(LForumRequest.getParamIntValue("showemail", 1));
				if (LForumRequest.getParamValue("website").indexOf(".") > -1
						&& !LForumRequest.getParamValue("website").toLowerCase().startsWith("http")) {
					user.getUserfields().setWebsite("http://" + LForumRequest.getParamValue("website"));
				} else {
					user.getUserfields().setWebsite(LForumRequest.getParamValue("website"));
				}
				user.getUserfields().setIcq(LForumRequest.getParamValue("icq"));
				user.getUserfields().setQq(LForumRequest.getParamValue("qq"));
				user.getUserfields().setYahoo(LForumRequest.getParamValue("yahoo"));
				user.getUserfields().setMsn(LForumRequest.getParamValue("msn"));
				user.getUserfields().setSkype(LForumRequest.getParamValue("skype"));
				user.getUserfields().setLocation(LForumRequest.getParamValue("location"));
				user.getUserfields().setBio(LForumRequest.getParamValue("bio"));

				try {
					userManager.updateUserInfo(user);
					reqcfg.setUrl("usercpprofile.action").setMetaRefresh().setShowBackLink(true).addMsgLine("修改个人档案完毕");
				} catch (Exception e) {
					reqcfg.addErrLine("修改个人档案失败!");
				}
			}
		}
		return SUCCESS;
	}

	public Users getUser() {
		return user;
	}

}
