<#-- 
	描述：论坛帖子显示页模板
	作者：黄磊 
	版本：v1.0 
-->
<#import "inc/page_comm.ftl" as comm />
<@comm.header/>
<script type="text/javascript">
var templatepath = "${templatepath}";
var postminchars = parseInt(${config.minpostsize.toString()});
var postmaxchars = parseInt(${config.maxpostsize.toString()});
var disablepostctrl = parseInt(${disablepostctrl});
var forumpath = "";
</script>
<#if enabletag>
<script type="text/javascript" src="cache/tag/closedtags.txt"></script>
<script type="text/javascript" src="cache/tag/colorfultags.txt"></script>
</#if>
<script type="text/javascript" src="javascript/template_showtopic.js"></script>
<script type="text/javascript" src="javascript/ajax.js"></script>
<#assign loopi=1>
<#if reqcfg.page_err==0>
${topicmagic}
<div id="foruminfo">
	<div id="nav">
		<div class="userinfolist">
			<p><a id="forumlist" href="${config.forumurl}" 
			<#if config.forumjump==1>
			onmouseover="showMenu(this.id);" onmouseout="showMenu(this.id);"
			</#if>
			>${config.forumtitle}</a>  &raquo; ${forumnav}
			<#assign ishtmltitle = 0>
			<#if ishtmltitle==1>
				  &raquo; <strong>{Topics.GetHtmlTitle(topic.Tid)}</strong>
			<#else>
				  &raquo; <strong>${topictitle}</strong>
			</#if>
		</p>
		</div>
	</div>
	<div id="headsearch">
		<div id="search">
		<#if (usergroupinfo.allowsearch>0)>
			<@comm.quicksearch/>
		</#if>
		</div>
	</div>
</div>
<#if config.forumjump==1>
	${navhomemenu}
</#if>

<@comm.pagewordadlist/>
<@comm.newpmmsgbox/>
<@comm.poll/>

<div class="pages_btns">
	<div class="pages">
		<em>${pageid}/${pagecount}页</em>${pagenumbers}
		<kbd>跳转到<input name="gopage" type="text" id="gopage" onKeyDown="if(event.keyCode==13) { window.location='showtopic.action?topicid=${topicid}&page=' + (parseInt(this.value) > 0 ? parseInt(this.value) : 1) ;}"" size="4" maxlength="9"/>页</kbd><strong>查看:${topicviews}</strong>
	</div>

<#if userid<0||canposttopic>
	<span onmouseover="$('newspecial').id = 'newspecialtmp';this.id = 'newspecial';if($('newspecial_menu').childNodes.length>0)  showMenu(this.id);" id="newspecial" class="postbtn"><a title="发新话题" id="newtopic" href="posttopic.action?forumid=${forum.fid}" onmouseover="if($('newspecial_menu').childNodes.length>0)  showMenu(this.id);"><img alt="发新话题" src="templates/${templatepath}/images/newtopic.gif"/></a></span>
</#if>
<#if userid<0||canreply>
	<SPAN class="replybtn"><a href="postreply.action?topicid=${topicid}"><img src="templates/${templatepath}/images/reply.gif" border="0" alt="回复该主题" /></a></span>
</#if>
</div>

<div class="mainbox viewthread">
	<span class="headactions">
		<#if topic.special==0>
			<#if (topic.price>0) && topic.usersByPosterid.uid!=userid>
					  <a href="buytopic.action?topicid=${topicid}&showpayments=1">浏览需支付 ${userextcreditsinfo.name} ${topic.price} ${userextcreditsinfo.unit}</a>
			</#if>
		<#elseif topic.special==2>
			本帖悬赏 ${userextcreditsinfo.name} ${topic.price} ${userextcreditsinfo.unit}
		<#elseif topic.special==3>
			本帖的悬赏已经结束 <a href="{aspxrewriteurl}">查看结果页面</a>
		<#elseif topic.special==4>
		<a href="{ShowDebateAspxRewrite(topic.tid)}">辩论模式</a>
		</#if>
	</span>
	<h1>
		<#if forum.forumfields.applytopictype==1 && forum.topictypeprefix==1>
			${topictypes} 
		</#if>
			${topictitle}		
	</h1>
