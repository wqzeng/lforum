package com.javaeye.lonlysky.lforum.config;

import com.javaeye.lonlysky.lforum.entity.global.Config;

/**
 * 读取配置文件接口
 * 
 * @author 黄磊
 *
 */
public interface IConfigFactory {

	/**
	 * 初始化配置信息
	 * 
	 * @return 配置
	 */
	Config initConfig();

	/**
	 * 保存配置信息
	 * 
	 * @param config 配置对象
	 */
	void saveConfig(Config config);

}
