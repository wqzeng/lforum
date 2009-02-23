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
 * 编辑器代码
 * 
 * @author 黄磊
 *
 */
@Entity
@Table(name = "bbcodes")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Bbcodes implements java.io.Serializable {

	// Fields    
	private static final long serialVersionUID = 2794702549234609706L;
	private Integer id;
	private Integer available;
	private String tag;
	private String icon;
	private String example;
	private Integer params;
	private Integer nest;
	private String explanation;
	private String replacement;
	private String paramsdescript;
	private String paramsdefvalue;


	// Property accessors
	@Id
	@GeneratedValue(generator = "paymentableGenerator")     
	@GenericGenerator(name = "paymentableGenerator", strategy = "increment")  
	@Column(name = "id", unique = true, nullable = false, insertable = true, updatable = true)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "available", unique = false, nullable = false, insertable = true, updatable = true)
	public Integer getAvailable() {
		return available;
	}

	public void setAvailable(Integer available) {
		this.available = available;
	}

	@Column(name = "tag", unique = false, nullable = false, insertable = true, updatable = true, length = 100)
	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	@Column(name = "icon", unique = false, nullable = true, insertable = true, updatable = true, length = 50)
	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	@Column(name = "example", unique = false, nullable = false, insertable = true, updatable = true)
	public String getExample() {
		return example;
	}

	public void setExample(String example) {
		this.example = example;
	}

	@Column(name = "params", unique = false, nullable = false, insertable = true, updatable = true)
	public Integer getParams() {
		return params;
	}

	public void setParams(Integer params) {
		this.params = params;
	}

	@Column(name = "nest", unique = false, nullable = false, insertable = true, updatable = true)
	public Integer getNest() {
		return nest;
	}

	public void setNest(Integer nest) {
		this.nest = nest;
	}

	@Column(name = "explanation", unique = false, nullable = true, insertable = true, updatable = true, length = 1073741823)
	public String getExplanation() {
		return explanation;
	}

	public void setExplanation(String explanation) {
		this.explanation = explanation;
	}

	@Column(name = "replacement", unique = false, nullable = true, insertable = true, updatable = true, length = 1073741823)
	public String getReplacement() {
		return replacement;
	}

	public void setReplacement(String replacement) {
		this.replacement = replacement;
	}

	@Column(name = "paramsdescript", unique = false, nullable = true, insertable = true, updatable = true, length = 1073741823)
	public String getParamsdescript() {
		return paramsdescript;
	}

	public void setParamsdescript(String paramsdescript) {
		this.paramsdescript = paramsdescript;
	}

	@Column(name = "paramsdefvalue", unique = false, nullable = true, insertable = true, updatable = true, length = 1073741823)
	public String getParamsdefvalue() {
		return paramsdefvalue;
	}

	public void setParamsdefvalue(String paramsdefvalue) {
		this.paramsdefvalue = paramsdefvalue;
	}

}