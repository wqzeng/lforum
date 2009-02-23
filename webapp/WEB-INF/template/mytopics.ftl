<#-- 
	描述：我的主题页模板
	作者：黄磊 
	版本：v1.0 
-->
<#import "inc/page_comm.ftl" as comm />
<@comm.header/>
<div id="foruminfo">
	<div id="nav">
		<a href="${config.forumurl}">${config.forumtitle}</a>  &raquo; <a href="usercp.action">用户中心</a>  &raquo; <strong>我的主题</strong>
	</div>
</div>
<!--body-->
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
				<div class="pannelbody">
					<div class="pannellist">
					<#if reqcfg.page_err==0>
						<table width="100%" border="0" cellspacing="0" cellpadding="0">
						<thead>
							<tr>
							<th width="3%">&nbsp;</th>
							<th width="4%">&nbsp;</th>
							<th width="50%" style="text-align:left;">标题</th>
							<th width="20%">所在版块</th>
							<th>最后发表</th>
							</tr>
						</thead>
						<#list topics as topic>
						<tbody>
						<tr class="messagetable" onmouseover="this.className='messagetableon'" onmouseout="this.className='messagetable'">
							<td width="3%">
							<#assign aspxrewriteurl="showtopic.action?topicid="+topic.tid>
							<#if topic.folder!="">
								<a href="${aspxrewriteurl}" target="_blank"><img src="templates/${templatepath}/images/folder_${topic.folder}.gif" alt="图例"/></a>
							</#if>
							</td>
							<td width="4%"><#if topic.iconid!=0><img src="images/posticons/${topic.iconid}.gif"/><#else>&nbsp;</#if></td>
							<td width="50%" style="text-align:left;"><a href="${aspxrewriteurl}" target="_blank">${topic.title}</a></td>
							<td width="20%">
							<#assign aspxrewriteurl = "showforum.action?forumid="+topic.forums.fid>
							<a href="${aspxrewriteurl}" target="_blank">${topic.forums.name}</a></td>
							<td><span class="fontfamily"><a href="showtopic.action?topicid=${topic.tid}&page=end"><script type="text/javascript">document.write(${topic.lastpost});</script></a> by <#if topic.usersByLastposterid.uid!=-1><#assign aspxrewriteurl = "userinfo.action?userid="+topic.usersByLastposterid.uid><a href="${aspxrewriteurl}" target="_blank">${topic.lastposter}</a><#else>${topic.lastposter}</#if></span></td>
						</tr>
						</tbody>
						</#list>
						</table>
						</div>
						<div class="pages_btns">
							<div class="pages">
								<em>${pageid}/${pagecount}页</em>${pagenumbers}
								<kbd>跳转<input name="gopage" type="text" id="gopage" onKeyDown="if(event.keyCode==13) {
									window.location='mytopics.action?page='+this.value;}"  size="4" maxlength="9" class="colorblue2"/>页</kbd>
							</div>
						</div>
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