<#-- 
	描述：用户中心收藏夹模板
	作者：黄磊 
	版本：v1.0 
-->
<#import "inc/page_comm.ftl" as comm />
<@comm.header/>
<div id="foruminfo">
	<div id="nav">
		<a href="${config.forumurl}" class="home">${config.forumtitle}</a>  &raquo; <a href="usercp.action">用户中心</a>  &raquo; <strong>查看主题订阅</strong>
	</div>
	<div id="headsearch">
		<div id="search">
			<@comm.quicksearch/>
		</div>
	</div>
</div>
<!--body-->
<script type="text/javascript">
	function checkCheckBox(form,objtag)
	{
		for(var i = 0; i < form.elements.length; i++) 
		{
			var e = form.elements[i];
			if(e.name == "titemid") 
			{
				e.checked = objtag.checked;
			}
		}
		objtag.checked = !objtag.checked;
	}
</script>
<div class="controlpannel">
   <@comm.menu/>
	<div class="pannelcontent">
		<div class="pcontent">
			<div class="panneldetail">
				<@comm.subscribemenu/>	
				<div class="pannelbody">
					<div class="pannellist">
					<#if reqcfg.page_err==0>
						<#if ispost>
							<@comm.msgbox/>
						<#else>
				  		<form id="favlist" name="favlist" method="post" action="">
						<table width="100%" border="0" cellspacing="0" cellpadding="0">
						<#list favoriteslist as favorites>
						<tbody>
						  <tr class="messagetable" onmouseover="this.className='messagetableon'" onmouseout="this.className='messagetable'">
                          <td width="4%"><input name="titemid" id="id${favorites.topics.tid}" type="checkbox"  id="Checkbox1"  value="${favorites.topics.tid}" style="margin-top:-1px;"/></td>
                          <td width="55%" style="text-align:left;">
							<a href="showtopic.action?topicid=${favorites.topics.tid}">${favorites.topics.title}</a>
						  </td>
                          <td width="15%">
						  <a href="userinfo.action?userid=${favorites.topics.usersByPosterid.uid}">${favorites.topics.poster}</a></td>
                          <td width="20%" class="fontfamily">${favorites.topics.postdatetime}</td>
                          <td><a href="#" onclick="$('id${favorites.topics.tid}').checked=true;$('favlist').submit();">删除</a></td>
                        </tr>
						</tbody>
						</#list>
						</table>
						</form>
						</div>
						<div class="pannelmessage">
							<div class="pannelleft" style="width: 160px;">
								<a href="javascript:;"  onclick="checkCheckBox($('favlist'),this)" class="selectall">全选</a>&nbsp;&nbsp;&nbsp;
								<a href="#" onclick="$('favlist').submit()" class="selectall">删除</a> 
							</div>
						</div>
						<div class="pages_btns">
							<div class="pages">
								<em>${pageid}/${pagecount}页</em>${pagenumbers}
								<kbd>跳转<input name="gopage" type="text" id="gopage" onKeyDown="if(event.keyCode==13) {
							window.location='usercpsubscribe.action?page=' + this.value;}"  size="4" maxlength="9"/>页</kbd>
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
<!--body-->
</div>
<@comm.copyright/>
<@comm.footer/>