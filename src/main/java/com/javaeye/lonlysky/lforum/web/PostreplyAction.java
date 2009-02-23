package com.javaeye.lonlysky.lforum.web;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import org.apache.struts2.config.ParentPackage;
import org.springframework.beans.factory.annotation.Autowired;

import com.javaeye.lonlysky.lforum.ForumBaseAction;
import com.javaeye.lonlysky.lforum.comm.ForumAction;
import com.javaeye.lonlysky.lforum.comm.LForumRequest;
import com.javaeye.lonlysky.lforum.comm.MD5;
import com.javaeye.lonlysky.lforum.comm.utils.ForumUtils;
import com.javaeye.lonlysky.lforum.comm.utils.UBBUtils;
import com.javaeye.lonlysky.lforum.comm.utils.Utils;
import com.javaeye.lonlysky.lforum.entity.forum.Admingroups;
import com.javaeye.lonlysky.lforum.entity.forum.AttachmentInfo;
import com.javaeye.lonlysky.lforum.entity.forum.Forums;
import com.javaeye.lonlysky.lforum.entity.forum.Postid;
import com.javaeye.lonlysky.lforum.entity.forum.PostpramsInfo;
import com.javaeye.lonlysky.lforum.entity.forum.Posts;
import com.javaeye.lonlysky.lforum.entity.forum.Smilies;
import com.javaeye.lonlysky.lforum.entity.forum.Topics;
import com.javaeye.lonlysky.lforum.entity.forum.Users;
import com.javaeye.lonlysky.lforum.service.AdminGroupManager;
import com.javaeye.lonlysky.lforum.service.AttachmentManager;
import com.javaeye.lonlysky.lforum.service.EditorManager;
import com.javaeye.lonlysky.lforum.service.ForumManager;
import com.javaeye.lonlysky.lforum.service.ModeratorManager;
import com.javaeye.lonlysky.lforum.service.PaymentLogManager;
import com.javaeye.lonlysky.lforum.service.PostManager;
import com.javaeye.lonlysky.lforum.service.ScoresetManager;
import com.javaeye.lonlysky.lforum.service.SmilieManager;
import com.javaeye.lonlysky.lforum.service.TemplateManager;
import com.javaeye.lonlysky.lforum.service.TopicManager;
import com.javaeye.lonlysky.lforum.service.UserCreditManager;

/**
 * 回复页面类
 * 
 * @author 黄磊
 *
 */
@ParentPackage("default")
public class PostreplyAction extends ForumBaseAction {

	private static final long serialVersionUID = 3805720031994011758L;

	/**
	 * 主题信息
	 */
	private Topics topic;

	/**
	 * 最后5条回复列表
	 */
	private List<Posts> lastpostlist;

	/**
	 * 所属版块Id
	 */
	private int forumid;

	/**
	 * 所属版块名称
	 */
	private String forumname;

	/**
	 * 主题Id
	 */
	private int topicid;

	/**
	 * 帖子Id
	 */
	private int postid;

	/**
	 * 回复内容
	 */
	private String message;

	/**
	 * 表情Javascript数组
	 */
	private String smilies;

	/**
	 * 主题标题
	 */
	private String topictitle = "";
	/**
	 * 回复标题
	 */
	private String replytitle;

	/**
	 * 论坛导航信息
	 */
	private String forumnav = "";

	/**
	 * 编辑器自定义按钮
	 */
	private String customeditbuttons;

	/**
	 * 主题图标
	 */
	private String topicicons;

	/**
	 * 是否解析网址
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
	 * 是否允许 [img] 代码
	 */
	private int allowimg;

	/**
	 * 是否受灌水限制
	 */
	private int disablepost;

	/**
	 * 允许的附件类型和大小数组
	 */
	private String attachextensions;

	/**
	 * 允许的附件类型
	 */
	private String attachextensionsnosize;

	/**
	 * 今天可上传附件大小
	 */
	private int attachsize;

	/**
	 * 继续进行回复
	 */
	private String continuereply = "";

	/**
	 * 所属版块信息
	 */
	private Forums forum;

	/**
	 * 表情分类
	 */
	private List<Smilies> smilietypes;

