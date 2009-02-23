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
 * 系统风格
 * 
 * @author 黄磊
 *
 */
@Entity
@Table(name = "templates")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Templates implements java.io.Serializable {

	private static final long serialVersionUID = 8674022659696975468L;
	private Integer templateid;
	private String directory;
	private String name;
	private String author;
	private String createdate;
	private String ver;
	private String fordntver;
	private String copyright;

	
	@Id
	@GeneratedValue(generator = "paymentableGenerator")     
	@GenericGenerator(name = "paymentableGenerator", strategy = "increment")  
	@Column(name = "templateid", unique = true, nullable = false, insertable = true, updatable = true)
	public Integer getTemplateid() {
		return templateid;
	}

	public void setTemplateid(Integer templateid) {
		this.templateid = templateid;
	}

	@Column(name = "directory", unique = false, nullable = false, insertable = true, updatable = true, length = 100)
	public String getDirectory() {
		return directory;
	}

	public void setDirectory(String directory) {
		this.directory = directory;
	}

	@Column(name = "name", unique = false, nullable = false, insertable = true, updatable = true, length = 50)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "author", unique = false, nullable = false, insertable = true, updatable = true, length = 100)
	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	@Column(name = "createdate", unique = false, nullable = false, insertable = true, updatable = true, length = 50)
	public String getCreatedate() {
		return createdate;
	}

	public void setCreatedate(String createdate) {
		this.createdate = createdate;
	}

	@Column(name = "ver", unique = false, nullable = false, insertable = true, updatable = true, length = 100)
	public String getVer() {
		return ver;
	}

	public void setVer(String ver) {
		this.ver = ver;
	}

	@Column(name = "fordntver", unique = false, nullable = false, insertable = true, updatable = true, length = 100)
	public String getFordntver() {
		return fordntver;
	}

	public void setFordntver(String fordntver) {
		this.fordntver = fordntver;
	}

	@Column(name = "copyright", unique = false, nullable = false, insertable = true, updatable = true, length = 100)
	public String getCopyright() {
		return copyright;
	}

	public void setCopyright(String copyright) {
		this.copyright = copyright;
	}

}