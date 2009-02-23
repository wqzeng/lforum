package com.javaeye.lonlysky.lforum.web;

import java.io.IOException;
import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.struts2.config.ParentPackage;
import org.springframework.beans.factory.annotation.Autowired;

import com.javaeye.lonlysky.lforum.ForumBaseAction;
import com.javaeye.lonlysky.lforum.comm.LForumRequest;
import com.javaeye.lonlysky.lforum.comm.utils.ForumUtils;
import com.javaeye.lonlysky.lforum.comm.utils.HQLUitls;
import com.javaeye.lonlysky.lforum.comm.utils.Utils;
import com.javaeye.lonlysky.lforum.entity.forum.Admingroups;
import com.javaeye.lonlysky.lforum.entity.forum.Forums;
import com.javaeye.lonlysky.lforum.entity.forum.Pms;
import com.javaeye.lonlysky.lforum.entity.forum.Posts;
import com.javaeye.lonlysky.lforum.entity.forum.Ratelog;
import com.javaeye.lonlysky.lforum.entity.forum.Topicidentify;
import com.javaeye.lonlysky.lforum.entity.forum.Topics;
import com.javaeye.lonlysky.lforum.entity.forum.Usergroups;
import com.javaeye.lonlysky.lforum.entity.forum.Users;
import com.javaeye.lonlysky.lforum.service.AdminGroupManager;
import com.javaeye.lonlysky.lforum.service.BonuManager;
import com.javaeye.lonlysky.lforum.service.ForumManager;
import com.javaeye.lonlysky.lforum.service.MessageManager;
import com.javaeye.lonlysky.lforum.service.ModeratorManager;
import com.javaeye.lonlysky.lforum.service.PostManager;
import com.javaeye.lonlysky.lforum.service.ScoresetManager;
import com.javaeye.lonlysky.lforum.service.TopicAdminManager;
import com.javaeye.lonlysky.lforum.service.TopicManager;
import com.javaeye.lonlysky.lforum.service.UserCreditManager;
import com.javaeye.lonlysky.lforum.service.admin.AdminModeratorLogMag;
import com.javaeye.lonlysky.lforum.service.admin.AdminRateLogManager;

/**
 * 帖子管理页面
 * 
 * @author 黄磊
 *
 */
@ParentPackage("default")
public class TopicadminAction extends ForumBaseAction {

	private static final long serialVersionUID = 4856646211105697203L;

	/**
	 * 操作标题
	 */
	private String operationtitle = "";

	/**
	 * 操作类型
	 */
	private String operation = "";

	/**
	 * 操作类型参数
	 */
	private String action = "";

	/**
	 * 主题列表
	 */
	private String topiclist = "0";

	/**
	 * 帖子Id列表
	 */
	private String postidlist = "0";

	/**
	 * 版块名称
	 */
	private String forumname = "";

	/**
	 * 论坛导航信息
	 */
	private String forumnav = "";

	/**
	 * 帖子标题
	 */
	private String title = "";

	/**
	 * 帖子作者用户名
	 */
	private String poster = "";

	/**
	 * 版块Id
	 */
	private int forumid = 0;

	/**
	 * 版块列表
	 */
	private String forumlist = "";

	/**
	 * 主题置顶状态
	 */
	private int displayorder = 0;

	/**
	 * 主题精华状态
	 */
	private int digest = 0;

	/**
	 * 高亮颜色
	 */
	private String highlight_color = "";

	/**
	 * 是否加粗
	 */
	private String highlight_style_b = "";

	/**
	 * 是否斜体
	 */
	private String highlight_style_i = "";

	/**
	 * 是否带下划线
	 */
	private String highlight_style_u = "";

	/**
	 * 关闭主题, 0=打开,1=关闭 
	 */
	private int close = 0;

	/**
	 * 移动主题时的目标版块Id
	 */
	private int moveto = 0;

	/**
	 * 移动方式
	 */
	private String type = ""; //移动方式

	/**
	 * 后续操作
	 */
	private int donext = 0;

	/**
	 * 帖子列表
	 */
	private List<Posts> postlist;

	/**
	 * 可用积分列表
	 */
	private List<Map<String, Object>> scorelist = new ArrayList<Map<String, Object>>();

	/**
	 * 主题鉴定类型列表
	 */
	private List<Topicidentify> identifylist;

	/**
	 * 主题鉴定js数组
	 */
	private String identifyjsarray;

	/**
	 * 主题分类选项
	 */
	private String topictypeselectoptions; //当前版块的主题类型选项

	/**
	 * 当前帖子评分日志列表
	 */
	private List<Ratelog> ratelog = new ArrayList<Ratelog>();
	private List<String> extcreditnames = new ArrayList<String>();

	/**
	 * 当前帖子评分日志记录数
	 */
	private int ratelogcount = 0;

	/**
	 * 当前的主题
	 */
	private Topics topicinfo;
	private int opinion = -1;
	private String op = "";

	@Autowired
	private TopicAdminManager topicAdminManager;

	@Autowired
	private ScoresetManager scoresetManager;

	@Autowired
	private ModeratorManager moderatorManager;

	@Autowired
	private TopicManager topicManager;

	@Autowired
	private PostManager postManager;

	@Autowired
	private ForumManager forumManager;

	@Autowired
	private AdminGroupManager adminGroupManager;

	@Autowired
	private AdminModeratorLogMag adminModeratorLogMag;

	@Autowired
	private MessageManager messageManager;

	@Autowired
	private AdminRateLogManager adminRateLogManager;

	@Autowired
	private UserCreditManager userCreditManager;

	@Autowired
	private BonuManager bonuManager;

	// 是否允许管理主题, 初始false为不允许
	protected boolean ismoder = false;
	protected int RateIsReady = 0;
	private Forums forum;
	private int highlight = 0;
	private boolean issendmessage = false;
	private boolean isreason = false;

