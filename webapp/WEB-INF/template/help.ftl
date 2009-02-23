<#-- 
	描述：论坛帮助页模板
	作者：黄磊 
	版本：v1.0 
-->
<#import "inc/page_comm.ftl" as comm />
<@comm.header/>
<#if reqcfg.page_err==0>
    <#if showversion==1>
        <div class="NavHelp">
        <div style="border:0px; background-color:#F3F3F3; width:100%; margin:auto; padding:6px">LForum 版本信息</div>       
         <ul style="margin: 8px 16px;">               
            数据库类型 : ${dbtype} <br />
            产品名称 : ${assemblyproductname} <br />
            版权信息 : ${Assemblycopyright} <br />
        </ul>
       </div> 
    <#else>
<div id="foruminfo">
	<div id="nav">
		<a href="${config.forumurl}">${config.forumtitle}</a> &raquo; <script type="text/javascript">if (getQueryString('hid')!='') document.write(' <strong><a href="help.action">帮助</a></strong>');</script>
	</div>
</div>
<div class="mainbox viewthread specialthread">	
	<table cellspacing="0" cellpadding="0" summary="在线帮助">	
		<td class="postcontent helpcontent">
			<h3>在线帮助</h3>
			<#list helplist as helpcontent>
				<#if helpcontent.pid==0>
						<h2>${helpcontent.title}</h2>
				<#else>
						<ul>
							<li class="helpsubtitle"><a name="h_${helpcontent.pid}_${helpcontent.id}"></a>${helpcontent.title}</li>
							<li>${helpcontent.message}</li>
						</ul>
				</#if>
			</#list>
		</td>
		<td class="postauthor helpmenu">
				<#list helplist as help>
					<#if help.pid==0>
						<p><strong><a href="?hid=${help.id}">${help.title}</a></strong></p>
					<#else>
						<p><a href="#h_${help.pid}_${help.id}" style="padding-left: 8px;">${help.title}</a></p>
					</#if>
				</#list>
		</td>
	</table>
</div>
  </#if>
<#else>
	<@comm.errmsgbox/>
</#if>
</div>
<@comm.copyright/>
<@comm.footer/>