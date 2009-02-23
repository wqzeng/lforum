package com.javaeye.lonlysky.lforum.plugin.verifyImage;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import org.springframework.stereotype.Service;

import com.javaeye.lonlysky.lforum.comm.utils.Utils;
import com.javaeye.lonlysky.lforum.entity.global.VerifyImageInfo;

/**
 * 验证码图片类
 * 
 * @author 黄磊
 *
 */
@Service
public class VerifyImage {

	private static Font[] fonts = { new Font("Times New Roman", Font.ROMAN_BASELINE, 16 + next(3)),
			new Font("Georgia", Font.ROMAN_BASELINE, 16 + next(3)), new Font("Arial", Font.ROMAN_BASELINE, 16 + next(3)),
			new Font("Comic Sans MS", Font.ROMAN_BASELINE, 16 + next(3)) };

	/**
	 * 获取下一个随机数
	 * @param max 最大值
	 * @return
	 */
	private static int next(int max) {
		return Utils.getRandomInt(150, 0, max);
	}

	/**
	 * 生成验证码图片
	 * @param code 要显示的验证码
	 * @param width 宽度
	 * @param height 高度
	 * @param bgcolor 背景色
	 * @param textcolor 文字颜色
	 * @return
	 */
	public VerifyImageInfo generateImage(String code, int width, int height, Color bgcolor, int textcolor) {
		VerifyImageInfo verifyimage = new VerifyImageInfo();
		verifyimage.setContentType("image/jpeg");

		// 直接指定尺寸, 而不使用外部参数中的建议尺寸
		width = 120;
		height = 40;

		int fixedNumber = textcolor == 2 ? 60 : 0;
		BufferedImage bitmap = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		bitmap.setRGB(0, 0, bgcolor.getRGB());
		
		Graphics g = bitmap.getGraphics();
		g.fillRect(0, 0, width, height);
		g.setColor(new Color(next(50) + fixedNumber, next(50) + fixedNumber, next(50) + fixedNumber));

		// 画弧线
		for (int i = 0; i < 3; i++) {
			g.drawArc(next(20) - 10, next(20) - 10, next(width) + 10, next(height) + 10, Utils.getRandomInt(200, -100,
					100), Utils.getRandomInt(300, -200, 200));
		}

		// 获取随机的颜色
		g.setColor(new Color(next(100), next(100), next(100)));
		
		// 画验证码
		int charx = -5;
		for (int i = 0; i < code.length(); i++) {
			// 用于递增X坐标
			charx = charx + 5 + Utils.getRandomInt(30, 10, 20);
			// 选择随机字体
			g.setFont(fonts[next(fonts.length - 1)]);
			g.drawString(code.charAt(i) + "", charx, 28);
		}

		g.dispose();

		verifyimage.setImage(bitmap);
		return verifyimage;
	}
}
