package com.javaeye.lonlysky.lforum.web.admin.rapidset;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.struts2.config.ParentPackage;
import org.dom4j.Element;

import com.javaeye.lonlysky.lforum.AdminBaseAction;
import com.javaeye.lonlysky.lforum.comm.LForumRequest;
import com.javaeye.lonlysky.lforum.comm.utils.XmlElementUtil;
import com.javaeye.lonlysky.lforum.service.admin.MenuManager;

/**
 * 后台菜单管理
 * 
 * @author 黄磊
 *
 */
@ParentPackage("default")
public class ManagemainmenuAction extends AdminBaseAction {

	private static final long serialVersionUID = 8815399502946327402L;
	private List<Element> toptabmenuList = new ArrayList<Element>();

	@Override
	public String execute() throws Exception {
		String submitMethod = LForumRequest.getParamValue("submitMethod");
		if (!submitMethod.equals("")) {
			submitMethod = submitMethod.substring(submitMethod.indexOf(":") + 1);
			if (submitMethod.equals("createMenu")) {
				createMenu();
			}
		}
		int menuid = LForumRequest.getParamIntValue("menuid", -1);
		String mode = LForumRequest.getParamValue("mode");
		if (!mode.equals("")) {
			if (mode.equals("del")) {
				MenuManager.deleteMainMenu(menuid);
			} else {
				if (menuid == 0) {
					MenuManager.newMainMenu(LForumRequest.getParamValue("menutitle"), LForumRequest
							.getParamValue("defaulturl"));
				} else {
					MenuManager.editMainMenu(menuid, LForumRequest.getParamValue("menutitle"), LForumRequest
							.getParamValue("defaulturl"));
				}
			}
			response.sendRedirect("managemainmenu.action");
			return null;
		} else {
			bindDataGrid();
		}
		return SUCCESS;
	}

	/**
	 * 生成菜单
	 * @throws IOException 
	 */
	private void createMenu() throws IOException {
		MenuManager.createMenuJson();
	}

	/**
	 * 绑定数据
	 */
	private void bindDataGrid() {
		Element root = XmlElementUtil.readXML(MenuManager.CONFIG_PATH).getRootElement();
		toptabmenuList = XmlElementUtil.findElements("toptabmenu", root);
	}

	public List<Element> getToptabmenuList() {
		return toptabmenuList;
	}
}
