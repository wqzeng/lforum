<#-- 
	描述：JQuery宏定义 
	作者：黄磊 
	版本：v1.0 
-->
<#macro jquery_main>
<script type="text/javascript" src="${path?default("../")}javascript/jquery.pack.js"></script>
</#macro>
	
<#macro jquery_validator>
<script type="text/javascript" src="${path?default("../")}javascript/jquery.validate.js"></script>
<script type="text/javascript" src="${path?default("../")}javascript/messages_cn.js"></script>
<link type="text/css" rel="stylesheet" href="${path?default("../")}style/validator.css" />	
</#macro>