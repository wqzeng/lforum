<#-- 
	描述：论坛板块显示页模板
	作者：黄磊 
	版本：v1.0 
-->
<#import "inc/page_comm.ftl" as comm />
<@comm.header/>
<script type="text/javascript" src="javascript/ajax.js"></script>
<#if reqcfg.page_err==0>
	<script type="text/javascript">
	var templatepath = "${templatepath}";
	var fid = parseInt(${forum.fid});
	var postminchars = parseInt(${config.minpostsize});
	var postmaxchars = parseInt(${config.maxpostsize});
	var disablepostctrl = parseInt(${disablepostctrl});
	</script>
</#if>
<script type="text/javascript" src="javascript/template_showforum.js"></script>
<div id="foruminfo">
	<div id="headsearch">
		<div id="search">
			<#if (usergroupinfo.allowsearch>0)>
			<@comm.quicksearch/>
			</#if>
		</div>
		<#if reqcfg.page_err==0>
		<p>
			<a href="showtopiclist.action?type=digest&amp;forums=${forum.fid}">精华帖区</a>
			<#if config.rssstatus!=0>		
			<a href="tools/rss.action?forumid=${forum.fid}" target="_blank"><img src="templates/${templatepath}/images/rss.gif" alt="Rss"/></a>
			</#if>
		</p>
		</#if>
	</div>
	<div id="nav">
		<div class="userinfolist">
			<p><a id="forumlist" href="${config.forumurl}" <#if config.forumjump==1>onmouseover="showMenu(this.id);" onmouseout="showMenu(this.id);"</#if>
			>${config.forumtitle}</a> &raquo; ${forumnav} </p>
			<#if reqcfg.page_err==0>
			<p> 版主: 
			<em>
			<#if forum.forumfields.moderators!="">
				${forum.forumfields.moderators}
			<#else>
				*空缺中*
			</#if>
			</em>
			</p>
			</#if>
		</div>
	</div>
</div>
<#if reqcfg.page_err==0>
<#if config.forumjump==1>
	${navhomemenu?default("")}
</#if>
<#if showforumlogin==1>
	<div class="mainbox formbox">
		<h3>本版块已经被管理员设置了密码</h3>
		<form id="forumlogin" name="forumlogin" method="post" action="">
			<table cellpadding="0" cellspacing="0" border="0">
				<tbody>
				<tr>
					<th>请输入密码:</th>
					<td><input name="forumpassword" type="password" id="forumpassword" size="20"/></td>
				</tr>
				</tbody>
			<#if isseccode>	
				<tbody>
				<tr>
					<th>输入验证码:</th>
					<td><@comm.vcode/></td>
				</tr>
				</tbody>
			</#if>
				<tbody>
				<tr>
					<th>&nbsp;</th>
					<td>
						<input type="submit"  value="确定"/>
					</td>
				</tr>
				</tbody>
			</table>
		</form>
	</div>
<#else>

<@comm.pagewordadlist/>

<#if forum.forumfields.rules!="">
<table class="portalbox" cellspacing="1" cellpadding="0" summary="rules and recommend">
<tbody>
	<tr>
	<td id="rules">
		<span class="headactions recommendrules"><img id="rules_img" title="收起" onclick="$('rules_link').style.display = '';toggle_collapse('rules', 1);" alt="收起" src="templates/${templatepath}/images/collapsed_no.gif" /></span> <h3>本版规则</h3><div id="rules_link">${forum.forumfields.rules}</div>
	</td>
	</tr>
</tbody>
</table>
</#if>

<@comm.newpmmsgbox/>

<#if (subforumcount>0)>
	<@comm.subforum/>
</#if>

<#if forum.layer!=0>
<div class="pages_btns">
	<div class="pages">
		<em>${pageid}/${pagecount}页</em>${pagenumbers}
		<kbd>跳转<input name="gopage" type="text" id="gopage" onKeyDown="if(event.keyCode==13) { window.location='showforum.action?forumid=${forumid}&page=' + (parseInt(this.value) > 0 ? parseInt(this.value) : 1)}" size="4" maxlength="9" />页</kbd>
	</div>
