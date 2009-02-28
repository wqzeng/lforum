package com.javaeye.lonlysky.lforum.web.admin.global;

import org.apache.struts2.config.ParentPackage;
import org.dom4j.Document;
import org.springframework.beans.factory.annotation.Autowired;

import com.javaeye.lonlysky.lforum.AdminBaseAction;
import com.javaeye.lonlysky.lforum.cache.LForumCache;
import com.javaeye.lonlysky.lforum.comm.LForumRequest;
import com.javaeye.lonlysky.lforum.comm.utils.Utils;
import com.javaeye.lonlysky.lforum.comm.utils.XmlElementUtil;
import com.javaeye.lonlysky.lforum.exception.WebException;
import com.javaeye.lonlysky.lforum.service.ScoresetManager;
import com.javaeye.lonlysky.lforum.service.UserCreditManager;

/**
 * 积分设置
 * 
 * @author 黄磊
 *
 */
@ParentPackage("default")
public class Global_scoresetAction extends AdminBaseAction {

	private static final long serialVersionUID = 5946195404893608407L;

	@Autowired
	private ScoresetManager scoresetManager;

	@Autowired
	private UserCreditManager userCreditManager;

	private String formula;
	private String creditstrans;
	private String transfermincredits;
	private String maxincperthread;
	private String creditstax;
	private String exchangemincredits;
	private String maxchargespan;
	private String[][] scoreSets;
	private int updateid = -1;

	@Override
	public String execute() throws Exception {
		scoreSets = scoresetManager.getScoreSets();
		formula = scoresetManager.getScoreCalFormula();
		creditstrans = Utils.null2String(scoresetManager.getCreditsTrans());
		transfermincredits = Utils.null2String(scoresetManager.getTransferMinCredits());
		maxincperthread = Utils.null2String(scoresetManager.getMaxIncPerTopic());
		creditstax = Utils.null2String(scoresetManager.getCreditsTax());
		exchangemincredits = Utils.null2String(scoresetManager.getExchangeMinCredits());
		maxchargespan = Utils.null2String(scoresetManager.getMaxChargeSpan());

		// 如果表单提交
		if (LForumRequest.isPost()) {
			String submitMethod = LForumRequest.getParamValue("submitMethod");
			if (!submitMethod.equals("")) {
				submitMethod = submitMethod.substring(submitMethod.indexOf(":") + 1);
				if (submitMethod.equals("doEidtScore")) {
					doEidtScore();
					return SUCCESS;
				} else if (submitMethod.equals("updateScore")) {
					updateScore();
					return SUCCESS;
				} else if (submitMethod.equals("noUpdateScore")) {
					return SUCCESS;
				}
			}
			if (Utils.null2Double(creditstax.trim(), 2) > 1) {
				registerStartupScript("",
						"<script>alert('积分交易税必须是0--1之间的小数');window.location.href='global_scoreset.action';</script>");
				return SUCCESS;
			}
			if (Utils.null2Double(transfermincredits.trim(), -1) < 0) {
				registerStartupScript("",
						"<script>alert('转账最低余额必须是大于或等于0');window.location.href='global_scoreset.action';</script>");
				return SUCCESS;
			}

			if (Utils.null2Double(exchangemincredits.trim(), -1) < 0) {
				registerStartupScript("",
						"<script>alert('兑换最低余额必须是大于或等于0');window.location.href='global_scoreset.action';</script>");
				return SUCCESS;
			}

			if (Utils.null2Double(maxincperthread.trim(), -1) < 0) {
				registerStartupScript("",
						"<script>alert('单主题最高收入必须是大于或等于0');window.location.href='global_scoreset.action';</script>");
				return SUCCESS;
			}

			if (Utils.null2Double(maxchargespan.trim(), -1) < 0) {
				registerStartupScript("",
						"<script>alert('单主题最高出售时限必须是大于或等于0');window.location.href='global_scoreset.action';</script>");
				return SUCCESS;
			}
			Document document = scoresetManager.getScoreDoc(config.getWebpath());
			document.selectSingleNode("/scoreset/formula/formulacontext").setText(formula);
			document.selectSingleNode("/scoreset/formula/creditstrans").setText(creditstrans);
			document.selectSingleNode("/scoreset/formula/transfermincredits").setText(transfermincredits);
			document.selectSingleNode("/scoreset/formula/maxincperthread").setText(maxincperthread);
			document.selectSingleNode("/scoreset/formula/creditstax").setText(creditstax);
			document.selectSingleNode("/scoreset/formula/exchangemincredits").setText(exchangemincredits);
			document.selectSingleNode("/scoreset/formula/maxchargespan").setText(maxchargespan);
			try {
				XmlElementUtil.saveXML(config.getWebpath() + "WEB-INF/config/scoreset.xml", document);
			} catch (WebException e) {
				registerStartupScript("", "<script>alert('" + e.getMessage()
						+ "');window.location.href='global_scoreset.action';</script>");
				return SUCCESS;
			}

			LForumCache.getInstance().removeCache("Formula");
			LForumCache.getInstance().removeCache("CreditsTrans");
			LForumCache.getInstance().removeCache("CreditsTax");
			LForumCache.getInstance().removeCache("TransferMinCredits");
			LForumCache.getInstance().removeCache("ExchangeMinCredits");
			LForumCache.getInstance().removeCache("MaxIncPerThread");
			LForumCache.getInstance().removeCache("MaxChargeSpan");
			LForumCache.getInstance().removeCache("ValidScoreUnit");

			adminVistLogManager.insertLog(user, username, usergroup, grouptitle, ip, "修改积分设置", "修改积分设置");

			if (LForumRequest.getParamValue("RefreshUserScore").equals("1")) {
				userCreditManager.updateUserCredits(formula);
			}

			registerStartupScript("PAGE", "window.location.href='global_scoreset.action';");
		}
		return SUCCESS;
	}

