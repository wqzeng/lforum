package com.javaeye.lonlysky.lforum.service;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.beanutils.BeanUtils;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springside.modules.orm.hibernate.Page;
import org.springside.modules.orm.hibernate.SimpleHibernateTemplate;

import com.javaeye.lonlysky.lforum.comm.utils.UBBUtils;
import com.javaeye.lonlysky.lforum.comm.utils.Utils;
import com.javaeye.lonlysky.lforum.config.impl.ConfigLoader;
import com.javaeye.lonlysky.lforum.entity.forum.Attachments;
import com.javaeye.lonlysky.lforum.entity.forum.ForumStatistics;
import com.javaeye.lonlysky.lforum.entity.forum.Forums;
import com.javaeye.lonlysky.lforum.entity.forum.Myposts;
import com.javaeye.lonlysky.lforum.entity.forum.Postid;
import com.javaeye.lonlysky.lforum.entity.forum.PostpramsInfo;
import com.javaeye.lonlysky.lforum.entity.forum.Posts;
import com.javaeye.lonlysky.lforum.entity.forum.Ratelog;
import com.javaeye.lonlysky.lforum.entity.forum.ShowtopicPageAttachmentInfo;
import com.javaeye.lonlysky.lforum.entity.forum.ShowtopicPagePostInfo;
import com.javaeye.lonlysky.lforum.entity.forum.Topics;
import com.javaeye.lonlysky.lforum.entity.forum.Usergroups;
import com.javaeye.lonlysky.lforum.entity.forum.Users;

/**
 * 帖子操作类
 * 
 * @author 黄磊
 *
 */
@Service
@Transactional
public class PostManager {

	private static final Logger logger = LoggerFactory.getLogger(PostManager.class);
	private SimpleHibernateTemplate<Posts, Integer> postDAO;
	private SimpleHibernateTemplate<Postid, Integer> pidDAO;
	private SimpleHibernateTemplate<Myposts, Integer> mypostDAO;
	private SimpleHibernateTemplate<Ratelog, Integer> ratelogDAO;

	private Pattern patternAttach = Pattern.compile("\\[attach\\](\\d+?)\\[\\/attach\\]");
	private Pattern patternHide = Pattern.compile("\\s*\\[hide\\][\n\r]*([\\s\\S]+?)[\n\r]*\\[\\/hide\\]\\s*");
	private Pattern patternAttachImg = Pattern.compile("\\[attachimg\\](\\d+?)\\[\\/attachimg\\]");

	@Autowired
	private ForumManager forumManager;

	@Autowired
	private UserManager userManager;

	@Autowired
	private TopicManager topicManager;

	@Autowired
	private StatisticManager statisticManager;

	@Autowired
	private UserGroupManager userGroupManager;

	@Autowired
	private CachesManager cachesManager;

	@Autowired
	private AttachmentManager attachmentManager;

