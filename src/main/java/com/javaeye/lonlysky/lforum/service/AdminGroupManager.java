package com.javaeye.lonlysky.lforum.service;

import java.util.List;

import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springside.modules.orm.hibernate.SimpleHibernateTemplate;

import com.javaeye.lonlysky.lforum.cache.LForumCache;
import com.javaeye.lonlysky.lforum.entity.forum.Admingroups;

/**
 * 管理组业务相关操作
 * 
 * @author 黄磊
 *
 */
@Service
@Transactional
public class AdminGroupManager {

	private static final Logger logger = LoggerFactory.getLogger(AdminGroupManager.class);
	private SimpleHibernateTemplate<Admingroups, Integer> adminGroupDAO;

	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		adminGroupDAO = new SimpleHibernateTemplate<Admingroups, Integer>(sessionFactory, Admingroups.class);
	}

	/**
	 * 获取管理组列表
	 * 
	 * @return 管理组列表信息
	 */
	public List<Admingroups> getAdminGroupList() {
		List<Admingroups> adminGroupList = LForumCache.getInstance().getListCache("AdminGroupList", Admingroups.class);
		if (adminGroupList == null) {
			adminGroupList = adminGroupDAO.findAll();
			if (logger.isDebugEnabled()) {
				logger.debug("获取管理组列表成功：" + adminGroupList.size());
			}
			LForumCache.getInstance().addCache("AdminGroupList", adminGroupList);
		}
		return adminGroupList;
	}

	/**
	 * 获取指定ID的管理组信息
	 * 
	 * @param admingid 管理组ID
	 * @return 没有返回null
	 */
	public Admingroups getAdminGroup(int admingid) {
		if (admingid > 0) {
			List<Admingroups> adminGroupList = getAdminGroupList();
			for (Admingroups admingroups : adminGroupList) {
				if (admingroups.getAdmingid() == admingid) {
					if (logger.isDebugEnabled()) {
						logger.debug("找到ID为{}的管理组信息", admingid);
					}
					return admingroups;
				}
			}//end for
		}
		// 如果不存在则返回null
		return null;
	}

	/**
	 * 更新管理组信息
	 * 
	 * @param admingroup 管理组信息
	 */
	public void updateAdminGroup(Admingroups admingroup) {
		adminGroupDAO.save(admingroup);
		if (logger.isDebugEnabled()) {
			logger.debug("更新管理组{}信息成功", admingroup.getAdmingid());
		}
	}

	/**
	 * 创建管理组
	 * 
	 * @param admingroup 管理组
	 */

	public void createAdminGroup(Admingroups admingroup) {
		adminGroupDAO.save(admingroup);
		if (logger.isDebugEnabled()) {
			logger.debug("创建管理组{}成功", admingroup.getAdmingid());
		}
	}

	/**
	 * 删除管理组
	 * 
	 * @param admingid 管理组ID
	 */
	public void deleteAdminGroup(int admingid) {
		adminGroupDAO.delete(getAdminGroup(admingid));
		if (logger.isDebugEnabled()) {
			logger.debug("删除管理组{}成功", admingid);
		}
	}
}
