<#import "../../controls/control.ftl" as c>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN" >
<html>
<head>
    <title>baseset</title>
    <script type="text/javascript" src="../js/common.js"></script>
    <link href="../styles/dntmanager.css" type="text/css" rel="stylesheet" />
    <link href="../styles/modelpopup.css" type="text/css" rel="stylesheet" />
    <script type="text/javascript" src="../js/modalpopup.js"></script>
    <@c.validator "Form1" />
</head>
<body>
    <form id="Form1" method="post">
    ${htmlBuilder}
        <div class="ManagerForm">
            <fieldset>
                <legend style="background: url(../images/icons/icon24.jpg) no-repeat 6px 50%;">附件设置</legend>
                    <table cellspacing="0" cellpadding="4" width="100%" align="center">
		                <tr>
		                    <td  class="panelbox" width="50%" align="left">
		                        <table width="100%">
		                            <tr>
                                        <td style="width: 130px">帖子中显示图片附件:</td>
                                        <td>
                                            <@c.radioButtonList id="attachimgpost"  hintTitle="提示" hintInfo="在帖子中直接将图片或动画附件显示出来, 而不需要点击附件链接">
                                                <@c.radioItem name="attachimgpost" value="1" checked="${config.attachimgpost}">是</@c.radioItem>
						 						<@c.radioItem name="attachimgpost" value="0" checked="${config.attachimgpost}">否</@c.radioItem> 
                                            </@c.radioButtonList>
                                        </td>
		                            </tr>
		                            <tr>
                                        <td>附件保存方式:</td>
                                        <td>
                                            <@c.radioButtonList id="attachsave"  hintTitle="提示" hintInfo="本设置只影响新上传的附件, 设置更改之前的附件仍存放在原来位置.">
                                                <@c.radioItem name="attachsave" value="0" checked="${config.attachsave}">按年/月/日存入不同目录</@c.radioItem><br>
                                                <@c.radioItem name="attachsave" value="1" checked="${config.attachsave}">按年/月/日/论坛存入不同目录</@c.radioItem><br>
                                                <@c.radioItem name="attachsave" value="2" checked="${config.attachsave}">按版块存入不同目录 [不推荐]</@c.radioItem><br>
                                                <@c.radioItem name="attachsave" value="3" checked="${config.attachsave}">按文件类型存入不同目录 [不推荐]</@c.radioItem>
                                            </@c.radioButtonList>
                                        </td>
		                            </tr>
		                            <tr>
                                        <td>文字型水印的内容:</td>
                                        <td>
                                            <@c.textBox id="watermarktext"  hintTitle="提示" 
                                                hintInfo="可以使用替换变量: {1}表示论坛标题 {2}表示论坛地址 {3}表示当前日期 {4}表示当前时间.例如: {3} {4}上传于{1} {2}"
                                                width="200px" value="${config.watermarktext}"></@c.textBox>
                                        </td>
		                            </tr>
		                            <tr>
                                        <td>图片附件文字水印字体:</td>
                                        <td>
                                        	<select name="watermarkfontname" id="watermarkfontname">
                                        	${watermarkfontname}
                                        	</select>
                                        </td>
		                            </tr>
		                            <tr>
                                        <td>图片附件地址显示开关:</td>
                                        <td>
                                            <@c.radioButtonList id="showattachmentpath" hintTitle="提示" hintInfo="如果选择是, 则系统会以真实路径显示图片.如果选择否, 则以程序路径显示">
                                                <@c.radioItem name="showattachmentpath" value="1" checked="${config.showattachmentpath}">显示</@c.radioItem>
                                                <@c.radioItem name="showattachmentpath" value="0" checked="${config.showattachmentpath}">不显示</@c.radioItem>
                                            </@c.radioButtonList>
                                        </td>
		                            </tr>
		                            <tr>
                                        <td>图片最大高度:</td>
                                        <td>
                                            <@c.textBox id="attachimgmaxheight"  hintTitle="提示" hintInfo="0为不受限制.本设置只适用于bmp/png/jpeg格式图片"
                                                size="7" width="50" value="${config.attachimgmaxheight}" maxlength="5"></@c.textBox>(单位:像素)
                                        </td>
		                            </tr>
		                            <tr>
                                        <td>水印位置:</td>
                                        <td>
                                            <table cellspacing="0" cellpadding="4">
                                                <tr class="altbg2" align="left">
                                                    <td title="请在此选择水印添加的位置(共 9 个位置可选).添加水印暂不支持动画 GIF 格式. 附加的水印图片在下面的使用的 图片水印文件 中指定.">
                                                        ${watermarkstatus}
                                                    </td>
                                                </tr>
                                            </table>
                                        </td>
		                            </tr>
		                        </table>
		                    </td>
		                    <td  class="panelbox" width="50%" align="right">
		                        <table width="100%">
                                    <tr>
                                        <td style="width: 130px">下载附件来路检查:</td>
                                        <td>
                                            <@c.radioButtonList id="attachrefcheck"  hintTitle="提示" hintShowType="down"
                                            hintInfo="选择&amp;quot;是&amp;quot;将检查下载附件的来路, 来自其他网站或论坛的下载请求将被禁止. 注意: 本功能在开启&amp;quot;帖子中显示图片附件&amp;quot;时,会加重服务器负担">
                                                <@c.radioItem name="attachrefcheck" value="1" checked="${config.attachrefcheck}">是</@c.radioItem>
                                                <@c.radioItem name="attachrefcheck" value="0" checked="${config.attachrefcheck}">否</@c.radioItem>
                                            </@c.radioButtonList>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>图片附件水印类型:</td>
                                        <td>
                                            <@c.radioButtonList id="watermarktype">
                                                <@c.radioItem name="watermarktype" value="0" checked="${config.watermarktype}">文字</@c.radioItem>
                                                <@c.radioItem name="watermarktype" value="1" checked="${config.watermarktype}">图片</@c.radioItem>
                                            </@c.radioButtonList>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>图片型水印文件:</td>
                                        <td>
                                            <@c.textBox id="watermarkpic"  hintTitle="提示" 
                                                hintInfo="附加的水印图片需存放到论坛目录的watermark子目录下.注意:如果图片不存在系统将使用文字类型的水印. "
                                                width="200px" value="${config.watermarkpic}"></@c.textBox>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>图片附件文字水印大小:</td>
                                        <td>
                                            <@c.textBox id="watermarkfontsize" width="50"  size="7" maxlength="5" value="${config.watermarkfontsize}"></@c.textBox>(单位:像素)
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>JPG图片质量:</td>
                                        <td>
                                            <@c.textBox id="attachimgquality"  hintTitle="提示" hintInfo="本设置只适用于加水印的jpeg格式图片.取值范围 0-100, 0质量最低, 100质量最高, 默认80"
                                                size="5" width="50" value="${config.attachimgquality}" maxlength="3"></@c.textBox>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>图片最大宽度:</td>
                                        <td>
                                            <@c.textBox id="attachimgmaxwidth"  hintTitle="提示" hintInfo="0为不受限制.本设置只适用于bmp/png/jpeg格式图片"
                                                size="7" width="50" value="${config.attachimgmaxwidth}" maxlength="5"></@c.textBox>(单位:像素)
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>图片水印透明度:</td>
                                        <td>
                                            <@c.textBox id="watermarktransparency"  hintTitle="提示" hintInfo="取值范围1--10 (10为不透明)."
                                                maxlength="2" width="50" value="${config.watermarktransparency}" size="5"></@c.textBox>
                                        </td>
                                    </tr>
		                        </table>
		                    </td>
		                </tr>
		            </table>
            </fieldset>
            <@c.hint hintImageUrl="../images" />
            <div class="Navbutton"><@c.button id="SaveInfo" text="提交"  /></div>
        </div>
    </form>
    ${footer}
</body>
</html>
