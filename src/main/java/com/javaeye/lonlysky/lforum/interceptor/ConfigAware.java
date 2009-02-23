package com.javaeye.lonlysky.lforum.interceptor;

import com.javaeye.lonlysky.lforum.entity.global.Config;

/**
 * Action如果想获得配置信息则实现本类
 * 
 * @author 黄磊
 *
 */
public interface ConfigAware {

	/**
	 * 设置配置信息
	 * 
	 * @param config 配置信息
	 */
	public void setConfig(Config config);
}
