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
 * 主题提问
 * 
 * @author 黄磊
 *
 */
@Entity
@Table(name = "bonuslog")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Bonuslog implements java.io.Serializable {

	// Fields  
	private static final long serialVersionUID = -3769966194024374540L;
	private Integer id;
	private Users usersByAnswerid;
	private Topics topics;
	private Users usersByAuthorid;
	private Postid postid;
	private String answername;
	private String dateline;
	private Integer bonus;
	private Integer extid;
	private Integer isbest;

	// Property accessors
	@Id
	@GeneratedValue(generator = "paymentableGenerator")
	@GenericGenerator(name = "paymentableGenerator", strategy = "increment")
	@Column(name = "tid", unique = true, nullable = false, insertable = true, updatable = true)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "answerid", unique = false, nullable = true, insertable = true, updatable = true)
	public Users getUsersByAnswerid() {
		return usersByAnswerid;
	}

	public void setUsersByAnswerid(Users usersByAnswerid) {
		this.usersByAnswerid = usersByAnswerid;
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
	@JoinColumn(name = "authorid", unique = false, nullable = false, insertable = true, updatable = true)
	public Users getUsersByAuthorid() {
		return usersByAuthorid;
	}

	public void setUsersByAuthorid(Users usersByAuthorid) {
		this.usersByAuthorid = usersByAuthorid;
	}

	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "pid", unique = false, nullable = false, insertable = true, updatable = true)
	public Postid getPostid() {
		return postid;
	}

	public void setPostid(Postid postid) {
		this.postid = postid;
	}

	@Column(name = "answername", unique = false, nullable = false, insertable = true, updatable = true, length = 20)
	public String getAnswername() {
		return answername;
	}

	public void setAnswername(String answername) {
		this.answername = answername;
	}

	@Column(name = "dateline", unique = false, nullable = false, insertable = true, updatable = true, length = 23)
	public String getDateline() {
		return dateline;
	}

	public void setDateline(String dateline) {
		this.dateline = dateline;
	}

	@Column(name = "bonus", unique = false, nullable = false, insertable = true, updatable = true)
	public Integer getBonus() {
		return bonus;
	}

	public void setBonus(Integer bonus) {
		this.bonus = bonus;
	}

	@Column(name = "extid", unique = false, nullable = false, insertable = true, updatable = true)
	public Integer getExtid() {
		return extid;
	}

	public void setExtid(Integer extid) {
		this.extid = extid;
	}

	@Column(name = "isbest", unique = false, nullable = false, insertable = true, updatable = true)
	public Integer getIsbest() {
		return isbest;
	}

	public void setIsbest(Integer isbest) {
		this.isbest = isbest;
	}

}