package com.javaeye.lonlysky.lforum.entity.forum;

import java.util.List;

/**
 * 帖子参数
 * 
 * @author 黄磊
 *
 */
public class PostpramsInfo {
	private int fid; //版块id
	private int tid; //主题id
	private int pid; //帖子id
	private int pagesize; //分页显示帖子数量
	private int pageindex; //分页当前页数
	private String getattachperm; //下载附件权限设定,格式为 groupid1,groupid2...
	private boolean ubbmode; //是否对Lforum代码进行解析(true:解析,false:不解析)
	private int usergroupid; //用户所在组ID
	private int usergroupreadaccess; //用户的下载/访问权限
	private int attachimgpost; //是否显示图片附件
	private int showattachmentpath; //是否显示附件的直实路径
	private int hide; //判断是否为回复可见帖, hide=0为非回复可见(正常), hide>0为回复可见
	private int price; //判断是否为购买可见帖, price=0为非购买可见(正常), price>0 为购买可见
	private String condition; //附加条件
	private int jammer; //帖子是否增加干扰码
	private int onlinetimeout;
	private int currentuserid;//当前在线用户id
	private Usergroups currentusergroup; //当前用户组
	
	// 一下为UBB转换专用属性
	private String sdetail;		//帖子内容
	private int smileyoff;		//禁止笑脸显示.
	private int bbcodeoff;		//禁止Discuz!NT代码转换
	private int parseurloff;	//禁止网址自动转换
	private int showimages;		//是否对帖子中的图片标签进行解析.
	private int allowhtml;		//是否允许解析html标签.
	private List<Smilies> smiliesinfo;	//表情库
	private List<CustomEditorButtonInfo> customeditorbuttoninfo; ///自定义按钮图库
	private int smiliesmax;		//帖子中解析的单个表情最大个数.
	private int bbcodemode;		//LForum代码兼容模式(0:不兼容)
	private int signature;		//是否为签名，用于签名ubb转换
    private int isforspace = 0;     //是为个人空间而进行的解析  
    
    
	public int getFid() {
		return fid;
	}
	public void setFid(int fid) {
		this.fid = fid;
	}
	public int getTid() {
		return tid;
	}
	public void setTid(int tid) {
		this.tid = tid;
	}
	public int getPid() {
		return pid;
	}
	public void setPid(int pid) {
		this.pid = pid;
	}
	public int getPagesize() {
		return pagesize;
	}
	public void setPagesize(int pagesize) {
		this.pagesize = pagesize;
	}
	public int getPageindex() {
		return pageindex;
	}
	public void setPageindex(int pageindex) {
		this.pageindex = pageindex;
	}
	public String getGetattachperm() {
		return getattachperm;
	}
	public void setGetattachperm(String getattachperm) {
		this.getattachperm = getattachperm;
	}
	public boolean isUbbmode() {
		return ubbmode;
	}
	public void setUbbmode(boolean ubbmode) {
		this.ubbmode = ubbmode;
	}
	public int getUsergroupid() {
		return usergroupid;
	}
	public void setUsergroupid(int usergroupid) {
		this.usergroupid = usergroupid;
	}
	public int getUsergroupreadaccess() {
		return usergroupreadaccess;
	}
	public void setUsergroupreadaccess(int usergroupreadaccess) {
		this.usergroupreadaccess = usergroupreadaccess;
	}
	public int getAttachimgpost() {
		return attachimgpost;
	}
	public void setAttachimgpost(int attachimgpost) {
		this.attachimgpost = attachimgpost;
	}
	public int getShowattachmentpath() {
		return showattachmentpath;
	}
	public void setShowattachmentpath(int showattachmentpath) {
		this.showattachmentpath = showattachmentpath;
	}
	public int getHide() {
		return hide;
	}
	public void setHide(int hide) {
		this.hide = hide;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	public String getCondition() {
		return condition;
	}
	public void setCondition(String condition) {
		this.condition = condition;
	}
	public int getJammer() {
		return jammer;
	}
	public void setJammer(int jammer) {
		this.jammer = jammer;
	}
	public int getOnlinetimeout() {
		return onlinetimeout;
	}
	public void setOnlinetimeout(int onlinetimeout) {
		this.onlinetimeout = onlinetimeout;
	}
	public int getCurrentuserid() {
		return currentuserid;
	}
	public void setCurrentuserid(int currentuserid) {
		this.currentuserid = currentuserid;
	}
	public Usergroups getCurrentusergroup() {
		return currentusergroup;
	}
	public void setCurrentusergroup(Usergroups currentusergroup) {
		this.currentusergroup = currentusergroup;
	}
	public String getSdetail() {
		return sdetail;
	}
	public void setSdetail(String sdetail) {
		this.sdetail = sdetail;
	}
	public int getSmileyoff() {
		return smileyoff;
	}
	public void setSmileyoff(int smileyoff) {
		this.smileyoff = smileyoff;
	}
	public int getBbcodeoff() {
		return bbcodeoff;
	}
	public void setBbcodeoff(int bbcodeoff) {
		this.bbcodeoff = bbcodeoff;
	}
	public int getParseurloff() {
		return parseurloff;
	}
	public void setParseurloff(int parseurloff) {
		this.parseurloff = parseurloff;
	}
	public int getShowimages() {
		return showimages;
	}
	public void setShowimages(int showimages) {
		this.showimages = showimages;
	}
	public int getAllowhtml() {
		return allowhtml;
	}
	public void setAllowhtml(int allowhtml) {
		this.allowhtml = allowhtml;
	}
	public List<Smilies> getSmiliesinfo() {
		return smiliesinfo;
	}
	public void setSmiliesinfo(List<Smilies> smiliesinfo) {
		this.smiliesinfo = smiliesinfo;
	}
	public List<CustomEditorButtonInfo> getCustomeditorbuttoninfo() {
		return customeditorbuttoninfo;
	}
	public void setCustomeditorbuttoninfo(List<CustomEditorButtonInfo> customeditorbuttoninfo) {
		this.customeditorbuttoninfo = customeditorbuttoninfo;
	}
	public int getSmiliesmax() {
		return smiliesmax;
	}
	public void setSmiliesmax(int smiliesmax) {
		this.smiliesmax = smiliesmax;
	}
	public int getBbcodemode() {
		return bbcodemode;
	}
	public void setBbcodemode(int bbcodemode) {
		this.bbcodemode = bbcodemode;
	}
	public int getSignature() {
		return signature;
	}
	public void setSignature(int signature) {
		this.signature = signature;
	}
	public int getIsforspace() {
		return isforspace;
	}
	public void setIsforspace(int isforspace) {
		this.isforspace = isforspace;
	}
	
}
