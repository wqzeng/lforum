<#-- 
	描述：论坛首页模板
	作者：黄磊 
	版本：v1.0 
-->
<#import "inc/page_comm.ftl" as comm />
<@comm.header/>
<div id="foruminfo">
	<div id="userinfo">
		<div id="nav">
		<p><a id="forumlist" href="${config.forumurl}" <#if config.forumjump==1>onmouseover="showMenu(this.id);" onmouseout="showMenu(this.id);"</#if>>${config.forumtitle}</a>		主题:<em>${totaltopic}</em>, 帖子:<em>${totalpost}</em> 
		</p>
		<p>
		<#if userid==-1>
		<form id="loginform" name="login" method="post" action="login.action?loginsubmit=true">
			<input type="hidden" name="referer" value="main.action" />
			<input onclick="if(this.value=='用户名')this.value = ''" value="用户名" maxlength="40" size="15" name="username" id="username" 
	type="text" />
			<input type="password" size="10" name="password" id="password" />
			<button value="true" type="submit" name="userlogin" onclick="javascript:window.location.replace('?agree=yes')"> 登录 </button>
		</form>
		<#else>
		您上次访问是在: ${userinfo.lastvisit} 
		<a href="showtopiclist.action">查看新帖</a>
		</#if>		
		</p>
		</div>
	</div>
	<div id="forumstats">
		<#if (usergroupinfo.allowsearch>0)>
			<@comm.quicksearch />
		</#if>
		<p>
		今日:<em>${todayposts}</em>, 昨日:<em>${yesterdayposts}</em>, 
		<#if highestpostsdate!="">
		最高日:<em>${highestposts}</em>(${highestpostsdate})
		</#if>

			<a href="showtopiclist.action?type=digest&amp;forums=all">精华区</a>
		<#if config.rssstatus!=0>	
			<a href="tools/rss.action" target="_blank"><img src="templates/${templatepath}/images/rss.gif" alt="rss"/></a>
		</#if>
		</p>
	</div>
</div>
<#-- 公告列表 -->
<#if (announcementcount>0)>
<div onmouseout="annstop = 0" onmouseover="annstop = 1" id="announcement">
	<div id="announcementbody">
		<ul>
		<#list announcementlist as announcement>		
        <li><a href="announcement.action#${announcement[id]}">${announcement.title}<em>${announcement.starttime}</em></a></li>
		</#list>
		</ul>
	</div>
</div>
<script type="text/javascript">
	var anndelay = 3000;
	var annst = 0;
	var annstop = 0;
	var annrowcount = 0;
	var anncount = 0;
	var annlis = $('announcementbody').getElementsByTagName("LI");
	var annrows = new Array();
	var annstatus;

	function announcementScroll() {
		if(annstop) {
			annst = setTimeout('announcementScroll()', anndelay);
			return;
		}
		if(!annst) {
			var lasttop = -1;
			for(i = 0;i < annlis.length;i++) {

				if(lasttop != annlis[i].offsetTop) {
					if(lasttop == -1) {
						lasttop = 0;
					}
					annrows[annrowcount] = annlis[i].offsetTop - lasttop;
					annrowcount++;
				}
				lasttop = annlis[i].offsetTop;
			}

			if(annrows.length == 1) {
				$('announcement').onmouseover = $('announcement').onmouseout = null;
			} else {
				annrows[annrowcount] = annrows[1];
				$('announcementbody').innerHTML += '<br style="clear:both" />' + $('announcementbody').innerHTML;
				annst = setTimeout('announcementScroll()', anndelay);
			}
			annrowcount = 1;
			return;
		}

		if(annrowcount >= annrows.length) {
			$('announcementbody').scrollTop = 0;
			annrowcount = 1;
			annst = setTimeout('announcementScroll()', anndelay);
		} else {
			anncount = 0;
			announcementScrollnext(annrows[annrowcount]);
		}
	}

	function announcementScrollnext(time) {
		$('announcementbody').scrollTop++;
		anncount++;
		if(anncount != time) {
			annst = setTimeout('announcementScrollnext(' + time + ')', 10);
		} else {
			annrowcount++;
			annst = setTimeout('announcementScroll()', anndelay);
		}
	}
	announcementScroll();
</script>
</#if>
<@comm.newpmmsgbox/>
<#-- 论坛板块列表 -->
<!--topic-->
<#assign lastforumlayer=-1 lastcolcount=1 lastforumid=0 subforumcount=0 />
<#list forumlist as forum>
	<#if forum.layer==0>
		<#if (lastforumlayer>-1)>
		<#if lastcolcount!=1>
			<#if subforumcount!=0>
			<#list 0..(lastcolcount-subforumcount) as x>
			<td>&nbsp;</td>
			</#list>
		</tr>
			</#if>
		</table>
