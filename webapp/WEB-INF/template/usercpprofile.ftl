<#-- 
	描述：用户中心个人设置模板
	作者：黄磊 
	版本：v1.0 
-->
<#import "inc/page_comm.ftl" as comm />
<@comm.header/>
<!--header end-->
<div id="foruminfo">
	<div id="nav">
		<a href="${config.forumurl}" class="home">${config.forumtitle}</a>  &raquo; <a href="usercp.action">用户中心</a>  &raquo; <strong>编辑个人档案</strong>
	</div>
	<div id="headsearch">
		<div id="search">
			<@comm.quicksearch/>
		</div>
	</div>
</div>

<!--主体-->
<div class="controlpannel">
   <@comm.menu/>
	<div class="pannelcontent">
		<div class="pcontent">
			<div class="panneldetail">
				<@comm.permenu/>	
				<div class="pannelbody">
					<div class="pannellist">
					<#if reqcfg.page_err==0>
						<#if ispost>
							<@comm.msgbox/>
						<#else>
						<form action="" method="post" ID="Form1">
						<label for="realname">真实姓名:</label>
							<input name="realname" type="text" id="realname" value="${user.userfields.realname}" size="10" />
						<br />
						<label for="idcard">身份证号码:</label>
							<input name="idcard" type="text" id="idcard" value="${user.userfields.idcard}" size="20" />
						<br />
						<label for="mobile">移动电话号码:</label>
							<input name="mobile" type="text" id="mobile" value="${user.userfields.mobile}" size="20" />
						<br />
						<label for="phone">固定电话号码:</label>
							<input name="phone" type="text" id="phone" value="${user.userfields.phone}" size="20" />
						<br />
						<div style="line-height:180%;">
						<label for="gender">性别:</label>
							<input type="radio" name="gender" value="1"  class="radioinput" 
							<#if user.gender==1>
							 checked="checked"
							</#if>
							 ID="Radio1"/>
							男
							<input type="radio" name="gender" value="2"  class="radioinput" 
							<#if user.gender==2>
							 checked="checked"
							</#if>
							 ID="Radio2"/>
							女
							<input name="gender" type="radio" value="0"   class="radioinput" 
							<#if user.gender==0>
							 checked="checked"
							</#if>
							 ID="Radio3"/>
							保密 
						</div>
						<br style="height:1px;line-height:5px;"/>
						<label for="nickname">呢称:</label>
						<input name="nickname" type="text" id="nickname" value="${user.nickname}" size="30" /><br />
						<label for="EMail">EMail:</label>
							<input name="email" type="text" id="email" value="${user.email}" size="30"/> 
							<input name="showemail" type="checkbox" id="showemail" value="0" 
							<#if user.showemail==0>
									checked="checked"
							</#if>/>
									Email保密
						<br />
						<label for="website">主页:</label>
						<input name="website" type="text" class="colorblue" id="website" value="${user.userfields.website}" size="30" /> 
						<br />
						<label for="location">来自:</label>
						<input name="location" type="text" id="location" class="colorblue" value="${user.userfields.location}" size="20" />
						<br />
						<label for="bday">生日:</label>
						<input name="bday" type="text" id="bday" size="10" value="${user.bday}" style="cursor:default" onClick="showcalendar(event, 'bday', 'cal_startdate', 'cal_enddate', '1980-01-01');" readonly="readonly" />&nbsp;<button onclick="$('bday').value='';">清空</button>
						<input type="hidden" name="cal_startdate" id="cal_startdate" size="10"  value="1900-01-01" />
						<input type="hidden" name="cal_enddate" id="cal_enddate" size="10"  value="${nowdatetime}" />
						<br />
						<label for="msn">MSN Messenger:</label>
						<input name="msn" type="text" id="msn" value="${user.userfields.msn}" size="30" />
						<br />
						<label for="qq">QQ:</label>
						<input name="qq" type="text" id="qq" value="${user.userfields.qq}" size="12" />
						<br />
						<label for="Skype">Skype:</label>
						<input name="skype" type="text" id="skype" value="${user.userfields.skype}" size="30" />
						<br />
						<label for="ICQ">ICQ:</label>
						<input name="icq" type="text" id="icq" value="${user.userfields.icq}" size="12" />
						<br />
						<label for="yahoo">Yahoo Messenger:</label>
						<input name="yahoo" type="text" class="colorblue" id="yahoo" value="${user.userfields.yahoo}" size="30" />
						<br />
						<label for="bio">自我介绍:</label>
						<textarea name="bio" cols="50" rows="6" id="bio" style="width:80%;">${user.userfields.bio}</textarea>
						<br />
						<label for="Submit">&nbsp;</label><input type="submit" name="Submit" value="确定" onclick="if (document.getElementById('bio').value.length > 500) {alert('自我介绍长度最大为500字'); return false;}" ID="Submit1"/>
								</form>
						</#if>
						</div>
					<#else>
					<@comm.usercperrmsgbox/>
					</#if>
			  </div>
			</div>
		</div>
	</div>
</div>
<!--主体-->
</div>
<script type="text/javascript" src="javascript/template_calendar.js"></script>
<@comm.copyright/>
<@comm.footer/>
