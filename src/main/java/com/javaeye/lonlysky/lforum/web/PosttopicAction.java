package com.javaeye.lonlysky.lforum.web;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.struts2.config.ParentPackage;
import org.springframework.beans.factory.annotation.Autowired;

import com.javaeye.lonlysky.lforum.ForumBaseAction;
import com.javaeye.lonlysky.lforum.comm.ForumAction;
import com.javaeye.lonlysky.lforum.comm.LForumRequest;
import com.javaeye.lonlysky.lforum.comm.MD5;
import com.javaeye.lonlysky.lforum.comm.utils.ForumUtils;
import com.javaeye.lonlysky.lforum.comm.utils.Utils;
import com.javaeye.lonlysky.lforum.entity.forum.Admingroups;
import com.javaeye.lonlysky.lforum.entity.forum.AttachmentInfo;
import com.javaeye.lonlysky.lforum.entity.forum.Debates;
import com.javaeye.lonlysky.lforum.entity.forum.Forums;
import com.javaeye.lonlysky.lforum.entity.forum.Postid;
import com.javaeye.lonlysky.lforum.entity.forum.Posts;
import com.javaeye.lonlysky.lforum.entity.forum.Smilies;
import com.javaeye.lonlysky.lforum.entity.forum.Topics;
import com.javaeye.lonlysky.lforum.entity.forum.Topictypes;
import com.javaeye.lonlysky.lforum.entity.forum.UserExtcreditsInfo;
import com.javaeye.lonlysky.lforum.entity.forum.Users;
import com.javaeye.lonlysky.lforum.service.AdminGroupManager;
import com.javaeye.lonlysky.lforum.service.AttachmentManager;
import com.javaeye.lonlysky.lforum.service.ForumManager;
import com.javaeye.lonlysky.lforum.service.ModeratorManager;
import com.javaeye.lonlysky.lforum.service.PollManager;
import com.javaeye.lonlysky.lforum.service.PostManager;
import com.javaeye.lonlysky.lforum.service.ScoresetManager;
import com.javaeye.lonlysky.lforum.service.TopicAdminManager;
import com.javaeye.lonlysky.lforum.service.TopicManager;
import com.javaeye.lonlysky.lforum.service.UserCreditManager;

/**
 * 发表主题页面
 * 
 * @author 黄磊
 *
 */
@ParentPackage("default")
public class PosttopicAction extends ForumBaseAction {

	private static final long serialVersionUID = 7319080946520087809L;

	/**
	 * 所属版块名称
	 */
	private String forumname;

	/**
	 * 所属板块Id
	 */
	private int forumid;

	/**
	 * 主题内容
	 */
	private String message;

	/**
	 * 是否允许发表主题
	 */
	private boolean allowposttopic;

	/**
	 * 表情Javascript数组
	 */
	private String smilies;

	/**
	 * 主题图标
	 */
	private String topicicons;

	/**
	 * 论坛导航信息
	 */
	private String forumnav = "";

	/**
	 * 编辑器自定义按钮
	 */
	private String customeditbuttons;

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
	 * 是否受发贴灌水限制
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
	 * 积分策略信息
	 */
	private UserExtcreditsInfo userextcreditsinfo;

	/**
	 * 最高售价
	 */
	private int maxprice;

	/**
	 * 所属版块信息
	 */
	private Forums forum;

	/**
	 * 主题分类选项字串
	 */
	private String topictypeselectoptions;

	/**
	 * 表情列表
	 */
	private List<Smilies> smilietypes;

	/**
	 * 是否允许上传附件
	 */
	private boolean canpostattach;

	/**
	 * 是否允许同时发布到相册
	 */
	private boolean caninsertalbum;

	/**
	 * 交易积分
	 */
	private int creditstrans;

	/**
	 * 投票截止时间
	 */
	private String enddatetime = Utils.getNowShortDate();

	/**
	 * 是否允许Html标题
	 */
	private boolean canhtmltitle = false;

	/**
	 * 第一页表情的JSON
	 */
	private String firstpagesmilies = "";

	/**
	 * 本版是否可用Tag
	 */
	private boolean enabletag = false;

	/**
	 * 发帖的类型，如普通帖、悬赏帖等。。
	 */
	private String type = "";

	/**
	 * 当前登录用户的交易积分值, 仅悬赏帖时有效
	 */
	private float mycurrenttranscredits;

	/**
	 * 是否需要登录
	 */
	private boolean needlogin = false;

	private File[] postfiles = new File[0];
	private String[] postfileFileNames = new String[0];
	private String[] postfileContentTypes = new String[0];

	@Autowired
	private ForumManager forumManager;

