<#-- 
	描述：用户中心积分交易菜单模板
	作者：黄磊 
	版本：v1.0 
-->
﻿<div class="panneltabs">
<#if (userid>0)>
	<a href="usercpcreditspay.action"
	<#if pagename=="usercpcreditspay.action">
		class="current"
	</#if>>积分兑换</a>
	<a href="usercpcreditstransfer.action"
	<#if pagename=="usercpcreditstransfer.action">
		class="current"
	</#if>>积分转帐</a>
	<a href="usercpcreditspayoutlog.action"
	<#if pagename=="usercpcreditspayoutlog.action">
		class="current"
	</#if>>支出记录</a>
	<a href="usercpcreditspayinlog.action"
	<#if pagename=="usercpcreditspayinlog.action">
		class="current"
	</#if>>收入记录</a>
	<a href="usercpcreaditstransferlog.action"
	<#if pagename=="usercpcreaditstransferlog.action">
		class="current"
	</#if>>兑换与转帐记录</a>
</#if>
</div>	