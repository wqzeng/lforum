package com.javaeye.lonlysky.lforum.service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.Element;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springside.modules.orm.hibernate.SimpleHibernateTemplate;

import com.javaeye.lonlysky.lforum.cache.LForumCache;
import com.javaeye.lonlysky.lforum.comm.utils.Utils;
import com.javaeye.lonlysky.lforum.comm.utils.XmlElementUtil;
import com.javaeye.lonlysky.lforum.config.impl.ConfigLoader;
import com.javaeye.lonlysky.lforum.entity.forum.Templates;

/**
 * 前台模板业务操作相关
 * 
 * @author 黄磊 
 *
 */
@Service
@Transactional
public class TemplateManager {

	private static final Logger logger = LoggerFactory.getLogger(TemplateManager.class);
	private static final Object synObject = new Object();
	private SimpleHibernateTemplate<Templates, Integer> templateDAO;

	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		templateDAO = new SimpleHibernateTemplate<Templates, Integer>(sessionFactory, Templates.class);
	}

	/**
	 * 获取模板文件列表
	 * @param apppath
	 * @return
	 */
	public List<String[]> getTemplateFiles(String apppath) {
		List<String[]> fileList = new ArrayList<String[]>();
		File templateFile = new File(apppath + "WEB-INF/template/");
		for (File file : templateFile.listFiles()) {
			//0-文件名,1-文件名带后缀,2-文件类型
			String[] fileStrs = new String[3];
			if (file.getName().indexOf(".ftl") != -1) {
				fileStrs[0] = file.getName().substring(0, file.getName().indexOf("."));
				fileStrs[1] = file.getName();
				fileStrs[2] = file.getName().substring(file.getName().indexOf(".") + 1);
				fileList.add(fileStrs);
			}
		}
		templateFile = new File(apppath + "WEB-INF/template/inc/commfiles/");
		for (File file : templateFile.listFiles()) {
			//0-文件名,1-文件名带后缀,2-文件类型
			String[] fileStrs = new String[3];
			if (file.getName().indexOf(".ftl") != -1) {
				fileStrs[0] = file.getName().substring(0, file.getName().indexOf("."));
				fileStrs[1] = file.getName();
				fileStrs[2] = file.getName().substring(file.getName().indexOf(".") + 1);
				fileList.add(fileStrs);
			}
		}
		return fileList;
	}

	/**
	 * 获取模板其他文件
	 * @param apppath
	 * @param templatename
	 * @return
	 */
	public List<String[]> getTemplateOtherFiles(String apppath, String templatename) {
		List<String[]> fileList = new ArrayList<String[]>();
		File templateFile = new File(apppath + "templates/" + templatename+"/");
		for (File file : templateFile.listFiles()) {
			//0-文件名,1-文件名带后缀,2-文件类型
			String[] fileStrs = new String[3];
			if (file.getName().indexOf(".") != -1) {
				fileStrs[0] = file.getName().substring(0, file.getName().indexOf("."));
				fileStrs[1] = file.getName();
				fileStrs[2] = file.getName().substring(file.getName().indexOf(".") + 1);
				if (fileStrs[2].equals("xml") || fileStrs[2].equals("css")) {
					fileList.add(fileStrs);
				}
			}
		}
		return fileList;
	}

	public static void main(String[] agos) {
		String path = "E:\\项目\\LForum项目\\lforum\\webapp\\";
		TemplateManager templateManager = new TemplateManager();
		templateManager.getTemplateFiles(path);
	}

	/**
	 * 从模板说明文件中获得模板说明信息
	 * @param xmlPath 说明文件路径
	 * @return 模板说明信息
	 */
	public TemplateAboutInfo getTemplateAboutInfo(String xmlPath) {
		TemplateAboutInfo aboutInfo = new TemplateAboutInfo();
		aboutInfo.setAuthor("");
		aboutInfo.setCopyright("");
		aboutInfo.setCreatedate("");
		aboutInfo.setName("");
		aboutInfo.setVer("");

		///存放关于信息的文件 about.xml是否存在,不存在返回空串
		if (!Utils.fileExists(xmlPath)) {
			if (logger.isDebugEnabled()) {
				logger.debug("找不到模板about.xml文件{}", xmlPath);
			}
			return aboutInfo;
		}
		Document document = XmlElementUtil.readXML(xmlPath);
		// 获取template元素
		Element template = XmlElementUtil.findElement("template", document.getRootElement());
		// 设值
		aboutInfo.setAuthor(Utils.null2String(template.attributeValue("author")));
		aboutInfo.setCopyright(Utils.null2String(template.attributeValue("copyright")));
		aboutInfo.setCreatedate(Utils.null2String(template.attributeValue("createdate")));
		aboutInfo.setName(Utils.null2String(template.attributeValue("name")));
		aboutInfo.setVer(Utils.null2String(template.attributeValue("ver")));
		aboutInfo.setForumver(Utils.null2String(template.attributeValue("fordntver")));
		return aboutInfo;
	}

	/**
	 * 获取所有模板列表
	 * 
	 * @param appPath 系统路径
	 * @return 模板列表
	 */
	public List<TemplateAboutInfo> getTemplateList(String appPath) {
		List<TemplateAboutInfo> aboutList = new ArrayList<TemplateAboutInfo>();
		File file = new File(appPath + "templates/");
		for (File root : file.listFiles()) {
			// 循环templates目录，获取模板信息
			if (Utils.fileExists(root.getPath() + "/about.xml")) {
				TemplateAboutInfo aboutInfo = getTemplateAboutInfo(root.getPath() + "/about.xml");
				aboutInfo.setDirectory(root.getName());
				aboutList.add(aboutInfo);
			}
		}
		return aboutList;
	}

	/**
	 * 获取所有模板列表
	 * 
	 * @return 模板列表
	 */
	public List<TemplateAboutInfo> getTemplateList() {
		return getTemplateList(ConfigLoader.getConfig().getWebpath());
	}

	/**
	 * 获取所有模板列表
	 * @return
	 */
	public List<Templates> getAllTemplates() {
		List<Templates> allList = new ArrayList<Templates>();
		List<TemplateAboutInfo> aboutList = getTemplateList();
		List<Templates> templateList = getValidTemplateList();
		for (TemplateAboutInfo info : aboutList) {
			Templates templates = new Templates();
			boolean isIn = false; // 是否入库
			for (Templates template : templateList) {
				if (template.getName().trim().equals(info.getName().trim())) {
					isIn = true;
					templates = template;
					break;
				} else {
					isIn = false;
				}
			}
			if (!isIn) {
				templates.setAuthor(info.getAuthor().trim());
				templates.setCopyright(info.getCopyright().trim());
				templates.setCreatedate(info.getCreatedate().trim());
				templates.setDirectory(info.getDirectory().trim());
				templates.setFordntver(info.getForumver());
				templates.setTemplateid(0);
				templates.setVer(info.getVer());
				templates.setName(info.getName().trim());
			}
			allList.add(templates);
		}
		return allList;
	}

	/**
	 * 获得前台有效的模板列表
	 * @return 模板列表
	 */
	public List<Templates> getValidTemplateList() {
		synchronized (synObject) {
			List<Templates> templateList = LForumCache.getInstance().getListCache("ValidTemplateList", Templates.class);
			if (templateList != null) {
				return templateList;
			}
			templateList = new ArrayList<Templates>();
			templateList = templateDAO.findAll();

			if (logger.isDebugEnabled()) {
				logger.debug("找到前台有效模板信息{}个", templateList.size());
			}
			LForumCache.getInstance().addCache("ValidTemplateList", templateList);
			return templateList;
		}
	}

	/**
	 * 获得前台有效的模板ID列表
	 * @return 模板ID列表
	 */
	@SuppressWarnings("unchecked")
	public String getValidTemplateIDList() {
		synchronized (synObject) {
			String templateidlist = LForumCache.getInstance().getCache("ValidTemplateIDList", String.class);
			if (templateidlist != null) {
				return templateidlist;
			}
			List<Integer> idList = templateDAO.find("select templateid from Templates Order By templateid");
			StringBuilder sb = new StringBuilder("");
			for (Integer integer : idList) {
				sb.append(",");
				sb.append(integer);
			}
			templateidlist = sb.toString();
			LForumCache.getInstance().addCache("ValidTemplateIDList", templateidlist);
			return templateidlist;
		}
	}

	/**
	 * 获得指定的模板信息
	 * @param templateid 皮肤id
	 * @return 模板信息
	 */
	public Templates getTemplateItem(int templateid) {
		if (templateid < 0) {
			return null;
		}
		Templates template = null;
		for (Templates item : getValidTemplateList()) {
			if (item.getTemplateid() == templateid) {
				template = item;
				if (logger.isDebugEnabled()) {
					logger.debug("找到ID为{}的模板信息", templateid);
				}
				break;
			}
		}
		return template;
	}

	/**
	 * 模板入库
	 * 
	 * @param template
	 */
	public void intoDB(Templates template) {
		Templates templates = new Templates();
		templates.setAuthor(template.getAuthor().trim());
		templates.setCopyright(template.getCopyright().trim());
		templates.setCreatedate(template.getCreatedate().trim());
		templates.setDirectory(template.getDirectory().trim());
		templates.setFordntver(template.getFordntver());
		templates.setVer(template.getVer());
		templates.setName(template.getName().trim());
		templateDAO.save(templates);
	}

	/**
	 * 更新论坛板块和用户个性模板设置
	 * 
	 * @param templateidlist
	 */
	public void updateForumAndUserTemplateId(String templateidlist) {
		templateDAO.createQuery("update Forums set templateid=1 where templateid in(" + templateidlist + ")")
				.executeUpdate();
		templateDAO.createQuery("update Users set templateid=1 where templateid in(" + templateidlist + ")")
				.executeUpdate();
	}

	/**
	 * 模板出库
	 * 
	 * @param templateidlist
	 */
	public void deleteTemplateItem(String templateidlist) {
		templateDAO.createQuery("delete from Templates where templateid in(" + templateidlist + ")").executeUpdate();
	}

	/**
	 * 模板基本信息<br>
	 * 每个模板目录下均可使用指定的xml文件来说明该模板的基本信息
	 * 
	 * @author 黄磊
	 *
	 */
	public class TemplateAboutInfo {
		private String name; // 模板名称
		private String author; // 模板作者
		private String createdate; // 创建时间
		private String ver; // 版本
		private String forumver; // 版本
		private String copyright; // 版权文字
		private String directory; // 目录

		public String getForumver() {
			return forumver;
		}

		public void setForumver(String forumver) {
			this.forumver = forumver;
		}

		public String getDirectory() {
			return directory;
		}

		public void setDirectory(String directory) {
			this.directory = directory;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getAuthor() {
			return author;
		}

		public void setAuthor(String author) {
			this.author = author;
		}

		public String getCreatedate() {
			return createdate;
		}

		public void setCreatedate(String createdate) {
			this.createdate = createdate;
		}

		public String getVer() {
			return ver;
		}

		public void setVer(String ver) {
			this.ver = ver;
		}

		public String getCopyright() {
			return copyright;
		}

		public void setCopyright(String copyright) {
			this.copyright = copyright;
		}
	}
}
