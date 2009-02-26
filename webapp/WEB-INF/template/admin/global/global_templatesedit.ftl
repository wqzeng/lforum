<#import "../../controls/control.ftl" as c>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN" >
<html>
<head>
    <title>templatesedit</title>
    <link href="../styles/dntmanager.css" type="text/css" rel="stylesheet" />
    <link href="../styles/colorpicker.css" type="text/css" rel="stylesheet" />
    <script language="javascript" src="../js/colorpicker.js"></script>
    <style type="text/css">
        .img
        {
	        border:0px;
	        align:bottom;
	        position:relative;
	        top:0.5%;
        }	
    </style>
    <script type="text/javascript">
        var n = 0;
        function displayHTML(obj) {
	        window.open(obj, 'popup', 'toolbar = no, status = no, scrollbars=yes');
        }	
        	
        function HighlightAll(obj) {
	        obj.focus();
	        obj.select();
	        if (document.getElementById("templatenew_posttextarea")) {
		        obj.createTextRange().execCommand("Copy");
		        window.status = '将模板内容复制到剪贴板';
		        setTimeout("window.status=''", 1800);
	        }
        }

        function findInPage(obj, str) {
        try
        {
	        var txt, i, found;
	        if (str == "") {
		        return false;
	        }
	        if (document.layers) {
		        if (!obj.find(str)) {
			        while(obj.find(str, false, true)) {
				        n++;
			        }
		        } else {
			        n++;
		        }
		        if (n == 0) {
			        alert("未找到指定字串. ");
		        }
	        }
	        if (document.getElementById("templatenew_posttextarea")) {
		        txt = obj.createTextRange();
		        for (i = 0; i <= n && (found = txt.findText(str)) != false; i++) {
			        txt.moveStart('character', 1);
			        txt.moveEnd('textedit');
		        }
		        if (found) {
			        txt.moveStart('character', -1);
			        txt.findText(str);
			        txt.select();
			        txt.scrollIntoView();
			        n++;
		        } else {
			        if (n > 0) {
				        n = 0;
				        findInPage(str);
			        } else {
				        alert("未找到指定字串. ");
			        }
		        }
	        }
	        return false;
        }
        catch(error)
        {
	        alert("已经到达文件尾！");
        }
        }
    </script>
    <script type="text/javascript" src="../js/common.js"></script>
</head>
<body>
    <form id="Form1" method="post" >
    ${htmlBuilder}
        <table cellspacing="0" cellpadding="4" width="100%" align="center">
	        <tr>
	            <td  class="panelbox" align="center">
	                <table width="80%">
	                    <tr>
	                        <td>编辑文件 - <b>${filename}</b></td>
	                    </tr>
	                    <tr>
	                        <td>获取颜色:<@c.colorPicker id="ColorPicker1"></@c.colorPicker></td>
	                    </tr>
	                    <tr>
	                        <td><@c.textareaResize id="templatenew" value="${content}" rows="20" cols="100"></@c.textareaResize></td>
	                    </tr>
	                    <tr>
	                        <td>
                                <input name="search" type="text" accesskey="t" size="20" onchange="n=0;" />&nbsp;
                                <button type="button" class="ManagerButton" accesskey="f" onclick="findInPage(this.form.templatenew_posttextarea, this.form.search.value)">
                                    <img src="../images/search.gif" />搜索
                                </button>&nbsp;&nbsp;
                                <button type="button" class="ManagerButton" accesskey="c" onclick="HighlightAll(this.form.templatenew_posttextarea,'')">
                                    <img src="../images/topictype.gif" />复制
                                </button>&nbsp;&nbsp;                               
	                        </td>
	                    </tr>
	                </table>
	            </td>
	        </tr>
	    </table>
	    <p style="text-align:right;width:80%;">
	    	<input type="hidden" value="${filename}" name="filename">
	    	<input type="hidden" value="${path}" name="path">
	    	<input type="hidden" value="${templateid}" name="templateid">
	    	<input type="hidden" value="${templatename}" name="templatename">
            <@c.button id="Save" text="提 交" ></@c.button>&nbsp;&nbsp;
	        <button class="ManagerButton" type="button" onclick="javascript:window.location.href='global_templatetree.action?templateid=${templateid}&path=${path}&templatename=${templatename}';">
                <img src="../images/arrow_undo.gif" /> 返 回 
            </button>
	    </p>
    </form>
    ${footer}
</body>
</html>
