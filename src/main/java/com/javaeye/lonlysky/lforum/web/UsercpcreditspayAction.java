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
 * 积分兑换
 * 
 * @author 黄磊
 *
 */
@ParentPackage("default")
public class UsercpcreditspayAction extends ForumBaseAction {

	private static final long serialVersionUID = -4888414795837851269L;

	/**
	 * 扩展积分列表
	 */
	private List<Map<String, Object>> extcreditspaylist;

	/**
	 * 积分交易税
	 */
	private double creditstax;

	/**
	 * 积分计算器js脚本
	 */
	private String jscreditsratearray;

	/**
	 * 当前用户信息
	 */
	private Users user = new Users();

	/**
	 * 可用的积分名称列表
	 */
	private String[] score;

	private double extcredits1rate, extcredits2rate;

	@Autowired
	private ScoresetManager scoresetManager;

	@Autowired
	private CreditsLogManager creditsLogManager;

	@Override
	public String execute() throws Exception {
		pagetitle = "用户控制面板";

		creditstax = scoresetManager.getCreditsTax();
		extcreditspaylist = scoresetManager.getScorePaySet(0);
		score = scoresetManager.getValidScoreName();

		jscreditsratearray = "<script type=\"text/javascript\">\r\nvar creditsrate = new Array();\r\n";
		for (Map<String, Object> map : extcreditspaylist) {
			jscreditsratearray = jscreditsratearray + "creditsrate[" + map.get("id") + "] = " + map.get("rate")
					+ ";\r\n";
		}
		jscreditsratearray = jscreditsratearray + "\r\n</script>";

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
				reqcfg.addErrLine("数量必须是大于0的整数");
				return SUCCESS;
			}

			int extcredits1 = LForumRequest.getParamIntValue("extcredits1", 0);
			int extcredits2 = LForumRequest.getParamIntValue("extcredits2", 0);
			if (extcredits1 < 1 || extcredits2 < 1 || extcredits1 > 8 || extcredits2 > 8) {
				reqcfg.addErrLine("请正确选择要兑换的积分类型!");
				return SUCCESS;
			}
			if (extcredits1 == extcredits2) {
				reqcfg.addErrLine("不能兑换相同类型的积分");
				return SUCCESS;
			}

			//对交易后的积分增减进行修改设置
			UserExtcreditsInfo extcredits1info = scoresetManager.getScoreSet(extcredits1);
			UserExtcreditsInfo extcredits2info = scoresetManager.getScoreSet(extcredits2);

			if ((extcredits1info.getName().trim().equals("")) || (extcredits2info.getName().trim().equals(""))) {
				reqcfg.addErrLine("错误的输入!");
				return SUCCESS;
			}

			if ((userManager.getUserExtCredits(userid, extcredits1) - paynum) < scoresetManager.getExchangeMinCredits()) {
				reqcfg.addErrLine("抱歉, 您的 \"" + extcredits1info.getName() + "\" 不足."
						+ scoresetManager.getExchangeMinCredits());
				return SUCCESS;
			}

			//计算并更新2个扩展积分的新值
			extcredits1rate = extcredits1info.getRate();
			extcredits2rate = extcredits2info.getRate();
			Double extcredit2paynum = Utils.round(paynum * (extcredits1rate / extcredits2rate) * (1 - creditstax), 2);
			userManager.updateUserExtCredits(userid, extcredits1, paynum * -1);
			userManager.updateUserExtCredits(userid, extcredits2, extcredit2paynum);
			creditsLogManager.addCreditsLog(userid, userid, extcredits1, extcredits2, paynum, extcredit2paynum
					.floatValue(), Utils.getNowTime(), 1);
			reqcfg.setUrl("usercpcreaditstransferlog.action").setMetaRefresh().setShowBackLink(false).addMsgLine(
					"积分兑换完毕, 正在返回积分兑换与转帐记录");
		}
		return SUCCESS;
	}

	public List<Map<String, Object>> getExtcreditspaylist() {
		return extcreditspaylist;
	}

	public double getCreditstax() {
		return creditstax;
	}

	public String getJscreditsratearray() {
		return jscreditsratearray;
	}

	public Users getUser() {
		return user;
	}

	public String[] getScore() {
		return score;
	}
}
