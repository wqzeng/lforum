<#-- 
	描述：用户列表页模板
	作者：黄磊 
	版本：v1.0 
-->
<#import "inc/page_comm.ftl" as comm />
<@comm.header/>
<div id="foruminfo">
	<div id="nav"><a href="${config.forumurl}" class="home">${config.forumtitle}</a> &raquo; 用户列表</div>
</div>
<#if reqcfg.page_err==0>
<div class="pages_btns">
	<div class="pages">
		<em>共${totalusers}名用户</em><strong>${pagecount}页</strong>${pagenumbers}
		<kbd>跳转<input name="gopage" type="text" id="gopage" onKeyDown="if(event.keyCode==13) {
		window.location='showuser.action?page='+this.value;}"  size="4" maxlength="9"  class="colorblue2"/>页
		</kbd>
	</div>
	<span class="postbtn">
	<input type="text" size="22" id="username" name="username" onKeyDown="javascript:if(event.keyCode==13) window.location.href='userinfo.action?username='+this.value;" />
	<input type="button" value="查看用户" onclick="if(document.getElementById('username').value==''){return false;}else{window.location.href='userinfo.action?username=' + document.getElementById('username').value;}" />
	</span>
</div>
<div class="mainbox">
	<h3>会员列表</h3>
	<table summary="用户列表" cellspacing="0" cellpadding="0">
		<thead class="category">
		<tr>
			<th>&nbsp;</th>
			<th>用户名</th>
			<th>组别</th>
			<th>积分</th>
			<th>发帖数</th>
			<th>来自</th>
			<th>注册时间</th>
			<th>最后访问</th>
		</tr>
		</thead>
	<#list userlist as user>
		<tbody>
		<tr>
			<td>${user[12]}</td>
			<th>
						<a href="userinfo.action?userid=${user[0]}" class="bold">${user[2]}</a>
						<#if user[3]!="">
							<#if user[3]!=user[2]>&nbsp;&nbsp;(${user[3]})
							</#if>	
						</#if>
			</th>
			<td>${user[11]}</td>
			<td>${user[5]}</td>
			<td>${user[6]}</td>
			<td>${user[10]}&nbsp;</td>
			<td>${user[4]}</td>
			<td>${user[7]}</td>
		</tr>
		</tbody>
	</#list>
	</table>
</div>
<div class="pages_btns">
	<div class="pages">
		<em>共${totalusers}名用户</em><strong>${pagecount}页</strong>${pagenumbers}
		<kbd>跳转<input name="gopage" type="text" id="gopage" onKeyDown="if(event.keyCode==13) {
		window.location='showuser.action?page='+this.value;}"  size="4" maxlength="9"  class="colorblue2"/>页
		</kbd>
	</div>
	<span class="postbtn">
		<form method="get" action="">
			按:
			<input id="page" type="hidden" value="{pageid}" name="page" />
			<select name="orderby" id="orderby">
			  <option value=""></option>
			  <option value="uid">用户ID</option>
			  <option value="username">用户名</option>
			  <option value="credits">积分</option>
			  <option value="posts">发帖数</option>
			  <option value="admin">管理权限</option>
			  <option value="joindate">注册日期</option>
			  <option value="lastactivity">最后访问日期</option>
			</select>
			<select name="ordertype" id="ordertype">
			  <option value="asc">升序</option>
			  <option value="desc">降序</option>
			</select>
			<script type="text/javascript">
				document.getElementById('orderby').value="{request[orderby]}";
				document.getElementById('ordertype').value="{request[ordertype]}";
			</script>
			&nbsp;
			<input type="submit" name="Submit" value="排序" class="lightbutton" onclick="document.getElementById('username').value='';this.form.submit();" />
		</form>
	</span>
</div>
</div>
<#else>
<@comm.errmsgbox/>
</#if>
<@comm.copyright/>
<@comm.footer/>