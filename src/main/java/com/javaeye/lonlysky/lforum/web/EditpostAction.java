package com.javaeye.lonlysky.lforum.web;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.lang.time.DateUtils;
import org.apache.struts2.config.ParentPackage;
import org.springframework.beans.factory.annotation.Autowired;

import com.javaeye.lonlysky.lforum.ForumBaseAction;
import com.javaeye.lonlysky.lforum.comm.LForumRequest;
import com.javaeye.lonlysky.lforum.comm.MD5;
import com.javaeye.lonlysky.lforum.comm.utils.ForumUtils;
import com.javaeye.lonlysky.lforum.comm.utils.Utils;
import com.javaeye.lonlysky.lforum.entity.forum.Admingroups;
import com.javaeye.lonlysky.lforum.entity.forum.AttachmentInfo;
import com.javaeye.lonlysky.lforum.entity.forum.Attachments;
import com.javaeye.lonlysky.lforum.entity.forum.Forums;
import com.javaeye.lonlysky.lforum.entity.forum.PollOptionView;
import com.javaeye.lonlysky.lforum.entity.forum.Polls;
import com.javaeye.lonlysky.lforum.entity.forum.Postid;
import com.javaeye.lonlysky.lforum.entity.forum.Posts;
import com.javaeye.lonlysky.lforum.entity.forum.Smilies;
import com.javaeye.lonlysky.lforum.entity.forum.Topics;
import com.javaeye.lonlysky.lforum.entity.forum.Topictypes;
import com.javaeye.lonlysky.lforum.entity.forum.UserExtcreditsInfo;
import com.javaeye.lonlysky.lforum.entity.forum.Usergroups;
import com.javaeye.lonlysky.lforum.entity.forum.Users;
import com.javaeye.lonlysky.lforum.service.AdminGroupManager;
import com.javaeye.lonlysky.lforum.service.AttachmentManager;
import com.javaeye.lonlysky.lforum.service.ForumManager;
import com.javaeye.lonlysky.lforum.service.ModeratorManager;
import com.javaeye.lonlysky.lforum.service.PollManager;
import com.javaeye.lonlysky.lforum.service.PostManager;
import com.javaeye.lonlysky.lforum.service.ScoresetManager;
import com.javaeye.lonlysky.lforum.service.TopicManager;
import com.javaeye.lonlysky.lforum.service.UserCreditManager;

/**
 * 编辑帖子页面
 * 
 * @author 黄磊
 *
 */
@ParentPackage("default")
public class EditpostAction extends ForumBaseAction {

	private static final long serialVersionUID = -2806875093161751301L;

	/**
	 * 帖子所属版块Id
	 */
	private int forumid;

	/**
	 * 帖子所属版块名称
	 */
	private String forumname;

	/**
	 * 论坛导航信息
	 */
	private String forumnav;

	/**
	 * 帖子信息
	 */
	private Posts postinfo;

	/**
	 * 帖子所属主题信息
	 */
	private Topics topic;

	/**
	 * 投票选项列表
	 */
	private List<PollOptionView> polloptionlist;

	/**
	 * 投票帖类型
	 */
	private Polls pollinfo;

	/**
	 * 附件列表
	 */
	private List<Attachments> attachmentlist;
	/**
	 * 附件数
	 */
	private int attachmentcount;

	/**
	 * 投票截止时间
	 */
	//private String pollenddatetime;
	/**
	 * 帖子内容
	 */
	private String message;

	/**
	 * 表情的JavaScript数组
	 */
	private String smilies;

	/**
	 * 自定义编辑器按钮
	 */
	private String customeditbuttons;

	/**
	 * 主题图标
	 */
	private String topicicons;

	/**
	 * 是否进行URL解析
	 */
	private int parseurloff;

	/**
	 * 是否进行表情解析
	 */
	private int smileyoff;

	/**
	 * 是否进行LForum代码解析
	 */
	private int bbcodeoff;

	/**
	 * 是否使用签名
	 */
	private int usesig;

	/**
	 * 是否允许[img]代码
	 */
	private int allowimg;

	/**
	 * 是否受发帖控制限制
	 */
	private int disablepostctrl;

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
	 * 当前版块的主题类型选项
	 */
	private String topictypeselectoptions;

	/**
	 * 表情分类
	 */
	private List<Smilies> smilietypes;

	/**
	 * 是否允许上传附件
	 */
	private boolean canpostattach;

	/**
	 * 是否显示下载链接
	 */
	private boolean allowviewattach = false;

	/**
	 * 是否有Html标题的权限
	 */
	private boolean canhtmltitle = false;

	/**
	 * 当前帖的Html标题
	 */
	private String htmltitle = "";

	/**
	 * 第一页表情的JSON
	 */
	private String firstpagesmilies = "";