<#if (topic.moderated>0) && (config.moderactions>0)>
	<div class="navtopiccommend">${moderactions}</div>
</#if>
<form id="postsform" name="postsform" method="post" action="topicadmin.action?action=moderate&forumid=${forumid}">
	<input name="forumid" type="hidden" value="${forumid}" />
	<input name="topicid" type="hidden" value="${topicid}" />
	<input name="operat" type="hidden" value="delposts" />
	<div id="postsContainer">
<#list postlist as post>

	<#if post.id==2 && postleaderboardad!="">
		<div id="postleaderboardad">
			${postleaderboardad}
		</div>
	</#if>
	<table id="${post.pid}" summary="${post.pid}" cellspacing="0" cellpadding="0">
		<tbody>
		<tr>
		<td class="postauthor" id="${post.pid}">
			<#if post.id==postlist?size>
			<a name="lastpost"></a>
			</#if>
		<!--register user-->
				<#if post.posterid!=-1>
					<cite>
						<a href="userinfo.action?userid=${post.posterid}" target="_blank" id="memberinfo_${loopi}" class="dropmenu" onmouseover="showMenu(this.id)">
						<#if post.onlinestate==1>					
							<img src="templates/${templatepath}/images/useronline.gif" alt="在线" title="在线"/>
						<#else>
							<img src="templates/${templatepath}/images/useroutline.gif"  alt="离线" title="离线"/>
						</#if>						
						${post.poster}
						</a>
					</cite>
					<div class="avatar">
						    <#if config.showavatars==1>
							<#if post.avatar!="">
							<img src="${post.avatar}" onerror="this.onerror=null;this.src='templates/${templatepath}/images/noavatar.gif';" 
							<#if (post.avatarwidth>0)>width="${post.avatarwidth}" height="${post.avatarheight}"</#if> alt="头像"/>			
						</#if>
							</#if>
					</div>
					<#if post.nickname!="">
						<p><em>${post.nickname}</em></p>
					</#if>
					<p>
						<script type="text/javascript">
							ShowStars(${post.stars}, ${config.starthreshold});
						</script>
					</p>
					
					<ul class="otherinfo">
						<#if config.userstatusby==1>
						<li>组别:${post.status}</li>
						</#if>
						<li>性别:<script type="text/javascript">document.write(displayGender(${post.gender}));</script></li>
						<#if post.bday!="">
						<li>生日:${post.bday}</li>
						</#if>
						<li>积分:${post.credits}</li>
						<li>帖子:${post.posts}</li>
					</ul>
					<#if post.medals!="">
						<div class="medals">${post.medals}</div>
					</#if>
				<#else>
						<div class="ipshow"><strong>${post.poster}</strong>  ${post.ip}
							<#if (useradminid>0) && admininfo.allowviewip==1>
								<a href="getip.action?pid=${post.pid}&topicid=${topicid}" title="查看IP"><img src="templates/${templatepath}/images/ip.gif" alt="查看IP"/></a>
							</#if>
						</div>
					<!--guest-->
						<div class="noregediter">
							未注册
						</div>
				</#if>	
		</td>
		<td class="postcontent">
			<div class="postinfo">
				<#if topic.special==4>
					<#if post.debateopinion==1>
						<em>正方</em>
					<#elseif post.debateopinion==2>
						<em>反方</em>
					<#else>
					<em>辩论主题</em>
					</#if>
				 </#if>

				<em>${post.postdatetime}</em>
			<#if post.posterid!=-1>
				
			<#if onlyauthor=="1">
				<a href="showtopic.action?topicid=${topicid}">显示全部</a>  
			<#else>
				<#if topic.usersByPosterid.uid==post.posterid>
				<a href="showtopic.action?topicid=${topicid}&onlyauthor=1"><em>| 只看楼主</em></a>  
				</#if>
			</#if>
				
			</#if>
				<a href="showtree.action?topicid=${topicid}&postid=${post.pid}">树型</a>|
				<a href="favorites.action?topicid=${topicid}">收藏</a>|
			<#if ismoder==1>
				<#if topic.special==4>
				<a href="editpost.action?topicid=${topicid}&postid=${post.pid}&pageid=${pageid}&debate=${post.debateopinion}">编辑</a>|
				<#else>
				<a href="editpost.action?topicid=${topicid}&postid=${post.pid}&pageid=${pageid}">编辑</a>|
				 </#if>
				<a href="delpost.action?topicid=${topicid}&postid=${post.pid}" onclick="return confirm('确定要删除吗?');">删除</a>|
			<#if post.id==1 && topic.special==2>
				<#if (topic.replies>0)>
				<a href="###" onclick="action_onchange('bonus',$('moderate'),'${topic.tid}');">结帖</a></#if>|
				</#if>
				<#if post.posterid!=-1>
				<a href="###" onclick="action_onchange('rate',$('moderate'),'${post.pid}');">评分</a>|
				<#if (post.ratetimes>0)>
				<a href="###" onclick="action_onchange('cancelrate',$('moderate'),'${post.pid}');">撤销评分</a>|
				</#if>
				</#if>
				<#if post.invisible==-2>
					<a href="###" onclick="action_onchange('banpost',$('moderate'),'${post.pid}','0');">撤销屏蔽</a>|
				<#else>
					<a href="###" onclick="action_onchange('banpost',$('moderate'),'${post.pid}','-2');">屏蔽帖子</a>|
				</#if>
				<input name="postid" id="postid" value="${post.pid}" type="checkbox"/>
			<#else>
				<#if post.posterid!=-1 && userid==post.posterid>
					<#if topic.closed==0>
						<#if topic.special==4>
						<a href="editpost.action?topicid=${topicid}&postid=${post.pid}&pageid=${pageid}&debate=${post.debateopinion}">编辑</a>|
						<#else>
						<a href="editpost.action?topicid=${topicid}&postid=${post.pid}&pageid=${pageid}">编辑</a>|
						 </#if>
					</#if>
					<a href="delpost.action?topicid=${topicid}&postid=${post.pid}" onclick="return confirm('确定要删除吗?');">删除</a>|
					<#if post.id==1 && topic.special==2>
						<#if (topic.replies>0)>
						<a href="###" onclick="action_onchange('bonus',$('moderate'),'${topic.tid}');">结帖</a>|
						</#if>
						</#if>
					</#if>
					<#if usergroupinfo.raterange!="" && post.posterid!=-1><a href="###" onclick="action_onchange('rate',$('moderate'),'${post.pid}');">评分</a>
					</#if>
				</#if>
					<a href="###" class="t_number" onclick="$('message${post.pid}').className='t_smallfont'">小</a>			
					<a href="###" class="t_number" onclick="$('message${post.pid}').className='t_msgfont'">中</a>
					<a href="###" class="t_number" onclick="$('message${post.pid}').className='t_bigfont'">大</a>
					<a href="###" class="bold" title="复制帖子链接到剪贴板" onclick="setcopy(window.location.toString().replace(/#(.*?)$/ig, '') + '#${post.pid}', '已经复制到剪贴板')">${post.id}<sup>#</sup></a>
			</div>	
			<div id="ad_thread2_${post.id}"></div>		
			<div class="postmessage defaultpost">			
			<!--单贴屏蔽判断-->
			<#if post.invisible!=-2 || ismoder==1>
				<#if (topic.identify>0) && post.id==1>
				<div class="ntprint" onclick="this.style.display='none';"><img onload="setIdentify(this.parentNode);" src="images/identify/${topicidentify.filename}" alt="点击关闭鉴定图章" title="点击关闭鉴定图章" /></div>
				</#if>				 
					<#if ismoder==1 && post.invisible==-2>
						<!--管理组有权查看内容开始-->
						<div class='hintinfo'>提示: 该帖已被屏蔽, 您拥有管理权限, 以下是帖子内容</div>		              
					</#if>
				<h2>${post.title}</h2>
				<div id="message${post.pid}" class="t_msgfont">
					<div id="ad_thread3_${post.id}"></div>
					<#if post.id==1>
						<div id="firstpost">
					</#if>
						${post.message}
					<#if post.id==1>
						</div>							
					</#if>
				</div>	
				<#if (attachmentlist?size>0)>
					<#assign currentattachcount = 0>
					<#list attachmentlist as attachtemp>
						<#if attachtemp.postid.pid==post.pid>
							<#assign currentattachcount = currentattachcount + 1>
						</#if>
					</#list>
					<#if  (currentattachcount>0)>
						<#assign getattachperm = attachmentlist.get(0).getattachperm>
						<#if getattachperm==1>
					<div class="box postattachlist">
						<h4>附件</h4>
					<#list attachmentlist as attachment>
						<#if attachment.postid.pid==post.pid>
							<!--附件开始-->
								<#if attachment.allowread==1>
								<#include "inc/commfiles/_attachmentinfo.ftl">
								<#else>
								<span class="notdown">你的下载权限 ${usergroupinfo.readaccess} 低于此附件所需权限 ${attachment.readperm}, 你无权查看此附件</span>
								</#if>
							<!--附件结束-->
						</#if>
					</#list>
					</div>
						<#else>
					<div class="notice" style="width: 500px;">
					附件:<em>您所在的用户组无法下载或查看附件</em>
					</div>
						</#if>
					</#if>
				</#if>
				<#if (post.ratetimes>0)>
					 <#include "inc/commfiles/_ratelog.ftl">
				</#if>
				<#if post.lastedit!="">
				<!--最后编辑开始-->
				<div class="lastediter"><img src="templates/${templatepath}/images/lastedit.gif" alt="最后编辑"/>${post.lastedit}</div>
				<!--最后编辑结束-->
				</#if>
				<#if enabletag && post.id==1>				
					<script type="text/javascript">
						function forumhottag_callback(data)
						{
							tags = data;
						}
					</script>
					<script type="text/javascript" src="cache/hottags_forum_cache_jsonp.txt"></script>
					<div id="topictag">
						<#assign hastag = 0>
						<#if hastag==1>
							<script type="text/javascript">getTopicTags(${topic.tid});</script>
						<#else>
							<script type="text/javascript">parsetag();</script>
						</#if>
					</div>
				</#if>
				<#if post.id==1>
					<#if topic.special==3>
						<div class="quote">
							<div class="text"><p>本帖得分:</p>
								<div class="attachmentinfo">
									<#list bonuslogs as bonuslog>
										<#assign aspxrewriteurl = "userinfo.action?uid="+bonuslog.usersByAnswerid.uid>
										<#assign unit = scoreunit[bonuslog.extid]>
										<#assign name = score[bonuslog.extid]>
										<a href="${aspxrewriteurl}">${bonuslog.answername}</a>(${bonuslog.bonus} ${unit}${name})
										<#if item_index!=bonuslogs.size>
											,
										</#if>
									</#list>					
								</div>
							</div>
						</div>
				<#elseif topic.special==4>
					<br />
					<div class="msgheader">正方论点</div>
					<div class="msgborder">${debateexpand.positiveopinion}</div>
					<br />
					<div class="msgheader">反方论点</div>
					<div class="msgborder">${debateexpand.negativeopinion}</div>
				</#if>
			</#if>
			</div>
			<#else>
			<div id="message${post.pid}" class="t_msgfont">
						<div id="ad_thread3_${post.id}"></div>
			          <div class='hintinfo'>提示: 该帖被管理员或版主屏蔽</div>
			</div>
			</#if>
				<#if config.showsignatures==1>
				<#if post.usesig==1>
					<#if post.signature!="">
				<!--签名开始-->
					<div class="postertext">
						<#if (config.maxsigrows>0)>
							<#assign ieheight = config.maxsigrows*19>
							<#assign heightem = config.maxsigrows*1.6 >
							<div class="signatures" style="max-height: ${heightem}em;maxHeightIE:${ieheight}px">${post.signature}</div>
						<#else>
							${post.signature}
						</#if>
					</div>
				<!--签名结束-->
					</#if>
				</#if>
			</#if>
	</td>
	</tr>
	</tbody>
	<tbody>
	<tr>
		<td class="postauthor">&nbsp;</td>
		<td class="postcontent">
			<div class="postactions">
				<p>
				<#if userid!=-1>
				<@comm.report/>|
				</#if>
				<#if canreply>
					<a href="postreply.action?topicid=${topicid}&postid=${post.pid}&quote=yes">引用</a>|
					<#if userid!=-1>
					<a href="###" onclick="replyToFloor('${post.id}', '${post.poster}', '${post.pid}')">回复</a>|
					</#if>
				</#if>
					<a href="###" onclick="window.scrollTo(0,0)">TOP</a>
				</p>
				<div id="ad_thread1_${post.id}"></div>
			</div>
		</td>
	</tr>
	</tbody>
	</table>
	<div class="threadline">&nbsp;</div>
