<#import "../../controls/control.ftl" as c>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN" >
<html>
<head>
<title>baseset</title>
<link href="../styles/dntmanager.css" type="text/css" rel="stylesheet" />
<link href="../styles/modelpopup.css" type="text/css" rel="stylesheet" />
<script type="text/javascript" src="../js/common.js"></script>
<script type="text/javascript" src="../js/modalpopup.js"></script>
<@c.validator "Form1" />
</head>
<body>
<div class="ManagerForm">
<form id="Form1" method="post" name="Form1">${htmlBuilder}
<fieldset><legend
	style="background: url(../images/icons/legendimg.jpg) no-repeat 6px 50%;">基本设置</legend>
<table cellspacing="0" cellpadding="4" width="100%" align="center">
	<tr>
		<td class="panelbox" width="50%" align="left">
		<table width="100%">
			<tr>
				<td style="width: 80px">网站名称:</td>
				<td><@c.textBox id="webtitle" width="200px" hintTitle="提示" 
						    hintInfo="网站名称, 将显示在页面底部的联系方式处" value="${config.webtitle}"/>
				</td>
			</tr>
			<tr>
				<td>论坛名称:</td>
				<td><@c.textBox id="forumtitle" width="200px" hintTitle="提示" 
						    hintInfo="论坛名称, 将显示在导航条和标题中" value="${config.forumtitle}"/>
				</td>
			</tr>
			<tr>
				<td>网站备案信息代码:</td>
				<td><@c.textBox id="icp" width="200px" hintTitle="提示" 
						    hintInfo="页面底部可以显示 ICP 备案信息,如果网站已备案,在此输入您的授权码,它将显示在页面底部,如果没有请留空" value="${config.icp}"/>
				</td>
			</tr>
			<tr>
				<td>启用 RSS:</td>
				<td><@c.radioButtonList id="rssstatus" hintTitle="提示" 
						    hintInfo="选择&amp;quot;是&amp;quot;, 论坛将允许用户使用 RSS 客户端软件接收最新的论坛帖子更新. 注意: 在分论坛很多的情况下, 本功能可能会加重服务器负担">
						 <@c.radioItem name="rssstatus" value="1" checked="${config.rssstatus}">是</@c.radioItem>
						 <@c.radioItem name="rssstatus" value="0" checked="${config.rssstatus}">否</@c.radioItem>   
					</@c.radioButtonList>
				</td>
			</tr>
			<tr>
				<td>启用 SiteMap:</td>
				<td><@c.radioButtonList id="sitemapstatus" hintTitle="提示" 
						    hintInfo="SiteMap为百度论坛收录协议,是否允许百度收录">
						 <@c.radioItem name="sitemapstatus" value="1" checked="${config.sitemapstatus}">是</@c.radioItem>
						 <@c.radioItem name="sitemapstatus" value="0" checked="${config.sitemapstatus}">否</@c.radioItem>   
					</@c.radioButtonList>
				</td>
			</tr>
			<tr>
				<td>身份验证Cookie域:</td>
				<td><@c.textBox id="CookieDomain" width="200px" hintTitle="提示" 
						    hintInfo="如需所有子域共享此Cookie, 例如:&lt;br />要让www.abc.com 与 bbs.abc.com共享论坛Cookie,则请设置此处为 .abc.com" value="${config.cookiedomain}"/>
				</td>
			</tr>
			<tr>
				<td>允许查看会员列表:</td>
				<td><@c.radioButtonList id="memliststatus" hintTitle="提示" 
						    hintInfo="允许查看会员列表">
						 <@c.radioItem name="memliststatus" value="1" checked="${config.memliststatus}">是</@c.radioItem>
						 <@c.radioItem name="memliststatus" value="0" checked="${config.memliststatus}">否</@c.radioItem>   
					</@c.radioButtonList>
				</td>				
			</tr>
			<tr>
				<td style="width: 140px">&nbsp;&nbsp;外部链接:</td>
				<td><@c.textareaResize id="Linktext" hintTitle="提示" hintInfo="用户可以自己添加的外部链接html字符串" value="${config.linktext}"/>
				</td>
			</tr>
		</table>
		</td>
		<td class="panelbox" width="50%" align="right">
		<table width="100%">
			<tr>
				<td style="width: 80px">网站URL地址:</td>
				<td><@c.textBox id="weburl" width="200px" hintTitle="提示" url="true" 
						    hintInfo="网站 URL, 将作为链接显示在页面底部" value="${config.weburl}"/>
				</td>
			</tr>
			<tr>
				<td>论坛URL地址:</td>
				<td><@c.textBox id="forumurl" width="200px" hintTitle="提示" 
						    hintInfo="论坛URL地址" value="${config.forumurl}"/>
				</td>
			</tr>
			<tr>
				<td>RSS TTL(单位:分钟):</td>
				<td><@c.textBox id="rssttl" width="200px" hintTitle="提示" required="true" min="0" size="5" maxlength="4"
						    hintInfo="TTL(Time to Live) 是 RSS 2.0 的一项属性, 用于控制订阅内容的自动刷新时间, 时间越短则资料实时性就越高, 但会加重服务器负担, 通常可设置为 30～180 范围内的数值" value="${config.rssttl}"/>
				</td>				
			</tr>
			<tr>
				<td>SiteMap TTL<br />
				(单位:小时):</td>
				<td><@c.textBox id="sitemapttl" width="200px" hintTitle="提示" maxlength="2" required="true" min="1" max="24" size="5" maxlength="2"
						    hintInfo="百度论坛收录协议更新时间, 用于控制百度论坛收录协议更新时间, 时间越短则资料实时性就越高, 但会加重服务器负担, 通常可设置为 1～24 范围内的数值" value="${config.sitemapttl}"/>
				</td>		
			</tr>
			<tr>
				<td>禁止浏览器缓冲:</td>
				<td><@c.radioButtonList id="nocacheheaders" hintTitle="提示" 
						    hintInfo="选择&quot;是&quot;将禁止浏览器对论坛页面进行缓冲, 用于解决极个别浏览器内容刷新不正常的问题. 注意: 本功能会加重服务器负担">
						 <@c.radioItem name="nocacheheaders" value="1" checked="${config.nocacheheaders}">是</@c.radioItem>
						 <@c.radioItem name="nocacheheaders" value="0" checked="${config.nocacheheaders}">否</@c.radioItem>   
					</@c.radioButtonList>
				</td>				
			</tr>
			<tr>
				<td style="width: 140px">&nbsp;&nbsp;统计代码设置:</td>
				<td><@c.textareaResize id="Statcode" hintTitle="提示"
							hintInfo="用户可以自己添加的统计代码" value="${config.statcode}"/>
				</td>
			</tr>
		</table>
		</td>
	</tr>
