<#import "../../controls/control.ftl" as c>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN" >
<html>
<head>
    <title>积分设置</title>
    <link href="../styles/datagrid.css" type="text/css" rel="stylesheet" />
    <script type="text/javascript" src="../js/common.js"></script>
    <link href="../styles/dntmanager.css" type="text/css" rel="stylesheet" />
    <link href="../styles/modelpopup.css" type="text/css" rel="stylesheet" />
    <script type="text/javascript" src="../js/modalpopup.js"></script>
    <@c.validator "Form1" />
    <style type="text/css">
	    body{margin:0 20px;}
	</style>
</head>
<body>
    <form id="Form1" method="post">
    ${htmlBuilder}
        <@c.pageInfo id="info1" icon="Information"
        text="以下标明(+)的为增加的积分数, 标明(-)的为减少的积分数, 您也可以通过设置负值的方式变更积分的增减, 各项积分增减允许的范围为-999～+999. 如果为更多的操作设置积分策略, 系统就需要更频繁的更新用户积分, 同时意味着消耗更多的系统资源, 因此请根据实际情况酌情设置"/>
       <table cellspacing="0" cellpadding="4" width="100%" align="center">
            <tr>
                <td>
                    <table class="ntcplist" >

<tr class="head">

<td><img src='../images/icons/icon31.jpg'>积分设置</td>

</tr>

<tr>

<td>

<table class="datalist" cellspacing="0" rules="all" border="1" id="DataGrid1" style="border-collapse:collapse;">
	<tr class="category">
		<td nowrap="nowrap" colspan="1" style="border-color:#EAE9E1;border-width:1px;border-style:solid;">&nbsp;</td>
		<td nowrap="nowrap" style="border-color:#EAE9E1;border-width:1px;border-style:solid;">名称</td>
		<td nowrap="nowrap" style="border-color:#EAE9E1;border-width:1px;border-style:solid;">extcredits1</td>
		<td nowrap="nowrap" style="border-color:#EAE9E1;border-width:1px;border-style:solid;">extcredits2</td>
		<td nowrap="nowrap" style="border-color:#EAE9E1;border-width:1px;border-style:solid;">extcredits3</td>
		<td nowrap="nowrap" style="border-color:#EAE9E1;border-width:1px;border-style:solid;">extcredits4</td>
		<td nowrap="nowrap" style="border-color:#EAE9E1;border-width:1px;border-style:solid;">extcredits5</td>
		<td nowrap="nowrap" style="border-color:#EAE9E1;border-width:1px;border-style:solid;">extcredits6</td>
		<td nowrap="nowrap" style="border-color:#EAE9E1;border-width:1px;border-style:solid;">extcredits7</td>
		<td nowrap="nowrap" style="border-color:#EAE9E1;border-width:1px;border-style:solid;">extcredits8</td>
	</tr>
	<#list 0..12 as i>
	<tr onmouseover="this.className='mouseoverstyle'" onmouseout="this.className='mouseoutstyle'" style="cursor:hand;">
		<#if updateid?exists && updateid==i>
		<td nowrap="nowrap" style="border-color:#EAE9E1;border-width:1px;border-style:Solid;width:70px;">
		<input type="hidden" name="scoreID" value="${updateid}">
		<a href="javascript:doPost('updateScore')">更新</a>&nbsp;
		<a href="javascript:doPost('noUpdateScore');">取消</a>		
		</td>
		<td style="border-color:#EAE9E1;border-width:1px;border-style:solid;">${scoreSets[i][0]}</td>
		<input type="hidden" name="scoreName" value="${scoreSets[i][0]}">
		<#list 1..8 as j>		
		<td style="border-color:#EAE9E1;border-width:1px;border-style:Solid;">
		<input name="ct${i}_ct${j}" type="text" value="${scoreSets[i][j]}" onfocus="this.className='FormFocus';" onblur="this.className='FormBase';" class="FormBase" style="width:60px;" />
		</td>
		</#list>		
		<#else>
		<td nowrap="nowrap" style="border-color:#EAE9E1;border-width:1px;border-style:Solid;width:70px;">
		<a href="javascript:document.Form1.action="?updateid=${i}}";doPost('doEidtScore');">编辑</a>
		</td>
		<#list 0..8 as j>
		<td style="border-color:#EAE9E1;border-width:1px;border-style:solid;">${scoreSets[i][j]}</td>
		</#list>
		</#if>
	</tr>
	</#list>	
