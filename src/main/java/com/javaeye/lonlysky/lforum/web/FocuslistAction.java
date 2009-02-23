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
import com.javaeye.lonlysky.lforum.entity.IndexOnline;
import com.javaeye.lonlysky.lforum.entity.forum.Announcements;
import com.javaeye.lonlysky.lforum.entity.forum.ForumStatistics;
import com.javaeye.lonlysky.lforum.entity.forum.Forumlinks;
import com.javaeye.lonlysky.lforum.entity.forum.Pms;
import com.javaeye.lonlysky.lforum.entity.forum.Topics;
import com.javaeye.lonlysky.lforum.entity.forum.Users;
import com.javaeye.lonlysky.lforum.service.MessageManager;
import com.javaeye.lonlysky.lforum.service.OnlineUserManager;
import com.javaeye.lonlysky.lforum.service.ScoresetManager;
import com.javaeye.lonlysky.lforum.service.StatisticManager;

/**
 * 分栏模式首页
 * 
 * @author 黄磊
 *
 */
@ParentPackage("default")
public class FocuslistAction extends ForumBaseAction {

	private static final long serialVersionUID = 4636876932468273051L;

	/**
	 * 精华主题列表
	 */
	private List<Topics> digesttopiclist;

	/**
	 * 热门主题列表
	 */
	private List<Topics> hottopiclist;

	/**
	 * 当前登录用户上次访问时间
	 */
	private String lastvisit = "";

	/**
	 * 在线用户列表
	 */
	private List<IndexOnline> onlineuserlist;

	/**
	 * 获取友情链接列表
	 */
	private List<Forumlinks> forumlinklist;

	/**
	 * 当前登录用户的短消息列表
	 */
	private List<Pms> pmlist;

	/**
	 * 公告列表
	 */
	private List<Announcements> announcementlist;

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
	 * 公告数量
	 */
	private int announcementcount;
	/**
	 * 在线图例列表
	 */
	private String onlineiconlist = "";
	/**
	 * 当前登录用户简要信息
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
	 * 友情链接数
	 */
	private int forumlinkcount;

	/**
	 * 最后注册的用户的用户名
	 */
	private String lastusername;

	/**
	 * 最后注册的用户的用户Id
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
	 * 是否显示短消息提示
	 */
	private boolean showpmhint = false;

	@Autowired
	private StatisticManager statisticManager;

	@Autowired
	private ScoresetManager scoresetManager;
	
	@Autowired
	private MessageManager messageManager;

	@Override
	public String execute() throws Exception {
		pagetitle = "首页";

		score = scoresetManager.getValidScoreName();

		if (config.getRssstatus() == 1) {
			reqcfg.addLinkRss("tools/rss.action", config.getForumtitle() + "最新主题");
		}

		onlineUserManager.updateAction(olid, ForumAction.IndexShow.ACTION_ID, 0, config.getOnlinetimeout());

		if (newpmcount > 0) {
			pmlist = messageManager.getPrivateMessageListForIndex(userid, 5, 1, 1);
		}

		userinfo = new Users();
		if (userid != -1) {
			userinfo = userManager.getUserInfo(userid);
			if (userinfo.getNewpm() == 0) {
				newpmcount = 0;
			}
			lastvisit = userinfo.getLastvisit();
			showpmhint = userinfo.getNewsletter() > 4;
		} else {
			userinfo.setCredits(0);
		}

		// 获取论坛统计信息
		Map<String, Integer> countMap = new HashMap<String, Integer>();
		statisticManager.getPostCountFromForum(0, countMap);
		totalpost = countMap.get(StatisticManager.POST_COUNT);
		todayposts = countMap.get(StatisticManager.TODAYPOST_COUNT);
		totaltopic = countMap.get(StatisticManager.TOPIC_COUNT);

		// 精华和热门主题
		digesttopiclist = new ArrayList<Topics>();
		hottopiclist = new ArrayList<Topics>();

		// 友情连接
		forumlinklist = cachesManager.getForumLinkList();
		forumlinkcount = forumlinklist.size();

		// 获得统计信息
		ForumStatistics statistics = statisticManager.getStatistic();
		totalusers = statistics.getTotalusers();
		lastusername = statistics.getLastusername();
		lastuserid = statistics.getUsers().getUid();

		totalonline = onlineusercount;

		showforumonline = false;
		if (totalonline < config.getMaxonlinelist() || LForumRequest.getParamValue("showonline").equals("yes")) {
			showforumonline = true;
			onlineuserlist = onlineUserManager.getOnlineUserList(countMap);
			onlineusercount = countMap.get(OnlineUserManager.TOTALUSER);
			totalonlineguest = countMap.get(OnlineUserManager.GUESTUSER);
			totalonlineinvisibleuser = countMap.get(OnlineUserManager.INVISIBLEUSER);
			onlineiconlist = cachesManager.getOnlineGroupIconList();
		}

		if (LForumRequest.getParamValue("showonline").equals("no")) {
			showforumonline = false;
		}

		highestonlineusercount = statistics.getHighestonlineusercount();
		highestonlineusertime = statistics.getHighestonlineusertime();

		// 得到公告
		announcementlist = new ArrayList<Announcements>();
		announcementcount = 0;
		if (announcementlist != null) {
			announcementcount = announcementlist.size();
		}

		///得到广告列表
		headerad = "";
		footerad = "";
		pagewordad = new String[0];
		doublead = "";
		floatad = "";
		return SUCCESS;
	}

	public List<Topics> getDigesttopiclist() {
		return digesttopiclist;
	}

	public List<Topics> getHottopiclist() {
		return hottopiclist;
	}

	public String getLastvisit() {
		return lastvisit;
	}

	public List<IndexOnline> getOnlineuserlist() {
		return onlineuserlist;
	}

	public List<Forumlinks> getForumlinklist() {
		return forumlinklist;
	}

	public List<Pms> getPmlist() {
		return pmlist;
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

	public boolean isShowpmhint() {
		return showpmhint;
	}

}
