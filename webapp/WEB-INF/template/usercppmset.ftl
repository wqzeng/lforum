<#-- 
	描述：短信设置模板
	作者：黄磊 
	版本：v1.0 
-->
<#import "inc/page_comm.ftl" as comm />
<@comm.header/>
<!--header end-->

<div id="foruminfo">
	<div id="nav">
		<a href="${config.forumurl}" class="home">${config.forumtitle}</a>  &raquo; <a href="usercp.action">用户中心</a>  &raquo; <strong>选项</strong>
	</div>
	<div id="headsearch">
		<div id="search">
			<@comm.quicksearch/>
		</div>
	</div>
</div>

<!--主体-->
<script type="text/javascript">
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
</script>
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
						<form action="" method="post" ID="postpmset">
						<label for="pmsound" class="labellong2" style="line-height:180%;">短消息提示音:</label>
							<select name="pmsound" id="pmsound">
							  <option value="0" 
							  <#if user.pmsound==0>
							  selected="selected"
							  </#if>
							  >无</option>							  
							  <option value="1" 
							  <#if user.pmsound==1>
							  selected="selected"
							  </#if>
							  >提示音1</option>							  
							  <option value="2" 
							  <#if user.pmsound==2>
							  selected="selected"
							  </#if>
							  >提示音2</option>							  
							  <option value="3" 
							  <#if user.pmsound==3>
							  selected="selected"
							  </#if>
							  >提示音3</option>			
							  <option value="4" 
							  <#if user.pmsound==4>
							  selected="selected"
							  </#if>
							  >提示音4</option>			
							  <option value="5" 
							  <#if user.pmsound==5>
							  selected="selected"
							  </#if>
							  >提示音5</option>
							  </select>
							<br/>
							<div class="compartline" style="margin-top:8px;">&nbsp;</div>
							<label for="receivesetting" class="labellong2" style="line-height:180%;">接收设置:</label>
							<input id="receiveuser" onclick="checkSetting();" type="checkbox" name="receivesetting" value="2" <#if receivepmsetting==2 || receivepmsetting==3>checked="checked"</#if><#if receivepmsetting==6 || receivepmsetting==7>checked="checked"</#if>/>接收用户短消息
							<input id="showhint" onclick="checkSetting();" type="checkbox" name="receivesetting" value="4" <#if  receivepmsetting==0>disabled</#if><#if receivepmsetting==1 ||  receivepmsetting==5>disabled</#if><#if (receivepmsetting>4)>checked="checked"</#if>/>显示短消息提示框
							<br/>
							<div class="compartline" style="margin-top:8px;">&nbsp;</div>
							<input type="submit" name="Submit" value="确定" ID="Submit1"/>
						</form>
						
						
						</#if>
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
</div>
<@comm.copyright/>
<@comm.footer/>
