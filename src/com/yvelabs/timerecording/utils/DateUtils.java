package com.yvelabs.timerecording.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class DateUtils {
	
	public static final String DEFAULT_TIME_PATTERN = "HH:mm:ss";
	public static final String DEFAULT_DATE_PATTERN = "yyyy-MM-dd";
	public static final String DEFAULT_DATETIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
	
	private static long timeZoneAdjust;
	
	public DateUtils() {
		setTimeZoneAdjust();
	}
	
	public static String dateFormat (Date time, String pattern) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
		return dateFormat.format(time);
	}
	
	public static String dateFormat (Date time) {
		return dateFormat(time, DEFAULT_TIME_PATTERN);
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
	
	public String formatTime(long time, String pattern) {
		return new SimpleDateFormat(pattern).format(timeZoneAdjust + time);
	}
	
	public String formatTime(long time) {
		return formatTime(time, DEFAULT_TIME_PATTERN);
	}
	

}
