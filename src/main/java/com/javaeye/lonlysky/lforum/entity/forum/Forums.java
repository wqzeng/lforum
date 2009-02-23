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

/**
 * 论坛板块
 * 
 * @author 黄磊
 *
 */
@Entity
@Table(name = "forums")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Forums implements java.io.Serializable {

	private static final long serialVersionUID = 2568732442822012693L;
	private Integer fid;
	private Topics topics;
	private Forums forums;
	private Users users;
	private Integer layer;
	private String pathlist;
	private String parentidlist;
	private Integer subforumcount;
	private String name;
	private Integer status;
	private Integer colcount;
	private Integer displayorder;
	private Integer templateid;
	private Integer topics_1;
	private Integer curtopics;
	private Integer posts;
	private Integer todayposts;
	private String lasttitle;
	private String lastpost;
	private String lastposter;
	private Integer allowsmilies;
	private Integer allowrss;
	private Integer allowhtml;
	private Integer allowbbcode;
	private Integer allowimgcode;
	private Integer allowblog;
	private Integer istrade;
	private Integer allowpostspecial;
	private Integer allowspecialonly;
	private Integer alloweditrules;
	private Integer allowthumbnail;
	private Integer allowtag;
	private Integer recyclebin;
	private Integer modnewposts;
	private Integer jammer;
	private Integer disablewatermark;
	private Integer inheritedmod;
	private Integer autoclose;
	private Forumfields forumfields;
	private Set<Moderatormanagelog> moderatormanagelogs = new HashSet<Moderatormanagelog>(0);
	private Set<Posts> postses = new HashSet<Posts>(0);
	private Set<Moderators> moderatorses = new HashSet<Moderators>(0);
	private Set<Topics> topicses = new HashSet<Topics>(0);
	private Set<Online> onlines = new HashSet<Online>(0);

	@Id
	@GeneratedValue(generator = "paymentableGenerator")
	@GenericGenerator(name = "paymentableGenerator", strategy = "increment")
	@Column(name = "fid", unique = true, nullable = false, insertable = true, updatable = true)
	public Integer getFid() {
		return fid;
	}

	public void setFid(Integer fid) {
		this.fid = fid;
	}

	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "lasttid", unique = false, nullable = true, insertable = true, updatable = true)
	public Topics getTopics() {
		return topics;
	}

	public void setTopics(Topics topics) {
		this.topics = topics;
	}

	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "parentid", unique = false, nullable = true, insertable = true, updatable = true)
	public Forums getForums() {
		return forums;
	}

	public void setForums(Forums forums) {
		this.forums = forums;
	}

	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "lastposterid", unique = false, nullable = true, insertable = true, updatable = true)
	public Users getUsers() {
		return users;
	}

	public void setUsers(Users users) {
		this.users = users;
	}

	@Column(name = "layer", unique = false, nullable = false, insertable = true, updatable = true)
	public Integer getLayer() {
		return layer;
	}

	public void setLayer(Integer layer) {
		this.layer = layer;
	}

	@Column(name = "pathlist", unique = false, nullable = false, insertable = true, updatable = true, length = 3000)
	public String getPathlist() {
		return pathlist;
	}

	public void setPathlist(String pathlist) {
		this.pathlist = pathlist;
	}

	@Column(name = "parentidlist", unique = false, nullable = false, insertable = true, updatable = true, length = 300)
	public String getParentidlist() {
		return parentidlist;
	}

	public void setParentidlist(String parentidlist) {
		this.parentidlist = parentidlist;
	}

	@Column(name = "subforumcount", unique = false, nullable = false, insertable = true, updatable = true)
	public Integer getSubforumcount() {
		return subforumcount;
	}

	public void setSubforumcount(Integer subforumcount) {
		this.subforumcount = subforumcount;
	}

	@Column(name = "name", unique = false, nullable = false, insertable = true, updatable = true, length = 50)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "status", unique = false, nullable = false, insertable = true, updatable = true)
	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	@Column(name = "colcount", unique = false, nullable = false, insertable = true, updatable = true)
	public Integer getColcount() {
		return colcount;
	}

	public void setColcount(Integer colcount) {
		this.colcount = colcount;
	}

	@Column(name = "displayorder", unique = false, nullable = false, insertable = true, updatable = true)
	public Integer getDisplayorder() {
		return displayorder;
	}

	public void setDisplayorder(Integer displayorder) {
		this.displayorder = displayorder;
	}

	@Column(name = "templateid", unique = false, nullable = false, insertable = true, updatable = true)
	public Integer getTemplateid() {
		return templateid;
	}

	public void setTemplateid(Integer templateid) {
		this.templateid = templateid;
	}

	@Column(name = "topics", unique = false, nullable = false, insertable = true, updatable = true)
	public Integer getTopics_1() {
		return topics_1;
	}

	public void setTopics_1(Integer topics_1) {
		this.topics_1 = topics_1;
	}

	@Column(name = "curtopics", unique = false, nullable = false, insertable = true, updatable = true)
	public Integer getCurtopics() {
		return curtopics;
	}

	public void setCurtopics(Integer curtopics) {
		this.curtopics = curtopics;
	}

	@Column(name = "posts", unique = false, nullable = false, insertable = true, updatable = true)
	public Integer getPosts() {
		return posts;
	}

	public void setPosts(Integer posts) {
		this.posts = posts;
	}

	@Column(name = "todayposts", unique = false, nullable = false, insertable = true, updatable = true)
	public Integer getTodayposts() {
		return todayposts;
	}

	public void setTodayposts(Integer todayposts) {
		this.todayposts = todayposts;
	}

	@Column(name = "lasttitle", unique = false, nullable = true, insertable = true, updatable = true, length = 60)
	public String getLasttitle() {
		return lasttitle;
	}

	public void setLasttitle(String lasttitle) {
		this.lasttitle = lasttitle;
	}

	@Column(name = "lastpost", unique = false, nullable = true, insertable = true, updatable = true, length = 23)
	public String getLastpost() {
		return lastpost;
	}

	public void setLastpost(String lastpost) {
		this.lastpost = lastpost;
	}

	@Column(name = "lastposter", unique = false, nullable = true, insertable = true, updatable = true, length = 20)
	public String getLastposter() {
		return lastposter;
	}

	public void setLastposter(String lastposter) {
		this.lastposter = lastposter;
	}

	@Column(name = "allowsmilies", unique = false, nullable = false, insertable = true, updatable = true)
	public Integer getAllowsmilies() {
		return allowsmilies;
	}

	public void setAllowsmilies(Integer allowsmilies) {
		this.allowsmilies = allowsmilies;
	}

	@Column(name = "allowrss", unique = false, nullable = false, insertable = true, updatable = true)
	public Integer getAllowrss() {
		return allowrss;
	}

	public void setAllowrss(Integer allowrss) {
		this.allowrss = allowrss;
	}

	@Column(name = "allowhtml", unique = false, nullable = false, insertable = true, updatable = true)
	public Integer getAllowhtml() {
		return allowhtml;
	}

	public void setAllowhtml(Integer allowhtml) {
		this.allowhtml = allowhtml;
	}

	@Column(name = "allowbbcode", unique = false, nullable = false, insertable = true, updatable = true)
	public Integer getAllowbbcode() {
		return allowbbcode;
	}

	public void setAllowbbcode(Integer allowbbcode) {
		this.allowbbcode = allowbbcode;
	}

	@Column(name = "allowimgcode", unique = false, nullable = false, insertable = true, updatable = true)
	public Integer getAllowimgcode() {
		return allowimgcode;
	}

	public void setAllowimgcode(Integer allowimgcode) {
		this.allowimgcode = allowimgcode;
	}

	@Column(name = "allowblog", unique = false, nullable = false, insertable = true, updatable = true)
	public Integer getAllowblog() {
		return allowblog;
	}

	public void setAllowblog(Integer allowblog) {
		this.allowblog = allowblog;
	}

	@Column(name = "istrade", unique = false, nullable = false, insertable = true, updatable = true)
	public Integer getIstrade() {
		return istrade;
	}

	public void setIstrade(Integer istrade) {
		this.istrade = istrade;
	}

	@Column(name = "allowpostspecial", unique = false, nullable = false, insertable = true, updatable = true)
	public Integer getAllowpostspecial() {
		return allowpostspecial;
	}

	public void setAllowpostspecial(Integer allowpostspecial) {
		this.allowpostspecial = allowpostspecial;
	}

	@Column(name = "allowspecialonly", unique = false, nullable = true, insertable = true, updatable = true)
	public Integer getAllowspecialonly() {
		return allowspecialonly;
	}

	public void setAllowspecialonly(Integer allowspecialonly) {
		this.allowspecialonly = allowspecialonly;
	}

	@Column(name = "alloweditrules", unique = false, nullable = false, insertable = true, updatable = true)
	public Integer getAlloweditrules() {
		return alloweditrules;
	}

	public void setAlloweditrules(Integer alloweditrules) {
		this.alloweditrules = alloweditrules;
	}

	@Column(name = "allowthumbnail", unique = false, nullable = false, insertable = true, updatable = true)
	public Integer getAllowthumbnail() {
		return allowthumbnail;
	}

	public void setAllowthumbnail(Integer allowthumbnail) {
		this.allowthumbnail = allowthumbnail;
	}

	@Column(name = "allowtag", unique = false, nullable = false, insertable = true, updatable = true)
	public Integer getAllowtag() {
		return allowtag;
	}

	public void setAllowtag(Integer allowtag) {
		this.allowtag = allowtag;
	}

	@Column(name = "recyclebin", unique = false, nullable = false, insertable = true, updatable = true)
	public Integer getRecyclebin() {
		return recyclebin;
	}

	public void setRecyclebin(Integer recyclebin) {
		this.recyclebin = recyclebin;
	}

	@Column(name = "modnewposts", unique = false, nullable = false, insertable = true, updatable = true)
	public Integer getModnewposts() {
		return modnewposts;
	}

	public void setModnewposts(Integer modnewposts) {
		this.modnewposts = modnewposts;
	}

	@Column(name = "jammer", unique = false, nullable = false, insertable = true, updatable = true)
	public Integer getJammer() {
		return jammer;
	}

	public void setJammer(Integer jammer) {
		this.jammer = jammer;
	}

	@Column(name = "disablewatermark", unique = false, nullable = false, insertable = true, updatable = true)
	public Integer getDisablewatermark() {
		return disablewatermark;
	}

	public void setDisablewatermark(Integer disablewatermark) {
		this.disablewatermark = disablewatermark;
	}

	@Column(name = "inheritedmod", unique = false, nullable = false, insertable = true, updatable = true)
	public Integer getInheritedmod() {
		return inheritedmod;
	}

	public void setInheritedmod(Integer inheritedmod) {
		this.inheritedmod = inheritedmod;
	}

	@Column(name = "autoclose", unique = false, nullable = false, insertable = true, updatable = true)
	public Integer getAutoclose() {
		return autoclose;
	}

	public void setAutoclose(Integer autoclose) {
		this.autoclose = autoclose;
	}

	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY, mappedBy = "forums")
	public Set<Moderatormanagelog> getModeratormanagelogs() {
		return moderatormanagelogs;
	}

	public void setModeratormanagelogs(Set<Moderatormanagelog> moderatormanagelogs) {
		this.moderatormanagelogs = moderatormanagelogs;
	}

	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY, mappedBy = "forums")
	public Set<Posts> getPostses() {
		return postses;
	}

	public void setPostses(Set<Posts> postses) {
		this.postses = postses;
	}

	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY, mappedBy = "forums")
	public Set<Moderators> getModeratorses() {
		return moderatorses;
	}

	public void setModeratorses(Set<Moderators> moderatorses) {
		this.moderatorses = moderatorses;
	}

	@OneToOne(cascade = { CascadeType.ALL }, fetch = FetchType.EAGER, mappedBy = "forums")
	public Forumfields getForumfields() {
		return forumfields;
	}

	public void setForumfields(Forumfields forumfields) {
		this.forumfields = forumfields;
	}

	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY, mappedBy = "forums")
	public Set<Topics> getTopicses() {
		return topicses;
	}

	public void setTopicses(Set<Topics> topicses) {
		this.topicses = topicses;
	}

	@OneToMany(cascade = { CascadeType.ALL },fetch = FetchType.LAZY, mappedBy = "forums")
	public Set<Online> getOnlines() {
		return onlines;
	}

	public void setOnlines(Set<Online> onlines) {
		this.onlines = onlines;
	}
}