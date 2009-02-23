package com.javaeye.lonlysky.lforum.service.admin;

import java.util.List;

import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springside.modules.orm.hibernate.SimpleHibernateTemplate;

import com.javaeye.lonlysky.lforum.GlobalsKeys;
import com.javaeye.lonlysky.lforum.cache.LForumCache;
import com.javaeye.lonlysky.lforum.comm.utils.Utils;
import com.javaeye.lonlysky.lforum.entity.forum.Admingroups;
import com.javaeye.lonlysky.lforum.entity.forum.Forums;
import com.javaeye.lonlysky.lforum.entity.forum.Moderators;
import com.javaeye.lonlysky.lforum.entity.forum.Users;
import com.javaeye.lonlysky.lforum.service.ForumManager;
import com.javaeye.lonlysky.lforum.service.UserManager;

/**
 * 后台论坛版块管理类
 * 
 * @author 黄磊
 *
 */
@Service
@Transactional
public class AdminForumManager {

	private static final Logger logger = LoggerFactory.getLogger(AdminForumManager.class);
	private SimpleHibernateTemplate<Forums, Integer> forumDAO;
	private SimpleHibernateTemplate<Moderators, Integer> moderatorDAO;

	@Autowired
	private ForumManager forumManager;

	@Autowired
	private UserManager userManager;

	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		forumDAO = new SimpleHibernateTemplate<Forums, Integer>(sessionFactory, Forums.class);
		moderatorDAO = new SimpleHibernateTemplate<Moderators, Integer>(sessionFactory, Moderators.class);
	}

	/**
	 * 设置版块列表中层数(layer)和父列表(parentidlist)字段
	 */
	public void setForumslayer() {
		List<Forums> forumList = forumManager.getForumList();
		for (Forums forum : forumList) {
			int layer = 0;
			String parentidlist = "";
			int parentid = forum.getForums().getFid();

			//如果是(分类)顶层则直接更新数据库
			if (parentid == 0) {
				//				forum.setLayer(layer);
				//				forum.setParentidlist("0");
				//				forumManager.updateForum(forum);
				System.out.println("顶层直接更新数据库:" + forum.getFid());
				forumDAO
						.createQuery("update Forums set layer=?,parentidlist=? where fid=?", layer, "0", forum.getFid())
						.executeUpdate();
				continue;
			}

			do { //更新子版块的层数(layer)和父列表(parentidlist)字段
				int tmp = parentid;
				parentid = Utils.null2Int(forumDAO.findUnique("select forums.fid from Forums where fid=?", parentid));
				layer++;
				if (parentid != 0) {
					parentidlist = tmp + "," + parentidlist;
				} else {
					parentidlist = tmp + "," + parentidlist;
					//					forum.setLayer(layer);
					//					forum.setParentidlist(parentidlist.substring(0, parentidlist.length() - 1));
					//					forumManager.updateForum(forum);
					System.out.println("更新子版块：" + forum.getFid() + ",层数:" + layer + ",上级ID列表："
							+ parentidlist.substring(0, parentidlist.length() - 1));
					forumDAO.createQuery("update Forums set layer=?,parentidlist=? where fid=?", layer,
							parentidlist.substring(0, parentidlist.length() - 1), forum.getFid()).executeUpdate();
					break;
				}
			} while (true);
		}
		if (logger.isDebugEnabled()) {
			logger.debug("设置版块列表中层数(layer)和父列表(parentidlist)字段");
		}

	}

	public static String childNode = "0";

	/**
	 * 递归所有子节点并返回字符串
	 * @param correntfid 当前
	 * @return 子版块的集合,格式:1,2,3,4,
	 */
	@SuppressWarnings("unchecked")
	public String findChildNode(int correntfid) {
		synchronized (childNode) {

			List<Object> list = forumDAO.find("select fid from Forums where forums.fid=? order by displayorder asc",
					correntfid);

			childNode = childNode + "," + correntfid;

			if (list.size() > 0) {
				//有子节点
				for (Object object : list) {
					findChildNode(Utils.null2Int(object));
				}
			}
			if (logger.isDebugEnabled()) {
				logger.debug("递归所有子节点{}", childNode);
			}
			return childNode;
		}
	}

	/**
	 * 设置论坛字版数和显示顺序
	 */
	public void setForumsSubForumCountAndDispalyorder() {
		if (logger.isDebugEnabled()) {
			logger.debug("设置论坛字版数和显示顺序");
		}
		List<Forums> forumList = forumManager.getForumList();
		for (Forums forum : forumList) {
			int subcount = forumDAO.find("select fid from Forums where forums.fid=?", forum.getFid()).size();
			//			forumDAO.createQuery("update Forums set subforumcount=? where fid=?", subcount, forum.getFid())
			//					.executeUpdate();
			forum.setSubforumcount(subcount);
			forumManager.updateForum(forum);
			System.out.println("更新子版块数量为：" + forum.getSubforumcount() + ",板块：" + forum.getFid());
		}

		if (forumList.size() == 1)
			return;

		int displayorder = 1;
		String fidlist;
		for (Forums forum : forumManager.getForumList("forums.fid=0")) {
			if (forum.getForums().getFid() == 0) {
				childNode = "0";
				fidlist = ("," + findChildNode(forum.getFid())).replace(",0,", "");
				for (String fidstr : fidlist.split(",")) {
					//					forumDAO.createQuery("update Forums set displayorder=? where fid=?", displayorder,
					//							Utils.null2Int(fidstr)).executeUpdate();
					Forums forums = forumDAO.get(Utils.null2Int(fidstr));
					forums.setDisplayorder(displayorder);
					forumManager.updateForum(forums);
					System.out.println("更新板块：" + forums.getFid() + "显示顺序：" + forums.getDisplayorder());
					displayorder++;
				}

			}
		}
	}

	/**
	 * 移动论坛版块
	 * @param currentfid 当前论坛版块id
	 * @param targetfid 目标论坛版块id
	 * @param isaschildnode 是否作为子论坛移动
	 * @return
	 */
	@Transactional(readOnly = true)
	public void moveForumsPos(int currentfid, int targetfid, boolean isaschildnode) {
		if (logger.isDebugEnabled()) {
			logger.debug("移动板块,当前板块：{},目标板块：{},是否作为子论坛：" + isaschildnode, currentfid, targetfid);
		}
		//取得当前论坛版块的信息
		Forums currentForum = forumDAO.get(currentfid);

		//取得目标论坛版块的信息
		Forums targetForum = forumDAO.get(targetfid);

		//当前论坛版块带子版块时
		if (forumDAO.find("select fid from Forums where forums.fid=?", currentfid).size() > 0) {
			System.out.println("当前论坛版块带子版块");
			if (isaschildnode) { //作为论坛子版块插入
				//让位于当前论坛版块(分类)显示顺序之后的论坛版块全部加1(为新加入的论坛版块让位结果)
				forumDAO.createQuery("update Forums set displayorder=displayorder+1 where displayorder>=?",
						(targetForum.getDisplayorder() + 1)).executeUpdate();

				//更新当前论坛版块的相关信息
				currentForum.setForums(targetForum);
				currentForum.setDisplayorder(targetForum.getDisplayorder() + 1);
				//				forumDAO.createQuery("update Forums set forums.fid=?,displayorder=? where fid=?", targetForum.getFid(),
				//						targetForum.getDisplayorder() + 1, currentfid).executeUpdate();
			} else { //作为同级论坛版块,在目标论坛版块之前插入
				//让位于包括当前论坛版块显示顺序之后的论坛版块全部加1(为新加入的论坛版块让位结果)
				forumDAO.createQuery("update Forums set displayorder=displayorder+1 where displayorder>=? or fid=?",
						targetForum.getDisplayorder(), targetForum.getFid()).executeUpdate();
				//更新当前论坛版块的相关信息
				currentForum.setForums(targetForum.getForums());
				currentForum.setDisplayorder(targetForum.getDisplayorder());
				//				forumDAO.createQuery("update Forums set forums.fid=?,displayorder=? where fid=?",
				//						targetForum.getForums().getFid(), targetForum.getDisplayorder(), currentfid).executeUpdate();

			}

			//更新由于上述操作所影响的版块数和帖子数
			if (currentForum.getTopics_1() != 0 && currentForum.getTopics_1() > 0
					&& (currentForum.getPosts() != 0 && currentForum.getPosts() > 0)) {
				if (!currentForum.getParentidlist().trim().equals("")) {
					forumDAO.createQuery(
							"update Forums set topics_1=topics_1-" + currentForum.getTopics_1() + ",posts=posts-"
									+ currentForum.getPosts() + " where fid in("
									+ currentForum.getParentidlist().trim() + ")").executeUpdate();
				}
				if (!targetForum.getParentidlist().trim().equals("")) {
					forumDAO.createQuery(
							"update Forums set topics_1=topics_1+" + currentForum.getTopics_1() + ",posts=posts+"
									+ currentForum.getPosts() + " where fid in(" + targetForum.getParentidlist().trim()
									+ ")").executeUpdate();
				}
			}
		} else { //当前论坛版块不带子版
			System.out.println("当前论坛版块不带子版");
			//设置旧的父一级的子论坛数
			forumDAO.createQuery("update Forums set subforumcount=subforumcount-1 where fid=?",
					currentForum.getForums().getFid()).executeUpdate();

			//让位于当前节点显示顺序之后的节点全部减1 [起到删除节点的效果]
			if (isaschildnode) { //作为子论坛版块插入
				//更新相应的被影响的版块数和帖子数				
				if ((currentForum.getTopics_1() != 0) && (currentForum.getTopics_1() > 0)
						&& (currentForum.getPosts() != 0) && (currentForum.getPosts() > 0)) {
					forumDAO.createQuery(
							"update Forums set topics_1=topics_1-" + currentForum.getTopics_1() + ",posts=posts-"
									+ currentForum.getPosts() + " where fid in("
									+ currentForum.getParentidlist().trim() + ")").executeUpdate();
					if (!targetForum.getParentidlist().trim().equals("0")) {
						forumDAO.createQuery(
								"update Forums set topics_1=topics_1+" + currentForum.getTopics_1() + ",posts=posts+"
										+ currentForum.getPosts() + " where fid in("
										+ targetForum.getParentidlist().trim() + "," + targetfid + ")").executeUpdate();
					}
				}

				//让位于当前论坛版块显示顺序之后的论坛版块全部加1(为新加入的论坛版块让位结果)
				forumDAO.createQuery("update Forums set displayorder=displayorder+1 where displayorder>=?",
						targetForum.getDisplayorder() + 1).executeUpdate();

				//设置新的父一级的子论坛数
				targetForum.setSubforumcount(targetForum.getSubforumcount() + 1);
				//				forumDAO.createQuery("update Forums set subforumcount=subforumcount+1 where fid=?", targetfid)
				//						.executeUpdate();

				String parentidlist = null;
				if (targetForum.getParentidlist().trim().equals("0")) {
					parentidlist = targetfid + "";
				} else {
					parentidlist = targetForum.getParentidlist().trim() + "," + targetfid;
				}

				//更新当前论坛版块的相关信息
				currentForum.setForums(targetForum);
				currentForum.setLayer(targetForum.getLayer() + 1);
				currentForum.setPathlist(targetForum.getPathlist().trim() + "<a href=\"showforum.action?forumid="
						+ currentfid + "\">" + currentForum.getName().trim().replace("'", "''") + "</a>");
				currentForum.setParentidlist(parentidlist);
				currentForum.setDisplayorder(targetForum.getDisplayorder() + 1);
				System.out.println("作为子论坛版块插入：" + currentForum.getLayer());
				//				forumDAO.createQuery(
				//						"update Forums set forums.fid=?,layer=?,pathlist=?,parentidlist=?,displayorder=? where fid=?",
				//						targetForum.getFid(),
				//						targetForum.getLayer() + 1,
				//						targetForum.getPathlist().trim() + "<a href=\"showforum.action?forumid=" + currentfid + "\">"
				//								+ currentForum.getName().trim().replace("'", "''") + "</a>", parentidlist,
				//						targetForum.getDisplayorder() + 1, currentfid).executeUpdate();

			} else { //作为同级论坛版块,在目标论坛版块之前插入
				//更新相应的被影响的版块数和帖子数
				if ((currentForum.getTopics_1() != 0) && (currentForum.getTopics_1() > 0)
						&& (currentForum.getPosts() != 0) && (currentForum.getPosts() > 0)) {
					forumDAO.createQuery(
							"update Forums set topics_1=topics_1-" + currentForum.getTopics_1() + ",posts=posts-"
									+ currentForum.getPosts() + " where fid in("
									+ currentForum.getParentidlist().trim() + ")").executeUpdate();
					forumDAO.createQuery(
							"update Forums set topics_1=topics_1+" + currentForum.getTopics_1() + ",posts=posts+"
									+ currentForum.getPosts() + " where fid in(" + targetForum.getParentidlist().trim()
									+ ")").executeUpdate();

				}

				//让位于包括当前论坛版块显示顺序之后的论坛版块全部加1(为新加入的论坛版块让位结果)
				forumDAO.createQuery("update Forums set displayorder=displayorder+1 where displayorder>=? or fid=?",
						targetForum.getDisplayorder() + 1, targetForum.getFid()).executeUpdate();

				//设置新的父一级的子论坛数
				forumDAO.createQuery("update Forums set subforumcount=subforumcount+1 where fid=?",
						targetForum.getForums().getFid()).executeUpdate();
				String parentpathlist = Utils.null2String(forumDAO.findUnique(
						"select pathlist from Forums where fid=?", targetForum.getForums().getFid()));

				//更新当前论坛版块的相关信息
				currentForum.setForums(targetForum.getForums());
				currentForum.setLayer(targetForum.getLayer());
				currentForum.setPathlist(parentpathlist + "<a href=\"showforum.action?forumid=" + currentfid + "\">"
						+ currentForum.getName().trim() + "</a>");
				currentForum.setParentidlist(targetForum.getParentidlist().trim());
				currentForum.setDisplayorder(targetForum.getDisplayorder());
				//				forumDAO.createQuery(
				//						"update Forums set forums.fid=?,layer=?,pathlist=?,parentidlist=?,displayorder=? where fid=?",
				//						targetForum.getForums().getFid(),
				//						targetForum.getLayer(),
				//						parentpathlist + "<a href=\"showforum.action?forumid=" + currentfid + "\">"
				//								+ currentForum.getName().trim() + "</a>", targetForum.getParentidlist().trim(),
				//						targetForum.getDisplayorder(), currentfid).executeUpdate();
			}
		}
		forumManager.updateForum(currentForum);
		forumManager.updateForum(targetForum);
	}

	/**
	 * 移动论坛版块
	 * @param currentfid 当前论坛版块id
	 * @param targetfid 目标论坛版块id
	 * @param isaschildnode 是否作为子论坛移动
	 * @return
	 */
	public boolean movingForumsPos(int currentfid, int targetfid, boolean isaschildnode) {
		moveForumsPos(currentfid, targetfid, isaschildnode);
		setForumslayer();
		setForumsSubForumCountAndDispalyorder();
		setForumsPathList();

		LForumCache.getInstance().removeCache("ForumListBoxOptions");
		LForumCache.getInstance().removeCache("ForumList");
		return true;
	}

	/**
	 * 设置版块列表中论坛路径(pathlist)字段
	 */
	@Transactional(readOnly = true)
	public void setForumsPathList() {
		List<Forums> forumList = forumManager.getForumList();
		for (Forums forum : forumList) {
			String pathlist = "";

			if (forum.getParentidlist().trim().equals("0")) {

				pathlist = "<a href=\"showforum.action?forumid=" + forum.getFid() + "\">" + forum.getName().trim()
						+ "</a>";
			} else {
				for (String parentid : forum.getParentidlist().trim().split(",")) {
					if (!parentid.trim().equals("")) {
						Forums tmpForums = forumDAO.get(Utils.null2Int(parentid));
						if (tmpForums != null) {

							pathlist += "<a href=\"showforum.action?forumid=" + tmpForums.getFid() + "\">"
									+ tmpForums.getName().trim() + "</a>";
						}
					}
				}

				pathlist += "<a href=\"showforum.action?forumid=" + forum.getFid() + "\">" + forum.getName().trim()
						+ "</a>";
			}

			forum.setPathlist(pathlist);
			forumManager.updateForum(forum);
			if (logger.isDebugEnabled()) {
				logger.debug("设置论坛 {} 路径为 {}", forum.getFid(), forum.getPathlist());
			}
		}
	}

	/**
	 * 获得用于树形的板块列表
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Object[]> getForumTree() {
		return forumDAO.find("select fid,name from Forums");
	}

	/**
	 * 在当前节点之后加入同级论坛时的displayorder字段值
	 * @param minDisplayOrder
	 */
	public void updateForumsDisplayOrder(int minDisplayOrder) {
		forumDAO.createQuery("update Forums set displayorder=displayorder+1 where displayorder>?", minDisplayOrder);
	}

	/**
	 * 向版块列表中插入新的版块信息
	 * @param foruminfo
	 * @return
	 */
	public String insertForumsInf(Forums foruminfo) {
		forumDAO.save(foruminfo);

		setForumsPathList();
		LForumCache.getInstance().removeCache("ForumListBoxOptions");
		LForumCache.getInstance().removeCache("ForumList");
		LForumCache.getInstance().removeCache("HotForumList");
		LForumCache.getInstance().removeCache("ForumHotTopicList");
		LForumCache.getInstance().removeCache("ForumNewTopicList");
		if (logger.isDebugEnabled()) {
			logger.debug("添加新的论坛板块 {} 成功", foruminfo.getFid());
		}
		return setForumsModerators(foruminfo.getFid(), foruminfo.getForumfields().getModerators(), foruminfo
				.getInheritedmod());

	}

	/**
	 * 设置指定论坛版块版主
	 * @param fid 指定的论坛版块id
	 * @param moderators 相关要设置的版主名称(注:用","号分割)
	 * @param inheritedmod 是否使用继承选项 1为使用  0为不使用
	 * @return
	 */
	public String setForumsModerators(int fid, String moderators, int inheritedmod) {
		deleteModeratorByFid(fid);

		//使用继承机制时
		if (inheritedmod == 1) {
			int parentid = fid;
			String parendidlist = "-1";
			while (true) {
				parentid = Utils.null2Int(forumDAO.findUnique(
						"select forums.fid from Forums where inheritedmod=1 and fid=?", fid));
				if (parentid == -1) {
					break;
				}
				if (parentid == 0) {
					break;
				}

				parendidlist = parendidlist + "," + parentid;

			}

			int count = 1;
			for (Users user : getUidModeratorByFid(parendidlist)) {
				addModerator(user, fid, count, 1);
				count++;
			}
		}

		insertForumsModerators(fid, moderators, 1, 0);

		return updateUserInfoWithModerator(moderators);
	}

	/**
	 * 更新当前已设置为指定版块版主的相关用户信息
	 * @param moderators 相关要设置的版主名称(注:用","号分割)
	 * @return 返回不存在用户的字符串
	 */
	public String updateUserInfoWithModerator(String moderators) {
		moderators = moderators == null ? "" : moderators;
		String usernamenoexsit = "";
		Object obj = new Object();
		for (String moderator : moderators.split(",")) {
			if (!moderator.equals("")) {
				//当用户名是系统保留的用户名,请您输入其它的用户名
				if (GlobalsKeys.SYSTEM_USERNAME.equals(moderator)) {
					continue;
				}

				obj = getModeratorInfo(moderator);
				if (obj != null) {
					Object[] objects = (Object[]) obj;
					int groupid = Utils.null2Int(objects[1]);
					if ((groupid <= 3) && (groupid > 0))
						continue; //当为管理员,超级版主,版主时
					else {
						int radminid = Utils.null2Int(forumDAO.findUnique(
								"select admingroups.admingid from Usergroups where groupid=?", groupid));
						if (radminid <= 0)
							setModerator(moderator);
						else
							continue;
					}
				} else {
					usernamenoexsit = usernamenoexsit + moderator + ",";
				}
			}

		}

		AdminCacheManager.reSetModeratorList();

		return usernamenoexsit;
	}

	public void setModerator(String moderator) {
		forumDAO.createQuery("update Users set admingroups.admingid=3,usergroups.groupid=3 where username=?",
				moderator.trim()).executeUpdate();
		forumDAO.createQuery("update Online set admingroups.admingid=3,usergroups.groupid=3 where username=?",
				moderator.trim()).executeUpdate();
	}

	/**
	 * 获取版主信息
	 * @param moderator 版主名称
	 * @return
	 */
	public Object getModeratorInfo(String moderator) {
		return moderatorDAO
				.createQuery(
						"select uid,usergroups.groupid from Users where usergroups.groupid<>7 and usergroups.groupid<>8 and username=?",
						moderator).setMaxResults(1).uniqueResult();
	}

	/**
	 * 向版主列表中插入相关的版主信息
	 * @param fid 向版主列表中插入相关的版主信息
	 * @param moderators 相关要设置的版主名称(注:用","号分割)
	 * @param displayorder 显示顺序
	 * @param inherited 是否使用继承机制
	 */
	@Transactional(readOnly = true)
	public void insertForumsModerators(int fid, String moderators, int displayorder, int inherited) {
		moderators = moderators == null ? "" : moderators;
		int count = displayorder;
		Forums forums = forumDAO.get(fid);
		//数据库中存在的用户
		String usernamelist = "";
		//清除已有论坛的版主设置
		for (String username : moderators.split(",")) {
			if (!username.trim().equals("")) {
				Object object = forumDAO.createQuery(
						"from Users where usergroups.groupid<>7 and usergroups.groupid<>8 and username=?", username)
						.setMaxResults(1).uniqueResult();
				//先取出当前节点的信息
				if (object != null) {
					Moderators moderator = new Moderators();
					moderator.setDisplayorder(count);
					moderator.setUsers((Users) object);
					moderator.setForums(forums);
					moderator.setInherited(inherited);

					usernamelist = usernamelist + username.trim() + ",";
					count++;
				}
			}
		}

		if (!usernamelist.equals("")) {
			forums.getForumfields().setModerators(moderators);
		} else {
			forums.getForumfields().setModerators("");
		}
		forumDAO.save(forums);
		AdminCacheManager.reSetModeratorList();
	}

	/**
	 * 添加论坛版主
	 * @param users 用户
	 * @param fid 板块ID
	 * @param displayorder 显示顺序
	 * @param inherited 是否使用继承选项  1为使用  0为不使用
	 */
	public void addModerator(Users users, int fid, int displayorder, int inherited) {
		Forums forums = new Forums();
		forums.setFid(fid);

		Moderators moderators = new Moderators();
		moderators.setDisplayorder(displayorder);
		moderators.setForums(forums);
		moderators.setInherited(inherited);
		moderators.setUsers(users);

		moderatorDAO.save(moderators);

		if (logger.isDebugEnabled()) {
			logger.debug("添加论坛 {} 版主 {} 成功", fid, moderators.getId());
		}
	}

	/**
	 * 获取指定板块版主列表
	 * @param fidlist
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Users> getUidModeratorByFid(String fidlist) {
		if (logger.isDebugEnabled()) {
			logger.debug("获取指定板块{}版主列表", fidlist);
		}
		return forumDAO.find("select distinct users from Moderators where forums.fid in(" + fidlist + ")");
	}

	/**
	 * 清除已有论坛的版主设置
	 * 
	 * @param fid
	 */
	public void deleteModeratorByFid(int fid) {
		forumDAO.createQuery("delete from Moderators where forums.fid=?", fid).executeUpdate();
	}

	/**
	 * 检测指定板块ID下是否有子板块
	 * @param fid 板块ID
	 * @return 是否
	 */
	public boolean isExistSubForum(int fid) {
		int count = Utils.null2Int(forumDAO.findUnique("select count(fid) from Forums where forums.fid=?", fid), 0);
		return count > 0 ? true : false;
	}

	/**
	 * 删除指定fid的论坛版块
	 * @param fid 要删除的论坛版块的fid
	 * @return
	 */
	@Transactional(readOnly = true)
	public boolean deleteForumsByFid(int fid) {
		if (isExistSubForum(fid)) {
			return false;
		}
		//先取出当前节点的信息
		Forums forums = forumDAO.get(fid);

		//调整在当前节点排序位置之后的节点,做减1操作
		forumDAO.createQuery("update Forums set displayorder=displayorder-1 where displayorder>?",
				forums.getDisplayorder()).executeUpdate();

		//修改父结点中的子论坛个数
		forumDAO
				.createQuery("update Forums set subforumcount=subforumcount-1 where fid=?", forums.getForums().getFid())
				.executeUpdate();

		//删除相关投票的信息
		forumDAO.createQuery("delete from Polls where topics.tid in(select tid from Topics where forums.fid=?)", fid)
				.executeUpdate();

		//删除帖子附件表中的信息
		forumDAO
				.createQuery(
						"delete from Attachments where topics.tid in(select tid from Topics where forums.fid=? or postid.pid in(select pid from Posts where forums.fid=?))",
						fid, fid).executeUpdate();

		//删除相关帖子
		forumDAO.createQuery("delete from Posts where forums.fid=?", fid).executeUpdate();

		//删除相关主题
		forumDAO.createQuery("delete from Topics where forums.fid=?", fid).executeUpdate();

		//删除当前节点
		forumDAO.delete(forums);

		//删除版主列表中的相关信息
		forumDAO.createQuery("delete from Moderators where forums.fid=?", fid).executeUpdate();

		LForumCache.getInstance().removeCache("ForumListBoxOptions");
		LForumCache.getInstance().removeCache("ForumList");

		if (logger.isDebugEnabled()) {
			logger.debug("删除论坛板块 {} 成功", fid);
		}
		return true;
	}

	/**
	 * 检测指定论坛是否为顶级论坛
	 * @param fid 
	 * @return
	 */
	public int getTopForum(int fid) {
		return Utils.null2Int(forumDAO.createQuery("select fid from Forums where forums.fid=0 and fid=?", fid), -1);
	}

	/**
	 * 合并版块
	 * @param sourcefid 合并版块
	 * @param targetfid 目标论坛版块
	 * @return
	 */
	@Transactional(readOnly = true)
	public boolean combinationForums(int sourcefid, int targetfid) {
		if (isExistSubForum(sourcefid)) {
			return false;
		} else {
			childNode = "0";
			String fidlist = ("," + findChildNode(targetfid)).replace(",0,", "");

			//更新帖子与主题的信息
			forumDAO.createQuery("update Topics set forums.fid=? where forums.fid=?", targetfid, sourcefid)
					.executeUpdate();

			//要更新目标论坛的主题数
			int totaltopics = Utils.null2Int(forumDAO.findUnique("select count(tid) from Topics where forums.fid in ("
					+ fidlist + ")"), 0);
			int totalposts = 0;

			forumDAO.createQuery("update Posts set forums.fid=? where forums.fid=?", targetfid, sourcefid)
					.executeUpdate();

			//要更新目标论坛的帖子数
			totalposts = totalposts
					+ Utils.null2Int(forumDAO.findUnique("select count(pid) from Posts where forums.fid in (" + fidlist
							+ ")"), 0);

			// 获取论坛信息
			Forums targetForum = forumDAO.get(targetfid);
			Forums sourceForum = forumDAO.get(sourcefid);

			targetForum.setTopics_1(totaltopics);
			targetForum.setPosts(totalposts);

			//调整在当前节点排序位置之后的节点,做减1操作
			forumDAO.createQuery("update Forums set displayorder=displayorder-1 where displayorder>?",
					sourceForum.getDisplayorder()).executeUpdate();

			//修改父结点中的子论坛个数
			forumDAO.createQuery("update Forums set subforumcount=subforumcount-1 where fid=?",
					sourceForum.getForums().getFid()).executeUpdate();

			//删除源论坛版块
			forumDAO.delete(sourceForum);
			forumManager.updateForum(targetForum);

			LForumCache.getInstance().removeCache("ForumListBoxOptions");
			LForumCache.getInstance().removeCache("ForumList");
			if (logger.isDebugEnabled()) {
				logger.debug("合并论坛 {} 到目标论坛 {}", sourcefid, targetfid);
			}
			return true;
		}
	}

	/**
	 * 获取当前UID是否为非指定板块的版主
	 * @param currentfid 板块
	 * @param uid UID
	 * @return 不是则返回-1
	 */
	public int getUidInModeratorsByUid(int currentfid, int uid) {
		return Utils.null2Int(forumDAO.createQuery(
				"select users.uid  from Moderators where forums.fid<>? and users.uid=?", currentfid, uid)
				.setMaxResults(1).uniqueResult());
	}

	/**
	 * 对比指定的论坛版块的新老信息,将作出相应的调整
	 * @param oldmoderators 老版主名称(注:用","号分割)
	 * @param newmoderators 新版主名称(注:用","号分割)
	 * @param currentfid 当前论坛版块的fid
	 */
	public void compareOldAndNewModerator(String oldmoderators, String newmoderators, int currentfid) {
		if (Utils.null2String(oldmoderators).equals("")) {
			return;
		}

		//在新的版主名单中查找老的版主是否存在
		for (String oldmoderator : oldmoderators.split(",")) {
			if ((!oldmoderator.equals("")) && ("," + newmoderators + ",").indexOf("," + oldmoderator + ",") < 0) //当不存在，则表示当前老的版主已被删除，则执行删除当前老版主
			{
				Object[] objects = userManager.getUidAdminIdByUsername(oldmoderator);
				if (objects != null) //当前用户存在
				{
					int uid = Utils.null2Int(objects[0]);
					int radminid = Utils.null2Int(objects[1]);

					//在其他版块未曾设置为版主 ,则不作任何处理
					if ((getUidInModeratorsByUid(currentfid, uid) != -1) && (radminid != 1)) {
						Users userinfo = userManager.getUserInfo(uid);

						forumDAO.createQuery("update Online] SET usergroups.groupid=?  where users.uid=?", userinfo
								.getUsergroups().getGroupid(), uid);
						Admingroups admingroups = new Admingroups();
						admingroups.setAdmingid(0);
						userinfo.setAdmingroups(admingroups);
						userManager.updateUserInfo(userinfo);
					}
				}
			}
		}
	}

	/**
	 * 保存论坛版块(分类)的相关信息
	 * @param forums
	 * @return
	 */
	public String saveForumsInf(Forums forums) {
		forumManager.updateForum(forums);
		setForumsPathList();

		LForumCache.getInstance().removeCache("ForumListBoxOptions");
		LForumCache.getInstance().removeCache("ForumList");
		LForumCache.getInstance().removeCache("TopicTypesOption" + forums.getFid());
		LForumCache.getInstance().removeCache("TopicTypesLink" + forums.getFid());
		LForumCache.getInstance().removeCache("HotForumList");
		LForumCache.getInstance().removeCache("ForumHotTopicList");
		LForumCache.getInstance().removeCache("ForumNewTopicList");
		if (logger.isDebugEnabled()) {
			logger.debug("修改论坛板块 {} 成功", forums.getFid());
		}
		return setForumsModerators(forums.getFid(), forums.getForumfields().getModerators(), forums.getInheritedmod());
	}
}
