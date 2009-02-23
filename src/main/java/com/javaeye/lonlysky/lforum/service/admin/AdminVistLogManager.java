package com.javaeye.lonlysky.lforum.service.admin;

import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springside.modules.orm.hibernate.SimpleHibernateTemplate;

import com.javaeye.lonlysky.lforum.comm.utils.Utils;
import com.javaeye.lonlysky.lforum.entity.forum.Adminvisitlog;
import com.javaeye.lonlysky.lforum.entity.forum.Usergroups;
import com.javaeye.lonlysky.lforum.entity.forum.Users;

/**
 * 后台访问日志管理操作类
 * 
 * @author 黄磊
 *
 */
@Service
@Transactional
public class AdminVistLogManager {

	private static final Logger logger = LoggerFactory.getLogger(AdminVistLogManager.class);

	private SimpleHibernateTemplate<Adminvisitlog, Integer> adminvisitlogDAO;

	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		adminvisitlogDAO = new SimpleHibernateTemplate<Adminvisitlog, Integer>(sessionFactory, Adminvisitlog.class);
	}

	/**
	 * 插入管理日志记录
	 * @param users 用户
	 * @param username 用户名
	 * @param usergroups 所属组
	 * @param grouptitle 所属组名称
	 * @param ip IP地址
	 * @param actions 动作
	 * @param others
	 * @return
	 */
	public boolean insertLog(Users users, String username, Usergroups usergroups, String grouptitle, String ip,
			String actions, String others) {
		Adminvisitlog log = new Adminvisitlog();
		log.setActions(actions);
		log.setGrouptitle(grouptitle);
		log.setIp(ip);
		log.setOthers(others);
		log.setUsername(username);
		log.setUsergroups(usergroups);
		log.setUsers(users);
		log.setPostdatetime(Utils.getNowTime());
		adminvisitlogDAO.save(log);
		if (logger.isDebugEnabled()) {
			logger.debug("插入管理日志记录 {}", log.getVisitid());
		}
		return true;
	}
}
