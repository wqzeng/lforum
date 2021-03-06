<#-- 
	描述：投票页面模板
	作者：黄磊 
	版本：v1.0 
-->
﻿<#if ispoll>
<!--投票区开始-->
<div class="box pollpanel">
	<form id="formpoll" name="formpoll" method="post" action="poll.action?topicid=${topicid}">
		<h4>
		投票：<strong>${topictitle}</strong>
		<#if pollinfo.multiple==1>
		-多选(最多可选${pollinfo.maxchoices}项)
		</#if>
		-截止时间：${pollinfo.expiration}
		</h4>
	<div class="postmessage">
		<table border="0" cellpadding="0" cellspacing="0" summary="pollpanel">
		<#list polloptionlist as polloption>
		<tbody>
		<tr>
			<td style="padding:3px 0;">
			<#if allowvote>
				<#if pollinfo.multiple==1>
					<input type="checkbox" name="pollitemid" value="${polloption.polloptionid}" onclick='checkbox(this)'/>
				<#else>
					<input type="radio" name="pollitemid"  value="${polloption.polloptionid}"  />
				</#if>
			</#if>
				${polloption.name}
			</td>
			<td width="420">
			    <#if showpollresult>
				<div class="optionbar">
					<div style="width:${polloption.percentwidth}px"></div>
				</div>				
				<div id="voterlist${polloption.polloptionid}" style="display:none;clear:both;">
				  <#if useradminid==1 && polloption.value!=0>       
					 ${polloption.votername}
				  </#if>
				</div>
			<#else>
				&nbsp;
			</#if>
			</td>
			<td>
			<#if showpollresult>
			<strong>${polloption.value}</strong>票 / ${polloption.percent*100}%
				<#if useradminid==1 && polloption.value!=0> 
				( <a href="javascript:void(0);" onclick="javascript:displayvoter($('voterlist${polloption.polloptionid}'));">查看该项投票人</a> )
				</#if>
			<#else>
			&nbsp;
			</#if>	
			</td>
		</tr>
		</tbody>
		</#list>
		<script language="javascript">
		var max_obj = ${pollinfo.maxchoices};
	    var p = 0;
	    
	    function checkbox(obj) {
	        if(obj.checked) {
		        p++;
		        for (var i = 0; i < $('formpoll').elements.length; i++) {
			        var e = $('formpoll').elements[i];
			        if(p == max_obj) {
				        if(e.name.match('pollitemid') && !e.checked) {
					        e.disabled = true;
				        }
			        }
		        }
	        } else {
		        p--;
		        for (var i = 0; i < $('formpoll').elements.length; i++) {
			        var e = $('formpoll').elements[i];
			        if(e.name.match('pollitemid') && e.disabled) {
				        e.disabled = false;
			        }
		        }
	        }
        }	   
         
	    function displayvoter(objid) {
	        if(objid.style.display == 'block') {
	           objid.style.display = 'none';
	        }
	        else {
	           objid.style.display = 'block';
	        }
	    }	    
	    </script>		
		<tbody>
		<tr>
			<td colspan="3" style="padding:4px 0;">
				<#if useradminid==1 && voters!="">   
					<button onclick="expandoptions('ticketvoterlist');" />查看投票用户名单>></button>
				</#if>
				<#if usergroupinfo.allowvote==1>
					<#if allowvote>
						<input type="submit" name="Submit" value="投票"/>
					<#else>							
						提示: 您已经投过票或者投票已经过期
					</#if>			
				<#else>
					抱歉,您没有参与投票的权限
				</#if>
			 </td>
		</tr>
		</tbody>
		</table>
	</div>
	</form>
</div>
<#if useradminid==1 && voters!="">       
<div id="ticketvoterlist" style="display:none;" class="box">
	<h4>投票用户名单</h4>
	<div class="postmessage">
	<table width="96%" border="0" cellpadding="4" cellspacing="0" summary="投票用户名单">
		<tr>
			<td>${voters}</td>
		</tr>
	</table>
	</div>
</div>
</#if>
<!--投票区结束-->
</#if>