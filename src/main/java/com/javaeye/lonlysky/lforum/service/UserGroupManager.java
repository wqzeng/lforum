package com.javaeye.lonlysky.lforum.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springside.modules.orm.hibernate.SimpleHibernateTemplate;

import com.javaeye.lonlysky.lforum.cache.LForumCache;
import com.javaeye.lonlysky.lforum.comm.utils.Utils;
import com.javaeye.lonlysky.lforum.entity.forum.UserExtcreditsInfo;
import com.javaeye.lonlysky.lforum.entity.forum.Usergroups;

/**
 * 用户组业务相关操作
 * 
 * @author 黄磊
 *
 */
@Service
@Transactional
public class UserGroupManager {

	private static final Logger logger = LoggerFactory.getLogger(UserGroupManager.class);
	private SimpleHibernateTemplate<Usergroups, Integer> userGroupDAO;

	@Autowired
	private ScoresetManager scoresetManager;

	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		userGroupDAO = new SimpleHibernateTemplate<Usergroups, Integer>(sessionFactory, Usergroups.class);
	}

	/**
	 * 获取用户组列表信息
	 * 
	 * @return 用户组列表信息
	 */
	public List<Usergroups> getUserGroupList() {
		List<Usergroups> userGroupList = LForumCache.getInstance().getListCache("UserGroupList", Usergroups.class);
		if (userGroupList != null) {
			return userGroupList;
		}
		userGroupList = userGroupDAO.findAll();
		if (logger.isDebugEnabled()) {
			logger.debug("找到用户组信息{}个", userGroupList.size());
		}
		LForumCache.getInstance().addCache("UserGroupList", userGroupList);
		return userGroupList;
	}

	/**
	 * 获取用户组信息
	 * 
	 * @param groupid 用户组ID
	 * @return 用户组
	 */
	public Usergroups getUsergroup(int groupid) {
		List<Usergroups> groupList = getUserGroupList();
		Usergroups group = null;
		if (groupid == 7) { // 如果为游客则直接返回游客组
			group = groupList.get(6);
		}

		for (Usergroups usergroups : groupList) {
			if (usergroups.getGroupid() == groupid) {
				group = usergroups;
				break;
			}
		}
		if (logger.isDebugEnabled()) {
			logger.debug("获取到ID为{}的用户组{}", groupid, group.getGrouptitle());
		}
		return group;
	}

	/**
	 * 通过组ID得到允许的评分范围,如果无设置则返回空表
	 * @param usergroupid 
	 * @return 评分范围
	 */
	public List<Map<String, Object>> groupParticipateScore(int usergroupid) {
		String raterange = Utils.null2String(userGroupDAO.findUnique(
				"select raterange from Usergroups where groupid=?", usergroupid));

		if (raterange.trim().equals("")) {
			//当用户组未设置允许的评分范围时返回空表
			return null;
		} else {
			List<Map<String, Object>> tableList = new ArrayList<Map<String, Object>>();
			//向表中加载默认设置
			for (int rowcount = 0; rowcount < 8; rowcount++) {
				Map<String, Object> row = new HashMap<String, Object>();
				row.put("id", rowcount + 1);
				//是否参与积分字段
				row.put("available", false);
				//积分代号
				row.put("ScoreCode", rowcount + 1);
				//积分名称
				row.put("ScoreName", "");
				//评分最小值
				row.put("Min", "");
				//评分最大值
				row.put("Max", "");
				//24小时最大评分数
				row.put("MaxInDay", "");
				//options HTML代码 
				row.put("Options", "");
				tableList.add(row);
			}

			//通过CONFIG文件得到相关的ScoreName名称设置
			List<UserExtcreditsInfo> list = scoresetManager.getScoreSet();
			for (int count = 0; count < 8; count++) {
				if ((!list.get(count).getName().trim().equals(""))) {
					tableList.get(count).put("ScoreName", list.get(count).getName().trim());
				}
				//				else {
				//					tableList.remove(count);
				//				}
			}

			//用数据库中的记录更新已装入的默认数据
			int i = 0;
			for (String raterangestr : raterange.trim().split("\\|")) {
				if (!raterangestr.trim().equals("")) {
					String[] scoredata = raterangestr.split(",");
					System.out.println(scoredata.length);
					//判断是否参与积分字段的数据判断
					if (scoredata[1].trim().equals("True")) {
						tableList.get(i).put("available", true);
					}
					//更新其它字段
					try {
						tableList.get(i).put("Min", scoredata[4].trim());
						tableList.get(i).put("Max", scoredata[5].trim());
						tableList.get(i).put("MaxInDay", scoredata[6].trim());
					} catch (ArrayIndexOutOfBoundsException e) {
						tableList.get(i).put("Min", "");
						tableList.get(i).put("Max", "");
						tableList.get(i).put("MaxInDay", "");
					}
				}
				i++;
			}
			return tableList;
		}
	}

	/**
	 * 通过组ID得到允许的评分范围,如果无设置则返回空表
	 * @param uid 
	 * @param gid 组ID
	 * @return 评分范围
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> groupParticipateScore(int uid, int gid) {
		List<Map<String, Object>> scoreList = groupParticipateScore(gid);
		int[] extcredits = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0 };
		StringBuilder sb = new StringBuilder();
		if (scoreList != null) {
			List<Object[]> objList = userGroupDAO.find(
					"select extcredits,sum(abs(score)) from Ratelog where day(postdatetime)-day('" + Utils.getNowTime()
							+ "')=0 and users.uid=? group by extcredits", uid);
			if (objList != null) {
				for (Object[] objects : objList) {
					extcredits[Utils.null2Int(objects[0], 0)] = Utils.null2Int(objects[1], 0);
				}
			}
			Map<String, Object> map = null;
			for (int r = scoreList.size() - 1; r >= 0; r--) {
				map = scoreList.get(r);
				if (!Boolean.parseBoolean(Utils.null2String(map.get("available")))) {
					scoreList.remove(r);
					continue;
				}
				map.put("MaxInDay", Utils.null2Int(map.get("MaxInDay"), 0)
						- extcredits[Utils.null2Int(map.get("ScoreCode"), 0)]);
				if (Utils.null2Int(map.get("MaxInDay"), 0) <= 0) {
					scoreList.remove(r);
					continue;
				}
				sb.delete(0, sb.length());
				for (int i = Utils.null2Int(map.get("Min"), 0); i <= Utils.null2Int(map.get("Max"), 0); i += 4) {
					if (Math.abs(i) <= Utils.null2Int(map.get("MaxInDay"), 0)) {
						sb.append("\n<option value=\"");
						sb.append(i);
						sb.append("\">");
						sb.append(i > 0 ? "+" : "");
						sb.append(i);
						sb.append("</option>");
					}
				}
				map.put("Options", sb.toString());
				scoreList.set(r, map);
			}
		}
		if (scoreList == null) {
			return new ArrayList<Map<String, Object>>();
		}
		return scoreList;
	}
}
