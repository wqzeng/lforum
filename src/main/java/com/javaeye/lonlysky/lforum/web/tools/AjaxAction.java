package com.javaeye.lonlysky.lforum.web.tools;

import java.text.ParseException;
import java.util.List;

import org.apache.struts2.config.ParentPackage;
import org.springframework.beans.factory.annotation.Autowired;

import com.javaeye.lonlysky.lforum.ForumBaseAction;
import com.javaeye.lonlysky.lforum.GlobalsKeys;
import com.javaeye.lonlysky.lforum.comm.ForumAction;
import com.javaeye.lonlysky.lforum.comm.LForumRequest;
import com.javaeye.lonlysky.lforum.comm.utils.ForumUtils;
import com.javaeye.lonlysky.lforum.comm.utils.UBBUtils;
import com.javaeye.lonlysky.lforum.comm.utils.Utils;
import com.javaeye.lonlysky.lforum.entity.forum.Admingroups;
import com.javaeye.lonlysky.lforum.entity.forum.Forums;
import com.javaeye.lonlysky.lforum.entity.forum.Postid;
import com.javaeye.lonlysky.lforum.entity.forum.PostpramsInfo;
import com.javaeye.lonlysky.lforum.entity.forum.Posts;
import com.javaeye.lonlysky.lforum.entity.forum.Ratelog;
import com.javaeye.lonlysky.lforum.entity.forum.Topics;
import com.javaeye.lonlysky.lforum.entity.forum.Usergroups;
import com.javaeye.lonlysky.lforum.entity.forum.Users;
import com.javaeye.lonlysky.lforum.service.AdminGroupManager;
import com.javaeye.lonlysky.lforum.service.CachesManager;
import com.javaeye.lonlysky.lforum.service.EditorManager;
import com.javaeye.lonlysky.lforum.service.ForumManager;
import com.javaeye.lonlysky.lforum.service.ModeratorManager;
import com.javaeye.lonlysky.lforum.service.PaymentLogManager;
import com.javaeye.lonlysky.lforum.service.PostManager;
import com.javaeye.lonlysky.lforum.service.ScoresetManager;
import com.javaeye.lonlysky.lforum.service.SmilieManager;
import com.javaeye.lonlysky.lforum.service.TopicManager;
import com.javaeye.lonlysky.lforum.service.UserCreditManager;

/**
 * Ajax相关功能操作
 * 
 * @author 黄磊
 *
 */
@ParentPackage("default")
public class AjaxAction extends ForumBaseAction {

	private static final long serialVersionUID = -1635148394012085482L;

	@Autowired
	private ForumManager forumManager;

	@Autowired
	private CachesManager cachesManager;

	@Autowired
	private AdminGroupManager adminGroupManager;

	@Autowired
	private TopicManager topicManager;

	@Autowired
	private PostManager postManager;

	@Autowired
	private ScoresetManager scoresetManager;

	@Autowired
	private ModeratorManager moderatorManager;

	@Autowired
	private UserCreditManager userCreditManager;

	@Autowired
	private PaymentLogManager paymentLogManager;

	@Autowired
	private SmilieManager smilieManager;

	@Autowired
	private EditorManager editorManager;

	/*
	 * (non-Javadoc)
	 * @see com.javaeye.lonlysky.lforum.ForumBaseAction#initAction()
	 */
	@Override
	public String initAction() throws Exception {
		String result = super.initAction();
		if (!result.equals(GlobalsKeys.ACTION_INIT)) {
			return result;
		}

		// 获取请求类型
		String type = LForumRequest.getParamValue("t");
		if (type.equals("forumtree")) {
			// 获得指定版块的子版块信息,以xml文件输出
			getForumTree();
		} else if (type.equals("topictree")) {
			// 获得指定主题的回复信息,以xml文件输出
			getTopicTree();
		} else if (type.equals("checkusername")) {
			// 检测用户名是否存在
			checkUserName();
		} else if (type.equals("smilies")) {
			// 获取表情
			getSmilies();
		} else if (type.equals("quickreply")) {
			//快速回复主题
			quickReply();
		} else if (type.equals("ratelist")) {
			//帖子评分记录
			getRateLogList();
		}
		return null;
	}

