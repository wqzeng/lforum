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
 * 投票主题
 * 
 * @author 黄磊
 *
 */
@Entity
@Table(name = "polls")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Polls implements java.io.Serializable {

	private static final long serialVersionUID = 9083850242829458884L;
	private Integer pollid;
	private Topics topics;
	private Users users;
	private Integer displayorder;
	private Integer multiple;
	private Integer visible;
	private Integer maxchoices;
	private String expiration;
	private String voternames;
	private Set<Polloptions> polloptionses = new HashSet<Polloptions>(0);

	
	@Id
	@GeneratedValue(generator = "paymentableGenerator")     
	@GenericGenerator(name = "paymentableGenerator", strategy = "increment")  
	@Column(name = "pollid", unique = true, nullable = false, insertable = true, updatable = true)
	public Integer getPollid() {
		return pollid;
	}

	public void setPollid(Integer pollid) {
		this.pollid = pollid;
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

	@Column(name = "displayorder", unique = false, nullable = false, insertable = true, updatable = true)
	public Integer getDisplayorder() {
		return displayorder;
	}

	public void setDisplayorder(Integer displayorder) {
		this.displayorder = displayorder;
	}

	@Column(name = "multiple", unique = false, nullable = false, insertable = true, updatable = true)
	public Integer getMultiple() {
		return multiple;
	}

	public void setMultiple(Integer multiple) {
		this.multiple = multiple;
	}

	@Column(name = "visible", unique = false, nullable = false, insertable = true, updatable = true)
	public Integer getVisible() {
		return visible;
	}

	public void setVisible(Integer visible) {
		this.visible = visible;
	}

	@Column(name = "maxchoices", unique = false, nullable = false, insertable = true, updatable = true)
	public Integer getMaxchoices() {
		return maxchoices;
	}

	public void setMaxchoices(Integer maxchoices) {
		this.maxchoices = maxchoices;
	}

	@Column(name = "expiration", unique = false, nullable = false, insertable = true, updatable = true, length = 23)
	public String getExpiration() {
		return expiration;
	}

	public void setExpiration(String expiration) {
		this.expiration = expiration;
	}

	@Column(name = "voternames", unique = false, nullable = false, insertable = true, updatable = true, length = 1073741823)
	public String getVoternames() {
		return voternames;
	}

	public void setVoternames(String voternames) {
		this.voternames = voternames;
	}

	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY, mappedBy = "polls")
	public Set<Polloptions> getPolloptionses() {
		return polloptionses;
	}

	public void setPolloptionses(Set<Polloptions> polloptionses) {
		this.polloptionses = polloptionses;
	}

}