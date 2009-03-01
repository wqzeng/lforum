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
 * 子菜单项管理
 * 
 * @author 黄磊
 *
 */
@ParentPackage("default")
public class ManagesubmenuitemAction extends AdminBaseAction {

	private static final long serialVersionUID = -735123966531740245L;

	private List<Element> submenuitemList = new ArrayList<Element>();
	private int menuid;
	private Integer submenuid;
	private String pagename;

	@Override
	public String execute() throws Exception {
		int id = LForumRequest.getParamIntValue("id", -2);
		String mode = LForumRequest.getParamValue("mode");
		if (id != -2) {
			if (mode.equals("del")) {
				MenuManager.deleteMenuItem(id);
			} else {
				if (id == -1) {
					MenuManager.newMenuItem(submenuid, LForumRequest.getParamValue("menutitle"), LForumRequest
							.getParamValue("link"));
				} else {
					MenuManager.editMenuItem(id, LForumRequest.getParamValue("menutitle"), LForumRequest
							.getParamValue("link"));
				}
			}
			response.sendRedirect("managesubmenuitem.action?menuid=" + menuid + "&submenuid=" + submenuid
					+ "&pagename=" + Utils.urlEncode(pagename));
			return null;
		} else {
			bindDataGrid();
		}
		return SUCCESS;
	}

	/**
	 * 绑定数据
	 */
	private void bindDataGrid() {
		Element root = XmlElementUtil.readXML(MenuManager.CONFIG_PATH).getRootElement();
		List<Element> elList = XmlElementUtil.findElements("submain", root);
		int i = 0;
		for (Element element : elList) {
			if (element.elementTextTrim("menuparentid").equals(Utils.null2String(submenuid))) {
				element.addElement("id").setText(i + "");
				submenuitemList.add(element);
			}
			i++;
		}

	}

	public int getMenuid() {
		return menuid;
	}

	public void setMenuid(int menuid) {
		this.menuid = menuid;
	}

	public int getSubmenuid() {
		return submenuid;
	}

	public void setSubmenuid(int submenuid) {
		this.submenuid = submenuid;
	}

	public String getPagename() {
		return pagename;
	}

	public void setPagename(String pagename) {
		this.pagename = Utils.stringFormat(Utils.urlDecode(pagename));
	}

	public List<Element> getSubmenuitemList() {
		return submenuitemList;
	}
}
