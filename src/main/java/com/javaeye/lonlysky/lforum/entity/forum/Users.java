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
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;

import com.javaeye.lonlysky.lforum.entity.global.Tags;

/**
 * 注册用户
 * 
 * @author 黄磊
 *
 */
@Entity
@Table(name = "users")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Users implements java.io.Serializable {

	// Fields   
	private static final long serialVersionUID = -605525845048826217L;
	private Integer uid;
	private Usergroups usergroups;
	private Topics topics;
	private Admingroups admingroups;
	private String username;
	private String nickname;
	private String password;
	private String secques;
	private Integer spaceid;
	private Integer gender;
	private Integer groupexpiry;
	private String extgroupids;
	private String regip;
	private String joindate;
	private String lastip;
	private String lastvisit;
	private String lastactivity;
	private String lastpost;
	private String lastposttitle;
	private Integer posts;
	private Integer digestposts;
	private Integer oltime;
	private Integer pageviews;
	private Integer credits;
	private Double extcredits1;
	private Double extcredits2;
	private Double extcredits3;
	private Double extcredits4;
	private Double extcredits5;
	private Double extcredits6;
	private Double extcredits7;
	private Double extcredits8;
	private Integer avatarshowid;
	private String email;
	private String bday;
	private Integer sigstatus;
	private Integer tpp;
	private Integer ppp;
	private Integer templateid;
	private Integer pmsound;
	private Integer showemail;
	private Integer invisible;
	private Integer newpm;
	private Integer newpmcount;
	private Integer accessmasks;
	private Integer onlinestate;
	private Integer newsletter;
	private Userfields userfields;
	private Set<Favorites> favoriteses = new HashSet<Favorites>(0);
	private Set<Posts> postses = new HashSet<Posts>(0);
	private Set<Announcements> announcementses = new HashSet<Announcements>(0);
	private Set<Ratelog> ratelogs = new HashSet<Ratelog>(0);
	private Set<Bonuslog> bonuslogsForAnswerid = new HashSet<Bonuslog>(0);
	private Set<ForumStatistics> statisticses = new HashSet<ForumStatistics>(0);
	private Set<Adminvisitlog> adminvisitlogs = new HashSet<Adminvisitlog>(0);
	private Set<Bonuslog> bonuslogsForAuthorid = new HashSet<Bonuslog>(0);
	private Set<Myposts> mypostses = new HashSet<Myposts>(0);
	private Set<Debatediggs> debatediggses = new HashSet<Debatediggs>(0);
	private Set<Myattachments> myattachmentses = new HashSet<Myattachments>(0);
	private Set<Topics> topicsesForLastposterid = new HashSet<Topics>(0);
	private Set<Paymentlog> paymentlogsForUid = new HashSet<Paymentlog>(0);
	private Set<Forums> forumses = new HashSet<Forums>(0);
	private Set<Onlinetime> onlinetimes = new HashSet<Onlinetime>(0);
	private Set<Attachments> attachmentses = new HashSet<Attachments>(0);
	private Set<Medalslog> medalslogs = new HashSet<Medalslog>(0);
	private Set<Pms> pmsesForMsgtoid = new HashSet<Pms>(0);
	private Set<Online> onlines = new HashSet<Online>(0);
	private Set<Creditslog> creditslogsForUid = new HashSet<Creditslog>(0);
	private Set<Searchcaches> searchcacheses = new HashSet<Searchcaches>(0);
	private Set<Pms> pmsesForMsgfromid = new HashSet<Pms>(0);
	private Set<Polls> pollses = new HashSet<Polls>(0);
	private Set<Tags> tagses = new HashSet<Tags>(0);
	private Set<Moderators> moderatorses = new HashSet<Moderators>(0);
	private Set<Paymentlog> paymentlogsForAuthorid = new HashSet<Paymentlog>(0);
	private Set<Topics> topicsesForPosterid = new HashSet<Topics>(0);
	private Set<Creditslog> creditslogsForFromto = new HashSet<Creditslog>(0);
	private Set<Moderatormanagelog> moderatormanagelogs = new HashSet<Moderatormanagelog>(0);
	private Set<Mytopics> mytopicses = new HashSet<Mytopics>(0);

	public Users(int uid) {
		this.uid = uid;
	}
	
	public Users() {
	}

	@Id
	@GeneratedValue(generator = "paymentableGenerator")
	@GenericGenerator(name = "paymentableGenerator", strategy = "increment")
	@Column(name = "uid", unique = true, nullable = false, insertable = true, updatable = true)
	public Integer getUid() {
		return uid;
	}

	public void setUid(Integer uid) {
		this.uid = uid;
	}

	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "groupid", unique = false, nullable = false, insertable = true, updatable = true)
	public Usergroups getUsergroups() {
		return usergroups;
	}

	public void setUsergroups(Usergroups usergroups) {
		this.usergroups = usergroups;
	}

	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "lastpostid", unique = false, nullable = false, insertable = false, updatable = true)
	public Topics getTopics() {
		return topics;
	}

	public void setTopics(Topics topics) {
		this.topics = topics;
	}

	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "admingid", unique = false, nullable = true, insertable = true, updatable = true)
	public Admingroups getAdmingroups() {
		return admingroups;
	}

	public void setAdmingroups(Admingroups admingroups) {
		this.admingroups = admingroups;
	}

	@Column(name = "username", unique = false, nullable = false, insertable = true, updatable = true, length = 20)
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Column(name = "nickname", unique = false, nullable = false, insertable = true, updatable = true, length = 20)
	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	@Column(name = "password", unique = false, nullable = false, insertable = true, updatable = true, length = 32)
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Column(name = "secques", unique = false, nullable = false, insertable = true, updatable = true, length = 8)
	public String getSecques() {
		return secques;
	}

	public void setSecques(String secques) {
		this.secques = secques;
	}

	@Column(name = "spaceid", unique = false, nullable = false, insertable = true, updatable = true)
	public Integer getSpaceid() {
		return spaceid;
	}

	public void setSpaceid(Integer spaceid) {
		this.spaceid = spaceid;
	}

	@Column(name = "gender", unique = false, nullable = false, insertable = true, updatable = true)
	public Integer getGender() {
		return gender;
	}

	public void setGender(Integer gender) {
		this.gender = gender;
	}

	@Column(name = "groupexpiry", unique = false, nullable = false, insertable = true, updatable = true)
	public Integer getGroupexpiry() {
		return groupexpiry;
	}

	public void setGroupexpiry(Integer groupexpiry) {
		this.groupexpiry = groupexpiry;
	}

	@Column(name = "extgroupids", unique = false, nullable = false, insertable = true, updatable = true, length = 60)
	public String getExtgroupids() {
		return extgroupids;
	}

	public void setExtgroupids(String extgroupids) {
		this.extgroupids = extgroupids;
	}

	@Column(name = "regip", unique = false, nullable = false, insertable = true, updatable = true, length = 15)
	public String getRegip() {
		return regip;
	}

	public void setRegip(String regip) {
		this.regip = regip;
	}

	@Column(name = "joindate", unique = false, nullable = false, insertable = true, updatable = true, length = 16)
	public String getJoindate() {
		return joindate;
	}

	public void setJoindate(String joindate) {
		this.joindate = joindate;
	}

	@Column(name = "lastip", unique = false, nullable = false, insertable = true, updatable = true, length = 15)
	public String getLastip() {
		return lastip;
	}

	public void setLastip(String lastip) {
		this.lastip = lastip;
	}

	@Column(name = "lastvisit", unique = false, nullable = false, insertable = true, updatable = true, length = 23)
	public String getLastvisit() {
		return lastvisit;
	}

	public void setLastvisit(String lastvisit) {
		this.lastvisit = lastvisit;
	}

	@Column(name = "lastactivity", unique = false, nullable = false, insertable = true, updatable = true, length = 23)
	public String getLastactivity() {
		return lastactivity;
	}

	public void setLastactivity(String lastactivity) {
		this.lastactivity = lastactivity;
	}

	@Column(name = "lastpost", unique = false, nullable = false, insertable = true, updatable = true, length = 23)
	public String getLastpost() {
		return lastpost;
	}

	public void setLastpost(String lastpost) {
		this.lastpost = lastpost;
	}

	@Column(name = "lastposttitle", unique = false, nullable = false, insertable = true, updatable = true, length = 60)
	public String getLastposttitle() {
		return lastposttitle;
	}

	public void setLastposttitle(String lastposttitle) {
		this.lastposttitle = lastposttitle;
	}

	@Column(name = "posts", unique = false, nullable = false, insertable = true, updatable = true)
	public Integer getPosts() {
		return posts;
	}

	public void setPosts(Integer posts) {
		this.posts = posts;
	}

	@Column(name = "digestposts", unique = false, nullable = false, insertable = true, updatable = true)
	public Integer getDigestposts() {
		return digestposts;
	}

	public void setDigestposts(Integer digestposts) {
		this.digestposts = digestposts;
	}

	@Column(name = "oltime", unique = false, nullable = false, insertable = true, updatable = true)
	public Integer getOltime() {
		return oltime;
	}

	public void setOltime(Integer oltime) {
		this.oltime = oltime;
	}

	@Column(name = "pageviews", unique = false, nullable = false, insertable = true, updatable = true)
	public Integer getPageviews() {
		return pageviews;
	}

	public void setPageviews(Integer pageviews) {
		this.pageviews = pageviews;
	}

	@Column(name = "credits", unique = false, nullable = false, insertable = true, updatable = true)
	public Integer getCredits() {
		return credits;
	}

	public void setCredits(Integer credits) {
		this.credits = credits;
	}

	@Column(name = "extcredits1", unique = false, nullable = false, insertable = true, updatable = true, precision = 18)
	public Double getExtcredits1() {
		return extcredits1;
	}

	public void setExtcredits1(Double extcredits1) {
		this.extcredits1 = extcredits1;
	}

	@Column(name = "extcredits2", unique = false, nullable = false, insertable = true, updatable = true, precision = 18)
	public Double getExtcredits2() {
		return extcredits2;
	}

	public void setExtcredits2(Double extcredits2) {
		this.extcredits2 = extcredits2;
	}

	@Column(name = "extcredits3", unique = false, nullable = false, insertable = true, updatable = true, precision = 18)
	public Double getExtcredits3() {
		return extcredits3;
	}

	public void setExtcredits3(Double extcredits3) {
		this.extcredits3 = extcredits3;
	}

	@Column(name = "extcredits4", unique = false, nullable = false, insertable = true, updatable = true, precision = 18)
	public Double getExtcredits4() {
		return extcredits4;
	}

	public void setExtcredits4(Double extcredits4) {
		this.extcredits4 = extcredits4;
	}

	@Column(name = "extcredits5", unique = false, nullable = false, insertable = true, updatable = true, precision = 18)
	public Double getExtcredits5() {
		return extcredits5;
	}

	public void setExtcredits5(Double extcredits5) {
		this.extcredits5 = extcredits5;
	}

	@Column(name = "extcredits6", unique = false, nullable = false, insertable = true, updatable = true, precision = 18)
	public Double getExtcredits6() {
		return extcredits6;
	}

	public void setExtcredits6(Double extcredits6) {
		this.extcredits6 = extcredits6;
	}

	@Column(name = "extcredits7", unique = false, nullable = false, insertable = true, updatable = true, precision = 18)
	public Double getExtcredits7() {
		return extcredits7;
	}

	public void setExtcredits7(Double extcredits7) {
		this.extcredits7 = extcredits7;
	}

	@Column(name = "extcredits8", unique = false, nullable = false, insertable = true, updatable = true, precision = 18)
	public Double getExtcredits8() {
		return extcredits8;
	}

	public void setExtcredits8(Double extcredits8) {
		this.extcredits8 = extcredits8;
	}

	@Column(name = "avatarshowid", unique = false, nullable = false, insertable = true, updatable = true)
	public Integer getAvatarshowid() {
		return avatarshowid;
	}

	public void setAvatarshowid(Integer avatarshowid) {
		this.avatarshowid = avatarshowid;
	}

	@Column(name = "email", unique = false, nullable = false, insertable = true, updatable = true, length = 50)
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Column(name = "bday", unique = false, nullable = false, insertable = true, updatable = true, length = 10)
	public String getBday() {
		return bday;
	}

	public void setBday(String bday) {
		this.bday = bday;
	}

	@Column(name = "sigstatus", unique = false, nullable = false, insertable = true, updatable = true)
	public Integer getSigstatus() {
		return sigstatus;
	}

	public void setSigstatus(Integer sigstatus) {
		this.sigstatus = sigstatus;
	}

	@Column(name = "tpp", unique = false, nullable = false, insertable = true, updatable = true)
	public Integer getTpp() {
		return tpp;
	}

	public void setTpp(Integer tpp) {
		this.tpp = tpp;
	}

	@Column(name = "ppp", unique = false, nullable = false, insertable = true, updatable = true)
	public Integer getPpp() {
		return ppp;
	}

	public void setPpp(Integer ppp) {
		this.ppp = ppp;
	}

	@Column(name = "templateid", unique = false, nullable = false, insertable = true, updatable = true)
	public Integer getTemplateid() {
		return templateid;
	}

	public void setTemplateid(Integer templateid) {
		this.templateid = templateid;
	}

	@Column(name = "pmsound", unique = false, nullable = false, insertable = true, updatable = true)
	public Integer getPmsound() {
		return pmsound;
	}

	public void setPmsound(Integer pmsound) {
		this.pmsound = pmsound;
	}

	@Column(name = "showemail", unique = false, nullable = false, insertable = true, updatable = true)
	public Integer getShowemail() {
		return showemail;
	}

	public void setShowemail(Integer showemail) {
		this.showemail = showemail;
	}

	@Column(name = "invisible", unique = false, nullable = false, insertable = true, updatable = true)
	public Integer getInvisible() {
		return invisible;
	}

	public void setInvisible(Integer invisible) {
		this.invisible = invisible;
	}

	@Column(name = "newpm", unique = false, nullable = false, insertable = true, updatable = true)
	public Integer getNewpm() {
		return newpm;
	}

	public void setNewpm(Integer newpm) {
		this.newpm = newpm;
	}

	@Column(name = "newpmcount", unique = false, nullable = false, insertable = true, updatable = true)
	public Integer getNewpmcount() {
		return newpmcount;
	}

	public void setNewpmcount(Integer newpmcount) {
		this.newpmcount = newpmcount;
	}

	@Column(name = "accessmasks", unique = false, nullable = false, insertable = true, updatable = true)
	public Integer getAccessmasks() {
		return accessmasks;
	}

	public void setAccessmasks(Integer accessmasks) {
		this.accessmasks = accessmasks;
	}

	@Column(name = "onlinestate", unique = false, nullable = false, insertable = true, updatable = true)
	public Integer getOnlinestate() {
		return onlinestate;
	}

	public void setOnlinestate(Integer onlinestate) {
		this.onlinestate = onlinestate;
	}

	@Column(name = "newsletter", unique = false, nullable = false, insertable = true, updatable = true)
	public Integer getNewsletter() {
		return newsletter;
	}

	public void setNewsletter(Integer newsletter) {
		this.newsletter = newsletter;
	}

	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY, mappedBy = "users")
	public Set<Favorites> getFavoriteses() {
		return favoriteses;
	}

	public void setFavoriteses(Set<Favorites> favoriteses) {
		this.favoriteses = favoriteses;
	}

	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY, mappedBy = "users")
	public Set<Posts> getPostses() {
		return postses;
	}

	public void setPostses(Set<Posts> postses) {
		this.postses = postses;
	}

	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY, mappedBy = "users")
	public Set<Announcements> getAnnouncementses() {
		return announcementses;
	}

	public void setAnnouncementses(Set<Announcements> announcementses) {
		this.announcementses = announcementses;
	}

	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY, mappedBy = "users")
	public Set<Ratelog> getRatelogs() {
		return ratelogs;
	}

	public void setRatelogs(Set<Ratelog> ratelogs) {
		this.ratelogs = ratelogs;
	}

	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY, mappedBy = "usersByAnswerid")
	public Set<Bonuslog> getBonuslogsForAnswerid() {
		return bonuslogsForAnswerid;
	}

	public void setBonuslogsForAnswerid(Set<Bonuslog> bonuslogsForAnswerid) {
		this.bonuslogsForAnswerid = bonuslogsForAnswerid;
	}

	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY, mappedBy = "users")
	public Set<ForumStatistics> getStatisticses() {
		return statisticses;
	}

	public void setStatisticses(Set<ForumStatistics> statisticses) {
		this.statisticses = statisticses;
	}

	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY, mappedBy = "users")
	public Set<Adminvisitlog> getAdminvisitlogs() {
		return adminvisitlogs;
	}

	public void setAdminvisitlogs(Set<Adminvisitlog> adminvisitlogs) {
		this.adminvisitlogs = adminvisitlogs;
	}

	@OneToOne(cascade = { CascadeType.ALL }, fetch = FetchType.EAGER, mappedBy = "users")
	public Userfields getUserfields() {
		return userfields;
	}

	public void setUserfields(Userfields userfields) {
		this.userfields = userfields;
	}

	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY, mappedBy = "usersByAuthorid")
	public Set<Bonuslog> getBonuslogsForAuthorid() {
		return bonuslogsForAuthorid;
	}

	public void setBonuslogsForAuthorid(Set<Bonuslog> bonuslogsForAuthorid) {
		this.bonuslogsForAuthorid = bonuslogsForAuthorid;
	}

	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY, mappedBy = "users")
	public Set<Myposts> getMypostses() {
		return mypostses;
	}

	public void setMypostses(Set<Myposts> mypostses) {
		this.mypostses = mypostses;
	}

	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY, mappedBy = "users")
	public Set<Debatediggs> getDebatediggses() {
		return debatediggses;
	}

	public void setDebatediggses(Set<Debatediggs> debatediggses) {
		this.debatediggses = debatediggses;
	}

	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY, mappedBy = "users")
	public Set<Myattachments> getMyattachmentses() {
		return myattachmentses;
	}

	public void setMyattachmentses(Set<Myattachments> myattachmentses) {
		this.myattachmentses = myattachmentses;
	}

	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY, mappedBy = "usersByLastposterid")
	public Set<Topics> getTopicsesForLastposterid() {
		return topicsesForLastposterid;
	}

	public void setTopicsesForLastposterid(Set<Topics> topicsesForLastposterid) {
		this.topicsesForLastposterid = topicsesForLastposterid;
	}

	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY, mappedBy = "usersByUid")
	public Set<Paymentlog> getPaymentlogsForUid() {
		return paymentlogsForUid;
	}

	public void setPaymentlogsForUid(Set<Paymentlog> paymentlogsForUid) {
		this.paymentlogsForUid = paymentlogsForUid;
	}

	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY, mappedBy = "users")
	public Set<Forums> getForumses() {
		return forumses;
	}

	public void setForumses(Set<Forums> forumses) {
		this.forumses = forumses;
	}

	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY, mappedBy = "users")
	public Set<Onlinetime> getOnlinetimes() {
		return onlinetimes;
	}

	public void setOnlinetimes(Set<Onlinetime> onlinetimes) {
		this.onlinetimes = onlinetimes;
	}

	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY, mappedBy = "users")
	public Set<Attachments> getAttachmentses() {
		return attachmentses;
	}

	public void setAttachmentses(Set<Attachments> attachmentses) {
		this.attachmentses = attachmentses;
	}

	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY, mappedBy = "users")
	public Set<Medalslog> getMedalslogs() {
		return medalslogs;
	}

	public void setMedalslogs(Set<Medalslog> medalslogs) {
		this.medalslogs = medalslogs;
	}

	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY, mappedBy = "usersByMsgtoid")
	public Set<Pms> getPmsesForMsgtoid() {
		return pmsesForMsgtoid;
	}

	public void setPmsesForMsgtoid(Set<Pms> pmsesForMsgtoid) {
		this.pmsesForMsgtoid = pmsesForMsgtoid;
	}

	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY, mappedBy = "users")
	public Set<Online> getOnlines() {
		return onlines;
	}

	public void setOnlines(Set<Online> onlines) {
		this.onlines = onlines;
	}

	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY, mappedBy = "usersByUid")
	public Set<Creditslog> getCreditslogsForUid() {
		return creditslogsForUid;
	}

	public void setCreditslogsForUid(Set<Creditslog> creditslogsForUid) {
		this.creditslogsForUid = creditslogsForUid;
	}

	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY, mappedBy = "users")
	public Set<Searchcaches> getSearchcacheses() {
		return searchcacheses;
	}

	public void setSearchcacheses(Set<Searchcaches> searchcacheses) {
		this.searchcacheses = searchcacheses;
	}

	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY, mappedBy = "usersByMsgfromid")
	public Set<Pms> getPmsesForMsgfromid() {
		return pmsesForMsgfromid;
	}

	public void setPmsesForMsgfromid(Set<Pms> pmsesForMsgfromid) {
		this.pmsesForMsgfromid = pmsesForMsgfromid;
	}

	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY, mappedBy = "users")
	public Set<Polls> getPollses() {
		return pollses;
	}

	public void setPollses(Set<Polls> pollses) {
		this.pollses = pollses;
	}

	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY, mappedBy = "users")
	public Set<Tags> getTagses() {
		return tagses;
	}

	public void setTagses(Set<Tags> tagses) {
		this.tagses = tagses;
	}

	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY, mappedBy = "users")
	public Set<Moderators> getModeratorses() {
		return moderatorses;
	}

	public void setModeratorses(Set<Moderators> moderatorses) {
		this.moderatorses = moderatorses;
	}

	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY, mappedBy = "usersByAuthorid")
	public Set<Paymentlog> getPaymentlogsForAuthorid() {
		return paymentlogsForAuthorid;
	}

	public void setPaymentlogsForAuthorid(Set<Paymentlog> paymentlogsForAuthorid) {
		this.paymentlogsForAuthorid = paymentlogsForAuthorid;
	}

	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY, mappedBy = "usersByPosterid")
	public Set<Topics> getTopicsesForPosterid() {
		return topicsesForPosterid;
	}

	public void setTopicsesForPosterid(Set<Topics> topicsesForPosterid) {
		this.topicsesForPosterid = topicsesForPosterid;
	}

	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY, mappedBy = "usersByFromto")
	public Set<Creditslog> getCreditslogsForFromto() {
		return creditslogsForFromto;
	}

	public void setCreditslogsForFromto(Set<Creditslog> creditslogsForFromto) {
		this.creditslogsForFromto = creditslogsForFromto;
	}

	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY, mappedBy = "users")
	public Set<Moderatormanagelog> getModeratormanagelogs() {
		return moderatormanagelogs;
	}

	public void setModeratormanagelogs(Set<Moderatormanagelog> moderatormanagelogs) {
		this.moderatormanagelogs = moderatormanagelogs;
	}

	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY, mappedBy = "users")
	public Set<Mytopics> getMytopicses() {
		return mytopicses;
	}

	public void setMytopicses(Set<Mytopics> mytopicses) {
		this.mytopicses = mytopicses;
	}

}