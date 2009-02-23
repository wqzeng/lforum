<#import "../../controls/control.ftl" as c>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" >
    <head runat="server">
        <title>论坛选项</title>
        <script type="text/javascript" src="../js/common.js"></script>		
	    <link href="../styles/dntmanager.css" type="text/css" rel="stylesheet" />        
        <link href="../styles/modelpopup.css" type="text/css" rel="stylesheet" />
	    <script type="text/javascript" src="../js/modalpopup.js"></script>
    	<@c.validator "Form1" />
        <script type="text/javascript">
            function ratevalveimg(ratelevel)
            {
                var imgelement =  '<img src="../images/agree.gif" border="0" alt="" style="vertical-align:middle;"/>';
                var valveimg = '';
                if(isie())
                {
                    imgelement = '<img src="../images/agree.gif" border="0" alt="" />';
                }
	            for(i = 0; i < ratelevel; i++) {
		            valveimg += imgelement;  
	            }            	
	            return valveimg;
            }
        </script>
    </head>
    <body>
    <form id="Form1" method="post">
    ${htmlBuilder}
    <div class="ManagerForm">
    <fieldset>
		<legend style="background:url(../images/icons/legendimg.jpg) no-repeat 6px 50%;">论坛选项</legend>
		<table cellspacing="0" cellpadding="4" width="100%" align="center">
            <tr>
                <td  class="panelbox" align="left" width="50%">
                    <table width="100%">
                        <!-- <tr>
			                <td style="width: 120px">主题浏览统计队列:</td>
			                <td>
			                    <span id="Span1"  onmouseover="showhintinfo(this,0,35,'提示','建议访问量大时开启,访问积累一定数量统一更新,减少服务器压力. 如开启,建议队列长度为20-50','50','down','0');" onmouseout="hidehintinfo();">
			                    <input id="Topicqueuestats_1"  
			                    type="radio" name="Topicqueuestats" value="1" checked="true" onclick="document.getElementByid('topicqueuestatscount').style.visibility='visible';" />开启
			                    <input id="Topicqueuestats_0"  type="radio" name="Topicqueuestats" value="0" 
			                    onclick="document.getElementByid('topicqueuestatscount').style.visibility='hidden';" />关闭
			                    </span>
				                &nbsp;&nbsp;<@c.textBox style="visibility:hidden;" id="topicqueuestatscount"  value="${config.topicqueuestatscount}" size="5" 
				                maxlength="4" max="1000" min="0" width="50"></@c.textBox>
			                </td>
                        </tr>-->
                        <tr>
			                <td>使用论坛流量统计:</td>
			                <td>
			                    <@c.radioButtonList id="statstatus"  hintInfo="选择&amp;quot;是&amp;quot;将打开论坛统计功能,提供详细的论坛访问统计信息." hintTitle="提示" >
					                <@c.radioItem name="statstatus" value="1" checked="${config.statstatus}">是</@c.radioItem>
					                <@c.radioItem name="statstatus" value="0" checked="${config.statstatus}">否</@c.radioItem>
				                </@c.radioButtonList>
			                </td>
			            </tr>
                        <tr>
		                    <td>是否显示组别:</td>
			                <td>
				                <@c.radioButtonList id="userstatusby"    hintTitle="提示" 
				                hintInfo="浏览帖子时是否显示用户所在的组">
					                <@c.radioItem value="1" name="userstatusby" checked="${config.userstatusby}">是</@c.radioItem>	
					                <@c.radioItem value="0" name="userstatusby" checked="${config.userstatusby}">否</@c.radioItem>					
				                </@c.radioButtonList>
		                    </td>
                        </tr>			            
			            <tr>
			                <td style="width: 130px">统计系统缓存时间:</td>
			                <td>
			                    <@c.textBox id="statscachelife" value="${config.statscachelife}"  width="50" size="6" maxlength="4" hintTitle="提示" 
			                    hintInfo="统计数据缓存更新的时间,数值越大,数据更新频率越低,越节约资源,但数据实时程度越低,建议设置为 60 以上,以免占用过多的服务器资源"></@c.textBox>(单位:分钟)
			                </td>
			            </tr>
                        <tr>
			                <td style="width: 130px">管理记录保留时间:</td>
			                <td>
			                    <@c.textBox id="maxmodworksmonths" value="${config.maxmodworksmonths}"  width="50" size="6" maxlength="4" hintTitle="提示" 
			                    hintInfo="系统中保留管理记录的时间,默认为 3 个月,建议在 3～6 个月的范围内取值"></@c.textBox>(单位:月)
			                </td>
			            </tr>
                        <tr>
			                <td>缓存游客页面<br />的失效时间:</td>
			                <td>
			                    <@c.textBox id="guestcachepagetimeout"   hintTitle="提示" 
			                    hintInfo="论坛在线人数大时建议开启, 为0不缓存, 大于0则缓存该值的时间,单位为分钟, 建议值为10." width="50" size="4" 
			                    value="${config.guestcachepagetimeout}" maxlength="3"></@c.textBox>(单位:分钟)
			                </td>
                        </tr>
                        <tr>
			                <td>编辑帖子<br />时间限制:</td>
			                <td>
				                <@c.textBox id="edittimelimit"  hintInfo="帖子作者发帖后超过此时间限制将不能再编辑帖, 版主和管理员不受此限制, 0 为不限制" 
				                hintTitle="提示" width="50" value="${config.edittimelimit}" size="5" maxlength="4"></@c.textBox>(单位:分钟)
			                </td>
                        </tr>
                        <tr>
		                    <td>每页主题数:</td>
			                <td>
                                <@c.textBox id="tpp"  width="50" value="${config.tpp}" size="6" maxlength="4" hintTitle="提示" hintInfo="版块每页显示的主题数"></@c.textBox>
                            </td>
                        </tr>
                        <tr>
		                    <td>热门话题最低帖数:</td>
			                <td>
				                <@c.textBox id="hottopic"  width="50" value="${config.hottopic}" size="6" maxlength="4" hintTitle="提示" 
				                hintInfo="超过一定帖子数的话题将显示为热门话题"></@c.textBox>
			                </td>
                        </tr>
                        <tr>
		                    <td>是否允许使用<br />标签(Tag)功能:</td>
		                    <td>
                                <@c.radioButtonList id="enabletag"  hintInfo="选择允许使用标签功能."
                                    hintTitle="提示" >
                                    <@c.radioItem name="enabletag" checked="${config.enabletag}" value="1">是</@c.radioItem>
                                    <@c.radioItem name="enabletag" checked="${config.enabletag}" value="0">否</@c.radioItem>
                                </@c.radioButtonList>
		                    </td>
                        </tr>                        
                        <tr>
			                <td>首页显示热门<br />标签(Tag)数量设置:</td>
			                <td>
			                    <@c.textBox id="hottagcount" value="${config.hottagcount}"  width="50" min="0" max="60" size="6" maxlength="2" hintTitle="提示" 
			                    hintInfo="取值范围为0~60.如果取值为0,则关闭首页热门标签(Tag)的显示"></@c.textBox>
			                </td>
			            </tr>
                        <tr>
		                    <td>快速发帖:</td>
		                    <td>
		                        <@c.radioButtonList id="fastpost"  hintTitle="提示" hintInfo="浏览论坛和帖子页面底部显示快速发帖表单">
				                    <@c.radioItem name="fastpost" checked="${config.fastpost}" value="0">不显示</@c.radioItem><br>
				                    <@c.radioItem name="fastpost" checked="${config.fastpost}" value="1">只显示快速发表主题</@c.radioItem><br>
				                    <@c.radioItem name="fastpost" checked="${config.fastpost}" value="2">只显示快速发表回复</@c.radioItem><br>
				                    <@c.radioItem name="fastpost" checked="${config.fastpost}" value="3">同时显示快速发表主题和回复</@c.radioItem>
			                    </@c.radioButtonList>
		                    </td>
                        </tr>
                        <tr>
                            <td>新用户广告<br />强力屏蔽:</td>
                            <td>
                                <@c.radioButtonList id="disablepostad"  hintInfo="是否启用新用户广告强力屏蔽功能"
                                    hintTitle="提示" >
                                    <@c.radioItem name="disablepostad" checked="${config.disablepostad}" value="1">是</@c.radioItem>
                                    <@c.radioItem name="disablepostad" checked="${config.disablepostad}" value="0">否</@c.radioItem>
                                </@c.radioButtonList><br />
                                <div id="postadstatus" >
                                    <table width="100%">
                                    <tr>
                                    <td>注册分钟:</td>
                                    <td><@c.textBox id="disablepostadregminute" width="50" value="${config.disablepostadregminute}" size="6" maxlength="4" hintTitle="提示" 
				                        hintInfo="用户注册N分钟内进行新用户广告强力屏蔽功能检查,0为不行进该项检查"></@c.textBox>(分钟)
				                     </td>
				                     </tr>
				                     <tr>
				                     <td>发帖数:</td>
				                     <td><@c.textBox id="disablepostadpostcount"  width="50" value="${config.disablepostadpostcount}" size="6" maxlength="4" hintTitle="提示" 
				                        hintInfo="用户发帖N帖内进行新用户广告强力屏蔽功能检查,0为不行进该项检查"></@c.textBox>(帖)
				                     </td>
				                     </tr>
				                     <tr>
				                     <td colspan="2">正则式:</td>
				                     </tr>
				                     <tr>
				                     <td colspan="2">
				                         <@c.textareaResize id="disablepostadregular" value="${config.disablepostadregular}"   cols="30" hintTitle="提示" 
				                            hintInfo="用于对新用户进行广告屏蔽的正则表达式,每条正则表达式用回车符间隔"></@c.textareaResize>
                                     </td>
				                     </tr>                            
                                    </table>
                                </div>
                            </td>
                        </tr>
                    </table>
                </td>
                <td  class="panelbox" align="right" width="50%">
                    <table width="100%">
		                <tr>						        
			                <td style="width: 100px">是否允许使用<br />HTML标题:</td>
                            <td>
				                <@c.radioButtonList id="allowhtmltitle"  hintInfo="如果允许使用HTML标题,还需在&amp;quot;用户权限&amp;quot;中设置哪些组可以使用." 
				                hintTitle="提示" >
					                <@c.radioItem name="allowhtmltitle" checked="${config.htmltitle}" value="1">是</@c.radioItem>
					                <@c.radioItem name="allowhtmltitle" checked="${config.htmltitle}" value="0">否</@c.radioItem>
				                </@c.radioButtonList>
			                </td>
		                </tr>
		                <tr>
		                    <td>启用论坛管<br />理工作日志:</td>
			                <td>
				                <@c.radioButtonList id="modworkstatus"    hintTitle="提示" 
				                hintInfo="论坛管理工作统计可以使管理员了解版主等管理人员的工作状况. 注意: 本功能会轻微加重系统负担">
					                <@c.radioItem name="modworkstatus" checked="${config.modworkstatus}" value="1">是</@c.radioItem>
					                <@c.radioItem name="modworkstatus" checked="${config.modworkstatus}" value="0">否</@c.radioItem>
				                </@c.radioButtonList>
			                </td>
		                </tr>
			            <tr>
			                <td>用户在线时间<br />更新时长:</td>
			                <td>
			                    <@c.textBox id="oltimespan" width="50" value="${config.oltimespan}" size="6" maxlength="4" hintTitle="提示" 
			                    hintInfo="可统计每个用户总共和当月的在线时间,本设置用以设定更新用户在线时间的时间频率.例如设置为 10,则用户每在线 10 分钟更新一次记录.本设置值越小,则统计越精确,但消耗资源越大.建议设置为 5～30 范围内,0 为不记录用户在线时间"></@c.textBox>(单位:分钟)
			                </td>
			            </tr>
		                <tr>
			                <td>缓存游客查看主<br />题页面的权重:</td>
			                <td>
				                <@c.textBox id="topiccachemark"  hintTitle="提示" hintInfo="为0则不缓存, 范围0 - 100  (数字越大, 缓存数据越多)" 
				                 width="50" value="${config.topiccachemark}"  size="4" maxlength="3" max="100" min="0"></@c.textBox>
			                </td>
		                </tr>
		                <tr>
			                <td>删帖不减积<br />分时间期限:</td>
                            <td>
				                <@c.textBox id="losslessdel"   
				                hintInfo="设置版主或管理员从前台删除发表于多少天以前的帖子时, 不更新用户积分, 可用于清理老帖子而不对作者积分造成损失. 0 为不使用此功能, 始终更新用户积分" 
				                hintTitle="提示" width="50" value="${config.losslessdel}" size="5" maxlength="4"></@c.textBox>(单位:天)
			                </td>
		                </tr>
		                <tr>
			                <td>编辑帖子附<br />加编辑记录:</td>
                            <td>
				                <@c.radioButtonList id="editedby"   hintInfo="在 60 秒后编辑帖子添加“本帖由 xxx 于 xxxx-xx-xx 编辑”字样. 管理员编辑不受此限制" 
				                hintTitle="提示">
					                <@c.radioItem name="editedby" checked="${config.editedby}" value="1">是</@c.radioItem>
					                <@c.radioItem name="editedby" checked="${config.editedby}" value="0">否</@c.radioItem>
				                </@c.radioButtonList>
			                </td>
		                </tr>
		                <tr>
                            <td>每页帖子数:</td>
			                <td>
                                <@c.textBox id="ppp"  width="50" value="${config.ppp}" size="6" maxlength="4" hintTitle="提示" hintInfo="查看主题时每页帖子数"></@c.textBox>
                            </td>
		                </tr>
		                <tr>
			                <td>星星升级阀值:</td>
				                <td>
			                    <@c.textBox id="starthreshold" width="50" value="${config.starthreshold}"  size="6" maxlength="4" hintTitle="提示" 
			                    hintInfo="N 个星星显示为 1 个月亮、N 个月亮显示为 1 个太阳. 默认值为 2, 如设为 0 则取消此项功能, 始终以星星显示"></@c.textBox>
		                    </td>
		                </tr>
                        <tr>
		                    <td>默认的编辑器模式:</td>
                            <td>
				                <@c.radioButtonList id="defaulteditormode"  hintInfo="默认的编辑器模式" hintTitle="提示">
					                <@c.radioItem name="defaulteditormode" checked="${config.defaulteditormode}" value="0">LForum代码编辑器</@c.radioItem><br>
					                <@c.radioItem name="defaulteditormode" checked="${config.defaulteditormode}" value="1">可视化编辑器</@c.radioItem>
				                </@c.radioButtonList>
			                </td>
                        </tr>
		                <tr>
			                <td>是否允许切换<br />编辑器模式:</td>
                            <td>
				                <@c.radioButtonList id="allowswitcheditor"  hintInfo="选择否将禁止用户在 LForum 代码模式和所见即所得模式之间切换." hintTitle="提示" >
					                <@c.radioItem name="allowswitcheditor" checked="${config.allowswitcheditor}" value="1">是</@c.radioItem>
					                <@c.radioItem name="allowswitcheditor" checked="${config.allowswitcheditor}" value="0">否</@c.radioItem>
				                </@c.radioButtonList>
			                </td>
		                </tr>
		                <tr>			    
		                    <td>评分等级:</td>
		                    <td>
		                        <@c.textBox id="ratevalveset1" value="${ratevalveset1}" width="50"  size="3" maxlength="3"></@c.textBox> <script type="text/javascript">document.write(ratevalveimg(1));</script><br />
		                        <@c.textBox id="ratevalveset2" value="${ratevalveset2}" width="50"  size="3" maxlength="3"></@c.textBox> <script type="text/javascript">document.write(ratevalveimg(2));</script><br />
		                        <@c.textBox id="ratevalveset3" value="${ratevalveset3}" width="50"  size="3" maxlength="3"></@c.textBox> <script type="text/javascript">document.write(ratevalveimg(3));</script><br />
		                        <@c.textBox id="ratevalveset4" value="${ratevalveset4}" width="50"  size="3" maxlength="3"></@c.textBox> <script type="text/javascript">document.write(ratevalveimg(4));</script><br />
		                        <@c.textBox id="ratevalveset5" value="${ratevalveset5}" width="50"  size="3" maxlength="3"></@c.textBox> <script type="text/javascript">document.write(ratevalveimg(5));</script>
		                    </td>
		                </tr>
                    </table>
                </td>
            </tr>
        </table>
	</fieldset>
	<@c.hint hintImageUrl="../images" />	
    <div class="Navbutton">
	    <@c.button id="SaveInfo" text=" 提交"  />
	</div>
    </div>   
    </form>
    ${footer}
</body>
</html>

