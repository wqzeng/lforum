<#-- 
	描述：用户信息页模板
	作者：黄磊 
	版本：v1.0 
-->
<#import "inc/page_comm.ftl" as comm />
<@comm.header/>
<div id="foruminfo">
	<div id="nav">
		<a href="${config.forumurl}" class="home">${config.forumtitle}</a>  &raquo; <a href="usercp.action">用户中心</a>  &raquo; <strong>查看用户信息</strong>
	</div>
	<div id="headsearch">
		<div id="search">
			<@comm.quicksearch/>
		</div>
	</div>
</div>
<#if reqcfg.page_err==0>
<!--主体-->
<div class="mainbox viewthread specialthread">
<table cellspacing="0" cellpadding="0" summary="辩论主题">
	<tr>
	<td class="postcontent">
		<h3>用户信息${user.username}</h3>
		<table cellspacing="0" cellpadding="0" summary="辩论主题">
		<thead>
		<tr>
			<th class="infotitle">论坛信息</th>
			<td>&nbsp;</td>
		</tr>
		</thead> 
		<tr>
			<th class="usertitle">用户名</th>
			<td class="navname">${user.username}</td>
		</tr>
		<tr>
			<th>用户ID</th>
			<td>${user.uid}</td>
		</tr>
		<tr>
			<th>昵称</th>
			<td>${user.nickname}</td>
		</tr>
		<tr>
			<th>自定义头衔</th>
			<td>${user.userfields.customstatus}</td>
		</tr>
		<tr>
			<th>用户组</th>
			<td>${group.grouptitle}</td>
		</tr>
		<tr>
			<th>阅读权限</th>
			<td>${group.readaccess}</td>
		</tr>		
		<tr>
			<th>积分</th>
			<td>${user.credits}</td>
		</tr>
		<#if score[1]!="">
		<tr>
			<th>${score[1]}</th>
			<td>${user.extcredits1}</td>
		</tr>
		</#if>
		<#if score[2]!="">
		<tr>
			<th>${score[2]}</th>
			<td>${user.extcredits2}</td>
		</tr>
		</#if>
		<#if score[3]!="">
		<tr>
			<th>${score[3]}</th>
			<td>${user.extcredits3}</td>
		</tr>
		</#if>
		<#if score[4]!="">
		<tr>
			<th>${score[4]}</th>
			<td>${user.extcredits4}</td>
		</tr>
		</#if>
		<#if score[5]!="">
		<tr>
			<th>${score[5]}</th>
			<td>${user.extcredits5}</td>
		</tr>
		</#if>
		<#if score[6]!="">
		<tr>
			<th>${score[6]}</th>
			<td>${user.extcredits6}</td>
		</tr>
		</#if>
		<#if score[7]!="">
		<tr>
			<th>${score[7]}</th>
			<td>${user.extcredits7}</td>
		</tr>
		</#if>
		<#if score[8]!="">
		<tr>
			<th>${score[8]}</th>
			<td>${user.extcredits8}</td>
		</tr>
		</#if>
		<tr>
			<th>发帖量</th>
			<td>${user.posts}</td>
		</tr>
		<tr>
			<th>精华帖数</th>
			<td>${user.digestposts}</td>
		</tr>
		<tr>
			<th>在线时间</th>
			<td>${user.oltime}分</td>
		</tr>
		<thead>
		<tr>
			<th class="infotitle">个人信息</th>
			<td>&nbsp;</td>
		</tr>
		</thead>
		<tr>
			<th>来自</th>
			<td>${user.userfields.location}</td>
		</tr>
		<tr>
			<th>性别</th>
			<td><#if user.gender==0>保密</#if>
				<#if user.gender==1>男</#if>
				<#if user.gender==2>女</#if>
			</td>
		</tr>
		<tr>
			<th>生日</th>
			<td>${user.bday}</td>
		</tr>
		<#if admininfo?exists&&admininfo.allowviewip==1>
		<tr>
			<th>注册IP</th>
			<td>${user.regip}</td>
		</tr>
		</#if>
		<tr>
			<th>注册日期</th>
			<td>${user.joindate}</td>
		</tr>
		<tr>
			<th>个人主页</th>
			<td>${user.userfields.website}</td>
		</tr>
		<tr>
			<th>自我介绍</th>
			<td>${user.userfields.bio}</td>
		</tr>
<#if admininfo?exists&&admininfo.allowviewrealname==1>
		<tr>
			<th>真实姓名</th>
			<td>${user.userfields.realname}</td>
		</tr>
		<tr>
			<th>身份证号码</th>
			<td>${user.userfields.idcard}</td>
		</tr>
		<tr>
			<th>移动电话号码</th>
			<td>${user.userfields.mobile}</td>
		</tr>		
		<tr>
			<th>固定电话号码</th>
			<td>${user.userfields.phone}</td>
		</tr>
</#if>
<#if user.showemail==1>
		<tr>
			<th>E-Mail</th>
			<td><a herf="#" onclick="javascript:location.href='mailto:${user.email}';">${user.email}</a></td>
		</tr>
</#if>
		<tr>
			<th>QQ</th>
			<td>${user.userfields.qq}</td>
		</tr>
		<tr>
			<th>MSN Messenger</th>
			<td>${user.userfields.msn}</td>
		</tr>
		<tr>
			<th>Yahoo Messenger</th>
			<td>${user.userfields.yahoo}</td>
		</tr>
		<tr>
			<th>Skype</th>
			<td>${user.userfields.skype}</td>
		</tr>
		<tr>
			<th>ICQ</th>
			<td>${user.userfields.icq}</td>
		</tr>
		<thead>
		<tr>
			<th class="infotitle">发帖情况</th>
			<td>&nbsp;</td>
		</tr>
		</thead>
		<tr>
			<th>最后发帖</th>
			<td class="userlink"><a href="showtree.action?postid=${user.topics.tid}" target="_blank">${user.lastposttitle}</a> <span>${user.lastpost}</span></td>
		</tr>
		<tr>
			<th>最后访问(登录)</th>
			<td>${user.lastvisit}</td>
		</tr>
		<tr>
			<th>最后活动</th>
			<td>${user.lastactivity}</td>
		</tr>
		<tr>
			<th>&nbsp;</th>
			<td class="userlink"><a href="search.action?posterid=${user.uid}">搜索该用户发表的主题及相关内容</a></td>
		</tr>
		</table>
	</td>
	<td class="postauthor">
		<#if user.userfields.avatar!="">
		<div class="avatar">
			<img class="avatar" src="${user.userfields.avatar}"
				<#if (user.userfields.avatarwidth>0)>
					width="${user.userfields.avatarwidth}"
					height="${user.userfields.avatarheight}"
				</#if>
			/>
		</div>
		</#if>
		<#if user.showemail==1>
		<p class="usermail"><a href="mailto:${user.email}">给该用户发送Email</a></p>
		</#if>
		<p class="userpm"><a href="usercppostpm.action?msgtoid=${user.uid}">给该用户发送短消息</a></p>
		<#if (useradminid>0&&admininfo?exists)>
			<#if admininfo.allowbanuser==1>
			<p class="userban"><a href="useradmin.action?action=banuser&uid=${user.uid}" title="禁止用户">禁言用户</a></p>
			</#if>
		</#if>
	</td>
	</tr>
</table>
</div>
<!--主体-->
</div>
<@comm.copyright/>
<#else>
	<#if needlogin>
		<@comm.login/>
	<#else>
	<@comm.errmsgbox/>
	</#if>
</div>
</#if>
<@comm.footer/>