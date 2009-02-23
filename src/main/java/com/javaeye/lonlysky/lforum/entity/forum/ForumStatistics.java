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
 * 论坛状态
 * 
 * @author 黄磊
 *
 */
@Entity
@Table(name = "forumstatistics")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ForumStatistics implements java.io.Serializable {

	private static final long serialVersionUID = 2987468877672109929L;
	private Integer id;
	private Users users;
	private Integer totaltopic;
	private Integer totalpost;
	private Integer totalusers;
	private String lastusername;
	private Integer highestonlineusercount;
	private String highestonlineusertime;
	private Integer yesterdayposts;
	private Integer highestposts;
	private String highestpostsdate;

	
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

	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "lastuserid", unique = false, nullable = false, insertable = true, updatable = true)
	public Users getUsers() {
		return users;
	}

	public void setUsers(Users users) {
		this.users = users;
	}

	@Column(name = "totaltopic", unique = false, nullable = false, insertable = true, updatable = true)
	public Integer getTotaltopic() {
		return totaltopic;
	}

	public void setTotaltopic(Integer totaltopic) {
		this.totaltopic = totaltopic;
	}

	@Column(name = "totalpost", unique = false, nullable = false, insertable = true, updatable = true)
	public Integer getTotalpost() {
		return totalpost;
	}

	public void setTotalpost(Integer totalpost) {
		this.totalpost = totalpost;
	}

	@Column(name = "totalusers", unique = false, nullable = false, insertable = true, updatable = true)
	public Integer getTotalusers() {
		return totalusers;
	}

	public void setTotalusers(Integer totalusers) {
		this.totalusers = totalusers;
	}

	@Column(name = "lastusername", unique = false, nullable = false, insertable = true, updatable = true, length = 20)
	public String getLastusername() {
		return lastusername;
	}

	public void setLastusername(String lastusername) {
		this.lastusername = lastusername;
	}

	@Column(name = "highestonlineusercount", unique = false, nullable = true, insertable = true, updatable = true)
	public Integer getHighestonlineusercount() {
		return highestonlineusercount;
	}

	public void setHighestonlineusercount(Integer highestonlineusercount) {
		this.highestonlineusercount = highestonlineusercount;
	}

	@Column(name = "highestonlineusertime", unique = false, nullable = true, insertable = true, updatable = true, length = 16)
	public String getHighestonlineusertime() {
		return highestonlineusertime;
	}

	public void setHighestonlineusertime(String highestonlineusertime) {
		this.highestonlineusertime = highestonlineusertime;
	}

	@Column(name = "yesterdayposts", unique = false, nullable = false, insertable = true, updatable = true)
	public Integer getYesterdayposts() {
		return yesterdayposts;
	}

	public void setYesterdayposts(Integer yesterdayposts) {
		this.yesterdayposts = yesterdayposts;
	}

	@Column(name = "highestposts", unique = false, nullable = false, insertable = true, updatable = true)
	public Integer getHighestposts() {
		return highestposts;
	}

	public void setHighestposts(Integer highestposts) {
		this.highestposts = highestposts;
	}

	@Column(name = "highestpostsdate", unique = false, nullable = false, insertable = true, updatable = true, length = 10)
	public String getHighestpostsdate() {
		return highestpostsdate;
	}

	public void setHighestpostsdate(String highestpostsdate) {
		this.highestpostsdate = highestpostsdate;
	}

}