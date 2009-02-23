package com.javaeye.lonlysky.lforum.entity.global;

import java.io.Serializable;

import com.javaeye.lonlysky.lforum.comm.utils.Utils;

/**
 * 程序基本设置描述类
 * 
 * @author 黄磊
 *
 */
public class Config implements Serializable {

	private static final long serialVersionUID = -2490618145966816150L;

	private String datatype; //数据库类型
	private String webpath; //系统物理路径	
	private int install; //是否安装	
	private String forumtitle; //论坛名称
	private String forumurl; //论坛url地址
	private String webtitle; //网站名称
	private String weburl; //论坛网站url地址
	private String icp; //网站备案信息
	private int closed; //论坛关闭
	private String closedreason; //论坛关闭提示信息
	private int isframeshow; //是否以框架方式显示  1是　0不是
	private String linktext; //外部链接html
	private String statcode; //统计代码

	private String passwordkey; //用户密码Key

	private int regstatus; //是否允许新用户注册
	private int regadvance; //注册时候是否显示高级选项
	private int realnamesystem; //注册时是否启用实名制
	private String censoruser; //用户信息保留关键字
	private int doublee; //允许同一 Email 注册不同用户
	private int regverify; //新用户注册验证
	private String accessemail; //Email允许地址
	private String censoremail; //Email禁止地址
	private int hideprivate; //隐藏无权访问的论坛
	private int regctrl; //IP 注册间隔限制(小时)
	private String ipregctrl; //特殊 IP 注册限制
	private String ipdenyaccess; //IP禁止访问列表
	private String ipaccess; //IP访问列表
	private String adminipaccess; //管理员后台IP访问列表
	private int newbiespan; //新手见习期限(单位:分钟)
	private int welcomems; //发送欢迎短消息
	private String welcomemsgtxt; //欢迎短消息内容
	private int rules; //是否显示注册许可协议
	private String rulestxt; //许可协议内容
	private int secques; //是否启用用户登录安全提问

	private int templateid; //默认论坛风格
	private int hottopic; //热门话题最低贴数
	private int starthreshold; //星星升级阀值
	private int visitedforums; //显示最近访问论坛数量
	private int maxsigrows; //最大签名高度(行)
	private int moddisplay; //版主显示方式 0=平面显示 1=下拉菜单
	private int subforumsindex; //首页是否显示论坛的下级子论坛
	private int stylejump; //显示风格下拉菜单
	private int fastpost; //快速发帖
	private int showsignatures; //是否显示签名
	private int showavatars; //是否显示头像
	private int showimages; //是否在帖子中显示图片
	private int smiliesmax; //帖子中最大允许的表情符数量

	private int archiverstatus; //启用 Archiver
	private String seotitle; //标题附加字
	private String seokeywords; //Meta Keywords
	private String seodescription; //Meta Description
	private String seohead; //其他头部信息

	private int rssstatus; //rssstatus
	private int rssttl; //RSS TTL(分钟)
	private int sitemapstatus; //Sitemap是否开启
	private int sitemapttl; //Sitemap TTL(小时)
	private int nocacheheaders; //禁止浏览器缓冲
	private int fullmytopics; //我的话题全文搜索 0=只搜索用户是主题发表者的主题 1=搜索用户是主题发表者或回复者的主题

	private int whosonlinestatus; //显示在线用户 0=不显示 1=仅在首页显示 2=仅在分论坛显示 3=在首页和分论坛显示
	private int maxonlinelist; //最多显示在线人数
	private int userstatusby; //衡量并显示用户头衔
	private int forumjump; //显示论坛跳转菜单
	private int modworkstatus; //论坛管理工作统计
	private int maxmodworksmonths; //管理记录保留时间(月)

	private String seccodestatus; //使用验证码的页面列表,用","分隔
	private int guestcachepagetimeout; // 缓存游客页面的失效时间, 为0则不缓存, 大于0则缓存该值的时间(单位:分钟)
	private int topiccachemark; //缓存游客查看主题页面的权重, 为0则不缓存, 范围0 - 100
	private int maxonlines; //最大在线人数
	private int postinterval; //发帖灌水预防(秒)
	private int searchctrl; //搜索时间限制(秒)
	private int maxspm; //60 秒最大搜索次数

