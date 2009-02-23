package com.javaeye.lonlysky.lforum.entity.forum;

public class AttachmentInfo extends Attachments {
	private static final long serialVersionUID = 4321097468875704476L;

	private int sys_index; //非数据库字段,用来代替上传文件所对应上传组件(file)的Index
	private String sys_noupload; //非数据库字段,用来存放未被上传的文件名

	public int getSys_index() {
		return sys_index;
	}

	public void setSys_index(int sys_index) {
		this.sys_index = sys_index;
	}

	public String getSys_noupload() {
		return sys_noupload;
	}

	public void setSys_noupload(String sys_noupload) {
		this.sys_noupload = sys_noupload;
	}

}
