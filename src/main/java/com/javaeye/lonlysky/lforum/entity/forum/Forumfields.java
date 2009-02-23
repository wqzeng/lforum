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
 * 板块扩展信息
 * 
 * @author 黄磊
 *
 */
@Entity
@Table(name = "forumfields")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Forumfields implements java.io.Serializable {

	private static final long serialVersionUID = 5754677907201644096L;
	private Integer fid;
	private Forums forums;
	private String password;
	private String icon;
	private String postcredits;
	private String replycredits;
	private String redirect;
	private String attachextensions;
	private String rules;
	private String topictypes;
	private String viewperm;
	private String postperm;
	private String replyperm;
	private String getattachperm;
	private String postattachperm;
	private String moderators;
	private String description;
	private Integer applytopictype;
	private Integer postbytopictype;
	private Integer viewbytopictype;
	private Integer topictypeprefix;
	private String permuserlist;

	
	@Id
	@GeneratedValue(generator = "paymentableGenerator")     
	@GenericGenerator(name = "paymentableGenerator", strategy = "increment")  
	@Column(name = "fid", unique = true, nullable = false, insertable = true, updatable = true)
	public Integer getFid() {
		return fid;
	}

	public void setFid(Integer fid) {
		this.fid = fid;
	}

	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "fid", unique = true, nullable = false, insertable = false, updatable = false)
	public Forums getForums() {
		return forums;
	}

	public void setForums(Forums forums) {
		this.forums = forums;
	}

	@Column(name = "password", unique = false, nullable = false, insertable = true, updatable = true, length = 16)
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Column(name = "icon", unique = false, nullable = true, insertable = true, updatable = true)
	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	@Column(name = "postcredits", unique = false, nullable = true, insertable = true, updatable = true)
	public String getPostcredits() {
		return postcredits;
	}

	public void setPostcredits(String postcredits) {
		this.postcredits = postcredits;
	}

	@Column(name = "replycredits", unique = false, nullable = true, insertable = true, updatable = true)
	public String getReplycredits() {
		return replycredits;
	}

	public void setReplycredits(String replycredits) {
		this.replycredits = replycredits;
	}

	@Column(name = "redirect", unique = false, nullable = true, insertable = true, updatable = true)
	public String getRedirect() {
		return redirect;
	}

	public void setRedirect(String redirect) {
		this.redirect = redirect;
	}

	@Column(name = "attachextensions", unique = false, nullable = true, insertable = true, updatable = true)
	public String getAttachextensions() {
		return attachextensions;
	}

	public void setAttachextensions(String attachextensions) {
		this.attachextensions = attachextensions;
	}

	@Column(name = "rules", unique = false, nullable = true, insertable = true, updatable = true, length = 1073741823)
	public String getRules() {
		return rules;
	}

	public void setRules(String rules) {
		this.rules = rules;
	}

	@Column(name = "topictypes", unique = false, nullable = true, insertable = true, updatable = true)
	public String getTopictypes() {
		return topictypes;
	}

	public void setTopictypes(String topictypes) {
		this.topictypes = topictypes;
	}

	@Column(name = "viewperm", unique = false, nullable = true, insertable = true, updatable = true)
	public String getViewperm() {
		return viewperm;
	}

	public void setViewperm(String viewperm) {
		this.viewperm = viewperm;
	}

	@Column(name = "postperm", unique = false, nullable = true, insertable = true, updatable = true)
	public String getPostperm() {
		return postperm;
	}

	public void setPostperm(String postperm) {
		this.postperm = postperm;
	}

	@Column(name = "replyperm", unique = false, nullable = true, insertable = true, updatable = true)
	public String getReplyperm() {
		return replyperm;
	}

	public void setReplyperm(String replyperm) {
		this.replyperm = replyperm;
	}

	@Column(name = "getattachperm", unique = false, nullable = true, insertable = true, updatable = true)
	public String getGetattachperm() {
		return getattachperm;
	}

	public void setGetattachperm(String getattachperm) {
		this.getattachperm = getattachperm;
	}

	@Column(name = "postattachperm", unique = false, nullable = true, insertable = true, updatable = true)
	public String getPostattachperm() {
		return postattachperm;
	}

	public void setPostattachperm(String postattachperm) {
		this.postattachperm = postattachperm;
	}

	@Column(name = "moderators", unique = false, nullable = true, insertable = true, updatable = true, length = 1073741823)
	public String getModerators() {
		return moderators;
	}

	public void setModerators(String moderators) {
		this.moderators = moderators;
	}

	@Column(name = "description", unique = false, nullable = true, insertable = true, updatable = true, length = 1073741823)
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Column(name = "applytopictype", unique = false, nullable = false, insertable = true, updatable = true)
	public Integer getApplytopictype() {
		return applytopictype;
	}

	public void setApplytopictype(Integer applytopictype) {
		this.applytopictype = applytopictype;
	}

	@Column(name = "postbytopictype", unique = false, nullable = false, insertable = true, updatable = true)
	public Integer getPostbytopictype() {
		return postbytopictype;
	}

	public void setPostbytopictype(Integer postbytopictype) {
		this.postbytopictype = postbytopictype;
	}

	@Column(name = "viewbytopictype", unique = false, nullable = false, insertable = true, updatable = true)
	public Integer getViewbytopictype() {
		return viewbytopictype;
	}

	public void setViewbytopictype(Integer viewbytopictype) {
		this.viewbytopictype = viewbytopictype;
	}

	@Column(name = "topictypeprefix", unique = false, nullable = false, insertable = true, updatable = true)
	public Integer getTopictypeprefix() {
		return topictypeprefix;
	}

	public void setTopictypeprefix(Integer topictypeprefix) {
		this.topictypeprefix = topictypeprefix;
	}

	@Column(name = "permuserlist", unique = false, nullable = true, insertable = true, updatable = true, length = 1073741823)
	public String getPermuserlist() {
		return permuserlist;
	}

	public void setPermuserlist(String permuserlist) {
		this.permuserlist = permuserlist;
	}

}