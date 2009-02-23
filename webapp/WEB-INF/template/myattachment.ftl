<#-- 
	描述：我的附件页模板
	作者：黄磊 
	版本：v1.0 
-->
<#import "inc/page_comm.ftl" as comm />
<@comm.header/>
<div id="foruminfo">
	<div id="nav">
		<a href="${config.forumurl}">${config.forumtitle}</a>&raquo;<a href="usercp.action">用户中心</a>&raquo;<strong>附件</strong>
	</div>
	<div id="headsearch">
		<div id="search">
			<@comm.quicksearch/>
		</div>
	</div>
</div>
<script type="text/javascript">
	function checkCheckBox(form,objtag)
	{
		for(var i = 0; i < form.elements.length; i++) 
		{
			var e = form.elements[i];
			if(e.name == "pmitemid") 
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
				<#if reqcfg.page_err==0>
				<@comm.attachmentmenu/>	
				<div class="pannelbody">
					<div class="pannellist">
						<#if ispost>
							<@comm.msgbox/>
						<#else>
				  		<form id="pmform" name="pmform" method="post" action="">
						<table width="100%" border="0" cellspacing="0" cellpadding="0">
						<#list myattachmentlist as myatinfo>
						<tbody>
							<tr class="messagetable" onmouseover="this.className='messagetableon'" onmouseout="this.className='messagetable'">
							<td width="33%" style="text-align:left;"><a href="${myatinfo.filename}" title="${myatinfo.attachment}">${myatinfo.attachment}</a></td>
							<td width="22%" style="text-align:left;">${myatinfo.description}</td>
							<td width="28%">${myatinfo.postdatetime}</td>
							<td width="17%">${myatinfo.downloads}</td>
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
							window.location='myattachment.action?page=' + this.value;}"  size="4" maxlength="9"  class="colorblue2"/>页</kbd>
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