	/**
	 * 更新指定策略
	 */
	private void updateScore() {
		int scoreID = LForumRequest.getParamIntValue("scoreID", -1);
		if (scoreID == -1) {
			registerStartupScript("",
					"<script>alert('无效的策略ID');window.location.href='global_scoreset.action';</script>");
		}
		String scoreName = LForumRequest.getParamValue("scoreName");
		Document document = scoresetManager.getScoreDoc(config.getWebpath());
		for (int i = 1; i <= 8; i++) {
			String value = LForumRequest.getParamValue("ct" + scoreID + "_ct" + i);
			document.selectSingleNode("/scoreset/record[name=\"" + scoreName + "\"]/extcredits" + i).setText(value);
		}
		try {
			XmlElementUtil.saveXML(config.getWebpath() + "WEB-INF/config/scoreset.xml", document);
		} catch (WebException e) {
			registerStartupScript("", "<script>alert('" + e.getMessage()
					+ "');window.location.href='global_scoreset.action';</script>");
		}
		LForumCache.getInstance().removeCache("ScoreSets");
		LForumCache.getInstance().removeCache("ScoreSet");

		adminVistLogManager.insertLog(user, username, usergroup, grouptitle, ip, "积分设置", "");

		registerStartupScript("PAGE", "window.location.href='global_scoreset.action';");
	}

	/**
	 * 编辑积分策略
	 */
	private void doEidtScore() {
		updateid = LForumRequest.getParamIntValue("updateid", -1);
		if (updateid == -1) {
			registerStartupScript("",
					"<script>alert('无效的策略ID');window.location.href='global_scoreset.action';</script>");
		}
	}

	public int getUpdateid() {
		return updateid;
	}

	public String[][] getScoreSets() {
		return scoreSets;
	}

	public ScoresetManager getScoresetManager() {
		return scoresetManager;
	}

	public void setScoresetManager(ScoresetManager scoresetManager) {
		this.scoresetManager = scoresetManager;
	}

	public String getFormula() {
		return formula;
	}

	public void setFormula(String formula) {
		this.formula = formula;
	}

	public String getCreditstrans() {
		return creditstrans;
	}

	public void setCreditstrans(String creditstrans) {
		this.creditstrans = creditstrans;
	}

	public String getTransfermincredits() {
		return transfermincredits;
	}

	public void setTransfermincredits(String transfermincredits) {
		this.transfermincredits = transfermincredits;
	}

	public String getMaxincperthread() {
		return maxincperthread;
	}

	public void setMaxincperthread(String maxincperthread) {
		this.maxincperthread = maxincperthread;
	}

	public String getCreditstax() {
		return creditstax;
	}

	public void setCreditstax(String creditstax) {
		this.creditstax = creditstax;
	}

	public String getExchangemincredits() {
		return exchangemincredits;
	}

	public void setExchangemincredits(String exchangemincredits) {
		this.exchangemincredits = exchangemincredits;
	}

	public String getMaxchargespan() {
		return maxchargespan;
	}

	public void setMaxchargespan(String maxchargespan) {
		this.maxchargespan = maxchargespan;
	}

}
