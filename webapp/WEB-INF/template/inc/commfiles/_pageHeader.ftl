<#-- 
	描述：页面head部分
	作者：黄磊 
	版本：v1.0 
-->
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=7" />
${reqcfg.meta}
<#if pagetitle == "首页" >
<title>${config.forumtitle} - ${config.seotitle} - Powered by LForum For Java</title>
<#else>
<title>${pagetitle} - ${config.forumtitle} - ${config.seotitle} - Powered by LForum For Java</title>
</#if>
<link rel="icon" href="favicon.ico" type="image/x-icon" />
<link rel="shortcut icon" href="favicon.ico" type="image/x-icon" />
<!-- 调用样式表 -->
<link rel="stylesheet" href="templates/${templatepath}/dnt.css" type="text/css" media="all"  />
${reqcfg.link}
<script type="text/javascript" src="javascript/template_report.js"></script>
<script type="text/javascript" src="javascript/template_utils.js"></script>
<script type="text/javascript" src="javascript/common.js"></script>
<script type="text/javascript" src="javascript/menu.js"></script>
${reqcfg.script}
</head>