<#if post.posterid!=-1>
	<!-- member menu -->
<div class="popupmenu_popup userinfopanel" id="memberinfo_${loopi}_menu" style="display: none; z-index: 50; filter: progid:dximagetransform.microsoft.shadow(direction=135,color=#cccccc,strength=2); left: 19px; clip: rect(auto auto auto auto); position absolute; top: 253px; width:150px;" initialized ctrlkey="userinfo2" h="209">
	<p class="recivemessage"><a href="usercppostpm.action?msgtoid=${post.posterid}" target="_blank">发送短消息</a></p>
	<#if (useradminid>0)>
	<#if admininfo.allowviewip==1>
	<p  class="seeip"><a href="getip.action?pid=${post.pid}&topicid=${topicid}" title="查看IP">查看IP</a></p>
	</#if>
	<#if admininfo.allowbanuser==1>
	<p><a href="useradmin.action?action=banuser&uid=${post.posterid}" title="禁止用户">禁止用户</a></p>
	</#if>
	</#if>
	<p>
		<#assign aspxrewriteurl = "userinfo.action?userid="+post.posterid>
	<a href="${aspxrewriteurl}" target="_blank">查看公共资料</a>
	</p>
	<p><a href="search.action?posterid=${post.posterid}">查找该会员全部帖子</a></p>
	<ul>
		<li>UID:${post.posterid}</li>
		<li>精华:<#if (post.digestposts>0)><a href="search.action?posterid=${post.posterid}&type=digest">${post.digestposts}</a><#else>${post.digestposts}</#if></li>
		<#if score[1]!="">
			<li>${score[1]}:${post.extcredits1} ${scoreunit[1]}</li>
		</#if>
		<#if score[2]!="">
			<li>${score[2]}:${post.extcredits2} ${scoreunit[2]}</li>
		</#if>
		<#if score[3]!="">
			<li>${score[3]}:${post.extcredits3} ${scoreunit[3]}</li>
		</#if>
		<#if score[4]!="">
			<li>${score[4]}:${post.extcredits4} ${scoreunit[4]}</li>
		</#if>
		<#if score[5]!="">
			<li>${score[5]}:${post.extcredits5} ${scoreunit[5]}</li>
		</#if>
		<#if score[6]!="">
			<li>${score[6]}:${post.extcredits6} ${scoreunit[6]}</li>
		</#if>
		<#if score[7]!="">
			<li>${score[7]}:${post.extcredits7} ${scoreunit[7]}</li>
		</#if>
		<#if score[8]!="">
			<li>${score[8]}:${post.extcredits8} ${scoreunit[8]}</li>
		</#if>
			<li>来自:${post.location}</li>
			<li>注册:<#if post.joindate!="">${post.joindate}</#if></li>	
	</ul>
	<p>状态:<span><#if post.onlinestate==1>
		在线
		<#else>
		离线
		</#if></span>
	</p>
	<ul class="tools">
		<#if post.msn!="">
		<li>
			<img src="templates/${templatepath}/images/msnchat.gif" alt="MSN Messenger: ${post.msn}"/>
			<a href="mailto:${post.msn}" target="_blank">${post.msn}</a>
		</li>
		</#if>
		<#if post.skype!="">
		<li>
			<img src="templates/${templatepath}/images/skype.gif" alt="Skype: ${post.skype}"/>
			<a href="skype:${post.skype}" target="_blank">${post.skype}</a>
		</li>
		</#if>
		<#if post.icq!="">
		<li>
			<img src="templates/${templatepath}/images/icq.gif" alt="ICQ: ${post.icq}" />
			<a href="http://wwp.icq.com/scripts/search.dll?to=${post.icq}" target="_blank">${post.icq}</a>
		</li>
		</#if>
		<#if post.qq!="">
		<li>
			<img src="templates/${templatepath}/images/qq.gif" alt="QQ: ${post.qq}"/>
			<a href="http://wpa.qq.com/msgrd?V=1&Uin=${post.qq}&Site=${config.forumtitle}&Menu=yes" target="_blank">${post.qq}</a>
		</li>
		</#if>
		<#if post.yahoo!="">
		<li>
			<img src="templates/${templatepath}/images/yahoo.gif" width="16" alt="Yahoo Messenger: ${post.yahoo}"/>
			<a href="http://edit.yahoo.com/config/send_webmesg?.target=${post.yahoo}&.src=pg" target="_blank">${post.yahoo}</a>
		</li>
		</#if>
	</ul>
