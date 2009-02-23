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
 * 积分操作记录
 * 
 * @author 黄磊
 *
 */
@Entity
@Table(name = "creditslog")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Creditslog implements java.io.Serializable {

	private static final long serialVersionUID = -2812437575997570875L;
	private Integer id;
	private Users usersByFromto;
	private Users usersByUid;
	private Integer sendcredits;
	private Integer receivecredits;
	private Float send;
	private Float receive;
	private String paydate;
	private Integer operation;

	
	// Property accessors
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
	@JoinColumn(name = "fromto", unique = false, nullable = false, insertable = true, updatable = true)
	public Users getUsersByFromto() {
		return usersByFromto;
	}

	public void setUsersByFromto(Users usersByFromto) {
		this.usersByFromto = usersByFromto;
	}

	@ManyToOne(cascade = {}, fetch = FetchType.EAGER)
	@JoinColumn(name = "uid", unique = false, nullable = false, insertable = true, updatable = true)
	public Users getUsersByUid() {
		return usersByUid;
	}

	public void setUsersByUid(Users usersByUid) {
		this.usersByUid = usersByUid;
	}

	@Column(name = "sendcredits", unique = false, nullable = false, insertable = true, updatable = true)
	public Integer getSendcredits() {
		return sendcredits;
	}

	public void setSendcredits(Integer sendcredits) {
		this.sendcredits = sendcredits;
	}

	@Column(name = "receivecredits", unique = false, nullable = false, insertable = true, updatable = true)
	public Integer getReceivecredits() {
		return receivecredits;
	}

	public void setReceivecredits(Integer receivecredits) {
		this.receivecredits = receivecredits;
	}

	@Column(name = "send", unique = false, nullable = false, insertable = true, updatable = true, precision = 53, scale = 0)
	public Float getSend() {
		return send;
	}

	public void setSend(Float send) {
		this.send = send;
	}

	@Column(name = "receive", unique = false, nullable = false, insertable = true, updatable = true, precision = 53, scale = 0)
	public Float getReceive() {
		return receive;
	}

	public void setReceive(Float receive) {
		this.receive = receive;
	}

	@Column(name = "paydate", unique = false, nullable = false, insertable = true, updatable = true, length = 23)
	public String getPaydate() {
		return paydate;
	}

	public void setPaydate(String paydate) {
		this.paydate = paydate;
	}

	@Column(name = "operation", unique = false, nullable = false, insertable = true, updatable = true)
	public Integer getOperation() {
		return operation;
	}

	public void setOperation(Integer operation) {
		this.operation = operation;
	}

}