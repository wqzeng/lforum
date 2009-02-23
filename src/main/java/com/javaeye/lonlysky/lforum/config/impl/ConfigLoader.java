package com.javaeye.lonlysky.lforum.config.impl;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.javaeye.lonlysky.lforum.GlobalsKeys;
import com.javaeye.lonlysky.lforum.config.IConfigFactory;
import com.javaeye.lonlysky.lforum.entity.global.Config;
import com.javaeye.lonlysky.lforum.exception.WebException;

/**
 * 系统配置加载
 * 
 * @author 黄磊
 *
 */
public class ConfigLoader implements IConfigFactory {

	private static final Logger logger = LoggerFactory.getLogger(ConfigLoader.class);
	private String appPath; // 系统路径

	/**
	 * 默认构造方法
	 * @param appPath 系统路径
	 */
	public ConfigLoader(String path) {
		appPath = path;

	}

	/*
	 * (non-Javadoc)
	 * @see com.javaeye.lonlysky.lforum.config.IConfigFactory#initConfig()
	 */
	public Config initConfig() {
		File file = new File(appPath + GlobalsKeys.CONFIG_PATH);
		if (file.exists()) {
			if (logger.isDebugEnabled()) {
				logger.debug("当前配置文件路径为：" + file.getPath());
			}
			try {
				XMLDecoder decoder = new XMLDecoder(new FileInputStream(file));
				Config config = (Config) decoder.readObject();
				config.setWebpath(appPath); //设置当前系统路径
				return config;
			} catch (FileNotFoundException e) {
				throw new WebException("找不到配置文件：" + file.getPath());
			} catch (Exception e) {
				logger.error("加载配置信息异常：" + file.getPath(), e);
				throw new WebException("加载配置信息失败：" + e.getMessage());
			}

		} else {
			logger.error("找不到系统配置文件：" + file.getPath());
			throw new WebException("找不到系统配置文件：" + file.getPath());
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.javaeye.lonlysky.lforum.config.IConfigFactory#saveConfig(com.javaeye.lonlysky.lforum.config.Config)
	 */
	public void saveConfig(Config config) {
		File file = new File(appPath + GlobalsKeys.CONFIG_PATH);
		try {
			XMLEncoder encoder = new XMLEncoder(new FileOutputStream(file));
			encoder.writeObject(config);
			encoder.close();
			ServletActionContext.getServletContext().removeAttribute(GlobalsKeys.WEB_CONFIG);
			ServletActionContext.getServletContext().setAttribute(GlobalsKeys.WEB_CONFIG, config);
			if (logger.isDebugEnabled()) {
				logger.debug("保存系统配置文件成功");
			}
		} catch (Exception e) {
			logger.error("保存配置文件异常：" + file.getPath());
			throw new WebException("保存配置文件失败：" + e.getMessage());
		}
	}

	public static void main(String str[]) throws Exception {
		String path = "E:\\项目\\LForum项目\\工程\\LForum\\webapp\\";
		ConfigLoader loader = new ConfigLoader(path);
		Config config = loader.initConfig();
		System.out.println(config.getInstall());
		config.setInstall(1);
		loader.saveConfig(config);
		System.out.println(config.getInstall());
	}

	/**
	 * 获取配置对象
	 */
	public static Config getConfig() {
		return (Config) ServletActionContext.getServletContext().getAttribute(GlobalsKeys.WEB_CONFIG);
	}
}
