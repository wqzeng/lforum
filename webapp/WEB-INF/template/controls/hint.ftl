<#-- 
	描述：气泡提示层 
	作者：黄磊 
	版本：v1.0 
-->
<#macro hint hintImageUrl>
<!--提示层部分开始-->
<span id="hintdivup"
	style="display: none; position: absolute; z-index: 500;">
<div
	style="position: absolute; visibility: visible; width: 271px; z-index: 501;">
<p><img src="${hintImageUrl}/commandbg.gif" /></p>
<div class="messagetext"><img src="${hintImageUrl}/dot.gif" /><span
	id="hintinfoup"></span></div>
<p><img src="${hintImageUrl}/commandbg2.gif" /></p>
</div>
<iframe id="hintiframeup"
	style="position: absolute; z-index: 100; width: 266px; scrolling: no;"
	frameborder="0"></iframe> </span> <span id="hintdivdown"
	style="display: none; position: absolute; z-index: 500;">
<div
	style="position: absolute; visibility: visible; width: 271px; z-index: 501;">
<p><img src="${hintImageUrl}/commandbg3.gif" /></p>
<div class="messagetext"><img src="${hintImageUrl}/dot.gif" /><span
	id="hintinfodown"></span></div>
<p><img src="${hintImageUrl}/commandbg4.gif" /></p>
</div>
<iframe id="hintiframedown"
	style="position: absolute; z-index: 100; width: 266px; scrolling: no;"
	frameborder="0"></iframe> </span> 
<!--提示层部分结束-->
</#macro>