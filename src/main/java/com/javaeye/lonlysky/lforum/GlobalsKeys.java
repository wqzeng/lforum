package com.javaeye.lonlysky.lforum;

/**
 * <b>系统全局常量</b><br>
 * 本类封装系统中常用的变量名称<br>
 * 例如：会话中保存的用户键值
 * 
 * @author 黄磊
 *
 */
public final class GlobalsKeys {

	/**
	 * 配置文件路径
	 */
	public static final String CONFIG_PATH = "/WEB-INF/config/config.xml";

	/**
	 * 我的附件配置文件路径
	 */
	public static final String MYATTACHTYPE_CONFIG_PATH = "/WEB-INF/config/myattachment.xml";

	/**
	 * 会话中的用户信息键
	 */
	public static final String SESSION_USER = "session_user";

	/**
	 * 会话中的验证码键
	 */
	public static final String SESSION_RAND = "session_rand";

	/**
	 * 程序全局配置键
	 */
	public static final String WEB_CONFIG = "config";

	/**
	 * 程序主页键
	 */
	public static final String WEB_MAIN = "main";

	/**
	 * 程序安装向导键
	 */
	public static final String WEB_INSTALL = "install";

	/**
	 * 显示系统提示信息
	 */
	public static final String WEB_MESSAGE = "message";

	/**
	 * 显示请求信息KEY
	 */
	public static final String REQUEST_CONFIG = "reqcfg";

	/**
	 * 登录页面键
	 */
	public static final String WEB_LOGIN = "login";

	/**
	 * 分页数据键
	 */
	public static final String WEB_PAGE = "page";

	/**
	 * 页面执行时间KEY
	 */
	public static final String EXECUTION_TIME = "pageTime";

	/**
	 * 数据库方言
	 */
	public static final String DATABASE_DIALECT = "database.dialect";

	/**
	 * 数据库连接字符串
	 */
	public static final String DATABASE_URL = "database.url";

	/**
	 * JDBC驱动类
	 */
	public static final String DATABASE_DRIVER = "database.driver";

	/**
	 * 系统管理员插入语句
	 */
	public static final String DATABASE_ADMIN = "database.admin";

	/**
	 * 系统管理员扩展信息语句
	 */
	public static final String DATABASE_UFIELD = "database.ufield";

	/**
	 * Action初始化
	 */
	public static final String ACTION_INIT = "action.init";

	/**
	 * 负责发送新用户注册欢迎信件的用户名称, 该名称同时不允许用户注册
	 */
	public static final String SYSTEM_USERNAME = "系统";

}
