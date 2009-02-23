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
 * 后台管理记录
 * 
 * @author 黄磊
 *
 */
@Entity
@Table(name = "adminvisitlog")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Adminvisitlog implements java.io.Serializable {

	// Fields  
	private static final long serialVersionUID = 3193556774700180951L;
	private Integer visitid;
	private Usergroups usergroups;
	private Users users;
	private String username;
	private String grouptitle;
	private String ip;
	private String postdatetime;
	private String actions;
	private String others;

	@Id
	@GeneratedValue(generator = "paymentableGenerator")     
	@GenericGenerator(name = "paymentableGenerator", strategy = "increment")  
	@Column(name = "visitid", unique = true, nullable = false, insertable = true, updatable = true)
	public Integer getVisitid() {
		return visitid;
	}

	public void setVisitid(Integer visitid) {
		this.visitid = visitid;
	}

	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "groupid", unique = false, nullable = true, insertable = true, updatable = true)
	public Usergroups getUsergroups() {
		return usergroups;
	}

	public void setUsergroups(Usergroups usergroups) {
		this.usergroups = usergroups;
	}

	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "uid", unique = false, nullable = true, insertable = true, updatable = true)
	public Users getUsers() {
		return users;
	}

	public void setUsers(Users users) {
		this.users = users;
	}

	@Column(name = "username", unique = false, nullable = false, insertable = true, updatable = true, length = 20)
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Column(name = "grouptitle", unique = false, nullable = false, insertable = true, updatable = true, length = 50)
	public String getGrouptitle() {
		return grouptitle;
	}

	public void setGrouptitle(String grouptitle) {
		this.grouptitle = grouptitle;
	}

	@Column(name = "ip", unique = false, nullable = true, insertable = true, updatable = true, length = 15)
	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	@Column(name = "postdatetime", unique = false, nullable = true, insertable = true, updatable = true, length = 23)
	public String getPostdatetime() {
		return postdatetime;
	}

	public void setPostdatetime(String postdatetime) {
		this.postdatetime = postdatetime;
	}

	@Column(name = "actions", unique = false, nullable = false, insertable = true, updatable = true, length = 100)
	public String getActions() {
		return actions;
	}

	public void setActions(String actions) {
		this.actions = actions;
	}

	@Column(name = "others", unique = false, nullable = false, insertable = true, updatable = true, length = 200)
	public String getOthers() {
		return others;
	}

	public void setOthers(String others) {
		this.others = others;
	}

}