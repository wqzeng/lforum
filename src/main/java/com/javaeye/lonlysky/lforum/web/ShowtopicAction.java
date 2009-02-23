package com.javaeye.lonlysky.lforum.web;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.time.DateUtils;
import org.apache.struts2.config.ParentPackage;
import org.springframework.beans.factory.annotation.Autowired;

import com.javaeye.lonlysky.lforum.ForumBaseAction;
import com.javaeye.lonlysky.lforum.comm.ForumAction;
import com.javaeye.lonlysky.lforum.comm.LForumRequest;
import com.javaeye.lonlysky.lforum.comm.MD5;
import com.javaeye.lonlysky.lforum.comm.utils.ForumUtils;
import com.javaeye.lonlysky.lforum.comm.utils.Utils;
import com.javaeye.lonlysky.lforum.entity.forum.Admingroups;
import com.javaeye.lonlysky.lforum.entity.forum.Bonuslog;
import com.javaeye.lonlysky.lforum.entity.forum.Debates;
import com.javaeye.lonlysky.lforum.entity.forum.Forums;
import com.javaeye.lonlysky.lforum.entity.forum.Pms;
import com.javaeye.lonlysky.lforum.entity.forum.PollOptionView;
import com.javaeye.lonlysky.lforum.entity.forum.Polls;
import com.javaeye.lonlysky.lforum.entity.forum.PostpramsInfo;
import com.javaeye.lonlysky.lforum.entity.forum.ShowtopicPageAttachmentInfo;
import com.javaeye.lonlysky.lforum.entity.forum.ShowtopicPagePostInfo;
import com.javaeye.lonlysky.lforum.entity.forum.Smilies;
import com.javaeye.lonlysky.lforum.entity.forum.Topicidentify;
import com.javaeye.lonlysky.lforum.entity.forum.Topics;
import com.javaeye.lonlysky.lforum.entity.forum.UserExtcreditsInfo;
import com.javaeye.lonlysky.lforum.service.AdminGroupManager;
import com.javaeye.lonlysky.lforum.service.BonuManager;
import com.javaeye.lonlysky.lforum.service.CachesManager;
import com.javaeye.lonlysky.lforum.service.EditorManager;
import com.javaeye.lonlysky.lforum.service.ForumManager;
import com.javaeye.lonlysky.lforum.service.MessageManager;
import com.javaeye.lonlysky.lforum.service.ModeratorManager;
import com.javaeye.lonlysky.lforum.service.PaymentLogManager;
import com.javaeye.lonlysky.lforum.service.PollManager;
import com.javaeye.lonlysky.lforum.service.PostManager;
import com.javaeye.lonlysky.lforum.service.ScoresetManager;
import com.javaeye.lonlysky.lforum.service.SmilieManager;
import com.javaeye.lonlysky.lforum.service.TopicAdminManager;
import com.javaeye.lonlysky.lforum.service.TopicManager;
import com.javaeye.lonlysky.lforum.service.TopicStatManager;

/**
 * 查看主题页面
 * 
 * @author 黄磊
 *
 */
@ParentPackage("default")
public class ShowtopicAction extends ForumBaseAction {

	private static final long serialVersionUID = 5607314931026930030L;

	/**
	 * 主题信息
	 */
	private Topics topic;

	/**
	 * 帖子列表
	 */
	private List<ShowtopicPagePostInfo> postlist;

	/**
	 * 附件列表
	 */
	private List<ShowtopicPageAttachmentInfo> attachmentlist;

	/**
	 * 短消息列表
	 */
	private List<Pms> pmlist;

	/**
	 * 悬赏给分日志
	 */
	private List<Bonuslog> bonuslogs;

	/**
	 * 投票选项列表
	 */
	private List<PollOptionView> polloptionlist;

	/**
	 * 投票帖类型
	 */
	private Polls pollinfo;

	/**
	 * 是否显示投票结果
	 */
	private boolean showpollresult = true;

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
	 * 快速编辑器背景广告
	 */
	private String quickbgadimg = "";
	private String quickbgadlink = "";

	/**
	 * 所属版块Id
	 */
	private int forumid;

