<#-- 
	描述：用户中心论坛设置模板
	作者：黄磊 
	版本：v1.0 
-->
<#import "inc/page_comm.ftl" as comm />
<@comm.header/>
<!--header end-->

<div id="foruminfo">
	<div id="nav">
		<a href="${config.forumurl}" class="home">${config.forumtitle}</a>  &raquo; <a href="usercp.action">用户中心</a>  &raquo; <strong>论坛设置</strong>
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
				<@comm.forumsetmenu/>	
				<div class="pannelbody">
					<div class="pannellist">
					<#if reqcfg.page_err==0>
						<#if ispost>
							<@comm.msgbox/>
						<#else>
						<form action="" method="post" ID="Form1">
							<label for="tpp"  class="labellong2" style="line-height:180%;">每页主题数:</label>
							<input type="radio" value="0" name="tpp"
							<#if user.tpp==0>
							  checked="checked"
							</#if>/>默认&nbsp;&nbsp;&nbsp;
							<input type="radio" value="15" name="tpp"
							<#if user.tpp==15>
							  checked="checked"
							</#if>/>15&nbsp;&nbsp;&nbsp;
							<input type="radio" value="20" name="tpp"
							<#if user.tpp==20>
							  checked="checked"
							</#if>/>20&nbsp;&nbsp;&nbsp;
							<input type="radio" value="25" name="tpp"
							<#if user.tpp==25>
							  checked="checked"
							</#if>/>25&nbsp;&nbsp;&nbsp;
							<br />
							<div class="compartline">&nbsp;</div>
							<label for="ppp"  class="labellong2" style="line-height:180%;">每页帖子数:</label>
							<input type="radio" value="0" name="ppp"
							  <#if user.ppp==0>
							  checked="checked"
							  </#if>/>默认&nbsp;&nbsp;&nbsp;
							<input type="radio" value="10" name="ppp"
							  <#if user.ppp==10>
							  checked="checked"
							  </#if>/>10&nbsp;&nbsp;&nbsp;
							<input type="radio" value="15" name="ppp"
							  <#if user.ppp==15>
							  checked="checked"
							  </#if>/>15&nbsp;&nbsp;&nbsp;
							<input type="radio" value="20" name="ppp"
							  <#if user.ppp==20>
							  checked="checked"
							  </#if>/>20&nbsp;&nbsp;&nbsp;
							<br />
							<div class="compartline">&nbsp;</div>
							<label for="invisible" class="labellong2" style="line-height: 180%;">是否隐身:</label>
							<input type="radio" name="invisible" value="1" class="radioinput" 
								<#if user.invisible==1>
								checked="checked"
								</#if>
								 ID="Radio3"/>是
							<input name="invisible" type="radio" value="0" class="radioinput"  
								<#if user.invisible==0>
								checked="checked"
								</#if>
								 ID="Radio4"/>否
							<br />
							<div class="compartline">&nbsp;</div>
							<label for="signature">签名:</label>
							<textarea name="signature" cols="60" rows="8" id="signature">${user.userfields.signature}</textarea>
							<br />
							<label for="sigstatus">&nbsp;</label>
							<input name="sigstatus" type="checkbox" id="sigstatus" value="1" 
								<#if user.sigstatus==1>						
									checked="checked"
								</#if>							
								/>使用签名
							<br />
							<label for="newpassword2">&nbsp;</label>
							UBB代码: <strong>
									<#if usergroupinfo.allowsigbbcode==1>
										可用
									<#else>
										禁用
									</#if>
									</strong>&nbsp;&nbsp;
									[img]代码: <strong>
									<#if usergroupinfo.allowsigimgcode==1>
										可用
									<#else>
										禁用
									</#if>
									</strong>
							<br />
							<div class="compartline">&nbsp;</div>
							<input type="submit" name="Submit" value="确定" ID="Submit1"/>
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
