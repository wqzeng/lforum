package com.javaeye.lonlysky.lforum.entity.forum;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * 主题关键词
 * 
 * @author 黄磊
 *
 */
@Entity
@Table(name = "topictags")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Topictags implements java.io.Serializable {

	private static final long serialVersionUID = -8299633230263813287L;
	private Integer tagid;
	private Topics topics;

	
	@Id
	@Column(name = "tagid", unique = true, nullable = false, insertable = true, updatable = true)
	public Integer getTagid() {
		return tagid;
	}

	public void setTagid(Integer tagid) {
		this.tagid = tagid;
	}

	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "tid", unique = false, nullable = false, insertable = true, updatable = true)
	public Topics getTopics() {
		return topics;
	}

	public void setTopics(Topics topics) {
		this.topics = topics;
	}

}