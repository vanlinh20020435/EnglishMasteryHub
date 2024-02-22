package com.emh.util;

import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesLoader
{
	private static Properties properties;
	private static String resource = "classpath:application.properties";

	public static Properties loadProperties() throws IOException
	{
		return loadProperties(resource);
	}

	public static Properties loadProperties(String resourceFileName) throws IOException
	{
		if (properties == null)
		{
			properties = new Properties();
			File file = ResourceUtils.getFile(resource);
			InputStream inputStream = new FileInputStream(file);
			properties.load(inputStream);
			inputStream.close();
		}
		return properties;
	}

	public static String getProperty(String name) throws IOException
	{
		return loadProperties().getProperty(name);
	}

	public static void setProperty(String name, String value) throws IOException
	{
		properties.setProperty(name, value);
	}
}