<#if (userid<0)||canposttopic>
	<span onmouseover="$('newspecial').id = 'newspecialtmp';this.id = 'newspecial';if($('newspecial_menu').childNodes.length>0)  showMenu(this.id);" id="newspecial" class="postbtn"><a title="发新话题" id="newtopic" href="posttopic.action?forumid=${forum.fid}" onmouseover="if($('newspecial_menu').childNodes.length>0)  showMenu(this.id);"><img alt="发新话题" src="templates/${templatepath}/images/newtopic.gif"/></a></span>
</#if>
</div>
<div id="headfilter">
	<ul class="tabs">
	    <li <#if filter=="">class="current"</#if> ><a href="showforum.action?forumid=${forumid}">全部</a></li>
		<li <#if filter=="digest">class="current"</#if> ><a href="showforum.action?forumid=${forumid}&filter=digest">精华</a></li>
		<#assign specialpost=reqcfg.opNum(forum.allowpostspecial,1)/>
		<#if specialpost==1>
		<li <#if filter=="poll">class="current"</#if> ><a href="showforum.action?forumid=${forumid}&filter=poll">投票</a></li>
		</#if>
		<#assign specialpost=reqcfg.opNum(forum.allowpostspecial,4)/>
	    <#if specialpost==4>
		<li id="rewardmenu" class="<#if filter=="reward" || filter=="rewarding">current<#else><#if filter=="rewarded">current</#if></#if> dropmenu" onMouseOver="showMenu(this.id);"><a href="showforum.action?forumid=${forumid}&filter=reward">悬赏</a></li>
		</#if>
		<#assign specialpost=reqcfg.opNum(forum.allowpostspecial,16)/>
	    <#if specialpost==16>
		<li <#if filter=="debate">class="current"</#if> ><a href="showforum.action?forumid=${forumid}&filter=debate">辩论</a></li>
		</#if>
	</ul>
</div>

<ul class="popupmenu_popup headermenu_popup" id="rewardmenu_menu" style="display: none">
	<li><a href="showforum.action?forumid=${forumid}&filter=rewarding">进行中的悬赏</a></li>
	<li><a href="showforum.action?forumid=${forumid}&filter=rewarded">已结束的悬赏</a></li>
</ul>

