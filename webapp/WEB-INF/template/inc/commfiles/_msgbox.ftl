<#-- 
	描述：显示提示信息
	作者：黄磊 
	版本：v1.0 
-->
<div class="box message">
	<h1>LForum For Java 提示信息</h1>
	<p>${reqcfg.msgbox_text}</p>
	<#if reqcfg.msgbox_url!="">
	<p><a href="${reqcfg.msgbox_url}">如果浏览器没有转向, 请点击这里.</a></p>
	</#if>
</div>
</div>