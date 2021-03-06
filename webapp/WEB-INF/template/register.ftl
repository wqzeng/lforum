<#-- 
	描述：用户注册页模板
	作者：黄磊 
	版本：v1.0 
-->
<#import "inc/page_comm.ftl" as comm />
<@comm.header/>
<div id="nav"><a href="${config.forumurl}">${config.forumtitle}</a> &raquo; 用户注册</div>

<!--reg start-->
<#if agree?default("")=="">
	<#if reqcfg.page_err==0>
		<#if config.rules==1>
		<form id="form1" name="form1" method="post" action="">
		<div class="mainbox formbox regbox">
		<h1>用户注册协议</h1>
		<table cellspacing="0" cellpadding="0" width="100%" align="center" class="register">
			<tbody>
			<tr>
				<td>
				<textarea name="textarea" cols="" rows="" readonly="readonly" style="width:700px;height:320px;margin:10px 0;">${config.rulestxt}</textarea>
				</td>
			</tr>
			</tbody>
			<tbody>
			<tr>
			<td style="padding:2px;">
				<input name="agree" type="hidden" value="true" />
			  <input disabled="disabled" type="submit" id="btnagree" value="我同意" />
			  &nbsp;&nbsp;
			  <input name="cancel" type="button" id="cancel" value="不同意" onClick="javascript:location.replace('main.action')" />				  
				<script type="text/javascript">
				var secs = 5;
				var wait = secs * 1000;
				$("btnagree").value = "同 意(" + secs + ")";
				$("btnagree").disabled = true;
				for(i = 1; i <= secs; i++) {
						window.setTimeout("update(" + i + ")", i * 1000);
				}
				window.setTimeout("timer()", wait);
				function update(num, value) {
						if(num == (wait/1000)) {
								$("btnagree").value = "同 意";
						} else {
								printnr = (wait / 1000) - num;
								$("btnagree").value = "同 意(" + printnr + ")";
						}
				}
				function timer() {
						$("btnagree").disabled = false;
						$("btnagree").value = "同 意";
				}
				</script>
				 </td>
			 </tr>
			 </tbody>
		</table>
		</div>
		</form>
		<#--
		<script type="text/javascript">
		location.replace('register.action?agree=yes')
		</script>
		-->
		</#if>
	<#else>
		<@comm.errmsgbox/>
	</#if>
	</div>
<#else>

<#if createuser=="">
<form id="form1" name="form1" method="post" action="?createuser=1">
 
