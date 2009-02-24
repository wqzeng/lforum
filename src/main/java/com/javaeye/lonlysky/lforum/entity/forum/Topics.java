package com.javaeye.lonlysky.lforum.entity.forum;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;

/**
 * 主题
 * 
 * @author 黄磊
 *
 */
@Entity
@Table(name = "topics", uniqueConstraints = { @UniqueConstraint(columnNames = { "fid", "displayorder", "lastpostid" }) })
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Topics implements java.io.Serializable {

	private static final long serialVersionUID = 3237080052182324807L;
	private Integer tid;
	private Users usersByLastposterid;
	private Users usersByPosterid;
	private Postid postid;
	private Topictypes topictypes;
	private Forums forums;
	private Integer iconid;
	private Integer readperm;
	private Integer price;
	private String poster;
	private String title;
	private String postdatetime;
	private String lastpost;
	private String lastposter;
	private Integer views;
	private Integer replies;
	private Integer displayorder;
	private String highlight;
	private Integer digest;
	private Integer rate;
	private Integer hide;
	private Integer poll;
	private Integer attachment;
	private Integer moderated;
	private Integer closed;
	private Integer magic;
	private Integer identify;
	private Integer special;
	private Set<Debates> debateses = new HashSet<Debates>(0);
	private Set<Posts> postses = new HashSet<Posts>(0);
	private Set<Online> onlines = new HashSet<Online>(0);
	private Set<Polloptions> polloptionses = new HashSet<Polloptions>(0);
	private Set<Moderatormanagelog> moderatormanagelogs = new HashSet<Moderatormanagelog>(0);
	private Set<Users> userses = new HashSet<Users>(0);
	private Set<Myposts> mypostses = new HashSet<Myposts>(0);
	private Set<Favorites> favoriteses = new HashSet<Favorites>(0);
	private Set<Postdebatefields> postdebatefieldses = new HashSet<Postdebatefields>(0);
	private Set<Polls> pollses = new HashSet<Polls>(0);
	private Set<Topictags> topictagses = new HashSet<Topictags>(0);
	private Set<Bonuslog> bonuslogs = new HashSet<Bonuslog>(0);
	private Set<Debatediggs> debatediggses = new HashSet<Debatediggs>(0);
	private Set<Forums> forumses = new HashSet<Forums>(0);
	private Set<Mytopics> mytopicses = new HashSet<Mytopics>(0);
	private Set<Paymentlog> paymentlogs = new HashSet<Paymentlog>(0);
	private Set<Attachments> attachmentses = new HashSet<Attachments>(0);
	private Set<Myattachments> myattachmentses = new HashSet<Myattachments>(0);

	public Topics() {
	}

	public Topics(int tid) {
		this.tid = tid;
	}

	@Id
	@GeneratedValue(generator = "paymentableGenerator")
	@GenericGenerator(name = "paymentableGenerator", strategy = "increment")
	@Column(name = "tid", unique = true, nullable = false, insertable = true, updatable = true)
	public Integer getTid() {
		return tid;
	}

	public void setTid(Integer tid) {
		this.tid = tid;
	}

	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "lastposterid", unique = false, nullable = false, insertable = true, updatable = true)
	public Users getUsersByLastposterid() {
		return usersByLastposterid;
	}

	public void setUsersByLastposterid(Users usersByLastposterid) {
		this.usersByLastposterid = usersByLastposterid;
	}

	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "posterid", unique = false, nullable = false, insertable = true, updatable = true)
	public Users getUsersByPosterid() {
		return usersByPosterid;
	}

	public void setUsersByPosterid(Users usersByPosterid) {
		this.usersByPosterid = usersByPosterid;
	}

	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "lastpostid", unique = false, nullable = false, insertable = true, updatable = true)
	public Postid getPostid() {
		return postid;
	}

	public void setPostid(Postid postid) {
		this.postid = postid;
	}

	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "typeid", unique = false, nullable = false, insertable = true, updatable = true)
	public Topictypes getTopictypes() {
		return topictypes;
	}

	public void setTopictypes(Topictypes topictypes) {
		this.topictypes = topictypes;
	}

	@ManyToOne(cascade = {}, fetch = FetchType.EAGER)
	@JoinColumn(name = "fid", unique = false, nullable = false, insertable = true, updatable = true)
	public Forums getForums() {
		return forums;
	}

	public void setForums(Forums forums) {
		this.forums = forums;
	}

	@Column(name = "iconid", unique = false, nullable = false, insertable = true, updatable = true)
	public Integer getIconid() {
		return iconid;
	}

	public void setIconid(Integer iconid) {
		this.iconid = iconid;
	}

	@Column(name = "readperm", unique = false, nullable = false, insertable = true, updatable = true)
	public Integer getReadperm() {
		return readperm;
	}

	public void setReadperm(Integer readperm) {
		this.readperm = readperm;
	}

	@Column(name = "price", unique = false, nullable = false, insertable = true, updatable = true)
	public Integer getPrice() {
		return price;
	}

	public void setPrice(Integer price) {
		this.price = price;
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

	@Column(name = "postdatetime", unique = false, nullable = false, insertable = true, updatable = true, length = 23)
	public String getPostdatetime() {
		return postdatetime;
	}

	public void setPostdatetime(String postdatetime) {
		this.postdatetime = postdatetime;
	}

	@Column(name = "lastpost", unique = false, nullable = false, insertable = true, updatable = true, length = 23)
	public String getLastpost() {
		return lastpost;
	}

	public void setLastpost(String lastpost) {
		this.lastpost = lastpost;
	}

	@Column(name = "lastposter", unique = false, nullable = false, insertable = true, updatable = true, length = 20)
	public String getLastposter() {
		return lastposter;
	}

	public void setLastposter(String lastposter) {
		this.lastposter = lastposter;
	}

	@Column(name = "views", unique = false, nullable = false, insertable = true, updatable = true)
	public Integer getViews() {
		return views;
	}

	public void setViews(Integer views) {
		this.views = views;
	}

	@Column(name = "replies", unique = false, nullable = false, insertable = true, updatable = true)
	public Integer getReplies() {
		return replies;
	}

	public void setReplies(Integer replies) {
		this.replies = replies;
	}

	@Column(name = "displayorder", unique = false, nullable = false, insertable = true, updatable = true)
	public Integer getDisplayorder() {
		return displayorder;
	}

	public void setDisplayorder(Integer displayorder) {
		this.displayorder = displayorder;
	}

	@Column(name = "highlight", unique = false, nullable = false, insertable = true, updatable = true, length = 500)
	public String getHighlight() {
		return highlight;
	}

	public void setHighlight(String highlight) {
		this.highlight = highlight;
	}

	@Column(name = "digest", unique = false, nullable = false, insertable = true, updatable = true)
	public Integer getDigest() {
		return digest;
	}

	public void setDigest(Integer digest) {
		this.digest = digest;
	}

	@Column(name = "rate", unique = false, nullable = false, insertable = true, updatable = true)
	public Integer getRate() {
		return rate;
	}

	public void setRate(Integer rate) {
		this.rate = rate;
	}

	@Column(name = "hide", unique = false, nullable = false, insertable = true, updatable = true)
	public Integer getHide() {
		return hide;
	}

	public void setHide(Integer hide) {
		this.hide = hide;
	}

	@Column(name = "poll", unique = false, nullable = false, insertable = true, updatable = true)
	public Integer getPoll() {
		return poll;
	}

	public void setPoll(Integer poll) {
		this.poll = poll;
	}

	@Column(name = "attachment", unique = false, nullable = false, insertable = true, updatable = true)
	public Integer getAttachment() {
		return attachment;
	}

	public void setAttachment(Integer attachment) {
		this.attachment = attachment;
	}

	@Column(name = "moderated", unique = false, nullable = false, insertable = true, updatable = true)
	public Integer getModerated() {
		return moderated;
	}

	public void setModerated(Integer moderated) {
		this.moderated = moderated;
	}

	@Column(name = "closed", unique = false, nullable = false, insertable = true, updatable = true)
	public Integer getClosed() {
		return closed;
	}

	public void setClosed(Integer closed) {
		this.closed = closed;
	}

	@Column(name = "magic", unique = false, nullable = false, insertable = true, updatable = true)
	public Integer getMagic() {
		return magic;
	}

	public void setMagic(Integer magic) {
		this.magic = magic;
	}

	@Column(name = "identify", unique = false, nullable = false, insertable = true, updatable = true)
	public Integer getIdentify() {
		return identify;
	}

	public void setIdentify(Integer identify) {
		this.identify = identify;
	}

	@Column(name = "special", unique = false, nullable = false, insertable = true, updatable = true)
	public Integer getSpecial() {
		return special;
	}

	public void setSpecial(Integer special) {
		this.special = special;
	}

	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY, mappedBy = "topics")
	public Set<Debates> getDebateses() {
		return debateses;
	}

	public void setDebateses(Set<Debates> debateses) {
		this.debateses = debateses;
	}

	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY, mappedBy = "topics")
	public Set<Posts> getPostses() {
		return postses;
	}

	public void setPostses(Set<Posts> postses) {
		this.postses = postses;
	}

	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY, mappedBy = "topics")
	public Set<Online> getOnlines() {
		return onlines;
	}

	public void setOnlines(Set<Online> onlines) {
		this.onlines = onlines;
	}

	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY, mappedBy = "topics")
	public Set<Polloptions> getPolloptionses() {
		return polloptionses;
	}

	public void setPolloptionses(Set<Polloptions> polloptionses) {
		this.polloptionses = polloptionses;
	}

	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY, mappedBy = "topics")
	public Set<Moderatormanagelog> getModeratormanagelogs() {
		return moderatormanagelogs;
	}

	public void setModeratormanagelogs(Set<Moderatormanagelog> moderatormanagelogs) {
		this.moderatormanagelogs = moderatormanagelogs;
	}

	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY, mappedBy = "topics")
	public Set<Users> getUserses() {
		return userses;
	}

	public void setUserses(Set<Users> userses) {
		this.userses = userses;
	}

	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY, mappedBy = "topics")
	public Set<Myposts> getMypostses() {
		return mypostses;
	}

	public void setMypostses(Set<Myposts> mypostses) {
		this.mypostses = mypostses;
	}

	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY, mappedBy = "topics")
	public Set<Favorites> getFavoriteses() {
		return favoriteses;
	}

	public void setFavoriteses(Set<Favorites> favoriteses) {
		this.favoriteses = favoriteses;
	}

	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY, mappedBy = "topics")
	public Set<Postdebatefields> getPostdebatefieldses() {
		return postdebatefieldses;
	}

	public void setPostdebatefieldses(Set<Postdebatefields> postdebatefieldses) {
		this.postdebatefieldses = postdebatefieldses;
	}

	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY, mappedBy = "topics")
	public Set<Polls> getPollses() {
		return pollses;
	}

	public void setPollses(Set<Polls> pollses) {
		this.pollses = pollses;
	}

	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY, mappedBy = "topics")
	public Set<Topictags> getTopictagses() {
		return topictagses;
	}

	public void setTopictagses(Set<Topictags> topictagses) {
		this.topictagses = topictagses;
	}

	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY, mappedBy = "topics")
	public Set<Bonuslog> getBonuslogs() {
		return bonuslogs;
	}

	public void setBonuslogs(Set<Bonuslog> bonuslogs) {
		this.bonuslogs = bonuslogs;
	}

	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY, mappedBy = "topics")
	public Set<Debatediggs> getDebatediggses() {
		return debatediggses;
	}

	public void setDebatediggses(Set<Debatediggs> debatediggses) {
		this.debatediggses = debatediggses;
	}

	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY, mappedBy = "topics")
	public Set<Forums> getForumses() {
		return forumses;
	}

	public void setForumses(Set<Forums> forumses) {
		this.forumses = forumses;
	}

	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY, mappedBy = "topics")
	public Set<Mytopics> getMytopicses() {
		return mytopicses;
	}

	public void setMytopicses(Set<Mytopics> mytopicses) {
		this.mytopicses = mytopicses;
	}

	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY, mappedBy = "topics")
	public Set<Paymentlog> getPaymentlogs() {
		return paymentlogs;
	}

	public void setPaymentlogs(Set<Paymentlog> paymentlogs) {
		this.paymentlogs = paymentlogs;
	}

	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY, mappedBy = "topics")
	public Set<Attachments> getAttachmentses() {
		return attachmentses;
	}

	public void setAttachmentses(Set<Attachments> attachmentses) {
		this.attachmentses = attachmentses;
	}

	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY, mappedBy = "topics")
	public Set<Myattachments> getMyattachmentses() {
		return myattachmentses;
	}

	public void setMyattachmentses(Set<Myattachments> myattachmentses) {
		this.myattachmentses = myattachmentses;
	}

}