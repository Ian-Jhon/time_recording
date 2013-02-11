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
	
	public static long getCurrentTime () {
		return System.currentTimeMillis();
	}
	
	public static Date getDateByYMD (int year, int month, int day) {
		Calendar c = Calendar.getInstance();
		c.clear();
		c.set(year, month, day);
		return c.getTime();
	}
	
	public static Date getDateByDateTime (Date date) {
		Calendar c = Calendar.getInstance();
		c.clear();
		c.setTime(date);
		
		Calendar resultC = Calendar.getInstance();
		resultC.clear();
		resultC.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
		
		return resultC.getTime();
	}
	
	public static String formatTime (long milliSecond) {
		StringBuilder time = new StringBuilder();
		String hoursStr, minutesStr, secondsStr;
		long sec = milliSecond / 1000;
		
		long hours = sec / 3600;
		long minutes = (sec % 3600) / 60;
		long seconds = (sec % 3600) % 60;
		
		if (hours < 10) {
			hoursStr = "0" + String.valueOf(hours);
		} else {
			hoursStr = String.valueOf(hours);
		}
		
		if (minutes < 10) {
			minutesStr = "0" + String.valueOf(minutes);
		} else {
			minutesStr = String.valueOf(minutes);
		}
		
		if (seconds < 10) {
			secondsStr = "0" + String.valueOf(seconds);
		} else {
			secondsStr = String.valueOf(seconds);
		}
		
		time.append(hoursStr).append(":").append(minutesStr).append(":").append(secondsStr);
		return time.toString();
	}
	

}
