package com.javaeye.lonlysky.lforum.service;

import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springside.modules.orm.hibernate.SimpleHibernateTemplate;

import com.javaeye.lonlysky.lforum.cache.LForumCache;
import com.javaeye.lonlysky.lforum.entity.forum.Smilies;

/**
 * 表情符操作类
 * 
 * @author 黄磊
 *
 */
@Service
@Transactional
@SuppressWarnings("unchecked")
public class SmilieManager {

	private SimpleHibernateTemplate<Smilies, Integer> smileDAO;

	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		smileDAO = new SimpleHibernateTemplate<Smilies, Integer>(sessionFactory, Smilies.class);
	}

	/**
	 * 获得表情分类列表
	 * 
	 * @return 表情分类列表
	 */
	public List<Smilies> getSmilieTypes() {
		return smileDAO.find("from Smilies where type=? Order By displayorder,id", 0);
	}

	/**
	 * 得到表情符数据,包括表情分类
	 * @return 表情符表
	 */
	public List<Smilies> getSmiliesList() {
		return smileDAO.find("from Smilies Order By type,displayorder,id");
	}

	/**
	 * 得到不带分类的表情符数据
	 * @return 表情符表
	 */
	public List<Smilies> getSmiliesListWithoutType() {
		return smileDAO.find("from Smilies where type<>0 order by type,displayorder,id");
	}

	/**
	 * 将缓存中的表情信息返回,不包括表情分类
	 * @return
	 */
	public List<Smilies> getSmiliesListWithInfo() {
		List<Smilies> smilieList = LForumCache.getInstance().getListCache("SmiliesListWithInfo", Smilies.class);
		if (smilieList != null) {
			return smilieList;
		}
		smilieList = getSmiliesListWithoutType();
		if (smilieList.size() <= 0) {
			return null;
		}
		LForumCache.getInstance().addCache("SmiliesListWithInfo", smilieList);
		return smilieList;

	}
}