	@Autowired
	private ScoresetManager scoresetManager;

	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		postDAO = new SimpleHibernateTemplate<Posts, Integer>(sessionFactory, Posts.class);
		pidDAO = new SimpleHibernateTemplate<Postid, Integer>(sessionFactory, Postid.class);
		mypostDAO = new SimpleHibernateTemplate<Myposts, Integer>(sessionFactory, Myposts.class);
		ratelogDAO = new SimpleHibernateTemplate<Ratelog, Integer>(sessionFactory, Ratelog.class);
	}

	/**
	 * 创建帖子
	 * @param post 帖子信息类
	 * @throws ParseException 
	 */
	public void createPost(Posts post) throws ParseException {
		// 保存帖子信息
		Postid postid = new Postid();
		postid.setPostdatetime(post.getPostdatetime());
		postid.setPostForPid(post);
		post.setPostidByPid(postid);
		pidDAO.save(postid);
		if (post.getPostidByParentid().getPid() == 0) {
			post.setPostidByParentid(postid);
		}
		if (post.getInvisible() == 0) {
			// 更新论坛状态
			ForumStatistics statistics = statisticManager.getStatistic();
			statistics.setTotalpost(statistics.getTotalpost() + 1);
			statisticManager.updateStatistics(statistics);

			// 更新论坛板块信息
			String fids = Utils.null2String(postDAO.findUnique("select parentidlist from Forums where fid=?", post
					.getForums().getFid()));
			if (fids.equals("")) {
				fids = "" + post.getForums().getFid();
			} else {
				fids = fids + "," + post.getForums().getFid();
			}
			// 更新论坛信息
			postDAO
					.createQuery(
							"update Forums set posts=posts+1,todayposts=case when day(lastpost)-day(?)=0 then (todayposts+1) else 1 end,topics.tid=?,lasttitle=?,lastpost=?,lastposter=?,users.uid=? where (charindex(',' + rtrim(fid) + ',', ',"
									+ fids + ",') > 0)", Utils.getNowTime(), post.getTopics().getTid(),
							post.getTopics().getTitle(), post.getPostdatetime(), post.getPoster(),
							post.getUsers().getUid()).executeUpdate();

			// 更新主题信息
			Topics topics = post.getTopics();
			if (post.getLayer() <= 0) {
				topics.setReplies(0);
				topics.setLastposter(post.getPoster());
				topics.setLastpost(post.getPostdatetime());
				topics.setUsersByLastposterid(post.getUsers());
			} else {
				topics.setReplies(topics.getReplies() + 1);
				topics.setLastposter(post.getPoster());
				topics.setLastpost(post.getPostdatetime());
				topics.setUsersByLastposterid(post.getUsers());
			}
			topics.setPostid(postid);
			topicManager.updateTopicInfo(topics);

			if (post.getUsers().getUid() != -1) {
				// 更新会员信息
				Users users = userManager.getUserInfo(post.getUsers().getUid());
				users.setLastpost(post.getPostdatetime());
				users.setTopics(post.getTopics());
				users.setPosts(users.getPosts() + 1);
				users.setLastactivity(Utils.getNowTime());
				userManager.updateUserInfo(users);

				Myposts myposts = new Myposts();
				myposts.setDateline(post.getPostdatetime());
				myposts.setPostid(postid);
				myposts.setTopics(topics);
				myposts.setUsers(users);
				mypostDAO.save(myposts);
			}
		}

	}

	/**
	 * 检查帖子标题与内容中是否有广告
	 * @param regular 验证广告正则
	 * @param title 验证广告正则
	 * @param message 帖子内容
	 * @return 帖子标题与内容中是否有广告
	 */
	public boolean isAD(String regular, String title, String message) {
		if (regular.trim().equals("")) {
			return false;
		}
		Pattern pattern = Pattern.compile(regular);
		return (pattern.matcher(title).find() || pattern.matcher(message).find());
	}

	/**
	 * 按条件获取指定tid的帖子总数
	 * @param tid 帖子的tid
	 * @param condition
	 * @param posttableid 
	 * @return 指定tid的帖子总数
	 */
	public int getPostCount(int tid, String condition) {
		int count = Utils.null2Int(postDAO.findUnique("select count(pid) from Posts where " + condition
				+ " and layer>=0"), 0);
		return count;
	}

	/**
	 * 获取指定论坛板块的帖子总数
	 * @param fid 板块ID
	 * @return 帖子总数
	 */
	public int getPostCount(int fid) {
		return Utils.null2Int(postDAO.findUnique("select count(pid) from Posts where forums.fid=?", fid), 0);
	}

	/**
	 * 获取指定板块今日帖子总数
	 * @param fid
	 * @return
	 */
	public int getTodayPostCount(int fid) {
		return Utils.null2Int(postDAO.findUnique(
				"select count(pid) from Posts where forums.fid=? and day(postdatetime)-day(?)=0", fid, Utils
						.getNowTime()), 0);
	}

	/**
	 * 获取指定板块的最后发帖
	 * @param fid
	 * @return
	 */
	public Object[] getLastPostByFid(int fid) {
		Object object = postDAO
				.createQuery(
						"select topics.tid,title,postdatetime,users.uid,poster from Posts where forums.fid=? order by pid desc",
						fid).setMaxResults(1).uniqueResult();
		if (object != null) {
			return (Object[]) object;
		} else {
			return null;
		}
	}

	/**
	 * 按条件获取指定tid的帖子总数
	 * @param tid
	 * @param posterid
	 * @return
	 */
	public int getPostCount(int tid, int posterid) {
		String str = "topics.tid=" + tid + " and users.uid=" + posterid;
		return getPostCount(tid, str);
	}

	/**
	 * 判断指定用户是否是指定主题的回复者
	 * @param tid 主题id
	 * @param uid 用户id
	 * @return 是否是指定主题的回复者
	 */
	public boolean isReplier(int tid, int uid) {
		int count = Utils.null2Int(postDAO.findUnique(
				"select count(pid) from Posts where topics.tid=? and users.uid=? and " + uid + ">0", tid, uid), 0);
		return count > 0;
	}

	/**
	 * 获得单个帖子的信息, 包括发帖人的一般资料
	 * @param postpramsInfo 参数列表
	 * @param map
	 * @param ismoder
	 * @return 帖子的信息
	 * @throws ParseException
	 */
	public ShowtopicPagePostInfo getSinglePost(PostpramsInfo postpramsInfo,
			Map<Integer, List<ShowtopicPageAttachmentInfo>> map, boolean ismoder) throws ParseException {
		ShowtopicPagePostInfo info = null;
		List<ShowtopicPageAttachmentInfo> attcoll = new ArrayList<ShowtopicPageAttachmentInfo>();
		map.put(0, attcoll);
		Posts pos = getPostInfo(postpramsInfo.getPid());
		int allowGetAttach = 0;

		if (pos != null) {
			if (postpramsInfo.getGetattachperm().equals("")) {
				allowGetAttach = postpramsInfo.getCurrentusergroup().getAllowgetattach();
			} else {
				if (forumManager.allowGetAttach(postpramsInfo.getGetattachperm(), postpramsInfo.getUsergroupid())) {
					allowGetAttach = 1;
				}

			}
			//当帖子中的posterid字段为0时, 表示该数据出现异常
			if (pos.getUsers().getUid() == 0) {
				return null;
			}

			//用户组
			Usergroups tmpGroupInfo;

			info = new ShowtopicPagePostInfo();
			info.setPid(pos.getPid());
			info.setAttachment(pos.getAttachment());
			info.setFid(pos.getForums().getFid());
			info.setTitle(pos.getTitle().trim());
			info.setLayer(pos.getLayer());
			info.setMessage(pos.getMessage().trim());
			info.setLastedit(pos.getLastedit().trim());
			info.setPostdatetime(pos.getPostdatetime().trim());

			info.setPoster(pos.getPoster().trim());
			info.setPosterid(pos.getUsers().getUid());
			info.setInvisible(pos.getInvisible());
			info.setUsesig(pos.getUsesig());
			info.setHtmlon(pos.getHtmlon());
			info.setSmileyoff(pos.getSmileyoff());
			info.setParseurloff(pos.getParseurloff());
			info.setBbcodeoff(pos.getBbcodeoff());
			info.setRate(pos.getRate());
			info.setRatetimes(pos.getRatetimes());
			info.setUbbmessage(pos.getMessage().trim());
			if (info.getPosterid() > 0) {
				info.setNickname(pos.getUsers().getNickname().trim());
				info.setUsername(pos.getUsers().getUsername().trim());
				info.setGroupid(pos.getUsers().getUsergroups().getGroupid());
				info.setGender(pos.getUsers().getGender());
				info.setBday(pos.getUsers().getBday().trim());
				info.setShowemail(pos.getUsers().getShowemail());
				info.setDigestposts(pos.getUsers().getDigestposts());
				info.setCredits(pos.getUsers().getCredits());
				info.setExtcredits1(pos.getUsers().getExtcredits1());
				info.setExtcredits2(pos.getUsers().getExtcredits2());
				info.setExtcredits3(pos.getUsers().getExtcredits3());
				info.setExtcredits4(pos.getUsers().getExtcredits4());
				info.setExtcredits5(pos.getUsers().getExtcredits5());
				info.setExtcredits6(pos.getUsers().getExtcredits6());
				info.setExtcredits7(pos.getUsers().getExtcredits7());
				info.setExtcredits8(pos.getUsers().getExtcredits8());
				info.setPosts(pos.getUsers().getPosts());
				info.setJoindate(Utils.dateFormat(Utils.parseDate(pos.getUsers().getJoindate().trim()),
						Utils.SHORT_DATEFORMAT));
				info.setLastactivity(pos.getUsers().getLastactivity().trim());
				info.setUserinvisible(pos.getUsers().getInvisible());
				info.setAvatar(pos.getUsers().getUserfields().getAvatar().trim());
				info.setAvatarwidth(pos.getUsers().getUserfields().getAvatarwidth());
				info.setAvatarheight(pos.getUsers().getUserfields().getAvatarheight());
				info.setMedals(pos.getUsers().getUserfields().getMedals().trim());
				info.setSignature(pos.getUsers().getUserfields().getSightml().trim());
				info.setLocation(pos.getUsers().getUserfields().getLocation().trim());
				info.setCustomstatus(pos.getUsers().getUserfields().getCustomstatus().trim());
				info.setWebsite(pos.getUsers().getUserfields().getWebsite().trim());
				info.setIcq(pos.getUsers().getUserfields().getIcq().trim());
				info.setQq(pos.getUsers().getUserfields().getQq().trim());
				info.setMsn(pos.getUsers().getUserfields().getMsn().trim());
				info.setYahoo(pos.getUsers().getUserfields().getYahoo().trim());
				info.setSkype(pos.getUsers().getUserfields().getSkype().trim());

				//部分属性需要根据不同情况来赋值

				//根据用户自己的设置决定是否显示邮箱地址
				if (info.getShowemail() == 0) {
					info.setEmail("");
				} else {
					info.setEmail(pos.getUsers().getEmail().trim());
				}

				// 最后活动时间50分钟内的为在线, 否则不在线
				if (Utils.strDateDiffMinutes(info.getLastactivity(), 50) < 0) {
					info.setOnlinestate(1);
				} else {
					info.setOnlinestate(0);
				}

				//作者ID为-1即表明作者为游客, 为了区分会直接公开显示游客发帖时的IP, 这里将IP最后一位修改为*
				info.setIp(pos.getIp().trim());

				// 勋章
				if (info.getMedals().equals("")) {
					info.setMedals("");
				} else {
					info.setMedals(cachesManager.getMedalsList().get(info.getMedals()));
				}

				tmpGroupInfo = userGroupManager.getUsergroup(info.getGroupid());
				info.setStars(tmpGroupInfo.getStars());
				if (tmpGroupInfo.getColor().equals("")) {
					info.setStatus(tmpGroupInfo.getGrouptitle());
				} else {
					info.setStatus("<span style=\"color:" + tmpGroupInfo.getColor() + "\">"
							+ tmpGroupInfo.getGrouptitle() + "</span>");
				}
			} else {
				info.setNickname("游客");
				info.setUsername("游客");
				info.setGroupid(7);
				info.setShowemail(0);
				info.setDigestposts(0);
				info.setCredits(0);
				info.setExtcredits1(0);
				info.setExtcredits2(0);
				info.setExtcredits3(0);
				info.setExtcredits4(0);
				info.setExtcredits5(0);
				info.setExtcredits6(0);
				info.setExtcredits7(0);
				info.setExtcredits8(0);
				info.setPosts(0);
				info.setJoindate("2009-2-6");
				info.setLastactivity("2009-2-6 1:1:1");
				info.setUserinvisible(0);
				info.setAvatar("");
				info.setAvatarwidth(0);
				info.setAvatarheight(0);
				info.setMedals("");
				info.setSignature("");
				info.setLocation("");
				info.setCustomstatus("");
				info.setWebsite("");
				info.setIcq("");
				info.setQq("");
				info.setMsn("");
				info.setYahoo("");
				info.setSkype("");
				//部分属性需要根据不同情况来赋值
				info.setEmail("");
				info.setOnlinestate(1);
				info.setMedals("");

				info.setIp(pos.getIp().trim());
				if (info.getIp().indexOf(".") > -1) {
					info.setIp(info.getIp().substring(0, info.getIp().lastIndexOf(".") + 1) + "*");
				}

				tmpGroupInfo = userGroupManager.getUsergroup(7);
				info.setStars(tmpGroupInfo.getStars());
				info.setStatus("游客");
			}
			//扩展属性
			info.setId(1);
			info.setAdindex(Utils.getRandomInt(999999, 0, 10));

			if (!Utils.inArray(info.getGroupid() + "", "4,5,6")) {
				//处理帖子内容
				postpramsInfo.setSmileyoff(info.getSmileyoff());
				postpramsInfo.setBbcodeoff(info.getBbcodeoff());
				postpramsInfo.setParseurloff(info.getParseurloff());
				postpramsInfo.setAllowhtml(info.getHtmlon());
				postpramsInfo.setSdetail(info.getMessage());
				//校正hide处理
				if (tmpGroupInfo.getAllowhidecode() == 0) {
					postpramsInfo.setHide(0);
				}

				//先简单判断是否允许使用LForum代码,如果不允许,效率起见直接不进行代码转换 (虽然UBB.UBBToHTML方法内也进行判断)
				if (!postpramsInfo.isUbbmode()) {
					info.setMessage(UBBUtils.uBBToHTML(postpramsInfo));
				}

				if (postpramsInfo.getJammer() == 1) { //干扰码
				}

				String message = info.getMessage();
				if (info.getAttachment() > 0 || patternAttach.matcher(message).find()) {
					//获取在[hide]标签中的附件id
					String[] attHidArray = getHiddenAttachIdList(postpramsInfo.getSdetail(), postpramsInfo.getHide());
					for (ShowtopicPageAttachmentInfo attach : attcoll) {
						message = getMessageWithAttachInfo(postpramsInfo, allowGetAttach, attHidArray, info, attach,
								message);
					}
					info.setMessage(message);

				}

			} else//禁言的发帖人
			{
				if (ismoder) {
					info.setMessage("<div class='hintinfo'>该用户帖子内容已被屏蔽, 您拥有管理权限, 以下是帖子内容</div>" + info.getMessage());
				} else {
					info.setMessage("该用户帖子内容已被屏蔽");
				}
			}
		}
		return info;
	}

	/**
	 * 获取指定条件的帖子数据
	 * 
	 * @param postpramsInfo 参数列表
	 * @param ismoder
	 * @return
	 * @throws ParseException 
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 */
	public List<ShowtopicPagePostInfo> getPostList(PostpramsInfo postpramsInfo,
			Map<Integer, List<ShowtopicPageAttachmentInfo>> map, boolean ismoder) throws ParseException,
			IllegalAccessException, InvocationTargetException {
		List<ShowtopicPagePostInfo> postcoll = new ArrayList<ShowtopicPagePostInfo>();
		List<ShowtopicPageAttachmentInfo> attcoll = map.get(0);
		attcoll = new ArrayList<ShowtopicPageAttachmentInfo>();
		map.put(0, attcoll);
		StringBuilder pidlist = new StringBuilder();

		// 获取帖子分页数据
		Page<Posts> page = new Page<Posts>(postpramsInfo.getPagesize());
		page.setPageNo(postpramsInfo.getPageindex());
		if (!postpramsInfo.getCondition().equals("")) {
			page = postDAO.find(page, "from Posts where topics.tid=? and invisible=0 and "
					+ postpramsInfo.getCondition() + " order by pid", postpramsInfo.getTid());
		} else {
			page = postDAO.find(page, "from Posts where topics.tid=? and invisible<=0 order by pid", postpramsInfo
					.getTid());
		}
		if (logger.isDebugEnabled()) {
			logger.debug("获取帖子分页数据，页面大小：" + postpramsInfo.getPagesize() + "，页码：" + postpramsInfo.getPageindex());
		}
		return parsePostList(postpramsInfo, map, ismoder, postcoll, page, pidlist);
	}

	public List<ShowtopicPagePostInfo> parsePostList(PostpramsInfo postpramsInfo,
			Map<Integer, List<ShowtopicPageAttachmentInfo>> map, boolean ismoder, List<ShowtopicPagePostInfo> postcoll,
			Page<Posts> page, StringBuilder pidlist) throws ParseException, IllegalAccessException,
			InvocationTargetException {
		//String diggslist = "";
		int allowGetAttach = 0;

		if (forumManager.allowGetAttachByUserID(forumManager.getForumInfo(postpramsInfo.getFid()).getForumfields()
				.getPermuserlist(), postpramsInfo.getCurrentuserid())) {
			allowGetAttach = 1;
		} else {
			if (postpramsInfo.getGetattachperm().equals("") || postpramsInfo.getGetattachperm() == null) {
				allowGetAttach = postpramsInfo.getCurrentusergroup().getAllowgetattach();
			} else {
				if (forumManager.allowGetAttach(postpramsInfo.getGetattachperm(), postpramsInfo.getUsergroupid())) {
					allowGetAttach = 1;
				}
			}
		}
		Topics topicinfo = topicManager.getTopicInfo(postpramsInfo.getTid());
		if (topicinfo.getSpecial() == 4 && userGroupManager.getUsergroup(7).getAllowdiggs() != 1) {
			//diggslist = Debates.GetUesrDiggs(postpramsInfo.Tid, postpramsInfo.CurrentUserid);
		}
		//序号(楼层)的初值
		int id = (postpramsInfo.getPageindex() - 1) * postpramsInfo.getPagesize();

		//保存初始的hide值
		int oldHide = postpramsInfo.getHide();

		//取帖间广告
		int fid = postpramsInfo.getFid();
		int adcount = 0;//Advertisements.GetInPostAdCount("", fid);

		//用户组
		Usergroups tmpGroupInfo;

		for (Posts pos : page.getResult()) {
			postDAO.getSession().evict(pos);
			//当帖子中的posterid字段为0时, 表示该数据出现异常
			if (pos.getUsers().getUid() == 0) {
				continue;
			}
			ShowtopicPagePostInfo info = new ShowtopicPagePostInfo();
			info.setPid(pos.getPid());
			info.setAttachment(pos.getAttachment());
			if (info.getAttachment() > 0) {
				pidlist.append(",");
				pidlist.append(pos.getPid());
			}
			info.setFid(fid);
			info.setTitle(pos.getTitle().trim());
			info.setLayer(pos.getLayer());
			info.setMessage(pos.getMessage().trim());
			info.setLastedit(pos.getLastedit().trim());
			info.setPostdatetime(pos.getPostdatetime().trim());

			info.setPoster(pos.getPoster().trim());
			info.setPosterid(pos.getUsers().getUid());
			info.setInvisible(pos.getInvisible());
			info.setUsesig(pos.getUsesig());
			info.setHtmlon(pos.getHtmlon());
			info.setSmileyoff(pos.getSmileyoff());
			info.setParseurloff(pos.getParseurloff());
			info.setBbcodeoff(pos.getBbcodeoff());
			info.setRate(pos.getRate());
			info.setRatetimes(pos.getRatetimes());
			info.setUbbmessage(pos.getMessage().trim());
			if (info.getPosterid() > 0) {
				info.setNickname(pos.getUsers().getNickname().trim());
				info.setUsername(pos.getUsers().getUsername().trim());
				info.setGroupid(pos.getUsers().getUsergroups().getGroupid());
				info.setGender(pos.getUsers().getGender());
				info.setBday(pos.getUsers().getBday().trim());
				info.setShowemail(pos.getUsers().getShowemail());
				info.setDigestposts(pos.getUsers().getDigestposts());
				info.setCredits(pos.getUsers().getCredits());
				info.setExtcredits1(pos.getUsers().getExtcredits1());
				info.setExtcredits2(pos.getUsers().getExtcredits2());
				info.setExtcredits3(pos.getUsers().getExtcredits3());
				info.setExtcredits4(pos.getUsers().getExtcredits4());
				info.setExtcredits5(pos.getUsers().getExtcredits5());
				info.setExtcredits6(pos.getUsers().getExtcredits6());
				info.setExtcredits7(pos.getUsers().getExtcredits7());
				info.setExtcredits8(pos.getUsers().getExtcredits8());
				info.setPosts(pos.getUsers().getPosts());
				info.setJoindate(Utils.dateFormat(Utils.parseDate(pos.getUsers().getJoindate().trim()),
						Utils.SHORT_DATEFORMAT));
				info.setLastactivity(pos.getUsers().getLastactivity().trim());
				info.setUserinvisible(pos.getUsers().getInvisible());
				info.setAvatar(pos.getUsers().getUserfields().getAvatar().trim());
				info.setAvatarwidth(pos.getUsers().getUserfields().getAvatarwidth());
				info.setAvatarheight(pos.getUsers().getUserfields().getAvatarheight());
				info.setMedals(pos.getUsers().getUserfields().getMedals().trim());
				info.setSignature(pos.getUsers().getUserfields().getSightml().trim());
				info.setLocation(pos.getUsers().getUserfields().getLocation().trim());
				info.setCustomstatus(pos.getUsers().getUserfields().getCustomstatus().trim());
				info.setWebsite(pos.getUsers().getUserfields().getWebsite().trim());
				info.setIcq(pos.getUsers().getUserfields().getIcq().trim());
				info.setQq(pos.getUsers().getUserfields().getQq().trim());
				info.setMsn(pos.getUsers().getUserfields().getMsn().trim());
				info.setYahoo(pos.getUsers().getUserfields().getYahoo().trim());
				info.setSkype(pos.getUsers().getUserfields().getSkype().trim());

				//部分属性需要根据不同情况来赋值

				//根据用户自己的设置决定是否显示邮箱地址
				if (info.getShowemail() == 0) {
					info.setEmail("");
				} else {
					info.setEmail(pos.getUsers().getEmail().trim());
				}

				// 最后活动时间50分钟内的为在线, 否则不在线
				if (Utils.strDateDiffMinutes(info.getLastactivity(), 50) < 0) {
					info.setOnlinestate(1);
				} else {
					info.setOnlinestate(0);
				}

				//作者ID为-1即表明作者为游客, 为了区分会直接公开显示游客发帖时的IP, 这里将IP最后一位修改为*
				info.setIp(pos.getIp().trim());

				// 勋章
				if (info.getMedals().equals("")) {
					info.setMedals("");
				} else {
					info.setMedals(cachesManager.getMedalsList().get(info.getMedals()));
				}

				tmpGroupInfo = userGroupManager.getUsergroup(info.getGroupid());
				info.setStars(tmpGroupInfo.getStars());
				if (tmpGroupInfo.getColor().equals("")) {
					info.setStatus(tmpGroupInfo.getGrouptitle());
				} else {
					info.setStatus("<span style=\"color:" + tmpGroupInfo.getColor() + "\">"
							+ tmpGroupInfo.getGrouptitle() + "</span>");
				}

				if (topicinfo.getSpecial() == 4) {
					if (userGroupManager.getUsergroup(7).getAllowdiggs() == 1) {
						info.setDigged(false);//Digged = Debates.IsDigged(int.Parse(reader["pid"].ToString()), postpramsInfo.CurrentUserid);
					} else {
						info.setDigged(false);//Digged = diggslist.Contains(reader["pid"].ToString());
					}

				}

			} else {
				info.setNickname("游客");
				info.setUsername("游客");
				info.setGroupid(7);
				info.setShowemail(0);
				info.setDigestposts(0);
				info.setCredits(0);
				info.setExtcredits1(0);
				info.setExtcredits2(0);
				info.setExtcredits3(0);
				info.setExtcredits4(0);
				info.setExtcredits5(0);
				info.setExtcredits6(0);
				info.setExtcredits7(0);
				info.setExtcredits8(0);
				info.setPosts(0);
				info.setJoindate("2009-2-6");
				info.setLastactivity("2009-2-6 1:1:1");
				info.setUserinvisible(0);
				info.setAvatar("");
				info.setAvatarwidth(0);
				info.setAvatarheight(0);
				info.setMedals("");
				info.setSignature("");
				info.setLocation("");
				info.setCustomstatus("");
				info.setWebsite("");
				info.setIcq("");
				info.setQq("");
				info.setMsn("");
				info.setYahoo("");
				info.setSkype("");
				//部分属性需要根据不同情况来赋值
				info.setEmail("");
				info.setOnlinestate(1);
				info.setMedals("");

				info.setIp(pos.getIp().trim());
				if (info.getIp().indexOf(".") > -1) {
					info.setIp(info.getIp().substring(0, info.getIp().lastIndexOf(".") + 1) + "*");
				}

				tmpGroupInfo = userGroupManager.getUsergroup(7);
				info.setStars(tmpGroupInfo.getStars());
				info.setStatus("游客");

			}
			//扩展属性
			id++;
			info.setId(id);
			info.setAdindex(Utils.getRandomInt(999999, 0, adcount));

			postcoll.add(info);
		}
		List<ShowtopicPageAttachmentInfo> attcoll = map.get(0);
		if (pidlist.length() > 0) {
			pidlist.delete(0, 1);
			List<Attachments> attachList = attachmentManager.getAttachmentListByPid(pidlist.toString());
			if (attachList != null) {
				for (Attachments attachments : attachList) {
					ShowtopicPageAttachmentInfo attinfo = new ShowtopicPageAttachmentInfo();
					BeanUtils.copyProperties(attinfo, attachments);
					attcoll.add(attinfo);
				}
			}
		}

		for (ShowtopicPagePostInfo info : postcoll) {
			if (!Utils.inArray(info.getGroupid() + "", "4,5,6")) {
				//处理帖子内容
				postpramsInfo.setSmileyoff(info.getSmileyoff());
				postpramsInfo.setBbcodeoff(info.getBbcodeoff());
				postpramsInfo.setParseurloff(info.getParseurloff());
				postpramsInfo.setAllowhtml(info.getHtmlon());
				postpramsInfo.setSdetail(info.getMessage());
				postpramsInfo.setPid(info.getPid());
				//校正hide处理
				tmpGroupInfo = userGroupManager.getUsergroup(info.getGroupid());
				if (tmpGroupInfo.getAllowhidecode() == 0) {
					postpramsInfo.setHide(0);
				}

				info.setMessage(UBBUtils.uBBToHTML(postpramsInfo));

				String message = info.getMessage();
				if (info.getAttachment() > 0 || patternAttach.matcher(message).find()
						|| patternAttachImg.matcher(message).find()) {
					//获取在[hide]标签中的附件id
					String[] attHidArray = getHiddenAttachIdList(postpramsInfo.getSdetail(), postpramsInfo.getHide());
					List<ShowtopicPageAttachmentInfo> delattlist = new ArrayList<ShowtopicPageAttachmentInfo>();

					for (ShowtopicPageAttachmentInfo attach : attcoll) {
						message = getMessageWithAttachInfo(postpramsInfo, allowGetAttach, attHidArray, info, attach,
								message);
						if (Utils.inArray(attach.getAid().toString(), attHidArray) || attach.getPostid().getPid() == 0) {
							delattlist.add(attach);
						}
					}
					;

					for (ShowtopicPageAttachmentInfo attach : delattlist) {
						attcoll.remove(attach);
					}
					info.setMessage(message);

				}

				//恢复hide初值
				postpramsInfo.setHide(oldHide);
			} else//发帖人已经被禁止发言
			{
				if (ismoder) {
					info.setMessage("<div class='hintinfo'>该用户帖子内容已被屏蔽, 您拥有管理权限, 以下是帖子内容</div>" + info.getMessage());
				} else {
					info.setMessage("该用户帖子内容已被屏蔽");
					List<ShowtopicPageAttachmentInfo> delattlist = new ArrayList<ShowtopicPageAttachmentInfo>();

					for (ShowtopicPageAttachmentInfo attach : attcoll) {
						if (attach.getPostid().getPid() == info.getPid()) {
							delattlist.add(attach);
						}
					}

					for (ShowtopicPageAttachmentInfo attach : delattlist) {
						attcoll.remove(attach);
					}

				}
			}
		}
		map.put(0, attcoll);
		return postcoll;
	}

	/**
	 * 获取被包含在[hide]标签内的附件id
	 * @param content 帖子内容
	 * @param hide 隐藏标记
	 * @return 隐藏的附件id数组
	 */
	private String[] getHiddenAttachIdList(String content, int hide) {
		if (hide == 0) {
			return new String[0];
		}

		StringBuilder tmpStr = new StringBuilder();
		StringBuilder hidContent = new StringBuilder();
		Matcher m = patternHide.matcher(content);
		while (m.find()) {
			if (hide == 1) {
				hidContent.append(m.group(0));
			}
		}

		m = patternAttach.matcher(hidContent.toString());
		while (m.find()) {
			tmpStr.append(m.group(1));
			tmpStr.append(",");
		}

		m = patternAttachImg.matcher(hidContent.toString());
		while (m.find()) {
			tmpStr.append(m.group(1));
			tmpStr.append(",");
		}

		if (tmpStr.length() == 0) {
			return new String[0];
		}

		return tmpStr.delete(tmpStr.length() - 1, 1).toString().split(",");
	}

	private static String getMessageWithAttachInfo(PostpramsInfo postpramsInfo, int allowGetAttach,
			String[] attHidArray, ShowtopicPagePostInfo postinfo, ShowtopicPageAttachmentInfo attinfo, String message) {
		String forumPath = ConfigLoader.getConfig().getForumurl();
		String filesize;
		String replacement;
		if (Utils.inArray(attinfo.getAid().toString(), attHidArray)) {
			return message;
		}
		if ((Utils.null2Int(attinfo.getReadperm(), 0) <= postpramsInfo.getUsergroupreadaccess() || postinfo
				.getPosterid() == postpramsInfo.getCurrentuserid())
				&& allowGetAttach == 1)
			attinfo.setAllowread(1);
		else
			attinfo.setAllowread(0);

		attinfo.setGetattachperm(allowGetAttach);

		attinfo.setFilename(attinfo.getFilename().replace("\\", "/"));

		if (message.indexOf("[attach]" + attinfo.getAid() + "[/attach]") != -1
				|| message.indexOf("[attachimg]" + attinfo.getAid() + "[/attachimg]") != -1) {
			if (allowGetAttach != 1) {
				replacement = "<br /><img src=\"images/attachicons/attachment.gif\" alt=\"\">&nbsp;附件: <span class=\"attachnotdown\">您所在的用户组无法下载或查看附件</span>";
			} else if (attinfo.getAllowread() == 1) {
				if (attinfo.getFilesize() > 1024) {
					filesize = Utils.round(attinfo.getFilesize() / 1024, 2) + "K";
				} else {
					filesize = attinfo.getFilesize() + " B";
				}

				if (Utils.isImgFilename(attinfo.getAttachment())) {
					attinfo.setAttachimgpost(1);

					if (postpramsInfo.getShowattachmentpath() == 1) {
						if (postpramsInfo.getIsforspace() == 1) {
							if (attinfo.getFilename().toLowerCase().indexOf("http") == 0) {
								replacement = "<img src=\"" + attinfo.getFilename() + "\" />";
							} else {
								replacement = "<img src=\"" + forumPath + "upload/" + attinfo.getFilename() + "\" />";
							}
						} else {
							if (attinfo.getFilename().toLowerCase().indexOf("http") == 0) {
								replacement = "<span style=\"position: absolute; display: none;\" onmouseover=\"showMenu(this.id, 0, 1)\" id=\"attach_"
										+ attinfo.getAid()
										+ "\"><img border=\"0\" src=\""
										+ forumPath
										+ "images/attachicons/attachimg.gif\" /></span><img src=\""
										+ attinfo.getFilename()
										+ "\" onload=\"attachimg(this, 'load');\" onmouseover=\"attachimginfo(this, 'attach_"
										+ attinfo.getAid()
										+ "', 1);attachimg(this, 'mouseover')\" onclick=\"zoom(this);\" onmouseout=\"attachimginfo(this, 'attach_"
										+ attinfo.getAid()
										+ "', 0, event)\" /><div id=\"attach_"
										+ attinfo.getAid()
										+ "_menu\" style=\"display: none;\" class=\"t_attach\"><img border=\"0\" alt=\"\" class=\"absmiddle\" src=\""
										+ forumPath
										+ "images/attachicons/image.gif\" /><a target=\"_blank\" href=\""
										+ attinfo.getFilename()
										+ "\"><strong>"
										+ attinfo.getAttachment()
										+ "</strong></a>("
										+ filesize
										+ ")<br/><div class=\"t_smallfont\">"
										+ attinfo.getPostdatetime() + "</div></div>";
							} else {
								replacement = "<span style=\"position: absolute; display: none;\" onmouseover=\"showMenu(this.id, 0, 1)\" id=\"attach_"
										+ attinfo.getAid()
										+ "\"><img border=\"0\" src=\""
										+ forumPath
										+ "images/attachicons/attachimg.gif\" /></span><img src=\""
										+ forumPath
										+ "upload/"
										+ attinfo.getFilename()
										+ "\" onload=\"attachimg(this, 'load');\" onmouseover=\"attachimginfo(this, 'attach_"
										+ attinfo.getAid()
										+ "', 1);attachimg(this, 'mouseover')\" onclick=\"zoom(this);\" onmouseout=\"attachimginfo(this, 'attach_"
										+ attinfo.getAid()
										+ "', 0, event)\" /><div id=\"attach_"
										+ attinfo.getAid()
										+ "_menu\" style=\"display: none;\" class=\"t_attach\"><img border=\"0\" alt=\"\" class=\"absmiddle\" src=\""
										+ forumPath
										+ "images/attachicons/image.gif\" /><a target=\"_blank\" href=\""
										+ forumPath
										+ "upload/"
										+ attinfo.getFilename()
										+ "\"><strong>"
										+ attinfo.getAttachment()
										+ "</strong></a>("
										+ filesize
										+ ")<br/><div class=\"t_smallfont\">"
										+ attinfo.getPostdatetime()
										+ "</div></div>";
							}

						}
					} else {
						if (postpramsInfo.getIsforspace() == 1) {
							replacement = "<img src=\"" + forumPath + "attachment.action?attachmentid="
									+ attinfo.getAid() + "\" />";
						} else {
							replacement = "<span style=\"position: absolute; display: none;\" onmouseover=\"showMenu(this.id, 0, 1)\" id=\"attach_"
									+ attinfo.getAid()
									+ "\"><img border=\"0\" src=\""
									+ forumPath
									+ "images/attachicons/attachimg.gif\" /></span><img src=\""
									+ forumPath
									+ "attachment.action?attachmentid="
									+ attinfo.getAid()
									+ "\" onload=\"attachimg(this, 'load');\" onmouseover=\"attachimginfo(this, 'attach_"
									+ attinfo.getAid()
									+ "', 1);attachimg(this, 'mouseover')\" onclick=\"zoom(this);\" onmouseout=\"attachimginfo(this, 'attach_"
									+ attinfo.getAid()
									+ "', 0, event)\" /><div id=\"attach_"
									+ attinfo.getAid()
									+ "_menu\" style=\"display: none;\" class=\"t_attach\"><img border=\"0\" alt=\"\" class=\"absmiddle\" src=\""
									+ forumPath
									+ "images/attachicons/image.gif\" /><a target=\"_blank\" href=\""
									+ forumPath
									+ "attachment.action?attachmentid="
									+ attinfo.getAid()
									+ "\"><strong>"
									+ attinfo.getAttachment()
									+ "</strong></a>("
									+ filesize
									+ ")<br/><div class=\"t_smallfont\">" + attinfo.getPostdatetime() + "</div></div>";
						}
					}
				} else {
					attinfo.setAttachimgpost(0);
					replacement = "<p><img alt=\"\" src=\"images/attachicons/attachment.gif\" border=\"0\" /><span class=\"bold\">附件</span>: <a href=\"attachment.action?attachmentid="
							+ attinfo.getAid()
							+ "\" target=\"_blank\">"
							+ attinfo.getAttachment().trim()
							+ "</a> ("
							+ attinfo.getPostdatetime()
							+ ", "
							+ filesize
							+ ")<br />该附件被下载次数 "
							+ attinfo.getDownloads()
							+ "</p>";
				}
			} else {
				replacement = "<br /><span class=\"notdown\">你的下载权限 " + postpramsInfo.getUsergroupreadaccess()
						+ " 低于此附件所需权限 " + attinfo.getReadperm() + ", 你无法查看此附件</span>";
			}

			Pattern r = Pattern.compile("\\[attach\\]" + attinfo.getAid() + "\\[/attach\\]|\\[attachimg\\]"
					+ attinfo.getAid() + "\\[/attachimg\\]");
			message = r.matcher(message).replaceFirst(replacement);

			message = message.replace("[attach]" + attinfo.getAid() + "[/attach]", "");
			message = message.replace("[attachimg]" + attinfo.getAid() + "[/attachimg]", "");

			if (attinfo.getPostid().getPid() == postinfo.getPid()) {
				Postid postid = new Postid();
				postid.setPid(0);
				attinfo.setPostid(postid);
			}
		} else {
			if (attinfo.getPostid().getPid() == postinfo.getPid()) {
				if (Utils.isImgFilename(attinfo.getAttachment())) {
					attinfo.setAttachimgpost(1);
				} else {
					attinfo.setAttachimgpost(0);
				}

				//加载文件预览类指定方法
				//
			}
		}
		return message;
	}

	/**
	 * 更新指定帖子信息
	 * @param postsInfo 帖子信息
	 */
	public void updatePost(Posts postsInfo) {
		postDAO.save(postsInfo);
	}

	/**
	 * 更新帖子附件类型
	 * @param pid 帖子
	 * @param attType 附件类型
	 */
	public void updateAttachment(int pid, int attType) {
		Posts posts = postDAO.get(pid);
		posts.setAttachment(attType);
		updatePost(posts);
	}

	/**
	 * 获得指定的帖子描述信息
	 * @param pid
	 * @return
	 */
	public Posts getPostInfo(int pid) {
		return postDAO.get(pid);
	}

	/**
	 * 获得指定主题的第一个帖子的id
	 * @param tid 主题id
	 * @return
	 */
	public int getFirstPostId(int tid) {
		return Utils.null2Int(postDAO.createQuery("select pid from Posts where topics.tid=? order by pid", tid)
				.setMaxResults(1).uniqueResult());
	}

	/**
	 * 获得最后回复的帖子列表
	 * @param postpramsInfo 参数列表
	 * @return 帖子列表
	 */
	@Transactional(readOnly = true)
	public List<Posts> getLastPostList(PostpramsInfo postpramsInfo) {
		Page<Posts> page = new Page<Posts>(postpramsInfo.getPagesize());
		page.setPageNo(postpramsInfo.getPageindex());
		page = postDAO.find(page, "from Posts where topics.tid=? and invisible=0 order by pid desc", postpramsInfo
				.getTid());
		List<Posts> postList = page.getResult();
		for (Posts posts : postList) {
			//　ubb转为html代码在页面显示
			postpramsInfo.setSmileyoff(posts.getSmileyoff());
			postpramsInfo.setBbcodeoff(posts.getBbcodeoff());
			postpramsInfo.setParseurloff(posts.getParseurloff());
			postpramsInfo.setAllowhtml(posts.getHtmlon());
			postpramsInfo.setPid(posts.getPid());
			postpramsInfo.setSdetail(posts.getMessage());

			if (postpramsInfo.getPrice() > 0 && posts.getLayer() == 0) {
				posts.setMessage("<div class=\"paystyle\">此帖为交易帖,要付 "
						+ scoresetManager.getScoreSet(scoresetManager.getCreditsTrans()).getName()
						+ " <span class=\"bold\">" + postpramsInfo.getPrice() + "</span>"
						+ scoresetManager.getScoreSet(scoresetManager.getCreditsTrans()).getUnit() + " 才可查看</div>");
			} else {
				posts.setMessage(UBBUtils.uBBToHTML(postpramsInfo));
			}

			//是不是加干扰码
			if (postpramsInfo.getJammer() == 1) {
				// 
			}

			//是不是隐藏会员email
			if (posts.getUsers().getShowemail() == 1) {
				posts.getUsers().setEmail("");
			}
		}
		return postList;
	}

	/**
	 * 获取指定tid的帖子部分内容
	 * @param tid 帖子的tid
	 * @return 指定tid的帖子部分内容
	 */
	@SuppressWarnings("unchecked")
	public List<Object[]> getPostTree(int tid) {
		List<Object[]> objList = postDAO
				.find(
						"select pid,layer,title,poster,users.uid,postdatetime,message from Posts where topics.tid=? and invisible=0 and layer>0 order by postidByParentid.pid",
						tid);
		if (logger.isDebugEnabled()) {
			logger.debug("获取TID为{}的帖子树{}", tid, objList.size());
		}
		for (Object[] objects : objList) {
			// 截取帖子内容前50字符
			objects[6] = Utils.substring(Utils.cleanHtmlTag(objects[6].toString()), 50);
			if (!Utils.null2String(objects[6]).equals("")) {
				objects[2] = objects[6]; // 设置标题为截取后的内容
			}
		}
		return objList;
	}

	/**
	 * 获得指定主题的帖子列表
	 * @param topiclist 主题ID列表
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Posts> getPostList(String topiclist) {
		if (!Utils.isIntArray(topiclist.split(","))) {
			return null;
		}
		return postDAO.find("from Posts where topics.tid in(" + topiclist + ")");
	}

	/**
	 * 获得指定帖子列表
	 * @param topiclist 主题ID列表
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Posts> getPostListByPids(String postlist) {
		if (!Utils.isIntArray(postlist.split(","))) {
			return null;
		}
		return postDAO.find("from Posts where pid in(" + postlist + ")");
	}

	/**
	 * 删除指定ID的帖子
	 * @param pid
	 * @param reserveattach
	 * @param chanageposts
	 * @return
	 * @throws IOException
	 * @throws ParseException
	 */
	public int deletePost(int pid, boolean reserveattach, boolean chanageposts) throws IOException, ParseException {
		if (!reserveattach) {//删除附件 
			attachmentManager.deleteAttachmentByPid(pid);
		}

		return deletePost(pid, chanageposts);
	}

	public void deletePosts(Posts posts) {
		postDAO.delete(posts);
	}

	/**
	 * 删除指定ID的帖子
	 * @param pid 帖子ID
	 * @param chanageposts
	 * @return
	 * @throws ParseException
	 */
	public int deletePost(int pid, boolean chanageposts) throws ParseException {
		int postcount;
		int todaycount;
		Posts posts = getPostInfo(pid); //获取帖子信息
		String fidlist = posts.getForums().getParentidlist();
		if (!fidlist.trim().equals("")) {
			fidlist = fidlist.trim() + "," + posts.getForums().getFid();
		} else {
			fidlist = posts.getForums().getFid().toString();
		}

		if (posts.getLayer() != 0) {
			if (chanageposts) {
				// 更新论坛状态
				postDAO.createQuery("update ForumStatistics set totalpost=totalpost - 1").executeUpdate();

				// 更新板块状态
				if (Utils.dateFormat(Utils.parseDate(posts.getPostdatetime()), "yyyy-MM-dd").equals(
						Utils.dateFormat("yyyy-MM-dd"))) {
					postDAO.createQuery(
							"update Forums set posts=posts - 1,todayposts=todayposts-1 where charindex(','+rtrim(fid)+',',',"
									+ fidlist + ",')>0").executeUpdate();
				} else {
					postDAO.createQuery(
							"update Forums set posts=posts - 1 where charindex(','+rtrim(fid)+',','," + fidlist
									+ ",')>0").executeUpdate();
				}

				// 更新用户信息
				postDAO.createQuery("update Users set posts=posts - 1 where uid=?", posts.getUsers().getUid())
						.executeUpdate();

				// 更新主题信息
				postDAO.createQuery("update Topics set replies=replies - 1 where tid=?", posts.getTopics().getTid())
						.executeUpdate();
			}
			postDAO.createQuery("delete from Posts where pid=?", pid).executeUpdate(); // 删除帖子
		} else {
			postcount = Utils.null2Int(postDAO.findUnique("select count(pid) from Posts where topics.tid=?", posts
					.getTopics().getTid()), 0);
			todaycount = Utils.null2Int(postDAO.findUnique(
					"select count(pid) from Posts where topics.tid=? and day(postdatetime)-day(?)=0", posts.getTopics()
							.getTid(), Utils.getNowTime()), 0);
			if (chanageposts) {
				// 更新论坛状态
				postDAO.createQuery("update ForumStatistics set totaltopic=totaltopic-1,totalpost=totalpost - ?",
						postcount).executeUpdate();

				// 更新板块状态
				postDAO.createQuery(
						"update Forums set posts=posts - ?,todayposts=todayposts-? where charindex(','+rtrim(fid)+',',',"
								+ fidlist + ",')>0", postcount, todaycount).executeUpdate();

				// 更新用户信息
				postDAO.createQuery("update Users set posts=posts - ? where uid=?", posts.getUsers().getUid(),
						postcount).executeUpdate();
			}
			postDAO.createQuery("delete from Topics where tid=?", posts.getTopics().getTid()).executeUpdate();
			postDAO.createQuery("delete from Posts where pid=?", pid).executeUpdate(); // 删除帖子
		}
		return 1;

	}

	public int getLastPostTid(Forums foruminfo, String visibleForum) {
		String fidParam = "";
		if (foruminfo.getFid() > 0) {
			fidParam = "and (forums.fid = " + foruminfo.getFid() + " or charindex('," + foruminfo.getFid()
					+ ",' , ',' + rtrim(forums.parentidlist) + ',') > 0 )  ";
		}

		if (!visibleForum.trim().equals("")) {
			fidParam += " and forums.fid in (" + visibleForum + ")";
		}

		String hql = "select tid from Topics where closed<>1 and  displayorder >=0  " + fidParam
				+ "  order by lastpost desc";

		return Utils.null2Int(postDAO.createQuery(hql).setMaxResults(1).uniqueResult());
	}

	/**
	 * 返回指定主题的最后回复帖子
	 * @param tid
	 */
	public Posts getLastPostByTid(int tid) {
		Object object = postDAO.createQuery("from Posts where topics.tid=? order by pid desc", tid).setMaxResults(1)
				.uniqueResult();
		if (object != null) {
			return (Posts) object;
		}
		return null;

	}

	/**
	 * 删除悬赏帖子
	 * @param topicid
	 * @param opiniontext
	 * @param postid
	 */
	public void deleteDebatePost(int topicid, String opiniontext, int postid) {
		int postdebatefieldscount = postDAO.createQuery("delete from Postdebatefields where postid.pid=?", postid)
				.executeUpdate();
		int debatediggscount = postDAO.createQuery("delete from Debatediggs where postid.pid=?", postid)
				.executeUpdate();
		postDAO.createQuery("update Debates set " + opiniontext + "=" + opiniontext + "-? where topics.tid=?",
				postdebatefieldscount + debatediggscount, topicid).executeUpdate();
	}

	/**
	 * 更新帖子的评分值
	 * @param tid 主题ID
	 * @param postidlist 帖子ID列表
	 * @param ratetimes 
	 * @return 更新的帖子数量
	 */
	public int updatePostRateTimes(int tid, String postidlist, int ratetimes) {
		if (!Utils.isIntArray(postidlist.split(","))) {
			return 0;
		}
		return postDAO
				.createQuery("update Posts set ratetimes=ratetimes+? where pid in(" + postidlist + ")", ratetimes)
				.executeUpdate();

	}

	/**
	 * 获取帖子评分列表
	 * @param pid
	 * @return 帖子评分列表
	 */
	public List<Ratelog> getPostRateList(int pid) {
		Page<Ratelog> page = new Page<Ratelog>(ConfigLoader.getConfig().getDisplayratecount());
		page.setPageNo(1);
		page = ratelogDAO.find(page, "from Ratelog where postid.pid=? order by id desc", pid);
		return page.getResult();
	}

	/**
	 * 屏蔽帖子内容
	 * @param postidlist
	 * @param banposttype
	 */
	public void banPosts(String postidlist, int invisible) {
		postDAO.createQuery("update Posts set invisible=? where pid in(" + postidlist + ")", invisible).executeUpdate();
	}

	/**
	 * 通过主题ID得到主帖内容
	 * @param topicid
	 * @return
	 */
	public ShowtopicPagePostInfo getSinglePost(int topicid) {
		ShowtopicPagePostInfo info = new ShowtopicPagePostInfo();
		Posts posts = (Posts) postDAO.findUnique("from Posts where topics.tid=? and layer=0", topicid);
		if (posts != null) {
			info.setMessage(posts.getMessage().trim());
		}
		return info;
	}
}
