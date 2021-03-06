<#-- 
	描述：短信草稿箱模板
	作者：黄磊 
	版本：v1.0 
-->
<#import "inc/page_comm.ftl" as comm />
<@comm.header/>
<!--header end-->

<div id="foruminfo">
	<div id="nav">
		<a href="${config.forumurl}" class="home">${config.forumtitle}</a>  &raquo; <a href="usercp.action">用户中心</a>  &raquo; <strong>草稿箱</strong>
	</div>
	<div id="headsearch">
		<div id="search">
			<@comm.quicksearch/>
		</div>
	</div>
</div>

<!--主体-->
<script type="text/javascript">
	function checkCheckBox(form,objtag)
	{
		if (typeof(objtag.checked) == "undefined")
		{
			objtag.checked = true;
		}
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
				<@comm.smsmenu/>	
				<div class="pannelbody">
					<div class="pannellist">
					<#if reqcfg.page_err==0>
						<#if ispost>
							<@comm.msgbox/>
						<#else>
						<p class="newmessage"><a href="usercppostpm.action" class="submitbutton">写新消息</a></p>
				  		<form id="pmform" name="pmform" method="post" action="">
						<table width="100%" border="0" cellspacing="0" cellpadding="0" ID="Table1">
						<#list pmlist as pm>
							<tbody>
							<tr class="messagetable" onmouseover="this.className='messagetableon'" onmouseout="this.className='messagetable'">
							<td width="3%"><img src="templates/${templatepath}/images/message_${pm.new_}.gif" alt="短信息"/></td>
							<td width="4%"><input name="pmitemid" id="id${pm.pmid}" type="checkbox" value="${pm.pmid}" style="margin-top:-1px;"/></td>
							<td width="50%" style="text-align:left;"><a href="usercpshowpm.action?pmid=${pm.pmid}">${pm.subject}</a> </td>
							<td width="30%">
								<a href="userinfo.action?userid=${pm.usersByMsgfromid.uid}" target="_blank">${pm.msgfrom}</a>   <span class="fontfamily"><script type="text/javascript">document.write(${pm.postdatetime});</script></span></td>
							<td>
							    <a href="#" onclick="$('id${pm.pmid}').checked=true;$('pmform').submit();">删除</a></td>
							</tr>
							</tbody>
						</#list>
						</table>
						</form>
						</div>
						<div class="pannelmessage">
									<div class="pannelleft" style="width: 160px;">
										<a href="###"  onclick="checkCheckBox($('pmform'),this)" class="selectall">全选</a>&nbsp;&nbsp;&nbsp;
										<a href="#" onclick="$('pmform').submit()" class="selectall">删除</a> 
									</div>
									<div class="pannelright" style="width: 70%;">
											<table width="100%" border="0" cellspacing="0" cellpadding="0">
												<tbody>
													<tr>
														<th class="ticketleft">
															共有短消息:<span class="ticketnumbers">${usedmsgcount}</span>条 上限: ${maxmsg}条
														</th>
														<td width="300">
															<div class="optionbar" style="float:none;background:none;">
																<div style="width:${usedmsgbarwidth}%; background:#FFF url(templates/${templatepath}/images/ticket.gif) no-repeat 0 0;"></div>
															</div>
														</td>
													</tr>
												</tbody>
										 </table>
									</div>
						</div>
						<div class="pages_btns">
							<div class="pages">
								<em>${pageid}/${pagecount}页</em>${pagenumbers}
								<kbd>跳转<input name="gopage" type="text" id="gopage" onKeyDown="if(event.keyCode==13) {
							window.location='usercpinbox.action?page=' + this.value;}"  size="4" maxlength="9"/>页</kbd>
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
