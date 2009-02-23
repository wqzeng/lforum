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
 * 在线时间
 * 
 * @author 黄磊
 *
 */
@Entity
@Table(name = "onlinetime")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Onlinetime implements java.io.Serializable {

	private static final long serialVersionUID = 5654049471687455355L;
	private Integer uid;
	private Users users;
	private Integer thismonth;
	private Integer total;
	private Date lastupdate;

	
	@Id
	@GeneratedValue(generator = "paymentableGenerator")     
	@GenericGenerator(name = "paymentableGenerator", strategy = "increment")  
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

	@Column(name = "thismonth", unique = false, nullable = false, insertable = true, updatable = true)
	public Integer getThismonth() {
		return thismonth;
	}

	public void setThismonth(Integer thismonth) {
		this.thismonth = thismonth;
	}

	@Column(name = "total", unique = false, nullable = false, insertable = true, updatable = true)
	public Integer getTotal() {
		return total;
	}

	public void setTotal(Integer total) {
		this.total = total;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "lastupdate", unique = false, nullable = false, insertable = true, updatable = true, length = 23)
	public Date getLastupdate() {
		return lastupdate;
	}

	public void setLastupdate(Date lastupdate) {
		this.lastupdate = lastupdate;
	}

}