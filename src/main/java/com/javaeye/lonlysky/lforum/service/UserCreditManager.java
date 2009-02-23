package com.javaeye.lonlysky.lforum.service;

import java.util.List;

import org.dom4j.Document;
import org.dom4j.Element;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springside.modules.orm.hibernate.SimpleHibernateTemplate;

import com.javaeye.lonlysky.lforum.comm.utils.Utils;
import com.javaeye.lonlysky.lforum.comm.utils.XmlElementUtil;
import com.javaeye.lonlysky.lforum.config.impl.ConfigLoader;
import com.javaeye.lonlysky.lforum.entity.forum.CreditsOperationType;
import com.javaeye.lonlysky.lforum.entity.forum.Usergroups;
import com.javaeye.lonlysky.lforum.entity.forum.Users;

/**
 * 用户积分操作类
 * 
 * @author 黄磊
 */
@Service
@Transactional
public class UserCreditManager {

	private static final Logger logger = LoggerFactory.getLogger(UserCreditManager.class);

	private SimpleHibernateTemplate<Users, Integer> userDAO;

	@Autowired
	private ScoresetManager scoresetManager;

	@Autowired
	private UserGroupManager userGroupManager;

	@Autowired
	private UserManager userManager;

	@Autowired
	private OnlineUserManager onlineUserManager;

	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		userDAO = new SimpleHibernateTemplate<Users, Integer>(sessionFactory, Users.class);
	}

	/**
	 * 根据积分公式更新用户积分
	 * @param formula 积分公式
	 */
	public void updateUserCredits(String formula) {
		userDAO.createQuery("update Users set credits=" + formula).executeUpdate();
	}

	/**
	 * 根据积分公式更新用户积分,并且受分数变动影响有可能会更改用户所属的用户组
	 * @param uid 用户ID
	 * @return
	 */
	public int updateUserCredits(int uid) {
		Users tmpUserInfo = userManager.getUserInfo(uid);
		if (tmpUserInfo == null) {
			return 0;
		}

		userDAO.createQuery("update Users set credits=" + scoresetManager.getScoreCalFormula() + " where uid=?", uid)
				.executeUpdate();
		Usergroups tmpUserGroupInfo = userGroupManager.getUsergroup(tmpUserInfo.getUsergroups().getGroupid());
		if (tmpUserGroupInfo != null && tmpUserGroupInfo.getSystem() == 0
				&& tmpUserGroupInfo.getAdmingroups().getAdmingid() == 0) {
			tmpUserGroupInfo = getCreditsUserGroupID(tmpUserInfo.getCredits());
			tmpUserInfo.setUsergroups(tmpUserGroupInfo);
			userDAO.save(tmpUserInfo);
			onlineUserManager.updateGroupid(uid, tmpUserGroupInfo.getGroupid());
		} else {
			//当用户是已删除的特殊组成员时，则运算相应积分，并更新该用户所属组信息
			if (tmpUserGroupInfo != null && tmpUserGroupInfo.getGroupid() == 7
					&& tmpUserInfo.getAdmingroups().getAdmingid() == -1) {
				tmpUserGroupInfo = getCreditsUserGroupID(tmpUserInfo.getCredits());
				tmpUserInfo.setUsergroups(tmpUserGroupInfo);
				userManager.updateUserInfo(tmpUserInfo);
				onlineUserManager.updateGroupid(uid, tmpUserGroupInfo.getGroupid());
			}
		}
		if (logger.isDebugEnabled()) {
			logger.debug("根据积分公式更新用户{}积分", uid);
		}
		return 1;
	}

