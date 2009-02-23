package com.javaeye.lonlysky.lforum.service;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springside.modules.orm.hibernate.SimpleHibernateTemplate;

import com.javaeye.lonlysky.lforum.entity.global.Tags;

/**
 * 标签操作类
 * 
 * @author 黄磊
 *
 */
@Service
@Transactional
public class TagManager {

	private static final Logger logger = LoggerFactory.getLogger(TagManager.class);
	private SimpleHibernateTemplate<Tags, Integer> tagDAO;

	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		tagDAO = new SimpleHibernateTemplate<Tags, Integer>(sessionFactory, Tags.class);
	}

	/**
	 * 获取标签信息(不存在返回null)
	 * @param tagid 标签id
	 * @return
	 */
	public Tags getTagInfo(int tagid) {
		if (logger.isDebugEnabled()) {
			logger.debug("获取标签信息 {}", tagid);
		}
		return tagDAO.get(tagid);
	}

	/**
	 * 写入标签缓存文件
	 * @param filename 文件绝对路径
	 * @param tags 标签集合
	 * @param jsonp_callback jsonp的回调函数名, 如不使用, 请传入""
	 * @param outputcountfield 是否输出计数统计字段
	 */
	public void writeTagsCacheFile(String filename, List<Tags> tags, String jsonp_callback, boolean outputcountfield) {
		if (tags.size() > 0) {
			StringBuilder builder = new StringBuilder();
			if (!jsonp_callback.equals("")) {
				builder.append(jsonp_callback);
				builder.append("(");
			}

			builder.append("[\r\n  ");
			for (Tags tag : tags) {
				if (outputcountfield) {
					builder.append("{{'tagid' : '" + tag.getTagid() + "', 'tagname' : '" + tag.getTagname()
							+ "', 'fcount' : '" + tag.getFcount() + "', 'pcount' : '" + tag.getPcount()
							+ "', 'scount' : '" + tag.getScount() + "', 'vcount' : '" + tag.getVcount()
							+ "', 'gcount' : '" + tag.getGcount() + "'}}, ");
				} else {
					builder.append("{{'tagid' : '" + tag.getTagid() + "', 'tagname' : '" + tag.getTagname() + "'}}, ");
				}
			}
			builder.delete(builder.length() - 2, builder.length() - 1);
			builder.append("\r\n]");
			if (!jsonp_callback.equals("")) {
				builder.append(")");
			}
			try {
				FileUtils.writeStringToFile(new File(filename), builder.toString(), "UTF-8");
				logger.info("写入标签到文件成功：" + filename);
			} catch (IOException e) {
				logger.error("写入标签到文件失败：" + e.getMessage());
			}
		}
	}
}
