package com.javaeye.lonlysky.lforum.web.admin.global;

import java.util.List;

import org.apache.struts2.config.ParentPackage;
import org.springframework.beans.factory.annotation.Autowired;

import com.javaeye.lonlysky.lforum.AdminBaseAction;
import com.javaeye.lonlysky.lforum.comm.LForumRequest;
import com.javaeye.lonlysky.lforum.service.TemplateManager;

/**
 * 模板文件列表
 * 
 * @author 黄磊
 *
 */
@ParentPackage("default")
public class Global_templatetreeAction extends AdminBaseAction {

	private static final long serialVersionUID = -5516002861543588864L;

	@Autowired
	private TemplateManager templateManager;

	private List<String[]> templateFiles;
	private List<String[]> otherFiles;
	private int templateid;
	private String path;
	private String templatename;

	@Override
	public String execute() throws Exception {
		path = LForumRequest.getParamValue("path");
		templatename = LForumRequest.getParamValue("templatename");
		templateid = LForumRequest.getParamIntValue("templateid", 1);
		templateFiles = templateManager.getTemplateFiles(config.getWebpath());
		otherFiles = templateManager.getTemplateOtherFiles(config.getWebpath(), path);
		return SUCCESS;
	}

	public int getTemplateid() {
		return templateid;
	}

	public String getPath() {
		return path;
	}

	public String getTemplatename() {
		return templatename;
	}

	public List<String[]> getTemplateFiles() {
		return templateFiles;
	}

	public List<String[]> getOtherFiles() {
		return otherFiles;
	}
}