	/**
	 * 是否允许回复
	 */
	private boolean canreply = false;

	/**
	 * 是否允许上传附件
	 */
	private boolean canpostattach = false;

	/**
	 * 第一页表情的JSON
	 */
	private String firstpagesmilies = "";
	/**
	 * 是否需要登录
	 */
	private boolean needlogin = false;

	private File[] postfiles = new File[0];
	private String[] postfileFileNames = new String[0];
	private String[] postfileContentTypes = new String[0];

	@Autowired
	private AdminGroupManager adminGroupManager;

	@Autowired
	private ScoresetManager scoresetManager;

	@Autowired
	private PostManager postManager;

	@Autowired
	private TopicManager topicManager;

	@Autowired
	private ModeratorManager moderatorManager;

	@Autowired
	private ForumManager forumManager;

	@Autowired
	private AttachmentManager attachmentManager;

	@Autowired
	private TemplateManager templateManager;

	@Autowired
	private PaymentLogManager paymentLogManager;

	@Autowired
	private SmilieManager smilieManager;

	@Autowired
	private EditorManager editorManager;

	@Autowired
	private UserCreditManager userCreditManager;

	@Override
	public String execute() throws Exception {
		// 临时帐号发帖
		int realuserid = -1;
		String tempusername = LForumRequest.getParamValue("tempusername");
		if (!tempusername.equals("") && tempusername != username) {
			String temppassword = LForumRequest.getParamValue("temppassword");
			int question = LForumRequest.getParamIntValue("question", 0);
			String answer = LForumRequest.getParamValue("answer");
			// 检测临时用户账号密码
			if (config.getSecques() == 1) {
				realuserid = userManager.checkPasswordAndSecques(tempusername, temppassword, true, question, answer);
			} else {
				realuserid = userManager.checkPassword(tempusername, temppassword, true);
			}
			if (realuserid == -1) {
				reqcfg.addErrLine("临时帐号登录失败，无法继续发帖。");
				return SUCCESS;
			} else {
				userid = realuserid;
				username = tempusername;
				usergroupinfo = userGroupManager.getUsergroup(userManager.getUserInfo(userid).getUsergroups()
						.getGroupid());
				usergroupid = usergroupinfo.getGroupid();
				useradminid = userManager.getUserInfo(userid).getAdmingroups().getAdmingid();
			}
		}

		// 获取主题ID
		topicid = LForumRequest.getParamIntValue("topicid", -1);
		// 获取postid
		postid = LForumRequest.getParamIntValue("postid", -1);
		Posts postinfo = new Posts();
		int layer = 1;
		int parentid = 0;
		message = "";
		topictitle = "";
		replytitle = "";
		forumnav = "";
		firstpagesmilies = cachesManager.getSmiliesFirstPageCache();
		if (!LForumRequest.isPost()) {
			continuereply = LForumRequest.getParamValue("continuereply");
		}

		Admingroups admininfo = adminGroupManager.getAdminGroup(useradminid);
		disablepost = 0;
		//　如果当前用户非管理员并且论坛设定了禁止发帖时间段，当前时间如果在其中的一个时间段内，不允许用户发帖
		if (useradminid != 1 && usergroupinfo.getDisableperiodctrl() != 1) {
			String visittime = scoresetManager.betweenTime(config.getPostbanperiods());
			if (!visittime.equals("")) {
				reqcfg.addErrLine("在此时间段( " + visittime + " )内用户不可以发帖");
				return SUCCESS;
			}
		}

		if (postid != -1) {
			postinfo = postManager.getPostInfo(postid);
			if (postinfo == null) {
				reqcfg.addErrLine("无效的帖子ID");
				return SUCCESS;
			}
			if (topicid != postinfo.getTopics().getTid()) {
				reqcfg.addErrLine("主题ID无效");
				return SUCCESS;
			}

			layer = postinfo.getLayer() + 1;
			parentid = postinfo.getPostidByParentid().getPid();

			if (!LForumRequest.getParamValue("quote").equals("")) {
				if ((postinfo.getMessage().indexOf("[hide]") > -1) && (postinfo.getMessage().indexOf("[/hide]") > -1)) {
					message = "[quote] 原帖由 [b]" + postinfo.getPoster() + "[/b] 于 " + postinfo.getPostdatetime()
							+ " 发表\r\n ***隐藏帖*** [/quote]";
				} else {
					message = "[quote] 原帖由 [b]" + postinfo.getPoster() + "[/b] 于 " + postinfo.getPostdatetime()
							+ " 发表\r\n" + UBBUtils.clearAttachUBB(Utils.format(postinfo.getMessage(), 200, true))
							+ " [/quote]";
				}
			}
		} else {
			// 如果主题ID非数字
			if (topicid == -1) {
				reqcfg.addErrLine("无效的主题ID");
				return SUCCESS;
			}
		}

		// 获取该主题的信息
		topic = topicManager.getTopicInfo(topicid);
		// 如果该主题不存在
		if (topic == null) {
			reqcfg.addErrLine("不存在的主题ID");
			return SUCCESS;
		}

		topictitle = topic.getTitle().trim();
		replytitle = topictitle;
		if (replytitle.length() >= 50) {
			replytitle = Utils.substring(replytitle, 50) + "...";
		}

		pagetitle = topictitle.trim();
		forumid = topic.getForums().getFid();
		//　如果当前用户非管理员并且该主题已关闭，不允许用户发帖
		if (admininfo == null || !moderatorManager.isModer(admininfo.getAdmingid(), userid, forumid)) {
			if (topic.getClosed() == 1) {
				reqcfg.addErrLine("主题已关闭无法回复");
				return SUCCESS;
			}
		}

		forum = forumManager.getForumInfo(forumid);
		forumname = forum.getName().trim();
		forumnav = forum.getPathlist().trim();

		if (topic.getReadperm() > usergroupinfo.getReadaccess() && topic.getUsersByPosterid().getUid() != userid
				&& useradminid != 1
				&& !Utils.inArray(username, forum.getForumfields().getModerators().trim().split(","))) {
			reqcfg.addErrLine("本主题阅读权限为: " + topic.getReadperm() + ", 您当前的身份 \"" + usergroupinfo.getGrouptitle()
					+ "\" 阅读权限不够");
			return SUCCESS;
		}

		if (!ispost) {
			smilies = cachesManager.getSmiliesCache();
			smilietypes = cachesManager.getSmilieTypesCache();
			customeditbuttons = cachesManager.getCustomEditButtonList();
		}

		//得到用户可以上传的文件类型
		StringBuilder sbAttachmentTypeSelect = new StringBuilder();
		if (!usergroupinfo.getAttachextensions().trim().equals("")) {
			sbAttachmentTypeSelect.append("[id] in (");
			sbAttachmentTypeSelect.append(usergroupinfo.getAttachextensions());
			sbAttachmentTypeSelect.append(")");
		}

		if (!forum.getForumfields().getAttachextensions().trim().equals("")) {
			if (sbAttachmentTypeSelect.length() > 0) {
				sbAttachmentTypeSelect.append(" AND ");
			}
			sbAttachmentTypeSelect.append("[id] in (");
			sbAttachmentTypeSelect.append(forum.getForumfields().getAttachextensions());
			sbAttachmentTypeSelect.append(")");
		}
		attachextensions = attachmentManager.getAttachmentTypeArray(sbAttachmentTypeSelect.toString());
		attachextensionsnosize = attachmentManager.getAttachmentTypeString(sbAttachmentTypeSelect.toString());

		//得到今天允许用户上传的附件总大上(字节)
		int maxTodaySize = 0;
		if (userid > 0) {
			maxTodaySize = attachmentManager.getUploadFileSizeByuserid(userid); //今天已上传大小
		}
		attachsize = usergroupinfo.getMaxsizeperday() - maxTodaySize;

		StringBuilder builder = new StringBuilder();

		parseurloff = 0;

		smileyoff = 1 - forum.getAllowsmilies();

		bbcodeoff = 1;
		if (forum.getAllowbbcode() == 1 && usergroupinfo.getAllowcusbbcode() == 1) {
			bbcodeoff = 0;
		}

		usesig = ForumUtils.getCookie("sigstatus").equals("0") ? 0 : 1;

		allowimg = forum.getAllowimgcode();

		if (!forum.getForumfields().getPassword().trim().equals("")
				&& !MD5.encode(forum.getForumfields().getPassword().trim()).equals(
						ForumUtils.getCookie("forum" + forumid + "password"))) {
			reqcfg.addErrLine("本版块被管理员设置了密码");
			reqcfg.setBackLink("showforum.action?forumid=" + forumid);
			return SUCCESS;
		}

		if (!forumManager.allowViewByUserID(forum.getForumfields().getPermuserlist(), userid)) //判断当前用户在当前版块浏览权限
		{
			if (Utils.null2String(forum.getForumfields().getViewperm()).equals(""))//当板块权限为空时，按照用户组权限
			{
				if (usergroupinfo.getAllowvisit() != 1) {
					reqcfg.addErrLine("您当前的身份 \"" + usergroupinfo.getGrouptitle() + "\" 没有浏览该版块的权限");
					needlogin = true;
					return SUCCESS;
				}
			} else//当板块权限不为空，按照板块权限
			{
				if (!forumManager.allowView(forum.getForumfields().getViewperm(), usergroupid)) {
					reqcfg.addErrLine("您没有浏览该版块的权限");
					needlogin = true;
					return SUCCESS;
				}
			}
		}

		//是否有回复的权限
		if (!forumManager.allowReplyByUserID(forum.getForumfields().getPermuserlist(), userid)) {
			if (Utils.null2String(forum.getForumfields().getReplyperm()).equals(""))//当板块权限为空时根据用户组权限判断
			{
				// 验证用户是否有发表主题的权限
				if (usergroupinfo.getAllowreply() != 1) {
					reqcfg.addErrLine("您当前的身份 \"" + usergroupinfo.getGrouptitle() + "\" 没有发表回复的权限");
					needlogin = true;
					return SUCCESS;
				}
			} else//板块权限不为空时根据板块权限判断
			{
				if (!forumManager.allowReply(forum.getForumfields().getReplyperm(), usergroupid)) {
					reqcfg.addErrLine("您没有在该版块发表回复的权限");
					needlogin = true;
					return SUCCESS;
				}
			}
		}

		//是否有上传附件的权限
		if (forumManager.allowPostAttachByUserID(forum.getForumfields().getPermuserlist(), userid)) {
			canpostattach = true;
		} else {
			if (Utils.null2String(forum.getForumfields().getPostattachperm()).equals(""))//当板块权限为空时根据用户组权限判断
			{
				// 验证用户是否有上传附件的权限
				if (usergroupinfo.getAllowpostattach() == 1) {
					canpostattach = true;
				}
			} else//板块权限不为空时根据板块权限判断
			{
				if (forumManager.allowPostAttach(forum.getForumfields().getPostattachperm(), usergroupid)) {
					canpostattach = true;
				}
			}
		}

		Users user = userManager.getUserInfo(userid);

		// 如果是受灌水限制用户, 则判断是否是灌水
		if (admininfo != null) {
			disablepost = admininfo.getDisablepostctrl();
		}
		if (admininfo == null || admininfo.getDisablepostctrl() != 1) {

			int interval = Utils.strDateDiffSeconds(lastposttime, config.getPostinterval());
			if (interval < 0) {
				reqcfg.addErrLine("系统规定发帖间隔为" + config.getPostinterval() + "秒, 您还需要等待 " + (interval * -1) + " 秒");
				return SUCCESS;
			} else if (userid != -1) {
				String joindate = userManager.getUserJoinDate(userid);
				if (joindate == "") {
					reqcfg.addErrLine("您的用户资料出现错误");
					return SUCCESS;
				}

				interval = Utils.strDateDiffMinutes(joindate, config.getNewbiespan());
				if (interval < 0) {
					reqcfg.addErrLine("系统规定新注册用户必须要在" + config.getNewbiespan() + "分钟后才可以发帖, 您还需要等待 " + (interval * -1)
							+ " 分");
					return SUCCESS;
				}

			}
		}

		//如果不是提交...
		if (!ispost) {
			if (forum.getTemplateid() > 0) {
				templatepath = templateManager.getTemplateItem(forum.getTemplateid()).getDirectory();
			}

			reqcfg.addLinkCss("templates/" + templatepath + "/editor.css", "css");

			//判断是否为回复可见帖, hide=0为非回复可见(正常), hide > 0为回复可见, hide=-1为回复可见但当前用户已回复
			int hide = 0;
			if (topic.getHide() == 1) {
				hide = topic.getHide();
				if (postManager.isReplier(topicid, userid)) {
					hide = -1;
				}
			}
			//判断是否为回复可见帖, price=0为非购买可见(正常), price > 0 为购买可见, price=-1为购买可见但当前用户已购买
			int price = 0;
			if (topic.getPrice() > 0) {
				price = topic.getPrice();
				if (paymentLogManager.isBuyer(topicid, userid))//判断当前用户是否已经购买
				{
					price = -1;
				}
			}

			PostpramsInfo postpramsinfo = new PostpramsInfo();
			postpramsinfo.setFid(forum.getFid());
			postpramsinfo.setTid(topicid);
			postpramsinfo.setJammer(forum.getJammer());
			postpramsinfo.setPagesize(5);
			postpramsinfo.setPageindex(1);
			postpramsinfo.setGetattachperm(forum.getForumfields().getGetattachperm());
			postpramsinfo.setUsergroupid(usergroupid);
			postpramsinfo.setAttachimgpost(config.getAttachimgpost());
			postpramsinfo.setShowattachmentpath(config.getShowattachmentpath());
			postpramsinfo.setHide(hide);
			postpramsinfo.setPrice(price);
			postpramsinfo.setUbbmode(false);

			postpramsinfo.setShowimages(forum.getAllowimgcode());
			postpramsinfo.setSmiliesinfo(smilieManager.getSmiliesListWithInfo());
			postpramsinfo.setCustomeditorbuttoninfo(editorManager.getCustomEditButtonListWithInfo());
			postpramsinfo.setSmiliesmax(config.getSmiliesmax());
			postpramsinfo.setBbcodemode(0);

			lastpostlist = postManager.getLastPostList(postpramsinfo);
		} else {

			String backlink = "";
			if (LForumRequest.getParamIntValue("topicid", -1) > 0) {
				backlink = "postreply.action?topicid=" + topicid;
			} else {
				backlink = "postreply.aspx?postid=" + postid;
			}

			if (!LForumRequest.getParamValue("quote").equals("")) {
				backlink = backlink + "&quote=" + LForumRequest.getParamValue("quote");
			}
			backlink += "&restore=1";
			reqcfg.setBackLink(backlink);

			String postmessage = LForumRequest.getParamValue("message");

			if (ForumUtils.isCrossSitePost()) {
				reqcfg
						.addErrLine("您的请求来路不正确，无法提交。如果您安装了某种默认屏蔽来路信息的个人防火墙软件(如 Norton Internet Security)，请设置其不要禁止来路信息后再试。");
				return SUCCESS;
			}

			if (LForumRequest.getParamValue("title").indexOf("　") != -1) {
				reqcfg.addErrLine("标题不能包含全角空格符");
			} else if (LForumRequest.getParamValue("title").length() > 60) {
				reqcfg.addErrLine("标题最大长度为60个字符,当前为 " + LForumRequest.getParamValue("title").length() + " 个字符");
			}

			if (postmessage.equals("")) {
				reqcfg.addErrLine("内容不能为空");
			}

			if (admininfo != null && admininfo.getDisablepostctrl() != 1) {
				if (postmessage.length() < config.getMinpostsize()) {
					reqcfg.addErrLine("您发表的内容过少, 系统设置要求帖子内容不得少于 " + config.getMinpostsize() + " 字多于 "
							+ config.getMaxpostsize() + " 字");
				} else if (postmessage.length() > config.getMaxpostsize()) {
					reqcfg.addErrLine("您发表的内容过多, 系统设置要求帖子内容不得少于 " + config.getMinpostsize() + " 字多于 "
							+ config.getMaxpostsize() + " 字");
				}
			}

			if (topic.getSpecial() == 4 && LForumRequest.getParamIntValue("debateopinion", 0) == 0) {
				reqcfg.addErrLine("请选择您在辩论中的观点");
			}
			//            DebateInfo debateexpand = Debates.GetDebateTopic(topic.Tid);
			//            if (topic.Special == 4 && debateexpand.Terminaltime < DateTime.Now)
			//            {
			//                reqcfg.addErrLine("此辩论主题已经到期");
			//
			//            }

			if (reqcfg.isErr()) {
				return SUCCESS;
			}

			// 如果用户上传了附件,则检测用户是否有上传附件的权限			
			if (postfiles.length > 0) {

				if (!forumManager.allowPostAttachByUserID(forum.getForumfields().getPermuserlist(), userid)) {
					if (!forumManager.allowPostAttach(forum.getForumfields().getPostattachperm(), usergroupid)) {
						reqcfg.addErrLine("您没有在该版块上传附件的权限");
						return SUCCESS;
					} else if (usergroupinfo.getAllowpostattach() != 1) {
						reqcfg.addErrLine("您当前的身份 \"" + usergroupinfo.getGrouptitle() + "\" 没有上传附件的权限");
						return SUCCESS;
					}
				}
			}

			//新用户广告强力屏蔽检查

			if ((config.getDisablepostad() == 1) && useradminid < 1 || userid == -1) //如果开启新用户广告强力屏蔽检查或是游客
			{
				Calendar calendar = Calendar.getInstance();
				calendar.add(Calendar.MINUTE, -config.getDisablepostadregminute());
				if (userid == -1
						|| (config.getDisablepostadpostcount() != 0 && user.getPosts() <= config
								.getDisablepostadpostcount())
						|| (config.getDisablepostadregminute() != 0 && calendar.getTime().before(
								new SimpleDateFormat(Utils.SHORT_DATEFORMAT).parse(user.getJoindate())))) {

					for (String regular : config.getDisablepostadregular().replace("\r", "").split("\n")) {
						if (postManager.isAD(regular, LForumRequest.getParamValue("title"), postmessage)) {
							reqcfg.addErrLine("发帖失败，内容中似乎有广告信息，请检查标题和内容，如有疑问请与管理员联系");
							return SUCCESS;
						}
					}

				}
			}

			if (reqcfg.isErr()) {
				return SUCCESS;
			}

			int iconid = LForumRequest.getParamIntValue("iconid", 0);
			if (iconid > 15) {
				iconid = 0;
			}

			int hide = 0;
			if (ForumUtils.isHidePost(postmessage) && usergroupinfo.getAllowhidecode() == 1) {
				hide = 1;
			}

			String curdatetime = Utils.getNowTime();

			postinfo = new Posts();
			postinfo.setForums(forum);
			postinfo.setTopics(topic);
			Postid postidByParentid = new Postid();
			postidByParentid.setPid(parentid);
			postinfo.setPostidByParentid(postidByParentid);
			postinfo.setLayer(layer);
			postinfo.setPoster(username);
			postinfo.setUsers(user);

			if (useradminid == 1) {
				postinfo.setTitle(LForumRequest.getParamValue("title"));
				postinfo.setMessage(postmessage);
			} else {
				postinfo.setTitle(cachesManager.banWordFilter(LForumRequest.getParamValue("title")));
				postinfo.setMessage(cachesManager.banWordFilter(postmessage));
			}
			postinfo.setPostdatetime(curdatetime);

			if (cachesManager.hasBannedWord(postinfo.getTitle()) || cachesManager.hasBannedWord(postinfo.getMessage())) {
				reqcfg.addErrLine("对不起, 您提交的内容包含不良信息, 因此无法提交, 请返回修改!");
				return SUCCESS;
			}

			postinfo.setIp(LForumRequest.getIp());
			postinfo.setLastedit("");
			//postinfo.setDebateopinion = LForumRequest.getParamIntValue("debateopinion", 0);
			if (forum.getModnewposts() == 1 && useradminid != 1 && useradminid != 2) {
				postinfo.setInvisible(1);
			} else {
				postinfo.setInvisible(0);
			}

			//　如果当前用户非管理员并且论坛设定了发帖审核时间段，当前时间如果在其中的一个时间段内，则用户所发帖均为待审核状态
			if (useradminid != 1) {
				if (!scoresetManager.betweenTime(config.getPostmodperiods()).equals("")) {
					postinfo.setInvisible(1);
				}

				if (cachesManager.hasAuditWord(postinfo.getTitle())
						|| cachesManager.hasAuditWord(postinfo.getMessage())) {
					postinfo.setInvisible(1);
				}
			}

			postinfo.setUsesig(LForumRequest.getParamIntValue("usesig", 0));
			postinfo.setHtmlon(1);

			postinfo.setSmileyoff(smileyoff);
			if (smileyoff == 0) {
				postinfo.setSmileyoff(LForumRequest.getParamIntValue("smileyoff", 0));
			}

			postinfo.setBbcodeoff(1);
			if (usergroupinfo.getAllowcusbbcode() == 1 && forum.getAllowbbcode() == 1) {
				postinfo.setBbcodeoff(LForumRequest.getParamIntValue("bbcodeoff", 0));
			}
			postinfo.setParseurloff(LForumRequest.getParamIntValue("parseurloff", 0));
			postinfo.setAttachment(0);
			postinfo.setRate(0);
			postinfo.setRatetimes(0);

			// 产生新帖子
			postManager.createPost(postinfo);
			postid = postinfo.getPid();
			int pid = postid;

			if (hide == 1) {
				topic.setHide(hide);
				topicManager.updateTopicHide(topicid);
			}
			//topicManager.updateTopicReplies(topicid);
			topicManager.addParentForumTopics(forum.getParentidlist().trim(), 0, 1);
			//设置用户的积分
			///首先读取版块内自定义积分
			///版设置了自定义积分则使用，否则使用论坛默认积分
			double[] values = null;
			if (!forum.getForumfields().getReplycredits().equals("")) {
				int index = 0;
				float tempval = 0;
				values = new double[8];
				for (String ext : forum.getForumfields().getReplycredits().split(",")) {

					if (index == 0) {
						if (!ext.equals("True")) {
							values = null;
							break;
						}
						index++;
						continue;
					}
					tempval = Utils.null2Float(ext, 0.0f);
					values[index - 1] = tempval;
					index++;
					if (index > 8) {
						break;
					}
				}
			}

			builder = new StringBuilder();
			builder.delete(0, builder.length());

			int watermarkstatus = config.getWatermarkstatus();
			if (forum.getDisablewatermark() == 1) {
				watermarkstatus = 0;
			}
			AttachmentInfo[] attachmentinfo = ForumUtils.saveRequestFiles(forumid, config.getMaxattachments(),
					usergroupinfo.getMaxsizeperday(), usergroupinfo.getMaxattachsize(), maxTodaySize, attachextensions,
					watermarkstatus, config, postfiles, postfileFileNames, postfileContentTypes);
			if (attachmentinfo != null) {
				if (attachmentinfo.length > config.getMaxattachments()) {
					reqcfg.addErrLine("系统设置为每个帖子附件不得多于" + config.getMaxattachments() + "个");
					return SUCCESS;
				}
				int errorAttachment = attachmentManager.bindAttachment(attachmentinfo, postinfo.getPostidByPid(),
						builder, topic, user);
				int[] aid = attachmentManager.createAttachments(attachmentinfo);
				String tempMessage = attachmentManager.filterLocalTags(aid, attachmentinfo, postinfo.getMessage());

				if (!tempMessage.equals(postinfo.getMessage())) {
					postinfo.setMessage(tempMessage);
					postinfo.setPid(postinfo.getPid());
					postManager.updatePost(postinfo);
				}

				userCreditManager.updateUserCreditsByUploadAttachment(userid, aid.length - errorAttachment);
			}

			if (topic.getSpecial() == 4) {
				//辩论地址
				reqcfg.setUrl("showdebate.action?topicid=" + topicid);
			} else {
				reqcfg.setUrl("showtopic.action?page=end&topicid=" + topicid + "#" + pid);

			}

			if (LForumRequest.getParamValue("continuereply").equals("on")) {
				reqcfg.setUrl("postreply.action?topicid=" + topicid + "&continuereply=yes");
			}

			onlineUserManager.updateAction(olid, ForumAction.PostReply.ACTION_ID, forumid, forumname, topicid,
					topictitle, config.getOnlinetimeout());
			// 更新在线表中的用户最后发帖时间
			onlineUserManager.updatePostTime(olid);

			if (builder.length() > 0) {
				updateUserCredits(values);
				reqcfg.setMetaRefresh(5).setShowBackLink(true);
				builder
						.insert(
								0,
								"<table cellspacing=\"0\" cellpadding=\"4\" border=\"0\"><tr><td colspan=2 align=\"left\"><span class=\"bold\"><nobr>发表主题成功,但以下附件上传失败:</nobr></span><br /></td></tr>");
				builder.append("</table>");
				reqcfg.addMsgLine(builder.toString());
			} else {
				reqcfg.setMetaRefresh();
				reqcfg.setShowBackLink(false);
				//上面已经进行用户组判断
				if (postinfo.getInvisible() == 1) {
					reqcfg.addMsgLine("发表回复成功, 但需要经过审核才可以显示. "
							+ (LForumRequest.getParamValue("continuereply").equals("on") ? "继续回复" : "返回该主题")
							+ "<br /><br />(<a href=\"showforum.action?forumid=" + forumid + "\">点击这里返回" + forumname
							+ "</a>)");
				} else {
					updateUserCredits(values);
					reqcfg.addMsgLine("发表回复成功, "
							+ (LForumRequest.getParamValue("continuereply").equals("on") ? "继续回复" : "返回该主题")
							+ "<br />(<a href=\"showforum.action?forumid=" + forumid + "\">点击这里返回" + forumname
							+ "</a>)<br />");
				}
			}

			//发送邮件通知
			if (LForumRequest.getParamValue("emailnotify").equals("on")) {
				// 邮件发送通知
			}
		}
		return SUCCESS;
	}

