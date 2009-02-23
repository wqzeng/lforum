package com.javaeye.lonlysky.lforum.web;

import java.util.List;
import java.util.regex.Pattern;

import org.apache.struts2.config.ParentPackage;
import org.springframework.beans.factory.annotation.Autowired;

import com.javaeye.lonlysky.lforum.ForumBaseAction;
import com.javaeye.lonlysky.lforum.GlobalsKeys;
import com.javaeye.lonlysky.lforum.comm.ForumAction;
import com.javaeye.lonlysky.lforum.comm.LForumRequest;
import com.javaeye.lonlysky.lforum.comm.MD5;
import com.javaeye.lonlysky.lforum.comm.utils.ForumUtils;
import com.javaeye.lonlysky.lforum.comm.utils.Utils;
import com.javaeye.lonlysky.lforum.entity.forum.Admingroups;
import com.javaeye.lonlysky.lforum.entity.forum.ForumStatistics;
import com.javaeye.lonlysky.lforum.entity.forum.Pms;
import com.javaeye.lonlysky.lforum.entity.forum.Templates;
import com.javaeye.lonlysky.lforum.entity.forum.Topics;
import com.javaeye.lonlysky.lforum.entity.forum.UserExtcreditsInfo;
import com.javaeye.lonlysky.lforum.entity.forum.Userfields;
import com.javaeye.lonlysky.lforum.entity.forum.Usergroups;
import com.javaeye.lonlysky.lforum.entity.forum.Users;
import com.javaeye.lonlysky.lforum.service.MessageManager;
import com.javaeye.lonlysky.lforum.service.ScoresetManager;
import com.javaeye.lonlysky.lforum.service.StatisticManager;
import com.javaeye.lonlysky.lforum.service.UserCreditManager;

/**
 * 用户注册页
 * 
 * @author 黄磊
 *
 */
@ParentPackage("default")
public class RegisterAction extends ForumBaseAction {

	private static final long serialVersionUID = 4712420924487496696L;

	/**
	 * 可用的模板列表
	 */
	private List<Templates> templatelist;

	/**
	 * 此变量等于1时创建用户,否则显示填写用户信息界面
	 */
	private String createuser;

	/**
	 * 是否同意注册协议
	 */
	private String agree;

	@Autowired
	private ScoresetManager scoresetManager;

	@Autowired
	private StatisticManager statisticManager;

	@Autowired
	private MessageManager messageManager;

	@Autowired
	private UserCreditManager userCreditManager;

