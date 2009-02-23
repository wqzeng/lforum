package com.javaeye.lonlysky.lforum.entity.forum;

/**
 * 自定义按钮
 * 
 * @author 黄磊
 *
 */
public class CustomEditorButtonInfo {
	private int id;	//自动编号
	private int available;	//是否有效
	private String tag;	//LForum代码标签代码
	private String icon;	//图标地址
	private String replacement;	//替换内容
	private String example;	//范例(用于显示于帮助中)
	private String explanation;	//解释(用于显示于帮助中)
	private int params;	//参数个数
	private int nest;	//最大深度
	private String paramsdescript;	//参数对应的描述
	private String paramsdefvalue;	//参数对应的默认值
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getAvailable() {
		return available;
	}
	public void setAvailable(int available) {
		this.available = available;
	}
	public String getTag() {
		return tag;
	}
	public void setTag(String tag) {
		this.tag = tag;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public String getReplacement() {
		return replacement;
	}
	public void setReplacement(String replacement) {
		this.replacement = replacement;
	}
	public String getExample() {
		return example;
	}
	public void setExample(String example) {
		this.example = example;
	}
	public String getExplanation() {
		return explanation;
	}
	public void setExplanation(String explanation) {
		this.explanation = explanation;
	}
	public int getParams() {
		return params;
	}
	public void setParams(int params) {
		this.params = params;
	}
	public int getNest() {
		return nest;
	}
	public void setNest(int nest) {
		this.nest = nest;
	}
	public String getParamsdescript() {
		return paramsdescript;
	}
	public void setParamsdescript(String paramsdescript) {
		this.paramsdescript = paramsdescript;
	}
	public String getParamsdefvalue() {
		return paramsdefvalue;
	}
	public void setParamsdefvalue(String paramsdefvalue) {
		this.paramsdefvalue = paramsdefvalue;
	}
}
