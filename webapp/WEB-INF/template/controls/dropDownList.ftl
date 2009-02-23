<#-- 
	描述：下拉列表框
	作者：黄磊 
	版本：v1.0 
-->
<#macro dropDownList id hintInfo="" hintTitle="">
<span id="${id}"  <#if hintInfo!="" && hintTitle!="">onmouseover="showhintinfo(this,0,0,'${hintTitle}','${hintInfo}','50','up');" onmouseout="hidehintinfo();"</#if>>
<select name="${id}" id="${id}">
<#nested>
</select>
</span>
</#macro>

<#macro dropDownItem value selected>
<option value="${value}" <#if value==selected>selected="selected"</#if>><#nested></option>
</#macro>