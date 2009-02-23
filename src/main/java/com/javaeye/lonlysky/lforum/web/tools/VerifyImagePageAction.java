package com.javaeye.lonlysky.lforum.web.tools;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.OutputStream;

import javax.imageio.ImageIO;

import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;

import com.javaeye.lonlysky.lforum.comm.LForumRequest;
import com.javaeye.lonlysky.lforum.comm.utils.Utils;
import com.javaeye.lonlysky.lforum.config.impl.ConfigLoader;
import com.javaeye.lonlysky.lforum.entity.global.Config;
import com.javaeye.lonlysky.lforum.entity.global.VerifyImageInfo;
import com.javaeye.lonlysky.lforum.plugin.verifyImage.VerifyImage;
import com.javaeye.lonlysky.lforum.service.OnlineUserManager;
import com.opensymphony.xwork2.ActionSupport;

/**
 * 验证码图片页面
 * 
 * @author 黄磊
 *
 */
public class VerifyImagePageAction extends ActionSupport {

	private static final long serialVersionUID = 235034917126355436L;

	@Autowired
	private VerifyImage verifyImage;

	@Autowired
	private OnlineUserManager onlineUserManager;

	@Override
	public String execute() throws Exception {
		Config config = ConfigLoader.getConfig();
		String bgcolor = LForumRequest.getParamValue("bgcolor");
		int textcolor = LForumRequest.getParamIntValue("textcolor", 1);
		String[] bgcolorArray = bgcolor.split(",");

		Color bg = Color.WHITE;

		if (bgcolorArray.length == 1 && !bgcolor.equals("")) {
			bg = Utils.toColor(bgcolor);
		} else if (bgcolorArray.length == 3) {
			bg = new Color(Utils.null2Int(bgcolorArray[0], 255), Utils.null2Int(bgcolorArray[1], 255), Utils.null2Int(
					bgcolorArray[2], 255));
		}
		VerifyImageInfo verifyimg = verifyImage.generateImage(onlineUserManager.updateInfo(config.getPasswordkey(),
				config.getOnlinetimeout()).getVerifycode(), 120, 60, bg, textcolor);

		// 输出验证码到页面
		BufferedImage image = verifyimg.getImage();
		OutputStream out = ServletActionContext.getResponse().getOutputStream();
		try {
			ImageIO.write(image, "JPEG", out);
			out.flush();
			out.close();
		} catch (Exception e) {
		}
		return null;
	}

}
