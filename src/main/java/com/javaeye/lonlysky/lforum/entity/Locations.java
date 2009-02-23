package com.javaeye.lonlysky.lforum.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;

/**
 * 地址
 * 
 * @author 黄磊
 *
 */
@Entity
@Table(name = "locations")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Locations implements java.io.Serializable {

	private static final long serialVersionUID = -3136389898455798460L;
	private Integer lid;
	private String city;
	private String state;
	private String country;
	private String zipcode;
	
	@Id
	@GeneratedValue(generator = "paymentableGenerator")     
	@GenericGenerator(name = "paymentableGenerator", strategy = "increment")  
	@Column(name = "lid", unique = true, nullable = false, insertable = true, updatable = true)
	public Integer getLid() {
		return lid;
	}

	public void setLid(Integer lid) {
		this.lid = lid;
	}

	@Column(name = "city", unique = false, nullable = false, insertable = true, updatable = true, length = 50)
	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	@Column(name = "state", unique = false, nullable = false, insertable = true, updatable = true, length = 50)
	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	@Column(name = "country", unique = false, nullable = false, insertable = true, updatable = true, length = 50)
	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	@Column(name = "zipcode", unique = false, nullable = false, insertable = true, updatable = true, length = 20)
	public String getZipcode() {
		return zipcode;
	}

	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}

}