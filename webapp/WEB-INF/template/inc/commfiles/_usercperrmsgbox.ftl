<#-- 
	描述：用户中心错误提示模板
	作者：黄磊 
	版本：v1.0 
-->
﻿<div class="box message">
	<h1>错误显示</h1>
	<p>${reqcfg.msgbox_text}</p>
	<p class="errorback">
		<script type="text/javascript">
			if(${reqcfg.msgbox_showbacklink})
			{
				document.write("<a href=\"${reqcfg.msgbox_backlink}\">返回上一步</a> &nbsp; &nbsp;|  ");
			}
		</script>
		&nbsp; &nbsp; <a href="forumindex.action">论坛首页</a>
		<#if usergroupid==7>
		 |&nbsp; &nbsp; <a href="register.action">注册</a>
		</#if>
	</p>
</div>