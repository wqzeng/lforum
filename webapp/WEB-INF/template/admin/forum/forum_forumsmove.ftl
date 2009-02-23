<#import "../../controls/control.ftl" as c>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
  <head>
		<title>forumsmove</title>		
		<script type="text/javascript" src="../js/common.js"></script>
		<link href="../styles/dntmanager.css" type="text/css" rel="stylesheet" />        
        <link href="../styles/modelpopup.css" type="text/css" rel="stylesheet" />
        <script type="text/javascript" src="../js/modalpopup.js"></script>
        <@c.validator "Form1" />
    </head>
	<body>
	<div class="ManagerForm">
		<form name="Form1" id="Form1" method="post">
		${htmlBuilder}
		    <fieldset>
		    <legend style="background:url(../images/icons/legendimg.jpg) no-repeat 6px 50%;">移动版块</legend>
		    <table cellspacing="0" cellpadding="4" width="100%" align="center">
                <tr>
                    <td  class="panelbox" align="left" width="50%">
                        <table width="100%">
                            <tr>
					            <td style="width:90px;">源版块:</td>
					            <td>
					            <select name="sourceforumid">
					            ${sourceforum}
								</select>
					            </td>
                            </tr>
			                <tr>
					            <td style="width:90px;">移动方式:</td>
					            <td>
					            <table id="movetype" class="buttonlist" border="0" style="border-width:0px;border-style:Dotted;">
								<tr>
								<td><input id="movetype_0" type="radio" name="movetype" value="0" /><label for="movetype_0">调整顺序到目标版块前</label></td>
								</tr><tr>
								<td><input id="movetype_1" type="radio" name="movetype" value="1" checked="checked" /><label for="movetype_1">作为目标版块的子版块</label></td>
								</tr>
								</table>   
					            </td>
				            </tr>
                        </table>
                    </td>
                    <td  class="panelbox" align="right" width="50%">
                        <table width="100%">
				            <tr>
					            <td style="width:90px;">目标版块:</td>
					            <td>
					            <select size="4" name="targetforumid" style="height:290px;">
					            ${targetforum}
				            	</select>
					            </td>
				            </tr>
                        </table>
                    </td>
                </tr>
            </table>
			<div class="Navbutton">
	        	<@c.button id="SaveMoveInfo" text="提交"  />&nbsp;&nbsp;
	        	<button type="button" class="ManagerButton" onclick="javascript:window.location.href='forum_forumstree.action';"><img src="../images/arrow_undo.gif" /> 返 回 </button>
			</div>
			</fieldset>
		</form>
	</div>
	${footer}
	</body>
</html>
