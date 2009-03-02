package com.javaeye.lonlysky.lforum.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springside.modules.orm.hibernate.SimpleHibernateTemplate;

import com.javaeye.lonlysky.lforum.comm.ForumAction;
import com.javaeye.lonlysky.lforum.comm.LForumRequest;
import com.javaeye.lonlysky.lforum.comm.utils.ForumUtils;
import com.javaeye.lonlysky.lforum.comm.utils.Utils;
import com.javaeye.lonlysky.lforum.config.impl.ConfigLoader;
import com.javaeye.lonlysky.lforum.entity.IndexOnline;
import com.javaeye.lonlysky.lforum.entity.forum.Admingroups;
import com.javaeye.lonlysky.lforum.entity.forum.Forums;
import com.javaeye.lonlysky.lforum.entity.forum.Online;
import com.javaeye.lonlysky.lforum.entity.forum.Onlinelist;
import com.javaeye.lonlysky.lforum.entity.forum.Topics;
import com.javaeye.lonlysky.lforum.entity.forum.Usergroups;
import com.javaeye.lonlysky.lforum.entity.forum.Users;
import com.javaeye.lonlysky.lforum.exception.ServiceException;

/**
 * 在线用户相关业务
 * 
 * @author 黄磊
 *
 */
@Service
@Transactional
public class OnlineUserManager {

	private static final Logger logger = LoggerFactory.getLogger(OnlineUserManager.class);
	private static final Object synObject = new Object();
	private SimpleHibernateTemplate<Online, Integer> onlineDAO;
	private SimpleHibernateTemplate<Onlinelist, Integer> oimgDAO;

	/**
	 * 获取在线用户列表中返回的Map集合里面全部用户数
	 */
	public static final String TOTALUSER = "totaluser";

	/**
	 * 获取在线用户列表中返回的Map集合里面全部游客数
	 */
	public static final String GUESTUSER = "guestuser";

	/**
	 * 获取在线用户列表中返回的Map集合里面全部登录用户数
	 */
	public static final String USER = "user";

	/**
	 * 获取在线用户列表中返回的Map集合里面全部隐身会员数
	 */
	public static final String INVISIBLEUSER = "invisibleuser";