	/**
	 * 主题所用标签
	 */
	private String topictags = "";

	/**
	 * 本版是否启用了Tag
	 */
	private boolean enabletag = false;

	private File[] postfiles = new File[0];
	private String[] postfileFileNames = new String[0];
	private String[] postfileContentTypes = new String[0];

	// 是否允许编辑帖子, 初始false为不允许
	boolean alloweditpost = false;

	@Autowired
	private AdminGroupManager adminGroupManager;

	@Autowired
	private PostManager postManager;

	@Autowired
	private TopicManager topicManager;

	@Autowired
	private ForumManager forumManager;

	@Autowired
	private AttachmentManager attachmentManager;

	@Autowired
	private ModeratorManager moderatorManager;

	@Autowired
	private ScoresetManager scoresetManager;

	@Autowired
	private UserCreditManager userCreditManager;

	@Autowired
	private PollManager pollManager;

	@Override
	public String execute() throws Exception {
		forumnav = "";
		maxprice = usergroupinfo.getMaxprice();
		Admingroups admininfo = adminGroupManager.getAdminGroup(useradminid);
		firstpagesmilies = cachesManager.getSmiliesFirstPageCache();
		this.disablepostctrl = 0;
		if (admininfo != null) {
			disablepostctrl = admininfo.getDisablepostctrl();
		}

		int topicid = LForumRequest.getParamIntValue("topicid", -1);
		int postid = LForumRequest.getParamIntValue("postid", -1);

		if (userid == -1) {
			reqcfg.addErrLine("你尚未登录");
			return SUCCESS;
		}

		// 如果帖子ID非数字
		if (postid == -1) {
			//
			reqcfg.addErrLine("无效的帖子ID");

			return SUCCESS;
		}

		postinfo = postManager.getPostInfo(postid);

		// 如果帖子不存在
		if (postinfo == null) {
			//
			reqcfg.addErrLine("不存在的帖子ID");

			return SUCCESS;
		}

		// 获取主题ID
		if (topicid != postinfo.getTopics().getTid()) {
			reqcfg.addErrLine("主题ID无效");
			return SUCCESS;
		}

		// 如果主题ID非数字
		if (topicid == -1) {
			//
			reqcfg.addErrLine("无效的主题ID");

			return SUCCESS;
		}

		// 获取该主题的信息
		topic = topicManager.getTopicInfo(topicid);
		// 如果该主题不存在
		if (topic == null) {
			reqcfg.addErrLine("不存在的主题ID");

			return SUCCESS;
		}

		//非创始人且作者与当前编辑者不同时
		if (postinfo.getUsers().getUid() != userid) {
			//            if (postinfo.getUsers().getUid() == admin)
			//            {
			//                reqcfg.addErrLine("您无权编辑创始人的帖子");
			//                return SUCCESS;
			//            }
			if (postinfo.getUsers().getUid() != -1) {
				Usergroups postergroup = userGroupManager.getUsergroup(userManager.getUserInfo(
						postinfo.getUsers().getUid()).getUsergroups().getGroupid());
				if (postergroup.getAdmingroups().getAdmingid() > 0
						&& postergroup.getAdmingroups().getAdmingid() < useradminid) {
					reqcfg.addErrLine("您无权编辑更高权限人的帖子");
					return SUCCESS;
				}
			}
		}

		pagetitle = postinfo.getTitle();

		///得到所在版块信息
		forumid = topic.getForums().getFid();
		forum = forumManager.getForumInfo(forumid);

		// 如果该版块不存在
		if (forum == null) {
			reqcfg.addErrLine("版块已不存在");
			forum = new Forums();
			return SUCCESS;
		}

		if (!forum.getForumfields().getPassword().equals("")
				&& !MD5.encode(forum.getForumfields().getPassword().trim()).equals(
						ForumUtils.getCookie("forum" + forumid + "password"))) {
			reqcfg.addErrLine("本版块被管理员设置了密码").setBackLink("showforum.action?forumid=" + forumid);
			return SUCCESS;
		}

		forumname = forum.getName();
		forumnav = forum.getPathlist().trim();

		if (forum.getForumfields().getApplytopictype() == 1) //启用主题分类
		{
			topictypeselectoptions = forumManager.getCurrentTopicTypesOption(forum.getFid(), forum.getForumfields()
					.getTopictypes().trim());
		}

		//得到用户可以上传的文件类型
		StringBuilder sbAttachmentTypeSelect = new StringBuilder();
		if (!usergroupinfo.getAttachextensions().trim().equals("")) {
			sbAttachmentTypeSelect.append("id in (");
			sbAttachmentTypeSelect.append(usergroupinfo.getAttachextensions().trim());
			sbAttachmentTypeSelect.append(")");
		}

		if (!forum.getForumfields().getAttachextensions().trim().equals("")) {
			if (sbAttachmentTypeSelect.length() > 0) {
				sbAttachmentTypeSelect.append(" and ");
			}
			sbAttachmentTypeSelect.append("id in (");
			sbAttachmentTypeSelect.append(forum.getForumfields().getAttachextensions().trim());
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

		//-------------设置帖子的可用功能allhtml,smileyoff,parseurlof,bbcodeoff
		StringBuilder sb = new StringBuilder();

		parseurloff = 0;

		smileyoff = 1 - forum.getAllowsmilies();

		bbcodeoff = 1;
		if (forum.getAllowbbcode() == 1) {
			if (usergroupinfo.getAllowcusbbcode() == 1) {
				bbcodeoff = 0;
			}
		}

		usesig = 1;

		allowimg = forum.getAllowimgcode();

		parseurloff = postinfo.getParseurloff();

		if (!LForumRequest.isPost()) {
			smileyoff = postinfo.getSmileyoff();
		}

		bbcodeoff = 1;
		if (usergroupinfo.getAllowcusbbcode() == 1) {
			bbcodeoff = postinfo.getBbcodeoff();
		}
		usesig = postinfo.getUsesig();

		if (!forumManager.allowViewByUserID(forum.getForumfields().getPermuserlist().trim(), userid)) //判断当前用户在当前版块浏览权限
		{
			if (Utils.null2String(forum.getForumfields().getViewperm()).equals(""))//当板块权限为空时，按照用户组权限
			{
				if (usergroupinfo.getAllowvisit() != 1) {
					reqcfg.addErrLine("您当前的身份 \"" + usergroupinfo.getGrouptitle() + "\" 没有浏览该版块的权限");
					return SUCCESS;
				}
			} else//当板块权限不为空，按照板块权限
			{
				if (!forumManager.allowView(forum.getForumfields().getViewperm(), usergroupid)) {
					reqcfg.addErrLine("您没有浏览该版块的权限");
					return SUCCESS;
				}
			}
		}

		//当前用户是否有允许下载附件权限
		if (forumManager.allowGetAttachByUserID(forum.getForumfields().getPermuserlist(), userid)) {
			allowviewattach = true;
		} else {
			if (Utils.null2String(forum.getForumfields().getGetattachperm()).equals(""))//权限设置为空时，根据用户组权限判断
			{
				// 验证用户是否有有允许下载附件权限
				if (usergroupinfo.getAllowgetattach() == 1) {
					allowviewattach = true;
				}
			} else if (forumManager.allowGetAttach(forum.getForumfields().getGetattachperm(), usergroupid)) {
				allowviewattach = true;
			}
		}

		//是否有上传附件的权限
		if (forumManager.allowPostAttachByUserID(forum.getForumfields().getPermuserlist(), userid)) {
			canpostattach = true;
		} else {
			if (Utils.null2String(forum.getForumfields().getPostattachperm()).equals("")) {
				if (usergroupinfo.getAllowpostattach() == 1) {
					canpostattach = true;
				}
			} else {
				if (forumManager.allowPostAttach(forum.getForumfields().getPostattachperm(), usergroupid)) {
					canpostattach = true;
				}
			}
		}

		// 判断当前用户是否有修改权限
		// 检查是否具有版主的身份
		if (!moderatorManager.isModer(useradminid, userid, forumid)) {
			if (postinfo.getUsers().getUid() != userid) {
				reqcfg.addErrLine("你并非作者, 且你当前的身份 \"" + usergroupinfo.getGrouptitle() + "\" 没有修改该帖的权限");

				return SUCCESS;
			} else if (config.getEdittimelimit() > 0
					&& Utils.strDateDiffMinutes(postinfo.getPostdatetime(), config.getEdittimelimit()) > 0) {
				reqcfg.addErrLine("抱歉, 系统规定只能在帖子发表" + config.getEdittimelimit() + "分钟内才可以修改");
				return SUCCESS;

			}

		}

		userextcreditsinfo = scoresetManager.getScoreSet(scoresetManager.getCreditsTrans());

		if (topic.getSpecial() == 1 && postinfo.getLayer() == 0) {
			pollinfo = pollManager.getPollInfo(topicid);

			{
				polloptionlist = pollManager.getPollOptionList(topicid);
			}
		}
		if (postinfo.getLayer() == 0) {
			canhtmltitle = config.getHtmltitle() == 1
					&& Utils.inArray(usergroupid + "", config.getHtmltitleusergroup());
		}

		attachmentlist = attachmentManager.getAttachmentListByPid(postid);
		attachmentcount = attachmentlist.size();

		//        if (topicManager.getMagicValue(topic.Magic, MagicType.HtmlTitle) == 1)
		//        {
		//            htmltitle = Topics.GetHtmlTitle(topic.Tid).Replace("\"", "\\\"").Replace("'", "\\'");
		//        }

		enabletag = (config.getEnabletag() & forum.getAllowtag()) == 1;
		//        if (enabletag && Topics.GetMagicValue(topic.Magic, MagicType.TopicTag) == 1)
		//        {
		//            List<Tags> tags = ForumTags.GetTagsListByTopic(topic.Tid);
		//
		//            for (TagInfo tag in tags)
		//            {
		//                if (tag.Orderid > -1)
		//                {
		//                    topictags += string.Format(" {0}", tag.Tagname);
		//                }
		//            }
		//            topictags = topictags.trim();
		//        }
		Users user = userManager.getUserInfo(userid);
		if (!ispost) {
			reqcfg.addLinkCss("templates/" + templatepath + "/editor.css", "css");

			// 帖子内容
			message = postinfo.getMessage();

			smilies = cachesManager.getSmiliesCache();
			smilietypes = cachesManager.getSmilieTypesCache();
			customeditbuttons = cachesManager.getCustomEditButtonList();
			topicicons = "";
		} else {

			reqcfg.setBackLink("editpost.action?topicid=" + postinfo.getTopics().getTid() + "&postid="
					+ postinfo.getPid());

			if (ForumUtils.isCrossSitePost()) {
				reqcfg
						.addErrLine("您的请求来路不正确，无法提交。如果您安装了某种默认屏蔽来路信息的个人防火墙软件(如 Norton Internet Security)，请设置其不要禁止来路信息后再试。");
				return SUCCESS;
			}

			if (postinfo.getLayer() == 0 && forum.getForumfields().getApplytopictype() == 1
					&& forum.getForumfields().getPostbytopictype() == 1 && !topictypeselectoptions.equals("")) {
				if (LForumRequest.getParamValue("typeid").equals("")
						|| LForumRequest.getParamValue("typeid").equals("0")) {
					reqcfg.addErrLine("主题类型不能为空");
					return SUCCESS;
				}

				if (!forumManager.isCurrentForumTopicType(LForumRequest.getParamValue("typeid"), forum.getForumfields()
						.getTopictypes())) {
					reqcfg.addErrLine("错误的主题类型");
					return SUCCESS;
				}
			}

			///删除附件
			if (LForumRequest.getParamIntValue("isdeleteatt", 0) == 1) {
				int aid = LForumRequest.getParamIntValue("aid", 0);
				if (aid > 0) {
					attachmentManager.deleteAttachment(aid);
					attachmentlist = attachmentManager.getAttachmentListByPid(postid);
					attachmentcount = attachmentManager.getAttachmentCountByPid(postid);
				}

				reqcfg.addLinkCss("templates/" + templatepath + "/editor.css", "css");

				// 帖子内容
				message = postinfo.getMessage();

				smilies = cachesManager.getSmiliesCache();
				customeditbuttons = cachesManager.getCustomEditButtonList();
				topicicons = "";

				ispost = false;

				return SUCCESS;
			}

			if (LForumRequest.getParamValue("title").equals("") && postinfo.getLayer() == 0) {
				reqcfg.addErrLine("标题不能为空");
			} else if (LForumRequest.getParamValue("title").indexOf("　") != -1) {
				reqcfg.addErrLine("标题不能包含全角空格符");
			} else if (LForumRequest.getParamValue("title").length() > 60) {
				reqcfg.addErrLine("标题最大长度为60个字符,当前为 " + LForumRequest.getParamValue("title").length() + " 个字符");
			}

			String postmessage = LForumRequest.getParamValue("message");
			if (postmessage.equals("")) {
				reqcfg.addErrLine("内容不能为空");
			}

			if (admininfo != null && this.disablepostctrl != 1) {
				if (postmessage.length() < config.getMinpostsize()) {
					reqcfg.addErrLine("您发表的内容过少, 系统设置要求帖子内容不得少于 " + config.getMinpostsize() + " 字多于 "
							+ config.getMaxpostsize() + " 字");
				} else if (postmessage.length() > config.getMaxpostsize()) {
					reqcfg.addErrLine("您发表的内容过多, 系统设置要求帖子内容不得少于 " + config.getMinpostsize() + " 字多于 "
							+ config.getMaxpostsize() + " 字");
				}
			}

			String enddatetime = LForumRequest.getParamValue("enddatetime");
			String[] pollitem = {};

			if (!LForumRequest.getParamValue("updatepoll").equals("") && topic.getSpecial() == 1) {

				pollinfo.setMultiple(LForumRequest.getParamIntValue("multiple", 0));

				// 验证用户是否有发布投票的权限
				if (usergroupinfo.getAllowpostpoll() != 1) {
					reqcfg.addErrLine("您当前的身份 \"" + usergroupinfo.getGrouptitle() + "\" 没有发布投票的权限");
					return SUCCESS;
				}

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
			}

			int topicprice = 0;
			String tmpprice = LForumRequest.getParamValue("topicprice");

			if (Pattern.compile("^[0-9]*[0-9][0-9]*$").matcher(tmpprice).find() || tmpprice.equals("")) {
				if (topic.getSpecial() != 2) {
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
				if (topic.getSpecial() != 2) {
					reqcfg.addErrLine("主题售价只能为整数");
				} else {
					reqcfg.addErrLine("悬赏价格只能为整数");
				}
			}

			//新用户广告强力屏蔽检查
			if ((config.getDisablepostad() == 1) && useradminid < 1 || userid == -1) //如果开启新用户广告强力屏蔽检查或是游客
			{
				Date date = DateUtils.addMinutes(new Date(), -config.getDisablepostadregminute());
				if (userid == -1
						|| (config.getDisablepostadpostcount() != 0 && user.getPosts() <= config
								.getDisablepostadpostcount())
						|| (config.getDisablepostadregminute() != 0 && (date
								.before(Utils.parseDate(user.getJoindate())) || date.equals(Utils.parseDate(user
								.getJoindate()))))) {
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

			String curdatetime = Utils.getNowTime();

			if (useradminid == 1) {
				postinfo.setTitle(Utils.cleanHtmlTag(LForumRequest.getParamValue("title")));
				postinfo.setMessage(Utils.cleanHtmlTag(LForumRequest.getParamValue("message")));
			} else {
				postinfo
						.setTitle(Utils.cleanHtmlTag(cachesManager.banWordFilter(LForumRequest.getParamValue("title"))));
				postinfo.setMessage(Utils.cleanHtmlTag(cachesManager.banWordFilter(LForumRequest
						.getParamValue("message"))));
			}

			if (cachesManager.hasBannedWord(postinfo.getTitle()) || cachesManager.hasBannedWord(postinfo.getMessage())) {
				reqcfg.addErrLine("对不起, 您提交的内容包含不良信息, 因此无法提交, 请返回修改!");
				return SUCCESS;
			}

			if (useradminid != 1) {
				if (cachesManager.hasAuditWord(postinfo.getTitle())
						|| cachesManager.hasAuditWord(postinfo.getMessage())) {
					reqcfg.addErrLine("对不起, 管理员设置了需要对发帖进行审核, 您没有权力编辑已通过审核的帖子, 请返回修改!");
					return SUCCESS;
				}
			}
			//如果是不是管理员组,或者编辑间隔超过60秒,则附加编辑信息
			if (Utils.strDateDiffSeconds(postinfo.getPostdatetime(), 60) > 0 && config.getEditedby() == 1
					&& useradminid != 1) {
				postinfo.setLastedit(username + " 最后编辑于 " + curdatetime);
			}
			postinfo.setUsesig(Utils.null2Int(LForumRequest.getParamValue("usesig"), 0));
			postinfo.setHtmlon(1);
			postinfo.setSmileyoff(smileyoff);
			if (smileyoff == 0) {
				postinfo.setSmileyoff(Utils.null2Int(LForumRequest.getParamValue("smileyoff"), 0));
			}

			postinfo.setBbcodeoff(1);
			if (usergroupinfo.getAllowcusbbcode() == 1) {
				postinfo.setBbcodeoff(Utils.null2Int(LForumRequest.getParamValue("bbcodeoff"), 0));
			}
			postinfo.setParseurloff(Utils.null2Int(LForumRequest.getParamValue("parseurloff"), 0));

			// 如果所在管理组存在且所在管理组有删帖的管理权限
			if (admininfo != null && admininfo.getAlloweditpost() == 1
					&& moderatorManager.isModer(useradminid, userid, forumid)) {
				alloweditpost = true;
			} else if (userid != postinfo.getUsers().getUid()) {
				reqcfg.addErrLine("您当前的身份不是作者");
				return SUCCESS;
			} else {
				alloweditpost = true;
			}

			if (alloweditpost) {

				if (postinfo.getLayer() == 0) {

					///修改投票信息
					if (topic.getSpecial() == 1) {
						String pollItemname = Utils.cleanHtmlTag(LForumRequest.getParamValue("PollItemname"));

						if (pollItemname != "") {
							int multiple = LForumRequest.getParamValue("multiple") == "on" ? 1 : 0;
							int maxchoices = 0;

							if (multiple == 1) {
								maxchoices = LForumRequest.getParamIntValue("maxchoices", 0);
								if (maxchoices > pollitem.length) {
									maxchoices = pollitem.length;
								}
							}

							if (!pollManager.updatePoll(topic, multiple, pollitem.length, LForumRequest
									.getParamValue("PollOptionID"), pollItemname.trim(), LForumRequest
									.getParamValue("PollOptionDisplayOrder"), enddatetime, maxchoices, LForumRequest
									.getParamValue("visiblepoll") == "on" ? 1 : 0)) {
								reqcfg.addErrLine("投票错误,请检查显示顺序");
								return SUCCESS;
							}
						} else {
							reqcfg.addErrLine("投票项为空");
							return SUCCESS;
						}
					}

					int iconid = LForumRequest.getParamIntValue("iconid", 0);
					if (iconid > 15 || iconid < 0) {
						iconid = 0;
					}

					topic.setIconid(iconid);
					topic.setTitle(postinfo.getTitle());

					//悬赏差价处理
					if (topic.getSpecial() == 2) {
						int pricediff = topicprice - topic.getPrice();
						if (pricediff > 0) {
							//扣分
							if (userManager.getUserExtCredits(topic.getUsersByPosterid().getUid(), scoresetManager
									.getCreditsTrans()) < pricediff) {
								reqcfg.addErrLine("主题作者积分不足, 无法追加悬赏");
								return SUCCESS;
							} else {
								topic.setPrice(topicprice);
								userManager.updateUserExtCredits(topic.getUsersByPosterid().getUid(), scoresetManager
										.getCreditsTrans(), -pricediff);
							}
						} else if (pricediff < 0) {
							reqcfg.addErrLine("不能降低悬赏价格");
							return SUCCESS;
						}
					} else if (topic.getSpecial() == 0)//普通主题,出售
					{
						topic.setPrice(topicprice);
					}
					if (usergroupinfo.getAllowsetreadperm() == 1) {
						int topicreadperm = LForumRequest.getParamIntValue("topicreadperm", 0);
						topicreadperm = topicreadperm > 255 ? 255 : topicreadperm;
						topic.setReadperm(topicreadperm);
					}
					if (ForumUtils.isHidePost(postmessage) && usergroupinfo.getAllowhidecode() == 1) {
						topic.setHide(1);
					}
					Topictypes topictypes = new Topictypes();
					topictypes.setTypeid(LForumRequest.getParamIntValue("typeid", 0));
					topic.setTopictypes(topictypes);

					String htmltitle = LForumRequest.getParamValue("htmltitle").trim();
					if (!htmltitle.equals("") && htmltitle.trim().equals(topic.getTitle())) {
						topic.setMagic(11000);
						//按照  附加位/htmltitle(1位)/magic(3位)/以后扩展（未知位数） 的方式来存储
						//例： 11001
					} else {
						topic.setMagic(0);
					}

					//                    forumt.DeleteTopicTags(topic.Tid);
					//                    topicManager.deleteRelatedTopics(topic.Tid);
					//                    String tags = LForumRequest.getParamValue("tags").trim();
					//                    String[] tagArray = null;
					//                    if (enabletag && tags != String.Empty)
					//                    {
					//                        if (ForumUtils.HasBannedWord(tags))
					//                        {
					//                            reqcfg.addErrLine("标签中含有系统禁止词语,请修改");
					//                            return SUCCESS;
					//                        }
					//
					//                        tagArray = Utils.SplitString(tags, " ", true, 10);
					//                        if (tagArray.length() > 0)
					//                        {
					//                            topic.Magic = Topics.SetMagicValue(topic.Magic, MagicType.TopicTag, 1);
					//                            ForumTags.CreateTopicTags(tagArray, topic.Tid, userid, curdatetime);
					//                        }
					//                    }
					topicManager.updateTopic(topic);

					//保存htmltitle
					if (canhtmltitle && !htmltitle.equals("") && !htmltitle.equals(topic.getTitle())) {
						topicManager.writeHtmlTitleFile(htmltitle, topic.getTid());
					}

				} else {
					if (ForumUtils.isHidePost(postmessage) && usergroupinfo.getAllowhidecode() == 1) {
						topic.setHide(1);
					}

					topicManager.updateTopicHide(topicid);

				}

				// 通过验证的用户可以编辑帖子
				postManager.updatePost(postinfo);

				sb = new StringBuilder();
				sb.delete(0, sb.length());

				String[] delaids = LForumRequest.getParamValues("deleteaid");

				//编辑帖子时如果进行了批量删除附件
				if (delaids != null) {
					if (Utils.isIntArray(LForumRequest.getParamValues("deleteaid")))//如果要删除的附件ID列表为数字数组
					{
						attachmentManager.deleteAttachment(LForumRequest.getParamValues("deleteaid", ","));

					}
				} else {
					delaids = new String[0];
				}
				//编辑帖子时如果进行了更新附件操作
				String[] updatedAttId = LForumRequest.getParamValues("attachupdatedid") == null ? new String[0]
						: LForumRequest.getParamValues("attachupdatedid");//被更新的附件Id列表
				String[] updateAttId = LForumRequest.getParamValues("attachupdateid") == null ? new String[0]
						: LForumRequest.getParamValues("attachupdateid");//所有已上传的附件Id列表
				String[] descriptionArray = LForumRequest.getParamValues("attachupdatedesc") == null ? new String[0]
						: LForumRequest.getParamValues("attachupdatedesc");//所有已上传的附件的描述
				String[] readpermArray = LForumRequest.getParamValues("attachupdatereadperm") == null ? new String[0]
						: LForumRequest.getParamValues("attachupdatereadperm");//所有已上传得附件的阅读权限

				List<String> updateAttArrayList = new ArrayList<String>();
				if (updateAttId != null) {
					for (String s : updateAttId) {
						if (!Utils.inArray(s, delaids)) { //已上传的附件Id不在被删除的附件Id列表中时
							updateAttArrayList.add(s);
						}
					}
				}

				String[] updateAttArray = new String[updateAttArrayList.size()];
				for (int i = 0; i < updateAttArrayList.size(); i++) {
					updateAttArray[i] = updateAttArrayList.get(i);
				}
				if (updateAttId != null) { //原来有附件
					//int watermarkstate = config.getWatermarkstatus();
					//
					//					if (forum.getDisablewatermark() == 1)
					//						watermarkstate = 0;

					//保存新的文件
					//					AttachmentInfo[] attArray = ForumUtils.saveRequestFiles(forumid, config.getMaxattachments()
					//							+ updateAttId.length, usergroupinfo.getMaxsizeperday(), usergroupinfo.getMaxattachsize(),
					//							maxTodaySize, attachextensions, watermarkstate, config, attachupdatedfiles,
					//							attachupdatedFileNames, attachupdatedContentTypes);

					if (Utils.isIntArray(updateAttArray)) {
						for (int i = 0; i < updateAttArray.length; i++) { //遍历原来所有附件
							String attachmentId = updateAttArray[i];
							if (Utils.inArray(attachmentId, updatedAttId)) { //附件文件被更新
								if (Utils.inArray(attachmentId, delaids)) { //附件进行了删除操作, 则不操作此附件,即使其也被更新
									continue;
								}
								//更新附件
								int attachmentUpdatedIndex = getAttachmentUpdatedIndex(attachmentId, updatedAttId);//获取此次上传的被更新附件在数组中的索引
								if (attachmentUpdatedIndex > -1) { //附件索引存在
									//									if (attArray[attachmentUpdatedIndex].getSys_noupload().equals("")) { //由此属性为空可以判断上传成功
									//										//获取将被更新的附件信息
									//										AttachmentInfo attachmentInfo = attachmentManager.getAttachmentInfo(Utils
									//												.null2Int(updatedAttId[attachmentUpdatedIndex], 0));
									//										if (attachmentInfo != null) {
									//											if (attachmentInfo.getFilename().trim().toLowerCase().indexOf("http") < 0) {
									//												//删除原来的文件
									//												FileUtils.forceDelete(new File(config.getWebpath() + "upload/"
									//														+ attachmentInfo.getFilename()));
									//											}
									//
									//											//记住Aid以便稍后更新
									//											attArray[attachmentUpdatedIndex].setAid(attachmentInfo.getAid());
									//											attArray[attachmentUpdatedIndex].setDescription(descriptionArray[i]);
									//											int att_readperm = Utils.null2Int(readpermArray[i], 0);
									//											att_readperm = att_readperm > 255 ? 255 : att_readperm;
									//											attArray[attachmentUpdatedIndex].setReadperm(att_readperm);
									//
									//											attachmentManager.updateAttachment(attArray[attachmentUpdatedIndex]);
									//										}
									//									} else { //上传失败的附件，稍后提示
									//										sb.append("<tr><td align=\"left\">");
									//										sb.append(attArray[attachmentUpdatedIndex].getAttachment());
									//										sb.append("</td>");
									//										sb.append("<td align=\"left\">");
									//										sb.append(attArray[attachmentUpdatedIndex].getSys_noupload());
									//										sb.append("</td></tr>");
									//									}
								}
							} else { //仅修改了阅读权限和描述等
								if (Utils.inArray(updateAttArray[i], delaids)) {
									continue;
								}
								if (!(attachmentlist.get(i).getReadperm().equals(readpermArray[i]))
										|| !(attachmentlist.get(i).getDescription().trim().equals(descriptionArray[i]))) {
									int att_readperm = Utils.null2Int(readpermArray[i], 0);
									att_readperm = att_readperm > 255 ? 255 : att_readperm;
									attachmentManager.updateAttachment(Utils.null2Int(updateAttArray[i], 0),
											att_readperm, descriptionArray[i]);
								}
							}
						}
					}
					//						}
				}

				///上传附件
				int watermarkstatus = config.getWatermarkstatus();
				if (forum.getDisablewatermark() == 1) {
					watermarkstatus = 0;
				}
				AttachmentInfo[] attachmentinfo = ForumUtils.saveRequestFiles(forumid, config.getMaxattachments()
						- updateAttArray.length, usergroupinfo.getMaxsizeperday(), usergroupinfo.getMaxattachsize(),
						maxTodaySize, attachextensions, watermarkstatus, config, postfiles, postfileFileNames,
						postfileContentTypes);
				if (attachmentinfo != null) {
					if (attachmentinfo.length > config.getMaxattachments() - updateAttArray.length) {
						reqcfg.addErrLine("系统设置为附件不得多于" + config.getMaxattachments() + "个");
						return SUCCESS;
					}
					Postid pids = new Postid();
					pids.setPid(postid);
					int errorAttachment = attachmentManager.bindAttachment(attachmentinfo, pids, sb, topic, user);

					int[] aid = attachmentManager.createAttachments(attachmentinfo);
					String tempMessage = attachmentManager.filterLocalTags(aid, attachmentinfo, postinfo.getMessage());

					if (!tempMessage.equals(postinfo.getMessage())) {
						postinfo.setMessage(tempMessage);
						postinfo.setPid(postid);
						postinfo.setPostidByPid(pids);
						postManager.updatePost(postinfo);
					}

					userCreditManager.updateUserCreditsByUploadAttachment(userid, aid.length - errorAttachment);
				}

				//编辑后跳转地址
				if (topic.getSpecial() == 4) {
					//辩论地址
					reqcfg.setUrl("showdebate.action?topicid=" + topicid);
				} else if (!LForumRequest.getParamValue("referer").equals("")) { //ajax快速回复将传递referer参数
					reqcfg.setUrl("showtopic.action?page=end&topicid=" + topicid + "#" + postinfo.getPid());
				} else if (!LForumRequest.getParamValue("pageid").equals("")) { //如果不是ajax,则应该是带pageid的参数
					reqcfg.setUrl("showtopic.action?&topicid=" + topicid + "&page="
							+ LForumRequest.getParamValue("pageid") + "#" + postinfo.getPid());
				} else { //如果都为空.就跳转到第一页(以免意外情况)
					reqcfg.setUrl("showtopic.aspx?&topicid=" + topicid);
				}

				if (sb.length() > 0) {
					reqcfg.setMetaRefresh(5).setShowBackLink(true);
					sb
							.insert(
									0,
									"<table cellspacing=\"0\" cellpadding=\"4\" border=\"0\"><tr><td colspan=2 align=\"left\"><span class=\"bold\"><nobr>编辑帖子成功,但以下附件上传失败:</nobr></span><br /></td></tr>");
					sb.append("</table>");
					reqcfg.addMsgLine(sb.toString());
				} else {
					reqcfg.setMetaRefresh().setShowBackLink(false).addMsgLine("编辑帖子成功, 返回该主题");
				}
				// 删除主题游客缓存
				if (postinfo.getLayer() == 0) {
					//ForumUtils.deleteTopicCacheFile(topicid);
				}
				return SUCCESS;
			} else {
				reqcfg.addErrLine("您当前的身份没有编辑帖子的权限");
				return SUCCESS;
			}

		}
		return SUCCESS;
	}

	private int getAttachmentUpdatedIndex(String attachmentId, String[] updatedAttId) {
		for (int i = 0; i < updatedAttId.length; i++) {
			if (updatedAttId[i] == attachmentId) {
				return i;
			}
		}

		return -1;
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

	public Posts getPostinfo() {
		return postinfo;
	}

	public Topics getTopic() {
		return topic;
	}

	public List<PollOptionView> getPolloptionlist() {
		return polloptionlist;
	}

	public Polls getPollinfo() {
		return pollinfo;
	}

	public List<Attachments> getAttachmentlist() {
		return attachmentlist;
	}

	public int getAttachmentcount() {
		return attachmentcount;
	}

	public String getMessage() {
		return message;
	}

	public String getSmilies() {
		return smilies;
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

	public int getDisablepostctrl() {
		return disablepostctrl;
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

	public boolean isAllowviewattach() {
		return allowviewattach;
	}

	public boolean isCanhtmltitle() {
		return canhtmltitle;
	}

	public String getHtmltitle() {
		return htmltitle;
	}

	public String getFirstpagesmilies() {
		return firstpagesmilies;
	}

	public String getTopictags() {
		return topictags;
	}

	public boolean isEnabletag() {
		return enabletag;
	}

	public boolean isAlloweditpost() {
		return alloweditpost;
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

	public ScoresetManager getScoresetManager() {
		return scoresetManager;
	}
}
