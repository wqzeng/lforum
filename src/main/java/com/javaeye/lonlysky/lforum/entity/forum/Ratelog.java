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
 * 积分评定记录
 * 
 * @author 黄磊
 *
 */
@Entity
@Table(name = "ratelog")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Ratelog implements java.io.Serializable {

	private static final long serialVersionUID = 5938144160367549501L;
	private Integer id;
	private Users users;
	private Postid postid;
	private String username;
	private Integer extcredits;
	private String postdatetime;
	private Integer score;
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

	@ManyToOne(cascade = {}, fetch = FetchType.EAGER)
	@JoinColumn(name = "uid", unique = false, nullable = false, insertable = true, updatable = true)
	public Users getUsers() {
		return users;
	}

	public void setUsers(Users users) {
		this.users = users;
	}

	@ManyToOne(cascade = {}, fetch = FetchType.EAGER)
	@JoinColumn(name = "pid", unique = false, nullable = false, insertable = true, updatable = true)
	public Postid getPostid() {
		return postid;
	}

	public void setPostid(Postid postid) {
		this.postid = postid;
	}

	@Column(name = "username", unique = false, nullable = false, insertable = true, updatable = true, length = 20)
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Column(name = "extcredits", unique = false, nullable = false, insertable = true, updatable = true)
	public Integer getExtcredits() {
		return extcredits;
	}

	public void setExtcredits(Integer extcredits) {
		this.extcredits = extcredits;
	}

	@Column(name = "postdatetime", unique = false, nullable = false, insertable = true, updatable = true, length = 23)
	public String getPostdatetime() {
		return postdatetime;
	}

	public void setPostdatetime(String postdatetime) {
		this.postdatetime = postdatetime;
	}

	@Column(name = "score", unique = false, nullable = false, insertable = true, updatable = true)
	public Integer getScore() {
		return score;
	}

	public void setScore(Integer score) {
		this.score = score;
	}

	@Column(name = "reason", unique = false, nullable = false, insertable = true, updatable = true, length = 50)
	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

}