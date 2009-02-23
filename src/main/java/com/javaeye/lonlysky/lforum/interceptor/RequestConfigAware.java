package com.javaeye.lonlysky.lforum.interceptor;

import com.javaeye.lonlysky.lforum.RequestConfig;

public interface RequestConfigAware {

	/**
	 * 设置RequestConfig
	 * 
	 * @param reqcfg RequestConfig
	 */
	public void setRequestConfig(RequestConfig reqcfg);
}
