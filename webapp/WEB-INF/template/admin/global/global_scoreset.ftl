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
        <!-- <@c.pageInfo id="info1" icon="Information"
        text="以下标明(+)的为增加的积分数, 标明(-)的为减少的积分数, 您也可以通过设置负值的方式变更积分的增减, 各项积分增减允许的范围为-999～+999. 如果为更多的操作设置积分策略, 系统就需要更频繁的更新用户积分, 同时意味着消耗更多的系统资源, 因此请根据实际情况酌情设置"/>
        -->
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
