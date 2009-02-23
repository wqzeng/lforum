package com.javaeye.lonlysky.lforum.entity.forum;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;

/**
 * 广告
 * 
 * @author 黄磊
 *
 */
@Entity
@Table(name = "advertisements")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Advertisements implements java.io.Serializable {

	// Fields    
	private static final long serialVersionUID = 1143965360436564863L;
	private Integer advid;
	private Integer available;
	private String type;
	private Integer displayorder;
	private String title;
	private String targets;
	private Date starttime;
	private Date endtime;
	private String code;
	private String parameters;


	// Property accessors
	@Id
	@GeneratedValue(generator = "paymentableGenerator")     
	@GenericGenerator(name = "paymentableGenerator", strategy = "increment")  
	@Column(name = "advid", unique = true, nullable = false, insertable = true, updatable = true)
	public Integer getAdvid() {
		return advid;
	}

	public void setAdvid(Integer advid) {
		this.advid = advid;
	}

	@Column(name = "available", unique = false, nullable = false, insertable = true, updatable = true)
	public Integer getAvailable() {
		return available;
	}

	public void setAvailable(Integer available) {
		this.available = available;
	}

	@Column(name = "type", unique = false, nullable = false, insertable = true, updatable = true, length = 50)
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Column(name = "displayorder", unique = false, nullable = false, insertable = true, updatable = true)
	public Integer getDisplayorder() {
		return displayorder;
	}

	public void setDisplayorder(Integer displayorder) {
		this.displayorder = displayorder;
	}

	@Column(name = "title", unique = false, nullable = false, insertable = true, updatable = true, length = 50)
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Column(name = "targets", unique = false, nullable = false, insertable = true, updatable = true)
	public String getTargets() {
		return targets;
	}

	public void setTargets(String targets) {
		this.targets = targets;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "starttime", unique = false, nullable = false, insertable = true, updatable = true, length = 23)
	public Date getStarttime() {
		return starttime;
	}

	public void setStarttime(Date starttime) {
		this.starttime = starttime;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "endtime", unique = false, nullable = false, insertable = true, updatable = true, length = 23)
	public Date getEndtime() {
		return endtime;
	}

	public void setEndtime(Date endtime) {
		this.endtime = endtime;
	}

	@Column(name = "code", unique = false, nullable = false, insertable = true, updatable = true, length = 1073741823)
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Column(name = "parameters", unique = false, nullable = false, insertable = true, updatable = true, length = 1073741823)
	public String getParameters() {
		return parameters;
	}

	public void setParameters(String parameters) {
		this.parameters = parameters;
	}

}