<form id="moderate" name="moderate" method="post" action="topicadmin.action?action=moderate&forumid=${forumid}">
<div class="mainbox threadlist">
	<span class="headactions">
	<#if forum.forumfields.applytopictype==1 && forum.viewbytopictype==1>
		${topictypeselectlink}
	</#if>
	</span>
	<h1>
		<a href="showforum.action?forumid=${forumid}">${forum.name}</a>
	</h1>
	<table summary="${forum.fid}" id="${forum.fid}" cellspacing="0" cellpadding="0">
		<thead class="category">
			<tr>
				<td class="folder">&nbsp;</td>
				<td class="icon">&nbsp;</td>
				<th>标题</th>
				<td class="author">作者</td>
				<td class="nums">回复/查看</td>
				<td class="lastpost">最后发表</td>
			</tr>
		</thead>
		<!--announcement start-->
		<#if announcementlist?exists>
		<#list announcementlist as announcement>
		<tbody>
		<tr>
			<td><img src="templates/${templatepath}/images/announcement.gif" alt="announcement" /></td>
			<td>&nbsp;</td>
			<th>
				<a href="announcement.action#${announcement.id}">${announcement.title}</a>
			</th>
			<td>
				<cite><#if announcement.posterid==-1>
					游客
				<#else>
					<a href="userinfo.action?userid=${announcement.posterid}>${announcement.poster}</a>
				</#if>
				</cite>
			</td>
			<td>-</td>
			<td>-</td>
		</tr>
		</tbody>
		</#list>
		</#if>
		<!--announcement end-->
		<!--NtForumList start-->
		<#if toptopiclist?exists>
		<#list toptopiclist as toptopic>
		<tbody>
			<tr>
				<td class="folder">
					<#assign aspxrewriteurl = "showtopic.action?topicid="+toptopic.tid>
					<a href="${aspxrewriteurl}" target="_blank"><img src="templates/${templatepath}/images/t_top${toptopic.displayorder}.gif"/></a>
				</td>
				<td class="icon">
					<#if toptopic.special==0>
						<#if toptopic.iconid!=0>
								<img src="images/posticons/${toptopic.iconid}.gif" alt="listicon" />
						<#else>
								&nbsp;
						</#if>
					</#if>
					<#-- 特殊贴图标 -->
					<#if toptopic.special==1>
						<img src="templates/${templatepath}/images/pollsmall.gif" alt="投票" />
					</#if>
					<#if toptopic.special==2>
						<img src="templates/${templatepath}/images/bonus.gif" alt="悬赏"/>
					</#if>
					<#if toptopic.special==3>
						<img src="templates/${templatepath}/images/rewardsmallend.gif" alt="悬赏已结束"/>
					</#if>
					<#if toptopic.special==4>
						<img src="templates/${templatepath}/images/debatesmall.gif" alt="辩论"/>
					</#if>
				</td>
				<th class="common">
					<label>
					<#if (toptopic.digest>0)>
						<img src="templates/${templatepath}/images/digest${toptopic.digest}.gif" alt="digtest"/>
					</#if>
					<#if (toptopic.attachment>0)>
						<img src="templates/${templatepath}/images/attachment.gif" alt="附件"/>
					</#if>
					<#if (toptopic.rate>0)>
						<img src="templates/${templatepath}/images/agree.gif" alt="正分"/>
					</#if>
					<#if toptopic.rate<0>
						<img src="templates/${templatepath}/images/disagree.gif" alt="负分"/>
					</#if>
					</label>
					<#if (useradminid>0) && ismoder>
						<#if toptopic.forums.fid==forum.fid>
						<input type="checkbox" name="topicid" value="${toptopic.tid}" />
						<#else>
						<input type="checkbox" disabled />
						</#if>				
					</#if>
					<#if (toptopic.replies>0)>
						<img src="templates/${templatepath}/images/topItem_exp.gif" id="imgButton_${toptopic.tid}" onclick="showtree(${toptopic.tid},${ppp},0);" class="cursor" alt="展开帖子列表" title="展开帖子列表" />
					<#else>
						<img src="templates/${templatepath}/images/no-sublist.gif" id="imgButton_${toptopic.tid}" alt="闭合帖子列表"/>
					</#if>
					<#if forum.forumfields.applytopictype==1 && forum.topictypeprefix==1>
							<em>
							<#if forum.viewbytopictype==1 && toptopic.topictypename!="">
							[<a href="showforum.action?forumid=${toptopic.fid}&typeid=${toptopic.typeid}" >${toptopic.topictypename}</a>]
							<#elseif toptopic.topictypename!="">
							[${toptopic.topictypename}]
							</#if>
							</em>
					</#if>
					<#if toptopic.special==3>
						<#assign aspxrewriteurl = "showbonus.action?topicid="+toptopic.tid>
					</#if>
					<#if toptopic.special==4>
						<#assign aspxrewriteurl = "showdebate.action?topicid="+toptopic.tid>
					</#if>
					<#assign ishtmltitle=topicManager.getMagicValue(toptopic.magic, 1)/>
					<#if ishtmltitle==1>
						<a href="${aspxrewriteurl}">${topicManager.getHtmlTitle(toptopic.tid)}</a>
					<#else>
						<a href="${aspxrewriteurl}">${toptopic.title}</a>
					</#if>
					<#if toptopic.special==2>
						- [悬赏 ${userextcreditsinfo.name} <span class="bold">${toptopic.price}</span> ${userextcreditsinfo.unit}] 
					<#elseif toptopic.special==3>
						- [悬赏已结束]
					<#elseif toptopic.special==0>
						<#if (toptopic.price>0)>
							- [售价 ${userextcreditsinfo.name} <span class="bold">${toptopic.price}</span> ${userextcreditsinfo.unit}] 
						</#if>
					</#if>					
					<#if (toptopic.readperm>0)>
						- [阅读权限 <span class="bold">${toptopic.readperm}</span>] 
					</#if>
					<#if (toptopic.replies/ppp>0)>					
							<span class="threadpages"><script type="text/javascript">getpagenumbers("",${toptopic.replies},${ppp},0,"",${toptopic.tid});</script></span>				
					</#if>
				</th>
				<td class="author">
					<cite>
					<#if toptopic.usersByPosterid.uid==-1>
						游客
					<#else>
						<a href="userinfo.action?userid=${toptopic.usersByPosterid.uid}">${toptopic.poster}</a>
					</#if></cite>
					<em>${toptopic.postdatetime}</em>
				</td>
				<td class="nums"><strong>${toptopic.replies}</strong> / <em>${toptopic.views}</em></td>
				<td class="lastpost">
					<em><a href="showtopic.action?topicid=${toptopic.tid}&page=end#lastpost">${toptopic.lastpost}</a></em>
					<cite>by
						<#if toptopic.usersByLastposterid.uid==-1>
							游客
						<#else>
							<a href="userinfo.action?userid=${toptopic.usersByLastposterid.uid}" target="_blank">${toptopic.lastposter}</a>
						</#if>
					</cite>
				</td>
			</tr>
			<tr><td colspan="7" id="divTopic${toptopic.tid}" style="border:0; padding:0;"></td></tr>
		</tbody>
		</#list>
		</#if>
	</table>
	<table cellpadding="0" cellspacing="0" border="0">
	<#if showsplitter>
		<thead class="separation">
			<tr>
				<td>&nbsp;</td>
				<td>&nbsp;</td>
				<td colspan="4">版块主题</td>
			</tr>
		</thead>
    </#if>
    	<#if topiclist?exists>
    	<#list topiclist as topic>
		<tbody>
			<tr>
				<td class="folder">
				<#assign aspxrewriteurl = "showtopic.action?topicid="+topic.tid>
				<#if topic.folder!="">
					<a href="${aspxrewriteurl}" target="_blank"><img src="templates/${templatepath}/images/folder_${topic.folder}.gif" alt="topicicon" /></a>
				</#if>
				</td>
				<td class="icon">
					<#if topic.special==0>
						<#if topic.iconid!=0>
							<img src="images/posticons/${topic.iconid}.gif" alt="listicon"/>
						<#else>
							&nbsp;
						</#if>
					</#if>
					<#-- 特殊贴图标 -->
					<#if topic.special==1>
						<img src="templates/${templatepath}/images/pollsmall.gif" alt="投票" />
					</#if>
					<#if topic.special==2>
						<img src="templates/${templatepath}/images/bonus.gif" alt="悬赏"/>
					</#if>
					<#if topic.special==3>
						<img src="templates/${templatepath}/images/rewardsmallend.gif" alt="悬赏已结束"/>
					</#if>
					<#if topic.special==4>
						<img src="templates/${templatepath}/images/debatesmall.gif" alt="辩论"/>
					</#if>
				</td>
				<th class="common">
					<label>
					<#if (topic.digest>0)>
						<img src="templates/${templatepath}/images/digest${topic.digest}.gif" alt="精华"/>
					</#if>
					<#if (topic.attachment>0)>
						<img src="templates/${templatepath}/images/attachment.gif" alt="附件"/>
					</#if>
					<#if (topic.rate>0)>
						<img src="templates/${templatepath}/images/agree.gif" alt="正分"/>
					</#if>
					<#if topic.rate<0>
						<img src="templates/${templatepath}/images/disagree.gif" alt="负分"/>
					</#if>
					</label>
					<#if (useradminid>0) && ismoder>
						<input type="checkbox" name="topicid" value="${topic.tid}" />
					</#if>
					<#if (topic.replies>0)>
						<img src="templates/${templatepath}/images/topItem_exp.gif" id="imgButton_${topic.tid}" onclick="showtree(${topic.tid},${ppp},0);" class="cursor" alt="展开帖子列表" title="展开帖子列表" />
					<#else>
						<img src="templates/${templatepath}/images/no-sublist.gif" id="imgButton_${topic.tid}" alt="闭合帖子列表" />
					</#if>
					<#if pageid==1 && forum.allowthumbnail==1>
						<#if topic.attachment==2>
							<span id="t_thumbnail_${topic.tid}" onmouseover="showMenu(this.id, 0, 0, 1, 0)">
							<#if forum.forumfields.applytopictype==1 && forum.topictypeprefix==1>
								<em>
								<#if forum.viewbytopictype==1 && topic.topictypename!="">
								[<a href="showforum.action?forumid=${forumid}&typeid=${topic.typeid}" >${topic.topictypename}</a>]
								<#elseif topic.topictypename!="">
								[${topic.topictypename}]
								</#if>
								</em>
							</#if>	
							<#if topic.special==3>
								<#assign aspxrewriteurl = "showbonus.action?topicid="+topic.tid>
							</#if>
							<#if topic.special==4>
								<#assign aspxrewriteurl = "showdebate.action?topicid="+topic.tid>
							</#if>
							<#assign ishtmltitle=topicManager.getMagicValue(topic.magic, 1)/>
							<#if ishtmltitle==1>
								<a href="${aspxrewriteurl}">${topicManager.getHtmlTitle(topic.tid)}</a>
							<#else>
								<a href="${aspxrewriteurl}">${topic.title}</a>
							</#if>							
							<#if topic.folder=="new">
								<img src="templates/${templatepath}/images/posts_new.gif" />
							</#if>
							</span>
						<#else>
						   <#if forum.forumfields.applytopictype==1 && forum.topictypeprefix==1>
								<em>
								<#if forum.viewbytopictype==1 && topic.topictypename!="">
								[<a href="showforum.action?forumid=${forumid}&typeid=${topic.typeid}" >${topic.topictypename}</a>]
								<#elseif topic.topictypename!="">
								[${topic.topictypename}]
								</#if>
								</em>
							</#if>	
							<#if topic.special==3>
								<#assign aspxrewriteurl = "showbonus.action?topicid="+topic.tid>
							</#if>
							<#if topic.special==4>
								<#assign aspxrewriteurl = "showdebate.action?topicid="+topic.tid>
							</#if>
							<#assign ishtmltitle=topicManager.getMagicValue(topic.magic, 1)/>
							<#if ishtmltitle==1>
								<a href="${aspxrewriteurl}">${topicManager.getHtmlTitle(topic.tid)}</a>
							<#else>
								<a href="${aspxrewriteurl}">${topic.title}</a>
							</#if>
						   <#if topic.folder=="new">
								<img src="templates/${templatepath}/images/posts_new.gif"/>
							</#if>
						</#if>
					<#else>
						<#if forum.forumfields.applytopictype==1 && forum.topictypeprefix==1>
								<em>
								<#if forum.viewbytopictype==1 && topic.topictypename!="">
								[<a href="showforum.action?forumid=${forumid}&typeid=${topic.typeid}" >${topic.topictypename}</a>]
								<#elseif topic.topictypename!="">
								[${topic.topictypename}]
								</#if>
								</em>
						</#if>	

						<#if topic.special==3>
							<#assign aspxrewriteurl = "showbonus.action?topicid="+topic.tid>
						</#if>
						<#if topic.special==4>
							<#assign aspxrewriteurl = "showdebate.action?topicid="+topic.tid>
						</#if>						
						<#assign ishtmltitle=topicManager.getMagicValue(topic.magic, 1)/>
						<#if ishtmltitle==1>
							<a href="${aspxrewriteurl}">${topicManager.getHtmlTitle(topic.tid)}</a>
						<#else>
							<a href="${aspxrewriteurl}">${topic.title}</a>
						</#if>
						<#if topic.folder=="new">
							<img src="templates/${templatepath}/images/posts_new.gif"/>
						</#if>
					</#if>
					
					<#if topic.special==2>
						- [悬赏 ${userextcreditsinfo.name} <span class="bold">${topic.price}</span> ${userextcreditsinfo.unit}] 
					<#elseif topic.special==3>
						- [悬赏已结束]
					<#elseif topic.special==0>
						<#if (topic.price>0)>
							- [售价 ${userextcreditsinfo.name} <span class="bold">${topic.price}</span> ${userextcreditsinfo.unit}] 
						</#if>
					</#if>
					<#if (topic.readperm>0)>
						- [阅读权限 <span class="bold">${topic.readperm}</span>] 
					</#if>
					<#if (topic.replies/ppp>0)>
						<span class="threadpages"><script type="text/javascript">getpagenumbers("", ${topic.replies},${ppp},0,"",${topic.tid});</script></span>
					</#if>
				</th>
				<td class="author">
					<cite>
						<#if topic.usersByPosterid.uid==-1>
							游客
						<#else>
							<a href="userinfo.action?userid=${topic.usersByPosterid.uid}">${topic.poster}</a>
						</#if>
					</cite>
					<em>${topic.postdatetime}</em>
				</td>
				<td class="nums"><strong>${topic.replies}</strong> / <em>${topic.views}</em></td>
				<td class="lastpost">							
							<em><a href="showtopic.action?topicid=${topic.tid}&page=end#lastpost">${topic.lastpost}</a></em>
							<cite>by
							<#if topic.usersByLastposterid.uid==-1>
								游客
							<#else>
								<a href="userinfo.action?userid=${topic.usersByLastposterid.uid}" target="_blank">${topic.lastposter}</a>
							</#if>
							</cite>
				</td>
			</tr>
			<#if pageid==1 && forum.allowthumbnail==1>
				<#if topic.attachment==2>
					<#assign timg="" />
					<#if timg!="">
						<div id="t_thumbnail_${topic.tid}_menu" style="display: none;" class="popupmenu_popup"><img src="${timg}" /></div>
					</#if>
				</#if>
			</#if>
			<tr><td colspan="6" id="divTopic${topic.tid}" style=" border:0;padding:0;"></td></tr>
		</tbody>
		</#list>
		</#if>
		<!--NtForumList end-->
	</table>
	<#if (useradminid>0) && ismoder>
	<div class="footoperation"><strong>批量管理选项</strong> &nbsp;
		<input class="radio" name="operat" type="hidden" />
		<input class="checkbox" name="chkall" onclick="checkall(this.form, 'topicid')" type="checkbox" />全选
		<button onclick="document.moderate.operat.value = 'delete';document.moderate.submit()"/>删除主题</button>
		<button onclick="document.moderate.operat.value = 'move';document.moderate.submit()" />移动主题</button>
		<button onclick="document.moderate.operat.value = 'highlight';document.moderate.submit()" />高亮显示</button>
		<button onclick="document.moderate.operat.value = 'type';document.moderate.submit()" />主题分类</button>
		<button onclick="document.moderate.operat.value = 'identify';document.moderate.submit()" />鉴定主题</button>
		<button onclick="document.moderate.operat.value = 'close';document.moderate.submit()" />关闭/打开主题</button>
		<button onclick="document.moderate.operat.value = 'displayorder';document.moderate.submit()" />置顶/解除置顶</button>
		<button onclick="document.moderate.operat.value = 'digest';document.moderate.submit()" />加入/解除精华</button>
		<button onclick="document.moderate.operat.value = 'bump';document.moderate.submit()" />提升/下沉主题</button>
	</div>
	</#if>
