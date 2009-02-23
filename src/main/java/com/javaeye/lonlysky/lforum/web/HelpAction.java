package com.javaeye.lonlysky.lforum.web;

import java.util.List;

import org.apache.struts2.config.ParentPackage;
import org.springframework.beans.factory.annotation.Autowired;

import com.javaeye.lonlysky.lforum.ForumBaseAction;
import com.javaeye.lonlysky.lforum.comm.LForumRequest;
import com.javaeye.lonlysky.lforum.entity.help.Help;
import com.javaeye.lonlysky.lforum.service.HelpManager;

/**
 * 论坛帮助页面
 * 
 * @author 黄磊
 *
 */
@ParentPackage("default")
public class HelpAction extends ForumBaseAction {

	private static final long serialVersionUID = 8348796189228378946L;

	/**
	 * 帮助列表
	 */
	private List<Help> helplist;

	/**
	 * 数据库类型
	 */
	private String dbtype;

	/**
	 * 产品名称
	 */
	private String assemblyproductname;

	/**
	 * 版权
	 */
	private String Assemblycopyright;

	/**
	 * 显示版本信息
	 */
	private int showversion;

	@Autowired
	private HelpManager helpManager;

	@Override
	public String execute() throws Exception {
		pagetitle = "帮助";
		showversion = LForumRequest.getParamIntValue("version", 0);

		int helpid = LForumRequest.getParamIntValue("hid", 0);
		if (helpid > 0) {
			helplist = helpManager.getHelpList(helpid);
		} else {
			helplist = helpManager.getHelpList();
		}
		if (helplist == null) {
			reqcfg.addErrLine("没有信息可读取！");
		}
		if (showversion == 1) {
			dbtype = config.getDatatype();
			assemblyproductname = "LForum For Java";
			Assemblycopyright = config.getForumcopyright();
		}

		return SUCCESS;
	}

	public List<Help> getHelplist() {
		return helplist;
	}

	public String getDbtype() {
		return dbtype;
	}

	public String getAssemblyproductname() {
		return assemblyproductname;
	}

	public String getAssemblycopyright() {
		return Assemblycopyright;
	}

	public int getShowversion() {
		return showversion;
	}

	public HelpManager getHelpManager() {
		return helpManager;
	}

}
