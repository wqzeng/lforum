<#import "../../controls/control.ftl" as c>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" >
    <head runat="server">
        <title>无标题页</title>
        <script type="text/javascript" src="../js/common.js"></script>		
	    <link href="../styles/dntmanager.css" type="text/css" rel="stylesheet" />        
        <link href="../styles/modelpopup.css" type="text/css" rel="stylesheet" />
	    <script type="text/javascript" src="../js/modalpopup.js"></script>
    </head>
    <body>
        <form id="Form1" method="post">
        ${htmlBuilder}
            <div class="ManagerForm">
		        <fieldset>
		            <legend style="background:url(../images/icons/icon42.jpg) no-repeat 6px 50%;">用户权限</legend>
		            <table cellspacing="0" cellpadding="4" width="100%" align="center">
                    <tr>
                        <td  class="panelbox" align="left" width="50%">
                            <table width="100%">
                                <tr>
			                        <td style="width: 130px">允许重复评分:</td>
                                    <td>
					                    <@c.radioButtonList id="dupkarmarate" hintInfo="选择&amp;quot;是&amp;quot;将允许用户对一个帖子进行多次评分, 默认为&amp;quot;否&amp;quot;" hintTitle="提示">
						                    <@c.radioItem name="dupkarmarate" checked="${config.dupkarmarate}" value="1">是</@c.radioItem>
						                    <@c.radioItem name="dupkarmarate" checked="${config.dupkarmarate}" value="0">否</@c.radioItem>
					                    </@c.radioButtonList>
				                    </td>
                                </tr>
                                 <tr>
				                    <td>最大允许的上传附件数:</td>
				                    <td>
					                    <@c.textBox id="maxattachments" value="${config.maxattachments}" hintInfo="最大允许的上传附件数" hintTitle="提示" maxlength="5" width="50" size="6" />
				                    </td>
                                </tr>
                                <tr>
				                    <td>评分时间限制:</td>
				                    <td>
					                    <@c.textBox id="karmaratelimit"   hintInfo="帖子发表后超过此时间限制其他用户将不能对此帖评分, 版主和管理员不受此限制, 0 为不限制" 
					                    	hintTitle="提示" value="${config.karmaratelimit}" width="50" size="5" maxlength="4"></@c.textBox>(单位:小时)
				                    </td>
                                </tr>
                                <tr>
				                    <td>收藏夹容量:</td>
                                    <td>
					                    <@c.textBox id="maxfavorites" value="${config.maxfavorites}" width="80" size="8" maxlength="7" 
					                    hintInfo="允许收藏的最大板块 / 主题数, 默认为100" hintTitle="提示"></@c.textBox>
				                    </td>
                                </tr>
                                <tr>
				                    <td>头像最大宽度:</td>
				                    <td>
					                    <@c.textBox id="maxavatarwidth" value="${config.maxavatarwidth}" width="80" size="8"  maxlength="7" 
					                    hintInfo="用户头像将被缩小到此大小范围之内" hintTitle="提示"></@c.textBox>(单位:像素)
				                    </td>
                                </tr>
                                <tr>
				                    <td>主题查看页面显<br />示管理操作否:</td>
                                    <td>
					                    <@c.radioButtonList id="moderactions"  hintInfo="是否在主题查看页面显示管理操作" hintTitle="提示">
						                    <@c.radioItem name="moderactions" checked="${config.moderactions}" value="1">显示</@c.radioItem>
						                    <@c.radioItem name="moderactions" checked="${config.moderactions}" value="0">不显示</@c.radioItem>
					                    </@c.radioButtonList>
				                    </td>
                                </tr>
                            </table>
                        </td>
                        <td align="right" class="panelbox" width="50%">
                            <table width="100%">
			                    <tr>
				                    <td style="width: 130px">投票最大选项数:</td>
                                    <td>
				                        <@c.textBox id="maxpolloptions" value="${config.maxpolloptions}" width="50"  size="8" 
				                        maxlength="7" hintInfo="设定发布投票包含的最大选项数" hintTitle="提示"></@c.textBox>
			                        </td>
			                    </tr>
			                    <tr>
				                    <td>帖子最小字数:</td>
                                    <td>
					                    <@c.textBox id="minpostsize" value="${config.minpostsize.toString()}" width="80" size="8"  maxlength="7" 
					                    hintInfo="管理组成员可通过&amp;quot;发帖不受限制&amp;quot;设置而不受影响, 0 为不限制" hintTitle="提示"></@c.textBox>(单位:字节)
					                    <select onchange="$('minpostsize').value=this.value">
						                    <option value="">请选择</option>
						                    <option value="51200">50K</option>
						                    <option value="102400">100K</option>
						                    <option value="153600">150K</option>
						                    <option value="204800">200K</option>
						                    <option value="256000">250K</option>
						                    <option value="307200">300K</option>
						                    <option value="358400">350K</option>
						                    <option value="409600">400K</option>
						                    <option value="512000">500K</option>
						                    <option value="614400">600K</option>
						                    <option value="716800">700K</option>
						                    <option value="819200">800K</option>
						                    <option value="921600">900K</option>
						                    <option value="1024000">1M</option>
						                    <option value="2048000">2M</option>
						                    <option value="4096000">4M</option>								
					                    </select>
				                    </td>
			                    </tr>
			                    <tr>
				                    <td>帖子最大字数:</td>
                                    <td>
					                    <@c.textBox id="maxpostsize"  value="${config.maxpostsize.toString()}" width="80"  size="8" maxlength="7" 
					                    hintInfo="管理组成员可通过&amp;quot;发帖不受限制&amp;quot;设置而不受影响" hintTitle="提示"></@c.textBox>(单位:字节)
					                    <select onchange="$('maxpostsize').value=this.value">
						                    <option value="">请选择</option>
						                    <option value="51200">50K</option>
						                    <option value="102400">100K</option>
						                    <option value="153600">150K</option>
						                    <option value="204800">200K</option>
						                    <option value="256000">250K</option>
						                    <option value="307200">300K</option>
						                    <option value="358400">350K</option>
						                    <option value="409600">400K</option>
						                    <option value="512000">500K</option>
						                    <option value="614400">600K</option>
						                    <option value="716800">700K</option>
						                    <option value="819200">800K</option>
						                    <option value="921600">900K</option>
						                    <option value="1024000">1M</option>
						                    <option value="2048000">2M</option>
						                    <option value="4096000">4M</option>								
					                    </select>
				                    </td>
			                    </tr>
			                    <tr>
				                    <td>头像最大字节数:</td>
                                    <td>
					                    <@c.textBox id="maxavatarsize" value="${config.maxavatarsize.toString()}" width="80" size="8" maxlength="7" 
					                    hintInfo="用户上传头像不能超过此限制, 0 为不限制" hintTitle="提示"></@c.textBox>(单位:字节)
					                    <select onchange="$('maxavatarsize').value=this.value">
						                    <option value="">请选择</option>
						                    <option value="51200">50K</option>
						                    <option value="102400">100K</option>
						                    <option value="153600">150K</option>
						                    <option value="204800">200K</option>
						                    <option value="256000">250K</option>
						                    <option value="307200">300K</option>
						                    <option value="358400">350K</option>
						                    <option value="409600">400K</option>
						                    <option value="512000">500K</option>
						                    <option value="614400">600K</option>
						                    <option value="716800">700K</option>
						                    <option value="819200">800K</option>
						                    <option value="921600">900K</option>
						                    <option value="1024000">1M</option>
						                    <option value="2048000">2M</option>
						                    <option value="4096000">4M</option>								
					                    </select>					
				                    </td>
			                    </tr>
			                    <tr>
				                    <td>头像最大高度:</td>
				                    <td>
					                    <@c.textBox id="maxavatarheight" value="${config.maxavatarheight.toString()}" width="80" size="8"  maxlength="7" 
					                    hintInfo="用户头像将被缩小到此大小范围之内" hintTitle="提示"></@c.textBox>(单位:像素)
				                    </td>
			                    </tr>
			                    <tr>
				                    <td>允许使用HTML标题:</td>					
				                    <td>
				                    <table id="UserGroup" class="buttonlist" border="0" style="width:100%;">
				                    	${userGroupTab}
				                    </table>
				                    </td>
			                    </tr>
                            </table>
                        </td>
                    </tr>
                    </table>
		        </fieldset>	
		        <@c.hint hintImageUrl="../images" />
		        <div align="center">
		        	<@c.button id="SaveInfo" text=" 提 交 "  />
		        </div>
	        </div>	
        </form>
    ${footer}
    </body>
</html>
