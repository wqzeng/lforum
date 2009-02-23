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
 * 辩论帖
 * 
 * @author 黄磊
 *
 */
@Entity
@Table(name = "postdebatefields")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Postdebatefields implements java.io.Serializable {

	private static final long serialVersionUID = 3439683828887641865L;
	private Integer tid;
	private Topics topics;
	private Postid postid;
	private Integer opinion;
	private Integer diggs;

	
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
	@JoinColumn(name = "pid", unique = false, nullable = false, insertable = true, updatable = true)
	public Postid getPostid() {
		return postid;
	}

	public void setPostid(Postid postid) {
		this.postid = postid;
	}

	@Column(name = "opinion", unique = false, nullable = false, insertable = true, updatable = true)
	public Integer getOpinion() {
		return opinion;
	}

	public void setOpinion(Integer opinion) {
		this.opinion = opinion;
	}

	@Column(name = "diggs", unique = false, nullable = false, insertable = true, updatable = true)
	public Integer getDiggs() {
		return diggs;
	}

	public void setDiggs(Integer diggs) {
		this.diggs = diggs;
	}

}