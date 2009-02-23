package com.javaeye.lonlysky.lforum.entity.forum;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;


/**
 * 附件
 * 
 * @author 黄磊
 *
 */
@Entity
@Table(name = "attachments")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Attachments implements java.io.Serializable {

	// Fields    
	private static final long serialVersionUID = -2303854176001461724L;
	private Integer aid;
	private Topics topics;
	private Users users;
	private Postid postid;
	private String postdatetime;
	private Integer readperm;
	private String filename;
	private String description;
	private String filetype;
	private Integer filesize;
	private String attachment;
	private Integer downloads;
	private Set<Myattachments> myattachmentses = new HashSet<Myattachments>(0);

	
	// Property accessors
	@Id
	@GeneratedValue(generator = "paymentableGenerator")     
	@GenericGenerator(name = "paymentableGenerator", strategy = "increment")  
	@Column(name = "aid", unique = true, nullable = false, insertable = true, updatable = true)
	public Integer getAid() {
		return aid;
	}

	public void setAid(Integer aid) {
		this.aid = aid;
	}

	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "tid", unique = false, nullable = false, insertable = true, updatable = true)
	public Topics getTopics() {
		return topics;
	}

	public void setTopics(Topics topics) {
		this.topics = topics;
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
	@JoinColumn(name = "pid", unique = false, nullable = false, insertable = true, updatable = true)
	public Postid getPostid() {
		return postid;
	}

	public void setPostid(Postid postid) {
		this.postid = postid;
	}

	@Column(name = "postdatetime", unique = false, nullable = false, insertable = true, updatable = true, length = 23)
	public String getPostdatetime() {
		return postdatetime;
	}

	public void setPostdatetime(String postdatetime) {
		this.postdatetime = postdatetime;
	}

	@Column(name = "readperm", unique = false, nullable = false, insertable = true, updatable = true)
	public Integer getReadperm() {
		return readperm;
	}

	public void setReadperm(Integer readperm) {
		this.readperm = readperm;
	}

	@Column(name = "filename", unique = false, nullable = false, insertable = true, updatable = true, length = 100)
	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	@Column(name = "description", unique = false, nullable = false, insertable = true, updatable = true, length = 100)
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Column(name = "filetype", unique = false, nullable = false, insertable = true, updatable = true, length = 50)
	public String getFiletype() {
		return filetype;
	}

	public void setFiletype(String filetype) {
		this.filetype = filetype;
	}

	@Column(name = "filesize", unique = false, nullable = false, insertable = true, updatable = true)
	public Integer getFilesize() {
		return filesize;
	}

	public void setFilesize(Integer filesize) {
		this.filesize = filesize;
	}

	@Column(name = "attachment", unique = false, nullable = false, insertable = true, updatable = true, length = 100)
	public String getAttachment() {
		return attachment;
	}

	public void setAttachment(String attachment) {
		this.attachment = attachment;
	}

	@Column(name = "downloads", unique = false, nullable = false, insertable = true, updatable = true)
	public Integer getDownloads() {
		return downloads;
	}

	public void setDownloads(Integer downloads) {
		this.downloads = downloads;
	}

	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY, mappedBy = "attachments")
	public Set<Myattachments> getMyattachmentses() {
		return myattachmentses;
	}

	public void setMyattachmentses(Set<Myattachments> myattachmentses) {
		this.myattachmentses = myattachmentses;
	}

}