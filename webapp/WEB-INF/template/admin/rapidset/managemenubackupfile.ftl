<#import "../../controls/control.ftl" as c>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
    <title>无标题页</title>
    <link href="../styles/datagrid.css" type="text/css" rel="stylesheet" />
    <link href="../styles/dntmanager.css" type="text/css" rel="stylesheet" />        
    <link href="../styles/modelpopup.css" type="text/css" rel="stylesheet" />
    <script type="text/javascript" src="../js/modalpopup.js"></script>
    <script type="text/javascript" src="../js/common.js"></script>
    <script type="text/javascript">
        function Check(form)
        {
            CheckAll(form);
            checkedEnabledButton(form,'backupname','Delbackupfile');
        }
    </script>
</head>
<body>
<br />
    <form id="Form1" method="post">
    ${htmlBuilder}
<table class="ntcplist" >
<tr class="head">
<td>菜单备份管理</td>
</tr>
<tr>
<td>
<table class="datalist" cellspacing="0" rules="all" border="1" id="DataGrid1" style="border-collapse:collapse;">
	<tr class="category">
		<td nowrap="nowrap" style="border-color:#EAE9E1;border-width:1px;border-style:solid;width:20px;">
		<input title='选中/取消' onclick='Check(this.form)' type='checkbox' name='chkall' id='chkall' />
		</td>
		<td nowrap="nowrap" style="border-color:#EAE9E1;border-width:1px;border-style:solid;">备份日期</td>
		<td nowrap="nowrap" style="border-color:#EAE9E1;border-width:1px;border-style:solid;">操作</td>
	</tr>
	<#list backupFiles as file>
	<tr onmouseover="this.className='mouseoverstyle'" onmouseout="this.className='mouseoutstyle'" style="cursor:hand;">
		<td nowrap="nowrap" style="border-color:#EAE9E1;border-width:1px;border-style:solid;">
			<input id="backupname" onclick="checkedEnabledButton(this.form,'backupname','Delbackupfile')" type="checkbox" value="${file[0]}"	name="backupname" />
		</td>
		<td nowrap="nowrap" style="border-color:#EAE9E1;border-width:1px;border-style:solid;">${file[1]}</td>
		<td style="border-color:#EAE9E1;border-width:1px;border-style:solid;">
            <a href="managemenubackupfile.action?filename=${file[0]}" onclick='return confirm("您确认要将 navmenu 的备份菜单恢复吗？\n\t注意：恢复将覆盖当前使用中的菜单！");'>恢复此备份</a>
        </td>
	</tr>
	</#list>
	<tr>
		<td class="datagridPager" align="left" valign="bottom" colspan="3"></td>
	</tr>
	</table>
	<table class="datagridpage"><tr><td height="2"></td></tr>
	<tr><td> <font color=black>&nbsp;&nbsp;</td>
	</tr>
</table></td></tr></TABLE>
	<p style="text-align:right;">
	    <@c.button id="backupfile"  text="备 份" buttonImgUrl="../images/zip.gif" method="backupfile"></@c.button>&nbsp;&nbsp;
		<@c.button id="Delbackupfile"  text="删 除" buttonImgUrl="../images/del.gif" method="delbackupfile" enabled="false" ></@c.button>&nbsp;&nbsp;
	    <button type="button" class="ManagerButton" id="Button3" onclick="window.location='managemainmenu.action';"><img src="../images/arrow_undo.gif"/> 返 回</button>
	</p>
    </form>
    ${footer}
</body>
</html>
