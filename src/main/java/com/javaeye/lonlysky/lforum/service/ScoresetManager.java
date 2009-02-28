package com.javaeye.lonlysky.lforum.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.dom4j.Document;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.javaeye.lonlysky.lforum.cache.LForumCache;
import com.javaeye.lonlysky.lforum.comm.utils.Utils;
import com.javaeye.lonlysky.lforum.comm.utils.XmlElementUtil;
import com.javaeye.lonlysky.lforum.config.impl.ConfigLoader;
import com.javaeye.lonlysky.lforum.entity.forum.UserExtcreditsInfo;
import com.javaeye.lonlysky.lforum.exception.WebException;

/**
 * 积分策略操作
 * 
 * @author 黄磊
 *
 */
@Service
@Transactional
public class ScoresetManager {

	private static final Logger logger = LoggerFactory.getLogger(ScoresetManager.class);

	/**
	 * 获取积分策略XML文档对象
	 * 
	 * @param appPath 系统路径
	 * @return 文档
	 */
	public Document getScoreDoc(String appPath) {
		Document document = null;
		String filePath = appPath + "WEB-INF/config/scoreset.xml";
		if (Utils.fileExists(filePath)) {
			document = XmlElementUtil.readXML(filePath);
		} else {
			logger.error("找不到积分策略配置文件：" + filePath);
			throw new WebException("找不到积分策略配置文件：" + filePath);
		}
		return document;
	}

	/**
	 * 获取积分策略集合(表格)
	 * 
	 * @return 获取积分策略集合
	 */
	public String[][] getScoreSets() {
		String[][] scoreSets = (String[][]) LForumCache.getInstance().getCache("ScoreSets");
		if (scoreSets == null) {
			// 13行9列的表格
			scoreSets = new String[13][9];
			Document document = getScoreDoc(ConfigLoader.getConfig().getWebpath());
			// 循环Record节点并加入到集合中
			List<Element> recordList = XmlElementUtil.findElements("record", document.getRootElement());
			int i = 0; // 行标识			
			for (Element element : recordList) {
				scoreSets[i][0] = XmlElementUtil.findElement("name", element).getTextTrim();
				for (int j = 1; j <= 8; j++) {
					// 循环列
					scoreSets[i][j] = XmlElementUtil.findElement("extcredits" + j, element).getTextTrim();
					System.out.println(scoreSets[i][j]);
				}
				i++;
			}
			LForumCache.getInstance().addCache("ScoreSets", scoreSets);
		}
		return scoreSets;
	}

	/**
	 * 获取积分策略集合
	 * 
	 * @return 积分策略集合
	 */
	public List<UserExtcreditsInfo> getScoreSet(String appPath) {
		List<UserExtcreditsInfo> extcreditsList = LForumCache.getInstance().getListCache("ScoreSet",
				UserExtcreditsInfo.class);
		if (extcreditsList != null) {
			return extcreditsList;
		}
		extcreditsList = new ArrayList<UserExtcreditsInfo>();
		Document document = getScoreDoc(appPath);
		// 循环Record节点并加入到集合中
		List<Element> recordList = XmlElementUtil.findElements("record", document.getRootElement());
		int i = 0; // 用来对应XML中第几个Record
		UserExtcreditsInfo creditInfo = null;
		for (Element record : recordList) {
			// 总共允许有8个扩展积分项目
			// 循环8次分别根据不同积分策略设置对象属性
			for (int j = 1; j <= 8; j++) {
				try {
					creditInfo = extcreditsList.get(j - 1);
				} catch (IndexOutOfBoundsException e) {
					// 数组索引越界则直接创建
					creditInfo = new UserExtcreditsInfo();
				}
				switch (i) {
				case 0: //积分名称
					creditInfo.setName(getString("extcredits" + j, record));
					break;
				case 1: //积分单位
					creditInfo.setUnit(getString("extcredits" + j, record));
					break;
				case 2: //兑换比率
					creditInfo.setRate(getDouble("extcredits" + j, record));
					break;
				case 3: //注册初始积分
					creditInfo.setInit(getDouble("extcredits" + j, record));
					break;
				case 4: //发主题(＋)
					creditInfo.setTopic(getDouble("extcredits" + j, record));
					break;
				case 5: //回复(＋)
					creditInfo.setReply(getDouble("extcredits" + j, record));
					break;
				case 6: //加精华(＋)
					creditInfo.setDigest(getDouble("extcredits" + j, record));
					break;
				case 7: //上传附件(＋)
					creditInfo.setUpload(getDouble("extcredits" + j, record));
					break;
				case 8: //下载附件(－)
					creditInfo.setDownload(getDouble("extcredits" + j, record));
					break;
				case 9: //发短消息(－)
					creditInfo.setPm(getDouble("extcredits" + j, record));
					break;
				case 10: //搜索(－)
					creditInfo.setSearch(getDouble("extcredits" + j, record));
					break;
				case 11: //交易成功(＋)
					creditInfo.setPay(getDouble("extcredits" + j, record));
					break;
				case 12: //参与投票(＋)
					creditInfo.setVote(getDouble("extcredits" + j, record));
					break;
				}

				try {
					extcreditsList.set(j - 1, creditInfo);
				} catch (IndexOutOfBoundsException e) {
					// 数组越界异常则直接添加
					extcreditsList.add(creditInfo);
					if (logger.isDebugEnabled() && !creditInfo.getName().equals("")) {
						logger.debug("获取到积分策略：{}", creditInfo.getName());
					}
				}
			}
			i++;
		}
		LForumCache.getInstance().addCache("ScoreSet", extcreditsList);
		return extcreditsList;
	}

