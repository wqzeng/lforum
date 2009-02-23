package com.javaeye.lonlysky.lforum.web.admin.global;

import java.awt.GraphicsEnvironment;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.struts2.config.ParentPackage;

import com.javaeye.lonlysky.lforum.AdminBaseAction;
import com.javaeye.lonlysky.lforum.comm.LForumRequest;
import com.javaeye.lonlysky.lforum.comm.utils.Utils;
import com.javaeye.lonlysky.lforum.config.impl.ConfigLoader;

/**
 * 附件设置
 * 
 * @author 黄磊
 *
 */
@ParentPackage("default")
public class Global_attachAction extends AdminBaseAction {

	private static final long serialVersionUID = -7580883697250587969L;

	/**
	 * 水印字体选择框
	 */
	private String watermarkfontname = "";

	/**
	 * 水印位置
	 */
	private String watermarkstatus = "";

	@Override
	public String execute() throws Exception {
		if (!LForumRequest.isPost()) {
			// 未提交之前
			loadSystemFont();
			loadPosition(config.getWatermarkstatus());
		} else {
			Map<String, String> map = new HashMap<String, String>();
			map.put("图片附件文字水印大小", LForumRequest.getParamValue("watermarkfontsize"));
			map.put("JPG图片质量", LForumRequest.getParamValue("attachimgquality"));
			map.put("图片最大高度", LForumRequest.getParamValue("attachimgquality"));
			map.put("图片最大宽度", LForumRequest.getParamValue("attachimgmaxwidth"));

			for (Entry<String, String> entry : map.entrySet()) {
				if (!Utils.isInt(entry.getValue())) {
					registerStartupScript("", "<script>alert('输入错误," + entry.getKey()
							+ "只能是0或者正整数');window.location.href='global_attach.action';</script>");
					return SUCCESS;
				}

			}

			if (LForumRequest.getParamIntValue("attachimgquality", 80) > 100
					|| (LForumRequest.getParamIntValue("attachimgquality", 80) < 0)) {
				registerStartupScript("",
						"<script>alert('JPG图片质量只能在0-100之间');window.location.href='global_attach.action';</script>");
				return SUCCESS;
			}

			if (LForumRequest.getParamIntValue("watermarktransparency", 5) > 10
					|| LForumRequest.getParamIntValue("watermarktransparency", 5) < 1) {
				registerStartupScript("",
						"<script>alert('图片水印透明度取值范围1-10');window.location.href='global_attach.action';</script>");
				return SUCCESS;
			}

			if (LForumRequest.getParamIntValue("watermarkfontsize", 10) <= 0) {
				registerStartupScript("",
						"<script>alert('图片附件添加文字水印的大小必须大于0');window.location.href='global_attach.action';</script>");
				return SUCCESS;
			}

			if (LForumRequest.getParamIntValue("attachimgmaxheight", 0) < 0) {
				registerStartupScript("",
						"<script>alert('图片最大高度必须大于或等于0');window.location.href='global_attach.action';</script>");
				return SUCCESS;
			}

			if (LForumRequest.getParamIntValue("attachimgmaxwidth", 0) < 0) {
				registerStartupScript("",
						"<script>alert('图片最大宽度必须大于或等于0');window.location.href='global_attach.action';</script>");
				return SUCCESS;
			}
			
			config.setAttachrefcheck(LForumRequest.getParamIntValue("attachrefcheck", config.getAttachrefcheck()));
			config.setAttachsave(LForumRequest.getParamIntValue("attachsave", config.getAttachsave()));
			config.setWatermarkstatus(LForumRequest.getParamIntValue("watermarkstatus", config.getWatermarkstatus()));
			config.setAttachimgpost(LForumRequest.getParamIntValue("attachimgpost", config.getAttachimgpost()));
			config.setWatermarktype(LForumRequest.getParamIntValue("watermarktype", config.getWatermarktype()));
			config.setShowattachmentpath(LForumRequest.getParamIntValue("showattachmentpath", config
					.getShowattachmentpath()));
			config.setAttachimgmaxheight(LForumRequest.getParamIntValue("attachimgmaxheight", config
					.getAttachimgmaxheight()));
			config.setAttachimgmaxwidth(LForumRequest.getParamIntValue("attachimgmaxwidth", config
					.getAttachimgmaxwidth()));
			config
					.setAttachimgquality(LForumRequest.getParamIntValue("attachimgquality", config
							.getAttachimgquality()));
			config.setWatermarktext(LForumRequest.getParamValue("watermarktext"));
			config.setWatermarkpic(LForumRequest.getParamValue("watermarkpic"));
			config.setWatermarkfontname(LForumRequest.getParamValue("watermarkfontname"));
			config.setWatermarkfontsize(LForumRequest.getParamIntValue("watermarkfontsize", config
					.getWatermarkfontsize()));
			config.setWatermarktransparency(LForumRequest.getParamIntValue("watermarktransparency", config
					.getWatermarktransparency()));

			ConfigLoader configLoader = new ConfigLoader(config.getWebpath());
			configLoader.saveConfig(config);
			adminVistLogManager.insertLog(user, username, usergroup, grouptitle, ip, "附件设置", "");
			registerStartupScript("PAGE", "window.location.href='global_attach.action';");
		}
		return SUCCESS;
	}

	/**
	 * 加载系统字体
	 */
	private void loadSystemFont() {
		String[] fontlist = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
		for (String string : fontlist) {
			if (string.equals(config.getWatermarkfontname().trim())) {
				watermarkfontname += "<option value=\"" + string + "\" selected=\"selected\">" + string + "</option>";
			} else {
				watermarkfontname += "<option value=\"" + string + "\">" + string + "</option>";
			}
		}
	}

	/**
	 * 加载水印设置界面
	 * @param selectid
	 */
	private void loadPosition(int selectid) {
		watermarkstatus = "<table width=\"256\" height=\"207\" border=\"0\" background=\"../images/flower.jpg\">";
		for (int i = 1; i < 10; i++) {
			if (i % 3 == 1)
				watermarkstatus += "<tr>";
			if (selectid == i) {
				watermarkstatus += "<td width=\"33%\" align=\"center\" style=\"vertical-align:middle;\"><input type=\"radio\" id=\"watermarkstatus\" name=\"watermarkstatus\" value=\""
						+ i + "\" checked><b>#" + i + "</b></td>";
			} else {
				watermarkstatus += "<td width=\"33%\" align=\"center\" style=\"vertical-align:middle;\"><input type=\"radio\" id=\"watermarkstatus\" name=\"watermarkstatus\" value=\""
						+ i + "\" ><b>#" + i + "</b></td>";
			}
			if (i % 3 == 0)
				watermarkstatus += "</tr>";
		}
		watermarkstatus += "</table><input type=\"radio\" id=\"watermarkstatus\" name=\"watermarkstatus\" value=\"0\" ";
		if (selectid == 0) {
			watermarkstatus += " checked";
		}
		watermarkstatus += ">不启用水印功能";
	}

	public String getWatermarkfontname() {
		return watermarkfontname;
	}

	public String getWatermarkstatus() {
		return watermarkstatus;
	}
}
