<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title>shortcut</title>
<link href="../styles/dntmanager.css" type="text/css" rel="stylesheet">
<script type="text/javascript" src="../js/common.js"></script>
<script type="text/javascript">       
            var currentid=0;
            var bar=0;
            var filenameliststr='${filenamelist}';
            var filenamelist=new Array();
            filenamelist= filenameliststr.split('|');
            function runstatic()
            {  
                 if(filenamelist[currentid]!="")
                 {
		         document.getElementById('Layer5').innerHTML =  '<br /><table><tr><td valign=top><img border=\"0\" src=\"../images/ajax_loading.gif\"  /></td><td valign=middle style=\"font-size: 14px;\" >正在更新'+filenamelist[currentid]+'.htm模板, 请稍等...<BR /></td></tr></table><BR />';
                 document.getElementById('Layer5').style.witdh='350';
                 document.getElementById('success').style.witdh='400';
                 document.getElementById('success').style.display ="block"; 
               
                 getReturn('updatesingletemplate.aspx?path='+document.getElementById('Templatepath').value+'&filename='+filenamelist[currentid]);
                 currentid++;
                 }
                 else//filenamelist.count<=currentid
                 {
                    document.getElementById('Layer5').innerHTML="<br />模板更新成功, 请稍等...";
                    document.getElementById('success').style.display = "block";
		            count(); 
		            document.getElementById('Form1').submit();
                 }
           }
           
           function count()
           { 
		        bar=bar+2;
		        if (bar<99) {setTimeout("count()",100);} 
		        else { document.getElementById('success').style.display ="none"; } 
           }
           
           function run()
           {
              bar=0;
              document.getElementById('Layer5').innerHTML="<BR /><table><tr><td valign=top><img border=\"0\" src=\"../images/ajax_loading.gif\"  /></td><td valign=middle style=\"font-size: 14px;\" >正在提交数据, 请稍等...<BR /></td></tr></table><BR />";
              document.getElementById('success').style.display = "block";
              setInterval('runstatic()',5000); //每次提交时间为6秒
           }
           
           function validateform(theform)
           {
              document.getElementById('Form1').submit();
 	          return true;
           }
           
           function validate(theform)
           {
              if(document.getElementById('createtype').checked)
              {
                  run();
                  return false;
              }
              else
              {
 	              return true;
	          }
           }
    
   </script>

</head>
<body>
<form id="Form1" method="post">
${htmlBuilder}
<table cellspacing="0" cellpadding="4" width="100%" align="center">
	<tr>
		<td class="panelbox">
		<table width="100%">
			<tr>
				<td style="width: 120px">编辑用户:</td>
				<td style="width: 120px"><input name="Username" type="text"
					id="Username" class="FormBase"
					onfocus="this.className='FormFocus';"
					onblur="this.className='FormBase';"
					onkeydown="if(event.keyCode==13) return(document.forms(0).EditUser.focus());"
					size="30" style="width: 200px;" /></td>
				<td><span>
				<button type="button" class="ManagerButton" id="EditUser"
					onclick="if (typeof(Page_ClientValidate) == 'function') { if (Page_ClientValidate() == false) { return false; }}this.disabled=true;document.getElementById('success').style.display = 'block';HideOverSels('success');__doPostBack('EditUser','');"><img
					src="../images/submit.gif" />提 交</button>
				</span></td>
			</tr>
			<tr>
				<td>编辑论坛:</td>
				<td><select name="_ctl0">
					<option selected="selected" value="0">请选择</option>
					<option value="1">默认分类</option>
					<option value="2">&#160;&#160;&#160;&#160;默认版块</option>

				</select></td>
				<td><span>
				<button type="button" class="ManagerButton" id="EditForum"
					onclick="if (typeof(Page_ClientValidate) == 'function') { if (Page_ClientValidate() == false) { return false; }}this.disabled=true;document.getElementById('success').style.display = 'block';HideOverSels('success');__doPostBack('EditForum','');"><img
					src="../images/submit.gif" />提 交</button>
				</span></td>
			</tr>
			<tr>
				<td>编辑用户组:</td>
				<td><select name="Usergroupid" id="Usergroupid">
					<option selected="selected" value="0">请选择</option>
					<option value="1">管理员</option>
					<option value="2">超级版主</option>
					<option value="3">版主</option>
					<option value="4">禁止发言</option>
					<option value="5">禁止访问</option>
					<option value="6">禁止 IP</option>
					<option value="7">游客</option>
					<option value="8">等待验证会员</option>
					<option value="9">乞丐</option>
					<option value="10">新手上路</option>
					<option value="11">注册会员</option>
					<option value="12">中级会员</option>
					<option value="13">高级会员</option>
					<option value="14">金牌会员</option>
					<option value="15">论坛元老</option>

				</select></td>
				<td><span>
				<button type="button" class="ManagerButton" id="EditUserGroup"
					onclick="if (typeof(Page_ClientValidate) == 'function') { if (Page_ClientValidate() == false) { return false; }}this.disabled=true;document.getElementById('success').style.display = 'block';HideOverSels('success');__doPostBack('EditUserGroup','');"><img
					src="../images/submit.gif" />提 交</button>
				</span></td>
			</tr>
			<tr>
				<td>更新缓存:</td>
				<td></td>
				<td><span>
				<button type="button" class="ManagerButton" id="UpdateCache"
					onclick="if (typeof(Page_ClientValidate) == 'function') { if (Page_ClientValidate() == false) { return false; }}this.disabled=true;document.getElementById('success').style.display = 'block';HideOverSels('success');__doPostBack('UpdateCache','');"><img
					src="../images/submit.gif" />提 交</button>
				</span></td>
			</tr>
			<tr>
				<td>生成模板:</td>
				<td><select name="Templatepath" id="Templatepath">
					<option selected="selected" value="default">Default</option>
					<option value="beijing2008">beijing2008</option>
					<option value="pink">pink</option>
					<option value="special">special</option>

				</select> <input type="checkbox" id="createtype" name="createtype">降低CPU占用</td>
				<td><span>
				<button type="button" class="ManagerButton" id="CreateTemplate"
					onclick="if (typeof(Page_ClientValidate) == 'function') { if (Page_ClientValidate() == false) { return false; }}this.disabled=true;document.getElementById('success').style.display = 'block';HideOverSels('success');if(validate(this.form)){__doPostBack('CreateTemplate','');}"><img
					src="../images/submit.gif" />提 交</button>
				</span></td>
			</tr>
			<tr>
				<td>更新论坛统计:</td>
				<td></td>
				<td><span>
				<button type="button" class="ManagerButton"
					id="UpdateForumStatistics"
					onclick="if (typeof(Page_ClientValidate) == 'function') { if (Page_ClientValidate() == false) { return false; }}this.disabled=true;document.getElementById('success').style.display = 'block';HideOverSels('success');__doPostBack('UpdateForumStatistics','');"><img
					src="../images/submit.gif" />提 交</button>
				</span></td>
			</tr>
		</table>
		</td>
	</tr>	
	<tr>
		<td colspan="3" height="10"><iframe width="100%" height="100px"
			id="checkveriframe" framespacing="0px" style="border-width: 0px;"
			frameborder="0px" noResize></iframe></td>
	</tr>
</table>
</form>
${footer}
</body>
</html>
