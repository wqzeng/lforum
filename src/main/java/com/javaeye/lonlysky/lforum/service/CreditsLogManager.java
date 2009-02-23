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
import com.javaeye.lonlysky.lforum.entity.forum.Creditslog;
import com.javaeye.lonlysky.lforum.entity.forum.Users;

/**
 * 积分转帐历史记录操作类
 * 
 * @author 黄磊
 *
 */
@Service
@Transactional
public class CreditsLogManager {

	private final static Logger logger = LoggerFactory.getLogger(CreditsLogManager.class);
	private SimpleHibernateTemplate<Creditslog, Integer> creditslogDAO;

	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		creditslogDAO = new SimpleHibernateTemplate<Creditslog, Integer>(sessionFactory, Creditslog.class);
	}

	/**
	 * 添加积分转帐兑换记录
	 * @param uid 用户id
	 * @param fromto 来自/到
	 * @param sendcredits 付出积分类型
	 * @param receivecredits 得到积分类型
	 * @param send 付出积分数额
	 * @param receive 得到积分数额
	 * @param paydate 时间
	 * @param operation 积分操作(1=兑换, 2=转帐)
	 */
	public void addCreditsLog(int uid, int fromto, int sendcredits, int receivecredits, float send, float receive,
			String paydate, int operation) {
		Creditslog creditslog = new Creditslog();
		creditslog.setOperation(operation);
		creditslog.setPaydate(paydate);
		creditslog.setReceive(receive);
		creditslog.setReceivecredits(receivecredits);
		creditslog.setSend(send);
		creditslog.setSendcredits(sendcredits);
		Users usersByFromto = new Users();
		usersByFromto.setUid(fromto);
		creditslog.setUsersByFromto(usersByFromto);
		Users usersByUid = new Users();
		usersByUid.setUid(uid);
		creditslog.setUsersByUid(usersByUid);
		creditslogDAO.save(creditslog);
		if (logger.isDebugEnabled()) {
			logger.debug("添加积分转帐兑换记录 {}", creditslog.getId());
		}
	}

	/**
	 * 返回指定范围的积分日志
	 * @param pagesize 页大小
	 * @param currentpage 当前页数
	 * @param uid 用户id
	 * @return 积分日志
	 */
	public List<Creditslog> getCreditsLogList(int pagesize, int currentpage, int uid) {
		Page<Creditslog> page = new Page<Creditslog>(pagesize);
		page.setPageNo(currentpage);
		page = creditslogDAO.find(page,
				"from Creditslog where usersByUid.uid=? or usersByFromto.uid=? order by id desc", uid, uid);
		return page.getResult();
	}

	/**
	 * 获得指定用户的积分交易历史记录总条数
	 * @param userid 用户id
	 * @return 历史记录总条数
	 */
	public int getCreditsLogRecordCount(int userid) {
		return Utils.null2Int(creditslogDAO.findUnique(
				"select count(id) from Creditslog where usersByUid.uid=? or usersByFromto.uid=?", userid, userid), 0);
	}

}
