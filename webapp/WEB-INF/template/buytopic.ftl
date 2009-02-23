<#-- 
	描述：主题购买页模板
	作者：黄磊 
	版本：v1.0 
-->
<#import "inc/page_comm.ftl" as comm />
<@comm.header/>
<div id="foruminfo">
	<div id="nav">
		<a href="${config.forumurl}">${config.forumtitle}</a> &raquo; ${forumnav} 
	     &raquo; <a href="showtopic.action?topicid=${topicid}">${topictitle}</a> &raquo; <strong>交易信息</strong>
	</div>
</div>
<#if reqcfg.page_err==0>
<!--1-->
<#if ispost>
<!--2-->
<@comm.msgbox/>
<#else>
<!--3-->
<#if buyit==1>
<!--4-->
	<div class="mainbox">
		<h3>购买主题</h3>
		<form id="form1" name="form1" method="post" action="">
		<table cellpadding="0" cellspacing="0" border="0" summary="购买主题">
		<tbody>
		<tr>
			<th>用户名:</th>
			<td>${username} [<a href="logout.action?userkey=${userkey}">退出登录</a>]</td>
		</tr>
		</tbody>
		<tbody>
		<tr>
			<th>作者:</th>
			<td>
			<a href="userinfo.action?userid=${posterid}">${poster}</a>
			</td>
		</tr>
		</tbody>
		<tbody>
		<tr>
			<th>标题:</th>
			<td>
			<a href="showtopic.action?topicid=${topicid}">${topictitle}</a>
			</td>
		</tr>
		</tbody>
		<tbody>
		<tr>
			<th>售价(${userextcreditsinfo.name}):</th>
			<td>${topicprice}</td>
		</tr>
		</tbody>
		<tbody>
		<tr>
			<th>作者所得(${userextcreditsinfo.name}):</th>
			<td>${netamount}</td>
		</tr>
		</tbody>
		<tbody>
		<tr>
			<th>购买后余额(${userextcreditsinfo.name}):</th>
			<td>${userlastprice}</td>
		</tr>
		</tbody>
		<tbody>
		<tr>
			<th>&nbsp;</th>
			<td>
			<input type="submit" name="paysubmit" value="提交">
			</td>
		</tr>
		</tbody>
		</table>
		</form>
	</div>
<#else>
	<!--5-->
	<#if showpayments==1>
	<!--6-->
	<div class="mainbox">
		<table cellpadding="0" cellspacing="0" border="0"  summary="购买主题">
			<thead>
			<tr>
				<th>${userextcreditsinfo.name}</th>
				<th>用户名</th>
				<th>时间</th>
			</tr>
			</thead>
			<#list paymentloglist as paymentlog>
			<tbody>
			<tr>
				<td><div class="ForumBuyTopicLeft">${paymentlog.amount}</div></td>
				<th>
                  <a href="userinfo.action?userid=${paymentlog.usersByUid.uid}" target="_blank">${paymentlog.usersByUid.username}</a></th>
				<td>${paymentlog.buydate}</td>
				
			</tr>
			</tbody>
			</#list>
		</table>
</div>
	</#if>
	<#if (price>0)>
	<!--7-->
	<div class="mainbox">
		<h3>标题:${topictitle}</h3>
		${postmessage}
		<div class="navformcommend" style="border-bottom:none;"></div>
		<table cellpadding="0" cellspacing="0" border="0">
		<thead>
		<tr>
			<th>&nbsp</th>
			<td>本主题需向作者支付相应积分才能浏览，您可根据作者信誉、出售价格及已购买用户的评价确定购买与否。</td>
		</tr>
		</thead>
		<tbody>
		<tr>
			<th >售价(${userextcreditsinfo.name})</th>
			<td>${topicprice}</td>
		</tr>
		</tbody>
		<tbody>
		<tr>
			<th>积分交易税</th>
			<td>${creditstax}%</td>
		</tr>
		</tbody>
		<tbody>
		<tr>
			<th>作者所得(${userextcreditsinfo.name})</th>
			<td>${netamount}</td>
		</tr>
		</tbody>
		<tbody>
		<tr>
			<th>已购买人数</th>
			<td>${buyers}</td>
		</tr>
		</tbody>
		<tbody>
		<tr>
			<th>作者最高收入(${userextcreditsinfo.name})</th>
			<td>${maxincpertopic}</td>
		</tr>
		</tbody>
		<tbody>
		<tr>
			<th>强制免费期限(小时)</th>
			<td>${maxchargespan}</td>
		</tr>
		</tbody>
		<tbody>
		<tr>
			<th>&nbsp;</th>
			<td>
			<a href="buytopic.action?topicid=${topicid}&showpayments=1">[查看付款记录]</a><a href="buytopic.action?topicid=${topicid}&buyit=1">[购买主题]</a> <a href="#" onclick="history.go(-1)">[返回上一页]</a>
			</td>
		</tr>
		</tbody>
		</table>
	</div>
	<div class="mainbox">
		<h3>最后5帖</h3>
		<table cellpadding="0" cellspacing="0" border="0">
		<#list lastpostlist as lastpost>
		<tbody>
		<tr>
			<th>
			<a href="userinfo.action?userid=${lastpost.users.uid}"><strong>${lastpost.poster}</strong></a>&nbsp;&nbsp;${lastpost.postdatetime}
			</th>
		</tr>
		<tr>
			<td>${lastpost.message}</td>
		</tr>
		</tbody>
		</#list>
		</table>
	</div>
	<#else>
		此主题无需购买
	</#if>
</#if>
</div>
</#if>
<#else>
	<#if needlogin>
		<@comm.login/>
	<#else>
		<@comm.errmsgbox/>
	</#if>
</div>
</#if>
<@comm.copyright/>
<@comm.footer/>