	@Override
	public String execute() throws Exception {
		validatePermission();
		if (!bindTitle()) {
			return SUCCESS;
		}
		return SUCCESS;
	}

	/**
	 * 验证权限
	 * @throws IOException 
	 * @throws ParseException 
	 */
	private void validatePermission() throws IOException, ParseException {
		opinion = LForumRequest.getParamIntValue("opinion", -1);
		if (userid == -1) {
			reqcfg.addErrLine("请先登录");
			return;
		}
		Usergroups usergroupinfo = userGroupManager.getUsergroup(userManager.getUserInfo(userid).getUsergroups()
				.getGroupid());
		switch (usergroupinfo.getReasonpm()) {

		case 1:
			isreason = true;
			break;
		case 2:
			issendmessage = true;
			break;
		case 3:
			isreason = true;
			issendmessage = true;
			break;

		default:
			break;
		}
		action = LForumRequest.getParamValue("action");
		if (ForumUtils.isCrossSitePost(LForumRequest.getUrlReferrer(), LForumRequest.getHost()) || action.equals("")) {
			reqcfg.addErrLine("非法提交");
			return;
		}

		forumid = LForumRequest.getParamIntValue("forumid", -1);
		// 检查是否具有版主的身份
		ismoder = moderatorManager.isModer(useradminid, userid, forumid);
		// 如果拥有管理组身份
		Admingroups admininfo = adminGroupManager.getAdminGroup(useradminid);

		operation = LForumRequest.getParamValue("operation").toLowerCase();
		if (!operation.equals("rate") && !operation.equals("bonus") && !operation.equals("banpost")
				&& !LForumRequest.getParamValue("operat").equals("rate")
				&& !LForumRequest.getParamValue("operat").equals("bonus")
				&& !LForumRequest.getParamValue("operat").equals("banpost")) {
			// 如果所属管理组不存在
			if (admininfo == null) {
				reqcfg.addErrLine("你没有管理权限");
				return;
			}
		}

		operationtitle = "操作提示";

		reqcfg.setUrl("showforum.action?forumid=" + forumid);

		topiclist = LForumRequest.getParamValues("topicid", ",");
		postidlist = LForumRequest.getParamValues("postid", ",");

		if (action.equals("")) {
			reqcfg.addErrLine("操作类型参数为空");
			return;
		}

		if (forumid == -1) {
			//
			reqcfg.addErrLine("版块ID必须为数字");
			return;
		}

		if (!topiclist.equals("") && !topicManager.inSameForum(topiclist, forumid)) {
			reqcfg.addErrLine("无法对非本版块主题进行管理操作");
			return;
		}
		displayorder = topicAdminManager.getDisplayorder(topiclist);
		digest = topicAdminManager.getDigest(topiclist);
		forum = forumManager.getForumInfo(forumid);
		forumname = forum.getName();
		topictypeselectoptions = forumManager.getCurrentTopicTypesOption(forum.getFid(), forum.getForumfields()
				.getTopictypes().trim());

		if (!forumManager.allowView(forum.getForumfields().getViewperm().trim(), usergroupid)) {
			reqcfg.addErrLine("您没有浏览该版块的权限");
			return;
		}

		pagetitle = Utils.cleanHtmlTag(forumname);
		forumnav = forum.getPathlist().trim();

		if (topiclist.compareTo("") == 0) {
			reqcfg.addErrLine("您没有选择主题或相应的管理操作,请返回修改");
			return;
		}
		if (operation.compareTo("") != 0) {
			if (!doOperations(forum, admininfo, config.getReasonpm())) {
				return;
			}
		}

		if (action.compareTo("moderate") != 0) {
			if ("delete,move,type,highlight,close,displayorder,digest,copy,split,merge,bump,repair,delposts,banpost"
					.indexOf(operation) == -1) {
				reqcfg.addErrLine("你无权操作此功能");
				return;
			}
			operation = action;
		} else {
			if (operation.compareTo("") == 0) {
				operation = LForumRequest.getParamValue("operat");
			}

			if (operation.compareTo("") == 0) {
				//
				reqcfg.addErrLine("您没有选择主题或相应的管理操作,请返回修改");
				return;
			}
		}

	}

