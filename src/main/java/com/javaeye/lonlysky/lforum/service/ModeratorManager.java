package com.javaeye.lonlysky.lforum.service;

import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springside.modules.orm.hibernate.SimpleHibernateTemplate;

import com.javaeye.lonlysky.lforum.cache.LForumCache;
import com.javaeye.lonlysky.lforum.entity.forum.Moderators;

/**
 * 版主操作类
 * 
 * @author 黄磊
 *
 */
@Service
@Transactional
public class ModeratorManager {

	private SimpleHibernateTemplate<Moderators, Integer> moderatorDAO;

	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		moderatorDAO = new SimpleHibernateTemplate<Moderators, Integer>(sessionFactory, Moderators.class);
	}

	/**
	 * 获得所有版主信息
	 * 
	 * @return 所有版主信息
	 */
	public List<Moderators> getModeratorList() {
		List<Moderators> moderatorList = LForumCache.getInstance().getListCache("ModeratorList", Moderators.class);
		if (moderatorList != null) {
			return moderatorList;
		}
		moderatorList = moderatorDAO.findAll();
		LForumCache.getInstance().addCache("ModeratorList", moderatorList);
		return moderatorList;
	}

	/**
	 * 判断指定用户是否是指定版块的版主
	 * @param admingid 管理组id
	 * @param uid 管理组id
	 * @param fid 论坛id
	 * @return 如果是版主返回true
	 */
	public boolean isModer(int admingid, int uid, int fid) {
		if (admingid == 0) {
			return false;
		}
		// 用户为管理员或总版主直接返回真
		if (admingid == 1 || admingid == 2) {
			return true;
		}
		if (admingid == 3) {
			// 如果是管理员或总版主, 或者是普通版主且在该版块有版主权限
			List<Moderators> moderList = getModeratorList();
			for (Moderators moderators : moderList) {
				// 论坛版主表中存在,则返回真
				if (moderators.getUsers().getUid() == uid && moderators.getForums().getFid() == fid) {
					return true;
				}
			}
		}
		return false;
	}

}
