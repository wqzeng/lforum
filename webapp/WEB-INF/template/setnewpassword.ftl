<#-- 
	描述：设置新密码(密码找回)模板
	作者：黄磊 
	版本：v1.0 
-->
<#import "inc/page_comm.ftl" as comm />
<@comm.header/>
<div id="foruminfo">
	<div id="nav"><a href="${config.forumurl}" class="home">${config.forumtitle}</a> &raquo; 密码重置</div>
</div>

<#if ispost>
	<#if reqcfg.page_err==0>
		<@comm.msgbox/>
	<#else>
		<@comm.errmsgbox/>
	</#if>
<#else>
	<#if reqcfg.page_err==0>
<form id="form1" name="form1" method="post" action="setnewpassword.action#">	
<div class="mainbox">
	<h3>密码重置</h3>
	<table cellpadding="0" cellspacing="0" border="0" summary="密码重置">
		<tbody>
			<tr>
				<th><label for="newpassword">新密码</label></th>
				<td>
					<input name="newpassword" type="password" id="newpassword" size="25"  />
					<input name="uid" type="hidden" id="uid" value="${uid}" />
					<input name="authstr" type="hidden" id="authstr" value="${authstr}" />
				</td>
			</tr>
		</tbody>
		<tbody>
		<tr>
			<th><label for="confirmpassword">确认密码</label></th>
			<td class="formbody"><input name="confirmpassword" type="password" id="confirmpassword" size="25" >
			</td>
		</tr>
		</tbody>
	<#if isseccode>	
		<tbody>
			<tr>
				<th><label for="vcode">验证码</label></th>
				<td><@comm.vcode/></td>
			</tr>
		</tbody>
	</#if>			
		<tbody>
			<tr>
				<th>&nbsp;</th>
				<td>
					<input name="login" type="submit" id="login" value="确定" onclick="javascript:location.replace('?agree=yes')" />
					<input name="cancel" type="button" id="cancel" value="取消" onclick="javascript:location.replace('/')" />
				</td>
			</tr>
		</tbody>
	</table>
</div>					
</form>
</div>
<#else>
	<@comm.errmsgbox/>
</div>
</#if>
</#if>
<@comm.copyright/>
<@comm.footer/>
