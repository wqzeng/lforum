package com.javaeye.lonlysky.lforum.entity.global;

import java.awt.image.BufferedImage;

/**
 * 验证码图片信息
 * 
 * @author 黄磊
 *
 */
public class VerifyImageInfo {
	
	private BufferedImage image;
	private String contentType = "image/jpeg";
	
	public BufferedImage getImage() {
		return image;
	}
	public void setImage(BufferedImage image) {
		this.image = image;
	}
	public String getContentType() {
		return contentType;
	}
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	
}
