package com.javaeye.lonlysky.lforum.entity.forum;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

/**
 * 帖子表
 * 
 * @author 黄磊
 *
 */
@Entity
@Table(name = "posts", uniqueConstraints = { @UniqueConstraint(columnNames = { "tid", "invisible" }) })
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Posts implements java.io.Serializable {

	private static final long serialVersionUID = -4629621496408371833L;
	private Integer pid;
	private Postid postidByParentid;
	private Topics topics;
	private Users users;
	private Postid postidByPid;
	private Forums forums;
	private Integer layer;
	private String poster;
	private String title;
	private String postdatetime;
	private String message;
	private String ip;
	private String lastedit;
	private Integer invisible;
	private Integer usesig;
	private Integer htmlon;
	private Integer smileyoff;
	private Integer parseurloff;
	private Integer bbcodeoff;
	private Integer attachment;
	private Integer rate;
	private Integer ratetimes;

	@Id
	@GeneratedValue(generator = "idGenerator")
	@GenericGenerator(name = "idGenerator", strategy = "foreign", parameters = { @Parameter(name = "property", value = "postidByPid") })
	@Column(name = "pid", unique = true, nullable = false, insertable = true, updatable = true)
	public Integer getPid() {
		return pid;
	}

	public void setPid(Integer pid) {
		this.pid = pid;
	}

	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "parentid", unique = false, nullable = true, insertable = true, updatable = true)
	public Postid getPostidByParentid() {
		return postidByParentid;
	}

	public void setPostidByParentid(Postid postidByParentid) {
		this.postidByParentid = postidByParentid;
	}

	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "tid", unique = false, nullable = false, insertable = true, updatable = true)
	public Topics getTopics() {
		return topics;
	}

	public void setTopics(Topics topics) {
		this.topics = topics;
	}

	@ManyToOne(cascade = {}, fetch = FetchType.EAGER)
	@JoinColumn(name = "posterid", unique = false, nullable = false, insertable = true, updatable = true)
	public Users getUsers() {
		return users;
	}

	public void setUsers(Users users) {
		this.users = users;
	}

	@OneToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "pid", unique = true, nullable = false, insertable = false, updatable = false)
	public Postid getPostidByPid() {
		return postidByPid;
	}

	public void setPostidByPid(Postid postidByPid) {
		this.postidByPid = postidByPid;
	}

	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "fid", unique = false, nullable = false, insertable = true, updatable = true)
	public Forums getForums() {
		return forums;
	}

	public void setForums(Forums forums) {
		this.forums = forums;
	}

	@Column(name = "layer", unique = false, nullable = false, insertable = true, updatable = true)
	public Integer getLayer() {
		return layer;
	}

	public void setLayer(Integer layer) {
		this.layer = layer;
	}

	@Column(name = "poster", unique = false, nullable = false, insertable = true, updatable = true, length = 20)
	public String getPoster() {
		return poster;
	}

	public void setPoster(String poster) {
		this.poster = poster;
	}

	@Column(name = "title", unique = false, nullable = false, insertable = true, updatable = true, length = 60)
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Column(name = "postdatetime", unique = false, nullable = false, insertable = true, updatable = true, length = 16)
	public String getPostdatetime() {
		return postdatetime;
	}

	public void setPostdatetime(String postdatetime) {
		this.postdatetime = postdatetime;
	}

	@Column(name = "message", unique = false, nullable = false, insertable = true, updatable = true, length = 1073741823)
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Column(name = "ip", unique = false, nullable = false, insertable = true, updatable = true, length = 15)
	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	@Column(name = "lastedit", unique = false, nullable = false, insertable = true, updatable = true, length = 50)
	public String getLastedit() {
		return lastedit;
	}

	public void setLastedit(String lastedit) {
		this.lastedit = lastedit;
	}

	@Column(name = "invisible", unique = false, nullable = false, insertable = true, updatable = true)
	public Integer getInvisible() {
		return invisible;
	}

	public void setInvisible(Integer invisible) {
		this.invisible = invisible;
	}

	@Column(name = "usesig", unique = false, nullable = false, insertable = true, updatable = true)
	public Integer getUsesig() {
		return usesig;
	}

	public void setUsesig(Integer usesig) {
		this.usesig = usesig;
	}

	@Column(name = "htmlon", unique = false, nullable = false, insertable = true, updatable = true)
	public Integer getHtmlon() {
		return htmlon;
	}

	public void setHtmlon(Integer htmlon) {
		this.htmlon = htmlon;
	}

	@Column(name = "smileyoff", unique = false, nullable = false, insertable = true, updatable = true)
	public Integer getSmileyoff() {
		return smileyoff;
	}

	public void setSmileyoff(Integer smileyoff) {
		this.smileyoff = smileyoff;
	}

	@Column(name = "parseurloff", unique = false, nullable = false, insertable = true, updatable = true)
	public Integer getParseurloff() {
		return parseurloff;
	}

	public void setParseurloff(Integer parseurloff) {
		this.parseurloff = parseurloff;
	}

	@Column(name = "bbcodeoff", unique = false, nullable = false, insertable = true, updatable = true)
	public Integer getBbcodeoff() {
		return bbcodeoff;
	}

	public void setBbcodeoff(Integer bbcodeoff) {
		this.bbcodeoff = bbcodeoff;
	}

	@Column(name = "attachment", unique = false, nullable = false, insertable = true, updatable = true)
	public Integer getAttachment() {
		return attachment;
	}

	public void setAttachment(Integer attachment) {
		this.attachment = attachment;
	}

	@Column(name = "rate", unique = false, nullable = false, insertable = true, updatable = true)
	public Integer getRate() {
		return rate;
	}

	public void setRate(Integer rate) {
		this.rate = rate;
	}

	@Column(name = "ratetimes", unique = false, nullable = false, insertable = true, updatable = true)
	public Integer getRatetimes() {
		return ratetimes;
	}

	public void setRatetimes(Integer ratetimes) {
		this.ratetimes = ratetimes;
	}

}