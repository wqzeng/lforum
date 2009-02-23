<#-- 
	描述：按钮定义
	作者：黄磊 
	版本：v1.0 
-->
<#macro button id text buttonImgUrl="../images/submit.gif" type="submit" method="" enabled="false">
<span>
<button type="${type}" disabled="${enabled}" class="ManagerButton" id="${id}" name="${id}" value="${id}"
	onclick="this.disabled=true;document.getElementById('success').style.display = 'block';HideOverSels('success');doPost('${method}');"><img
	src="${buttonImgUrl}" />${text}</button>
</span>
</#macro>