	/**
	 * 所属版块名称
	 */
	private String forumname;

	/**
	 * 论坛导航信息
	 */
	private String forumnav;

	/**
	 * 主题Id
	 */
	private int topicid;

	/**
	 * 是否是投票帖
	 */
	private boolean ispoll;

	/**
	 * 是否允许投票
	 */
	private boolean allowvote;

	/**
	 * 是否允许评分
	 */
	private boolean allowrate;

	/**
	 * 主题标题
	 */
	private String topictitle;

	/**
	 * 回复标题
	 */
	private String replytitle;

	/**
	 * 主题魔法表情
	 */
	private String topicmagic = "";

	/**
	 * 主题浏览量
	 */
	private int topicviews;

	/**
	 * 当前页码
	 */
	private int pageid;

	/**
	 * 回复帖子数
	 */
	private int postcount;

	/**
	 * 分页页数
	 */
	private int pagecount;

	/**
	 * 分页页码链接
	 */
	private String pagenumbers;
	/**
	 * 表情Javascript数组
	 */
	private String smilies;

	/**
	 * 参与投票的用户列表
	 */
	private String voters;

	/**
	 * 论坛跳转链接选项
	 */
	private String forumlistboxoptions;

	/**
	 * 是否是管理者
	 */
	private int ismoder = 0;

	/**
	 * 上一次进行的管理操作
	 */
	private String moderactions;

	/**
	 * 是否解析URL
	 */
	private int parseurloff;

	/**
	 * 是否解析表情
	 */
	private int smileyoff;

	/**
	 * 是否解析 Discuz!NT 代码
	 */
	private int bbcodeoff;

	/**
	 * 是否使用签名
	 */
	private int usesig;

	/**
	 * 是否允许 [img]标签
	 */
	private int allowimg;

	/**
	 * 是否显示评分记录
	 */
	private int showratelog;

	/**
	 * 可用的扩展积分名称列表
	 */
	private String[] score;

	/**
	 * 可用的扩展积分单位列表
	 */
	private String[] scoreunit;

	/**
	 * 是否受发贴灌水限制
	 */
	private int disablepostctrl;

	/**
	 * 积分策略信息
	 */
	private UserExtcreditsInfo userextcreditsinfo;

	/**
	 * 主题鉴定信息
	 */
	private Topicidentify topicidentify;

	/**
	 * 用户的管理组信息
	 */
	private Admingroups admininfo = null;

	/**
	 * 当前版块信息
	 */
	private Forums forum;

	/**
	 * 是否只查看楼主贴子 1:只看楼主  0:显示全部
	 */
	private String onlyauthor = "0";

	/**
	 * 当前的主题类型
	 */
	private String topictypes = "";

	/**
	 * 表情分类列表
	 */
	private List<Smilies> smilietypes;

	/**
	 * 是否显示下载链接
	 */
	private boolean allowdownloadattach = false;

	/**
	 * 是否有发表主题的权限
	 */
	private boolean canposttopic = false;

	/**
	 * 是否有回复的权限
	 */
	private boolean canreply = false;

	/**
	 * 论坛弹出导航菜单HTML代码
	 */
	private String navhomemenu = "";

	/**
	 * 帖间通栏广告
	 */
	private String postleaderboardad = "";

	/**
	 * 是否显示短消息列表
	 */
	private boolean showpmhint = false;

	/**
	 * 帖内广告
	 */
	private String inpostad = "";

	/**
	 * 快速发帖广告
	 */
	private String quickeditorad = "";

	/**
	 * 每页帖子数
	 */
	private int ppp;

	/**
	 * 是否显示需要登录后访问的错误提示
	 */
	private boolean needlogin = false;

	/**
	 * 第一页表情的JSON
	 */
	private String firstpagesmilies = "";

	/**
	 * 相关主题集合
	 */
	private List<Topics> relatedtopics;
	/**
	 * 本版是否启用了Tag
	 */
	private boolean enabletag = false;

	/**
	 * 通过TID得到帖子观点
	 */
	private Map<Integer, Integer> debateList;

	/**
	 * 作为辩论主题的扩展属性
	 */
	private Debates debateexpand;
	/**
	 * 已经结束的辩论
	 */
	private boolean isenddebate = false;

