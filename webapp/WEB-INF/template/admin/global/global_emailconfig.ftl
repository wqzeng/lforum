<#import "../../controls/control.ftl" as c>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN" >
<html>
	<head>
		<title>邮箱设置</title>
		<script type="text/javascript" src="../js/common.js"></script>
		<link href="../styles/dntmanager.css" type="text/css" rel="stylesheet" />  
	</head>
	<body>
	<div class="ManagerForm">
		<form id="Form1" method="post" >
		${htmlBuilder}
		<fieldset>
		<legend style="background:url(../images/icons/icon41.jpg) no-repeat 6px 50%;">SMTP配置</legend>
		<table cellspacing="0" cellpadding="4" width="100%" align="center">
            <tr>
                <td  class="panelbox" align="left" width="50%">
                    <table width="100%">
                        <tr>
					        <td style="width: 80px">SMTP服务器:</td>
					        <td><@c.textBox id="smtp" value="${emailInfo.smtp}"   required="true" width="200" hintTitle="提示" hintInfo="设置发送邮件的SMTP服务器地址"></@c.textBox></td>
                        </tr>
                        <tr>
					        <td>系统邮箱名称:</td>
					        <td><@c.textBox id="sysemail" value="${emailInfo.sysemail}"  email="true" required="true" width="200" hintTitle="提示" hintInfo="设置发送邮件的邮箱地址"></@c.textBox></td>
                        </tr>
                        <tr>
					        <td>系统邮箱密码:</td>
					        <td><@c.textBox id="password" value="${emailInfo.password}"  required="true" width="200" hintTitle="提示" hintInfo="设置邮箱的密码"></@c.textBox></td>
                        </tr>
                    </table>
                </td>
                <td  class="panelbox" align="right" width="50%">
                    <table width="100%">
				        <tr>
					        <td style="width: 80px">SMTP端口:</td>
					        <td><@c.textBox id="port" value="${emailInfo.port}"  required="true" size="7" maxlength="5" hintTitle="提示" hintInfo="设置SMTP服务器的端口"></@c.textBox></td>
				        </tr>
				        <tr>
					        <td>用户名:</td>
					        <td><@c.textBox id="username"  value="${emailInfo.username}" required="true" width="200" hintTitle="提示" hintInfo="设置邮箱的用户名"></@c.textBox></td>
				        </tr>
                    </table>
                </td>
            </tr>
            <tr>
                <td colspan="2" align="center"><@c.button id="SaveInfo" text="提交"  /></td>
            </tr>
        </table>
		</fieldset>
		<fieldset>
		<legend style="background:url(../images/icons/icon40.jpg) no-repeat 6px 50%;">测试邮件配置</legend>
			<table cellspacing="0" cellpadding="4" width="100%" align="center">
				<tr>
					<td style="width: 80px">EMAIL:</td>
					<td style="width: 350px"><@c.textBox id="testEmail"  email="true" width="300" hintTitle="提示" hintInfo="设置要测试的邮箱地址,测试程序将发送一封邮件到测试邮箱中"></@c.textBox></td>
				    <td><@c.button id="SaveInfo" method="testEmail" text="提交"  /></td></tr>
			</table>
		</fieldset>
		<@c.hint hintImageUrl="../images" />
		</form>
		</div>
		${footer}
	</body>
</html>