	/**
	 * 进行相关操作
	 * @param forum2
	 * @param admininfo
	 * @param reasonpm
	 * @return
	 * @throws IOException 
	 * @throws ParseException 
	 */
	private boolean doOperations(Forums forum2, Admingroups admininfo, int reasonpm) throws IOException, ParseException {
		op = operation;
		String operationName = "";
		String next = LForumRequest.getParamValue("next");
		String referer = LForumRequest.getParamValue("referer");

		List<Posts> pList = null;
		List<Topics> tList = null;

		String reason = LForumRequest.getParamValue("reason");
		int sendmsg = LForumRequest.getParamIntValue("sendmessage", 0);
		if (issendmessage && sendmsg == 0) {
			reqcfg.addErrLine("您所在的用户组需要在操作时发送短消息");
			return false;
		}

		if (!operation.equals("identify") && !operation.equals("bonus")) {

			if (isreason) {
				if (reason.equals("")) {
					reqcfg.addErrLine("操作原因不能为空");
					return false;
				} else {
					if (reason.length() > 200) {
						reqcfg.addErrLine("操作原因不能多于200个字符");
						return false;
					}
				}
			}
		}

		if ("delete,move,type,highlight,close,displayorder,digest,copy,split,merge,bump,repair,rate,cancelrate,delposts,identify,bonus,banpost"
				.indexOf(operation) == -1) {
			reqcfg.addErrLine("未知的操作参数");
			return false;
		}
		//执行提交操作
		if (!next.trim().equals("")) {
			referer = "topicadmin.action?action=" + next + "&forumid=" + forumid + "&topicid=" + topiclist;
		}
		int operationid = 0;

		boolean istopic = false;
		String subjecttype;
		String postoperations = "rate,delposts,banpost";
		if (postoperations.contains(operation)) {
			pList = postManager.getPostListByPids(postidlist);
			subjecttype = "帖子";
		} else {
			tList = topicManager.getTopicList(topiclist, -1);
			istopic = true;
			subjecttype = "主题";

		}

		if (operation.equals("delete")) { // 删除
			operationName = "删除主题";
			if (!doDeleteOperation(forum))
				return false;
			operationid = 1;
			//		}else if (operation.equals("move")) {
			//			operationName = "移动主题";
			//            if (!doMoveOperation())
			//                return false;
			//            operationid = 2;
			//		}else if (operation.equals("type")) {
			//			 operationName = "主题分类";
			//             if (!doTypeOperation())
			//                 return false;
			//             operationid = 3;
		} else if (operation.equals("highlight")) {
			operationName = "设置高亮";
			if (!doHighlightOperation())
				return false;
			operationid = 4;
		} else if (operation.equals("close")) {
			operationName = "关闭主题/取消";
			if (!doCloseOperation())
				return false;
			operationid = 5;
		} else if (operation.equals("displayorder")) {
			operationName = "主题置顶/取消";
			if (!doDisplayOrderOperation(admininfo))
				return false;
			operationid = 6;
		} else if (operation.equals("digest")) {
			operationName = "设置精华/取消";
			if (!doDigestOperation())
				return false;
			operationid = 7;
			//		}else if (operation.equals("copy")) {
			//			operationName = "复制主题";
			//            if (!DoCopyOperation())
			//                return false;
			//            operationid = 8;
			//		}else if (operation.equals("split")) {
			//			operationName = "分割主题";
			//            if (!DoSplitOperation())
			//                return false;
			//            operationid = 9;
			//		}else if (operation.equals("merge")) {
			//			operationName = "合并主题";
			//            if (!DoMergeOperation())
			//                return false;
			//            operationid = 10;
		} else if (operation.equals("bump")) {
			operationName = "提升/下沉主题";
			if (!doBumpTopicsOperation())
				return false;
			operationid = 11;
			//		}else if (operation.equals("repair")) {
			//			operationName = "修复主题";
			//            TopicAdmins.RepairTopicList(topiclist);
			//            operationid = 12;
		} else if (operation.equals("rate")) {
			operationName = "帖子评分";
			if (!doRateOperation(reason))
				return false;
			operationid = 13;
		} else if (operation.equals("delposts")) {
			operationName = "批量删贴";
			if (!ismoder) {
				reqcfg.addErrLine("您当前的身份 \"" + usergroupinfo.getGrouptitle() + "\" 没有删除的权限");
				return false;
			}
			if (!doDelpostsOperation(reason, forum))
				return false;
			operationid = 14;
		} else if (operation.equals("identify")) {
			operationName = "鉴定主题";
			if (!ismoder) {
				reqcfg.addErrLine("您当前的身份 \"" + usergroupinfo.getGrouptitle() + "\" 没有鉴定的权限");
				return false;
			}
			if (!doIndentifyOperation())
				return false;
			operationid = 15;
		} else if (operation.equals("cancelrate")) {
			operationName = "撤销评分";
			if (!doCancelRateOperation(reason))
				return false;
			operationid = 16;
		} else if (operation.equals("bonus")) {
			operationName = "结帖";
			if (!doBonusOperation())
				return false;
			operationid = 16;
		} else if (operation.equals("banpost")) {
			operationName = "屏蔽帖子";
			if (!doBanPostOperatopn())
				return false;
			operationid = 17;
		} else {
			operationName = "未知操作";
		}

		if (next.compareTo("") == 0) {
			reqcfg.addMsgLine("管理操作成功,现在将转入主题列表");
		} else {
			reqcfg.addMsgLine("管理操作成功,现在将转入后续操作");
		}

		if (!operation.equals("rate")) {
			if (config.getModworkstatus() == 1) {
				if (postidlist.equals("")) {
					for (String tid : topiclist.split(",")) {
						String title;
						if (operation.equals("delete")) {
							title = "";
						} else {
							Topics topicinfo = topicManager.getTopicInfo(Utils.null2Int(tid, -1));
							title = topicinfo.getTitle();
						}
						adminModeratorLogMag.insertLog(userid, username, usergroupid, usergroupinfo.getGrouptitle(),
								LForumRequest.getIp(), Utils.getNowTime(), forumid, forumname, Utils.null2Int(tid),
								title, operation, reason);
					}
				} else {
					String[] postarray = postidlist.split(",");
					Topics topinfo = topicManager.getTopicInfo(Utils.null2Int(topiclist, -1));
					for (String postid : postarray) {
						Posts postinfo = postManager.getPostInfo(Utils.null2Int(postid, 0));
						String postitle;
						if (postinfo == null) {
							postitle = topinfo.getTitle();

						} else {
							postitle = postinfo.getTitle().equals("") ? topinfo.getTitle() : postinfo.getTitle();
						}

						adminModeratorLogMag.insertLog(userid, username, usergroupid, usergroupinfo.getGrouptitle(),
								LForumRequest.getIp(), Utils.getNowTime(), forumid, forumname, Utils.null2Int(postid),
								postitle, operation, reason);
					}
				}
			}
		}
		sendMessage(operationid, pList == null ? tList : pList, istopic, operationName, reason, sendmsg, subjecttype);
		//}

		//执行完某一操作后转到后续操作
		reqcfg.setUrl(referer);
		if (!next.equals("")) {
			response.sendRedirect(referer);
		} else {
			reqcfg.addScript("window.setTimeout('redirectURL()', 2000);function redirectURL() {window.location='"
					+ referer + "';}");
		}
		reqcfg.setShowBackLink(false);
		return true;
	}

