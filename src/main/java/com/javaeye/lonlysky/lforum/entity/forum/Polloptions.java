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
 * 投票选项
 * 
 * @author 黄磊
 *
 */
@Entity
@Table(name = "polloptions")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Polloptions implements java.io.Serializable {

	private static final long serialVersionUID = -1079720305968254563L;
	private Integer polloptionid;
	private Polls polls;
	private Topics topics;
	private Integer votes;
	private Integer displayorder;
	private String polloption;
	private String voternames;

	
	@Id
	@GeneratedValue(generator = "paymentableGenerator")     
	@GenericGenerator(name = "paymentableGenerator", strategy = "increment")  
	@Column(name = "polloptionid", unique = true, nullable = false, insertable = true, updatable = true)
	public Integer getPolloptionid() {
		return polloptionid;
	}

	public void setPolloptionid(Integer polloptionid) {
		this.polloptionid = polloptionid;
	}

	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "pollid", unique = false, nullable = false, insertable = true, updatable = true)
	public Polls getPolls() {
		return polls;
	}

	public void setPolls(Polls polls) {
		this.polls = polls;
	}

	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "tid", unique = false, nullable = false, insertable = true, updatable = true)
	public Topics getTopics() {
		return topics;
	}

	public void setTopics(Topics topics) {
		this.topics = topics;
	}

	@Column(name = "votes", unique = false, nullable = false, insertable = true, updatable = true)
	public Integer getVotes() {
		return votes;
	}

	public void setVotes(Integer votes) {
		this.votes = votes;
	}

	@Column(name = "displayorder", unique = false, nullable = false, insertable = true, updatable = true)
	public Integer getDisplayorder() {
		return displayorder;
	}

	public void setDisplayorder(Integer displayorder) {
		this.displayorder = displayorder;
	}

	@Column(name = "polloption", unique = false, nullable = false, insertable = true, updatable = true, length = 80)
	public String getPolloption() {
		return polloption;
	}

	public void setPolloption(String polloption) {
		this.polloption = polloption;
	}

	@Column(name = "voternames", unique = false, nullable = false, insertable = true, updatable = true, length = 1073741823)
	public String getVoternames() {
		return voternames;
	}

	public void setVoternames(String voternames) {
		this.voternames = voternames;
	}

}