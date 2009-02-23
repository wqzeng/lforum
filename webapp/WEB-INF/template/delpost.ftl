<#-- 
	描述：删除帖子模板
	作者：黄磊 
	版本：v1.0 
-->
<#import "inc/page_comm.ftl" as comm />
<@comm.header/>
<#if reqcfg.page_err==0>
<div id="foruminfo">
	<div id="nav">
	<a href="${config.forumurl}">${config.forumtitle}</a> &raquo; ${forumnav}
	 &raquo; <a href="showtopic.action?topicid=${topicid}">${topictitle}</a> &raquo; <strong>删除</strong>
	</div>
</div>
<@comm.msgbox/>
<#else>
<@comm.errmsgbox/>
</#if>
</div>
<@comm.copyright/>
<@comm.footer/>