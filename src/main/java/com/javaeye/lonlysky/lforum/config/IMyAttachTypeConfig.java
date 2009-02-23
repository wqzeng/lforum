package com.javaeye.lonlysky.lforum.config;

import com.javaeye.lonlysky.lforum.entity.global.MyAttachTypeConfig;


/**
 * 我的附件类型配置接口
 * 
 * @author 黄磊
 *
 */
public interface IMyAttachTypeConfig {
	
	/**
	 * 初始化配置信息
	 * 
	 * @return 配置
	 */
	MyAttachTypeConfig initConfig();

	/**
	 * 保存配置信息
	 * 
	 * @param config 配置对象
	 */
	void saveConfig(MyAttachTypeConfig config);
}
