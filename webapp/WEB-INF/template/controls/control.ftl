<#-- 
	描述：控件宏定义 
	作者：黄磊 
	版本：v1.0 
-->
<#import "../inc/jquery.ftl" as jquery>
<#include "textbox.ftl">
<#include "radioButtonList.ftl">
<#include "textareaResize.ftl">
<#include "hint.ftl">
<#include "button.ftl">
<#include "pageInfo.ftl">
<#include "checkBoxList.ftl">
<#include "dropDownList.ftl">
<#include "tabControl.ftl">
<#include "colorPicker.ftl">

<#-- 表单验证器定义 -->
<#macro validator formname>
<#-- 
<@jquery.jquery_main />
<@jquery.jquery_validator />
<script type="text/javascript">
$(document).ready(function(){
$("#${formname}").validate();
});
</script>
-->
</#macro>