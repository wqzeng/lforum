package com.javaeye.lonlysky.lforum.entity.forum;

/**
 * 帖子显示信息
 * 
 * @author 黄磊
 *
 */
public class ShowtopicPagePostInfo {
	private String ubbmessage;
	private int id; //序号(楼层)
	private int pid; //帖子PID
	private int fid; //归属版块ID
	private String title; //标题
	private int layer; //帖子所处层次
	private String message; //内容
	private String ip; //IP地址
	private String lastedit; //最后编辑
	private String postdatetime; //发表时间
	private int attachment; //是否含有附件
	private String poster; //帖子作者
	private int posterid; //作者UID
	private int invisible; //是否隐藏, 如果未通过审核则为隐藏
	private int usesig; //是否启用签名
	private int htmlon; //是否支持html
	private int smileyoff; //是否关闭smaile表情
	private int parseurloff; //是否关闭url自动解析
	private int bbcodeoff; //是否支持html
	private int rate; //评分分数
	private int ratetimes; //评分次数
	private String nickname; //昵称
	private String username; //用户名
	private int groupid; //用户组ID
	private String email; //邮件地址
	private int showemail; //是否显示邮箱
	private int digestposts; //精华贴数
	private int credits; //积分数
	private double extcredits1; //扩展积分1
	private double extcredits2; //扩展积分2
	private double extcredits3; //扩展积分3
	private double extcredits4; //扩展积分4
	private double extcredits5; //扩展积分5
	private double extcredits6; //扩展积分6
	private double extcredits7; //扩展积分7
	private double extcredits8; //扩展积分8
	private int posts; //发帖数
	private String joindate; //注册时间
	private int onlinestate; //在线状态, 1为在线, 0为不在线
	private String lastactivity; //最后活动时间
	private int userinvisible; //是否隐身
	private String avatar; //头像
	private int avatarwidth; //头像宽度
	private int avatarheight; //头像高度
	private String medals; //勋章列表
	private String signature; //签名Html
	private String location; //来自
	private String customstatus; //自定义头衔
	private String website; //网站
	private String icq; //ICQ帐号
	private String qq; //QQ帐号
	private String msn; //MSN messenger帐号
	private String yahoo; //Yahoo messenger帐号
	private String skype; //skype帐号
	//扩展属性
	private String status; //头衔
	private int stars; //星星
	private int adindex; //广告序号
	private int spaceid;//空间Id
	private int gender;//性别
	private String bday;//生日
	private int debateopinion;//辩论观点,1,正方观点，2反方观点
	private int diggs;//作为辩论观点的支持数
	private boolean digged = true;

	public String getUbbmessage() {
		return ubbmessage;
	}