	/**
	 * 更新用户积分
	 * @param values 版块积分设置
	 */
	private void updateUserCredits(double[] values) {
		if (values != null) {
			///使用版块内积分
			userCreditManager.updateUserCreditsByPosts(userid, values);
		} else {
			///使用默认积分
			userCreditManager.updateUserCreditsByPosts(userid);
		}

	}

	public Topics getTopic() {
		return topic;
	}

	public List<Posts> getLastpostlist() {
		return lastpostlist;
	}

	public int getForumid() {
		return forumid;
	}

	public String getForumname() {
		return forumname;
	}

	public int getTopicid() {
		return topicid;
	}

	public int getPostid() {
		return postid;
	}

	public String getMessage() {
		return message;
	}

	public String getSmilies() {
		return smilies;
	}

	public String getTopictitle() {
		return topictitle;
	}

	public String getReplytitle() {
		return replytitle;
	}

	public String getForumnav() {
		return forumnav;
	}

	public String getCustomeditbuttons() {
		return customeditbuttons;
	}

	public String getTopicicons() {
		return topicicons;
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

	public int getDisablepost() {
		return disablepost;
	}

	public String getAttachextensions() {
		return attachextensions;
	}

	public String getAttachextensionsnosize() {
		return attachextensionsnosize;
	}

	public int getAttachsize() {
		return attachsize;
	}

	public String getContinuereply() {
		return continuereply;
	}

	public Forums getForum() {
		return forum;
	}

	public List<Smilies> getSmilietypes() {
		return smilietypes;
	}

	public boolean isCanreply() {
		return canreply;
	}

	public boolean isCanpostattach() {
		return canpostattach;
	}

	public String getFirstpagesmilies() {
		return firstpagesmilies;
	}

	public boolean isNeedlogin() {
		return needlogin;
	}

	public File[] getPostfile() {
		return postfiles;
	}

	public void setPostfile(File[] postfiles) {
		this.postfiles = postfiles;
	}

	public String[] getPostfileFileName() {
		return postfileFileNames;
	}

	public void setPostfileFileName(String[] postfileFileNames) {
		this.postfileFileNames = postfileFileNames;
	}

	public String[] getPostfileContentType() {
		return postfileContentTypes;
	}

	public void setPostfileContentType(String[] postfileContentTypes) {
		this.postfileContentTypes = postfileContentTypes;
	}

}
