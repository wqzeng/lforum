package com.javaeye.lonlysky.lforum.service.admin;

import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springside.modules.orm.hibernate.SimpleHibernateTemplate;

import com.javaeye.lonlysky.lforum.entity.forum.Forums;
import com.javaeye.lonlysky.lforum.entity.forum.Moderatormanagelog;
import com.javaeye.lonlysky.lforum.entity.forum.Topics;
import com.javaeye.lonlysky.lforum.entity.forum.Usergroups;
import com.javaeye.lonlysky.lforum.entity.forum.Users;

/**
 * 前台管理日志管理操作类
 * 
 * @author 黄磊
 *
 */
@Service
@Transactional
public class AdminModeratorLogMag {

	private static final Logger logger = LoggerFactory.getLogger(AdminModeratorLogMag.class);
	private SimpleHibernateTemplate<Moderatormanagelog, Integer> moderatorlogDAO;

	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		moderatorlogDAO = new SimpleHibernateTemplate<Moderatormanagelog, Integer>(sessionFactory,
				Moderatormanagelog.class);
	}

	/**
	 * 插入版主管理日志记录
	 * @param moderatoruid
	 * @param moderatorname 版主名
	 * @param groupid
	 * @param grouptitle 所属组名称
	 * @param ip 客户端的IP
	 * @param postdatetime
	 * @param fid
	 * @param fname 版块的名称
	 * @param tid
	 * @param title 主题的名称
	 * @param actions 动作
	 * @param reason 原因
	 * @return
	 */
	public boolean insertLog(int moderatoruid, String moderatorname, int groupid, String grouptitle, String ip,
			String postdatetime, int fid, String fname, int tid, String title, String actions, String reason) {
		Moderatormanagelog log = new Moderatormanagelog();
		Topics topics = new Topics();
		topics.setTid(tid);
		Forums forums = new Forums();
		forums.setFid(fid);
		Usergroups usergroups = new Usergroups();
		usergroups.setGroupid(groupid);
		Users users = new Users();
		users.setUid(moderatoruid);

		log.setActions(actions);
		log.setFname(fname);
		log.setForums(forums);
		log.setGrouptitle(grouptitle);
		log.setIp(ip);
		log.setModeratorname(moderatorname);
		log.setPostdatetime(postdatetime);
		log.setReason(reason);
		log.setTitle(title);
		log.setTopics(topics);
		log.setUsergroups(usergroups);
		log.setUsers(users);
		moderatorlogDAO.save(log);
		if (logger.isDebugEnabled()) {
			logger.debug("插入版主管理日志记录 {}", log.getId());
		}
		return true;
	}

}
