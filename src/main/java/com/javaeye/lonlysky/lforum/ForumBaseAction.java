package com.javaeye.lonlysky.lforum;

import java.text.ParseException;

import org.springframework.beans.factory.annotation.Autowired;

import com.javaeye.lonlysky.lforum.comm.LForumRequest;
import com.javaeye.lonlysky.lforum.comm.utils.ForumUtils;
import com.javaeye.lonlysky.lforum.comm.utils.Utils;
import com.javaeye.lonlysky.lforum.entity.forum.Online;
import com.javaeye.lonlysky.lforum.entity.forum.Usergroups;
import com.javaeye.lonlysky.lforum.interceptor.ActionInitAware;
import com.javaeye.lonlysky.lforum.service.CachesManager;
import com.javaeye.lonlysky.lforum.service.OnlineUserManager;
import com.javaeye.lonlysky.lforum.service.ScoresetManager;
import com.javaeye.lonlysky.lforum.service.TemplateManager;
import com.javaeye.lonlysky.lforum.service.UserGroupManager;
import com.javaeye.lonlysky.lforum.service.UserManager;

/**
 * <b>论坛Action基类</b><br>
 * 
 * @author 黄磊
 *
 */
public class ForumBaseAction extends BaseAction implements ActionInitAware {

	private static final long serialVersionUID = 8954710035971066384L;

	/**
	 * 当前用户用户组
	 */
	protected Usergroups usergroupinfo;

	/**
	 * 在线用户信息
	 */
	protected Online oluserinfo;

	/**
	 * 当前用户名
	 */
	protected String username;

	/**
	 * 论坛地址
	 */
	protected String forumurl;

	/**
	 * 当前用户密码
	 */
	protected String password;

	/**
	 * 当前用户特征串
	 */
	protected String userkey;

	/**
	 * 当前用户ID
	 */
	protected int userid;

	/**
	 * 当前用户在线表ID
	 */
	protected int olid;

	/**
	 * 当前用户组ID
	 */
	protected int usergroupid;

	/**
	 * 当前用户管理组ID
	 */
	protected int useradminid;

	/**
	 * 最后发帖时间
	 */
	protected String lastposttime;

	/**
	 * 最后发短信时间
	 */
	protected String lastpostpmtime;

	/**
	 * 最后搜索时间
	 */
	protected String lastsearchtime;

	/**
	 * 短信铃声编号 
	 */
	protected int pmsound;

	/**
	 * 当前用户的短消息数目
	 */
	protected int newpmcount = 0;

	/**
	 * 当前用户的短消息数目
	 */
	protected int realnewpmcount = 0;

	/**
	 * 当前页面标题
	 */
	protected String pagetitle = "页面";

	/**
	 * 模板id
	 */
	protected int templateid;

	/**
	 * 模板风格选择列表框选项
	 */
	protected String templatelistboxoptions;

	/**
	* 当前模板路径
	*/
	protected String templatepath;

	/**
	* 当前日期
	*/
	protected String nowdate;

	/**
	* 当前时间
	*/
	protected String nowtime;

	/**
	* 当前日期时间
	*/
	protected String nowdatetime;

	/**
	  * 多少分钟之前的帖子为新帖
	  */
	protected int newtopicminute = 120;

	/**
	* 当前在线人数
	*/
	protected int onlineusercount = 1;

	/**
	* 头部广告
	*/
	protected String headerad = "";
	/**
	* 底部广告
	*/
	protected String footerad = "";

	/**
	* 是否为需检测校验码页面
	*/
	protected boolean isseccode = true;

	/**
	 * 当前页面名称
	 */
	protected String pagename;

	/**
	 * 当前页面是否被POST请求
	 */
	protected boolean ispost;

	/**
	 * 当前页面是否被GET请求
	 */
	protected boolean isget;

	/**
	 * 用户头像
	 */
	public String useravatar = "";

	protected int issmileyinsert = 0;

	/**
	 * 在线用户业务处理
	 */
	@Autowired
	protected OnlineUserManager onlineUserManager;

