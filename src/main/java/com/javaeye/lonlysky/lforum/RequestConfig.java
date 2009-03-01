package com.javaeye.lonlysky.lforum;

import com.javaeye.lonlysky.lforum.comm.utils.Utils;

/**
 * <b>客户端请求配置信息</b><br>
 * 本类主要封装一些页面通用配置信息
 * 
 * @author 黄磊
 * 
 */
public class RequestConfig {

	private int page_err = 0;
	private String msgbox_text = "";
	private String msgbox_showbacklink = "true";
	private String msgbox_backlink = "javascript:history.back();";

	private String msgbox_url = "";
	private String meta = "";
	private String script = "";
	private String link = "";
	private long pageTime = 0;

	/**
	 * 设置页面定时转向
	 */
	public RequestConfig setMetaRefresh() {
		return setMetaRefresh(2, msgbox_url);
	}

	public RequestConfig setMetaRefresh(int sec) {
		return setMetaRefresh(sec, msgbox_url);
	}

	public RequestConfig setMetaRefresh(int sec, String url) {
		meta = meta + "\r\n<meta http-equiv=\"refresh\" content=\"" + sec + "; url=" + url + "\" />";
		return this;
	}

	/**
	 * 插入指定路径的CSS
	 * @param url CSS路径
	 */
	public RequestConfig addLinkCss(String url) {
		link = link + "\r\n<link href=\"" + url + "\" rel=\"stylesheet\" type=\"text/css\" 测试link/>";
		return this;
	}

	/**
	 * 插入指定路径的RSS
	 * @param url Rss路径
	 * @param title 标题
	 * @return
	 */
	public RequestConfig addLinkRss(String url, String title) {
		link = link + "\r\n<link rel=\"alternate\" type=\"application/rss+xml\" title=\"" + title + "\" href=\"" + url
				+ "\" />";
		return this;
	}

	/**
	 * 插入指定路径的CSS
	 */
	public RequestConfig addLinkCss(String url, String linkid) {
		link = link + "\r\n<link href=\"" + url + "\" rel=\"stylesheet\" type=\"text/css\" id=\"" + linkid + "\" />";
		return this;
	}

	/**
	 * 更新页面Meta
	 * @param seokeywords 关键词
	 * @param seodescription 说明
	 * @param Seohead 其它增加项
	 */
	public void updateMetaInfo(String seokeywords, String seodescription, String Seohead) {
		String[] metaArray = meta.split("\r\n");
		//设置为空,并在下面代码中进行重新赋值
		meta = "";
		for (String metaoption : metaArray) {
			//找出keywords关键字
			if (metaoption.toLowerCase().indexOf("name=\"keywords\"") > 0) {
				if (seokeywords != null && seokeywords.trim() != "") {
					meta += "<meta name=\"keywords\" content=\"" + Utils.cleanHtmlTag(seokeywords) + "\" />\r\n";
					continue;
				}
			}

			//找出description关键字
			if (metaoption.toLowerCase().indexOf("name=\"description\"") > 0) {
				if (seodescription != null && seodescription.trim() != "") {
					meta += "<meta name=\"description\" content=\"" + Utils.cleanHtmlTag(seodescription) + "\" />\r\n";
					continue;
				}
			}

			meta = meta + metaoption + "\r\n";
		}

	}

	/**
	 * 添加页面Meta信息
	 * @param seokeywords 关键词
	 * @param seodescription 说明
	 * @param seohead 其它增加项
	 */
	public RequestConfig addMetaInfo(String seokeywords, String seodescription, String seohead) {
		if (seokeywords != "") {
			meta = meta + "<meta name=\"keywords\" content=\"" + Utils.cleanHtmlTag(seokeywords) + "\" />\r\n";
		}
		if (seodescription != "") {
			meta = meta + "<meta name=\"description\" content=\"" + Utils.cleanHtmlTag(seodescription) + "\" />\r\n";
		}
		meta = meta + seohead;
		return this;
	}

	/**
	 * 插入脚本内容到页面head中
	 * 
	 * @param scriptstr
	 *            脚本内容
	 * @param scripttype
	 *            脚本类型(值为：vbscript或javascript,默认为javascript)
	 */
	public RequestConfig addScript(String scriptstr, String scripttype) {
		if (!scripttype.toLowerCase().equals("vbscript") && !scripttype.toLowerCase().equals("vbscript")) {
			scripttype = "javascript";
		}
		script = script + "\r\n<script type=\"text/" + scripttype + "\">" + scriptstr + "</script>";
		return this;
	}

	public RequestConfig addScript(String scriptstr) {
		return addScript(scriptstr, "javascript");
	}

	/**
	 * 插入指定Meta
	 * 
	 * @param metastr
	 *            Meta项
	 */
	public RequestConfig addMeta(String metastr) {
		meta = meta + "\r\n<meta " + metastr + " />";
		return this;
	}

	/**
	 * 增加错误信息
	 * 
	 * @param errinfo
	 *            错误信息
	 */
	public RequestConfig addErrLine(String errinfo) {
		errinfo = "<li>" + errinfo + "</li>";
		if (msgbox_text.length() == 0) {
			msgbox_text += errinfo;
		} else {
			msgbox_text += "<br />" + errinfo;
		}
		page_err++;
		return this;
	}

	/**
	 * 增加提示信息
	 * 
	 * @param strinfo
	 *            提示信息
	 */
	public RequestConfig addMsgLine(String strinfo) {
		if (msgbox_text.length() == 0) {
			msgbox_text = msgbox_text + strinfo;
		} else {
			msgbox_text += "<br />" + strinfo;
		}
		return this;
	}

	/**
	 * 是否已经发生错误
	 * 
	 * @return
	 */
	public boolean isErr() {
		return page_err > 0;
	}

	/**
	 * 设置要转向的url
	 * 
	 * @param strurl
	 */
	public RequestConfig setUrl(String strurl) {
		msgbox_url = strurl;
		return this;
	}

	/**
	 * 设置回退链接的内容
	 * 
	 * @param strback
	 */
	public RequestConfig setBackLink(String strback) {
		msgbox_backlink = strback;
		return this;
	}

	/**
	 * 设置是否显示回退链接
	 * @param link 要显示则为true, 否则为false
	 */
	public RequestConfig setShowBackLink(boolean link) {
		if (link) {
			msgbox_showbacklink = "true";
		} else {
			msgbox_showbacklink = "false";
		}
		return this;
	}

	public long getPageTime() {
		return System.currentTimeMillis() - pageTime;
	}

	public RequestConfig setPageTime(long executiontime) {
		pageTime = executiontime;
		return this;
	}

	public String getMsgbox_showbacklink() {
		return msgbox_showbacklink;
	}

	public String getMsgbox_text() {
		return msgbox_text;
	}

	public String getMsgbox_url() {
		return msgbox_url;
	}

	public String getMeta() {
		return meta;
	}

	public String getScript() {
		return script;
	}

	public int getPage_err() {
		return page_err;
	}

	public String getMsgbox_backlink() {
		return msgbox_backlink;
	}

	public String getLink() {
		return link;
	}

	/**
	 * 按位&运算
	 * @param num1
	 * @param num2
	 * @return
	 */
	public int opNum(int num1, int num2) {
		return num1 & num2;
	}

	public String urlEncode(String str) {
		return Utils.urlEncode(str);
	}
}
