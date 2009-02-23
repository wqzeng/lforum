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
 * 未知
 * 
 * @author 黄磊
 *
 */
@Entity
@Table(name = "statvars")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Statvars implements java.io.Serializable {

	private static final long serialVersionUID = 5819636838801376523L;
	private String type;
	private String variable;
	private String value;

	@Id
	@GeneratedValue(generator = "paymentableGenerator")     
	@GenericGenerator(name = "paymentableGenerator", strategy = "increment")  
	@Column(name = "type", unique = true, nullable = false, insertable = true, updatable = true, length = 20)
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

	@Column(name = "value", unique = false, nullable = false, insertable = true, updatable = true)
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}