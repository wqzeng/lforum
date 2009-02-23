package com.javaeye.lonlysky.lforum.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springside.modules.test.SpringTransactionalTestCase;

import com.javaeye.lonlysky.lforum.entity.forum.Online;

/**
 * 在线用户业务测试用例
 * 
 * @author 黄磊
 *
 */
public class OnlineUserManagerTest extends SpringTransactionalTestCase {

	@Autowired
	private OnlineUserManager onlineManager;

	public void testOnline() {
		// 测试创建一个游客用户
		Online online = onlineManager.createGuestUser(10);
		flush();
		assertNotNull(online);
		
	}

	public void testOnlineImg() {
		String img = onlineManager.getGourpImg(7);
		System.out.println(img);
		Online online = onlineManager.getOnlineUserByIp(2, "1");
		assertNull(online);
	}

}