	@Autowired
	private CachesManager cachesManager;

	@Autowired
	private TopicManager topicManager;

	@Autowired
	private ForumManager forumManager;

	@Autowired
	private ModeratorManager moderatorManager;

	@Autowired
	private AdminGroupManager adminGroupManager;

	@Autowired
	private ScoresetManager scoresetManager;

	@Autowired
	private PaymentLogManager paymentLogManager;

	@Autowired
	private BonuManager bonuManager;

	@Autowired
	private PollManager pollManager;

	@Autowired
	private PostManager postManager;

	@Autowired
	private SmilieManager smilieManager;

	@Autowired
	private EditorManager editorManager;

	@Autowired
	private MessageManager messageManager;

	@Autowired
	private TopicStatManager topicStatManager;

	@Autowired
	private TopicAdminManager topicAdminManager;

	@Override
	public String execute() throws Exception {
		headerad = "";
		footerad = "";
		postleaderboardad = "";

		doublead = "";
		floatad = "";

		allowrate = false;
		disablepostctrl = 0;
		// 获取主题ID
		topicid = LForumRequest.getParamIntValue("topicid", -1);
		// 获取该主题的信息
		String go = LForumRequest.getParamValue("go").toLowerCase();
		int fid = LForumRequest.getParamIntValue("forumid", 0);
		firstpagesmilies = cachesManager.getSmiliesFirstPageCache();

		if (go.equals("")) {
			fid = 0;
		} else if (fid == 0) {
			go = "";
		}

		String errmsg = "";
		if (go.equals("prev")) {
			topic = topicManager.getTopicInfo(topicid, fid, 1);
			errmsg = "没有更旧的主题, 请返回";
		} else if (go.equals("next")) {
			topic = topicManager.getTopicInfo(topicid, fid, 2);
			errmsg = "没有更新的主题, 请返回";
		} else {
			topic = topicManager.getTopicInfo(topicid);
			errmsg = "该主题不存在";
		}

		try {
			if (topic.getIdentify() > 0) {
				topicidentify = cachesManager.getTopicIdentify(topic.getIdentify());
			}
		} catch (Exception e) {
			reqcfg.addErrLine(errmsg);

			// 获取广告信息
			headerad = "";
			footerad = "";
			pagewordad = new String[0];
			doublead = "";
			floatad = "";
			return SUCCESS;
		}
		topicid = topic.getTid();
		forumid = topic.getForums().getFid();
		forum = forumManager.getForumInfo(forumid);
		forumname = forum.getName();
		pagetitle = topic.getTitle();
		forumnav = forum.getPathlist().trim();
		navhomemenu = cachesManager.getForumListMenuDiv(usergroupid, userid, "");

		fid = forumid;

		///得到广告列表
		///头部
		headerad = "";
		footerad = "";
		postleaderboardad = "";

		pagewordad = new String[0];
		doublead = "";
		floatad = "";

		// 检查是否具有版主的身份
		if (useradminid != 0) {
			ismoder = moderatorManager.isModer(useradminid, userid, forumid) ? 1 : 0;
			//得到管理组信息
			admininfo = adminGroupManager.getAdminGroup(useradminid);
			if (admininfo != null) {
				disablepostctrl = admininfo.getDisablepostctrl();
			}
		}
		//验证不通过则返回
		if (!isConditionsValid())
			return SUCCESS;

		showratelog = config.getDisplayratecount() > 0 ? 1 : 0;

		topictitle = topic.getTitle().trim();
		replytitle = topictitle;
		if (replytitle.length() >= 50) {
			replytitle = Utils.format(replytitle, 50, true);
		}

		topicviews = topic.getViews() + 1
				+ (config.getTopicqueuestats() == 1 ? topicStatManager.getStoredTopicViewCount(topic.getTid()) : 0);
		smilies = cachesManager.getSmiliesCache();
		smilietypes = cachesManager.getSmilieTypesCache();

		//编辑器状态
		StringBuilder sb = new StringBuilder();
		sb.append("var Allowhtml=1;\r\n");

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

		usesig = ForumUtils.getCookie("sigstatus").equals("0") ? 0 : 1;

		allowimg = forum.getAllowimgcode();
		sb.append("var Allowimgcode=" + allowimg + ";\r\n");

		reqcfg.addScript(sb.toString());
		int price = 0;
		if (topic.getSpecial() == 0)//普通主题
		{
			//购买帖子操作
			//判断是否为购买可见帖, price=0为非购买可见(正常), price>0 为购买可见, price=-1为购买可见但当前用户已购买                
			if (topic.getPrice() > 0 && userid != topic.getUsersByPosterid().getUid() && ismoder != 1) {
				price = topic.getPrice();
				//时间乘以-1是因为当scoresetManager.getMaxChargeSpan()==0时,帖子始终为购买帖
				if (paymentLogManager.isBuyer(topicid, userid)
						|| (Utils.strDateDiffHours(topic.getPostdatetime(), scoresetManager.getMaxChargeSpan()) > 0 && scoresetManager
								.getMaxChargeSpan() != 0)) { //判断当前用户是否已经购买
					price = -1;
				}
			}
			if (price > 0) {
				response.sendRedirect("buytopic.action?topicid=" + topic.getTid());
				return null;
			}
		}

		if (topic.getSpecial() == 3)//已给分的悬赏帖
		{
			bonuslogs = bonuManager.getLogs(topic.getTid());
		}

		if (topic.getModerated() > 0) {
			moderactions = topicAdminManager.getTopicListModeratorLog(topicid);
		}
		topictypes = Utils.null2String(cachesManager.getTopicTypeArray().get(topic.getTopictypes().getTypeid()));
		topictypes = !topictypes.equals("") ? "[" + topictypes + "]" : "";

		userextcreditsinfo = scoresetManager.getScoreSet(scoresetManager.getCreditsTrans());

		if (newpmcount > 0) {
			pmlist = messageManager.getPrivateMessageListForIndex(userid, 5, 1, 1);
			showpmhint = userManager.getUserInfo(userid).getNewsletter() > 4;
		}

		ispoll = false;
		allowvote = false;
		Map<Integer, Boolean> tmp = new HashMap<Integer, Boolean>();
		if (topic.getSpecial() == 1) {
			ispoll = true;
			polloptionlist = pollManager.getPollOptionList(topicid);
			pollinfo = pollManager.getPollInfo(topicid);
			voters = pollManager.getVoters(topicid, userid, username, tmp);
			allowvote = tmp.get(0);

			if (pollinfo.getUsers().getUid() != userid && useradminid != 1) //当前用户不是投票发起人或不是管理组成员
			{
				if (pollinfo.getVisible() == 1 && //当为投票才可见时
						(allowvote || (userid == -1 && !Utils.inArray(topicid + "", ForumUtils.getCookie("polled")))))//当允许投票或为游客(且并未投过票时)时
				{
					showpollresult = false;
				}
			}
		}

		if (ispoll) {
			if (Utils.null2String(pollinfo.getExpiration()).equals("")) {
				pollinfo.setExpiration(Utils.getNowTime());
			}

			SimpleDateFormat sdf = new SimpleDateFormat(Utils.FULL_DATEFORMAT);
			if (sdf.parse(pollinfo.getExpiration()).before(Utils.getNowDate())) {
				allowvote = false;
			}

		}

		// 获取帖子总数
		onlyauthor = LForumRequest.getParamValue("onlyauthor");
		if (onlyauthor == "" || onlyauthor == "0") {
			postcount = topic.getReplies() + 1;
		} else {
			postcount = postManager.getPostCount(topicid, topic.getUsersByPosterid().getUid());
		}

		// 得到Ppp设置
		ppp = Utils.null2Int(ForumUtils.getCookie("ppp"), config.getPpp());

		if (ppp <= 0) {
			ppp = config.getPpp();
		}

		//获取总页数
		pagecount = postcount % ppp == 0 ? postcount / ppp : postcount / ppp + 1;
		if (pagecount == 0) {
			pagecount = 1;
		}
		// 得到当前用户请求的页数
		if (LForumRequest.getParamValue("page").toLowerCase().equals("end")) {
			pageid = pagecount;
		} else {
			pageid = LForumRequest.getParamIntValue("page", 1);
		}
		//修正请求页数中可能的错误
		if (pageid < 1) {
			pageid = 1;
		}
		if (pageid > pagecount) {
			pageid = pagecount;
		}
		//判断是否为回复可见帖, hide=0为不解析[hide]标签, hide>0解析为回复可见字样, hide=-1解析为以下内容回复可见字样显示真实内容
		//将逻辑判断放入取列表的循环中处理,此处只做是否为回复人的判断，主题作者也该可见
		int hide = 1;
		if (topic.getHide() == 1 && (postManager.isReplier(topicid, userid) || ismoder == 1)) {
			hide = -1;
		}

		//获取当前页主题列表

		PostpramsInfo postpramsInfo = new PostpramsInfo();
		postpramsInfo.setFid(forum.getFid());
		postpramsInfo.setTid(topicid);
		postpramsInfo.setJammer(forum.getJammer());
		postpramsInfo.setPagesize(ppp);
		postpramsInfo.setPageindex(pageid);
		postpramsInfo.setGetattachperm(forum.getForumfields().getGetattachperm());
		postpramsInfo.setUsergroupid(usergroupid);
		postpramsInfo.setAttachimgpost(config.getAttachimgpost());
		postpramsInfo.setShowattachmentpath(config.getShowattachmentpath());
		postpramsInfo.setHide(hide);
		postpramsInfo.setPrice(price);
		postpramsInfo.setUsergroupreadaccess(usergroupinfo.getReadaccess());
		if (ismoder == 1) {
			postpramsInfo.setUsergroupreadaccess(Integer.MAX_VALUE);
		}
		postpramsInfo.setCurrentuserid(userid);
		postpramsInfo.setShowimages(forum.getAllowimgcode());
		postpramsInfo.setSmiliesinfo(smilieManager.getSmiliesListWithInfo());
		postpramsInfo.setCustomeditorbuttoninfo(editorManager.getCustomEditButtonListWithInfo());
		postpramsInfo.setSmiliesmax(config.getSmiliesmax());
		postpramsInfo.setBbcodemode(0);
		postpramsInfo.setCurrentusergroup(usergroupinfo);
		postpramsInfo.setCondition("");
		if (!(onlyauthor.equals("") || onlyauthor.equals("0"))) {
			postpramsInfo.setCondition("users.uid=" + topic.getUsersByPosterid().getUid());
		}
		Map<Integer, List<ShowtopicPageAttachmentInfo>> map = new HashMap<Integer, List<ShowtopicPageAttachmentInfo>>();
		map.put(0, attachmentlist);
		postlist = postManager.getPostList(postpramsInfo, map, ismoder == 1);
		attachmentlist = map.get(0);
		for (ShowtopicPageAttachmentInfo showtopicpageattachinfo : attachmentlist) {
			if (forumManager.allowGetAttachByUserID(forum.getForumfields().getPermuserlist().trim(), userid)) {
				showtopicpageattachinfo.setGetattachperm(1);
				showtopicpageattachinfo.setAllowread(1);
			}
		}

		if (topic.getSpecial() == 4) {
			//            debateexpand = Debates.GetDebateTopic(topicid);
			//            debateList = Debates.GetPostDebateList(topicid);//通过TID得到帖子观点
			//
			//            if (debateexpand.Terminaltime < DateTime.Now)
			//            {
			//                isenddebate = true;
			//            }
			//
			//            for (ShowtopicPagePostInfo postlistinfo in postlist)
			//            {
			//                //设置POST的观点属性
			//                if (debateList != null && debateList.ContainsKey(postlistinfo.Pid))
			//                postlistinfo.Debateopinion = Convert.ToInt32(debateList[postlistinfo.Pid]);
			//
			//                
			//            }
		}
		//加载帖内广告
		inpostad = "";//Advertisements.GetInPostAd("", fid, templatepath, postlist.Count > ppp ? ppp : postlist.Count);
		//快速发帖广告
		quickeditorad = "";//Advertisements.GetQuickEditorAD("", fid);

		//快速编辑器背景广告
		String[] quickbgad = new String[0];//Advertisements.GetQuickEditorBgAd("", fid);

		if (quickbgad.length > 1) {
			quickbgadimg = quickbgad[0];
			quickbgadlink = quickbgad[1];
		}

		if (postlist.size() <= 0) {
			reqcfg.addErrLine("读取信息失败");
			return SUCCESS;
		}

		//更新页面Meta中的Description项, 提高SEO友好性
		String metadescritpion = Utils.cleanHtmlTag((postlist.get(0).getMessage()));
		metadescritpion = metadescritpion.length() > 100 ? metadescritpion.substring(0, 100) : metadescritpion;
		reqcfg.updateMetaInfo(config.getSeokeywords(), metadescritpion, config.getSeohead());

		//获取相关主题集合
		enabletag = (config.getEnabletag() & forum.getAllowtag()) == 1;
		if (enabletag) {
			relatedtopics = new ArrayList<Topics>();
			//relatedtopics = Topics.GetRelatedTopics(topicid, 5);
		}

		//得到页码链接
		if (onlyauthor == "" || onlyauthor == "0") {
			pagenumbers = ForumUtils.getPageNumbers(pageid, pagecount, "showtopic.action?topicid=" + topicid, 8);
		} else {
			pagenumbers = ForumUtils.getPageNumbers(pageid, pagecount, "showtopic.action?onlyauthor=1&topicid="
					+ topicid, 8);
		}

		//更新查看次数
		topicStatManager.track(topicid, 1);

		onlineUserManager.updateAction(olid, ForumAction.ShowTopic.ACTION_ID, forumid, forumname, topicid, topictitle,
				config.getOnlinetimeout());
		forumlistboxoptions = cachesManager.getForumListBoxOptionsCache();

		//得到页码链接
		if (onlyauthor == "" || onlyauthor == "0") {
			ForumUtils.writeCookie("referer", "showtopic.action?topicid=" + topicid + "&page=" + pageid);
		} else {
			ForumUtils.writeCookie("referer", "showtopic.aspx?onlyauthor=1&topicid=" + topicid + "&page=" + pageid);
		}

		score = scoresetManager.getValidScoreName();
		scoreunit = scoresetManager.getValidScoreUnit();

		String oldtopic = ForumUtils.getCookie("oldtopic") + "D";

		if (oldtopic.indexOf("D" + topic.getTid() + "D") == -1
				&& DateUtils.addMinutes(new Date(), -1 * 600).before(Utils.parseDate(topic.getLastpost()))) {
			oldtopic = "D" + topic.getTid() + oldtopic.substring(0, oldtopic.length() - 1);
			if (oldtopic.length() > 3000) {
				oldtopic = Utils.format(oldtopic, 3000, false);
				oldtopic = Utils.format(oldtopic, oldtopic.lastIndexOf("D"), false);
			}
			ForumUtils.writeCookie("oldtopic", oldtopic);
		}

		// 判断是否需要生成游客缓存页面
		if (userid == -1 && pageid == 1) {
			int topiccachemark = 100 - (int) (topic.getDisplayorder() * 15 + topic.getDigest() * 10
					+ Math.min(topic.getViews() / 20, 50) + Math.min(topic.getReplies() / config.getPpp() * 1.5, 15));
			if (topiccachemark < config.getTopiccachemark()) {
				//isguestcachepage = 1;
			}
		}
		return SUCCESS;
	}

