<#-- 
	描述：投票页模板
	作者：黄磊 
	版本：v1.0 
-->
<#import "inc/page_comm.ftl" as comm />
<@comm.header/>
<!--TheCurrent start-->
<div id="foruminfo">
	<div class="userinfo">
	<h2><a href="${config.forumurl}">${config.forumtitle}</a><span>${forumnav}</span>
	<a href="showforum.action?forumid=${forumid}">${topictitle}</a><strong>投票</strong></h2>
	</div>
  </div>
<!--TheCurrent end-->

<#if reqcfg.page_err==0>
<@comm.msgbox/>
<#else>
<@comm.errmsgbox/>
</#if>
</div>
<@comm.copyright/>
<@comm.footer/>