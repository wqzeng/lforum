package com.javaeye.lonlysky.lforum.entity.forum;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;

/**
 * 勋章
 * 
 * @author 黄磊
 *
 */
@Entity
@Table(name = "medals")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Medals implements java.io.Serializable {

	private static final long serialVersionUID = -6179820632546837134L;
	private Integer medalid;
	private String name;
	private Integer available;
	private String image;
	private Set<Medalslog> medalslogs = new HashSet<Medalslog>(0);

	
	@Id
	@GeneratedValue(generator = "paymentableGenerator")     
	@GenericGenerator(name = "paymentableGenerator", strategy = "increment")  
	@Column(name = "medalid", unique = true, nullable = false, insertable = true, updatable = true)
	public Integer getMedalid() {
		return medalid;
	}

	public void setMedalid(Integer medalid) {
		this.medalid = medalid;
	}

	@Column(name = "name", unique = false, nullable = false, insertable = true, updatable = true, length = 50)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "available", unique = false, nullable = false, insertable = true, updatable = true)
	public Integer getAvailable() {
		return available;
	}

	public void setAvailable(Integer available) {
		this.available = available;
	}

	@Column(name = "image", unique = false, nullable = false, insertable = true, updatable = true, length = 30)
	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY, mappedBy = "medals")
	public Set<Medalslog> getMedalslogs() {
		return medalslogs;
	}

	public void setMedalslogs(Set<Medalslog> medalslogs) {
		this.medalslogs = medalslogs;
	}

}