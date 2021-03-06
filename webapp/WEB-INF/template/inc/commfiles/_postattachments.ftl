﻿<#-- 
	描述：发表附件模板
	作者：黄磊 
	版本：v1.0 
-->
<#if (attachsize>0)>
<#if attachextensions!="">
<div class="box" style="padding:0;">
<table cellspacing="0" cellpadding="0" summary="Upload">
	<thead>
		<tr>
			<th><img src="templates/${templatepath}/images/attachment.gif" alt="附件"/>上传附件</th>
			<#if userid!=-1 && usergroupinfo.allowsetattachperm==1>
			<td class="nums">阅读权限</td>
			</#if>
			<td>描述</td>
		</tr>
	</thead>
	<tbody style="display: none;" id="attachbodyhidden"><tr>
		<td>
			<input type="file" name="postfile"/>
			<span id="localfile[]"></span>&nbsp;
			<input type="hidden" name="localid" />
		</td>
		<#if userid!=-1 && usergroupinfo.allowsetattachperm==1>
		<td class="nums"><input type="text" name="readperm" value="0" size="1"/></td>
		</#if>
		<td><input type="text" name="attachdesc" size="25"/>			
		</td>
	</tr>
	</tbody>
	<tbody id="attachbody"></tbody>
	<tbody>
	<tr>
		<td style="border-bottom: medium none;" colspan="5">
			单个附件大小: <strong><script type="text/javascript">ShowFormatBytesStr(${usergroupinfo.maxattachsize.toString()});</script></strong><br/>
			今天可上传大小: <strong><script type="text/javascript">ShowFormatBytesStr(${attachsize.toString()});</script></strong><br/>
			附件类型: <strong>${attachextensionsnosize}</strong><br/>
		</td>
	</tr>
	</tbody>
</table>
</div>
<img id="img_hidden" alt="1" style="position:absolute;top:-100000px;filter:progid:DXImageTransform.Microsoft.AlphaImageLoader(sizingMethod='image');width:400;height:300"></img>
<script type="text/javascript">
var aid = 1;
var thumbwidth = parseInt(400);
var thumbheight = parseInt(300);
var attachexts = new Array();
var attachwh = new Array();

function delAttach(id) 
{
    var curattlength = $('attachbody').childNodes.length;
    $('attachbody').removeChild($('attach_' + id).parentNode.parentNode);
    $('attachbody').innerHTML == '' && addAttach();
    if (curattlength == ${config.maxattachments.toString()} && $("attach_" + (aid-1)).value != "")
    {
	    addAttach();
    }
	if ($('localimgpreview_' + id + '_menu'))
	{
		document.body.removeChild($('localimgpreview_' + id + '_menu'));
	}    
}

function addAttach() 
{
    if ($('attachbody').childNodes.length > ${config.maxattachments.toString()} - 1)
    {
	    return;
    }
    newnode = $('attachbodyhidden').firstChild.cloneNode(true);
    var id = aid;
    var tags;
    tags = findtags(newnode, 'input');
    for(i in tags) 
    {
        if(tags[i].name == 'postfile') 
        {
            tags[i].id = 'attach_' + id;
            tags[i].onchange = function() 
            {
	            insertAttach(id);
            };
            tags[i].unselectable = 'on';
        }
        if(tags[i].name == 'localid') 
        {
            tags[i].value = id;
        }
    }
    tags = findtags(newnode, 'span');
    for(i in tags) 
    {
        if(tags[i].id == 'localfile[]') 
        {
            tags[i].id = 'localfile_' + id;
        }
    }
    aid++;
    $('attachbody').appendChild(newnode);
}

addAttach();