	private String visitbanperiods; //禁止访问时间段	
	private String postbanperiods; //禁止发帖时间段
	private String postmodperiods; //发帖审核时间段
	private String attachbanperiods; //禁止下载附件时间段
	private String searchbanperiods; //禁止全文搜索时间段

	private int memliststatus; //允许查看会员列表
	private int dupkarmarate; //允许重复评分
	private int minpostsize; //帖子最小字数(字节)
	private int maxpostsize; //帖子最大字数(字节)
	private int tpp; //每页主题数
	private int ppp; //每页帖子数
	private int maxfavorites; //收藏夹容量
	private int maxavatarsize; //头像最大尺寸(字节)
	private int maxavatarwidth; //头像最大宽度(像素)
	private int maxavatarheight; //头像最大高度(像素)
	private int maxpolloptions; //投票最大选项数
	private int maxattachments; //最大允许的上传附件数

	private int attachimgpost; //帖子中显示图片附件
	private int attachrefcheck; //下载附件来路检查
	private int attachsave; //附件保存方式  0=按年/月/日存入不同目录 1=按年/月/日/论坛存入不同目录 2=按论坛存入不同目录 3=按文件类型存入不同目录
	private int watermarkstatus; //图片附件添加水印 0=不使用 1=左上 2=中上 3=右上 4=左中 ... 9=右下
	private int watermarktype; //图片附件添加何种水印 0=文字 1=图片
	private int watermarktransparency; //图片水印透明度 取值范围1--10 (10为不透明)
	private String watermarktext; //图片附件添加文字水印的内容 {1}表示论坛标题 {2}表示论坛地址 {3}表示当前日期 {4}表示当前时间, 例如: {3} {4}上传于{1} {2}
	private String watermarkpic; //使用的水印图片的名称
	private String watermarkfontname; //图片附件添加文字水印的字体
	private int watermarkfontsize; //图片附件添加文字水印的大小(像素)
	private int showattachmentpath; //图片附件如果直接显示, 地址是否直接使用图片真实路径
	private int attachimgquality; //是否是高质量图片 取值范围0--100
	private int attachimgmaxheight; //附件图片最大高度
	private int attachimgmaxwidth; //附件图片最大宽度

	private int reasonpm; //是否将管理操作的理由短消息通知作者
	private int moderactions; //是否在主题查看页面显示管理操作
	private int karmaratelimit; //评分时间限制(小时)
	private int losslessdel; //删贴不减积分时间期限(天)
	private int edittimelimit; //编辑帖子时间限制(分钟)
	private int editedby; //编辑帖子附加编辑记录
	private int defaulteditormode; //默认的编辑器模式 0=ubb代码编辑器 1=可视化编辑器
	private int allowswitcheditor; //是否允许切换编辑器模式
	private int smileyinsert; //显示可点击表情
	private String cookiedomain;//身份验证Cookie域

	private int onlinetimeout; //多久无动作视为离线

	private int topicqueuestats; //是否开启主题统计队列功能
	private int topicqueuestatscount; //主题统计队列长度(浏览量)

	private int displayratecount; //评分记录现实的最大数量

	private String reportusergroup; //举报报告用户组
	private String photomangegroups; //图片管理用户组

	private String ratevalveset; //评分阀值;

	private int viewnewtopicminute; //设置前台"查看新帖"的分钟数

	private int htmltitle; //是否使用html标题
	private String htmltitleusergroup; //可以使用html标题的用户组
	private int specifytemplate; //版块是否指定模板 (0:为未指定)
	private int mytopicsavetime;//我的主题保留时间
	private int mypostsavetime;//我的帖子保留时间        
	private int myattachmentsavetime;//我的附件保留时间
	private int enabletag;//是否启用Tag功能        
	private int enablemall;//是否开启商场 (0:不开启 1:开启 2:开启高级模式)
	private int statscachelife;//统计缓存时间(分钟)
	private int statstatus;//是否开启浏览统计
	private int pvfrequence; //页面访问量更新频率(页面数)
	private int oltimespan;//用户在线时间更新时长(分钟):

