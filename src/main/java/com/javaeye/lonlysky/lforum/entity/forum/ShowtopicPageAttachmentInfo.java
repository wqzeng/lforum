package com.javaeye.lonlysky.lforum.entity.forum;

/**
 * 帖子显示页附件信息
 * 
 * @author 黄磊
 *
 */
public class ShowtopicPageAttachmentInfo extends AttachmentInfo {

	private static final long serialVersionUID = -4757881205770886037L;

	private int getattachperm; //下载附件权限
	private int attachimgpost; //附件是否为图片
	private int allowread; //附件是否允许读取	
	private String preview = ""; //预览信息

	public int getGetattachperm() {
		return getattachperm;
	}

	public void setGetattachperm(int getattachperm) {
		this.getattachperm = getattachperm;
	}

	public int getAttachimgpost() {
		return attachimgpost;
	}

	public void setAttachimgpost(int attachimgpost) {
		this.attachimgpost = attachimgpost;
	}

	public int getAllowread() {
		return allowread;
	}

	public void setAllowread(int allowread) {
		this.allowread = allowread;
	}

	public String getPreview() {
		return preview;
	}

	public void setPreview(String preview) {
		this.preview = preview;
	}

}
