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
 * 公告信息
 * 
 * @author 黄磊
 *
 */
@Entity
@Table(name = "announcements")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Announcements implements java.io.Serializable {

	// Fields 
	private static final long serialVersionUID = -2316380186182554777L;
	private Integer id;
	private Users users;
	private String poster;
	private String title;
	private Integer displayorder;
	private String starttime;
	private String endtime;
	private String message;

	
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
	@JoinColumn(name = "posterid", unique = false, nullable = false, insertable = true, updatable = true)
	public Users getUsers() {
		return users;
	}

	public void setUsers(Users users) {
		this.users = users;
	}

	@Column(name = "poster", unique = false, nullable = false, insertable = true, updatable = true, length = 20)
	public String getPoster() {
		return poster;
	}

	public void setPoster(String poster) {
		this.poster = poster;
	}

	@Column(name = "title", unique = false, nullable = false, insertable = true, updatable = true, length = 250)
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Column(name = "displayorder", unique = false, nullable = false, insertable = true, updatable = true)
	public Integer getDisplayorder() {
		return displayorder;
	}

	public void setDisplayorder(Integer displayorder) {
		this.displayorder = displayorder;
	}

	@Column(name = "starttime", unique = false, nullable = false, insertable = true, updatable = true, length = 23)
	public String getStarttime() {
		return starttime;
	}

	public void setStarttime(String starttime) {
		this.starttime = starttime;
	}

	@Column(name = "endtime", unique = false, nullable = false, insertable = true, updatable = true, length = 23)
	public String getEndtime() {
		return endtime;
	}

	public void setEndtime(String endtime) {
		this.endtime = endtime;
	}

	@Column(name = "message", unique = false, nullable = false, insertable = true, updatable = true, length = 1073741823)
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}