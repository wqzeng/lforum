<#import "../../controls/control.ftl" as c>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
    <title>主菜单管理</title>
    <link href="../styles/datagrid.css" type="text/css" rel="stylesheet" />
    <link href="../styles/dntmanager.css" type="text/css" rel="stylesheet" />        
    <link href="../styles/modelpopup.css" type="text/css" rel="stylesheet" />
    <script type="text/javascript" src="../js/modalpopup.js"></script>
    <script type="text/javascript" src="../js/common.js"></script>
    <script type="text/javascript">
        function newMenu()
        {
            document.getElementById("opt").innerHTML = "新建主菜单";
            document.getElementById("menuid").value = "0";
            document.getElementById("mode").value = "new";
            document.getElementById("menutitle").value = "";
            document.getElementById("defaulturl").value = "";
            BOX_show('neworeditmainmenu');
        }
        function editMenu(menuid,menutitle,defaulturl)
        {
            document.getElementById("opt").innerHTML = "编辑主菜单";
            document.getElementById("menuid").value = menuid;
            document.getElementById("mode").value = "edit";
            document.getElementById("menutitle").value = menutitle;
            document.getElementById("defaulturl").value = defaulturl;
            BOX_show('neworeditmainmenu');
        }
        function chkSubmit()
        {
            if(document.getElementById("menutitle").value == "")
            {
                alert("主菜单名称不能为空！");
                document.getElementById("menutitle").focus();
                return false;
            }
            if(document.getElementById("defaulturl").value == "")
            {
                if(!confirm("您确认要将默认展现页面地址置空吗？"))
                {
                    document.getElementById("defaulturl").focus();
                    return false;
                }
            }
            document.getElementById("form1").submit();
            return true;
        }
    </script>
</head>
<body>
<br />
    <form id="Form1" method="post">
    ${htmlBuilder}
	    <@c.pageInfo id="info1"  icon="information" text="<li>主菜单项必须在其下没有子菜单时才可删除!</li><li>编辑完菜单后必须点击<b>生成菜单</b>按钮才能使编辑生效!</li><li>建议在编辑菜单前先进入<b>备份管理</b>中对当前未编辑的菜单进行备份,如果修改不当还可以从备份中恢复!</li>" />
	    <table class="ntcplist" >

<tr class="head">

<td>菜单管理</td>

</tr>

<tr>

<td>

<table class="datalist" cellspacing="0" rules="all" border="1" id="DataGrid1" style="border-collapse:collapse;">
	<tr class="category">
		<td nowrap="nowrap" style="border-color:#EAE9E1;border-width:1px;border-style:solid;">序号</td>
		<td nowrap="nowrap" style="border-color:#EAE9E1;border-width:1px;border-style:solid;">主菜单名称</td>
		<td nowrap="nowrap" style="border-color:#EAE9E1;border-width:1px;border-style:solid;">默认展现页面地址</td>
		<td nowrap="nowrap" style="border-color:#EAE9E1;border-width:1px;border-style:solid;">系统菜单</td>
		<td nowrap="nowrap" style="border-color:#EAE9E1;border-width:1px;border-style:solid;">操作</td>
	</tr>
	<#list toptabmenuList as element>
	<tr onmouseover="this.className='mouseoverstyle'" onmouseout="this.className='mouseoutstyle'" style="cursor:hand;">
		<td nowrap="nowrap" style="border-color:#EAE9E1;border-width:1px;border-style:solid;">${element.elementTextTrim("id")}</td>
		<td nowrap="nowrap" style="border-color:#EAE9E1;border-width:1px;border-style:solid;">${element.elementTextTrim("title")}</td>
		<td align="left" style="border-color:#EAE9E1;border-width:1px;border-style:solid;">${element.elementTextTrim("defaulturl")}</td>
		<td style="border-color:#EAE9E1;border-width:1px;border-style:solid;"><#if element.elementTextTrim("system")!="0">是<#else>否</#if></td>
		<td style="border-color:#EAE9E1;border-width:1px;border-style:solid;">
                    <a href="javascript:;" onclick="editMenu('${element.elementTextTrim("id")}','${element.elementTextTrim("title")}','${element.elementTextTrim("defaulturl")}');">编辑</a>&nbsp;
       <#if element.elementTextTrim("mainmenulist")!="">
                    删除&nbsp;
       <#else>
       <a href="managemainmenu.action?mode=del&menuid=${element.elementTextTrim("id")}" onclick="return confirm(\"您确认要删除此菜单项吗?\")">删除</a>
       </#if>
                    <a href="managesubmenu.action?menuid=${element.elementTextTrim("id")}">管理子菜单</a>
         </td>
	</tr>
	</#list>
	<tr>
		<td class="datagridPager" align="left" valign="bottom" colspan="5"></td></tr></table><table class="datagridpage"><tr><td height="2"></td></tr><tr><td> <font color=black>   &nbsp;&nbsp;</td>
	</tr>
</table></td></tr></TABLE>
	    <p style="text-align:right;">
	        <button type="button" class="ManagerButton" id="Button2" onclick="newMenu();"><img src="../images/add.gif"/> 新 建 </button>
	        <button type="button" class="ManagerButton" id="Button3" onclick="window.location='managemenubackupfile.action';"><img src="../images/zip.gif"/>备份管理</button>
	        <@c.button id="createMenu" method="createMenu" text="生成菜单"  />        
	    </p>
        <div id="BOX_overlay" style="background: #000; position: absolute; z-index:100; filter:alpha(opacity=50);-moz-opacity: 0.6;opacity: 0.6;"></div>
        <div id="neworeditmainmenu" style="display: none; background :#fff; padding:10px; border:1px solid #999; width:400px;">
            <div class="ManagerForm">
			    <fieldset>
			    <legend id="opt" style="background:url(../images/icons/icon53.jpg) no-repeat 6px 50%;">新建主菜单</legend>
			    <table cellspacing="0" cellPadding="4" class="tabledatagrid" width="80%">
			        <tr>
					    <td width="30%">
					        主菜单名称:
					        <input type="hidden" id="menuid" name="menuid" value="0" />
					        <input type="hidden" id="mode" name="mode" value="" />
					    </td>
					    <td width="70%"><input id="menutitle"  name="menutitle" type="text" maxlength="8" size="10"class="FormBase" onfocus="this.className='FormFocus';" onblur="this.className='FormBase';" /></td>
				    </tr>
				    <tr>
					    <td>默认展现<br />页面地址:</td>
					    <td><input id="defaulturl" name="defaulturl" type="text" maxlength="100" size="30" class="FormBase" onfocus="this.className='FormFocus';" onblur="this.className='FormBase';" /></td>
				    </tr>
				    <tr>
				        <td colspan="2" align="center">
				            <button type="button" class="ManagerButton" id="AddNewRec" onclick="chkSubmit();"><img src="../images/add.gif"/> 提 交 </button>&nbsp;&nbsp;
				            <button type="button" class="ManagerButton" id="Button1" onclick="BOX_remove('neworeditmainmenu');"><img src="../images/state1.gif"/> 取 消 </button>
				        </td>
				    </tr>
			    </table>
			    </fieldset>
		    </div>
        </div>
    </form>
    <div id="setting" />
    ${footer}
</body>
</html>
