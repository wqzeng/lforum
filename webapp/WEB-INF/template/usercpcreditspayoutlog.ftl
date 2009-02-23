<#-- 
	描述：用户中心积分支出记录模板
	作者：黄磊 
	版本：v1.0 
-->
<#import "inc/page_comm.ftl" as comm />
<@comm.header/>
<!--header end-->
<div id="foruminfo">
	<div id="nav">
		<a href="${config.forumurl}" class="home">${config.forumtitle}</a>  &raquo; <a href="usercp.action">用户中心</a>  &raquo; <strong>积分支出记录</strong>
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
							<th width="28%">标题</th>
							<th width="10%">作者</th>
							<th width="13%">发表时间</th>
							<th width="17%">论坛</th>
							<th width="13%">付费时间</th>
							<th width="5%">售价</th>
							<th width="7%">作者所得</th>
					  </tr>
					  	<#list payloglist as paylog>
						<tbody>
						<tr class="messagetable" onmouseover="this.className='messagetableon'" onmouseout="this.className='messagetable'">
                          <td width="28%" style="text-align:left;">
						  <a href="showtopic.action?topicid=${paylog.topics.tid}" target="_blank">${paylog.topics.title}</a></td>
                          <td width="10%">
						  <a href="userinfo.action?userid=${paylog.usersByAuthorid.uid}" target="_blank">${paylog.usersByAuthorid.username}</a></td>
                          <td width="15%">${paylog.topics.postdatetime}</td>
                          <td width="17%">
						  <a href="showforum.action?forumid=${paylog.topics.forums.fid}" target="_blank">${paylog.topics.forums.name}</a></td>
                          <td width="15%">${paylog.buydate}</td>
                          <td width="5%">${paylog.amount}</td>
                          <td width="7%">${paylog.netamount}</td>
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
							window.location='usercpcreditspayoutlog.action?page=' + this.value;}"  size="4" maxlength="9"/>页</kbd>
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