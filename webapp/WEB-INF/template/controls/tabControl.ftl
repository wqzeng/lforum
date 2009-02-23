<#-- 
	描述：选项卡
	作者：黄磊 
	版本：v1.0 
-->
<#macro tabControl id tabScriptPath default height>
<SCRIPT language="javascript" src="${tabScriptPath}"></SCRIPT>
<LINK href="../styles/tab.css" type="text/css" rel="stylesheet">
<div Class="tabs" ID="${id}_Tab" >
<input name="${id}" type="hidden" id="${id}" value="${id}:${default}" />
	<ul>
		<#nested>
	</ul>
</div>
</#macro>

<#macro tabPage id caption tabid default>
<li id="${tabid}:${id}_li" class="<#if id==default>CurrentTabSelect<#else>TabSelect</#if>" onclick="tabpage_selectonclient(this,'${tabid}:${id}');"><a href="#" <#if id==default>class="current"</#if> onfocus="this.blur();">${caption}</a></li>
</#macro>

<#macro tabItem id tabid default>
<div id="${tabid}:${id}" class="tab-page" style="display: <#if id==default>block<#else>none</#if>;background: #fff;">
<#nested>
</div>
</#macro>