	@Autowired
	private AttachmentManager attachmentManager;

	@Autowired
	private ScoresetManager scoresetManager;

	@Autowired
	private AdminGroupManager adminGroupManager;

	@Autowired
	private PostManager postManager;

	@Autowired
	private TopicManager topicManager;

	@Autowired
	private PollManager pollManager;

	@Autowired
	private ModeratorManager moderatorManager;

	@Autowired
	private UserCreditManager userCreditManager;

	@Autowired
	private TopicAdminManager topicAdminManager;

	@Override
	public String execute() throws Exception {
		// 临时账号发帖
		int realuserid = -1;
		String tempusername = LForumRequest.getParamValue("tempusername");
		if (!tempusername.equals("") && !tempusername.equals(username)) {
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

		canhtmltitle = config.getHtmltitle() == 1 && Utils.inArray(usergroupid + "", config.getHtmltitleusergroup());
		firstpagesmilies = cachesManager.getSmiliesFirstPageCache();
		boolean createpoll = false;
		String[] pollitem = {};

		//内容设置为空;  
		message = "";
		maxprice = usergroupinfo.getMaxprice();

		forumid = LForumRequest.getParamIntValue("forumid", -1);

		allowposttopic = true;

		if (forumid == -1) {
			allowposttopic = false;
			reqcfg.addErrLine("错误的论坛ID");
			forumnav = "";
			return SUCCESS;
		} else {
			forum = forumManager.getForumInfo(forumid);
			if (forum == null || forum.getLayer() == 0) {
				allowposttopic = false;
				reqcfg.addErrLine("错误的论坛ID");
				forumnav = "";
				return SUCCESS;
			}
			forumname = forum.getName();
			pagetitle = Utils.cleanHtmlTag(forum.getName());
			forumnav = forum.getPathlist().trim();
			enabletag = (config.getEnabletag() & forum.getAllowtag()) == 1;
			if (forum.getForumfields().getApplytopictype() == 1) //启用主题分类
			{
				topictypeselectoptions = forumManager.getCurrentTopicTypesOption(forum.getFid(), forum.getForumfields()
						.getTopictypes());
			}
		}

		//得到用户可以上传的文件类型
		StringBuilder sbAttachmentTypeSelect = new StringBuilder();
		if (!usergroupinfo.getAttachextensions().trim().equals("")) {
			sbAttachmentTypeSelect.append("id in (");
			sbAttachmentTypeSelect.append(usergroupinfo.getAttachextensions());
			sbAttachmentTypeSelect.append(")");
		}

		if (!forum.getForumfields().getAttachextensions().equals("")) {
			if (sbAttachmentTypeSelect.length() > 0) {
				sbAttachmentTypeSelect.append(" and ");
			}
			sbAttachmentTypeSelect.append("id in (");
			sbAttachmentTypeSelect.append(forum.getForumfields().getAttachextensions());
			sbAttachmentTypeSelect.append(")");
		}
		attachextensions = attachmentManager.getAttachmentTypeArray(sbAttachmentTypeSelect.toString());
		attachextensionsnosize = attachmentManager.getAttachmentTypeString(sbAttachmentTypeSelect.toString());

		//得到今天允许用户上传的附件总大小(字节)
		int maxTodaySize = 0;
		if (userid > 0) {
			maxTodaySize = attachmentManager.getUploadFileSizeByuserid(userid); //今天已上传大小 
		}
		attachsize = usergroupinfo.getMaxsizeperday() - maxTodaySize; //今天可上传得大小 

		StringBuilder sb = new StringBuilder();
		parseurloff = 0;

		smileyoff = 1 - forum.getAllowsmilies();

		bbcodeoff = 1;
		if (forum.getAllowbbcode() == 1 && usergroupinfo.getAllowcusbbcode() == 1) {
			bbcodeoff = 0;
		}

		usesig = ForumUtils.getCookie("sigstatus").equals("0") ? 0 : 1;

		allowimg = forum.getAllowimgcode();

		//　如果当前用户非管理员并且论坛设定了禁止发帖时间段，当前时间如果在其中的一个时间段内，不允许用户发帖
		if (useradminid != 1 && usergroupinfo.getDisableperiodctrl() != 1) {
			String visittime = scoresetManager.betweenTime(config.getPostbanperiods());
			if (!visittime.equals("")) {
				reqcfg.addErrLine("在此时间段( " + visittime + " )内用户不可以发帖");
				return SUCCESS;
			}
		}

		if (!forum.getForumfields().getPassword().trim().equals("")
				&& !MD5.encode(forum.getForumfields().getPassword().trim()).equals(
						ForumUtils.getCookie("forum" + forumid + "password"))) {
			reqcfg.addErrLine("本版块被管理员设置了密码").setBackLink("showforum.action?forumid=" + forumid);
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

		if (!forumManager.allowPostByUserID(forum.getForumfields().getPermuserlist(), userid)) //判断当前用户在当前版块发主题权限
		{
			if (Utils.null2String(forum.getForumfields().getPostperm()).equals(""))//权限设置为空时，根据用户组权限判断
			{
				// 验证用户是否有发表主题的权限
				if (usergroupinfo.getAllowpost() != 1) {
					reqcfg.addErrLine("您当前的身份 \"" + usergroupinfo.getGrouptitle() + "\" 没有发表主题的权限");
					needlogin = true;
					return SUCCESS;
				}
			} else//权限设置不为空时,根据板块权限判断
			{
				if (!forumManager.allowPost(forum.getForumfields().getPostperm(), usergroupid)) {
					reqcfg.addErrLine("您没有在该版块发表主题的权限");
					needlogin = true;
					return SUCCESS;
				}
			}
		}

		//是否有上传附件的权限
		if (forumManager.allowPostAttachByUserID(forum.getForumfields().getPermuserlist(), userid)) {
			canpostattach = true;
		} else {
			if (forum.getForumfields().getPostattachperm().equals("")) {
				if (usergroupinfo.getAllowpostattach() == 1) {
					canpostattach = true;
				}
			} else {
				if (forumManager.allowPostAttach(forum.getForumfields().getPostattachperm(), usergroupid)) {
					canpostattach = true;
				}
			}
		}

		Users user = userManager.getUserInfo(userid);

		// 如果是受灌水限制用户, 则判断是否是灌水
		Admingroups admininfo = adminGroupManager.getAdminGroup(useradminid);

		disablepost = 0;
		if (admininfo != null) {
			disablepost = admininfo.getDisablepostctrl();
		}
		if (admininfo == null || admininfo.getDisablepostctrl() != 1) {

			int interval = Utils.null2Int(Utils.howLong("s", lastposttime, Utils.getNowTime()), config
					.getPostinterval());
			if (interval < 0) {
				reqcfg.addErrLine("系统规定发帖间隔为" + config.getPostinterval() + "秒, 您还需要等待 " + (interval * -1) + " 秒");
				return SUCCESS;
			} else if (userid != -1) {
				String joindate = userManager.getUserJoinDate(userid);
				if (joindate.equals("")) {
					reqcfg.addErrLine("您的用户资料出现错误");
					return SUCCESS;
				}

				interval = Utils.null2Int(Utils.howLong("m", joindate, Utils.getNowTime()), config.getNewbiespan());
				if (interval < 0) {
					reqcfg.addErrLine("系统规定新注册用户必须要在" + config.getNewbiespan() + "分钟后才可以发帖, 您还需要等待 " + (interval * -1)
							+ " 分");
					return SUCCESS;
				}

			}
		}

		creditstrans = scoresetManager.getCreditsTrans();
		userextcreditsinfo = scoresetManager.getScoreSet(creditstrans);

		type = LForumRequest.getParamValue("type").toLowerCase();

		if (forum.getAllowspecialonly() > 0 && type.equals("")) {
			reqcfg.addErrLine("当前版块 \"" + forum.getName() + "\" 不允许发表普通主题");
			return SUCCESS;
		}

		if (forum.getAllowpostspecial() > 0) {
			if (type.equals("poll") && (forum.getAllowpostspecial() & 1) != 1) {
				reqcfg.addErrLine("当前版块 \"" + forum.getName() + "\" 不允许发表投票");
				return SUCCESS;
			}

			if (type.equals("bonus") && (forum.getAllowpostspecial() & 4) != 4) {
				reqcfg.addErrLine("当前版块 \"" + forum.getName() + "\" 不允许发表悬赏");
				return SUCCESS;
			}
			if (type.equals("debate") && (forum.getAllowpostspecial() & 16) != 16) {
				reqcfg.addErrLine("当前版块 \"" + forum.getName() + "\" 不允许发表辩论");
				return SUCCESS;
			}
		}

		// 验证用户是否有发布投票的权限
		if (type.equals("poll") && usergroupinfo.getAllowpostpoll() != 1) {
			reqcfg.addErrLine("您当前的身份 \"" + usergroupinfo.getGrouptitle() + "\" 没有发布投票的权限");
			needlogin = true;
			return SUCCESS;
		}

		// 验证用户是否有发布悬赏的权限
		if (type.equals("bonus") && usergroupinfo.getAllowbonus() != 1) {
			reqcfg.addErrLine("您当前的身份 \"" + usergroupinfo.getGrouptitle() + "\" 没有发布悬赏的权限");
			needlogin = true;
			return SUCCESS;
		}

		// 验证用户是否有发起辩论的权限
		if (type.equals("debate") && usergroupinfo.getAllowdebate() != 1) {
			reqcfg.addErrLine("您当前的身份 \"" + usergroupinfo.getGrouptitle() + "\" 没有发起辩论的权限");
			needlogin = true;
			return SUCCESS;
		}

		if (type.equals("bonus")) {
			//当“交易积分设置”有效时(1-8的整数):
			int creditTrans = scoresetManager.getCreditsTrans();
			if (creditTrans <= 0) {
				reqcfg.addErrLine("系统未设置\"交易积分设置\", 无法判断当前要使用的(扩展)积分字段, 暂时无法发布悬赏");
				return SUCCESS;
			}
			mycurrenttranscredits = userManager.getUserExtCredits(userid, creditTrans);
		}

		//如果不是提交...
		if (!ispost) {
			reqcfg.addLinkCss("templates/" + templatepath + "/editor.css", "css");

			smilies = cachesManager.getSmiliesCache();
			smilietypes = cachesManager.getSmilieTypesCache();
			customeditbuttons = cachesManager.getCustomEditButtonList();
			topicicons = "";
		} else {
			reqcfg.setBackLink("posttopic.action?forumid=" + forumid + "&restore=1&type=" + type + "");
			String postmessage = LForumRequest.getParamValue("message");
			ForumUtils.writeCookie("postmessage", postmessage);

			if (ForumUtils.isCrossSitePost()) {
				reqcfg
						.addErrLine("您的请求来路不正确，无法提交。如果您安装了某种默认屏蔽来路信息的个人防火墙软件(如 Norton Internet Security)，请设置其不要禁止来路信息后再试。");
				return SUCCESS;
			}

			if (forum.getForumfields().getApplytopictype() == 1 && forum.getForumfields().getPostbytopictype() == 1
					&& !topictypeselectoptions.equals("")) {
				if (LForumRequest.getParamValue("typeid").equals("")) {
					reqcfg.addErrLine("主题类型不能为空");
				}
				//检测所选主题分类是否有效
				if (!forumManager.isCurrentForumTopicType(LForumRequest.getParamValue("typeid"), forum.getForumfields()
						.getTopictypes())) {
					reqcfg.addErrLine("错误的主题类型");
				}
			}
			if (LForumRequest.getParamValue("title").equals("")) {
				reqcfg.addErrLine("标题不能为空");
			} else if (LForumRequest.getParamValue("title").indexOf("　") != -1) {
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

			// 如果用户上传了附件,则检测用户是否有上传附件的权限
			if (postfiles.length > 0) {
				if (attachmentManager.getAttachmentTypeArray(sbAttachmentTypeSelect.toString()).trim().equals("")) {
					reqcfg.addErrLine("系统不允许上传附件");
				}

				if (!forumManager.allowPostAttachByUserID(forum.getForumfields().getPermuserlist(), userid)) {
					if (!forumManager.allowPostAttach(forum.getForumfields().getPostattachperm(), usergroupid)) {
						reqcfg.addErrLine("您没有在该版块上传附件的权限");
					} else if (usergroupinfo.getAllowpostattach() != 1) {
						reqcfg.addErrLine("您当前的身份 \"" + usergroupinfo.getGrouptitle() + "\" 没有上传附件的权限");
					}
				}
			}

			if (!LForumRequest.getParamValue("createpoll").equals("")) {
				// 验证用户是否有发布投票的权限
				if (usergroupinfo.getAllowpostpoll() != 1) {
					reqcfg.addErrLine("您当前的身份 \"" + usergroupinfo.getGrouptitle() + "\" 没有发布投票的权限");
					return SUCCESS;
				}

				createpoll = true;
				pollitem = LForumRequest.getParamValue("PollItemname").split("\r\n");
				if (pollitem.length < 2) {
					reqcfg.addErrLine("投票项不得少于2个");
				} else if (pollitem.length > config.getMaxpolloptions()) {
					reqcfg.addErrLine("系统设置为投票项不得多于" + config.getMaxpolloptions() + "个");
				} else {
					for (int i = 0; i < pollitem.length; i++) {
						if (pollitem[i].trim().equals("")) {
							reqcfg.addErrLine("投票项不能为空");
						}
					}
				}

				enddatetime = LForumRequest.getParamValue("enddatetime");
				if (!Utils.checkDate(enddatetime, Utils.SHORT_DATEFORMAT)) {
					reqcfg.addErrLine("投票结束日期格式错误");
				}
			}

			boolean isbonus = type.equals("bonus");

			int topicprice = 0;
			String tmpprice = LForumRequest.getParamValue("topicprice");

			if (Pattern.matches("^[0-9]*[0-9][0-9]*$", tmpprice) || tmpprice.equals("")) {
				if (!isbonus) {
					topicprice = Utils.null2Int(tmpprice, 0);

					if (topicprice > maxprice && maxprice > 0) {
						if (userextcreditsinfo.getUnit().equals("")) {
							reqcfg.addErrLine("主题售价不能高于 " + maxprice + " " + userextcreditsinfo.getName());
						} else {
							reqcfg.addErrLine("主题售价不能高于 " + maxprice + " " + userextcreditsinfo.getName() + "("
									+ userextcreditsinfo.getUnit() + ")");
						}
					} else if (topicprice > 0 && maxprice <= 0) {
						reqcfg.addErrLine("您当前的身份 \"" + usergroupinfo.getGrouptitle() + "\" 未被允许出售主题");
					} else if (topicprice < 0) {
						reqcfg.addErrLine("主题售价不能为负数");
					}
				} else {
					topicprice = Utils.null2Int(tmpprice, 0);

					if (usergroupinfo.getAllowbonus() == 0) {
						reqcfg.addErrLine("您当前的身份 \"" + usergroupinfo.getGrouptitle() + "\" 未被允许进行悬赏");
					}

					if (topicprice < usergroupinfo.getMinbonusprice() || topicprice > usergroupinfo.getMaxbonusprice()) {
						reqcfg.addErrLine("悬赏价格超出范围, 您应在 " + usergroupinfo.getMinbonusprice() + " - "
								+ usergroupinfo.getMaxbonusprice() + " " + userextcreditsinfo.getUnit()
								+ userextcreditsinfo.getName() + " 范围内进行悬赏");
					}
				}
			} else {
				if (!isbonus) {
					reqcfg.addErrLine("主题售价只能为整数");
				} else {
					reqcfg.addErrLine("悬赏价格只能为整数");
				}
			}

			String positiveopinion = LForumRequest.getParamValue("positiveopinion");
			String negativeopinion = LForumRequest.getParamValue("negativeopinion");
			String terminaltime = LForumRequest.getParamValue("terminaltime");

			if (type.equals("debate")) {
				if (usergroupinfo.getAllowdebate() != 1) {
					reqcfg.addErrLine("您当前的身份 \"" + usergroupinfo.getGrouptitle() + "\" 没有发起辩论的权限");
					return SUCCESS;

				}

				if (positiveopinion.equals("")) {
					reqcfg.addErrLine("正方观点不能为空");
				}
				if (negativeopinion.equals("")) {
					reqcfg.addErrLine("反方观点不能为空");
				}
				if (!Utils.checkDate(terminaltime, Utils.SHORT_DATEFORMAT)) {
					reqcfg.addErrLine("结束日期格式不正确");
				}

			}

			if (reqcfg.isErr()) {
				return SUCCESS;
			}

			int iconid = LForumRequest.getParamIntValue("iconid", 0);
			if (iconid > 15 || iconid < 0) {
				iconid = 0;
			}
			int hide = 0;
			if (ForumUtils.isHidePost(postmessage) && usergroupinfo.getAllowhidecode() == 1) {
				hide = 1;
			}

			String curdatetime = Utils.getNowTime();

			Topics topicinfo = new Topics();
			topicinfo.setForums(forum);
			topicinfo.setIconid(iconid);
			if (useradminid == 1) {
				topicinfo.setTitle(Utils.cleanHtmlTag(LForumRequest.getParamValue("title")));
				message = postmessage;
			} else {
				topicinfo.setTitle(cachesManager
						.banWordFilter(Utils.cleanHtmlTag(LForumRequest.getParamValue("title"))));
				message = cachesManager.banWordFilter(postmessage);
			}

			if (cachesManager.hasBannedWord(topicinfo.getTitle()) || cachesManager.hasBannedWord(message)) {
				reqcfg.addErrLine("对不起, 您提交的内容包含不良信息, 因此无法提交, 请返回修改!");
				return SUCCESS;
			}
			Topictypes topictypes = new Topictypes();
			topictypes.setTypeid(LForumRequest.getParamIntValue("typeid", 0));
			topicinfo.setTopictypes(topictypes);
			if (usergroupinfo.getAllowsetreadperm() == 1) {
				int topicreadperm = LForumRequest.getParamIntValue("topicreadperm", 0);
				topicreadperm = topicreadperm > 255 ? 255 : topicreadperm;
				topicinfo.setReadperm(topicreadperm);
			} else {
				topicinfo.setReadperm(0);
			}
			topicinfo.setPrice(topicprice);
			topicinfo.setPoster(username);
			topicinfo.setUsersByPosterid(user);
			topicinfo.setPostdatetime(curdatetime);
			topicinfo.setLastpost(curdatetime);
			topicinfo.setLastposter(username);
			topicinfo.setViews(0);
			topicinfo.setReplies(0);

			if (forum.getModnewposts() == 1 && useradminid != 1) {
				if (useradminid > 1) {
					if (disablepost == 1) {
						topicinfo.setDisplayorder(0);
					} else {
						topicinfo.setDisplayorder(-2);
					}
				} else {
					topicinfo.setDisplayorder(-2);
				}
			} else {
				topicinfo.setDisplayorder(0);
			}

			if (useradminid != 1) {
				if (!scoresetManager.betweenTime(config.getPostmodperiods()).equals("")
						|| cachesManager.hasAuditWord(topicinfo.getTitle()) || cachesManager.hasAuditWord(message)) {
					topicinfo.setDisplayorder(-2);
				}
			}

			topicinfo.setHighlight("");
			topicinfo.setDigest(0);
			topicinfo.setRate(0);
			topicinfo.setHide(hide);
			topicinfo.setAttachment(0);
			topicinfo.setModerated(0);
			topicinfo.setClosed(0);
			topicinfo.setMagic(0);
			topicinfo.setSpecial(0);

			String htmltitle = LForumRequest.getParamValue("htmltitle");
			if (!htmltitle.equals("") && !htmltitle.equals(topicinfo.getTitle())) {
				topicinfo.setMagic(11000);
				//按照  附加位/htmltitle(1位)/magic(3位)/以后扩展（未知位数） 的方式来存储
				//例： 11001
			}

			//标签(Tag)操作                
			String tags = LForumRequest.getParamValue("tags");
			String[] tagArray = null;
			if (enabletag && !tags.equals("")) {
				tagArray = tags.split(" ");
				if (tagArray.length > 0 && tagArray.length <= 5) {
					if (topicinfo.getMagic() == 0) {
						topicinfo.setMagic(10000);
					}
					topicinfo.setMagic(Utils.null2Int((topicinfo.getMagic().toString() + "1"), 0));
				} else {

					reqcfg.addErrLine("超过标签数的最大限制，最多可填写 5 个标签");
					return SUCCESS;
				}
			}

			if (isbonus) {
				topicinfo.setSpecial(2);
				//检查积分是否足够
				if (mycurrenttranscredits < topicprice) {
					reqcfg.addErrLine("您的积分不足, 无法进行悬赏");
					return SUCCESS;
				} else {
					userManager.updateUserExtCredits(userid, scoresetManager.getCreditsTrans(), -topicprice);
				}
			}

			if (type.equals("poll")) {
				topicinfo.setSpecial(1);
			}
			//辩论帖
			if (type.equals("debate")) {
				topicinfo.setSpecial(4);
			}

			topicManager.createTopic(topicinfo);
			//保存htmltitle
			if (canhtmltitle && !htmltitle.equals("") && !htmltitle.equals(topicinfo.getTitle())) {
				topicManager.writeHtmlTitleFile(htmltitle, topicinfo.getTid());
			}

			if (enabletag && tagArray != null && tagArray.length > 0) {
				if (cachesManager.hasBannedWord(tags)) {
					reqcfg.addErrLine("标签中含有系统禁止词语,请修改");
					return SUCCESS;
				}
				//				ForumTags.CreateTopicTags(tagArray, topicid, userid, curdatetime);
			}

			if (type.equals("debate")) {
				// 辩论主题扩展信息
				Debates debatetopic = new Debates();
				debatetopic.setPositivediggs(0);
				debatetopic.setNegativediggs(0);
				debatetopic.setTopics(topicinfo);
				debatetopic.setPositiveopinion(positiveopinion);
				debatetopic.setNegativeopinion(negativeopinion);
				debatetopic.setTerminaltime(LForumRequest.getParamValue("terminaltime"));
				topicManager.addDebateTopic(debatetopic);
			}

			Posts postinfo = new Posts();
			postinfo.setForums(forum);
			postinfo.setTopics(topicinfo);
			Postid postidByParentid = new Postid();
			postidByParentid.setPid(0);
			postinfo.setPostidByParentid(postidByParentid);
			postinfo.setLayer(0);
			postinfo.setPoster(username);
			postinfo.setUsers(user);
			if (useradminid == 1) {
				postinfo.setTitle(Utils.cleanHtmlTag(LForumRequest.getParamValue("title")));
			} else {
				postinfo
						.setTitle(cachesManager.banWordFilter(Utils.cleanHtmlTag(LForumRequest.getParamValue("title"))));
			}

			postinfo.setPostdatetime(curdatetime);
			postinfo.setMessage(message);
			postinfo.setIp(LForumRequest.getIp());
			postinfo.setLastedit("");

			if (cachesManager.hasAuditWord(postinfo.getMessage())) {
				postinfo.setInvisible(1);
			}

			if (forum.getModnewposts() == 1 && useradminid != 1) {
				if (useradminid > 1) {
					if (disablepost == 1) {
						postinfo.setInvisible(0);
					} else {
						postinfo.setInvisible(1);
					}
				} else {
					postinfo.setInvisible(1);
				}
			} else {
				postinfo.setInvisible(0);
			}
			//　如果当前用户非管理员并且论坛设定了发帖审核时间段，当前时间如果在其中的一个时间段内，则用户所发帖均为待审核状态
			if (useradminid != 1) {
				if (!scoresetManager.betweenTime(config.getPostmodperiods()).equals("")) {
					postinfo.setInvisible(0);
				}
			}

			postinfo.setUsesig(Utils.null2Int(LForumRequest.getParamValue("usesig"), 0));
			postinfo.setHtmlon(1);

			postinfo.setSmileyoff(smileyoff);
			if (smileyoff == 0 && forum.getAllowsmilies() == 1) {
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
			postinfo.setTitle(topicinfo.getTitle());

			try {
				postManager.createPost(postinfo);
			} catch (Exception e) {
				e.printStackTrace();
				topicAdminManager.deleteTopics(topicinfo.getTid().toString(), false); //删除主题
				reqcfg.addErrLine("帖子保存出现异常");
				return SUCCESS;
			}

			//设置用户的积分
			///首先读取版块内自定义积分
			///版设置了自定义积分则使用，否则使用论坛默认积分
			double[] values = null;
			if (!forum.getForumfields().getPostcredits().equals("")) {
				int index = 0;
				float tempval = 0;
				values = new double[8];
				for (String ext : forum.getForumfields().getPostcredits().split(",")) {
					if (index == 0) {
						if (!ext.equals("True")) {
							values = null;
							break;
						}
						index++;
						continue;
					}
					tempval = Utils.null2Float(ext, 0);
					values[index - 1] = tempval;
					index++;
					if (index > 8) {
						break;
					}
				}
			}

			StringBuilder itemvaluelist = new StringBuilder("");
			if (createpoll) {
				// 生成以回车换行符为分割的项目与结果列
				for (int i = 0; i < pollitem.length; i++) {
					itemvaluelist.append("0\r\n");
				}

				String pollItemname = LForumRequest.getParamValue("PollItemname");
				if (!pollItemname.equals("")) {
					int multiple = LForumRequest.getParamValue("multiple").equals("on") ? 1 : 0;
					int maxchoices = 0;
					if (multiple <= 0) {
						multiple = 0;
					}

					if (multiple == 1) {
						maxchoices = LForumRequest.getParamIntValue("maxchoices", 1);
						if (maxchoices > pollitem.length) {
							maxchoices = pollitem.length;
						}
					}

					if (!pollManager.createPoll(topicinfo, multiple, pollitem.length, pollItemname.trim(),
							itemvaluelist.toString().trim(), enddatetime + " 23:59:00", user, maxchoices, LForumRequest
									.getParamValue("visiblepoll") == "on" ? 1 : 0)) {
						reqcfg.addErrLine("投票错误");
						return SUCCESS;
					}
				} else {
					reqcfg.addErrLine("投票项为空");
					return SUCCESS;
				}
			}

			sb = new StringBuilder();
			sb.delete(0, sb.length());

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
				int errorAttachment = attachmentManager.bindAttachment(attachmentinfo, postinfo.getPostidByPid(), sb,
						topicinfo, user);
				int[] aid = attachmentManager.createAttachments(attachmentinfo);
				String tempMessage = attachmentManager.filterLocalTags(aid, attachmentinfo, postinfo.getMessage());

				if (!tempMessage.equals(postinfo.getMessage())) {
					postinfo.setMessage(tempMessage);
					postinfo.setPid(postinfo.getPid());
					postManager.updatePost(postinfo);
				}

				userCreditManager.updateUserCreditsByUploadAttachment(userid, aid.length - errorAttachment);
			}

			onlineUserManager.updateAction(olid, ForumAction.PostTopic.ACTION_ID, forumid, forumname, -1, "", config
					.getOnlinetimeout());
			// 更新在线表中的用户最后发帖时间
			onlineUserManager.updatePostTime(olid);

			if (sb.length() > 0) {
				reqcfg.setUrl("showtopic.action?topicid=" + topicinfo.getTid()).setMetaRefresh(5).setShowBackLink(true);
				sb
						.insert(
								0,
								"<table cellspacing=\"0\" cellpadding=\"4\" border=\"0\"><tr><td colspan=2 align=\"left\"><span class=\"bold\"><nobr>发表主题成功,但以下附件上传失败:</nobr></span><br /></td></tr>");
				sb.append("</table>");
				reqcfg.addMsgLine(sb.toString());
			} else {

				reqcfg.setShowBackLink(false);
				if (useradminid != 1) {
					boolean needaudit = false; //是否需要审核

					if (!scoresetManager.betweenTime(config.getPostmodperiods()).equals("")) {
						needaudit = true;
					} else {
						if (forum.getModnewposts() == 1 && useradminid != 1) {
							if (useradminid > 1) {
								if (disablepost == 1 && topicinfo.getDisplayorder() != -2) {
									if (useradminid == 3 && !moderatorManager.isModer(useradminid, userid, forumid)) {
										needaudit = true;
									} else {
										needaudit = false;
									}
								} else {
									needaudit = true;
								}
							} else {
								needaudit = true;
							}
						} else {
							if (useradminid != 1 && topicinfo.getDisplayorder() == -2) {
								needaudit = true;
							}
						}
					}
					if (needaudit) {
						reqcfg.setUrl("showforum.action?forumid=" + forumid).setMetaRefresh().addMsgLine(
								"发表主题成功, 但需要经过审核才可以显示. 返回该版块");
					} else {
						postTopicSucceed(values, topicinfo, topicinfo.getTid());
					}
				} else {
					postTopicSucceed(values, topicinfo, topicinfo.getTid());
				}
			}
			ForumUtils.writeCookie("postmessage", "");

			//如果已登录就不需要再登录
			if (needlogin && userid > 0)
				needlogin = false;
		}
		return SUCCESS;
	}

	/**
	 * 发帖成功
	 * @param values 版块积分设置
	 * @param topicinfo 主题信息
	 * @param topicid 主题ID
	 */
	private void postTopicSucceed(double[] values, Topics topicinfo, int topicid) {
		if (values != null) {
			///使用版块内积分	
			userCreditManager.updateUserCreditsByPostTopic(userid, values);
		} else {
			///使用默认积分
			userCreditManager.updateUserCreditsByPostTopic(userid);
		}
		reqcfg.setUrl(
				topicinfo.getSpecial() == 4 ? "showdebate.action?topicid=" + topicid : "showtopic.action?topicid="
						+ topicid).setMetaRefresh().addMsgLine(
				"发表主题成功, 返回该主题<br />(<a href=\"showforum.action?forumid=" + forumid + "\">点击这里返回 " + forumname
						+ "</a>)<br />");
	}

	public String getForumname() {
		return forumname;
	}

	public int getForumid() {
		return forumid;
	}

	public String getMessage() {
		return message;
	}

	public boolean isAllowposttopic() {
		return allowposttopic;
	}

	public String getSmilies() {
		return smilies;
	}

	public String getTopicicons() {
		return topicicons;
	}

	public String getForumnav() {
		return forumnav;
	}

	public String getCustomeditbuttons() {
		return customeditbuttons;
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

	public UserExtcreditsInfo getUserextcreditsinfo() {
		return userextcreditsinfo;
	}

	public int getMaxprice() {
		return maxprice;
	}

	public Forums getForum() {
		return forum;
	}

	public String getTopictypeselectoptions() {
		return topictypeselectoptions;
	}

	public List<Smilies> getSmilietypes() {
		return smilietypes;
	}

	public boolean isCanpostattach() {
		return canpostattach;
	}

	public boolean isCaninsertalbum() {
		return caninsertalbum;
	}

	public int getCreditstrans() {
		return creditstrans;
	}

	public String getEnddatetime() {
		return enddatetime;
	}

	public boolean isCanhtmltitle() {
		return canhtmltitle;
	}

	public String getFirstpagesmilies() {
		return firstpagesmilies;
	}

	public boolean isEnabletag() {
		return enabletag;
	}

	public String getType() {
		return type;
	}

	public float getMycurrenttranscredits() {
		return mycurrenttranscredits;
	}

	public boolean isNeedlogin() {
		return needlogin;
	}

	public ScoresetManager getScoresetManager() {
		return scoresetManager;
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
