<#-- 
	描述：版块列表(分栏模式)页模板
	作者：黄磊 
	版本：v1.0 
-->
<#import "inc/page_comm.ftl" as comm />
<@comm.pageHeader/>
<style>
body{
	margin:0px;
	padding:0px;
	text-align:left;
	background:#F5FAFD url(images/left-bg.jpg) repeat-y top right;
}
.collapse { BACKGROUND-POSITION: center center; BACKGROUND-IMAGE: url(templates/${templatepath}
	/images/collapse.gif); WIDTH: 6px; BACKGROUND-REPEAT: no-repeat; POSITION: absolute; HEIGHT: 50px; }
	.expand { BACKGROUND-POSITION: center center; BACKGROUND-IMAGE: url(templates/${templatepath}
	/images/expand.gif); WIDTH: 6px; BACKGROUND-REPEAT: no-repeat; POSITION: absolute; HEIGHT: 50px; }
</style>
<body onLoad="window_load();">
	<script type="text/javascript" src="javascript/ajax.js"></script>
	<script type="text/javascript">
			var NoUser = ${userid} == -1 ? true : false;
			var lastA = null;
		
			function window_load(){
				documentbody = document.documentElement.clientHeight > document.body.clientHeight ? document.documentElement : document.body;
				var leftbar = document.getElementById('leftbar')
				leftbar.style.height = documentbody.clientHeight +'px';
				leftbar.style.left = 0; //document.body.clientWidth - 6;
				leftbar.style.top = documentbody.scrollTop + 'px'; //document.body.clientWidth - 6;
				document.onscroll = function(){ 
												leftbar.style.height=documentbody.clientHeight +'px';
												leftbar.style.top=documentbody.scrollTop + 'px'; 
											}
											
				document.onresize = function(){ 
												leftbar.style.height=documentbody.clientHeight +'px';
												leftbar.style.top=documentbody.scrollTop + 'px'; 
											}
				
			}
			
			function resizediv_onClick(){
				if (document.getElementById('menubar').style.display != 'none'){
					top.document.getElementsByTagName('FRAMESET')[0].cols = "6,*";
					document.getElementById('menubar').style.display = 'none';
					document.getElementById('leftbar').className = "expand";
				}
				else{
					top.document.getElementsByTagName('FRAMESET')[0].cols = "210,*";
					document.getElementById('leftbar').className = "collapse";
					document.getElementById('menubar').style.display = '';
				}
			
			}
			
			//↓----------获得版块的树形列表相关脚本-------------------------
  			function changeExtImg(objImg){
				if (!objImg){ return; }	
				var fileName = objImg.src.toLowerCase().substring(objImg.src.lastIndexOf("/"));
				switch(fileName){
					case "/p0.gif":
						objImg.src = "images/tree/m0.gif";
						break;
					case "/p1.gif":
						objImg.src = "images/tree/m1.gif";
						break;
					case "/p2.gif":
						objImg.src = "images/tree/m2.gif";
						break;
					case "/p3.gif":
						objImg.src = "images/tree/m3.gif";
						break;
					case "/m0.gif":
						objImg.src = "images/tree/p0.gif";
						break;
					case "/m1.gif":
						objImg.src = "images/tree/p1.gif";
						break;
					case "/m2.gif":
						objImg.src = "images/tree/p2.gif";
						break;
					case "/m3.gif":
						objImg.src = "images/tree/p3.gif";
						break;
				}
			}
  
  			function changeFolderImg(objImg){
				if (!objImg){ return; }	
				var fileName = objImg.src.toLowerCase().substring(objImg.src.lastIndexOf("/"));
				switch(fileName){
					case "/folder.gif":
						objImg.src = "images/tree/folderopen.gif";
						break;
					case "/folderopen.gif":
						objImg.src = "images/tree/folder.gif";
						break;
				}
			}
			
			
			function a_click(objA){
				if (lastA){
					lastA.className=''; 
				}
				objA.className='bold'; 
				lastA = objA; 
			}
  			function writesubforum(objreturn,fid,AtEnd){
				var process = document.getElementById("process_" + fid);
				var forum = document.getElementById("forum_" + fid);
				var dataArray = objreturn.getElementsByTagName('forum');
				var dataArrayLen = dataArray.length;
				
				changeExtImg(document.getElementById("forumExt_" + fid));
				changeFolderImg(document.getElementById("forumFolder_" + fid));
				
				for (i=0;i<dataArrayLen;i++){
					var thisfid = dataArray[i].getAttribute("fid");
					var subforumcount = dataArray[i].getAttribute("subforumcount");
					var thisEnd = i==dataArrayLen-1;
					
					var layer = dataArray[i].getAttribute("layer");

						//显示树型线
						list = "";
						
						for (l=1;l<=layer;l++){
							if (AtEnd && NoUser){
								list += "<nobr><img src = \"images/tree/l5.gif\" align=\"absmiddle\" />";
							}
							else{
								list += "<img src = \"images/tree/l4.gif\" align=\"absmiddle\" />";
							}
						}
						if (subforumcount>0){
							folder = "folder.gif";
							if (layer==0 && thisEnd){
								if (NoUser){
									src = "p2.gif";
								}
								else{
									src = "p1.gif";
								}
							}
							else{
								if (thisEnd && layer>0){
									src = "p2.gif";
								}
								else{
									//if (i==0 && layer==0){
									//	src = "P0.gif";
									//}
									//else{
										src = "p1.gif";
									//}
								}
							}
						}
						else{
							folder = "file.gif";
							if (layer==0 && thisEnd){
								if (NoUser){
									src = "m2.gif";
								}
								else{
									src = "m1.gif";
								}
							}
							else{
								if (thisEnd){
									src = "l2.gif";
								}
								else{
									//if (i==0 && layer==0){
									//	src = "l0.gif";
									//}
									//else{
										src = "l1.gif";
									//}
								}
							}
						}
                      
					list += "<img id=\"forumExt_" + thisfid + "\" src = \"images/tree/" + src + "\" align=\"absmiddle\" /><img id=\"forumFolder_" + thisfid + "\" src = \"images/tree/" + folder + "\" align=\"absmiddle\" /> <a href=\"showforum.action?forumid=" + thisfid + "\" target=\"main\" title=\"" + dataArray[i].getAttribute("name") + "\" onclick=\"a_click(this);\">" + dataArray[i].getAttribute("name") + "</a></nobr>";

					var div_forumtitle =  document.createElement("DIV");
						div_forumtitle.id = "forumtitle_" + thisfid;
						div_forumtitle.className = "tree_forumtitle";
						if (subforumcount>0){
							div_forumtitle.onclick = new Function("getsubforum(" + thisfid + "," + thisEnd + ");");
						}
						div_forumtitle.innerHTML = list;
						forum.appendChild(div_forumtitle);
						
					var div_forum = document.createElement("DIV");
						div_forum.id = "forum_" + thisfid;
						div_forum.className = "tree_forum";
						forum.appendChild(div_forum);
					
					
				}
				process.style.display="none";
			}
			
			
			
			function getsubforum(fid,AtEnd){
				if (!document.getElementById("forum_" + fid)){
					document.writeln("<div id=\"forum_" + fid + "\"></div>");
				}
				if (!document.getElementById("process_" + fid)){
					var div = document.createElement("DIV");
					div.id = "process_" + fid;
					div.className = "tree_process";
					div.innerHTML = "<img src='images/common/loading.gif' />载入中...";
					
					document.getElementById("forum_" + fid).appendChild(div);
					
					ajaxRead("tools/ajax.action?t=forumtree&fid=" + fid, "writesubforum(obj," + fid+ "," + AtEnd + ");");
				}
				else{
					changeExtImg(document.getElementById("forumExt_" + fid));
					changeFolderImg(document.getElementById("forumFolder_" + fid));
					if (document.getElementById("forum_" + fid).style.display == "none"){
						document.getElementById("forum_" + fid).style.display = "block";
					}
					else{												
						document.getElementById("forum_" + fid).style.display = "none";
					}
				}

			}
			
			//↑----------获得版块的树形列表相关脚本-------------------------
			
	</script>
	<div id="leftbar" class="collapse" onmouseover="this.style.backgroundColor='#A7E8F3';"
		onmouseout="this.style.backgroundColor = '';" onclick="resizediv_onClick()" style="width:6px; cursor:pointer"
		title="打开/关闭导航">
	</div>
	<div id="menubar" style="white-space:nowrap;" valign="top">
	<div id="frameback">
		<strong><A href="###" onClick="resizediv_onClick()">隐藏侧栏</a></strong><em><A href="main.action?f=0" target="_top">平板模式</a></em>
	</div>
	<div class="framemenu">
		<ul>
			<#if userid!=-1>
				<li>欢迎访问${config.forumtitle}</li><br />
				<li><strong>
				<a href="#" target="main" >${userinfo.username}</a> </strong>[ <a href="logout.action?userkey=${userkey}&amp;reurl=focuslist.action" target="main">退出</a> ]</li>
				<li>积分: <strong>${userinfo.credits}</strong>  [<span id="creditlist" onMouseOver="showMenu(this.id, false);" style="CURSOR:pointer">详细积分</span>]
			</li>
				<li>头衔: ${usergroupinfo.grouptitle}
					<#if useradminid==1>
						| <a href="admin/index.action" target="_blank">系统设置</a>
					</#if>
				</li>
			<#else>
				<li>头衔: 游客
					[<a href="register.action" target="main">注册</a>] 
			[<a href="login.action?reurl=focuslist.action" target="main">登录</a>]
				</li>
			</#if>
			<#if (newpmcount>0)>
			<li>
				新的短消息<a href="usercpinbox.action" target="main"><span id="newpmcount">${newpmcount}</span></a>条
			</li>
			</#if>
			<li>
				<img src="templates/${templatepath}/images/home.gif">
				<a href="focuslist.action" target="main">论坛首页</a>
			</li>
			<li>
				<img src="images/tree/l1.gif" width="20" height="20" /><img src="templates/${templatepath}/images/folder_new.gif" width="20" height="20" />
				<a href="showtopiclist.action?type=newtopic&amp;newtopic=${newtopicminute}&amp;forums=all" target="main">
					查看新帖</a>
			</li>
			<li>
				<img src="images/tree/l1.gif" width="20" height="20"><img src="templates/${templatepath}/images/showdigest.gif" width="20" height="20" />
				<a href="showtopiclist.action?type=digest&amp;forums=all" target="main">精华帖区</a>
			</li>
	  </ul>
			<script type="text/javascript">
				//生成版块列表
				getsubforum(0);
			</script>
			<#if userid!=-1>
		<div onClick="getsubforum(-1,true);">
			<img id="forumExt_-1" src="images/tree/m2.gif" width="20" height="20" /><img id="forumFolder_-1" src="templates/${templatepath}/images/mytopic.gif" /><span class="cursor">用户功能区</span>
		</div>
		<div id="process_-1"></div>
		<div id="forum_-1" style="DISPLAY:block">
			<div><img src="images/tree/l5.gif" width="20" height="20" border="0"><img src="images/tree/l1.gif" width="20" height="20" border="0"><img src="templates/${templatepath}/images/folder_mytopic.gif" width="16" height="16">
				<a href="mytopics.action" target="main">我的主题</a></div>
			<div><img src="images/tree/l5.gif" width="20" height="20" border="0"><img src="images/tree/l1.gif" width="20" height="20" border="0"><img src="templates/${templatepath}/images/folder.gif" width="16" height="16">
				<a href="myposts.action" target="main">我的帖子</a></div>
			<div><img src="images/tree/l5.gif" width="20" height="20" border="0"><img src="images/tree/l1.gif" width="20" height="20" border="0"><img src="templates/${templatepath}/images/digest.gif">
				<a href="search.action?posterid=${userid}&amp;type=digest" target="main">我的精华</a></div>
			<div><img src="images/tree/l5.gif" width="20" height="20" border="0"><img src="images/tree/l1.gif" width="20" height="20" border="0"><img src="templates/${templatepath}/images/favorite.gif">
				<a href="usercpsubscribe.action" target="main">我的收藏</a></div>
			<div><img src="images/tree/l5.gif" width="20" height="20" border="0"><img src="images/tree/l1.gif" width="20" height="20" border="0"><img src="templates/${templatepath}/images/moderate.gif">
				<a href="usercp.action" target="main">用户中心</a></div>
			<div><img src="images/tree/l5.gif" width="20" height="20" border="0"><img src="images/tree/l2.gif" width="20" height="20" border="0"><img src="templates/${templatepath}/images/postpm.gif" width="16" height="16">
				<a href="usercppostpm.action" target="main">撰写短消息</a></div>
		</div>
		</#if>
	</div>
	<div class="framemenu">
		<ul>
			<li>在线用户: </li>
			<li>${totalonline}人在线  (${totalonlineuser}位会员) </li>
			<#if config.rssstatus!=0>
			<li>
			<a href="tools/rss.action" target="_blank"><img src="templates/${templatepath}/images/rss2.gif" alt="RSS订阅全部论坛"></a>
			</li>
			</#if>
	  </ul>
	</div>
</div>
<@comm.footer/>
<div id="creditlist_menu" class="popupmenu_popup" style="display:none;">
<#if userid!=-1>
	<#if score[1]!="">
	${score[1]}: ${userinfo.extcredits1}<br />
	</#if>
	<#if score[2]!="">
	${score[2]}: ${userinfo.extcredits2}<br />
	</#if>
	<#if score[3]!="">
	${score[3]}: ${userinfo.extcredits3}<br />
	</#if>
	<#if score[4]!="">
	${score[4]}: ${userinfo.extcredits4}<br />
	</#if>
	<#if score[5]!="">
	${score[5]}: ${userinfo.extcredits5}<br />
	</#if>
	<#if score[6]!="">
	${score[6]}: ${userinfo.extcredits6}<br />
	</#if>
	<#if score[7]!="">
	${score[7]}: ${userinfo.extcredits7}<br />
	</#if>
	<#if score[8]!="">
	${score[8]}: ${userinfo.extcredits8}<br />
	</#if>
</#if>
</div>