	private String recommenddebates = "";

	private int gpp;//每页商品数

	private int debatepagesize;
	private int hottagcount; //首页热门标签数量
	private int disablepostad; //新用户广告强力屏蔽(0:不开启 1:开启)
	private int disablepostadregminute; //用户注册N分钟内进行新用户广告强力屏蔽功能检查,默认60分钟
	private int disablepostadpostcount; //用户发帖N帖内进行新用户广告强力屏蔽功能检查,默认5帖
	private String disablepostadregular; //新用户广告强力屏蔽功能正则表达式
	private int whosonlinecontract; //在线列表是否隐藏游客: 1 是 0 否
	private String forumcopyright; // 版权信息(只读)

	public String getForumcopyright() {
		forumcopyright = "&copy; 2009-" + Utils.dateFormat("yyyy")
				+ " <a href=\"http://lonlysky.javaeye.com\" target=\"_blank\">Lonlysky</a>.";
		return forumcopyright;
	}

	public String getDatatype() {
		return datatype;
	}

	public void setDatatype(String datatype) {
		this.datatype = datatype;
	}

	public String getWebpath() {
		return webpath;
	}

	public void setWebpath(String webpath) {
		this.webpath = webpath;
	}

	public int getInstall() {
		return install;
	}

	public void setInstall(int install) {
		this.install = install;
	}

	public String getForumtitle() {
		return forumtitle;
	}

	public void setForumtitle(String forumtitle) {
		this.forumtitle = forumtitle;
	}

	public String getForumurl() {
		return forumurl;
	}

	public void setForumurl(String forumurl) {
		this.forumurl = forumurl;
	}

	public String getWebtitle() {
		return webtitle;
	}

	public void setWebtitle(String webtitle) {
		this.webtitle = webtitle;
	}

	public String getWeburl() {
		return weburl;
	}

	public void setWeburl(String weburl) {
		this.weburl = weburl;
	}

	public String getIcp() {
		return icp;
	}

	public void setIcp(String icp) {
		this.icp = icp;
	}

	public int getClosed() {
		return closed;
	}

	public void setClosed(int closed) {
		this.closed = closed;
	}

	public String getClosedreason() {
		return closedreason;
	}

	public void setClosedreason(String closedreason) {
		this.closedreason = closedreason;
	}

	public int getIsframeshow() {
		return isframeshow;
	}

	public void setIsframeshow(int isframeshow) {
		this.isframeshow = isframeshow;
	}

	public String getLinktext() {
		return linktext;
	}

	public void setLinktext(String linktext) {
		this.linktext = linktext;
	}

	public String getStatcode() {
		return statcode;
	}

	public void setStatcode(String statcode) {
		this.statcode = statcode;
	}

	public String getPasswordkey() {
		return passwordkey;
	}

	public void setPasswordkey(String passwordkey) {
		this.passwordkey = passwordkey;
	}

	public int getRegstatus() {
		return regstatus;
	}

	public void setRegstatus(int regstatus) {
		this.regstatus = regstatus;
	}

	public int getRegadvance() {
		return regadvance;
	}

	public void setRegadvance(int regadvance) {
		this.regadvance = regadvance;
	}

	public int getRealnamesystem() {
		return realnamesystem;
	}

	public void setRealnamesystem(int realnamesystem) {
		this.realnamesystem = realnamesystem;
	}

	public String getCensoruser() {
		return censoruser;
	}

	public void setCensoruser(String censoruser) {
		this.censoruser = censoruser;
	}

	public int getDoublee() {
		return doublee;
	}

	public void setDoublee(int doublee) {
		this.doublee = doublee;
	}

	public int getRegverify() {
		return regverify;
	}

	public void setRegverify(int regverify) {
		this.regverify = regverify;
	}

	public String getAccessemail() {
		return accessemail;
	}

	public void setAccessemail(String accessemail) {
		this.accessemail = accessemail;
	}

	public String getCensoremail() {
		return censoremail;
	}

	public void setCensoremail(String censoremail) {
		this.censoremail = censoremail;
	}

