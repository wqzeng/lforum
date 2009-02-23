package com.javaeye.lonlysky.lforum.service;

import java.text.ParseException;

import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springside.modules.orm.hibernate.SimpleHibernateTemplate;

import com.javaeye.lonlysky.lforum.comm.utils.Utils;
import com.javaeye.lonlysky.lforum.entity.forum.Failedlogins;
import com.javaeye.lonlysky.lforum.exception.ServiceException;

/**
 * 登录日志操作
 * 
 * @author 黄磊
 *
 */
@Service
@Transactional
public class LoginLogManager {

	private static final Logger logger = LoggerFactory.getLogger(LoginLogManager.class);
	private SimpleHibernateTemplate<Failedlogins, String> loginlogDAO;

	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		loginlogDAO = new SimpleHibernateTemplate<Failedlogins, String>(sessionFactory, Failedlogins.class);
	}

	/**
	 * 增加错误次数并返回错误次数, 如不存在登录错误日志则建立
	 * @param ip ip地址
	 * @param update
	 * @return 错误次数
	 */
	public int updateLoginLog(String ip, boolean update) {
		Failedlogins loginLog = loginlogDAO.findUniqueByProperty("ip", ip);
		if (loginLog != null) {
			int errcount = loginLog.getErrcount();
			try {
				if (Utils.howLong("m", loginLog.getLastupdate(), Utils.getNowTime()) < 15) {
					if (errcount >= 5 || !update) {
						loginlogDAO.save(loginLog);
						return errcount;
					} else {
						loginLog.setErrcount(errcount + 1);
						loginLog.setLastupdate(Utils.getNowTime());
						loginlogDAO.save(loginLog);
						if (logger.isDebugEnabled()) {
							logger.debug("IP{}登录错误{}次", ip, errcount + 1);
						}
						return errcount + 1;
					}
				}//end if
			} catch (ParseException e) {
				throw new ServiceException("更新失败登录日志失败");
			}
			loginLog.setErrcount(1);
			loginLog.setLastupdate(Utils.getNowTime());
			loginlogDAO.save(loginLog);
			return 1;
		} else {
			if (update) {
				loginLog = new Failedlogins();
				loginLog.setIp(ip);
				loginLog.setErrcount(1);
				loginLog.setLastupdate(Utils.getNowTime());
				loginlogDAO.save(loginLog);
			}
			return 1;
		}
	}

	/**
	 * 删除指定ip地址的登录错误日志
	 * @param ip ip地址
	 */
	public void deleteLoginLog(String ip) {
		Failedlogins loginLog = loginlogDAO.findUniqueByProperty("ip", ip);
		if (loginLog != null) {
			loginlogDAO.delete(loginLog);
		}
	}
}
