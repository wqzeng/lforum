package com.javaeye.lonlysky.lforum.service;

import java.awt.Color;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import javax.imageio.ImageIO;

import junit.framework.TestCase;

import com.javaeye.lonlysky.lforum.comm.utils.Utils;
import com.javaeye.lonlysky.lforum.entity.forum.UserExtcreditsInfo;
import com.javaeye.lonlysky.lforum.entity.global.VerifyImageInfo;
import com.javaeye.lonlysky.lforum.plugin.verifyImage.VerifyImage;

public class OtherTest extends TestCase {

	public void test() throws FileNotFoundException, IOException {
		String string = "尊敬的用户:您好!您已在本论坛注册成功, 论坛地址为:FORUMURL感谢您使用FORUMTITLE的服务, 如果您需要了解更多FORUMTITLE相关的信息, 请登录我们的网站: WEBURL希望您继续支持我们WEBTITLE";
		String body = string.replaceAll("WEBTITLE", "LForum");
		body= body.replaceAll("WEBURL", "<a href=test.com>test.com</a>");
		body =body.replaceAll("FORUMURL", "<a href=aaa.com>aaa.com</a>");
		body =body.replaceAll("FORUMTITLE", "LForum");
		body = "<pre style=\"width:100%;word-wrap:break-word\">" + body + "</pre>";
		
		System.out.println(body);
		//		BufferedImage image = ImageIO.read(new FileInputStream("c:/test.jpg"));
		//		ForumUtils.addImageSignText(image, "c:/ttt.jpg", "lonlysky.javaeye.com 测试水印", 2, 0.5f, "宋体", 12);
		//		String string = "abccdccssdaa";
		//
		//		// 计算出现次数最多的字符		
		//		String[] strs = string.split("");
		//		Map<String, Integer> map = new HashMap<String, Integer>();
		//		for (String str : strs) {
		//			if (map.containsKey(str) || str.equals("")) {
		//				// 如果已经存在则重新循环
		//				continue;
		//			}
		//			map.put(str, 0);
		//			for (String str2 : strs) {
		//				// 计算字符出现次数
		//				if (str.equals(str2)) {
		//					map.put(str, map.get(str) + 1);
		//				}
		//			}
		//		}
		//		for (String key : map.keySet()) {
		//			System.out.println("字符：" + key + " 出现：" + map.get(key) + " 次");
		//		}
		//		String ch = string.charAt(0) + "";
		//		int chcount = 0;
		//		Map<String, Integer> map = new HashMap<String, Integer>();
		//		for (int i = 0; i < string.length(); i++) {
		//			char tmp = string.charAt(i);
		//			int count = 0;
		//			if (map.containsKey(tmp + "")) {
		//				// 如果已经存在则重新循环
		//				continue;
		//			}
		//			for (int j = 0; j < string.length(); j++) {
		//				// 计算次数
		//				if (string.charAt(j) == tmp) {
		//					count++;
		//				}
		//			}//end for
		//			map.put(tmp + "", count);
		//			for (String key : map.keySet()) {
		//				// 比较
		//				if (map.get(key) > chcount) {
		//					ch = key;
		//					chcount = map.get(key);
		//				}
		//			}
		//		}
		//System.out.println("字符：" + ch + " 出现次数最多，总共：" + chcount + " 次");
	}

	public void testUtils() throws ParseException {
		assertEquals(false, Utils.isSafeSqlString("("));
		assertEquals(false, Utils.isSafeUserInfoString("*"));
		System.out.println(Utils.getNowTime());
		System.out.println(Utils.getRandomInt(100, 0, 3));
		System.out.println(Utils.getRandomInt(100, 0, 50));
		System.out.println(Utils.toColor("F5FAFE"));
		System.out.println("时间差：" + Utils.strDateDiffMinutes("2009-2-4 13:11:16", 50));
	}

	public void testScoreset() {
		ScoresetManager scoresetManager = new ScoresetManager();
		System.out.println(scoresetManager.getCreditsTrans("E:\\项目\\LForum项目\\工程\\LForum\\webapp\\"));
		List<UserExtcreditsInfo> creditsList = scoresetManager.getScoreSet("E:\\项目\\LForum项目\\工程\\LForum\\webapp\\");
		for (UserExtcreditsInfo userExtcreditsInfo : creditsList) {
			System.out.println("-------------积分策略开始显示-------------");
			System.out.println(userExtcreditsInfo.getName());
			System.out.println(userExtcreditsInfo.getUnit());
			System.out.println(userExtcreditsInfo.getRate());
			System.out.println(userExtcreditsInfo.getDigest());
			System.out.println(userExtcreditsInfo.getDownload());
			System.out.println(userExtcreditsInfo.getInit());
			System.out.println(userExtcreditsInfo.getPay());
			System.out.println(userExtcreditsInfo.getPm());
			System.out.println("-----------------------------------------");
			System.out.println("");
			System.out.println("");
		}
	}

	public void testImage() {
		VerifyImage verifyImage = new VerifyImage();
		VerifyImageInfo verifyImageInfo = verifyImage.generateImage("ab1d5", 100, 40, Color.WHITE, 2);
		FileOutputStream out;
		try {
			out = new FileOutputStream("d://test.jpg");
			ImageIO.write(verifyImageInfo.getImage(), "JPEG", out);
			out.flush();
			out.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
