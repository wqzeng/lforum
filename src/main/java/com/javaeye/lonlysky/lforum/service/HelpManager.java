package com.javaeye.lonlysky.lforum.service;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springside.modules.orm.hibernate.SimpleHibernateTemplate;

import com.javaeye.lonlysky.lforum.comm.utils.Utils;
import com.javaeye.lonlysky.lforum.entity.help.Help;
import com.javaeye.lonlysky.lforum.exception.ServiceException;

/**
 * 论坛帮助操作类
 * 
 * @author 黄磊
 *
 */
@Service
@Transactional
public class HelpManager {

	private static final Logger logger = LoggerFactory.getLogger(HelpManager.class);
	private SimpleHibernateTemplate<Help, Integer> helpDAO;

	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		helpDAO = new SimpleHibernateTemplate<Help, Integer>(sessionFactory, Help.class);
	}

	/**
	 * 获取帮助列表
	 * 
	 * @return 帮助列表
	 */
	@SuppressWarnings("unchecked")
	public List<Help> getHelpList() {
		List<Help> helpList = new ArrayList<Help>();
		List<Integer> ids = helpDAO.find("select id from Help where pid=0 Order By orderby ASC");
		for (Integer integer : ids) {
			List<Help> tmpList = getHelpList(integer);
			for (Help help : tmpList) {
				helpList.add(help);
			}
		}
		if (logger.isDebugEnabled()) {
			logger.debug("获取所有帮助信息列表");
		}
		return helpList;
	}

	/**
	 * 获取帮助分类以及相应帮助主题
	 * @param pid
	 * @return 帮助分类以及相应帮助主题
	 */
	@SuppressWarnings("unchecked")
	public List<Help> getHelpList(int pid) {
		if (logger.isDebugEnabled()) {
			logger.debug("获取分类{}帮助信息列表", pid);
		}
		List<Help> heList = helpDAO.find("from Help where pid=? or id=? Order By pid ASC,orderby ASC", pid, pid);
		return heList.size() > 0 ? heList : null;
	}

	/**
	 * 帮助数量
	 * 
	 * @return 帮助数量
	 */
	public int getHelpcount() {
		int count = Utils.null2Int(helpDAO.createCriteria().setProjection(Projections.rowCount()).uniqueResult(), 0);
		if (logger.isDebugEnabled()) {
			logger.debug("所有帮助数量{}", count);
		}
		return count;
	}

	/**
	 * 获取帮助信息
	 * 
	 * @param id 帮助ID
	 * @return 帮助信息
	 */
	public Help getHelp(int id) {
		return helpDAO.get(id);
	}

	/**
	 * 更新帮助信息
	 * 
	 * @param id 帮助ID
	 * @param title 帮助标题
	 * @param message 帮助内容
	 * @param pid 帮助
	 * @param orderby 排序方式
	 */
	public void updatehelp(int id, String title, String message, int pid, int orderby) {
		Help help = getHelp(id);
		if (help == null) {
			throw new ServiceException("找不到ID为" + id + "的帮助信息");
		}
		help.setTitle(title);
		help.setMessage(message);
		help.setPid(pid);
		help.setOrderby(orderby);
		helpDAO.save(help);
	}

	/**
	 * 增加帮助
	 * @param title 帮助标题
	 * @param message 帮助标题
	 * @param pid 帮助
	 */
	public void addHelp(String title, String message, int pid) {
		int count = getHelpcount();
		Help help = new Help();
		help.setTitle(title);
		help.setMessage(message);
		help.setPid(pid);
		help.setOrderby(count);
		helpDAO.save(help);
		if (logger.isDebugEnabled()) {
			logger.debug("增加帮助{}成功,ID{}", help.getTitle(), help.getId());
		}
	}

	/**
	 * 删除帮助
	 * @param id 帮助ID
	 */
	public void delHelp(int id) {
		if (logger.isDebugEnabled()) {
			logger.debug("删除帮助{}", id);
		}
		helpDAO.delete(id);
	}

	/**
	 * 通过PID来确定是否为分类
	 * @param pid 属于的分类ID
	 * @return 是否为分类
	 */
	public static boolean choosepage(int pid) {
		return pid == 0;
	}

}
