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
 * 主题鉴定
 * 
 * @author 黄磊
 *
 */
@Entity
@Table(name = "topicidentify")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Topicidentify implements java.io.Serializable {

	private static final long serialVersionUID = -5095239383160606606L;
	private Integer identifyid;
	private String name;
	private String filename;

	
	@Id
	@GeneratedValue(generator = "paymentableGenerator")     
	@GenericGenerator(name = "paymentableGenerator", strategy = "increment")  
	@Column(name = "identifyid", unique = true, nullable = false, insertable = true, updatable = true)
	public Integer getIdentifyid() {
		return identifyid;
	}

	public void setIdentifyid(Integer identifyid) {
		this.identifyid = identifyid;
	}

	@Column(name = "name", unique = false, nullable = false, insertable = true, updatable = true, length = 50)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "filename", unique = false, nullable = false, insertable = true, updatable = true, length = 50)
	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

}