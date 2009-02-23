package com.javaeye.lonlysky.lforum.entity.forum;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * 错误登录日志
 * 
 * @author 黄磊
 *
 */
@Entity
@Table(name = "failedlogins")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Failedlogins implements java.io.Serializable {

	private static final long serialVersionUID = -2469361002314606009L;
	private String ip;
	private Integer errcount;
	private String lastupdate;

	
	// Property accessors
	@Id
	@Column(name = "ip", unique = true, nullable = false, insertable = true, updatable = true, length = 15)
	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	@Column(name = "errcount", unique = false, nullable = false, insertable = true, updatable = true)
	public Integer getErrcount() {
		return errcount;
	}

	public void setErrcount(Integer errcount) {
		this.errcount = errcount;
	}

	@Column(name = "lastupdate", unique = false, nullable = false, insertable = true, updatable = true, length = 16)
	public String getLastupdate() {
		return lastupdate;
	}

	public void setLastupdate(String lastupdate) {
		this.lastupdate = lastupdate;
	}

}