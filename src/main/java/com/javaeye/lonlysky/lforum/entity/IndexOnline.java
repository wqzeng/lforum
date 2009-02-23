package com.javaeye.lonlysky.lforum.entity;

import com.javaeye.lonlysky.lforum.entity.forum.Online;

public class IndexOnline extends Online {
	private static final long serialVersionUID = -5578401236708452890L;
	
	private String actionname;

	public void setActionname(String actionname) {
		this.actionname = actionname;
	}

	public String getActionname() {
		return actionname;
	}
	

}
