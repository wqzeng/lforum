package com.javaeye.lonlysky.lforum.web;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.struts2.config.ParentPackage;
import org.springframework.beans.factory.annotation.Autowired;

import com.javaeye.lonlysky.lforum.ForumBaseAction;
import com.javaeye.lonlysky.lforum.comm.LForumRequest;
import com.javaeye.lonlysky.lforum.comm.MD5;
import com.javaeye.lonlysky.lforum.comm.utils.ForumUtils;
import com.javaeye.lonlysky.lforum.comm.utils.Utils;
import com.javaeye.lonlysky.lforum.entity.forum.Forums;
import com.javaeye.lonlysky.lforum.entity.forum.Paymentlog;
import com.javaeye.lonlysky.lforum.entity.forum.PostpramsInfo;
import com.javaeye.lonlysky.lforum.entity.forum.Posts;
import com.javaeye.lonlysky.lforum.entity.forum.ShowtopicPagePostInfo;
import com.javaeye.lonlysky.lforum.entity.forum.Topics;
import com.javaeye.lonlysky.lforum.entity.forum.UserExtcreditsInfo;
import com.javaeye.lonlysky.lforum.entity.forum.Users;
import com.javaeye.lonlysky.lforum.service.EditorManager;
import com.javaeye.lonlysky.lforum.service.ForumManager;
import com.javaeye.lonlysky.lforum.service.ModeratorManager;
import com.javaeye.lonlysky.lforum.service.PaymentLogManager;
import com.javaeye.lonlysky.lforum.service.PostManager;
import com.javaeye.lonlysky.lforum.service.ScoresetManager;
import com.javaeye.lonlysky.lforum.service.SmilieManager;
import com.javaeye.lonlysky.lforum.service.TopicManager;

/**
 * 购买主题页面类
 * 
 * @author 黄磊
 *
 */
@ParentPackage("default")
public class BuytopicAction extends ForumBaseAction {

	private static final long serialVersionUID = 4229048802348452351L;

	/**
	 * 所要购买的主题信息
	 */
	private Topics topic;

	/**
	 * 最后5个回复列表
	 */
	private List<Posts> lastpostlist;

	/**
	 * 已购买的支付记录列表
	 */
	private List<Paymentlog> paymentloglist;

	/**
	 * 用户积分策略信息
	 */
	private UserExtcreditsInfo userextcreditsinfo;

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
	 * 论坛导航信息
	 */
	private String forumnav;

	/**
	 * 当前页码
	 */
	private int pageid;

	/**
	 * 主题购买总次数
	 */
	private int buyers;

	/**
	 * 分页总数
	 */
	private int pagecount;

	/**
	 * 页码链接字串
	 */
	private String pagenumbers;

	/**
	 * 主题标题
	 */
	private String topictitle;

	/**
	 * 是否显示购买信息列表
	 */
	private int showpayments;

	/**
	 * 在判断此值等于1时显示点击购买主题后的确认购买界面
	 */
	private int buyit;

	/**
	 * 主题售价
	 */
	private int topicprice;

	/**
	 * 作者所得
	 */
	private float netamount;

	/**
	 * 单个主题最高收入
	 */
	private int maxincpertopic;

	/**
	 * 单个主题最高出售时限(小时)
	 */
	private int maxchargespan;

	/**
	 * 积分交易税
	 */
	private float creditstax;

	/**
	 * 主题售价
	 */
	private int price;

	/**
	 * 主题作者Id
	 */
	private int posterid;

	/**
	 * 主题作者用户名
	 */
	private String poster;

	/**
	 * 购买后余额
	 */
	private double userlastprice;
	private boolean needlogin = false;

	private ShowtopicPagePostInfo postinfo;
	private String postmessage = "";
	private Pattern pattern = Pattern.compile("\\s*\\[free\\][\n\r]*([\\s\\S]+?)[\n\r]*\\[\\/free\\]\\s*");

	private int ismoder = 0;
	private int pagesize = 16;
	private final String NO_PERMISSION = "您无权购买本主题";
	private final String UNKNOWN_REASON = "未知原因,交易无法进行,给您带来的不方便我们很抱歉";
	private final String NOT_ENOUGH_MONEY = "对不起,您的账户余额少于交易额,无法进行交易";
	private final String PURCHASE_SUCCESS = "购买主题成功,返回该主题";
	private final String WRONG_TOPIC = "无效的主题ID";
	private final String NOT_EXIST_TOPIC = "不存在的主题ID";

	@Autowired
	private TopicManager topicManager;

	@Autowired
	private ForumManager forumManager;

	@Autowired
	private PostManager postManager;

	@Autowired
	private PaymentLogManager paymentLogManager;

	@Autowired
	private ScoresetManager scoresetManager;

