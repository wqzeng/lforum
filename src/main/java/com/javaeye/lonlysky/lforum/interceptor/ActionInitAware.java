package com.javaeye.lonlysky.lforum.interceptor;

/**
 * Action接口,实现类将初始化调用initAction()方法
 * 
 * @author 黄磊
 *
 */
public interface ActionInitAware {
	
	/**
	 * 初始化Action函数
	 */
	public String initAction() throws Exception;
}
