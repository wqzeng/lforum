package com.javaeye.lonlysky.lforum.entity.forum;

import java.io.Serializable;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * 用户扩展积分信息
 * 
 * @author 黄磊
 *
 */
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class UserExtcreditsInfo implements Serializable {

	private static final long serialVersionUID = -89360189050635884L;
	private String name; //积分名称
	private String unit; //积分单位
	private double rate; //兑换比率
	private double init; //注册初始积分
	private double topic; //发主题
	private double reply; //回复
	private double digest; //加精华
	private double upload; //上传附件
	private double download; //下载附件
	private double pm; //发短消息
	private double search; //搜索
	private double pay; //交易成功
	private double vote; //参与投票

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public double getRate() {
		return rate;
	}

	public void setRate(double rate) {
		this.rate = rate;
	}

	public double getInit() {
		return init;
	}

	public void setInit(double init) {
		this.init = init;
	}

	public double getTopic() {
		return topic;
	}

	public void setTopic(double topic) {
		this.topic = topic;
	}

	public double getReply() {
		return reply;
	}

	public void setReply(double reply) {
		this.reply = reply;
	}

	public double getDigest() {
		return digest;
	}

	public void setDigest(double digest) {
		this.digest = digest;
	}

	public double getUpload() {
		return upload;
	}

	public void setUpload(double upload) {
		this.upload = upload;
	}

	public double getDownload() {
		return download;
	}

	public void setDownload(double download) {
		this.download = download;
	}

	public double getPm() {
		return pm;
	}

	public void setPm(double pm) {
		this.pm = pm;
	}

	public double getSearch() {
		return search;
	}

	public void setSearch(double search) {
		this.search = search;
	}

	public double getPay() {
		return pay;
	}

	public void setPay(double pay) {
		this.pay = pay;
	}

	public double getVote() {
		return vote;
	}

	public void setVote(double vote) {
		this.vote = vote;
	}

}
