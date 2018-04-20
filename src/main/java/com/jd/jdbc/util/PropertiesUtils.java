package com.jd.jdbc.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

/**
 * contentType: 读取配置
 * <P>
 * 优先识别编码为utf-8,并且读取配置文件定制的编码,如果不相同以配置文件定制的编码为准
 * <p>
 * remark:只读取config.properties一个文件
 * 
 * @author wanghong12
 */
public class PropertiesUtils {

	private static Properties pro;

	private static String conding = "utf-8";

	private final static String CONFIG_FILE_PATH = "properties/system.properties";

	private PropertiesUtils() {
		throw new RuntimeException("不能实例化");
	}

	/**
	 * 使用静态代码块的好处是只加载一次 但是每次修改配置文件都要重新部署
	 */
	static {
		pro = new Properties();
		ClassLoader loader = PropertiesUtils.class.getClassLoader();
		InputStream in = loader.getResourceAsStream(CONFIG_FILE_PATH);
		InputStreamReader isr = null;
		try {
			isr = new InputStreamReader(in, conding);
			pro.load(isr);
			String _conding = pro.getProperty("conding");
			if (_conding != null && !conding.equals(_conding)) {
				in = loader.getResourceAsStream(CONFIG_FILE_PATH);
				isr = new InputStreamReader(in, _conding);
				pro.load(isr);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				in.close();
				if (isr != null) {
					isr.close();
				}
			} catch (IOException e) {
			}
		}
	}

	/**
	 * @param key
	 * @return
	 */
	public static String getValue(String key) {
		return pro.getProperty(key);
	}

	public static Integer getIntValue(String key) {
		String v = pro.getProperty(key);
		try {
			return Integer.parseInt(v);
		} catch (Exception e) {
			return null;
		}
	}

}
