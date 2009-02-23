<#-- 
	描述：用户中心模板
	作者：黄磊 
	版本：v1.0 
-->
<#import "inc/page_comm.ftl" as comm />
<@comm.header/>
<div id="foruminfo">
	<div id="nav">
		<a href="${config.forumurl}" class="home">${config.forumtitle}</a>  &raquo; <a href="usercp.action">用户中心</a>
	</div>
	<div id="headsearch">
		<div id="search">
			<@comm.quicksearch/>
		</div>
	</div>
</div>

<div class="controlpannel">
   <@comm.menu/>
	<div class="pannelcontent">
		<div class="pcontent">
			<div class="panneldetail">	
				<div class="pannelbody">
					<div class="pannellist">
					<#if reqcfg.page_err==0>
						<#if ispost>
							<@comm.msgbox/>
						<#else>
						<div class="photoimg">
							<img src="${user.userfields.avatar}" <#if (user.userfields.avatarwidth>0)>width="${user.userfields.avatarwidth}" height="${user.userfields.avatarheight}"</#if> />
							<ul>
								<li><span class="usermessagesname">${user.username}</span></li>
								<li>积分: ${user.credits}</li>
									<#if score[1]!="">
									<li>${score[1]}: ${user.extcredits1}</li>
									</#if>
									<#if score[2]!="">
									<li>${score[2]}: ${user.extcredits2}</li>
									</#if>
									<#if score[3]!="">
									<li>${score[3]}: ${user.extcredits3}</li>
									</#if>
									<#if score[4]!="">
									<li>${score[4]}: ${user.extcredits4}</li>
									</#if>
									<#if score[5]!="">
									<li>${score[5]}: ${user.extcredits5}</li>
									</#if>
									<#if score[6]!="">
									<li>${score[6]}: ${user.extcredits6}</li>
									</#if>
									<#if score[7]!="">
									<li>${score[7]}: ${user.extcredits7}</li>
									</#if>
									<#if score[8]!="">
									<li>${score[8]}: ${user.extcredits8}</li>
									</#if>
									<li>用户组: ${usergroupinfo.grouptitle}</li>
							</ul>
						</div>
						<ul>
							<li><strong>签名:</strong></li>
							<li>
								<#if user.userfields.signature=="">
								暂无签名
								<#else>
								${user.userfields.sightml}
								</#if>
							</li>
						</ul>
						</#if>
						</div>
					<#else>
					<@comm.usercperrmsgbox/>
					</#if>
			  </div>
			</div>
		</div>
	</div>
</div>
</div>
<@comm.copyright/>
<@comm.footer/>
