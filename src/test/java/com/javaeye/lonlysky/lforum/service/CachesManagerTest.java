package com.javaeye.lonlysky.lforum.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springside.modules.test.SpringTransactionalTestCase;

/**
 * CachesManager集成测试
 * 
 * @author 黄磊
 *
 */
public class CachesManagerTest extends SpringTransactionalTestCase {

	@Autowired
	private CachesManager cachesManager;
	
	public void test() {
		System.out.println(cachesManager.getCustomEditButtonList());
		System.out.println(cachesManager.getForumListMenuDiv(1, 1, ""));
		System.out.println(cachesManager.getSmiliesFirstPageCache());
		System.out.println(cachesManager.getSmiliesCache());
	}
}
