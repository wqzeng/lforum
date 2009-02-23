package com.javaeye.lonlysky.lforum.entity.forum;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;


/**
 * 帖子ID
 * 
 * @author 黄磊
 *
 */
@Entity
@Table(name = "postid")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Postid implements java.io.Serializable {

	private static final long serialVersionUID = -2207017720715574559L;
	private Integer pid;
	private String postdatetime;
	private Posts postForPid;
	private Set<Myattachments> myattachmentses = new HashSet<Myattachments>(0);
	private Set<Postdebatefields> postdebatefieldses = new HashSet<Postdebatefields>(0);
	private Set<Posts> postsesForParentid = new HashSet<Posts>(0);
	private Set<Bonuslog> bonuslogs = new HashSet<Bonuslog>(0);
	private Set<Topics> topicses = new HashSet<Topics>(0);
	private Set<Debatediggs> debatediggses = new HashSet<Debatediggs>(0);
	private Set<Myposts> mypostses = new HashSet<Myposts>(0);
	private Set<Attachments> attachmentses = new HashSet<Attachments>(0);
	private Set<Ratelog> ratelogs = new HashSet<Ratelog>(0);
	

	
	@Id
	@GeneratedValue(generator = "paymentableGenerator")     
	@GenericGenerator(name = "paymentableGenerator", strategy = "increment")  
	@Column(name = "pid", unique = true, nullable = false, insertable = true, updatable = true)
	public Integer getPid() {
		return pid;
	}

	public void setPid(Integer pid) {
		this.pid = pid;
	}

	@Column(name = "postdatetime", unique = false, nullable = false, insertable = true, updatable = true, length = 23)
	public String getPostdatetime() {
		return postdatetime;
	}

	public void setPostdatetime(String postdatetime) {
		this.postdatetime = postdatetime;
	}

	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY, mappedBy = "postid")
	public Set<Myattachments> getMyattachmentses() {
		return myattachmentses;
	}

	public void setMyattachmentses(Set<Myattachments> myattachmentses) {
		this.myattachmentses = myattachmentses;
	}

	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY, mappedBy = "postid")
	public Set<Postdebatefields> getPostdebatefieldses() {
		return postdebatefieldses;
	}

	public void setPostdebatefieldses(Set<Postdebatefields> postdebatefieldses) {
		this.postdebatefieldses = postdebatefieldses;
	}

	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY, mappedBy = "postidByParentid")
	public Set<Posts> getPostsesForParentid() {
		return postsesForParentid;
	}

	public void setPostsesForParentid(Set<Posts> postsesForParentid) {
		this.postsesForParentid = postsesForParentid;
	}

	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY, mappedBy = "postid")
	public Set<Bonuslog> getBonuslogs() {
		return bonuslogs;
	}

	public void setBonuslogs(Set<Bonuslog> bonuslogs) {
		this.bonuslogs = bonuslogs;
	}

	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY, mappedBy = "postid")
	public Set<Topics> getTopicses() {
		return topicses;
	}

	public void setTopicses(Set<Topics> topicses) {
		this.topicses = topicses;
	}

	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY, mappedBy = "postid")
	public Set<Debatediggs> getDebatediggses() {
		return debatediggses;
	}

	public void setDebatediggses(Set<Debatediggs> debatediggses) {
		this.debatediggses = debatediggses;
	}

	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY, mappedBy = "postid")
	public Set<Myposts> getMypostses() {
		return mypostses;
	}

	public void setMypostses(Set<Myposts> mypostses) {
		this.mypostses = mypostses;
	}

	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY, mappedBy = "postid")
	public Set<Attachments> getAttachmentses() {
		return attachmentses;
	}

	public void setAttachmentses(Set<Attachments> attachmentses) {
		this.attachmentses = attachmentses;
	}

	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY, mappedBy = "postid")
	public Set<Ratelog> getRatelogs() {
		return ratelogs;
	}

	public void setRatelogs(Set<Ratelog> ratelogs) {
		this.ratelogs = ratelogs;
	}

	@OneToOne(cascade = { CascadeType.ALL }, fetch = FetchType.EAGER, mappedBy = "postidByPid")
	public Posts getPostForPid() {
		return postForPid;
	}

	public void setPostForPid(Posts postForPid) {
		this.postForPid = postForPid;
	}

}