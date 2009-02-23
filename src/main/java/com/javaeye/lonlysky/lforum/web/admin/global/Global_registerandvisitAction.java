package com.javaeye.lonlysky.lforum.web.admin.global;

import java.util.HashMap;
import java.util.Map;

import org.apache.struts2.config.ParentPackage;

import com.javaeye.lonlysky.lforum.AdminBaseAction;
import com.javaeye.lonlysky.lforum.comm.LForumRequest;
import com.javaeye.lonlysky.lforum.comm.utils.Utils;
import com.javaeye.lonlysky.lforum.config.impl.ConfigLoader;

/**
 * 注册与访问控制
 * 
 * @author 黄磊
 *
 */
@ParentPackage("default")
public class Global_registerandvisitAction extends AdminBaseAction {

	private static final long serialVersionUID = -8490848492599465416L;

	@Override
	public String execute() throws Exception {
		if (LForumRequest.isPost()) {
			config.setRegstatus(LForumRequest.getParamIntValue("regstatus", 0));
			config.setCensoruser(LForumRequest.getParamValue("censoruser_posttextarea"));
			config.setDoublee(LForumRequest.getParamIntValue("doublee", 0));
			config.setRegverify(LForumRequest.getParamIntValue("regverify", 0));
			config.setAccessemail(LForumRequest.getParamValue("accessemail_posttextarea"));
			config.setCensoremail(LForumRequest.getParamValue("censoremail_posttextarea"));
			config.setHideprivate(LForumRequest.getParamIntValue("hideprivate", 0));
			config.setIpdenyaccess(LForumRequest.getParamValue("ipdenyaccess_posttextarea"));
			config.setIpaccess(LForumRequest.getParamValue("ipaccess_posttextarea"));
			config.setRegctrl(LForumRequest.getParamIntValue("regctrl", 0));
			config.setIpregctrl(LForumRequest.getParamValue("ipregctrl_posttextarea"));
			config.setAdminipaccess(LForumRequest.getParamValue("adminipaccess_posttextarea"));
			config.setWelcomems(LForumRequest.getParamIntValue("welcomems", 0));
			config.setWelcomemsgtxt(LForumRequest.getParamValue("welcomemsgtxt_posttextarea"));
			config.setRules(LForumRequest.getParamIntValue("rules", 0));
			config.setRulestxt(LForumRequest.getParamValue("rulestxt_posttextarea"));
			config.setNewbiespan(LForumRequest.getParamIntValue("newbiespan", 0));
			config.setRegadvance(LForumRequest.getParamIntValue("regadvance", 0));
			config.setRealnamesystem(LForumRequest.getParamIntValue("realnamesystem", 0));

			Map<String, String> ipMap = new HashMap<String, String>();
			ipMap.put("特殊 IP 注册限制", LForumRequest.getParamValue("ipregctrl_posttextarea"));
			ipMap.put("IP 禁止访问列表", LForumRequest.getParamValue("ipdenyaccess_posttextarea"));
			ipMap.put("IP 访问列表", LForumRequest.getParamValue("ipaccess_posttextarea"));
			ipMap.put("管理员后台IP访问列表", LForumRequest.getParamValue("adminipaccess_posttextarea"));

			Map<Integer, String> keyMap = new HashMap<Integer, String>();
			if (!Utils.isRuleTip(ipMap, "ip", keyMap)) {
				registerStartupScript("erro", "<script>alert('" + keyMap.get(0) + ",IP格式错误');</script>");
				return SUCCESS;
			}

			Map<String, String> emailMap = new HashMap<String, String>();
			emailMap.put("Email 允许地址", LForumRequest.getParamValue("accessemail_posttextarea"));
			emailMap.put("Email 禁止地址", LForumRequest.getParamValue("censoremail_posttextarea"));

			if (!Utils.isRuleTip(emailMap, "email", keyMap)) {
				registerStartupScript("erro", "<script>alert('" + keyMap.get(0) + ",Email格式错误');</script>");
				return SUCCESS;
			}

			ConfigLoader configLoader = new ConfigLoader(config.getWebpath());
			configLoader.saveConfig(config);
			adminVistLogManager.insertLog(user, username, usergroup, grouptitle, ip, "注册与访问控制设置", "");
			registerStartupScript("PAGE", "window.location.href='global_registerandvisit.action';");
		}
		return SUCCESS;
	}
}