	public int getHideprivate() {
		return hideprivate;
	}

	public void setHideprivate(int hideprivate) {
		this.hideprivate = hideprivate;
	}

	public int getRegctrl() {
		return regctrl;
	}

	public void setRegctrl(int regctrl) {
		this.regctrl = regctrl;
	}

	public String getIpregctrl() {
		return ipregctrl;
	}

	public void setIpregctrl(String ipregctrl) {
		this.ipregctrl = ipregctrl;
	}

	public String getIpdenyaccess() {
		return ipdenyaccess;
	}

	public void setIpdenyaccess(String ipdenyaccess) {
		this.ipdenyaccess = ipdenyaccess;
	}

	public String getIpaccess() {
		return ipaccess;
	}

	public void setIpaccess(String ipaccess) {
		this.ipaccess = ipaccess;
	}

	public String getAdminipaccess() {
		return adminipaccess;
	}

	public void setAdminipaccess(String adminipaccess) {
		this.adminipaccess = adminipaccess;
	}

	public int getNewbiespan() {
		return newbiespan;
	}

	public void setNewbiespan(int newbiespan) {
		this.newbiespan = newbiespan;
	}

	public int getWelcomems() {
		return welcomems;
	}

	public void setWelcomems(int welcomems) {
		this.welcomems = welcomems;
	}

	public String getWelcomemsgtxt() {
		return welcomemsgtxt;
	}

	public void setWelcomemsgtxt(String welcomemsgtxt) {
		this.welcomemsgtxt = welcomemsgtxt;
	}

	public int getRules() {
		return rules;
	}

	public void setRules(int rules) {
		this.rules = rules;
	}

	public String getRulestxt() {
		return rulestxt;
	}

	public void setRulestxt(String rulestxt) {
		this.rulestxt = rulestxt;
	}

	public int getSecques() {
		return secques;
	}

	public void setSecques(int secques) {
		this.secques = secques;
	}

	public int getTemplateid() {
		return templateid;
	}

	public void setTemplateid(int templateid) {
		this.templateid = templateid;
	}

	public int getHottopic() {
		return hottopic;
	}

	public void setHottopic(int hottopic) {
		this.hottopic = hottopic;
	}

	public int getStarthreshold() {
		return starthreshold;
	}

	public void setStarthreshold(int starthreshold) {
		this.starthreshold = starthreshold;
	}

	public int getVisitedforums() {
		return visitedforums;
	}

	public void setVisitedforums(int visitedforums) {
		this.visitedforums = visitedforums;
	}

	public int getMaxsigrows() {
		return maxsigrows;
	}

	public void setMaxsigrows(int maxsigrows) {
		this.maxsigrows = maxsigrows;
	}

	public int getModdisplay() {
		return moddisplay;
	}

	public void setModdisplay(int moddisplay) {
		this.moddisplay = moddisplay;
	}

	public int getSubforumsindex() {
		return subforumsindex;
	}

	public void setSubforumsindex(int subforumsindex) {
		this.subforumsindex = subforumsindex;
	}

	public int getStylejump() {
		return stylejump;
	}

	public void setStylejump(int stylejump) {
		this.stylejump = stylejump;
	}

	public int getFastpost() {
		return fastpost;
	}

	public void setFastpost(int fastpost) {
		this.fastpost = fastpost;
	}

	public int getShowsignatures() {
		return showsignatures;
	}

	public void setShowsignatures(int showsignatures) {
		this.showsignatures = showsignatures;
	}

	public int getShowavatars() {
		return showavatars;
	}

	public void setShowavatars(int showavatars) {
		this.showavatars = showavatars;
	}

	public int getShowimages() {
		return showimages;
	}

	public void setShowimages(int showimages) {
		this.showimages = showimages;
	}

	public int getSmiliesmax() {
		return smiliesmax;
	}

	public void setSmiliesmax(int smiliesmax) {
		this.smiliesmax = smiliesmax;
	}

	public int getArchiverstatus() {
		return archiverstatus;
	}

	public void setArchiverstatus(int archiverstatus) {
		this.archiverstatus = archiverstatus;
	}

