package com.javaeye.lonlysky.lforum.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.struts2.config.ParentPackage;
import org.springframework.beans.factory.annotation.Autowired;

import com.javaeye.lonlysky.lforum.ForumBaseAction;
import com.javaeye.lonlysky.lforum.comm.ForumAction;
import com.javaeye.lonlysky.lforum.comm.LForumRequest;
import com.javaeye.lonlysky.lforum.comm.MD5;
import com.javaeye.lonlysky.lforum.comm.utils.ForumUtils;
import com.javaeye.lonlysky.lforum.comm.utils.HQLUitls;
import com.javaeye.lonlysky.lforum.comm.utils.Utils;
import com.javaeye.lonlysky.lforum.entity.forum.Admingroups;
import com.javaeye.lonlysky.lforum.entity.forum.Announcements;
import com.javaeye.lonlysky.lforum.entity.forum.Forumfields;
import com.javaeye.lonlysky.lforum.entity.forum.Forums;
import com.javaeye.lonlysky.lforum.entity.forum.IndexPageForumInfo;
import com.javaeye.lonlysky.lforum.entity.forum.Online;
import com.javaeye.lonlysky.lforum.entity.forum.Pms;
import com.javaeye.lonlysky.lforum.entity.forum.ShowforumPageTopicInfo;
import com.javaeye.lonlysky.lforum.entity.forum.Users;
import com.javaeye.lonlysky.lforum.service.AdminGroupManager;
import com.javaeye.lonlysky.lforum.service.AnnouncementManager;
import com.javaeye.lonlysky.lforum.service.ForumManager;
import com.javaeye.lonlysky.lforum.service.MessageManager;
import com.javaeye.lonlysky.lforum.service.TopicManager;

/**
 * 查看新帖、精华帖
 * 
 * @author 黄磊
 *
 */
@ParentPackage("default")
public class ShowtopiclistAction extends ForumBaseAction {

	private static final long serialVersionUID = -5167787382509257714L;

	/**
	 * 在线用户列表
	 */
	private List<Online> onlineuserlist;

	/**
	 * 在线用户图例
	 */
	private String onlineiconlist;

	/**
	 * 主题列表
	 */
	private List<ShowforumPageTopicInfo> topiclist;

	/**
	 * 子版块列表
	 */
	private List<IndexPageForumInfo> subforumlist;

	/**
	 * 短消息列表
	 */
	private List<Pms> pmlist;

	/**
	 * 公告列表
	 */
	private List<Announcements> announcementlist;

	/**
	 * 版块信息
	 */
	private Forums forum;

	/**
	 * 当前用户管理组信息
	 */
	private Admingroups admingroupinfo;

	/**
	 * 论坛在线总数
	 */
	private int forumtotalonline;

	/**
	 * 论坛在线注册用户总数
	 */
	private int forumtotalonlineuser;

	/**
	 * 论坛在线游客数
	 */
	private int forumtotalonlineguest;

	/**
	 * 论坛在线隐身用户数
	 */
	private int forumtotalonlineinvisibleuser;

	/**
	 * 版块Id
	 */
	private int forumid;

	/**
	 * 版块名称
	 */
	private String forumname = "";

	/**
	 * 子版块数
	 */
	private int subforumcount;

	/**
	 * 论坛导航信息
	 */
	private String forumnav = "";

	/**
	 * 是否显示版块密码提示 1为显示, 0不显示
	 */
	private int showforumlogin;

	/**
	 * 当前页码
	 */
	private int pageid;

	/**
	 * 主题总数
	 */
	private int topiccount = 0;

	/**
	 * 分页总数
	 */
	private int pagecount = 0;

	/**
	 * 分页页码链接
	 */
	private String pagenumbers = "";

	/**
	 * 置顶主题数
	 */
	private int toptopiccount = 0;

	/**
	 * 版块跳转链接选项
	 */
	private String forumlistboxoptions;

	/**
	 * 最近访问的版块选项
	 */
	private String visitedforumsoptions;

	/**
	 * 是否允许Rss订阅
	 */
	private int forumallowrss;

	/**
	 * 是否显示在线用户列表
	 */
	private boolean showforumonline;

	/**
	 * 排序方式
	 */
	private int order = 2; //排序字段

	private int direct = 1; //排序方向[默认：降序]

	/**
	 * 查看方式,digest=精华帖,其他值=新帖
	 */
	private String type = "";

	/**
	 * 新帖时限
	 */
	private int newtopic = 120;

	/**
	 * 用户选择的版块
	 */
	private String forums = "";

	/**
	 * 论坛选择多选框列表
	 */
	private String forumcheckboxlist = "";

