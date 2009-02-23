package com.javaeye.lonlysky.lforum.web.admin.global;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.struts2.config.ParentPackage;

import com.javaeye.lonlysky.lforum.AdminBaseAction;
import com.javaeye.lonlysky.lforum.comm.LForumRequest;
import com.javaeye.lonlysky.lforum.comm.utils.Utils;
import com.javaeye.lonlysky.lforum.config.impl.ConfigLoader;

/**
 * 安全控制
 * 
 * @author 黄磊
 *
 */
@ParentPackage("default")
public class Global_safecontrolAction extends AdminBaseAction {

	private static final long serialVersionUID = 7529992970885945117L;

	private String seccodestatus;
	private String maxonlines;
	private String searchctrl;
	private int secques;
	private String postinterval;
	private String maxspm;

	@Override
	public String execute() throws Exception {
		if (!LForumRequest.isPost()) {
			seccodestatus = config.getSeccodestatus().replace(",", "\r\n");
		} else {
			Map<String, String> checkMap = new HashMap<String, String>();
			checkMap.put("最大在线人数", maxonlines);
			checkMap.put("发帖灌水预防", postinterval);
			checkMap.put("搜索时间限制", searchctrl);
			checkMap.put("60 秒最大搜索次数", maxspm);
			for (Entry<String, String> entry : checkMap.entrySet()) {
				if (!Utils.isInt(entry.getValue())) {
					registerStartupScript("", "<script>alert('输入错误:" + entry.getKey()
							+ ",只能是0或者正整数');window.location.href='global_safecontrol.action';</script>");
					return SUCCESS;
				}
			}

			config.setMaxonlines(Utils.null2Int(maxonlines));
			config.setPostinterval(Utils.null2Int(postinterval));
			config.setSearchctrl(Utils.null2Int(searchctrl));
			config.setSeccodestatus(seccodestatus.trim().replace("\r\n", ","));
			config.setMaxspm(Utils.null2Int(maxspm));
			config.setSecques(secques);

			ConfigLoader configLoader = new ConfigLoader(config.getWebpath());
			configLoader.saveConfig(config);
			adminVistLogManager.insertLog(user, username, usergroup, grouptitle, ip, "安全控制设置", "");
			registerStartupScript("PAGE", "window.location.href='global_safecontrol.action';");
		}
		return SUCCESS;
	}

	public String getSeccodestatus() {
		return seccodestatus;
	}

	public void setSeccodestatus(String seccodestatus) {
		this.seccodestatus = seccodestatus;
	}

	public String getMaxonlines() {
		return maxonlines;
	}

	public void setMaxonlines(String maxonlines) {
		this.maxonlines = maxonlines;
	}

	public String getSearchctrl() {
		return searchctrl;
	}

	public void setSearchctrl(String searchctrl) {
		this.searchctrl = searchctrl;
	}

	public int getSecques() {
		return secques;
	}

	public void setSecques(int secques) {
		this.secques = secques;
	}

	public String getPostinterval() {
		return postinterval;
	}

	public void setPostinterval(String postinterval) {
		this.postinterval = postinterval;
	}

	public String getMaxspm() {
		return maxspm;
	}

	public void setMaxspm(String maxspm) {
		this.maxspm = maxspm;
	}
}
