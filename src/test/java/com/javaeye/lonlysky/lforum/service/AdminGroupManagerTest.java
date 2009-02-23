package com.javaeye.lonlysky.lforum.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springside.modules.test.SpringTransactionalTestCase;

import com.javaeye.lonlysky.lforum.entity.forum.Admingroups;

/**
 * 管理组业务操作集成测试
 * 
 * @author 黄磊
 *
 */
public class AdminGroupManagerTest extends SpringTransactionalTestCase {

	@Autowired
	private AdminGroupManager adminGroupManager;
	
	public void test() {
		assertEquals(3, adminGroupManager.getAdminGroupList().size());
		
		Admingroups admingroup = adminGroupManager.getAdminGroup(1);
		assertNotNull(admingroup);
		
		admingroup.setAllowbanip(1);
		adminGroupManager.updateAdminGroup(admingroup);
		flush();
		
		Admingroups test = new Admingroups();
		int a = 1;
		test.setAllowbanip(a);
		test.setAllowbanuser(a);
		test.setAllowcensorword(a);
		test.setAllowdelpost(a);
		test.setAlloweditpoll(a);
		test.setAlloweditpost(a);
		test.setAllowedituser(a);
		test.setAllowmassprune(a);
		test.setAllowmodpost(a);
		test.setAllowmoduser(a);
		test.setAllowpostannounce(a);
		test.setAllowrefund(a);
		test.setAllowstickthread(a);
		test.setAllowviewip(a);
		test.setAllowviewlog(a);
		test.setAllowviewrealname(a);
		test.setDisablepostctrl(a);
		
		adminGroupManager.createAdminGroup(test);
		flush();
		
		adminGroupManager.deleteAdminGroup(test.getAdmingid());
		flush();
				
	}

}