</div>
<!-- member menu -->
</#if>
<#assign loopi=loopi+1>
</#list>
</div>
</form>
<div class="navnextpage">
	&lt;&lt;<a href="showtopic.action?forumid=${forumid}&topicid=${topicid}&go=prev">上一主题</a>|<a href="showtopic.action?forumid=${forumid}&topicid=${topicid}&go=next">下一主题</a>&gt;&gt;
</div>
</div>
<!--ntforumbox end-->
<div class="pages_btns">
	<div class="pages">
		<em>${pageid}/${pagecount}页</em>${pagenumbers}
		<kbd>跳转到<input name="gopage" type="text" id="gopage" onKeyDown="if(event.keyCode==13) { window.location='showtopic.action?topicid=${topicid}&page=' + (parseInt(this.value) > 0 ? parseInt(this.value) : 1) ;}"" size="4" maxlength="9"/>页</kbd>
	</div>
	<#if userid<0||canposttopic>
	<span class="postbtn" id="newspecialtmp" onmouseover="$('newspecial').id = 'newspecialtmp';this.id = 'newspecial';if($('newspecial_menu').childNodes.length>0)  showMenu(this.id);"><a href="posttopic.action?forumid=${forumid}"><img src="templates/${templatepath}/images/newtopic.gif""  alt="发表新主题" /></a></span>
	</#if>
	<#if userid<0||canreply>
	<span class="replybtn"><a href="postreply.action?topicid=${topicid}"><img src="templates/${templatepath}/images/reply.gif" alt="回复该主题" /></a></span>
	</#if>
