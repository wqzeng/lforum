package com.javaeye.lonlysky.lforum;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.time.DateUtils;
import org.dom4j.Document;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;

import com.javaeye.lonlysky.lforum.comm.LForumRequest;
import com.javaeye.lonlysky.lforum.comm.utils.ForumUtils;
import com.javaeye.lonlysky.lforum.comm.utils.Utils;
import com.javaeye.lonlysky.lforum.comm.utils.XmlElementUtil;
import com.javaeye.lonlysky.lforum.entity.forum.Online;
import com.javaeye.lonlysky.lforum.entity.forum.Usergroups;
import com.javaeye.lonlysky.lforum.entity.forum.Users;
import com.javaeye.lonlysky.lforum.interceptor.ActionInitAware;
import com.javaeye.lonlysky.lforum.service.OnlineUserManager;
import com.javaeye.lonlysky.lforum.service.UserManager;
import com.javaeye.lonlysky.lforum.service.admin.AdminVistLogManager;
import com.javaeye.lonlysky.lforum.service.admin.GroupManager;

/**
 * 后台管理页面基类
 * 
 * @author 黄磊
 *
 */
public class AdminBaseAction extends BaseAction implements ActionInitAware {

	private static final long serialVersionUID = 1118380429041990169L;

	protected String username;

	/**
	 * 当前用户的用户
	 */
	protected Users user;

	/**
	 * 当前用户的用户组
	 */
	protected Usergroups usergroup;

	/**
	 * 当前用户的管理组ID
	 */
	protected int useradminid = 0;

	/**
	 * 当前用户组名称
	 */
	protected String grouptitle;

	/**
	 * 当前IP地址
	 */
	protected String ip;

	protected StringBuilder htmlBuilder = new StringBuilder();

	private final int maxShortcutMenuCount = 15; //快捷菜单最大收藏数

	protected String footer = "<div align=\"center\" style=\" padding-top:60px;font-size:11px; font-family: Arial;\">Powered by <a style=\"COLOR: #000000\" href=\"http://lonlysk.javaeye.com\" target=\"_blank\">LForum For Java</a> &nbsp;&copy; 2001-"
			+ Utils.dateFormat(new Date(), "yyyy")
			+ ", <a style=\"COLOR: #000000;font-weight:bold\" href=\"http://lonlysk.javaeye.com\" target=\"_blank\">Lonlysky Blog.</a></div>";

	@Autowired
	protected OnlineUserManager onlineUserManager;

	@Autowired
	protected GroupManager groupManager;

	@Autowired
	protected UserManager userManager;

	@Autowired
	protected AdminVistLogManager adminVistLogManager;

