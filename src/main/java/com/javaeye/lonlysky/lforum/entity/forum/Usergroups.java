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

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;


/**
 * 用户组
 * 
 * @author 黄磊
 *
 */
@Entity
@Table(name = "usergroups")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Usergroups implements java.io.Serializable {

	private static final long serialVersionUID = 7189286707567133079L;
	private Integer groupid;
	private Admingroups admingroups;
	private Integer type;
	private Integer system;
	private String grouptitle;
	private Integer creditshigher;
	private Integer creditslower;
	private Integer stars;
	private String color;
	private String groupavatar;
	private Integer readaccess;
	private Integer allowvisit;
	private Integer allowpost;
	private Integer allowreply;
	private Integer allowpostpoll;
	private Integer allowdirectpost;
	private Integer allowgetattach;
	private Integer allowpostattach;
	private Integer allowvote;
	private Integer allowmultigroups;
	private Integer allowsearch;
	private Integer allowavatar;
	private Integer allowcstatus;
	private Integer allowuseblog;
	private Integer allowinvisible;
	private Integer allowtransfer;
	private Integer allowsetreadperm;
	private Integer allowsetattachperm;
	private Integer allowhidecode;
	private Integer allowhtml;
	private Integer allowcusbbcode;
	private Integer allownickname;
	private Integer allowsigbbcode;
	private Integer allowsigimgcode;
	private Integer allowviewpro;
	private Integer allowviewstats;
	private Integer disableperiodctrl;
	private Integer reasonpm;
	private Integer maxprice;
	private Integer maxpmnum;
	private Integer maxsigsize;
	private Integer maxattachsize;
	private Integer maxsizeperday;
	private String attachextensions;
	private String raterange;
	private Integer allowspace;
	private Integer maxspaceattachsize;
	private Integer maxspacephotosize;
	private Integer allowdebate;
	private Integer allowbonus;
	private Integer minbonusprice;
	private Integer maxbonusprice;
	private Integer allowtrade;
	private Integer allowdiggs;
	private Set<Online> onlines = new HashSet<Online>(0);
	private Set<Moderatormanagelog> moderatormanagelogs = new HashSet<Moderatormanagelog>(0);
	private Set<Adminvisitlog> adminvisitlogs = new HashSet<Adminvisitlog>(0);
	private Set<Users> userses = new HashSet<Users>(0);
	private Set<Onlinelist> onlinelists = new HashSet<Onlinelist>(0);
	private Set<Searchcaches> searchcacheses = new HashSet<Searchcaches>(0);

	
	@Id
	@GeneratedValue(generator = "paymentableGenerator")     
	@GenericGenerator(name = "paymentableGenerator", strategy = "increment")  
	@Column(name = "groupid", unique = true, nullable = false, insertable = true, updatable = true)
	public Integer getGroupid() {
		return groupid;
	}

	public void setGroupid(Integer groupid) {
		this.groupid = groupid;
	}

	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "admingid", unique = false, nullable = true, insertable = true, updatable = true)
	public Admingroups getAdmingroups() {
		return admingroups;
	}

	public void setAdmingroups(Admingroups admingroups) {
		this.admingroups = admingroups;
	}

	@Column(name = "type", unique = false, nullable = true, insertable = true, updatable = true)
	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	@Column(name = "system", unique = false, nullable = false, insertable = true, updatable = true)
	public Integer getSystem() {
		return system;
	}

	public void setSystem(Integer system) {
		this.system = system;
	}

	@Column(name = "grouptitle", unique = false, nullable = false, insertable = true, updatable = true, length = 50)
	public String getGrouptitle() {
		return grouptitle;
	}

	public void setGrouptitle(String grouptitle) {
		this.grouptitle = grouptitle;
	}

	@Column(name = "creditshigher", unique = false, nullable = false, insertable = true, updatable = true)
	public Integer getCreditshigher() {
		return creditshigher;
	}

	public void setCreditshigher(Integer creditshigher) {
		this.creditshigher = creditshigher;
	}

	@Column(name = "creditslower", unique = false, nullable = true, insertable = true, updatable = true)
	public Integer getCreditslower() {
		return creditslower;
	}

	public void setCreditslower(Integer creditslower) {
		this.creditslower = creditslower;
	}

	@Column(name = "stars", unique = false, nullable = false, insertable = true, updatable = true)
	public Integer getStars() {
		return stars;
	}

	public void setStars(Integer stars) {
		this.stars = stars;
	}

	@Column(name = "color", unique = false, nullable = false, insertable = true, updatable = true, length = 7)
	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	@Column(name = "groupavatar", unique = false, nullable = false, insertable = true, updatable = true, length = 60)
	public String getGroupavatar() {
		return groupavatar;
	}

	public void setGroupavatar(String groupavatar) {
		this.groupavatar = groupavatar;
	}

	@Column(name = "readaccess", unique = false, nullable = false, insertable = true, updatable = true)
	public Integer getReadaccess() {
		return readaccess;
	}

	public void setReadaccess(Integer readaccess) {
		this.readaccess = readaccess;
	}

	@Column(name = "allowvisit", unique = false, nullable = false, insertable = true, updatable = true)
	public Integer getAllowvisit() {
		return allowvisit;
	}

	public void setAllowvisit(Integer allowvisit) {
		this.allowvisit = allowvisit;
	}

	@Column(name = "allowpost", unique = false, nullable = false, insertable = true, updatable = true)
	public Integer getAllowpost() {
		return allowpost;
	}

	public void setAllowpost(Integer allowpost) {
		this.allowpost = allowpost;
	}

	@Column(name = "allowreply", unique = false, nullable = false, insertable = true, updatable = true)
	public Integer getAllowreply() {
		return allowreply;
	}

	public void setAllowreply(Integer allowreply) {
		this.allowreply = allowreply;
	}

	@Column(name = "allowpostpoll", unique = false, nullable = false, insertable = true, updatable = true)
	public Integer getAllowpostpoll() {
		return allowpostpoll;
	}

	public void setAllowpostpoll(Integer allowpostpoll) {
		this.allowpostpoll = allowpostpoll;
	}

	@Column(name = "allowdirectpost", unique = false, nullable = false, insertable = true, updatable = true)
	public Integer getAllowdirectpost() {
		return allowdirectpost;
	}

	public void setAllowdirectpost(Integer allowdirectpost) {
		this.allowdirectpost = allowdirectpost;
	}

	@Column(name = "allowgetattach", unique = false, nullable = false, insertable = true, updatable = true)
	public Integer getAllowgetattach() {
		return allowgetattach;
	}

	public void setAllowgetattach(Integer allowgetattach) {
		this.allowgetattach = allowgetattach;
	}

	@Column(name = "allowpostattach", unique = false, nullable = false, insertable = true, updatable = true)
	public Integer getAllowpostattach() {
		return allowpostattach;
	}

	public void setAllowpostattach(Integer allowpostattach) {
		this.allowpostattach = allowpostattach;
	}

	@Column(name = "allowvote", unique = false, nullable = false, insertable = true, updatable = true)
	public Integer getAllowvote() {
		return allowvote;
	}

	public void setAllowvote(Integer allowvote) {
		this.allowvote = allowvote;
	}

	@Column(name = "allowmultigroups", unique = false, nullable = false, insertable = true, updatable = true)
	public Integer getAllowmultigroups() {
		return allowmultigroups;
	}

	public void setAllowmultigroups(Integer allowmultigroups) {
		this.allowmultigroups = allowmultigroups;
	}

	@Column(name = "allowsearch", unique = false, nullable = false, insertable = true, updatable = true)
	public Integer getAllowsearch() {
		return allowsearch;
	}

	public void setAllowsearch(Integer allowsearch) {
		this.allowsearch = allowsearch;
	}

	@Column(name = "allowavatar", unique = false, nullable = false, insertable = true, updatable = true)
	public Integer getAllowavatar() {
		return allowavatar;
	}

	public void setAllowavatar(Integer allowavatar) {
		this.allowavatar = allowavatar;
	}

	@Column(name = "allowcstatus", unique = false, nullable = false, insertable = true, updatable = true)
	public Integer getAllowcstatus() {
		return allowcstatus;
	}

	public void setAllowcstatus(Integer allowcstatus) {
		this.allowcstatus = allowcstatus;
	}

	@Column(name = "allowuseblog", unique = false, nullable = false, insertable = true, updatable = true)
	public Integer getAllowuseblog() {
		return allowuseblog;
	}

	public void setAllowuseblog(Integer allowuseblog) {
		this.allowuseblog = allowuseblog;
	}

	@Column(name = "allowinvisible", unique = false, nullable = false, insertable = true, updatable = true)
	public Integer getAllowinvisible() {
		return allowinvisible;
	}

	public void setAllowinvisible(Integer allowinvisible) {
		this.allowinvisible = allowinvisible;
	}

	@Column(name = "allowtransfer", unique = false, nullable = false, insertable = true, updatable = true)
	public Integer getAllowtransfer() {
		return allowtransfer;
	}

	public void setAllowtransfer(Integer allowtransfer) {
		this.allowtransfer = allowtransfer;
	}

	@Column(name = "allowsetreadperm", unique = false, nullable = false, insertable = true, updatable = true)
	public Integer getAllowsetreadperm() {
		return allowsetreadperm;
	}

	public void setAllowsetreadperm(Integer allowsetreadperm) {
		this.allowsetreadperm = allowsetreadperm;
	}

	@Column(name = "allowsetattachperm", unique = false, nullable = false, insertable = true, updatable = true)
	public Integer getAllowsetattachperm() {
		return allowsetattachperm;
	}

	public void setAllowsetattachperm(Integer allowsetattachperm) {
		this.allowsetattachperm = allowsetattachperm;
	}

	@Column(name = "allowhidecode", unique = false, nullable = false, insertable = true, updatable = true)
	public Integer getAllowhidecode() {
		return allowhidecode;
	}

	public void setAllowhidecode(Integer allowhidecode) {
		this.allowhidecode = allowhidecode;
	}

	@Column(name = "allowhtml", unique = false, nullable = false, insertable = true, updatable = true)
	public Integer getAllowhtml() {
		return allowhtml;
	}

	public void setAllowhtml(Integer allowhtml) {
		this.allowhtml = allowhtml;
	}

	@Column(name = "allowcusbbcode", unique = false, nullable = false, insertable = true, updatable = true)
	public Integer getAllowcusbbcode() {
		return allowcusbbcode;
	}

	public void setAllowcusbbcode(Integer allowcusbbcode) {
		this.allowcusbbcode = allowcusbbcode;
	}

	@Column(name = "allownickname", unique = false, nullable = false, insertable = true, updatable = true)
	public Integer getAllownickname() {
		return allownickname;
	}

	public void setAllownickname(Integer allownickname) {
		this.allownickname = allownickname;
	}

	@Column(name = "allowsigbbcode", unique = false, nullable = false, insertable = true, updatable = true)
	public Integer getAllowsigbbcode() {
		return allowsigbbcode;
	}

	public void setAllowsigbbcode(Integer allowsigbbcode) {
		this.allowsigbbcode = allowsigbbcode;
	}

	@Column(name = "allowsigimgcode", unique = false, nullable = false, insertable = true, updatable = true)
	public Integer getAllowsigimgcode() {
		return allowsigimgcode;
	}

	public void setAllowsigimgcode(Integer allowsigimgcode) {
		this.allowsigimgcode = allowsigimgcode;
	}

	@Column(name = "allowviewpro", unique = false, nullable = false, insertable = true, updatable = true)
	public Integer getAllowviewpro() {
		return allowviewpro;
	}

	public void setAllowviewpro(Integer allowviewpro) {
		this.allowviewpro = allowviewpro;
	}

	@Column(name = "allowviewstats", unique = false, nullable = false, insertable = true, updatable = true)
	public Integer getAllowviewstats() {
		return allowviewstats;
	}

	public void setAllowviewstats(Integer allowviewstats) {
		this.allowviewstats = allowviewstats;
	}

	@Column(name = "disableperiodctrl", unique = false, nullable = false, insertable = true, updatable = true)
	public Integer getDisableperiodctrl() {
		return disableperiodctrl;
	}

	public void setDisableperiodctrl(Integer disableperiodctrl) {
		this.disableperiodctrl = disableperiodctrl;
	}

	@Column(name = "reasonpm", unique = false, nullable = false, insertable = true, updatable = true)
	public Integer getReasonpm() {
		return reasonpm;
	}

	public void setReasonpm(Integer reasonpm) {
		this.reasonpm = reasonpm;
	}

	@Column(name = "maxprice", unique = false, nullable = false, insertable = true, updatable = true)
	public Integer getMaxprice() {
		return maxprice;
	}

	public void setMaxprice(Integer maxprice) {
		this.maxprice = maxprice;
	}

	@Column(name = "maxpmnum", unique = false, nullable = false, insertable = true, updatable = true)
	public Integer getMaxpmnum() {
		return maxpmnum;
	}

	public void setMaxpmnum(Integer maxpmnum) {
		this.maxpmnum = maxpmnum;
	}

	@Column(name = "maxsigsize", unique = false, nullable = false, insertable = true, updatable = true)
	public Integer getMaxsigsize() {
		return maxsigsize;
	}

	public void setMaxsigsize(Integer maxsigsize) {
		this.maxsigsize = maxsigsize;
	}

	@Column(name = "maxattachsize", unique = false, nullable = false, insertable = true, updatable = true)
	public Integer getMaxattachsize() {
		return maxattachsize;
	}

	public void setMaxattachsize(Integer maxattachsize) {
		this.maxattachsize = maxattachsize;
	}

	@Column(name = "maxsizeperday", unique = false, nullable = false, insertable = true, updatable = true)
	public Integer getMaxsizeperday() {
		return maxsizeperday;
	}

	public void setMaxsizeperday(Integer maxsizeperday) {
		this.maxsizeperday = maxsizeperday;
	}

	@Column(name = "attachextensions", unique = false, nullable = false, insertable = true, updatable = true, length = 100)
	public String getAttachextensions() {
		return attachextensions;
	}

	public void setAttachextensions(String attachextensions) {
		this.attachextensions = attachextensions;
	}

	@Column(name = "raterange", unique = false, nullable = false, insertable = true, updatable = true, length = 500)
	public String getRaterange() {
		return raterange;
	}

	public void setRaterange(String raterange) {
		this.raterange = raterange;
	}

	@Column(name = "allowspace", unique = false, nullable = false, insertable = true, updatable = true)
	public Integer getAllowspace() {
		return allowspace;
	}

	public void setAllowspace(Integer allowspace) {
		this.allowspace = allowspace;
	}

	@Column(name = "maxspaceattachsize", unique = false, nullable = false, insertable = true, updatable = true)
	public Integer getMaxspaceattachsize() {
		return maxspaceattachsize;
	}

	public void setMaxspaceattachsize(Integer maxspaceattachsize) {
		this.maxspaceattachsize = maxspaceattachsize;
	}

	@Column(name = "maxspacephotosize", unique = false, nullable = false, insertable = true, updatable = true)
	public Integer getMaxspacephotosize() {
		return maxspacephotosize;
	}

	public void setMaxspacephotosize(Integer maxspacephotosize) {
		this.maxspacephotosize = maxspacephotosize;
	}

	@Column(name = "allowdebate", unique = false, nullable = false, insertable = true, updatable = true)
	public Integer getAllowdebate() {
		return allowdebate;
	}

	public void setAllowdebate(Integer allowdebate) {
		this.allowdebate = allowdebate;
	}

	@Column(name = "allowbonus", unique = false, nullable = false, insertable = true, updatable = true)
	public Integer getAllowbonus() {
		return allowbonus;
	}

	public void setAllowbonus(Integer allowbonus) {
		this.allowbonus = allowbonus;
	}

	@Column(name = "minbonusprice", unique = false, nullable = false, insertable = true, updatable = true)
	public Integer getMinbonusprice() {
		return minbonusprice;
	}

	public void setMinbonusprice(Integer minbonusprice) {
		this.minbonusprice = minbonusprice;
	}

	@Column(name = "maxbonusprice", unique = false, nullable = false, insertable = true, updatable = true)
	public Integer getMaxbonusprice() {
		return maxbonusprice;
	}

	public void setMaxbonusprice(Integer maxbonusprice) {
		this.maxbonusprice = maxbonusprice;
	}

	@Column(name = "allowtrade", unique = false, nullable = false, insertable = true, updatable = true)
	public Integer getAllowtrade() {
		return allowtrade;
	}

	public void setAllowtrade(Integer allowtrade) {
		this.allowtrade = allowtrade;
	}

	@Column(name = "allowdiggs", unique = false, nullable = false, insertable = true, updatable = true)
	public Integer getAllowdiggs() {
		return allowdiggs;
	}

	public void setAllowdiggs(Integer allowdiggs) {
		this.allowdiggs = allowdiggs;
	}

	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY, mappedBy = "usergroups")
	public Set<Online> getOnlines() {
		return onlines;
	}

	public void setOnlines(Set<Online> onlines) {
		this.onlines = onlines;
	}

	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY, mappedBy = "usergroups")
	public Set<Moderatormanagelog> getModeratormanagelogs() {
		return moderatormanagelogs;
	}

	public void setModeratormanagelogs(Set<Moderatormanagelog> moderatormanagelogs) {
		this.moderatormanagelogs = moderatormanagelogs;
	}

	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY, mappedBy = "usergroups")
	public Set<Adminvisitlog> getAdminvisitlogs() {
		return adminvisitlogs;
	}

	public void setAdminvisitlogs(Set<Adminvisitlog> adminvisitlogs) {
		this.adminvisitlogs = adminvisitlogs;
	}

	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY, mappedBy = "usergroups")
	public Set<Users> getUserses() {
		return userses;
	}

	public void setUserses(Set<Users> userses) {
		this.userses = userses;
	}

	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY, mappedBy = "usergroups")
	public Set<Onlinelist> getOnlinelists() {
		return onlinelists;
	}

	public void setOnlinelists(Set<Onlinelist> onlinelists) {
		this.onlinelists = onlinelists;
	}

	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY, mappedBy = "usergroups")
	public Set<Searchcaches> getSearchcacheses() {
		return searchcacheses;
	}

	public void setSearchcacheses(Set<Searchcaches> searchcacheses) {
		this.searchcacheses = searchcacheses;
	}

}