	/**
	 * 屏蔽帖子
	 * @return
	 */
	private boolean doBanPostOperatopn() {
		if (!Utils.isInt(topiclist)) {
			reqcfg.addErrLine("无效的主题ID");
			return false;
		}
		Topics topic = topicManager.getTopicInfo(Utils.null2Int(topiclist, 0));
		if (topic == null) {
			reqcfg.addErrLine("不存在的主题");
			return false;
		}
		if (!Utils.isIntArray(postidlist.split(","))) {
			reqcfg.addErrLine("非法的帖子ID");
			return false;
		}
		int banposttype = LForumRequest.getParamIntValue("banpost", -1);

		if (banposttype != -1 && (banposttype == 0 || banposttype == -2)) {
			postManager.banPosts(postidlist, banposttype);
			return true;
		}
		return false;
	}

	/**
	 * 悬赏结帖
	 * @return
	 */
	private boolean doBonusOperation() {
		//身份验证
		int topicid = LForumRequest.getParamIntValue("topicid", 0);
		topicinfo = topicManager.getTopicInfo(topicid);
		if (topicinfo.getSpecial() == 3) {
			reqcfg.addErrLine("本主题的悬赏已经结束");
		}

		if (topicinfo.getUsersByPosterid().getUid() <= 0) {
			reqcfg.addErrLine("无法结束游客发布的悬赏");
		}

		if (topicinfo.getUsersByPosterid().getUid() != userid && !ismoder) { //不是作者或管理者
			reqcfg.addErrLine("您没有权限结束此悬赏");
		}

		int costBonus = 0;
		String[] costBonusArray = LForumRequest.getParamValues("postbonus");

		for (String s : costBonusArray) {
			costBonus += Utils.null2Int(s, 0);
		}

		if (costBonus != topicinfo.getPrice()) {
			reqcfg.addErrLine("分数总和与悬赏总分不相符");
		}

		String[] addonsArray = LForumRequest.getParamValues("addons");
		int[] winneridArray = new int[addonsArray.length];
		int[] postidArray = new int[addonsArray.length];
		String[] winnernameArray = new String[addonsArray.length];
		for (String addon : addonsArray) {
			int winnerid = Utils.null2Int(addon.split("\\|")[0], 0);
			if (winnerid == topicinfo.getUsersByPosterid().getUid()) {
				reqcfg.addErrLine("不能向悬赏者发放积分奖励");
				break;
			}
		}

		if (costBonusArray.length != addonsArray.length) {
			reqcfg.addErrLine("获奖者数量与积分奖励数量不一致");
		}

		String[] valuableAnswerArray = LForumRequest.getParamValues("valuableAnswers");
		int bestAnswer = LForumRequest.getParamIntValue("bestAnswer", 0);
		if (reqcfg.isErr()) {
			return false;
		}

		for (int i = 0; i < addonsArray.length; i++) {
			winneridArray[i] = Utils.null2Int(addonsArray[i].split("\\|")[0], 0);
			postidArray[i] = Utils.null2Int(addonsArray[i].split("\\|")[1], 0);
			winnernameArray[i] = addonsArray[i].split("\\|")[2];
		}
		bonuManager.closeBonus(topicinfo, userid, postidArray, winneridArray, winnernameArray, costBonusArray,
				valuableAnswerArray, bestAnswer);
		return true;
	}

	/**
	 * 撤销评分
	 * @param reason
	 * @return
	 */
	private boolean doCancelRateOperation(String reason) {
		if (!checkRatePermission()) {
			return false;
		}

		if (postidlist.equals("")) {
			reqcfg.addErrLine("您没有选择要撤销评分的帖子, 请返回修改.");
			return false;
		}

		if (!ismoder) {
			reqcfg.addErrLine("您当前的身份 \"" + usergroupinfo.getGrouptitle() + "\" 没有撤消评分的权限");
			return false;
		}

		if (LForumRequest.getParamValue("ratelogid").equals("")) {
			reqcfg.addErrLine("您未选取要撤消的评分记录");
			return false;
		}
		int ratetimes = (LForumRequest.getParamValues("ratelogid").length) * -1;
		postManager.updatePostRateTimes(Utils.null2Int(topiclist, 0), postidlist, ratetimes);
		topicAdminManager.cancelRatePosts(LForumRequest.getParamValues("ratelogid", ","), Utils.null2Int(topiclist, 0),
				postidlist, userid, username, usergroupinfo.getGroupid(), usergroupinfo.getGrouptitle(), forumid,
				forumname, reason);
		return true;
	}