	/**
	 * 用户组业务处理
	 */
	@Autowired
	protected UserGroupManager userGroupManager;

	/**
	 * 用户业务处理
	 */
	@Autowired
	protected UserManager userManager;

	/**
	 * 模板业务处理
	 */
	@Autowired
	protected TemplateManager templateManager;

	/**
	 * HTML缓存
	 */
	@Autowired
	protected CachesManager cachesManager;

	@Autowired
	private ScoresetManager scoresetManager;

	/**
	 * 检测用户是否可访问论坛
	 * @throws ParseException 
	 */
	private String validateUserPermission() throws ParseException {
		if (onlineusercount >= config.getMaxonlines() && useradminid != 1 && !pagename.equals("login.action")
				&& !pagename.equals("logout.action")) {
			return showMessage("抱歉,目前访问人数太多,你暂时无法访问论坛.", 0);
		}

		if (usergroupinfo.getAllowvisit() != 1 && useradminid != 1 && !pagename.equals("login.action")
				&& !pagename.equals("register.action") && !pagename.equals("logout.action")
				&& !pagename.equals("activationuser.action")) {
			return showMessage("抱歉, 您所在的用户组不允许访问论坛", 0);
		}

		// 检测IP访问白名单
		if (config.getIpaccess().trim().length() != 0) {
			String[] ips = config.getIpaccess().trim().split("\n");
			if (logger.isDebugEnabled()) {
				logger.debug("系统启用IP白名单：");
				for (String string : ips) {
					logger.debug(string);
				}
				logger.debug("用户IP：" + LForumRequest.getIp());
			}
			if (!Utils.inIPArray(LForumRequest.getIp(), ips)) {
				return showMessage("抱歉, 系统设置了IP访问列表限制, 您无法访问本论坛", 0);
			}
		}

		// 检测IP访问黑名单
		if (config.getIpdenyaccess().trim().length() != 0) {
			String[] ips = config.getIpdenyaccess().split("\n");
			if (logger.isDebugEnabled()) {
				logger.debug("系统启用IP黑名单：");
				for (String string : ips) {
					logger.debug(string);
				}
				logger.debug("用户IP：" + LForumRequest.getIp());
			}
			if (Utils.inIPArray(LForumRequest.getIp(), ips)) {
				return showMessage("由于您严重违反了论坛的相关规定, 已被禁止访问.", 2);
			}
		}
		//　如果当前用户请求页面不是登录页面并且当前用户非管理员并且论坛设定了时间段,当时间在其中的一个时间段内,则跳转到论坛登录页面
		if (useradminid != 1 && !pagename.equals("login.action") && !pagename.equals("logout.action")
				&& usergroupinfo.getDisableperiodctrl() != 1) {
			String timeString = scoresetManager.betweenTime(config.getVisitbanperiods());
			if (!timeString.equals("")) {
				return showMessage("在时间段 " + timeString + " 内不允许访问本论坛", 2);
			}
		}
		return "";
	}

	/**
	 * 效验验证码
	 * @return 是否通过
	 */
	private boolean validateVerifyCode() {
		if (LForumRequest.getParamValue("vcode").equals("")) {
			if (pagename.endsWith("ajax.action")) {
				if (LForumRequest.getParamValue("t").equals("quickreply")) {
					responseAjaxVcodeError();
				}
			} else {
				if (LForumRequest.getParamValue("loginsubmit").equals("true") && pagename.equals("login.action")) {
					//快速登录时不报错
				} else if (LForumRequest.getParamValue("agree").equals("true") && pagename.equals("register.action")) {
					//同意注册协议也不受此限制
				} else {
					reqcfg.addErrLine("验证码错误");
					return false;
				}
			}

		} else {
			if (!onlineUserManager.checkUserVerifyCode(olid, LForumRequest.getParamValue("vcode"))) {
				if (pagename.endsWith("ajax.action")) {
					responseAjaxVcodeError();
				} else {
					reqcfg.addErrLine("验证码错误");
					return false;
				}
			}
		}//end if
		return true;
	}

