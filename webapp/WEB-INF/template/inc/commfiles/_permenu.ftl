<#-- 
	描述：个人设置菜单模板
	作者：黄磊 
	版本：v1.0 
-->
﻿				<div class="panneltabs">
				<#if (userid>0)>
					<a href="usercpprofile.action"
					<#if pagename=="usercpprofile.action">
					 class="current"
					 </#if>>编辑个人档案</a>
					<a href="usercpnewpassword.action"
					<#if pagename=="usercpnewpassword.action">
					 class="current"
					 </#if>>更改密码</a>
					 
					 <a href="usercppreference.action"
					<#if pagename=="usercppreference.action">
					 class="current"
					 </#if>>个性设置</a>
				</#if>
				</div>	