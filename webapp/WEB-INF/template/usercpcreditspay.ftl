<#-- 
	描述：用户中心积分交易模板
	作者：黄磊 
	版本：v1.0 
-->
<#import "inc/page_comm.ftl" as comm />
<@comm.header/>
<!--header end-->
<div id="foruminfo">
	<div id="nav">
		<a href="${config.forumurl}" class="home">${config.forumtitle}</a>  &raquo; <a href="usercp.action">用户中心</a>  &raquo; <strong>积分兑换</strong>
	</div>
	<div id="headsearch">
		<div id="search">
			<@comm.quicksearch/>
		</div>
	</div>
</div>

<!--主体-->
<div class="controlpannel">
   <@comm.menu/>
	<div class="pannelcontent">
		<div class="pcontent">
			<div class="panneldetail">
				<@comm.scoremenu/>	
				<div class="pannelbody">
					<div class="pannellist">
					<#if reqcfg.page_err==0>
					${jscreditsratearray}
						<#if ispost>
							<@comm.msgbox/>
						<#else>
						<form action="" method="post" ID="Form1">
						<ul>
							<li class="paychange"><strong>当前帐户</strong></li>
							<li class="paychange">
								<#if score[1]!="">
								${score[1]}: <em>${user.extcredits1}</em>&nbsp;&nbsp;
								</#if>
								<#if score[2]!="">
								${score[2]}: <em>${user.extcredits2}</em>&nbsp;&nbsp;
								</#if>
								<#if score[3]!="">
								${score[3]}: <em>${user.extcredits3}</em>&nbsp;&nbsp;
								</#if>
								<#if score[4]!="">
								${score[4]}: <em>${user.extcredits4}</em>&nbsp;&nbsp;
								</#if>
								<#if score[5]!="">
								${score[5]}: <em>${user.extcredits5}</em>&nbsp;&nbsp;
								</#if>
								<#if score[6]!="">
								${score[6]}: <em>${user.extcredits6}</em>&nbsp;&nbsp;
								</#if>
								<#if score[7]!="">
								${score[7]}: <em>${user.extcredits7}</em>&nbsp;&nbsp;
								</#if>
								<#if score[8]!="">
								${score[8]}: <em>${user.extcredits8}</em>&nbsp;&nbsp;
								</#if>
							</li>
						</ul>
						进行兑换操作: 将数量&nbsp;
						<input name="paynum" type="text" id="paynum" value="100" size="10" />&nbsp;的&nbsp;
						<select name="extcredits1" id="extcredits1" style="width:120px;">
						<option value="0">请选择</option>
						<#list extcreditspaylist as extcredits>
						<option value="${extcredits.id}">${extcredits.name}</option>
						</#list>
						</select>&nbsp;转换为&nbsp;<select name="extcredits2" id="extcredits2"  style="width:120px;">
						<option value="">请选择</option>
						<#list extcreditspaylist as extcredits2>
						<option value="${extcredits2.id}">${extcredits2.name}</option>
						</#list>
                        </select>
						<br/><br/>
						验证用户密码: <input name="password" type="password" id="password" size="20"/>
						<div class="hintinfo">兑换时将根据论坛当前设置(${creditstax})扣除交易税</div>
						<input type="submit" name="Submit" value="确定" ID="Submit1"/>
						&nbsp;&nbsp;
						<input type="button" name="Input" value=" 计 算 " onclick="if(this.form.extcredits1.options[this.form.extcredits1.selectedIndex].value != 0 && this.form.extcredits2.options[this.form.extcredits2.selectedIndex].value != 0){alert('接收者的所得为 '+this.form.extcredits2.options[this.form.extcredits2.selectedIndex].text + ':'+Math.round(this.form.paynum.value*(creditsrate[this.form.extcredits1.options[this.form.extcredits1.selectedIndex].value]/creditsrate[this.form.extcredits2.options[this.form.extcredits2.selectedIndex].value] *(1-${creditstax}))*100)/100)}else{alert('请完整填写和选择进行兑换的积分!')}"/>
						</form>
						</#if>
						</div>
					<#else>
					<@comm.usercperrmsgbox/>
					</#if>
			  </div>
			</div>
		</div>
	</div>
</div>
<!--主体-->
</div>
<@comm.copyright/>
<@comm.footer/>
