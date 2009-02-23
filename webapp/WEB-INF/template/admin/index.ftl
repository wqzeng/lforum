<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Frameset//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-frameset.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>系统设置 - Powered by LForum</title>
<meta name="keywords" content="JAVA,论坛" />
<meta name="description" content="LForum,论坛,JAVA,论坛" />
<link href="styles/dntmanager.css" rel="stylesheet" type="text/css" />
<style>
body {
	margin: 0;
}
</style>
</head>
<frameset rows="43,*" frameborder="no" border="0" framespacing="0">
	<frame src="framepage/top.action" name="topFrame" scrolling="No"
		noresize="noresize" id="topFrame" />]
	<#if fromurl?exists>
		<frame src="framepage/managerbody.action?fromurl=${fromurl}"
			name="mainFrame" id="mainFrame" onresize="mainFrame.setscreendiv();"
			scrolling="No" />		
	<#else>	
		<frame src="framepage/managerbody.action" name="mainFrame"
			id="mainFrame" onresize="mainFrame.setscreendiv();" scrolling="No" />
	</#if>
</frameset>
</frameset>
<noframes></noframes>
</html>


