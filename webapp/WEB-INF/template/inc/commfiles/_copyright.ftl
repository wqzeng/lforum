<#-- 
	描述：底部版权声明 
	作者：黄磊 
	版本：v1.0 
-->
<#if footerad!="">
<!--底部广告显示-->
<div id="ad_footerbanner">${footerad}</div>
<!--底部广告结束-->
</#if>
<div id="footer">
	<div class="wrap">
		<div id="footlinks">
			<p><a href="${config.weburl}" target="_blank">${config.webtitle}</a>&nbsp; ${config.linktext}<#if config.sitemapstatus==1>&nbsp;<a href="tools/sitemap.action" target="_blank" title="百度论坛收录协议">Sitemap</a></#if>
				${config.statcode}
				${config.icp}
			</p>
			<p>
			<a href="http://lonlysky.javaeye.com/" target="_blank">Lonlysky's Blog</a>
			- <a href="archiver/index.action" target="_blank">简洁版本</a>
			- <span class="scrolltop" onclick="window.scrollTo(0,0);">TOP</span>

			<#if config.stylejump==1>
				<#if userid!=-1 || config.guestcachepagetimeout<=0>
			- <span id="styleswitcher" class="dropmenu" onmouseover="showMenu(this.id)" onclick="window.location.href='showtemplate.action'">界面风格</span>

				<div id="styleswitcher_menu" class="popupmenu_popup" style="display: none;">
					<ul>
						${templatelistboxoptions}
					</ul>
				</div>
				</#if>
			</#if>
			</p>
		</div>
		<a title="Powered by LForum For Java" target="_blank" href="http://lonlysky.javaeye.com"><img border="0" alt="LForum" src="templates/${templatepath}/images/lforum_logo.gif"/></a>

		<p id="copyright">
			Powered by <strong><a href="http://lonlysky.javaeye.com" target="_blank">LForum For Java</a></strong> <em>1.0</em>
			${config.forumcopyright}
		</p>
		<p id="debuginfo">
		Processed in ${reqcfg.pageTime} ms.
		</p>
	</div>
</div>
<ul class="popupmenu_popup headermenu_popup" id="stats_menu" style="display: none">
	<li><a href="stats.action">基本状况</a></li>
	<#if config.statstatus==1>
	<li><a href="stats.action?type=views">流量统计</a></li>
	<li><a href="stats.action?type=client">客户软件</a></li>
	</#if>
	<li><a href="stats.action?type=posts">发帖量记录</a></li>
	<li><a href="stats.action?type=forumsrank">版块排行</a></li>
	<li><a href="stats.action?type=topicsrank">主题排行</a></li>
	<li><a href="stats.action?type=postsrank">发帖排行</a></li>
	<li><a href="stats.action?type=creditsrank">积分排行</a></li>
	<#if (config.oltimespan>0)>
	<li><a href="stats.action?type=onlinetime">在线时间</a></li>
	</#if>
</ul>
<ul class="popupmenu_popup headermenu_popup" id="my_menu" style="display: none">
	<li><a href="mytopics.action">我的主题</a></li>
	<li><a href="myposts.action">我的帖子</a></li>
	<li><a href="search.action?posterid=${userid}&amp;type=digest">我的精华</a></li>
	<li><a href="myattachment.action">我的附件</a></li>
	<#if (userid>0)>
	<li><a href="usercpsubscribe.action">我的收藏</a></li>
	</#if>	
	<script type="text/javascript" src="javascript/mymenu.js"></script>
</ul>
<ul class="popupmenu_popup" id="viewpro_menu" style="display: none">
	<#if useravatar!="">
		<img src="${useravatar}"/>
	</#if>
	<li class="popuser"><a href="userinfo.action?userid=${userid}">我的资料</a></li>
    </ul>
<div id="quicksearch_menu" class="searchmenu" style="display: none;">
	<div onclick="document.getElementById('keywordtype').value='0';document.getElementById('quicksearch').innerHTML='帖子标题';document.getElementById('quicksearch_menu').style.display='none';" onmouseover="MouseCursor(this);">帖子标题</div>	
	<div onclick="document.getElementById('keywordtype').value='8';document.getElementById('quicksearch').innerHTML='作&nbsp;&nbsp;者';document.getElementById('quicksearch_menu').style.display='none';" onmouseover="MouseCursor(this);">作&nbsp;&nbsp;者</div>
</div>