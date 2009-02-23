<#-- 
	描述：用户中心菜单模板
	作者：黄磊 
	版本：v1.0 
-->
﻿<div class="pannelmenu">
<#if (userid>0)>
      <#if pagename=="usercptopic.action"||pagename=="usercppost.action"||pagename=="usercpdigest.action"||pagename=="usercpprofile.action"
      ||pagename=="usercpnewpassword.action"||pagename=="usercppreference.action">
	   <a href="usercpprofile.action" class="current"><span>个人设置</span></a>
	   <#else>
	   <a href="usercpprofile.action">个人设置</a>
	   </#if>
	   
	  <#if pagename=="usercpinbox.action"||pagename=="usercpsentbox.action"||pagename=="usercpdraftbox.action"||pagename=="usercppostpm.action"||pagename=="usercpshowpm.action"||pagename=="usercppmset.action">
	   <a href="usercpinbox.action" class="current"><span>短消息</span></a>
	   <#else>
	   <a href="usercpinbox.action">短消息</a>
	   </#if>
	   	  
	  <#if pagename=="usercpsubscribe.action">
	   <a href="usercpsubscribe.action" class="current"><span>收藏夹</span></a>
	   <#else>
	   <a href="usercpsubscribe.action">收藏夹</a>
	   </#if>
      
	   <#if pagename=="usercpcreditspay.action"||pagename=="usercpcreditstransfer.action"||pagename=="usercpcreditspayoutlog.action"||pagename=="usercpcreditspayinlog.action"
	   ||pagename=="usercpcreaditstransferlog.action">
       <a href="usercpcreditspay.action" class="current"><span>积分交易</span></a>
	   <#else>
       <a href="usercpcreditspay.action">积分交易</a>
       </#if>
		<#if pagename=="usercpforumsetting.action">
			<a href="usercpforumsetting.action" class="current"><span>论坛设置</span></a>
		<#else>
			<a href="usercpforumsetting.action">论坛设置</a>
		</#if>
</#if>       
	</div>