package com.javaeye.lonlysky.lforum.service;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.io.FileUtils;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springside.modules.orm.hibernate.Page;
import org.springside.modules.orm.hibernate.SimpleHibernateTemplate;

import com.javaeye.lonlysky.lforum.cache.LForumCache;
import com.javaeye.lonlysky.lforum.comm.LForumRequest;
import com.javaeye.lonlysky.lforum.comm.utils.Utils;
import com.javaeye.lonlysky.lforum.config.impl.ConfigLoader;
import com.javaeye.lonlysky.lforum.config.impl.MyAttachTypeConfigLoader;
import com.javaeye.lonlysky.lforum.entity.forum.AttachmentInfo;
import com.javaeye.lonlysky.lforum.entity.forum.Attachments;
import com.javaeye.lonlysky.lforum.entity.forum.Attachtypes;
import com.javaeye.lonlysky.lforum.entity.forum.Myattachments;
import com.javaeye.lonlysky.lforum.entity.forum.Postid;
import com.javaeye.lonlysky.lforum.entity.forum.Topics;
import com.javaeye.lonlysky.lforum.entity.forum.Users;
import com.javaeye.lonlysky.lforum.entity.global.AttachmentType;

/**
 * 附件操作类
 * 
 * @author 黄磊
 *
 */
@Service
@Transactional
public class AttachmentManager {

	private static final Logger logger = LoggerFactory.getLogger(AttachmentManager.class);

	private SimpleHibernateTemplate<Attachments, Integer> attachmentDAO;
	private SimpleHibernateTemplate<Attachtypes, Integer> attachtypeDAO;
	private SimpleHibernateTemplate<Myattachments, Integer> myattachDAO;

	@Autowired
	private PostManager postManager;

