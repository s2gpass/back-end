package com.kt.safe2go.platform.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;

public class DateTimeFormatter {
	
	private static final String DEFAULT_FORMAT = "yyyyMMddHHmmss";
	
	public static String format() {
		return format(new Date());
	}

	public static String format(String format) {
		return format(new Date(), format);
	}
	
	public static String format(Date date) {
		return format(date, DEFAULT_FORMAT);
	}
	
	public static String format(Date date, String format) {
		return DateFormatUtils.format(date, format);
	}
	
	public static String convert(String from, String to) throws ParseException {
		Date date = DateUtils.parseDate(from, DEFAULT_FORMAT);
		return format(date, "MM/dd HH:mm");
	}
	
	 public static String changeCode(String valueDate) throws ParseException {
    	SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddhhmmssSSS");
    	Date Date = simpleDateFormat.parse(valueDate);
    	String pushCodeNum = DateFormatUtils.format(Date, "mm");
    	
    	int minNum = Integer.parseInt(pushCodeNum);
    	if (minNum >= 0 && minNum <= 19) {
    		pushCodeNum = "00";
    	} else if (minNum >= 20 && minNum <= 39) {
    		pushCodeNum = "20";
    	} else if (minNum >= 40 && minNum <= 59) {
    		pushCodeNum = "40";
    	}
    	
    	return pushCodeNum;
	}

}
