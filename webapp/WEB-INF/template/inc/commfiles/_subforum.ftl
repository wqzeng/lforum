<#-- 
	描述：子板块列表模板 
	作者：黄磊 
	版本：v1.0 
-->
<#if forum.colcount==1>
<!--ntforumboxstart-->
<div class="mainbox forumlist">
	<span class="headactions">
		<img id="category_${forum.fid}_img" src="templates/${templatepath}/images/collapsed_no.gif" alt="展开/收起" onclick="toggle_collapse('category_${forum.fid}');"/>
	</span>
	<h3>子版块</h3>
	<table cellspacing="0" cellpadding="0" summary="category_${forum.fid}">
		<thead class="category">
			<tr>
				<th>版块</th>
				<td class="nums">主题</td>
				<td class="nums">帖子</td>
				<td class="lastpost">最后发表</td>
			</tr>
		</thead>
		<#list subforumlist as subforum>
		<tbody id="category_${forum.fid}">
			<tr>
				<th <#if subforum.havenew=="new" >class="new"</#if>>
					<h2>
						<#if subforum.forumfields.icon!="">
							<img src="${subforum.forumfields.icon}" border="0" align="left" hspace="5" alt="${subforum.name}"/>
						</#if>
						<#if subforum.forumfields.redirect=="">
							<a href="showforum.action?forumid=${subforum.fid}">
						<#else>
							<a href="${subforum.forumfields.redirect}" target="_blank">
						</#if>
						${subforum.name}</a><#if (subforum.todayposts>0)><span class="today">(${subforum.todayposts})</span></#if>
					</h2>
					<#if subforum.forumfields.description!=""><p>${subforum.forumfields.description}</p></#if>
					<#if subforum.forumfields.moderators!=""><p class="moderators">版主:${subforum.forumfields.moderators}</p></#if>
				</th>
				<td class="nums">${subforum.topics_1}</td>
				<td class="nums">${subforum.posts}</td>
				<td class="lastposter">
					<#if subforum.status==-1>
						私密论坛
					<#else>
						<#if subforum.topics.tid!=0>
						<p>
							<a href="showtopic.action?topicid=${subforum.topics.tid}">${subforum.lasttitle}</a>
						</p>
						<div class="topicbackwriter">by
							<#if subforum.lastposter!="">
								<#if subforum.users.uid==-1>
									游客
								<#else>
									<a href="userinfo.action?userid=${subforum.users.uid}" target="_blank">${subforum.lastposter}</a>
								</#if>
							<#else>
								匿名
							</#if>
						- 	<a href="showtopic.action?topicid=${subforum.topics.tid}&page=end#lastpost" title="${subforum.lasttitle}"><span>${subforum.lastpost}</span></a>
						</div>
						<#else>
							从未
						</#if>
					</#if>
				</td>
			  </tr>
		   </tbody>
			</#list>
			</table>
	</div>
<!--ntforumbox end-->
<#else>
<!--ntforumbox start-->
<div class="mainbox forumlist">
	<span class="headactions">
		<#if forum.forumfields.moderators!="">
			分类版主: ${forum.forumfields.moderators}
		</#if>
		<img id="category_${forum.fid}_img"  src="templates/${templatepath}/images/collapsed_no.gif" alt="展开/收起" onclick="toggle_collapse('category_${forum.fid}');"/>
	</span>
	<h3>子论坛</h3>
	<table id="category_${forum.fid}"  cellspacing="0" cellpadding="0" summary="category_${forum.fid}">
		<#assign subforumindex=0>
		<#assign colwidth = 99.6 / forum.colcount >
		<#list subforumlist as subforum>
		<#assign subforumindex=subforumindex+1>
		<#if subforumindex==1>
		<tbody id="category_${forum.fid}">
			<tr>
		</#if>
			  <th style="width:${colwidth}%;"<#if subforum.havenew=="new" >class="new"</#if>>
					<h2>
					<#if subforum.forumfields.icon!="">
						<img src="${subforum.forumfields.icon}" alt="${subforum.name}" hspace="5" />
					</#if>
					<#if forum.forumfields.redirect=="">
						<a href="showforum.action?forumid=${subforum.fid}">
					<#else>
						<a href="${subforum.forumfields.redirect}" target="_blank">
					</#if>
					${subforum.name}</a>
					<#if (subforum.todayposts>0)>
					<span class="today">(${subforum.todayposts})</span>
					</#if>
					</h2>
					<p>主题:${subforum.topics_1}, 帖数:${subforum.posts}</p>
					<#if subforum.status==-1>
					<p>私密版块</p>
					<#else>
						<#if subforum.topics.tid!=0>
							<p>最后: <a href="showtopic.action?topicid=${subforum.topics.tid}&page=end#lastpost" title="${subforum.lasttitle}"><span>${subforum.lastpost}</span></a> by 
								<#if subforum.lastposter!="">
									<#if subforum.users.uid==-1>
										游客
									<#else>
										<a href="userinfo.action?userid=${subforum.users.uid}" target="_blank">${subforum.lastposter}</a>
									</#if>
								<#else>
									匿名
								</#if>
							</p>
						</#if>				
					</#if>
			  </th>
		<#if subforumindex==forum.colcount>
			</tr>
			<#assign subforumindex=0>
		</#if>
		</#list>
		<#if subforumindex!=0>
			<#list 0..(forum.colcount-subforumindex) as x>
				<td>&nbsp;</td>
			</#list>
			</tr>
		</#if>
	</table>
</div>
<!--ntforumbox end-->
</#if>