	public String getSeotitle() {
		return seotitle;
	}

	public void setSeotitle(String seotitle) {
		this.seotitle = seotitle;
	}

	public String getSeokeywords() {
		return seokeywords;
	}

	public void setSeokeywords(String seokeywords) {
		this.seokeywords = seokeywords;
	}

	public String getSeodescription() {
		return seodescription;
	}

	public void setSeodescription(String seodescription) {
		this.seodescription = seodescription;
	}

	public String getSeohead() {
		return seohead;
	}

	public void setSeohead(String seohead) {
		this.seohead = seohead;
	}

	public int getRssstatus() {
		return rssstatus;
	}

	public void setRssstatus(int rssstatus) {
		this.rssstatus = rssstatus;
	}

	public int getRssttl() {
		return rssttl;
	}

	public void setRssttl(int rssttl) {
		this.rssttl = rssttl;
	}

	public int getSitemapstatus() {
		return sitemapstatus;
	}

	public void setSitemapstatus(int sitemapstatus) {
		this.sitemapstatus = sitemapstatus;
	}

	public int getSitemapttl() {
		return sitemapttl;
	}

	public void setSitemapttl(int sitemapttl) {
		this.sitemapttl = sitemapttl;
	}

	public int getNocacheheaders() {
		return nocacheheaders;
	}

	public void setNocacheheaders(int nocacheheaders) {
		this.nocacheheaders = nocacheheaders;
	}

	public int getFullmytopics() {
		return fullmytopics;
	}

	public void setFullmytopics(int fullmytopics) {
		this.fullmytopics = fullmytopics;
	}

	public int getWhosonlinestatus() {
		return whosonlinestatus;
	}

	public void setWhosonlinestatus(int whosonlinestatus) {
		this.whosonlinestatus = whosonlinestatus;
	}

	public int getMaxonlinelist() {
		return maxonlinelist;
	}

	public void setMaxonlinelist(int maxonlinelist) {
		this.maxonlinelist = maxonlinelist;
	}

	public int getUserstatusby() {
		return userstatusby;
	}

	public void setUserstatusby(int userstatusby) {
		this.userstatusby = userstatusby;
	}

	public int getForumjump() {
		return forumjump;
	}

	public void setForumjump(int forumjump) {
		this.forumjump = forumjump;
	}

	public int getModworkstatus() {
		return modworkstatus;
	}

	public void setModworkstatus(int modworkstatus) {
		this.modworkstatus = modworkstatus;
	}

	public int getMaxmodworksmonths() {
		return maxmodworksmonths;
	}

	public void setMaxmodworksmonths(int maxmodworksmonths) {
		this.maxmodworksmonths = maxmodworksmonths;
	}

	public String getSeccodestatus() {
		return seccodestatus;
	}

	public void setSeccodestatus(String seccodestatus) {
		this.seccodestatus = seccodestatus;
	}

	public int getGuestcachepagetimeout() {
		return guestcachepagetimeout;
	}

	public void setGuestcachepagetimeout(int guestcachepagetimeout) {
		this.guestcachepagetimeout = guestcachepagetimeout;
	}

	public int getTopiccachemark() {
		return topiccachemark;
	}

	public void setTopiccachemark(int topiccachemark) {
		this.topiccachemark = topiccachemark;
	}

	public int getMaxonlines() {
		return maxonlines;
	}

	public void setMaxonlines(int maxonlines) {
		this.maxonlines = maxonlines;
	}

	public int getPostinterval() {
		return postinterval;
	}

	public void setPostinterval(int postinterval) {
		this.postinterval = postinterval;
	}

	public int getSearchctrl() {
		return searchctrl;
	}

	public void setSearchctrl(int searchctrl) {
		this.searchctrl = searchctrl;
	}

	public int getMaxspm() {
		return maxspm;
	}

	public void setMaxspm(int maxspm) {
		this.maxspm = maxspm;
	}

	public String getVisitbanperiods() {
		return visitbanperiods;
	}

	public void setVisitbanperiods(String visitbanperiods) {
		this.visitbanperiods = visitbanperiods;
	}

