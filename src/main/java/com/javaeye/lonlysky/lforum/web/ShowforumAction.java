package com.javaeye.lonlysky.lforum.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.struts2.config.ParentPackage;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;

import com.javaeye.lonlysky.lforum.ForumBaseAction;
import com.javaeye.lonlysky.lforum.comm.ForumAction;
import com.javaeye.lonlysky.lforum.comm.LForumRequest;
import com.javaeye.lonlysky.lforum.comm.MD5;
import com.javaeye.lonlysky.lforum.comm.utils.ForumUtils;
import com.javaeye.lonlysky.lforum.comm.utils.HQLUitls;
import com.javaeye.lonlysky.lforum.comm.utils.Utils;
import com.javaeye.lonlysky.lforum.comm.utils.XmlElementUtil;
import com.javaeye.lonlysky.lforum.entity.forum.Admingroups;
import com.javaeye.lonlysky.lforum.entity.forum.Announcements;
import com.javaeye.lonlysky.lforum.entity.forum.Forums;
import com.javaeye.lonlysky.lforum.entity.forum.IndexPageForumInfo;
import com.javaeye.lonlysky.lforum.entity.forum.Online;
import com.javaeye.lonlysky.lforum.entity.forum.Pms;
import com.javaeye.lonlysky.lforum.entity.forum.ShowforumPageTopicInfo;
import com.javaeye.lonlysky.lforum.entity.forum.Smilies;
import com.javaeye.lonlysky.lforum.entity.forum.UserExtcreditsInfo;
import com.javaeye.lonlysky.lforum.service.AdminGroupManager;
import com.javaeye.lonlysky.lforum.service.AnnouncementManager;
import com.javaeye.lonlysky.lforum.service.ForumManager;
import com.javaeye.lonlysky.lforum.service.MessageManager;
import com.javaeye.lonlysky.lforum.service.ModeratorManager;
import com.javaeye.lonlysky.lforum.service.OnlineUserManager;
import com.javaeye.lonlysky.lforum.service.ScoresetManager;
import com.javaeye.lonlysky.lforum.service.TopicManager;

/**
 * 查看版块页面
 * 
 * @author 黄磊
 *
 */
@ParentPackage("default")
public class ShowforumAction extends ForumBaseAction {

	private static final long serialVersionUID = 4530060416188187942L;

	/**
	 * 当前版块在线用户列表
	 */
	private List<Online> onlineuserlist;

	/**
	 * 主题列表
	 */
	private List<ShowforumPageTopicInfo> topiclist;

	/**
	 * 置顶主题列表
	 */
	private List<ShowforumPageTopicInfo> toptopiclist = new ArrayList<ShowforumPageTopicInfo>();

	/**
	 * 子版块列表
	 */
	private List<IndexPageForumInfo> subforumlist;

	/**
	 * 短消息列表
	 */
	private List<Pms> pmlist;

	/**
	   * 在线图例列表
	   */
	private String onlineiconlist = "";

	/**
	 * 公告列表
	 */
	private List<Announcements> announcementlist = new ArrayList<Announcements>();

	/**
	 * 页内文字广告
	 */
	private String[] pagewordad;

	/**
	 * 对联广告
	 */
	private String doublead;

	/**
	 * 浮动广告
	 */
	private String floatad;

	/**
	 * 快速发帖广告
	 */
	private String quickeditorad;

	/**
	 * 快速编辑器背景广告
	 */
	private String quickbgadimg = "";
	private String quickbgadlink = "";

	/**
	 * 当前版块信息
	 */
	private Forums forum;

	/**
	 * 用户的管理组信息
	 */
	private Admingroups admingroupinfo;

	/**
	 * 积分策略
	 */
	private UserExtcreditsInfo userextcreditsinfo;

	/**
	 * 当前版块总在线用户数
	 */
	private int forumtotalonline;

	/**
	 * 当前版块总在线注册用户数
	 */
	private int forumtotalonlineuser;

	/**
	 * 当前版块总在线游客数
	 */
	private int forumtotalonlineguest;

	/**
	 * 当前版块在线隐身用户数
	 */
	private int forumtotalonlineinvisibleuser;

	/**
	 * 当前版块ID
	 */
	private int forumid;

	/**
	 * 当前版块名称
	 */
	private String forumname;

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
	private int pagecount = 1;

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
	 * 是否显示在线列表
	 */
	private boolean showforumonline;

	/**
	 * 是否显示分隔符
	 */
	private boolean showsplitter;