</div>
</form>

<div class="pages_btns">
	<div class="pages">
		<em>${pageid}/${pagecount}页</em>${pagenumbers}
		<kbd>跳转<input name="gopage" type="text" id="gopage" onKeyDown="if(event.keyCode==13) { window.location='showforum.action?forumid=${forumid}&page=' + (parseInt(this.value) > 0 ? parseInt(this.value) : 1)}" size="4" maxlength="9" />页</kbd>
	</div>
<#if userid<0||canposttopic>
	<span onmouseover="$('newspecial').id = 'newspecialtmp';this.id = 'newspecial'; if($('newspecial_menu').childNodes.length>0)  showMenu(this.id);" id="Span1" class="postbtn"><a title="发新话题" id="A1" href="posttopic.action?forumid=${forum.fid}" onmouseover="if($('newspecial_menu').childNodes.length>0)  showMenu(this.id);"><img alt="发新话题" src="templates/${templatepath}/images/newtopic.gif"/></a></span>
</#if>
</div>


<#if canquickpost>
	<@comm.quickpost/>
</#if>

<#if userid<0||canposttopic>
	<ul class="popupmenu_popup newspecialmenu" id="newspecial_menu" style="display: none">
	<#if forum.allowspecialonly<=0>
	<li><a href="posttopic.action?forumid=${forum.fid}">发新主题</a></li>
	</#if>
	<#assign specialpost=reqcfg.opNum(forum.allowpostspecial,1)/>
	<#if specialpost==1 && usergroupinfo.allowpostpoll==1>
	<li class="poll"><a href="posttopic.action?forumid=${forum.fid}&type=poll">发布投票</a></li>
	</#if>
	<#assign specialpost=reqcfg.opNum(forum.allowpostspecial,4)/>
	<#if specialpost==4 && usergroupinfo.allowbonus==1>
		<li class="reward"><a href="posttopic.action?forumid=${forum.fid}&type=bonus">发布悬赏</a></li>
	</#if>
	<#assign specialpost=reqcfg.opNum(forum.allowpostspecial,16)/>
	<#if specialpost==16 && usergroupinfo.allowdebate==1>
		<li class="debate"><a href="posttopic.action?forumid=${forum.fid}&type=debate">发起辩论</a></li>
	</#if>
	</ul>
