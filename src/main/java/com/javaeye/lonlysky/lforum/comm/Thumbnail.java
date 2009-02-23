package com.javaeye.lonlysky.lforum.comm;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.javaeye.lonlysky.lforum.config.impl.ConfigLoader;

/**
 * 缩略图操作类
 * 
 * @author 黄磊
 *
 */
public class Thumbnail {

	private BufferedImage srcImage;
	private String srcFileName;

	/**
	 * 创建
	 * @param fileName 原始图片路径
	 * @return 是否成功
	 */
	public boolean setImage(String fileName) {
		srcFileName = ConfigLoader.getConfig().getWebpath() + fileName;
		try {
			srcImage = ImageIO.read(new FileInputStream(srcFileName));
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	/**
	 * 保存缩略图
	 * @param width 
	 * @param height
	 * @throws IOException
	 */
	public void saveThumbnailImage(int width, int height) throws IOException {
		BufferedImage output = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics2D g2d = output.createGraphics();
		g2d.setBackground(Color.WHITE);
		g2d.fillRect(0, 0, width, height);
		int oWidth = srcImage.getWidth();
		int oHeight = srcImage.getHeight();
		int dx = oWidth < width ? (width - oWidth) / 2 : 0;
		int dy = oHeight < height ? (height - oHeight) / 2 : 0;
		int dw = Math.min(oWidth, width);
		int dh = Math.min(oHeight, height);
		g2d.drawImage(srcImage, dx, dy, dw, dh, null);
		File outputFile = new File(srcFileName);
		ImageIO.write(output, "jpg", outputFile);
	}
}
