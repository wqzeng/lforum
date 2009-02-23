package com.javaeye.lonlysky.lforum.service;

import java.sql.Connection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springside.modules.test.SpringTestCase;

import com.javaeye.lonlysky.lforum.comm.ParseDBScriptFile;
import com.javaeye.lonlysky.lforum.config.impl.ConfigLoader;
import com.javaeye.lonlysky.lforum.entity.global.Config;

/**
 * InstallManager集成测试用例
 * 
 * @author 黄磊
 * 
 */
public class InstallManagerTest extends SpringTestCase {

	@Autowired
	private InstallManager installManager;

	private String appPath = "E:\\项目\\LForum项目\\工程\\LForum\\webapp\\", dataType = "sqlserver05",
			dataSource = "localhost", dataPort = "1433", dataBase = "demo", db_user = "sa", db_pwd = "sqlpass";

	// 测试连接是否正常
	public void testConnection() {
		ConfigLoader loader = new ConfigLoader(appPath);
		Config config = loader.initConfig();
		Connection connection = installManager.getConnection(config.getWebpath(), dataType, dataSource, dataPort,
				dataBase, db_user, db_pwd);
		assertNotNull(connection);
	}

	// 测试生成数据表
	public void testCreateTable() throws Exception {
		ConfigLoader loader = new ConfigLoader(appPath);
		Config config = loader.initConfig();
		Connection connection = installManager.getConnection(config.getWebpath(), dataType, dataSource, dataPort,
				dataBase, db_user, db_pwd);
		connection.setAutoCommit(false);
		String sqlpath = config.getWebpath() + "/WEB-INF/database/" + dataType + "/setup.sql";

		List<String> sqList = ParseDBScriptFile.parse(sqlpath);
		installManager.createTable(sqList, connection);
		connection.rollback();

	}
}
