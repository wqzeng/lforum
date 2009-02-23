package com.javaeye.lonlysky.lforum.entity.forum;

/**
 * 投票选项(用于显示)
 * 
 * @author 黄磊
 *
 */
public class PollOptionView {

	private String name; //投票项名称
	private int value; //票数
	private int barid;
	private double barwidth; //显示宽度
	private double percent; //投票百分比
	private int multiple; //是否多选
	private int polloptionid; //投票ID
	private int displayorder; //排序位置
	private String votername; //投票人姓名
	private double percentwidth; //投票进度条宽度

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public int getBarid() {
		return barid;
	}

	public void setBarid(int barid) {
		this.barid = barid;
	}

	public double getBarwidth() {
		return barwidth;
	}

	public void setBarwidth(double barwidth) {
		this.barwidth = barwidth;
	}

	public double getPercent() {
		return percent;
	}

	public void setPercent(double percent) {
		this.percent = percent;
	}

	public int getMultiple() {
		return multiple;
	}

	public void setMultiple(int multiple) {
		this.multiple = multiple;
	}

	public int getPolloptionid() {
		return polloptionid;
	}

	public void setPolloptionid(int polloptionid) {
		this.polloptionid = polloptionid;
	}

	public int getDisplayorder() {
		return displayorder;
	}

	public void setDisplayorder(int displayorder) {
		this.displayorder = displayorder;
	}

	public String getVotername() {
		return votername;
	}

	public void setVotername(String votername) {
		this.votername = votername;
	}

	public double getPercentwidth() {
		return percentwidth;
	}

	public void setPercentwidth(double percentwidth) {
		this.percentwidth = percentwidth;
	}
}
