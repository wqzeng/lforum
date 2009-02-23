<#import "../inc/jquery.ftl" as jquery />
<#assign s=JspTaglibs["/WEB-INF/struts-tags.tld"] />
<#macro bottom>
<div class="bottom">
        Powered by <a href="mailto:huangking124@163.com">
            <span>Lonlysky</span>
        </a>
        <br />
        <a href="mailto:huangking124@163.com">
            <span>版权所有&nbsp;(C)&nbsp;LForum For Java&nbsp;2008</span>
        </a>
</div>
</#macro>

<#macro header title>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>
	${title}
</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link rev="stylesheet" media="all" href="${path}Install/Images/styles.css" type="text/css" rel="stylesheet" />
<@jquery.jquery_main />
<@jquery.jquery_validator />
<#if reqcfg?exists>
${reqcfg.meta?default("")}
${reqcfg.script?default("")}
</#if>
</head>
</#macro>

<#macro first>
<input type="button" value="第一步" onclick="javascript:window.location='${path}install/install.action'" class="button_link" />
</#macro>