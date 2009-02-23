<#import "../../controls/control.ftl" as c>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN" >
<html>
<head>
	<title>baseset</title>
	<script type="text/javascript" src="../js/common.js"></script>
	<link href="../styles/dntmanager.css" type="text/css" rel="stylesheet" />        
    <link href="../styles/modelpopup.css" type="text/css" rel="stylesheet" />
	<script type="text/javascript" src="../js/modalpopup.js"></script>
	<@c.validator "Form1" />
</head>
<body>
	<div class="ManagerForm">
	<form id="Form1" method="post">
	${htmlBuilder}
		<fieldset>
		    <legend style="background:url(../images/icons/icon2.jpg) no-repeat 6px 50%;">时间段设置</legend>
		        <table cellspacing="0" cellpadding="4" width="100%" align="center">
		            <tr>
		                <td  class="panelbox" width="50%" align="left">
		                    <table width="100%">
		                        <tr>
					                <td style="width: 120px">禁止访问时间段:</td>
					                <td>                        
					                    <@c.textareaResize id="visitbanperiods" hintShowType="down" cols="35" 
					                        hintInfo="每天该时间段内用户不能访问论坛, 请使用 24 小时时段格式, 每个时间段一行, 如需要也可跨越零点, 留空为不限制. 例如:每日晚 11:25 到次日早 5:05 可设置为: 23:25-5:05, 每日早 9:00 到当日下午 2:30 可设置为: 9:00-14:30.注意: 格式不正确将可能导致意想不到的问题. 所有时间段设置均以论坛系统默认时区为准, 不受用户自定义时区的影响" hintTitle="提示" hintHeight="0" value="${config.visitbanperiods}"></@c.textareaResize>
					                </td>
		                        </tr>
		                        <tr>
					                <td>发帖审核时间段:</td>
                                    <td>
					                    <@c.textareaResize id="postmodperiods" cols="35" 
					                        hintInfo="每天该时间段内用户发帖不直接显示, 需经版主或管理员人工审核才能发表, 格式和用法同上" hintTitle="提示" value="${config.postmodperiods}"></@c.textareaResize>
					                </td>
		                        </tr>
		                        <tr>
					                <td>禁止全文搜索时间段:</td>
                                    <td>
					                    <@c.textareaResize id="searchbanperiods"  cols="35"  
					                    hintInfo="每天该时间段内用户不能使用全文搜索, 格式和用法同上" hintTitle="提示" value="${config.searchbanperiods}"></@c.textareaResize>
					                </td>
		                        </tr>
		                    </table>
		                </td>
		                <td  class="panelbox" width="50%" align="right">
		                    <table width="100%">
				                <tr>
					                <td style="width: 120px">禁止发帖时间段:</td>
                                    <td>
                                        <@c.textareaResize id="postbanperiods"  cols="35" 
                                            hintInfo="每天该时间段内用户不能发帖, 格式和用法同上" hintTitle="提示" value="${config.postbanperiods}"></@c.textareaResize>
					                </td>
				                </tr>
				                <tr>
					                <td>禁止下载附件时间段:</td>
                                    <td>
					                    <@c.textareaResize id="attachbanperiods"  cols="35" 
					                        hintInfo="每天该时间段内用户不能下载附件, 格式和用法同上" hintTitle="提示" value="${config.attachbanperiods}"></@c.textareaResize>
					                </td>
				                </tr>
		                    </table>
		                </td>
		            </tr>
		        </table>
		    
		    
		    
			<table class="table1" cellspacing="0" cellpadding="4" width="100%" align="center">
			</table>
		</fieldset>	
		<@c.hint hintImageUrl="../images" />
		<div align="center">
		    <@c.button id="SaveInfo" text="提交"  />
		</div>		
	</form>
	</div>		
	${footer}
</body>
</html>