	@Autowired
	private UserManager userManager;

	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		onlineDAO = new SimpleHibernateTemplate<Online, Integer>(sessionFactory, Online.class);
		oimgDAO = new SimpleHibernateTemplate<Onlinelist, Integer>(sessionFactory, Onlinelist.class);
	}

	/**
	 * 清理之前的在线表记录(本方法在应用程序初始化时被调用)
	 */
	public void initOnlineList() {
		List<Online> onList = onlineDAO.findAll();
		for (Online online : onList) {
			onlineDAO.delete(online);
		}
		if (logger.isDebugEnabled()) {
			logger.debug("清理在线记录{}条", onList.size());
		}
	}

	/**
	 * 获得在线用户总数量
	 * 
	 * @return 用户数量
	 */
	public int getOnlineAllUserCount() {
		int count = Utils.null2Int(onlineDAO.createCriteria().setProjection(Projections.rowCount()).uniqueResult(), 0);
		if (logger.isDebugEnabled()) {
			logger.debug("在线用户总数{}", count);
		}
		return count;
	}

	/**
	 * 返回缓存中在线用户总数
	 * @return 缓存中用户总数
	 */
	public int getCacheOnlineAllUserCount() {
		int count = Utils.null2Int(ForumUtils.getCookie("onlineusercount"), 0);
		if (count == 0) {
			count = getOnlineAllUserCount();
			ForumUtils.writeCookie("onlineusercount", count + "", 3);
		}
		return count;
	}

	/**
	 * 获取在线注册用户总数
	 * 
	 * @return 注册用户数量
	 */
	public int getOnlineUserCount() {
		int count = Utils.null2Int(onlineDAO.createCriteria(Property.forName("users.uid").gt(0)).setProjection(
				Projections.rowCount()).uniqueResult(), 0);
		if (logger.isDebugEnabled()) {
			logger.debug("在线注册用户{}", count);
		}
		return count;
	}

	/**
	 * 返回板块用户在线列表
	 * @param forumid 板块ID
	 * @param map 统计在线用户信息(为null则不统计)
	 * @return 在线用户列表
	 */
	public List<Online> getForumOnlineUserList(int forumid, Map<String, Integer> map) {
		List<Online> onList = new ArrayList<Online>();
		synchronized (synObject) {
			onList = onlineDAO.findByCriteria(Property.forName("forums.fid").eq(forumid));
		}
		if (map != null) { // 如果不为Null则统计
			int totaluser = 0, guest = 0, user = 0, invisibleuser = 0;
			for (Online online : onList) {
				totaluser++; // 统计所有用户
				if (online.getUsers().getUid() == -1) {
					guest++; // 统计游客
				} else if (online.getUsers().getInvisible() == 1) {
					invisibleuser++; // 统计隐身会员
				}
			}//end for
			user = totaluser - guest; // 统计登录用户
			map.put(TOTALUSER, totaluser);
			map.put(GUESTUSER, guest);
			map.put(USER, user);
			map.put(INVISIBLEUSER, invisibleuser);
			if (logger.isDebugEnabled()) {
				logger.debug("论坛板块" + forumid + "所有在线-" + totaluser + ",登录会员-" + user + ",游客-" + guest + ",隐身会员-"
						+ invisibleuser);
			}
		}
		return onList;
	}

	/**
	 * 返回在线用户列表
	 * @param map 统计在线用户信息(为null则不统计)
	 * @return 在线用户列表
	 */
	public List<IndexOnline> getOnlineUserList(Map<String, Integer> map) {
		List<Online> onList = onlineDAO.findAll();
		List<IndexOnline> indexonlineList = new ArrayList<IndexOnline>();
		int totaluser = onList.size(); // 统计所有用户

		// 此处更新系统状态信息

		if (map != null) { // 如果不为Null则统计
			int guest = 0, user = 0, invisibleuser = 0;
			for (Online online : onList) {
				IndexOnline indexOnline = new IndexOnline();
				try {
					BeanUtils.copyProperties(indexOnline, online);
				} catch (Exception e) {
					throw new ServiceException("获取在线用户列表失败");
				}
				if (indexOnline.getUsers().getUid() == -1) {
					guest++; // 统计游客
				} else if (indexOnline.getUsers().getInvisible() == 1) {
					invisibleuser++; // 统计隐身会员
				}
				if (indexOnline.getUsers().getUid() > 0
						|| (indexOnline.getUsers().getUid() == -1 && ConfigLoader.getConfig().getWhosonlinecontract() == 0)) {
					indexOnline.setActionname(ForumAction.getActionDescriptionByID(indexOnline.getAction()));
				}
				indexonlineList.add(indexOnline);
			}//end for
			user = totaluser - guest; // 统计登录用户
			map.put(TOTALUSER, totaluser);
			map.put(GUESTUSER, guest);
			map.put(USER, user);
			map.put(INVISIBLEUSER, invisibleuser);
			if (logger.isDebugEnabled()) {
				logger.debug("论坛所有在线-" + totaluser + ",登录会员-" + user + ",游客-" + guest + ",隐身会员-" + invisibleuser);
			}
		}
		return indexonlineList;
	}

	/**
	 * 返回在线用户图例列表
	 * 
	 * @return 图例列表
	 */
	public List<Onlinelist> getOnlineGroupIconList() {
		List<Onlinelist> iconList = null;
		iconList = oimgDAO.findByCriteria(Property.forName("img").ne(""));
		if (logger.isDebugEnabled()) {
			logger.debug("获取{}个在线用户图例", iconList.size());
		}
		return iconList;
	}

	/**
	 * 返回用户组图标
	 * @param groupId 用户组ID
	 * @return 图标
	 */
	public String getGourpImg(int groupId) {
		// 默认图标为空""
		String img = "";
		List<Onlinelist> oimgList = getOnlineGroupIconList();
		if (oimgList.size() > 0) {
			for (Onlinelist onlinelist : oimgList) {
				// 默认图标类型为用户
				// 如果有匹配图标则替换
				if ((onlinelist.getGroupid() == 0 && img.equals("")) || (onlinelist.getGroupid() == groupId)) {
					img = "<img src=\"images\\groupicons\\" + onlinelist.getImg() + "\" />";
				}
			}
		}
		return img;
	}

	/**
	 * 获得指定用户的详细信息
	 * @param olid ID
	 * @return 在线信息
	 */
	@Transactional(readOnly = true)
	public Online getOnlineUser(int olid) {
		Online online = onlineDAO.get(olid);
		if (logger.isDebugEnabled()) {
			logger.debug("获取ID为{}的在线信息成功", olid);
		}
		return online;
	}

	/**
	 * 获得指定用户的详细信息
	 * @param userid 用户ID
	 * @param password 用户密码
	 * @return 在线信息
	 */
	public Online getOnlineUser(int userid, String password) {
		Online online = (Online) onlineDAO.createCriteria(Property.forName("users.uid").eq(userid)).add(
				Property.forName("password").eq(password)).uniqueResult();
		if (logger.isDebugEnabled()) {
			logger.debug("获取ID为{},密码为{}的在线信息", userid, password);
		}
		return online;
	}

	/**
	 * 获得指定用户的详细信息
	 * @param userid 用户ID
	 * @param ip IP地址
	 * @return 在线信息
	 */
	public Online getOnlineUserByIp(int userid, String ip) {
		Online online = (Online) onlineDAO.createQuery("from Online where users.uid=? and ip=?", userid, ip)
				.setMaxResults(1).uniqueResult();
		if (logger.isDebugEnabled()) {
			logger.debug("获取ID为{}且IP为{}的用户在线信息", userid, ip);
		}
		return online;
	}

	/**
	 * 保存或更新指定在线信息
	 * @param online
	 */
	private void saveOnline(Online online) {
		onlineDAO.save(online);
		if (logger.isDebugEnabled()) {
			logger.debug("保存或更新ID为{}的在线信息成功", online.getOlid());
		}
	}

	/**
	 * 检查在线用户验证码是否有效
	 * @param olid 在线用户ID
	 * @param verifyCode 验证码
	 * @return 是否有效
	 */
	public boolean checkUserVerifyCode(int olid, String verifyCode) {
		Online online = getOnlineUser(olid);
		if (online == null) {
			return false;
		} else if (!online.getVerifycode().equalsIgnoreCase(verifyCode)) {
			return false;
		}
		// 如果有效则更新为新的验证码
		online.setVerifycode(Utils.getRandomString(5));
		saveOnline(online);
		if (logger.isDebugEnabled()) {
			logger.debug("检查在线用户{}验证码成功", olid);
		}
		return true;
	}

	/**
	 * 添加在线用户信息
	 * @param online 在线信息
	 * @param timeout 过期时间
	 */
	public void addOnlineUser(Online online, int timeout) {
		// 如果timeout为负数则代表不需要精确更新用户是否在线的状态
		if (timeout > 0) {
			if (online.getUsers().getUid() > 0) {
				// 非游客则更新用户状态为在线
				userManager.updateUserOnlineState(online.getUsers().getUid(), 1, "");
			}
		} else {
			timeout = timeout * -1;
		}

		if (timeout > 9999) {
			timeout = 9999;
		}
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MINUTE, timeout * -1);
		String timestr = Utils.dateFormat(calendar.getTime(), Utils.FULL_DATEFORMAT);
		if (logger.isDebugEnabled()) {
			logger.debug("过期时间：" + timestr);
		}
		List<Online> onlList = onlineDAO.findByCriteria(Property.forName("lastupdatetime").lt(timestr));
		if (onlList.size() > 0) {
			for (Online olu : onlList) {
				if (olu.getUsers().getUid() != -1) {
					// 如果用户不是游客,则更新用户状态	
					userManager.updateUserOnlineState(olu.getUsers().getUid(), 0, Utils.getNowTime());
				}
				onlineDAO.delete(olu);
			}//end for
		}
		saveOnline(online);
	}

	/**
	 * 创建一个游客用户增加到在线列表
	 * @param timeout 过期时间
	 * @return 用户
	 */
	public Online createGuestUser(int timeout) {
		Online online = new Online();
		Users user = new Users();
		user.setUid(-1);
		Usergroups group = new Usergroups();
		group.setGroupid(7);
		Admingroups adming = new Admingroups();
		adming.setAdmingid(0);

		online.setUsers(user);
		online.setUsername("游客");
		online.setNickname("游客");
		online.setPassword("");
		online.setUsergroups(group);
		online.setOlimg(getGourpImg(7));
		online.setAdmingroups(adming);
		online.setInvisible(0);
		online.setLastposttime("1900-1-1 00:00:00");
		online.setLastpostpmtime("1900-1-1 00:00:00");
		online.setLastsearchtime("1900-1-1 00:00:00");
		online.setLastupdatetime(Utils.getNowTime());
		online.setAction(0);
		online.setLastactivity(0);
		online.setVerifycode(Utils.getRandomString(5));
		online.setIp(LForumRequest.getIp());

		addOnlineUser(online, timeout);
		return online;
	}

	/**
	 * 创建一个会员用户增加到在线列表
	 * @param uid 用户ID
	 * @param timeout 超时时间
	 * @return 在线信息
	 */
	public Online createUser(int uid, int timeout) {
		Online online = new Online();
		if (uid > 0) {
			Users user = userManager.getUserInfo(uid);
			if (user != null) {
				online.setUsers(user);
				online.setUsername(user.getUsername().trim());
				online.setNickname(user.getNickname().trim());
				online.setPassword(user.getPassword().trim());
				online.setUsergroups(user.getUsergroups());
				online.setOlimg(getGourpImg(user.getUsergroups().getGroupid()));
				online.setAdmingroups(user.getAdmingroups());
				online.setInvisible(0);
				online.setLastposttime("1900-1-1 00:00:00");
				online.setLastpostpmtime("1900-1-1 00:00:00");
				online.setLastsearchtime("1900-1-1 00:00:00");
				online.setLastupdatetime(Utils.getNowTime());
				online.setAction(0);
				online.setLastactivity(0);
				online.setVerifycode(Utils.getRandomString(5));
				online.setIp(LForumRequest.getIp());

				addOnlineUser(online, timeout);
				userManager.updateUserOnlineState(uid, 1, Utils.getNowTime());
			}
		} else {
			online = createGuestUser(timeout);
		}
		return online;
	}

	/**
	 * 更新用户的当前动作及相关信息
	 * @param olid 在线列表id
	 * @param action 动作
	 * @param inid 所在位置代码
	 * @param timeout 超时时间
	 */
	public void updateAction(int olid, int action, int inid, int timeout) {
		// 如果上次刷新cookie间隔小于5分钟, 则不刷新数据库最后活动时间
		if (timeout < 0
				&& (System.currentTimeMillis() - Utils.null2Long(ForumUtils.getCookie("lastolupdate"), System
						.currentTimeMillis())) < 300000) {
			ForumUtils.writeCookie("lastolupdate", System.currentTimeMillis() + "");
		} else {
			updateAction(olid, action, inid);
		}
	}

	/**
	 * 更新用户的当前动作及相关信息
	 * @param olid 在线列表id
	 * @param action 动作
	 * @param inid 所在位置代码
	 */
	public void updateAction(int olid, int action, int inid) {
		Online online = getOnlineUser(olid);
		Forums forums = new Forums();
		forums.setFid(inid);
		Topics topics = new Topics();
		topics.setTid(inid);

		online.setLastactivity(action);
		online.setAction(action);
		online.setLastupdatetime(Utils.getNowTime());
		online.setForums(forums);
		online.setForumname("");
		online.setTopics(topics);
		online.setTitle("");
		if (logger.isDebugEnabled()) {
			logger.debug("更新在线用户动作,olid:" + olid + ",action:" + action + ",inid:" + inid);
		}
		saveOnline(online);
	}

	/**
	 * 更新用户的当前动作及相关信息
	 * @param olid 在线列表id
	 * @param action 动作id
	 * @param fid 版块id
	 * @param forumname 版块名
	 * @param tid 主题id
	 * @param topictitle 主题名
	 * @param timeout 超时时间
	 */
	public void updateAction(int olid, int action, int fid, String forumname, int tid, String topictitle, int timeout) {
		// 如果上次刷新cookie间隔小于5分钟, 则不刷新数据库最后活动时间
		if (timeout < 0
				&& (System.currentTimeMillis() - Utils.null2Long(ForumUtils.getCookie("lastolupdate"), System
						.currentTimeMillis())) < 300000) {
			ForumUtils.writeCookie("lastolupdate", System.currentTimeMillis() + "");
		} else {
			if (action == ForumAction.IndexShow.ACTION_ID || action == ForumAction.PostTopic.ACTION_ID
					|| action == ForumAction.ShowTopic.ACTION_ID || action == ForumAction.PostReply.ACTION_ID) {
				Utils.format(forumname, 37, true);
			}
			if (action == ForumAction.ShowTopic.ACTION_ID || action == ForumAction.PostReply.ACTION_ID) {
				Utils.format(topictitle, 37, true);
			}
			Online online = getOnlineUser(olid);
			Forums forums = new Forums();
			forums.setFid(fid);
			Topics topics = new Topics();
			topics.setTid(tid);

			online.setLastactivity(action);
			online.setAction(action);
			online.setLastupdatetime(Utils.getNowTime());
			online.setForums(forums);
			online.setForumname(forumname);
			online.setTopics(topics);
			online.setTitle(topictitle);
			if (logger.isDebugEnabled()) {
				logger.debug("更新在线用户动作,olid:" + olid + ",action:" + action + ",fid:" + fid + ",forumname:" + forumname
						+ ",tid:" + tid + ",title:" + topictitle);
			}
			saveOnline(online);
		}
	}

	/**
	 * 更新用户最后活动时间
	 * @param olid 在线id
	 * @param timeout 超时时间
	 */
	public void updateLastTime(int olid, int timeout) {
		// 如果上次刷新cookie间隔小于5分钟, 则不刷新数据库最后活动时间
		if (timeout < 0
				&& (System.currentTimeMillis() - Utils.null2Long(ForumUtils.getCookie("lastolupdate"), System
						.currentTimeMillis())) < 300000) {
			ForumUtils.writeCookie("lastolupdate", System.currentTimeMillis() + "");
		} else {
			Online online = getOnlineUser(olid);
			online.setLastupdatetime(Utils.getNowTime());
			saveOnline(online);
		}
	}

	/**
	 * 用户在线信息维护。<br>
	 * 判断当前用户的身份(会员还是游客),是否在在线列表中存在,如果存在则更新会员的当前动,不存在则建立.
	 * @param pwdkey 论坛passwordkey
	 * @param timeout 在线超时时间
	 * @return 在线信息
	 */
	public Online updateInfo(String pwdkey, int timeout) {
		return updateInfo(pwdkey, timeout, -1, "");
	}

	/**
	 * 用户在线信息维护。<br>
	 * 判断当前用户的身份(会员还是游客),是否在在线列表中存在,如果存在则更新会员的当前动,不存在则建立.
	 * @param pwdkey 论坛passwordkey
	 * @param timeout 在线超时时间
	 * @param uid 用户ID
	 * @param password 用户密码
	 * @return 在线信息
	 */
	public Online updateInfo(String pwdkey, int timeout, int uid, String password) {
		synchronized (synObject) { // 线程同步的
			Online online = new Online();
			String ip = LForumRequest.getIp();
			int userid = Utils.null2Int(ForumUtils.getCookie("userid"), uid);
			String pwd = (password.equals("") ? ForumUtils.getCookiePassword(pwdkey) : ForumUtils.getCookiePassword(
					password, pwdkey));
			if (pwd.length() == 0) {
				userid = -1;
			}

			if (userid != -1) { // 如果不是游客
				online = getOnlineUser(userid, pwd);

				// 更新流量统计
				if (LForumRequest.getPageName().indexOf("ajax.action") == -1
						&& ConfigLoader.getConfig().getStatstatus() == 1) {
					// 调用更新
				}

				if (online != null) {
					if (!online.getIp().equals(ip)) {
						updateIp(online.getOlid(), ip);
						online.setIp(ip);
						return online;
					}//end if
				} else {
					// 判断密码是否正确
					userid = userManager.checkPassword(userid, pwd, false);
					if (userid != -1) {
						deleteRowsByIP(ip);
						return createUser(userid, timeout);
					} else {
						// 密码错误的情况创建游客
						online = getOnlineUserByIp(-1, ip);
						if (online == null) {
							return createGuestUser(timeout);
						}
					}
				}//end if
			} else {
				online = getOnlineUserByIp(-1, ip);
				// 更新流量统计
				if (LForumRequest.getPageName().indexOf("ajax.action") == -1
						&& ConfigLoader.getConfig().getStatstatus() == 1) {
					// 调用更新
				}
				if (online == null) {
					return createGuestUser(timeout);
				}
			}
			online.setLastupdatetime(Utils.getNowTime());
			return online;
		}
	}

	/**
	 * 更新用户最后发帖时间
	 * @param olid 在线ID
	 */
	public void updatePostTime(int olid) {
		Online online = getOnlineUser(olid);
		online.setLastposttime(Utils.getNowTime());
		if (logger.isDebugEnabled()) {
			logger.debug("更新在线信息{}最后发帖时间", olid);
		}
		saveOnline(online);
	}

	/**
	 * 更新用户最后发短消息时间
	 * @param olid 在线id
	 */
	public void updatePostPMTime(int olid) {
		Online online = getOnlineUser(olid);
		online.setLastpostpmtime(Utils.getNowTime());
		if (logger.isDebugEnabled()) {
			logger.debug("更新在线信息{}最后短信时间", olid);
		}
		saveOnline(online);
	}

	/**
	 * 更新在线表中指定用户是否隐身
	 * @param olid 在线id
	 * @param invisible 是否隐身
	 */
	public void updateInvisible(int olid, int invisible) {
		Online online = getOnlineUser(olid);
		online.setInvisible(invisible);
		if (logger.isDebugEnabled()) {
			logger.debug("更新在线信息{}是否隐身{}", olid, invisible);
		}
		saveOnline(online);
	}

	/**
	 * 更新在线表中指定用户的用户密码
	 * @param olid 在线id
	 * @param password 用户密码
	 */
	public void updatePassword(int olid, String password) {
		Online online = getOnlineUser(olid);
		online.setPassword(password);
		if (logger.isDebugEnabled()) {
			logger.debug("更新在线信息{}用户密码{}", olid, password);
		}
		saveOnline(online);
	}

	/**
	 * 更新用户IP地址
	 * @param olid 在线id
	 * @param ip ip地址
	 */
	public void updateIp(int olid, String ip) {
		Online online = getOnlineUser(olid);
		online.setIp(ip);
		if (logger.isDebugEnabled()) {
			logger.debug("更新在线信息{}IP地址{}", olid, ip);
		}
		saveOnline(online);
	}

	/**
	 * 更新用户最后搜索时间
	 * @param olid 在线id
	 */
	public void updateSearchTime(int olid) {
		Online online = getOnlineUser(olid);
		online.setLastsearchtime(Utils.getNowTime());
		if (logger.isDebugEnabled()) {
			logger.debug("更新在线信息{}最后查询时间", olid);
		}
		saveOnline(online);
	}

	/**
	 * 更新用户的用户组
	 * @param userid 用户ID
	 * @param groupid 用户ID
	 */
	public void updateGroupid(int userid, int groupid) {
		if (logger.isDebugEnabled()) {
			logger.debug("更新用户{}在线信息为用户组{}", userid, groupid);
		}
		onlineDAO.createQuery("update Online set usergroups.groupid=? where users.uid=?", groupid, userid)
				.executeUpdate();
	}

	/**
	 * 删除符合条件的一个或多个用户信息
	 * @param ip IP地址
	 */
	public void deleteRowsByIP(String ip) {
		List<Online> onlineList = onlineDAO.findByCriteria(Property.forName("ip").eq(ip));
		for (Online online : onlineList) {
			if (online.getUsers().getUid() > 0) {
				userManager.updateUserOnlineState(online.getUsers().getUid(), 0, Utils.getNowTime());
			}
			onlineDAO.delete(online);
		}
	}

	/**
	 * 删除在线表中指定在线id的行
	 * @param olid 在线id
	 */
	public void deleteRows(int olid) {
		onlineDAO.delete(olid);
	}

	/**
	 * 更新用户在线时长
	 * 
	 * @param oltimespan
	 * @param uid
	 */
	public void updateOnlineTime(int oltimespan, int uid) {
		//为0代表关闭统计功能
		if (oltimespan == 0) {
			return;
		}

		if (ForumUtils.getCookie("onlinetime") == "") {
			ForumUtils.writeCookie("onlinetime", System.currentTimeMillis() + "");
		}
		if (System.currentTimeMillis()
				- Utils.null2Long(ForumUtils.getCookie("onlinetime"), System.currentTimeMillis()) >= oltimespan * 60 * 1000) {
			ForumUtils.writeCookie("onlinetime", System.currentTimeMillis() + "");

			//判断是否同步oltime (登录后的第一次onlinetime更新的时候或者在线超过1小时)
			if (ForumUtils.getCookie("onlinetime") == ""
					|| System.currentTimeMillis()
							- Utils.null2Long(ForumUtils.getCookie("onlinetime"), System.currentTimeMillis()) >= oltimespan * 60 * 1000) {
				userManager.updateOnlineTime(uid);
				ForumUtils.writeCookie("onlinetime", System.currentTimeMillis() + "");
			}
		}
	}
}
