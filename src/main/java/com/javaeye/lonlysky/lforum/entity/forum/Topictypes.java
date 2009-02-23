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
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;

/**
 * 主题类别
 * 
 * @author 黄磊
 *
 */
@Entity
@Table(name = "topictypes")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Topictypes implements java.io.Serializable {

	private static final long serialVersionUID = 2025839022891072540L;
	private Integer typeid;
	private Integer displayorder;
	private String name;
	private String description;
	private Set<Topics> topicses = new HashSet<Topics>(0);

	
	@Id
	@GeneratedValue(generator = "paymentableGenerator")     
	@GenericGenerator(name = "paymentableGenerator", strategy = "increment")  
	@Column(name = "typeid", unique = true, nullable = false, insertable = true, updatable = true)
	public Integer getTypeid() {
		return typeid;
	}

	public void setTypeid(Integer typeid) {
		this.typeid = typeid;
	}

	@Column(name = "displayorder", unique = false, nullable = false, insertable = true, updatable = true)
	public Integer getDisplayorder() {
		return displayorder;
	}

	public void setDisplayorder(Integer displayorder) {
		this.displayorder = displayorder;
	}

	@Column(name = "name", unique = false, nullable = false, insertable = true, updatable = true, length = 30)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "description", unique = false, nullable = false, insertable = true, updatable = true, length = 500)
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY, mappedBy = "topictypes")
	public Set<Topics> getTopicses() {
		return topicses;
	}

	public void setTopicses(Set<Topics> topicses) {
		this.topicses = topicses;
	}

}