</#if>

<div id="footfilter" class="box">
	<form name="LookBySearch" method="post" action="showforum.action?search=1&forumid=${forumid}&typeid=${topictypeid}&filter=${filter}">
		<#if topictypeid==0>  
		查看
		<select name="cond" id="cond">
		  <option value="0" <#if cond==0>selected</#if>>全部主题</option>
		  <option value="1" <#if cond==1>selected</#if>>1 天以来主题</option>
		  <option value="2" <#if cond==2>selected</#if>>2 天以来主题</option>
		  <option value="7" <#if cond==7>selected</#if>>1 周以来主题</option>
		  <option value="30" <#if cond==30>selected</#if>>1 个月以来主题</option>
		  <option value="90" <#if cond==90>selected</#if>>3 个月以来主题</option>
		  <option value="180" <#if cond==180>selected</#if>>6 个月以来主题</option>
		  <option value="365" <#if cond==365>selected</#if>>1 年以来主题</option>
		</select>
	</#if>
		排序方式
		<select name="order" id="order">
		  <option value="1" <#if order==1>selected</#if>>最后回复时间</option>
		  <option value="2" <#if order==2>selected</#if>>发布时间</option>
		</select>
		<select name="direct" id="direct">
		  <option value="0" <#if direct==0>selected</#if>>按升序排列</option>
		  <option value="1" <#if direct==1>selected</#if>>按降序排列</option>
		</select>
		<button type="submit">提交</button>
	</form>
	<#if config.forumjump==1>
    <select onchange="if(this.options[this.selectedIndex].value != '') { jumpurl(this.options[this.selectedIndex].value);}">
		<option>论坛跳转...</option>
		${forumlistboxoptions?default("")}
		</select>
	</#if>
	<#if (config.visitedforums>0)>
    <select name="select2" onchange="if(this.options[this.selectedIndex].value != '') {jumpurl(this.options[this.selectedIndex].value);}">
		<option>最近访问...</option>${visitedforumsoptions?default("")}
	</select>
	</#if>		
	<script type="text/javascript">
	function jumpurl(fid) {		
		window.location='showforum.action?forumid=' + fid ;
	}
	</script>

