package com.javaeye.lonlysky.lforum.web;

import java.io.File;
import java.util.List;

import org.apache.struts2.config.ParentPackage;

import com.javaeye.lonlysky.lforum.ForumBaseAction;
import com.javaeye.lonlysky.lforum.comm.LForumRequest;
import com.javaeye.lonlysky.lforum.comm.Thumbnail;
import com.javaeye.lonlysky.lforum.comm.utils.ForumUtils;
import com.javaeye.lonlysky.lforum.comm.utils.Utils;
import com.javaeye.lonlysky.lforum.entity.forum.Templates;
import com.javaeye.lonlysky.lforum.entity.forum.Users;

/**
 * 用户个性设置
 * 
 * @author 黄磊
 *
 */
@ParentPackage("default")
public class UsercppreferenceAction extends ForumBaseAction {

	private static final long serialVersionUID = 3921179509163879466L;

	/**
	 * 当前用户信息
	 */
	private Users user = new Users();

	/**
	 * 用户头像
	 */
	private String avatar;

	/**
	 * 用户头像地址
	 */
	private String avatarurl;

	/**
	 * 用户头像类型
	 */
	private int avatartype;

	/**
	 * 头像宽度
	 */
	private int avatarwidth;

	/**
	 * 头像高度
	 */
	private int avatarheight;

	/**
	 * 可用的模板列表
	 */
	private List<Templates> templatelist;

	/**
	 * 系统头像列表
	 */
	private List<String> avatarfilelist;

	private File file = new File("");
	private String fileFileName = "";
	private String fileContentType = "";

