<#-- 
	描述：论坛模板选择页模板
	作者：黄磊 
	版本：v1.0 
-->
<#import "inc/page_comm.ftl" as comm />
<@comm.header/>
<div id="foruminfo">
	<div id="nav">
		<a href="${config.forumurl}">${config.forumtitle}</a>  &raquo; <strong>切换模板</strong>
	</div>
</div>
<#if reqcfg.page_err==0>
<#if templateid!=0>
<script type="text/javascript">
	if (top.frames["leftmenu"])
	{
		top.frames["leftmenu"].location.reload();
	}
</script>
<@comm.msgbox/>
<#else>
<form id="form1" name="form1" method="post" action="">
<div class="mainbox">
	<h1>界面</h1>
	<ul id="forumtemplate">
	<#list templatelist as template>
		<li><span><img src="templates/${template.directory}/about.png" /></span><br />
		  <br /><input name="templateid" type="radio" value="${template.templateid}" 
		<#if templatepath==template.directory>checked </#if>/>
		 ${template.name}
		</li>
	</#list>
	</ul>
</div>
<div class="templatebutton"><input type="submit" name="Submit" value="确定"/></div>
</form>
</div>
</#if>
<#else>
<@comm.errmsgbox/>
</div>
</#if>
<@comm.copyright/>
<@comm.footer/>