package com.javaeye.lonlysky.lforum.comm.utils;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import org.apache.commons.lang.time.DateUtils;

/**
 * HQL语句工具
 * 
 * @author 黄磊
 *
 */
public class HQLUitls {

	/**
	 * 获取板块显示条件
	 * 
	 * @param sqlid 条件ID
	 * @param cond 条件2
	 * @return HQL语句
	 */
	public static String showforumcondition(int sqlid, int cond) {
		String sql = "";
		switch (sqlid) {
		case 1:
			sql = " and typeid=";
			break;
		case 2:
			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.DAY_OF_MONTH, -1 * cond);
			sql = " and postdatetime>='" + Utils.dateFormat(calendar.getTime(), Utils.FULL_DATEFORMAT) + "'";
			break;
		case 3:
			sql = "tid";
			break;

		}
		return sql;
	}

	/**
	 * 获取主题显示过滤HQL
	 * @param filter
	 * @return
	 */
	public static String getTopicFilterCondition(String filter) {
		filter = filter.toLowerCase().trim();
		String str = "";
		if (filter.equals("poll")) {
			str = " and special=1 ";
		} else if (filter.equals("reward")) {
			str = " and special=2 or special=3 ";
		} else if (filter.equals("rewarded")) {
			str = " and special=3 ";
		} else if (filter.equals("rewarding")) {
			str = " and special=2 ";
		} else if (filter.equals("debate")) {
			str = " and special=4 ";
		} else if (filter.equals("digest")) {
			str = " and digest>0 ";
		}
		return str;
	}

	/**
	 * 获取忒子统计条件
	 * @param type
	 * @param gettype
	 * @param getnewtopic
	 * @return
	 */
	public static String getTopicCountCondition(Map<Integer, String> type, String gettype, int getnewtopic) {
		String condition = "";
		type.put(0, "");
		if (gettype.equals("digest")) {
			type.put(0, "digest");
			condition += " and digest>0 ";
		}

		if (gettype.equals("newtopic")) {
			type.put(0, "newtopic");
			condition += " and postdatetime>='"
					+ Utils.dateFormat(DateUtils.addMinutes(new Date(), -getnewtopic), Utils.FULL_DATEFORMAT) + "'";
		}
		return condition;
	}

	/**
	 * 获取置顶用户和帖子ID的评分记录HQL语句
	 * @param userid
	 * @param postidlist
	 * @return
	 */
	public static String getRateLogCountCondition(int userid, String postidlist) {
		return "users.uid=" + userid + " and postid.pid = " + Utils.null2Int(postidlist, 0);
	}

}
