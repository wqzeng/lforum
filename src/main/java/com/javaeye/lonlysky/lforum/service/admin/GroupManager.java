package com.javaeye.lonlysky.lforum.service.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.javaeye.lonlysky.lforum.entity.forum.Usergroups;
import com.javaeye.lonlysky.lforum.service.UserGroupManager;

/**
 * 后台用户组管理操作类
 * 
 * @author 黄磊
 *
 */
@Service
@Transactional
public class GroupManager {

	//private static final Logger logger = LoggerFactory.getLogger(AdminGroupManager.class);

	@Autowired
	private UserGroupManager userGroupManager;

	/**
	 * 通过指定的用户组id得到相关的用户组信息
	 * @param groupid
	 * @return
	 */
	public Usergroups adminGetUserGroupInfo(int groupid) {		
		return userGroupManager.getUsergroup(groupid);
	}
}
