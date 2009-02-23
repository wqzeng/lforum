<#-- 
	描述：页面底部广告
	作者：黄磊 
	版本：v1.0 
-->
<#if floatad!="">
	<script type="text/javascript" src="javascript/template_floatadv.js"></script>
	${floatad}
	<script type="text/javascript">theFloaters.play();</script>
<#elseif doublead!="">
	<script type="text/javascript" src="javascript/template_floatadv.js"></script>
	${doublead}
	<script type="text/javascript">theFloaters.play();</script>
</#if>
