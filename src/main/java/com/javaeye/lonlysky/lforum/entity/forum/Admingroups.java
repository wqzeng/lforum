package com.javaeye.lonlysky.lforum.entity.forum;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;


/**
 * 管理组
 * 
 * @author 黄磊
 *
 */
@Entity
@Table(name = "admingroups")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Admingroups implements java.io.Serializable {

	private static final long serialVersionUID = 5652344580802916954L;
	private Integer admingid;
	private Integer alloweditpost;
	private Integer alloweditpoll;
	private Integer allowstickthread;
	private Integer allowmodpost;
	private Integer allowdelpost;
	private Integer allowmassprune;
	private Integer allowrefund;
	private Integer allowcensorword;
	private Integer allowviewip;
	private Integer allowbanip;
	private Integer allowedituser;
	private Integer allowmoduser;
	private Integer allowbanuser;
	private Integer allowpostannounce;
	private Integer allowviewlog;
	private Integer disablepostctrl;
	private Integer allowviewrealname;
	private Set<Medalslog> medalslogs = new HashSet<Medalslog>(0);
	private Set<Online> onlines = new HashSet<Online>(0);
	private Set<Usergroups> usergroupses = new HashSet<Usergroups>(0);
	private Set<Users> userses = new HashSet<Users>(0);


	// Property accessors
	@Id
	@GeneratedValue(generator = "paymentableGenerator")     
	@GenericGenerator(name = "paymentableGenerator", strategy = "increment")  
	@Column(name = "admingid", unique = true, nullable = false, insertable = true, updatable = true)
	public Integer getAdmingid() {
		return admingid;
	}

	public void setAdmingid(Integer admingid) {
		this.admingid = admingid;
	}

	@Column(name = "alloweditpost", unique = false, nullable = false, insertable = true, updatable = true)
	public Integer getAlloweditpost() {
		return alloweditpost;
	}

	public void setAlloweditpost(Integer alloweditpost) {
		this.alloweditpost = alloweditpost;
	}

	@Column(name = "alloweditpoll", unique = false, nullable = false, insertable = true, updatable = true)
	public Integer getAlloweditpoll() {
		return alloweditpoll;
	}

	public void setAlloweditpoll(Integer alloweditpoll) {
		this.alloweditpoll = alloweditpoll;
	}

	@Column(name = "allowstickthread", unique = false, nullable = false, insertable = true, updatable = true)
	public Integer getAllowstickthread() {
		return allowstickthread;
	}

	public void setAllowstickthread(Integer allowstickthread) {
		this.allowstickthread = allowstickthread;
	}

	@Column(name = "allowmodpost", unique = false, nullable = false, insertable = true, updatable = true)
	public Integer getAllowmodpost() {
		return allowmodpost;
	}

	public void setAllowmodpost(Integer allowmodpost) {
		this.allowmodpost = allowmodpost;
	}

	@Column(name = "allowdelpost", unique = false, nullable = false, insertable = true, updatable = true)
	public Integer getAllowdelpost() {
		return allowdelpost;
	}

	public void setAllowdelpost(Integer allowdelpost) {
		this.allowdelpost = allowdelpost;
	}

	@Column(name = "allowmassprune", unique = false, nullable = false, insertable = true, updatable = true)
	public Integer getAllowmassprune() {
		return allowmassprune;
	}

	public void setAllowmassprune(Integer allowmassprune) {
		this.allowmassprune = allowmassprune;
	}

	@Column(name = "allowrefund", unique = false, nullable = false, insertable = true, updatable = true)
	public Integer getAllowrefund() {
		return allowrefund;
	}

	public void setAllowrefund(Integer allowrefund) {
		this.allowrefund = allowrefund;
	}

	@Column(name = "allowcensorword", unique = false, nullable = false, insertable = true, updatable = true)
	public Integer getAllowcensorword() {
		return allowcensorword;
	}

	public void setAllowcensorword(Integer allowcensorword) {
		this.allowcensorword = allowcensorword;
	}

	@Column(name = "allowviewip", unique = false, nullable = false, insertable = true, updatable = true)
	public Integer getAllowviewip() {
		return allowviewip;
	}

	public void setAllowviewip(Integer allowviewip) {
		this.allowviewip = allowviewip;
	}

	@Column(name = "allowbanip", unique = false, nullable = false, insertable = true, updatable = true)
	public Integer getAllowbanip() {
		return allowbanip;
	}

	public void setAllowbanip(Integer allowbanip) {
		this.allowbanip = allowbanip;
	}

	@Column(name = "allowedituser", unique = false, nullable = false, insertable = true, updatable = true)
	public Integer getAllowedituser() {
		return allowedituser;
	}

	public void setAllowedituser(Integer allowedituser) {
		this.allowedituser = allowedituser;
	}

	@Column(name = "allowmoduser", unique = false, nullable = false, insertable = true, updatable = true)
	public Integer getAllowmoduser() {
		return allowmoduser;
	}

	public void setAllowmoduser(Integer allowmoduser) {
		this.allowmoduser = allowmoduser;
	}

	@Column(name = "allowbanuser", unique = false, nullable = false, insertable = true, updatable = true)
	public Integer getAllowbanuser() {
		return allowbanuser;
	}

	public void setAllowbanuser(Integer allowbanuser) {
		this.allowbanuser = allowbanuser;
	}

	@Column(name = "allowpostannounce", unique = false, nullable = false, insertable = true, updatable = true)
	public Integer getAllowpostannounce() {
		return allowpostannounce;
	}

	public void setAllowpostannounce(Integer allowpostannounce) {
		this.allowpostannounce = allowpostannounce;
	}

	@Column(name = "allowviewlog", unique = false, nullable = false, insertable = true, updatable = true)
	public Integer getAllowviewlog() {
		return allowviewlog;
	}

	public void setAllowviewlog(Integer allowviewlog) {
		this.allowviewlog = allowviewlog;
	}

	@Column(name = "disablepostctrl", unique = false, nullable = false, insertable = true, updatable = true)
	public Integer getDisablepostctrl() {
		return disablepostctrl;
	}

	public void setDisablepostctrl(Integer disablepostctrl) {
		this.disablepostctrl = disablepostctrl;
	}

	@Column(name = "allowviewrealname", unique = false, nullable = false, insertable = true, updatable = true)
	public Integer getAllowviewrealname() {
		return allowviewrealname;
	}

	public void setAllowviewrealname(Integer allowviewrealname) {
		this.allowviewrealname = allowviewrealname;
	}

	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY, mappedBy = "admingroups")
	public Set<Medalslog> getMedalslogs() {
		return medalslogs;
	}

	public void setMedalslogs(Set<Medalslog> medalslogs) {
		this.medalslogs = medalslogs;
	}

	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY, mappedBy = "admingroups")
	public Set<Online> getOnlines() {
		return onlines;
	}

	public void setOnlines(Set<Online> onlines) {
		this.onlines = onlines;
	}

	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY, mappedBy = "admingroups")
	public Set<Usergroups> getUsergroupses() {
		return usergroupses;
	}

	public void setUsergroupses(Set<Usergroups> usergroupses) {
		this.usergroupses = usergroupses;
	}

	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY, mappedBy = "admingroups")
	public Set<Users> getUserses() {
		return userses;
	}

	public void setUserses(Set<Users> userses) {
		this.userses = userses;
	}

}