	/*
	 * (non-Javadoc)
	 * @see com.javaeye.lonlysky.lforum.interceptor.ActionInitAware#initAction()
	 */
	public String initAction() throws Exception {

		registerAdminPageClientScriptBlock();

		// 如果IP访问列表有设置则进行判断
		if (!config.getAdminipaccess().trim().equals("")) {
			String[] regctrl = config.getAdminipaccess().trim().split("\n");
			if (!Utils.inIPArray(LForumRequest.getIp(), regctrl)) {
				response.sendRedirect(config.getWeburl() + "syslogin.action");
				return null;
			}
		}

		// 获取用户信息
		Online oluserinfo = onlineUserManager.updateInfo(config.getPasswordkey(), config.getOnlinetimeout());
		usergroup = groupManager.adminGetUserGroupInfo(oluserinfo.getUsergroups().getGroupid());
		if (oluserinfo.getUsers().getUid() <= 0 || usergroup.getAdmingroups().getAdmingid() != 1) {
			response.sendRedirect(config.getWeburl() + "syslogin.action");
			return null;
		}

		String secques = userManager.getUserInfo(oluserinfo.getUsers().getUid()).getSecques();

		// 管理员身份验证
		// 管理员身份验证
		if (ForumUtils.getCookie("lforumkey").equals("")
				|| !ForumUtils.getCookiePassword(ForumUtils.getCookie("lforumkey"), config.getPasswordkey()).equals(
						(oluserinfo.getPassword() + secques + oluserinfo.getUsers().getUid()))) {
			response.sendRedirect(config.getWeburl() + "syslogin.action");
			return null;
		} else {
			ForumUtils.writeCookie("lforumkey", ForumUtils.setCookiePassword(oluserinfo.getPassword() + secques
					+ oluserinfo.getUsers().getUid(), config.getPasswordkey()), Utils.null2Int(DateUtils.addMinutes(
					new Date(), 30).getTime()));
		}

		user = userManager.getUserInfo(oluserinfo.getUsers().getUid());
		username = oluserinfo.getUsername();
		useradminid = usergroup.getAdmingroups().getAdmingid();
		grouptitle = usergroup.getGrouptitle();
		ip = LForumRequest.getIp();

		String headerStr = "<script type=\"text/javascript\" src=\"../js/ajaxhelper.js\"></script><script type='text/javascript'>\nfunction ResetShortcutMenu(){window.parent.LoadShortcutMenu();}\nfunction FavoriteFunction(url){\nAjaxHelper.Updater('../UserControls/favoritefunction','resultmessage','url='+url,ResetShortcutMenu);\n}\n</script>\n";
		headerStr += "<div align='right' style=''>";
		//获取当前页面在收藏夹中的状态
		FavoriteStatus status = getFavoriteStatus();
		//根据当前页面收藏夹状态生成收藏快捷操作的链接
		if (status != FavoriteStatus.Hidden) {
			if (status == FavoriteStatus.Exist) {
				headerStr += headerStr += "<span id='resultmessage' title='已经将该页面加入到快捷操作菜单中'><img src='../images/existmenu.gif' style='vertical-align:middle' /> 已经收藏</span>";
			} else if (status == FavoriteStatus.Full) {
				headerStr += headerStr += "<span id='resultmessage' title='快捷操作菜单最大收藏数为" + maxShortcutMenuCount
						+ "项'><img src='../images/fullmenu.gif' style='vertical-align:middle' /> 收藏已满</span>\n</b>";
			} else if (status == FavoriteStatus.Show) {
				headerStr += "<span align='right' id='resultmessage'>\n<a href='javascript:;' title='将该页面加入快捷操作菜单' onclick='FavoriteFunction(window.location.pathname.toLowerCase().replace(\"/admin/\",\"\") + window.location.search.toLowerCase());' style='text-decoration:none;color:#333;' onfocus=\"this.blur();\"><img src='../images/addmenu.gif' align='absmiddle' /> 加入收藏</a>\n</span>";
			}
		}
		headerStr += "<span><a href='#' onclick='window.parent.showNavigation()' title='按ESC键或点击链接显示导航菜单' style='text-decoration:none;color:#333;'><img src='../images/navigation.gif' style='vertical-align:middle'> 管理导航</a></span></div>";
		headerStr += "<input type='hidden' name='submitMethod' value=''/>";
		headerStr += "<script type=\"text/javascript\">function doPost(method){";
		headerStr += "if(method!=''){document.getElementById('submitMethod').value='method:'+method;}";
		headerStr += "var theForm = document.forms['Form1'];";
		headerStr += "if (!theForm) {theForm = document.Form1;}";
		headerStr += "if (!theForm.onsubmit || (theForm.onsubmit() != false)) {theForm.submit();}}</script>";
		htmlBuilder.append(headerStr);
		return GlobalsKeys.ACTION_INIT;
	}