	/*
	 * (non-Javadoc)
	 * @see com.javaeye.lonlysky.lforum.interceptor.ActionInitAware#initAction()
	 */
	public String initAction() throws Exception {
		issmileyinsert = config.getSmileyinsert();

		// 添加Meta信息
		reqcfg.addMetaInfo(config.getSeokeywords(), config.getSeodescription(), config.getSeohead());

		if (config.getNocacheheaders() == 1) {
			response.setHeader("Pragma", "No-cache");
			response.setHeader("Cache-Control", "no-cache");
			response.setDateHeader("Expires", 0);
		}

		forumurl = config.getForumurl();
		pagename = LForumRequest.getPageName();
		// 获取cookie中的用户ID
		userid = Utils.null2Int(ForumUtils.getCookie("userid"));

		// 更新在线信息
		//当为forumlist或focuslist,可能出现在线并发问题,这时系统会延时2秒
		if (!pagename.equals("forumlist.action") && !pagename.equals("focuslist.action")) {
			oluserinfo = onlineUserManager.updateInfo(config.getPasswordkey(), config.getOnlinetimeout());
		} else {
			try {
				oluserinfo = onlineUserManager.updateInfo(config.getPasswordkey(), config.getOnlinetimeout());
			} catch (Exception e) {
				Thread.sleep(2000);
				oluserinfo = onlineUserManager.updateInfo(config.getPasswordkey(), config.getOnlinetimeout());
			}
		}

		userid = oluserinfo.getUsers().getUid();
		usergroupid = oluserinfo.getUsergroups().getGroupid();
		username = oluserinfo.getUsername();
		password = oluserinfo.getPassword();

		// 设置用户KEY
		if (password.length() > 16) {
			userkey = password.substring(4, 8);
		} else {
			userkey = "";
		}
		lastposttime = oluserinfo.getLastposttime();
		lastpostpmtime = oluserinfo.getLastpostpmtime();
		lastsearchtime = oluserinfo.getLastsearchtime();
		olid = oluserinfo.getOlid();

		// 确保头像可以取得
		if (userid > 0) {
			useravatar = Utils.urlDecode(ForumUtils.getCookie("avatar"));
			if (useravatar.equals("")) {
				useravatar = userManager.getUserInfo(userid).getUserfields().getAvatar();
				ForumUtils.writeCookie("avatar", Utils.urlEncode(useravatar));
			}
		}

		// 获取模板ID
		String templateids = templateManager.getValidTemplateIDList();
		//		if (Utils.inArray(LForumRequest.getParamValue("selectedtemplateid"), templateids)) {
		//			templateid = LForumRequest.getParamIntValue("selectedtemplateid", 0);
		//		} else 
		if (Utils.inArray(ForumUtils.getCookie("templateid"), templateids)) {
			templateid = Utils.null2Int(ForumUtils.getCookie("templateid"), config.getTemplateid());
		}

		if (templateid == 0) {
			templateid = config.getTemplateid();
		}

		// 设置短信铃声
		pmsound = Utils.null2Int(ForumUtils.getCookie("pmsound"), 0);

		// 获取用户组信息
		usergroupinfo = userGroupManager.getUsergroup(usergroupid);

		// 获取管理组ID
		useradminid = usergroupinfo.getAdmingroups().getAdmingid();

		// 如果论坛关闭且当前用户请求页面不是登录页面且用户非管理员, 则跳转至论坛关闭信息页
		if (config.getClosed() == 1 && !pagename.equals("login.action") && !pagename.equals("logout.action")
				&& useradminid != 1) {
			return showMessage("", 1);
		}

		// 获取在线总数
		if (config.getOnlinetimeout() > 0 && userid != -1) {
			onlineusercount = onlineUserManager.getOnlineAllUserCount();
		} else {
			onlineusercount = onlineUserManager.getCacheOnlineAllUserCount();
		}

		//校验用户是否可以访问论坛
		String result = validateUserPermission();
		if (!"".equals(result)) {
			return result;
		}

		if (userid != -1) {
			// 更新在线时间
			onlineUserManager.updateOnlineTime(config.getOltimespan(), userid);

			// 判断是否忽略短信提醒
			String ignore = LForumRequest.getParamValue("ignore");
			newpmcount = userManager.getUserNewPmCount(userid);
			realnewpmcount = Math.abs(newpmcount);
			if (ignore.toLowerCase().equals("yes")) {
				newpmcount = newpmcount * -1;
				userManager.setUserNewPMCount(userid, newpmcount);
			}
		}

		templatepath = templateManager.getTemplateItem(templateid).getDirectory();
		nowdate = Utils.getNowShortDate();
		nowtime = Utils.getNowShortTime();
		nowdatetime = Utils.getNowTime();

		newtopicminute = config.getViewnewtopicminute();

		ispost = LForumRequest.isPost();
		isget = LForumRequest.isGet();

		templatelistboxoptions = cachesManager.getTemplateListBoxOptionsCache();

		// 判断页面是否需要验证
		isseccode = Utils.inArray(pagename, config.getSeccodestatus());

		// 效验验证码
		if (isseccode && ispost) {
			if (!validateVerifyCode()) {
				return SUCCESS;
			}
		}

		// 获取广告信息
		headerad = "";
		footerad = "";
		return GlobalsKeys.ACTION_INIT;
	}

