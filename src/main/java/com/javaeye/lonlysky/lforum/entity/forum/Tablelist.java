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
 * 帖子数据表
 * 
 * @author 黄磊
 *
 */
@Entity
@Table(name = "tablelist")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Tablelist implements java.io.Serializable {

	private static final long serialVersionUID = 5717299965016128439L;
	private Integer id;
	private Date createdatetime;
	private String description;
	private Integer mintid;
	private Integer maxtid;

	
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

	@Temporal(TemporalType.DATE)
	@Column(name = "createdatetime", unique = false, nullable = false, insertable = true, updatable = true, length = 23)
	public Date getCreatedatetime() {
		return createdatetime;
	}

	public void setCreatedatetime(Date createdatetime) {
		this.createdatetime = createdatetime;
	}

	@Column(name = "description", unique = false, nullable = false, insertable = true, updatable = true, length = 50)
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Column(name = "mintid", unique = false, nullable = false, insertable = true, updatable = true)
	public Integer getMintid() {
		return mintid;
	}

	public void setMintid(Integer mintid) {
		this.mintid = mintid;
	}

	@Column(name = "maxtid", unique = false, nullable = false, insertable = true, updatable = true)
	public Integer getMaxtid() {
		return maxtid;
	}

	public void setMaxtid(Integer maxtid) {
		this.maxtid = maxtid;
	}

}