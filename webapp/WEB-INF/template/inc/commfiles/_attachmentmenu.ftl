<#-- 
	描述：附件菜单定义 
	作者：黄磊 
	版本：v1.0 
-->
﻿				<div class="panneltabs">
                                <a href="myattachment.action"  <#if typeid==0>
					 class="current"
					 </#if>>全部</a>
				<#list typelist as list>
					<a href="?typeid=${list.typeId}" 
                                        <#if typeid==list.typeId>
					 class="current"
					 </#if>>${list.typeName}</a>
				</#list>
				</div>	