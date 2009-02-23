<#-- 
	描述：热门标签模板
	作者：黄磊 
	版本：v1.0 
-->
<#if config.enabletag==1>
<!--tag-->
<table cellspacing="1" cellpadding="0" class="portalbox" summary="HeadBox">
<tbody>
	<tr>
	<td>
		<div id="hottags">
			<h3><a target="_blank" href="tags.action">热门标签</a></h3>
			<ul id="forumhottags">
			<#list taglist as tag>
					<li><a 				
				href="tags.aspx?t=topic&tagid=${tag.tagid}" 
				<#if tag.color!="">	
					style="color: #${tag.color};"
				</#if>
					title="${tag.fcount}">${tag.tagname}</a></li>
			</#list>
			</ul>
		</div>
	</td>
	</tr>
</tbody> 
</table>
<!--tag end-->
</#if>