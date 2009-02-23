<#import "../../controls/control.ftl" as c>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN" >
<html>
<head>
    <title>公告列表</title>
    <link href="../styles/datagrid.css" type="text/css" rel="stylesheet" />
    <link href="../styles/dntmanager.css" type="text/css" rel="stylesheet" />
    <link href="../styles/calendar.css" type="text/css" rel="stylesheet" />
    <script type="text/javascript" src="../js/common.js"></script>
    <script type="text/javascript">
        function Check(form)
        {
            CheckAll(form);
            checkedEnabledButton(form,'id','DelRec');
        }
    </script>
</head>
<body>
    <form id="Form1" method="post" >
    ${htmlBuilder}
        <table class="ntcplist" >

<tr class="head">

<td>公告列表</td>

</tr>

<tr>

<td>

<table class="datalist" cellspacing="0" rules="all" border="1" id="DataGrid1" style="border-collapse:collapse;">
	<tr class="category">
		<td nowrap="nowrap" style="border-color:#EAE9E1;border-width:1px;border-style:solid;width:20px;"><input title='选中/取消' onclick='Check(this.form)' type='checkbox' name='chkall' id='chkall' /></td><td nowrap="nowrap" style="border-color:#EAE9E1;border-width:1px;border-style:solid;">&nbsp;</td><td nowrap="nowrap" style="border-color:#EAE9E1;border-width:1px;border-style:solid;">发布者用户名</td><td nowrap="nowrap" style="border-color:#EAE9E1;border-width:1px;border-style:solid;">公告标题</td><td nowrap="nowrap" style="border-color:#EAE9E1;border-width:1px;border-style:solid;">显示顺序</td><td nowrap="nowrap" style="border-color:#EAE9E1;border-width:1px;border-style:solid;">起始时间</td><td nowrap="nowrap" style="border-color:#EAE9E1;border-width:1px;border-style:solid;">结束时间</td>
	</tr>
	<#list annoncelist as annonce>
	<tr onmouseover="this.className='mouseoverstyle'" onmouseout="this.className='mouseoutstyle'" style="cursor:hand;">
		<td nowrap="nowrap" style="border-color:#EAE9E1;border-width:1px;border-style:solid;">
							<input id="id" onclick="checkedEnabledButton(this.form,'id','DelRec')" type="checkbox" value="${annonce.id}" name="id" />
		</td>
		<td nowrap="nowrap" style="border-color:#EAE9E1;border-width:1px;border-style:solid;">
							<a href="global_editannounce.action?id=${annonce.id}">编辑</a>
		</td>
		<td style="border-color:#EAE9E1;border-width:1px;border-style:solid;">${annonce.poster}</td>
		<td align="left" style="border-color:#EAE9E1;border-width:1px;border-style:solid;">${annonce.title}</td>
		<td style="border-color:#EAE9E1;border-width:1px;border-style:solid;">${annonce.displayorder}</td>
		<td style="border-color:#EAE9E1;border-width:1px;border-style:solid;">${annonce.starttime}</td>
		<td style="border-color:#EAE9E1;border-width:1px;border-style:solid;">${annonce.endtime}</td>
	</tr>
	</#list>
	<tr>
		<td class="datagridPager" align="left" valign="bottom" colspan="7"></td></tr></table><table class="datagridpage"><tr><td height="2"></td></tr><tr><td> <font color=black>共 1 页, 当前第 1 页, 共 2 条记录    &nbsp;&nbsp;</td>
	</tr>
</table></td></tr></TABLE>
        <p style="text-align:right;">
            <button type="button" class="ManagerButton" onclick="javascript:window.location.href='global_addannounce.action';">
                <img src="../images/add.gif" />添加公告
            </button>&nbsp;&nbsp;
            <@c.button id="DelRec"  text=" 删 除 " buttonImgUrl="../images/del.gif" enabled="false"></@c.button>
        </p>
    </form>
    ${footer}
</body>
</html>
