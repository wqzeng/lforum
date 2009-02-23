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
 * 交易记录
 * 
 * @author 黄磊
 *
 */
@Entity
@Table(name = "paymentlog")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Paymentlog implements java.io.Serializable {

	private static final long serialVersionUID = 4926879716320830400L;
	private Integer id;
	private Topics topics;
	private Users usersByUid;
	private Users usersByAuthorid;
	private String buydate;
	private Integer amount;
	private Integer netamount;

	
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

	@ManyToOne(cascade = {}, fetch = FetchType.EAGER)
	@JoinColumn(name = "uid", unique = false, nullable = false, insertable = true, updatable = true)
	public Users getUsersByUid() {
		return usersByUid;
	}

	public void setUsersByUid(Users usersByUid) {
		this.usersByUid = usersByUid;
	}

	@ManyToOne(cascade = {}, fetch = FetchType.EAGER)
	@JoinColumn(name = "authorid", unique = false, nullable = false, insertable = true, updatable = true)
	public Users getUsersByAuthorid() {
		return usersByAuthorid;
	}

	public void setUsersByAuthorid(Users usersByAuthorid) {
		this.usersByAuthorid = usersByAuthorid;
	}

	@Column(name = "buydate", unique = false, nullable = false, insertable = true, updatable = true, length = 23)
	public String getBuydate() {
		return buydate;
	}

	public void setBuydate(String buydate) {
		this.buydate = buydate;
	}

	@Column(name = "amount", unique = false, nullable = false, insertable = true, updatable = true)
	public Integer getAmount() {
		return amount;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}

	@Column(name = "netamount", unique = false, nullable = false, insertable = true, updatable = true)
	public Integer getNetamount() {
		return netamount;
	}

	public void setNetamount(Integer netamount) {
		this.netamount = netamount;
	}

}