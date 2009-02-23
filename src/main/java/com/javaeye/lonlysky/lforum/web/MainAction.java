package com.javaeye.lonlysky.lforum.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.struts2.config.ParentPackage;
import org.springframework.beans.factory.annotation.Autowired;

import com.javaeye.lonlysky.lforum.ForumBaseAction;
import com.javaeye.lonlysky.lforum.comm.ForumAction;
import com.javaeye.lonlysky.lforum.comm.LForumRequest;
import com.javaeye.lonlysky.lforum.comm.utils.ForumUtils;
import com.javaeye.lonlysky.lforum.comm.utils.Utils;
import com.javaeye.lonlysky.lforum.entity.IndexOnline;
import com.javaeye.lonlysky.lforum.entity.forum.Announcements;
import com.javaeye.lonlysky.lforum.entity.forum.Forumlinks;
import com.javaeye.lonlysky.lforum.entity.forum.IndexPageForumInfo;
import com.javaeye.lonlysky.lforum.entity.forum.Pms;
import com.javaeye.lonlysky.lforum.entity.forum.ForumStatistics;
import com.javaeye.lonlysky.lforum.entity.forum.Users;
import com.javaeye.lonlysky.lforum.entity.global.Tags;
import com.javaeye.lonlysky.lforum.service.AnnouncementManager;
import com.javaeye.lonlysky.lforum.service.ForumManager;
import com.javaeye.lonlysky.lforum.service.MessageManager;
import com.javaeye.lonlysky.lforum.service.OnlineUserManager;
import com.javaeye.lonlysky.lforum.service.ScoresetManager;
import com.javaeye.lonlysky.lforum.service.StatisticManager;

/**
 * 论坛首页
 * 
 * @author 黄磊
 *
 */
@ParentPackage("default")
public class MainAction extends ForumBaseAction {

	private static final long serialVersionUID = 7708540818536466578L;

	/**
	 * 论坛版块列表
	 */
	private List<IndexPageForumInfo> forumlist;

	/**
	 * 在线用户列表
	 */
	private List<IndexOnline> onlineuserlist;

	/**
	 * 当前登录的用户短消息列表
	 */
	private List<Pms> pmlist;

	/**
	 * 当前用户最后访问时间
	 */
	private String lastvisit = "未知";

	/**
	 * 友情链接列表
	 */
	private List<Forumlinks> forumlinklist;

	/**
	 * 公告列表
	 */
	private List<Announcements> announcementlist;

	/**
	 * 页内文字广告
	 */
	private List<String> pagewordad;

	/**
	 * 对联广告
	 */
	private String doublead;

	/**
	 * 浮动广告
	 */
	private String floatad;

	/**
	 * 分类间广告
	 */
	private String inforumad;

	/**
	 * 公告数量
	 */
	private int announcementcount;

	/**
	 * 在线图例列表
	 */
	private String onlineiconlist = "";

	/**
	 * 当前登录用户的信息
	 */
	private Users userinfo;

	/**
	 * 总主题数
	 */
	private int totaltopic;

	/**
	 * 总帖子数
	 */
	private int totalpost;

	/**
	 * 总用户数
	 */
	private int totalusers;

	/**
	 * 今日帖数
	 */
	private int todayposts;

	/**
	 * 昨日帖数
	 */
	private int yesterdayposts;

	/**
	 * 最高日帖数
	 */
	private int highestposts;

	/**
	 * 最高发帖日
	 */
	private String highestpostsdate;

	/**
	 * 友情链接数
	 */
	private int forumlinkcount;

	/**
	 * 最新注册的用户名
	 */
	private String lastusername;

	/**
	 * 最新注册的用户Id
	 */
	private int lastuserid;

	/**
	 * 总在线用户数
	 */
	private int totalonline;

	/**
	 * 总在线注册用户数
	 */
	private int totalonlineuser;

	/**
	 * 总在线游客数
	 */
	private int totalonlineguest;

	/**
	 * 总在线隐身用户数
	 */
	private int totalonlineinvisibleuser;

