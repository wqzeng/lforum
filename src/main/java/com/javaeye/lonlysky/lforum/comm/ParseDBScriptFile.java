package com.javaeye.lonlysky.lforum.comm;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 读取数据库脚本
 * 
 * @author 寂寞地铁
 * 
 */
public class ParseDBScriptFile {

	private static final Logger logger = LoggerFactory.getLogger(ParseDBScriptFile.class);

	/**
	 * 读取指定数据库脚本并返回集合
	 * @param filename 数据库脚本文件
	 * @return
	 */
	public static List<String> parse(String filename) {
		List<String> statements = new ArrayList<String>();
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(filename));
			String line;
			StringBuffer sqlBuffer = new StringBuffer("");
			while ((line = reader.readLine()) != null) {
				if (line.length() == 0) {
					continue;
				}
				char charAt = line.charAt(0);
				// 如果是注释则不添加
				if (charAt == '/' || charAt == '-') {
					continue;
				} else if (charAt == ';') {
					statements.add(sqlBuffer.toString());
					sqlBuffer.delete(0, sqlBuffer.length());
				} else {
					if (logger.isDebugEnabled()) {
						logger.debug("读取到SQL语句:" + line);
					}
					sqlBuffer.append(line);
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (Exception e) {
				}
			}
		}

		return statements;
	}
}
