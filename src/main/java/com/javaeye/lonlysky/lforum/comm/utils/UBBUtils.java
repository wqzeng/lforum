package com.javaeye.lonlysky.lforum.comm.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.javaeye.lonlysky.lforum.entity.forum.CustomEditorButtonInfo;
import com.javaeye.lonlysky.lforum.entity.forum.PostpramsInfo;
import com.javaeye.lonlysky.lforum.entity.forum.Smilies;

/**
 * UBB处理类
 * 
 * @author 黄磊
 *
 */
public class UBBUtils {

	public static String[] regxs = new String[20];

	static {
		regxs[0] = "\\s*\\[code\\]([\\s\\S]+?)\\[/code\\]\\s*";
		regxs[1] = "\\[upload=([^\\]]{1,4})\\])(.*?)(\\[\\/upload\\])";
		regxs[2] = "(\\[uploadimage\\])(.*?)(\\[\\/uploadimage\\])";
		regxs[3] = "(\\[uploadfile\\])(.*?)(\\[\\/uploadfile\\])";
		regxs[4] = "(\\[upload\\])(.*?)(\\[\\/upload\\])";
		regxs[5] = "(\r\n((&nbsp;)|　)+)(?<正文>\\S+)";
		regxs[6] = "\\s*\\[hide\\][\n\r]*([\\s\\S]+?)[\n\r]*\\[\\/hide\\]\\s*";
		regxs[7] = "\\[table(?:=(\\d{1,4}%?)(?:,([\\(\\)%,#\\w ]+))?)?\\]\\s*([\\s\\S]+?)\\s*\\[\\/table\\]";
		regxs[8] = "\\[media=(\\w{1,4}),(\\d{1,4}),(\\d{1,4}),(\\d)\\]\\s*([^\\[\\<\r\n]+?)\\s*\\[\\/media\\]";
		regxs[9] = "\\[attach\\](\\d+)(\\[/attach\\])*";
		regxs[10] = "\\[attachimg\\](\\d+)(\\[/attachimg\\])*";
		regxs[11] = "\\s*\\[free\\][\n\r]*([\\s\\S]+?)[\n\r]*\\[\\/free\\]\\s*";
	}

