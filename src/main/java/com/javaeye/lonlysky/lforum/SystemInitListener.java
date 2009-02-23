package com.javaeye.lonlysky.lforum;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.javaeye.lonlysky.lforum.config.impl.ConfigLoader;
import com.javaeye.lonlysky.lforum.entity.global.Config;

/**
 * <b>系统初始化监听器</b><br>
 * 目的：加载全局配置文件并保存到Context范围内
 * 
 * @author 黄磊
 *
 */
public class SystemInitListener implements ServletContextListener {

	private static final Logger logger = LoggerFactory.getLogger(SystemInitListener.class);

	private Config config; // 系统配置文件

	/**
	 * 销毁函数
	 */
	public void contextDestroyed(ServletContextEvent contextEvent) {
		ServletContext context = contextEvent.getServletContext();
		if (config != null) {
			config = null;
		}
		if (context.getAttribute(GlobalsKeys.WEB_CONFIG) != null) {
			context.removeAttribute(GlobalsKeys.WEB_CONFIG);
		}
	}

	/**
	 * 初始化加载
	 */
	public void contextInitialized(ServletContextEvent contextEvent) {
		logger.info("开始加载系统配置...");
		ServletContext context = contextEvent.getServletContext();
		// 读取全局配置文件
		if (config == null) {
			String appPath = context.getRealPath("/");
			ConfigLoader loader = new ConfigLoader(appPath);
			config = loader.initConfig();
		}
		context.setAttribute(GlobalsKeys.WEB_CONFIG, config);
		logger.info("系统配置加载成功...");
	}

}