	/**
	 * 最高在线用户数
	 */
	private int highestonlineusercount;

	/**
	 * 最高在线用户数发生时间
	 */
	private String highestonlineusertime;

	/**
	 * 是否显示在线列表
	 */
	private boolean showforumonline;

	/**
	 * 可用的扩展积分显示名称
	 */
	private String[] score;

	/**
	 * 弹出导航菜单的HTML代码
	 */
	private String navhomemenu = "";

	/**
	 * 标签列表
	 */
	private List<Tags> taglist;

	/**
	 * 是否显示短消息
	 */
	private boolean showpmhint = false;

	@Autowired
	private StatisticManager statisticManager;

	@Autowired
	private ForumManager forumManager;

	@Autowired
	private ScoresetManager scoresetManager;

	@Autowired
	private MessageManager messageManager;

	@Autowired
	private AnnouncementManager announcementManager;

	@Override
	public String execute() throws Exception {
		pagetitle = "首页";

		score = scoresetManager.getValidScoreName();

		// 是否分栏显示
		int toframe = LForumRequest.getParamIntValue("f", 1);
		if (toframe == 0) {
			ForumUtils.writeCookie("isframe", 1);
		} else {
			toframe = Utils.null2Int(ForumUtils.getCookie("isframe"));
			if (toframe == -1) {
				toframe = config.getIsframeshow();
			}
		}
		if (toframe == 2) {
			// 跳转到分栏首页
			response.sendRedirect("frame.action");
		}

		if (config.getRssstatus() == 1) {
			// 启用RSS
			reqcfg.addLinkRss("tools/rss.action", "最新主题");
		}

		// 更新用户动作
		onlineUserManager.updateAction(olid, ForumAction.IndexShow.ACTION_ID, 0, config.getOnlinetimeout());

		if (newpmcount > 0) {
			pmlist = messageManager.getPrivateMessageListForIndex(userid, 5, 1, 1);
		}

		userinfo = new Users();
		if (userid != -1) {
			userinfo = userManager.getUserInfo(userid);
			if (userinfo == null) {
				userid = -1;
				// 清除cookie
			} else {
				if (userinfo.getNewpm() == 0) {
					newpmcount = 0;
				}
				lastvisit = userinfo.getLastvisit();
				showpmhint = userinfo.getNewsletter() > 4;
			}
		}//end if

		// 获取前台板块弹出HTML
		navhomemenu = cachesManager.getForumListMenuDiv(usergroupid, userid, "");

		totaltopic = 0;
		totalpost = 0;
		todayposts = 0;

		// 获取板块列表/友情连接列表/友情连接数量
		Map<String, Integer> countMap = new HashMap<String, Integer>();
		forumlist = forumManager.getIndexPageForum(config.getHideprivate(), toframe, config.getModdisplay(), countMap);
		totaltopic = Utils.null2Int(countMap.get(ForumManager.TOPIC_COUNT), 0);
		totalpost = Utils.null2Int(countMap.get(ForumManager.POST_COUNT), 0);
		todayposts = Utils.null2Int(countMap.get(ForumManager.TODAY_COUNT), 0);
		forumlinklist = cachesManager.getForumLinkList();
		forumlinkcount = forumlinklist.size();

		totalonline = onlineusercount;
		showforumonline = false;
		onlineiconlist = cachesManager.getOnlineGroupIconList();

		if (totalonline < config.getMaxonlinelist() || LForumRequest.getParamValue("showonline").equals("yes")) {
			showforumonline = true;
			//获得在线用户列表和图标
			countMap = new HashMap<String, Integer>();
			onlineuserlist = onlineUserManager.getOnlineUserList(countMap);
			totalonline = countMap.get(OnlineUserManager.TOTALUSER);
			totalonlineguest = countMap.get(OnlineUserManager.GUESTUSER);
			totalonlineuser = countMap.get(OnlineUserManager.USER);
			totalonlineinvisibleuser = countMap.get(OnlineUserManager.INVISIBLEUSER);
		}

		if (LForumRequest.getParamValue("showonline").equals("no")) {
			showforumonline = false;
		}

		// 获取统计信息
		statisticManager.updateYesterdayPosts();
		ForumStatistics statistics = statisticManager.getStatistic();
		if (totalonline > statistics.getHighestonlineusercount()) {
			statistics.setHighestonlineusercount(totalonline);
			statistics.setHighestonlineusertime(Utils.getNowTime());
			statisticManager.updateStatistics(statistics);
		}
		totalusers = statistics.getTotalusers();
		lastusername = statistics.getLastusername();
		lastuserid = statistics.getUsers().getUid();
		yesterdayposts = statistics.getYesterdayposts();
		highestposts = statistics.getHighestposts();
		highestpostsdate = statistics.getHighestpostsdate();
		if (todayposts > highestposts) {
			highestposts = todayposts;
			highestpostsdate = Utils.getNowTime();
			statisticManager.updateStatistics(statistics);
		}
		highestonlineusercount = statistics.getHighestonlineusercount();
		highestonlineusertime = statistics.getHighestonlineusertime();

		// 获取公告信息
		announcementlist = announcementManager.getAnnouncementList(nowdatetime, "2999-01-01 00:00:00");
		announcementcount = 0;
		if (announcementlist != null) {
			announcementcount = announcementlist.size();
		}

		if (config.getEnabletag() == 1) {
			// 获取Tag
			taglist = new ArrayList<Tags>();
		} else {
			taglist = new ArrayList<Tags>();
		}

		// 获取广告信息
		inforumad = "";
		pagewordad = new ArrayList<String>();
		doublead = "";
		floatad = "";

		return SUCCESS;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public List<IndexPageForumInfo> getForumlist() {
		return forumlist;
	}

	public List<IndexOnline> getOnlineuserlist() {
		return onlineuserlist;
	}

	public List<Pms> getPmlist() {
		return pmlist;
	}

	public String getLastvisit() {
		return lastvisit;
	}

	public List<Forumlinks> getForumlinklist() {
		return forumlinklist;
	}

	public List<Announcements> getAnnouncementlist() {
		return announcementlist;
	}

	public List<String> getPagewordad() {
		return pagewordad;
	}

	public String getDoublead() {
		return doublead;
	}

	public String getFloatad() {
		return floatad;
	}

	public String getInforumad() {
		return inforumad;
	}

	public int getAnnouncementcount() {
		return announcementcount;
	}

	public String getOnlineiconlist() {
		return onlineiconlist;
	}

	public Users getUserinfo() {
		return userinfo;
	}

	public int getTotaltopic() {
		return totaltopic;
	}

	public int getTotalpost() {
		return totalpost;
	}

	public int getTotalusers() {
		return totalusers;
	}

	public int getTodayposts() {
		return todayposts;
	}

	public int getYesterdayposts() {
		return yesterdayposts;
	}

	public int getHighestposts() {
		return highestposts;
	}

	public String getHighestpostsdate() {
		return highestpostsdate;
	}

	public int getForumlinkcount() {
		return forumlinkcount;
	}

	public String getLastusername() {
		return lastusername;
	}

	public int getLastuserid() {
		return lastuserid;
	}

	public int getTotalonline() {
		return totalonline;
	}

	public int getTotalonlineuser() {
		return totalonlineuser;
	}

	public int getTotalonlineguest() {
		return totalonlineguest;
	}

	public int getTotalonlineinvisibleuser() {
		return totalonlineinvisibleuser;
	}

	public int getHighestonlineusercount() {
		return highestonlineusercount;
	}

	public String getHighestonlineusertime() {
		return highestonlineusertime;
	}

	public boolean isShowforumonline() {
		return showforumonline;
	}

	public String[] getScore() {
		return score;
	}

	public String getNavhomemenu() {
		return navhomemenu;
	}

	public List<Tags> getTaglist() {
		return taglist;
	}

	public boolean isShowpmhint() {
		return showpmhint;
	}

}