</div>
		<#else>
		</table>
	</div>			
		</#if>
<div id="ad_intercat_${lastforumid}"></div>
	</#if>
	<#if forum.colcount==1>
<div class="mainbox forumlist">
	<span class="headactions">
		<#if forum.forumfields.moderators!="">
			分类版主: ${forum.forumfields.moderators}
		</#if><img id="category_${forum.fid}_img"  
		<#if forum.collapse!="">
		src="templates/${templatepath}/images/collapsed_yes.gif"
		<#else>
		src="templates/${templatepath}/images/collapsed_no.gif"
		</#if>
		 alt="展开/收起" onclick="toggle_collapse('category_${forum.fid}');"/>
	</span>
	<h3>
		<a href="showforum.action?forumid=${forum.fid}">${forum.name}</a>
	</h3>	
	<table id="category_${forum.fid}" summary="category_${forum.fid}" cellspacing="0" cellpadding="0"  style="${forum.collapse}">
	<thead class="category">
		<tr>
			<th>版块</th>
			<td class="nums">主题</td>
			<td class="nums">帖子</td>
			<td class="lastpost">最后发表</td>
		</tr>
	</thead>
	<#else>
	<#assign subforumcount=0 />
<div class="mainbox forumlist">
	<span class="headactions">
		<#if forum.forumfields.moderators!="">
			分类版主: ${forum.forumfields.moderators}
		</#if>
		<img id="category_${forum.fid}_img"
		<#if forum.collapse!="">
		src="templates/${templatepath}/images/collapsed_yes.gif"
		<#else>
		src="templates/${templatepath}/images/collapsed_no.gif"
		</#if>
		alt="展开/收起" onclick="toggle_collapse('category_${forum.fid}');"/>
	</span>
	<h3>
		<a href="showforum.action?forumid=${forum.fid}">${forum.name}</a>					
	</h3>
	<table id="category_${forum.fid}" summary="category_${forum.fid}" cellspacing="0" cellpadding="0"  style="${forum.collapse}">	
	</#if>
		<#assign lastforumlayer=0 />
		<#assign lastcolcount=forum.colcount />
		<#assign lastforumid=forum.fid/>
		<#else>
		<#if forum.colcount==1>
		<tbody id="forum${forum.fid}">
			<tr>
				<th <#if forum.havenew=="new" >class="new"</#if>>
				<#if forum.forumfields.icon!="">
					<img src="${forum.forumfields.icon}" border="0" align="left" hspace="5" alt="${forum.name}"/>
				</#if>
					<h2>
					<#if forum.forumfields.redirect=="">
						<a href="showforum.action?forumid=${forum.fid}">
					<#else>
						<a href="${forum.forumfields.redirect}" target="_blank">
					</#if>
					${forum.name}</a><#if (forum.todayposts>0)><em>(${forum.todayposts})</em></#if>
					</h2>
					<#if forum.forumfields.description!=""><p>${forum.forumfields.description}</p></#if>
					<#if forum.forumfields.moderators!=""><p class="moderators">版主: ${forum.forumfields.moderators}</p></#if>
				</th>
				<td class="nums"><#if forum.istrade!=1>${forum.topics_1}<#else>&nbsp;</#if></td>
				<td class="nums"><#if forum.istrade!=1>${forum.posts}<#else>&nbsp;</#if></td>
				<td class="lastpost">
				<#if forum.istrade!=1>
				<#if forum.status==-1>
					私密版块
				<#else>
					<#if forum.topics.tid!=0>
					<p>
						<a href="showtopic.action?topicid=${forum.topics.tid}">${forum.lasttitle}</a>
					</p>
					<div class="topicbackwriter">by
						<#if forum.lastposter!="">
							<#if forum.users.uid==-1>
								游客
							<#else>
								<a href="userinfo.action?userid=${forum.users.uid}" target="_blank">${forum.lastposter}</a>
							</#if>
						<#else>
							匿名
						</#if>						
						- <a href="showtopic.action?topicid=${forum.topics.tid}&page=end#lastpost" title="${forum.lastpost}"><span>${forum.lastpost}</span></a>
					</div>
					<#else>
						从未
					</#if>
				</#if>
				<#else>
				   <p>${forum.forumfields.description}</p>
				</#if>
				</td>
			</tr>
		</tbody>
	<#else>
	<#assign subforumcount=subforumcount+1 />
	<#assign colwidth = 99.9 / forum.colcount />
		<#if subforumcount==1>
		<tbody>
		<tr>
		</#if>
			<th style="width:{colwidth}%;"<#if forum.havenew=="new" >class="new"</#if>>
				<h2>
				<#if forum.forumfields.icon!="">
					<img src="${forum.forumfields.icon}" border="0" align="left" hspace="5" alt="${forum.name}"/>
				</#if>
				<#if forum.forumfields.redirect=="">
					<a href="showforum.action?forumid=${forum.fid}">
				<#else>
					<a href="${forum.forumfields.redirect}" target="_blank">
				</#if>
				${forum.name}</a>
				<#if (forum.todayposts>0)>
				<em>(${forum.todayposts})</em>
				</#if>
				</h2>
				<p><#if forum.istrade!=1>主题:${forum.topics_1}, 帖数:${forum.posts}</#if></p>
				<#if forum.istrade!=1>
				<#if forum.status==-1>
				<p>私密版块</p>
				<#else>
					<#if forum.topics.tid!=0>
						<p>最后: <a href="showtopic.action?topicid=${forum.topics.tid}&page=end#lastpost" title="${forum.lasttitle}"><span>${forum.lastpost}</span></a> by 
							<#if forum.lastposter!="">
								<#if forum.users.uid==-1>
									游客
								<#else>
									<a href="userinfo.action?userid=${forum.users.uid}" target="_blank">${forum.lastposter}</a>
								</#if>
							<#else>
								匿名
							</#if>
						</p>
					</#if>				
				</#if>
				<#else>
				  <p>${forum.forumfields.description}</p>
				</#if>
			</th>