	/**
	 * 是否受发帖控制限制
	 */
	private int disablepostctrl;

	/**
	 * 是否解析URL
	 */
	private int parseurloff;

	/**
	 * 是否解析表情
	 */
	private int smileyoff;

	/**
	 * 是否解析 LForum 代码
	 */
	private int bbcodeoff;

	/**
	 * 是否使用签名
	 */
	private int usesig;

	/**
	 * 是否允许 [img] 标签
	 */
	private int allowimg;

	/**
	 * 表情Javascript数组
	 */
	private String smilies;

	/**
	 * 每页显示主题数
	 */
	private int tpp;

	/**
	 * 每页显示帖子数
	 */
	private int ppp;

	/**
	 * 是否是管理者
	 */
	private boolean ismoder = false;

	/**
	 * 主题分类选项
	 */
	private String topictypeselectoptions; //当前版块的主题类型选项

	/**
	 * 当前版块的主题类型链接串
	 */
	private String topictypeselectlink;

	/**
	 * 主题分类Id
	 */
	private int topictypeid = 0; //主题类型

	/**
	 * 过滤主题类型
	 */
	private String filter = "";

	/**
	 * 表情分类列表
	 */
	private List<Smilies> smilietypes;

	/**
	 * 是否允许发表主题
	 */
	private boolean canposttopic;

	/**
	 * 是否允许快速发表主题
	 */
	private boolean canquickpost;

	/**
	 * 论坛弹出导航菜单HTML代码
	 */
	private String navhomemenu;

	/**
	 * 是否显示短消息提示
	 */
	private boolean showpmhint;

	/**
	 * 是否显示需要登录后访问的错误提示
	 */
	private boolean needlogin;

	/**
	 * 排序方式
	 */
	private int order = 1; //排序字段
	/**
	 * 时间范围
	 */
	private int cond = 0;
	/**
	 * 排序方向
	 */
	private int direct = 1; //排序方向[默认：降序]

	/**
	 * 第一页表情的JSON
	 */
	private String firstpagesmilies;

	private String condition = ""; //查询条件

	@Autowired
	private AdminGroupManager adminGroupManager;

	@Autowired
	private ForumManager forumManager;

	@Autowired
	private ModeratorManager moderatorManager;

	@Autowired
	private ScoresetManager scoresetManager;

	@Autowired
	private TopicManager topicManager;

	@Autowired
	private MessageManager messageManager;

	@Autowired
	private AnnouncementManager announcementManager;

