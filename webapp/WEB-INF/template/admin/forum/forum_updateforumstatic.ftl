<#import "../../controls/control.ftl" as c>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html >
	<head>
		<title>updateforumstatic</title>
		<link href="../styles/dntmanager.css" type="text/css" rel="stylesheet" /> 
		<script type="text/javascript" src="../js/common.js"></script>
		<script type="text/javascript">	
            var lastnumber=0;
            var currentnum=0;
             
            function runstatic(opname,pertask)
            {  
              if(lastnumber>-1)
              { 
                 currentnum=lastnumber/1+pertask/1;    // alert(currentnum);

                 var result;
                 switch (opname)      
                 {
                    case "ReSetFourmTopicAPost":     result="\r\n重建论坛帖数:"+lastnumber+" 至 "+currentnum;break
                    case "ReSetUserDigestPosts":     result="\r\n重建用户精华帖数"+lastnumber+" 至 "+currentnum;break
                    case "ReSetUserPosts":           result="\r\n重建用户发帖数"+lastnumber+" 至 "+currentnum;break
                    case "ReSetTopicPosts":          result="\r\n重建主题帖数"+lastnumber+" 至 "+currentnum;break
                 }
                 
	             document.getElementById('Layer5').innerHTML ="<br /><table><tr><td valign=top><img border=\"0\" src=\"../images/ajax_loading.gif\"  /></td><td valign=middle style=\"font-size: 14px;\" >"+result+"<BR /></td></tr></table><BR />";
                 document.getElementById('success').style.display ="block"; 
                 
                 lastnumber=getReturn('../global/global_ajaxcall.action?opname='+opname+'&lastnumber='+lastnumber+'&pertask='+pertask);
                 //alert(lastnumber);
                 if(lastnumber==null)
                 {
                    document.getElementById('Layer5').innerHTML="<br />操作成功执行";
                    document.getElementById('success').style.display = "block";
	                count(); 
	                document.getElementById('Form1').submit();
                 }
                }
              else
              {
                 document.getElementById('Layer5').innerHTML="<br />操作成功执行";
                 document.getElementById('success').style.display = "block";
	             count(); 
	             document.getElementById('Form1').submit();
              }
            }

            function clearflag()
            {
                 bar=0;
                 document.getElementById('Layer5').innerHTML="<br />操作成功执行";
                 document.getElementById('success').style.display = "block";
	             count(); 
            }


            var bar=0;
            function count()
            { 
		            bar=bar+4;
	                if (bar<99) {setTimeout("count()",100);} 
		            else { document.getElementById('success').style.display ="none"; } 
            }

            function run(opname,pertask)
            {
              if(pertask=="")
              {   
                  alert('每个循环更新数量不能为空!');return;
              }
              lastnumber=0;
              currentnum=0;
              bar=0;
              
              document.getElementById('Layer5').innerHTML="<br /><table><tr><td valign=top><img border=\"0\" src=\"../images/ajax_loading.gif\"  /></td><td valign=middle style=\"font-size: 14px;\" >正在处理数据, 请稍等...<BR /></td></tr></table><BR />";
              document.getElementById('success').style.display = "block";
              //runstatic(opname,pertask);
              setInterval('runstatic("'+opname+'",'+pertask+')',1000); //每次提交时间为1秒
            }


            var result=0;
            function run2(opname,startvalue,endvalue)
            {

              document.getElementById('Layer5').innerHTML="<br /><table><tr><td valign=top><img border=\"0\" src=\"../images/ajax_loading.gif\"  /></td><td valign=middle style=\"font-size: 14px;\" >正在处理数据, 请稍等...<BR /></td></tr></table><BR />";
              document.getElementById('success').style.display = "block";
              //alert(opname); 
              pageurl='../global/global_ajaxcall.action?opname='+opname+'&startvalue='+startvalue+'&endvalue='+endvalue;
              setTimeout('getforumdata("'+pageurl+'")',1000); //每次提交时间为5秒
            }


            function getforumdata(pageurl)
            {
              result=getReturn(pageurl);
              if((result>0)||(result==null))
              {
		            bar=0;
                    document.getElementById('Layer5').innerHTML="<br />操作成功执行";
                    document.getElementById('success').style.display = "block";
                    count();
	                document.getElementById('Form1').submit();
              }
              result=0;
            }
	    </script>
	</head>
	<body>	
		<form id="Form1" method="post">
		${htmlBuilder}
		    <div style="width:98%;margin:0 auto;">
		    <table cellspacing="0" cellpadding="4" width="100%" align="center">
		        <tr>
                    <td class="panelbox">
                        <table width="100%">
                            <tr>
                                <td style="width:260px">重建论坛全部帖数:</td>
                                <td style="width:260px">
                                    每个循环更新数量:&nbsp;&nbsp;&nbsp;&nbsp;
			                        <@c.textBox id="pertask1"  value="15" width="50"  size="5"></@c.textBox>
                                </td>
                                <td>
                                    <span id="ReSetFourmTopicAPost_id"  onmouseover="showhintinfo(this,0,0,'','重建论坛全部帖数','50','up');" onmouseout="hidehintinfo();">
                                        <span>
	                                        <button type="button" id="ReSetFourmTopicAPost_id" class="ManagerButton" onclick="javascript:run('ReSetFourmTopicAPost',pertask1.value);">
	                                        <img src="../images/submit.gif" />提 交</button>
                                        </span>
                                    </span>
                                </td>
                            </tr>
                            <tr>
                                <td>重建全部用户精华帖数:</td>
                                <td>
                                    每个循环更新数量:&nbsp;&nbsp;&nbsp;&nbsp;
			                        <@c.textBox id="pertask2"  value="1000" width="50"  size="5"></@c.textBox>
                                </td>
                                <td>
                                    <span id="ReSetUserDigestPosts_id"  onmouseover="showhintinfo(this,0,0,'','重建全部用户精华帖数','50','up');" onmouseout="hidehintinfo();">
                                        <span>
			                                <button id="ReSetUserDigestPosts_id" type="button" class="ManagerButton" onclick="javascript:run('ReSetUserDigestPosts',pertask2.value);">
			                                    <img src="../images/submit.gif" />提 交
			                                </button>
			                            </span>
			                        </span>
                                </td>
                            </tr>
                            <tr>
                                <td>重建全部用户发帖数:</td>
                                <td>
                                    每个循环更新数量:&nbsp;&nbsp;&nbsp;&nbsp;
			                        <@c.textBox id="pertask3"  value="1000" width="50"  size="5"></@c.textBox>
                                </td>
                                <td>
		                            <span id="ReSetUserPosts_id"  onmouseover="showhintinfo(this,0,0,'','重建全部用户发帖数','50','up');" onmouseout="hidehintinfo();">
		                                <span>
			                                <button id="ReSetUserPosts_id" type="button" class="ManagerButton" onclick="javascript:run('ReSetUserPosts',pertask3.value);">
			                                    <img src="../images/submit.gif" />提 交
			                                </button>
			                            </span>
			                        </span>
                                </td>
                            </tr>
                            <tr>
                                <td>重建全部主题帖数:</td>
                                <td>
                                    每个循环更新数量:&nbsp;&nbsp;
			                        <@c.textBox id="pertask4"  value="500"  width="50" size="5"></@c.textBox>
                                </td>
                                <td>
		                            <span id="ReSetTopicPosts_id"  onmouseover="showhintinfo(this,0,0,'','重建全部主题帖数','50','up');" onmouseout="hidehintinfo();">
		                                <span>
			                                <button id="ReSetTopicPosts_id" type="button" class="ManagerButton" onclick="javascript:run('ReSetTopicPosts',pertask4.value);" >
			                                    <img src="../images/submit.gif" />提 交
			                                </button>
			                            </span>
			                        </span>	
                                </td>
                            </tr>
                        </table>
                    </td>
                </tr>
            </table>	
            <hr style="border-top:0; border-bottom:1px solid #ccc;" size="1" />
            <table cellspacing="0" cellpadding="4" width="100%" align="center">
		        <tr>
                    <td class="panelbox">
                        <table width="100%">
                            <tr>
                                <td style="width:260px">重建指定论坛区间帖数:</td>
                                <td style="width:260px">
			                        开始版块FID:
			                        <@c.textBox id="startfid"  value="1" width="100"  size="10"></@c.textBox><br />
			                        结束版块FID:
			                        <@c.textBox id="endfid"  value="20" width="100" size="10"></@c.textBox>
			                    </td>
                                <td>
		                            <span id="ReSetFourmTopicAPost_StartEnd_id"  onmouseover="showhintinfo(this,0,0,'','重建指定论坛区间帖数','50','up');" onmouseout="hidehintinfo();">
		                                <span>
			                                <button id="ReSetFourmTopicAPost_StartEnd_id" type="button" class="ManagerButton" 
			                                onclick="javascript:run2('ReSetFourmTopicAPost_StartEnd',startfid.value,endfid.value);">
			                                    <img src="../images/submit.gif" />提 交
			                                </button>
			                            </span>
			                        </span>
                                </td>
                            </tr>
                            <tr>
                                <td>重建指定用户区间精华帖数:</td>
                                <td>
			                        开始用户UID:
			                        <@c.textBox id="startuid_digest"  value="1" width="100" size="10"></@c.textBox><br />
			                        结束用户UID:
			                        <@c.textBox id="enduid_digest"  value="20" width="100" size="10"></@c.textBox>
                                </td>
                                <td>
		                            <span id="ReSetUserDigestPosts_StartEnd_id"  onmouseover="showhintinfo(this,0,0,'','重建指定用户区间精华帖数','50','up');" onmouseout="hidehintinfo();">
		                                <span>
			                                <button id="ReSetUserDigestPosts_StartEnd_id" type="button" class="ManagerButton" 
			                                onclick="javascript:run2('ReSetUserDigestPosts_StartEnd',startuid_digest.value,enduid_digest.value);">
			                                    <img src="../images/submit.gif" />提 交
			                                </button>
			                            </span>
			                        </span>
                                </td>
                            </tr>
                            <tr>
                                <td>重建指定用户区间发帖数:</td>
                                <td>
                                    开始用户UID:
                                    <@c.textBox id="startuid_post"  value="1" width="100" size="10"></@c.textBox><br />
                                    结束用户UID:
                                    <@c.textBox id="enduid_post"  value="20" width="100" size="10"></@c.textBox>
                                </td>
                                <td>
                                    <span id="ReSetUserPosts_StartEnd_id"  onmouseover="showhintinfo(this,0,0,'','重建指定用户区间发帖数','50','up');" onmouseout="hidehintinfo();">
                                        <span>
                                            <button id="ReSetUserPosts_StartEnd_id" type="button" class="ManagerButton" 
                                            onclick="javascript:run2('ReSetUserPosts_StartEnd',startuid_post.value,enduid_post.value);">
                                                <img src="../images/submit.gif" />提 交
                                            </button>
                                        </span>
                                    </span>
                                </td>
                            </tr>
                            <tr>
                                <td>重建指定主题区间帖数:</td>
                                <td>
			                        开始主题TID:
			                        <@c.textBox id="starttid"  value="1" width="100" size="10"></@c.textBox><br />
			                        结束主题TID:
			                        <@c.textBox id="endtid"  value="20" width="100" size="10"></@c.textBox>
                                </td>
                                <td>
                                    <span id="ReSetTopicPosts_StartEnd_id"  onmouseover="showhintinfo(this,0,0,'','重建指定主题区间帖数','50','up');" onmouseout="hidehintinfo();">
		                                <span>
			                                <button id="ReSetTopicPosts_StartEnd_id" type="button" class="ManagerButton" 
			                                onclick="javascript:run2('ReSetTopicPosts_StartEnd',starttid.value,endtid.value);">
			                                    <img src="../images/submit.gif" />提 交
			                                </button>
			                            </span>
			                        </span>
                                </td>
                            </tr>
                        </table>
                    </td>
                </tr>
            </table>		
		    <hr style="border-top:0; border-bottom:1px solid #ccc;" size="1" />
		    <table cellspacing="0" cellpadding="4" width="100%" align="center">
		        <tr>
                    <td class="panelbox">
                        <table width="100%">
                            <tr>
                                <td style="width:540px">清理移动标记:</td>
                                <td><@c.button id="SubmitClearFlag" method="clearFlag"  hintTitle="提示" hintInfo="清理移动标记" text="提 交" /></td>
                            </tr>
                            <tr>
                                <td>重建论坛统计数据:</td>
                                <td><@c.button id="ReSetStatistic" method="reSetStatistic"  hintTitle="提示" hintInfo="重建论坛统计(表)数据" text="提 交" /></td>
                            </tr>
                            <tr>
                                <td>系统调整论坛版块:</td>
                                <td><@c.button id="SysteAutoSet" method="systeAutoSet" hintTitle="提示" hintInfo="系统调整论坛版块,对论坛版块表中的链接, 子版数等相关内容进行调整. " text="提 交" /></td>
                            </tr>   
                            <tr>
                                <td>更新所有版块的当前帖数:</td>
                                <td><@c.button id="UpdateCurTopics" method="updateCurTopics" text="提 交" hintTitle="提示" hintInfo="如果版块内主题数缺少或分页不准, 可执行此操作"></@c.button></td>
                            </tr>
                            <tr>
                                <td>更新所有版块最后发帖:</td>
                                <td><@c.button id="UpdateForumLastPost" method="updateForumLastPost" hintTitle="提示" hintInfo="更新版块最后发帖" text="提 交" /></td>
                            </tr>
                        </table>
                    </td>
                </tr>
            </table>
		    <hr style="border-top:0; border-bottom:1px solid #ccc;" size="1" />
		    <table cellspacing="0" cellpadding="4" width="100%" align="center">
		        <tr>
                    <td class="panelbox">
                        <table width="100%">
                            <tr>
                                <td style="width:540px">更新我的主题:</td>
                                <td><@c.button id="UpdateMyTopic" enabled="true" method="updateMyTopic" hintTitle="提示" hintInfo="更新我的主题" text="提 交" /></td>
                            </tr>
                            <tr>
                                <td>更新我的帖子:</td>
                                <td><@c.button id="UpdateMyPost" enabled="true" method="updateMyPost" hintTitle="提示" hintInfo="更新我的帖子" text="提 交" /></td>
                            </tr>
                        </table>
                    </td>
                </tr>
            </table>
		    <@c.hint hintImageUrl="../images" />	
		      </div>
		</form>
		${footer}
	</body>
</html>