	/**
	 * 获取积分策略集合
	 * 
	 * @return 积分策略集合
	 */
	public List<UserExtcreditsInfo> getScoreSet() {
		return getScoreSet(ConfigLoader.getConfig().getWebpath());
	}

	/**
	 * 获得积分策略
	 * 
	 * @param extcredits 策略ID 1 - 8
	 * @return 积分策略描述
	 */
	public UserExtcreditsInfo getScoreSet(int extcredits) {
		List<UserExtcreditsInfo> creditList = getScoreSet();
		try {
			return creditList.get(extcredits - 1);
		} catch (IndexOutOfBoundsException e) {
			return null;
		}
	}

	/**
	 * 返回前台可以使用的扩展字段名和显示名称
	 * @return 前台可以使用的扩展字段名名和显示名称
	 */
	public String[] getValidScoreName() {
		String[] score = (String[]) LForumCache.getInstance().getCache("ValidScoreName");
		if (score != null) {
			return score;
		}
		// 为了前台模板中的可读性, score中有效元素也对应extcredits1 - 8字段, score[0]无用
		score = new String[9];
		score[0] = "";
		List<UserExtcreditsInfo> creditList = getScoreSet();
		int i = 1;
		for (UserExtcreditsInfo userExtcreditsInfo : creditList) {
			score[i] = userExtcreditsInfo.getName();
			i++;
		}
		LForumCache.getInstance().addCache("ValidScoreName", score);
		return score;
	}

	/**
	 * 返回前台可以使用的扩展字段单位
	 * @return 前台可以使用的扩展字段单位
	 */
	public String[] getValidScoreUnit() {
		String[] score = (String[]) LForumCache.getInstance().getCache("ValidScoreUnit");
		if (score != null) {
			return score;
		}
		// 为了前台模板中的可读性, score中有效元素也对应extcredits1 - 8字段, score[0]无用
		score = new String[9];
		score[0] = "";
		List<UserExtcreditsInfo> creditList = getScoreSet();
		int i = 1;
		for (UserExtcreditsInfo userExtcreditsInfo : creditList) {
			score[i] = userExtcreditsInfo.getUnit();
			i++;
		}
		LForumCache.getInstance().addCache("ValidScoreUnit", score);
		return score;
	}

	/**
	 * 返回交易积分
	 * @return 交易积分
	 */
	public int getCreditsTrans() {
		return getCreditsTrans(ConfigLoader.getConfig().getWebpath());
	}

	/**
	 * 返回交易积分
	 * @return 交易积分
	 */
	public int getCreditsTrans(String apppath) {
		String trans = LForumCache.getInstance().getCache("CreditsTrans", String.class);
		if (trans != null) {
			return Utils.null2Int(trans, 0);
		}
		Document document = getScoreDoc(apppath);
		trans = document.selectSingleNode("/scoreset/formula/creditstrans").getText();
		LForumCache.getInstance().addCache("CreditsTrans", trans);
		return Utils.null2Int(trans, 0);
	}

	/**
	 * 获得积分规则
	 * @return
	 */
	public String getScoreCalFormula() {
		return getScoreCalFormula(ConfigLoader.getConfig().getWebpath());
	}

	/**
	 * 获得积分规则
	 * @param apppath
	 * @return
	 */
	public String getScoreCalFormula(String apppath) {
		String forumla = LForumCache.getInstance().getCache("Formula", String.class);
		if (forumla != null) {
			return forumla;
		}
		Document document = getScoreDoc(apppath);
		forumla = document.selectSingleNode("/scoreset/formula/formulacontext").getText();
		LForumCache.getInstance().addCache("Formula", forumla);
		return forumla;
	}