<#if subforumcount==forum.colcount>
		</tr>
		</tbody>
	<#assign subforumcount=0 />
</#if>
</#if>
	<#assign lastforumlayer=1 />
	<#assign lastcolcount=forum.colcount />
</#if>
</#list>
<#if lastcolcount!=1 && subforumcount!=0>
		<#list 0..(lastcolcount-subforumcount) as x>
		<td>&nbsp;</td>
		</#list>
		</tr>
</#if>
	</table>
</div>
<!--end topic-->
<@comm.hottagbox />
<#if (forumlinkcount>0)>
<#-- 友情连接列表 -->
<div class="box">
	<span class="headactions"><img id="forumlinks_img" src="templates/${templatepath}/images/collapsed_no.gif" alt="" onclick="toggle_collapse('forumlinks');"/></span>
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

<#-- 在线用户列表 -->
<#if config.whosonlinestatus!=0 && config.whosonlinestatus!=2>
<div class="box" id="online">
	<span class="headactions">
		<#if !showforumonline>
			<a href="?showonline=yes#online"><img src="templates/${templatepath}/images/collapsed_yes.gif" alt="展开/收起" />
		<#else>
			<a href="?showonline=no#online"><img src="templates/${templatepath}/images/collapsed_no.gif" alt="展开/收起" />
		</#if></a>
	</span>
	<h4>
		<strong><a href="${forumurl}onlineuser.action">在线用户</a></strong>- <em>${totalonline}</em> 人在线 <#if showforumonline>- ${totalonlineuser} 会员<span id="invisible"></span>, ${totalonlineguest} 游客</#if>- 最高记录是 <em>${highestonlineusercount}</em> 于 <em>${highestonlineusertime}</em>
	</h4>
	<dl id="onlinelist">
		<dt>${onlineiconlist}</dt>
		<dd class="onlineusernumber">
			共<strong>${totalusers}</strong>位会员 <span class="newuser">新会员:
			<a href="userinfo.action?userid=${lastuserid}">${lastusername}</a></span>
		</dd>
		<dd>
			<ul class="userlist">
				<#if showforumonline>
				<#assign invisiblecount = 0 />
				<#list onlineuserlist as onlineuser>
					<#if onlineuser.invisible==1>
						<#assign invisiblecount = invisiblecount+1 />
				<li>(隐身会员)</li>
					<#else>
				<li>${onlineuser.olimg}
						<#if onlineuser.users.uid==-1>
							${onlineuser.username}
						<#else>
							<a href="userinfo.action?userid=${onlineuser.users.uid}" target="_blank" title="时间: ${onlineuser.lastupdatetime}
操作: ${onlineuser.actionname}
<#if onlineuser.forumname!="">
版块: ${onlineuser.forumname}
</#if>	">${onlineuser.username}</a>
						</#if>
				</li>
					</#if>
				</#list>
				<#if (invisiblecount>0)>
					<script type="text/javascript">$('invisible').innerHTML = '(${invisiblecount}' + " 隐身)";</script>
				</#if>
			<#else>
				<li style="width: auto"><a href="?showonline=yes#online">点击查看在线列表</a></li>
			</#if>
			</ul>
		</dd>
	</dl>
</div>
</#if>

<div class="legend">
	<label><img src="templates/${templatepath}/images/forum_new.gif" alt="有新帖的版块" />有新帖的版块</label>
	<label><img src="templates/${templatepath}/images/forum.gif" alt="无新帖的版块" />无新帖的版块</label>
</div>
<#if config.forumjump==1>
	${navhomemenu}
</#if>
</div>
<@comm.copyright/>
<@comm.footer/>