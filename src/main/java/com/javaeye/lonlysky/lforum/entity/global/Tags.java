package com.javaeye.lonlysky.lforum.entity.global;

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

import com.javaeye.lonlysky.lforum.entity.forum.Users;

/**
 * 关键词
 * 
 * @author 黄磊
 *
 */
@Entity
@Table(name = "tags")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Tags implements java.io.Serializable {

	private static final long serialVersionUID = -3118334288093065627L;
	private Integer tagid;
	private Users users;
	private String tagname;
	private String postdatetime;
	private Integer orderid;
	private String color;
	private Integer count;
	private Integer fcount;
	private Integer pcount;
	private Integer scount;
	private Integer vcount;
	private Integer gcount;

	@Id
	@GeneratedValue(generator = "paymentableGenerator")
	@GenericGenerator(name = "paymentableGenerator", strategy = "increment")
	@Column(name = "tagid", unique = true, nullable = false, insertable = true, updatable = true)
	public Integer getTagid() {
		return tagid;
	}

	public void setTagid(Integer tagid) {
		this.tagid = tagid;
	}

	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "userid", unique = false, nullable = false, insertable = true, updatable = true)
	public Users getUsers() {
		return users;
	}

	public void setUsers(Users users) {
		this.users = users;
	}

	@Column(name = "tagname", unique = false, nullable = false, insertable = true, updatable = true, length = 10)
	public String getTagname() {
		return tagname;
	}

	public void setTagname(String tagname) {
		this.tagname = tagname;
	}

	@Column(name = "postdatetime", unique = false, nullable = false, insertable = true, updatable = true, length = 23)
	public String getPostdatetime() {
		return postdatetime;
	}

	public void setPostdatetime(String postdatetime) {
		this.postdatetime = postdatetime;
	}

	@Column(name = "orderid", unique = false, nullable = false, insertable = true, updatable = true)
	public Integer getOrderid() {
		return orderid;
	}

	public void setOrderid(Integer orderid) {
		this.orderid = orderid;
	}

	@Column(name = "color", unique = false, nullable = false, insertable = true, updatable = true, length = 6)
	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	@Column(name = "count", unique = false, nullable = false, insertable = true, updatable = true)
	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	@Column(name = "fcount", unique = false, nullable = false, insertable = true, updatable = true)
	public Integer getFcount() {
		return fcount;
	}

	public void setFcount(Integer fcount) {
		this.fcount = fcount;
	}

	@Column(name = "pcount", unique = false, nullable = false, insertable = true, updatable = true)
	public Integer getPcount() {
		return pcount;
	}

	public void setPcount(Integer pcount) {
		this.pcount = pcount;
	}

	@Column(name = "scount", unique = false, nullable = false, insertable = true, updatable = true)
	public Integer getScount() {
		return scount;
	}

	public void setScount(Integer scount) {
		this.scount = scount;
	}

	@Column(name = "vcount", unique = false, nullable = false, insertable = true, updatable = true)
	public Integer getVcount() {
		return vcount;
	}

	public void setVcount(Integer vcount) {
		this.vcount = vcount;
	}

	@Column(name = "gcount", unique = false, nullable = false, insertable = true, updatable = true)
	public Integer getGcount() {
		return gcount;
	}

	public void setGcount(Integer gcount) {
		this.gcount = gcount;
	}

}