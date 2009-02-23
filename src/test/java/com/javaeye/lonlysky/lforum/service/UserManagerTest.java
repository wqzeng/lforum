package com.javaeye.lonlysky.lforum.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springside.modules.test.SpringTransactionalTestCase;

import com.javaeye.lonlysky.lforum.comm.MD5;
import com.javaeye.lonlysky.lforum.comm.utils.Utils;
import com.javaeye.lonlysky.lforum.entity.forum.Admingroups;
import com.javaeye.lonlysky.lforum.entity.forum.Userfields;
import com.javaeye.lonlysky.lforum.entity.forum.Usergroups;
import com.javaeye.lonlysky.lforum.entity.forum.Users;

/**
 * 用户业务操作集成测试用例
 * 
 * @author 黄磊 
 *
 */
public class UserManagerTest extends SpringTransactionalTestCase {

	@Autowired
	private UserManager userManager;
	
	/**
	 * 测试所有的exists方法
	 */
	public void testExists() {
		// 测试指定ID是否存在
		assertEquals(true, userManager.exists(1));
		
		// 测试指定用户名是否存在
		assertEquals(true, userManager.exists("Admin"));
		
		// 检测指定IP是否注册
		assertEquals(true, userManager.existsByIp("127.0.0.1"));		
	}
	
	/**
	 * 测试所有的check方法
	 */
	public void testCheck() {
		// 检测用户密码
		assertEquals(1, userManager.checkPassword("Admin", "huangking", true));
		
		// 检测密码+回答
		assertEquals(-1, userManager.checkPasswordAndSecques("Admin", "huangking", true, 1, "test"));
		
		// 检测邮箱+回答
		assertEquals(-1, userManager.checkEmailAndSecques("Admin", "huangking124", 1, "test"));
		
		// 检测密码
		assertEquals(1, userManager.checkPassword(1, "huangking", true));		
	}
	
	/**
	 * 测试所有的查询方法
	 */
	public void testFind() {
		assertNotNull(userManager.getUserByIp("127.0.0.1"));		
		assertEquals("", userManager.getUserJoinDate(3));
		assertEquals("Admin", userManager.getUserName(1));
		assertEquals(1, userManager.getUserCount());
		assertEquals(1, userManager.getUserCountByAdmin());
		assertEquals(0.0f, userManager.getUserExtCredits(1, 1));
		assertEquals(1, userManager.getUserId("Admin"));
		assertNotNull(userManager.getUserInfo(1));
		assertEquals(0, userManager.getUserNewPmCount(1));
		assertEquals(1, userManager.findUserByEmail("huangking124@163.com"));
	}
	
	/**
	 * 测试添加方法
	 */
	public void testAdd() {
		Users user = new Users();
		user.setAvatarshowid(0);
		user.setAccessmasks(1);
		user.setBday("");
		user.setCredits(0);
		user.setDigestposts(0);
		user.setEmail("test@tst.com");
		user.setExtcredits1(0.0);
		user.setExtcredits2(0.0);
		user.setExtcredits3(0.0);
		user.setExtcredits4(0.0);
		user.setExtcredits5(0.0);
		user.setExtcredits6(0.0);
		user.setExtcredits7(0.0);
		user.setExtcredits8(0.0);
		user.setExtgroupids("");
		user.setGender(1);
		user.setGroupexpiry(1);
		user.setInvisible(1);
		user.setJoindate(Utils.getNowTime());
		user.setLastactivity("1999-01-01");
		user.setLastip("0.0.0.0");
		user.setLastpost("");
		user.setLastposttitle("");
		user.setLastvisit("1999-01-01");
		user.setNewpm(0);
		user.setNewpmcount(0);
		user.setNewsletter(0);
		user.setNickname("huangking");
		user.setOltime(0);
		user.setOnlinestate(0);
		user.setPageviews(0);
		user.setPassword(MD5.encode("huangking"));
		user.setPmsound(1);
		user.setPosts(0);
		user.setPpp(10);
		user.setRegip("127.0.0.1");
		user.setSecques("");
		user.setSigstatus(0);
		user.setShowemail(1);
		user.setSpaceid(1);
		user.setTemplateid(1);
		user.setTpp(20);
		user.setUsername("huangking");
		
		Userfields userfields = new Userfields();
		userfields.setAuthflag(1);
		userfields.setAuthstr("");
		userfields.setAuthtime(Utils.getNowTime());
		userfields.setAvatar("");
		userfields.setAvatarheight(60);
		userfields.setAvatarwidth(60);
		userfields.setBio("");
		userfields.setCustomstatus("");
		userfields.setIcq("");
		userfields.setIdcard("");
		userfields.setLocation("");
		userfields.setMedals("");
		userfields.setMobile("");
		userfields.setMsn("");
		userfields.setPhone("");
		userfields.setQq("");
		userfields.setRealname("");
		userfields.setSightml("");
		userfields.setSignature("");
		userfields.setSkype("");
		userfields.setWebsite("");
		userfields.setYahoo("");
		userfields.setUsers(user);
		
		Usergroups usergroups = new Usergroups();
		usergroups.setGroupid(1);
		Admingroups admingroups = new Admingroups();
		admingroups.setAdmingid(1);
		
		user.setUserfields(userfields);
		user.setUsergroups(usergroups);
		user.setAdmingroups(admingroups);
		
		userManager.createUser(user);
		flush();
	}
	
	/**
	 * 测试所有Update方法
	 */
	public void testUpdate() {
		userManager.updateAuthStr(1);
		flush();
		
		userManager.updateMedals(1, "123");
		flush();
		
		userManager.updateUserExtCredits(1, 2, 2.0f);
		flush();
		
		Users user = userManager.getUserInfo(1);
		user.setLastvisit(Utils.getNowTime());
		assertEquals(true, userManager.updateUserInfo(user));
		flush();
		
		userManager.updateUserLastvisit(1, "127.0.0.1");
		flush();
		
		userManager.updateUserOnlineState(1, 0, Utils.getNowTime());
		flush();
		
		assertEquals(true, userManager.updateUserPassword(1, "123"));
		flush();
		
		assertEquals(true, userManager.updateUserSecques(1, 1, "test"));
		flush();
	}
	
	
	
}
