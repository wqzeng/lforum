<#-- 
	描述：分栏模式首页模板
	作者：黄磊 
	版本：v1.0 
-->
<#import "inc/page_comm.ftl" as comm />
<@comm.pageHeader/>
<body id="focuslist">
<div class="wrap">

<div id="foruminfo">
	<div id="userinfo">
		<div id="nav">
		<p>积分: <strong>${userinfo.credits}</strong> / 头衔:<strong> ${usergroupinfo.grouptitle}</strong> / 你上次访问是在 ${lastvisit}</p>
		<p>共 <strong>${totalusers}</strong> 位会员 / 欢迎新会员 <strong>
		<a href="{aspxrewriteurl}">${lastusername}</a></strong></p>
		</div>
	</div>	
	<div id="forumstats">
		<p>共 <strong>${totaltopic}</strong>  篇主题 /<strong> ${totalpost}</strong>  个帖子 / 今日<strong> ${todayposts}</strong>  个帖子</p>
		<p>
		<#if userid!=-1>
		<a href="mytopics.action">我的主题</a>
		<a href="myposts.action">我的帖子</a>
		<a href="search.action?posterid=${userid}&amp;type=digest">我的精华</a>
	</#if>
		<a href="showtopiclist.action?type=newtopic&amp;newtopic=${newtopicminute}&amp;forums=all">查看新帖</a>
		<a href="showtopiclist.action?type=digest&amp;forums=all">精华帖区</a>
	<#if config.rssstatus!=0>
		<a href="tools/rss.action" target="_blank"><img src="templates/${templatepath}/images/rss.gif" alt="Rss"/></a>
	</#if>
	</p>
	</div>
</div>
<@comm.newpmmsgbox/>
<div id="forumfocus">
	<div class="focuslistleft">
		<div class="mainbox">
			<h3>最新精华主题</h3>
			<ul class="navfocuslist">
				<#list digesttopiclist as digesttopic>
					<#if digesttopic.iconid!="0">
						<li><img src="images/posticons/${digesttopic.iconid}.gif" alt="smile"/><a href="{aspxrewriteurl}" target="main">${digesttopic.title}</a></li>
					<#else>
						<li class="listspace"><a href="{aspxrewriteurl}" target="main">${digesttopic.title}</a> </li>
					</#if>
				</#list>
			</ul>
		</div>
	</div>
	<div class="focuslistright">
		<div class="mainbox">
			<h3>最新热门主题</h3>
			<ul class="navfocuslist">
				<#list hottopiclist as hottopic>
					<#if hottopic.iconid!="0">
						<li><img src="images/posticons/${hottopic.iconid}.gif" alt="smile"/><a href="{aspxrewriteurl}" target="main">${hottopic.title}</a></li>
					<#else>
						<li class="listspace"><a href="{aspxrewriteurl}" target="main">${hottopic.title}</a> </li>
					</#if>
				</#list>
			</ul>
		</div>
	</div>
</div>
<!---bbs-list area end--->
<#if (forumlinkcount>0)>
<div class="box">
	<span class="headactions"><img id="forumlinks_img" src="templates/${templatepath}/images/collapsed_no.gif" alt="" onClick="toggle_collapse('forumlinks');" /></span>
	<h4>友情链接</h4>
	<table id="forumlinks" cellspacing="0" cellpadding="0" style="table-layout: fixed;" summary="友情链接">
		<#list forumlinklist as forumlink>
		<tbody>	
		<tr>	
		<#if forumlink.logo!="">
			<td>
				<a href="${forumlink.url}" target="_blank"><img src="${forumlink.logo}" alt="${forumlink.name}"  class="forumlink_logo"/></a>
				<h5><a href="${forumlink.url}" target="_blank">${forumlink.name}</a></h5>
				<p>${forumlink.note}</p>
			</td>
		<#elseif forumlink.name!="$$otherlink$$">
			<td>
				<h5>
					<a href="${forumlink.url}" target="_blank">${forumlink.name}</a>
				</h5>
				<p>${forumlink.note}</p>
			</td>
		<#else>
			<td>
				${forumlink.note}
			</td>
		</#if>
		</tr>
		</tbody>
		</#list>
	</table>
</div>
</#if>

<#if config.whosonlinestatus!=0 && config.whosonlinestatus!=2>
<div class="box" id="online">
	<span class="headactions">
		<#if !showforumonline>
			<a href="?showonline=yes#online"><img src="templates/${templatepath}/images/collapsed_yes.gif" alt="展开/收起" />
		<#else>
			<a href="?showonline=no#online"><img src="templates/${templatepath}/images/collapsed_no.gif" alt="展开/收起" />
		</#if>
		</a>
	</span>
	<h4>
		<strong><a href="${forumurl}onlineuser.action">在线用户</a></strong>- <em>${totalonline}</em> 人在线 <#if showforumonline>- ${totalonlineuser} 会员<span id="invisible"></span>, ${totalonlineguest} 游客</#if>- 最高记录是 <em>${highestonlineusercount}</em> 于 <em>${highestonlineusertime}</em>
	</h4>
	<dl id="onlinelist">
		<dt>${onlineiconlist}</dt>
		<dd class="onlineusernumber">
			共<strong>${totalusers}</strong>位会员 <span class="newuser">新会员:
			<a href="{aspxrewriteurl}">${lastusername}</a></span>
		</dd>
		<dd>
			<ul class="userlist">
				<#if showforumonline>
				<#assign invisiblecount = 0 />
				<#list onlineuserlist as onlineuser>
					<#if onlineuser.invisible==1>
					<#assign invisiblecount = invisiblecount + 1 />
				<li>(隐身会员)</li>
					<#else>
					<li>${onlineuser.olimg}
						<#if onlineuser.users.uid==-1>
							${onlineuser.username}
						<#else>
						<a href="{aspxrewriteurl}" target="_blank">${onlineuser.username}</a>
						</#if>
				</li>
					</#if>
				</#list>
			<#else>
				<li style="width: auto"><a href="?showonline=yes#online">点击查看在线列表</a></li>
			</#if>
			</ul>
		</dd>
	</dl>
</div>
</#if>
</div>
<@comm.copyright/>
<@comm.adlist/>
<@comm.footer/>