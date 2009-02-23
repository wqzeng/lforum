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
 * 表情
 * 
 * @author 黄磊
 *
 */
@Entity
@Table(name = "smilies")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Smilies implements java.io.Serializable {

	private static final long serialVersionUID = -3466244304364803997L;
	private Integer id;
	private Integer displayorder;
	private Integer type;
	private String code;
	private String url;

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

	@Column(name = "type", unique = false, nullable = false, insertable = true, updatable = true)
	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	@Column(name = "code", unique = false, nullable = false, insertable = true, updatable = true, length = 30)
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Column(name = "url", unique = false, nullable = false, insertable = true, updatable = true, length = 60)
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}