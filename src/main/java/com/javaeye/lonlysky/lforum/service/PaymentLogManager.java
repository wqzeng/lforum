package com.javaeye.lonlysky.lforum.service;

import java.util.List;

import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springside.modules.orm.hibernate.Page;
import org.springside.modules.orm.hibernate.SimpleHibernateTemplate;

import com.javaeye.lonlysky.lforum.comm.utils.Utils;
import com.javaeye.lonlysky.lforum.entity.forum.Paymentlog;
import com.javaeye.lonlysky.lforum.entity.forum.Topics;
import com.javaeye.lonlysky.lforum.entity.forum.Users;

/**
 * 交易日志操作类
 * 
 * @author 黄磊
 *
 */
@Service
@Transactional
public class PaymentLogManager {

	private static final Logger logger = LoggerFactory.getLogger(PaymentLogManager.class);

	private SimpleHibernateTemplate<Paymentlog, Integer> paymentlogDAO;

	@Autowired
	private ScoresetManager scoresetManager;

	@Autowired
	private UserManager userManager;

	@Autowired
	private UserCreditManager userCreditManager;

	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		paymentlogDAO = new SimpleHibernateTemplate<Paymentlog, Integer>(sessionFactory, Paymentlog.class);
	}

	/**
	 * 判断用户是否已购买主题
	 * @param tid 主题id
	 * @param uid 用户id
	 * @return
	 */
	public boolean isBuyer(int tid, int uid) {
		if (logger.isDebugEnabled()) {
			logger.debug("判断用户{}是否已购买主题{}", uid, tid);
		}
		return Utils.null2Int(paymentlogDAO.findUnique(
				"select id from Paymentlog where topics.tid=? and usersByUid.uid=?", tid, uid), 0) > 0;
	}

	/**
	 * 主题购买总次数
	 * @param tid 主题id
	 * @return
	 */
	public int getPaymentLogByTidCount(Integer tid) {
		return Utils.null2Int(paymentlogDAO.findUnique("select count(id) from Paymentlog where topics.tid=?", tid), 0);
	}

	/**
	 * 获取指定主题的购买记录
	 * @param pagesize 每页记录数
	 * @param pageid 当前页数
	 * @param tid 主题id
	 * @return
	 */
	public List<Paymentlog> getPaymentLogByTid(int pagesize, int pageid, Integer tid) {
		Page<Paymentlog> page = new Page<Paymentlog>(pagesize);
		page.setPageNo(pageid);
		page = paymentlogDAO.find(page, "from Paymentlog where topics.tid=? order by id desc", tid);
		return page.getResult();
	}

	/**
	 * 购买主题
	 * @param userid 用户
	 * @param topic 主题
	 * @param usersByPosterid 发帖者用户
	 * @param price 发帖者用户
	 * @param netamount
	 * @return
	 */
	public int buyTopic(int userid, Topics topic, Users usersByPosterid, Integer price, Float netamount) {
		Users users = userManager.getUserInfo(userid);
		if (users == null) {
			return -2;
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
		if (score < price) {
			return -1;
		}
		paymentlogDAO.createQuery(
				"update Users set extcredits" + scoresetManager.getCreditsTrans() + "=extcredits"
						+ scoresetManager.getCreditsTrans() + "-" + price + " where uid=?", userid).executeUpdate();
		paymentlogDAO.createQuery(
				"update Users set extcredits" + scoresetManager.getCreditsTrans() + "=extcredits"
						+ scoresetManager.getCreditsTrans() + "+" + netamount + " where uid=?",
				usersByPosterid.getUid()).executeUpdate();
		userCreditManager.updateUserCredits(userid);
		userCreditManager.updateUserCredits(usersByPosterid.getUid());
		Paymentlog paymentlog = new Paymentlog();
		paymentlog.setAmount(price);
		paymentlog.setBuydate(Utils.getNowTime());
		paymentlog.setNetamount(netamount.intValue());
		paymentlog.setTopics(topic);
		paymentlog.setUsersByAuthorid(usersByPosterid);
		paymentlog.setUsersByUid(users);
		paymentlogDAO.save(paymentlog);
		return 1;
	}

	/**
	 * 获取指定用户的收入交易日志
	 * @param pagesize 每页条数
	 * @param currentpage 当前页
	 * @param uid 用户id
	 * @return
	 */
	public List<Paymentlog> getPayLogInList(int pagesize, int currentpage, int uid) {
		Page<Paymentlog> page = new Page<Paymentlog>(pagesize);
		page.setPageNo(currentpage);
		page = paymentlogDAO.find(page, "from Paymentlog where usersByAuthorid.uid=? order by id desc", uid);
		return page.getResult();
	}

	/**
	 * 获取指定用户的收入日志记录数
	 * @param userid 用户id
	 * @return
	 */
	public int getPaymentLogInRecordCount(int userid) {
		return Utils.null2Int(paymentlogDAO.findUnique("select count(id) from Paymentlog where usersByAuthorid.uid=?",
				userid), 0);
	}
	
	/**
	 * 获取指定用户的支出交易日志
	 * @param pagesize 每页条数
	 * @param currentpage 当前页
	 * @param uid 用户id
	 * @return
	 */
	public List<Paymentlog> getPayLogOutList(int pagesize, int currentpage, int uid) {
		Page<Paymentlog> page = new Page<Paymentlog>(pagesize);
		page.setPageNo(currentpage);
		page = paymentlogDAO.find(page, "from Paymentlog where usersByUid.uid=? order by id desc", uid);
		return page.getResult();
	}

	/**
	 * 获取指定用户的支出日志记录数
	 * @param userid 用户id
	 * @return
	 */
	public int getPaymentLogOutRecordCount(int userid) {
		return Utils.null2Int(paymentlogDAO.findUnique("select count(id) from Paymentlog where usersByUid.uid=?",
				userid), 0);
	}
}
