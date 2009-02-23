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

/**
 * 在线用户
 * 
 * @author 黄磊
 *
 */
@Entity
@Table(name = "online")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Online implements java.io.Serializable {

	private static final long serialVersionUID = 3526427750354775478L;
	private Integer olid;
	private Users users;
	private Usergroups usergroups;
	private Topics topics;
	private Forums forums;
	private Admingroups admingroups;
	private String ip;
	private String username;
	private String nickname;
	private String password;
	private String olimg;
	private Integer invisible;
	private Integer action;
	private Integer lastactivity;
	private String lastposttime;
	private String lastpostpmtime;
	private String lastsearchtime;
	private String lastupdatetime;
	private String forumname;
	private String title;
	private String verifycode;

	@Id
	@GeneratedValue(generator = "paymentableGenerator")
	@GenericGenerator(name = "paymentableGenerator", strategy = "increment")
	@Column(name = "olid", unique = true, nullable = false, insertable = true, updatable = true)
	public Integer getOlid() {
		return olid;
	}

	public void setOlid(Integer olid) {
		this.olid = olid;
	}

	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "userid", unique = false, nullable = true, insertable = true, updatable = true)
	public Users getUsers() {
		return users;
	}

	public void setUsers(Users users) {
		this.users = users;
	}

	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "groupid", unique = false, nullable = false, insertable = true, updatable = true)
	public Usergroups getUsergroups() {
		return usergroups;
	}

	public void setUsergroups(Usergroups usergroups) {
		this.usergroups = usergroups;
	}

	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "titleid", unique = false, nullable = true, insertable = false, updatable = true)
	public Topics getTopics() {
		return topics;
	}

	public void setTopics(Topics topics) {
		this.topics = topics;
	}

	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "forumid", unique = false, nullable = true, insertable = false, updatable = true)
	public Forums getForums() {
		return forums;
	}

	public void setForums(Forums forums) {
		this.forums = forums;
	}

	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "admingid", unique = false, nullable = true, insertable = true, updatable = true)
	public Admingroups getAdmingroups() {
		return admingroups;
	}

	public void setAdmingroups(Admingroups admingroups) {
		this.admingroups = admingroups;
	}

	@Column(name = "ip", unique = false, nullable = false, insertable = true, updatable = true, length = 15)
	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	@Column(name = "username", unique = false, nullable = false, insertable = true, updatable = true, length = 20)
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Column(name = "nickname", unique = false, nullable = false, insertable = true, updatable = true, length = 20)
	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	@Column(name = "password", unique = false, nullable = false, insertable = true, updatable = true, length = 32)
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Column(name = "olimg", unique = false, nullable = false, insertable = true, updatable = true, length = 80)
	public String getOlimg() {
		return olimg;
	}

	public void setOlimg(String olimg) {
		this.olimg = olimg;
	}

	@Column(name = "invisible", unique = false, nullable = false, insertable = true, updatable = true)
	public Integer getInvisible() {
		return invisible;
	}

	public void setInvisible(Integer invisible) {
		this.invisible = invisible;
	}

	@Column(name = "action", unique = false, nullable = false, insertable = true, updatable = true)
	public Integer getAction() {
		return action;
	}

	public void setAction(Integer action) {
		this.action = action;
	}

	@Column(name = "lastactivity", unique = false, nullable = false, insertable = true, updatable = true)
	public Integer getLastactivity() {
		return lastactivity;
	}

	public void setLastactivity(Integer lastactivity) {
		this.lastactivity = lastactivity;
	}

	@Column(name = "lastposttime", unique = false, nullable = false, insertable = true, updatable = true, length = 23)
	public String getLastposttime() {
		return lastposttime;
	}

	public void setLastposttime(String lastposttime) {
		this.lastposttime = lastposttime;
	}

	@Column(name = "lastpostpmtime", unique = false, nullable = false, insertable = true, updatable = true, length = 23)
	public String getLastpostpmtime() {
		return lastpostpmtime;
	}

	public void setLastpostpmtime(String lastpostpmtime) {
		this.lastpostpmtime = lastpostpmtime;
	}

	@Column(name = "lastsearchtime", unique = false, nullable = false, insertable = true, updatable = true, length = 23)
	public String getLastsearchtime() {
		return lastsearchtime;
	}

	public void setLastsearchtime(String lastsearchtime) {
		this.lastsearchtime = lastsearchtime;
	}

	@Column(name = "lastupdatetime", unique = false, nullable = false, insertable = true, updatable = true, length = 23)
	public String getLastupdatetime() {
		return lastupdatetime;
	}

	public void setLastupdatetime(String lastupdatetime) {
		this.lastupdatetime = lastupdatetime;
	}

	@Column(name = "forumname", unique = false, nullable = true, insertable = false, updatable = true, length = 50)
	public String getForumname() {
		if (forumname == null) {
			forumname = "";
		}
		return forumname;
	}

	public void setForumname(String forumname) {
		this.forumname = forumname;
	}

	@Column(name = "title", unique = false, nullable = true, insertable = false, updatable = true, length = 80)
	public String getTitle() {
		if (title == null) {
			title = "";
		}
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Column(name = "verifycode", unique = false, nullable = false, insertable = true, updatable = true, length = 10)
	public String getVerifycode() {
		return verifycode;
	}

	public void setVerifycode(String verifycode) {
		this.verifycode = verifycode;
	}

}