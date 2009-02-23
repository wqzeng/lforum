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
 * 辩论主题
 * 
 * @author 黄磊
 *
 */
@Entity
@Table(name = "debatediggs")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Debatediggs implements java.io.Serializable {

	private static final long serialVersionUID = -552136283049705802L;
	private Integer tid;
	private Topics topics;
	private Users users;
	private Postid postid;
	private String digger;
	private String diggerip;
	private Date diggdatetime;

	
	// Property accessors
	@Id
	@GeneratedValue(generator = "paymentableGenerator")     
	@GenericGenerator(name = "paymentableGenerator", strategy = "increment")  
	@Column(name = "tid", unique = true, nullable = false, insertable = true, updatable = true)
	public Integer getTid() {
		return tid;
	}

	public void setTid(Integer tid) {
		this.tid = tid;
	}

	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "tid", unique = true, nullable = false, insertable = false, updatable = false)
	public Topics getTopics() {
		return topics;
	}

	public void setTopics(Topics topics) {
		this.topics = topics;
	}

	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "diggerid", unique = false, nullable = false, insertable = true, updatable = true)
	public Users getUsers() {
		return users;
	}

	public void setUsers(Users users) {
		this.users = users;
	}

	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "pid", unique = false, nullable = false, insertable = true, updatable = true)
	public Postid getPostid() {
		return postid;
	}

	public void setPostid(Postid postid) {
		this.postid = postid;
	}

	@Column(name = "digger", unique = false, nullable = false, insertable = true, updatable = true, length = 20)
	public String getDigger() {
		return digger;
	}

	public void setDigger(String digger) {
		this.digger = digger;
	}

	@Column(name = "diggerip", unique = false, nullable = false, insertable = true, updatable = true, length = 15)
	public String getDiggerip() {
		return diggerip;
	}

	public void setDiggerip(String diggerip) {
		this.diggerip = diggerip;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "diggdatetime", unique = false, nullable = false, insertable = true, updatable = true, length = 23)
	public Date getDiggdatetime() {
		return diggdatetime;
	}

	public void setDiggdatetime(Date diggdatetime) {
		this.diggdatetime = diggdatetime;
	}

}