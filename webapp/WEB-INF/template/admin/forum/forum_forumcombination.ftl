<#import "../../controls/control.ftl" as c>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
	<head>
		<title>forumcombination</title>		
		<script type="text/javascript" src="../js/common.js"></script>
		<link href="../styles/dntmanager.css" type="text/css" rel="stylesheet" />
    </head>

	<body>
		<form id="Form1" method="post">
		${htmlBuilder}
        <@c.pageInfo id="info1" icon="Information" text="合并论坛后, 源论坛的帖子全部转入目标论坛, 同时删除源论坛"></@c.pageInfo>
        <@c.pageInfo id="PageInfo1" icon="Warning"
        text="目前的功能要求进行合并的论坛不能有子论坛"></@c.pageInfo>
        <div class="ManagerForm">
            <fieldset>
		    <legend style="background:url(../images/icons/icon44.jpg) no-repeat 6px 50%;">合并版块</legend>
		    <table cellspacing="0" cellpadding="4" width="100%" align="center">
                <tr>
                    <td  class="panelbox" align="left" width="50%">
                        <table width="100%">
                            <tr>
					            <td style="width: 90px">源论坛:</td>
					            <td>
					            <select name="sourceforumid">
					            <option selected="selected" value="0">请选择     </option>
					            ${sourceforum}
								</select>
					            </td>
				            </tr>
				        </table>
				    </td>
				    <td  class="panelbox" align="left" width="50%">
                        <table width="100%">
                            <tr>
					            <td style="width: 90px">目标论坛:</td>
					            <td>
					            <select name="targetforumid">
					            <option selected="selected" value="0">请选择     </option>
					            ${targetforum}
				            	</select>
								</td>
				            </tr>
				        </table>
				    </td>
			    </tr>
			</table>			
            </fieldset>
            <div class="Navbutton">
            	<@c.button id="SaveCombinationInfo" text="提交"  />
			</div>
        </div>
		</form>		
		${footer}
	</body>
</html>
