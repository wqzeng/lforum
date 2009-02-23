<#-- 
	描述：用户中心积分兑换转账记录模板
	作者：黄磊 
	版本：v1.0 
-->
<#import "inc/page_comm.ftl" as comm />
<@comm.header/>
<!--header end-->
<div id="foruminfo">
	<div id="nav">
		<a href="${config.forumurl}" class="home">${config.forumtitle}</a>  &raquo; <a href="usercp.action">用户中心</a>  &raquo; <strong>转账与兑换记录</strong>
	</div>
	<div id="headsearch">
		<div id="search">
			<@comm.quicksearch/>
		</div>
	</div>
</div>

<!--主体-->
<div class="controlpannel">
   <@comm.menu/>
	<div class="pannelcontent">
		<div class="pcontent">
			<div class="panneldetail">
				<@comm.scoremenu/>	
				<div class="pannelbody">
					<div class="pannellist">
					<#if reqcfg.page_err==0>
						<#if ispost>
							<@comm.msgbox/>
						<#else>
				  		<form id="form1" name="form1" method="post" action="">
						<table width="100%" border="0" cellspacing="0" cellpadding="0">
				  		<tr>
							<th width="30%">来自/到</th>
							<th width="20%">时间</th>
							<th width="20%">支出</th>
							<th width="20%">收入</th>
							<th width="6%">操作</th>
						</tr>
						<#list creditsloglist as creditslog>
						<tbody>
						<tr class="messagetable" onmouseover="this.className='messagetableon'" onmouseout="this.className='messagetable'">
                          <td width="30%">
							<#if creditslog.usersByUid.uid==userid>
								<a href="userinfo.action?userid=${creditslog.usersByFromto.uid}" target="_blank">${creditslog.usersByFromto.username}</a>
							<#else>
								<a href="userinfo.action?userid=${creditslog.usersByUid.uid}" target="_blank">${creditslog.usersByUid.username}</a>
							</#if>
						  </td>
                          <td width="20%">${creditslog.paydate}</td>
                          <td width="20%">${creditslog.send}</td>
                          <td width="20%">${creditslog.receive}</td>
                          <td><#if creditslog.operation==1>兑换<#else>转账</#if></td>
						</tr>
						</tbody>
						</#list>
						</table>
						</form>
						</div>
						<div class="pages_btns">
							<div class="pages">
								<em>${pageid}/${pagecount}页</em>${pagenumbers}
								<kbd>跳转<input name="gopage" type="text" id="gopage" onKeyDown="if(event.keyCode==13) {
							window.location='usercpcreaditstransferlog.action?page=' + this.value;}"  size="4" maxlength="9"/>页</kbd>
							</div>
						</div>
						</#if>
					<#else>
					<@comm.usercperrmsgbox/>
					</#if>
			  </div>
			</div>
		</div>
	</div>
</div>
<!--主体-->
</div>
<@comm.copyright/>
<@comm.footer/>
