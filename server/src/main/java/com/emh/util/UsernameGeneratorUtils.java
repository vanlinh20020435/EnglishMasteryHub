package com.emh.util;

import java.text.Normalizer;
import java.util.Random;
import java.util.regex.Pattern;

public class UsernameGeneratorUtils
{
	public static String generateUsername(String fullname)
	{
		fullname = removeAccent(fullname);
		String[] nameParts = fullname.toLowerCase().split("\\s+");
		StringBuilder usernameBuilder = new StringBuilder();
		String lastPart = nameParts[nameParts.length - 1];
		usernameBuilder.append(lastPart);
		for (int i = 0; i < nameParts.length - 1; i++)
		{
			String part = nameParts[i];
			if (!part.isEmpty())
			{
				usernameBuilder.append(part.charAt(0));
			}
		}
		return usernameBuilder + "_" + String.format("%04d", new Random().nextInt(10000));
	}

	public static String removeAccent(String s)
	{
		String temp = Normalizer.normalize(s, Normalizer.Form.NFD);
		Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
		temp = pattern.matcher(temp).replaceAll("");
		return temp.replaceAll("đ", "d");
	}
}
