<#-- 
	描述：新短消息模板  
	作者：黄磊 
	版本：v1.0 
-->
﻿<#if (newpmcount>0) && showpmhint>
<!--短信息 area start-->
<div class="mainbox">
	<#if (pmsound>0)>
		<bgsound src="sound/pm${pmsound}.wav" />
	</#if>
	<span class="headactions"><a href="usercpinbox.action" target="_blank">查看详情</a> <a href="###" onclick="document.getElementById('frmnewpm').submit();">不再提示</a></span>
	<h3>您有 ${newpmcount} 条新的短消息</h3>
	<table cellpadding="0" cellspacing="0" border="0" width="100%">
	<#list pmlist as pm>
	<tbody>
		<tr>
			<td style="width:53px;text-align:center;"><img src="templates/${templatepath}/images/message_${pm.new_}.gif" alt="短信息"/></td>
			<th><a href="usercpshowpm.action?pmid=${pm.pmid}">${pm.subject}</a></th>
			<td>
				<a href="userinfo.action?userid=${pm.usersByMsgfromid.uid}" target="_blank">${pm.msgfrom}</a>
				<span class="fontfamily">${pm.postdatetime}</span>
			</td>
		</tr>
	</tbody>
	</#list>
	</table>
	<form id="frmnewpm" name="frmnewpm" method="post" action="#">
		<input id="ignore" name="ignore" type="hidden" value="yes" />
	</form>
</div>
<!--短信息 area end-->
</#if>