<#-- 
	描述：搜索页模板
	作者：黄磊 
	版本：v1.0 
-->
<#import "inc/page_comm.ftl" as comm />
<@comm.header/>
<div id="foruminfo">
	<div id="nav">
		<a href="${config.forumurl}">${config.forumtitle}</a> &raquo; <strong>搜索</strong>
	</div>
	<div id="headsearch">
		<div id="search">
			<#if (usergroupinfo.allowsearch>0)>
				<#if searchid!=-1 || searchpost>
					<@comm.quicksearch/>
				</#if>
			</#if>
		</div>
	</div>
</div>
<#if reqcfg.page_err==0>
<#if searchpost>
<@comm.msgbox/>
<#else> 

<#if searchid==-1>
<div id="options_item">
	<div id="postoptions">
		<table cellpadding="0" cellspacing="0" border="0">		
		<tbody>
		<tr>
			<th><label for="searchtime">时间</label></th>
			<td>
				<select name="searchtime" id="searchtime">
				  <option value="0" selected="selected">全部时间</option>
				  <option value="-1">1天前</option>
				  <option value="-2">2天前</option>
				  <option value="-3">3天前</option>
				  <option value="-7">1周前</option>
				  <option value="-30">1个月前</option>
				  <option value="-90">3个月前</option>
				  <option value="-180">半年前</option>
				  <option value="-365">1年前</option>
				</select>
				  <input type="radio" name="searchtimetype" value="1" />
				之前
				<input name="searchtimetype" type="radio" value="0" checked />
				之后
			</td>
		</tr>
		</tbody>
		<tbody>
		<tr>
			<th><label for="resultorder">结果排序</label></th>
			<td>
				<select name="resultorder" id="resultorder">
				  <option value="0" selected="selected">最后回复时间</option>
				  <option value="1">发表时间</option>
				  <option value="2">回复数量</option>
				  <option value="3">查看次数</option>
				</select>
				<input type="radio" name="resultordertype" value="1" />
				升序
				<input name="resultordertype" type="radio" value="0" checked />
				降序
			</td>
		</tr>
		</tbody>
		<tbody>
		<tr>
			<th><label for="searchforumid">搜索范围</label></th>
			<td>
				<select name="searchforumid" size="12" style="width:450px" multiple="multiple" id="searchforumid">
				 <option selected value="">---------- 所有版块(默认) ----------</option>
					<!--模版中所有版块的下拉框中一定要加入value=""否则会提示没有选择版块-->
					${forumlist}
				 </select>
				 <p>(按Ctrl或Shift键可以多选,不选择)</p>
			</td>
		</tr>
		</tbody>
		</table>
	</div>
	<div id="spacepostoptions">
		<table cellpadding="0" cellspacing="0" border="0">
		<tbody>
		<tr>
			<th><label for="searchtime">时间</label></th>
			<td>
				<select name="searchtime" id="searchtime">
				  <option value="0" selected="selected">全部时间</option>
				  <option value="-1">1天前</option>
				  <option value="-2">2天前</option>
				  <option value="-3">3天前</option>
				  <option value="-7">1周前</option>
				  <option value="-30">1个月前</option>
				  <option value="-90">3个月前</option>
				  <option value="-180">半年前</option>
				  <option value="-365">1年前</option>
				</select>
				  <input type="radio" name="searchtimetype" value="1" />
				之前
				<input name="searchtimetype" type="radio" value="0" checked />
				之后
			</td>
		</tr>
		</tbody>
		<tbody>
		<tr>
			<th><label for="resultorder">结果排序</label></th>
			<td>
				<select name="resultorder" id="resultorder">
				  <option value="0" selected="selected">发表时间</option>
				  <option value="1">回复数量</option>
				  <option value="2">查看次数</option>
				</select>
				<input type="radio" name="resultordertype" value="1" />
				升序
				<input name="resultordertype" type="radio" value="0" checked />
				降序
			</td>
		</tr>
		</tbody>
		</table>
	</div>
	<div id="albumoptions">
		<table cellpadding="0" cellspacing="0" border="0">
		<tbody>
		<tr>
			<th><label for="searchtime">时间</label></th>
			<td>
				<select name="searchtime" id="searchtime">
				  <option value="0" selected="selected">全部时间</option>
				  <option value="-1">1天前</option>
				  <option value="-2">2天前</option>
				  <option value="-3">3天前</option>
				  <option value="-7">1周前</option>
				  <option value="-30">1个月前</option>
				  <option value="-90">3个月前</option>
				  <option value="-180">半年前</option>
				  <option value="-365">1年前</option>
				</select>
				  <input type="radio" name="searchtimetype" value="1" />
				之前
				<input name="searchtimetype" type="radio" value="0" checked />
				之后
			</td>
		</tr>
		</tbody>
		<tbody>
		<tr>
			<th><label for="resultorder">结果排序</label></th>
			<td>
				<select name="resultorder" id="resultorder">
				  <option value="0" selected="selected">创建时间</option>
				</select>
				<input type="radio" name="resultordertype" value="1" />
				升序
				<input name="resultordertype" type="radio" value="0" checked />
				降序
			</td>
		</tr>
		</tbody>
		</table>
	</div>
