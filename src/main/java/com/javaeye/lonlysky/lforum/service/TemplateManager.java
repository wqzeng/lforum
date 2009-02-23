package com.javaeye.lonlysky.lforum.service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
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
		File file = new File(xmlPath);
		if (!file.exists()) {
			if (logger.isDebugEnabled()) {
				logger.debug("找不到模板about.xml文件{}", xmlPath);
			}
			return aboutInfo;
		}
		SAXReader saxReader = new SAXReader();
		try {
			Document document = saxReader.read(file);
			// 获取template元素
			Element template = XmlElementUtil.findElement("template", document.getRootElement());
			// 设值
			aboutInfo.setAuthor(Utils.null2String(template.attributeValue("author")));
			aboutInfo.setCopyright(Utils.null2String(template.attributeValue("copyright")));
			aboutInfo.setCreatedate(Utils.null2String(template.attributeValue("createdate")));
			aboutInfo.setName(Utils.null2String(template.attributeValue("name")));
			aboutInfo.setVer(Utils.null2String(template.attributeValue("ver")));
		} catch (DocumentException e) {
			logger.warn("解析{}异常", xmlPath, e);
		}
		return aboutInfo;
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
		private String copyright; // 版权文字

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
