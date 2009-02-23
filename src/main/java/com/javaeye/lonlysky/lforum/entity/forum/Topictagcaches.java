package com.javaeye.lonlysky.lforum.entity.forum;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;

/**
 * 主题缓存
 * 
 * @author 黄磊
 *
 */
@Entity
@Table(name = "topictagcaches")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Topictagcaches implements java.io.Serializable {

	private static final long serialVersionUID = 8104137587768021579L;
	private Integer tid;
	private Integer linktid;
	private String linktitle;

	
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

	@Column(name = "linktid", unique = false, nullable = false, insertable = true, updatable = true)
	public Integer getLinktid() {
		return linktid;
	}

	public void setLinktid(Integer linktid) {
		this.linktid = linktid;
	}

	@Column(name = "linktitle", unique = false, nullable = false, insertable = true, updatable = true, length = 60)
	public String getLinktitle() {
		return linktitle;
	}

	public void setLinktitle(String linktitle) {
		this.linktitle = linktitle;
	}

}