	/**
	 * 帖子评分
	 * @param reason
	 * @return
	 */
	private boolean doRateOperation(String reason) {
		if (!checkRatePermission()) {
			return false;
		}
		String score = LForumRequest.getParamValues("score", ",");
		String extcredits = LForumRequest.getParamValues("extcredits", ",");

		if (postidlist.equals("")) {
			reqcfg.addErrLine("您没有选择要评分的帖子, 请返回修改.");
			return false;
		}
		if (config.getDupkarmarate() != 1
				&& adminRateLogManager.recordCount(HQLUitls.getRateLogCountCondition(userid, postidlist)) > 0) {
			reqcfg.addErrLine("您不能对本帖重复评分.");
			return false;
		}

		scorelist = userGroupManager.groupParticipateScore(userid, usergroupid);
		String[] scoreArr = score.split(",");
		String[] extcreditsArr = extcredits.split(",");
		String cscoreArr = "";
		String cextcreditsArr = "";
		int arr = 0, ratetimes = 0;
		for (int i = 0; i < scoreArr.length; i++) {
			if (Utils.isInt(scoreArr[i]) && !scoreArr[i].equals("0") && !scoreArr[i].contains(".")) {
				cscoreArr = cscoreArr + scoreArr[i] + ",";
				cextcreditsArr = cextcreditsArr + extcreditsArr[i] + ",";
				ratetimes++;
			}

		}

		if (cscoreArr.length() == 0) {
			reqcfg.addErrLine("请至少为一个评分项目设置非零整数值");
			return false;
		}

		for (Map<String, Object> scoredr : scorelist) {
			if (scoredr.get("ScoreCode").toString().equals(extcreditsArr[arr])) {
				if (Utils.null2Int(scoredr.get("MaxInDay"), 0) < Math.abs(Utils.null2Int(scoreArr[arr], 0))) {
					reqcfg.addErrLine("您未输入分值或超过每次评分范围限制,请返回修改.");
					reqcfg.addErrLine("您的今日" + scoredr.get("ScoreName") + "评分范围: " + scoredr.get("Min") + " - "
							+ scoredr.get("Max"));
					return false;
				}

				if (Utils.null2Int(scoredr.get("Max"), 0) < Utils.null2Int(scoreArr[arr], 0)) {
					reqcfg.addErrLine("您未输入分值或超过每次评分范围限制,请返回修改.");
					reqcfg.addErrLine("您的单次" + scoredr.get("ScoreName") + "评分范围: " + scoredr.get("Min") + " - "
							+ scoredr.get("Max"));
					return false;
				}

				if (Utils.null2Int(scoredr.get("Min"), 0) > Utils.null2Int(scoreArr[arr], 0)) {
					reqcfg.addErrLine("您未输入分值或超过每次评分范围限制,请返回修改.");
					reqcfg.addErrLine("您的单次" + scoredr.get("ScoreName") + "评分范围: " + scoredr.get("Min") + " - "
							+ scoredr.get("Max"));
					return false;
				}
			}
			arr++;
		}
		postManager.updatePostRateTimes(Utils.null2Int(topiclist, 0), postidlist, ratetimes);
		topicAdminManager.ratePosts(Utils.null2Int(topiclist, 0), postidlist, cscoreArr, cextcreditsArr, userid,
				username, reason);
		RateIsReady = 1;
		return true;
	}

	/**
	 * 检查评分权限
	 * @return
	 */
	private boolean checkRatePermission() {
		if (usergroupinfo.getRaterange().trim().equals("")) {
			reqcfg.addErrLine("您当前的身份 \"" + usergroupinfo.getGrouptitle() + "\" 没有评分的权限");
			return false;
		} else {
			String[] rolesByScoreType = usergroupinfo.getRaterange().split("\\|");
			boolean hasExtcreditsCanRate = false;
			for (String roleByScoreType : rolesByScoreType) {
				String[] role = roleByScoreType.split(",");
				//数组结构:  扩展积分编号,参与评分,积分代号,积分名称,评分最小值,评分最大值,24小时最大评分数
				//				0			1			2		3		4			5			6
				if (Utils.null2Boolean(role[1], false)) {
					hasExtcreditsCanRate = true;
				}
			}
			if (!hasExtcreditsCanRate) {
				reqcfg.addErrLine("您当前的身份 \"" + usergroupinfo.getGrouptitle() + "\" 没有评分的权限");
				return false;
			}
		}
		return true;
	}

	/**
	 * 发送短信通知
	 */
	private void sendMessage(int operationid, List<? extends Serializable> list, boolean istopic, String operationName,
			String reason, int sendmsg, String subjecttype) {
		if (istopic) {
			topicManager.updateTopicModerated(topiclist, operationid);

		}
		if (list != null) {
			if (cachesManager.hasBannedWord(reason)) {
				reqcfg.addErrLine("对不起, 您提交的内容包含不良信息, 因此无法提交, 请返回修改!");
				return;
			} else {
				reason = cachesManager.banWordFilter(reason);
			}
			for (Serializable item : list) {
				if (istopic) {
					adminModeratorLogMag.insertLog(userid, username, usergroupid, usergroupinfo.getGrouptitle(),
							LForumRequest.getIp(), Utils.getNowTime(), forumid, forumname, ((Topics) item).getTid(),
							((Topics) item).getTitle(), operationName, reason);
				}

				if (sendmsg == 1) {
					messagePost(istopic, item, operationName, subjecttype, reason);
				}

			}
		}
	}

	private void messagePost(boolean istopic, Serializable item, String operationName, String subjecttype, String reason) {
		int posterid = -1;
		if (istopic) {
			posterid = ((Topics) item).getUsersByPosterid().getUid();
		} else {
			posterid = ((Posts) item).getUsers().getUid();
		}

		if (posterid != -1) //是游客，管理操作就不发短消息了
		{

			Pms privatemessageinfo = new Pms();

			String curdatetime = Utils.getNowTime();
			// 收件箱
			if (istopic) {
				privatemessageinfo.setMessage("这是由论坛系统自动发送的通知短消息。\r\n以下您所发表的" + subjecttype + "被 "
						+ Utils.cleanHtmlTag(usergroupinfo.getGrouptitle()) + " " + username + " 执行 " + operationName
						+ " 操作。\r\n\r\n" + subjecttype + ": " + ((Topics) item).getTitle().trim() + " \r\n发表时间: "
						+ ((Topics) item).getPostdatetime() + "\r\n所在论坛: " + forumname + "\r\n操作理由: " + reason
						+ "\r\n\r\n如果您对本管理操作有异议，请与我取得联系。");
			} else {
				privatemessageinfo.setMessage("这是由论坛系统自动发送的通知短消息。\r\n以下您所发表的" + subjecttype + "被 "
						+ Utils.cleanHtmlTag(usergroupinfo.getGrouptitle()) + " " + username + " 执行 " + operationName
						+ " 操作。\r\n\r\n" + subjecttype + ": " + ((Posts) item).getTitle().trim() + " \r\n发表时间: "
						+ ((Posts) item).getPostdatetime() + "\r\n所在论坛: " + forumname + "\r\n操作理由: " + reason
						+ "\r\n\r\n如果您对本管理操作有异议，请与我取得联系。");
			}

			privatemessageinfo.setSubject("您发表的" + subjecttype + "被执行管理操作");
			if (istopic) {
				privatemessageinfo.setMsgto(((Topics) item).getPoster());
			} else {
				privatemessageinfo.setMsgto(((Posts) item).getPoster());
			}
			Users users = new Users();
			users.setUid(posterid);

			privatemessageinfo.setUsersByMsgtoid(users);
			privatemessageinfo.setMsgfrom(username);

			Users users2 = new Users();
			users2.setUid(userid);
			privatemessageinfo.setUsersByMsgfromid(users2);
			privatemessageinfo.setNew_(1);
			privatemessageinfo.setPostdatetime(curdatetime);
			privatemessageinfo.setFolder(0);
			messageManager.createPrivateMessage(privatemessageinfo, 0);
		}
	}