//	/**
//	 * 通过指定值更新用户积分
//	 * @param uid 用户ID 	
//	 * @param values 积分变动值,应保证是一个长度为8的数组,对应8种扩展积分的变动值
//	 * @param allowMinus 是否允许被扣成负分,true允许,false不允许并且不进行扣分返回-1
//	 * @return
//	 */
//	public int updateUserCredits(int uid, double[] values, boolean allowMinus) {
//		Users tmpUserInfo = userManager.getUserInfo(uid);
//		if (tmpUserInfo == null) {
//			return 0;
//		}
//		if (values.length < 8) {
//			return -1;
//		}
//		if (!allowMinus)//不允许扣成负分时要进行判断积分是否足够被减
//		{
//			// 如果要减扩展积分, 首先判断扩展积分是否足够被减
//			if (!checkUserCreditsIsEnough(uid, values)) {
//				return -1;
//			}
//		}
//		tmpUserInfo.setExtcredits1(values[0]);
//		tmpUserInfo.setExtcredits2(values[1]);
//		tmpUserInfo.setExtcredits3(values[2]);
//		tmpUserInfo.setExtcredits4(values[3]);
//		tmpUserInfo.setExtcredits5(values[4]);
//		tmpUserInfo.setExtcredits6(values[5]);
//		tmpUserInfo.setExtcredits7(values[6]);
//		tmpUserInfo.setExtcredits8(values[7]);
//		userDAO.save(tmpUserInfo);
//
//		if (logger.isDebugEnabled()) {
//			logger.debug("通过指定值更新用户{}积分", uid);
//		}
//
//		///更新用户积分
//		return updateUserCredits(uid);
//	}
	
	/**
	 * 通过指定值更新用户积分
	 * @param uid 用户ID
	 * @param values 积分变动值,应保证是一个长度为8的数组,对应8种扩展积分的变动值
	 * @param allowMinus 是否允许被扣成负分,true允许,false不允许并且不进行扣分返回-1
	 * @return
	 */
	private int updateUserCredits(int uid, double[] values, boolean allowMinus) {
		Users tmpUserInfo = userManager.getUserInfo(uid);
		if (tmpUserInfo == null) {
			return 0;
		}

		if (values.length < 8) {
			return -1;
		}
		if (!allowMinus)//不允许扣成负分时要进行判断积分是否足够被减
		{
			// 如果要减扩展积分, 首先判断扩展积分是否足够被减
			if (!checkUserCreditsIsEnough(uid, values)) {
				return -1;
			}
		}
		userManager.updateUserCredits(uid, values);

		///更新用户积分
		return updateUserCredits(uid);
	}

	/**
	 * 通过指定值更新用户积分(积分不够时不扣,返回-1)
	 * @param uid 用户ID
	 * @param values 积分变动值,应保证是一个长度为8的数组,对应8种扩展积分的变动值
	 * @return
	 */
	public int updateUserCredits(int uid, double[] values) {
		return updateUserCredits(uid, values, false);
	}

	/**
	 * 检查用户积分是否足够被减(适用于单用户, 单个或多个积分)
	 * @param uid 用户ID
	 * @param mount 更新数量,比如由上传2个附件引发此操作,那么此参数值应为2
	 * @param creditsOperationType 积分操作类型,如发帖等
	 * @param pos 加或减标志(正数为加,负数为减,通常被传入1或者-1)
	 * @param allowMinus 是否允许被扣成负分,true允许,false不允许并且不进行扣分返回-1
	 * @return
	 */
	public int updateUserCredits(int uid, int mount, int creditsOperationType, int pos, boolean allowMinus) {
		Users tmpUserInfo = userManager.getUserInfo(uid);
		if (tmpUserInfo == null) {
			return 0;
		}

		Document document = scoresetManager.getScoreDoc(ConfigLoader.getConfig().getWebpath());
		List<Element> recordList = XmlElementUtil.findElements("record", document.getRootElement());
		Element element = recordList.get(creditsOperationType);

		// 如果要减扩展积分, 首先判断扩展积分是否足够被减
		if (pos < 0) {
			//当不是删除主题或回复时
			if (creditsOperationType != CreditsOperationType.POSTTOPIC
					&& creditsOperationType != CreditsOperationType.POSTREPLY) {
				if (!allowMinus && !checkUserCreditsIsEnough(uid, element, pos, mount)) {
					return -1;
				}
			}
		}
		double[] values = new double[8];
		for (int i = 1; i <= values.length; i++) {
			values[i - 1] = getDouble("extcredits" + i, element) * pos * mount;
			if (pos < 0 && mount < 0) {
				values[i - 1] = -Utils.null2Int(values[i - 1]);
			}
		}
		tmpUserInfo.setExtcredits1(tmpUserInfo.getExtcredits1() + values[0]);
		tmpUserInfo.setExtcredits2(tmpUserInfo.getExtcredits2() + values[1]);
		tmpUserInfo.setExtcredits3(tmpUserInfo.getExtcredits3() + values[2]);
		tmpUserInfo.setExtcredits4(tmpUserInfo.getExtcredits4() + values[3]);
		tmpUserInfo.setExtcredits5(tmpUserInfo.getExtcredits5() + values[4]);
		tmpUserInfo.setExtcredits6(tmpUserInfo.getExtcredits6() + values[5]);
		tmpUserInfo.setExtcredits7(tmpUserInfo.getExtcredits7() + values[6]);
		tmpUserInfo.setExtcredits8(tmpUserInfo.getExtcredits8() + values[7]);

		userManager.updateUserInfo(tmpUserInfo);

		// 更新用户积分
		return updateUserCredits(uid);
	}

	/**
	 * 根据用户列表,一次更新多个用户的积分
	 * @param uidlist 用户ID列表
	 * @param values 扩展积分值
	 * @return
	 */
	private int updateUserCredits(String uidlist, double[] values) {
		if (Utils.isIntArray(uidlist.split(","))) {
			///根据公式计算用户的总积分,并更新
			int reval = 0;
			for (String uid : uidlist.split(",")) {
				if (Utils.null2Int(uid, 0) > 0) {
					reval = reval + updateUserCredits(Utils.null2Int(uid, 0), values, true);
				}
			}
			return reval;
		}
		return -1;
	}

	/**
	 * 根据用户列表,一次更新多个用户的积分
	 * @param uidlist 用户ID列表
	 * @param creditsOperationType 积分操作类型,如发帖等
	 * @param pos 加或减标志(正数为加,负数为减,通常被传入1或者-1)
	 * @return
	 */
	private int updateUserCredits(String uidlist, int creditsOperationType, int pos) {
		if (Utils.isIntArray(uidlist.split(","))) {
			///根据公式计算用户的总积分,并更新
			int reval = 0;
			for (String uid : uidlist.split(",")) {
				if (Utils.null2Int(uid, 0) > 0) {
					reval = reval + updateUserCredits(Utils.null2Int(uid, 0), 1, creditsOperationType, pos);
				}
			}
			return reval;
		}
		return 0;
	}


	/**
	 * 根据用户列表,一次更新多个用户的积分(此方法只在删除主题时使用过)
	 * @param uidlist 用户ID列表 
	 * @param creditsOperationType 积分操作类型,如发帖等
	 * @param pos 加或减标志(正数为加,负数为减,通常被传入1或者-1)
	 * @return
	 */
	private int updateUserCredits(int[] uidlist, int creditsOperationType, int pos) {
		///根据公式计算用户的总积分,并更新
		int reval = 0;
		for (int i = 0; i < uidlist.length; i++) {
			if (uidlist[i] > 0) {
				reval = reval + updateUserCredits(uidlist[i], 1, creditsOperationType, pos, true);
			}
		}

		return reval;
	}

	/**
	 * 根据用户列表,一次更新多个用户的积分(此方法只在删除主题时使用过)
	 * @param uidlist 用户ID列表
	 * @param mountlist 更新数量,比如由上传2个附件引发此操作,那么此参数值应为2,数组长度应与uidlist相同
	 * @param creditsOperationType 积分操作类型,如发帖等
	 * @param pos 加或减标志(正数为加,负数为减,通常被传入1或者-1)
	 * @return
	 */
	private int updateUserCredits(int[] uidlist, int[] mountlist, int creditsOperationType, int pos) {
		///根据公式计算用户的总积分,并更新
		int reval = 0;
		for (int i = 0; i < uidlist.length; i++) {
			if (uidlist[i] > 0) {
				reval = reval + updateUserCredits(uidlist[i], mountlist[i], creditsOperationType, pos, true);
			}
		}

		return reval;
	}

	/**
	 * 检查用户积分是否足够被减(适用于单用户, 单个或多个积分)
	 * @param uid 用户ID
	 * @param mount 更新数量,比如由上传2个附件引发此操作,那么此参数值应为2
	 * @param creditsOperationType 积分操作类型,如发帖等
	 * @param pos 加或减标志(正数为加,负数为减,通常被传入1或者-1)
	 * @return
	 */
	public int updateUserCredits(int uid, int mount, int creditsOperationType, int pos) {
		return updateUserCredits(uid, mount, creditsOperationType, pos, false);
	}

	/**
	 * 检查用户积分是否足够被减(适用于单用户, 单个或多个积分)
	 * @param uid 用户ID
	 * @param creditsOperationType 积分操作类型,如发帖等
	 * @param pos 加或减标志(正数为加,负数为减,通常被传入1或者-1)
	 * @return
	 */
	public int updateUserCredits(int uid, int creditsOperationType, int pos) {
		return updateUserCredits(uid, 1, creditsOperationType, pos);
	}

	/**
	 * 用户发表主题时更新用户的积分
	 * @param uid 用户id
	 * @param values 积分变动值,应保证是一个长度为8的数组,对应8种扩展积分的变动值
	 * @return
	 */
	public int updateUserCreditsByPostTopic(int uid, double[] values) {
		return updateUserCredits(uid, values);
	}

	/**
	 * 用户发表主题时更新用户的积分
	 * @param uid 用户id
	 * @return
	 */
	public int updateUserCreditsByPostTopic(int uid) {
		return updateUserCredits(uid, CreditsOperationType.POSTTOPIC, 1);
	}

	/**
	 * 用户上传附件时更新用户的积分
	 * @param uid 用户id
	 * @param mount 上传附件数量
	 * @return
	 */
	public int updateUserCreditsByUploadAttachment(int uid, int mount) {
		return updateUserCredits(uid, mount, CreditsOperationType.UPLOADATTACHMENT, 1);
	}

	/**
	 * 用户下载附件时更新用户的积分
	 * @param uid 用户id
	 * @param mount 下载附件数量
	 * @return
	 */
	public int updateUserCreditsByDownloadAttachment(int uid, int mount) {
		return updateUserCredits(uid, mount, CreditsOperationType.DOWNLOADATTACHMENT, -1);
	}

	/**
	 * 用户下载附件时更新用户的积分
	 * @param uid 用户id
	 * @return
	 */
	public int updateUserCreditsByDownloadAttachment(int uid) {
		return updateUserCreditsByDownloadAttachment(uid, 1);
	}

	/**
	 * 用户上传附件时更新用户的积分
	 * @param uid 用户id
	 * @return
	 */
	public int updateUserCreditsByUploadAttachment(int uid) {
		return updateUserCreditsByUploadAttachment(uid, 1);
	}

	/**
	 * 用户发表回复时更新用户的积分
	 * @param uid 用户id
	 * @return
	 */
	public int updateUserCreditsByPosts(int uid) {
		return updateUserCredits(uid, CreditsOperationType.POSTREPLY, 1);
	}

	/**
	 * 用户搜索时更新用户的积分
	 * @param userid 用户id
	 * @return
	 */
	public int updateUserCreditsBySearch(int userid) {
		return updateUserCredits(userid, CreditsOperationType.SEARCH, -1);
	}

	/**
	 * 用户发表回复时更新用户的积分
	 * @param uid 用户id
	 * @return
	 */
	public int updateUserCreditsByPosts(int uid, double[] values) {
		return updateUserCredits(uid, values);
	}

	/*
	 * 获取String
	 */
	private String getString(String name, Element el) {
		return Utils.null2String(XmlElementUtil.findElement(name, el).getText());
	}

	/*
	 * 获取Float
	 */
	private double getDouble(String name, Element el) {
		return Utils.null2Double(getString(name, el), 0);
	}

	/**
	 * 根据积分获得积分用户组所应该匹配的用户组描述 (如果没有匹配项或用户非积分用户组则返回null)
	 * @param Credits 积分
	 * @return 用户组描述
	 */
	public Usergroups getCreditsUserGroupID(float Credits) {
		List<Usergroups> usergroupinfo = userGroupManager.getUserGroupList();
		Usergroups tmpitem = null;

		for (Usergroups infoitem : usergroupinfo) {
			// 积分用户组的特征是radminid等于0
			if (infoitem.getAdmingroups().getAdmingid() == 0 && infoitem.getSystem() == 0
					&& (Credits >= infoitem.getCreditshigher() && Credits <= infoitem.getCreditslower())) {
				if (tmpitem == null || infoitem.getCreditshigher() > tmpitem.getCreditshigher()) {
					tmpitem = infoitem;
				}
			}
		}

		return tmpitem == null ? new Usergroups() : tmpitem;
	}

	/**
	 * 用户发送短消息时更新用户的积分
	 * @param userid 用户id
	 * @return
	 */
	public int updateUserCreditsBySendpms(int userid) {
		return updateUserCredits(userid, CreditsOperationType.SENDMESSAGE, 1);
	}

	/**
	 * 检查用户积分是否足够被减(适用于单用户, 单个或多个积分)
	 * @param userid 用户ID
	 * @param mount 更新数量,比如由上传2个附件引发此操作,那么此参数值应为2
	 * @param creditsOperationType 积分操作类型,如发帖等
	 * @param pos 加或减标志(正数为加,负数为减,通常被传入1或者-1)
	 * @return
	 */
	public boolean checkUserCreditsIsEnough(int userid, int mount, int creditsOperationType, int pos) {
		Document document = scoresetManager.getScoreDoc(ConfigLoader.getConfig().getWebpath());
		List<Element> recordList = XmlElementUtil.findElements("record", document.getRootElement());
		Element element = recordList.get(creditsOperationType);

		for (int i = 1; i < 9; i++) {
			//只要任何一项要求减分,就去数据库检查
			if (Utils.null2Float(XmlElementUtil.findElement("extcredits" + i, element), 0) < 0) {
				return checkUserCreditsIsEnough(userid, element, pos, mount);
			}
		}
		return true;
	}

	/**
	 * 检查用户积分是否足够被减(适用于单用户, 单个或多个积分)
	 * @param userid
	 * @param element
	 * @param pos
	 * @param mount
	 * @return
	 */
	public boolean checkUserCreditsIsEnough(int userid, Element element, int pos, int mount) {
		Users users = userManager.getUserInfo(userid);
		if (users == null) {
			return false;
		} else {
			double ext1 = Utils.null2Double(XmlElementUtil.findElement("extcredits1", element), 0) * pos * mount;
			double ext2 = Utils.null2Double(XmlElementUtil.findElement("extcredits2", element), 0) * pos * mount;
			double ext3 = Utils.null2Double(XmlElementUtil.findElement("extcredits3", element), 0) * pos * mount;
			double ext4 = Utils.null2Double(XmlElementUtil.findElement("extcredits4", element), 0) * pos * mount;
			double ext5 = Utils.null2Double(XmlElementUtil.findElement("extcredits5", element), 0) * pos * mount;
			double ext6 = Utils.null2Double(XmlElementUtil.findElement("extcredits6", element), 0) * pos * mount;
			double ext7 = Utils.null2Double(XmlElementUtil.findElement("extcredits7", element), 0) * pos * mount;
			double ext8 = Utils.null2Double(XmlElementUtil.findElement("extcredits8", element), 0) * pos * mount;
			ext1 = ext1 >= 0 ? Math.abs(ext1) : 0;
			ext2 = ext2 >= 0 ? Math.abs(ext2) : 0;
			ext3 = ext3 >= 0 ? Math.abs(ext3) : 0;
			ext4 = ext4 >= 0 ? Math.abs(ext4) : 0;
			ext5 = ext5 >= 0 ? Math.abs(ext5) : 0;
			ext6 = ext6 >= 0 ? Math.abs(ext6) : 0;
			ext7 = ext7 >= 0 ? Math.abs(ext7) : 0;
			ext8 = ext8 >= 0 ? Math.abs(ext8) : 0;
			if (users.getExtcredits1() >= ext1 && users.getExtcredits2() >= ext2 && users.getExtcredits3() >= ext3
					&& users.getExtcredits4() >= ext4 && users.getExtcredits5() >= ext5
					&& users.getExtcredits6() >= ext6 && users.getExtcredits7() >= ext7
					&& users.getExtcredits8() >= ext8) {
				return true;
			} else {
				return false;
			}
		}
	}

	/**
	 * 检查用户积分是否足够被减(适用于单用户, 单个或多个积分)
	 * @param uid
	 * @param values
	 * @return
	 */
	public boolean checkUserCreditsIsEnough(int uid, double[] values) {
		Users users = userManager.getUserInfo(uid);
		if (users == null) {
			return false;
		} else {
			for (double f : values) {
				f = f >= 0 ? Math.abs(f) : 0;
			}
			if (users.getExtcredits1() >= values[0] && users.getExtcredits2() >= values[1]
					&& users.getExtcredits3() >= values[2] && users.getExtcredits4() >= values[3]
					&& users.getExtcredits5() >= values[4] && users.getExtcredits6() >= values[5]
					&& users.getExtcredits7() >= values[6] && users.getExtcredits8() >= values[7]) {
				return true;
			} else {
				return false;
			}
		}
	}

	/**
	 * 用户所发表的主题或帖子被置为精华时更新用户的积分
	 * @param uid 用户id
	 * @param mount 被置为精华的主题或帖子数量
	 * @return
	 */
	public int updateUserCreditsByDigest(int uid, int mount) {
		return updateUserCredits(uid, mount, CreditsOperationType.DIGEST, 1, true);
	}

	/**
	 * 用户所发表的主题或帖子被置为精华时更新用户的积分
	 * @param useridlist 用户id列表
	 * @param mount 被置为精华的主题或帖子数量
	 * @return
	 */
	public int updateUserCreditsByDigest(String useridlist, int mount) {
		if (!useridlist.equals("")) {
			if (Utils.isIntArray(useridlist.split(","))) {
				return updateUserCredits(useridlist, CreditsOperationType.DIGEST, 1);
			}
		}
		return 0;
	}

	/**
	 * 版主删除论坛主题
	 * @param tuidlist 要删除的主题用户id
	 * @param auidlist 要删除的主题对应的的附件数量,应与tuidlist长度相同
	 * @param pos
	 */
	public int updateUserCreditsByDeleteTopic(int[] tuidlist, int[] auidlist, int pos) {
		return updateUserCredits(tuidlist, CreditsOperationType.POSTTOPIC, pos)
				+ updateUserCredits(tuidlist, auidlist, CreditsOperationType.UPLOADATTACHMENT, pos);

	}

	/**
	 * 用户对帖子评分时更新用户的积分
	 * @param useridlist
	 * @param extcreditslist
	 * @return
	 */
	public int updateUserCreditsByRate(String useridlist, double[] extcreditslist) {
		return updateUserCredits(useridlist, extcreditslist);
	}

	/**
	 * 用户发表回复时更新用户的积分
	 * @param uid 用户id
	 * @param pos
	 * @return
	 */
	public int updateUserCreditsByPosts(int uid, int pos) {
		return updateUserCredits(uid, CreditsOperationType.POSTREPLY, pos);

	}

	/**
	 * 用户参与投票时更新用户的积分
	 * @param userid 用户id
	 * @return
	 */
	public int updateUserCreditsByVotepoll(int userid) {
		return updateUserCredits(userid, CreditsOperationType.VOTE, 1);
	}

}
