package com.javaeye.lonlysky.lforum.comm.utils;

import java.awt.Color;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 一些通用的工具处理方法
 * 
 * @author 黄磊
 * 
 */
public class Utils {

	public static final String FULL_DATEFORMAT = "yyyy-MM-dd HH:mm:ss";
	public static final String SHORT_DATEFORMAT = "yyyy-MM-dd";
	public static final String SHORT_TIMEFORMAT = "HH:mm:ss";

	private Utils() {
	}

	/**
	 * 判断是否为INT
	 * @param expression
	 * @return
	 */
	public static boolean isInt(Object expression) {
		if (expression != null) {
			try {
				Integer.parseInt(expression.toString());
			} catch (Exception e) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 判读是否为DOUBLE
	 * @param expression
	 * @return
	 */
	public static boolean isDuble(Object expression) {
		if (expression != null) {
			try {
				Double.parseDouble(expression.toString());
			} catch (Exception e) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 判断给定的字符串数组中的数据是不是都为数值型
	 * @param array 字符串数组
	 * @return 是否成功
	 */
	public static boolean isIntArray(String[] array) {
		if (array == null) {
			return false;
		}
		if (array.length < 1) {
			return false;
		}
		for (String string : array) {
			if (!isInt(string)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 清除HTML标记
	 * 
	 * @param htmlText
	 *            带HTML标记的字符串
	 * @return 纯文本字符串
	 */
	public static String cleanHtmlTag(String htmlText) {
		String reg = "</?[a-z][a-z0-9]*[^<>]*>?";
		return htmlText.replaceAll(reg, "");
	}

	/**
	 * 返回系统当前时间
	 * 
	 * @return String 当前时间
	 */
	public static String getNowTime() {
		return dateFormat(getNowDate(), FULL_DATEFORMAT);
	}

	/**
	 * 格式化内容，只保留前n个字符，并进一步确认是否要在后面加上...
	 * 
	 * @param str
	 *            要处理的字符串
	 * @param num
	 *            保留的字数
	 * @param hasDot
	 *            是否显示...
	 * @return
	 */

	public static String format(String str, int num, boolean hasDot) {
		if (str == null)
			return "";
		else {
			if (str.getBytes().length < num * 2)
				return str;
			else {
				byte[] ss = str.getBytes();
				byte[] bs = new byte[num * 2];
				for (int i = 0; i < bs.length; i++) {
					bs[i] = ss[i];
				}
				String subStr = Utils.substring(str, num * 2);
				if (hasDot) {
					subStr = subStr + "...";
				}
				return subStr;
			}
		}
	}

	/**
	 * 取出指定长度字符串
	 * 
	 * @param s
	 *            字符串
	 * @param maxLength
	 *            长度
	 * @return
	 */
	public static String substring(String s, int maxLength) {
		if (s.getBytes().length <= maxLength)
			return s;
		int i = 0;
		for (int k = 0; k < maxLength && i < s.length(); i++, k++) {
			if (s.charAt(i) > '一') {
				k++;
			}
		}
		if (i < s.length()) {
			s = s.substring(0, i);
		}
		return s;
	}

	/**
	 * 转换对象为字符串
	 * 
	 * @param s
	 *            对象
	 * @return 如果为空则返回""
	 */
	public static String null2String(Object s) {
		return s == null ? "" : s.toString().trim();
	}

	/**
	 * 随机生成指定位数且不重复的字符串.去除了部分容易混淆的字符，如1和l，o和0等，
	 * 
	 * 随机范围1-9 a-z A-Z
	 * 
	 * @param length
	 *            指定字符串长度
	 * @return 返回指定位数且不重复的字符串
	 */
	public static String getRandomString(int length) {
		StringBuffer bu = new StringBuffer();
		String[] arr = { "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k",
				"m", "n", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "A", "B", "C", "D", "E", "F", "G",
				"H", "I", "J", "K", "L", "M", "N", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z" };
		Random random = new Random();
		while (bu.length() < length) {
			String temp = arr[random.nextInt(57)];
			if (bu.indexOf(temp) == -1) {
				bu.append(temp);
			}
		}
		return bu.toString();
	}

	/**
	 * 获取某个范围内的随机整数
	 * 
	 * @param sek
	 *            随机种子
	 * @param start
	 *            最小范围
	 * @param max
	 *            最大范围
	 * @return 整数
	 */
	public static int getRandomInt(int sek, int min, int max) {

		Random random = new Random();

		int temp = 0;

		do {
			temp = random.nextInt(sek);
		} while (temp < min || temp > max);

		return temp;
	}

	/**
	 * 转换对象为INT
	 * 
	 * @param s
	 *            对象
	 * @return 如果为空则返回-1
	 */
	public static int null2Int(Object s) {
		return null2Int(s, -1);
	}

	/**
	 * 转换对象为INT
	 * @param object 对象
	 * @param def 失败默认值
	 * @return INT值
	 */
	public static int null2Int(Object object, int def) {

		if (object != null) {
			try {
				return Integer.parseInt(object.toString());
			} catch (Exception e) {
			}
		}
		return def;
	}

	/**
	 * 转换对象为Long
	 * @param object 对象
	 * @param def 失败默认值
	 * @return Long值
	 */
	public static Long null2Long(Object object, Long def) {

		if (object != null) {
			try {
				return Long.parseLong(object.toString());
			} catch (Exception e) {
			}
		}
		return def;
	}

	/**
	 * 转换对象为Float
	 * 
	 * @param s
	 *            对象
	 * @return 如果为空则返回-1
	 */
	public static float null2Float(Object s) {
		return null2Float(s, -1);
	}

	/**
	 * 转换对象为Float
	 * 
	 * @param s
	 *            对象
	 * @return 如果为空则返回-1
	 */
	public static float null2Float(Object s, float defValue) {
		if (s != null) {
			try {
				return Float.parseFloat(s.toString());
			} catch (Exception e) {
			}
		}
		return defValue;
	}

	/**
	 * 转换对象为Float
	 * 
	 * @param s
	 *            对象
	 * @return 如果为空则返回-1
	 */
	public static double null2Double(Object s, double defValue) {
		if (s != null) {
			try {
				return Double.parseDouble(s.toString());
			} catch (Exception e) {
			}
		}
		return defValue;
	}

	/**
	 * object型转换为bool型
	 * @param expression 要转换的对象
	 * @param defValue 缺省值
	 * @return 转换后的bool类型结果
	 */
	public static boolean null2Boolean(Object expression, boolean defValue) {
		try {
			return Boolean.parseBoolean(null2String(expression));
		} catch (Exception e) {
			return defValue;
		}
	}

	/**
	 * 按照指定方式格式化时间
	 * @param date 时间
	 * @param type 格式
	 * @return
	 */
	public static String dateFormat(Date date, String type) {
		SimpleDateFormat sdf = new SimpleDateFormat(type);
		return sdf.format(date);
	}

	/**
	 * 按照指定方式格式化时间
	 * @param type 格式
	 * @return
	 */
	public static String dateFormat(String type) {
		return dateFormat(getNowDate(), type);
	}

	/**
	 * 返回系统当前时间的date对象
	 * 
	 * @return
	 */
	public static Date getNowDate() {
		return new Date();
	}

	/**
	 * 返回系统当前日期
	 * 
	 * @return String 日期
	 */
	public static String getNowShortDate() {
		return dateFormat(getNowDate(), SHORT_DATEFORMAT);
	}

	/**
	 * 返回系统当前时间
	 * 
	 * @return 时间
	 */
	public static String getNowShortTime() {
		return dateFormat(getNowDate(), SHORT_TIMEFORMAT);
	}

	/**
	 * 判断时间格式
	 * 
	 * @param date
	 * @param format
	 * @return
	 */
	public static boolean checkDate(String date, String format) {
		SimpleDateFormat df = new SimpleDateFormat(format);
		try {
			df.parse(date);
		} catch (Exception e) {
			// 如果不能转换,肯定是错误格式
			return false;
		}
		return true;
	}

	/**
	  * 计算时间差 (时间单位,开始时间,结束时间)<br>
	  * s - 秒,m - 分,h - 时,d - 天
	  * 调用方法   howLong("h","2007-08-09 10:22:26","2007-08-09 20:21:30")  ///9小时56分 返回9小时
	* @throws ParseException 
	  * */
	public static long howLong(String unit, String time1, String time2) throws ParseException {
		//时间单位(如：不足1天(24小时) 则返回0)，开始时间，结束时间
		Date date1 = new SimpleDateFormat(FULL_DATEFORMAT).parse(time1);
		Date date2 = new SimpleDateFormat(FULL_DATEFORMAT).parse(time2);
		long ltime = date1.getTime() - date2.getTime() < 0 ? date2.getTime() - date1.getTime() : date1.getTime()
				- date2.getTime();
		if (unit.equals("s")) {
			return ltime / 1000;//返回秒
		} else if (unit.equals("m")) {
			return ltime / 60000;//返回分钟
		} else if (unit.equals("h")) {
			return ltime / 3600000;//返回小时
		} else if (unit.equals("d")) {
			return ltime / 86400000;//返回天数
		} else {
			return 0;
		}
	}

	/**
	 * 转换字符串为date类型
	 * @param time
	 * @param type
	 * @return
	 * @throws ParseException
	 */
	public static Date parseDate(String time, String type) throws ParseException {
		return new SimpleDateFormat(type).parse(time);
	}

	/**
	 * 转换字符串为date类型
	 * @param time
	 * @return
	 * @throws ParseException
	 */
	public static Date parseDate(String time) throws ParseException {
		return new SimpleDateFormat(FULL_DATEFORMAT).parse(time);
	}

	/**
	 * 返回相差的时间
	 * @param time 时间
	 * @param tim 时间
	 * @param type 类型 'h' - 小时,'m' - 分,'s' - 秒
	 * @return
	 * @throws ParseException
	 */
	public static int strDateDiffTimes(String time, int tim, char type) throws ParseException {
		if (null2String(time).equals("")) {
			return 1;
		}
		long diff = 1;
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new SimpleDateFormat(FULL_DATEFORMAT).parse(time));
		switch (type) {
		case 'h':
			calendar.add(Calendar.HOUR_OF_DAY, tim);
			break;
		case 'm':
			calendar.add(Calendar.MINUTE, tim);
			break;
		case 's':
			calendar.add(Calendar.SECOND, tim);
			break;
		}
		Date date = calendar.getTime();
		long ltime = getNowDate().getTime() - date.getTime();
		switch (type) {
		case 'h':
			diff = ltime / 3600000;//返回小时
			break;
		case 'm':
			diff = ltime / 60000;//返回分
			break;
		case 's':
			diff = ltime / 1000;//返回秒
			break;
		}
		if (diff > Integer.MAX_VALUE) {
			return Integer.MAX_VALUE;
		} else if (diff < Integer.MIN_VALUE) {
			return Integer.MIN_VALUE;
		}
		return Utils.null2Int(diff);
	}

	/**
	 * 返回相差的小时数
	 * @param time 时间
	 * @param hours 小时
	 * @return
	 * @throws ParseException
	 */
	public static int strDateDiffHours(String time, int hours) throws ParseException {
		return strDateDiffTimes(time, hours, 'h');
	}

	/**
	 * 返回相差的分钟数
	 * @param time 时间
	 * @param minutes
	 * @return
	 * @throws ParseException
	 */
	public static int strDateDiffMinutes(String time, int minutes) throws ParseException {
		return strDateDiffTimes(time, minutes, 'm');
	}

	/**
	 * 返回相差的秒数
	 * @param time 时间
	 * @param minutes
	 * @return
	 * @throws ParseException
	 */
	public static int strDateDiffSeconds(String time, int sec) throws ParseException {
		return strDateDiffTimes(time, sec, 's');
	}

	/**
	 * 检测邮件格式
	 * 
	 * @param email
	 * @return
	 */
	public static boolean checkEmail(String email) {
		Pattern pattern = Pattern.compile("\\w+(\\.\\w+)*@\\w+(\\.\\w+)+");
		Matcher matcher = pattern.matcher(email);
		if (matcher.matches())
			return true;
		return false;
	}

	/**
	 * 转换字符串为指定编码格式
	 * 
	 * @param s
	 * @param encoding
	 * @return
	 */
	public static String stringFormat(String s, String encod, String encoding) {
		try {
			return new String(s.getBytes(encod), encoding);
		} catch (UnsupportedEncodingException e) {
			return s;
		}
	}

	public static String stringFormat(String s) {
		return stringFormat(s, "ISO8859-1", "UTF-8");
	}

	/**
	 * 
	 * 提供精确的小数位四舍五入处理。
	 * 
	 * @param v
	 *            需要四舍五入的数字
	 * @param scale
	 *            小数点后保留几位
	 * @return 四舍五入后的结果
	 */
	public static double round(double v, int scale) {

		if (scale < 0) {
			throw new IllegalArgumentException("The scale must be a positive integer or zero");
		}

		BigDecimal b = new BigDecimal(Double.toString(v));

		BigDecimal one = new BigDecimal("1");

		return b.divide(one, scale, BigDecimal.ROUND_HALF_UP).doubleValue();

	}

	/**
	 * URL编码指定字符串
	 * @param s 字符串
	 * @return URL编码结果
	 */
	public static String urlEncode(String s) {
		try {
			return URLEncoder.encode(s, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			return s;
		}
	}

	/**
	 * URL解码指定字符串
	 * @param s 字符串
	 * @return 解码结果
	 */
	public static String urlDecode(String s) {
		try {
			return URLDecoder.decode(s, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			return s;
		}
	}

	/**
	 * 返回指定IP是否在指定的IP数组所限定的范围内<br>
	 * IP数组内的IP地址可以使用*表示该IP段任意, 例如192.168.1.*
	 * 
	 * @param ip
	 * @param ipArry
	 * @return
	 */
	public static boolean inIPArray(String ip, String[] ipArry) {
		String[] userip = ip.split("\\.");
		for (int ipIndex = 0; ipIndex < ipArry.length; ipIndex++) {
			String[] tempip = ipArry[ipIndex].split("\\.");
			int r = 0;
			for (int i = 0; i < tempip.length; i++) {
				if (tempip[i].equals("*")) {
					return true;
				}
				if (userip.length > i) {
					if (tempip[i].equals(userip[i])) {
						r++;
					} else {
						break;
					}
				} else {
					break;
				}
			}// end for
			if (r == 4) {
				return true;
			}
		}// end for
		return false;
	}

	/**
	 * 判断指定字符串在指定字符串数组中的位置
	 * @param strSearch 字符串
	 * @param stringArray 字符串数组
	 * @param caseInsensetive 是否不区分大小写, true为不区分, false为区分
	 * @return 字符串在指定字符串数组中的位置, 如不存在则返回-1
	 */
	public static int getInArrayID(String strSearch, String[] stringArray, boolean caseInsensetive) {
		for (int i = 0; i < stringArray.length; i++) {
			if (caseInsensetive) {
				if (strSearch.toLowerCase().equals(stringArray[i].toLowerCase())) {
					return i;
				}
			} else {
				if (strSearch.equals(stringArray[i])) {
					return i;
				}
			}

		}
		return -1;
	}

	/**
	 * 判断指定字符串在指定字符串数组中的位置
	 * @param strSearch 字符串
	 * @param stringArray 字符串数组
	 * @return 字符串在指定字符串数组中的位置, 如不存在则返回-1
	 */
	public static int getInArrayID(String strSearch, String[] stringArray) {
		return getInArrayID(strSearch, stringArray, true);
	}

	/**
	 * 判断指定字符串是否属于指定字符串数组中的一个元素
	 * @param str 字符串
	 * @param stringArray 字符串数组
	 * @param caseInsensetive 是否不区分大小写, true为不区分, false为区分
	 * @return 判断结果
	 */
	public static boolean inArray(String str, String[] stringArray, boolean caseInsensetive) {
		return getInArrayID(str, stringArray, caseInsensetive) >= 0;
	}

	/**
	 * 判断指定字符串是否属于指定字符串数组中的一个元素
	 * @param str 字符串
	 * @param stringArray 字符串数组
	 * @return 判断结果
	 */
	public static boolean inArray(String str, String[] stringArray) {
		return inArray(str, stringArray, false);
	}

	/**
	 * 判断指定字符串是否属于指定字符串数组中的一个元素
	 * @param str 字符串
	 * @param stringArray 内部以指定符号分割单词的字符串
	 * @param strsplit 分割字符串
	 * @param caseInsensetive 是否不区分大小写, true为不区分, false为区分
	 * @return 判断结果
	 */
	public static boolean inArray(String str, String stringArray, String strsplit, boolean caseInsensetive) {
		return inArray(str, stringArray.split(strsplit), caseInsensetive);
	}

	/**
	 * 判断指定字符串是否属于指定字符串数组中的一个元素
	 * @param str 字符串
	 * @param stringArray 内部以指定符号分割单词的字符串
	 * @param strsplit 分割字符串
	 * @return 判断结果
	 */
	public static boolean inArray(String str, String stringArray, String strsplit) {
		return inArray(str, stringArray.split(strsplit), false);
	}

	/**
	 * 判断指定字符串是否属于指定字符串数组中的一个元素
	 * @param str 字符串
	 * @param stringArray 内部以逗号分割单词的字符串
	 * @return 判断结果
	 */
	public static boolean inArray(String str, String stringArray) {
		return inArray(str, stringArray.split(","), false);
	}

	/**
	 * 检测是否有Sql危险字符
	 * @param str 要判断字符串
	 * @return 判断结果
	 */
	public static boolean isSafeSqlString(String str) {
		Pattern pattern = Pattern.compile("[-|;|,|/|(|)|\\[|\\]|\\}|\\{|%|@|\\*|!|\\']");
		return !pattern.matcher(str).find();
	}

	/**
	 * 检测是否有危险的可能用于链接的字符串
	 * @param str 要判断字符串
	 * @return 判断结果
	 */
	public static boolean isSafeUserInfoString(String str) {
		Pattern pattern = Pattern.compile("^\\s*$|^c:\\con\\con$|[%,\\*\\\\s\\t\\<\\>\\&]|游客|^Guest");
		return !pattern.matcher(str).find();
	}

	/**
	 * 返回文件是否存在
	 * 
	 * @param filePath 文件名
	 * @return 是否存在
	 */
	public static boolean fileExists(String filePath) {
		File file = new File(filePath);
		return file.exists();
	}

	/**
	 * 生成指定数量的html空格符号
	 * @param spacesCount
	 * @return
	 */
	public static String getSpacesString(int spacesCount) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < spacesCount; i++) {
			sb.append("&nbsp;&nbsp;");
		}
		return sb.toString();
	}

	/**
	 * 将字符串转换为Color
	 * @param color
	 * @return
	 */
	public static Color toColor(String color) {
		int red, green, blue = 0;
		char[] rgb;
		color = "#" + color.trim();
		color = color.toLowerCase().replaceAll("[g-zG-Z]", "");
		switch (color.length()) {
		case 3:
			rgb = color.toCharArray();
			red = Utils.null2Int(rgb[0] + "" + rgb[0], 16);
			green = Utils.null2Int(rgb[1] + "" + rgb[1], 16);
			blue = Utils.null2Int(rgb[2] + "" + rgb[2], 16);
			return new Color(red, green, blue);
		case 6:
			rgb = color.toCharArray();
			red = Utils.null2Int(rgb[0] + "" + rgb[1], 16);
			green = Utils.null2Int(rgb[2] + "" + rgb[3], 16);
			blue = Utils.null2Int(rgb[4] + "" + rgb[5], 16);
			return new Color(red, green, blue);
		default:
			return Color.decode(color);
		}
	}

	/**
	 * 为脚本替换特殊字符串
	 * @param str
	 * @return
	 */
	public static String replaceStrToScript(String str) {
		str = str.replace("\\", "\\\\");
		str = str.replace("'", "\\'");
		str = str.replace("\"", "\\\"");
		return str;
	}

	/**
	 * 判断文件名是否为浏览器可以直接显示的图片文件名
	 * @param filename 文件名
	 * @return 是否可以直接显示
	 */
	public static boolean isImgFilename(String filename) {
		filename = filename.trim();
		if (filename.endsWith(".") || filename.indexOf(".") == -1) {
			return false;
		}
		String extname = filename.substring(filename.lastIndexOf(".") + 1).toLowerCase();
		return (extname.equals("jpg") || extname.equals("jpeg") || extname.equals("png") || extname.equals("bmp") || extname
				.equals("gif"));
	}

	/**
	 * 检测是否为IP
	 * @param ip
	 * @return
	 */
	public static boolean isIPSect(String ip) {
		return Pattern
				.compile(
						"^((2[0-4]\\d|25[0-5]|[01]?\\d\\d?)\\.){2}((2[0-4]\\d|25[0-5]|[01]?\\d\\d?|\\*)\\.)(2[0-4]\\d|25[0-5]|[01]?\\d\\d?|\\*)$")
				.matcher(ip).find();

	}

	/**
	 * 判断是否为时间
	 * @param timeval
	 * @return
	 */
	public static boolean isTime(String timeval) {
		return Pattern.compile("^((([0-1]?[0-9])|(2[0-3])):([0-5]?[0-9])(:[0-5]?[0-9])?)$").matcher(timeval).find();
	}

	public static boolean isRuleTip(Map<String, String> newHash, String ruletype, Map<Integer, String> keyMap) {
		keyMap.put(0, "");
		ruletype = ruletype.trim().toLowerCase();
		Set<Entry<String, String>> entrys = newHash.entrySet();
		for (Entry<String, String> entry : entrys) {
			String[] single = entry.getValue().split("\r\n");
			for (String str : single) {
				try {
					if (!str.equals("")) {

						if (ruletype.equals("email")) {
							if (!checkEmail(str)) {
								throw new Exception();
							}
						} else if (ruletype.equals("ip")) {
							if (!isIPSect(str)) {
								throw new Exception();
							}
						} else if (ruletype.equals("timesect")) {
							String[] splitetime = str.split("-");
							if (!isTime(splitetime[1]) || !isTime(splitetime[0])) {
								throw new Exception();
							}
						}
					}
				} catch (Exception e) {
					keyMap.put(0, entry.getKey());
					return false;
				}
			}
		}

		return true;

	}
}