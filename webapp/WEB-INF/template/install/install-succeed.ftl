<#import "install-comm.ftl" as comm />
<@comm.header title="安装向导--安装完成！" />
<body>
    <div class="top">
      
    </div>
    <div class="con">
   <table cellspacing="0" cellpadding="0" border="0" id="WzdInstall" style="width:100%;border-collapse:collapse;">
	<tr style="height:100%;">
		<td>
                        <div id="WzdInstall_DivIntallComplete" class="left140">
                            <dl>
                                <dt class="check">
                                    <img alt="" src="${path}Install/Images/ico01.gif"/>安装完成。 </dt>
                                <dd class="message">
                                    已经成功安装 <b>${config.forumtitle}</b>！<br/>安装结束后，无需重命名或删除此安装文件，系统会自动限制此文件的访问。
<br/>请重新启动WEB服务器让设置生效！
<br/>请点击“<strong>完成</strong>”按钮关闭页面。                             
                                        
 
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 
                                    <div class="clearbox">
                                    
                                            
&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160; </div>
                                        
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                    
                                    <div class="clearbox"></div>
                                </dd>
                            </dl>
                        </div>
                    </td>
	</tr><tr>
		<td align="right">
                    <center>
                        <br />
                        <input type="button" onclick="javascript:window.close();" value="完成" class="button_link" />
                    </center>
                </td>
	</tr>
</table>
<div>
</div>
    </div>
<@comm.bottom />
</body>
</html>
