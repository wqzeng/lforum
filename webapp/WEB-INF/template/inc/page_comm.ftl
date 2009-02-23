<#-- 
	描述：公共宏定义 
	作者：黄磊 
	版本：v1.0 
-->
<#-- 页面head部分 -->
<#macro pageHeader>
<#include "commfiles/_pageHeader.ftl"/>
</#macro>

<#-- 页面顶部定义 -->
<#macro header>
<@pageHeader />
<#include "commfiles/_header.ftl"/>
</#macro>

<#-- 底部版权声明 -->
<#macro copyright>
<#include "commfiles/_copyright.ftl"/>
</#macro>

<#-- 页面底部 -->
<#macro footer>
</div>
</body>
</html>
</#macro>

<#-- 显示错误信息 -->
<#macro errmsgbox>
<#include "commfiles/_errmsgbox.ftl"/>
</#macro>

<#-- 显示提示信息 -->
<#macro msgbox>
<#include "commfiles/_msgbox.ftl"/>
</#macro>

<#-- 底部广告定义 -->
<#macro adlist>
<#include "commfiles/_adlist.ftl"/>
</#macro>

<#-- 快速搜索定义 -->
<#macro quicksearch>
<#include "commfiles/_quicksearch.ftl"/>
</#macro>

<#-- 热门标签定义 -->
<#macro hottagbox>
<#include "commfiles/_hottagbox.ftl"/>
</#macro>

<#-- 真实姓名定义 -->
<#macro realnamesystem>
<#include "commfiles/_realnamesystem.ftl"/>
</#macro>

<#-- 编辑器定义 -->
<#macro editor>
<#include "commfiles/_editor.ftl"/>
</#macro>

<#-- AJAX快速回复定义 -->
<#macro ajaxquickreply>
<#include "commfiles/_ajaxquickreply.ftl"/>
</#macro>

<#-- 附件信息定义 -->
<#macro attachmentinfo>
<#include "commfiles/_attachmentinfo.ftl"/>
</#macro>

<#-- 附件菜单定义 -->
<#macro attachmentmenu>
<#include "commfiles/_attachmentmenu.ftl"/>
</#macro>

<#-- 论坛设置显示定义 -->
<#macro forumsetmenu>
<#include "commfiles/_forumsetmenu.ftl"/>
</#macro>

<#-- 快速登录定义 -->
<#macro login>
<#include "commfiles/_login.ftl"/>
</#macro>

<#-- 论坛短消息定义 -->
<#macro newpmmsgbox>
<#include "commfiles/_newpmmsgbox.ftl"/>
</#macro>

<#-- 页面文章广告定义 -->
<#macro pagewordadlist>
<#include "commfiles/_pagewordadlist.ftl"/>
</#macro>

<#-- 投票页面定义 -->
<#macro poll>
<#include "commfiles/_poll.ftl"/>
</#macro>

<#-- 发表附件定义 -->
<#macro postattachments>
<#include "commfiles/_postattachments.ftl"/>
</#macro>

<#-- 快速留言定义 -->
<#macro postleaveword>
<#include "commfiles/_postleaveword.ftl"/>
</#macro>

<#-- 快速主题定义 -->
<#macro quickpost>
<#include "commfiles/_quickpost.ftl"/>
</#macro>

<#-- 快速回复定义 -->
<#macro quickreply>
<#include "commfiles/_quickreply.ftl"/>
</#macro>

<#-- 子板块列表定义 -->
<#macro subforum>
<#include "commfiles/_subforum.ftl"/>
</#macro>

<#-- 随机验证码定义 -->
<#macro vcode>
<#include "commfiles/_vcode.ftl"/>
</#macro>

<#-- 发帖临时账号登录定义 -->
<#macro tempaccounts>
<#include "commfiles/_tempaccounts.ftl"/>
</#macro>

<#-- 帖子评分记录定义 -->
<#macro ratelog>
<#include "commfiles/_ratelog.ftl"/>
</#macro>

<#macro report>
<#include "commfiles/_report.ftl"/>
</#macro>

<#-- 用户中心菜单定义 -->
<#macro menu>
<#include "commfiles/_menu.ftl"/>
</#macro>

<#-- 用户中心错误提示定义 -->
<#macro usercperrmsgbox>
<#include "commfiles/_usercperrmsgbox.ftl"/>
</#macro>

<#-- 个人设置菜单定义 -->
<#macro permenu>
<#include "commfiles/_permenu.ftl"/>
</#macro>

<#-- 短信菜单定义 -->
<#macro smsmenu>
<#include "commfiles/_smsmenu.ftl"/>
</#macro>

<#-- 收藏菜单定义 -->
<#macro subscribemenu>
<#include "commfiles/_subscribemenu.ftl"/>
</#macro>

<#-- 积分交易菜单定义 -->
<#macro scoremenu>
<#include "commfiles/_scoremenu.ftl"/>
</#macro>