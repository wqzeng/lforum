<#import "../../controls/control.ftl" as c>
<html>
	<head>
		<title>添加版块</title>
		<link href="../styles/datagrid.css" type="text/css" rel="stylesheet" />
		<link href="../styles/tab.css" type="text/css" rel="stylesheet" />
	    <script type="text/javascript" src="../js/common.js"></script>
	    <script type="text/javascript" src="../js/tabstrip.js"></script>
	    <link href="../styles/dntmanager.css" type="text/css" rel="stylesheet" />        
        <link href="../styles/modelpopup.css" type="text/css" rel="stylesheet" />
        <script type="text/javascript" src="../js/modalpopup.js"></script>
        <script type="text/javascript" src="../js/AjaxHelper.js"></script>
        <@c.validator "Form1" />
        <style type="text/css">
	    .td_alternating_item1{font-size: 12px;}
	    .td_alternating_item2{font-size: 12px;background-color: #F5F7F8;}
	    </style>
	</head>
	<body >
		<form id="Form1" method="post" >
		 ${htmlBuilder}
		 <@c.tabControl id="TabControl1" tabScriptPath="../js/tabstrip.js" height="100%" default="tabPage1">
		    <@c.tabPage caption="基本信息" id="tabPage1" tabid="TabControl1" default="tabPage1"/>
		    <@c.tabPage caption="权限设定" id="tabPage2" tabid="TabControl1" default="tabPage1"/>
		     <@c.tabPage caption="高级设置" id="tabPage3" tabid="TabControl1" default="tabPage1"/>
		</@c.tabControl>
		<div id="TabControl1tabarea" class="tabarea">
		<@c.tabItem tabid="TabControl1" id="tabPage1" default="tabPage1">
			<table cellspacing="0" cellpadding="4" width="100%" align="center">
                <tr>
                    <td class="panelbox" align="left" width="50%">
                        <table width="100%">
                            <tr>
							    <td style="width: 90px">论坛名称:</td>
							    <td>
								    <@c.textBox id="name"  required="true" width="250" size="30"  maxlength="49"></@c.textBox>
							    </td>
                            </tr>
                            <tr>
						        <td style="width: 90px">论坛描述:</td>
							    <td>
							        <@c.textareaResize id="description"  rows="5"></@c.textareaResize>
							    </td>
						    </tr>
                        </table>
                    </td>
                    <td class="panelbox" align="left" width="50%">
                        <table width="100%">
                            <tr>          
                            <td style="width: 90px">是否显示:</td>
						    <td>
						        <@c.radioButtonList id="status" hintTitle="提示" hintInfo="设置本版块是否是隐藏版块" >
						        <@c.radioItem name="status" value="1" checked="1">显示</@c.radioItem>
						        <@c.radioItem name="status" value="0" checked="1">不显示</@c.radioItem>
						        </@c.radioButtonList>
						    </td>                     
                            </tr>
					       <tr>
							    <td>版主列表:</td>
						    	<td>
							    <@c.textareaResize id="moderators" hintTitle="提示" hintInfo="当前版块版主列表，以&amp;quot;,&amp;quot;进行分割" rows="5"></@c.textareaResize>
							    <br />以','进行分割,如:lisi,zhangsan
								</td>
						    </tr>
                        </table>
                    </td>
                </tr>
            </table>
			</@c.tabItem>			
			<@c.tabItem tabid="TabControl1" id="tabPage2" default="tabPage1">
            <@c.pageInfo id="info1" icon="Information" text="每个组的权限项不选择时,权限为使用本版块用户的用户组权限设置,且版块权限设置优先于用户组权限设置."></@c.pageInfo>    			
                <table width="100%" id="powerset" align="center" class="table1" cellspacing="0" cellPadding="4"  bgcolor="#C3C7D1" >	
				    <tr>
					    <td class="td_alternating_item2">全选</td>
					    <td class="td_alternating_item2"><input type="checkbox" id="c1" onclick="seleCol('viewperm',this.checked)"/><label for="c1">浏览论坛</label></td>
					    <td class="td_alternating_item2"><input type="checkbox" id="c2" onclick="seleCol('postperm',this.checked)"/><label for="c2">发新话题</label></td>
					    <td class="td_alternating_item2"><input type="checkbox" id="c3" onclick="seleCol('replyperm',this.checked)"/><label for="c3">发表回复</label></td>
					    <td class="td_alternating_item2"><input type="checkbox" id="c4" onclick="seleCol('getattachperm',this.checked)"/><label for="c4">下载/查看附件</label></td>
					    <td class="td_alternating_item2"><input type="checkbox" id="c5" onclick="seleCol('postattachperm',this.checked)"/><label for="c5">上传附件</label></td>
				    </tr>
				<#list groupList as group>
					<#assign class="td_alternating_item1"/>
					<#if (group_index+1)%2 != 1>
					<#assign class="td_alternating_item2"/>
					</#if>
				 	<tr>
						<td class="${class}"><input type='checkbox' id='r${group_index+1}' onclick='selectRow(${group_index+1},this.checked)'><label for='r${group_index+1}'>${group.grouptitle}</lable></td>
						<td class="${class}"><input type='checkbox' name='viewperm' id='viewperm${group_index+1}' value='${group.groupid}'></td>
						<td class="${class}"><input type='checkbox' name='postperm' id='postperm${group_index+1}' value='${group.groupid}'></td>
						<td class="${class}"><input type='checkbox' name='replyperm' id='replyperm${group_index+1}' value='${group.groupid}'></td>
						<td class="${class}"><input type='checkbox' name='getattachperm' id='getattachperm${group_index+1}' value='${group.groupid}'></td>
						<td class="${class}"><input type='checkbox' name='postattachperm' id='postattachperm${group_index+1}' value='${group.groupid}'></td>
					</tr>		
				</#list>
                </table>
			</@c.tabItem>            
			<@c.tabItem tabid="TabControl1" id="tabPage3" default="tabPage1">            
            <table cellspacing="0" cellpadding="4" width="100%" align="center">
            <tr>
                <td  class="panelbox" align="left" width="50%">
                    <table width="100%">
                        <tr>
						    <td style="width: 90px">访问本论<br />坛的密码:</td>
						    <td>
							    <@c.textBox id="password" hintTitle="提示" hintInfo="设置本版块的密码,留空则本版块不使用密码"
							    maxlength="16" size="20"></@c.textBox>
							</td>
                        </tr>
                        <tr>
						    <td>指向外部链<br />接的地址:</td>
						    <td>
							    <@c.textBox id="redirect" maxlength="253" hintTitle="提示" hintInfo="设置版块为一个链接，当点击本版块是将跳转到指定的地址上"></@c.textBox>
							</td>
                        </tr>
                        <tr>
						    <td>本版规则:</td>
						    <td>
							    <@c.textBox id="rules" hintTitle="提示" hintInfo="支持Html" 
							    width="250" height="100" multiLine="true"></@c.textBox>
							</td>
                        </tr>
                        <tr>
                        </tr>
                    </table>
                </td>
                <td  class="panelbox" align="right" width="50%">
                    <table width="100%">
					    <tr>
						    <td style="width: 90px">论坛图标:</td>
						    <td>
							    <@c.textBox id="icon" hintTitle="提示" hintInfo="显示于首页论坛列表等" width="250px"
							       maxlength="253"></@c.textBox>
							</td>
					    </tr>
					    <tr>
						    <td>允许的附<br />件类型:</td>
						    <td>
								    <@c.checkBoxList id="attachextensions" hintTitle="提示"  repeatColumns="1"
								    hintInfo="允许在本论坛上传的附件类型,留空为使用用户组设置, 且版块设置优先于用户组设置"><tr><#assign n=0/><#list attachtypeList as attachtype><#if n == 4></tr><tr><#assign n=-1/></#if><td><@c.checkBoxItem id="attachextensions_${attachtype.id}" name="attachextensions" value="${attachtype.id}">${attachtype.extension}</@c.checkBoxItem><td><#assign n=n+1/></#list></tr></@c.checkBoxList>
						    </td>
					    </tr>
					    <tr>
						    <td>定期自动<br />关闭主题:</td>
						    <td>
						        <table>
						            <tr>
						                <td>
					                        <@c.radioButtonList id="autocloseoption" repeatColumns="1" 
					                        onclick="javascript:document.getElementById('showclose').style.display= (document.getElementById('autocloseoption0').checked ? 'none' : 'block');"><tr></td>
                                            <@c.radioItem name="autocloseoption" id="autocloseoption0" value="0" checked="0">不自动关闭</@c.radioItem></td><td>
                                            <@c.radioItem name="autocloseoption" id="autocloseoption1" value="1" checked="0">按发布时间</@c.radioItem></td></tr>
			 		                        </@c.radioButtonList>
			 		                    </td>
			 		                    <td valign=bottom>
	 		                                <div id="showclose" style="display:none">
	 		    	                            <@c.textBox id="autocloseday" width="50" value="4" size="4" maxlength="3"></@c.textBox>天自动关闭	
	 		                                </div>
			 		    	            </td>
			 		    	        </tr>
			 		    	    </table>
		       			    </td>
		       		    </tr>
	       		        <tr>
	       		            <td>只允许发布特<br />殊类型主题:</td>
	       		            <td>
	       		                <@c.radioButtonList id="allowspecialonly" hintTitle="提示" hintInfo="设置本版是否只允许发布特殊类型主题">
						            <@c.radioItem name="allowspecialonly" value="1" checked="0">是</@c.radioItem>
						            <@c.radioItem name="allowspecialonly" value="0" checked="0">否</@c.radioItem>
						        </@c.radioButtonList>
	       		            </td>
	       		        </tr>
                    </table>
                </td>
            </tr>
            <tr>
                <td class="panelbox" colspan="2">
                    <table width="100%">
                        <tr>
						    <td style="width: 90px">设置:</td>
						    <td>
							    <table id="setting" class="buttonlist" border="0" style="width:100%;">
	<tr>
		<td><input id="setting_0" type="checkbox" name="Allowsmilies" value="1" /><label for="setting_0">允许使用表情符</label></td><td><input id="setting_4" type="checkbox" name="Recyclebin" value="1" /><label for="setting_4">打开回收站</label></td><td><input id="setting_8" type="checkbox" name="Inheritedmod "  value="1"/><label for="setting_8">继承上级论坛或分类的版主设定</label></td><td><input id="setting_11" type="checkbox" name="allowpoll"  value="1"/><label for="setting_11">允许发投票</label></td>
	</tr><tr>
		<td><input id="setting_1" type="checkbox" name="Allowrss" value="1"/><label for="setting_1">允许RSS</label></td><td><input id="setting_5" type="checkbox" name="Modnewposts"  value="1"/><label for="setting_5">发帖需要审核</label></td><td><input id="setting_9" type="checkbox" name="Allowthumbnail"  value="1"/><label for="setting_9">主题列表中显示缩略图</label></td><td><input id="setting_12" type="checkbox" name="allowbianl"  value="1"/><label for="setting_12">允许辩论</label></td>
	</tr><tr>
		<td><input id="setting_2" type="checkbox" name="Allowbbcode" value="1"/><label for="setting_2">允许LForum代码</label></td><td><input id="setting_6" type="checkbox" name="Jammer"  value="1"/><label for="setting_6">帖子中添加干扰码</label></td><td><input id="setting_10" type="checkbox" name="Allowtag"  value="1"/><label for="setting_10">允许标签</label></td><td><input id="setting_13" type="checkbox" name="allowxuans"  value="1"/><label for="setting_13">允许悬赏</label></td>
	</tr><tr>
		<td><input id="setting_3" type="checkbox" name="Allowimgcode" value="1"/><label for="setting_3">允许[img]代码</label></td><td><input id="setting_7" type="checkbox" name="Disablewatermark " value="1" /><label for="setting_7">禁止附件自动水印</label></td><td></td><td></td>
	</tr>
</table>
							</td>
                        </tr>
                    </table>
                </td>
            </tr>
            </table>
		    </@c.tabItem>           
			</div>
            <div class="Navbutton">
			   <@c.hint hintImageUrl="../images" />
		       <@c.button id="SubmitAdd" text=" 添 加 "  />
			</div>
		</form>
	${footer}
	</body>
</html>