	/**
	 * 提升/下沉主题
	 * @return
	 */
	private boolean doBumpTopicsOperation() {
		if (!Utils.isIntArray(topiclist.split(","))) {
			reqcfg.addErrLine("非法的主题ID");
			return false;
		}
		int bumptype = LForumRequest.getParamIntValue("bumptype", 0);
		if (Math.abs(bumptype) != 1) {
			reqcfg.addErrLine("错误的参数");
			return false;
		}
		topicAdminManager.bumpTopics(topiclist, bumptype);
		return true;
	}

	/**
	 * 关闭主题/取消
	 * @return
	 */
	private boolean doCloseOperation() {
		if (!ismoder) {
			reqcfg.addErrLine("您当前的身份 \"" + usergroupinfo.getGrouptitle() + "\" 没有关闭主题的权限");
			return false;
		}
		close = LForumRequest.getParamIntValue("close", -1);
		if (close == -1) {
			//
			reqcfg.addErrLine("您没有选择操作类型(打开/关闭)");
			return false;
		}

		int reval = topicAdminManager.setClose(topiclist, close);
		if (reval < 1) {
			reqcfg.addErrLine("要(打开/关闭)的主题未找到");
			return false;
		}
		return true;
	}

	/**
	 * 设置高亮
	 * @return
	 */
	private boolean doHighlightOperation() {
		if (!ismoder) {
			reqcfg.addErrLine("您当前的身份 \"" + usergroupinfo.getGrouptitle() + "\" 没有设置高亮的权限");
			return false;
		}
		highlight_color = LForumRequest.getParamValue("highlight_color");
		highlight_style_b = LForumRequest.getParamValue("highlight_style_b");
		highlight_style_i = LForumRequest.getParamValue("highlight_style_i");
		highlight_style_u = LForumRequest.getParamValue("highlight_style_u");

		String highlightStyle = "";

		//加粗
		if (!highlight_style_b.equals("")) {
			highlightStyle = highlightStyle + "font-weight:bold;";
		}

		//加斜
		if (!highlight_style_i.equals("")) {
			highlightStyle = highlightStyle + "font-style:italic;";
		}

		//加下划线
		if (!highlight_style_u.equals("")) {
			highlightStyle = highlightStyle + "text-decoration:underline;";
		}

		//设置颜色
		if (!highlight_color.equals("")) {
			highlightStyle = highlightStyle + "color:" + highlight_color + ";";
		}

		if (highlight == -1) {
			//
			reqcfg.addErrLine("您没有选择字体样式及颜色");
			return false;
		}

		topicAdminManager.setHighlight(topiclist, highlightStyle);
		return true;
	}

	/**
	 * 设置精华/取消
	 * @return
	 */
	private boolean doDigestOperation() {
		if (!ismoder) {
			reqcfg.addErrLine("您当前的身份 \"" + usergroupinfo.getGrouptitle() + "\" 没有设置精华的权限");
			return false;
		}
		digest = LForumRequest.getParamIntValue("level", -1);
		if (digest > 3 || digest < 0) {
			digest = -1;
		}
		if (digest == -1) {
			//
			reqcfg.addErrLine("您没有选择精华级别");
			return false;
		}

		topicAdminManager.setDigest(topiclist, digest);
		return true;
	}

	/**
	 * 主题置顶/取消
	 * @param admininfo
	 * @return
	 * @throws IOException 
	 */
	private boolean doDisplayOrderOperation(Admingroups admininfo) throws IOException {
		displayorder = LForumRequest.getParamIntValue("level", -1);
		if (displayorder < 0 || displayorder > 3) {
			reqcfg.addErrLine("置顶参数只能是0到3之间的值");
			return false;
		}
		// 检查用户所在管理组是否具有置顶的管理权限
		if (admininfo.getAdmingid() != 1) {
			if (admininfo.getAllowstickthread() < displayorder) {
				reqcfg.addErrLine("您没有" + displayorder + "级置顶的管理权限");
				return false;
			}
		}

		topicAdminManager.setTopTopicList(forumid, topiclist, displayorder);
		return true;
	}