	/**
	 * UBB代码处理函数
	 * @param postpramsinfo UBB转换参数表
	 * @return 输出字符串
	 */
	public static String uBBToHTML(PostpramsInfo postpramsinfo) {

		Matcher m;
		Pattern pattern;

		String sDetail = postpramsinfo.getSdetail();
		postpramsinfo.setAllowhtml(1);

		StringBuilder sb = new StringBuilder();
		int pcodecount = -1;
		String sbStr = "";
		String prefix = postpramsinfo.getPid() + "";
		Map<Integer, Object> map = new HashMap<Integer, Object>();
		map.put(0, pcodecount);
		map.put(1, sb);

		// 处理code标签
		if (postpramsinfo.getBbcodeoff() == 0) {
			pattern = Pattern.compile(regxs[0]);
			m = pattern.matcher(sDetail);
			while (m.find()) {
				sbStr = parsecode(m.group(1), prefix, map);
				pcodecount = Utils.null2Int(map.get(0));
				sb = (StringBuilder) map.get(1);
				sDetail = sDetail.replace(m.group(0), sbStr);
			}
		}

		if (postpramsinfo.getBbcodeoff() == 0) {
			sDetail = hideDetail(sDetail, postpramsinfo.getHide());
		}

		//清除无效的smilie标签
		sDetail = sDetail.replaceAll("\\[smilie\\](.+?)\\[\\/smilie\\]", "$1");

		// 建立smile表情标签
		if (postpramsinfo.getSmileyoff() == 0 && postpramsinfo.getSmiliesinfo() != null) {
			sDetail = parseSmilies(sDetail, postpramsinfo.getSmiliesinfo(), postpramsinfo.getSmiliesmax());
		}
		// [smilie]处标记
		sDetail = sDetail.replaceAll("\\[smilie\\](.+?)\\[\\/smilie\\]", "<img src=\"$1\" />");

		if (postpramsinfo.getBbcodeoff() == 0) {

			pattern = Pattern.compile(regxs[11]);
			m = pattern.matcher(sDetail);
			while (m.find()) {
				sDetail = sDetail.replace(m.group(0),
						"<br /><div class=\"msgheader\">免费内容:</div><div class=\"msgborder\">" + m.group(1)
								+ "</div><br />");
			}

			// Bold, Italic, Underline
			sDetail = sDetail.replaceAll("\\[b(?:\\s*)\\]", "<b>");
			sDetail = sDetail.replaceAll("\\[i(?:\\s*)\\]", "<i>");
			sDetail = sDetail.replaceAll("\\[u(?:\\s*)\\]", "<u>");
			sDetail = sDetail.replaceAll("\\[/b(?:\\s*)\\]", "</b>");
			sDetail = sDetail.replaceAll("\\[/i(?:\\s*)\\]", "</i>");
			sDetail = sDetail.replaceAll("\\[/u(?:\\s*)\\]", "</u>");

			// Sub/Sup
			//
			sDetail = sDetail.replaceAll("\\[sup(?:\\s*)\\]", "<sup>");
			sDetail = sDetail.replaceAll("\\[sub(?:\\s*)\\]", "<sub>");
			sDetail = sDetail.replaceAll("\\[/sup(?:\\s*)\\]", "</sup>");
			sDetail = sDetail.replaceAll("\\[/sub(?:\\s*)\\]", "</sub>");

			// P
			//
			sDetail = sDetail.replaceAll("((\r\n)*\\[p\\])(.*?)((\r\n)*\\[\\/p\\])", "<p>$3</p>");

			// Anchors
			//
			sDetail = sDetail.replaceAll("\\[url(?:\\s*)\\](www\\.(.*?))\\[/url(?:\\s*)\\]",
					"<a href=\"http://$1\" target=\"_blank\">$1</a>");
			sDetail = sDetail
					.replaceAll(
							"\\[url(?:\\s*)\\]\\s*((https?://|ftp://|gopher://|news://|telnet://|rtsp://|mms://|callto://|bctp://|ed2k://|tencent)([^\\[\"\"']+?))\\s*\\[\\/url(?:\\s*)\\]",
							"<a href=\"$1\" target=\"_blank\">$1</a>");
			sDetail = sDetail.replaceAll("\\[url=www.([^\\[\"\"']+?)(?:\\s*)\\]([\\s\\S]+?)\\[/url(?:\\s*)\\]",
					"<a href=\"http://www.$1\" target=\"_blank\">$2</a>");
			sDetail = sDetail
					.replaceAll(
							"\\[url=((https?://|ftp://|gopher://|news://|telnet://|rtsp://|mms://|callto://|bctp://|ed2k://|tencent://)([^\\[\"\"']+?))(?:\\s*)\\]([\\s\\S]+?)\\[/url(?:\\s*)\\]",
							"<a href=\"$1\" target=\"_blank\">$4</a>");

			// Email
			//
			sDetail = sDetail.replaceAll("\\[email(?:\\s*)\\](.*?)\\[\\/email\\]",
					"<a href=\"mailto:$1\" target=\"_blank\">$1</a>");
			sDetail = sDetail.replaceAll("\\[email=(.[^\\[]*)(?:\\s*)\\](.*?)\\[\\/email(?:\\s*)\\]",
					"<a href=\"mailto:$1\" target=\"_blank\">$2</a>");

			// Font
			sDetail = sDetail.replaceAll("\\[color=([^\\[\\<]+?)\\]", "<font color=\"$1\">");
			sDetail = sDetail.replaceAll("\\[size=(\\d+?)\\]", "<font size=\"$1\">");
			sDetail = sDetail.replaceAll("\\[size=(\\d+(\\.\\d+)?(px|pt|in|cm|mm|pc|em|ex|%)+?)\\]",
					"<font style=\"font-size: $1\">");
			sDetail = sDetail.replaceAll("\\[font=([^\\[\\<]+?)\\]", "<font face=\"$1\">");

			sDetail = sDetail.replaceAll("\\[align=([^\\[\\<]+?)\\]", "<p align=\"$1\">");
			sDetail = sDetail.replaceAll("\\[float=(left|right)\\]",
					"<br style=\"clear: both\"><span style=\"float: $1;\">");

			sDetail = sDetail.replaceAll("\\[/color(?:\\s*)\\]", "</font>");
			sDetail = sDetail.replaceAll("\\[/size(?:\\s*)\\]", "</font>");
			sDetail = sDetail.replaceAll("\\[/font(?:\\s*)\\]", "</font>");
			sDetail = sDetail.replaceAll("\\[/align(?:\\s*)\\]", "</p>");
			sDetail = sDetail.replaceAll("\\[/float(?:\\s*)\\]", "</span>");

			// BlockQuote
			//
			sDetail = sDetail.replaceAll("\\[indent(?:\\s*)\\]", "<blockquote>");
			sDetail = sDetail.replaceAll("\\[/indent(?:\\s*)\\]", "</blockquote>");
			sDetail = sDetail.replaceAll("\\[simpletag(?:\\s*)\\]", "<blockquote>");
			sDetail = sDetail.replaceAll("\\[/simpletag(?:\\s*)\\]", "</blockquote>");

			// List
			//
			sDetail = sDetail.replaceAll("\\[list\\]", "<ul>");
			sDetail = sDetail.replaceAll("\\[list=(1|A|a|I|i| )\\]", "<ul type=\"$1\">");
			sDetail = sDetail.replaceAll("\\[\\*\\]", "<li>");
			sDetail = sDetail.replaceAll("\\[/list\\]", "</ul>");

			// Font
			sDetail = sDetail.replaceAll("\\[color=([^\\[\\<]+?)\\]([\\s]||[\\s\\S]+?)\\[/color(?:\\s*)\\]",
					"<font color=\"$1\">$2</font>");
			sDetail = sDetail.replaceAll("\\[size=(\\d+?)\\]([\\s]||[\\s\\S]+?)\\[/size(?:\\s*)\\]",
					"<font size=\"$1\">$2</font>");
			sDetail = sDetail.replaceAll(
					"\\[size=(\\d+(\\.\\d+)?(px|pt|in|cm|mm|pc|em|ex|%)+?)\\]([\\s]||[\\s\\S]+?)\\[/size(?:\\s*)\\]",
					"<font style=\"font-size: $1\">$4</font>");
			sDetail = sDetail.replaceAll("\\[font=([^\\[\\<]+?)\\]([\\s]||[\\s\\S]+?)\\[/font(?:\\s*)\\]",
					"<font face=\"$1\">$2</font>");

			sDetail = sDetail.replaceAll("\\[align=(left|center|right)\\]([\\s]||[\\s\\S]+?)\\[/align(?:\\s*)\\]",
					"<p align=\"$1\">$2</p>");
			sDetail = sDetail.replaceAll("\\[float=(left|right)\\]([\\s]||[\\s\\S]+?)\\[/float(?:\\s*)\\]",
					"<br style=\"clear: both\" /><span style=\"float: $1;\">$2</span>");

			// BlockQuote
			//
			sDetail = sDetail.replaceAll("\\[indent(?:\\s*)\\]([\\s]||[\\s\\S]+?)\\[/indent(?:\\s*)\\]",
					"<blockquote>$1</blockquote>");
			sDetail = sDetail.replaceAll("\\[simpletag(?:\\s*)\\]([\\s]||[\\s\\S]+?)\\[/simpletag(?:\\s*)\\]",
					"<blockquote>$1</blockquote>");

			// List
			//
			sDetail = sDetail.replaceAll("\\[list\\]([\\s]||[\\s\\S]+?)\\[/list\\]", "<ul>$1</ul>");
			sDetail = sDetail.replaceAll("\\[list=(1|A|a|I|i| )\\]([\\s]||[\\s\\S]+?)\\[/list\\]",
					"<ul type=\"$1\">$2</ul>");
			sDetail = sDetail.replaceAll("\\[\\*\\]", "<li>");

			//循环转换table

			sDetail = parseTable(sDetail);

			// shadow
			//
			sDetail = sDetail.replaceAll(
					"(\\[SHADOW=)(\\d*?),(#*\\w*?),(\\d*?)\\]([\\s]||[\\s\\S]+?)(\\[\\/SHADOW\\])",
					"<table width='$2'  style='filter:SHADOW(COLOR=$3, STRENGTH=$4)'>$5</table>");

			// glow
			//
			sDetail = sDetail.replaceAll("(\\[glow=)(\\d*?),(#*\\w*?),(\\d*?)\\]([\\s]||[\\s\\S]+?)(\\[\\/glow\\])",
					"<table width='$2'  style='filter:GLOW(COLOR=$3, STRENGTH=$4)'>$5</table>");

			// center
			//
			sDetail = sDetail.replaceAll("\\[center\\]([\\s]||[\\s\\S]+?)\\[\\/center\\]", "<center>$1</center>");

			// Media
			//             
			pattern = Pattern.compile(regxs[8]);
			m = pattern.matcher(sDetail);
			while (m.find()) {
				String tmp = parseMedia(m.group(1), Utils.null2Int(m.group(2), 64), Utils.null2Int(m.group(3), 48), m
						.group(4).equals("1"), m.group(5));
				sDetail = sDetail.replace(m.group(0), tmp);
			}

			// 自定义标签

			if (postpramsinfo.getCustomeditorbuttoninfo() != null) {
				sDetail = replaceCustomTag(sDetail, postpramsinfo.getCustomeditorbuttoninfo());
			}

			// 处理[quote][/quote]标记

			int intQuoteIndexOf = sDetail.toLowerCase().indexOf("[quote]");
			int quotecount = 0;
			while (intQuoteIndexOf >= 0 && sDetail.toLowerCase().indexOf("[/quote]") >= 0 && quotecount < 3) {
				quotecount++;
				sDetail = sDetail.replaceAll("\\[quote\\]([\\s\\S]+?)\\[/quote\\]",
						"<br /><br /><div class=\"msgheader\">引用:</div><div class=\"msgborder\">$1</div>");

				intQuoteIndexOf = sDetail.toLowerCase().indexOf("[quote]", intQuoteIndexOf + 7);
			}

			//处理[area]标签
			sDetail = sDetail.replaceAll("\\[area=([\\s\\S]+?)\\]([\\s\\S]+?)\\[/area\\]",
					"<br /><br /><div class=\"msgheader\">$1</div><div class=\"msgborder\">$2</div>");

			sDetail = sDetail.replaceAll("\\[area\\]([\\s\\S]+?)\\[/area\\]",
					"<br /><br /><div class=\"msgheader\"></div><div class=\"msgborder\">$1</div>");

		}

		// 将网址字符串转换为链接

		if (postpramsinfo.getParseurloff() == 0) {
			sDetail = sDetail.replaceAll("&amp;", "&");

			// p2p link
			sDetail = sDetail.replaceAll(
					"^((tencent|ed2k|thunder|vagaa):\\/\\/[\\[\\]\\|A-Za-z0-9\\.\\/=\\?%\\-&_~`@':+!]+)",
					"<a target=\"_blank\" href=\"$1\">$1</a>");
			sDetail = sDetail.replaceAll(
					"((tencent|ed2k|thunder|vagaa):\\/\\/[\\[\\]\\|A-Za-z0-9\\.\\/=\\?%\\-&_~`@':+!]+)$",
					"<a target=\"_blank\" href=\"$1\">$1</a>");
			sDetail = sDetail.replaceAll(
					"[^>=\\]\"\"]((tencent|ed2k|thunder|vagaa):\\/\\/[\\[\\]\\|A-Za-z0-9\\.\\/=\\?%\\-&_~`@':+!]+)",
					"<a target=\"_blank\" href=\"$1\">$1</a>");

		}

		// 处理[img][/img]标记

		if (postpramsinfo.getShowimages() == 1) {
			//控制签名html长度
			if (postpramsinfo.getSignature() == 1) {
				sDetail = sDetail.replaceAll("\\[img\\]\\s*(http://[^\\[\\<\r\n]+?)\\s*\\[\\/img\\]",
						"<img src=\"$1\" border=\"0\" />");
				sDetail = sDetail.replaceAll(
						"\\[img=(\\d{1,4})[x|\\,](\\d{1,4})\\]\\s*(http://[^\\[\\<\r\n]+?)\\s*\\[\\/img\\]",
						"<img src=\"$3\" width=\"$1\" height=\"$2\" border=\"0\" />");
				sDetail = sDetail.replaceAll("\\[image\\](http://[\\s\\S]+?)\\[/image\\]",
						"<img src=\"$1\" border=\"0\" />");
			} else {
				sDetail = sDetail.replaceAll("\\[img\\]\\s*(http://[^\\[\\<\r\n]+?)\\s*\\[\\/img\\]",
						"<img src=\"$1\" border=\"0\" onload=\"attachimg(this, 'load');\" onclick=\"zoom(this)\" />");
				sDetail = sDetail
						.replaceAll(
								"\\[img=(\\d{1,4})[x|\\,](\\d{1,4})\\]\\s*(http://[^\\[\\<\r\n]+?)\\s*\\[\\/img\\]",
								"<img src=\"$3\" width=\"$1\" height=\"$2\" border=\"0\" onload=\"attachimg(this, 'load');\" onclick=\"zoom(this)\"  />");
				sDetail = sDetail.replaceAll("\\[image\\](http://[\\s\\S]+?)\\[/image\\]",
						"<img src=\"$1\" border=\"0\" />");
			}
		}

		pcodecount = 0;
		for (String str : sb.toString().split("<>")) {
			sDetail = sDetail.replace("[\tDISCUZ_CODE_" + prefix + "_" + pcodecount + "\t]", str);
			pcodecount++;
		}

		// 处理空格

		sDetail = sDetail.replace("\t", "&nbsp; &nbsp; ");
		sDetail = sDetail.replace("  ", "&nbsp; ");

		// [r/]
		//
		sDetail = sDetail.replaceAll("\\[r/\\]", "\r");

		// [n/]
		//
		sDetail = sDetail.replaceAll("\\[n/\\]", "\n");

		// 处理换行

		//处理换行,在每个新行的前面添加两个全角空格
		//		pattern = Pattern.compile(regxs[5]);
		//		m = pattern.matcher(sDetail);
		//		while (m.find()) {
		//			sDetail = sDetail.replace(m.group(0), "<br />　　" + m.group(0));
		//		}

		//处理换行,在每个新行的前面添加两个全角空格
		sDetail = sDetail.replace("\r\n", "<br />");
		sDetail = sDetail.replace("\r", "");
		sDetail = sDetail.replace("\n\n", "<br /><br />");
		sDetail = sDetail.replace("\n", "<br />");
		sDetail = sDetail.replace("{rn}", "\r\n");
		sDetail = sDetail.replace("{nn}", "\n\n");
		sDetail = sDetail.replace("{r}", "\r");
		sDetail = sDetail.replace("{n}", "\n");

		return sDetail;
	}

