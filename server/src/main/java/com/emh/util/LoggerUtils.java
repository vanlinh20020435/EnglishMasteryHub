package com.emh.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggerUtils
{
	private static Logger logger;

	private static Logger getLogger()
	{
		if(logger == null)
			logger = LoggerFactory.getLogger(LoggerUtils.class);
		return logger;
	}

	public static void log (String message)
	{
		getLogger().warn(message);
	}

	public static void log (Exception e)
	{
		getLogger().error("{0}", e);
	}
}