	@Override
	public String execute() throws Exception {

		forumnav = "";
		forumallowrss = 0;
		forumid = LForumRequest.getParamIntValue("forumid", -1);

		///得到广告列表
		headerad = "";
		footerad = "";

		pagewordad = new String[0];
		doublead = "";
		floatad = "";
		//快速发帖广告
		quickeditorad = "";

		//快速编辑器背景广告
		String[] quickbgad = new String[0];

		if (quickbgad.length > 1) {
			quickbgadimg = quickbgad[0];
			quickbgadlink = quickbgad[1];
		}

		disablepostctrl = 0;
		smilies = cachesManager.getSmiliesCache();
		smilietypes = cachesManager.getSmilieTypesCache();
		firstpagesmilies = cachesManager.getSmiliesFirstPageCache();

		if (userid > 0 && useradminid > 0) {
			admingroupinfo = adminGroupManager.getAdminGroup(useradminid);
		}

		if (admingroupinfo != null) {
			disablepostctrl = admingroupinfo.getDisablepostctrl();
		}

		if (forumid == -1) {
			reqcfg.addLinkRss("tools/rss.action", "最新主题").addErrLine("无效的版块ID");
			return SUCCESS;
		} else {
			forum = forumManager.getForumInfo(forumid);
			if (forum == null) {
				if (config.getRssstatus() == 1) {
					reqcfg.addLinkRss("tools/rss.action", Utils.cleanHtmlTag(config.getForumtitle()) + " 最新主题");
				}
				reqcfg.addErrLine("不存在的版块ID");
				return SUCCESS;
			}

			//当版块有外部链接时,则直接跳转
			if (!(Utils.null2String(forum.getForumfields().getRedirect()).equals(""))) {
				response.sendRedirect(forum.getForumfields().getRedirect());
			}
			if (useradminid > 0) {
				// 检查是否具有版主的身份
				ismoder = moderatorManager.isModer(useradminid, userid, forumid);
			}

			String orderStr = "";
			condition = "";

			topictypeid = LForumRequest.getParamIntValue("typeid", 0);
			if (topictypeid > 0) {
				condition = HQLUitls.showforumcondition(1, 0) + topictypeid;
			}

			filter = LForumRequest.getParamValue("filter");
			if (!filter.equals("")) {
				condition += HQLUitls.getTopicFilterCondition(filter);
			}

			if (!LForumRequest.getParamValue("search").equals("")) //进行指定查询
			{
				//多少时间以来的数据
				cond = LForumRequest.getParamIntValue("cond", -1);
				if (cond < 1) {
					cond = 0;
				} else {
					if (!(topictypeid > 0)) //当有主题分类时，则不加入下面的日期查询条件
					{
						condition = HQLUitls.showforumcondition(2, cond);
					}
				}

				//排序的字段
				order = LForumRequest.getParamIntValue("order", -1);
				switch (order) {
				case 2:
					orderStr = HQLUitls.showforumcondition(3, 0); //发布时间

					break;
				default:
					orderStr = "";
					break;
				}

				if (LForumRequest.getParamIntValue("direct", -1) == 0) {
					direct = 0;
				}
			}

			if (forum.getFid() < 1) {
				if (config.getRssstatus() == 1 && forum.getAllowrss() == 1) {
					reqcfg.addLinkRss("tools/rss.action?forumid=" + forumid, Utils.cleanHtmlTag(forum.getName())
							+ " 最新主题");
				}
				reqcfg.addErrLine("不存在的版块ID");
				return SUCCESS;
			}
			if (config.getRssstatus() == 1) {
				reqcfg.addLinkRss("tools/rss.action?forumid=" + forumid, Utils.cleanHtmlTag(forum.getName()) + " 最新主题");
			}
			forumname = forum.getName();
			pagetitle = Utils.cleanHtmlTag(forum.getName());
			subforumcount = forum.getSubforumcount();
			forumnav = forum.getPathlist();
			navhomemenu = cachesManager.getForumListMenuDiv(usergroupid, userid, "");

			//更新页面Meta中的Description项, 提高SEO友好性
			reqcfg.updateMetaInfo(config.getSeokeywords(), forum.getName(), config.getSeohead());

			if (forum.getForumfields().getApplytopictype() == 1) //启用主题分类
			{
				topictypeselectoptions = forumManager.getCurrentTopicTypesOption(forum.getFid(), forum.getForumfields()
						.getTopictypes());
			}

			if (forum.getForumfields().getViewbytopictype() == 1) //允许按类别浏览
			{
				topictypeselectlink = forumManager.getCurrentTopicTypesLink(forum.getFid(), forum.getForumfields()
						.getTopictypes(), "showforum.action");
			}

			//编辑器状态
			StringBuilder sb = new StringBuilder();
			sb.append("var Allowhtml=1;\r\n"); //+ allhtml.ToString() + "

			parseurloff = 0;

			smileyoff = 1 - forum.getAllowsmilies();
			sb.append("var Allowsmilies=" + (1 - smileyoff) + ";\r\n");

			bbcodeoff = 1;
			if (forum.getAllowbbcode() == 1) {
				if (usergroupinfo.getAllowcusbbcode() == 1) {
					bbcodeoff = 0;
				}
			}
			sb.append("var Allowbbcode=" + (1 - bbcodeoff) + ";\r\n");

			usesig = ForumUtils.getCookie("sigstatus") == "0" ? 0 : 1;

			allowimg = forum.getAllowimgcode();
			sb.append("var Allowimgcode=" + allowimg + ";\r\n");

			reqcfg.addScript(sb.toString());

			// 是否显示版块密码提示 1为显示, 0不显示
			showforumlogin = 1;
			// 如果版块未设密码
			if (forum.getForumfields().getPassword().equals("")) {
				showforumlogin = 0;
			} else {
				// 如果检测到相应的cookie正确
				if (MD5.encode(forum.getForumfields().getPassword()) == ForumUtils.getCookie("forum" + forumid
						+ "password")) {
					showforumlogin = 0;
				} else {
					// 如果用户提交的密码正确则保存cookie
					if (forum.getForumfields().getPassword().equals(LForumRequest.getParamValue("forumpassword"))) {
						ForumUtils.writeCookie("forum" + forumid + "password", MD5.encode(forum.getForumfields()
								.getPassword()));
						showforumlogin = 0;
					}
				}
			}

			if (!forumManager.allowViewByUserID(forum.getForumfields().getPermuserlist(), userid)) //判断当前用户在当前版块浏览权限
			{
				if (forum.getForumfields().getViewperm() == null || forum.getForumfields().getViewperm().equals("")) //当板块权限为空时，按照用户组权限
				{
					if (usergroupinfo.getAllowvisit() != 1) {
						reqcfg.addErrLine("您当前的身份 \"" + usergroupinfo.getGrouptitle() + "\" 没有浏览该版块的权限");
						if (userid == -1) {
							needlogin = true;
						}
						return SUCCESS;
					}
				} else //当板块权限不为空，按照板块权限
				{
					if (!forumManager.allowView(forum.getForumfields().getViewperm(), usergroupid)) {
						reqcfg.addErrLine("您没有浏览该版块的权限");
						if (userid == -1) {
							needlogin = true;
						}
						return SUCCESS;
					}
				}
			}

			//判断是否有发主题的权限
			if (userid > -1 && forumManager.allowViewByUserID(forum.getForumfields().getPermuserlist(), userid)) {
				canposttopic = true;
			}

			if (forum.getForumfields().getPostperm() == null || forum.getForumfields().getPostperm().equals("")) //权限设置为空时，根据用户组权限判断
			{
				// 验证用户是否有发表主题的权限
				if (usergroupinfo.getAllowpost() == 1) {
					canposttopic = true;
				}
			} else if (forumManager.allowPost(forum.getForumfields().getPostperm(), usergroupid)) {
				canposttopic = true;
			}

			//　如果当前用户非管理员并且论坛设定了禁止发帖时间段，当前时间如果在其中的一个时间段内，不允许用户发帖
			if (useradminid != 1 && usergroupinfo.getDisableperiodctrl() != 1) {
				String visittime = scoresetManager.betweenTime(config.getPostbanperiods());
				if (!visittime.equals("")) {
					canposttopic = false;
				}
			}

			//判断是否有快速发主题的权限
			if (canposttopic == true) {
				if (config.getFastpost() == 1 || config.getFastpost() == 3) {
					canquickpost = true;
				}
			}

			userextcreditsinfo = scoresetManager.getScoreSet(scoresetManager.getCreditsTrans());

			if (newpmcount > 0) {
				pmlist = messageManager.getPrivateMessageListForIndex(userid, 5, 1, 1);
				showpmhint = userManager.getUserInfo(userid).getNewsletter() > 4;
			}

			// 得到子版块列表
			if (subforumcount > 0) {
				subforumlist = forumManager.getSubForumList(forumid, forum.getColcount(), config.getHideprivate(),
						usergroupid, config.getModdisplay());
			}

			//得到当前用户请求的页数
			pageid = LForumRequest.getParamIntValue("page", 1);

			//获取主题总数
			topiccount = topicManager.getTopicCount(forumid, condition, true);

			// 得到Tpp设置
			tpp = Utils.null2Int(ForumUtils.getCookie("tpp"), config.getTpp());

			if (tpp <= 0) {
				tpp = config.getTpp();
			}

			// 得到Tpp设置
			ppp = Utils.null2Int(ForumUtils.getCookie("ppp"), config.getPpp());

			if (ppp <= 0) {
				ppp = config.getPpp();
			}

			//修正请求页数中可能的错误
			if (pageid < 1) {
				pageid = 1;
			}

			int toptopicpagecount = 0;

			if (forum.getLayer() > 0) {
				// 得到公告
				announcementlist = announcementManager.getAnnouncementList(nowdatetime, "2999-01-01 00:00:00");

				//获取当前页置顶主题列表
				Element element = topicManager.getTopTopicListID(forumid);
				if (element != null && !XmlElementUtil.findElement("tid", element).getText().trim().equals("")) {
					topiccount = topiccount
							+ Utils.null2Int(XmlElementUtil.findElement("tid0Count", element).getText(), 0);

					//获取总页数
					pagecount = topiccount % tpp == 0 ? topiccount / tpp : topiccount / tpp + 1;
					if (pagecount == 0) {
						pagecount = 1;
					}

					if (pageid > pagecount) {
						pageid = pagecount;
					}
					toptopiccount = Utils.null2Int(XmlElementUtil.findElement("tidCount", element).getText(), 0);
					if (toptopiccount > tpp * (pageid - 1)) {
						toptopiclist = topicManager.getTopTopicList(forumid, tpp, pageid, XmlElementUtil.findElement(
								"tid", element).getText(), forum.getAutoclose(), forum.getForumfields()
								.getTopictypeprefix());
						toptopicpagecount = toptopiccount / tpp;
					}

					if (toptopicpagecount >= pageid || (pageid == 1 && toptopicpagecount != toptopiccount)) {
						if (orderStr.equals("") && direct == 1) {
							topiclist = topicManager.getTopicList(forumid, tpp - toptopiccount % tpp, pageid
									- toptopicpagecount, 0, 600, config.getHottopic(), forum.getAutoclose(), forum
									.getForumfields().getTopictypeprefix(), condition);
						} else {
							if (direct == 0 && orderStr.equals("")) {
								orderStr = "lastpostid";
							}
							topiclist = topicManager.getTopicList(forumid, tpp - toptopiccount % tpp, pageid
									- toptopicpagecount, 0, 600, config.getHottopic(), forum.getAutoclose(), forum
									.getForumfields().getTopictypeprefix(), condition, orderStr, direct);
						}
					} else {
						if (orderStr.equals("") && direct == 1) {
							topiclist = topicManager.getTopicList(forumid, tpp - toptopiccount % tpp, pageid
									- toptopicpagecount, 0, 600, config.getHottopic(), forum.getAutoclose(), forum
									.getForumfields().getTopictypeprefix(), condition);
						} else {
							if (direct == 0 && orderStr.equals("")) {
								orderStr = "lastpostid";
							}
							topiclist = topicManager.getTopicList(forumid, tpp - toptopiccount % tpp, pageid
									- toptopicpagecount, 0, 600, config.getHottopic(), forum.getAutoclose(), forum
									.getForumfields().getTopictypeprefix(), condition, orderStr, direct);
						}
					}
				} else {

					toptopiclist = new ArrayList<ShowforumPageTopicInfo>();

					//获取总页数
					pagecount = topiccount % tpp == 0 ? topiccount / tpp : topiccount / tpp + 1;
					if (pagecount == 0) {
						pagecount = 1;
					}

					if (pageid > pagecount) {
						pageid = pagecount;
					}

					toptopicpagecount = 0;
					if (orderStr.equals("") && direct == 1) {
						topiclist = topicManager.getTopicList(forumid, tpp - toptopiccount % tpp, pageid
								- toptopicpagecount, 0, 600, config.getHottopic(), forum.getAutoclose(), forum
								.getForumfields().getTopictypeprefix(), condition);
					} else {
						if (direct == 0 && orderStr.equals("")) {
							orderStr = "lastpostid";
						}
						topiclist = topicManager.getTopicList(forumid, tpp - toptopiccount % tpp, pageid
								- toptopicpagecount, 0, 600, config.getHottopic(), forum.getAutoclose(), forum
								.getForumfields().getTopictypeprefix(), condition, orderStr, direct);
					}
				}

				if (forum.getForumfields().getTopictypeprefix() == 1) {
					toptopiclist = topicManager.getTopicTypeName(toptopiclist);
					topiclist = topicManager.getTopicTypeName(topiclist);
				}

				//如果topiclist为空则更新当前论坛帖数
				if (topiclist == null || topiclist.size() == 0 || topiclist.size() > topiccount) {
					forumManager.setRealCurrentTopics(forum.getFid());
				}

				//得到页码链接
				if (LForumRequest.getParamValue("search").equals("")) {
					if (topictypeid == 0) {
						pagenumbers = ForumUtils.getPageNumbers(pageid, pagecount, "showforum.action?forumid="
								+ forumid + (Utils.null2String(filter).equals("") ? "" : "&filter=") + filter, 8);
					} else {//当有主题类型条件时
						pagenumbers = ForumUtils.getPageNumbers(pageid, pagecount, "showforum.action?forumid="
								+ forumid + "&typeid=" + topictypeid
								+ (Utils.null2String(filter).equals("") ? "" : "&filter=") + filter, 8);
					}
				} else {
					pagenumbers = ForumUtils.getPageNumbers(pageid, pagecount, "showforum.action?search=1&cond="
							+ LForumRequest.getParamValue("cond") + "&order=" + LForumRequest.getParamValue("order")
							+ "&direct=" + LForumRequest.getParamValue("direct") + "&forumid=" + forumid + "&typeid="
							+ topictypeid + (Utils.null2String(filter).equals("") ? "" : "&filter=") + filter, 8);
				}
			}
		}

		forumlistboxoptions = cachesManager.getForumListBoxOptionsCache();

		onlineUserManager.updateAction(olid, ForumAction.ShowForum.ACTION_ID, forumid, forumname, -1, "", config
				.getOnlinetimeout());

		showsplitter = false;
		if (toptopiclist.size() > 0 && topiclist.size() > 0) {
			showsplitter = true;
		}

		showforumonline = false;
		onlineiconlist = cachesManager.getOnlineGroupIconList();

		if (forumtotalonline < config.getMaxonlinelist() || LForumRequest.getParamValue("showonline").equals("yes")) {
			// 获取在线统计
			showforumonline = true;
			Map<String, Integer> countMap = new HashMap<String, Integer>();
			onlineuserlist = onlineUserManager.getForumOnlineUserList(forumid, countMap);
			forumtotalonline = countMap.get(OnlineUserManager.TOTALUSER);
			forumtotalonlineguest = countMap.get(OnlineUserManager.GUESTUSER);
			forumtotalonlineuser = countMap.get(OnlineUserManager.USER);
			forumtotalonlineinvisibleuser = countMap.get(OnlineUserManager.INVISIBLEUSER);

			if (LForumRequest.getParamValue("showonline").equals("no")) {
				showforumonline = false;
			}

			ForumUtils.updateVisitedForumsOptions(forumid);
			visitedforumsoptions = forumManager.getVisitedForumsOptions(config.getVisitedforums());
			forumallowrss = forum.getAllowrss();
		}
		return SUCCESS;
	}