	//后台指定的最大查询贴数
	private int maxseachnumber = 10000;
	private String condition = ""; //查询条件

	@Autowired
	private AdminGroupManager adminGroupManager;

	@Autowired
	private ForumManager forumManager;

	@Autowired
	private MessageManager messageManager;

	@Autowired
	private TopicManager topicManager;

	@Autowired
	private AnnouncementManager announcementManager;

	@Override
	public String execute() throws Exception {
		pagetitle = "查看新帖";
		forumallowrss = 0;
		forumid = LForumRequest.getParamIntValue("forumid", -1);
		forum = new Forums();
		admingroupinfo = new Admingroups();
		if (userid > 0 && useradminid > 0) {
			admingroupinfo = adminGroupManager.getAdminGroup(useradminid);
		}

		if (config.getRssstatus() == 1) {
			reqcfg.addLinkRss("tools/rss.action", "最新主题");
		}

		//当所选论坛为多个时或全部时

		if (forumid == -1) {
			//用户点选相应的论坛
			if (!(LForumRequest.getParamValues("fidlist", ",").trim().equals(""))) {
				forums = LForumRequest.getParamValues("fidlist", ",");
			} else {
				forums = LForumRequest.getParamValue("forums");
			}
			//获得已选取的论坛列表
			forumcheckboxlist = getForumCheckBoxListCache(forums);

			//获得有权限的fid
			//如果是选择全部版块
			if (forums.toLowerCase().equals("all") || forums.equals("")) {
				//取得所有列表
				forums = "";//先清空
				List<Forums> objForumInfoList = forumManager.getForumList();
				for (Forums objForumInfo : objForumInfoList) {
					forums += "," + objForumInfo.getFid();
				}
				forums = getAllowviewForums(forums.trim().substring(forums.indexOf(",") + 1));
				System.out.println(forums);
			} else { //如果是选择指定版块
				forums = getAllowviewForums(forums);
			}
		}

		// 对搜索条件进行检索
		String orderStr = "";

		if (!LForumRequest.getParamValue("search").trim().equals("")) { //进行指定查询
			//排序的字段
			order = LForumRequest.getParamIntValue("order", 2);
			switch (order) {
			case 1:
				orderStr = "postid.pid";
				break;
			case 2:
				orderStr = "tid";
				break;

			default:
				orderStr = "tid";
				break;
			}

			direct = LForumRequest.getParamIntValue("direct", 1);
		}

		newtopic = LForumRequest.getParamIntValue("newtopic", 120);
		Map<Integer, String> map = new HashMap<Integer, String>();
		condition = HQLUitls.getTopicCountCondition(map, LForumRequest.getParamValue("type"), newtopic);
		type = map.get(0);
		if (type.equals("digest")) {
			pagetitle = "查看精华";
		}
		if (!forums.equals("")) {
			//验证重新生成的版块id列表是否合法(需要放入HQL语句查询)                
			if (!Utils.isIntArray(forums.split(","))) {
				reqcfg.addErrLine("错误的Url");
				return SUCCESS;
			}
			condition += " and forums.fid in(" + forums + ")";
		} else if (forumid > 0) {
			condition += " and forums.fid =" + forumid;
		} else { //无可访问的版块fid存留
			reqcfg.addErrLine("没有可访问的版块,或者Url参数错误!<br >如果是需要登录的版块,请登录后再访问.");
			return SUCCESS;
		}

		if (forumid > 0) {
			forum = forumManager.getForumInfo(forumid);
			forumname = forum.getName();
			pagetitle = Utils.cleanHtmlTag(forum.getName());
			subforumcount = forum.getSubforumcount();
			forumnav = forum.getPathlist().trim();

			// 是否显示版块密码提示 1为显示, 0不显示
			showforumlogin = 1;
			// 如果版块未设密码
			if (forum.getForumfields().getPassword().trim().equals("")) {
				showforumlogin = 0;
			} else {
				// 如果检测到相应的cookie正确
				if (MD5.encode(forum.getForumfields().getPassword().trim()).equals(
						ForumUtils.getCookie("forum" + forumid + "password"))) {
					showforumlogin = 0;
				} else {
					// 如果用户提交的密码正确则保存cookie
					if (forum.getForumfields().getPassword().trim()
							.equals(LForumRequest.getParamValue("forumpassword"))) {
						ForumUtils.writeCookie("forum" + forumid + "password", MD5.encode(forum.getForumfields()
								.getPassword().trim()));
						showforumlogin = 0;
					}
				}
			}

			if (!forumManager.allowView(forum.getForumfields().getViewperm().trim(), usergroupid)) {
				reqcfg.addErrLine("您没有浏览该版块的权限");
				return SUCCESS;
			}
			// 得到子版块列表
			subforumlist = forumManager.getSubForumList(forumid, forum.getColcount(), config.getHideprivate(),
					usergroupid, config.getModdisplay());
		} else {
			forum.setAutoclose(0);
			Forumfields forumfields = new Forumfields();
			forumfields.setTopictypeprefix(0);
			forum.setForumfields(forumfields);
			forum.setAllowrss(config.getRssstatus());
			forum.setFid(0);
		}

		if (newpmcount > 0) {
			pmlist = messageManager.getPrivateMessageListForIndex(userid, 5, 1, 1);
		}

		// 得到公告
		announcementlist = announcementManager.getAnnouncementList(nowdatetime, "2999-01-01 00:00:00");

		//得到当前用户请求的页数
		pageid = LForumRequest.getParamIntValue("page", 1);
		//获取主题总数
		topiccount = topicManager.getTopicCount(condition);

		//防止查询数超过系统规定的最大值
		topiccount = maxseachnumber > topiccount ? topiccount : maxseachnumber;

		// 得到Tpp设置
		int tpp = Utils.null2Int(ForumUtils.getCookie("tpp"), config.getTpp());
		if (tpp <= 0) {
			tpp = config.getTpp();
		}
		//得到用户设置的每页显示主题数
		if (userid != -1) {
			Users userinfo = userManager.getUserInfo(userid);
			if (userinfo != null) {
				if (userinfo.getTpp() > 0) {
					tpp = userinfo.getTpp();
				}
				if (userinfo.getNewpm() == 0) {
					newpmcount = 0;
				}
			}
		}

		//获取总页数
		pagecount = topiccount % tpp == 0 ? topiccount / tpp : topiccount / tpp + 1;
		if (pagecount == 0) {
			pagecount = 1;
		}
		//修正请求页数中可能的错误
		if (pageid < 1) {
			pageid = 1;
		}
		if (pageid > pagecount) {
			pageid = pagecount;
		}
		//如果当前页面的返回结果超过系统规定的的范围时，则进行相应删剪
		if ((pageid * tpp) > topiccount) {
			tpp = tpp - (pageid * tpp - topiccount);
		}

		if (orderStr.equals("")) {
			topiclist = topicManager.getTopicListByType(tpp, pageid, 0, 10, config.getHottopic(), forum.getAutoclose(),
					forum.getForumfields().getTopictypeprefix(), condition, direct);
		} else {
			topiclist = topicManager.getTopicListByTypeDate(tpp, pageid, 0, 10, config.getHottopic(), forum
					.getAutoclose(), forum.getForumfields().getTopictypeprefix(), condition, orderStr, direct);
		}

		//得到页码链接
		if ("".equals(LForumRequest.getParamValue("search"))) {
			pagenumbers = ForumUtils.getPageNumbers(pageid, pagecount, "showtopiclist.action?type=" + type
					+ "&newtopic=" + newtopic + "&forumid=" + forumid + "&forums=" + forums, 8);
		} else {
			pagenumbers = ForumUtils.getPageNumbers(pageid, pagecount, "showtopiclist.action?search=1&type=" + type
					+ "&newtopic=" + newtopic + "&order=" + LForumRequest.getParamValue("order") + "&direct="
					+ LForumRequest.getParamValue("direct") + "&forumid=" + forumid + "&forums=" + forums, 8);
		}

		forumlistboxoptions = cachesManager.getForumListBoxOptionsCache();
		onlineUserManager.updateAction(olid, ForumAction.ShowForum.ACTION_ID, forumid, config.getOnlinetimeout());

		showforumonline = false;
		if (forumtotalonline < 300 || !LForumRequest.getParamValue("showonline").equals("")) {
			showforumonline = true;
		}

		ForumUtils.updateVisitedForumsOptions(forumid);
		visitedforumsoptions = forumManager.getVisitedForumsOptions(config.getVisitedforums());
		forumallowrss = forum.getAllowrss();
		return SUCCESS;
	}

