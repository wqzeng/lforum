package com.javaeye.lonlysky.lforum.service;

import java.text.ParseException;
import java.util.List;

import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springside.modules.orm.hibernate.SimpleHibernateTemplate;

import com.javaeye.lonlysky.lforum.comm.MD5;
import com.javaeye.lonlysky.lforum.comm.utils.ForumUtils;
import com.javaeye.lonlysky.lforum.comm.utils.Utils;
import com.javaeye.lonlysky.lforum.entity.forum.Usergroups;
import com.javaeye.lonlysky.lforum.entity.forum.Users;
import com.javaeye.lonlysky.lforum.exception.ServiceException;

/**
 * 用户操作相关业务
 * 
 * @author 黄磊
 *
 */
@Service
@Transactional
public class UserManager {

	private SimpleHibernateTemplate<Users, Integer> userDAO;
	private static final Logger logger = LoggerFactory.getLogger(UserManager.class);

	@Autowired
	private UserGroupManager userGroupManager;

	@Autowired
	private OnlineUserManager onlineUserManager;

	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		userDAO = new SimpleHibernateTemplate<Users, Integer>(sessionFactory, Users.class);
	}

	/**
	 * 获取指定ID用户信息
	 * @param uid 用户ID
	 * @return 用户信息
	 */
	public Users getUserInfo(int uid) {
		if (!exists(uid)) {
			return null;
		}
		Users users = userDAO.get(uid);
		if (logger.isDebugEnabled()) {
			logger.debug("找到ID为{}的用户", uid);
		}
		return users;
	}

	/**
	 * 查找指定注册IP用户
	 * @param ip
	 * @return
	 */
	public Users getUserByIp(String ip) {
		Users user = null;
		try {
			user = (Users) userDAO.createCriteria(Property.forName("regip").eq(ip.trim())).addOrder(Order.desc("uid"))
					.list().get(0);
			if (logger.isDebugEnabled()) {
				logger.debug("找到IP为{}的用户", ip);
			}
		} catch (IndexOutOfBoundsException e) {
			if (logger.isDebugEnabled()) {
				logger.debug("没有找到IP为{}的用户", ip);
			}
		}
		return user;
	}

	/**
	 * 根据用户id返回用户名
	 * @param uid 用户ID
	 * @return 用户名
	 */
	public String getUserName(int uid) {
		String name = Utils.null2String(userDAO.findUnique("select username from Users where uid=?", uid));
		if (logger.isDebugEnabled()) {
			logger.debug("找到ID为{}的用户名{}", uid, name);
		}
		return name;
	}

	/**
	 * 根据用户id返回用户注册日期
	 * @param uid 用户ID
	 * @return 注册日期
	 */
	@Transactional(readOnly = true)
	public String getUserJoinDate(int uid) {
		String time = Utils.null2String(userDAO.findUnique("select joindate from Users where uid=?", uid));
		if (logger.isDebugEnabled()) {
			logger.debug("找到ID为{}的用户注册日期{}", uid, time);
		}
		return time;
	}

	/**
	 * 根据用户名返回用户id
	 * @param username
	 * @return 如果没有则返回-1
	 */
	public int getUserId(String username) {
		int uid = Utils.null2Int(userDAO.findUnique("select uid from Users where username=?", username.trim()));
		if (logger.isDebugEnabled()) {
			logger.debug("找到用户{}的ID{}", username, uid);
		}
		return uid;
	}

	/**
	 * 获取用户分页列表
	 * 
	 * @param pageSize 每页记录
	 * @param pageNo 当前页数
	 * @param orderby 排序
	 * @param orderType 排序方式
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Object[]> getUserList(int pageSize, int pageNo, String orderby, String orderType) {
		// 设置分页属性
		String[] arrayorderby = new String[] { "username", "credits", "posts", "admin", "lastactivity" };
		int i = Utils.getInArrayID(orderby, arrayorderby);

		switch (i) {
		case 0:
			orderby = "order by username " + orderType + ",uid " + orderType;
			break;
		case 1:
			orderby = "order by credits " + orderType + ",uid " + orderType;
			break;
		case 2:
			orderby = "order by posts " + orderType + ",uid " + orderType;
			break;
		case 3:
			orderby = "where admingroups.admingid > 0 order by admingroups.admingid " + orderType + ", uid "
					+ orderType;
			break;
		case 4:
			orderby = "order by lastactivity " + orderType + ",uid " + orderType;
			break;
		default:
			orderby = "order by uid " + orderType;
			break;
		}
		List<Object[]> objList = userDAO
				.createQuery(
						"select uid,usergroups.groupid,username,nickname,joindate,credits,posts,lastactivity,email,oltime,userfields.location From Users "
								+ orderby).setMaxResults(pageSize).setFirstResult((pageNo - 1) * pageSize).list();
		i = 0;
		for (Object[] objects : objList) {
			Object[] obj2 = new Object[13];
			System.arraycopy(objects, 0, obj2, 0, objects.length);
			objects = obj2;
			objects[3] = Utils.null2String(objects[3]);
			Usergroups group = userGroupManager.getUsergroup(Utils.null2Int(objects[1]));
			if (group.getColor().equals("")) {
				objects[11] = group.getGrouptitle();
			} else {
				objects[11] = "<font color='" + group.getColor().trim() + "'>" + group.getGrouptitle() + "</font>";
			}
			objects[12] = onlineUserManager.getGourpImg(group.getGroupid());
			objList.set(i, objects);
			i++;
		}
		if (logger.isDebugEnabled()) {
			logger.debug("获取用户分页列表成功,pageSize:{},pageNo:{},size:" + objList.size(), pageSize, pageNo);
		}
		return objList;
	}

	/**
	 * 判断指定用户ID的用户是否存在
	 * @param uid 用户ID
	 * @return 是否存在
	 */
	public boolean exists(int uid) {
		boolean flag = Utils.null2Int(userDAO.createCriteria(Property.forName("uid").eq(uid)).setProjection(
				Projections.rowCount()).uniqueResult()) >= 1;
		if (logger.isDebugEnabled()) {
			logger.debug("判断用户ID{}是否存在：{}", uid, flag ? "存在" : "不存在");
		}
		return flag;
	}

	/**
	 * 判断指定用户名的用户是否存在
	 * @param username 用户名
	 * @return 是否存在
	 */
	public boolean exists(String username) {
		boolean flag = Utils.null2Int(userDAO.createCriteria(Property.forName("username").eq(username.trim()))
				.setProjection(Projections.rowCount()).uniqueResult()) >= 1;
		if (logger.isDebugEnabled()) {
			logger.debug("判断用户名{}是否存在：{}", username, flag ? "存在" : "不存在");
		}
		return flag;
	}

	/**
	 * 是否有指定ip地址的用户注册
	 * @param ip 注册IP
	 * @return 是否存在
	 */
	public boolean existsByIp(String ip) {
		boolean flag = Utils.null2Int(userDAO.createCriteria(Property.forName("regip").eq(ip.trim())).setProjection(
				Projections.rowCount()).uniqueResult()) >= 1;
		if (logger.isDebugEnabled()) {
			logger.debug("判断IP{}是否存在：{}", ip, flag ? "存在" : "不存在");
		}
		return flag;
	}

	/**
	 * 检测Email和安全项
	 * @param username 用户名
	 * @param email email
	 * @param questionid 问题id
	 * @param answer 答案
	 * @return 如果正确则返回用户id, 否则返回-1
	 */
	public int checkEmailAndSecques(String username, String email, int questionid, String answer) {
		String secques = ForumUtils.getUserSecques(questionid, answer);
		int uid = Utils.null2Int(userDAO.findUnique("select uid from Users where username=? and email=? and secques=?",
				username, email, secques));
		if (logger.isDebugEnabled()) {
			logger.debug("检测用户{}的Email和安全项返回：{}", username, uid);
		}
		return uid;
	}

	/**
	 * 检测密码和安全项
	 * @param username 用户名
	 * @param password 密码
	 * @param originalpassword 是否非MD5密码
	 * @param questionid 问题id
	 * @param answer 答案
	 * @return 如果正确则返回用户id, 否则返回-1
	 */
	public int checkPasswordAndSecques(String username, String password, Boolean originalpassword, int questionid,
			String answer) {
		String secques = ForumUtils.getUserSecques(questionid, answer);
		String pwd = originalpassword ? MD5.encode(password) : password;
		int uid = Utils.null2Int(userDAO.findUnique(
				"select uid from Users where username=? and password=? and secques=?", username, pwd, secques));
		if (logger.isDebugEnabled()) {
			logger.debug("检测用户{}密码和安全项返回：{}", username, uid);
		}
		return uid;
	}

	/**
	 * 检查密码
	 * @param username 用户名
	 * @param password 密码
	 * @param originalpassword 是否非MD5密码
	 * @return 如果正确则返回用户id, 否则返回-1
	 */
	public int checkPassword(String username, String password, Boolean originalpassword) {
		String pwd = originalpassword ? MD5.encode(password) : password;
		int uid = Utils.null2Int(userDAO.findUnique("select uid from Users where username=? and password=?", username,
				pwd));
		if (logger.isDebugEnabled()) {
			logger.debug("检查用户{}密码返回：{}", username, uid);
		}
		return uid;
	}

	/**
	 * 检查密码
	 * @param uid 用户ID
	 * @param password 密码
	 * @param originalpassword 是否非MD5密码
	 * @return 如果正确则返回用户id, 否则返回-1
	 */
	@Transactional(readOnly = true)
	public int checkPassword(int uid, String password, Boolean originalpassword) {
		String pwd = originalpassword ? MD5.encode(password) : password;
		int id = Utils.null2Int(userDAO.findUnique("select uid from Users where uid=? and password=?", uid, pwd));
		if (logger.isDebugEnabled()) {
			logger.debug("检测ID为{}的用户密码返回：{}", uid, id);
		}
		return id;
	}

	/**
	 * 根据指定的email查找用户并返回用户uid
	 * @param email Email地址
	 * @return 如果有则返回用户id, 否则返回-1
	 */
	public int findUserByEmail(String email) {
		int uid = Utils.null2Int(userDAO.findUnique("select uid from Users where email=?", email));
		if (logger.isDebugEnabled()) {
			logger.debug("获取Email为{}的用户ID返回：{}", email, uid);
		}
		return uid;
	}

	/**
	 * 得到论坛中用户总数
	 * @return 用户总数
	 */
	public int getUserCount() {
		int count = Utils.null2Int(userDAO.createCriteria().setProjection(Projections.rowCount()).uniqueResult());
		if (logger.isDebugEnabled()) {
			logger.debug("获取论坛用户总数：{}", count);
		}
		return count;
	}

	/**
	 * 得到论坛中管理组用户总数
	 * @return 管理组用户总数
	 */
	public int getUserCountByAdmin() {
		int count = Utils.null2Int(userDAO.createCriteria(Property.forName("admingroups.admingid").gt(0))
				.setProjection(Projections.rowCount()).uniqueResult());
		if (logger.isDebugEnabled()) {
			logger.debug("获取论坛管理组用户总数：{}", count);
		}
		return count;
	}

	/**
	 * 创建新用户
	 * @param user	用户信息
	 */
	public void createUser(Users user) {
		if (exists(user.getUsername())) {
			throw new ServiceException("用户名" + user.getUsername() + "已经存在");
		}
		userDAO.save(user);
		if (logger.isDebugEnabled()) {
			logger.debug("创建新用户{}成功", user.getUid());
		}
	}

	/**
	 * 更新权限验证字符串
	 * @param uid 用户id
	 */
	public void updateAuthStr(int uid) {
		updateAuthStr(uid, Utils.getRandomString(20), 1);
	}

	/**
	 * 更新权限验证字符串
	 * @param uid 用户id
	 * @param authstr 验证串
	 * @param authflag 验证标志
	 */
	public void updateAuthStr(int uid, String authstr, int authflag) {
		Users user = getUserInfo(uid);
		user.getUserfields().setAuthstr(authstr);
		user.getUserfields().setAuthflag(authflag);
		if (logger.isDebugEnabled()) {
			logger.debug("更新用户" + uid + "权限验证字符串" + authstr);
		}
		updateUserInfo(user);
	}

	/**
	 * 更新指定用户的个人资料
	 * @param user 用户信息
	 * @return 如果用户不存在则为false, 否则为true
	 */
	public boolean updateUserInfo(Users user) {
		if (!exists(user.getUid())) {
			return false;
		}
		userDAO.save(user);
		if (logger.isDebugEnabled()) {
			logger.debug("更新用户信息成功");
		}
		return true;
	}

	/**
	 * 修改用户自定义积分字段的值
	 * @param uid 用户id
	 * @param extid 扩展字段序号(1-8)
	 * @param pos 增加的数值(可以是负数)
	 */
	public void updateUserExtCredits(int uid, int extid, double pos) {
		Users user = getUserInfo(uid);
		if (user == null) {
			return;
		}
		switch (extid) {
		case 1:
			user.setExtcredits1(user.getExtcredits1() + pos);
			break;
		case 2:
			user.setExtcredits2(user.getExtcredits2() + pos);
			break;
		case 3:
			user.setExtcredits3(user.getExtcredits3() + pos);
			break;
		case 4:
			user.setExtcredits4(user.getExtcredits4() + pos);
			break;
		case 5:
			user.setExtcredits5(user.getExtcredits5() + pos);
			break;
		case 6:
			user.setExtcredits6(user.getExtcredits6() + pos);
			break;
		case 7:
			user.setExtcredits7(user.getExtcredits7() + pos);
			break;
		case 8:
			user.setExtcredits8(user.getExtcredits8() + pos);
			break;
		}
		if (logger.isDebugEnabled()) {
			logger.debug("修改用户" + uid + "自定义积分 " + extid + " 值为" + pos);
		}
		updateUserInfo(user);
	}

	/**
	 * 获得指定用户的指定积分扩展字段的值
	 * @param uid 用户id
	 * @param extid 扩展字段序号(1-8)
	 * @return 值(如果没有找到则返回-1)
	 */
	public float getUserExtCredits(int uid, int extid) {
		float pos = Utils.null2Float(userDAO.findUnique("select extcredits" + extid + " from Users where uid=?", uid));
		if (logger.isDebugEnabled()) {
			logger.debug("获取用户" + uid + "扩展积分 " + extid + " 值为" + pos);
		}
		return pos;
	}

	/**
	 * 更新用户头像
	 * @param uid 用户id
	 * @param avatar 头像
	 * @param avatarwidth 头像宽度
	 * @param avatarheight 头像高度
	 * @param templateid 模板Id
	 * @return 如果用户不存在则返回false, 否则返回true
	 */
	public boolean updateUserPreference(int uid, String avatar, int avatarwidth, int avatarheight, int templateid) {
		Users user = getUserInfo(uid);
		if (user == null) {
			return false;
		}
		user.getUserfields().setAvatar(avatar);
		user.getUserfields().setAvatarwidth(avatarwidth);
		user.getUserfields().setAvatarheight(avatarheight);
		user.setTemplateid(templateid);
		if (logger.isDebugEnabled()) {
			logger.debug("更新用户" + uid + "头像值为" + avatar);
		}
		updateUserInfo(user);
		return true;
	}

	/**
	 * 更新用户密码
	 * @param uid 用户id
	 * @param password 密码
	 * @return 成功返回true否则false
	 */
	public boolean updateUserPassword(int uid, String password) {
		return updateUserPassword(uid, password, true);
	}

	/**
	 * 更新用户密码
	 * @param uid 用户id
	 * @param password 密码
	 * @param originalpassword 是否非MD5密码
	 * @return 成功返回true否则false
	 */
	public boolean updateUserPassword(int uid, String password, Boolean originalpassword) {
		Users user = getUserInfo(uid);
		if (user == null) {
			return false;
		}
		user.setPassword(originalpassword ? MD5.encode(password) : password);
		if (logger.isDebugEnabled()) {
			logger.debug("更新用户" + uid + "密码为" + password);
		}
		updateUserInfo(user);
		return true;
	}

	/**
	 * 更新用户安全问题
	 * @param uid 用户id
	 * @param questionid 问题id
	 * @param answer 答案
	 * @return 成功返回true否则false
	 */
	public boolean updateUserSecques(int uid, int questionid, String answer) {
		Users user = getUserInfo(uid);
		if (user == null) {
			return false;
		}
		user.setSecques(ForumUtils.getUserSecques(questionid, answer));
		if (logger.isDebugEnabled()) {
			logger.debug("更新用户" + uid + "安全提问");
		}
		updateUserInfo(user);
		return true;
	}

	/**
	 * 更新用户最后登录时间
	 * @param uid 用户id
	 * @param ip IP
	 */
	public void updateUserLastvisit(int uid, String ip) {
		Users user = getUserInfo(uid);
		if (user == null) {
			return;
		}
		user.setLastactivity(Utils.getNowTime());
		user.setLastip(ip);
		if (logger.isDebugEnabled()) {
			logger.debug("更新用户" + uid + "最后登录时间");
		}
		updateUserInfo(user);
	}

	/**
	 * 更新用户当前的在线状态
	 * @param uidlist 用户uid列表(用","分割)
	 * @param state 当前在线状态(0-3)
	 * @param activitytime
	 */
	public void updateUserOnlineState(String uidlist, int state, String activitytime) {
		String[] uids = uidlist.split(",");
		if (!Utils.isIntArray(uids)) {
			return;
		}
		for (String string : uids) {
			updateUserOnlineState(Utils.null2Int(string), state, activitytime);
		}
	}

	/**
	 * 更新用户当前的在线状态
	 * @param uid 用户uid
	 * @param state 当前在线状态(0-3)
	 * @param activitytime
	 */
	public void updateUserOnlineState(int uid, int state, String activitytime) {
		Users user = getUserInfo(uid);
		if (user == null) {
			return;
		}
		switch (state) {
		case 0: // 正常退出
			user.setLastactivity(activitytime);
			user.setOnlinestate(0);
			break;
		case 1: // 正常登录
			user.setLastvisit(activitytime);
			user.setOnlinestate(1);
			break;
		case 2: // 超时退出
			user.setLastactivity(activitytime);
			user.setOnlinestate(0);
			break;
		case 3: // 隐身登录
			user.setLastvisit(activitytime);
			user.setOnlinestate(0);
			break;
		}
		if (logger.isDebugEnabled()) {
			logger.debug("更新用户" + uid + "在线状态为：" + state);
		}
		updateUserInfo(user);
	}

	/**
	 * 更新用户在线时间
	 * @param uid
	 */
	public void updateOnlineTime(int uid) {
		Users user = getUserInfo(uid);
		if (user == null) {
			return;
		}
		try {
			Long time = Utils.howLong("m", user.getLastvisit(), Utils.getNowTime());
			user.setOltime(user.getOltime() + Utils.null2Int(time));
			if (logger.isDebugEnabled()) {
				logger.debug("更新用户{}在线时长为{}", uid, user.getOltime() + Utils.null2Int(time));
			}
		} catch (ParseException e) {
			throw new ServiceException("更新用户在线时间失败");
		}
	}

	/**
	 * 设置用户信息表中未读短消息的数量
	 * @param uid 用户ID
	 * @param pmnum 短消息数量
	 */
	public void setUserNewPMCount(int uid, int pmnum) {
		Users user = getUserInfo(uid);
		if (user == null) {
			return;
		}
		user.setNewpmcount(pmnum);
		if (logger.isDebugEnabled()) {
			logger.debug("设置用户" + uid + "未读短信数量为：" + pmnum);
		}
		updateUserInfo(user);
	}

	/**
	 * 更新指定用户的勋章信息
	 * @param uid 用户ID
	 * @param medals 勋章信息
	 */
	public void updateMedals(int uid, String medals) {
		Users user = getUserInfo(uid);
		if (user == null) {
			return;
		}
		user.getUserfields().setMedals(medals);
		if (logger.isDebugEnabled()) {
			logger.debug("更新用户" + uid + "勋章为：" + medals);
		}
		updateUserInfo(user);
	}

	/**
	 * 将用户的未读短信息数量减小一个指定的值 
	 * @param uid 用户ID 
	 * @param subval 短消息将要减小的值,负数为加
	 */
	public void decreaseNewPMCount(int uid, int subval) {
		Users user = getUserInfo(uid);
		if (user == null) {
			return;
		}
		if (user.getNewpmcount() >= 0) {
			user.setNewpmcount(user.getNewpmcount() - subval);
		}else {
			user.setNewpmcount(0);
		}
		if (logger.isDebugEnabled()) {
			logger.debug("更改用户" + uid + "未读短信数量为：" + subval);
		}
		updateUserInfo(user);
	}

	/**
	 * 将用户的未读短信息数量减一
	 * @param uid 用户ID 
	 */
	public void decreaseNewPMCount(int uid) {
		decreaseNewPMCount(uid, 1);
	}

	/**
	 * 得到用户新短消息数量
	 * @param uid 用户id
	 * @return 新短消息数
	 */
	public int getUserNewPmCount(int uid) {
		int count;
		count = Utils.null2Int(userDAO.findUnique("select newpmcount from Users where uid=?", uid), 0);
		if (logger.isDebugEnabled()) {
			logger.debug("得到用户" + uid + "新短信数量：" + count);
		}
		return count;
	}

	/**
	 * 获取指定用户名的UID和管理组ID 
	 * @param username 用户名
	 * @return
	 */
	public Object[] getUidAdminIdByUsername(String username) {
		Object object = userDAO.createQuery("select uid,admingroups.admingid from Users where username=?", username)
				.setMaxResults(1).uniqueResult();
		if (object != null) {
			return (Object[]) object;
		}
		return null;
	}

	public void updateUserPreference() {

	}

	/**
	 * 更新用户精华数
	 * @param useridlist uid列表
	 */
	public int updateUserDigest(String useridlist) {
		if (!Utils.isIntArray(useridlist.split(","))) {
			return 0;
		}
		StringBuilder hql = new StringBuilder();
		hql.append("update Users set digestposts = (");
		hql.append("select count(tid) from Topics where usersByPosterid.uid = uid and digest>0");
		hql.append(") where uid in (");
		hql.append(useridlist);
		hql.append(")");
		if (logger.isDebugEnabled()) {
			logger.debug("更新用户精华数:{}", hql.toString());
		}
		return userDAO.createQuery(hql.toString()).executeUpdate();
	}

	/**
	 * 更新用户积分
	 * @param uid
	 * @param values
	 */
	public void updateUserCredits(int uid, double[] values) {
		Users tmpUserInfo = getUserInfo(uid);
		if (tmpUserInfo == null) {
			return;
		}
		tmpUserInfo.setExtcredits1(tmpUserInfo.getExtcredits1() + values[0]);
		tmpUserInfo.setExtcredits2(tmpUserInfo.getExtcredits2() + values[1]);
		tmpUserInfo.setExtcredits3(tmpUserInfo.getExtcredits3() + values[2]);
		tmpUserInfo.setExtcredits4(tmpUserInfo.getExtcredits4() + values[3]);
		tmpUserInfo.setExtcredits5(tmpUserInfo.getExtcredits5() + values[4]);
		tmpUserInfo.setExtcredits6(tmpUserInfo.getExtcredits6() + values[5]);
		tmpUserInfo.setExtcredits7(tmpUserInfo.getExtcredits7() + values[6]);
		tmpUserInfo.setExtcredits8(tmpUserInfo.getExtcredits8() + values[7]);
		updateUserInfo(tmpUserInfo);
	}
}
