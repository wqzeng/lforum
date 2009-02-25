<#import "../../controls/control.ftl" as c>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN" >
<html>
<head>
	<title>registerandvisit</title>		
	<script type="text/javascript" src="../js/common.js"></script>			    
    <link href="../styles/dntmanager.css" type="text/css" rel="stylesheet" />        
    <link href="../styles/modelpopup.css" type="text/css" rel="stylesheet" />
	<script type="text/javascript" src="../js/modalpopup.js"></script>
	<script type="text/javascript">
	    function LoadImage(index)
	    {
	        document.getElementById("preview").src = images[index];
	    }
	</script>
</head>
<body>
	<div class="ManagerForm">
		<form id="Form1" method="post" >
		${htmlBuilder}
			<@c.hint hintImageUrl="../images" />
		    <fieldset>
		        <legend style="background:url(../images/icons/icon18.jpg) no-repeat 6px 50%;">界面与显示方式设置</legend>
			    <table cellspacing="0" cellpadding="4" width="100%" align="center">
			    <tr>
			        <td  class="panelbox" width="50%" align="left">
			            <table width="100%">
			                <tr>
			                    <td style="width: 120px">默认论坛风格:</td>
				                <td>
				                    <@c.dropDownList id="templateid"   hintTitle="提示" hintInfo="论坛默认的界面风格, 游客和使用默认风格的会员将以此风格显示" onchange="LoadImage(this.selectedIndex)">
				                    ${templatelist}
					                </@c.dropDownList>
				                </td>
				            </tr>
				            <#-- 
				            <tr>
                                <td>浏览自动生成:</td>
                                <td>
                                  <@c.radiobuttonlist id="browsecreatetemplate"   hintTitle="提示" 
                                    hintInfo="设置当页面模板不存在,用户浏览时是否自动生成.不推荐使用">
                                    <@c.radioItem name="browsecreatetemplate" checked="1" value="1">是</@c.radioItem>
                                    <@c.radioItem name="browsecreatetemplate" checked="1" value="0">否</@c.radioItem>
                                  </@c.radiobuttonlist>
                                </td>
			                </tr>
			                -->
			                <tr>					    
				                <td>显示风格下拉菜单:</td>
				                <td>
				                    <@c.radioButtonList id="stylejump"   hintTitle="提示" 
				                        hintInfo="设置是否在论坛底部显示可用的论坛风格下拉菜单, 用户可以通过此菜单切换不同的论坛风格">
						                <@c.radioItem name="stylejump" checked="${config.stylejump}" value="1">是</@c.radioItem>
						                <@c.radioItem name="stylejump" checked="${config.stylejump}" value="0">否</@c.radioItem>
					                </@c.radioButtonList>
				                </td>				
                            </tr><tr>			    
                                <td>显示最近访问<br />论坛数量:</td>
                                <td>
                                    <@c.textBox id="visitedforums" value="${config.visitedforums}" width="50"  size="6"  maxlength="4" hintTitle="提示" 
                                    hintInfo="设置在论坛列表和帖子浏览中, 显示的最近访问过的论坛下拉列表数量, 建议设置为 30 以内, 0 为关闭此功能"></@c.textBox>
                                </td>
                            </tr>
                            <tr>
				                <td>最大签名高度:</td>
				                <td>
				                    <@c.textBox id="maxsigrows"  width="50" value="${config.maxsigrows}"  size="6" maxlength="4" hintTitle="提示" 
				                    hintInfo="设置帖子中允许显示签名的最大高度, 0 为不限制. 注意: 本限制只对 IE 浏览器有效"></@c.textBox>(单位:行)
				                </td>
                            </tr>
                            <tr>
					            <td>是否显示签名:</td>
					            <td>
					                <@c.radioButtonList id="showsignatures"   hintTitle="提示" hintInfo="是否在帖子中显示会员签名">
							            <@c.radioItem name="showsignatures" checked="${config.showsignatures}" value="1">是</@c.radioItem>
							            <@c.radioItem name="showsignatures" checked="${config.showsignatures}" value="0">否</@c.radioItem>
						            </@c.radioButtonList>
					            </td>
                            </tr>
                            <tr>
					            <td>是否显示图片:</td>
					            <td>
					                <@c.radioButtonList id="showimages"   hintTitle="提示" hintInfo="是否在帖子中显示图片(包括上传的附件图片和 [img] 代码图片)">
							            <@c.radioItem name="showimages" checked="${config.showimages}" value="1">是</@c.radioItem>
							            <@c.radioItem name="showimages" checked="${config.showimages}" value="0">否</@c.radioItem>
						            </@c.radioButtonList>
					            </td>
                            </tr>
                            <tr>
				                <td>显示可点击表情符:</td>
                                <td>
						            <@c.radioButtonList id="smileyinsert"  hintInfo="发帖页面包含表情符快捷工具, 点击图标即可插入表情符" hintTitle="提示" >
							            <@c.radioItem name="smileyinsert" checked="${config.smileyinsert}" value="1">是</@c.radioItem>
							            <@c.radioItem name="smileyinsert" checked="${config.smileyinsert}" value="0">否</@c.radioItem>
						            </@c.radioButtonList>
					            </td>
                            </tr>                            
                            <tr>
				                <td>帖子中显示作者状态:</td>
				                <td>
						            <@c.radioButtonList id="showauthorstatusinpost"  hintTitle="提示" 
						            hintInfo="浏览帖子时显示作者在线状态, 如果在线用户数量较多时, 启用&amp;quot;精确判断作者在线状态&amp;quot;功能会加重服务器负担, 此时建议使用&amp;quot;简单判断作者在线状态&amp;quot;">
							            <@c.radioItem value="1" name="showauthorstatusinpost" checked="1">简单判断作者在线状态并显示(推荐)</@c.radioItem><br>
							            <@c.radioItem value="2" name="showauthorstatusinpost" checked="1">精确判断作者在线状态并显示</@c.radioItem>
						            </@c.radioButtonList>
					            </td>
                            </tr>
                            <tr>
					            <td>显示论坛跳转菜单:</td>
					            <td>
						            <@c.radioButtonList id="forumjump"    hintTitle="提示" 
						            hintInfo="选择&amp;quot;是&amp;quot;将在列表页面下部显示快捷跳转菜单. 只有在本设置启用时JS菜单中的论坛跳转设置才有效. 注意: 当分论坛很多时, 本功能会严重加重服务器负担">
							            <@c.radioItem name="forumjump" checked="${config.forumjump}" value="1">是</@c.radioItem>
							            <@c.radioItem name="forumjump" checked="${config.forumjump}" value="0">否</@c.radioItem>
						            </@c.radioButtonList>
					            </td>
                            </tr>
                            <tr>
					            <td>查看新帖时间(分钟):</td>
					            <td>
					                <@c.textBox id="viewnewtopicminute" width="50" value="${config.viewnewtopicminute}"  size="5" maxlength="5" hintTitle="提示" hintInfo="设置多长时间内发布的帖子算是新帖" 
					                min="5" max="14400"></@c.textBox>
					            </td>
                            </tr>
                            <tr>
                                <td>是否开启左右分栏:</td>
                                <td>
                                    <@c.radioButtonList id="isframeshow"   hintTitle="提示" 
				                        hintInfo="开启此功能后，论坛的头部会出现切换的链接(平板模式/分栏模式)">
						                <@c.radioItem name="isframeshow" checked="${config.isframeshow}" value="0">关闭</@c.radioItem><br>
						                <@c.radioItem name="isframeshow" checked="${config.isframeshow}" value="1">开启，默认为平板模式</@c.radioItem><br>
						                <@c.radioItem name="isframeshow" checked="${config.isframeshow}" value="2">开启，默认为分栏模式</@c.radioItem>
					                </@c.radioButtonList>
                                </td>
                            </tr>
			            </table>			        
			        </td>
			        <td  class="panelbox" width="50%" align="right">
			            <table width="100%">
                            <tr>
                              <td colspan="2"><img id="preview"  alt="选择模板预览" src="../../templates/${templatepath}/about.png" /></td>
                            </tr>
		                    <tr>
				                <td>版主显示方式:</td>
                                <td>
                                    <@c.radioButtonList id="moddisplay"   hintTitle="提示" hintInfo="首页论坛列表中版主显示方式">
                                        <@c.radioItem name="moddisplay" checked="${config.moddisplay}" value="1">下拉菜单</@c.radioItem>
                                        <@c.radioItem name="moddisplay" checked="${config.moddisplay}" value="0">平面显示</@c.radioItem>
                                    </@c.radioButtonList>
                                </td>
			                </tr>
			                <tr>
					            <td>是否显示头像:</td>
					            <td>
					                <@c.radioButtonList id="showavatars"   hintTitle="提示" hintInfo="是否在帖子中显示会员头像">
							            <@c.radioItem name="showavatars" checked="${config.showavatars}" value="1">是</@c.radioItem>
							            <@c.radioItem name="showavatars" checked="${config.showavatars}" value="0">否</@c.radioItem>
						            </@c.radioButtonList>
					            </td>
				            </tr>
			                <tr>
					            <td>帖子中同一表情符<br />出现的最大次数:</td>
					            <td>
					                <@c.textBox id="smiliesmax"   width="50" value="${config.smiliesmax}" size="6" maxlength="4" hintTitle="提示" hintInfo="设置在帖子中出现同一表情的次数,默认值为5"></@c.textBox>
					             </td>
				            </tr>
				            
				            <tr>
					            <td>显示在线用户:</td>
					            <td>
						            <@c.radioButtonList id="whosonlinestatus"   hintTitle="提示" 
						            hintInfo="在首页和论坛列表页显示在线会员列表(最大在线超过 500 人系统将自动缩略显示在线列表)">
							            <@c.radioItem name="whosonlinecontract" checked="${config.whosonlinecontract}" value="0">不显示</@c.radioItem><br>
							            <@c.radioItem name="whosonlinecontract" checked="${config.whosonlinecontract}" value="1">仅在首页显示</@c.radioItem><br>
							            <@c.radioItem name="whosonlinecontract" checked="${config.whosonlinecontract}" value="2">仅在分论坛显示</@c.radioItem><br>
							            <@c.radioItem name="whosonlinecontract" checked="${config.whosonlinecontract}" value="3">在首页和分论坛显示</@c.radioItem>
						            </@c.radioButtonList>
					            </td>
				            </tr>
				            <tr>
					            <td>在线列表是否隐藏游客:</td>
					            <td>
						            <@c.radioButtonList id="whosonlinestatus"   hintTitle="提示" 
						            hintInfo="在线列表是否隐藏游客">
							            <@c.radioItem name="whosonlinestatus" checked="${config.whosonlinestatus}" value="1">是</@c.radioItem>
							            <@c.radioItem name="whosonlinestatus" checked="${config.whosonlinestatus}" value="0">否</@c.radioItem>
						            </@c.radioButtonList>
					            </td>
				            </tr>
				            <tr>
					            <td>最多显示在线人数:</td>
					            <td>
						            <@c.textBox id="maxonlinelist" width="50" value="${config.maxonlinelist}"  size="5" maxlength="4" hintTitle="提示" 
						            hintInfo="此设置只有在显示在线用户启用时才有效. 设置为0则为不限制"></@c.textBox>
					            </td>
				            </tr>
				            <tr>
					            <td>无动作离线时间:</td>
					            <td>
						            <@c.textBox id="onlinetimeout" width="50" size="5" value="${timeout}" maxlength="4" hintTitle="提示" 
						            hintInfo="多久无动作视为离线, 默认为10"></@c.textBox>(单位:分钟)
					            </td>
				            </tr>
			            </table>
			        </td>
			    </tr>
			    </table>
		    </fieldset>
		    <div class="Navbutton">
		        <@c.button id="SaveInfo" text="提交"  />
		    </div>
		</form>
	</div>			
	${footer}
</body>
</html>
