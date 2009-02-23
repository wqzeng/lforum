<#-- 
	描述：分栏框架页模板
	作者：黄磊 
	版本：v1.0 
-->
<#import "inc/page_comm.ftl" as comm />
<@comm.pageHeader />
<script type="text/javascript">
	if (top.main){
		top.main.location = "focuslist.action";
	}
</script>
<div id="container">
<frameset border="0" name="content" framespacing="0" frameborder="0" cols="210,*">
	<frame id="leftmenu" name="leftmenu" marginwidth="0" marginheight="0" src="forumlist.action" noresize>
	<frame id="main" name="main" src="focuslist.action">
</frameset><noframes></noframes>
</div>