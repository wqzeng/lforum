<#-- 
	描述：按钮定义
	作者：黄磊 
	版本：v1.0 
-->
<#macro button id text buttonImgUrl="../images/submit.gif" hintTitle="" hintInfo="" type="submit" method="" enabled="false">
<#if hintTitle!="" && hintInfo!="">
<span id="${id}"  onmouseover="showhintinfo(this,0,0,'${hintTitle}','${hintInfo}','50','up');" onmouseout="hidehintinfo();">
</#if>
<span>
<button type="${type}" <#if enabled=="true">disabled="true"</#if> class="ManagerButton" id="${id}" name="${id}" value="${id}"
	onclick="this.disabled=true;document.getElementById('success').style.display = 'block';HideOverSels('success');doPost('${method}');"><img
	src="${buttonImgUrl}" />${text}</button>
</span><#if hintTitle!="" && hintInfo!=""></span></#if>
</#macro>