<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>LForum For Java提示信息 - Powered by Lonlysky</title>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<#if reqcfg?exists>
		${reqcfg.meta?default("")}
		${reqcfg.script?default("")}
		</#if>
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
			align="center"> 
			<#if reqcfg?exists>				
					${reqcfg.msgbox_text}
			<#else>
				没有任何可用提示信息，如有疑问请联系管理员	
			</#if>
			<br />			
		</div>		
		<br />
		<br />
		<div style="border: 0px; padding: 0px; width: 500px; margin: auto">
		<#if config.install==1 && pagename?exists>
		<strong>当前页面</strong> ${pagename}<br />		
		<strong>可选择操作:</strong> <#if userid==-1><a href="login.action">登录</a> | <a href="register.action">注册</a><#else>
		<a href="logout.action?userkey=${userkey}">退出登录</a></#if>
		<#else>
		<strong>可选择操作:</strong> <a href="javascript:history.back();">返回上页</a> | <a href="../main.action">返回首页</a>
		</#if>
		<br />
			<strong>版权所有:</strong> (C)&nbsp;LForum For Java&nbsp;2008 <#if reqcfg?exists>&nbsp;Processed in ${reqcfg.pageTime} ms.</#if>
		</div>
	</body>
</html>