package com.javaeye.lonlysky.lforum.web.admin.forum;

import org.apache.struts2.config.ParentPackage;

import com.javaeye.lonlysky.lforum.AdminBaseAction;
import com.javaeye.lonlysky.lforum.comm.LForumRequest;
import com.javaeye.lonlysky.lforum.comm.utils.Utils;

/**
 * 论坛设置选项
 * 
 * @author 黄磊
 *
 */
@ParentPackage("default")
public class Forum_optionAction extends AdminBaseAction {

	private static final long serialVersionUID = -2923627504975329862L;

	private int ratevalveset1;
	private int ratevalveset2;
	private int ratevalveset3;
	private int ratevalveset4;
	private int ratevalveset5;

	@Override
	public String execute() throws Exception {
		String[] ratevalveset = config.getRatevalveset().split(",");
		ratevalveset1 = Utils.null2Int(ratevalveset[0]);
		ratevalveset2 = Utils.null2Int(ratevalveset[1]);
		ratevalveset3 = Utils.null2Int(ratevalveset[2]);
		ratevalveset4 = Utils.null2Int(ratevalveset[3]);
		ratevalveset5 = Utils.null2Int(ratevalveset[4]);
	
		if (LForumRequest.isPost()) {
			
		}
		return SUCCESS;
	}

	public int getRatevalveset1() {
		return ratevalveset1;
	}

	public int getRatevalveset2() {
		return ratevalveset2;
	}

	public int getRatevalveset3() {
		return ratevalveset3;
	}

	public int getRatevalveset4() {
		return ratevalveset4;
	}

	public int getRatevalveset5() {
		return ratevalveset5;
	}
}
