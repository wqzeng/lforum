<#-- 
	描述：文本输入框 
	作者：黄磊 
	版本：v1.0 
	【id】 唯一标识
	【width】控件宽度
	【hintTitle】气泡提示标题
	【hintInfo】气泡提示信息
	【value】默认值
	【required】是否可为空
-->
<#macro textBox id="" readonly="" size="30" width="200px" height="50px" multiLine="" rows="0" cols="0" hintTitle="" hintInfo="" hintHeight="30" hintShowType="up" value="" min="" max="" maxlength="" minlength="" required="false" url="false" email="false" number="false" digits="false" style="">
	<#if multiLine=="">
	<input name="${id}" type="text" value="${value}" id="${id}" <#if readonly=="true">readonly</#if> class="FormBase" onfocus="this.className='FormFocus';"
	onblur="this.className='FormBase';" size="${size}" required="${required}" url="${url}" email="${email}" number="${number}" digits="${digits}"
	<#if hintTitle!="" && hintInfo!="">
	onmouseover="showhintinfo(this,0,0,'${hintTitle}','${hintInfo}','${hintHeight}','${hintShowType}')"
	onmouseout="hidehintinfo()"</#if> style="width: ${width};" <#if maxlength!="">maxlength="${maxlength}"</#if><#if minlength!="">minlength="${minlength}"</#if>
	<#if min!="">min="${min}"</#if><#if max!="">max="${max}"</#if> style="${style}"/>
	<#else>
	<script type="text/javascript">
	function isMaxLen(o){
		var nMaxLen=o.getAttribute? parseInt(o.getAttribute("maxlength")):"";
		if(o.getAttribute && o.value.length>nMaxLen){
 			o.value=o.value.substring(0,nMaxLen)
	}}
	</script>
	<textarea name="${id}" id="${id}" <#if readonly=="true">readonly</#if> class="FormBase" onfocus="this.className='FormFocus';" onblur="this.className='FormBase';" onkeyup="return isMaxLen(this)" rows="${rows}" cols="${cols}" class="FormBase" onmouseover="showhintinfo(this,0,0,'${hintTitle}','${hintInfo}','${hintHeight}','${hintShowType}')" onmouseout="hidehintinfo()" style="height:${height};width:90%;">${value}</textarea>
	</#if>
</#macro>