	/**
	 * 删除帖子
	 * @param reason
	 * @param forum
	 * @return
	 * @throws ParseException 
	 * @throws IOException 
	 */
	private boolean doDelpostsOperation(String reason, Forums forum) throws ParseException, IOException {
		if (!Utils.isInt(topiclist)) {
			reqcfg.addErrLine("无效的主题ID");
			return false;
		}
		Topics topic = topicManager.getTopicInfo(Utils.null2Int(topiclist, 0));
		if (topic == null) {
			reqcfg.addErrLine("不存在的主题");
			return false;
		}

		if (!Utils.isIntArray(postidlist.split(","))) {
			reqcfg.addErrLine("非法的帖子ID");
			return false;
		}

		boolean flag = false;
		for (String postid : postidlist.split(",")) {
			Posts post = postManager.getPostInfo(Utils.null2Int(postid, 0));
			if (post == null || (post.getLayer() <= 0 && topic.getReplies() > 0)
					|| topic.getTid() != post.getTopics().getTid()) {
				reqcfg.addErrLine("ID为" + postid + "的帖子因为对应主题无效或者已被回复,所以无法删除");
				continue;
			}
			int losslessdel = Utils.strDateDiffHours(post.getPostdatetime(), config.getLosslessdel() * 24);
			// 通过验证的用户可以删除帖子
			if (post.getLayer() == 0) {
				topicAdminManager.deleteTopics(topic.getTid().toString(), forum.getRecyclebin(), LForumRequest
						.getParamIntValue("reserveattach", 0) == 1);
				break;
			} else {
				int reval = postManager.deletePost(post.getPid(),
						LForumRequest.getParamIntValue("reserveattach", 0) == 1, true);
				if (reval > 0 && config.getModworkstatus() == 1) {
					String newTitle = post.getMessage().replace(" ", "").replace("\\|", "");
					if (newTitle.length() > 100)
						newTitle = newTitle.substring(0, 100) + "...";
					newTitle = "(pid:" + postid + ")" + post.getTitle() + "|" + newTitle;
					adminModeratorLogMag.insertLog(userid, username, usergroupid, usergroupinfo.getGrouptitle(),
							LForumRequest.getIp(), Utils.getNowTime(), forumid, forumname, topic.getTid(), newTitle,
							operation, reason);
				}
				if (topic.getSpecial() == 4) {
					String opiniontext = "";

					if (opinion != 1 && opinion != 2) {
						reqcfg.addErrLine("参数错误");
						return false;
					}
					switch (opinion) {
					case 1:
						opiniontext = "positivediggs";
						break;
					case 2:
						opiniontext = "negativediggs";
						break;

					}
					postManager.deleteDebatePost(topic.getTid(), opiniontext, Utils.null2Int(postid, -1));
				}

				if (reval > 0 && losslessdel < 0) {
					userCreditManager.updateUserCreditsByPosts(post.getUsers().getUid(), -1);
				}
			}
			flag = true;
		}
		//确保回复数精确

		topicManager.updateTopicReplies(topic.getTid());

		//更新指定版块的最新发帖数信息
		forumManager.updateLastPost(forum);
		return flag;
	}

	/**
	 * 鉴定主题
	 * @return
	 */
	private boolean doIndentifyOperation() {
		int identify = LForumRequest.getParamIntValue("selectidentify", 0);
		if (identify > 0 || identify == -1) {
			topicAdminManager.identifyTopic(topiclist, identify);
			return true;
		} else {
			reqcfg.addErrLine("请选择签定类型");
			return false;
		}
	}

	/**
	 * 删除主题
	 * @param forum
	 * @return
	 * @throws ParseException 
	 * @throws IOException 
	 */
	private boolean doDeleteOperation(Forums forum) throws IOException, ParseException {
		if (!ismoder) {
			reqcfg.addErrLine("您当前的身份 \"" + usergroupinfo.getGrouptitle() + "\" 没有删除的权限");
			return false;
		}
		topicAdminManager.deleteTopics(topiclist, forum.getRecyclebin(), LForumRequest.getParamIntValue(
				"reserveattach", 0) == 1);
		forumManager.setRealCurrentTopics(forum.getFid());

		//更新指定版块的最新发帖数信息
		forumManager.updateLastPost(forum);

		return true;
	}

