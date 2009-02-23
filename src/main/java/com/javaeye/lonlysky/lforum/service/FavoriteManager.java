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
import com.javaeye.lonlysky.lforum.entity.forum.Favorites;
import com.javaeye.lonlysky.lforum.entity.forum.Topics;
import com.javaeye.lonlysky.lforum.entity.forum.Users;

/**
 * 收藏夹操作类
 * 
 * @author 黄磊
 *
 */
@Service
@Transactional
public class FavoriteManager {

	private static final Logger logger = LoggerFactory.getLogger(FavoriteManager.class);

	private SimpleHibernateTemplate<Favorites, Integer> favoriteDAO;

	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		favoriteDAO = new SimpleHibernateTemplate<Favorites, Integer>(sessionFactory, Favorites.class);
	}

	/**
	 * 创建收藏信息
	 * @param uid 用户ID
	 * @param tid 主题ID
	 */
	public void createFavorites(int uid, int tid) {
		Favorites favorites = new Favorites();
		Topics topics = new Topics();
		topics.setTid(tid);
		Users users = new Users();
		users.setUid(uid);

		favorites.setTopics(topics);
		favorites.setUsers(users);
		favorites.setTypeid(0);

		favoriteDAO.save(favorites);
		if (logger.isDebugEnabled()) {
			logger.debug("创建收藏夹 {} ,UID：{},TID：" + tid, favorites.getId(), uid);
		}
	}

	/**
	 * 删除指定用户的收藏信息
	 * @param uid 用户id
	 * @param fidlist 要删除的收藏信息id列表,以英文逗号分割
	 */
	public int deleteFavorites(int uid, String[] fitemid, int type) {
		String fidlist = "";
		for (String string : fitemid) {
			if (!Utils.isInt(string)) {
				return -1;
			}
			fidlist += string + ",";
		}
		if (fidlist.equals("")) {
			return -1;
		}
		fidlist = fidlist.substring(0, fidlist.length() - 1);

		return favoriteDAO.createQuery(
				"delete from Favorites where topics.tid in(" + fidlist + ") and users.uid=? and typeid=?", uid, type)
				.executeUpdate();
	}

	/**
	 * 得到用户收藏信息列表
	 * @param uid 用户id
	 * @param pagesize 分页时每页的记录数
	 * @param pageindex 当前页码
	 * @return
	 */
	public List<Favorites> getFavoritesList(int uid, int pagesize, int pageindex) {
		Page<Favorites> page = new Page<Favorites>(pagesize);
		page.setPageNo(pageindex);
		page = favoriteDAO.find(page, "from Favorites where typeid=0 and users.uid=?", uid);
		return page.getResult();
	}

	/**
	 * 得到用户收藏的总数
	 * @param uid 用户id
	 * @return 收藏总数
	 */
	public int getFavoritesCount(int uid) {
		return Utils.null2Int(favoriteDAO.findUnique("select count(id) from Favorites where users.uid=?", uid), 0);
	}

	/**
	 * 收藏夹里是否包含了指定的项
	 * @param uid 用户Id
	 * @param tid 主题Id
	 * @return
	 */
	public int checkFavoritesIsIN(int uid, int tid) {
		return Utils.null2Int(favoriteDAO.findUnique(
				"select count(id) from Favorites where users.uid=? and typeid=0 and topics.tid=?", uid, tid), 0);
	}

}
