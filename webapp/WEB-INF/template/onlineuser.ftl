<#-- 
	描述：在线用户列表模板
	作者：黄磊 
	版本：v1.0 
-->
<#import "inc/page_comm.ftl" as comm />
<@comm.header/>
<div id="foruminfo">
	<div id="nav">
		<a href="${config.forumurl}">${config.forumtitle}</a> &raquo; <strong>在线用户列表</strong>
	</div>
	<div id="headsearch">
		<div id="search">
			<@comm.quicksearch/>
		</div>
	</div>
</div>

<div class="pages_btns">
	<div class="pages">
		<em>共${totalonline}人在线</em> - ${totalonlineuser}位会员 
		<#if (totalonlineinvisibleuser>0)>
		${totalonlineinvisibleuser}隐身
		,</#if>${totalonlineguest}位游客 | 最高纪录是 ${highestonlineusercount} 于 ${highestonlineusertime}
	</div>
</div>

<div class="mainbox">
	<h3>在线用户列表</h3>
	<table summary="在线用户列表" cellspacing="0" cellpadding="0">
		<thead class="category">
			<tr>
			<th>&nbsp;</th>
			<th>用户名</th>
			<th>时间</th>
			<th>当前动作</th>
			<th>所在论坛</th>
			<th>所在主题</th>
			</tr>
		</thead>
		<#list onlineuserlist as onlineuserinfo>
		<tbody>
			<tr>
				<td><img src="templates/${templatepath}/images/member.gif" alt="用户" /></td>
				<td>
					<#if onlineuserinfo.users.uid==-1>
						 游客
					  <#else>
						 <#if onlineuserinfo.invisible==1>
						 (隐身用户)
						 <#else>
						 <a href="userinfo.action?userid=${onlineuserinfo.users.uid}">${onlineuserinfo.username}</a>
						 </#if>
					  </#if>
				</td>
				<td>${onlineuserinfo.lastupdatetime}</td>
				<td>${onlineuserinfo.actionname?default("")}&nbsp;</td>
				<td><#if onlineuserinfo.forums?exists><a href="showforum.action?forumid=${onlineuserinfo.forums.fid}">${onlineuserinfo.forumname}</a></#if>&nbsp;</td>
				<td><#if onlineuserinfo.topics?exists><a href="showtopic.action?topicid=${onlineuserinfo.topics.tid}">${onlineuserinfo.title}</a></#if>&nbsp;</td>
			</tr>
		</tbody>
		</#list>
	</table>
</div>
<div class="pages_btns">
	<div class="pages">
		<em>共${onlineusernumber}名用户</em><strong>${pagecount}页</strong>${pagenumbers}
		<kbd>跳转<input name="gopage" type="text" id="gopage" onKeyDown="if(event.keyCode==13) {
	window.location='onlineuser.action?page='+this.value;}"  size="4" maxlength="9" class="colorblue2"/>页
		</kbd>
	</div>
</div>

</div>
<@comm.copyright/>
<@comm.footer/>