</div>

<#if enabletag && (relatedtopics?size>0)>
<div class="mainbox">
	<h3>相关主题</h3>
	<table cellpadding="0" cellspacing="0" border="0">
		<tbody>
		<#list relatedtopics as rtopic>
		<#assign aspxrewriteurl = "showtopic.action?topicid="+rtopic.tid>
		<tr>
			<td><a href="${aspxrewriteurl}" target="_blank">${rtopic.title}</td>
		</tr>
		</#list>
	</table>
</div>
</#if>

<#if canreply && userid!=-1>
<!--quickreply-->
	<!--快速回复主题,将_ajaxquickreply替换成_quickreply可变为传统form提交方式-->
		<@comm.ajaxquickreply/>
<!--quickreply-->	
</#if>

<#if userid<0||canposttopic>
	<ul class="popupmenu_popup newspecialmenu" id="newspecial_menu" style="display: none">
	 <#if forum.allowspecialonly<=0>
		<li><a href="posttopic.action?forumid=${forum.fid}">发新主题</a></li>
		</#if>
		<#assign specialpost = reqcfg.opNum(forum.allowpostspecial,1) >
		<#if specialpost==1 && usergroupinfo.allowpostpoll==1>
		<li class="poll"><a href="posttopic.action?forumid=${forum.fid}&type=poll">发布投票</a></li>
		</#if>
		<#assign specialpost = reqcfg.opNum(forum.allowpostspecial,4) >
		<#if specialpost==4 && usergroupinfo.allowbonus==1>
			<li class="reward"><a href="posttopic.action?forumid=${forum.fid}&type=bonus">发布悬赏</a></li>
		</#if>
		<#assign specialpost = reqcfg.opNum(forum.allowpostspecial,16) >
		<#if specialpost==16 && usergroupinfo.allowdebate==1>
			<li class="debate"><a href="posttopic.action?forumid=${forum.fid}&type=debate">发起辩论</a></li>
		</#if>
	</ul>
