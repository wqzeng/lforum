<#-- 
	描述：页面帮助/提示信息
	作者：黄磊 
	版本：v1.0 
-->
<#macro pageInfo id icon text>
<div id="${id}" style="clear:both; margin-top:10px; position:relative;background:url(<#if icon=="Information">'../images/hint.gif'<#elseif icon=="Warning">'../images/warning.gif'</#if>) no-repeat 20px 15px;border:1px dotted #DBDDD3; background-color:#FDFFF2;
            margin-bottom:10px; padding:15px 10px 10px 56px; text-align: left; font-size: 12px;">
    <span class="infomessage">
        <a href="#" onclick="document.getElementById('info1').style.display='none';"><img src="../images/off.gif" alt="关闭" style="margin-left:3px;"/></a>
    </span>
 ${text}
</div>
<div style="clear:both;"></div>
</#macro>