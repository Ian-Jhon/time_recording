package com.yvelabs.timerecording.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class DateUtils {
	
	public static final String DEFAULT_TIME_PATTERN = "HH:mm:ss";
	public static final String DEFAULT_DATE_PATTERN = "yyyy-MM-dd";
	public static final String DEFAULT_DATETIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
	
	private static long timeZoneAdjust;
	
	static {
		setTimeZoneAdjust();
	}
	
	public static void setTimeZoneAdjust() {
		Calendar calendar = Calendar.getInstance();
		long unixTime = calendar.getTimeInMillis();
		long unixTimeGMT = unixTime - TimeZone.getDefault().getRawOffset();
		timeZoneAdjust = unixTimeGMT - unixTime;
	}
	
	public static long getTimeZoneAdjust() {
		return timeZoneAdjust;
	}
	
	public static String format (Date time, String pattern) {
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		return sdf.format(time);
	}
	
	public static String format (Date time) {
		return format(time, DEFAULT_DATETIME_PATTERN);
	}
	
	public static String formatAdjust(long time, String pattern) {
		return new SimpleDateFormat(pattern).format(timeZoneAdjust + time);
	}
	
	public static String formatAdjust(long time) {
		return formatAdjust(time, DEFAULT_TIME_PATTERN);
	}
	
	public static Date getDateWithoutTime(Date oriDate) throws ParseException {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String dateStr = sdf.format(date);
		
		return sdf.parse(dateStr);
	}
	

}