	@Autowired
	private ModeratorManager moderatorManager;

	@Autowired
	private SmilieManager smilieManager;

	@Autowired
	private EditorManager editorManager;

	@Override
	public String execute() throws Exception {
		topictitle = "";
		forumnav = "";
		showpayments = LForumRequest.getParamIntValue("showpayments", 0);
		buyit = LForumRequest.getParamIntValue("buyit", 0);
		topicid = LForumRequest.getParamIntValue("topicid", -1);
		// 如果主题ID非数字
		if (topicid == -1) {
			reqcfg.addErrLine(WRONG_TOPIC);
			return SUCCESS;
		}

		// 获取该主题的信息
		topic = topicManager.getTopicInfo(topicid);
		// 如果该主题不存在
		if (topic == null) {
			reqcfg.addErrLine(NOT_EXIST_TOPIC);
			return SUCCESS;
		}

		if (topic.getUsersByPosterid().getUid() == userid) {
			response.sendRedirect("showtopic.action?topicid=" + topicid);
			return null;
		}

		if (topic.getPrice() <= 0) {
			response.sendRedirect("showtopic.action?topicid=" + topicid);
			return null;
		}

		topictitle = topic.getTitle().trim();
		topicprice = topic.getPrice();
		poster = topic.getPoster();
		posterid = topic.getUsersByPosterid().getUid();
		pagetitle = topictitle;
		forumid = topic.getForums().getFid();
		Forums forum = forumManager.getForumInfo(forumid);
		forumname = forum.getName().trim();
		forumnav = forum.getPathlist().trim();
		postinfo = postManager.getSinglePost(topicid);
		if (postinfo.getMessage().toLowerCase().contains("[free]")
				|| postinfo.getMessage().toLowerCase().contains("[/free]")) {
			Matcher m = pattern.matcher(postinfo.getMessage());
			while (m.find()) {
				postmessage += "<br /><div class=\"msgheader\">免费内容:</div><div class=\"msgborder\">" + m.group(1)
						+ "</div><br />";

			}

		}
		//判断是否为回复可见帖, price=0为非购买可见(正常), price>0 为购买可见, price=-1为购买可见但当前用户已购买
		price = 0;
		if (topic.getPrice() > 0) {
			price = topic.getPrice();
			if (paymentLogManager.isBuyer(topicid, userid)
					|| (Utils.strDateDiffHours(topic.getPostdatetime(), scoresetManager.getMaxChargeSpan()) > 0 && scoresetManager
							.getMaxChargeSpan() != 0)) { //判断当前用户是否已经购买
				price = -1;
			}
		}

		if (useradminid != 0) {
			ismoder = moderatorManager.isModer(useradminid, userid, forumid) ? 1 : 0;
		}

		if (topic.getReadperm() > usergroupinfo.getReadaccess() && topic.getUsersByPosterid().getUid() != userid
				&& useradminid != 1 && ismoder != 1) {
			reqcfg.addErrLine("本主题阅读权限为: " + topic.getReadperm() + ", 您当前的身份 \"" + usergroupinfo.getGrouptitle()
					+ "\" 阅读权限不够");
			return SUCCESS;
		}

		if (topic.getDisplayorder() == -1) {
			reqcfg.addErrLine("此主题已被删除！");
			return SUCCESS;
		}

		if (topic.getDisplayorder() == -2) {
			reqcfg.addErrLine("此主题未经审核！");
			return SUCCESS;
		}

		if (!forum.getForumfields().getPassword().equals("")
				&& !MD5.encode(forum.getForumfields().getPassword()).equals(
						ForumUtils.getCookie("forum" + forumid + "password"))) {
			response.sendRedirect("showforum.action?forumid=" + forumid);
			return null;
		}

		if (!forumManager.allowViewByUserID(forum.getForumfields().getPermuserlist().trim(), userid)) //判断当前用户在当前版块浏览权限
		{
			if (forum.getForumfields().getViewperm().equals("")) { //当板块权限为空时，按照用户组权限
				if (usergroupinfo.getAllowvisit() != 1) {
					reqcfg.addErrLine("您当前的身份 \"" + usergroupinfo.getGrouptitle() + "\" 没有浏览该版块的权限");
					return SUCCESS;
				}
			} else //当板块权限不为空，按照板块权限
			{
				if (!forumManager.allowView(forum.getForumfields().getViewperm().trim(), usergroupid)) {
					reqcfg.addErrLine("您没有浏览该版块的权限");
					return SUCCESS;
				}
			}
		}

		userextcreditsinfo = scoresetManager.getScoreSet(scoresetManager.getCreditsTrans());
		maxincpertopic = scoresetManager.getMaxIncPerTopic();
		maxchargespan = scoresetManager.getMaxChargeSpan();
		creditstax = scoresetManager.getCreditsTax() * 100;

		netamount = topicprice - topicprice * creditstax / 100;
		if (topicprice > maxincpertopic) {
			netamount = maxincpertopic - maxincpertopic * creditstax / 100;
		}

		if (price != -1) {
			Users users = userManager.getUserInfo(userid);
			if (users == null) {
				reqcfg.addErrLine(NO_PERMISSION);
				needlogin = true;
				return SUCCESS;
			}
			double score = 0;
			switch (scoresetManager.getCreditsTrans()) {
			case 1:
				score = users.getExtcredits1();
				break;
			case 2:
				score = users.getExtcredits2();
				break;
			case 3:
				score = users.getExtcredits3();
				break;
			case 4:
				score = users.getExtcredits4();
				break;
			case 5:
				score = users.getExtcredits5();
				break;
			case 6:
				score = users.getExtcredits6();
				break;
			case 7:
				score = users.getExtcredits7();
				break;
			case 8:
				score = users.getExtcredits8();
				break;
			}
			if (score < topic.getPrice()) {
				reqcfg.addErrLine("对不起,您的账户余额 <span class=\"bold\">" + score + "</span> 少于交易额 " + topic.getPrice()
						+ " ,无法进行交易");
				return SUCCESS;
			}

			userlastprice = score - topic.getPrice();
		}

		//如果不是提交...
		if (!ispost) {
			buyers = paymentLogManager.getPaymentLogByTidCount(topic.getTid());

			//显示购买信息列表
			if (showpayments == 1) {
				//得到当前用户请求的页数
				pageid = LForumRequest.getParamIntValue("page", 1);
				//获取主题总数
				//获取总页数
				pagecount = buyers % pagesize == 0 ? buyers / pagesize : buyers / pagesize + 1;
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

				//获取收入记录并分页显示
				paymentloglist = paymentLogManager.getPaymentLogByTid(pagesize, pageid, topic.getTid());
			}

			//判断是否为回复可见帖, hide=0为非回复可见(正常), hide>0为回复可见, hide=-1为回复可见但当前用户已回复
			int hide = 0;
			if (topic.getHide() == 1) {
				hide = topic.getHide();
				if (postManager.isReplier(topicid, userid)) {
					hide = -1;
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

			lastpostlist = postManager.getLastPostList(postpramsinfo);
		} else {

			int reval = paymentLogManager.buyTopic(userid, topic, topic.getUsersByPosterid(), topic.getPrice(),
					netamount);
			if (reval > 0) {
				reqcfg.setUrl("showtopic.action?topicid=" + topic.getTid()).setMetaRefresh().setShowBackLink(false)
						.addMsgLine(PURCHASE_SUCCESS);
				return SUCCESS;
			} else {
				reqcfg.setBackLink("showforum.action?forumid=" + topic.getForums().getFid());

				if (reval == -1) {
					reqcfg.addErrLine(NOT_ENOUGH_MONEY);
					return SUCCESS;
				} else if (reval == -2) {
					reqcfg.addErrLine(NO_PERMISSION);
					return SUCCESS;
				} else {
					reqcfg.addErrLine(UNKNOWN_REASON);
					return SUCCESS;
				}
			}
		}
		return SUCCESS;
	}

	public Topics getTopic() {
		return topic;
	}

	public List<Posts> getLastpostlist() {
		return lastpostlist;
	}

	public List<Paymentlog> getPaymentloglist() {
		return paymentloglist;
	}

	public UserExtcreditsInfo getUserextcreditsinfo() {
		return userextcreditsinfo;
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

	public String getForumnav() {
		return forumnav;
	}

	public int getPageid() {
		return pageid;
	}

	public int getBuyers() {
		return buyers;
	}

	public int getPagecount() {
		return pagecount;
	}

	public String getPagenumbers() {
		return pagenumbers;
	}

	public String getTopictitle() {
		return topictitle;
	}

	public int getShowpayments() {
		return showpayments;
	}

	public int getBuyit() {
		return buyit;
	}

	public int getTopicprice() {
		return topicprice;
	}

	public float getNetamount() {
		return netamount;
	}

	public int getMaxincpertopic() {
		return maxincpertopic;
	}

	public int getMaxchargespan() {
		return maxchargespan;
	}

	public float getCreditstax() {
		return creditstax;
	}

	public int getPrice() {
		return price;
	}

	public int getPosterid() {
		return posterid;
	}

	public String getPoster() {
		return poster;
	}

	public double getUserlastprice() {
		return userlastprice;
	}

	public boolean isNeedlogin() {
		return needlogin;
	}

	public ShowtopicPagePostInfo getPostinfo() {
		return postinfo;
	}

	public String getPostmessage() {
		return postmessage;
	}

	public Pattern getPattern() {
		return pattern;
	}
}
