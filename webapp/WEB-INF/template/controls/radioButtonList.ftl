<#-- 
	描述：单选按钮组 
	作者：黄磊 
	版本：v1.0 
	【id】 唯一标识
	【hintTitle】气泡提示标题
	【hintInfo】气泡提示信息
	【checkedvalue】默认选中值
-->
<#macro radioButtonList id="" onclick="" repeatColumns="" hintTitle="" hintInfo="" hintHeight="30" hintShowType="up" checkedvalue="0">
	<span id="${id}"
	<#if hintTitle!="" && hintInfo!="">
	onmouseover="showhintinfo(this,0,0,'${hintTitle}','${hintInfo}','${hintHeight}','${hintShowType}','0');"
	onmouseout="hidehintinfo();"</#if>>
	<#if repeatColumns!="1"><span id="${id}" class="buttonlist" style="display: inline-block; border-width: 0px; border-style: Dotted;"><#else>
	<table id="${id}" class="buttonlist" onclick="${onclick}" border="0" style="border-width:0px;border-style:Dotted;"></#if>
	<#nested><#if repeatColumns!="1"></span><#else></table></#if></span>
</#macro>

<#macro radioItem name value checked id="">
<input type="radio" <#if id!="">id="${id}"</#if> name="${name}" value="${value}"<#if value==checked>checked="checked"</#if> /><label for="${name}"><#nested></label>
</#macro>