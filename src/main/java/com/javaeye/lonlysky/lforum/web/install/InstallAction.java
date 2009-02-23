package com.javaeye.lonlysky.lforum.web.install;

import java.sql.Connection;
import java.util.List;

import org.apache.struts2.config.ParentPackage;
import org.springframework.beans.factory.annotation.Required;

import com.javaeye.lonlysky.lforum.BaseAction;
import com.javaeye.lonlysky.lforum.comm.LForumRequest;
import com.javaeye.lonlysky.lforum.comm.ParseDBScriptFile;
import com.javaeye.lonlysky.lforum.exception.ServiceException;
import com.javaeye.lonlysky.lforum.interceptor.annotations.Token;
import com.javaeye.lonlysky.lforum.service.InstallManager;

/**
 * 系统在线安装向导
 * 
 * @author 黄磊
 * 
 */
@ParentPackage("default")
public class InstallAction extends BaseAction {

	private static final long serialVersionUID = -8493326133274340457L;

	private InstallManager installManager; // 系统安装向导业务实现

	private boolean createTable; // 是否创建数据表
	private String dataType; // 数据库类型
	private String dataSource; // 数据源
	private String dataPort; // 数据源端口
	private String dataBase; // 数据库名称
	private String userID; // 数据库用户名
	private String password; // 数据库密码
	private String adminName; // 管理员名称
	private String adminPassword; // 管理员密码
	private String webName; // 论坛名称
	private String webUrl; // 论坛URL	

	/**
	 * 设置连接属性
	 */
	@Token
	public String setConn() {
		return "setConn";
	}

	/**
	 * 创建数据表
	 */
	@Token
	public String createData() throws Exception {
		if (!createTable)
			// 不创建数据库表则直接跳过
			return "setConfig";
		Connection connection = null;
		try {
			// 获取数据库连接对象
			connection = installManager.getConnection(config.getWebpath(), dataType, dataSource, dataPort, dataBase,
					userID, password);
			connection.setAutoCommit(false);
			// 创建数据库表
			String sqlpath = config.getWebpath() + "WEB-INF/database/" + dataType + "/setup.sql";
			List<String> sqList = ParseDBScriptFile.parse(sqlpath);
			installManager.createTable(sqList, connection);
			// 添加初始管理员
			installManager.insertAdmin(config.getWebpath(), dataType, adminName, adminPassword, LForumRequest.getIp(),
					connection);
			connection.commit();
		} catch (ServiceException e) {
			if (connection != null) {
				connection.rollback();
			}
			getReqcfg().addErrLine(e.getMessage());
		} finally {
			if (connection != null) {
				connection.close();
			}
		}
		return getReqcfg().isErr() ? "setConn" : "dataSuccess";

	}

	/**
	 * 设置系统配置
	 */
	@Token
	public String setConfig() {
		return "setConfig";
	}

	/**
	 * 完成安装向导
	 */
	@Token
	public String succeed() {
		try {
			config.setInstall(1);
			config.setForumtitle(webName);
			config.setForumurl(webUrl);
			installManager.saveConfig(config);
		} catch (ServiceException e) {
			getReqcfg().addErrLine(e.getMessage());
		}

		return getReqcfg().isErr() ? "setConfig" : "succeed";
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public String getDataSource() {
		return dataSource;
	}

	public void setDataSource(String dataSource) {
		this.dataSource = dataSource;
	}

	public String getDataPort() {
		return dataPort;
	}

	public void setDataPort(String dataPort) {
		this.dataPort = dataPort;
	}

	public String getDataBase() {
		return dataBase;
	}

	public void setDataBase(String dataBase) {
		this.dataBase = dataBase;
	}

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getAdminName() {
		return adminName;
	}

	public void setAdminName(String adminName) {
		this.adminName = adminName;
	}

	public String getAdminPassword() {
		return adminPassword;
	}

	public void setAdminPassword(String adminPassword) {
		this.adminPassword = adminPassword;
	}

	public boolean isCreateTable() {
		return createTable;
	}

	public void setCreateTable(boolean createTable) {
		this.createTable = createTable;
	}

	public String getWebName() {
		return webName;
	}

	public void setWebName(String webName) {
		this.webName = webName;
	}

	public String getWebUrl() {
		return webUrl;
	}

	public void setWebUrl(String webUrl) {
		this.webUrl = webUrl;
	}

	@Required
	public void setInstallManager(InstallManager installManager) {
		this.installManager = installManager;
	}
}