	@Override
	public String execute() throws Exception {
		pagetitle = "用户控制面板";
		if (userid == -1) {
			reqcfg.addErrLine("你尚未登录");
			return SUCCESS;
		}
		user = userManager.getUserInfo(userid);

		avatarwidth = 100;
		avatarheight = 100;

		if (LForumRequest.isPost()) {
			int avatartype = LForumRequest.getParamIntValue("avatartype", -1);
			if (avatartype != -1) {
				switch (avatartype) {
				case 0: //从系统选择
					avatar = LForumRequest.getParamValue("usingavatar");
					avatar = Utils.urlDecode(avatar.substring(avatar.indexOf("avatar")));
					avatarwidth = 0;
					avatarheight = 0;
					if (!Utils.fileExists(config.getWebpath() + avatar)) {
						reqcfg.addErrLine("不存在的头像文件");
						return SUCCESS;
					}

					break;

				case 1: //上传头像

					if (usergroupinfo.getAllowavatar() < 3) {
						reqcfg.addErrLine("您当前的身份 \"" + usergroupinfo.getGrouptitle() + "\" 没有上传头像的权限");
						return SUCCESS;
					}
					avatar = ForumUtils.saveRequestAvatarFile(file, fileFileName, fileContentType, userid, config
							.getMaxavatarsize());
					if (avatar.equals("")) {
						reqcfg.addErrLine("头像图片不合法, 系统要求必须为gif jpg png图片, 且宽高不得超过 " + config.getMaxavatarwidth() + "x"
								+ config.getMaxavatarheight() + ", 大小不得超过 " + config.getMaxavatarsize() + " 字节");
						return SUCCESS;
					}
					Thumbnail thumb = new Thumbnail();
					if (!thumb.setImage(avatar)) {
						reqcfg.addErrLine("非法的图片格式");
						return SUCCESS;
					}
					thumb.saveThumbnailImage(config.getMaxavatarwidth(), config.getMaxavatarheight());
					avatarwidth = 0;
					avatarheight = 0;
					break;

				case 2: //自定义头像Url

					if (usergroupinfo.getAllowavatar() < 2) {
						reqcfg.addErrLine("您当前的身份 \"" + usergroupinfo.getGrouptitle() + "\" 没有使用自定义头像的权限");
						return SUCCESS;
					}
					avatar = LForumRequest.getParamValue("avatarurl").trim();
					if (avatar.length() < 10) {
						reqcfg.addErrLine("头像路径不合法");

						return SUCCESS;
					}
					if (!avatar.substring(0, 7).toLowerCase().equals("http://")) {
						reqcfg.addErrLine("头像路径必须以http://开始");
						return SUCCESS;
					}
					String fileextname = avatar.substring(avatar.lastIndexOf(".") + 1).toLowerCase();
					// 判断 文件扩展名/文件大小/文件类型 是否符合要求
					if (!(fileextname.equals(".jpg") || fileextname.equals(".gif") || fileextname.equals(".png"))) {
						reqcfg.addErrLine("头像路径必须是.jpg .gif或.png结尾");
						return SUCCESS;
					}

					avatarwidth = LForumRequest.getParamIntValue("avatarwidth", config.getMaxavatarwidth());
					avatarheight = LForumRequest.getParamIntValue("avatarheight", config.getMaxavatarheight());
					if (avatarwidth <= 0 || avatarwidth > config.getMaxavatarwidth() || avatarheight <= 0
							|| avatarheight > config.getMaxavatarheight()) {
						reqcfg.addErrLine("自定义URL地址头像尺寸必须大于0, 且必须小于系统当前设置的最大尺寸 " + config.getMaxavatarwidth() + "x"
								+ config.getMaxavatarheight());
						return SUCCESS;
					}
					break;
				}
			} else {
				//当允许使用头像时
				if (usergroupinfo.getAllowavatar() > 0) {
					reqcfg.addErrLine("请指定新头像的信息<br />");
					return SUCCESS;
				}
			}

			//当不允许使用头像时
			if (usergroupinfo.getAllowavatar() == 0) {
				avatar = user.getUserfields().getAvatar().trim();
				avatarwidth = user.getUserfields().getAvatarwidth();
				avatarheight = user.getUserfields().getAvatarheight();
			}
			user.getUserfields().setAvatar(avatar);
			user.getUserfields().setAvatarwidth(avatarwidth);
			user.getUserfields().setAvatarheight(avatarheight);
			user.setTemplateid(LForumRequest.getParamIntValue("templateid", 0));
			userManager.updateUserInfo(user);
			reqcfg.setUrl("usercppreference.action").setMetaRefresh().setShowBackLink(true).addMsgLine("修改个性设置完毕");
		} else {
			templatelist = templateManager.getValidTemplateList();
			avatarfilelist = cachesManager.getAvatarList();

			Users userinfo = userManager.getUserInfo(userid);
			avatar = userinfo.getUserfields().getAvatar().trim();
			avatarurl = "";
			avatartype = 1;
			avatarwidth = 0;
			avatarheight = 0;
			if (avatar.substring(0, 15).toLowerCase().equals("avatars\\common\\")) {
				avatartype = 0;
			} else if (avatar.substring(0, 7).toLowerCase().equals("http://")) {
				avatarurl = avatar;
				avatartype = 2;
				avatarwidth = userinfo.getUserfields().getAvatarwidth();
				avatarheight = userinfo.getUserfields().getAvatarheight();
			}
		}
		return SUCCESS;
	}

	public Users getUser() {
		return user;
	}

	public String getAvatar() {
		return avatar;
	}

	public String getAvatarurl() {
		return avatarurl;
	}

	public int getAvatartype() {
		return avatartype;
	}

	public int getAvatarwidth() {
		return avatarwidth;
	}

	public int getAvatarheight() {
		return avatarheight;
	}

	public List<Templates> getTemplatelist() {
		return templatelist;
	}

	public List<String> getAvatarfilelist() {
		return avatarfilelist;
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public String getFileFileName() {
		return fileFileName;
	}

	public void setFileFileName(String fileFileName) {
		this.fileFileName = fileFileName;
	}

	public String getFileContentType() {
		return fileContentType;
	}

	public void setFileContentType(String fileContentType) {
		this.fileContentType = fileContentType;
	}
}
