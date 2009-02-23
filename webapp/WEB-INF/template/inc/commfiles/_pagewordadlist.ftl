<#-- 
	描述：页面文章广告模板
	作者：黄磊 
	版本：v1.0 
-->
﻿<#if (pagewordad?size>0)>
<!--adtext-->
<div id="ad_text" class="ad_text">
	<table cellspacing="1" cellpadding="0" summary="Text Ad">
	<tbody>
		<tr>
		<#assign adindex=0/>
		<#list pagewordad as pageword>
			<#if adindex<4>
				<td>${pageword}</td>
				<#assign adindex=adindex+1 />
			<#else>
				</tr><tr>
				<td>${pageword}</td>
				<#assign adindex=1 />
			</#if>								
		</#list>
		<#if (pagewordad.length%4>0)>
			<#list 0..(4-size(pagewordad)%4) as j>
				<td>&nbsp;</td>
			</#list>			
		</#if>
		</tr>
	</tbody>
	</table>
</div>
<!--adtext-->
</#if>