<HTML>
<HEAD>
<title>管理员控制台登录</title>
<link href="styles/dntmanager.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="js/common.js"></script>
<script type="text/javascript">
		if(top.location!=self.location)
			{
				top.location.href = "syslogin.action";
			}
		</script>
</HEAD>
<BODY style="background: #f4f6f7;">
<FORM id="Form1" method="post">
<div id="LoginBar">
<ul>
	<span class="td1"> ${msg}<br />
	<br />
	</span>
	<li class="LoginTop"><img src="images/login-top.gif" /></li>
	<li class="FormNav">
	<dl>
		<dt><label>用户名:</label> 
		<#if name?exists>
			<input name="UserName" type="text" value="${name}" id="UserName"
				class="nofocus" onfocus="this.className='nofocus';"
				onblur="this.className='nofocus';" readonly="true" size="25"
				style="width: 200px" />
		<#else>
			<input name="UserName" type="text" value="" id="UserName" size="25"
				style="width: 200px" />
		</#if></dt>
		<dd><label>密&nbsp;&nbsp;&nbsp;&nbsp;码:</label><input
			name="PassWord" type="password" id="PassWord" class="FormBase"
			onfocus="this.className='FormFocus';"
			onblur="this.className='FormBase';" size="20" style="width: 200px" />
		<dd><label>验证码:</label><input class="FormCode" id="vcode"
			onkeydown="if(event.keyCode==13)  document.getElementById('login').focus();"
			type="text" size="20" name="vcode" autocomplete="off"> <img
			id="vcodeimg" style="cursor: hand"
			onclick="this.src='../tools/verifyImagePage.action?time=' + Math.random()"
			title="点击刷新验证码" align="absMiddle" src="" /> <script
			type="text/javascript">
                        document.getElementById('vcodeimg').src='../tools/verifyImagePage.action?id=${olid}&time=' + Math.random();
	document.getElementById('vcode').value = "";
</script></dd>

		<dd><input id="login" type="submit" value=""
			style="width: 60px; height: 26px; border: 0; background: url(images/button.gif) no-repeat left top; cursor: pointer; margin-left: 65px;"></dd>

	</dl>
	</li>
	<li><img src="images/login-bottom.gif" /></li>
</ul>

</div>

</FORM>
<div id="copyright">${footer}</div>

</BODY>

</HTML>
