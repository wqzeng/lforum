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
 * 搜索缓存
 * 
 * @author 黄磊
 *
 */
@Entity
@Table(name = "searchcaches")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Searchcaches implements java.io.Serializable {

	private static final long serialVersionUID = 3590715419545391162L;
	private Integer searchid;
	private Usergroups usergroups;
	private Users users;
	private String keywords;
	private String searchstring;
	private String ip;
	private String postdatetime;
	private String expiration;
	private Integer topics;
	private String tids;

	
	@Id
	@GeneratedValue(generator = "paymentableGenerator")     
	@GenericGenerator(name = "paymentableGenerator", strategy = "increment")  
	@Column(name = "searchid", unique = true, nullable = false, insertable = true, updatable = true)
	public Integer getSearchid() {
		return searchid;
	}

	public void setSearchid(Integer searchid) {
		this.searchid = searchid;
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
	@JoinColumn(name = "uid", unique = false, nullable = false, insertable = true, updatable = true)
	public Users getUsers() {
		return users;
	}

	public void setUsers(Users users) {
		this.users = users;
	}

	@Column(name = "keywords", unique = false, nullable = false, insertable = true, updatable = true)
	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	@Column(name = "searchstring", unique = false, nullable = false, insertable = true, updatable = true)
	public String getSearchstring() {
		return searchstring;
	}

	public void setSearchstring(String searchstring) {
		this.searchstring = searchstring;
	}

	@Column(name = "ip", unique = false, nullable = false, insertable = true, updatable = true, length = 15)
	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	@Column(name = "postdatetime", unique = false, nullable = false, insertable = true, updatable = true, length = 23)
	public String getPostdatetime() {
		return postdatetime;
	}

	public void setPostdatetime(String postdatetime) {
		this.postdatetime = postdatetime;
	}

	@Column(name = "expiration", unique = false, nullable = false, insertable = true, updatable = true, length = 23)
	public String getExpiration() {
		return expiration;
	}

	public void setExpiration(String expiration) {
		this.expiration = expiration;
	}

	@Column(name = "topics", unique = false, nullable = false, insertable = true, updatable = true)
	public Integer getTopics() {
		return topics;
	}

	public void setTopics(Integer topics) {
		this.topics = topics;
	}

	@Column(name = "tids", unique = false, nullable = false, insertable = true, updatable = true)
	public String getTids() {
		return tids;
	}

	public void setTids(String tids) {
		this.tids = tids;
	}

}