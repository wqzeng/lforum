package com.javaeye.lonlysky.lforum.comm;

import org.apache.struts2.ServletActionContext;

/**
 * 本类封装获取服务器信息操作
 * 
 * @author 黄磊
 *
 */
public class SysInfo {

	/**
	 * 获取服务器系统名
	 * @return
	 */
	public static String getOsName() {
		return System.getProperty("os.name");
	}

	/**
	 * 获取服务器CPU信息
	 */
	public static String getCpuInfo() {
		return System.getProperty("os.arch");
	}

	/**
	 * 获取服务器系统版本
	 */
	public static String getOsVersion() {
		return System.getProperty("os.version");
	}

	/**
	 * 获取JVM版本
	 * @return
	 */
	public static String getJVMVersion() {
		return System.getProperty("java.vm.version");
	}

	/**
	 * 获取JDK版本
	 */
	public static String getJavaVersion() {
		return System.getProperty("java.version");
	}

	/**
	 * 获取Servlet版本
	 * @return
	 */
	public static String getServletVersion() {
		return ServletActionContext.getServletContext().getMajorVersion() + "."
				+ ServletActionContext.getServletContext().getMinorVersion();
	}
}
