package com.javaeye.lonlysky.lforum;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.springside.modules.web.struts2.SimpleActionSupport;

import com.javaeye.lonlysky.lforum.entity.global.Config;
import com.javaeye.lonlysky.lforum.interceptor.ConfigAware;
import com.javaeye.lonlysky.lforum.interceptor.RequestConfigAware;

/**
 * <b>客户端请求处理基类</b><br>
 * 本类继承SS3中的基础类,进一步进行扩展<br>
 * 本项目中所有Action均继承自本类
 * 
 * @author 黄磊
 * 
 */
public class BaseAction extends SimpleActionSupport implements ServletRequestAware, ServletResponseAware, ConfigAware,
		RequestConfigAware {

	private static final long serialVersionUID = 195147227343732105L;

	/**
	 * 全局配置
	 */
	protected Config config;

	/**
	 * 当前Request
	 */
	protected HttpServletRequest request;

	/**
	 * 当前Response
	 */
	protected HttpServletResponse response;

	/**
	 * 请求信息封装
	 */
	protected RequestConfig reqcfg;

	public RequestConfig getReqcfg() {
		return reqcfg;
	}

	/**
	 * 显示提示信息
	 *  
	 * @param hint 提示信息
	 * @param type 类型 1 - 关闭论坛
	 */
	protected String showMessage(String hint, int type) {
		String msg = (type == 1) ? config.getClosedreason() : hint;
		reqcfg.addMsgLine(msg);
		return GlobalsKeys.WEB_MESSAGE;
	}

	/*
	 * (non-Javadoc)
	 * @see org.apache.struts2.interceptor.ServletRequestAware#setServletRequest(javax.servlet.http.HttpServletRequest)
	 */
	public void setServletRequest(HttpServletRequest request) {
		this.request = request;

	}

	/*
	 * (non-Javadoc)
	 * @see org.apache.struts2.interceptor.ServletResponseAware#setServletResponse(javax.servlet.http.HttpServletResponse)
	 */
	public void setServletResponse(HttpServletResponse response) {
		this.response = response;

	}

	/*
	 * (non-Javadoc)
	 * @see com.javaeye.lonlysky.lforum.interceptor.ConfigAware#setConfig(com.javaeye.lonlysky.lforum.config.Config)
	 */
	public void setConfig(Config config) {
		this.config = config;
	}

	/*
	 * (non-Javadoc)
	 * @see com.javaeye.lonlysky.lforum.interceptor.RequestConfigAware#setRequestConfig(com.javaeye.lonlysky.lforum.RequestConfig)
	 */
	public void setRequestConfig(RequestConfig reqcfg) {
		this.reqcfg = reqcfg;

	}

}