<div class="mainbox formbox">
	<h1>填写注册信息</h1>
	<table summary="注册" cellspacing="0" cellpadding="0">
	<thead>
		<tr>
			<th>基本信息 ( * 为必填项)</th>
			<td>&nbsp;</td>
		</tr>
	</thead>
	<tbody>
	<tr>
		<th><label for="username">用户名 * </label></th>
		<td><input name="username" type="text" id="username" maxlength="20" size="20"  onkeyup="checkusername(this.value);" /><span id="checkresult">不超过20个字符</span></td>
	</tr>
	</tbody>
	<tbody>
	<tr>
		<th><label for="password">密码 * </label></th>
		<td><input name="password" type="password" id="password" size="20" onblur="return checkpasswd(this);" /><span id="pshowmsg"></span></td>
	</tr>

	</tbody>	
	<tbody id="passwdpower" style="display: none;">
	<tr><th>密码强度</th><td>
	 <script type="text/javascript">
				var PasswordStrength ={
					Level : ["极佳","一般","较弱","太短"],
					LevelValue : [15,10,5,0],//强度值
					Factor : [1,2,5],//字符加数,分别为字母，数字，其它
					KindFactor : [0,0,10,20],//密码含几种组成的加数 
					Regex : [/[a-zA-Z]/g,/\d/g,/[^a-zA-Z0-9]/g] //字符正则数字正则其它正则
					}
					
				PasswordStrength.StrengthValue = function(pwd)
				{
					var strengthValue = 0;
					var ComposedKind = 0;
					for(var i = 0 ; i < this.Regex.length;i++)
					{
						var chars = pwd.match(this.Regex[i]);
						if(chars != null)
						{
							strengthValue += chars.length * this.Factor[i];
							ComposedKind ++;
						}
					}
					strengthValue += this.KindFactor[ComposedKind];
					return strengthValue;
				} 
				
				PasswordStrength.StrengthLevel = function(pwd)
				{
					var value = this.StrengthValue(pwd);
					for(var i = 0 ; i < this.LevelValue.length ; i ++)
					{
						if(value >= this.LevelValue[i] )
							return this.Level[i];
					}
				}
			 
				function checkpasswd(o)
				{
					var pshowmsg;
					if(o.value.length<6)  {
					   pshowmsg="<img src='templates/${templatepath}/images/check_error.gif'/><span>不得少于6个字符</span>";
					} else {
					   var showmsg=PasswordStrength.StrengthLevel(o.value);

					   switch(showmsg) {
					   case "太短": showmsg+=" <img src='images/level/1.gif' width='88' height='11' />";break;
					   case "较弱": showmsg+=" <img src='images/level/2.gif' width='88' height='11' />";break;
					   case "一般": showmsg+=" <img src='images/level/3.gif' width='88' height='11' />";break;
					   case "极佳": showmsg+=" <img src='images/level/4.gif' width='88' height='11' />";break;
					   }
					   pshowmsg="<img src='templates/${templatepath}/images/check_right.gif'/>";
					   $('showmsg').innerHTML = showmsg;
					}

					$('pshowmsg').innerHTML = pshowmsg;
					$("passwdpower").style.display = "";
				}
				function checkemail(strMail)
				{
				    var str;
					if(strMail.length==0) return false; 
					var objReg = new RegExp("[A-Za-z0-9-_]+@[A-Za-z0-9-_]+[\.][A-Za-z0-9-_]") 
					var IsRightFmt = objReg.test(strMail) 
					var objRegErrChar = new RegExp("[^a-z0-9-._@]","ig") 
					var IsRightChar = (strMail.search(objRegErrChar)==-1) 
					var IsRightLength = strMail.length <= 60 
					var IsRightPos = (strMail.indexOf("@",0) != 0 && strMail.indexOf(".",0) != 0 && strMail.lastIndexOf("@")+1 != strMail.length && strMail.lastIndexOf(".")+1 != strMail.length) 
					var IsNoDupChar = (strMail.indexOf("@",0) == strMail.lastIndexOf("@")) 
                    if(IsRightFmt && IsRightChar && IsRightLength && IsRightPos && IsNoDupChar) 
                     {str="<img src='templates/${templatepath}/images/check_right.gif'/>"}
					 else
					 {str="<img src='templates/${templatepath}/images/check_error.gif'/>Email 地址无效，请返回重新填写。";}
					
                  $('isemail').innerHTML=str;
				
				}
				function htmlEncode(source, display, tabs)
				{
					function special(source)
					{
						var result = '';
						for (var i = 0; i < source.length; i++)
						{
							var c = source.charAt(i);
							if (c < ' ' || c > '~')
							{
								c = '&#' + c.charCodeAt() + ';';
							}
							result += c;
						}
						return result;
					}
					
					function format(source)
					{
						// Use only integer part of tabs, and default to 4
						tabs = (tabs >= 0) ? Math.floor(tabs) : 4;
						
						// split along line breaks
						var lines = source.split(/\r\n|\r|\n/);
						
						// expand tabs
						for (var i = 0; i < lines.length; i++)
						{
							var line = lines[i];
							var newLine = '';
							for (var p = 0; p < line.length; p++)
							{
								var c = line.charAt(p);
								if (c === '\t')
								{
									var spaces = tabs - (newLine.length % tabs);
									for (var s = 0; s < spaces; s++)
									{
										newLine += ' ';
									}
								}
								else
								{
									newLine += c;
								}
							}
							// If a line starts or ends with a space, it evaporates in html
							// unless it's an nbsp.
							newLine = newLine.replace(/(^ )|( $)/g, '&nbsp;');
							lines[i] = newLine;
						}
						
						// re-join lines
						var result = lines.join('<br />');
						
						// break up contiguous blocks of spaces with non-breaking spaces
						result = result.replace(/  /g, ' &nbsp;');
						
						// tada!
						return result;
					}

					var result = source;
					
					// ampersands (&)
					result = result.replace(/\&/g,'&amp;');

					// less-thans (<)
					result = result.replace(/\</g,'&lt;');

					// greater-thans (>)
					result = result.replace(/\>/g,'&gt;');
					
					if (display)
					{
						// format for display
						result = format(result);
					}
					else
					{
						// Replace quotes if it isn't for display,
						// since it's probably going in an html attribute.
						result = result.replace(new RegExp('"','g'), '&quot;');
					}

					// special characters
					result = special(result);
					
					// tada!
					return result;
				}


				var profile_username_toolong = '<img src=\'templates/${templatepath}/images/check_error.gif\'/>对不起，您的用户名超过 20 个字符，请输入一个较短的用户名。';
				var profile_username_tooshort = '<img src=\'templates/${templatepath}/images/check_error.gif\'/>对不起，您输入的用户名小于3个字符, 请输入一个较长的用户名。';
				var profile_username_pass = "<img src='templates/${templatepath}/images/check_right.gif'/>";

				function checkusername(username)
				{
					var unlen = username.replace(/[^\x00-\xff]/g, "**").length;

					if(unlen < 3 || unlen > 20) {
						$("checkresult").innerHTML =(unlen < 3 ? profile_username_tooshort : profile_username_toolong);
						return;
					}
					ajaxRead("tools/ajax.action?t=checkusername&username=" + escape(username), "showcheckresult(obj,'" + username + "');");
				}

				function showcheckresult(obj, username)
				{
					var res = obj.getElementsByTagName('result');
					var resContainer = $("checkresult");

					var result = "";
					if (res[0] != null && res[0] != undefined)
					{
						if (res[0].childNodes.length > 1) {
							result = res[0].childNodes[1].nodeValue;
						} else {
							result = res[0].firstChild.nodeValue;    		
						}
					}
					if (result == "1")
					{
						resContainer.innerHTML = "<img src=\'templates/${templatepath}/images/check_error.gif\'/><font color='#009900'>对不起，您输入的用户名 \"" + htmlEncode(username, true, 4) + "\" 已经被他人使用或被禁用，请选择其他名字后再试。</font>";
					}
					else
					{
						resContainer.innerHTML = profile_username_pass;
					}
				}
				function checkSetting()
				{
					if ($('receiveuser').checked)
					{
						$('showhint').disabled = false;
					}
					else
					{			
						$('showhint').checked = false;
						$('showhint').disabled = true;
					}
				}
				
				function checkdoublepassword()
				{
				  var pw1=$('password').value;
				  var pw2=$('password2').value;
				  if(pw1=='' &&  pw2=='')
				  {
				  return;
				  }
				  var str;
				  
				 if(pw1!=pw2)
				 {
				str ="<img src='templates/${templatepath}/images/check_error.gif'/>两次输入的密码不一致";
				 }
				 else
				 {
				 str="<img src='templates/${templatepath}/images/check_right.gif'/>";
				 
				 }
				$('password2tips').innerHTML=str;
				}
			</script>
			<script type="text/javascript" src="javascript/ajax.js"></script>
	<strong id="showmsg"></strong></td></tr>
	</tbody>
	
	<tbody>
	<tr>
		<th><label for="password2">确认密码 * </label></th>
		<td><input name="password2" type="password" id="password2" size="20" onblur="checkdoublepassword()"/><span id="password2tips"></span></td>
	</tr>
	</tbody>
	<tbody>
	<tr>
		<th><label for="email">Email * </label></th>
		<td><input name="email" type="text" id="email" size="20"  onblur="checkemail(this.value)"/><span id="isemail"></span></td>
	</tr>
	</tbody>
	<#if config.realnamesystem==1>
		<@comm.realnamesystem/>
	</#if>
	<#if isseccode>
	<tbody>
	<tr>
		<th><label for="vcode">验证码 * </label></th>
		<td><@comm.vcode/></td>
	</tr>
	</tbody>
	</#if>
	<tbody>
	<tr>
		<th>&nbsp;</th>
		<td><input name="submit" type="submit" value="创建用户"/>  <input type="button" onclick="javascript:window.location.replace('./main.action')" value="取消"/></td>
	</tr>
	</tbody>
	</table>
