package com.javaeye.lonlysky.lforum.entity.global;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;

/**
 * Stats entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "stats")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Stats implements java.io.Serializable {

	private static final long serialVersionUID = 2705342202476773884L;
	private String type;
	private String variable;
	private Integer count;


	@Id
	@GeneratedValue(generator = "paymentableGenerator")     
	@GenericGenerator(name = "paymentableGenerator", strategy = "increment")  
	@Column(name = "type", unique = true, nullable = false, insertable = true, updatable = true, length = 10)
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Column(name = "variable", unique = false, nullable = false, insertable = true, updatable = true, length = 20)
	public String getVariable() {
		return variable;
	}

	public void setVariable(String variable) {
		this.variable = variable;
	}

	@Column(name = "count", unique = false, nullable = false, insertable = true, updatable = true)
	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

}