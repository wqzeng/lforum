package com.javaeye.lonlysky.lforum.service;

import java.util.List;

import org.hibernate.ObjectNotFoundException;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springside.modules.orm.hibernate.Page;
import org.springside.modules.orm.hibernate.SimpleHibernateTemplate;

import com.javaeye.lonlysky.lforum.comm.utils.Utils;
import com.javaeye.lonlysky.lforum.entity.forum.Pms;

/**
 * 短消息操作类
 * 
 * @author 黄磊
 *
 */
@Service
@Transactional
public class MessageManager {

	private static final Logger logger = LoggerFactory.getLogger(MessageManager.class);

	private SimpleHibernateTemplate<Pms, Integer> pmsDAO;

	@Autowired
	private UserManager userManager;

	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		pmsDAO = new SimpleHibernateTemplate<Pms, Integer>(sessionFactory, Pms.class);
	}

	/**
	 * 获得指定ID的短消息的内容
	 * @param pmid 短消息pmid
	 * @return 短消息pmid
	 */
	public Pms getPrivateMessageInfo(int pmid) {
		if (logger.isDebugEnabled()) {
			logger.debug("获得指定ID {} 的短消息的内容", pmid);
		}
		try {
			return pmsDAO.get(pmid);
		} catch (ObjectNotFoundException e) {
			return null;
		}
	}

	/**
	 * 得到当用户的短消息数量
	 * @param userid 用户ID
	 * @param folder 所属文件夹(0:收件箱,1:发件箱,2:草稿箱)
	 * @return 短消息数量
	 */
	public int getPrivateMessageCount(int userid, int folder) {
		return getPrivateMessageCount(userid, folder, -1);
	}

	/**
	 * 得到当用户的短消息数量
	 * @param userid 用户ID
	 * @param folder 所属文件夹(0:收件箱,1:发件箱,2:草稿箱)
	 * @param state 短消息状态(0:已读短消息、1:未读短消息、-1:全部短消息)
	 * @return 短消息数量
	 */
	public int getPrivateMessageCount(int userid, int folder, int state) {
		int count = 0;
		if (folder == -1) { // 指定用户的所有短信总数
			count = Utils
					.null2Int(
							pmsDAO
									.findUnique(
											"select count(pmid) from Pms where (usersByMsgtoid.uid=? and folder=0) or (usersByMsgfromid.uid=? and folder = 1) or (usersByMsgfromid.uid = ? and folder = 2)",
											userid, userid, userid), 0);
		} else {
			if (folder == 0) { // 收件箱
				if (state == -1) { // 全部消息
					count = Utils.null2Int(pmsDAO.findUnique(
							"select count(pmid) from Pms where usersByMsgtoid.uid=? and folder=?", userid, folder), 0);
				} else {
					count = Utils.null2Int(pmsDAO.findUnique(
							"select count(pmid) from Pms where usersByMsgtoid.uid=? and folder=? and new_=?", userid,
							folder, state), 0);
				}
			} else {
				if (state == -1) { // 全部消息
					count = Utils
							.null2Int(pmsDAO.findUnique(
									"select count(pmid) from Pms where usersByMsgfromid.uid=? and folder=?", userid,
									folder), 0);
				} else {
					count = Utils.null2Int(pmsDAO.findUnique(
							"select count(pmid) from Pms where usersByMsgfromid.uid=? and folder=? and new_=?", userid,
							folder, state), 0);
				}
			}
		}
		if (logger.isDebugEnabled()) {
			logger.debug("获取用户 {} 的短消息数量  {},folder:" + folder + ",state:" + state, userid, count);
		}
		return count;
	}

	/**
	 * 创建短消息
	 * @param pms 短消息内容
	 * @param savetosentbox 设置短消息是否在发件箱保留(0为不保留, 1为保留)
	 */
	public void createPrivateMessage(Pms pms, int savetosentbox) {
		if (pms.getFolder() != 0) {
			pms.setMsgfrom(pms.getMsgto());
		} else {
			pmsDAO.createQuery("update Users set newpmcount=abs(newpmcount*1)+1,newpm=1 where uid=?",
					pms.getUsersByMsgtoid().getUid()).executeUpdate();
		}
		pmsDAO.save(pms);
		if (logger.isDebugEnabled()) {
			logger.debug("创建短消息 {} 成功", pms.getPmid());
		}
		if (savetosentbox == 1 && pms.getFolder() == 0) {
			// 保留在发件箱
			Pms pm = new Pms();
			pm.setFolder(1);
			pm.setMessage(pms.getMessage());
			pm.setMsgfrom(pms.getMsgfrom());
			pm.setMsgto(pms.getMsgto());
			pm.setNew_(pms.getNew_());
			pm.setPostdatetime(pms.getPostdatetime());
			pm.setSubject(pms.getSubject());
			pm.setUsersByMsgfromid(pms.getUsersByMsgfromid());
			pm.setUsersByMsgtoid(pms.getUsersByMsgtoid());
			pmsDAO.save(pm);
			if (logger.isDebugEnabled()) {
				logger.debug("保留短消息 {} 到发件箱成功", pm.getPmid());
			}
		}
	}

	/**
	 * 获取指定用户的新消息
	 * @param uid
	 * @return
	 */
	public int getNewPMCount(int uid) {
		return Utils.null2Int(pmsDAO.findUnique(
				"select count(pmid) from Pms where new_=1 and folder=0 and usersByMsgtoid.uid=?", uid), 0);
	}

	/**
	 * 删除指定用户的短信息
	 * @param userid 用户ID
	 * @param pmitemid 要删除的短信息列表(数组)
	 * @return 删除记录数
	 */
	public int deletePrivateMessage(int userid, String[] pmitemid) {
		String pmidlist = "";
		for (String id : pmitemid) {
			if (!Utils.isInt(id)) {
				return -1;
			}
			pmidlist += id + ",";
		}
		pmidlist = pmidlist.substring(0, pmidlist.length() - 1);
		int reval = pmsDAO.createQuery(
				"delete from Pms where pmid in(" + pmidlist + ") and (usersByMsgtoid.uid=? or usersByMsgfromid.uid=?)",
				userid, userid).executeUpdate();
		if (reval > 0) {
			int newpmcount = getNewPMCount(userid);
			userManager.setUserNewPMCount(userid, newpmcount);
		}

		return reval;
	}

	/**
	 * 删除指定用户的一条短信息
	 * @param userid 用户ID
	 * @param pmid 要删除的短信息ID
	 * @return 删除记录数
	 */
	public int deletePrivateMessage(int userid, int pmid) {
		int reval = pmsDAO.createQuery(
				"delete from Pms where pmid=? and (usersByMsgtoid.uid=? or usersByMsgfromid.uid=?)", pmid, userid,
				userid).executeUpdate();
		if (reval > 0) {
			int newpmcount = getNewPMCount(userid);
			userManager.setUserNewPMCount(userid, newpmcount);
		}

		return reval;
	}

	/**
	 * 设置短信息状态
	 * @param pmid 短信息ID
	 * @param state 状态值
	 */
	public void setPrivateMessageState(int pmid, int state) {
		Pms pms = getPrivateMessageInfo(pmid);
		pms.setNew_(state);
		pmsDAO.save(pms);
	}

	/**
	 * 获得指定用户的短信息列表
	 * @param userid 用户ID
	 * @param folder 短信息类型(0:收件箱,1:发件箱,2:草稿箱)
	 * @param pagesize 每页显示短信息数
	 * @param pageindex 当前要显示的页数
	 * @param inttype 筛选条件
	 * @return 短信息列表
	 */
	public List<Pms> getPrivateMessageList(int userid, int folder, int pagesize, int pageindex, int inttype) {
		Page<Pms> page = new Page<Pms>(pagesize);
		page.setPageNo(pageindex);
		String msgformortoid = "usersByMsgtoid.uid";
		if (folder == 1 || folder == 2) {
			msgformortoid = "usersByMsgfromid.uid";
		}
		String hql = "from Pms where " + msgformortoid + "=? and folder=? order by pmid desc";
		if (inttype == 1) {
			hql = "from Pms where " + msgformortoid + "=? and folder=? and new_=1 order by pmid desc";
		}
		page = pmsDAO.find(page, hql, userid, folder);
		return page.getResult();
	}

	/**
	 * 返回短标题的收件箱短消息列表
	 * @param userid 用户ID
	 * @param pagesize 每页显示短信息数
	 * @param pageindex 每页显示短信息数
	 * @param inttype 筛选条件
	 * @return 收件箱短消息列表
	 */
	public List<Pms> getPrivateMessageListForIndex(int userid, int pagesize, int pageindex, int inttype) {
		List<Pms> coll = getPrivateMessageList(userid, 0, pagesize, pageindex, inttype);
		if (coll.size() > 0) {
			for (Pms pms : coll) {
				pmsDAO.getSession().evict(pms);
				pms.setMessage(Utils.format(pms.getMessage(), 20, true));
			}
		}
		return coll;
	}
}
