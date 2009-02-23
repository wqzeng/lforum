package com.javaeye.lonlysky.lforum.entity.forum;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;


/**
 * 勋章发放记录
 * 
 * @author 黄磊
 *
 */
@Entity
@Table(name = "medalslog")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Medalslog implements java.io.Serializable {

	private static final long serialVersionUID = 6813022840354897870L;
	private Integer id;
	private Users users;
	private Medals medals;
	private Admingroups admingroups;
	private String adminname;
	private String ip;
	private Date postdatetime;
	private String username;
	private String actions;
	private String reason;

	
	@Id
	@GeneratedValue(generator = "paymentableGenerator")     
	@GenericGenerator(name = "paymentableGenerator", strategy = "increment")  
	@Column(name = "id", unique = true, nullable = false, insertable = true, updatable = true)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "uid", unique = false, nullable = true, insertable = true, updatable = true)
	public Users getUsers() {
		return users;
	}

	public void setUsers(Users users) {
		this.users = users;
	}

	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "medals", unique = false, nullable = true, insertable = true, updatable = true)
	public Medals getMedals() {
		return medals;
	}

	public void setMedals(Medals medals) {
		this.medals = medals;
	}

	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "admingid", unique = false, nullable = true, insertable = true, updatable = true)
	public Admingroups getAdmingroups() {
		return admingroups;
	}

	public void setAdmingroups(Admingroups admingroups) {
		this.admingroups = admingroups;
	}

	@Column(name = "adminname", unique = false, nullable = true, insertable = true, updatable = true, length = 50)
	public String getAdminname() {
		return adminname;
	}

	public void setAdminname(String adminname) {
		this.adminname = adminname;
	}

	@Column(name = "ip", unique = false, nullable = true, insertable = true, updatable = true, length = 15)
	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "postdatetime", unique = false, nullable = true, insertable = true, updatable = true, length = 23)
	public Date getPostdatetime() {
		return postdatetime;
	}

	public void setPostdatetime(Date postdatetime) {
		this.postdatetime = postdatetime;
	}

	@Column(name = "username", unique = false, nullable = true, insertable = true, updatable = true, length = 50)
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Column(name = "actions", unique = false, nullable = true, insertable = true, updatable = true, length = 100)
	public String getActions() {
		return actions;
	}

	public void setActions(String actions) {
		this.actions = actions;
	}

	@Column(name = "reason", unique = false, nullable = true, insertable = true, updatable = true, length = 100)
	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

}