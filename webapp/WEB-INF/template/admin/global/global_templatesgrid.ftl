<#import "../../controls/control.ftl" as c>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN" >
<html>
<head>
    <title>模板列表</title>
    <link href="../styles/datagrid.css" type="text/css" rel="stylesheet" />
    <link href="../styles/dntmanager.css" type="text/css" rel="stylesheet" />
    <script type="text/javascript" src="../js/common.js"></script>
    <script type="text/javascript" src="../../javascript/menu.js"></script>
    <script type="text/javascript" src="../../javascript/common.js"></script>
    <script type="text/javascript">
	    function CreateTemplate(pathname)
	    {
	        if(confirm("生成" + pathname + "下所有模板的操作非常耗时,确认要继续吗？")) 
	        {
	            document.getElementById('success').style.display = 'block';
	            document.getElementById('Layer5').innerHTML='<BR /><table><tr><td valign=top><img border=0 src=../images/ajax_loading.gif  /></td><td valign=middle style=font-size:14px;>正在生成'+pathname+'文件夹下的模板, <BR />请稍等...<BR /></td></tr></table><BR />';
	            window.location="?createtemplate=" + pathname;
	        }
	    }
	    
	    function Check(form)
	    {
	        CheckAll(form);
	        checkedEnabledButton(form,'templateid','IntoDB','DelRec','DelTemplates');
	    }
    </script>
</head>
<body>
    <form id="Form1" method="post" >
    ${htmlBuilder}
        <@c.pageInfo id="info1"  icon="information" text="<ul><li>模板入库是将模板置为可用状态，让用户可以在前台使用此模板 </ul>  <ul><li>模板出库是将可用状态的模板置为不可用状态，用户在前台将无法继续使用此模板。注意模板出库并非将模板做物理性删除，如果以后想再次使用此模板，可以将其再次入库 <li>删除模板是将模板做物理性删除，此操作将不可恢复，请慎重使用！(列表的第一项是系统初始化模板,系统不允许删除)</ul>" />
        <@c.pageInfo id="PageInfo1"  icon="information" text="模板资源文件是存放在论坛根下templates文件夹中，模板文件则是存放在WEB-INF/templates文件夹，一套模板放在一个文件夹中。模板文件是以ftl为扩展名" />
         <table class="ntcplist" >

<tr class="head">

<td>模板列表</td>

</tr>

<tr>

<td>

<table class="datalist" cellspacing="0" rules="all" border="1" id="DataGrid1" style="border-collapse:collapse;">
	<tr class="category">
		<td nowrap="nowrap" style="border-color:#EAE9E1;border-width:1px;border-style:solid;width:20px;">
		<input title='选中/取消' onclick='Check(this.form)' type='checkbox' name='chkall' id='chkall' /></td>
		<td nowrap="nowrap" style="border-color:#EAE9E1;border-width:1px;border-style:solid;">模板名称</td>
		<td nowrap="nowrap" style="border-color:#EAE9E1;border-width:1px;border-style:solid;">存放路径</td>
		<td nowrap="nowrap" style="border-color:#EAE9E1;border-width:1px;border-style:solid;">版权</td>
		<td nowrap="nowrap" style="border-color:#EAE9E1;border-width:1px;border-style:solid;">作者</td>
		<td nowrap="nowrap" style="border-color:#EAE9E1;border-width:1px;border-style:solid;">创建日期</td>
		<td nowrap="nowrap" style="border-color:#EAE9E1;border-width:1px;border-style:solid;">模板版本</td>
		<td nowrap="nowrap" style="border-color:#EAE9E1;border-width:1px;border-style:solid;">论坛版本</td>
		<td nowrap="nowrap" style="border-color:#EAE9E1;border-width:1px;border-style:solid;">已入库</td>
		<td nowrap="nowrap" style="border-color:#EAE9E1;border-width:1px;border-style:solid;width:60px;">&nbsp;</td>
	   </tr>
	   <#list templates as template>
	   <tr onmouseover="this.className='mouseoverstyle'" onmouseout="this.className='mouseoutstyle'" style="cursor:hand;">
		<td nowrap="nowrap" style="border-color:#EAE9E1;border-width:1px;border-style:solid;">
							<input id="templateid" onclick="checkedEnabledButton(this.form,'templateid','IntoDB','DelRec','DelTemplates')" type="checkbox" 
								value="${template.templateid}_${template.name}"	name="templateid" />
					</td><td align="left" style="border-color:#EAE9E1;border-width:1px;border-style:solid;">
						    &nbsp;<span id="${template.name}" onmouseover="showMenu(this.id, 0, 0, 1, 0);" style="font-weight:bold">
							    ${template.name}&nbsp;
							    <img src="../images/eye.gif" style="vertical-align:middle" />
							</span>
							<div id="${template.name}_menu" style="display:none">
							    <img src="../../templates/${template.directory}/about.png" onerror="this.src='../../images/common/none.gif'" />
							</div>
					</td>
					<td style="border-color:#EAE9E1;border-width:1px;border-style:solid;">${template.name}</td>
					<td style="border-color:#EAE9E1;border-width:1px;border-style:solid;">${template.copyright}</td>
					<td style="border-color:#EAE9E1;border-width:1px;border-style:solid;">${template.author}</td>
					<td style="border-color:#EAE9E1;border-width:1px;border-style:solid;">${template.createdate}</td>
					<td style="border-color:#EAE9E1;border-width:1px;border-style:solid;">${template.ver}</td>
					<td style="border-color:#EAE9E1;border-width:1px;border-style:solid;">${template.fordntver}</td>
					<td style="border-color:#EAE9E1;border-width:1px;border-style:solid;">
						<span id="ctl${template_index}_Label1"><div align=center><img src=../images/state<#if template.templateid==0>3<#else>2</#if>.gif /></div></span>
					</td><td style="border-color:#EAE9E1;border-width:1px;border-style:solid;">
						<span id="ctl${template_index}_Label2"><a href=global_templatetree.action?path=${template.directory}&templateid=${template.templateid}&templatename=${template.name}>管理</a></span>
					</td>
	</tr>
	</#list>
</table></td></tr></TABLE>
<br />
        <p style="text-align:right;">
            <@c.button id="IntoDB" method="intoDB"  text=" 入 库 " enabled="true" scriptContent="document.getElementByid('Layer5').innerHTML='<BR /><table><tr><td valign=top><img border=0 src=../images/ajax_loading.gif  /></td><td valign=middle style=font-size:14px;>选中模板正在入库, 请稍等...<BR /></td></tr></table><BR />';">
            </@c.button>&nbsp;&nbsp;
            <@c.button id="DelRec" method="delRec" text=" 出 库 " enabled="true" buttonImgUrl="../images/del.gif"
                scriptContent="document.getElementByid('Layer5').innerHTML='<BR /><table><tr><td valign=top><img border=0 src=../images/ajax_loading.gif  /></td><td valign=middle style=font-size:14px;>选中模板正在出库, 请稍等...<BR /></td></tr></table><BR />';">
            </@c.button>&nbsp;&nbsp;
            <@c.button id="DelTemplates" method="delTemplates" text=" 删 除 " enabled="true" buttonImgUrl="../images/state1.gif"
               scriptContent="document.getElementByid('Layer5').innerHTML='<BR /><table><tr><td valign=top><img border=0 src=../images/ajax_loading.gif  /></td><td valign=middle style=font-size:14px;>正在删除选中模板, 请稍等...<BR /></td></tr></table><BR />';">
            </@c.button>
        </p>
    </form>
    ${footer}
</body>
</html>
