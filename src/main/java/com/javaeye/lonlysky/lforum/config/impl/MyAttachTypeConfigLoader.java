package com.javaeye.lonlysky.lforum.config.impl;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.javaeye.lonlysky.lforum.GlobalsKeys;
import com.javaeye.lonlysky.lforum.cache.LForumCache;
import com.javaeye.lonlysky.lforum.config.IMyAttachTypeConfig;
import com.javaeye.lonlysky.lforum.entity.global.AttachmentType;
import com.javaeye.lonlysky.lforum.entity.global.MyAttachTypeConfig;
import com.javaeye.lonlysky.lforum.exception.WebException;

/**
 * 我的附件类型配置加载
 * 
 * @author 黄磊
 *
 */
public class MyAttachTypeConfigLoader implements IMyAttachTypeConfig {

	private static final Logger logger = LoggerFactory.getLogger(MyAttachTypeConfigLoader.class);
	private String appPath; // 系统路径

	/**
	 * 默认构造方法
	 */
	public MyAttachTypeConfigLoader() {
		appPath = ConfigLoader.getConfig().getWebpath();

	}

	public MyAttachTypeConfigLoader(String path) {
		appPath = path;

	}

	public static MyAttachTypeConfigLoader getInstance() {
		return new MyAttachTypeConfigLoader();
	}

	public static MyAttachTypeConfigLoader getInstance(String path) {
		return new MyAttachTypeConfigLoader(path);
	}

	/*
	 * (non-Javadoc)
	 * @see com.javaeye.lonlysky.lforum.config.IMyAttachTypeConfig#initConfig()
	 */
	public MyAttachTypeConfig initConfig() {
		MyAttachTypeConfig config = LForumCache.getInstance().getCache("MyAttachTypeConfig", MyAttachTypeConfig.class);
		if (config == null) {
			File file = new File(appPath + GlobalsKeys.MYATTACHTYPE_CONFIG_PATH);
			if (file.exists()) {
				if (logger.isDebugEnabled()) {
					logger.debug("当前附件类型配置文件路径为：" + file.getPath());
				}
				try {
					XMLDecoder decoder = new XMLDecoder(new FileInputStream(file));
					config = (MyAttachTypeConfig) decoder.readObject();
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
			LForumCache.getInstance().addCache("MyAttachTypeConfig", config);
		}
		return config;
	}

	/*
	 * (non-Javadoc)
	 * @see com.javaeye.lonlysky.lforum.config.IMyAttachTypeConfig#saveConfig(com.javaeye.lonlysky.lforum.entity.global.MyAttachTypeConfig)
	 */
	public void saveConfig(MyAttachTypeConfig config) {
		File file = new File(appPath + GlobalsKeys.MYATTACHTYPE_CONFIG_PATH);
		try {
			XMLEncoder encoder = new XMLEncoder(new FileOutputStream(file));
			encoder.writeObject(config);
			encoder.close();
			LForumCache.getInstance().removeCache("MyAttachTypeConfig");
			if (logger.isDebugEnabled()) {
				logger.debug("保存附件类型配置文件成功");
			}
		} catch (Exception e) {
			logger.error("保存配置文件异常：" + file.getPath());
			throw new WebException("保存配置文件失败：" + e.getMessage());
		}
	}

	public static void main(String str[]) throws Exception {
		String path = "E:\\项目\\LForum项目\\工程\\LForum\\webapp\\";
		AttachmentType[] types = new AttachmentType[] { new AttachmentType("1类", 1, "jpg,gif"),
				new AttachmentType("3类", 2, "torrent"), new AttachmentType("2类", 3, "rar,jpg"),
				new AttachmentType("4类", 4, "rar,jpg") };
		MyAttachTypeConfig myAttachTypeConfig = new MyAttachTypeConfig();
		myAttachTypeConfig.setAttachmentTypes(types);
		MyAttachTypeConfigLoader.getInstance(path).saveConfig(myAttachTypeConfig);
	}
}
