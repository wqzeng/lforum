package com.javaeye.lonlysky.lforum.entity.help;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;

/**
 * 帮助信息
 * 
 * @author 黄磊
 *
 */
@Entity
@Table(name = "help")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Help implements java.io.Serializable {

	private static final long serialVersionUID = -8487511695652602149L;
	private Integer id;
	private String title;
	private String message;
	private Integer pid;
	private Integer orderby;

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

	@Column(name = "title", unique = false, nullable = false, insertable = true, updatable = true, length = 100)
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Column(name = "message", unique = false, nullable = true, insertable = true, updatable = true, length = 1073741823)
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Column(name = "pid", unique = false, nullable = false, insertable = true, updatable = true)
	public Integer getPid() {
		return pid;
	}

	public void setPid(Integer pid) {
		this.pid = pid;
	}

	@Column(name = "orderby", unique = false, nullable = true, insertable = true, updatable = true)
	public Integer getOrderby() {
		return orderby;
	}

	public void setOrderby(Integer orderby) {
		this.orderby = orderby;
	}

}