</table></td></tr></TABLE>
  </td>
            </tr>
            <tr>
                <td class="panelbox">
                    <table class="table1" cellspacing="0" cellpadding="4" width="100%" align="center">
                        <tr>
                            <td width="100">兑换比率</td>
                            <td>
                                兑换比率为单项积分对应一个单位标准积分的值, 例如 extcredits1 的比率为 1.5(相当于 1.5 个单位标准积分)、extcredits2 的比率为
                                3(相当于 3 个单位标准积分)、extcredits3 的比率为 15(相当于 15 个单位标准积分), 则 extcredits3 的 1 分相当于 extcredits2
                                的 5 分或 extcredits1 的 10 分. 一旦设置兑换比率, 则用户将可以在控制面板中自行兑换各项设置了兑换比率的积分, 如不希望实行积分自由兑换,
                                请将其兑换比率设置为 0
                            </td>
                        </tr>
                        <tr>
                            <td>积分名称</td>
                            <td>该项积分的名称, 如果为空则不启用该项积分显示</td>
                        </tr>
                        <tr>
                            <td>积分单位</td>
                            <td>如金币,元等</td>
                        </tr>
                        <tr>
                            <td>发主题(+)</td>
                            <td>作者发新主题增加的积分数, 如果该主题被删除, 作者积分也会按此标准相应减少</td>
                        </tr>
                        <tr>
                            <td>回复(+)</td>
                            <td>作者发新回复增加的积分数, 如果该回复被删除, 作者积分也会按此标准相应减少</td>
                        </tr>
                        <tr>
                            <td>加精华(+)</td>
                            <td>主题被加入精华时单位级别作者增加的积分数(根据精华级别乘以1～3), 如果该主题被移除精华, 作者积分也会按此标准相应减少</td>
                        </tr>
                        <tr>
                            <td>上传附件(+)</td>
                            <td>用户每上传一个附件增加的积分数, 如果该附件被删除, 发布者积分也会按此标准相应减少</td>
                        </tr>
                        <tr>
                            <td>下载附件(-)</td>
                            <td>用户每下载一个附件扣除的积分数. 注意: 如果允许游客组下载附件, 本策略将可能被绕过</td>
                        </tr>
                        <tr>
                            <td>发短消息(-)</td>
                            <td>用户每发送一条短消息扣除的积分数</td>
                        </tr>
                        <tr>
                            <td>搜索(-)</td>
                            <td>用户每进行一次帖子搜索或短消息搜索扣除的积分数</td>
                        </tr>
                        <tr>
                            <td>交易成功(+)</td>
                            <td>用户每成功进行一次交易后增加的积分数</td>
                        </tr>
                        <tr>
                            <td>参与投票(+)</td>
                            <td>用户每参与一次投票后增加的积分数</td>
                        </tr>
                    </table>
                </td>
            </tr>
        </table>
        <div class="ManagerForm">
            <fieldset>
                <legend style="background: url(../images/icons/icon25.jpg) no-repeat 6px 50%;">积分设置</legend>
                    <table cellspacing="0" cellpadding="4" width="100%" align="center">
                        <tr>
                            <td colspan="2">
                                <table width="100%">
                                    <tr>
                                        <td style="width: 145px">&nbsp;&nbsp;总积分计算公式:</td>
                                        <td>
                                            <@c.textBox id="formula" value="${formula}" multiLine="true"
                                                hintInfo="总积分是衡量用户级别的唯一标准, 您可以在此设定用户的总积分计算公式, 其中 posts 代表发帖数;digestposts 代表精华帖数;oltime 代表用户总在线时间(分钟);extcredits1～extcredits8 分别代表上述 8 个自定义积分. 公式中可使用包括 + - * / () 在内的运算符号, 例如&amp;quot;<i><u>posts*0.5+digestposts*10+oltime*10+extcredits1*2+extcredits8</u></i>&amp;quot;代表总积分为&amp;quot;<i><u>发帖数</u></i>*0.5+<i><u>精华帖数</u></i>*10+<i><u>总在线时间(分钟)</u></i>*10+<i><u>自定义积分1</u></i>*2+<i><u>自定义积分8</u></i>&amp;quot;. 注意: 一旦修改积分公式, 将可能导致所有用户的积分和所在会员用户组重新计算, 因此会加重服务器负担, 直至全部用户更新完毕. 其中在线时间,用户可以通过长时间联机刷新而作弊, 请慎用"
                                                hintTitle="提示" hintShowType="down" width="90%"></@c.textBox>
                                            <div class="countor">
                                                <@c.checkBoxList id="RefreshUserScore">
                                                    <@c.checkBoxItem id="RefreshUserScore" value="1">根据该公式刷新所有用户总积分</@c.checkBoxItem>
                                                </@c.checkBoxList>
                                            </div>
                                        </td>
                                    </tr>
                                </table>
                            </td>
                        </tr>
                        <tr>
                            <td  class="panelbox" width="50%" align="left">
                                <table width="100%">
                                    <tr>
                                        <td style="width: 120px">交易积分设置:</td>
                                        <td>
                                            <@c.dropDownList id="creditstrans" hintInfo="交易积分是一种可以由用户间自行转让、买卖交易的积分类型, 您可以指定一种积分作为交易积分. 如果不指定交易积分, 则用户间积分交易功能将不能使用. 注意: 交易积分必须是已启用的积分, 一旦确定请尽量不要更改, 否则以往记录及交易可能会产生问题."
                                                hintTitle="提示">
                                                <@c.dropDownItem value="0" selected="${creditstrans}">无</@c.dropDownItem>
                                                <@c.dropDownItem value="1" selected="${creditstrans}">extcredits1</@c.dropDownItem>
                                                <@c.dropDownItem value="2" selected="${creditstrans}">extcredits2</@c.dropDownItem>
                                                <@c.dropDownItem value="3" selected="${creditstrans}">extcredits3</@c.dropDownItem>
                                                <@c.dropDownItem value="4" selected="${creditstrans}">extcredits4</@c.dropDownItem>
                                                <@c.dropDownItem value="5" selected="${creditstrans}">extcredits5</@c.dropDownItem>
                                                <@c.dropDownItem value="6" selected="${creditstrans}">extcredits6</@c.dropDownItem>
                                                <@c.dropDownItem value="7" selected="${creditstrans}">extcredits7</@c.dropDownItem>
                                                <@c.dropDownItem value="8" selected="${creditstrans}">extcredits8</@c.dropDownItem>
                                            </@c.dropDownList>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>转账最低余额:</td>
                                        <td>
                                            <@c.textBox id="transfermincredits" number="true" required="true"
                                                size="5" maxlength="4" hintInfo="积分转账后要求用户所拥有的余额最小数值. 利用此功能, 您可以设置较大的余额限制, 使积分小于这个数值的用户无法转账;也可以将余额限制设置为负数, 使得转账在限额内可以透支"
                                                hintTitle="提示" value="${transfermincredits}" width="50"></@c.textBox><br />
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>单主题最高收入:</td>
                                        <td>
                                            <@c.textBox id="maxincperthread"  number="true" required="true"
                                                size="5" maxlength="4" hintInfo="设置单一主题作者出售所得的最高税后积分收入, 超过此限制后购买者将仍然被扣除相应积分, 但主题作者收益将不再上涨. 本限制只在主题买卖时起作用, 0 为不限制"
                                                hintTitle="提示" value="${maxincperthread}" width="50"></@c.textBox><br />
                                        </td>
                                    </tr>
                                </table>
                            </td>
                            <td  class="panelbox" width="50%" align="right">
                                <table width="100%">
                                    <tr>
                                        <td style="width: 120px">积分交易税:</td>
                                        <td>
                                            <@c.textBox id="creditstax" number="true" required="true"
                                                width="50" size="5" maxlength="5" hintInfo="积分交易税(损失率)为用户在利用积分进行转让、兑换、买卖时扣除的税率, 范围为 0～1 之间的浮点数, 例如设置为 0.2, 则用户在转换 100 个单位积分时, 损失掉的积分为 20 个单位, 0 为不损失"
                                                hintTitle="提示" value="${creditstax}"></@c.textBox><br />
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>兑换最低余额:</td>
                                        <td>
                                            <@c.textBox id="exchangemincredits" number="true" required="true"
                                                size="5" maxlength="4" hintInfo="积分兑换后要求用户所拥有的余额最小数值. 利用此功能, 您可以设置较大的余额限制, 使积分小于这个数值的用户无法兑换;也可以将余额限制设置为负数, 使得兑换在限额内可以透支"
                                                hintTitle="提示" value="${exchangemincredits}" width="50"></@c.textBox><br />
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>单主题最高出售时限:</td>
                                        <td>
                                            <@c.textBox id="maxchargespan" number="true" required="true"
                                                size="5" maxlength="4" hintInfo="设置当主题被作者出售时, 系统允许自主题发布时间起, 其可出售的最长时间. 超过此时间限制后将变为普通主题, 阅读者无需支付积分购买, 作者也将不再获得相应收益, 以小时为单位, 0 为不限制"
                                                hintTitle="提示" value="${maxchargespan}" width="50"></@c.textBox>(单位:小时)<br />
                                        </td>
                                    </tr>
                                </table>
                            </td>
                        </tr>
                        <tr>
                            <td colspan="2" align="center">
                                <@c.button id="Save" text="提 交" ></@c.button>
                            </td>
                        </tr>
                    </table>
            </fieldset>
        </div>
        <@c.hint hintImageUrl="../images" />
    </form>
    ${footer}
</body>
</html>
