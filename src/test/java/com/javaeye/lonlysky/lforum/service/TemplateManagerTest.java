package com.javaeye.lonlysky.lforum.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springside.modules.test.SpringTransactionalTestCase;

import com.javaeye.lonlysky.lforum.service.TemplateManager.TemplateAboutInfo;

/**
 * 模板相关业务集成测试
 * 
 * @author 黄磊
 *
 */
public class TemplateManagerTest extends SpringTransactionalTestCase {

	@Autowired
	private TemplateManager templateManager;

	public void test() {
		String xmlpath = "E:\\项目\\LForum项目\\工程\\LForum\\webapp\\templates\\default\\about.xml";
		TemplateAboutInfo aboutInfo = templateManager.getTemplateAboutInfo(xmlpath);
		assertEquals("默认风格", aboutInfo.getName());
		
		assertEquals(1, templateManager.getValidTemplateList().size());
		
		assertNotNull(templateManager.getTemplateItem(1));
	}
	
	public void testList() {
		String path = "E:\\项目\\LForum项目\\lforum\\webapp\\";
		List<TemplateAboutInfo> abList = templateManager.getTemplateList(path);
		for (TemplateAboutInfo templateAboutInfo : abList) {
			System.out.println(templateAboutInfo.getName());
		}
	}

}