	public String getPostbanperiods() {
		return postbanperiods;
	}

	public void setPostbanperiods(String postbanperiods) {
		this.postbanperiods = postbanperiods;
	}

	public String getPostmodperiods() {
		return postmodperiods;
	}

	public void setPostmodperiods(String postmodperiods) {
		this.postmodperiods = postmodperiods;
	}

	public String getAttachbanperiods() {
		return attachbanperiods;
	}

	public void setAttachbanperiods(String attachbanperiods) {
		this.attachbanperiods = attachbanperiods;
	}

	public String getSearchbanperiods() {
		return searchbanperiods;
	}

	public void setSearchbanperiods(String searchbanperiods) {
		this.searchbanperiods = searchbanperiods;
	}

	public int getMemliststatus() {
		return memliststatus;
	}

	public void setMemliststatus(int memliststatus) {
		this.memliststatus = memliststatus;
	}

	public int getDupkarmarate() {
		return dupkarmarate;
	}

	public void setDupkarmarate(int dupkarmarate) {
		this.dupkarmarate = dupkarmarate;
	}

	public int getMinpostsize() {
		return minpostsize;
	}

	public void setMinpostsize(int minpostsize) {
		this.minpostsize = minpostsize;
	}

	public int getMaxpostsize() {
		return maxpostsize;
	}

	public void setMaxpostsize(int maxpostsize) {
		this.maxpostsize = maxpostsize;
	}

	public int getTpp() {
		return tpp;
	}

	public void setTpp(int tpp) {
		this.tpp = tpp;
	}

	public int getPpp() {
		return ppp;
	}

	public void setPpp(int ppp) {
		this.ppp = ppp;
	}

	public int getMaxfavorites() {
		return maxfavorites;
	}

	public void setMaxfavorites(int maxfavorites) {
		this.maxfavorites = maxfavorites;
	}

	public int getMaxavatarsize() {
		return maxavatarsize;
	}

	public void setMaxavatarsize(int maxavatarsize) {
		this.maxavatarsize = maxavatarsize;
	}

	public int getMaxavatarwidth() {
		return maxavatarwidth;
	}

	public void setMaxavatarwidth(int maxavatarwidth) {
		this.maxavatarwidth = maxavatarwidth;
	}

	public int getMaxavatarheight() {
		return maxavatarheight;
	}

	public void setMaxavatarheight(int maxavatarheight) {
		this.maxavatarheight = maxavatarheight;
	}

	public int getMaxpolloptions() {
		return maxpolloptions;
	}

	public void setMaxpolloptions(int maxpolloptions) {
		this.maxpolloptions = maxpolloptions;
	}

	public int getMaxattachments() {
		return maxattachments;
	}

	public void setMaxattachments(int maxattachments) {
		this.maxattachments = maxattachments;
	}

	public int getAttachimgpost() {
		return attachimgpost;
	}

	public void setAttachimgpost(int attachimgpost) {
		this.attachimgpost = attachimgpost;
	}

	public int getAttachrefcheck() {
		return attachrefcheck;
	}

	public void setAttachrefcheck(int attachrefcheck) {
		this.attachrefcheck = attachrefcheck;
	}

	public int getAttachsave() {
		return attachsave;
	}

	public void setAttachsave(int attachsave) {
		this.attachsave = attachsave;
	}

	public int getWatermarkstatus() {
		return watermarkstatus;
	}

	public void setWatermarkstatus(int watermarkstatus) {
		this.watermarkstatus = watermarkstatus;
	}

	public int getWatermarktype() {
		return watermarktype;
	}

	public void setWatermarktype(int watermarktype) {
		this.watermarktype = watermarktype;
	}

	public int getWatermarktransparency() {
		return watermarktransparency;
	}

	public void setWatermarktransparency(int watermarktransparency) {
		this.watermarktransparency = watermarktransparency;
	}

	public String getWatermarktext() {
		return watermarktext;
	}

	public void setWatermarktext(String watermarktext) {
		this.watermarktext = watermarktext;
	}

	public String getWatermarkpic() {
		return watermarkpic;
	}