	/**
	 * 隐藏[hide]标签中的内容
	 * @param str 帖子内容
	 * @param hide hide标记
	 * @return 帖子内容
	 */
	private static String hideDetail(String str, int hide) {
		if (hide == 0) {
			return str;
		}
		Pattern pattern = Pattern.compile(regxs[6]);
		Matcher m;

		int intTableIndexOf = str.toLowerCase().indexOf("[hide]");

		while (intTableIndexOf >= 0 && str.toLowerCase().indexOf("[/hide]") >= 0) {
			m = pattern.matcher(str);
			while (m.find()) {
				if (hide == 1) {
					str = str.replace(m.group(0),
							"<div class=\"hide\"><div class=\"hidestyle\">***** 该内容需会员回复才可浏览 *****</div></div>");
				} else {
					str = str
							.replace(
									m.group(0),
									"<div class=\"hide\"><div class=\"hidestyle\">以下内容会员跟帖回复才能看到</div><div class=\"hidetext\"><br />==============================<br /><br />"
											+ m.group(1) + "<br /><br />==============================</div></div>");
				}
			}
			if (intTableIndexOf + 7 > str.length()) {
				intTableIndexOf = str.toLowerCase().indexOf("[table", str.length());
			} else {
				intTableIndexOf = str.toLowerCase().indexOf("[table", intTableIndexOf + 7);
			}

		}

		return str;

	}