</#if>

<#if (useradminid>0)||usergroupinfo.raterange!=""||config.forumjump==1||(topic.special==2&&topic.usersByPosterid.uid==userid)>
<!--forumjumping start-->
<div id="footfilter" class="box">
	<script type="text/javascript">
		function action_onchange(value,objfrm,postid,banstatus){
			if (value != ''){
				objfrm.operat.value = value;
				objfrm.postid.value = postid;
				if (value != "delete")
				{
					objfrm.action = objfrm.action + '&referer=' + escape(window.location);
				}
				if (value == 'banpost' && typeof(banstatus) != "undefined")
				{
					objfrm.operat.value = value;
					objfrm.action = objfrm.action + "&banstatus=" + banstatus;
					objfrm.submit();
					return;
				}
				if(value == 'delposts' || value == 'banpost'){
					$('postsform').operat.value = value; 
					$('postsform').action = $('postsform').action + '&referer=' + escape(window.location);
					$('postsform').submit();
				}
				else{
					objfrm.submit();
				}
			}
		}
	</script>

	<#assign canuseadminfunc = usergroupinfo.raterange!="" || (usergroupinfo.maxprice>0) || (topic.special==2&&topic.usersByPosterid.uid==userid)>
	<#if (useradminid>0)>
		<form id="moderate" name="moderate" method="post" action="topicadmin.action?action=moderate&forumid=${forumid}">
			<input name="forumid" type="hidden" value="${forumid}" />
			<input name="topicid" type="hidden" value="${topicid}" />
			<input name="postid" type="hidden" value="" />
			<input name="operat" type="hidden" value="" />
			<select id="operatSel" onchange="action_onchange(this.options[this.selectedIndex].value,this.form,0);"
				name="operatSel">
				<option value="" selected="selected">管理选项</option>
				<option value="delete">删除主题</option>
				<option value="banpost">屏蔽单贴</option>
				<option value="bump">提沉主题</option>
				<option value="delposts">批量删帖</option>
				<option value="close">关闭主题</option>
				<option value="move">移动主题</option>
				<option value="copy">复制主题</option>
				<option value="highlight">高亮显示</option>
				<option value="digest">设置精华</option>
				<option value="identify">鉴定主题</option>
				<option value="displayorder">主题置顶</option>
				<option value="split">分割主题</option>
				<option value="merge">合并主题</option>
				<option value="repair">修复主题</option>
			</select>

		</form>
	<#elseif canuseadminfunc>
		<form id="moderate" name="moderate" method="post" action="topicadmin.action?action=moderate&forumid=${forumid}">
			<input name="forumid" type="hidden" value="${forumid}" />
			<input name="topicid" type="hidden" value="${topicid}" />
			<input name="postid" type="hidden" value="" />
			<input name="operat" type="hidden" value="" />
		</form>
	</#if>
	<#if config.forumjump==1>
	<select onchange="if(this.options[this.selectedIndex].value != '') {window.location='showforum.action?forumid='+this.options[this.selectedIndex].value;}">
		  <option>论坛跳转...</option>
		${forumlistboxoptions}
	</select>
	<script type="text/javascript">		
	function jumpurl(fid, aspxrewrite, extname) {
		for(var i in categorydata) {
		   if(categorydata[i].fid == fid) {
			   if(aspxrewrite) {
				   window.location='showgoodslist-' +categorydata[i].categoryid + extname;
			   }
			   else {
				   window.location='showgoodslist.action?categoryid=' +categorydata[i].categoryid;
			   }
			   return;
		   } 
		}
		
		if(aspxrewrite) {
			window.location='showforum-' + fid + extname;
		}
		else {
			window.location='showforum.action?forumid=' + fid ;
		}
	}
	</script>
	</#if>
</div>
<!--forumjumping end-->
</#if>
</div>
<#else>
	<#if needlogin>
		<@comm.login/>
	<#else>
		<@comm.errmsgbox/>
	</#if>
</#if>
${inpostad}
<@comm.copyright/>
<@comm.adlist/>
<@comm.footer/>
