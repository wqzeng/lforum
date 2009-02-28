package com.javaeye.lonlysky.lforum.web.admin.global;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.apache.struts2.config.ParentPackage;

import com.javaeye.lonlysky.lforum.AdminBaseAction;
import com.javaeye.lonlysky.lforum.comm.LForumRequest;
import com.javaeye.lonlysky.lforum.comm.utils.Utils;

/**
 * 模板文件编辑
 * 
 * @author 黄磊
 *
 */
@ParentPackage("default")
public class Global_templateseditAction extends AdminBaseAction {

	private static final long serialVersionUID = -2836258538665367111L;

	private String filename;
	private int templateid;
	private String path;
	private String templatename;
	private String content;

	@Override
	public String execute() throws Exception {
		filename = LForumRequest.getParamValue("filename");
		path = LForumRequest.getParamValue("path");
		templateid = LForumRequest.getParamIntValue("templateid", 1);
		templatename = LForumRequest.getParamValue("templatename");
		if (!LForumRequest.isPost()) {
			// 读取文件
			if (Utils.fileExists(config.getWebpath() + "WEB-INF/template/" + filename)) {
				content = FileUtils.readFileToString(new File(config.getWebpath() + "WEB-INF/template/" + filename),
						"UTF-8");
			} else if (Utils.fileExists(config.getWebpath() + "WEB-INF/template/inc/commfiles/" + filename)) {
				content = FileUtils.readFileToString(new File(config.getWebpath() + "WEB-INF/template/inc/commfiles/"
						+ filename), "UTF-8");
			} else if (Utils.fileExists(config.getWebpath() + "templates/" + path + "/" + filename)) {
				content = FileUtils.readFileToString(new File(config.getWebpath() + "templates/" + path + "/"
						+ filename), "UTF-8");
			} else {
				registerStartupScript("", "<script>alert('找不到文件：" + filename
						+ "');window.location.href='global_templatesedit.action';</script>");
			}
		} else {
			// 保存文件
			content = LForumRequest.getParamValue("templatenew_posttextarea");
			if (Utils.fileExists(config.getWebpath() + "WEB-INF/template/" + filename)) {
				FileUtils.writeStringToFile(new File(config.getWebpath() + "WEB-INF/template/" + filename), content,
						"UTF-8");
			} else if (Utils.fileExists(config.getWebpath() + "WEB-INF/template/inc/commfiles/" + filename)) {
				FileUtils.writeStringToFile(
						new File(config.getWebpath() + "WEB-INF/template/inc/commfiles/" + filename), content, "UTF-8");
			} else if (Utils.fileExists(config.getWebpath() + "templates/" + path + "/" + filename)) {
				FileUtils.writeStringToFile(new File(config.getWebpath() + "templates/" + path + "/" + filename),
						content, "UTF-8");
			}
			registerStartupScript("PAGE", "window.location.href='global_templatetree.action?path=" + path
					+ "&templateid=" + templateid + "&templatename=" + templatename + "';");
		}

		return SUCCESS;
	}

	public String getContent() {
		return content;
	}

	public String getFilename() {
		return filename;
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
}