	/**
	 * 转换表情
	 * @param sDetail 帖子内容
	 * @param smiliesinfo 表情数组
	 * @param smiliesmax 每种表情的最大使用数
	 * @return 帖子内容
	 */
	private static String parseSmilies(String sDetail, List<Smilies> smiliesinfo, int smiliesmax) {
		if (smiliesinfo == null)
			return sDetail;
		for (int i = smiliesinfo.size() - 1; i >= 0; i--) {
			String regxstr = smiliesinfo.get(i).getCode().trim();
			if (smiliesmax > 0) {
				for (int j = 0; j < smiliesmax; j++) {
					sDetail = sDetail.replace(regxstr, "[smilie]editor/images/smilies/" + smiliesinfo.get(i).getUrl()
							+ "[/smilie]");
				}
			} else {
				sDetail = sDetail.replace(regxstr, "[smilie]editor/images/smilies/" + smiliesinfo.get(i).getUrl()
						+ "[/smilie]");
			}
		}
		return sDetail;
	}

	/**
	 * 转换自定义标签
	 * @param sDetail 帖子内容
	 * @param customeditorbuttoninfo 自定义标签数组
	 * @return 帖子内容
	 */
	private static String replaceCustomTag(String sDetail, List<CustomEditorButtonInfo> customeditorbuttoninfo) {
		if (customeditorbuttoninfo == null)
			return sDetail;

		String replacement = "";
		int b_params = 0;
		String tempreplacement;

		Pattern pattern;
		Matcher m;

		StringBuilder regexbuilder = new StringBuilder();

		for (int i = 0; i < customeditorbuttoninfo.size(); i++) {
			// 组装正则表达式
			if (regexbuilder.length() > 0) {
				regexbuilder.delete(0, regexbuilder.length());
			}
			regexbuilder.append("(\\[");
			regexbuilder.append(customeditorbuttoninfo.get(i).getTag());
			if (customeditorbuttoninfo.get(i).getParams() > 1) {
				regexbuilder.append("=");
				for (int j = 2; j <= customeditorbuttoninfo.get(i).getParams(); j++) {
					regexbuilder.append("(.*?)");
					if (j < customeditorbuttoninfo.get(i).getParams()) {
						regexbuilder.append(",");
					}
				}
			}

			regexbuilder.append("\\])([\\s\\S]+?)\\[\\/");
			regexbuilder.append(customeditorbuttoninfo.get(i).getTag());
			regexbuilder.append("\\]");
			System.out.println("正则：" + regexbuilder.toString());
			replacement = customeditorbuttoninfo.get(i).getReplacement();
			b_params = customeditorbuttoninfo.get(i).getParams();

			for (int k = 0; k < customeditorbuttoninfo.get(i).getNest(); k++) {
				pattern = Pattern.compile(regexbuilder.toString());
				m = pattern.matcher(sDetail);
				while (m.find()) {
					tempreplacement = replacement.replace("{1}", m.group(m.groupCount()));				
					if (b_params > 1) {
						for (int j = 2; j <= b_params; j++) {
							if (m.groupCount()+1 > j) {
								tempreplacement = tempreplacement.replace("{" + j + "}", m.group(j));
							}
						}
					}
					sDetail = sDetail.replace(m.group(0), tempreplacement);
					sDetail = sDetail.replace("{RANDOM}", Utils.getRandomString(12));
				}
			}
		}

		return sDetail;
	}

