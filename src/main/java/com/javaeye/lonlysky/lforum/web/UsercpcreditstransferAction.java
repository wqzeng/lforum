package com.javaeye.lonlysky.lforum.web;

import java.util.List;
import java.util.Map;

import org.apache.struts2.config.ParentPackage;
import org.springframework.beans.factory.annotation.Autowired;

import com.javaeye.lonlysky.lforum.ForumBaseAction;
import com.javaeye.lonlysky.lforum.comm.LForumRequest;
import com.javaeye.lonlysky.lforum.comm.MD5;
import com.javaeye.lonlysky.lforum.comm.utils.ForumUtils;
import com.javaeye.lonlysky.lforum.comm.utils.Utils;
import com.javaeye.lonlysky.lforum.entity.forum.UserExtcreditsInfo;
import com.javaeye.lonlysky.lforum.entity.forum.Users;
import com.javaeye.lonlysky.lforum.service.CreditsLogManager;
import com.javaeye.lonlysky.lforum.service.ScoresetManager;

/**
 * 积分转账
 * 
 * @author 黄磊
 *
 */
@ParentPackage("default")
public class UsercpcreditstransferAction extends ForumBaseAction {

	private static final long serialVersionUID = 1473286362723332492L;

	/**
	 * 具有兑换比率的可交易积分策略
	 */
	private List<Map<String, Object>> extcreditspaylist;

	/**
	 * 交易税
	 */
	private float creditstax;

	/**
	 * 当前用户信息
	 */
	private Users user = new Users();

	/**
	 * 可用的积分名称列表
	 */
	private String[] score;

	@Autowired
	private ScoresetManager scoresetManager;

	@Autowired
	private CreditsLogManager creditsLogManager;

	@Override
	public String execute() throws Exception {
		pagetitle = "用户控制面板";
		creditstax = scoresetManager.getCreditsTax();

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

			if (!MD5.encode(LForumRequest.getParamValue("password")).equals(password)) {
				reqcfg.addErrLine("密码错误");
				return SUCCESS;
			}

			int paynum = LForumRequest.getParamIntValue("paynum", 0);
			if (paynum <= 0) {
				reqcfg.addErrLine("数量必须是大于等于0的整数");
				return SUCCESS;
			}

			int fromto = userManager.getUserId(LForumRequest.getParamValue("fromto"));
			if (fromto == -1) {
				reqcfg.addErrLine("指定的转帐接受人不存在");
				return SUCCESS;
			}

			int extcredits = LForumRequest.getParamIntValue("extcredits", 0);
			if (extcredits < 1 || extcredits > 8) {
				reqcfg.addErrLine("请正确选择要转帐的积分类型!");
				return SUCCESS;
			}

			//对转帐后的积分增减进行修改设置
			UserExtcreditsInfo extcreditsinfo = scoresetManager.getScoreSet(extcredits);
			if (extcreditsinfo.getName().trim().equals("")) {
				reqcfg.addErrLine("错误的输入!");
				return SUCCESS;
			}

			if ((userManager.getUserExtCredits(userid, extcredits) - paynum) < scoresetManager.getTransferMinCredits()) {
				reqcfg.addErrLine("抱歉, 您的 \"" + extcreditsinfo.getName() + "\" 不足.系统当前规定转帐余额不得小于"
						+ scoresetManager.getTransferMinCredits());
				return SUCCESS;
			}

			//计算并更新2个扩展积分的新值
			Double topaynum = Utils.round(paynum * (1 - creditstax), 2);
			userManager.updateUserExtCredits(userid, extcredits, paynum * -1);
			userManager.updateUserExtCredits(fromto, extcredits, topaynum);
			creditsLogManager.addCreditsLog(userid, fromto, extcredits, extcredits, paynum, topaynum.floatValue(),
					Utils.getNowTime(), 2);

			reqcfg.setUrl("usercpcreaditstransferlog.action").setMetaRefresh().setShowBackLink(false).addMsgLine(
					"积分转帐完毕, 正在返回积分兑换与转帐记录");
		}
		score = scoresetManager.getValidScoreName();
		extcreditspaylist = scoresetManager.getScorePaySet(1);
		return SUCCESS;
	}

	public List<Map<String, Object>> getExtcreditspaylist() {
		return extcreditspaylist;
	}

	public float getCreditstax() {
		return creditstax;
	}

	public Users getUser() {
		return user;
	}

	public String[] getScore() {
		return score;
	}
}
