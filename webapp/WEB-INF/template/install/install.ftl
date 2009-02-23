<#import "install-comm.ftl" as comm />
<@comm.header title="安装向导--许可协议" />
 <script type="text/javascript">
	$(document).ready(function(){
		$("#javaform").validate({
			rules:{
				aggre:{required:true}
			},
			messages:{
				aggre:{required:"必须同意协议才能继续"}
			}
		});
	});
</script>
<body >
    <div class="top"></div>
    <div class="con">
        <form name="javaform" method="get" action="${path}install/install!setConn.action" id="javaform">
		<@s.token />
<div>
</div>
            <table cellspacing="0" cellpadding="0" border="0" id="WzdInstall" style="width:100%;border-collapse:collapse;">
	<tr style="height:100%;">
		<td>
                        <div id="WzdInstall_DivDefault" class="left140">
                            <dl>
                                <dt class="title"><span>欢迎安装LForum For Java</span></dt>
                                <dd class="message">
                                    欢迎您选择安装LForum For Java Program！<br />
                                    本向导将协助您一步步的安装此软件。<br />
                                    建议您在运行本向导前仔细阅读程序包中的《安装说明》文档，如果您已经阅读过，请点击下一步。<br />
                                    <table width="82%" border="0" style="text-align: left;" cellpadding="1" cellspacing="1" class="border">
                                        <tr class='tdbg'>
                                            <td style="height: 18px" align="center">
                                                <strong>阅读许可协议</strong></td>
                                        </tr>
                                        <tr class="tdbg">
                                            <td align="center">
<textarea style="width: 99%; height: 180px" cols="100" rows="12" readonly="readonly">
欢迎安装LForum For Java 用户许可协议

版权所有（C） LForum For Java 2008

寂寞地铁-Lonlysky 为 <LForum For Java>的开发商，依法独立拥有<LForum For Java>的所有著作权。

需仔细阅读本许可协议，在理解、同意、并遵守本许可协议的全部条件和条款后，方可开始使用<LForum For Java>。

本许可协议适用于且仅适用于<LForum For Java>，寂寞地铁-Lonlysky拥有对本使用许可协议的最终解释权。下面提到的“本软件”均系指<LForum For Java>。“基于本软件的衍生著作”均系指基于<LForum For Java>而产生的任何的衍生著作，例如对<LForum For Java>进行翻译或是修改而产生的软件著作。

有关本软件的用户许可协议、商业授权与技术服务的详细内容，均由寂寞地铁-Lonlysky独家提供。寂寞地铁-Lonlysky拥有在不事先通知的情况下，修改许可协议和服务价目表的权力，修改后的协议或价目表对自改变之日起的新授权用户生效。 

电子文本形式的许可协议如同双方书面签署的协议一样，具有完全的和等同的法律效力。您一旦开始确认本协议并安装、使用、修改或分发本软件（或任何基于本软件的衍生著作），则表示您已经完全接受本许可协议的所有的条件和条款。如果您有任何违反本许可协议的行为，作者有权收回对您的许可授权，责令停止损害，并追究您的相关法律及经济责任。

1、许可
1.1	本软件仅供给个人用户非商业使用。如果您是个人用户，那么您可以在完全遵守本用户许可协议的基础上，将本软件应用于非商业用途，而不必支付软件授权许可费用。 
1.2	您可以在本协议规定的约束和限制范围内修改本软件的源代码和界面风格以适应您的要求。
1.3	您可以在本协议规定的约束和限制范围内通过任何的媒介和渠道复制与分发本软件的源代码的副本（要求是逐字拷贝的副本）。
1.4	您可以去除本软件在模板（不论是前台还是后台模板）中的动易著作权信息，但您不得去除源代码中的动易著作权信息，且必须完整保留本软件中的License.txt文件，并维持文件内容及位置的原样。
1.5	您拥有使用本软件构建的网站全部内容所有权，并独立承担与这些内容的相关法律义务。
1.6	在获得商业授权之后，您可以将本软件应用于商业用途。 

3、无担保及免责声明
3.1	用户出于自愿而使用本软件，您必须了解使用本软件的风险，且同意自己承担使用本软件的风险。
3.2	用户利用本软件构建的网站的任何信息内容以及导致的任何版权纠纷和法律争议及后果与寂寞地铁-Lonlysky无关，寂寞地铁-Lonlysky对此不承担任何责任。
3.3	在适用法律允许的最大范围内，寂寞地铁-Lonlysky在任何情况下不就因使用或不能使用本软件所发生的特殊的、意外的、非直接或间接的损失承担赔偿责任（包括但不限于，资料损失，资料执行不精确，或应由您或第三人承担的损失，或本程序无法与其他程序运作等）。即使用户已事先被寂寞地铁-Lonlysky告知该损害发生的可能性。</textarea> </td>
                                        </tr>
                                        <tr class="tdbg">
                                            <td align='left'>
                      <input id="aggre" type="checkbox" name="aggre" value="true"/>
                                                <label for="ChlkAgreeLicense">
                                                    我已经阅读并同意此协议</label></td>
                                        </tr>
                                    </table>
                                </dd>
                            </dl>
                        </div>
                        <div class="left140">
                            <div class="left140_cen">
                          <input type="submit" value="下一步" class="button_link" />
                            </div>
                        </div>
        </td>
	</tr><tr>
		<td align="right">
                    <center>
                    </center>
        </td>
	</tr>
</table>
        
<div>
</div></form>
    </div>
<@comm.bottom />
</body>
</html>
