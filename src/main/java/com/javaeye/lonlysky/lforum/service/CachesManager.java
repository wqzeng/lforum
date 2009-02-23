package com.javaeye.lonlysky.lforum.service;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Property;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springside.modules.orm.hibernate.SimpleHibernateTemplate;

import com.javaeye.lonlysky.lforum.cache.LForumCache;
import com.javaeye.lonlysky.lforum.comm.utils.Utils;
import com.javaeye.lonlysky.lforum.config.impl.ConfigLoader;
import com.javaeye.lonlysky.lforum.entity.forum.Bbcodes;
import com.javaeye.lonlysky.lforum.entity.forum.Forumlinks;
import com.javaeye.lonlysky.lforum.entity.forum.Forums;
import com.javaeye.lonlysky.lforum.entity.forum.Medals;
import com.javaeye.lonlysky.lforum.entity.forum.Onlinelist;
import com.javaeye.lonlysky.lforum.entity.forum.Smilies;
import com.javaeye.lonlysky.lforum.entity.forum.Templates;
import com.javaeye.lonlysky.lforum.entity.forum.Topicidentify;

/**
 * 缓存论坛HTML数据
 * 
 * @author 黄磊
 *
 */
@Service
public class CachesManager {

	private static final Object synObject = new Object();
	private SimpleHibernateTemplate<Forumlinks, Integer> forumlinkDAO;
	private SimpleHibernateTemplate<Topicidentify, Integer> topicidentifyDAO;
	private SimpleHibernateTemplate<Medals, Integer> medalDAO;

	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		forumlinkDAO = new SimpleHibernateTemplate<Forumlinks, Integer>(sessionFactory, Forumlinks.class);
		topicidentifyDAO = new SimpleHibernateTemplate<Topicidentify, Integer>(sessionFactory, Topicidentify.class);
		medalDAO = new SimpleHibernateTemplate<Medals, Integer>(sessionFactory, Medals.class);
	}

	@Autowired
	private TemplateManager templateManager;

	@Autowired
	private OnlineUserManager onlineUserManager;

	@Autowired
	private ForumManager forumManager;

	@Autowired
	private SmilieManager smilieManager;

	@Autowired
	private EditorManager editorManager;

	/**
	 * 获得表情符的json数据
	 * 
	 * @return 表情符的json数据
	 */
	public String getSmiliesCache() {
		String smilesString = LForumCache.getInstance().getCache("SmiliesList", String.class);
		if (smilesString == null) {

			StringBuilder builder = new StringBuilder();
			List<Smilies> smileList = smilieManager.getSmiliesList();
			for (Smilies smilies : smileList) {
				// 如果是表情分类
				if (smilies.getType() == 0) {
					builder.append("'" + smilies.getCode().trim().replace("'", "\\'") + "': [\r\n");
					boolean flag = false;
					for (Smilies smilies2 : smileList) {
						// 如果属于以上表情分类
						if (smilies2.getType().equals(smilies.getId())) {
							builder.append("{'code' : '");
							builder.append(smilies2.getCode().trim().replace("'", "\\'"));
							builder.append("', 'url' : '");
							builder.append(smilies2.getUrl().trim().replace("'", "\\'"));
							builder.append("'},\r\n");
							flag = true;
						}
					}//end for
					if (builder.length() > 0 && flag) {
						builder = builder.delete(builder.length() - 3, builder.length());
						//builder = builder.deleteCharAt(builder.length() - 3);
					}
					builder.append("\r\n],\r\n");
				}//end if		

			}//end for
			builder = builder.delete(builder.length() - 3, builder.length());
			smilesString = builder.toString();
			LForumCache.getInstance().addCache("SmiliesList", smilesString);
		}
		return smilesString;
	}

	/**
	 * 获得表情分类列表
	 * 
	 * @return 表情分类列表
	 */
	public List<Smilies> getSmilieTypesCache() {
		List<Smilies> smilesList = LForumCache.getInstance().getListCache("SmilieTypes", Smilies.class);
		if (smilesList == null) {
			smilesList = smilieManager.getSmilieTypes();
			LForumCache.getInstance().addCache("SmilieTypes", smilesList);
		}
		return smilesList;
	}

	/**
	 * 获取第一页的表情
	 * 
	 * @return 获取第一页的表情
	 */
	public String getSmiliesFirstPageCache() {
		String smiliesFirstPage = LForumCache.getInstance().getCache("SmiliesFirstPage", String.class);
		if (smiliesFirstPage == null) {
			StringBuilder builder = new StringBuilder();
			List<Smilies> smileList = smilieManager.getSmiliesList();
			for (int i = 0; i < smileList.size(); i++) {
				// 如果是表情分类
				if (smileList.get(i).getType() == 0) {
					builder.append("'" + smileList.get(i).getCode().trim().replace("'", "\\'") + "': [\r\n");
					boolean flag = false;
					int smiliescount = 0;
					for (int j = 0; j < smileList.size(); j++) {
						// 如果属于以上表情分类
						if (smileList.get(j).getType().equals(smileList.get(i).getId()) && smiliescount < 16) {
							builder.append("{'code' : '");
							builder.append(smileList.get(j).getCode().trim().replace("'", "\\'"));
							builder.append("', 'url' : '");
							builder.append(smileList.get(j).getUrl().trim().replace("'", "\\'"));
							builder.append("'},\r\n");
							flag = true;
							smiliescount++;
						}
					}//end for
					if (builder.length() > 0 && flag) {
						builder = builder.delete(builder.length() - 3, builder.length());
					}
					builder.append("\r\n],\r\n");
				}//end if		

			}//end for
			builder = builder.delete(builder.length() - 3, builder.length());
			smiliesFirstPage = builder.toString();
			LForumCache.getInstance().addCache("SmiliesFirstPage", smiliesFirstPage);
		}
		return smiliesFirstPage;
	}

	/**
	 * 返回模板列表的下拉框html
	 * 
	 * @return 下拉框html
	 */
	public String getTemplateListBoxOptionsCache() {
		String templateListBoxOptions = LForumCache.getInstance().getCache("TemplateListBoxOptions", String.class);
		if (templateListBoxOptions == null) {
			synchronized (synObject) {
				StringBuilder sb = new StringBuilder();
				List<Templates> templateList = templateManager.getValidTemplateList();
				for (Templates templates : templateList) {
					sb.append("<li class=\"current\">");
					sb.append("<a href=\"#\" onclick=\"window.location.href='showtemplate.action?templateid="
							+ templates.getTemplateid() + "'\">");
					sb.append(templates.getName().trim());
					sb.append("</a>");
					sb.append("</li>");
				}
				templateListBoxOptions = sb.toString();
				LForumCache.getInstance().addCache("TemplateListBoxOptions", templateListBoxOptions);
			}
		}
		return templateListBoxOptions;
	}

	/**
	 * 获得在线用户列表图例
	 * @return 在线用户列表图例
	 */
	public String getOnlineGroupIconList() {
		String onlineGroupIconList = LForumCache.getInstance().getCache("OnlineGroupIconList", String.class);
		if (onlineGroupIconList == null) {
			List<Onlinelist> iconList = onlineUserManager.getOnlineGroupIconList();
			StringBuilder sb = new StringBuilder();
			for (Onlinelist onlinelist : iconList) {
				sb.append("<img src=\"images/groupicons/");
				sb.append(onlinelist.getImg());
				sb.append("\" /> ");
				sb.append(onlinelist.getTitle());
				sb.append(" &nbsp; &nbsp; &nbsp; ");
			}
			onlineGroupIconList = sb.toString();
			LForumCache.getInstance().addCache("OnlineGroupIconList", onlineGroupIconList);
		}
		return onlineGroupIconList;
	}

	/**
	 * 获得友情链接列表
	 * @return 友情链接列表
	 */
	@SuppressWarnings("unchecked")
	public List<Forumlinks> getForumLinkList() {
		List<Forumlinks> forumlinkList = LForumCache.getInstance().getListCache("ForumLinkList", Forumlinks.class);
		if (forumlinkList == null) {
			forumlinkList = forumlinkDAO.createCriteria(Property.forName("displayorder").gt(0)).add(
					Property.forName("logo").ne("")).addOrder(Order.asc("displayorder")).list();
			LForumCache.getInstance().addCache("ForumLinkList", forumlinkList);
		}
		return forumlinkList;
	}

	/**
	 * 前台版块列表弹出菜单
	 * @param usergroupid 用户组id
	 * @param userid 当前用户id
	 * @param extname 扩展名称
	 * @return 版块列表弹出菜单
	 */
	public String getForumListMenuDiv(int usergroupid, int userid, String extname) {
		String forumListMenuDiv = LForumCache.getInstance().getCache("ForumListMenuDiv", String.class);
		if (forumListMenuDiv == null) {
			StringBuilder sb = new StringBuilder();
			List<Forums> forumList = forumManager.getForumList();
			if (forumList.size() > 0) {
				sb
						.append("<div class=\"popupmenu_popup\" id=\"forumlist_menu\" style=\"overflow-y: auto; display:none\">");
				for (Forums forums : forumList) {
					if (forums.getLayer() >= 0 && forums.getLayer() <= 1 && forums.getStatus() == 1) {
						// 判断是否为私有论坛
						if (!forums.getForumfields().getViewperm().trim().equals("")
								&& !Utils.inArray(usergroupid + "", forums.getForumfields().getViewperm())) {
							// 暂无处理
						} else {
							if (forums.getLayer() == 0) { // 如果是论坛分类
								sb.append("<dl>");
								sb.append("<dt>");
								sb.append("<a href=\"showforum.action?forumid=");
								sb.append(forums.getFid());
								sb.append("\">");
								sb.append(forums.getName());
								sb.append("</a></dt>");
								sb.append("<dd><ul>");
								for (Forums forum : forumList) {
									if (Utils.null2Int(forum.getParentidlist().split(",")[0].trim()) == forums.getFid()
											&& forum.getLayer() == 1) {
										// 判断是否为第一级板块
										sb.append("<li><a href=\"showforum.action?forumid=");
										sb.append(forum.getFid());
										sb.append("\">");
										sb.append(forum.getName());
										sb.append("</a></li>");
									}
								}//end for
								sb.append("</ul></dd>");
								sb.append("</dl>");
							}//end if

						}//end if
					}
				}//end for
			}
			sb.append("</div>");
			forumListMenuDiv = sb.toString().replace("<dd><ul></ul></dd>", "");
			LForumCache.getInstance().addCache("ForumListMenuDiv", forumListMenuDiv);
		}
		return forumListMenuDiv;
	}

	/**
	 * 获得主题类型数组
	 * 
	 * @return 主题类型数组
	 */
	@SuppressWarnings("unchecked")
	public Map<Integer, Object> getTopicTypeArray() {
		Map<Integer, Object> topicTypeArray = (Map<Integer, Object>) LForumCache.getInstance().getCache(
				"TopicTypeArray");
		if (topicTypeArray == null) {
			topicTypeArray = new HashMap<Integer, Object>();
			List<Object[]> objList = forumlinkDAO.find("select typeid,name from Topictypes order by displayorder");
			if (objList.size() > 0) {
				for (Object[] objects : objList) {
					if (Utils.null2String(objects[0]) != "" && Utils.null2String(objects[1]) != "") {
						topicTypeArray.put(Utils.null2Int(objects[0]), objects[1]);
					}
				}
			}
			LForumCache.getInstance().addCache("TopicTypeArray", topicTypeArray);
		}
		return topicTypeArray;
	}

	/**
	 * 获得版块下拉列表
	 * @return 列表内容的html
	 */
	@SuppressWarnings("unchecked")
	public String getForumListBoxOptionsCache() {
		String forumListBoxOptions = LForumCache.getInstance().getCache("ForumListBoxOptions", String.class);
		if (forumListBoxOptions == null) {
			StringBuilder sb = new StringBuilder();
			List<Object[]> objList = forumlinkDAO
					.find("select name,fid,layer from Forums where forums.fid not in (select fid from Forums where status<1 and layer=0) and status>0 and displayorder>=0 order by displayorder");
			for (Object[] objects : objList) {
				sb.append("<option value=\"");
				sb.append(objects[1]);
				sb.append("\">");
				sb.append(Utils.getSpacesString(Utils.null2Int(objects[2], 0)));
				sb.append(objects[0].toString().trim());
				sb.append("</option>");
			}
			forumListBoxOptions = sb.toString();
			LForumCache.getInstance().addCache("ForumListBoxOptions", forumListBoxOptions);
		}
		return forumListBoxOptions;
	}

	/**
	 * 获得编辑器自定义按钮信息的javascript数组
	 * @return 表情符的javascript数组
	 */
	public String getCustomEditButtonList() {
		String str = LForumCache.getInstance().getCache("CustomEditButtonList", String.class);
		if (str != null) {
			return str;
		}
		StringBuilder sb = new StringBuilder();
		List<Bbcodes> bbcodeList = editorManager.getCustomEditButtonList();

		for (Bbcodes bbcodes : bbcodeList) {
			//说明:[标签名,对应图标文件名,[参数1描述,参数2描述,...],[参数1默认值,参数2默认值,...]]
			//实例["fly","swf.gif",["请输入flash网址","请输入flash宽度","请输入flash高度"],["http://","200","200"],3]
			sb.append(",'" + Utils.replaceStrToScript(bbcodes.getTag()) + "':['");
			sb.append(Utils.replaceStrToScript(bbcodes.getTag()));
			sb.append("','");
			sb.append(Utils.replaceStrToScript(bbcodes.getIcon()));
			sb.append("','");
			sb.append(Utils.replaceStrToScript(bbcodes.getExplanation()));
			sb.append("',['");
			sb.append(Utils.replaceStrToScript(bbcodes.getParamsdescript()).replace(",", "','"));
			sb.append("'],['");
			sb.append(Utils.replaceStrToScript(bbcodes.getParamsdefvalue()).replace(",", "','"));
			sb.append("'],");
			sb.append(Utils.replaceStrToScript(bbcodes.getParams().toString()));
			sb.append("]");
		}
		if (sb.length() > 0) {
			sb = sb.delete(0, 1);
		}
		str = sb.toString().replace("\r\n", "");
		LForumCache.getInstance().addCache("CustomEditButtonList", str);
		return str;
	}

	/**
	 * 返回脏字过滤列表
	 * @return 返回脏字过滤列表数组
	 */
	@SuppressWarnings("unchecked")
	public String[][] getBanWordList() {
		String[][] str = (String[][]) LForumCache.getInstance().getCache("BanWordList");
		if (str != null) {
			return str;
		}
		List list = forumlinkDAO.find("select find,replacement from Words");
		if (list == null) {
			return str;
		}
		List<Object[]> words = list;
		str = new String[words.size()][2];
		String temp = "";
		Pattern pattern = Pattern.compile("\\{(\\d+)\\}");
		for (int i = 0; i < words.size(); i++) {
			temp = Utils.null2String(words.get(i)[0]);
			Matcher matcher = pattern.matcher(temp);
			for (int j = 0; j < matcher.groupCount(); j++) {
				temp = temp.replace(matcher.group(j), matcher.group(j).replace("{", ".{0,"));
			}
			str[i][0] = temp.replace("\\", "\\\\").replace("\"", "\\\"").replace("'", "\'").replace("[", "\\[")
					.replace("]", "\\]");
			str[i][1] = Utils.null2String(words.get(i)[1]);
		}
		LForumCache.getInstance().addCache("BanWordList", str);
		return str;
	}

	/**
	 * 替换原始字符串中的脏字词语
	 * @param text 原始字符串
	 * @return 替换后的结果
	 */
	public String banWordFilter(String text) {
		StringBuilder sb = new StringBuilder(text);
		String[][] str = getBanWordList();
		int count = str.length / 2;
		for (int i = 0; i < count; i++) {
			if (!str[i][1].equals("{BANNED}") && !str[i][1].equals("{MOD}")) {
				sb = new StringBuilder().append(sb.toString().replace(str[i][0], str[i][1]));
			}
		}
		return sb.toString();
	}

	/**
	 * 判断字符串是否包含脏字词语
	 * @param text 原始字符串
	 * @return 如果包含则返回true, 否则返回false
	 */
	public boolean hasBannedWord(String text) {
		String[][] str = getBanWordList();
		int count = str.length / 2;
		Pattern r_word = null;
		for (int i = 0; i < count; i++) {
			if (str[i][1].equals("{BANNED}")) {

				r_word = Pattern.compile(str[i][0]);
				Matcher matcher = r_word.matcher(text);
				while (matcher.find()) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 指定的字符串中是否含有需要审核词汇
	 */
	public boolean hasAuditWord(String text) {
		String[][] str = getBanWordList();
		int count = str.length / 2;
		Pattern r_word = null;
		for (int i = 0; i < count; i++) {
			if (str[i][1].equals("{MOD}")) {

				r_word = Pattern.compile(str[i][0]);
				Matcher matcher = r_word.matcher(text);
				while (matcher.find()) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 获取主题鉴定项
	 * @param identifyid 主题签定id
	 * @return 主题鉴定信息
	 */
	public Topicidentify getTopicIdentify(int identifyid) {
		for (Topicidentify ti : getTopicIdentifyList()) {
			if (ti.getIdentifyid() == identifyid) {
				return ti;
			}
		}
		return new Topicidentify();
	}

	/**
	 * 获取主题签定集合项
	 * @return 主题签定集合项
	 */
	public List<Topicidentify> getTopicIdentifyList() {
		List<Topicidentify> topicidentifyList = LForumCache.getInstance().getListCache("TopicIdentifyList",
				Topicidentify.class);
		if (topicidentifyList != null) {
			return topicidentifyList;
		}
		topicidentifyList = new ArrayList<Topicidentify>();
		topicidentifyList = topicidentifyDAO.findAll();
		StringBuilder jsArray = new StringBuilder("<script type='text/javascript'>var topicidentify = { ");

		for (Topicidentify topicidentify : topicidentifyList) {
			jsArray.append("'" + topicidentify.getIdentifyid() + "':'" + topicidentify.getFilename() + "',");
		}

		jsArray.deleteCharAt(jsArray.length() - 1);
		jsArray.append("};</script>");
		LForumCache.getInstance().addCache("TopicIdentifyList", topicidentifyList);
		LForumCache.getInstance().addCache("TopicIndentifysJsArray", jsArray.toString());
		return topicidentifyList;
	}

	/**
	 * 获得勋章列表
	 * @return 勋章列表
	 */
	@SuppressWarnings("unchecked")
	public Map<String, String> getMedalsList() {
		Map<String, String> medalsMap = (Map<String, String>) LForumCache.getInstance().getCache("MedalsList");
		if (medalsMap == null) {
			medalsMap = new HashMap<String, String>();
			List<Medals> medalList = medalDAO.findAll();
			for (Medals medals : medalList) {
				if (medals.getAvailable() == 1) {
					if (!medals.getImage().trim().equals("")) {
						medals.setImage("<img border=\"0\" src=\"images/medals/" + medals.getImage() + "\" alt=\""
								+ medals.getName() + "\" title=\"" + medals.getName() + "\" class=\"medals\" />");
					} else {
						medals.setImage("");
					}
				} else {
					medals.setImage("");
				}
				medalsMap.put(medals.getName(), medals.getImage());
			}
			LForumCache.getInstance().addCache("MedalsList", medalsMap);
		}
		return medalsMap;
	}

	/**
	 * 获取指定id的勋章列表html
	 * @param mdealList 勋章id
	 * @return 勋章列表html
	 */
	public String getMedalsList(String mdealList) {
		Map<String, String> medalsMap = getMedalsList();
		String[] list = mdealList.split(",");
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < list.length; i++) {
			sb.append(medalsMap.get(list[i]));
		}
		return sb.toString();
	}

	/**
	 * 获取自带头像列表
	 * @return 获取自带头像列表
	 */
	public List<String> getAvatarList() {
		List<String> avatarList = LForumCache.getInstance().getListCache("CommonAvatarList", String.class);
		if (avatarList == null) {
			avatarList = new ArrayList<String>();
			File dirinfo = new File(ConfigLoader.getConfig().getWebpath() + "avatars/common/");
			String extname = "";
			for (File file : dirinfo.listFiles()) {
				if (file != null) {
					extname = file.getName().substring(file.getName().lastIndexOf(".")).toLowerCase();
					if (extname.equals(".jpg") || extname.equals(".gif") || extname.equals(".png")) {
						avatarList.add("avatars/common/" + file.getName());
					}
				}
			}
			LForumCache.getInstance().addCache("CommonAvatarList", avatarList);
		}
		return avatarList;
	}

	/**
	 * 获取主题鉴定图片地址缓存数组
	 * @return 主题鉴定图片地址缓存数组
	 */
	public String getTopicIdentifyFileNameJsArray() {
		String jsArray = LForumCache.getInstance().getCache("TopicIndentifysJsArray", String.class);
		if (jsArray == null) {
			getTopicIdentifyList();
			jsArray = LForumCache.getInstance().getCache("TopicIndentifysJsArray", String.class);
		}
		return jsArray;
	}
}
