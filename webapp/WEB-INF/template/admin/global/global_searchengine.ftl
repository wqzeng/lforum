<#import "../../controls/control.ftl" as c>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN" >
<html>
<head>
    <title>registerandvisit</title>
    <script type="text/javascript" src="../js/common.js"></script>
    <link href="../styles/dntmanager.css" type="text/css" rel="stylesheet" />
    <link href="../styles/modelpopup.css" type="text/css" rel="stylesheet" />
    <script type="text/javascript" src="../js/modalpopup.js"></script>
    <@c.validator "Form1" />
</head>
<body>
    <div class="ManagerForm">
        <form id="Form1" method="post">
        ${htmlBuilder}
            <fieldset>
                <legend style="background: url(../images/icons/icon13.jpg) no-repeat 6px 50%;">搜索引擎优化</legend>
                    <table cellspacing="0" cellpadding="4" width="100%" align="center">
                        <tr>
                            <td  class="panelbox" width="50%" align="left">
                                <table width="100%">
		                            <tr>
                                        <td style="width: 110px">标题附加字:</td>
                                        <td>
                                            <@c.textareaResize id="seotitle" hintTitle="提示" value="${config.seotitle}"
                                            	hintInfo="网页标题通常是搜索引擎关注的重点, 本附加字设置将出现在标题中论坛名称的前面, 如果有多个关键字, 建议用	&amp;quot;|&amp;quot;、&amp;quot;,&amp;quot;(不含引号) 等符号分隔">
                                            </@c.textareaResize>
                                        </td>
		                            </tr>
		                            <tr>
                                        <td>Meta Description:</td>
                                        <td>
                                            <@c.textareaResize id="seodescription" value="${config.seodescription}"
                                                hintTitle="提示" hintInfo="Description 出现在页面头部的 Meta 标签中, 用于记录本页面的概要与描述"></@c.textareaResize>
                                        </td>
		                            </tr>
		                        </table>
		                    </td>
		                    <td  class="panelbox" width="50%" align="right">
                                <table width="100%">
                                    <tr>
                                        <td style="width: 110px">Meta Keywords:</td>
                                        <td>
                                            <@c.textareaResize id="seokeywords" value="${config.seokeywords}"
                                                hintTitle="提示" hintInfo="Keywords 项出现在页面头部的 Meta 标签中, 用于记录本页面的关键字, 多个关键字间请用半角逗号 &amp;quot;,&amp;quot; 隔开">
                                            </@c.textareaResize>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>其他头部信息:</td>
                                        <td>
                                            <@c.textareaResize id="seohead" value="${config.seohead}" hintTitle="提示"
                                                hintInfo="如需在 &amp;lt;head&amp;gt;&amp;lt;/head&amp;gt; 中添加其他的 html 代码, 可以使用本设置, 否则请留空">
                                            </@c.textareaResize>
                                        </td>
                                    </tr>
		                        </table>
		                    </td>
		                </tr>
		            </table>
            </fieldset>
            <@c.hint hintImageUrl="../images"/>
            <div align="center">
                <@c.button id="SaveInfo" text=" 提 交 "  />
            </div>
        </form>
    </div>
    ${footer}
</body>
</html>