</div>
</#if>

<#if config.whosonlinestatus!=0 && config.whosonlinestatus!=1>
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
		<strong>在线用户</strong>- <em>${forumtotalonline}</em> 人在线<span id="invisible"></span>
	</h4>
	<dl id="onlinelist">
		<dt>${onlineiconlist}</dt>
		<dd>
			<ul class="userlist">
				<#if showforumonline>
				<#assign invisiblecount=0/>
				<#list onlineuserlist as onlineuser>
					<#if onlineuser.invisible==1>
						<#assign invisiblecount=invisiblecount+1/>
				<li>(隐身会员)</li>
					<#else>
				<li>${onlineuser.olimg}
						<#if onlineuser.userid==-1>
							${onlineuser.username}
						<#else>
							<a href="{aspxrewriteurl}" target="_blank">${onlineuser.username}</a>
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

<#if forum.layer!=0>
<div class="legend">
	<label><img src="templates/${templatepath}/images/folder_new.gif" alt="有新的回复"/>有新回复</label>
	<label><img src="templates/${templatepath}/images/folder_old.gif" alt="无新回复"/>无新回复</label>
	<label><img src="templates/${templatepath}/images/folder_newhot.gif" alt="多于15篇回复"/>热门主题</label>
	<label><img src="templates/${templatepath}/images/folder_closed.gif" alt="关闭的主题"/>关闭主题</label>
</div>
</#if>

		</div>
	</#if>
<#else>
	<#if needlogin>
		<@comm.login/>
	<#else>
		<@comm.errmsgbox/>
	</#if>
	</div>
</#if>
<@comm.adlist/>
<@comm.copyright/>
<@comm.footer/>