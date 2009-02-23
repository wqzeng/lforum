<#-- 
	描述：短信菜单模板 
	作者：黄磊 
	版本：v1.0 
-->
﻿				<div class="panneltabs">
				<#if (userid>0)>
					<a href="usercpinbox.action"
					<#if pagename=="usercpinbox.action">
					 class="current"
					 </#if>>收件箱</a>
					<a href="usercpsentbox.action"
					<#if pagename=="usercpsentbox.action">
					 class="current"
					 </#if>>发件箱</a>
					<a href="usercpdraftbox.action"
					<#if pagename=="usercpdraftbox.action">
					 class="current"
					 </#if>>草稿箱</a>
					 <a href="usercppmset.action"
					 <#if pagename=="usercppmset.action">
						class="current"
					 </#if>>选项</a>
					<a href="usercppostpm.action"
					<#if pagename=="usercppostpm.action">
					 class="current addbutton"
					<#else>
					 class="addbutton"
					 </#if>>写新消息</a>
				</#if>
				</div>	