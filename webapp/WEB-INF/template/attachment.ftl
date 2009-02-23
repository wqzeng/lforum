<#-- 
	描述：附件查看页模板
	作者：黄磊 
	版本：v1.0 
-->
<#import "inc/page_comm.ftl" as comm />
<@comm.header/>
<#if reqcfg.page_err==0>
<div id="foruminfo">
	<div id="nav">
		<a href="${config.forumurl}">${config.forumtitle}</a> &raquo; ${forumnav} &raquo; 
	    <a href="showtopic.action?topicid=${topicid}">${topictitle}</a><strong>附件</strong>
	</div>
</div>
<#else>
	<#if needlogin>
		<@comm.login/>
	<#else>
		<@comm.errmsgbox/>
	</#if>
</#if>
</div>
<@comm.footer/>