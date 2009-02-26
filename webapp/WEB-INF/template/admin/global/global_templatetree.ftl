<#import "../../controls/control.ftl" as c>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<html>
<head id="Head1">
	<title></title>
	<link rev="stylesheet" media="all" href="../styles/default.css" type="text/css" rel="stylesheet" />
	<link href="../styles/dntmanager.css" type="text/css" rel="stylesheet" />
	<link href="../styles/tab.css" type="text/css" rel="stylesheet" />
    <script type="text/javascript" src="../js/common.js"></script>
	<script type="text/javascript">	
        /*function getCookie(name)//取cookies函数        
        {
            var arr = document.cookie.match(new RegExp("(^| )"+name+"=([^;]*)(;|$)"));
            if(arr != null) 
                return unescape(arr[2]); 
            return null;
        }*/
	
        function nodeCheckChanged(node)
        {
            var status = "未选取"; 
            if (node.Checked) status = "选取"; 
        }  
        
        function checkedEnabledButton1()
        {
            for (var i = 0; i < arguments[0].elements.length; i++)
            {
                var e = arguments[0].elements[i];
                if (e.type == "checkbox" && e.checked)
                {
                    for(var j = 1; j < arguments.length; j++)
                    {
                        document.getElementById(arguments[j]).disabled = false;
                    }
                    return;
                }
            }
            for(var j = 1; j < arguments.length; j++)
            {
                document.getElementById(arguments[j]).disabled = true;
            }
        }
        
        function Check(form)
        {
            CheckAll(form);
            checkedEnabledButton1(form,'TabControl1:tabPage22:CreateTemplate','TabControl1:tabPage22:DeleteTemplateFile')
        }
        
        /*function getTemplateList()
        {
            var commontemplate = getCookie("commontemplate");
            if(commontemplate == null) return;
            var tempstr = "";
            var filelist = commontemplate.split(",");
            for(var i = 0 ; i < filelist.length ; i++)
            {
                if(filelist[i].indexOf(".config") != -1)
                {
                    tempstr += "<img src='../images/config.gif' />" + filelist[i] + "&nbsp;";
                }
                else
                {
                    tempstr += "<img src='../images/htm.gif' />" + filelist[i] + "&nbsp;";
                }
            }
            return tempstr;
        }*/
	</script>
</head>
<body topmargin="3">
	<form id="Form1" >
	${htmlBuilder}
		<#if templateid==1>
        <@c.pageInfo id="info1"  icon="information" text="<ul><li>您正在修改默认模板,为了扩充其他模板的方便,强烈建议您不要对默认模板的内容进行修改. </li></ul><ul><li>点击相应的模板文件进行编辑</li></ul>" />
        <#else>
        <@c.pageInfo id="PageInfo1"  icon="information" text="点击相应的模板文件进行编辑" />
        </#if>
	    &nbsp;&nbsp; <b>当前模板: ${templatename}</b><br /><br />
	    <table class="table1" cellspacing="0" cellpadding="4" width="100%" border="0">
	        <tr>
	            <td width="3"></td>
	            <td>
	                <@c.tabControl id="TabControl1" tabScriptPath="../js/tabstrip.js" height="100%" default="tabPage1">
		    			<@c.tabPage caption="模板文件" id="tabPage1" tabid="TabControl1" default="tabPage1"/>
		    			<@c.tabPage caption="其他文件" id="tabPage2" tabid="TabControl1" default="tabPage1"/>
					</@c.tabControl>
					<div id="TabControl1tabarea" class="tabarea">
						<@c.tabItem tabid="TabControl1" id="tabPage1" default="tabPage1">
	     	                <div style="OVERFLOW: auto;HEIGHT: 400px">
	     	                <table id="treeView1" class="buttonlist" border="0" style="width:100%;">
	     	                <#assign td=0>
	     	                <#list templateFiles as file>
	     	                	<#if td=0><tr></#if>
									<td><label>
									<img src=../images/htm.gif border="0"  style="position:relative;top:5 px;height:16 px"> ${file[0]} <a href="global_templatesedit.action?path=${path}&filename=${file[1]}&templateid=${templateid}&templatename=${templatename}" title="编辑${file[0]}模板文件"><img src='../images/editfile.gif' border='0'/></a>
									</label></td>
								<#assign td=td+1>
								<#if td=3><#assign td=0></tr></#if>
	     	                </#list>
	     	                </table>
		                    </div>
		                    <br />		                   
			            </@c.tabItem>
			            <@c.tabItem tabid="TabControl1" id="tabPage2" default="tabPage1">
		                    <div style="OVERFLOW: auto;HEIGHT: 400px" align="left">
		                    <table id="treeView2" class="buttonlist" border="0" style="width:100%;">
	     	                <#list otherFiles as file>
	     	                	 <img src="../images/${file[2]}.gif" border="0"> <a href="global_templatesedit.action?path=${path}&filename=${file[1]}&templateid=${templateid}&templatename=${templatename}" title="${file[2]}文件">${file[0]}</a><br />
	     	                </#list>
	     	                </table>                   				
			                </div>						
			            </@c.tabItem>
	                </div>
		        </td>
		    </tr>
		</table>
		<br />	
		<button type="button" class="ManagerButton" id="Button3" onclick="window.location='global_templatesgrid.action';"><img src="../images/arrow_undo.gif"/> 返 回 </button>		
		<span id="lblClientSideCheck" class="hint">&nbsp;</span>
		<span id="lblCheckedNodes" class="hint">&nbsp;</span>
		<span id="lblServerSideCheck" class="hint">&nbsp;</span>
		<script type="text/javascript">
              document.getElementByid("lblClientSideCheck").innerText = document.getElementByid("lblServerSideCheck").innerText;
              //document.getElementByid("templatelist").innerHTML = getTemplateList();
		</script>			
		<br />
	</form>
	${footer}
</body>
</html>
