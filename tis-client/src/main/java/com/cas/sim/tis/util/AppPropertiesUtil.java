package com.cas.sim.tis.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.util.TypeUtils;

public class AppPropertiesUtil {
	private static final Logger LOGGER = LoggerFactory.getLogger(AppPropertiesUtil.class);
	private static final File file = new File("application.properties");
	private static final Properties PROPERTIES = new Properties();

	static {
		try {
			if (!file.exists()) {
				file.createNewFile();
			}
			PROPERTIES.load(new FileInputStream(file));
		} catch (IOException e) {
			e.printStackTrace();
			LOGGER.error("配置文件读取出错：{}", e.getMessage());
		}
	}

	public static String getStringValue(String key) {
		return PROPERTIES.getProperty(key);
	}

	public static String getStringValue(String key, String defaultValue) {
		return PROPERTIES.getProperty(key, defaultValue);
	}

	public static int getIntValue(String key) {
		return getIntValue(key, 0);
	}

	public static int getIntValue(String key, int defaultValue) {
		String value = PROPERTIES.getProperty(key);
		Integer intVal = TypeUtils.castToInt(value);
		if (intVal == null) {
			return defaultValue;
		}
		return intVal.intValue();
	}

	public static void set(String key, Object value) {
		if (value == null) {
			PROPERTIES.setProperty(key, "");
		} else {
			PROPERTIES.setProperty(key, String.valueOf(value));
		}
	}

	public static void store() {
		try {
			FileOutputStream fos = new FileOutputStream(file);
			PROPERTIES.store(fos, null);
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
