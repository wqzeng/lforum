package com.javaeye.lonlysky.lforum.service;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springside.modules.orm.hibernate.SimpleHibernateTemplate;

import com.javaeye.lonlysky.lforum.cache.LForumCache;
import com.javaeye.lonlysky.lforum.entity.forum.Bbcodes;
import com.javaeye.lonlysky.lforum.entity.forum.CustomEditorButtonInfo;

/**
 * 编辑器操作类
 * 
 * @author 黄磊
 *
 */
@Service
@Transactional
public class EditorManager {

	private SimpleHibernateTemplate<Bbcodes, Integer> bbcodeDAO;

	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		bbcodeDAO = new SimpleHibernateTemplate<Bbcodes, Integer>(sessionFactory, Bbcodes.class);
	}

	/**
	 * 返回自定义编辑器按钮列表
	 * 
	 * @return 自定义编辑器按钮列表
	 */
	public List<Bbcodes> getCustomEditButtonList() {
		return bbcodeDAO.findByProperty("available", 1);
	}

	public List<CustomEditorButtonInfo> getCustomEditButtonListWithInfo() {
		List<CustomEditorButtonInfo> buttonArray = LForumCache.getInstance().getListCache("CustomEditButtonInfo",
				CustomEditorButtonInfo.class);
		if (buttonArray == null) {
			List<Bbcodes> bbcodeList = getCustomEditButtonList();
			buttonArray = new ArrayList<CustomEditorButtonInfo>();
			for (Bbcodes bbcodes : bbcodeList) {
				CustomEditorButtonInfo buttonInfo = new CustomEditorButtonInfo();
				buttonInfo.setId(bbcodes.getId());
				buttonInfo.setTag(bbcodes.getTag());
				buttonInfo.setIcon(bbcodes.getIcon());
				buttonInfo.setAvailable(bbcodes.getAvailable());
				buttonInfo.setExample(bbcodes.getExample());
				buttonInfo.setExplanation(bbcodes.getExplanation());
				buttonInfo.setParams(bbcodes.getParams());
				buttonInfo.setNest(bbcodes.getNest());
				buttonInfo.setParamsdefvalue(bbcodes.getParamsdefvalue());
				buttonInfo.setParamsdescript(bbcodes.getParamsdescript());
				buttonInfo.setReplacement(bbcodes.getReplacement());

				buttonArray.add(buttonInfo);
			}
			LForumCache.getInstance().addCache("CustomEditButtonInfo", buttonArray);
		}

		return buttonArray;
	}

}