	public void setWatermarkpic(String watermarkpic) {
		this.watermarkpic = watermarkpic;
	}

	public String getWatermarkfontname() {
		return watermarkfontname;
	}

	public void setWatermarkfontname(String watermarkfontname) {
		this.watermarkfontname = watermarkfontname;
	}

	public int getWatermarkfontsize() {
		return watermarkfontsize;
	}

	public void setWatermarkfontsize(int watermarkfontsize) {
		this.watermarkfontsize = watermarkfontsize;
	}

	public int getShowattachmentpath() {
		return showattachmentpath;
	}

	public void setShowattachmentpath(int showattachmentpath) {
		this.showattachmentpath = showattachmentpath;
	}

	public int getAttachimgquality() {
		return attachimgquality;
	}

	public void setAttachimgquality(int attachimgquality) {
		this.attachimgquality = attachimgquality;
	}

	public int getAttachimgmaxheight() {
		return attachimgmaxheight;
	}

	public void setAttachimgmaxheight(int attachimgmaxheight) {
		this.attachimgmaxheight = attachimgmaxheight;
	}

	public int getAttachimgmaxwidth() {
		return attachimgmaxwidth;
	}

	public void setAttachimgmaxwidth(int attachimgmaxwidth) {
		this.attachimgmaxwidth = attachimgmaxwidth;
	}

	public int getReasonpm() {
		return reasonpm;
	}

	public void setReasonpm(int reasonpm) {
		this.reasonpm = reasonpm;
	}

	public int getModeractions() {
		return moderactions;
	}

	public void setModeractions(int moderactions) {
		this.moderactions = moderactions;
	}

	public int getKarmaratelimit() {
		return karmaratelimit;
	}

	public void setKarmaratelimit(int karmaratelimit) {
		this.karmaratelimit = karmaratelimit;
	}

	public int getLosslessdel() {
		return losslessdel;
	}

	public void setLosslessdel(int losslessdel) {
		this.losslessdel = losslessdel;
	}

	public int getEdittimelimit() {
		return edittimelimit;
	}

	public void setEdittimelimit(int edittimelimit) {
		this.edittimelimit = edittimelimit;
	}

	public int getEditedby() {
		return editedby;
	}

	public void setEditedby(int editedby) {
		this.editedby = editedby;
	}

	public int getDefaulteditormode() {
		return defaulteditormode;
	}

	public void setDefaulteditormode(int defaulteditormode) {
		this.defaulteditormode = defaulteditormode;
	}

	public int getAllowswitcheditor() {
		return allowswitcheditor;
	}

	public void setAllowswitcheditor(int allowswitcheditor) {
		this.allowswitcheditor = allowswitcheditor;
	}

	public int getSmileyinsert() {
		return smileyinsert;
	}

	public void setSmileyinsert(int smileyinsert) {
		this.smileyinsert = smileyinsert;
	}

	public String getCookiedomain() {
		return cookiedomain;
	}

	public void setCookiedomain(String cookiedomain) {
		this.cookiedomain = cookiedomain;
	}

	public int getOnlinetimeout() {
		return onlinetimeout;
	}

	public void setOnlinetimeout(int onlinetimeout) {
		this.onlinetimeout = onlinetimeout;
	}

	public int getTopicqueuestats() {
		return topicqueuestats;
	}

	public void setTopicqueuestats(int topicqueuestats) {
		this.topicqueuestats = topicqueuestats;
	}

	public int getTopicqueuestatscount() {
		return topicqueuestatscount;
	}

	public void setTopicqueuestatscount(int topicqueuestatscount) {
		this.topicqueuestatscount = topicqueuestatscount;
	}

	public int getDisplayratecount() {
		return displayratecount;
	}

	public void setDisplayratecount(int displayratecount) {
		this.displayratecount = displayratecount;
	}

	public String getReportusergroup() {
		return reportusergroup;
	}

	public void setReportusergroup(String reportusergroup) {
		this.reportusergroup = reportusergroup;
	}

	public String getPhotomangegroups() {
		return photomangegroups;
	}

	public void setPhotomangegroups(String photomangegroups) {
		this.photomangegroups = photomangegroups;
	}