	public void setUbbmessage(String ubbmessage) {
		this.ubbmessage = ubbmessage;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getPid() {
		return pid;
	}

	public void setPid(int pid) {
		this.pid = pid;
	}

	public int getFid() {
		return fid;
	}

	public void setFid(int fid) {
		this.fid = fid;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getLayer() {
		return layer;
	}

	public void setLayer(int layer) {
		this.layer = layer;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getLastedit() {
		return lastedit;
	}

	public void setLastedit(String lastedit) {
		this.lastedit = lastedit;
	}

	public String getPostdatetime() {
		return postdatetime;
	}

	public void setPostdatetime(String postdatetime) {
		this.postdatetime = postdatetime;
	}

	public int getAttachment() {
		return attachment;
	}

	public void setAttachment(int attachment) {
		this.attachment = attachment;
	}

	public String getPoster() {
		return poster;
	}

	public void setPoster(String poster) {
		this.poster = poster;
	}

	public int getPosterid() {
		return posterid;
	}

	public void setPosterid(int posterid) {
		this.posterid = posterid;
	}

	public int getInvisible() {
		return invisible;
	}

	public void setInvisible(int invisible) {
		this.invisible = invisible;
	}

	public int getUsesig() {
		return usesig;
	}

	public void setUsesig(int usesig) {
		this.usesig = usesig;
	}

	public int getHtmlon() {
		return htmlon;
	}

	public void setHtmlon(int htmlon) {
		this.htmlon = htmlon;
	}

	public int getSmileyoff() {
		return smileyoff;
	}

	public void setSmileyoff(int smileyoff) {
		this.smileyoff = smileyoff;
	}

	public int getParseurloff() {
		return parseurloff;
	}

	public void setParseurloff(int parseurloff) {
		this.parseurloff = parseurloff;
	}

	public int getBbcodeoff() {
		return bbcodeoff;
	}

	public void setBbcodeoff(int bbcodeoff) {
		this.bbcodeoff = bbcodeoff;
	}

	public int getRate() {
		return rate;
	}

	public void setRate(int rate) {
		this.rate = rate;
	}

	public int getRatetimes() {
		return ratetimes;
	}

	public void setRatetimes(int ratetimes) {
		this.ratetimes = ratetimes;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public int getGroupid() {
		return groupid;
	}

	public void setGroupid(int groupid) {
		this.groupid = groupid;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getShowemail() {
		return showemail;
	}

	public void setShowemail(int showemail) {
		this.showemail = showemail;
	}

	public int getDigestposts() {
		return digestposts;
	}

	public void setDigestposts(int digestposts) {
		this.digestposts = digestposts;
	}

	public int getCredits() {
		return credits;
	}

	public void setCredits(int credits) {
		this.credits = credits;
	}

	public double getExtcredits1() {
		return extcredits1;
	}

	public void setExtcredits1(double extcredits1) {
		this.extcredits1 = extcredits1;
	}

	public double getExtcredits2() {
		return extcredits2;
	}

	public void setExtcredits2(double extcredits2) {
		this.extcredits2 = extcredits2;
	}

	public double getExtcredits3() {
		return extcredits3;
	}

	public void setExtcredits3(double extcredits3) {
		this.extcredits3 = extcredits3;
	}

	public double getExtcredits4() {
		return extcredits4;
	}

	public void setExtcredits4(double extcredits4) {
		this.extcredits4 = extcredits4;
	}

	public double getExtcredits5() {
		return extcredits5;
	}

	public void setExtcredits5(double extcredits5) {
		this.extcredits5 = extcredits5;
	}

	public double getExtcredits6() {
		return extcredits6;
	}

	public void setExtcredits6(double extcredits6) {
		this.extcredits6 = extcredits6;
	}

	public double getExtcredits7() {
		return extcredits7;
	}

	public void setExtcredits7(double extcredits7) {
		this.extcredits7 = extcredits7;
	}

	public double getExtcredits8() {
		return extcredits8;
	}

	public void setExtcredits8(double extcredits8) {
		this.extcredits8 = extcredits8;
	}

	public int getPosts() {
		return posts;
	}

	public void setPosts(int posts) {
		this.posts = posts;
	}

	public String getJoindate() {
		return joindate;
	}

	public void setJoindate(String joindate) {
		this.joindate = joindate;
	}

	public int getOnlinestate() {
		return onlinestate;
	}

	public void setOnlinestate(int onlinestate) {
		this.onlinestate = onlinestate;
	}

	public String getLastactivity() {
		return lastactivity;
	}

	public void setLastactivity(String lastactivity) {
		this.lastactivity = lastactivity;
	}

	public int getUserinvisible() {
		return userinvisible;
	}

	public void setUserinvisible(int userinvisible) {
		this.userinvisible = userinvisible;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public int getAvatarwidth() {
		return avatarwidth;
	}

	public void setAvatarwidth(int avatarwidth) {
		this.avatarwidth = avatarwidth;
	}

	public int getAvatarheight() {
		return avatarheight;
	}

	public void setAvatarheight(int avatarheight) {
		this.avatarheight = avatarheight;
	}

	public String getMedals() {
		return medals;
	}

	public void setMedals(String medals) {
		this.medals = medals;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getCustomstatus() {
		return customstatus;
	}

	public void setCustomstatus(String customstatus) {
		this.customstatus = customstatus;
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	public String getIcq() {
		return icq;
	}

	public void setIcq(String icq) {
		this.icq = icq;
	}

	public String getQq() {
		return qq;
	}

	public void setQq(String qq) {
		this.qq = qq;
	}

	public String getMsn() {
		return msn;
	}

	public void setMsn(String msn) {
		this.msn = msn;
	}

	public String getYahoo() {
		return yahoo;
	}

	public void setYahoo(String yahoo) {
		this.yahoo = yahoo;
	}

	public String getSkype() {
		return skype;
	}

	public void setSkype(String skype) {
		this.skype = skype;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public int getStars() {
		return stars;
	}

	public void setStars(int stars) {
		this.stars = stars;
	}

	public int getAdindex() {
		return adindex;
	}

	public void setAdindex(int adindex) {
		this.adindex = adindex;
	}

	public int getSpaceid() {
		return spaceid;
	}

	public void setSpaceid(int spaceid) {
		this.spaceid = spaceid;
	}

	public int getGender() {
		return gender;
	}

	public void setGender(int gender) {
		this.gender = gender;
	}

	public String getBday() {
		return bday;
	}

	public void setBday(String bday) {
		this.bday = bday;
	}

	public int getDebateopinion() {
		return debateopinion;
	}

	public void setDebateopinion(int debateopinion) {
		this.debateopinion = debateopinion;
	}

	public int getDiggs() {
		return diggs;
	}

	public void setDiggs(int diggs) {
		this.diggs = diggs;
	}

	public boolean isDigged() {
		return digged;
	}

	public void setDigged(boolean digged) {
		this.digged = digged;
	}
}
