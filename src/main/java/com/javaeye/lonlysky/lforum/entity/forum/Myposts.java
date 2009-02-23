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
 * 我的主题
 * 
 * @author 黄磊
 *
 */
@Entity
@Table(name = "myposts")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Myposts implements java.io.Serializable {

	private static final long serialVersionUID = 3714302951667264334L;
	private Integer id;
	private Topics topics;
	private Users users;
	private Postid postid;
	private String dateline;


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
	@JoinColumn(name = "tid", unique = false, nullable = false, insertable = true, updatable = true)
	public Topics getTopics() {
		return topics;
	}

	public void setTopics(Topics topics) {
		this.topics = topics;
	}

	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "uid", unique = false, nullable = false, insertable = true, updatable = true)
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

	@Column(name = "dateline", unique = false, nullable = false, insertable = true, updatable = true, length = 16)
	public String getDateline() {
		return dateline;
	}

	public void setDateline(String dateline) {
		this.dateline = dateline;
	}

}