	public String getRatevalveset() {
		return ratevalveset;
	}

	public void setRatevalveset(String ratevalveset) {
		this.ratevalveset = ratevalveset;
	}

	public int getViewnewtopicminute() {
		return viewnewtopicminute;
	}

	public void setViewnewtopicminute(int viewnewtopicminute) {
		this.viewnewtopicminute = viewnewtopicminute;
	}

	public int getHtmltitle() {
		return htmltitle;
	}

	public void setHtmltitle(int htmltitle) {
		this.htmltitle = htmltitle;
	}

	public String getHtmltitleusergroup() {
		return htmltitleusergroup;
	}

	public void setHtmltitleusergroup(String htmltitleusergroup) {
		this.htmltitleusergroup = htmltitleusergroup;
	}

	public int getSpecifytemplate() {
		return specifytemplate;
	}

	public void setSpecifytemplate(int specifytemplate) {
		this.specifytemplate = specifytemplate;
	}

	public int getMytopicsavetime() {
		return mytopicsavetime;
	}

	public void setMytopicsavetime(int mytopicsavetime) {
		this.mytopicsavetime = mytopicsavetime;
	}

	public int getMypostsavetime() {
		return mypostsavetime;
	}

	public void setMypostsavetime(int mypostsavetime) {
		this.mypostsavetime = mypostsavetime;
	}

	public int getMyattachmentsavetime() {
		return myattachmentsavetime;
	}

	public void setMyattachmentsavetime(int myattachmentsavetime) {
		this.myattachmentsavetime = myattachmentsavetime;
	}

	public int getEnabletag() {
		return enabletag;
	}

	public void setEnabletag(int enabletag) {
		this.enabletag = enabletag;
	}

	public int getEnablemall() {
		return enablemall;
	}

	public void setEnablemall(int enablemall) {
		this.enablemall = enablemall;
	}

	public int getStatscachelife() {
		return statscachelife;
	}

	public void setStatscachelife(int statscachelife) {
		this.statscachelife = statscachelife;
	}

	public int getStatstatus() {
		return statstatus;
	}

	public void setStatstatus(int statstatus) {
		this.statstatus = statstatus;
	}

	public int getPvfrequence() {
		return pvfrequence;
	}

	public void setPvfrequence(int pvfrequence) {
		this.pvfrequence = pvfrequence;
	}

	public int getOltimespan() {
		return oltimespan;
	}

	public void setOltimespan(int oltimespan) {
		this.oltimespan = oltimespan;
	}

	public String getRecommenddebates() {
		return recommenddebates;
	}

	public void setRecommenddebates(String recommenddebates) {
		this.recommenddebates = recommenddebates;
	}

	public int getGpp() {
		return gpp;
	}

	public void setGpp(int gpp) {
		this.gpp = gpp;
	}

	public int getDebatepagesize() {
		return debatepagesize;
	}

	public void setDebatepagesize(int debatepagesize) {
		this.debatepagesize = debatepagesize;
	}

	public int getHottagcount() {
		return hottagcount;
	}

	public void setHottagcount(int hottagcount) {
		this.hottagcount = hottagcount;
	}

	public int getDisablepostad() {
		return disablepostad;
	}

	public void setDisablepostad(int disablepostad) {
		this.disablepostad = disablepostad;
	}

	public int getDisablepostadregminute() {
		return disablepostadregminute;
	}

	public void setDisablepostadregminute(int disablepostadregminute) {
		this.disablepostadregminute = disablepostadregminute;
	}

	public int getDisablepostadpostcount() {
		return disablepostadpostcount;
	}

	public void setDisablepostadpostcount(int disablepostadpostcount) {
		this.disablepostadpostcount = disablepostadpostcount;
	}

	public String getDisablepostadregular() {
		return disablepostadregular;
	}

	public void setDisablepostadregular(String disablepostadregular) {
		this.disablepostadregular = disablepostadregular;
	}

	public int getWhosonlinecontract() {
		return whosonlinecontract;
	}

	public void setWhosonlinecontract(int whosonlinecontract) {
		this.whosonlinecontract = whosonlinecontract;
	}
}
