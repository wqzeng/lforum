package com.javaeye.lonlysky.lforum.web.admin.global;

import org.apache.struts2.config.ParentPackage;

import com.javaeye.lonlysky.lforum.AdminBaseAction;
import com.javaeye.lonlysky.lforum.comm.LForumRequest;
import com.javaeye.lonlysky.lforum.config.impl.ConfigLoader;

/**
 * 搜索引擎优化
 * 
 * @author 黄磊
 *
 */
@ParentPackage("default")
public class Global_searchengineAction extends AdminBaseAction {

	private static final long serialVersionUID = -9107829020625538350L;

	@Override
	public String execute() throws Exception {
		if (LForumRequest.isPost()) {
			config.setSeotitle(LForumRequest.getParamValue("seotitle_posttextarea"));
			config.setSeokeywords(LForumRequest.getParamValue("seokeywords_posttextarea"));
			config.setSeodescription(LForumRequest.getParamValue("seodescription_posttextarea"));
			config.setSeohead(LForumRequest.getParamValue("seohead_posttextarea"));

			ConfigLoader configLoader = new ConfigLoader(config.getWebpath());
			configLoader.saveConfig(config);
			adminVistLogManager.insertLog(user, username, usergroup, grouptitle, ip, "搜索引擎优化设置", "");
			registerStartupScript("page", "window.location.href='global_searchengine.action';");
		}
		return SUCCESS;
	}

}