</table>
</fieldset>
<@c.hint hintImageUrl="../images" />
<fieldset><legend
	style="background: url(../images/icons/icon23.jpg) no-repeat 6px 50%;">模块开启设置</legend>
<table cellspacing="0" cellpadding="4" width="100%" align="center">
	<tr>
		<td class="panelbox" width="50%" align="left">
		<table width="100%">
			<tr>
				<td style="width: 140px">是否全部关闭<br />
				LForum模块:</td>
				<td><@c.radioButtonList id="closed" hintTitle="提示" 
						    hintInfo="暂时将LForum模块关闭(论坛), 其他人无法访问, 但不影响管理员访问">
						 <@c.radioItem name="closed" value="1" checked="${config.closed}">是</@c.radioItem>
						 <@c.radioItem name="closed" value="0" checked="${config.closed}">否</@c.radioItem>   
					</@c.radioButtonList>
				</td>
			</tr>
		</table>
		</td>
		<td class="panelbox" width="50%" align="right">
		<table width="100%">
			<tr>
				<td><span id="closeinfo">关闭的原因:</span></td>
				<td><@c.textareaResize id="closedreason" hintTitle="提示"
							hintInfo="论坛关闭时出现的提示信息" value="${config.closedreason}"/>
				</td>
			</tr>
		</table>
		</td>
	</tr>
</table>
</fieldset>
<div class="Navbutton"><@c.button id="SaveInfo" text="提交"  /></div>
</form>
</div>
${footer}
</body>
</html>
