package com.javaeye.lonlysky.lforum.web;

import java.util.List;

import org.apache.struts2.config.ParentPackage;

import com.javaeye.lonlysky.lforum.ForumBaseAction;
import com.javaeye.lonlysky.lforum.comm.LForumRequest;
import com.javaeye.lonlysky.lforum.comm.utils.ForumUtils;
import com.javaeye.lonlysky.lforum.comm.utils.Utils;
import com.javaeye.lonlysky.lforum.entity.forum.Templates;

/**
 * 模板列表选择
 * 
 * @author 黄磊
 *
 */
@ParentPackage("default")
public class ShowtemplateAction extends ForumBaseAction {

	private static final long serialVersionUID = -359048301823159268L;

	/**
	 * 可用模板列表
	 */
	private List<Templates> templatelist;
	
	private int templateid;

	@Override
	public String execute() throws Exception {
		pagetitle = "选择模板";

		if (userid == -1 && config.getGuestcachepagetimeout() > 0) {
			reqcfg.addErrLine("当前的系统设置不允许游客选择模板");
			return SUCCESS;
		}

		templateid = LForumRequest.getParamIntValue("templateid", 0);
		if (templateid > 0) {
			String strtemplateid = templateManager.getValidTemplateIDList();
			if (!Utils.inArray(templateid + "", strtemplateid)) {
				templateid = config.getTemplateid();
			}
			ForumUtils.writeCookie("templateid", templateid + "", 999999);

			String rurl = ForumUtils.getReUrl();
			reqcfg.setUrl(
					(rurl.indexOf("logout.action") > -1 || rurl.indexOf("showtemplate.action") > -1) ? "main.action"
							: rurl).addMsgLine("切换模板成功, 返回切换模板前页面").setMetaRefresh().setShowBackLink(false);
		} else {
			templatelist = templateManager.getValidTemplateList();
			if ((LForumRequest.getUrlReferrer().equals(""))
					|| (LForumRequest.getUrlReferrer().indexOf("showtemplate") > -1)) {
				ForumUtils.writeCookie("reurl", "main.action");
			} else {
				ForumUtils.writeCookie("reurl", LForumRequest.getUrlReferrer());
			}
		}
		return SUCCESS;
	}

	public List<Templates> getTemplatelist() {
		return templatelist;
	}

	public int getTemplateid() {
		return templateid;
	}

}