function insertAttach(id) 
{
    var localimgpreview = '';
    var path = $('attach_' + id).value;
    var extensions = '${attachextensionsnosize}';
    var ext = path.lastIndexOf('.') == -1 ? '' : path.substr(path.lastIndexOf('.') + 1, path.length).toLowerCase();
    var re = new RegExp("(^|\\s|,)" + ext + "($|\\s|,)", "ig");
    var localfile = $('attach_' + id).value.substr($('attach_' + id).value.replace(/\\/g, '/').lastIndexOf('/') + 1);

    if(path == '') 
    {
        return;
    }
    if(extensions != '' && (re.exec(extensions) == null || ext == '')) 
    {
        alert('对不起，不支持上传此类扩展名的附件。');
        return;
    }
    attachexts[id] = is_ie && in_array(ext, ['gif', 'jpg', 'jpeg', 'png', 'bmp']) ? 2 : 1;

    var err = false;
    if(attachexts[id] == 2) 
    {
        $('img_hidden').alt = id;
        try 
        {
	        $('img_hidden').filters.item("DXImageTransform.Microsoft.AlphaImageLoader").sizingMethod = 'image';
        } 
        catch (e) 
        {err = true;}
        try 
        {
            $('img_hidden').filters.item("DXImageTransform.Microsoft.AlphaImageLoader").src = $('attach_' + id).value;
        } 
        catch (e) 
        {
			alert('无效的图片文件。');
			delAttach(id);

			err = true;		
            
            return;
        }
        var wh = {'w' : $('img_hidden').offsetWidth, 'h' : $('img_hidden').offsetHeight};
        var aid = $('img_hidden').alt;
        if(wh['w'] >= thumbwidth || wh['h'] >= thumbheight) 
        {
            wh = thumbImg(wh['w'], wh['h']);
        }
        attachwh[id] = wh;
        $('img_hidden').style.width = wh['w']
        $('img_hidden').style.height = wh['h'];
        try 
        {
	        $('img_hidden').filters.item("DXImageTransform.Microsoft.AlphaImageLoader").sizingMethod = 'scale';
        }
        catch (e)
        {
        }
        if (err == true)
        {
	        $('img_hidden').src = $('attach_' + id).value;
        }
        div = document.createElement('div');
        div.id = 'localimgpreview_' + id + '_menu';
        div.style.display = 'none';
        div.style.marginLeft = '20px';
        div.className = 'popupmenu_popup';
        document.body.appendChild(div);
        div.innerHTML = '<img style="filter:progid:DXImageTransform.Microsoft.AlphaImageLoader(sizingMethod=\'scale\',src=\'' + $('attach_' + id).value.replace(')','%29').replace('(','%28') +'\');width:'+wh['w']+';height:'+wh['h']+'" src=\'images/common/none.gif\' border="0" aid="attach_'+ aid +'" alt="" />';
    }
    var isimg = in_array(ext, ['gif', 'jpg', 'jpeg', 'png', 'bmp']) ? 2 : 1;
    $('localfile_' + id).innerHTML = '<a href="###delAttach" onclick="delAttach(' + id + ')">[删除]</a> <a href="###insertAttach" title="点击这里将本附件插入帖子内容中当前光标的位置" onclick="insertAttachtext(' + id + ', ' + err + ');return false;">[插入]</a> ' +
    (attachexts[id] == 2 ? '<span id="localimgpreview_' + id + '" onmouseover="showMenu(this.id, 0, 0, 1, 0)"> <span class="smalltxt">[' + id + ']</span> <a href="###attachment" onclick="insertAttachtext(' + id + ', ' + err + ');return false;">' + localfile + '</a></span>' : '<span class="smalltxt">[' + id + ']</span> ' + localfile);
    $('attach_' + id).style.display = 'none';
    /*if(isimg == 2)
        insertAttachtext(id, err);*/
    addAttach();
}

function insertAttachtext(id, iserr) 
{
    if(!attachexts[id]) 
    {
        return;
    }
    if(attachexts[id] == 2) 
    {
        bbinsert && wysiwyg && iserr == false ? insertText($('localimgpreview_' + id + '_menu').innerHTML, false) : AddText('[localimg=' + attachwh[id]['w'] + ',' + attachwh[id]['h'] + ']' + id + '[/localimg]');
    } 
    else 
    {
        bbinsert && wysiwyg ? insertText('[local]' + id + '[/local]', false) : AddText('[local]' + id + '[/local]');
    }
}

function thumbImg(w, h) 
{
    var x_ratio = thumbwidth / w;
    var y_ratio = thumbheight / h;
    var wh = new Array();

    if((x_ratio * h) < thumbheight) 
    {
        wh['h'] = Math.ceil(x_ratio * h);
        wh['w'] = thumbwidth;
    } 
    else 
    {
        wh['w'] = Math.ceil(y_ratio * w);
        wh['h'] = thumbheight;
    }
    return wh;
}
</script>


	<#else>
		<div class="hintinfo">							
				你没有上传附件的权限.
		</div>
	</#if>
<#else>
	<div class="hintinfo">
		<#if (usergroupinfo.maxsizeperday>0 && usergroupinfo.maxattachsize>0)>
			你目前可上传的附件大小为 0 字节.
		<#else>
			你没有上传附件的权限.
		</#if>
	</div>
</#if>
