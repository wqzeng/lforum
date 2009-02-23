<#-- 
	描述：添加收藏模板
	作者：黄磊 
	版本：v1.0 
-->
<#import "inc/page_comm.ftl" as comm />
<@comm.header/>

<#if (reqcfg.page_err>0)>
	<@comm.errmsgbox/>
<#else>
<div id="foruminfo">
	<div id="nav">
	<#if topicid!=-1>
		<a href="${config.forumurl}">${config.forumtitle}</a> &raquo; ${forumnav} &raquo; 
		<a href="showforum.action?fid=${forumid}">${topictitle}</a> &raquo; <strong>收藏主题</strong>
	</#if>	
	</div>
</div>
	<@comm.msgbox/>
</#if>
</div>
<@comm.copyright/>
<@comm.footer/>