	/**
	 * 验证
	 */
	private boolean isConditionsValid() throws ParseException, IOException {
		// 如果包含True, 则必然允许某项扩展积分的评分
		if (usergroupinfo.getRaterange().indexOf("True") > -1) {
			allowrate = true;
		}

		// 如果主题ID非数字
		if (topicid == -1) {
			reqcfg.addErrLine("无效的主题ID");

			return false;
		}

		// 如果该主题不存在
		if (topic == null) {
			reqcfg.addErrLine("不存在的主题ID");
			return false;
		}

		if (topic.getClosed() > 1) {
			topicid = topic.getClosed();
			topic = topicManager.getTopicInfo(topicid);

			// 如果该主题不存在
			if (topic == null || topic.getClosed() > 1) {
				reqcfg.addErrLine("不存在的主题ID");
				return false;
			}
		}

		if (topic.getReadperm() > usergroupinfo.getReadaccess() && topic.getUsersByPosterid().getUid() != userid
				&& useradminid != 1 && ismoder != 1) {
			reqcfg.addErrLine("本主题阅读权限为: " + topic.getReadperm() + ", 您当前的身份 \"" + usergroupinfo.getGrouptitle()
					+ "\" 阅读权限不够");
			if (userid == -1) {
				needlogin = true;
			}
			return false;
		}

		if (topic.getDisplayorder() == -1) {
			reqcfg.addErrLine("此主题已被删除！");
			return false;
		}

		if (topic.getDisplayorder() == -2) {
			reqcfg.addErrLine("此主题未经审核！");
			return false;
		}

		if (!forum.getForumfields().getPassword().equals("")
				&& !MD5.encode(forum.getForumfields().getPassword()).equals(
						ForumUtils.getCookie("forum" + forumid + "password"))) {
			reqcfg.addErrLine("本版块被管理员设置了密码");
			response.sendRedirect("showforum.action?forumid=" + forumid);
			return false;
		}

		if (!forumManager.allowViewByUserID(forum.getForumfields().getPermuserlist(), userid)) { //判断当前用户在当前版块浏览权限
			if (Utils.null2String(forum.getForumfields().getViewperm()).equals("")) { //当板块权限为空时，按照用户组权限
				if (usergroupinfo.getAllowvisit() != 1) {
					reqcfg.addErrLine("您当前的身份 \"" + usergroupinfo.getGrouptitle() + "\" 没有浏览该版块的权限");
					if (userid == -1) {
						needlogin = true;
					}
					return false;
				}
			} else { //当板块权限不为空，按照板块权限
				if (!forumManager.allowView(forum.getForumfields().getViewperm(), usergroupid)) {
					reqcfg.addErrLine("您没有浏览该版块的权限");
					if (userid == -1) {
						needlogin = true;
					}
					return false;
				}
			}
		}

		//是否显示回复链接
		if (forumManager.allowReplyByUserID(forum.getForumfields().getPermuserlist(), userid)) {
			canreply = true;
		} else {
			if (Utils.null2String(forum.getForumfields().getReplyperm()).equals("")) //权限设置为空时，根据用户组权限判断
			{
				// 验证用户是否有发表主题的权限
				if (usergroupinfo.getAllowreply() == 1) {
					canreply = true;
				}
			} else if (forumManager.allowReply(forum.getForumfields().getReplyperm(), usergroupid)) {
				canreply = true;
			}
		}

		if ((topic.getClosed() == 0 && canreply) || ismoder == 1) {
			canreply = true;
		} else {
			canreply = false;
		}

		//当前用户是否有允许下载附件权限
		if (forumManager.allowGetAttachByUserID(forum.getForumfields().getPermuserlist(), userid)) {
			allowdownloadattach = true;
		} else {
			if (Utils.null2String(forum.getForumfields().getGetattachperm()).equals("")) //权限设置为空时，根据用户组权限判断
			{
				// 验证用户是否有有允许下载附件权限
				if (usergroupinfo.getAllowgetattach() == 1) {
					allowdownloadattach = true;
				}
			} else if (forumManager.allowGetAttach(forum.getForumfields().getGetattachperm(), usergroupid)) {
				allowdownloadattach = true;
			}
		}

		//判断是否有发主题的权限
		if (userid > -1 && forumManager.allowPostByUserID(forum.getForumfields().getPermuserlist(), userid)) {
			canposttopic = true;
		}

		if (Utils.null2String(forum.getForumfields().getPostperm()).equals("")) //权限设置为空时，根据用户组权限判断
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

		//是否有回复的权限
		if (topic.getClosed() == 0 && userid > -1) {
			if (config.getFastpost() == 2 || config.getFastpost() == 3) {
				if (forumManager.allowReplyByUserID(forum.getForumfields().getPermuserlist(), userid)) {
					canreply = true;
				} else {
					if (Utils.null2String(forum.getForumfields().getReplyperm()).equals("")) //权限设置为空时，根据用户组权限判断
					{
						// 验证用户是否有发表主题的权限
						if (usergroupinfo.getAllowreply() == 1) {
							canreply = true;
						}
					} else if (forumManager.allowReply(forum.getForumfields().getPermuserlist(), usergroupid)) {
						canreply = true;
					}
				}
			}
		}
		return true;
	}