	@Autowired
	private TopicManager topicManager;

	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		attachmentDAO = new SimpleHibernateTemplate<Attachments, Integer>(sessionFactory, Attachments.class);
		attachtypeDAO = new SimpleHibernateTemplate<Attachtypes, Integer>(sessionFactory, Attachtypes.class);
		myattachDAO = new SimpleHibernateTemplate<Myattachments, Integer>(sessionFactory, Myattachments.class);
	}

	/**
	 * 将系统设置的附件类型存入缓存
	 * 
	 * @return 系统设置的附件类型
	 */
	public List<Attachtypes> getAttachmentType() {
		List<Attachtypes> attactypeList = LForumCache.getInstance().getListCache("AttachmentType", Attachtypes.class);
		if (attactypeList != null) {
			return attactypeList;
		}
		attactypeList = attachtypeDAO.findAll();
		if (logger.isDebugEnabled()) {
			logger.debug("获取所有的附件类型");
		}
		LForumCache.getInstance().addCache("AttachmentType", attactypeList);
		return attactypeList;
	}

	public List<AttachmentType> attachTypeList() {
		List<AttachmentType> typeList = new ArrayList<AttachmentType>();
		for (AttachmentType attachmentType : MyAttachTypeConfigLoader.getInstance().initConfig().getAttachmentTypes()) {
			typeList.add(attachmentType);
		}
		return typeList;
	}

	/**
	 * 获得系统允许的附件类型和大小, 格式为: 扩展名,最大允许大小<br>
	 * 扩展名,最大允许大小.......
	 * 
	 * @param filterexpression 
	 * @return
	 */
	public String getAttachmentTypeArray(String filterexpression) {
		List<Attachtypes> attachtypeList = getAttachmentType();
		StringBuilder sb = new StringBuilder();
		for (Attachtypes attachtypes : attachtypeList) {
			sb.append(attachtypes.getExtension());
			sb.append(",");
			sb.append(attachtypes.getMaxsize());
			sb.append("\r\n");
		}
		return sb.toString().trim();
	}

	/**
	 * 获得当前设置允许的附件类型
	 * 
	 * @param filterexpression
	 * @return 逗号间隔的附件类型字符串
	 */
	public String getAttachmentTypeString(String filterexpression) {
		List<Attachtypes> attachtypeList = getAttachmentType();
		StringBuilder sb = new StringBuilder();
		for (Attachtypes attachtypes : attachtypeList) {
			if (!sb.toString().equals("")) {
				sb.append(",");
			}
			sb.append(attachtypes.getExtension());
		}
		return sb.toString().trim();
	}

	/**
	 * 获得上传附件文件的大小
	 * 
	 * @param uid 用户id
	 * @return 已上传的附件大小
	 */
	public int getUploadFileSizeByuserid(int uid) {
		int filesize = Utils.null2Int(attachmentDAO.findUnique(
				"select sum(filesize) from Attachments where users.uid=? and day(postdatetime)-day(?)=0", uid, Utils
						.getNowTime()), 0);
		if (logger.isDebugEnabled()) {
			logger.debug("获取用户{}今日上传附件大小{}", uid, filesize);
		}
		return filesize;
	}

	/**
	 * 绑定附件数组中的参数，返回无效附件个数
	 * @param attachmentinfo 附件类型
	 * @param postid 帖子id
	 * @param msg 原有提示信息
	 * @param topic 主题
	 * @param user 用户
	 * @return 无效附件个数
	 */
	public int bindAttachment(AttachmentInfo[] attachmentinfo, Postid postid, StringBuilder msg, Topics topic,
			Users user) {
		int acount = attachmentinfo.length;
		// 附件查看权限
		String[] readperm = LForumRequest.getParamValues("readperm");
		String[] attachdesc = LForumRequest.getParamValues("attachdesc");
		String[] localid = LForumRequest.getParamValues("localid");
		//设置无效附件计数器，将在下面方法中减去该计数器的值
		int errorAttachment = 0;
		int i_readperm = 0;
		for (int i = 0; i < acount; i++) {
			if (attachmentinfo[i] != null) {
				if (Utils.isInt(localid[i + 1])) {
					attachmentinfo[i].setSys_index(Utils.null2Int(localid[i + 1]));
				}
				attachmentinfo[i].setUsers(user);
				attachmentinfo[i].setTopics(topic);
				attachmentinfo[i].setPostid(postid);
				attachmentinfo[i].setPostdatetime(Utils.getNowTime());
				attachmentinfo[i].setReadperm(0);
				if (readperm != null) {
					i_readperm = Utils.null2Int(readperm[i + 1], 0);
					//当为最大阅读仅限(255)时
					i_readperm = i_readperm > 255 ? 255 : i_readperm;
					attachmentinfo[i].setReadperm(i_readperm);
				}

				if (attachdesc != null && !attachdesc[i + 1].equals("")) {
					attachmentinfo[i].setDescription(attachdesc[i + 1]);
				}

				if (!attachmentinfo[i].getSys_noupload().equals("")) {
					msg.append("<tr><td align=\"left\">");
					msg.append(attachmentinfo[i].getAttachment());
					msg.append("</td>");
					msg.append("<td align=\"left\">");
					msg.append(attachmentinfo[i].getSys_noupload());
					msg.append("</td></tr>");
					errorAttachment++;
				}
			}
		}
		return errorAttachment;
	}

	/**
	 * 产生附件
	 * @param attachmentinfo 附件描述类数组
	 * @return 附件id数组
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 */
	public int[] createAttachments(AttachmentInfo[] attachmentinfo) throws IllegalAccessException,
			InvocationTargetException {
		int acount = attachmentinfo.length;
		int icount = 0;
		int tid = 0;
		int pid = 0;
		int[] aid = new int[acount];
		int attType = 1;//普通附件,2为图片附件

		for (int i = 0; i < acount; i++) {
			if (attachmentinfo[i] != null && attachmentinfo[i].getSys_noupload().equals("")) {
				Attachments attachments = new Attachments();
				BeanUtils.copyProperties(attachments, attachmentinfo[i]);
				attachments.setAid(null);
				attachmentDAO.save(attachments);
				aid[i] = attachments.getAid();
				attachmentinfo[i].setAid(aid[i]);
				if (logger.isDebugEnabled()) {
					logger.debug("保存附件{}到数据库成功", aid[i]);
				}
				postManager.updateAttachment(attachments.getPostid().getPid(), 1);
				Myattachments myattachments = new Myattachments();
				myattachments.setAttachment(attachments.getAttachment());
				myattachments.setAttachments(attachments);
				myattachments.setDescription(attachments.getDescription());
				myattachments.setDownloads(attachments.getDownloads());
				myattachments.setExtname(attachments.getFilename().substring(
						attachments.getFilename().lastIndexOf(".") + 1).toLowerCase());
				myattachments.setFilename(attachments.getFilename());
				myattachments.setPostdatetime(attachments.getPostdatetime());
				myattachments.setPostid(attachments.getPostid());
				myattachments.setTopics(attachments.getTopics());
				myattachments.setUsers(attachments.getUsers());
				myattachDAO.save(myattachments);

				icount++;
				tid = attachmentinfo[i].getTopics().getTid();
				pid = attachmentinfo[i].getPostid().getPid();
				if (attachmentinfo[i].getFiletype().toLowerCase().startsWith("image"))
					attType = 2;
			}
		}

		if (icount > 0) {
			topicManager.updateAttachment(tid, attType);
			postManager.updateAttachment(pid, attType);
		}

		return aid;
	}

	/**
	 * 过滤临时内容中的本地临时标签
	 * @param aid 附件id
	 * @param attachmentinfo 附件信息列表
	 * @param tempMessage 临时信息内容
	 * @return 过滤结果
	 */
	public String filterLocalTags(int[] aid, AttachmentInfo[] attachmentinfo, String tempMessage) {
		System.out.println(tempMessage);
		Matcher m;
		Pattern pattern;
		for (int i = 0; i < aid.length; i++) {
			System.out.println("aid  " + aid[i]);
			System.out.println("Sys_index  " + attachmentinfo[i].getSys_index());
			if (aid[i] > 0) {
				pattern = Pattern.compile("\\[localimg=(\\d{1,}),(\\d{1,})\\]" + attachmentinfo[i].getSys_index()
						+ "\\[\\/localimg\\]");
				m = pattern.matcher(tempMessage);
				while (m.find()) {
					System.out.println(m.group(0));
					tempMessage = tempMessage.replace(m.group(0), "[attachimg]" + aid[i] + "[/attachimg]");
				}
				pattern = Pattern.compile("\\[local\\]" + attachmentinfo[i].getSys_index() + "\\[\\/local\\]");
				m = pattern.matcher(tempMessage);
				while (m.find()) {
					System.out.println(m.group(0));
					tempMessage = tempMessage.replace(m.group(0), "[attach]" + aid[i] + "[/attach]");
				}
			}
		}
		System.out.println(tempMessage);
		tempMessage = tempMessage.replaceAll("\\[localimg=(\\d{1,}),\\s*(\\d{1,})\\][\\s\\S]+?\\[/localimg\\]", "");
		tempMessage = tempMessage.replaceAll("\\[local\\][\\s\\S]+?\\[/local\\]", "");
		System.out.println(tempMessage);
		return tempMessage;
	}

	/**
	 * 获得指定帖子的附件
	 * @param pid 帖子ID
	 * @return 帖子信息
	 */
	public List<Attachments> getAttachmentListByPid(int pid) {
		return attachmentDAO.findByProperty("postid.pid", pid);
	}

	/**
	 * 获得指定帖子的附件
	 * @param pid 帖子ID
	 * @return 帖子信息
	 */
	@SuppressWarnings("unchecked")
	public List<Attachments> getAttachmentListByPid(String pidlist) {
		return attachmentDAO.find("from Attachments where postid.pid in (" + pidlist + ")");
	}

	/**
	 * 获得指定附件的描述信息
	 * @param aid 附件id
	 * @return 描述信息
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public AttachmentInfo getAttachmentInfo(int aid) throws IllegalAccessException, InvocationTargetException {
		Attachments attachments = attachmentDAO.get(aid);
		if (attachments == null) {
			return null;
		}
		return loadSingleAttachmentInfo(attachments, true);
	}

	/**
	 * 将单个附件信息转换为AttachmentInfo类
	 * @param attachments 单个附件
	 * @param isOriginalFilename 是否返回原始路径
	 * @return AttachmentInfo类
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 */
	public AttachmentInfo loadSingleAttachmentInfo(Attachments attachments, boolean isOriginalFilename)
			throws IllegalAccessException, InvocationTargetException {
		AttachmentInfo attach = new AttachmentInfo();
		BeanUtils.copyProperties(attach, attachments);
		if (!isOriginalFilename) {
			if (attach.getFilename().trim().toLowerCase().indexOf("http") < 0) {
				attach.setFilename(ConfigLoader.getConfig().getForumurl() + "upload/"
						+ attach.getFilename().trim().replace("\\", "/"));
			} else {
				attach.setFilename(attach.getFilename().trim().replace("\\", "/"));
			}
		}
		return attach;
	}

	/**
	 * 更新附件下载次数
	 * @param aid 附件id
	 */
	public void updateAttachmentDownloads(int aid) {
		Attachments attachments = attachmentDAO.get(aid);
		if (attachments != null) {
			attachments.setDownloads(attachments.getDownloads() + 1);
		}
		attachmentDAO.save(attachments);
	}

	/**
	 * 删除指定附件id的附件同时更新主题和帖子中的附件个数
	 * @param aidlist
	 * @param pid
	 * @param tid
	 */
	public void deleteAttachment(String aidlist, int pid, int tid) {
		myattachDAO.createQuery("delete from Myattachments where attachments.aid in(" + aidlist + ")");
		if (tid > 0) {
			int attcount = getAttachmentCountByPid(pid);
			if (attcount <= 0) {
				postManager.updateAttachment(pid, 0);
			}

			attcount = getAttachmentCountByTid(tid);
			if (attcount <= 0) {
				topicManager.updateAttachment(tid, 0);
			}
		}
	}

	/**
	 * 批量删除附件
	 * @param aidList 附件Id，以英文逗号分割
	 */
	@SuppressWarnings("unchecked")
	public void deleteAttachment(String aidList) {
		List<Object[]> objList = attachmentDAO
				.find("select aid,filename,topics.tid,postid.pid from Attachments where aid in (" + aidList + ")");
		int tid = 0;
		int pid = 0;
		if (objList != null) {
			for (Object[] objects : objList) {
				if (objects[1].toString().trim().toLowerCase().indexOf("http") < 0) {
					String attachmentFilePath = ConfigLoader.getConfig().getWebpath() + "upload/"
							+ objects[1].toString();
					if (Utils.fileExists(attachmentFilePath)) {
						try {
							FileUtils.forceDelete(new File(attachmentFilePath));
						} catch (IOException e) {
						}
					}
					tid = Utils.null2Int(objects[2], 0);
					pid = Utils.null2Int(objects[3], 0);
				}
			}
		}

		attachmentDAO.createQuery("delete from Attachments where aid in (" + aidList + ")").executeUpdate();
		deleteAttachment(aidList, pid, tid);
	}

	/**
	 * 获得指定主题的附件个数
	 * @param tid 主题ID
	 * @return 附件个数
	 */
	public int getAttachmentCountByTid(int tid) {
		return Utils
				.null2Int(attachmentDAO.findUnique("select count(aid) from Attachments where topics.tid=?", tid), 0);
	}

	/**
	 * 获得指定帖子的附件个数
	 * @param pid 帖子ID
	 * @return 附件个数
	 */
	public int getAttachmentCountByPid(int pid) {
		return Utils
				.null2Int(attachmentDAO.findUnique("select count(aid) from Attachments where postid.pid=?", pid), 0);
	}

	/**
	 * 删除指定附件
	 * @param aid 附件aid
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 * @throws IOException 
	 */
	public void deleteAttachment(int aid) throws IllegalAccessException, InvocationTargetException, IOException {
		int tid = 0;
		int pid = 0;
		AttachmentInfo attachmentInfo = getAttachmentInfo(aid);
		if (attachmentInfo != null) {
			Attachments attachments = new Attachments();
			BeanUtils.copyProperties(attachments, getAttachmentInfo(aid));
			if (attachments.getFilename().trim().toLowerCase().indexOf("http") < 0) {
				// 删除硬盘上的附件
				String attachmentFilePath = ConfigLoader.getConfig().getWebpath() + "upload/"
						+ attachments.getFilename().trim();
				FileUtils.forceDelete(new File(attachmentFilePath));
			}
			tid = attachments.getTopics().getTid();
			pid = attachments.getPostid().getPid();
			attachmentDAO.delete(attachments);
		}
		deleteAttachment(aid + "", pid, tid);
	}

	/**
	 * 更新附件信息
	 * @param attachmentInfo
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public void updateAttachment(AttachmentInfo attachmentInfo) throws IllegalAccessException,
			InvocationTargetException {
		Attachments attachments = new Attachments();
		BeanUtils.copyProperties(attachments, attachmentInfo);
		attachmentDAO.save(attachments);
	}

	/**
	 * 更新附件信息
	 */
	public void updateAttachment(int aid, int readperm, String description) {
		Attachments attachments = attachmentDAO.get(aid);
		attachments.setReadperm(readperm);
		attachments.setDescription(description);
		attachmentDAO.save(attachments);
	}

	/**
	 * 删除指定主题的所有附件
	 * @param tidlist 版块tid列表
	 * @return
	 * @throws IOException 
	 */
	public int deleteAttachmentByTid(String tidlist) throws IOException {
		if (!Utils.isIntArray(tidlist.split(","))) {
			return -1;
		}

		List<Object[]> objList = getAttachmentListByTid(tidlist);

		if (objList != null) {
			for (Object[] objects : objList) {
				if (Utils.null2String(objects[1]).toLowerCase().indexOf("http") < 0) {
					// 删除附件
					String path = ConfigLoader.getConfig().getWebpath() + "upload/" + Utils.null2String(objects[1]);
					if (Utils.fileExists(path)) {
						FileUtils.forceDelete(new File(path));
					}
				}
			}

		}
		attachmentDAO.createQuery("delete from Myattachments where topics.tid in (" + tidlist + ")").executeUpdate();
		return attachmentDAO.createQuery("delete from Attachments where topics.tid in (" + tidlist + ")")
				.executeUpdate();
	}

	/**
	 * 获得指定主题的所有附件
	 * @param tidlist 主题Id列表，以英文逗号分割
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Object[]> getAttachmentListByTid(String tidlist) {
		return attachmentDAO.find("select aid,filename from Attachments where topics.tid in(" + tidlist + ")");
	}

	/**
	 * 根据帖子ID删除附件
	 * @param pid 帖子ID
	 * @throws IOException 
	 */
	public void deleteAttachmentByPid(int pid) throws IOException {
		List<Attachments> attachList = getAttachmentListByPid(pid);
		if (attachList != null) {
			for (Attachments attachments : attachList) {
				if (attachments.getFilename().trim().toLowerCase().indexOf("http") < 0) {
					FileUtils.forceDelete(new File(ConfigLoader.getConfig().getWebpath() + "upload/"
							+ attachments.getFilename()));
				}
			}
		}
		attachmentDAO.createQuery("delete from Myattachments where postid.pid=?", pid).executeUpdate();
		attachmentDAO.createQuery("delete from Attachments where postid.pid=?", pid).executeUpdate();
	}

	/**
	 * 获取指定用户id的附件数
	 * @param userid 用户id
	 * @param typeid 附件类型id
	 * @return 附件数量
	 */
	public int getUserAttachmentCount(int userid, int typeid) {
		return Utils.null2Int(attachmentDAO.findUnique("select count(id) from Myattachments where extname in("
				+ setExtNamelist(typeid) + ") and users.uid=?", userid), 0);
	}

	/**
	 * 获取指定用户id的附件数
	 * @param userid 用户id
	 * @return 附件数量
	 */
	public int getUserAttachmentCount(int userid) {
		return Utils.null2Int(
				attachmentDAO.findUnique("select count(id) from Myattachments where users.uid=?", userid), 0);
	}

	/**
	 * 按照附件分类返回用户的附件
	 * @param typeid 附件类型id
	 * @return 附件类型id
	 */
	public String setExtNamelist(int typeid) {
		String newstring = "";
		for (String s : getExtName(typeid).split(",")) {
			newstring += "'" + s + "',";
		}
		System.out.println(newstring);
		return newstring.substring(0, newstring.length() - 1);
	}

	/**
	 * 获取我的附件指定类型的扩展名
	 * @param typeid 附件类型id
	 * @return 
	 */
	public String getExtName(int typeid) {
		for (AttachmentType act : getAttach()) {
			if (act.getTypeId() == typeid) {
				return act.getExtName();
			}
		}
		return "";
	}

	/**
	 * 获取我的附件类型信息
	 * @return 我的附件类型信息
	 */
	public AttachmentType[] getAttach() {
		return MyAttachTypeConfigLoader.getInstance().initConfig().getAttachmentTypes();
	}

	/**
	 * 获取指定用户的附件
	 * @param uid
	 * @param typeid
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	public List<Myattachments> getAttachmentByUid(int uid, int typeid, int pageIndex, int pageSize) {
		List<Myattachments> myattachment = new ArrayList<Myattachments>();
		if (pageIndex <= 0) {
			return myattachment;
		} else {
			Page<Myattachments> page = new Page<Myattachments>(pageSize);
			page.setPageNo(pageIndex);
			if (typeid == 0) {
				page = myattachDAO
						.find(page, "from Myattachments where users.uid=? order by attachments.aid desc", uid);
			} else {
				page = myattachDAO.find(page, "from Myattachments where extname in(" + setExtNamelist(typeid)
						+ ") and users.uid=? order by attachments.aid desc", uid);
			}
			myattachment = page.getResult();
			for (Myattachments myattachments : myattachment) {
				myattachDAO.getSession().evict(myattachments);
				myattachments.setFilename(myattachments.getFilename().startsWith("http://") ? myattachments
						.getFilename() : "upload/" + myattachments.getFilename().replace("\\", "/"));
			}
			return myattachment;
		}
	}
}