	/**
	 * 确认当前时间是否在指定的时间列表内
	 * @param timelist 一个包含多个时间段的列表(格式为HH:mm-HH:mm)
	 * @return 符合条件的第一个是时间段，如果存在时间段则返回""
	 * @throws ParseException 
	 */
	public String betweenTime(String timelist) throws ParseException {
		if (!timelist.equals("")) {
			String[] enabledvisittime = timelist.split("\n");
			if (enabledvisittime.length > 0) {
				String starttime = "";
				int s = 0;
				String endtime = "";
				int e = 0;
				for (String visittime : enabledvisittime) {
					if (logger.isDebugEnabled()) {
						logger.debug("当前对比时间段{}", visittime);
					}
					visittime = visittime.trim();
					Pattern pattern = Pattern
							.compile("^((([0-1]?[0-9])|(2[0-3])):([0-5]?[0-9])-(([0-1]?[0-9])|(2[0-3])):([0-5]?[0-9]))$");
					if (pattern.matcher(visittime).find()) {
						starttime = Utils.getNowShortDate() + " " + visittime.substring(0, visittime.indexOf("-"));
						s = Utils.strDateDiffMinutes(starttime + ":00", 0);
						endtime = Utils.getNowShortDate() + " " + visittime.substring(visittime.indexOf("-") + 1);
						e = Utils.strDateDiffMinutes(endtime + ":00", 0);
						if ((new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(starttime)).before((new SimpleDateFormat(
								"yyyy-MM-dd HH:mm").parse(endtime)))) {
							//起始时间小于结束时间,认为未跨越0点
							if (s > 0 && e < 0) {
								return visittime;
							}
						} else { //起始时间大于结束时间,认为跨越0点
							if ((s < 0 && e < 0) || (s > 0 && e > 0 && e > s)) {
								return visittime;
							}
						}
					}
				}
			}
		}
		return "";
	}

	/**
	 * 单主题最高出售时限(小时)
	 * @return
	 */
	public int getMaxChargeSpan() {
		return getMaxChargeSpan(ConfigLoader.getConfig().getWebpath());
	}

	/**
	 * 单主题最高出售时限(小时)
	 * @param apppath
	 * @return
	 */
	public int getMaxChargeSpan(String apppath) {
		String maxchargespan = LForumCache.getInstance().getCache("MaxChargeSpan", String.class);
		if (maxchargespan != null) {
			Utils.null2Int(maxchargespan);
		}
		Document document = getScoreDoc(apppath);
		maxchargespan = document.selectSingleNode("/scoreset/formula/maxchargespan").getText();
		if (logger.isDebugEnabled()) {
			logger.debug("单主题最高出售时限{}小时", maxchargespan);
		}
		LForumCache.getInstance().addCache("MaxChargeSpan", maxchargespan);
		return Utils.null2Int(maxchargespan);
	}

	/**
	 * 单主题最高收入
	 * @return
	 */
	public int getMaxIncPerTopic() {
		return getMaxIncPerTopic(ConfigLoader.getConfig().getWebpath());
	}

	/**
	 * 单主题最高收入
	 * @return
	 */
	public int getMaxIncPerTopic(String apppath) {
		String maxincperthread = LForumCache.getInstance().getCache("MaxIncPerThread", String.class);
		if (maxincperthread != null) {
			Utils.null2Int(maxincperthread);
		}
		Document document = getScoreDoc(apppath);
		maxincperthread = document.selectSingleNode("/scoreset/formula/maxincperthread").getText();
		if (logger.isDebugEnabled()) {
			logger.debug("单主题最高收入{}", maxincperthread);
		}
		LForumCache.getInstance().addCache("MaxIncPerThread", maxincperthread);
		return Utils.null2Int(maxincperthread);
	}

	/**
	 * 返回兑换最低余额
	 * @return
	 */
	public int getExchangeMinCredits() {
		return getExchangeMinCredits(ConfigLoader.getConfig().getWebpath());
	}

	/**
	 * 返回兑换最低余额
	 * @return
	 */
	public int getExchangeMinCredits(String apppath) {
		String exchangemincredits = LForumCache.getInstance().getCache("ExchangeMinCredits", String.class);
		if (exchangemincredits != null) {
			Utils.null2Int(exchangemincredits);
		}
		Document document = getScoreDoc(apppath);
		exchangemincredits = document.selectSingleNode("/scoreset/formula/exchangemincredits").getText();
		if (logger.isDebugEnabled()) {
			logger.debug("返回兑换最低余额{}", exchangemincredits);
		}
		LForumCache.getInstance().addCache("ExchangeMinCredits", exchangemincredits);
		return Utils.null2Int(exchangemincredits);
	}