	public List<ShowtopicPagePostInfo> getPostlist() {
		return postlist;
	}

	public List<ShowtopicPageAttachmentInfo> getAttachmentlist() {
		return attachmentlist;
	}

	public List<Pms> getPmlist() {
		return pmlist;
	}

	public List<Bonuslog> getBonuslogs() {
		return bonuslogs;
	}

	public List<PollOptionView> getPolloptionlist() {
		return polloptionlist;
	}

	public Polls getPollinfo() {
		return pollinfo;
	}

	public boolean isShowpollresult() {
		return showpollresult;
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

	public String getQuickbgadimg() {
		return quickbgadimg;
	}

	public String getQuickbgadlink() {
		return quickbgadlink;
	}

	public int getForumid() {
		return forumid;
	}

	public String getForumname() {
		return forumname;
	}

	public String getForumnav() {
		return forumnav;
	}

	public int getTopicid() {
		return topicid;
	}

	public boolean isIspoll() {
		return ispoll;
	}

	public boolean isAllowvote() {
		return allowvote;
	}

	public boolean isAllowrate() {
		return allowrate;
	}

	public String getTopictitle() {
		return topictitle;
	}

	public String getReplytitle() {
		return replytitle;
	}

	public String getTopicmagic() {
		return topicmagic;
	}

	public int getTopicviews() {
		return topicviews;
	}

	public int getPageid() {
		return pageid;
	}

	public int getPostcount() {
		return postcount;
	}

	public int getPagecount() {
		return pagecount;
	}

	public String getPagenumbers() {
		return pagenumbers;
	}

	public String getSmilies() {
		return smilies;
	}

	public String getVoters() {
		return voters;
	}

	public String getForumlistboxoptions() {
		return forumlistboxoptions;
	}

	public int getIsmoder() {
		return ismoder;
	}

	public String getModeractions() {
		return moderactions;
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

	public int getShowratelog() {
		return showratelog;
	}

	public String[] getScore() {
		return score;
	}

	public String[] getScoreunit() {
		return scoreunit;
	}

	public int getDisablepostctrl() {
		return disablepostctrl;
	}

	public UserExtcreditsInfo getUserextcreditsinfo() {
		return userextcreditsinfo;
	}

	public Topicidentify getTopicidentify() {
		return topicidentify;
	}

	public Admingroups getAdmininfo() {
		return admininfo;
	}

	public Forums getForum() {
		return forum;
	}

	public String getOnlyauthor() {
		return onlyauthor;
	}

	public String getTopictypes() {
		return topictypes;
	}

	public List<Smilies> getSmilietypes() {
		return smilietypes;
	}

	public boolean isAllowdownloadattach() {
		return allowdownloadattach;
	}

	public boolean isCanposttopic() {
		return canposttopic;
	}

	public boolean isCanreply() {
		return canreply;
	}

	public String getNavhomemenu() {
		return navhomemenu;
	}

	public String getPostleaderboardad() {
		return postleaderboardad;
	}

	public boolean isShowpmhint() {
		return showpmhint;
	}

	public String getInpostad() {
		return inpostad;
	}

	public String getQuickeditorad() {
		return quickeditorad;
	}

	public int getPpp() {
		return ppp;
	}

	public boolean isNeedlogin() {
		return needlogin;
	}

	public String getFirstpagesmilies() {
		return firstpagesmilies;
	}

	public List<Topics> getRelatedtopics() {
		return relatedtopics;
	}

	public boolean isEnabletag() {
		return enabletag;
	}

	public Map<Integer, Integer> getDebateList() {
		return debateList;
	}

	public Debates getDebateexpand() {
		return debateexpand;
	}

	public boolean isIsenddebate() {
		return isenddebate;
	}

	public Topics getTopic() {
		return topic;
	}

}
