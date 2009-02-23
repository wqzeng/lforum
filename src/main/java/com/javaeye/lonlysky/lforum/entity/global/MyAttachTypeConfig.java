package com.javaeye.lonlysky.lforum.entity.global;

import java.io.Serializable;

/**
 * 我的附件类型
 * 
 * @author 黄磊
 *
 */
public class MyAttachTypeConfig implements Serializable {

	private static final long serialVersionUID = -5521335053046337640L;

	public MyAttachTypeConfig() {
	}

	private AttachmentType[] attachmentTypes;

	public AttachmentType[] getAttachmentTypes() {
		return attachmentTypes;
	}

	public void setAttachmentTypes(AttachmentType[] attachmentTypes) {
		this.attachmentTypes = attachmentTypes;
	}
}