</div>
<#if config.regadvance==1>	
<div class="mainbox formbox">
	<span class="headactions">
		<a href="###" onclick="toggle_collapse('regoptions');"><img id= "regoptions_img" src="templates/${templatepath}/images/collapsed_no.gif" alt="展开" /></a>
	</span>
	<h1>填写可选项</h1>
	<table summary="注册 高级选项" cellspacing="0" cellpadding="0" id="regoptions" style="display: none;">
		<thead>
		<tr>
			<th>找回密码问题</th>
			<td>&nbsp;</td>
		</tr>
		</thead>
		<tbody>
		<tr>
			<th><label for="question">问题</label></th>
			<td>
				<select name="question" id="question">
				  <option value="0" selected="selected">无</option>
				  <option value="1">母亲的名字</option>
				  <option value="2">爷爷的名字</option>
				  <option value="3">父亲出生的城市</option>
				  <option value="4">您其中一位老师的名字</option>
				  <option value="5">您个人计算机的型号</option>
				  <option value="6">您最喜欢的餐馆名称</option>
				  <option value="7">驾驶执照的最后四位数字</option>
				</select>
			</td>
		</tr>
		</tbody>
		<tbody>		
		<tr>
			<th><label for="answer">答案</label></th>
			<td>
				<input name="answer" type="text" id="answer" size="30" />
			</td>
		</tr>
		</tbody>
		<thead>
		<tr>
			<th>个人信息</th>
			<td>&nbsp;</td>
		</tr>
		</thead>
		<#if config.realnamesystem==0>
			<@comm.realnamesystem/>
		</#if>
		<tbody>
		<tr>
			<th><label for="gender">性别</label></th>
			<td>
				<input type="radio" name="gender" value="1" style="InPutRadio"/>男
				<input type="radio" name="gender" value="2"  style="InPutRadio"/>女
				<input name="gender" type="radio" value="0" checked="checked"  style="InPutRadio"/>保密
			</td>
		</tr>
		</tbody>
		<tbody>
		<tr>
			<th><label for="nickname">昵称</label></th>
			<td>
				<input name="nickname" type="text" id="nickname" size="20" />
			</td>
		</tr>
		</tbody>
		<tbody>	
		<tr>
			<th><label for="bday">生日</label></th>
			<td>
				<input name="bday_y" type="text" id="bday_y" size="4" maxlength="4" />年
				<input name="bday_m" type="text" id="bday_m" size="2"  maxlength="2"/>月
				<input name="bday_d" type="text" id="bday_d" size="2"  maxlength="2"/>日
			</td>
		</tr>
		</tbody>
		<tbody>
		<tr>
			<th><label for="location">来自</label></th>
			<td>
				<input name="location" type="text" id="location" size="20" />
			</td>
		</tr>
		</tbody>
		<tbody>
		<tr>
			<th><label for="msn">MSN Messenger</label></th>
			<td>
				<input name="msn" type="text" id="msn" size="30" />
			</td>
		</tr>
		</tbody>
		<tbody>
		<tr>
			<th><label for="yahoo">Yahoo Messenger</label></th>
			<td>
				<input name="yahoo" type="text" id="yahoo" size="30" />
			</td>
		</tr>
		</tbody>
		<tbody>
		<tr>
			<th><label for="skype">Skype</label></th>
			<td>
				<input name="skype" type="text" id="skype" size="30" />
			</td>
		</tr>
		</tbody>
		<tbody>
		<tr>
			<th><label for="ICQ">ICQ</label></th>
			<td>
				<input name="icq" type="text" id="icq" size="12" />
			</td>
		</tr>
		</tbody>
		<tbody>
		<tr>
			<th><label for="qq">QQ</label></th>
			<td>
				<input name="qq" type="text" id="qq" size="12" />
			</td>
		</tr>
		</tbody>
		<tbody>
		<tr>
			<th><label for="homepage">主页</label></th>
			<td>
				<input name="homepage" type="text" id="homepage" size="30" />
			</td>
		</tr>
		</tbody>
		<tbody>
		<tr>
			<th><label for="bio">自我介绍</label></th>
			<td>
				<textarea name="bio" cols="50" rows="6" id="bio" style="height:95px;width:85%;"></textarea>
			</td>
		</tr>
		</tbody>
		<thead>
		<tr>
			<th>论坛使用喜好设置:</th>
			<td>&nbsp;</td>
		</tr>
		</thead>
		<tbody>
		<tr>
			<th><label for="templateid">风格</label></th>
			<td>
				<select name="templateid" id="templateid" >
				  <option value="0" selected>默认</option>
				  <#list templatelist as template>
					<option value="${template.templateid}">${template.name}</option>
				  </#list>
				</select>
			</td>
		</tr>
		</tbody>
		<tbody>
		<tr>
			<th><label for="tpp">每页主题数</label></th>
			<td>
				<select name="tpp" id="tpp">
				  <option value="0" selected="selected">默认</option>
				  <option value="15">15</option>
				  <option value="20">20</option>
				  <option value="25">25</option>
				</select>
			</td>
		</tr>
		</tbody>
		<tbody>
		<tr>
			<th><label for="ppp">每页帖子数</label></th>
			<td>
				<select name="ppp" id="ppp">
				  <option value="0" selected="selected">默认</option>
				  <option value="10">10</option>
				  <option value="15">15</option>
				  <option value="20">20</option>
				</select>
			</td>
		</tr>
		</tbody>
		<tbody>
		<tr>
			<th><label for="newpm">是否提示短消息</label></th>
			<td>
				<input name="newpm" type="radio" value="radiobutton" checked="checked"  style="InPutRadio"/>是
				<input type="radio" name="newpm" value="radiobutton"  style="InPutRadio"/>否
			</td>
		</tr>
		</tbody>
		<tbody>
		<tr>
			<th><label for="pmsound">短消息铃声</label></th>
			<td>
				<select name="pmsound" id="pmsound">
				  <option value="1" selected="selected">默认</option>							  
				  <option value="1">提示音1</option>							  
				  <option value="2">提示音2</option>							  
				  <option value="3">提示音3</option>		
				  <option value="4">提示音4</option>		
				  <option value="5">提示音5</option>							  
				  <option value="0">无</option>
				</select>
			</td>
		</tr>
		</tbody>
		<tbody>
		<tr>
			<th><label for="showemail">是否显示Email</label></th>
			<td>
				<input name="showemail" type="radio" value="1" checked="checked"  style="InPutRadio"/>是
				<input type="radio" name="showemail" value="0"  style="InPutRadio"/>否
			</td>
		</tr>
		</tbody>
		<tbody>
		<tr>
			<th><label for="receivesetting">消息接收设置</label></th>
			<td>
				<input id="receiveuser" onclick="checkSetting();" type="checkbox" name="receivesetting" value="2" checked="checked" />接收用户短消息
				<input id="showhint" onclick="checkSetting();" type="checkbox" name="receivesetting" value="4" checked="checked" />显示短消息提示框
			</td>
		</tr>
		</tbody>
		<tbody>
		<tr>
			<th><label for="invisible">是否隐身</label></th>
			<td>
				<input type="radio" name="invisible" value="1"  style="InPutRadio"/>是
				<input name="invisible" type="radio" value="0" checked="checked"  style="InPutRadio"/>否
			</td>
		</tr>
		</tbody>
		<tbody>
		<tr>
			<th><label for="signature">签名</label></th>
			<td>
				<textarea name="signature" cols="50" rows="6" id="signature" style="height:95px;width:85%;"></textarea>
				<input name="sigstatus" type="checkbox" id="sigstatus" value="1" checked="checked" />使用签名
			</td>
		</tr>
		</tbody>
		<tbody>
		<tr>
			<th>&nbsp;</th>
			<td>
				<input name="submit" type="submit" value="创建用户"/>  <input type="button" onclick="javascript:window.location.replace('./main.action')" value="取消"/></td>
			</td>
		</tr>
		</tbody>
	</table>
</div>
</#if>
</form>
<!--reg end-->
</div>
</div>
<#else>
<#if createuser!="">
	<#if reqcfg.page_err==0>
	<@comm.msgbox/>
	<#else>
	<@comm.errmsgbox/>
	</#if>
	</div>
</#if>
</#if>
</#if>
<@comm.copyright/>
<@comm.footer/>