	/**
	 * 输出Ajax验证码错误信息
	 */
	private void responseAjaxVcodeError() {
		responseXML("<?xml version=\"1.0\" encoding=\"utf-8\"?>\r\n<error>验证码错误</error>", false);
	}

	/**
	 * 输出XML
	 * @param xml XML信息	
	 * @param hasCache 是否需要缓存
	 */
	protected void responseXML(String xml, boolean hasCache) {
		if (!hasCache) {
			response.setHeader("Pragma", "No-cache");
			response.setHeader("Cache-Control", "no-cache");
			response.setDateHeader("Expires", 0);
		}
		renderXML(xml);
	}

	public String getTemplatepath() {
		return templatepath;
	}

	public String getPagetitle() {
		return pagetitle;
	}

	public String getForumurl() {
		return forumurl;
	}

	public String getHeaderad() {
		return headerad;
	}

	public String getFooterad() {
		return footerad;
	}

	public Usergroups getUsergroupinfo() {
		return usergroupinfo;
	}

	public String getUsername() {
		return username;
	}

	public int getUserid() {
		return userid;
	}

	public int getUsergroupid() {
		return usergroupid;
	}

	public int getUseradminid() {
		return useradminid;
	}

	public String getTemplatelistboxoptions() {
		return templatelistboxoptions;
	}

	public String getUseravatar() {
		return useravatar;
	}

	public boolean isIspost() {
		return ispost;
	}

	public boolean isIsseccode() {
		return isseccode;
	}

	public Online getOluserinfo() {
		return oluserinfo;
	}

	public String getUserkey() {
		return userkey;
	}

	public int getOlid() {
		return olid;
	}

	public String getLastposttime() {
		return lastposttime;
	}

	public String getLastpostpmtime() {
		return lastpostpmtime;
	}

	public String getLastsearchtime() {
		return lastsearchtime;
	}

	public int getPmsound() {
		return pmsound;
	}

	public int getNewpmcount() {
		return newpmcount;
	}

	public int getRealnewpmcount() {
		return realnewpmcount;
	}

	public int getTemplateid() {
		return templateid;
	}

	public String getNowdate() {
		return nowdate;
	}

	public String getNowtime() {
		return nowtime;
	}

	public String getNowdatetime() {
		return nowdatetime;
	}

	public String getPagename() {
		return pagename;
	}

	public boolean isIsget() {
		return isget;
	}

	public int getNewtopicminute() {
		return newtopicminute;
	}

	public int getIssmileyinsert() {
		return issmileyinsert;
	}

}
