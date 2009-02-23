package com.javaeye.lonlysky.lforum.entity.forum;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

/**
 * 用户扩展信息
 *  
 * @author 黄磊
 *
 */
@Entity
@Table(name = "userfields")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Userfields implements java.io.Serializable {

	private static final long serialVersionUID = -7322636970699737729L;
	private Integer uid;
	private Users users;
	private String website;
	private String icq;
	private String qq;
	private String yahoo;
	private String msn;
	private String skype;
	private String location;
	private String customstatus;
	private String avatar;
	private Integer avatarwidth;
	private Integer avatarheight;
	private String medals;
	private String bio;
	private String signature;
	private String sightml;
	private String authstr;
	private String authtime;
	private Integer authflag;
	private String realname;
	private String idcard;
	private String mobile;
	private String phone;

	@Id
	@GeneratedValue(generator = "idGenerator")
	@GenericGenerator(name = "idGenerator", strategy = "foreign", parameters = { @Parameter(name = "property", value = "users") })
	@Column(name = "uid", unique = true, nullable = false, insertable = true, updatable = true)
	public Integer getUid() {
		return uid;
	}

	public void setUid(Integer uid) {
		this.uid = uid;
	}

	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "uid", unique = true, nullable = false, insertable = false, updatable = false)
	public Users getUsers() {
		return users;
	}

	public void setUsers(Users users) {
		this.users = users;
	}

	@Column(name = "website", unique = false, nullable = false, insertable = true, updatable = true, length = 80)
	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	@Column(name = "icq", unique = false, nullable = false, insertable = true, updatable = true, length = 12)
	public String getIcq() {
		return icq;
	}

	public void setIcq(String icq) {
		this.icq = icq;
	}

	@Column(name = "qq", unique = false, nullable = false, insertable = true, updatable = true, length = 12)
	public String getQq() {
		return qq;
	}

	public void setQq(String qq) {
		this.qq = qq;
	}

	@Column(name = "yahoo", unique = false, nullable = false, insertable = true, updatable = true, length = 40)
	public String getYahoo() {
		return yahoo;
	}

	public void setYahoo(String yahoo) {
		this.yahoo = yahoo;
	}

	@Column(name = "msn", unique = false, nullable = false, insertable = true, updatable = true, length = 40)
	public String getMsn() {
		return msn;
	}

	public void setMsn(String msn) {
		this.msn = msn;
	}

	@Column(name = "skype", unique = false, nullable = false, insertable = true, updatable = true, length = 40)
	public String getSkype() {
		return skype;
	}

	public void setSkype(String skype) {
		this.skype = skype;
	}

	@Column(name = "location", unique = false, nullable = false, insertable = true, updatable = true, length = 50)
	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	@Column(name = "customstatus", unique = false, nullable = false, insertable = true, updatable = true, length = 50)
	public String getCustomstatus() {
		return customstatus;
	}

	public void setCustomstatus(String customstatus) {
		this.customstatus = customstatus;
	}

	@Column(name = "avatar", unique = false, nullable = false, insertable = true, updatable = true)
	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	@Column(name = "avatarwidth", unique = false, nullable = false, insertable = true, updatable = true)
	public Integer getAvatarwidth() {
		return avatarwidth;
	}

	public void setAvatarwidth(Integer avatarwidth) {
		this.avatarwidth = avatarwidth;
	}

	@Column(name = "avatarheight", unique = false, nullable = false, insertable = true, updatable = true)
	public Integer getAvatarheight() {
		return avatarheight;
	}

	public void setAvatarheight(Integer avatarheight) {
		this.avatarheight = avatarheight;
	}

	@Column(name = "medals", unique = false, nullable = false, insertable = true, updatable = true, length = 300)
	public String getMedals() {
		return medals;
	}

	public void setMedals(String medals) {
		this.medals = medals;
	}

	@Column(name = "bio", unique = false, nullable = false, insertable = true, updatable = true, length = 500)
	public String getBio() {
		return bio;
	}

	public void setBio(String bio) {
		this.bio = bio;
	}

	@Column(name = "signature", unique = false, nullable = false, insertable = true, updatable = true, length = 500)
	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	@Column(name = "sightml", unique = false, nullable = false, insertable = true, updatable = true, length = 1000)
	public String getSightml() {
		return sightml;
	}

	public void setSightml(String sightml) {
		this.sightml = sightml;
	}

	@Column(name = "authstr", unique = false, nullable = false, insertable = true, updatable = true, length = 20)
	public String getAuthstr() {
		return authstr;
	}

	public void setAuthstr(String authstr) {
		this.authstr = authstr;
	}

	@Column(name = "authtime", unique = false, nullable = false, insertable = true, updatable = true, length = 16)
	public String getAuthtime() {
		return authtime;
	}

	public void setAuthtime(String authtime) {
		this.authtime = authtime;
	}

	@Column(name = "authflag", unique = false, nullable = false, insertable = true, updatable = true)
	public Integer getAuthflag() {
		return authflag;
	}

	public void setAuthflag(Integer authflag) {
		this.authflag = authflag;
	}

	@Column(name = "realname", unique = false, nullable = false, insertable = true, updatable = true, length = 10)
	public String getRealname() {
		return realname;
	}

	public void setRealname(String realname) {
		this.realname = realname;
	}

	@Column(name = "idcard", unique = false, nullable = false, insertable = true, updatable = true, length = 20)
	public String getIdcard() {
		return idcard;
	}

	public void setIdcard(String idcard) {
		this.idcard = idcard;
	}

	@Column(name = "mobile", unique = false, nullable = false, insertable = true, updatable = true, length = 20)
	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	@Column(name = "phone", unique = false, nullable = false, insertable = true, updatable = true, length = 20)
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

}