<#-- 
	描述：短信发送模板
	作者：黄磊 
	版本：v1.0 
-->
<#import "inc/page_comm.ftl" as comm />
<@comm.header/>
<!--header end-->

<div id="foruminfo">
	<div id="nav">
		<a href="${config.forumurl}" class="home">${config.forumtitle}</a>  &raquo; <a href="usercp.action">用户中心</a>  &raquo; <strong>撰写短消息</strong>
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
				<@comm.smsmenu/>	
				<div class="pannelbody">
					<div class="pannellist">
					<#if reqcfg.page_err==0>
						<#if ispost>
							<@comm.msgbox/>
						<#else>
						<form id="postpm" name="postpm" method="post" action="">
							<label for="user" class="labelshort">收件人:</label>
							<input name="msgto" type="text" id="msgto" value="${msgto}" size="20" /><br />
							<label for="email" class="labelshort">标题:</label>
							<input name="subject" type="text"id="subject" value="${subject}" size="40" /><br />
							<label for="comment" class="labelshort">内容:</label>
							<textarea name="message" cols="80" rows="20" id="message" onkeydown="if((event.ctrlKey && event.keyCode == 13) || (event.altKey && event.keyCode == 83)) document.getElementById('postpm').submit();" style="width:80%;">${message}</textarea><br/>
							<label for="savetosentbox"  class="labelshort">&nbsp;</label>
							<input name="savetosentbox" type="checkbox" id="Checkbox1" value="1" style="border:0;" />发送的同时保存到发件箱 
							<input type="checkbox" name="emailnotify" id="emailnotify" />邮件通知<br />
							<#if isseccode>
							<label for="savetosentbox"  class="labelshort">验证码</label><@comm.vcode/><br />
							</#if>
							<label for="savetosentbox"  class="labelshort">&nbsp;</label>
							<input name="sendmsg" type="submit" id="sendmsg" value="立即发送"/>
							<input name="savetousercpdraftbox" type="submit" id="savetousercpdraftbox" value="存为草稿"/>
							[完成后可按Ctrl+Enter提交]
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
</div>
<!--主体-->
<@comm.copyright/>
<@comm.footer/>