	/**
	 * 取得当前用户有权访问的版块列表
	 * @param forums 原始版块列表(用逗号分隔的fid)
	 * @return 有权访问的版块列表(用逗号分隔的fid)
	 */
	private String getAllowviewForums(String forums) {
		//验证版块id列表是否合法的数字列表                
		if (!Utils.isIntArray(forums.split(","))) {
			return "";
		}
		String allowviewforums = "";
		String[] fidlist = forums.split(",");

		for (String strfid : fidlist) {
			int fid = Utils.null2Int(strfid, 0);
			if (forumManager.allowView(forumManager.getForumInfo(fid).getForumfields().getViewperm(), usergroupid)) {
				if (forumManager.getForumInfo(fid).getForumfields().getPassword().trim().equals("")
						|| MD5.encode(forumManager.getForumInfo(fid).getForumfields().getPassword().trim()).equals(
								ForumUtils.getCookie("forum" + strfid + "password"))) {
					allowviewforums += "," + fid;
				}
			}
		}
		System.out.println(allowviewforums);
		if (allowviewforums.trim().substring(0, 1).equals(",")) {
			allowviewforums = allowviewforums.trim().substring(allowviewforums.indexOf(",") + 1);
		}
		return allowviewforums.trim();
	}

	/**
	 * 获得已选取的论坛列表
	 * @param forums
	 * @return
	 */
	private String getForumCheckBoxListCache(String forums) {
		StringBuilder sb = new StringBuilder();
		forums = "," + forums + ",";
		List<Forums> forumList = forumManager.getForumList();
		int count = 1;
		for (Forums forum : forumList) {
			if (forums.toLowerCase().equals(",all,")) {
				sb
						.append("<td><input id=\"fidlist\" onclick=\"javascript:SH_SelectOne(this)\" type=\"checkbox\" value=\""
								+ forum.getFid()
								+ "\"	name=\"fidlist\"  checked/> "
								+ forum.getName().trim()
								+ "</td>\r\n");
			} else {
				if (forums.indexOf("," + forum.getFid() + ",") >= 0) {
					sb
							.append("<td><input id=\"fidlist\" onclick=\"javascript:SH_SelectOne(this)\" type=\"checkbox\" value=\""
									+ forum.getFid()
									+ "\"	name=\"fidlist\"  checked/>  "
									+ forum.getName().trim()
									+ "</td>\r\n");
				} else {
					sb
							.append("<td><input id=\"fidlist\" onclick=\"javascript:SH_SelectOne(this)\" type=\"checkbox\" value=\""
									+ forum.getFid()
									+ "\"	name=\"fidlist\"  /> "
									+ forum.getName().trim()
									+ "</td>\r\n");
				}
			}

			if (count > 3) {
				sb.append("			  </tr>\r\n");
				sb.append("			  <tr>\r\n");
				count = 0;
			}
			count++;
		}
		return sb.toString();
	}

