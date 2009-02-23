<#import "../../controls/control.ftl" as c>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN" >
<html>
<head>
    <title>安全控制</title>
    <script type="text/javascript" src="../js/common.js"></script>
    <script type="text/javascript">
        function setstatus(obj)
        {
            if(obj.id == "seccodestatus_select_5")
            {
                document.getElementById("seccodestatus_select_2").checked = obj.checked;
                document.getElementById("seccodestatus_select_2").disabled = obj.checked;
            }
            
            if(obj.id == "seccodestatus_select_2")
            {
                document.getElementById("seccodestatus_select_5").checked = obj.checked;
                document.getElementById("seccodestatus_select_5").disabled = obj.checked;
            }
            
            if(obj.id == "seccodestatus_select_6")
            {
                //document.getElementById("seccodestatus_select_2").checked = obj.checked;
                //document.getElementById("seccodestatus_select_9").disabled = obj.checked;
                document.getElementById("seccodestatus_select_3").checked = obj.checked;
                document.getElementById("seccodestatus_select_3").disabled = obj.checked;
                //document.getElementById("seccodestatus_select_5").checked = obj.checked;
                //document.getElementById("seccodestatus_select_5").disabled = obj.checked;
            }
            checkselecedpage();
        }
    </script>
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
                <legend style="background: url(../images/icons/icon22.jpg) no-repeat 6px 50%;">安全控制</legend>
                    <table cellspacing="0" cellpadding="4" width="100%" align="center">
                        <tr>
                            <td class="panelbox" colspan="2">
                                <table width="100%">
                                    <tr>
                                        <td style="width: 110px">验证码显示方式:</td>
                                        <td class="td1"><@c.dropDownList id="VerifyImage"><@c.dropDownItem value="" selected="">系统默认验证码</@c.dropDownItem></@c.dropDownList></td>
                                    </tr>
                                </table>
                            </td>
                        </tr>
                        <tr>
                            <td class="panelbox" colspan="2">
                                <table width="100%">
                                    <tr>
                                        <td style="width: 110px">使用验证码<br />的页面列表:</td>
                                        <td style="width: 200px">                                        
                                            <@c.textBox id="seccodestatus" readonly="true"
                                                multiLine="true" height="90px" rows="4" cols="30" 
                                                hintInfo="请选取相应的页面复选框, 并在相应页面模板表单中加入<@comm.vcode/>校验码子模板, 就可以增加校验码判断功能."
                                                hintTitle="提示" hintShowType="down" value="${seccodestatus}"></@c.textBox>&nbsp;
                                        </td>    
                                        <td style="padding:1px">
                                            <table id="seccodestatus_select">
                                                <tr>
                                                    <td style="padding:0px">
                                                        <input id="seccodestatus_select_0" type="checkbox" name="seccodestatus_select:0" onclick="checkselecedpage()" value="register" />
                                                        <label for="seccodestatus_select_0">新用户注册</label>
                                                    </td>
                                                    <td style="padding: 0px">
                                                        <input id="seccodestatus_select_5" type="checkbox" name="seccodestatus_select:5" onclick="setstatus(this);" value="showforum" />
                                                        <label for="seccodestatus_select_5">查看(已设置密码)版块</label>
                                                    </td>
                                                    <td style="padding:0px">
                                                        <input id="seccodestatus_select_1" type="checkbox" name="seccodestatus_select:1" onclick="checkselecedpage();" value="login" />
                                                        <label for="seccodestatus_select_1">用户登录</label>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td style="padding:0px">
                                                        <input id="seccodestatus_select_6" type="checkbox" name="seccodestatus_select:6" onclick="setstatus(this)" value="showtopic" />
                                                        <label for="seccodestatus_select_6">快速回复(主题页)</label>
                                                    </td>
                                                    <td style="padding:0px">
                                                        <input id="seccodestatus_select_2" type="checkbox" name="seccodestatus_select:2" onclick="setstatus(this);" value="posttopic" />
                                                        <label for="seccodestatus_select_2">发表主题</label>
                                                    </td>
                                                    <td style="padding:0px">
                                                        <input id="seccodestatus_select_7" type="checkbox" name="seccodestatus_select:7" onclick="checkselecedpage()" value="usercpprofile" />
                                                        <label for="seccodestatus_select_7">修改个人密码</label>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td style="padding:0px">
                                                        <input id="seccodestatus_select_3" type="checkbox" name="seccodestatus_select:3" onclick="checkselecedpage()" value="postreply" />
                                                        <label for="seccodestatus_select_3">发表回复</label>
                                                    </td>
                                                    <td style="padding:0px">
                                                        <input id="seccodestatus_select_8" type="checkbox" name="seccodestatus_select:8" onclick="checkselecedpage()" value="editpost" />
                                                        <label for="seccodestatus_select_8">修改帖子</label>
                                                    </td>
                                                    <td style="padding:0px">
                                                        <input id="seccodestatus_select_4" type="checkbox" name="seccodestatus_select:4" onclick="checkselecedpage()" value="usercppostpm" />
                                                        <label for="seccodestatus_select_4">发送短消息</label>
                                                    </td>
                                                </tr>
                                                <tr>
                                                </tr>
                                            </table>
                                        </td>
                                    </tr>
                                </table>
                            </td>
                        </tr>                        
                        <tr>
                            <td  class="panelbox" width="50%" align="left">
                                <table width="100%">
                                    <tr>
                                        <td style="width: 110px">最大在线人数:</td>
                                        <td>
                                            <@c.textBox id="maxonlines" value="${config.maxonlines}"
                                                hintInfo="请设置合理的数值, 范围 10~65535, 建议设置为平均在线人数的 10 倍左右"
                                                hintTitle="提示" digits="true" required="true" maxlength="5"
                                                size="6" min="10" max="65535" width="50"></@c.textBox>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>搜索时间限制:</td>
                                        <td class="td2">
                                            <@c.textBox id="searchctrl" value="${config.searchctrl}"
                                                hintInfo="两次搜索间隔小于此时间将被禁止, 0 为不限制"
                                                hintTitle="提示" digits="true" required="true" maxlength="5"
                                                size="6" width="50"></@c.textBox>(单位:秒)
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>用户登录时是否<br />启用安全问题:</td>
                                        <td>
                                            <@c.radioButtonList id="secques" hintInfo="用户登录时是否启用安全问题" hintTitle="提示">
                                                <@c.radioItem name="secques" value="1" checked="${config.secques}">是</@c.radioItem>
						 						<@c.radioItem name="secques" value="0" checked="${config.secques}">否</@c.radioItem>
                                            </@c.radioButtonList>
                                        </td>
                                    </tr>
                                </table>
                            </td>
                            <td  class="panelbox" width="50%" align="right">
                                <table width="100%">
                                    <tr>
                                        <td style="width: 110px">发帖灌水预防:</td>
                                        <td>
                                            <@c.textBox id="postinterval" value="${config.postinterval}"
                                                hintInfo="两次发帖间隔小于此时间, 或两次发送短消息间隔小于此时间的二倍将被禁止, 0 为不限制"
                                                hintTitle="提示" digits="true" required="true" maxlength="5"
                                                size="6" width="50"></@c.textBox>(单位:秒)
                                        </td>
                                    </tr>
                                    <tr>
                                        
                                        <td>60 秒最大搜索次数:</td>
                                        <td>
                                            <@c.textBox id="maxspm" value="${config.maxspm}"
                                                hintInfo="论坛系统每 60 秒系统响应的最大搜索次数, 0 为不限制. 注意: 如果服务器负担较重, 建议设置为 5, 或在 5~20 范围内取值, 以避免过于频繁的搜索造成数据表被锁"
                                                hintTitle="提示" digits="true" required="true" maxlength="3"
                                                size="4" width="50"></@c.textBox>
                                        </td>
                                    </tr>                                   
                                </table>
                            </td>
                        </tr>
                    </table>
            </fieldset>
            <@c.hint hintImageUrl="../images" />
            <div class="Navbutton">
                <@c.button id="SaveInfo" text=" 提 交 " />
            </div>
            <script type="text/javascript">
                var seccodestatus='${config.seccodestatus.trim()}';
                loadseccodestatus();
                function loadseccodestatus()
                {
	                if(seccodestatus.indexOf('register')>=0) 
	                {
		                document.getElementById("seccodestatus_select_0").checked=true;
	                }
	                if(seccodestatus.indexOf('login')>=0) 
	                {
		                document.getElementById("seccodestatus_select_1").checked=true;
	                }
	                if(seccodestatus.indexOf('posttopic')>=0) 
	                {
		                document.getElementById("seccodestatus_select_2").checked=true;
	                }
	                if(seccodestatus.indexOf('postreply')>=0) 
	                {
		                document.getElementById("seccodestatus_select_3").checked=true;
	                }
	                if(seccodestatus.indexOf('usercppostpm')>=0) 
	                {
		                document.getElementById("seccodestatus_select_4").checked =true;
	                }
	                if(seccodestatus.indexOf('showforum')>=0) 
	                {
		                document.getElementById("seccodestatus_select_5").checked=true;
	                }
	                if(seccodestatus.indexOf('showtopic')>=0) 
	                {
		                document.getElementById("seccodestatus_select_6").checked=true;
	                }
	                if(seccodestatus.indexOf('usercpnewpassword')>=0) 
	                {
		                document.getElementById("seccodestatus_select_7").checked=true;
	                }
	                if(seccodestatus.indexOf('editpost')>=0) 
	                {
		                document.getElementById("seccodestatus_select_8").checked=true;
	                }  
                }

                function checkselecedpage()
                {
	                document.getElementById("seccodestatus").value='';
                    var selectstr='';
                    if(document.getElementById("seccodestatus_select_0").checked)
                    {
                       selectstr+='register.action\r\n';
                    }
                    if(document.getElementById("seccodestatus_select_1").checked)
                    {
                       selectstr+='login.action\r\n';
                    }
                    if(document.getElementById("seccodestatus_select_2").checked)
                    {
                       selectstr+='posttopic.action\r\n';
                    }
                    if(document.getElementById("seccodestatus_select_3").checked)
                    {
                       selectstr+='postreply.action\r\n';
                    }
                    if(document.getElementById("seccodestatus_select_4").checked)
                    {
                       selectstr+='usercppostpm.action\r\nusercpshowpm.action\r\n';
                    }
                    if(document.getElementById("seccodestatus_select_5").checked)
                    {
                       selectstr+='showforum.action\r\n';
                    }
                    if(document.getElementById("seccodestatus_select_6").checked)
                    {
                       selectstr+='showtopic.action\r\najax.action\r\nshowdebate.action\r\nshowtree.action\r\nshowbonus.action\r\n';
                    }
                    if(document.getElementById("seccodestatus_select_7").checked)
                    {
                       selectstr+='usercpnewpassword.action\r\n';
                    }
                    if(document.getElementById("seccodestatus_select_8").checked)
                    {
                       selectstr+='editpost.action\r\n';
                    }                    
	                document.getElementById("seccodestatus").value=selectstr;
                }      
            </script>
        </form>
    </div>
    ${footer}
</body>
</html>