	/**
	 * 获取收藏夹的状态
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private FavoriteStatus getFavoriteStatus() {
		String configPath = config.getWebpath() + "admin/xml/navmenu.xml";
		String url = LForumRequest.getUrl().toLowerCase();
		String pagename = url.substring(url.lastIndexOf('/') + 1);
		Document doc = XmlElementUtil.readXML(configPath);
		List<Element> submains = doc.selectNodes("/dataset/submain");
		boolean findmenu = false;
		for (Element submain : submains) {
			if (submain.selectSingleNode("link").getText().indexOf('/') == -1)
				continue;
			if (submain.selectSingleNode("link").getText().split("/")[1].toLowerCase().equals(pagename))
				findmenu = true;
		}
		//当前链接不在菜单文件中，则不允许显示
		if (!findmenu)
			return FavoriteStatus.Hidden; //不允许收藏
		List<Element> shortcuts = doc.selectNodes("/dataset/shortcut");
		for (Element singleshortcut : shortcuts) {
			if (singleshortcut.selectSingleNode("link").getText().indexOf(pagename) != -1)
				return FavoriteStatus.Exist; //在收藏夹中已存在
		}
		if (shortcuts.size() >= maxShortcutMenuCount)
			return FavoriteStatus.Full; //快捷菜单收藏最多收藏15条
		return FavoriteStatus.Show; //正常收藏
	}

	/**
	 * 注册提示信息JS脚本
	 */
	public void registerAdminPageClientScriptBlock() {
		String script = "<div id=\"success\" style=\"position:absolute;z-index:300;height:120px;width:284px;left:50%;top:50%;margin-left:-150px;margin-top:-80px;\">\r\n"
				+ "	<div id=\"Layer2\" style=\"position:absolute;z-index:300;width:270px;height:90px;background-color: #FFFFFF;border:solid #000000 1px;font-size:14px;\">\r\n"
				+ "		<div id=\"Layer4\" style=\"height:26px;background:#f1f1f1;line-height:26px;padding:0px 3px 0px 3px;font-weight:bolder;\">操作提示</div>\r\n"
				+ "		<div id=\"Layer5\" style=\"height:64px;line-height:150%;padding:0px 3px 0px 3px;\" align=\"center\"><BR /><table><tr><td valign=top><img border=\"0\" src=\"../images/ajax_loading.gif\"  /></td><td valign=middle style=\"font-size: 14px;\" >正在执行当前操作, 请稍等...<BR /></td></tr></table><BR /></div>\r\n"
				+ "	</div>\r\n"
				+ "	<div id=\"Layer3\" style=\"position:absolute;width:270px;height:90px;z-index:299;left:4px;top:5px;background-color: #E8E8E8;\"></div>\r\n"
				+ "</div>\r\n"
				+ "<script> \r\n"
				+ "document.getElementById('success').style.display = \"none\"; \r\n"
				+ "</script> \r\n" + "<script type=\"text/javascript\" src=\"../js/divcover.js\"></script>\r\n";
		htmlBuilder.append(script);
	}

	public void registerStartupScript(String key, String scriptstr) {
		key = key.toLowerCase();
		if ((key.equals("pagetemplate")) || (key.equals("page"))) {
			String script = "";

			if (key.equals("page")) {
				script = "<script> \r\n" + "var bar=0;\r\n"
						+ "document.getElementById('success').style.display = \"block\";  \r\n"
						+ "document.getElementById('Layer5').innerHTML ='<BR>操作成功执行<BR>';  \r\n" + "count() ; \r\n"
						+ "function count(){ \r\n" + "bar=bar+4; \r\n" + "if (bar<99) \r\n"
						+ "{setTimeout(\"count()\",100);} \r\n" + "else { \r\n"
						+ "document.getElementById('success').style.display = \"none\";HideOverSels('success'); \r\n"
						+ scriptstr + "} \r\n" + "} \r\n" + "</script> \r\n"
						+ "<script> window.onload = function(){HideOverSels('success')};</script>\r\n";
			}

			if (key.equals("pagetemplate")) {
				script = "<script> \r\n" + "var bar=0;\r\n success.style.display = \"block\";  \r\n"
						+ "document.getElementById('Layer5').innerHTML = '<BR>" + scriptstr + "<BR>';  \r\n"
						+ "count() ; \r\n" + "function count(){ \r\n" + "bar=bar+4; \r\n" + "if (bar<99) \r\n"
						+ "{setTimeout(\"count()\",100);} \r\n" + "else { \r\n"
						+ "document.getElementById('success').style.display = \"none\";HideOverSels('success'); \r\n"
						+ "}} \r\n" + "</script> \r\n"
						+ "<script> window.onload = function(){HideOverSels('success')};</script>\r\n";
			}
			htmlBuilder.append(script);

		} else {
			htmlBuilder.append(scriptstr);
		}
	}

