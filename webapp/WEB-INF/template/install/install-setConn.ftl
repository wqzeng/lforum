<#import "install-comm.ftl" as comm />
<@comm.header title="安装向导--数据库连接设置" />
 <script type="text/javascript">
 	$.validator.setDefaults({
		submitHandler: function() { 
			document.form1.submit();
			if($("#createTable").attr("checked")){
			$("#nextbtn").attr({disabled:"false"});
			$("#connConfig").html("<tr><td align='center'><img src='${path}images/loading.gif' />正在创建数据库表,请稍后...</td></tr>");
			}			
		}
	});
	$(document).ready(function(){
		$("#form1").validate({
			rules:{
				DataSource:{required:true},
				DataPort:{required:true,digits:true},
				DataBase:{required:true},
				UserID:{required:true},
				Password:{required:true},
				AdminName:{required:true,rangelength:[3,12]},
				AdminPassword:{required:true,rangelength:[6,18]},
				RAdminPassword:{required:true,equalTo:"#AdminPassword"}
			},
			messages:{
				DataSource:{required:"数据源不能为空"},
				DataPort:{required:"数据端口不能为空"},
				DataBase:{required:"数据库名称不能为空"},
				UserID:{required:"数据库用户名不能为空"},
				Password:{required:"数据库口令不能为空"},
				AdminName:{required:"管理员名称不能为空"},
				AdminPassword:{required:"管理员密码不能为空"},
				RAdminPassword:{required:"确认密码不能为空"}
			}
		});
	});
	function change(name){		
		if(name=="mysql"){
			$("#DataPort").val("3306");
			$("#UserID").val("root");
		}else{
			$("#DataPort").val("1433");
			$("#UserID").val("sa");
		}
		
	}
</script>
<body>
		<div class="top">
		</div>
		<div class="con"> 
			<form name="form1" method="post" action="${path}install/install!createData.action" id="form1">
				<@s.token />
				<input type="hidden" name="aggre" value="true"  />
				<table cellspacing="0" cellpadding="0" border="0" id="WzdInstall" style="width:100%;border-collapse:collapse;">
					<tr style="height:100%;">
						<td>
							<div id="WzdInstall_DivInstall2" class="left140">
								<dl>
									<dt class="check">
										<img alt="" src="${path}Install/Images/ico01.gif" />
										下面进行数据库连接设置</dt>
										<dd><label
												style="color: Blue;">请确保设置好的数据库中没有旧的数据表和存储过程。</label>
												<table id="connConfig" cellspacing="1" cellpadding="1" class="table_date">
													<tr>
														<td style="width: 235px">
															请选择数据库版本：</td>
															<td>
																<select onchange="change(this.value)" name="DataType" id="DataType">
																	<option selected="selected" value="sqlserver05">Sql Server 2005</option>
																	<option value="sqlserver00">Sql Server 2000</option>
																	<option value="mysql">MySQL</option>
																</select>
															</td>
													</tr>
													<tr>
														<td style="width: 235px">
															数据源：</td>
															<td>
																<input name="DataSource"  type="text" value="localhost" id="DataSource" class="inputtext" style="width:150px;" />
															</td>
													</tr>
													<tr>
														<td style="width: 235px">
															数据端口：</td>
															<td>
																<input name="DataPort"  type="text" value="1433" id="DataPort" class="inputtext" style="width:150px;" />
														
															</td>
													</tr>
													<tr>
														<td style="width: 235px">
															数据库名称：</td>
															<td>
																<input name="DataBase" type="text" value="LForumDB" id="DataBase"  class="inputtext" style="width:150px;" />
															</td>
													</tr>
													<tr>
														<td style="width: 235px">
															数据库用户名称：</td>
															<td>
																<input name="UserID" type="text" value="sa" id="UserID" class="inputtext"  style="width:150px;" />
															</td>
													</tr>
													<tr>
														<td style="width: 235px">
															数据库用户口令：</td>
															<td>
																<input name="Password" type="password" id="Password" class="inputtext" style="width:150px;"  />
															</td>
													</tr>
													  <tr>
                                            <td style="height: 15px" colspan="2">
                                            </td>
                                        </tr>
                                        <tr>
                                            <td>
                                                管理员名称：</td>
                                            <td>
													<input name="AdminName" type="text" value="Admin" id="AdminName" class="inputtext" style="width:150px;" />
                                            </td>
                                        </tr>
                                        <tr>
                                            <td>
                                                管理员密码：</td>
                                            <td>
                                                <input name="AdminPassword"  type="password" id="AdminPassword" class="inputtext" style="width:150px;" />
                                            </td>
                                        </tr>
                                        <tr>
                                            <td>
                                                确认密码：</td>
                                            <td>
                                                <input name="RAdminPassword"  type="password" id="RAdminPassword" class="inputtext" style="width:150px;" />
                                        </tr>                                        
													<tr>
														<td colspan="2">
															<div class="center">
																<span id="CheckConnectString" style="color:Red;"><#if reqcfg?exists>${reqcfg.msgbox_text}</#if></span>
															</div>
														</td>
													</tr>
												</table>
										</dd>
								</dl>
							</div>
							<div class="left140">
								<div class="left140_cen">
								<input type="checkbox" checked="true"  value="true" id="createTable" name="createTable"/>创建数据库表
								&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
									<@comm.first/>
									<input type="submit" id="nextbtn"  value="下一步" class="button_link" />
								</div>
							</div>
						</td>
					</tr>
					<tr>
						<td align="right">
							<center>
							</center>
						</td>
					</tr>
				</table>
				<div>
				</div>
			</form>
		</div>
		<@comm.bottom />
	</body>
</html>