	@Override
	public String execute() throws Exception {
		pagetitle = "用户注册";

		createuser = LForumRequest.getParamValue("createuser");
		agree = LForumRequest.getParamValue("agree");
		if (config.getRules() == 0) {
			agree = "true";
		}

		if (userid != -1) {
			reqcfg.setUrl("main.action").setMetaRefresh().setShowBackLink(false).addMsgLine("不能重复注册用户");
			ispost = true;
			createuser = "1";
			agree = "yes";
			return SUCCESS;
		}

		templatelist = templateManager.getValidTemplateList();
		if (config.getRegstatus() != 1) {
			reqcfg.addErrLine("论坛当前禁止新用户注册");
			return SUCCESS;
		}

		if (config.getRegctrl() > 0) {
			Users userinfo = userManager.getUserByIp(LForumRequest.getIp());
			if (userinfo != null) {
				int interval = Utils.null2Int(Utils.howLong("h", userinfo.getJoindate(), Utils.getNowTime()), 0);
				if (interval < config.getRegctrl()) {
					reqcfg.addErrLine("抱歉, 系统设置了IP注册间隔限制, 您必须在 " + (config.getRegctrl() - interval) + " 小时后才可以注册");
					return SUCCESS;
				}
			}
		}

		if (config.getIpregctrl().trim() != "") {
			String[] regctrl = config.getIpregctrl().trim().split("\n");
			if (Utils.inIPArray(LForumRequest.getIp(), regctrl)) {
				Users userinfo = userManager.getUserByIp(LForumRequest.getIp());
				if (userinfo != null) {
					int interval = Utils.null2Int(Utils.howLong("h", userinfo.getJoindate(), Utils.getNowTime()), 0);
					if (interval < 72) {
						reqcfg.addErrLine("抱歉, 系统设置了特殊IP注册限制, 您必须在 " + (72 - interval) + " 小时后才可以注册");
						return SUCCESS;
					}
				}
			}
		}
		//如果提交了用户注册信息...
		if (!createuser.equals("") && ispost) {
			reqcfg.setShowBackLink(true);

			String tmpUsername = LForumRequest.getParamValue("username");

			String email = LForumRequest.getParamValue("email").toLowerCase();

			String tmpBday = LForumRequest.getParamValue("bday");
			if (tmpBday == "") {
				tmpBday = LForumRequest.getParamValue("bday_y") + "-" + LForumRequest.getParamValue("bday_m") + "-"
						+ LForumRequest.getParamValue("bday_d");
			}
			if (tmpBday.equals("--")) {
				tmpBday = "";
			}

			validateUserInfo(tmpUsername, email, tmpBday);

			if (reqcfg.isErr()) {
				return SUCCESS;
			}

			if (userManager.exists(tmpUsername)) {
				//如果用户名符合注册规则, 则判断是否已存在
				reqcfg.addErrLine("请不要重复提交！");
				return SUCCESS;
			}
			// 如果找不到0积分的用户组则用户自动成为待验证用户
			Admingroups admingroups = new Admingroups();
			admingroups.setAdmingid(0);
			Topics topics = new Topics();
			topics.setTid(0);
			Userfields userfields = new Userfields();
			List<UserExtcreditsInfo> creditList = scoresetManager.getScoreSet();

			Users userinfo = new Users();
			userfields.setUsers(userinfo);
			userinfo.setUserfields(userfields);
			userinfo.setUsername(tmpUsername);
			userinfo.setNickname(LForumRequest.getParamValue("nickname"));
			userinfo.setPassword(MD5.encode(LForumRequest.getParamValue("password")));
			userinfo.setSecques(ForumUtils.getUserSecques(LForumRequest.getParamIntValue("question", 0), LForumRequest
					.getParamValue("answer")));
			userinfo.setGender(LForumRequest.getParamIntValue("gender", 0));
			userinfo.setAdmingroups(admingroups);
			userinfo.setGroupexpiry(0);
			userinfo.setExtgroupids("");
			userinfo.setRegip(LForumRequest.getIp());
			userinfo.setJoindate(Utils.getNowTime());
			userinfo.setLastip(LForumRequest.getIp());
			userinfo.setLastvisit(Utils.getNowTime());
			userinfo.setLastactivity(Utils.getNowTime());
			userinfo.setLastpost(Utils.getNowTime());
			userinfo.setTopics(topics);
			userinfo.setLastposttitle("");
			userinfo.setPosts(0);
			userinfo.setDigestposts(0);
			userinfo.setOltime(0);
			userinfo.setPageviews(0);
			userinfo.setCredits(0);
			userinfo.setExtcredits1(creditList.get(0).getInit());
			userinfo.setExtcredits2(creditList.get(1).getInit());
			userinfo.setExtcredits3(creditList.get(2).getInit());
			userinfo.setExtcredits4(creditList.get(3).getInit());
			userinfo.setExtcredits5(creditList.get(4).getInit());
			userinfo.setExtcredits6(creditList.get(5).getInit());
			userinfo.setExtcredits7(creditList.get(6).getInit());
			userinfo.setExtcredits8(creditList.get(7).getInit());
			userinfo.setAvatarshowid(0);
			userinfo.setEmail(email);
			userinfo.setBday(tmpBday);
			userinfo.setSigstatus(LForumRequest.getParamIntValue("sigstatus", 0));
			userinfo.setOnlinestate(0);
			userinfo.setSpaceid(0);

			if (userinfo.getSigstatus() != 0) {
				userinfo.setSigstatus(1);
			}
			userinfo.setTpp(LForumRequest.getParamIntValue("tpp", 0));
			userinfo.setPpp(LForumRequest.getParamIntValue("ppp", 0));
			userinfo.setTemplateid(LForumRequest.getParamIntValue("templateid", 0));
			userinfo.setPmsound(LForumRequest.getParamIntValue("pmsound", 0));
			userinfo.setShowemail(LForumRequest.getParamIntValue("showemail", 0));

			int receivepmsetting = 1;
			for (String rpms : LForumRequest.getParamValues("receivesetting")) {
				if (!rpms.equals("")) {
					int tmp = Utils.null2Int(rpms);
					receivepmsetting = receivepmsetting | tmp;
				}
			}

			if (config.getRegadvance() == 0) {
				receivepmsetting = 7;
			}

			userinfo.setNewsletter(receivepmsetting);
			userinfo.setInvisible(LForumRequest.getParamIntValue("invisible", 0));
			userinfo.setNewpm(0);
			userinfo.setNewpmcount(0);
			userinfo.getUserfields().setMedals("");
			if (config.getWelcomems() == 1) {
				userinfo.setNewpm(1);
				userinfo.setNewpmcount(1);
			}
			userinfo.setAccessmasks(LForumRequest.getParamIntValue("accessmasks", 0));
			userinfo.getUserfields().setWebsite(LForumRequest.getParamValue("website"));
			userinfo.getUserfields().setIcq(LForumRequest.getParamValue("icq"));
			userinfo.getUserfields().setQq(LForumRequest.getParamValue("qq"));
			userinfo.getUserfields().setYahoo(LForumRequest.getParamValue("yahoo"));
			userinfo.getUserfields().setMsn(LForumRequest.getParamValue("msn"));
			userinfo.getUserfields().setSkype(LForumRequest.getParamValue("skype"));
			userinfo.getUserfields().setLocation(LForumRequest.getParamValue("location"));
			if (usergroupinfo.getAllowcstatus() == 1) {
				userinfo.getUserfields().setCustomstatus(LForumRequest.getParamValue("customstatus"));
			} else {
				userinfo.getUserfields().setCustomstatus("");
			}
			userinfo.getUserfields().setAvatar("avatars/common/0.gif");
			userinfo.getUserfields().setAvatarwidth(0);
			userinfo.getUserfields().setAvatarheight(0);
			userinfo.getUserfields().setBio(LForumRequest.getParamValue("bio"));
			userinfo.getUserfields().setSignature(LForumRequest.getParamValue("signature"));

			// 此处需要添加用户论坛状态PostpramsInfo

			userinfo.getUserfields().setSightml("");

			//
			userinfo.getUserfields().setAuthtime(Utils.getNowTime());

			Usergroups usergroups = new Usergroups();

			//邮箱激活链接验证
			if (config.getRegverify() == 1) {
				userinfo.getUserfields().setAuthstr(Utils.getRandomString(20));
				userinfo.getUserfields().setAuthflag(1);
				usergroups.setGroupid(8);
				userinfo.setUsergroups(usergroups);

				// 此处发送激活邮件

			}
			//系统管理员进行后台验证
			else if (config.getRegverify() == 2) {
				userinfo.getUserfields().setAuthstr(LForumRequest.getParamValue("website"));
				userinfo.getUserfields().setAuthflag(1);
				usergroups.setGroupid(8);
				userinfo.setUsergroups(usergroups);
			} else {
				userinfo.getUserfields().setAuthstr("");
				userinfo.getUserfields().setAuthflag(0);
				usergroups.setGroupid(11);
				userinfo.setUsergroups(usergroups);
			}
			userinfo.getUserfields().setRealname(LForumRequest.getParamValue("realname"));
			userinfo.getUserfields().setIdcard(LForumRequest.getParamValue("idcard"));
			userinfo.getUserfields().setMobile(LForumRequest.getParamValue("mobile"));
			userinfo.getUserfields().setPhone(LForumRequest.getParamValue("phone"));

			userManager.createUser(userinfo);

			// 更新统计信息
			ForumStatistics statistics = statisticManager.getStatistic();
			statistics.setLastusername(userinfo.getUsername());
			statistics.setTotalusers(statistics.getTotalusers() + 1);
			statistics.setUsers(userinfo);
			statisticManager.updateStatistics(statistics);

			if (config.getWelcomems() == 1) {
				Pms privatemessageinfo = new Pms();
				String curdatetime = Utils.getNowTime();
				Users sysUsers = new Users();
				sysUsers.setUid(0);
				// 收件箱
				privatemessageinfo.setMessage(config.getWelcomemsgtxt());
				privatemessageinfo.setSubject("欢迎您的加入! (请勿回复本信息)");
				privatemessageinfo.setMsgto(userinfo.getUsername());
				privatemessageinfo.setUsersByMsgtoid(userinfo);
				privatemessageinfo.setMsgfrom(GlobalsKeys.SYSTEM_USERNAME);
				privatemessageinfo.setUsersByMsgfromid(sysUsers);
				privatemessageinfo.setNew_(1);
				privatemessageinfo.setPostdatetime(curdatetime);
				privatemessageinfo.setFolder(0);
				privatemessageinfo.setUsersByMsgfromid(sysUsers);

				messageManager.createPrivateMessage(privatemessageinfo, 0);
			}

			if (config.getRegverify() == 0) {
				userCreditManager.updateUserCredits(userinfo.getUid());
				ForumUtils.writeUserCookie(userinfo, -1, config.getPasswordkey(), 0, -1);
				onlineUserManager.updateAction(olid, ForumAction.Register.ACTION_ID, 0, config.getOnlinetimeout());

				reqcfg.setUrl("main.action").setMetaRefresh().setShowBackLink(false).addMsgLine("注册成功, 返回登录页");
			} else {
				reqcfg.setUrl("main.action").setMetaRefresh(5).setShowBackLink(false);
				if (config.getRegverify() == 1) {
					reqcfg.addMsgLine("注册成功, 请您到您的邮箱中点击激活链接来激活您的帐号");
				}

				if (config.getRegverify() == 2) {
					reqcfg.addMsgLine("注册成功, 但需要系统管理员审核您的帐户后才可登陆使用");
				}
			}
			agree = "yes";
		}

		return SUCCESS;
	}