	/**
	 * 转账最低余额
	 * @return
	 */
	public int getTransferMinCredits() {
		return getTransferMinCredits(ConfigLoader.getConfig().getWebpath());
	}

	/**
	 * 转账最低余额
	 * @return
	 */
	public int getTransferMinCredits(String apppath) {
		String transfermincredits = LForumCache.getInstance().getCache("TransferMinCredits", String.class);
		if (transfermincredits != null) {
			Utils.null2Int(transfermincredits);
		}
		Document document = getScoreDoc(apppath);
		transfermincredits = document.selectSingleNode("/scoreset/formula/transfermincredits").getText();
		if (logger.isDebugEnabled()) {
			logger.debug("转账最低余额{}", transfermincredits);
		}
		LForumCache.getInstance().addCache("TransferMinCredits", transfermincredits);
		return Utils.null2Int(transfermincredits);
	}

	/**
	 * 返回积分交易税
	 * @return
	 */
	public float getCreditsTax() {
		return getCreditsTax(ConfigLoader.getConfig().getWebpath());
	}

	/**
	 * 返回积分交易税
	 * @return
	 */
	public float getCreditsTax(String apppath) {
		String creditstax = LForumCache.getInstance().getCache("CreditsTax", String.class);
		if (creditstax != null) {
			Utils.null2Float(creditstax);
		}
		Document document = getScoreDoc(apppath);
		creditstax = document.selectSingleNode("/scoreset/formula/creditstax").getText();
		if (logger.isDebugEnabled()) {
			logger.debug("返回积分交易税{}", creditstax);
		}
		LForumCache.getInstance().addCache("CreditsTax", creditstax);
		return Utils.null2Float(creditstax);
	}

	/*
	 * 获取String
	 */
	private String getString(String name, Element el) {
		return Utils.null2String(XmlElementUtil.findElement(name, el).getText());
	}

	/*
	 * 获取Float
	 */
	private double getDouble(String name, Element el) {
		return Utils.null2Double(getString(name, el), 0);
	}

	/**
	 * 获取评分操作专用的积分策略
	 * @return 分操作专用的积分策略
	 */
	@SuppressWarnings("unchecked")
	public List<Object[]> getRateScoreSet() {
		List<Object[]> objList = (List<Object[]>) LForumCache.getInstance().getCache("RateScoreSet");
		if (objList == null) {
			objList = new ArrayList<Object[]>();
			List<UserExtcreditsInfo> extList = getScoreSet();
			int i = 1;
			for (UserExtcreditsInfo userExtcreditsInfo : extList) {
				Object[] objects = { i, userExtcreditsInfo.getName(), userExtcreditsInfo.getRate() };
				objList.add(objects);
				i++;
			}
			LForumCache.getInstance().addCache("RateScoreSet", objList);
		}
		return objList;
	}

	/**
	 * 获得具有兑换比率的可交易积分策略
	 * @param type 
	 * @return 兑换比率的可交易积分策略
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getScorePaySet(int type) {
		List<Map<String, Object>> payList = (List<Map<String, Object>>) (type == 0 ? LForumCache.getInstance()
				.getCache("ScorePaySet") : LForumCache.getInstance().getCache("ScorePaySet1"));
		boolean pass = true;
		if (payList != null) {
			return payList;
		} else {
			Document document = getScoreDoc(ConfigLoader.getConfig().getWebpath());
			List<Element> scoreList = XmlElementUtil.findElements("record", document.getRootElement());
			payList = new ArrayList<Map<String, Object>>();
			for (int i = 1; i < 9; i++) {
				if (type == 0) {
					pass = (!XmlElementUtil.findElement("extcredits" + i, scoreList.get(0)).getText().trim().equals(""))
							&& (!XmlElementUtil.findElement("extcredits" + i, scoreList.get(2)).getText().trim()
									.equals("0"));
				} else {
					pass = (!XmlElementUtil.findElement("extcredits" + i, scoreList.get(0)).getText().trim().equals(""));

				}
				if (pass) {
					Map<String, Object> rowMap = new HashMap<String, Object>();
					rowMap.put("id", i);
					rowMap.put("name", XmlElementUtil.findElement("extcredits" + i, scoreList.get(0)).getText().trim());
					rowMap.put("rate", XmlElementUtil.findElement("extcredits" + i, scoreList.get(2)).getText().trim());
					payList.add(rowMap);
				}
			}
			if (type == 0) {
				LForumCache.getInstance().addCache("ScorePaySet", payList);
			} else {
				LForumCache.getInstance().addCache("ScorePaySet1", payList);
			}
			return payList;
		}
	}
}