	/**
	 * 转换code标签
	 * @param text 帖子内容
	 * @param prefix 
	 * @param map 转换后的内容和code的数量
	 * @return 帖子内容
	 */
	public static String parsecode(String text, String prefix, Map<Integer, Object> map) {
		text = text.replaceAll("^[\n\r]*([\\s\\S]+?)[\n\r]*$", "$1");
		StringBuilder builder = (StringBuilder) map.get(1);
		int pcodecount = Utils.null2Int(map.get(0));
		if (!builder.toString().equals("")) {
			builder.append("<>");
		}
		builder
				.append("<br /><br /><div class=\"msgheader\"><div class=\"right\"><a href=\"###\" class=\"smalltxt\" onclick=\"copycode($('code"
						+ prefix
						+ "_"
						+ pcodecount
						+ "'));\">[复制到剪贴板]</a></div>CODE:</div><div id=\"code"
						+ prefix
						+ "_" + pcodecount + "\" class=\"msgborder\">");
		builder.append(text);
		builder.append("</div><br /><br />");
		pcodecount++;
		text = "[\tDISCUZ_CODE_" + prefix + "_" + pcodecount + "\t]";
		map.put(0, pcodecount);
		map.put(1, builder);
		return text;
	}

	/**
	 * 转换表格
	 * @param str 帖子内容
	 * @return 帖子内容
	 */
	private static String parseTable(String str) {
		Pattern pattern = Pattern.compile(regxs[7]);
		Matcher m;
		String stable = "";
		String width = "";
		String bgcolor = "";
		int intTableIndexOf = str.toLowerCase().indexOf("[table");

		while (intTableIndexOf >= 0 && str.toLowerCase().indexOf("[/table]") >= 0) {
			m = pattern.matcher(str);
			while (m.find()) {
				width = m.group(1);
				width = width.substring(width.length() - 1, width.length()).equals("%") ? Utils.null2Int(width
						.substring(0, width.length() - 1), 100) < 98 ? width : "98%"
						: (Utils.null2Int(width, 560) <= 560 ? width : "560");

				bgcolor = m.group(2);

				stable = "<table class=\"t_table\" cellspacing=\"1\" cellpadding=\"4\" style=\"";
				stable += width.equals("") ? "" : ("width:" + width + ";");
				stable += "".equals(bgcolor) ? "" : ("background: " + bgcolor + ";");
				stable += "\">";

				width = m.group(3);

				width = width.replaceAll("\\[td=(\\d{1,2}),(\\d{1,2})(,(\\d{1,4}%?))?\\]",
						"<td colspan=\"$1\" rowspan=\"$2\" width=\"$4\" class=\"t_table\">");

				width = width.replaceAll("\\[tr\\]", "<tr>");
				width = width.replaceAll("\\[td\\]", "<td>");
				width = width.replaceAll("\\[\\/td\\]", "</td>");
				width = width.replaceAll("\\[\\/tr\\]", "</tr>");

				width = width.replaceAll("\\<td\\>\\<\\/td\\>", "<td>&nbsp;</td>");

				stable += width;
				stable += "</table>";

				str = str.replace(m.group(0), stable);

			}

			intTableIndexOf = str.toLowerCase().indexOf("[table", intTableIndexOf + 7);

		}

		return str;
	}

