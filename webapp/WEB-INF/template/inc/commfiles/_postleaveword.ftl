﻿<#-- 
	描述：快速留言模板
	作者：黄磊 
	版本：v1.0 
-->
﻿<#if quickeditorad!="">
<div class="leaderboard">${quickeditorad}</div>
</#if>
<script type="text/javascript" src="javascript/bbcode.js"></script>
<script type="text/javascript" src="javascript/post.js"></script>
<form method="post" name="postform" id="postform" action="showgoods.action?goodsid=${goodsid}"
	enctype="multipart/form-data" onsubmit="return validateform(this);" >
	<input type="hidden" id="postleaveword" name="postleaveword"  value="add" />
	<input type="hidden" id="leavewordid" name="leavewordid" value="0" />
	<div id="quickleaveword" class="box" style="ovflow:hidden; border:none;">
	<h4>快速留言</h4>
	<div class="postform">
		<p><label>内容</label>
		<textarea name="message" cols="6" rows="4" class="autosave" id="message" tabindex="2" onkeydown="quickpost(event, this.form, ${goodsid}, ${leaveword_page_currentpage});"></textarea>
		</p>
		<#if isseccode><p class="formcode">验证码:<#include "_vcode.ftl"/></p></#if>
		<p class="btns">
			<button type="submit" id="postsubmit" name="topicsubmit" value="发表留言" tabindex="3">发表留言</button>[完成后可按 Ctrl+Enter 发布]&nbsp;&nbsp;&nbsp;
			<input name="restoredata" id="restoredata" value="恢复数据" tabindex="5" title="恢复上次自动保存的数据" onclick="loadData();" type="button" />&nbsp;<input name="topicsreset" value="清空内容" tabindex="6" type="reset" />
			<input type="hidden" id="postbytopictype" name="postbytopictype" value="${forum.postbytopictype}" tabindex="3" />
		</p>
	</div>
	<div class="smilies">
		<script type="text/javascript">
		var isendpage = (${pageid}==${pagecount});

		var textobj = $('message');
		var lang = new Array();
		if(is_ie >= 5 || is_moz >= 2) {
			window.onbeforeunload = function () {
				saveData(textobj.value);
			};
			lang['post_autosave_none'] = "没有可以恢复的数据";
			lang['post_autosave_confirm'] = "本操作将覆盖当前帖子内容，确定要恢复数据吗？";
		}
		else {
			$('restoredata').style.display = 'none';
		}

		var bbinsert = parseInt('1');
		var smileyinsert = parseInt('1');
		var editor_id = 'posteditor';
		var smiliesCount = 9;
		var colCount = 3;
		var showsmiliestitle = 0;
		var smiliesIsCreate = 0;

		var scrMaxLeft; //表情滚动条宽度
		var smilies_HASH = {};
		</script>
		<#assign defaulttypname=""/>
		<div class="navcontrol">
			<div class="smiliepanel">
				<div id="scrollbar" class="scrollbar">
					<table cellspacing="0" cellpadding="0" border="0">
						<tr>
						<#list smilietypes as stype>
							<#if stype.id==1>
							<#assign defaulttypname=stype.code/>
							</#if>
							<td id="t_s_${stype.id}"><div id="s_${stype.id}" onclick="showsmiles(${stype.id}, '${stype.code}');" <#if stype.id!=1>style="display:none;"<#else>class="lian"</#if>>${stype.code}</div></td>
						</#list>
						</tr>
					</table>
				</div>
				<div id="scrlcontrol">
					<img src="editor/images/smilie_prev_default.gif" alt="向前" onmouseover="if($('scrollbar').scrollLeft>0)this.src=this.src.replace(/_default|_selected/, '_hover');" onmouseout="this.src=this.src.replace(/_hover|_selected/, '_default');" onmousedown="if($('scrollbar').scrollLeft>0){this.src=this.src.replace(/_hover|_default/, '_selected');this.boder=1;}" onmouseup="if($('scrollbar').scrollLeft>0)this.src=this.src.replace(/_selected/, '_hover');else{this.src=this.src.replace(/_selected|_hover/, '_default');}this.border=0;" onclick="scrollSmilieTypeBar($('scrollbar'), 1-$('t_s_1').clientWidth);"/>&nbsp;
					<img src="editor/images/smilie_next_default.gif" alt="向后"  onmouseover="if($('scrollbar').scrollLeft<scrMaxLeft)this.src=this.src.replace(/_default|_selected/, '_hover');" onmouseout="this.src=this.src.replace(/_hover|_selected/, '_default');" onmousedown="if($('scrollbar').scrollLeft<scrMaxLeft){this.src=this.src.replace(/_hover|_default/, '_selected');this.boder=1;}" onmouseup="if($('scrollbar').scrollLeft<scrMaxLeft)this.src=this.src.replace(/_selected/, '_hover');else{this.src=this.src.replace(/_selected|_hover/, '_default');}this.border=0;" onclick="scrollSmilieTypeBar($('scrollbar'), $('t_s_1').clientWidth);" />
				</div>
			</div>
		</div>
		<div  id="showsmilie"><img src="images/common/loading_wide.gif" width="90%" alt="表情加载"/><p>正在加载表情...</p></div>
		<div id="showsmilie_pagenum"></div>
	</div>
	<script type="text/javascript">	
		var firstpagesmilies_json ={ ${firstpagesmilies} };
		showFirstPageSmilies(firstpagesmilies_json, '${defaulttypname}', 12);
		function getSmilies(func){
			var c="tools/ajax.action?t=smilies";
			_sendRequest(c,function(d){var e={};try{e=eval("("+d+")")}catch(f){e={}}var h=e?e:null;func(h);e=null;func=null},false,true);
		}
		getSmilies(function(obj){ 
		smilies_HASH = obj; 
		showsmiles(1, '${defaulttypname}');
		});
		window.onload = function() {
			
			$('scrollbar').scrollLeft = 10000;
			scrMaxLeft = $('scrollbar').scrollLeft;
			$('scrollbar').scrollLeft = 1;	
			if ($('scrollbar').scrollLeft > 0)
			{
				$('scrlcontrol').style.display = '';
				$('scrollbar').scrollLeft = 0;	
			}
		}
	</script>
</div>
</form>