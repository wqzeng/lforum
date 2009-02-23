package com.javaeye.lonlysky.lforum.service.admin;

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
import com.javaeye.lonlysky.lforum.entity.forum.Postid;
import com.javaeye.lonlysky.lforum.entity.forum.Ratelog;
import com.javaeye.lonlysky.lforum.entity.forum.Users;

/**
 * 后台评分日志管理操作类
 * 
 * @author 黄磊
 *
 */
@Service
@Transactional
public class AdminRateLogManager {

	private static final Logger logger = LoggerFactory.getLogger(AdminRateLogManager.class);
	private SimpleHibernateTemplate<Ratelog, Integer> ratelogDAO;

	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		ratelogDAO = new SimpleHibernateTemplate<Ratelog, Integer>(sessionFactory, Ratelog.class);
	}

	/**
	 * 添加评分记录
	 * @param postidlist 被评分帖子pid
	 * @param userid 评分者uid
	 * @param username 评分者用户名
	 * @param extid 分的积分类型
	 * @param score 积分数值
	 * @param reason 评分理由
	 * @return
	 */
	public int insertLog(String postidlist, int userid, String username, int extid, int score, String reason) {
		int reval = 0;
		Users users = new Users();
		users.setUid(userid);
		for (String pid : postidlist.split(",")) {
			Ratelog ratelog = new Ratelog();
			ratelog.setExtcredits(extid);
			ratelog.setPostdatetime(Utils.getNowTime());
			ratelog.setReason(reason);
			ratelog.setScore(score);
			ratelog.setUsername(username);
			ratelog.setUsers(users);
			Postid postid = new Postid();
			postid.setPid(Utils.null2Int(pid, 0));
			ratelog.setPostid(postid);
			ratelogDAO.save(ratelog);
			reval++;
		}
		if (logger.isDebugEnabled()) {
			logger.debug("添加评分记录{}个,postidlist:{},userid:" + userid, reval, postidlist);
		}
		return reval;
	}

	/**
	 * 删除日志
	 * @return
	 */
	public boolean deleteLog() {
		return ratelogDAO.createQuery("delete from Ratelog").executeUpdate() > 1;
	}

	/**
	 * 按指定条件删除日志
	 * @param condition
	 * @return
	 */
	public boolean deleteRateLog(String condition) {
		return ratelogDAO.createQuery("delete from Ratelog where " + condition).executeUpdate() > 1;
	}

	/**
	 * 得到指定查询条件下的评分日志数
	 * @param condition 查询条件
	 * @return
	 */
	public int recordCount(String condition) {
		return Utils.null2Int(ratelogDAO.findUnique("select count(id) from Ratelog where " + condition), 0);
	}

	/**
	 * 得到当前指定条件和页数的评分日志记录
	 * @param pagesize 当前分页的尺寸大小
	 * @param currentpage 当前页码
	 * @param condition 查询条件
	 * @return
	 */
	public List<Ratelog> logList(int pagesize, int currentpage, String condition) {
		Page<Ratelog> page = new Page<Ratelog>(pagesize);
		page.setPageNo(currentpage);
		page = ratelogDAO.find(page, "from Ratelog where "+condition+" order by id desc");
		return page.getResult();
	}
}
