package com.javaeye.lonlysky.lforum.entity.forum;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;

/**
 * 预定任务
 * 
 * @author 黄磊
 *
 */
@Entity
@Table(name = "scheduledevents")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Scheduledevents implements java.io.Serializable {

	private static final long serialVersionUID = 5648916946116346172L;
	private Integer scheduleId;
	private String key;
	private Date lastexecuted;
	private String servername;

	
	@Id
	@GeneratedValue(generator = "paymentableGenerator")     
	@GenericGenerator(name = "paymentableGenerator", strategy = "increment")  
	@Column(name = "scheduleID", unique = true, nullable = false, insertable = true, updatable = true)
	public Integer getScheduleId() {
		return scheduleId;
	}

	public void setScheduleId(Integer scheduleId) {
		this.scheduleId = scheduleId;
	}

	@Column(name = "key", unique = false, nullable = false, insertable = true, updatable = true, length = 50)
	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "lastexecuted", unique = false, nullable = false, insertable = true, updatable = true, length = 23)
	public Date getLastexecuted() {
		return lastexecuted;
	}

	public void setLastexecuted(Date lastexecuted) {
		this.lastexecuted = lastexecuted;
	}

	@Column(name = "servername", unique = false, nullable = false, insertable = true, updatable = true, length = 100)
	public String getServername() {
		return servername;
	}

	public void setServername(String servername) {
		this.servername = servername;
	}

}