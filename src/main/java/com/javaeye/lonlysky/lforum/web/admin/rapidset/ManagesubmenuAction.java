package com.javaeye.lonlysky.lforum.web.admin.rapidset;

import java.util.ArrayList;
import java.util.List;

import org.apache.struts2.config.ParentPackage;
import org.dom4j.Element;

import com.javaeye.lonlysky.lforum.AdminBaseAction;
import com.javaeye.lonlysky.lforum.comm.LForumRequest;
import com.javaeye.lonlysky.lforum.comm.utils.Utils;
import com.javaeye.lonlysky.lforum.comm.utils.XmlElementUtil;
import com.javaeye.lonlysky.lforum.service.admin.MenuManager;

/**
 * 模块分类菜单管理
 * 
 * @author 黄磊
 *
 */
@ParentPackage("default")
public class ManagesubmenuAction extends AdminBaseAction {

	private static final long serialVersionUID = 674814887236907400L;

	private List<Element> submenuList = new ArrayList<Element>();
	private String menuTitle = "";
	private int menuid;

	@Override
	public String execute() throws Exception {
		int submenuid = LForumRequest.getParamIntValue("submenuid", -1);
		String mode = LForumRequest.getParamValue("mode");
		if (submenuid != -1) {
			if (mode.equals("del")) {
				MenuManager.deleteSubMenu(submenuid, menuid);
			} else {
				if (submenuid == 0) {
					MenuManager.newSubMenu(menuid, LForumRequest.getParamValue("menutitle"));
				} else {
					MenuManager.editSubMenu(submenuid, LForumRequest.getParamValue("menutitle"));
				}
			}
			response.sendRedirect("managesubmenu.action?menuid=" + menuid);
			return SUCCESS;
		}
		if (!LForumRequest.isPost()) {
			bindData();
		}
		return SUCCESS;
	}

	/**
	 * 绑定数据
	 */
	private void bindData() {
		String menuSubMenuList = "";
		Element root = XmlElementUtil.readXML(MenuManager.CONFIG_PATH).getRootElement();
		List<Element> mainMenus = XmlElementUtil.findElements("toptabmenu", root);
		for (Element menuItem : mainMenus) { //查找主菜单信息
			if (menuItem.elementTextTrim("id").equals(Utils.null2String(menuid))) {
				menuTitle = menuItem.elementTextTrim("title");
				menuSubMenuList = menuItem.elementTextTrim("mainmenulist");
				break;
			}
		}
		List<Element> subMenus = XmlElementUtil.findElements("mainmenu", root);
		if (menuSubMenuList.equals("")) { //该菜单没有子菜单
			return;
		}
		for (Element subMenuItem : subMenus) {
			if (("," + menuSubMenuList + ",").indexOf("," + subMenuItem.elementTextTrim("id") + ",") != -1) {
				subMenuItem.addElement("submenuid").setText(subMenuItem.elementTextTrim("menuid"));
				subMenuItem.element("menuid").setText(menuid + "");
				if (findSubMenuItem(subMenuItem.elementTextTrim("submenuid"), root)) {
					subMenuItem.addElement("delitem").setText("删除");
				} else {
					subMenuItem.addElement("delitem").setText(
							"<a href='managesubmenu.action?mode=del&menuid=" + menuid + "&submenuid="
									+ subMenuItem.elementText("id")
									+ "' onclick='return confirm(\"您确认要删除此菜单项吗？\");'>删除</a>");
				}
				submenuList.add(subMenuItem);
			}
		}

	}

	/**
	 * 查找是否有子菜单
	 * @param menuid
	 * @param root
	 * @return
	 */
	private boolean findSubMenuItem(String menuid, Element root) {
		List<Element> submenuitem = XmlElementUtil.findElements("submain", root);
		for (Element item : submenuitem) {
			if (item.elementTextTrim("menuparentid").equals(menuid))
				return true;
		}
		return false;
	}

	public int getMenuid() {
		return menuid;
	}

	public void setMenuid(int menuid) {
		this.menuid = menuid;
	}

	public List<Element> getSubmenuList() {
		return submenuList;
	}

	public String getMenuTitle() {
		return menuTitle;
	}
}
