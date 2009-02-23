package com.javaeye.lonlysky.lforum.comm.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import com.javaeye.lonlysky.lforum.exception.ServiceException;
import com.javaeye.lonlysky.lforum.exception.WebException;

/**
 * 本类用于处理XML元素
 * 
 * @author 黄磊
 *
 */
@SuppressWarnings("unchecked")
public class XmlElementUtil {

	/**
	 * 查找指定名称元素
	 * 
	 * @param name	元素名称
	 * @param el	父元素
	 * @return	元素
	 */
	public static Element findElement(String name, Element el) {
		Element ret = null;
		if (el != null) {
			List<Element> e = el.elements(name);
			for (int i = 0; i < e.size(); i++) {
				Element n = (Element) e.get(i);
				if (n.getName().equals(name)) {
					ret = n;
					break;
				}
			}
		}
		return ret;
	}

	/**
	 * 查找执行名称的元素集合
	 * 
	 * @param name	元素名称
	 * @param el	父元素
	 * @return	元素集合
	 */
	public static List<Element> findElements(String name, Element el) {
		List<Element> list = new ArrayList<Element>();
		if (el != null) {
			List<Element> e = el.elements(name);
			for (int i = 0; i < e.size(); i++) {
				Element n = e.get(i);
				if (n.getName().equals(name))
					list.add(n);
			}
		}
		return list;
	}

	/**
	 * 读取XML文件返回document对象
	 * 
	 * @param filePath 文件路径
	 * @return document对象
	 */
	public static Document readXML(String filePath) {
		try {
			FileInputStream in = new FileInputStream(filePath);
			SAXReader reader = new SAXReader();
			return reader.read(in);
		} catch (FileNotFoundException e) {
			throw new ServiceException("不可能发生的异常,找不到文件：" + filePath);
		} catch (DocumentException e) {
			throw new ServiceException("解析文件" + filePath + "出错," + e.getMessage());
		}
	}

	/**
	 * 保存XML配置文件
	 * @param filePath 文件路径
	 * @param document DOC对象
	 */
	public static void saveXML(String filePath, Document document) {
		try {
			OutputFormat format = OutputFormat.createPrettyPrint();
			XMLWriter writer = new XMLWriter(new FileOutputStream(filePath), format);
			writer.write(document);
			writer.close();
		} catch (IOException e) {
			throw new WebException("保存配置文件失败");
		}
	}

}
