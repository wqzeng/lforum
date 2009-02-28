package com.javaeye.lonlysky.lforum.web.admin.forum;

import org.apache.struts2.config.ParentPackage;

import com.javaeye.lonlysky.lforum.AdminBaseAction;
import com.javaeye.lonlysky.lforum.comm.LForumRequest;
import com.javaeye.lonlysky.lforum.comm.utils.Utils;
import com.javaeye.lonlysky.lforum.config.impl.ConfigLoader;

/**
 * 论坛设置选项
 * 
 * @author 黄磊
 *
 */
@ParentPackage("default")
public class Forum_optionAction extends AdminBaseAction {

	private static final long serialVersionUID = -2923627504975329862L;

	private int ratevalveset1;
	private int ratevalveset2;
	private int ratevalveset3;
	private int ratevalveset4;
	private int ratevalveset5;

	@Override
	public String execute() throws Exception {
		String[] ratevalveset = config.getRatevalveset().split(",");
		ratevalveset1 = Utils.null2Int(ratevalveset[0]);
		ratevalveset2 = Utils.null2Int(ratevalveset[1]);
		ratevalveset3 = Utils.null2Int(ratevalveset[2]);
		ratevalveset4 = Utils.null2Int(ratevalveset[3]);
		ratevalveset5 = Utils.null2Int(ratevalveset[4]);

		if (LForumRequest.isPost()) {
			String[][] inputrule = new String[2][];
			inputrule[0] = new String[] { LForumRequest.getParamValue("losslessdel"),
					LForumRequest.getParamValue("edittimelimit"), LForumRequest.getParamValue("tpp"),
					LForumRequest.getParamValue("ppp"), LForumRequest.getParamValue("starthreshold"),
					LForumRequest.getParamValue("hottopic"), LForumRequest.getParamValue("guestcachepagetimeout"),
					LForumRequest.getParamValue("disablepostadregminute"),
					LForumRequest.getParamValue("disablepostadpostcount") };
			inputrule[1] = new String[] { "删帖不减积分时间", "编辑帖子时间", "每页主题数", "每页主题数", "星星升级阀值", "热门话题最低帖数", "缓存游客页面的失效时间",
					"新用户广告强力屏蔽注册分钟数", "新用户广告强力屏蔽发帖数" };
			for (int j = 0; j < inputrule[0].length; j++) {
				if (!Utils.isInt(inputrule[0][j].toString())) {
					registerStartupScript("", "<script>alert('输入错误:" + inputrule[1][j].toString()
							+ ",只能是0或者正整数');window.location.href='forum_option.action';</script>");
					return SUCCESS;
				}
			}
			if (LForumRequest.getParamIntValue("losslessdel", 0) > 9999
					|| LForumRequest.getParamIntValue("losslessdel", 0) < 0) {
				registerStartupScript("",
						"<script>alert('删帖不减积分时间期限只能在0-9999之间');window.location.href='forum_option.action';</script>");
				return SUCCESS;
			}

			if (LForumRequest.getParamIntValue("edittimelimit", 0) > 9999
					|| LForumRequest.getParamIntValue("edittimelimit", 0) < 0) {
				registerStartupScript("",
						"<script>alert('编辑帖子时间限制只能在0-9999之间');window.location.href='forum_option.action';</script>");
				return SUCCESS;
			}

			if (LForumRequest.getParamIntValue("tpp", 0) > 100 || LForumRequest.getParamIntValue("tpp", 0) <= 0) {
				registerStartupScript("",
						"<script>alert('每页主题数只能在1-100之间');window.location.href='forum_option.action';</script>");
				return SUCCESS;
			}

			if (LForumRequest.getParamIntValue("ppp", 0) > 100 || LForumRequest.getParamIntValue("ppp", 0) <= 0) {
				registerStartupScript("",
						"<script>alert('每页帖子数只能在1-100之间');window.location.href='forum_option.action';</script>");
				return SUCCESS;
			}
			if (LForumRequest.getParamIntValue("starthreshold", 0) > 9999
					|| LForumRequest.getParamIntValue("starthreshold", 0) < 0) {
				registerStartupScript("",
						"<script>alert('星星升级阀值只能在0-9999之间');window.location.href='forum_option.action';</script>");
				return SUCCESS;
			}

			if (LForumRequest.getParamIntValue("hottopic", 0) > 9999
					|| LForumRequest.getParamIntValue("hottopic", 0) < 0) {
				registerStartupScript("",
						"<script>alert('热门话题最低帖数只能在0-9999之间');window.location.href='forum_option.action';</script>");
				return SUCCESS;
			}

			if (LForumRequest.getParamIntValue("hottagcount", 0) > 60
					|| LForumRequest.getParamIntValue("hottagcount", 0) < 0) {
				registerStartupScript("",
						"<script>alert('首页热门标签(Tag)数量只能在0-60之间');window.location.href='forum_option.action';</script>");
			}

			if (!validateRatevalveset(LForumRequest.getParamValue("ratevalveset1")))
				return SUCCESS;
			if (!validateRatevalveset(LForumRequest.getParamValue("ratevalveset2")))
				return SUCCESS;
			if (!validateRatevalveset(LForumRequest.getParamValue("ratevalveset3")))
				return SUCCESS;
			if (!validateRatevalveset(LForumRequest.getParamValue("ratevalveset4")))
				return SUCCESS;
			if (!validateRatevalveset(LForumRequest.getParamValue("ratevalveset5")))
				return SUCCESS;
			if (!(LForumRequest.getParamIntValue("ratevalveset1", 0) < LForumRequest.getParamIntValue("ratevalveset2",
					0)
					&& LForumRequest.getParamIntValue("ratevalveset2", 0) < LForumRequest.getParamIntValue(
							"ratevalveset3", 0)
					&& LForumRequest.getParamIntValue("ratevalveset3", 0) < LForumRequest.getParamIntValue(
							"ratevalveset4", 0) && LForumRequest.getParamIntValue("ratevalveset4", 0) < LForumRequest
					.getParamIntValue("ratevalveset5", 0))) {
				registerStartupScript("",
						"<script>alert('评分阀值不是递增取值');window.location.href='forum_option.action';</script>");
				return SUCCESS;
			}
			if (LForumRequest.getParamIntValue("disablepostad", 0) == 1
					&& LForumRequest.getParamValue("disablepostadregular_posttextarea").equals("")) {
				registerStartupScript("",
						"<script>alert('新用户广告强力屏蔽正则表达式为空');window.location.href='forum_option.action';</script>");
				return SUCCESS;
			}

			config.setModworkstatus(LForumRequest.getParamIntValue("modworkstatus", config.getModworkstatus()));
			config.setUserstatusby(LForumRequest.getParamIntValue("userstatusby", config.getUserstatusby()));
			//			if (Topicqueuestats_1.Checked == true) {
			//				config.setTopicQueueStats = 1;
			//			} else {
			//				config.setTopicQueueStats = 0;
			//			}
			config.setTopicqueuestatscount(LForumRequest.getParamIntValue("topicqueuestatscount", config
					.getTopicqueuestatscount()));
			config.setGuestcachepagetimeout(LForumRequest.getParamIntValue("guestcachepagetimeout", config
					.getGuestcachepagetimeout()));
			config.setTopiccachemark(LForumRequest.getParamIntValue("topiccachemark", config.getTopiccachemark()));
			config.setLosslessdel(LForumRequest.getParamIntValue("losslessdel", config.getLosslessdel()));
			config.setEdittimelimit(LForumRequest.getParamIntValue("edittimelimit", config.getEdittimelimit()));
			config.setEditedby(LForumRequest.getParamIntValue("editedby", config.getEditedby()));
			config.setDefaulteditormode(LForumRequest.getParamIntValue("defaulteditormode", config
					.getDefaulteditormode()));
			config.setAllowswitcheditor(LForumRequest.getParamIntValue("allowswitcheditor", config
					.getAllowswitcheditor()));
			config.setReasonpm(LForumRequest.getParamIntValue("reasonpm", config.getReasonpm()));
			config.setHottopic(LForumRequest.getParamIntValue("hottopic", config.getHottopic()));
			config.setStarthreshold(LForumRequest.getParamIntValue("starthreshold", config.getStarthreshold()));
			config.setFastpost(LForumRequest.getParamIntValue("fastpost", config.getFastpost()));
			config.setTpp(LForumRequest.getParamIntValue("tpp", config.getTpp()));
			config.setPpp(LForumRequest.getParamIntValue("ppp", config.getPpp()));
			config.setHtmltitle(LForumRequest.getParamIntValue("allowhtmltitle", config.getHtmltitle()));
			config.setEnabletag(LForumRequest.getParamIntValue("enabletag", config.getEnabletag()));
			config.setRatevalveset(LForumRequest.getParamValue("ratevalveset1") + ","
					+ LForumRequest.getParamValue("ratevalveset2") + "," + LForumRequest.getParamValue("ratevalveset3")
					+ "," + LForumRequest.getParamValue("ratevalveset4") + ","
					+ LForumRequest.getParamValue("ratevalveset5"));
			config.setStatstatus(LForumRequest.getParamIntValue("statstatus", config.getStatstatus()));
			config.setStatscachelife(LForumRequest.getParamIntValue("statscachelife", config.getStatscachelife()));
			config.setHottagcount(LForumRequest.getParamIntValue("hottagcount", config.getHottagcount()));
			config.setOltimespan(LForumRequest.getParamIntValue("oltimespan", config.getOltimespan()));
			config.setMaxmodworksmonths(LForumRequest.getParamIntValue("maxmodworksmonths", config
					.getMaxmodworksmonths()));
			config.setDisablepostad(LForumRequest.getParamIntValue("disablepostad", config.getDisablepostad()));
			config.setDisablepostadregminute(LForumRequest.getParamIntValue("disablepostadregminute", config
					.getDisablepostadregminute()));
			config.setDisablepostadpostcount(LForumRequest.getParamIntValue("disablepostadpostcount", config
					.getDisablepostadpostcount()));
			config.setDisablepostadregular(LForumRequest.getParamValue("disablepostadregular_posttextarea"));

			ConfigLoader configLoader = new ConfigLoader(config.getWebpath());
			configLoader.saveConfig(config);
			adminVistLogManager.insertLog(user, username, usergroup, grouptitle, ip, "论坛功能常规选项设置", "");
			registerStartupScript("PAGE", "window.location.href='forum_option.action';");
		}
		return SUCCESS;
	}

	/**
	 * 验证值
	 * @param val
	 * @return
	 */
	private boolean validateRatevalveset(String val) {
		if (!Utils.isInt(val)) {
			registerStartupScript("",
					"<script>alert('评分各项阀值只能是数字');window.location.href='forum_option.action';</script>");
			return false;
		}
		if (Utils.null2Int(val) > 999 || Utils.null2Int(val) < 1) {
			registerStartupScript("",
					"<script>alert('评分各项阀值只能在1-999之间');window.location.href='forum_option.action';</script>");
			return false;
		} else
			return true;
	}

	public int getRatevalveset1() {
		return ratevalveset1;
	}

	public int getRatevalveset2() {
		return ratevalveset2;
	}

	public int getRatevalveset3() {
		return ratevalveset3;
	}

	public int getRatevalveset4() {
		return ratevalveset4;
	}

	public int getRatevalveset5() {
		return ratevalveset5;
	}
}