	/**
	 * 帖子评分记录
	 */
	private void getRateLogList() {
		StringBuilder xmlnode = new StringBuilder();
		xmlnode.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n");
		//如果不是提交...
		if (!ispost || ForumUtils.isCrossSitePost()) {
			xmlnode
					.append("<error>您的请求来路不正确，无法提交。如果您安装了某种默认屏蔽来路信息的个人防火墙软件(如 Norton Internet Security)，请设置其不要禁止来路信息后再试。</error>");
			responseXML(xmlnode.toString(), false);
			return;
		}

		int pid = LForumRequest.getParamIntValue("pid", 0);

		List<Ratelog> rateList = postManager.getPostRateList(pid);
		if (rateList == null || rateList.size() == 0) {
			xmlnode.append("<error>该帖没有评分记录</error>");
			responseXML(xmlnode.toString(), false);
			return;
		}
		xmlnode.append("<data>\r\n");

		String[] scorename = scoresetManager.getValidScoreName();
		String[] scoreunit = scoresetManager.getValidScoreUnit();

		for (Ratelog rate : rateList) {
			xmlnode.append("<ratelog>");
			xmlnode.append("\r\n\t<rateid>" + rate.getId() + "</rateid>");
			xmlnode.append("\r\n\t<uid>" + rate.getUsers().getUid() + "</uid>");
			xmlnode.append("\r\n\t<username>" + rate.getUsername().trim() + "</username>");
			xmlnode.append("\r\n\t<extcredits>" + rate.getExtcredits() + "</extcredits>");
			xmlnode.append("\r\n\t<extcreditsname>" + scorename[rate.getExtcredits()] + "</extcreditsname>");
			xmlnode.append("\r\n\t<extcreditsunit>" + scoreunit[rate.getExtcredits()] + "</extcreditsunit>");
			xmlnode.append("\r\n\t<postdatetime>" + rate.getPostdatetime() + "</postdatetime>");
			xmlnode.append("\r\n\t<score>" + (rate.getScore() > 0 ? ("+" + rate.getScore()) : rate.getScore())
					+ "</score>");
			xmlnode.append("\r\n\t<reason>" + rate.getReason() + "</reason>");

			xmlnode.append("\r\n</ratelog>\r\n");
		}
		xmlnode.append("</data>");
		responseXML(xmlnode.toString(), false);

	}

	/**
	 * 获得指定主题的回复信息,以xml文件输出
	 */
	private void getTopicTree() {
		int topicid = LForumRequest.getParamIntValue("topicid", 0);

		StringBuilder xmlnode = new StringBuilder();
		xmlnode.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");

		Topics topic = topicManager.getTopicInfo(topicid);
		Forums forum = forumManager.getForumInfo(topic.getForums().getFid());
		if (topic.getReadperm() > usergroupinfo.getReadaccess() && topic.getUsersByPosterid().getUid() != userid
				&& useradminid != 1 && !Utils.inArray(username, forum.getForumfields().getModerators().split(","))) {
			xmlnode.append("<error>本主题阅读权限为: " + topic.getReadperm() + ", 您当前的身份 \"" + usergroupinfo.getGrouptitle()
					+ "\" 阅读权限不够</error>");
			responseXML(xmlnode.toString(), false);
			return;
		}

		xmlnode.append("<data>\n");

		List<Object[]> objList = postManager.getPostTree(topicid);
		if (objList != null) {
			if (objList.size() > 0) {
				for (Object[] objects : objList) {
					xmlnode.append("<post title=\"");
					xmlnode.append(UBBUtils.clearBR(UBBUtils.clearUBB(objects[2].toString())));
					xmlnode.append("\" pid=\"");
					xmlnode.append(objects[0]);
					xmlnode.append("\" message=\"");
					if (UBBUtils.clearBR(UBBUtils.clearUBB(objects[6].toString())).equals("")) {
						xmlnode.append(objects[2]);
					} else {
						String tt = objects[6].toString();
						if (tt.indexOf("[hide]") > -1) {
							xmlnode.append("*** 隐藏帖 ***");
						} else {
							xmlnode.append(UBBUtils.clearBR(UBBUtils.clearUBB(objects[6].toString())));
						}
					}

					xmlnode.append("\" postdatetime=\"");
					xmlnode.append(objects[5]);
					xmlnode.append("\" poster=\"");
					xmlnode.append(objects[3]);
					xmlnode.append("\" posterid=\"");
					xmlnode.append(objects[4]);
					xmlnode.append("\" />\n");
				}
			}
		}
		xmlnode.append("</data>\n");
		responseXML(xmlnode.toString().replace("&", ""), false);
	}