	public List<Online> getOnlineuserlist() {
		return onlineuserlist;
	}

	public String getOnlineiconlist() {
		return onlineiconlist;
	}

	public List<ShowforumPageTopicInfo> getTopiclist() {
		return topiclist;
	}

	public List<IndexPageForumInfo> getSubforumlist() {
		return subforumlist;
	}

	public List<Pms> getPmlist() {
		return pmlist;
	}

	public List<Announcements> getAnnouncementlist() {
		return announcementlist;
	}

	public Forums getForum() {
		return forum;
	}

	public Admingroups getAdmingroupinfo() {
		return admingroupinfo;
	}

	public int getForumtotalonline() {
		return forumtotalonline;
	}

	public int getForumtotalonlineuser() {
		return forumtotalonlineuser;
	}

	public int getForumtotalonlineguest() {
		return forumtotalonlineguest;
	}

	public int getForumtotalonlineinvisibleuser() {
		return forumtotalonlineinvisibleuser;
	}

	public int getForumid() {
		return forumid;
	}

	public String getForumname() {
		return forumname;
	}

	public int getSubforumcount() {
		return subforumcount;
	}

	public String getForumnav() {
		return forumnav;
	}

	public int getShowforumlogin() {
		return showforumlogin;
	}

	public int getPageid() {
		return pageid;
	}

	public int getTopiccount() {
		return topiccount;
	}

	public int getPagecount() {
		return pagecount;
	}

	public String getPagenumbers() {
		return pagenumbers;
	}

	public int getToptopiccount() {
		return toptopiccount;
	}

	public String getForumlistboxoptions() {
		return forumlistboxoptions;
	}

	public String getVisitedforumsoptions() {
		return visitedforumsoptions;
	}

	public int getForumallowrss() {
		return forumallowrss;
	}

	public boolean isShowforumonline() {
		return showforumonline;
	}

	public int getOrder() {
		return order;
	}

	public int getDirect() {
		return direct;
	}

	public String getType() {
		return type;
	}

	public int getNewtopic() {
		return newtopic;
	}

	public String getForums() {
		return forums;
	}

	public String getForumcheckboxlist() {
		return forumcheckboxlist;
	}

	public int getMaxseachnumber() {
		return maxseachnumber;
	}

	public String getCondition() {
		return condition;
	}
}