	/**
	 * 转换多媒体标签
	 * @param type 类型
	 * @param width 宽
	 * @param height 高
	 * @param autostart 是否自动播放
	 * @param url 地址
	 * @return 转换后的代码
	 */
	public static String parseMedia(String type, int width, int height, boolean autostart, String url) {
		if (!Utils.inArray(type, "ra,rm,wma,wmv,mp3,mov"))
			return "";
		url = url.replace("\\\\", "\\").replace("<", "").replace(">", "");
		Random r = new Random(3);
		String mediaid = "media_" + r.nextInt();
		if (type.equals("ra")) {
			return "<object classid=\"\"clsid:CFCDAA03-8BE4-11CF-B84B-0020AFBBCCFA\"\" width=\"\""
					+ width
					+ "\"\" height=\"\"32\"\"><param name=\"\"autostart\"\" value=\"\""
					+ (autostart ? 1 : 0)
					+ "\"\" /><param name=\"\"src\"\" value=\"\""
					+ url
					+ "\"\" /><param name=\"\"controls\"\" value=\"\"controlpanel\"\" /><param name=\"\"console\"\" value=\"\""
					+ mediaid + "_\"\" /><embed src=\"\"" + url
					+ "\"\" type=\"\"audio/x-pn-realaudio-plugin\"\" controls=\"\"ControlPanel\"\" "
					+ (autostart ? "autostart=\"true\"" : "") + " console=\"\"" + mediaid + "_\"\" width=\"\"" + width
					+ "\"\" height=\"\"32\"\"></embed></object>";
		} else if (type.equals("rm")) {
			return "<object classid=\"\"clsid:CFCDAA03-8BE4-11cf-B84B-0020AFBBCCFA\"\" width=\"\""
					+ width
					+ "\"\" height=\"\""
					+ height
					+ "\"\"><param name=\"\"autostart\"\" value=\"\""
					+ (autostart ? 1 : 0)
					+ "\"\" /><param name=\"\"src\"\" value=\"\""
					+ url
					+ "\"\" /><param name=\"\"controls\"\" value=\"\"imagewindow\"\" /><param name=\"\"console\"\" value=\"\""
					+ mediaid
					+ "_\"\" /><embed src=\"\""
					+ url
					+ "\"\" type=\"\"audio/x-pn-realaudio-plugin\"\" controls=\"\"IMAGEWINDOW\"\" console=\"\""
					+ mediaid
					+ "_\"\" width=\"\""
					+ width
					+ "\"\" height=\"\""
					+ height
					+ "\"\"></embed></object><br /><object classid=\"\"clsid:CFCDAA03-8BE4-11CF-B84B-0020AFBBCCFA\"\" width=\"\""
					+ width
					+ "\"\" height=\"\"32\"\"><param name=\"\"src\"\" value=\"\""
					+ url
					+ "\"\" /><param name=\"\"controls\"\" value=\"\"controlpanel\"\" /><param name=\"\"console\"\" value=\"\""
					+ mediaid + "_\"\" /><embed src=\"\"" + url
					+ "\"\" type=\"\"audio/x-pn-realaudio-plugin\"\" controls=\"\"ControlPanel\"\" "
					+ (autostart ? "autostart=\"true\"" : "") + " console=\"\"" + mediaid + "_\"\" width=\"\"" + width
					+ "\"\" height=\"\"32\"\"></embed></object>";
		} else if (type.equals("wma")) {
			return "<object classid=\"\"clsid:6BF52A52-394A-11d3-B153-00C04F79FAA6\"\" width=\"\"" + width
					+ "\"\" height=\"\"64\"\"><param name=\"\"autostart\"\" value=\"\"" + (autostart ? 1 : 0)
					+ "\"\" /><param name=\"\"url\"\" value=\"\"" + url + "\"\" /><embed src=\"\"" + url
					+ "\"\" autostart=\"\"" + (autostart ? 1 : 0) + "\"\" type=\"\"audio/x-ms-wma\"\" width=\"\""
					+ width + "\"\" height=\"\"64\"\"></embed></object>";
		} else if (type.equals("wmv")) {
			return "<object classid=\"\"clsid:6BF52A52-394A-11d3-B153-00C04F79FAA6\"\" width=\"\"" + width
					+ "\"\" height=\"\"" + height + "\"\"><param name=\"\"autostart\"\" value=\"\""
					+ (autostart ? 1 : 0) + "\"\" /><param name=\"\"url\"\" value=\"\"" + url
					+ "\"\" /><embed src=\"\"" + url + "\"\" autostart=\"\"" + (autostart ? 1 : 0)
					+ "\"\" type=\"\"video/x-ms-wmv\"\" width=\"\"" + width + "\"\" height=\"\"" + height
					+ "\"\"></embed></object>";
		} else if (type.equals("mp3")) {
			return "<object classid=\"\"clsid:6BF52A52-394A-11d3-B153-00C04F79FAA6\"\" width=\"\"" + width
					+ "\"\" height=\"\"64\"\"><param name=\"\"autostart\"\" value=\"\"" + (autostart ? 1 : 0)
					+ "\"\"/><param name=\"\"url\"\" value=\"\"" + url + "\"\" /><embed src=\"\"" + url
					+ "\"\" autostart=\"\"" + (autostart ? 1 : 0)
					+ "\"\" type=\"\"application/x-mplayer2\"\" width=\"\"" + width
					+ "\"\" height=\"\"64\"\"></embed></object>";
		} else if (type.equals("mov")) {
			return "<object classid=\"\"clsid:02BF25D5-8C17-4B23-BC80-D3488ABDDC6B\"\" width=\"\"" + width
					+ "\"\" height=\"\"" + height + "\"\"><param name=\"\"autostart\"\" value=\"\"" + autostart
					+ "\"\" /><param name=\"\"src\"\" value=\"\"" + url
					+ "\"\" /><embed controller=\"\"true\"\" width=\"\"" + width + "\"\" height=\"\"" + height
					+ "\"\" src=\"\"" + url + "\"\" autostart=\"\"" + autostart + "\"\"></embed></object>";
		} else {
			return "";
		}
	}

	/**
	 * 清除UBB标签
	 * @param sDetail 帖子内容
	 * @return 帖子内容
	 */
	public static String clearUBB(String sDetail) {
		return sDetail.replaceAll("\\[[^\\]]*?\\]", "");
	}

	/**
	 * 清除BR
	 * @param sDetail 帖子内容
	 * @return 帖子内容
	 */
	public static String clearBR(String sDetail) {
		return sDetail.replaceAll("[\r\n]", "");
	}

	/**
	 * 清除[attach][attachimg]标签
	 * @param sDetail
	 * @return
	 */
	public static String clearAttachUBB(String sDetail) {
		sDetail = sDetail.replaceAll(regxs[9], "");
		return sDetail.replaceAll(regxs[10], "");
	}

	public static void main(String[] agos) {
		String a = ":)test";
		a = a.replaceAll(":\\)", "default/14.gif");
		System.out.println(a);
	}
}
