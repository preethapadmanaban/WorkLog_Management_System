package com.worklog.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.worklog.exceptions.RequiredResourceNotFoundException;

public class AppConfig {

	private static final Properties properties = new Properties();

	static {
		try (InputStream is = AppConfig.class.getClassLoader().getResourceAsStream("application.properties")) {
			
			if (is == null) {
				throw new RequiredResourceNotFoundException("application.properties Not Found in the path src/main/java");
			} else {
				properties.load(is);
			}

		} catch (IOException e) {
			throw new RequiredResourceNotFoundException("application.properties Not Found in the path src/main/java");
		}
	}

	public static String getProperty(String key) {
		return properties.getProperty(key);
	}

	public static int getPropertyInt(String key) {
		String value = properties.getProperty(key);
		if (value == null)
			throw new IllegalArgumentException("Key not found.");
		return Integer.parseInt(properties.getProperty(key, "-1"));
	}
}
