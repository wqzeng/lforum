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
 * 在线图例
 * 
 * @author 黄磊
 *
 */
@Entity
@Table(name = "onlinelist")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Onlinelist implements java.io.Serializable {

	private static final long serialVersionUID = 7781492191700822771L;
	private Integer groupid;
	private Usergroups usergroups;
	private Integer displayorder;
	private String title;
	private String img;

	
	@Id
	@GeneratedValue(generator = "paymentableGenerator")     
	@GenericGenerator(name = "paymentableGenerator", strategy = "increment")  
	@Column(name = "groupid", unique = true, nullable = false, insertable = true, updatable = true)
	public Integer getGroupid() {
		return groupid;
	}

	public void setGroupid(Integer groupid) {
		this.groupid = groupid;
	}

	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "groupid", unique = true, nullable = false, insertable = false, updatable = false)
	public Usergroups getUsergroups() {
		return usergroups;
	}

	public void setUsergroups(Usergroups usergroups) {
		this.usergroups = usergroups;
	}

	@Column(name = "displayorder", unique = false, nullable = true, insertable = true, updatable = true)
	public Integer getDisplayorder() {
		return displayorder;
	}

	public void setDisplayorder(Integer displayorder) {
		this.displayorder = displayorder;
	}

	@Column(name = "title", unique = false, nullable = false, insertable = true, updatable = true, length = 50)
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Column(name = "img", unique = false, nullable = true, insertable = true, updatable = true, length = 50)
	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

}