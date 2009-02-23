package com.javaeye.lonlysky.lforum.entity.global;

import java.io.Serializable;

public class AttachmentType implements Serializable {
	
	private static final long serialVersionUID = -1103979153116755825L;
	private String typeName;
	private int typeId;
	private String extName;

	public AttachmentType() {
	}

	public AttachmentType(String name, int id, String extname) {
		typeName = name;
		typeId = id;
		extName = extname;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public int getTypeId() {
		return typeId;
	}

	public void setTypeId(int typeId) {
		this.typeId = typeId;
	}

	public String getExtName() {
		return extName;
	}

	public void setExtName(String extName) {
		this.extName = extName;
	}
}
