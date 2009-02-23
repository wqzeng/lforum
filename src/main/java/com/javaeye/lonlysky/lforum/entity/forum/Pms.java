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
 * 论坛短信
 * 
 * @author 黄磊
 *
 */
@Entity
@Table(name = "pms")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Pms implements java.io.Serializable {

	private static final long serialVersionUID = -3074493004253118769L;
	private Integer pmid;
	private Users usersByMsgfromid;
	private Users usersByMsgtoid;
	private String msgfrom;
	private String msgto;
	private Integer folder;
	private Integer new_;
	private String subject;
	private String postdatetime;
	private String message;

	
	@Id
	@GeneratedValue(generator = "paymentableGenerator")     
	@GenericGenerator(name = "paymentableGenerator", strategy = "increment")  
	@Column(name = "pmid", unique = true, nullable = false, insertable = true, updatable = true)
	public Integer getPmid() {
		return pmid;
	}

	public void setPmid(Integer pmid) {
		this.pmid = pmid;
	}

	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "msgfromid", unique = false, nullable = false, insertable = true, updatable = true)
	public Users getUsersByMsgfromid() {
		return usersByMsgfromid;
	}

	public void setUsersByMsgfromid(Users usersByMsgfromid) {
		this.usersByMsgfromid = usersByMsgfromid;
	}

	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "msgtoid", unique = false, nullable = false, insertable = true, updatable = true)
	public Users getUsersByMsgtoid() {
		return usersByMsgtoid;
	}

	public void setUsersByMsgtoid(Users usersByMsgtoid) {
		this.usersByMsgtoid = usersByMsgtoid;
	}

	@Column(name = "msgfrom", unique = false, nullable = false, insertable = true, updatable = true, length = 50)
	public String getMsgfrom() {
		return msgfrom;
	}

	public void setMsgfrom(String msgfrom) {
		this.msgfrom = msgfrom;
	}

	@Column(name = "msgto", unique = false, nullable = false, insertable = true, updatable = true, length = 50)
	public String getMsgto() {
		return msgto;
	}

	public void setMsgto(String msgto) {
		this.msgto = msgto;
	}

	@Column(name = "folder", unique = false, nullable = false, insertable = true, updatable = true)
	public Integer getFolder() {
		return folder;
	}

	public void setFolder(Integer folder) {
		this.folder = folder;
	}

	@Column(name = "new", unique = false, nullable = false, insertable = true, updatable = true)
	public Integer getNew_() {
		return new_;
	}

	public void setNew_(Integer new_) {
		this.new_ = new_;
	}

	@Column(name = "subject", unique = false, nullable = false, insertable = true, updatable = true, length = 60)
	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	@Column(name = "postdatetime", unique = false, nullable = false, insertable = true, updatable = true, length = 23)
	public String getPostdatetime() {
		return postdatetime;
	}

	public void setPostdatetime(String postdatetime) {
		this.postdatetime = postdatetime;
	}

	@Column(name = "message", unique = false, nullable = false, insertable = true, updatable = true, length = 1073741823)
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}