	/**
	 * 快速回复
	 * @throws ParseException 
	 */
	private void quickReply() throws ParseException {
		Topics topic;
		int forumid;
		String forumname;
		int topicid;
		int postid = 0;
		String topictitle;

		int smileyoff;

		Forums forum;

		StringBuilder xmlnode = new StringBuilder();
		xmlnode.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");

		// 获取主题ID
		topicid = LForumRequest.getParamIntValue("topicid", -1);
		Posts postinfo;
		int layer = 1;
		int parentid = 0;
		topictitle = "";

		Admingroups admininfo = adminGroupManager.getAdminGroup(useradminid);

		// 获取该主题的信息
		topic = topicManager.getTopicInfo(topicid);

		forumid = topic.getForums().getFid();

		forum = forumManager.getForumInfo(forumid);
		forumname = forum.getName().trim();

		String postmessage = LForumRequest.getParamValue("message").trim();

		if (!isQuickReplyValid(xmlnode, topicid, topic, forum, admininfo, postmessage)) {
			responseXML(xmlnode.toString(), false);
			return;
		}

		smileyoff = 1 - forum.getAllowsmilies();

		// 在线状态如果为精确, 则更新用户动作, 否则不更新
		onlineUserManager.updateAction(olid, ForumAction.PostReply.ACTION_ID, forumid, forumname, topicid, topictitle,
				config.getOnlinetimeout());

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
		Users user = new Users();
		user.setUid(userid);
		postinfo.setUsers(user);

		postinfo.setPostdatetime(curdatetime);
		if (useradminid == 1) {
			postinfo.setTitle(LForumRequest.getParamValue("title"));
			postinfo.setMessage(postmessage);
		} else {
			postinfo.setTitle(cachesManager.banWordFilter(LForumRequest.getParamValue("title")));
			postinfo.setMessage(cachesManager.banWordFilter(postmessage));
		}

		if (cachesManager.hasBannedWord(postinfo.getTitle()) || cachesManager.hasBannedWord(postinfo.getMessage())) {
			reqcfg.addErrLine("对不起, 您提交的内容包含不良信息, 因此无法提交, 请返回修改!");
			return;
		}

		postinfo.setIp(LForumRequest.getIp());
		postinfo.setLastedit("");

		//		int disablepost = 0;
		//		if (admininfo != null) {
		//			disablepost = admininfo.getDisablepostctrl();
		//		}
		//判断当前版块以及用户所属组的审核设置来确定贴子是否需要审核
		if (forum.getModnewposts() == 1 && useradminid != 1) {
			postinfo.setInvisible(1);
		} else {
			postinfo.setInvisible(0);
		}

		//　如果当前用户非管理员并且论坛设定了发帖审核时间段，当前时间如果在其中的一个时间段内，则用户所发帖均为待审核状态
		if (useradminid != 1) {
			if (scoresetManager.betweenTime(config.getPostmodperiods()).equals("")) {
				postinfo.setInvisible(1);
			}

			if (cachesManager.hasAuditWord(postinfo.getTitle()) || cachesManager.hasAuditWord(postinfo.getMessage())) {
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
		try {
			postManager.createPost(postinfo);
			postid = postinfo.getPid();
		} catch (Exception e) {
			responseXML(xmlnode.append("<error>提交失败,请稍后重试！</error>").toString(), false);
			return;
		}

		if (postinfo.getInvisible() == 1) {
			responseXML(xmlnode.append("<error>发表回复成功, 但需要经过审核才可以显示!</error>").toString(), false);
			return;
		}

		if (hide == 1) {
			topic.setHide(hide);
			topicManager.updateTopicHide(topicid);
		}
		//topicManager.updateTopicReplies(topicid);
		topicManager.addParentForumTopics(forum.getParentidlist().trim(), 0, 1);

		//设置用户的积分
		///首先读取版块内自定义积分
		///版设置了自定义积分则使用,否则使用论坛默认积分
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

		if (values != null) {
			///使用版块内积分
			userCreditManager.updateUserCreditsByPosts(userid, values);

		} else {
			///使用默认积分
			userCreditManager.updateUserCreditsByPosts(userid);
		}

		// 更新在线表中的用户最后发帖时间
		onlineUserManager.updatePostTime(olid);

		xmlnode = getNewPostXML(xmlnode, postinfo, forum, topic, postid);

		// 删除主题游客缓存
		if (topic.getReplies() < (config.getPpp() + 10)) {
			//
		}

		responseXML(xmlnode.toString(), false);

	}

	/**
	 * 得到输出新帖子xml字符串
	 * @param xmlnode xml节点
	 * @param postinfo 帖子信息
	 * @param forum 版块信息
	 * @param topic 主题信息
	 * @param postid 帖子id
	 * @return xml结果
	 */
	private StringBuilder getNewPostXML(StringBuilder xmlnode, Posts postinfo, Forums forum, Topics topic, int postid) {
		int hide = 1;
		if (topic.getHide() == 1 || ForumUtils.isHidePost(postinfo.getMessage())) {
			hide = -1;
		}
		if (usergroupinfo.getAllowhidecode() == 0)
			hide = 0;
		//判断是否为回复可见帖, price=0为非购买可见(正常), price > 0 为购买可见, price=-1为购买可见但当前用户已购买
		int price = 0;
		if (topic.getPrice() > 0) {
			price = topic.getPrice();
			if (paymentLogManager.isBuyer(topic.getTid(), userid))//判断当前用户是否已经购买
			{
				price = -1;
			}
		}

		PostpramsInfo postpramsinfo = new PostpramsInfo();
		postpramsinfo.setFid(forum.getFid());
		postpramsinfo.setTid(postinfo.getTopics().getTid());
		postpramsinfo.setPid(postinfo.getPid());
		postpramsinfo.setJammer(forum.getJammer());
		postpramsinfo.setPagesize(1);
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
		postpramsinfo.setSmileyoff(postinfo.getSmileyoff());
		postpramsinfo.setBbcodeoff(postinfo.getBbcodeoff());
		postpramsinfo.setParseurloff(postinfo.getParseurloff());
		postpramsinfo.setAllowhtml(postinfo.getHtmlon());

		postpramsinfo.setSdetail(postinfo.getMessage());

		String message = UBBUtils.uBBToHTML(postpramsinfo);

		Users userinfo = userManager.getUserInfo(userid);

		//头衔、星星
		Usergroups tmpUserGroupInfo = userGroupManager.getUsergroup(Utils.null2Int(usergroupid, userCreditManager
				.getCreditsUserGroupID(userinfo.getCredits()).getGroupid()));
		String status = tmpUserGroupInfo.getGrouptitle();
		if (!tmpUserGroupInfo.getColor().trim().equals("")) {
			status = "<font color=\"" + tmpUserGroupInfo.getColor() + "\">" + tmpUserGroupInfo.getGrouptitle()
					+ "</font>";
		}
		int stars = tmpUserGroupInfo.getStars();
		String medals;
		medals = "".equals(userinfo.getUserfields().getMedals().trim()) ? "" : cachesManager.getMedalsList(userinfo
				.getUserfields().getMedals());

		xmlnode.append("<post>\r\n\t");

		xmlnode.append("<ismoder>"
				+ (moderatorManager.isModer(useradminid, userid, topic.getForums().getFid()) ? 1 : 0) + "</ismoder>");
		xmlnode.append("");
		xmlnode.append("<id>" + (topic.getReplies() + 2) + "</id>\r\n\t");
		xmlnode.append("<status><![CDATA[" + status + "]]></status>\r\n\t");
		xmlnode.append("<stars>" + stars + "</stars>\r\n\t");

		xmlnode.append("<fid>" + postinfo.getForums().getFid() + "</fid>\r\n\t");
		xmlnode.append("<invisible>" + postinfo.getInvisible() + "</invisible>\r\n\t");
		xmlnode.append("<ip>" + postinfo.getIp() + "</ip>\r\n\t");
		xmlnode.append("<lastedit>" + postinfo.getLastedit() + "</lastedit>\r\n\t");
		xmlnode.append("<layer>" + postinfo.getLayer() + "</layer>\r\n\t");
		xmlnode.append("<message><![CDATA[" + message + "]]></message>\r\n\t");
		xmlnode.append("<parentid>" + postinfo.getPostidByParentid().getPid() + "</parentid>\r\n\t");
		xmlnode.append("<pid>" + postid + "</pid>\r\n\t");
		xmlnode.append("<postdatetime>"
				+ postinfo.getPostdatetime().substring(0, postinfo.getPostdatetime().length() - 3)
				+ "</postdatetime>\r\n\t");
		xmlnode.append("<poster>" + postinfo.getPoster() + "</poster>\r\n\t");
		xmlnode.append("<posterid>" + postinfo.getUsers().getUid() + "</posterid>\r\n\t");
		xmlnode.append("<smileyoff>" + postinfo.getSmileyoff() + "</smileyoff>\r\n\t");
		xmlnode.append("<topicid>" + postinfo.getTopics().getTid() + "</topicid>\r\n\t");
		xmlnode.append("<title>" + postinfo.getTitle() + "</title>\r\n\t");
		xmlnode.append("<usesig>" + postinfo.getUsesig() + "</usesig>\r\n");
		xmlnode.append("<debateopinion>0</debateopinion>");

		xmlnode.append("<uid>" + userinfo.getUid() + "</uid>\r\n\t");
		xmlnode.append("<accessmasks>" + userinfo.getAccessmasks() + "</accessmasks>\r\n\t");
		xmlnode.append("<adminid>" + userinfo.getAdmingroups().getAdmingid() + "</adminid>\r\n\t");
		xmlnode.append("<avatar>" + userinfo.getUserfields().getAvatar().replace("\\", "/") + "</avatar>\r\n\t");
		xmlnode.append("<avatarheight>" + userinfo.getUserfields().getAvatarheight() + "</avatarheight>\r\n\t");
		xmlnode.append("<avatarshowid>" + userinfo.getAvatarshowid() + "</avatarshowid>\r\n\t");
		xmlnode.append("<avatarwidth>" + userinfo.getUserfields().getAvatarwidth() + "</avatarwidth>\r\n\t");
		xmlnode.append("<bday>" + userinfo.getBday() + "</bday>\r\n\t");
		//xmlnode.append("<bio>{0}</bio>\r\n\t", userinfo.Bio);
		xmlnode.append("<credits>" + userinfo.getCredits() + "</credits>\r\n\t");
		xmlnode.append("<digestposts>" + userinfo.getDigestposts() + "</digestposts>\r\n\t");
		xmlnode.append("<email>" + userinfo.getEmail().trim() + "</email>\r\n\t");

		String[] score = scoresetManager.getValidScoreName();
		xmlnode.append("<score1>" + score[0] + "</score1>\r\n\t");
		xmlnode.append("<score2>" + score[1] + "</score2>\r\n\t");
		xmlnode.append("<score3>" + score[2] + "</score3>\r\n\t");
		xmlnode.append("<score4>" + score[3] + "</score4>\r\n\t");
		xmlnode.append("<score5>" + score[4] + "</score5>\r\n\t");
		xmlnode.append("<score6>" + score[5] + "</score6>\r\n\t");
		xmlnode.append("<score7>" + score[6] + "</score7>\r\n\t");
		xmlnode.append("<score8>" + score[7] + "</score8>\r\n\t");
		String[] scoreunit = scoresetManager.getValidScoreUnit();
		xmlnode.append("<scoreunit1>" + scoreunit[0] + "</scoreunit1>\r\n\t");
		xmlnode.append("<scoreunit2>" + scoreunit[1] + "</scoreunit2>\r\n\t");
		xmlnode.append("<scoreunit3>" + scoreunit[2] + "</scoreunit3>\r\n\t");
		xmlnode.append("<scoreunit4>" + scoreunit[3] + "</scoreunit4>\r\n\t");
		xmlnode.append("<scoreunit5>" + scoreunit[4] + "</scoreunit5>\r\n\t");
		xmlnode.append("<scoreunit6>" + scoreunit[5] + "</scoreunit6>\r\n\t");
		xmlnode.append("<scoreunit7>" + scoreunit[6] + "</scoreunit7>\r\n\t");
		xmlnode.append("<scoreunit8>" + scoreunit[7] + "</scoreunit8>\r\n\t");

		xmlnode.append("<extcredits1>" + userinfo.getExtcredits1() + "</extcredits1>\r\n\t");
		xmlnode.append("<extcredits2>" + userinfo.getExtcredits2() + "</extcredits2>\r\n\t");
		xmlnode.append("<extcredits3>" + userinfo.getExtcredits3() + "</extcredits3>\r\n\t");
		xmlnode.append("<extcredits4>" + userinfo.getExtcredits4() + "</extcredits4>\r\n\t");
		xmlnode.append("<extcredits5>" + userinfo.getExtcredits5() + "</extcredits5>\r\n\t");
		xmlnode.append("<extcredits6>" + userinfo.getExtcredits6() + "</extcredits6>\r\n\t");
		xmlnode.append("<extcredits7>" + userinfo.getExtcredits7() + "</extcredits7>\r\n\t");
		xmlnode.append("<extcredits8>" + userinfo.getExtcredits8() + "</extcredits8>\r\n\t");
		xmlnode.append("<extgroupids>" + userinfo.getExtgroupids().trim() + "</extgroupids>\r\n\t");
		xmlnode.append("<gender>" + userinfo.getGender() + "</gender>\r\n\t");
		xmlnode.append("<icq>" + userinfo.getUserfields().getIcq() + "</icq>\r\n\t");
		xmlnode.append("<joindate>" + userinfo.getJoindate() + "</joindate>\r\n\t");
		xmlnode.append("<lastactivity>" + userinfo.getLastactivity() + "</lastactivity>\r\n\t");
		xmlnode.append("<medals><![CDATA[" + medals + "]]></medals>\r\n\t");
		xmlnode.append("<nickname>" + userinfo.getNickname() + "</nickname>\r\n\t");
		xmlnode.append("<oltime>" + userinfo.getOltime() + "</oltime>\r\n\t");
		xmlnode.append("<onlinestate>" + userinfo.getOnlinestate() + "</onlinestate>\r\n\t");
		xmlnode.append("<showemail>" + userinfo.getShowemail() + "</showemail>\r\n\t");
		xmlnode.append("<signature><![CDATA[" + userinfo.getUserfields().getSightml() + "]]></signature>\r\n\t");
		xmlnode.append("<sigstatus>" + userinfo.getSigstatus() + "</sigstatus>\r\n\t");
		xmlnode.append("<skype>" + userinfo.getUserfields().getSkype() + "</skype>\r\n\t");
		xmlnode.append("<website>" + userinfo.getUserfields().getWebsite() + "</website>\r\n\t");
		xmlnode.append("<yahoo>" + userinfo.getUserfields().getYahoo() + "</yahoo>\r\n");
		xmlnode.append("<qq>" + userinfo.getUserfields().getQq() + "</qq>\r\n");
		xmlnode.append("<msn>" + userinfo.getUserfields().getMsn() + "</msn>\r\n");
		xmlnode.append("<posts>" + userinfo.getPosts() + "</posts>\r\n");
		xmlnode.append("<location>" + userinfo.getUserfields().getLocation() + "</location>\r\n");

		xmlnode.append("<showavatars>" + config.getShowavatars() + "</showavatars>\r\n");
		xmlnode.append("<userstatusby>" + config.getUserstatusby() + "</userstatusby>\r\n");
		xmlnode.append("<starthreshold>" + config.getStarthreshold() + "</starthreshold>\r\n");
		xmlnode.append("<forumtitle>" + config.getForumtitle() + "</forumtitle>\r\n");
		xmlnode.append("<showsignatures>" + config.getShowsignatures() + "</showsignatures>\r\n");
		xmlnode.append("<maxsigrows>" + config.getMaxsigrows() + "</maxsigrows>\r\n");
		xmlnode.append("</post>\r\n");

		return xmlnode;
	}

	/**
	 * 验证回帖的条件
	 * @param xmlnode xml节点
	 * @param topicid 主题id
	 * @param topic 主题信息
	 * @param forum 版块信息
	 * @param admininfo 管理组信息
	 * @param postmessage 帖子内容
	 * @return 验证结果
	 * @throws ParseException 
	 */
	private boolean isQuickReplyValid(StringBuilder xmlnode, int topicid, Topics topic, Forums forum,
			Admingroups admininfo, String postmessage) throws ParseException {

		//如果不是提交...
		if (!ispost || ForumUtils.isCrossSitePost()) {
			xmlnode
					.append("<error>您的请求来路不正确，无法提交。如果您安装了某种默认屏蔽来路信息的个人防火墙软件(如 Norton Internet Security)，请设置其不要禁止来路信息后再试。</error>");
			return false;
		}

		//　如果当前用户非管理员并且论坛设定了禁止发帖时间段,当前时间如果在其中的一个时间段内,不允许用户发帖
		if (useradminid != 1 && usergroupinfo.getDisableperiodctrl() != 1) {
			String visittime = scoresetManager.betweenTime(config.getPostbanperiods());
			if (!visittime.equals("")) {
				xmlnode.append("<error>在此时间段( " + visittime + " )内用户不可以发帖</error>");
				return false;
			}
		}

		// 如果主题ID非数字
		if (topicid == -1) {
			xmlnode.append("<error>无效的主题ID</error>");
			return false;
		}

		// 如果该主题不存在
		if (topic == null) {
			xmlnode.append("<error>不存在的主题ID</error>");
			return false;
		}

		//　如果当前用户非管理员并且该主题已关闭,不允许用户发帖
		if (admininfo == null || !moderatorManager.isModer(admininfo.getAdmingid(), userid, forum.getFid())) {
			if (topic.getClosed() == 1) {
				xmlnode.append("<error>主题已关闭无法回复</error>");
				return false;
			}
		}

		if (topic.getReadperm() > usergroupinfo.getReadaccess() && topic.getUsersByPosterid().getUid() != userid
				&& useradminid != 1 && !Utils.inArray(username, forum.getForumfields().getModerators().split(","))) {
			xmlnode.append("<error>本主题阅读权限为: " + topic.getReadperm() + ", 您当前的身份 \"" + usergroupinfo.getGrouptitle()
					+ "\" 阅读权限不够</error>");
			return false;
		}

		if (Utils.null2String(forum.getForumfields().getReplyperm()).equals(""))//板块权限设置为空时,根据用户组权限判断
		{
			// 验证用户是否有发表主题的权限
			if (usergroupinfo.getAllowreply() != 1) {
				xmlnode.append("<error>您当前的身份 \"" + usergroupinfo.getGrouptitle() + "\" 没有发表回复的权限</error>");
				return false;
			}
		} else//板块权限不为空,根据板块权限判断
		{
			if (!forumManager.allowReply(forum.getForumfields().getReplyperm(), usergroupid)) {
				xmlnode.append("<error>您没有在该版块发表回复的权限</error>");
				return false;
			}
		}

		if (admininfo == null || admininfo.getDisablepostctrl() != 1) {

			int interval = Utils.strDateDiffSeconds(lastposttime, config.getPostinterval());
			if (interval < 0) {
				xmlnode.append("<error>系统规定发帖间隔为" + config.getPostinterval() + "秒, 您还需要等待 " + (interval * -1)
						+ " 秒</error>");
				return false;
			} else if (userid != -1) {
				String joindate = userManager.getUserJoinDate(userid);
				if (joindate == "") {
					xmlnode.append("<error>您的用户资料出现错误</error>");
					return false;
				}

				interval = Utils.strDateDiffMinutes(joindate, config.getNewbiespan());
				if (interval < 0) {
					xmlnode.append("<error>系统规定新注册用户必须要在" + config.getNewbiespan() + "分钟后才可以发帖, 您还需要等待 "
							+ (interval * -1) + " 分</error>");
					return false;
				}

			}
		}

		if (LForumRequest.getParamValue("title").indexOf("　") != -1) {
			xmlnode.append("<error>主题不能包含全角空格符</error>");
			return false;
		}
		if (LForumRequest.getParamValue("title").length() > 60) {
			xmlnode.append("<error>主题最大长度为60个字符,当前为 " + LForumRequest.getParamValue("title").length() + " 个字符</error>");
			return false;
		}

		if ("".equals(postmessage)) {
			xmlnode.append("<error>内容不能为空</error>");
			return false;
		}

		if (admininfo != null && admininfo.getDisablepostctrl() != 1) {
			if (postmessage.length() < config.getMinpostsize()) {
				xmlnode.append("<error>您发表的内容过少, 系统设置要求帖子内容不得少于 " + config.getMinpostsize() + " 字多于 "
						+ config.getMaxpostsize() + " 字</error>");
				return false;
			} else if (postmessage.length() > config.getMaxpostsize()) {
				xmlnode.append("<error>您发表的内容过多, 系统设置要求帖子内容不得少于 " + config.getMinpostsize() + " 字多于 "
						+ config.getMaxpostsize() + " 字</error>");
				return false;
			}
		}

		if (topic.getSpecial() == 4 && LForumRequest.getParamIntValue("debateopinion", 0) == 0) {
			xmlnode.append("<error>请选择您在辩论中的观点</error>");
			return false;
		}

		return true;
	}

	/**
	 * 输出表情字符串
	 */
	private void getSmilies() {
		//如果不是提交...
		if (ForumUtils.isCrossSitePost()) {
			return;
		}
		String smilies = cachesManager.getSmiliesCache();
		renderText("{" + smilies + "}");
	}

	/**
	 * 查询用户名是否存在
	 */
	private void checkUserName() {
		if (LForumRequest.getParamValue("username").equals("")) {
			return;
		}
		String result = "0";
		String tmpUsername = LForumRequest.getParamValue("username");
		if (tmpUsername.indexOf("　") != -1) {
			//用户名中不允许包含全角空格符
			result = "1";
		} else if (tmpUsername.indexOf(" ") != -1) {
			//用户名中不允许包含空格
			result = "1";
		} else if (tmpUsername.indexOf(":") != -1) {
			//用户名中不允许包含冒号
			result = "1";
		} else if (userManager.exists(tmpUsername)) {
			//该用户名已存在;
			result = "1";
		} else if ((!Utils.isSafeSqlString(tmpUsername)) || (!Utils.isSafeUserInfoString(tmpUsername))) {
			//用户名中存在非法字符
			result = "1";
		}

		StringBuilder xmlnode = new StringBuilder();
		xmlnode.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
		xmlnode.append("<result>");
		xmlnode.append(result);
		xmlnode.append("</result>");
		responseXML(xmlnode.toString(), false);
	}

	/**
	 * 获得指定版块的子版块信息,以xml文件输出
	 */
	private void getForumTree() {
		StringBuilder xmlnode = new StringBuilder();

		xmlnode.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
		xmlnode.append("<data>\n");

		int fid = LForumRequest.getParamIntValue("fid", 0);
		List<Forums> forumList = forumManager.getForumList(fid);
		for (Forums forums : forumList) {
			if (config.getHideprivate() == 1 && !forums.getForumfields().getViewperm().equals("")
					&& !Utils.inArray(usergroupid + "", forums.getForumfields().getViewperm())) {
				continue;
			}
			xmlnode.append("<forum name=\"");
			xmlnode.append(Utils.cleanHtmlTag(forums.getName().trim()).replace("&", "&amp;"));
			xmlnode.append("\" fid=\"");
			xmlnode.append(forums.getFid());
			xmlnode.append("\" subforumcount=\"");
			xmlnode.append(forums.getSubforumcount());
			xmlnode.append("\" layer=\"");
			xmlnode.append(forums.getLayer());
			xmlnode.append("\" parentid=\"");
			xmlnode.append(forums.getForums().getFid());
			xmlnode.append("\" parentidlist=\"");
			xmlnode.append(forums.getParentidlist());
			xmlnode.append("\" />\n");
		}
		xmlnode.append("</data>\n");

		responseXML(xmlnode.toString(), false);
	}
}
