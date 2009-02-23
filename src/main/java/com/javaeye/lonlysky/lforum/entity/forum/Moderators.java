package com.javaeye.lonlysky.lforum.entity.forum;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;


/**
 * 版主信息
 * 
 * @author 黄磊
 *
 */
@Entity
@Table(name = "moderators")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Moderators implements java.io.Serializable {

	private static final long serialVersionUID = 7481887546308547354L;
	private Integer id;
	private Users users;
	private Forums forums;
	private Integer displayorder;
	private Integer inherited;

	
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

	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "uid", unique = false, nullable = false, insertable = true, updatable = true)
	public Users getUsers() {
		return users;
	}

	public void setUsers(Users users) {
		this.users = users;
	}

	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "fid", unique = false, nullable = false, insertable = true, updatable = true)
	public Forums getForums() {
		return forums;
	}

	public void setForums(Forums forums) {
		this.forums = forums;
	}

	@Column(name = "displayorder", unique = false, nullable = false, insertable = true, updatable = true)
	public Integer getDisplayorder() {
		return displayorder;
	}

	public void setDisplayorder(Integer displayorder) {
		this.displayorder = displayorder;
	}

	@Column(name = "inherited", unique = false, nullable = false, insertable = true, updatable = true)
	public Integer getInherited() {
		return inherited;
	}

	public void setInherited(Integer inherited) {
		this.inherited = inherited;
	}

}