</div>
<form id="postpm" name="postpm" method="post" onsubmit="if(this.chkAuthor.checked)$('type').value='author';return true;" action="">
<DIV class="mainbox formbox">
	<h1>搜索</h1>
	<TABLE cellSpacing="0" cellPadding="0" summary="搜索">
		<tbody>
		<tr id="divkeyword">
			<th><label for="keyword">关键词</label></th>
			<td><input name="keyword" type="text" id="keyword" size="45" />&nbsp;&nbsp;多个关键词间用英文空格分开</td>
		</tr>
		</tbody>
		<tbody>
		<tr>
			<th><label for="poster">作者</label></th>
			<td>
			<input name="poster" type="text" id="poster" size="45" />
			<input type="checkbox" value="1" id="chkAuthor" name="chkAuthor" onclick="checkauthoroption(this);" />搜索该作者所有帖子及相关内容
			</td>
		</tr>
		</tbody>
	</table>
	<table cellSpacing="0" cellPadding="0" summary="搜索选项">
		<thead>
		<tr>
			<th id="divsearchoption">搜索选项</th>
			<td>&nbsp;</td>
		</tr>
		</thead>
		<tbody id="divsearchtype">
		<tr>
			<th><label for="poster">搜索类型</label></th>
			<td>
			    <input type="hidden" name="type" value="post" id="type" />
				<input name="keywordtype" type="radio" value="0" checked onclick="changeoption('post');" />
				帖子标题搜索
				<#if usergroupinfo.allowsearch==1>
					<input type="radio" name="keywordtype" value="1" onclick="changeoption('post');" />
				内容搜索
				</#if>				
			</td>
		</tr>
		</tbody>
	</table>
	<div id="options" style="margin-top:-1px;"></div>
	<script type="text/javascript" src="javascript/template_search.js"></script>	
	<table cellSpacing="0" cellPadding="0" summary="搜索类型" style="margin-top:-1px;">
		<tbody>
		<tr>
			<th>&nbsp;</th>
			<td>
				 <input name="submit" type="submit" id="submit" value="执行搜索" />
			</td>
		</tr>
		</tbody>
	</table>
</div>
</form>
</div>
<#else>
<#if type=="">
<div class="pages_btns">
	<div class="pages">
		<em>${pageid}/${pagecount}页</em>${pagenumbers}
		<kbd>跳转<input name="gopage" type="text" id="gopage" onKeyDown="if(event.keyCode==13) {
	window.location='search.action?searchid=${searchid}&page='+this.value;}"  size="4" maxlength="9"/>页</kbd>
	</div>
	<span class="postbtn">共搜索到${topiccount}个符合条件的帖子</span>
</div>

