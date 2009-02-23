<#-- 
	描述：复选框
	作者：黄磊 
	版本：v1.0 
-->
<#macro checkBoxList id repeatColumns="0" hintTitle="" hintInfo="" hintHeight="30" hintShowType="up">
<span id="${id}" class="buttonlist" <#if hintTitle!="" && hintInfo!="">
	onmouseover="showhintinfo(this,0,0,'${hintTitle}','${hintInfo}','${hintHeight}','${hintShowType}','0');"
	onmouseout="hidehintinfo();"</#if> style="display:inline-block;width:100%;"><#if repeatColumns!="0"><table id="${id}" class="buttonlist" border="0" style="width:100%;"></#if><#nested></span><#if repeatColumns!="0"></table></#if>
</#macro>

<#macro checkBoxItem id value name="" checked="">
<input type="checkbox" name="<#if name!="">${name}<#else>${id}</#if>" value="${value}" <#if checked==value>checked="checked"</#if> id="${id}"/><label for="${id}"><#nested></label>
</#macro>