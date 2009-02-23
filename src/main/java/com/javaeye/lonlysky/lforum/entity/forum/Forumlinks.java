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
 * 友情链接
 * 
 * @author 黄磊
 *
 */
@Entity
@Table(name = "forumlinks")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Forumlinks implements java.io.Serializable {

	private static final long serialVersionUID = -8292284706301428673L;
	private Integer id;
	private Integer displayorder;
	private String name;
	private String url;
	private String note;
	private String logo;

	
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

	@Column(name = "displayorder", unique = false, nullable = false, insertable = true, updatable = true)
	public Integer getDisplayorder() {
		return displayorder;
	}

	public void setDisplayorder(Integer displayorder) {
		this.displayorder = displayorder;
	}

	@Column(name = "name", unique = false, nullable = false, insertable = true, updatable = true, length = 100)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "url", unique = false, nullable = false, insertable = true, updatable = true, length = 100)
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Column(name = "note", unique = false, nullable = false, insertable = true, updatable = true, length = 200)
	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	@Column(name = "logo", unique = false, nullable = false, insertable = true, updatable = true, length = 100)
	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

}