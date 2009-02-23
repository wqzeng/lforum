<#import "../../controls/control.ftl" as c>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>registerandvisit</title>
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
                <legend style="background: url(../images/icons/icon21.jpg) no-repeat 6px 50%;">用户注册设置</legend>
                    <table cellspacing="0" cellpadding="4" width="100%" align="center">
                        <tr>
                            <td  class="panelbox" width="50%" align="left">
                                <table width="100%">
		                            <tr>
                                        <td style="width: 110px">允许新用户注册:</td>
                                        <td><@c.radioButtonList id="regstatus" hintTitle="提示" 
						    					hintInfo="选择&amp;quot;否&amp;quot;将禁止游客注册成为会员, 但不影响过去已注册的会员的使用">
						    					<@c.radioItem name="regstatus" value="1" checked="${config.regstatus}">是</@c.radioItem>
						 						<@c.radioItem name="regstatus" value="0" checked="${config.regstatus}">否</@c.radioItem>   
											</@c.radioButtonList>
										</td>	                                        
		                            </tr>
		                            <tr>
                                        <td>用户资料中是否必<br />须填写实名选项:</td>
                                        <td><@c.radioButtonList id="realnamesystem" hintTitle="提示" 
						    					hintInfo="选择&amp;quot;是&amp;quot;填写实名将出现在必填内容区,否则将出现在选填内容区">
						    					<@c.radioItem name="realnamesystem" value="1" checked="${config.realnamesystem}">是</@c.radioItem>
						 						<@c.radioItem name="realnamesystem" value="0" checked="${config.realnamesystem}">否</@c.radioItem>   
											</@c.radioButtonList>
										</td>                                       
		                            </tr>
		                            <tr>
                                        <td>用户信息保<br />留关键字:</td>
                                        <td><@c.textareaResize id="censoruser" hintTitle="提示" hintInfo="用户在其用户信息(如用户名、昵称、自定义头衔等)中无法使用这些关键字. 每个关键字一行, 可使用通配符 &amp;quot;*&amp;quot; 如 &amp;quot;*版主*&amp;quot;(不含引号)" value="${config.censoruser}"/>
										</td>                                       
		                            </tr>
		                            <tr>
                                        <td>新用户注册验证:</td>
                                        <td><@c.radioButtonList id="regverify" hintTitle="提示" 
						    					hintInfo="选择&quot;无&quot;用户可直接注册成功;选择&quot;Email 验证&quot;将向用户注册 Email 发送一封验证邮件以确认邮箱的有效性;选择&quot;人工审核&quot;将由管理员人工逐个确定是否允许新用户注册">
						    					<@c.radioItem name="regverify" value="0" checked="${config.regverify}">无</@c.radioItem>
						 						<@c.radioItem name="regverify" value="1" checked="${config.regverify}">Email验证</@c.radioItem>
						 						<@c.radioItem name="regverify" value="2" checked="${config.regverify}">人工审核</@c.radioItem>    
											</@c.radioButtonList>
										</td>                                       
		                            </tr>
		                            <tr>
                                        <td>Email 允许地址:</td>
                                        <td>
                                            <@c.textareaResize id="accessemail" hintTitle="提示" 
                                            	hintInfo="只允许某些域名结尾的邮箱注册, 每行一个域名, 例如 @hotmail.com.注意:此项开启时, 下面的&amp;quot;Email 禁止地址&amp;quot;项设置无效" value="${config.accessemail}"/>
                                        </td>
		                            </tr>		                            
                                    <tr>
                                        <td>Email 禁止地址:</td>
                                        <td>
                                            <@c.textareaResize id="censoremail" hintTitle="提示" 
                                            	hintInfo="由于一些大型邮件服务提供商会过滤论坛程序发送的有效邮件, 您可以要求新用户不得以某些域名结尾的邮箱注册, 每行一个域名, 例如 @hotmail.com" value="${config.censoremail}"/>
                                        </td>
                                    </tr>
		                            <tr>
                                        <td>IP注册间隔限制:</td>
                                        <td>
                                            <@c.textBox id="regctrl" required="true" size="5" maxlength="4" digits="true" hintTitle="提示"
                                                hintInfo="同一 IP 在本时间间隔内将只能注册一个帐号, 限制对自修改后的新注册用户生效, 0 为不限制" value="${config.regctrl}" />(单位:小时)
                                        </td>
		                            </tr>		                            
                                    <tr>
                                        <td>新手见习期限:</td>
                                        <td>
                                            <@c.textBox id="newbiespan" required="true" size="5" maxlength="4" digits="true"
                                                hintTitle="提示" hintInfo="新注册用户在本期限内将无法发帖, 不影响版主和管理员, 0 为不限制" value="${config.newbiespan}"/>(单位:分钟)
                                        </td>
                                    </tr>
		                        </table>
                            </td>
                            <td  class="panelbox" width="50%" align="right">
                                <table width="100%">
                                    <tr>
                                        <td style="width: 110px">注册时是否显<br />示高级选项:</td>
                                        <td>
                                            <@c.radioButtonList id="regadvance" hintTitle="提示"
                                                hintInfo="在注册页面是否显示选填内容">
                                                <@c.radioItem name="regadvance" value="1" checked="${config.regadvance}">是</@c.radioItem>
						 						<@c.radioItem name="regadvance" value="0" checked="${config.regadvance}">否</@c.radioItem>   
											</@c.radioButtonList>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>允许同一 Email<br />注册不同用户:</td>
                                        <td>
                                            <@c.radioButtonList id="doublee" hintTitle="提示"
                                                hintInfo="选择&amp;quot;否&amp;quot; ,一个 Email 地址只能注册一个用户名">
                                                <@c.radioItem name="doublee" value="1" checked="${config.doublee}">是</@c.radioItem>
						 						<@c.radioItem name="doublee" value="0" checked="${config.doublee}">否</@c.radioItem>   
											</@c.radioButtonList>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>特殊 IP 注册限制:</td>
                                        <td>
                                            <@c.textareaResize id="ipregctrl" hintTitle="提示" 
                                            	hintInfo="当用户处于本列表中的 IP 地址时, 每 72 小时将至多只允许注册一个帐号. 每个 IP 一行, 例如 &amp;quot;192.168.*.*&amp;quot;(不含引号) 可匹配 192.168.0.0~192.168.255.255范围内的所有地址, 留空为不设置" value="${config.ipregctrl}"/>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>注册许可协议:</td>
                                        <td>
                                            <@c.radioButtonList id="rules" hintTitle="提示"
                                                hintInfo="新用户注册时显示许可协议">
                                                <@c.radioItem name="rules" value="1" checked="${config.rules}">是</@c.radioItem>
						 						<@c.radioItem name="rules" value="0" checked="${config.rules}">否</@c.radioItem>   
											</@c.radioButtonList>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>许可协议内容:</td>
                                        <td>
                                            <@c.textareaResize id="rulestxt" hintTitle="提示" hintInfo="注册许可协议的详细内容" value="${config.rulestxt}"/>
                                        </td>
                                    </tr>  
                                     <tr>
                                        <td>发送欢迎短消息:</td>
                                        <td>
                                            <@c.radioButtonList id="welcomems" hintTitle="提示"
                                                hintInfo="选择&amp;quot;是&amp;quot;将自动向新注册用户发送一条欢迎短消息">
                                                <@c.radioItem name="welcomems" value="1" checked="${config.welcomems}">是</@c.radioItem>
						 						<@c.radioItem name="welcomems" value="0" checked="${config.welcomems}">否</@c.radioItem>   
											</@c.radioButtonList>
                                        </td>
		                            </tr>
		                            <tr>
                                        <td>欢迎短消息内容:</td>
                                        <td>
                                            <@c.textareaResize id="welcomemsgtxt" hintTitle="提示" 
                                            	hintInfo="系统发送的欢迎短消息的内容" value="${config.welcomemsgtxt}"/>
                                        </td>
		                            </tr>
		                        </table>
                            </td>
                        </tr>
                    </table>
            </fieldset>
            <@c.hint hintImageUrl="../images" />
            <fieldset>
                <legend style="background: url(../images/icons/icon17.jpg) no-repeat 6px 50%;">访问控制</legend>
                    <table cellspacing="0" cellpadding="4" width="100%" align="center">
                        <tr>
                            <td  class="panelbox" width="50%" align="left">
                                <table width="100%">
		                            <tr>
                                        <td style="width: 110px">隐藏无权访<br />问的论坛:</td>
                                        <td>
                                            <@c.radioButtonList id="hideprivate" hintTitle="提示"
                                                hintInfo="不在列表中显示当前用户无权访问的论坛">
                                                <@c.radioItem name="hideprivate" value="1" checked="${config.hideprivate}">是</@c.radioItem>
						 						<@c.radioItem name="hideprivate" value="0" checked="${config.hideprivate}">否</@c.radioItem>   
											</@c.radioButtonList>
                                        </td>
		                            </tr>
		                            <tr>
                                        <td>IP 访问列表:</td>
                                        <td>
                                            <@c.textareaResize id="ipaccess" hintTitle="提示" 
                                            	hintInfo="只有当用户处于本列表中的 IP 地址时才可以访问本论坛, 列表以外的地址访问将视为 IP 被禁止, 仅适用于诸如企业、学校内部论坛等极个别场合. 本功能对管理员没有特例, 如果管理员不在此列表范围内将同样不能登录, 请务必慎重使用本功能. 每个 IP 一行, 例如 &amp;quot;192.168.*.*&amp;quot;(不含引号) 可匹配 192.168.0.0~192.168.255.255 范围内的所有地址, 留空为所有 IP 除明确禁止的以外均可访问" value="${config.ipaccess}"/>
                                        </td>
		                            </tr>
		                        </table>
		                    </td>
		                    <td  class="panelbox" width="50%" align="right">
                                <table width="100%">
                                    <tr>
                                        <td style="width: 110px">IP 禁止访问列表:</td>
                                        <td>
                                            <@c.textareaResize id="ipdenyaccess" hintTitle="提示" 
                                            	hintInfo="当用户处于本列表中的 IP 地址时将禁止访问本论坛. 每个 IP 一行, 例如 &amp;quot;192.168.*.*&amp;quot;(不含引号) 可匹配 192.168.0.0~192.168.255.255 范围内的所有地址" value="${config.ipdenyaccess}"/>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>管理员后台<br />IP访问列表:</td>
                                        <td>
                                            <@c.textareaResize id="adminipaccess" hintTitle="提示" 
                                            	hintInfo="只有当管理员(超级版主及版主不在此列)处于本列表中的 IP 地址时才可以访问论坛系统设置, 列表以外的地址访问将无法访问, 但仍可访问论坛前端用户界面, 请务必慎重使用本功能. 每个 IP 一行, 例如 &amp;quot;192.168.*.*&amp;quot;(不含引号) 可匹配 192.168.0.0~192.168.255.255 范围内的所有地址, 留空为所有 IP 除明确禁止的以外均可访问系统设置" value="${config.adminipaccess}"/>
                                        </td>
                                    </tr>
		                        </table>
		                    </td>
		                </tr>
		            </table>
            </fieldset>
            <div class="Navbutton">
                <@c.button id="SaveInfo" text=" 提 交 " />
            </div>
        </form>
    </div>
    ${footer}
</body>
</html>
