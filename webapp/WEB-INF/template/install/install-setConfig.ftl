<#import "install-comm.ftl" as comm />
<@comm.header title="安装向导--设置配置文件" />
</head>
<body>
    <div class="top">
      
    </div>
    <div class="con">
        <form name="form1" method="post" action="${path}install/install!succeed.action" id="form1">
        <@s.token />
     <table cellspacing="0" cellpadding="0" border="0" id="WzdInstall" style="width:100%;border-collapse:collapse;">
	<tr style="height:100%;">
		<td>
                        <div id="WzdInstall_DivInstall4" class="left140">
                            <dl>
                                <dt class="check">
                                    <img alt="" src="${path}Install/Images/ico01.gif" />下面进行配置文件设置。</dt>
                                <dd>
                                    <table cellspacing="1" cellpadding="1" class="table_date">
                                        <tr>
                                            <td style="width: 30%">
                                                论坛名称：</td>
                                            <td>
                                                <input name="webName" type="text" value="${config.forumtitle}" id="webName" class="inputtext" style="width:150px;" />
                                        </tr>  
                                         <tr>
                                            <td style="width: 30%">
                                                论坛URL：</td>
                                            <td>
                                                <input name="webUrl" type="text" value="${config.forumurl}" id="webUrl" class="inputtext" style="width:200px;" />
                                        </tr> 
                                        <tr>
                                            <td colspan="2">
                                                <div class="center">
                                                   <span id="ErrorMessage" style="color:Red;"><#if reqcfg?exists>${reqcfg.msgbox_text}</#if></span></div>
                                            </td>
                                        </tr>
                                    </table>
 
                                </dd>
                            </dl>
                        </div>
                                                                                                                        <div class="left140">
                            <div class="left140_cen">
                            		<@comm.first/>
                                    <input type="submit"  value="下一步" class="button_link" />
                            </div>
                        </div>
                    </td>
	</tr><tr>
		<td align="right">
                    <center>
                    </center>
                </td>
	</tr>
</table>
<div>
</div></form>
    </div>
<@comm.bottom />
</body>
</html>
