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
 * 过滤词
 * 
 * @author 黄磊
 *
 */
@Entity
@Table(name = "words")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Words implements java.io.Serializable {

	private static final long serialVersionUID = 3271484243565024581L;
	private Integer id;
	private String admin;
	private String find;
	private String replacement;

	
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

	@Column(name = "admin", unique = false, nullable = false, insertable = true, updatable = true, length = 20)
	public String getAdmin() {
		return admin;
	}

	public void setAdmin(String admin) {
		this.admin = admin;
	}

	@Column(name = "find", unique = false, nullable = false, insertable = true, updatable = true)
	public String getFind() {
		return find;
	}

	public void setFind(String find) {
		this.find = find;
	}

	@Column(name = "replacement", unique = false, nullable = false, insertable = true, updatable = true)
	public String getReplacement() {
		return replacement;
	}

	public void setReplacement(String replacement) {
		this.replacement = replacement;
	}

}