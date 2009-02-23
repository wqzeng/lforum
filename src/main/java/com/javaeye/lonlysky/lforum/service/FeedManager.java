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
import com.javaeye.lonlysky.lforum.comm.utils.UBBUtils;
import com.javaeye.lonlysky.lforum.comm.utils.Utils;
import com.javaeye.lonlysky.lforum.config.impl.ConfigLoader;
import com.javaeye.lonlysky.lforum.entity.forum.Forums;
import com.javaeye.lonlysky.lforum.entity.forum.Posts;
import com.javaeye.lonlysky.lforum.entity.forum.Usergroups;

/**
 * 论坛订阅操作类
 * 
 * @author 黄磊
 *
 */
@Service
@Transactional
public class FeedManager {

	private static final Logger logger = LoggerFactory.getLogger(FeedManager.class);

	@Autowired
	private ForumManager forumManager;

	@Autowired
	private UserGroupManager userGroupManager;

	private SimpleHibernateTemplate<Posts, Integer> postDAO;

	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		postDAO = new SimpleHibernateTemplate<Posts, Integer>(sessionFactory, Posts.class);
	}

	/**
	 * 获得论坛最新的20个主题的Rss描述
	 * @param ttl TTL数值
	 * @return Rss描述
	 */
	@SuppressWarnings("unchecked")
	public String getRssXml(int ttl) {
		String str = LForumCache.getInstance().getCache("RSSIndex", String.class);
		if (str != null) {
			return str;
		}
		if (logger.isDebugEnabled()) {
			logger.debug("获得论坛最新的20个主题的Rss描述");
		}
		List<Forums> forumlist = forumManager.getForumList();
		Usergroups guestinfo = userGroupManager.getUsergroup(7);
		StringBuilder sbforumlist = new StringBuilder();//不允许游客访问的板块Id列表
		StringBuilder sbRss = new StringBuilder("<?xml version=\"1.0\" encoding=\"utf-8\" ?>\r\n");

		for (Forums f : forumlist) {
			if (f.getAllowrss() == 0) {
				sbforumlist.append("," + f.getFid());
			} else {
				if (f.getForumfields().getViewperm().trim().equals("")) {
					//板块权限设置为空，按照用户组权限走，RSS仅检查游客权限
					if (guestinfo.getAllowvisit() == 0) {
						sbforumlist.append("," + f.getFid());
					}
				} else {
					if (!Utils.inArray("7", f.getForumfields().getViewperm(), ",")) {
						sbforumlist.append("," + f.getFid());
					}
				}
			}
		}

		if (sbforumlist.length() > 0)
			sbforumlist.delete(0, 1);
		String forumurl = ConfigLoader.getConfig().getForumurl();

		sbRss.append("<?xml-stylesheet type=\"text/xsl\" href=\"rss.xsl\" media=\"screen\"?>\r\n");
		sbRss.append("<rss version=\"2.0\">\r\n");
		sbRss.append("  <channel>\r\n");
		sbRss.append("    <title>");
		sbRss.append(ConfigLoader.getConfig().getForumtitle());
		sbRss.append("</title>\r\n");
		sbRss.append("    <link>");
		sbRss.append(forumurl);
		sbRss.append("</link>\r\n");
		sbRss.append("    <description>Latest 20 threads</description>\r\n");
		sbRss.append("    <copyright>Copyright (c) ");
		sbRss.append(ConfigLoader.getConfig().getForumtitle());
		sbRss.append("</copyright>\r\n");
		sbRss.append("    <generator>");
		sbRss.append("LForum For Java");
		sbRss.append("</generator>\r\n");
		sbRss.append("    <pubDate>");
		sbRss.append(Utils.getNowTime());
		sbRss.append("</pubDate>\r\n");
		sbRss.append("    <ttl>" + ttl + "</ttl>\r\n");

		List<Object[]> objList = null;
		if (!sbforumlist.toString().equals("")) {
			objList = postDAO.createQuery(
					"select topics.tid,title,poster,postdatetime,message,forums.name from Posts where forums.fid not in("
							+ sbforumlist.toString() + ") and layer=0 order by pid desc").setMaxResults(20).list();
		} else {
			objList = postDAO
					.createQuery(
							"select topics.tid,title,poster,postdatetime,message,forums.name from Posts where layer=0 order by pid desc")
					.setMaxResults(20).list();
		}

		if (objList != null) {
			for (Object[] objects : objList) {
				sbRss.append("    <item>\r\n");
				sbRss.append("      <title>");
				sbRss.append(objects[1].toString().trim());
				sbRss.append("</title>\r\n");
				sbRss.append("    <description><![CDATA[");
				if (objects[4].toString().trim().indexOf("[hide]") > -1)
					sbRss.append("***内容隐藏***");
				else
					sbRss.append(Utils.format(UBBUtils.clearUBB(objects[4].toString().trim()), 200, true));
				sbRss.append("]]></description>\r\n");
				sbRss.append("      <link>");
				sbRss.append(forumurl);
				sbRss.append("showtopic.action?topicid=");
				sbRss.append(objects[0]);
				sbRss.append("</link>\r\n");
				sbRss.append("      <category>");
				sbRss.append(objects[5]);
				sbRss.append("</category>\r\n");
				sbRss.append("      <author>");
				sbRss.append(objects[2]);
				sbRss.append("</author>\r\n");
				sbRss.append("      <pubDate>");
				sbRss.append(objects[3]);
				sbRss.append("</pubDate>\r\n");
				sbRss.append("    </item>\r\n");
			}
		} else {
			sbRss = new StringBuilder();
			sbRss.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n");
			sbRss.append("<Rss>Error</Rss>\r\n");
			LForumCache.getInstance().addCache("RSSIndex", sbRss.toString());
			return sbRss.toString();
		}
		sbRss.append("  </channel>\r\n");
		sbRss.append("</rss>");
		LForumCache.getInstance().addCache("RSSIndex", sbRss.toString());
		return sbRss.toString();
	}

	/**
	 * 获得指定版块最新的20个主题的Rss描述
	 * @param ttl TTL数值
	 * @param fid 版块id
	 * @return Rss描述
	 */
	@SuppressWarnings("unchecked")
	public String getForumRssXml(int ttl, int fid) {

		String str = LForumCache.getInstance().getCache("RSSForum" + fid, String.class);
		if (str != null) {
			return str;
		}

		String forumurl = ConfigLoader.getConfig().getForumurl();
		Forums forum = forumManager.getForumInfo(fid);
		if (forum == null) {

			return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n<Rss>Specified forum not found</Rss>\r\n";
		}

		if (forum.getForumfields().getViewperm().equals("")) {
			//板块权限设置为空，按照用户组权限走，RSS仅检查游客权限
			Usergroups guestinfo = userGroupManager.getUsergroup(7);
			if (guestinfo.getAllowvisit() == 0) {
				return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n<Rss>Guest Denied</Rss>\r\n";
			}
		} else {
			if (!Utils.inArray("7", forum.getForumfields().getViewperm(), ",")) {
				return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n<Rss>Guest Denied</Rss>\r\n";
			}

		}
		StringBuilder sbRss = new StringBuilder("<?xml version=\"1.0\" encoding=\"utf-8\" ?>\r\n");
		sbRss.append("<?xml-stylesheet type=\"text/xsl\" href=\"rss.xsl\" media=\"screen\"?>\r\n");
		sbRss.append("<rss version=\"2.0\">\r\n");
		sbRss.append("  <channel>\r\n");
		sbRss.append("    <title>");
		sbRss.append(ConfigLoader.getConfig().getForumtitle());
		sbRss.append(" - ");
		sbRss.append(forum.getName());
		sbRss.append("</title>\r\n");
		sbRss.append("    <link>");
		sbRss.append(forumurl);
		sbRss.append("showforum.action?forumid=");
		sbRss.append(fid);
		sbRss.append("</link>\r\n");
		sbRss.append("    <description>Latest 20 threads</description>\r\n");
		sbRss.append("    <copyright>Copyright (c) ");
		sbRss.append(ConfigLoader.getConfig().getForumtitle());
		sbRss.append("</copyright>\r\n");
		sbRss.append("    <generator>");
		sbRss.append("Discuz!NT");
		sbRss.append("</generator>\r\n");
		sbRss.append("    <pubDate>");
		sbRss.append(Utils.getNowTime());
		sbRss.append("</pubDate>\r\n");
		sbRss.append("    <ttl>" + ttl + "</ttl>\r\n");

		List<Object[]> objList = postDAO
				.createQuery(
						"select tid,title,poster,postdatetime,postid.postForPid.message from Topics where forums.fid=? and postid.postForPid.layer=0 order by lastpost desc",
						fid).setMaxResults(20).list();

		if (objList != null) {
			for (Object[] objects : objList) {
				sbRss.append("    <item>\r\n");
				sbRss.append("      <title>");
				sbRss.append(objects[1].toString().trim());
				sbRss.append("</title>\r\n");
				sbRss.append("    <description><![CDATA[");
				if (objects[4].toString().trim().indexOf("[hide]") > -1)
					sbRss.append("***内容隐藏***");
				else
					sbRss.append(Utils.format(UBBUtils.clearUBB(objects[4].toString().trim()), 200, true));
				sbRss.append("]]></description>\r\n");
				sbRss.append("      <link>");
				sbRss.append(forumurl);
				sbRss.append("showtopic.action?topicid=");
				sbRss.append(objects[0]);
				sbRss.append("</link>\r\n");
				sbRss.append("      <category>");
				sbRss.append(forum.getName());
				sbRss.append("</category>\r\n");
				sbRss.append("      <author>");
				sbRss.append(objects[2]);
				sbRss.append("</author>\r\n");
				sbRss.append("      <pubDate>");
				sbRss.append(objects[3]);
				sbRss.append("</pubDate>\r\n");
				sbRss.append("    </item>\r\n");
			}
		}
		sbRss.append("  </channel>\r\n");
		sbRss.append("</rss>");
		LForumCache.getInstance().addCache("RSSForum" + fid, sbRss.toString());
		return sbRss.toString();
	}

	@SuppressWarnings("unchecked")
	public String getBaiduSitemap(int ttl) {
		String str = LForumCache.getInstance().getCache("Baidu", String.class);
		if (str != null) {
			return str;
		}
		List<Forums> forumlist = forumManager.getForumList();
		Usergroups guestinfo = userGroupManager.getUsergroup(7);
		StringBuilder sbforumlist = new StringBuilder();//不允许游客访问的板块Id列表
		StringBuilder sitemapBuilder = new StringBuilder("<?xml version=\"1.0\" encoding=\"utf-8\" ?>\r\n");

		for (Forums f : forumlist) {
			if (f.getAllowrss() == 0) {
				sbforumlist.append("," + f.getFid());
			} else {
				if (f.getForumfields().getViewperm().trim().equals("")) {
					//板块权限设置为空，按照用户组权限走，RSS仅检查游客权限
					if (guestinfo.getAllowvisit() == 0) {
						sbforumlist.append("," + f.getFid());
					}
				} else {
					if (!Utils.inArray("7", f.getForumfields().getViewperm(), ",")) {
						sbforumlist.append("," + f.getFid());
					}
				}
			}
		}

		if (sbforumlist.length() > 0)
			sbforumlist.delete(0, 1);
		String forumurl = ConfigLoader.getConfig().getForumurl();
		sitemapBuilder.append("<document xmlns:bbs=\"http://www.baidu.com/search/bbs_sitemap.xsd\">\r\n");
		sitemapBuilder.append("  <webSite>");
		sitemapBuilder.append(forumurl);
		sitemapBuilder.append("</webSite>\r\n");
		sitemapBuilder.append("  <webMaster>");
		sitemapBuilder.append("huangking124@163.com");
		sitemapBuilder.append("</webMaster>\r\n");
		sitemapBuilder.append("  <updatePeri>");
		sitemapBuilder.append(ConfigLoader.getConfig().getSitemapttl());
		sitemapBuilder.append("</updatePeri>\r\n");
		sitemapBuilder.append("  <updatetime>");
		sitemapBuilder.append(Utils.getNowTime());
		sitemapBuilder.append("</updatetime>\r\n");
		sitemapBuilder.append("  <version>");
		sitemapBuilder.append("LForum For Java V1.0");
		sitemapBuilder.append("</version>\r\n");

		List<Object[]> objList = null;
		if (!sbforumlist.toString().equals("")) {
			objList = postDAO.createQuery(
					"select tid,forums.fid,title,poster,postdatetime,lastpost,replies,views,digest from Topics where forums.fid not in("
							+ sbforumlist.toString() + ") order by tid desc").setMaxResults(20).list();
		} else {
			objList = postDAO
					.createQuery(
							"select tid,forums.fid,title,poster,postdatetime,lastpost,replies,views,digest from Topics order by tid desc")
					.setMaxResults(20).list();
		}

		if (objList != null) {
			for (Object[] objects : objList) {
				sitemapBuilder.append("    <item>\r\n");
				sitemapBuilder.append("      <link>");
				sitemapBuilder.append(forumurl);
				sitemapBuilder.append("showtopic.action?topicid=");
				sitemapBuilder.append(objects[0]);
				sitemapBuilder.append("</link>\r\n");
				sitemapBuilder.append("      <title>");
				sitemapBuilder.append(objects[2]);
				sitemapBuilder.append("</title>\r\n");
				sitemapBuilder.append("      <pubDate>");
				sitemapBuilder.append(objects[4]);
				sitemapBuilder.append("</pubDate>\r\n");
				sitemapBuilder.append("      <bbs:lastDate>");
				sitemapBuilder.append(objects[5]);
				sitemapBuilder.append("</bbs:lastDate>\r\n");
				sitemapBuilder.append("      <bbs:reply>");
				sitemapBuilder.append(objects[6]);
				sitemapBuilder.append("</bbs:reply>\r\n");
				sitemapBuilder.append("      <bbs:hit>");
				sitemapBuilder.append(objects[7]);
				sitemapBuilder.append("</bbs:hit>\r\n");
				sitemapBuilder.append("      <bbs:boardid>");
				sitemapBuilder.append(objects[1]);
				sitemapBuilder.append("</bbs:boardid>\r\n");
				sitemapBuilder.append("      <bbs:pick>");
				sitemapBuilder.append(objects[8]);
				sitemapBuilder.append("</bbs:pick>\r\n");
				sitemapBuilder.append("    </item>\r\n");
			}
		} else {
			sitemapBuilder = new StringBuilder();
			sitemapBuilder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n");
			sitemapBuilder.append("<document>Error</document>\r\n");

			LForumCache.getInstance().addCache("Baidu", sitemapBuilder.toString());
			return sitemapBuilder.toString();
		}

		sitemapBuilder.append("</document>");

		LForumCache.getInstance().addCache("Baidu", sitemapBuilder.toString());
		return sitemapBuilder.toString();
	}
}
