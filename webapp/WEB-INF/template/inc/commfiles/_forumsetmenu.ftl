<#-- 
	描述：论坛设置显示模板 
	作者：黄磊 
	版本：v1.0 
-->
﻿                <div class="panneltabs">
				<#if (userid>0)>
						  <a href="usercpforumsetting.action"
						 <#if pagename=="usercpforumsetting.action">
						 class="current"
						 </#if>>论坛设置</a>
				</#if>					
				</div>	