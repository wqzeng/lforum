<#-- 
	描述：可变文本编辑域 
	作者：黄磊 
	版本：v1.0 
	【id】 唯一标识
	【hintTitle】气泡提示标题
	【hintInfo】气泡提示信息
	【rows】行数
	【cols】列数
	【value】默认值
-->
<#macro textareaResize id="" rows="5" cols="28" hintTitle="" hintInfo="" hintHeight="30" hintShowType="up" value="">
	<script type="text/javascript">
	function zoomtextarea(objname, zoom) {
		zoomsize = zoom ? 10 : -10;		
		obj = document.getElementById(objname);
		if (obj.rows + zoomsize > 0 && obj.cols + zoomsize * 3 > 0) {
			obj.rows += zoomsize;
			obj.cols += zoomsize * 3;
		}
	}
	</script> 
	<span id="${id}" <#if hintTitle!="" && hintInfo!=""> onmouseover="showhintinfo(this,0,0,'${hintTitle}','${hintInfo}','${hintHeight}','${hintShowType}');" onmouseout="hidehintinfo();"</#if>> 
	<img src="../images/zoomin.gif" onmouseover="this.style.cursor='hand'" onclick="zoomtextarea('${id}_posttextarea', 1)" title="扩大" /> 
	<img src="../images/zoomout.gif" onmouseover="this.style.cursor='hand'" onclick="zoomtextarea('${id}_posttextarea', 0)" title="缩小" /> <br>
	<textarea name="${id}_posttextarea" id="${id}_posttextarea" rows="${rows}" cols="${cols}" onfocus="this.className='areamouseover';" onblur="this.className='';">${value}</textarea> </span>
</#macro>