	/**
	 * 绑定操作的标题
	 * @return
	 */
	private boolean bindTitle() {
		System.out.println("绑定标题");
		if (operation.equals("delete")) {
			operationtitle = "删除主题";
		} else if (operation.equals("move")) {

		} else if (operation.equals("type")) {
			operationtitle = "主题分类";

		} else if (operation.equals("highlight")) { //设置高亮
			operationtitle = "高亮显示";
			donext = 1;

		} else if (operation.equals("close")) {
			operationtitle = "关闭/打开主题";
			donext = 1;

		} else if (operation.equals("displayorder")) { //设置置顶
			operationtitle = "置顶/解除置顶";
			donext = 1;

		} else if (operation.equals("digest")) { //设置精华
			operationtitle = "加入/解除精华 ";
			donext = 1;
		} else if (operation.equals("copy")) {
			operationtitle = "复制主题";
			forumlist = cachesManager.getForumListBoxOptionsCache();
		} else if (operation.equals("split")) {
			operationtitle = "分割主题";
			int tid = Utils.null2Int(topiclist, 0);
			if (tid <= 0) {
				reqcfg.addErrLine("您当前的身份 \"" + usergroupinfo.getGrouptitle() + "\" 没有分割主题的权限");
				return false;
			}
			//			postlist = Posts.GetPostListTitle(tid);
			//			if (postlist != null) {
			//				if (postlist.Rows.Count > 0) {
			//					postlist.Rows[0].Delete();
			//					postlist.AcceptChanges();
			//				}
			//			}
		} else if (operation.equals("merge")) {
			operationtitle = "合并主题";

		} else if (operation.equals("bump")) {
			operationtitle = "提升/下沉主题";

		} else if (operation.equals("repair")) {
			operationtitle = "修复主题";

		} else if (operation.equals("rate")) {
			if (!checkRatePermission()) {
				return false;
			}

			String repost = topicAdminManager.checkRateState(postidlist, userid);
			if (config.getDupkarmarate() != 1 && !repost.equals("") && RateIsReady != 1) {
				reqcfg.addErrLine("对不起,您不能对帖子ID 为(" + repost + ") 的帖子重复评分,请返回.");
				return false;
			}
			scorelist = userGroupManager.groupParticipateScore(userid, usergroupid);
			if (scorelist.size() < 1) {
				reqcfg.addErrLine("您当前的身份 \"" + usergroupinfo.getGrouptitle() + "\" 没有评分的权限, 或者今日可用评分已经用完");
				return false;
			}
			Posts postinfo = postManager.getPostInfo(Utils.null2Int(postidlist, 0));
			if (postinfo == null) {
				reqcfg.addErrLine("您没有选择要评分的帖子, 请返回修改.");
				return false;
			}
			poster = postinfo.getPoster();
			if (postinfo.getUsers().getUid() == userid) {
				reqcfg.addErrLine("对不起,您不能对自已的帖子评分,请返回.");
				return false;
			}

			title = postinfo.getTitle();
			topiclist = postinfo.getTopics().getTid().toString();
			operationtitle = "参与评分";

		} else if (operation.equals("cancelrate")) {
			Posts postinfo = postManager.getPostInfo(Utils.null2Int(postidlist, 0));
			if (postinfo == null) {
				reqcfg.addErrLine("您没有选择要撤消评分的帖子, 请返回修改.");
				return false;
			}

			if (!ismoder) {
				reqcfg.addErrLine("您当前的身份 \"" + usergroupinfo.getGrouptitle() + "\" 没有撤消评分的权限");
				return false;
			}
			poster = postinfo.getPoster();

			title = postinfo.getTitle();
			topiclist = postinfo.getTopics().getTid().toString();
			operationtitle = "撤销评分";

			ratelogcount = adminRateLogManager.recordCount("pid = " + postidlist);
			ratelog = adminRateLogManager.logList(ratelogcount, 1, "pid = " + postidlist);
			String[] scorePaySet = scoresetManager.getValidScoreUnit();

			//绑定积分名称属性
			int index = 0;
			for (Ratelog log : ratelog) {
				int extcredits = log.getExtcredits();
				if ((extcredits > 0) && (extcredits < 9)) {
					if (scorePaySet.length > extcredits) {
						extcreditnames.add(index, scorePaySet[extcredits]);
					} else {
						extcreditnames.add(index, "");
					}
				} else {
					extcreditnames.add(index, "");
				}
				index++;
			}

		} else if (operation.equals("delposts")) {
			operationtitle = "批量删贴";

		} else if (operation.equals("identify")) {
			operationtitle = "鉴定主题";
			identifylist = cachesManager.getTopicIdentifyList();
			identifyjsarray = cachesManager.getTopicIdentifyFileNameJsArray();
		} else if (operation.equals("bonus")) {
			operationtitle = "结帖";
			int tid = Utils.null2Int(topiclist, 0);
			if (postlist != null) {
				if (postlist.size() > 0) {
					postManager.deletePosts(postlist.get(0));
				}
			}
			if (postlist.size() == 0) {
				reqcfg.addErrLine("无法对没有回复的悬赏进行结帖");
			}

			topicinfo = topicManager.getTopicInfo(tid);
			if (topicinfo.getSpecial() == 3) {
				reqcfg.addMsgLine("本主题的悬赏已经结束");
			}
		} else if (operation.equals("banpost")) {
			operationtitle = "单贴屏蔽";
		} else {
			operationtitle = "未知操作";
		}
		return true;
	}

	public String getOperationtitle() {
		return operationtitle;
	}

	public String getOperation() {
		return operation;
	}

	public String getAction() {
		return action;
	}

	public String getTopiclist() {
		return topiclist;
	}

	public String getPostidlist() {
		return postidlist;
	}

	public String getForumname() {
		return forumname;
	}

	public String getForumnav() {
		return forumnav;
	}

	public String getTitle() {
		return title;
	}

	public String getPoster() {
		return poster;
	}

	public int getForumid() {
		return forumid;
	}

	public String getForumlist() {
		return forumlist;
	}

	public int getDisplayorder() {
		return displayorder;
	}

	public int getDigest() {
		return digest;
	}

	public String getHighlight_color() {
		return highlight_color;
	}

	public String getHighlight_style_b() {
		return highlight_style_b;
	}

	public String getHighlight_style_i() {
		return highlight_style_i;
	}

	public String getHighlight_style_u() {
		return highlight_style_u;
	}

	public int getClose() {
		return close;
	}

	public int getMoveto() {
		return moveto;
	}

	public String getType() {
		return type;
	}

	public int getDonext() {
		return donext;
	}

	public List<Posts> getPostlist() {
		return postlist;
	}

	public List<Map<String, Object>> getScorelist() {
		return scorelist;
	}

	public List<Topicidentify> getIdentifylist() {
		return identifylist;
	}

	public String getIdentifyjsarray() {
		return identifyjsarray;
	}

	public String getTopictypeselectoptions() {
		return topictypeselectoptions;
	}

	public List<Ratelog> getRatelog() {
		return ratelog;
	}

	public int getRatelogcount() {
		return ratelogcount;
	}

	public Topics getTopicinfo() {
		return topicinfo;
	}

	public int getOpinion() {
		return opinion;
	}

	public boolean isIsmoder() {
		return ismoder;
	}

	public int getRateIsReady() {
		return RateIsReady;
	}

	public Forums getForum() {
		return forum;
	}

	public int getHighlight() {
		return highlight;
	}

	public boolean isIssendmessage() {
		return issendmessage;
	}

	public boolean isIsreason() {
		return isreason;
	}

	public String getOp() {
		return op;
	}

	public List<String> getExtcreditnames() {
		return extcreditnames;
	}
}
