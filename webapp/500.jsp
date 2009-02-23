<%@ page language="java" contentType="text/html; charset=UTF-8"
	isErrorPage="true" pageEncoding="UTF-8"%>
<%@ page import="java.io.*,java.util.*"%>
<%
	response.setStatus(HttpServletResponse.SC_OK);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>500错误提示 - Powered by Lonlysky</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<style type="text/css">
<!--
body {
	margin: 20px;
	font-family: Tahoma, Verdana;
	font-size: 14px;
	color: #333333;
	background-color: #FFFFFF;
}

a {
	color: #1F4881;
	text-decoration: none;
}
-->
</style>
</head>
<body>
<div
	style="border: #cccccc solid 1px; padding: 20px; width: 500px; margin: auto"
	align="center">程序发生了错误，有可能该页面正在调试或者是设计上的缺陷! <br />
<br />
<a href='javascript:history.back();'>【返回上页】</a><a
	href='${path}index.html'>【返回首页】</a></div>
<br />
<hr width=80%>
<h2><font color=#DB1260>LForum Error Page</font></h2>

<p>发生异常: <b> <%=exception.getClass()%>:<%=exception.getMessage()%></b></p>
<%
	System.out.println("Header....");
	Enumeration<String> e = request.getHeaderNames();
	String key;
	while (e.hasMoreElements()) {
		key = e.nextElement();
		System.out.println(key + "=" + request.getHeader(key));
	}
	System.out.println("Attribute....");
	e = request.getAttributeNames();
	while (e.hasMoreElements()) {
		key = e.nextElement();
		System.out.println(key + "=" + request.getAttribute(key));
	}

	System.out.println("Parameter....");
	e = request.getParameterNames();
	while (e.hasMoreElements()) {
		key = e.nextElement();
		System.out.println(key + "=" + request.getParameter(key));
	}
	System.out.println("CLASSPATH....");
	System.out.println(System.getProperty("java.class.path"));
	exception.printStackTrace();
	ByteArrayOutputStream ostr = new ByteArrayOutputStream();
	exception.printStackTrace(new PrintStream(ostr));
%>
</pre>
<hr width=80%>
<div style="border: 0px; padding: 0px; width: 500px; margin: auto">
<strong>版权所有:</strong> (C)&nbsp;LForum For Java&nbsp;2008</div>
</body>
</html>