	private void validateUserInfo(String username, String email, String birthday) {

		/*
		 * 检测用户名
		 */

		if (username.equals("")) {
			reqcfg.addErrLine("用户名不能为空");
			return;
		}
		if (username.length() > 20) {
			//如果用户名超过20...
			reqcfg.addErrLine("用户名不得超过20个字符");
			return;
		}
		if (username.getBytes().length < 3) {
			reqcfg.addErrLine("用户名不得小于3个字符");
			return;
		}
		if (username.indexOf("　") != -1) {
			//如果用户名符合注册规则, 则判断是否已存在
			reqcfg.addErrLine("用户名中不允许包含全角空格符");
			return;
		}
		if (username.indexOf(" ") != -1) {
			//如果用户名符合注册规则, 则判断是否已存在
			reqcfg.addErrLine("用户名中不允许包含空格");
			return;
		}
		if (username.indexOf(":") != -1) {
			//如果用户名符合注册规则, 则判断是否已存在
			reqcfg.addErrLine("用户名中不允许包含冒号");
			return;
		}
		if (userManager.exists(username)) {
			//如果用户名符合注册规则, 则判断是否已存在
			reqcfg.addErrLine("该用户名已存在");
			return;
		}
		if ((!Utils.isSafeSqlString(username)) || (!Utils.isSafeUserInfoString(username))) {
			reqcfg.addErrLine("用户名中存在非法字符");
			return;
		}
		// 如果用户名属于禁止名单, 或者与负责发送新用户注册欢迎信件的用户名称相同...
		if (username.equals(GlobalsKeys.SYSTEM_USERNAME)) {
			reqcfg.addErrLine("用户名 \"" + username + "\" 不允许在本论坛使用");
			return;
		}

		/*
		 * 检测用户密码
		 */

		if (LForumRequest.getParamValue("password").equals("")) {
			reqcfg.addErrLine("密码不能为空");
			return;
		}
		if (!LForumRequest.getParamValue("password").equals(LForumRequest.getParamValue("password2"))) {
			reqcfg.addErrLine("两次密码输入必须相同");
			return;
		}
		if (LForumRequest.getParamValue("password").length() < 6) {
			reqcfg.addErrLine("密码不得少于6个字符");
			return;
		}

		/*
		 * 检测邮件
		 */

		if (email.equals("")) {
			reqcfg.addErrLine("Email不能为空");
			return;
		}

		if (!Utils.checkEmail(email)) {
			reqcfg.addErrLine("Email格式不正确");
			return;
		}
		if (config.getDoublee() == 0 && userManager.findUserByEmail(email) != -1) {
			reqcfg.addErrLine("Email: \"" + email + "\" 已经被其它用户注册使用");
			return;
		}

		/*
		 * 实名认证
		 */

		if (config.getRealnamesystem() == 1) {
			if (LForumRequest.getParamValue("realname") == "") {
				reqcfg.addErrLine("真实姓名不能为空");
				return;
			}
			if (LForumRequest.getParamValue("realname").length() > 10) {
				reqcfg.addErrLine("真实姓名不能大于10个字符");
				return;
			}
			if (LForumRequest.getParamValue("idcard") == "") {
				reqcfg.addErrLine("身份证号码不能为空");
				return;
			}
			if (LForumRequest.getParamValue("idcard").length() > 20) {
				reqcfg.addErrLine("身份证号码不能大于20个字符");
				return;
			}
			if (LForumRequest.getParamValue("mobile") == "" && LForumRequest.getParamValue("phone") == "") {
				reqcfg.addErrLine("移动电话号码或固定电话号码必须填写其中一项");
				return;
			}
			if (LForumRequest.getParamValue("mobile").length() > 20) {
				reqcfg.addErrLine("移动电话号码不能大于20个字符");
				return;
			}
			if (LForumRequest.getParamValue("phone").length() > 20) {
				reqcfg.addErrLine("固定电话号码不能大于20个字符");
				return;
			}
		}

		/*
		 * 其他
		 */

		if (LForumRequest.getParamValue("idcard") != ""
				&& !Pattern.compile("^[\\x20-\\x80]+$").matcher(LForumRequest.getParamValue("idcard")).find()) {
			reqcfg.addErrLine("身份证号码中含有非法字符");
			return;
		}

		if (LForumRequest.getParamValue("mobile") != ""
				&& !Pattern.compile("^[\\d|-]+$").matcher(LForumRequest.getParamValue("mobile")).find()) {
			reqcfg.addErrLine("移动电话号码中含有非法字符");
			return;
		}

		if (LForumRequest.getParamValue("phone") != ""
				&& !Pattern.compile("^[\\d|-]+$").matcher(LForumRequest.getParamValue("phone")).find()) {
			reqcfg.addErrLine("固定电话号码中含有非法字符");
			return;
		}

		//用户注册模板中,生日可以单独用一个名为bday的文本框, 也可以分别用bday_y bday_m bday_d三个文本框, 用户可不填写

		if (!Utils.checkDate(birthday, Utils.SHORT_DATEFORMAT) && !birthday.equals("")) {
			reqcfg.addErrLine("生日格式错误, 如果不想填写生日请置空");
			return;
		}
		if (LForumRequest.getParamValue("bio").length() > 500) {
			//如果自我介绍超过500...
			reqcfg.addErrLine("自我介绍不得超过500个字符");
			return;
		}
		if (LForumRequest.getParamValue("signature").length() > 500) {
			//如果签名超过500...
			reqcfg.addErrLine("签名不得超过500个字符");
			return;
		}
	}

	public List<Templates> getTemplatelist() {
		return templatelist;
	}

	public String getCreateuser() {
		return createuser;
	}

	public String getAgree() {
		return agree;
	}
}
