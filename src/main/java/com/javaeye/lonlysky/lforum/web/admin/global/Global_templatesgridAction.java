package com.javaeye.lonlysky.lforum.web.admin.global;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.struts2.config.ParentPackage;
import org.springframework.beans.factory.annotation.Autowired;

import com.javaeye.lonlysky.lforum.AdminBaseAction;
import com.javaeye.lonlysky.lforum.cache.LForumCache;
import com.javaeye.lonlysky.lforum.comm.LForumRequest;
import com.javaeye.lonlysky.lforum.comm.utils.Utils;
import com.javaeye.lonlysky.lforum.config.impl.ConfigLoader;
import com.javaeye.lonlysky.lforum.entity.forum.Templates;
import com.javaeye.lonlysky.lforum.service.TemplateManager;

/**
 * 模板列表
 * 
 * @author 黄磊
 *
 */
@ParentPackage("default")
public class Global_templatesgridAction extends AdminBaseAction {

	private static final long serialVersionUID = -6076983086503362403L;

	private List<Templates> templates;

	@Autowired
	private TemplateManager templateManager;

	@Override
	public String execute() throws Exception {
		templates = templateManager.getAllTemplates();
		if (LForumRequest.isPost()) {
			String submitMethod = LForumRequest.getParamValue("submitMethod");
			if (!submitMethod.equals("")) {
				submitMethod = submitMethod.substring(submitMethod.indexOf(":") + 1);
				if (submitMethod.equals("intoDB")) {
					intoDB();
					return SUCCESS;
				} else if (submitMethod.equals("delRec")) {
					delRec();
					return SUCCESS;
				} else if (submitMethod.equals("delTemplates")) {
					delTemplates();
					return SUCCESS;
				}
			}
		}
		return SUCCESS;
	}

	/**
	 * 物理删除模板目录
	 * @throws IOException 
	 */
	private void delTemplates() throws IOException {
		String templateidlist = LForumRequest.getParamValues("templateid", ",");
		if (!templateidlist.equals("")) {
			String tempstr = "," + templateidlist + ",";
			if (tempstr.indexOf(",1_Default,") >= 0) {
				registerStartupScript("",
						"<script>alert('您选中的数据项中含有系统初始化模板,此次提交无法执行');window.location.href='global_templatesgrid.action'</script>");
				return;
			}
			delRec();
			for (Templates template : templates) {
				if (tempstr.indexOf("," + template.getTemplateid() + "_" + template.getName() + ",") != -1) {
					String folderpath = config.getWebpath() + "templates\\" + template.getDirectory();
					if (Utils.fileExists(folderpath)) {
						FileUtils.deleteDirectory(new File(folderpath));
					}
				}
			}
			response.sendRedirect("global_templatesgrid.action");
		} else {
			registerStartupScript("",
					"<script>alert('您未选中任何选项'); window.location.href='global_templatesgrid.action';</script>");
		}

	}

	/**
	 * 模板出库
	 */
	private void delRec() {
		String templateidlist = LForumRequest.getParamValues("templateid", ",");
		String tempstr = "," + templateidlist + ",";
		if (tempstr.indexOf(",1_Default,") >= 0) {
			registerStartupScript("",
					"<script>alert('您选中的数据项中含有系统初始化模板,此次提交无法执行');window.location.href='global_templatesgrid.action'</script>");
			return;
		}
		if (tempstr.indexOf("," + config.getTemplateid() + "_"
				+ templateManager.getTemplateItem(config.getTemplateid()).getName().trim() + ",") >= 0) { //当要删除的模板是系统的默认模板时
			config.setTemplateid(1);
			ConfigLoader configLoader = new ConfigLoader(config.getWebpath());
			configLoader.saveConfig(config);
		}
		tempstr = null;
		for (String tmp : templateidlist.split(",")) {
			if (tempstr == null) {
				tempstr = tmp.substring(0, tmp.indexOf("_"));
			} else {
				tempstr += "," + tmp.substring(0, tmp.indexOf("_"));
			}
		}
		templateManager.updateForumAndUserTemplateId(tempstr);
		templateManager.deleteTemplateItem(tempstr);
		LForumCache.getInstance().removeCache("ValidTemplateIDList");
		LForumCache.getInstance().removeCache("ValidTemplateList");
		LForumCache.getInstance().removeCache("TemplateList");
		LForumCache.getInstance().removeCache("TemplateIDList");
		LForumCache.getInstance().removeCache("TemplateListBoxOptions");
		adminVistLogManager.insertLog(user, username, usergroup, grouptitle, ip, "从数据库中删除模板文件", templateidlist.replace(
				"0 ", "").trim());
		registerStartupScript("PAGE", "window.location.href='global_templatesgrid.action';");
	}

	/**
	 * 模板入库
	 */
	private void intoDB() {
		String templateidlist = LForumRequest.getParamValues("templateid", ",");
		if (!templateidlist.equals("")) {
			String tempstr = "," + templateidlist + ",";
			if (tempstr.indexOf(",1_Default,") >= 0) {
				registerStartupScript("",
						"<script>alert('您选中的数据项中含有系统初始化模板,此次提交无法执行');window.location.href='global_templatesgrid.action'</script>");
				return;
			}
			String[] valids = templateManager.getValidTemplateIDList().split(",");
			for (String templateid : templateidlist.split(",")) {
				for (String id : valids) {
					if (id.equals(templateid.substring(0, templateid.indexOf("_")))) {
						registerStartupScript("",
								"<script>alert('选中入库的模板中含有已入库的模板,此次提交无法执行');window.location.href='global_templatesgrid.action'</script>");
						return;
					}
				}
			}
			for (Templates template : templates) {
				if (tempstr.indexOf("," + template.getTemplateid() + "_" + template.getName() + ",") != -1) {
					templateManager.intoDB(template);
					adminVistLogManager.insertLog(user, username, usergroup, grouptitle, ip, "模板文件入库", template
							.getName());
				}
			}
			LForumCache.getInstance().removeCache("ValidTemplateIDList");
			LForumCache.getInstance().removeCache("ValidTemplateList");
			LForumCache.getInstance().removeCache("TemplateList");
			LForumCache.getInstance().removeCache("TemplateIDList");
			LForumCache.getInstance().removeCache("TemplateListBoxOptions");
			registerStartupScript("PAGE", "window.location.href='global_templatesgrid.action';");
		} else {
			registerStartupScript("",
					"<script>alert('您未选中任何选项');window.location.href='global_templatesgrid.aciont';</script>");
		}
	}

	public List<Templates> getTemplates() {
		return templates;
	}
}
