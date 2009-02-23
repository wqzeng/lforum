<#import "install-comm.ftl" as comm />
<@comm.header title="安装向导--创建数据库信息" />
<body>
    <div class="top">
    </div>
    <div class="con">
        <form name="form1" method="post" action="${path}install/install!setConfig.action" id="form1">
		<@s.token />
      <table cellspacing="0" cellpadding="0" border="0" id="WzdInstall" style="width:100%;border-collapse:collapse;">
	<tr style="height:100%;">
		<td>
                        <div class="left140">
                            <dl>
                                <dt class="check">
                                    <img alt="" src="${path}Install/Images/ico01.gif" />创建数据库成功 <strong><#if reqcfg?exists>，大约花费了 ${reqcfg.pageTime} ms</#if> </strong>.</dt><dd>
                                        <br />
                                        <br />
                                        <table cellspacing="1" cellpadding="1" class="table_date">
                                            <tr>
                                                <td>
                                                    <div class="center">
                                                  	 <strong> 创建数据库成功</strong>
													<img src="${path}Install/Images/ok.gif" width="20" height="20" />
                                                    </div>
                                                </td>
                                            </tr>
                                            <tr>
                                        </table>
                                        <table cellspacing="1" cellpadding="1" class="table_date">
													<tr>
														<td style="width: 235px">
															数据库版本：</td>
															<td>
																${dataType?default("")}
															</td>
													</tr>
													<tr>
														<td style="width: 235px">
															数据源：</td>
															<td>
															${dataSource?default("")}
															</td>
													</tr>
													<tr>
														<td style="width: 235px">
															数据端口：</td>
															<td>
															${dataPort?default("")}
															</td>
													</tr>
													<tr>
														<td style="width: 235px">
															数据库名称：</td>
															<td>
															${dataBase?default("")}
															</td>
													</tr>	
                                        <tr>
                                            <td>
                                                管理员名称：</td>
                                            <td>
                                            ${adminName?default("")}
                                            </td>
                                        </tr>
                                        <tr>
                                            <td>
                                                管理员密码：</td>
                                            <td>
                                            ${adminPassword?default("")}
                                            </td>
                                        </tr>          
												</table>
                                        <br />
                                    </dd>
                            </dl>
                        </div>
                                                                                                <div class="left140">
                            <div class="left140_cen">
                            <@comm.first/>
							<input type="submit" value="下一步" id="btnnext" name="btnnext"  class="button_link" />
                            </div>
                        </div>
                    </td>
	</tr><tr>
		<td align="right">
                    <center>
                    </center>
                </td>
	</tr>
</table>
<div>
</div></form>
    </div>
<@comm.bottom />
</body>
</html>