<DIV class="mainbox forumlist">
	<h1>搜索结果</h1>
	<table cellSpacing="0" cellPadding="0" summary="搜索结果">
		<thead class="category">
		<tr>
			<th>标题</th>
			<td>所在版块</td>
			<td>作者</td>
			<td class="nums">回复</td>
			<td class="nums">查看</td>
			<td>最后发表</td>
		</tr>
		</thead>
		<#list topiclist as topic>
		<tbody>
			<tr>
				<td>
				    <#assign aspxrewriteurl = "showtopic.action?topicid="+topic[0]>
					<a href="${aspxrewriteurl}" target="_blank">${topic[1]}</a></td>
				<td>
				    <#assign aspxrewriteurl = "showforum.action?forumid="+topic[10]>
					<a href="${aspxrewriteurl}" target="_parent">${topic[11]}</a>
				</td>
				<td>
					<h4>
					<#if topic[3]==-1>
						游客
					<#else>
					    <#assign aspxrewriteurl = "userinfo.action?userid="+topic[3]>
						<a href="${aspxrewriteurl}" target="_parent">${topic[2]}</a>
					</#if></h4>
					<em>${topic[4]}</em>
				</td>
				<td class="nums">${topic[5]}</td>
				<td class="nums">${topic[6]}</td>
				<td>
						<em><a href="showtopic.action?topicid=${topic[0]}&page=end" target="_blank">${topic[7]}</a></em>
						<cite>
						<#if topic[9]==-1>
							游客
						<#else>
							<a href="userinfo.action?userid=${topic[9]}" target="_blank">${topic[8]}</a>
						</#if>
						</cite>
				</td>
			</tr>
		</tbody>
		</#list>
	</table>
</div>
<div class="pages_btns">
	<div class="pages">
		<em>${pageid}/${pagecount}页</em>${pagenumbers}
		<kbd>跳转<input name="gopage" type="text" id="gopage" onKeyDown="if(event.keyCode==13) {
	window.location='search.action?searchid=${searchid}&page='+this.value;}"  size="4" maxlength="9"/>页</kbd>
	</div>
	<span class="postbtn">共搜索到${topiccount}个符合条件的帖子</span>
</div>
</#if>
<#if type=="author">
<div class="searchtab">
		<a id="result1" class="currenttab" onmouseover="javascript:doClick_result(this)" href="###">帖子搜索结果</a>
</div>
<div id="resultid1" style="display:block;">
	<DIV class="mainbox forumlist">
	<h1>帖子搜索结果(共${topiccount}个)</h1>
	<table cellSpacing="0" cellPadding="0" summary="帖子搜索结果">
		<thead class="category">
		<tr>
			<th>标题</th>
			<td>所在版块</td>
			<td>作者</td>
			<td class="nums">回复</td>
			<td class="nums">查看</td>
			<td>最后发表</td>
		</tr>
		</thead>
		<#list topiclist as topic>
		<tbody>
			<tr>
				<th>
				    <#assign aspxrewriteurl = "showtopic.action?topicid="+topic[0]>
					<a href="${aspxrewriteurl}" target="_blank">${topic[1]}</a>
				</th>
				<td>
				    <#assign aspxrewriteurl = "showforum.action?forumid="+topic[10]>
					<a href="${aspxrewriteurl}" target="_parent">${topic[11]}</a>
				</td>
				<td>
					<p>
					<#if topic[3]==-1>
						游客
					<#else>
					    <#assign aspxrewriteurl = "userinfo.action?userid="+topic[3]>
						<a href="${aspxrewriteurl}" target="_parent">${topic[2]}</a>
					</#if></p>
					<em>${topic[4]}</em>
				</td>
				<td class="nums">${topic[5]}</td>
				<td class="nums">${topic[6]}</td>
				<td>
						<em><a href="showtopic.action?topicid=${topic[0]}&page=end" target="_blank">${topic[7]}</a></em>
						<cite>
					<#if topic[9]==-1>
							游客
						<#else>
							<a href="userinfo.action?userid=${topic[9]}" target="_blank">${topic[8]}</a>
						</#if>
						</cite>
				</td>
			</tr>
		</tbody>
		</#list>
	</table>
	</div>
	<div class="pages_btns">
		<div class="pages">
			<em>${topicpageid}/${topicpagecount}页</em>${topicpagenumbers}
		</div>
	</div>
</div>
<script type="text/javascript">
switch (getQueryString('show'))
{	
	case 'album':
		doClick_result($('result3'));
		break;
	case 'blog':
		doClick_result($('result2'));
		break;
	case 'topic':
	default:
		doClick_result($('result1'));
		break;
}

function doClick_result(o){
	o.className="currenttab";
	var j;
	var id;
	var e;
	for(var i=1;i<=3;i++){
		id ="result"+i;
		j = document.getElementById(id);
		e = document.getElementById("resultid"+i);
		if(id != o.id){
			j.className="";
			e.style.display = "none";
		}else{
			e.style.display = "block";
		}
	}
}
</script>
</#if>
</div>
</#if>
</#if>
<#else>
<@comm.errmsgbox/>
</#if>
<@comm.copyright/>
<@comm.footer/>