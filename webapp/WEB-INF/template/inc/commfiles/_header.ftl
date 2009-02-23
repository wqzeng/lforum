<#-- 
	描述：页面顶部定义  
	作者：黄磊 
	版本：v1.0 
-->
<body>
<div id="append_parent"></div><div id="ajaxwaitid"></div>
<div class="wrap">
<div id="header">
	<h2><a href="${forumurl}" title="LForum|BBS|论坛 - Powered by LForum For Java"><img src="templates/${templatepath}/images/logo.gif" alt="LForum For Java|BBS|论坛"/></a>
	</h2>
	<#if headerad !="">
		<div id="ad_headerbanner">${headerad}</div>
	</#if>
</div>
<div id="menu">
	<#if (config.isframeshow>0)>
	<div class="frameswitch">
		<script type="text/javascript">
			if(top == self) {
				document.write('<a href="frame.action?f=1" target="_top">分栏模式<\/a>');
			}
		</script>
	</div>
	</#if>
	<span class="avataonline">
		<#if userid==-1>
			<a href="${forumurl}login.action" class="reg">登录</a>
			<a href="${forumurl}register.action" class="reg">注册</a>
		<#else>
			<cite>欢迎:<a class="dropmenu" id="viewpro" onmouseover="showMenu(this.id)">${username}</a></cite>
			<a href="${forumurl}logout.action?userkey=${userkey}">退出</a>
		</#if>
	</span>
	<ul>
	<#if userid!=-1>
		<li><a href="${forumurl}usercpinbox.action" class="notabs">短消息</a></li>
		<li><a href="${forumurl}usercp.action" class="reg">用户中心</a></li>
		<#if useradminid==1>
		<li><a href="${forumurl}admin/index.action" target="_blank"  class="reg">系统设置</a></li>
		</#if>
		<li id="my" class="dropmenu" onMouseOver="showMenu(this.id);"><a href="#">我的</a></li>
	</#if>
		<li><a href="" <#if userid==-1>class="notabs"</#if> >标签</a></li>
	<#if usergroupinfo.allowviewstats==1>
		<li id="stats" class="dropmenu" onmouseover="showMenu(this.id)"><a href="stats.action">统计</a></li>
	</#if>
		<li><a href="${forumurl}showuser.action">会员</a></li>
	<#if usergroupinfo.allowsearch!=0>
		<li><a href="${forumurl}search.action">搜索</a></li>
	</#if>
	<li><a href="${forumurl}help.action" target="_blank">帮助</a></li>
	</ul>
</div>