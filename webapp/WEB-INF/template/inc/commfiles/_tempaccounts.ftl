<#-- 
	描述：发帖临时账号模板 
	作者：黄磊 
	版本：v1.0 
-->
﻿<script type="text/javascript">
var tempaccounts = false;
function showusername()
{
    $("usernamelayer").innerHTML = "<input name='tempusername' type='text' id='tempusername' size='20' class='colorblue' onfocus=\"this.className='colorfocus';\" onblur=\"this.className='colorblue';\" value='${username}' onkeyup=\"$('passwordlayer').style.display='';\">&nbsp;[<a href='javascript:;' onclick='resetusername()'>恢复</a>]";
    tempaccounts = true;
    var i = 1;
    while(true)
    {
        var obj = $("albums" + i);
        if(obj == null) break;
        obj.options[0].selected = true;
        obj.disabled = true
        i++;
    }
}
function resetusername()
{
    $('passwordlayer').style.display='none';
    $("usernamelayer").innerHTML = "${username}&nbsp;[<a href='javascript:;' onclick='showusername()'>切换临时帐号</a>]";
    tempaccounts = false;
    var i = 1;
    while(true)
    {
        var obj = $("albums" + i);
        if(obj == null) break;
        obj.disabled = false
        i++;
    }
}
</script>
<tbody>
	<tr>
		<th>用户名</th>
		<td><h5 id="usernamelayer">
		<#if (userid>0)>
		${username}&nbsp;[<a href='javascript:;' onclick='showusername()'>切换临时帐号</a>]
		<#else>
			匿名 [<a href="login.action">登录</a>] [<a href="register.action">注册</a>]
		</#if></h5>
		</td>
	</tr>
</tbody>
<tbody id="passwordlayer" style="display:none">
	<tr>
		<th><label for="temppassword">密码</label></th>
		<td>
			<input name="temppassword" type="password" id="temppassword" size="20" />
		</td>
	</tr>
<#if isseccode>	
	<tr>
		<th><label for="vcode">验证码</label></th>
		<td><#include "_vcode.ftl"/></td>
	</tr>
</#if>
<#if config.secques==1>
	<tr>
		<th><label for="question">安全问题</label></th>
		<td>
			<select name="question" id="question">
			<option value="0" selected="selected">无</option>
			<option value="1">母亲的名字</option>
			<option value="2">爷爷的名字</option>
			<option value="3">父亲出生的城市</option>
			<option value="4">您其中一位老师的名字</option>
			<option value="5">您个人计算机的型号</option>
			<option value="6">您最喜欢的餐馆名称</option>
			<option value="7">驾驶执照的最后四位数字</option>
			</select>
		</td>
	</tr>
	<tr>
		<th><label for="answer">答案</label></th>
		<td><input name="answer" type="text" id="answer" size="50" /><br/>如果您设置了安全提问，请在此输入正确的问题和回答</td>
	</tr>
</#if>
</tbody>
