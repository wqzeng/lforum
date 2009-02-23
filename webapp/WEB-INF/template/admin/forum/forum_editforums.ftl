<#import "../../controls/control.ftl" as c>
<html>
	<head>
		<title>编辑版块</title>
		<link href="../styles/datagrid.css" type="text/css" rel="stylesheet" />
		<link href="../styles/tab.css" type="text/css" rel="stylesheet" />
	    <script type="text/javascript" src="../js/common.js"></script>
	    <script type="text/javascript" src="../js/tabstrip.js"></script>
	    <link href="../styles/dntmanager.css" type="text/css" rel="stylesheet" />        
        <link href="../styles/modelpopup.css" type="text/css" rel="stylesheet" />
        <script type="text/javascript" src="../js/modalpopup.js"></script>
        <@c.validator "Form1" />
	    <style type="text/css">
	    .td_alternating_item1{font-size: 12px;}
	    .td_alternating_item2{font-size: 12px;background-color: #F5F7F8;}
	    </style>
	    <script type="text/javascript">
		function editcredit(fid,fieldname)
		{
			window.location="forum_scorestrategy.action?fid="+fid+"&fieldname="+fieldname;
		}
		function Check(form,bCheck,findstring)
		{
			for (var i=0;i<form.elements.length;i++)
			{
			var e = form.elements[i];
			if (e.name.indexOf(findstring) >= 0)
				e.checked = bCheck;
			}
		}
		function CheckRow(form,bCheck,rowId)
		{
			for (var i=0;i<form.elements.length;i++)
			{
			var e = form.elements[i];
			if (e.name.indexOf(rowId + ":viewbyuser") >= 0 || e.name.indexOf(rowId + ":postbyuser") >= 0
				 || e.name.indexOf(rowId + ":replybyuser") >= 0 || e.name.indexOf(rowId + ":getattachbyuser") >= 0
				  || e.name.indexOf(rowId + ":postattachbyuser") >= 0)
				e.checked = bCheck;
			}
		}
		function AddTopicType()
		{
		    typename = $("typename").value;
		    typeorder = $("typeorder").value;
		    typedescription = $("typedescription").value;
		    if(typename == "")
		    {
		        alert("主题分类名称不能为空！");
		        $("typename").focus();
		        return false;
		    }
		    if(!(/^\d+$/.test(typeorder)))
		    {
		        alert("显示顺序不能为空并且只能为数字！");
		        $("typeorder").value = "";
		        $("typeorder").focus();
		        return false;
		    }
		    AjaxHelper.Updater('../UserControls/addtopictype','resultmessage','typename='+typename+'&typeorder='+typeorder+'&typedescription='+typedescription,ResultFun);
		}
		function ResultFun()
		{
		    resultstring = $("resultmessage").innerHTML;
		    if(resultstring.indexOf("false") == -1)
		    {
		        var maxId = resultstring.replace(" </FORM>","");
		        if(/\s+/.test(maxId))
		        {
		            maxId = maxId.substring(0,maxId.length - 3);
		        }
                var theDoc = document;
		        var typetable = $("TabControl1_tabPage5_TopicTypeDataGrid");
		        var tbody = typetable.lastChild;
		        lasttr = tbody.lastChild;
		        tbody.removeChild(tbody.childNodes[tbody.childNodes.length - 1]);
		        rowscount = tbody.childNodes.length - 1;
                
                tr = theDoc.createElement("tr");
                if(window.navigator.appName == "Netscape")
                {                
                    tr.setAttribute("onmouseover","this.className='mouseoverstyle'");                    
                    tr.setAttribute("onmouseout","this.className='mouseoutstyle'");                    
                    tr.setAttribute("style","cursor:hand;");
                }
                else
                {
                    tr.onmouseover = "this.className='mouseoverstyle'";
                    tr.onmouseout = "this.className='mouseoutstyle'";
                    tr.style.cursor = "hand";
                }
                
		        td = GetTd();
		        td.innerHTML = $("typename").value;
		        tr.appendChild(td);
		        
		        td = GetTd();
		        td.innerHTML = $("typedescription").value;
		        tr.appendChild(td);
		        
		        td = GetTd();
		        td.innerHTML = "<input type='hidden' name='oldtopictype" + rowscount + "' value='' /><input type='radio' name='type" + rowscount + "' value='-1' />";
		        tr.appendChild(td);
		        
		        td = GetTd();
		        td.innerHTML = "<input type='radio' name='type" + rowscount + "' checked value='" + maxId + "," + $("typename").value + ",0|' />";
		        tr.appendChild(td);
		        
		        td = GetTd();
		        td.innerHTML = "<input type='radio' name='type" + rowscount + "' value='" + maxId + "," + $("typename").value + ",1|' />";
		        tr.appendChild(td);
		        
		        tbody.appendChild(tr);
		        tbody.appendChild(lasttr);
		        $("typename").value = "";
		        $("typeorder").value = "";
		        $("typedescription").value = "";
		    }
		    else
		    {
		        alert("数据库中已存在相同的主题分类名称");
		    }
		}
		
		function GetTd()
		{
		        td = document.createElement("td");
		        td.setAttribute("nowrap","nowrap");
		        td.style.borderColor = "#EAE9E1";
		        td.style.borderWidth = "1px";
		        td.style.borderStyle = "solid";
		        return td;
		}
        </script>
    </head>

	<body>
		<form name="Form1" id="Form1" method="post">
		${htmlBuilder}
	    <table width="100%">
	    <tr>
	    <td></td>
	    <td><br />
		    <span style="font-size:12px">当前版块为: <b>${forum.name}</b></span>
		    <@c.tabControl id="TabControl1" tabScriptPath="../js/tabstrip.js" height="100%" default="tabPage1">
		    	<@c.tabPage caption="基本信息" id="tabPage1" tabid="TabControl1" default="${tabDef}"/>
		    	<@c.tabPage caption="权限设定" id="tabPage2" tabid="TabControl1" default="${tabDef}"/>
		    	<#if forum.layer!=0>
		    	<@c.tabPage caption="高级设置" id="tabPage3" tabid="TabControl1" default="${tabDef}"/>
		    	<@c.tabPage caption="特殊用户" id="tabPage4" tabid="TabControl1" default="${tabDef}"/>
		    	<@c.tabPage caption="主题分类" id="tabPage5" tabid="TabControl1" default="${tabDef}"/>
		    	<@c.tabPage caption="统计信息" id="tabPage6" tabid="TabControl1" default="${tabDef}"/>
		    	</#if>
			</@c.tabControl>
			<div id="TabControl1tabarea" class="tabarea">
			<@c.tabItem tabid="TabControl1" id="tabPage1" default="${tabDef}">
                <table cellspacing="0" cellpadding="4" width="100%" align="center">
                    <tr>
                        <td  class="panelbox" align="left" width="50%">
                            <table width="100%">
                                <tr>
							        <td style="width: 90px">版块名称:</td>
							        <td>
								        <@c.textBox id="name" value="${forum.name}" required="true" size="30"  maxlength="49"></@c.textBox>
							        </td>
                                </tr>
                                <tr>
							        <td>是否显示:</td>
							        <td>
						                <@c.radioButtonList id="status" repeatColumns="2" hintTitle="提示" hintInfo="设置本版块是否是隐藏版块" >
						                <@c.radioItem name="status" value="1" checked="${forum.status}">显示</@c.radioItem>
						        		<@c.radioItem name="status" value="0" checked="${forum.status}">不显示</@c.radioItem>
						                </@c.radioButtonList>
							        </td>
                                </tr>
                                <tr>
							        <td>版主列表:</td>
							        <td>
								        <@c.textareaResize id="moderators" hintTitle="提示" hintInfo="当前版块版主列表，以&amp;quot;,&amp;quot;进行分割" 
								         cols="30" rows="5" value="${forum.forumfields.moderators}"></@c.textareaResize>
								        <br />以','进行分割,如:lisi,zhangsan
							        </td>
                                </tr>
                            </table>
                        </td>
                        <td  class="panelbox" align="right" width="50%">
                            <table width="100%">
						        <tr>
							        <td style="width: 90px">已继承的版主:</td>
							        <td></td>
						        </tr>
						        <tr>
							        <td>显示模式:</td>
							        <td>
							            <table>
							                <tr>
							                    <td>
							                    	<#assign colcount = forum.colcount/>
							                    	<#if colcount==1><#assign colcount = 1/><#else><#assign colcount = 2/></#if>
							                    	<@c.radioButtonList id="colcount"  hintTitle="提示" repeatColumns="1" 
						                    			hintInfo="用来设置该论坛(或分类)的子论坛在列表中的显示方式" 
						                    			onclick="javascript:document.getElementById('showcolnum').style.display= (document.getElementById('colcount1').checked ? 'none' : 'block');"><tr><td>
								               	 		<@c.radioItem name="colcount" id="colcount1" value="1" checked="${colcount}">传统模式[默认]</@c.radioItem ></td></tr><tr><td>
								               			<@c.radioItem name="colcount" id="colcount2" value="2" checked="${colcount}">子版块横排模式</@c.radioItem ></td></tr>
							                		</@c.radioButtonList>
								                </td>
								                <td valign="bottom">
				 		    	                    <div id="showcolnum" style="display:<#if (forum.colcount==1)>none<#else>block</#if>">
				 		    	                        <@c.textBox id="colcountnumber" value="${forum.colcount}" size="2" width="50" maxlength="1"></@c.textBox>
				 		    	                    </div>
				 		                        </td>
				 		                    </tr>
				 		                </table>
				 	    	        </td>
						        </tr>
						        <tr>
							        <td >版块描述:</td>
							        <td>
							            <@c.textareaResize id="description" cols="30" rows="5" value="${forum.forumfields.description}"></@c.textareaResize>
							        </td>
						        </tr>
                            </table>
                        </td>
                    </tr>
					<div id="templatestyle">
					<tr>
                        <td class="panelbox" colspan="2">
                            <table width="100%">
                                <tr>
						            <td style="width: 90px">模板风格:</td>
						            <td><@c.dropDownList id="templateid" hintInfo="设置本版块使用的模板,将不受系统模板限制"><option value="0">请选择     </option>${templatestr}</@c.dropDownList></td>
						            <td>&nbsp;</td>
						            <td>&nbsp;</td>
					            </tr>
					        </table>
					    </td>
					</tr>
					</div>
                </table>
                </@c.tabItem>
                <@c.tabItem tabid="TabControl1" id="tabPage2" default="${tabDef}">
                <@c.pageInfo id="PageInfo1" icon="Information" text="每个组的权限项不选择为使用用户组设置，且版块设置优先于用户组设置."></@c.pageInfo>    			
                <table width="100%" id="powerset" align="center" class="table1" cellspacing="0" cellPadding="4"  bgcolor="#C3C7D1" >	
				    <tr>
					    <td class="td_alternating_item2" width="1%">&nbsp;</td>
					    <td class="td_alternating_item2" width="20%" style="word-wrap: break-word">&nbsp;</td>
					    <td class="td_alternating_item2"><input type="checkbox" id="c1" onclick="seleCol('viewperm',this.checked)"/><label for="c1">浏览论坛</label></td>
					    <td class="td_alternating_item2"><input type="checkbox" id="c2" onclick="seleCol('postperm',this.checked)"/><label for="c2">发新话题</label></td>
					    <td class="td_alternating_item2"><input type="checkbox" id="c3" onclick="seleCol('replyperm',this.checked)"/><label for="c3">发表回复</label></td>
					    <td class="td_alternating_item2"><input type="checkbox" id="c4" onclick="seleCol('getattachperm',this.checked)"/><label for="c4">下载/查看附件</label></td>
					    <td class="td_alternating_item2"><input type="checkbox" id="c5" onclick="seleCol('postattachperm',this.checked)"/><label for="c5">上传附件</label></td>
				    </tr>
				 	${powerset}		
                </table>
                </@c.tabItem>      
                <#if forum.layer!=0>
                 <@c.tabItem tabid="TabControl1" id="tabPage3" default="${tabDef}">
                 <table cellspacing="0" cellpadding="4" width="100%" align="center">
                    <tr>
                        <td  class="panelbox" align="left" width="50%">
                            <table width="100%">
                                <tr>
							        <td style="width: 110px">访问本版块的密码:</td>
							        <td>
								        <@c.textBox id="password" value="${forum.forumfields.password}" hintTitle="提示" hintInfo="设置本版块的密码,留空则本版块不使用密码" maxlength="16" size="20"></@c.textBox>
							        </td>
                                </tr>
                                <tr>
							        <td>发主题积分策略:</td>
							        <td>
							            <a href="#" class="textButton" onclick="javascript:editcredit('${fid}','postcredits');" >编 辑</a>
							        </td>
                                </tr>
                                <tr>
							        <td>指向外部链接地址:</td>
							        <td>
								        <@c.textBox id="redirect" value="${forum.forumfields.redirect}" maxlength="253" hintInfo="设置版块为一个链接，当点击本版块是将跳转到指定的地址上"></@c.textBox>
							        </td>
                                </tr>
                                <tr>
							        <td>本版块规则:</td>
							        <td>
								        <@c.textBox id="rules" value="${forum.forumfields.rules}" hintTitle="提示" hintInfo="支持Html" multiLine="true" height="100"></@c.textBox>
							        </td>
                                </tr>
                            </table>
                        </td>
                        <td  class="panelbox" align="right" width="50%">
                             <table width="100%">
						        <tr>
							        <td style="width: 110px">版块图标:</td>
							        <td>
								        <@c.textBox id="icon" value="${forum.forumfields.icon}" hintTitle="提示" hintInfo="显示于首页论坛列表等" maxlength="253"></@c.textBox>
							        </td>
						        </tr>
						        <tr>
							        <td>发回复积分策略:</td>
							        <td>
							            <a href="#" class="textButton" onclick="javascript:editcredit('${fid}','replycredits');" >编 辑</a>
							        </td>
						        </tr>
						        <tr>
							        <td>允许的附件类型:</td>
							        <td>
								        <@c.checkBoxList id="attachextensions" hintTitle="提示" 
								        hintInfo="允许在本版块上传的附件类型,留空为使用用户组设置, 且版块设置优先于用户组设置" repeatColumns="4">
								        <tr>
								        <#assign n=0/>
								        <#list attachtypeList as attachtype>
								        <#if n == 4></tr><tr><#assign n=-1/></#if><td>								       
								        <input type="checkbox" name="attachextensions" <#list attachexts as tmp><#if tmp==attachtype.id>checked="${tmp}"</#if></#list> value="${attachtype.id}" id="attachextensions_${attachtype.id}"/><label for="attachextensions_${attachtype.id}">${attachtype.extension}</label>
								        <td><#assign n=n+1/></#list></tr>
								        </@c.checkBoxList>
							        </td>
						        </tr>
						        <tr>
							        <td>定期自动关闭主题:</td>
							        <td>
							            <table>
							                <tr>
							                    <td>
							                    	<#assign autoclose = 0/>
							                    	<#if (forum.autoclose>0)><#assign autoclose = 0/></#if>
				 		                            <@c.radioButtonList id="autocloseoption" repeatColumns="1" 
					                        			onclick="javascript:document.getElementById('showclose').style.display= (document.getElementById('autocloseoption0').checked ? 'none' : 'block');"><tr></td>
                                            			<@c.radioItem name="autocloseoption" id="autocloseoption0" value="0" checked="${autoclose}">不自动关闭</@c.radioItem></td><td>
                                            			<@c.radioItem name="autocloseoption" id="autocloseoption1" value="1" checked="${autoclose}">按发布时间</@c.radioItem></td></tr>
			 		                        		</@c.radioButtonList>
				 		                        </td>
				 		                        <td valign=bottom>
				 		                            <div id="showclose" style="display:<#if (forum.autoclose>0)>block<#else>none</#if>">
				 		    	                        <@c.textBox id="autocloseday" width="50" size="4" maxlength="3" value="${forum.autoclose}"></@c.textBox>
				 		    	                        <font style="font-size:12px">天自动关闭</font>	
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
						           			<@c.radioItem name="allowspecialonly" value="1" checked="${forum.allowspecialonly}">是</@c.radioItem>
						            		<@c.radioItem name="allowspecialonly" value="0" checked="${forum.allowspecialonly}">否</@c.radioItem>
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
							        <td style="width: 110px">设置:</td>
							        <td>
					<table id="setting" class="buttonlist" border="0" style="width:100%;">
						<tr>
							<td><input id="setting_0" type="checkbox" name="Allowsmilies" <#if forum.allowsmilies==1>checked="checked"</#if> value="1" /><label for="setting_0">允许使用表情符</label></td><td><input id="setting_4" type="checkbox" name="Recyclebin" <#if forum.recyclebin==1>checked="checked"</#if> value="1" /><label for="setting_4">打开回收站</label></td><td><input id="setting_8" type="checkbox" name="Inheritedmod" <#if forum.inheritedmod==1>checked="checked"</#if>  value="1"/><label for="setting_8">继承上级论坛或分类的版主设定</label></td><td><input id="setting_11" type="checkbox" name="allowpoll" <#if reqcfg.opNum(forum.allowpostspecial,1)!=0>checked="checked"</#if>  value="1"/><label for="setting_11">允许发投票</label></td>
						</tr><tr>
							<td><input id="setting_1" type="checkbox" name="Allowrss" <#if forum.allowrss==1>checked="checked"</#if> value="1"/><label for="setting_1">允许RSS</label></td><td><input id="setting_5" type="checkbox" name="Modnewposts" <#if forum.modnewposts==1>checked="checked"</#if>  value="1"/><label for="setting_5">发帖需要审核</label></td><td><input id="setting_9" type="checkbox" name="Allowthumbnail" <#if forum.allowthumbnail==1>checked="checked"</#if>  value="1"/><label for="setting_9">主题列表中显示缩略图</label></td><td><input id="setting_12" type="checkbox" name="allowbianl" <#if reqcfg.opNum(forum.allowpostspecial,16)!=0>checked="checked"</#if> value="1"/><label for="setting_12">允许辩论</label></td>
						</tr><tr>
							<td><input id="setting_2" type="checkbox" name="Allowbbcode" <#if forum.allowbbcode==1>checked="checked"</#if> value="1"/><label for="setting_2">允许LForum代码</label></td><td><input id="setting_6" type="checkbox" name="Jammer" <#if forum.jammer==1>checked="checked"</#if>  value="1"/><label for="setting_6">帖子中添加干扰码</label></td><td><input id="setting_10" type="checkbox" name="Allowtag" <#if forum.allowtag==1>checked="checked"</#if>  value="1"/><label for="setting_10">允许标签</label></td><td><input id="setting_13" type="checkbox" name="allowxuans" <#if reqcfg.opNum(forum.allowpostspecial,4)!=0>checked="checked"</#if> value="1"/><label for="setting_13">允许悬赏</label></td>
						</tr><tr>
							<td><input id="setting_3" type="checkbox" name="Allowimgcode" <#if forum.allowimgcode==1>checked="checked"</#if> value="1"/><label for="setting_3">允许[img]代码</label></td><td><input id="setting_7" type="checkbox" name="Disablewatermark" <#if forum.disablewatermark==1>checked="checked"</#if> value="1" /><label for="setting_7">禁止附件自动水印</label></td><td></td><td></td>
						</tr>
					</table>
							        </td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                </table>
                </@c.tabItem>
                <@c.tabItem tabid="TabControl1" id="tabPage4" default="${tabDef}">
                    <table width="100%" align="center" class="table1" cellspacing="0" cellPadding="4"  bgcolor="#C3C7D1">
	                    <tr>
		                    <td class="category">
			                    <input title="选中/取消选中 本页所有Case" onclick="Check(this.form,this.checked,'userid')" type="checkbox" name="chkall" id="chkall" />全选/取消全选 &nbsp; 
			                    <@c.button id="DelButton" method="delPower" text=" 删 除 " buttonImgUrl="../images/del.gif" ></@c.button>
		                    </td>
	                    </tr>
                   </table>		
				   
                    <br />
                    <@c.pageInfo id="info1" icon="Information" text="授予某些用户在本版一些特殊权限,在下面输入框用中输入要给予特殊权限的用户列表,以&quot;,&quot;分隔"></@c.pageInfo>
                    <table cellspacing="0" cellpadding="4" width="100%" align="center">
                        <tr>
                            <td class="panelbox" colspan="2">
                                <table width="100%">
	                                <tr>
		                                <td style="width: 110px">增加特殊用户列表:</td>
		                                <td>
			                                <@c.textareaResize id="UserList" cols="40" rows="2"></@c.textareaResize>            	
			                                &nbsp;&nbsp;<@c.button id="BindPower" method="bindPower" type="submit" text=" 增加 " ></@c.button>
		                                </td>
	                                </tr>
                                </table>
                            </td>
                        </tr>
                    </table>					
               </@c.tabItem>          
                <@c.tabItem tabid="TabControl1" id="tabPage5" default="${tabDef}">
                <table cellspacing="0" cellpadding="4" width="100%" align="center">
                    <tr>
                        <td  class="panelbox" align="left" width="50%">
                            <table width="100%">
                                <tr>
					                <td style="width: 90px">启用主题分类:</td>
					                <td>
						                <@c.radioButtonList id="applytopictype" hintTitle="提示" hintInfo="设置是否在本版块启用主题分类功能,您需要同时设定相应的分类选项,才能启用本功能">
							                <@c.radioItem value="1" name="applytopictype" checked="${forum.forumfields.applytopictype}">是</@c.radioItem>
							                <@c.radioItem value="0" name="applytopictype" checked="${forum.forumfields.applytopictype}">否</@c.radioItem>
						                </@c.radioButtonList>
					                </td>
                                </tr>
                                <tr>
					                <td>允许按类别浏览:</td>
					                <td>
						                <@c.radioButtonList id="viewbytopictype" hintTitle="提示" 
						                hintInfo="如果选择&amp;quot;是&amp;quot;,用户将可以在本版块中按照不同的类别浏览主题.注意: 本功能必须&amp;quot;启用主题分类&amp;quot;后才可使用并会加重服务器负担">
							                <@c.radioItem value="1" name="viewbytopictype" checked="${forum.forumfields.viewbytopictype}">是</@c.radioItem>
							                <@c.radioItem value="0" name="viewbytopictype" checked="${forum.forumfields.viewbytopictype}">否</@c.radioItem>
						                </@c.radioButtonList>
					                </td>
                                </tr>
                            </table>
                        </td>
                        <td  class="panelbox" align="right" width="50%">
                            <table width="100%">
				                <tr>
					                <td style="width: 90px">发帖必须归类:</td>
					                <td>
						                <@c.radioButtonList id="postbytopictype" hintTitle="提示" hintInfo="如果选择&amp;quot;是&amp;quot;,作者发新主题时,必须选择主题对应的类别才能发表.本功能必须&amp;quot;启用主题分类&amp;quot;后才可使用">
							                <@c.radioItem value="1" name="postbytopictype" checked="${forum.forumfields.postbytopictype}">是</@c.radioItem>
							                <@c.radioItem value="0" name="postbytopictype" checked="${forum.forumfields.postbytopictype}">否</@c.radioItem>
						                </@c.radioButtonList>
					                </td>
				                </tr>
				                <tr>
					                <td>类别前缀:</td>
					                <td>
						                <@c.radioButtonList id="topictypeprefix" hintTitle="提示" hintInfo="设置是否在主题列表中,给已分类的主题前加上类别的显示.注意: 本功能必须&amp;quot;启用主题分类&amp;quot;后才可使用">
							                <@c.radioItem value="1" name="topictypeprefix" checked="${forum.forumfields.topictypeprefix}">是</@c.radioItem>
							                <@c.radioItem value="0" name="topictypeprefix" checked="${forum.forumfields.topictypeprefix}">否</@c.radioItem>
						                </@c.radioButtonList>
					                </td>
				                </tr>
                            </table>
                        </td>
                    </tr>
                </table>
		        <br />
		        <table class="ntcplist" >

<tr class="head">

<td>当前版块:  ${forum.name}</td>

</tr>

<tr>

<td>

<table class="datalist" cellspacing="0" rules="all" border="1" id="TabControl1_tabPage5_TopicTypeDataGrid" style="border-collapse:collapse;">
	<tr class="category">
		<td nowrap="nowrap" style="border-color:#EAE9E1;border-width:1px;border-style:solid;width:15%;">主题分类</td><td nowrap="nowrap" style="border-color:#EAE9E1;border-width:1px;border-style:solid;width:40%;">描述</td><td nowrap="nowrap" style="border-color:#EAE9E1;border-width:1px;border-style:solid;width:15%;">不使用</td><td nowrap="nowrap" style="border-color:#EAE9E1;border-width:1px;border-style:solid;width:15%;">使用(平板显示)</td><td nowrap="nowrap" style="border-color:#EAE9E1;border-width:1px;border-style:solid;width:15%;">使用(下拉显示)</td>
	</tr><tr>
		<td class="datagridPager" align="left" valign="bottom" colspan="6"></td></tr></table><table class="datagridpage"><tr><td height="2"></td></tr><tr><td> <font color=black>共 1 页, 当前第 1 页    &nbsp;&nbsp;</td>
	</tr>
</table></td></tr></TABLE>
	          <table cellspacing="0" cellpadding="4" width="100%" align="center">
	                <tr>
                        <td class="panelbox" colspan="2">
                            <table width="100%">
			                    <tr>
				                    <td width="25%">
				                    主题分类名:
				                    <input name="typename" type="text" maxlength="200" id="typename" class="FormBase" onfocus="this.className='FormFocus';" onblur="this.className='FormBase';" maxlength="200" size="10" />
				                    </td>
				                    <td width="25%">
				                    显示顺序:
				                    <input name="typeorder" type="text" maxlength="4" id="typeorder" class="FormBase" onfocus="this.className='FormFocus';" onblur="this.className='FormBase';" maxlength="4" size="3" />
				                    </td>
				                    <td width="25%">
				                    描述:
				                    <input name="typedescription" type="text" maxlength="500" id="typedescription" class="FormBase" onfocus="this.className='FormFocus';" onblur="this.className='FormBase';" maxlength="500" size="10" />
				                    </td>
				                    <td width="25%">
				                    <button type="button" class="ManagerButton" id="AddNewRec" onclick="AddTopicType();"><img src="../images/submit.gif"/> 新增主题分类 </button>
				                    </td>
			                    </tr>
                            </table>
                        </td>
                    </tr>
                </table>
		        <div id="resultmessage" style="display:none"></div>
                </@c.tabItem>               
                <@c.tabItem tabid="TabControl1" id="tabPage6" default="${tabDef}">
		            <span id="forumsstatic">主题总数:${forum.topics_1}<br />帖子总数:${forum.posts}<br />今日回帖数总数:${forum.todayposts}<br />最后提交日期:${forum.lastpost}</span>
					<br />
					<br /><@c.button id="RunForumStatic" method="runForumStatic" text="统计最新信息" />
					${runforumsstatic}
				</@c.tabItem>               
				</#if>
				</div>
			     <div id="topictypes" style="display:none;width:100%;">
			         <table>
			        	
						<tr>
							<td><b>显示顺序:</b></td>
							<td>
								<@c.textBox id="displayorder"  required="true"></@c.textBox></td>
						</tr>
						<tr>
							<td><b>主题分类:</b></td>
							<td>
								<@c.textBox id="topictypes" width="370" height="50" multiLine="true"></@c.textBox></td>
						</tr>
						
						</table>
				   </div>
			<div class="Navbutton">
			    <@c.button id="SubmitInfo" text=" 提 交 "></@c.button>&nbsp;&nbsp;
			    <button onclick="window.location='forum_forumstree.action';" id="Button3" class="ManagerButton" type="button"><img src="../images/arrow_undo.gif"/> 返 回 </button>
			</div>
			</td>
		</tr>
		</table>
		<@c.hint hintImageUrl="../images"/>							
        <script type="text/javascript">
            function editcredit(fid,fieldname)
            {
                window.location="forum_scorestrategy.action?fid="+fid+"&fieldname="+fieldname;
            }
        </script>					
		</form>
	${footer}
	</body>
</html>
