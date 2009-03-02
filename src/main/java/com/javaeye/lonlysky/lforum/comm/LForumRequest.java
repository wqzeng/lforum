package com.javaeye.lonlysky.lforum.comm;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;

import com.javaeye.lonlysky.lforum.comm.utils.Utils;

/**
 * 本类用于封装Reuqest操作
 * 
 * @author 黄磊
 *
 */
public class LForumRequest {

	private LForumRequest() {
	}

	/**
	 * 浏览器可接受的字符集
	 */
	public static final String ACCEPT_CHARSET = "Accept-Charset";

	/**
	 * 浏览器所希望的语言种类
	 */
	public static final String ACCEPT_LANGUAGE = "Accept-Language";

	/**
	 * 浏览器能够进行解码的数据编码方式
	 */
	public static final String ACCEPT_ENCODING = "Accept-Encoding";

	/**
	 * 浏览器可接受的MIME类型
	 */
	public static final String ACCEPT = "Accept";

	/**
	 * 浏览器类型
	 */
	public static final String USER_AGENT = "User-Agent";

	/**
	 * 包含一个URL，用户从该URL代表的页面出发访问当前请求的页面
	 */
	public static final String REFERER = "Referer";

	/**
	 * 获取当前Request
	 */
	public static HttpServletRequest getRequest() {
		return ServletActionContext.getRequest();
	}

	/**
	 * 判断当前页面是否接收到了Post请求
	 * @return 是否接收到Post请求
	 */
	public static boolean isPost() {
		return getRequest().getMethod().equalsIgnoreCase("POST");
	}

	/**
	 * 判断当前页面是否接收到了Get请求
	 * @return 是否接收到Get请求
	 */
	public static boolean isGet() {
		return getRequest().getMethod().equalsIgnoreCase("GET");
	}

	/**
	 * 返回上一个页面的地址
	 * @return 上一个页面地址
	 */
	public static String getUrlReferrer() {
		return Utils.null2String(getServerString(REFERER));
	}

	/**
	 * 返回指定的请求头信息
	 * @param name 变量名
	 * @return 信息
	 */
	public static String getServerString(String name) {
		if (getRequest().getHeader(name) == null) {
			return "";
		}
		return getRequest().getHeader(name);
	}

	/**
	 * 得到当前完整主机头
	 * @return
	 */
	public static String getCurrentFullHost() {
		return getRequest().getRemoteHost() + ":" + getRequest().getRemotePort();
	}

	/**
	 * 得到主机头
	 * @return
	 */
	public static String getHost() {
		return getRequest().getRemoteHost();
	}

	/**
	 * 判断当前访问是否来自浏览器软件
	 * @return 当前访问是否来自浏览器软件
	 */
	public static boolean isBrowserGet() {
		String[] browserName = { "ie", "opera", "netscape", "mozilla", "konqueror", "firefox" };
		String curBrower = getServerString(USER_AGENT).toLowerCase();
		for (String string : browserName) {
			if (curBrower.indexOf(string) >= 0) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 判断是否来自搜索引擎链接
	 * @return 是否来自搜索引擎链接
	 */
	public static boolean isSearchEnginesGet() {
		if (getUrlReferrer().equals("")) {
			return false;
		}
		String[] searchEngine = { "google", "yahoo", "msn", "baidu", "sogou", "sohu", "sina", "163", "lycos", "tom",
				"yisou", "iask", "soso", "gougou", "zhongsou" };
		for (String string : searchEngine) {
			if (string.indexOf(getUrlReferrer()) >= 0) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 获取当前用户IP地址
	 */
	public static String getIp() {
		return getRequest().getRemoteAddr();
	}

	/**
	 * 获得当前完整URL地址
	 * @return 当前完整Url地址
	 */
	public static String getUrl() {
		return getRequest().getRequestURL().toString();
	}

	/**
	 * 获得指定请求参数值
	 * @param paramName 参数名
	 * @return 值(如果没有或为空则返回"")
	 */
	public static String getParamValue(String paramName) {
		return getParamValue(paramName, true);
	}

	/**
	 * 获得指定请求参数值
	 * @param paramName 参数名
	 * @return 值(如果没有或为空则返回"")
	 */
	public static String getParamValue(String paramName, boolean clearHtml) {
		if (clearHtml) {
			return Utils.cleanHtmlTag(Utils.null2String(getRequest().getParameter(paramName)).trim());
		}
		return Utils.null2String(getRequest().getParameter(paramName)).trim();
	}

	/**
	 * 获得指定请求参数值
	 * @param paramName 参数名
	 * @return
	 */
	public static String[] getParamValues(String paramName) {
		return getRequest().getParameterValues(paramName);
	}

	/**
	 * 获得指定请求参数值
	 * @param paramName
	 * @param split
	 * @return
	 */
	public static String getParamValues(String paramName, String split) {
		String[] values = getParamValues(paramName);
		if (values == null) {
			return "";
		}
		String result = "";
		for (String string : values) {
			if (!string.trim().equals("")) {
				result += string.trim() + split;
			}
		}
		return result.substring(0, result.length() - 1);
	}

	/**	 
	 * 获得指定请求参数值(int)
	 * @param paramName 参数名
	 * @param defVlaue 如果没有或为空则返回参数
	 * @return 值
	 */
	public static int getParamIntValue(String paramName, int defVlaue) {
		return Utils.null2Int(getParamValue(paramName), defVlaue);
	}

	/**
	 * 获取当前请求页面
	 */
	public static String getPageName() {
		// 获取请求路径
		String uri = getRequest().getRequestURI();
		return uri.substring(uri.lastIndexOf("/") + 1).toLowerCase();
	}

}
