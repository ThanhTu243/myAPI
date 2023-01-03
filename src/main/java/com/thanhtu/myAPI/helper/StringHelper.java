package com.thanhtu.myAPI.helper;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringHelper {
	public static List<String> matchPropertiesJavaClass(String contents, List<String> listProperties) {
		Matcher m = Pattern.compile("\\s*(\\w+)\\;").matcher(contents);
		while (m.find()) {
			listProperties.add(m.group(1));
		}
		return listProperties;
	}
}
