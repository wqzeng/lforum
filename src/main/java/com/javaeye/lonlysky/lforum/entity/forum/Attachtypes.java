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
 * 附件类型
 * 
 * @author 黄磊
 *
 */
@Entity
@Table(name = "attachtypes")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Attachtypes implements java.io.Serializable {

	// Fields 
	private static final long serialVersionUID = 3117259075201428733L;
	private Integer id;
	private String extension;
	private Integer maxsize;

	
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

	@Column(name = "extension", unique = false, nullable = false, insertable = true, updatable = true, length = 256)
	public String getExtension() {
		return extension;
	}

	public void setExtension(String extension) {
		this.extension = extension;
	}

	@Column(name = "maxsize", unique = false, nullable = false, insertable = true, updatable = true)
	public Integer getMaxsize() {
		return maxsize;
	}

	public void setMaxsize(Integer maxsize) {
		this.maxsize = maxsize;
	}

}