	/**
	 * 论坛提示信息
	 * @return
	 */
	protected String getShowMessage() {
		String message = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">";
		message += "<html xmlns=\"http://www.w3.org/1999/xhtml\"><head><title>您没有权限运行当前程序!</title><meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\">";
		message += "<link href=\"../styles/default.css\" type=\"text/css\" rel=\"stylesheet\"></head><body><br><br><div style=\"width:100%\" align=\"center\">";
		message += "<div align=\"center\" style=\"width:660px; border:1px dotted #FF6600; background-color:#FFFCEC; margin:auto; padding:20px;\"><img src=\"../images/hint.gif\" border=\"0\" alt=\"提示:\" align=\"absmiddle\" width=\"11\" height=\"13\" /> &nbsp;";
		message += "您没有权限运行当前程序,请您以论坛创始人身份登陆后台进行操作!</div></div></body></html>";
		return message;
	}

	public void loadRegisterStartupScript(String scriptstr) {
		String message = "程序执行中... <BR /> 当前操作可能要运行一段时间.<BR />您可在此期间进行其它操作<BR /><BR />";

		String script = "<script> \r\n" + "var bar=0;\r\n success.style.display = \"block\";  \r\n"
				+ "document.getElementById('Layer5').innerHTML ='" + message + "';  \r\n" + "count() ; \r\n"
				+ "function count(){ \r\n" + "bar=bar+2; \r\n" + "if (bar<99) \r\n"
				+ "{setTimeout(\"count()\",100);} \r\n" + "else { \r\n"
				+ "	document.getElementById('success').style.display = \"none\";HideOverSels('success'); \r\n"
				+ scriptstr + "} \r\n" + "} \r\n" + "</script> \r\n"
				+ "<script> window.onload = function(){HideOverSels('success')};</script>\r\n";

		htmlBuilder.append(script);
	}

	private void registerMessage(String scriptstr, boolean autoHidd, String autoJumpUrl) {
		String script = "<script type='text/javascript'>\r\n" + "var bar=0;\r\n"
				+ "document.getElementById('success').style.display = \"block\";\r\n"
				+ "document.getElementById('Layer5').innerHTML = '<BR>" + scriptstr + "<BR>';\r\n";
		if (autoHidd) {
			script += "count();\r\n" + "function count()\r\n" + "{\r\n" + "\tbar=bar+4;\r\n" + "\tif (bar<99)\r\n"
					+ "\t{\r\n" + "\t\tsetTimeout(\"count()\",200);\r\n" + "\t}\r\n" + "\telse\r\n" + "\t{\r\n";
			if (autoJumpUrl == "") {
				script += "\t\tdocument.getElementById('success').style.display = \"none\";HideOverSels('success');\r\n";
			} else {
				script += "\t\twindow.location='" + autoJumpUrl + "';\r\n";
			}
			script += "\t}\r\n" + "}\r\n";
		}
		script += "</script>\r\n" + "<script> window.onload = function(){HideOverSels('success')};</script>\r\n";
		htmlBuilder.append(script);
	}

	protected void registerMessage(String scriptstr, String autoJumpUrl) {
		registerMessage(scriptstr, true, autoJumpUrl);
	}

	protected void registerMessage(String scriptstr, boolean autoHidd) {
		registerMessage(scriptstr, autoHidd, "");
	}

	protected void registerMessage(String scriptstr) {
		registerMessage(scriptstr, false);
	}

	public String getUsername() {
		return username;
	}

	public Users getUser() {
		return user;
	}

	public Usergroups getUsergroup() {
		return usergroup;
	}

	public int getUseradminid() {
		return useradminid;
	}

	public String getGrouptitle() {
		return grouptitle;
	}

	public String getIp() {
		return ip;
	}

	public int getMaxShortcutMenuCount() {
		return maxShortcutMenuCount;
	}

	public String getFooter() {
		return footer;
	}

	public StringBuilder getHtmlBuilder() {
		return htmlBuilder;
	}

	/**
	 * 收藏夹的状态，Show正常显示，Hidden不允许收藏，Full收藏夹已满，Exist收藏项已经存在
	 *
	 */
	private enum FavoriteStatus {
		Show, Hidden, Full, Exist
	}
}