	public List<Online> getOnlineuserlist() {
		return onlineuserlist;
	}

	public List<ShowforumPageTopicInfo> getTopiclist() {
		return topiclist;
	}

	public List<ShowforumPageTopicInfo> getToptopiclist() {
		return toptopiclist;
	}

	public List<IndexPageForumInfo> getSubforumlist() {
		return subforumlist;
	}

	public List<Pms> getPmlist() {
		return pmlist;
	}

	public String getOnlineiconlist() {
		return onlineiconlist;
	}

	public List<Announcements> getAnnouncementlist() {
		return announcementlist;
	}

	public String[] getPagewordad() {
		return pagewordad;
	}

	public String getDoublead() {
		return doublead;
	}

	public String getFloatad() {
		return floatad;
	}

	public String getQuickeditorad() {
		return quickeditorad;
	}

	public String getQuickbgadimg() {
		return quickbgadimg;
	}

	public String getQuickbgadlink() {
		return quickbgadlink;
	}

	public Forums getForum() {
		return forum;
	}

	public Admingroups getAdmingroupinfo() {
		return admingroupinfo;
	}

	public UserExtcreditsInfo getUserextcreditsinfo() {
		return userextcreditsinfo;
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

	public boolean isShowsplitter() {
		return showsplitter;
	}

	public int getDisablepostctrl() {
		return disablepostctrl;
	}

	public int getParseurloff() {
		return parseurloff;
	}

	public int getSmileyoff() {
		return smileyoff;
	}

	public int getBbcodeoff() {
		return bbcodeoff;
	}

	public int getUsesig() {
		return usesig;
	}

	public int getAllowimg() {
		return allowimg;
	}

	public String getSmilies() {
		return smilies;
	}

	public int getTpp() {
		return tpp;
	}

	public int getPpp() {
		return ppp;
	}

	public boolean isIsmoder() {
		return ismoder;
	}

	public String getTopictypeselectoptions() {
		return topictypeselectoptions;
	}

	public String getTopictypeselectlink() {
		return topictypeselectlink;
	}

	public int getTopictypeid() {
		return topictypeid;
	}

	public String getFilter() {
		return filter;
	}

	public List<Smilies> getSmilietypes() {
		return smilietypes;
	}

	public boolean isCanposttopic() {
		return canposttopic;
	}

	public boolean isCanquickpost() {
		return canquickpost;
	}

	public String getNavhomemenu() {
		return navhomemenu;
	}

	public boolean isShowpmhint() {
		return showpmhint;
	}

	public boolean isNeedlogin() {
		return needlogin;
	}

	public int getOrder() {
		return order;
	}

	public int getCond() {
		return cond;
	}

	public int getDirect() {
		return direct;
	}

	public String getFirstpagesmilies() {
		return firstpagesmilies;
	}

	public String getCondition() {
		return condition;
	}

	public TopicManager getTopicManager() {
		return topicManager;
	}
		
}
