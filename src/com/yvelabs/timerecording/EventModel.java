package com.yvelabs.timerecording;

import java.io.Serializable;
import java.util.Date;

import com.yvelabs.timerecording.utils.DateUtils;

public class EventModel implements Serializable {
	
	public static final String STATE_START = "START";
	public static final String STATE_PAUSE = "PAUSE";
	public static final String STATE_STOP = "STOP";
	//private static final String STATE_RESET = "RESET";
	
	private int eventId = -1;
	private String eventName;
	private String eventCategoryName;
	private int order; //position
	private String status;
	
	//recorder 页面需要的属性
	private String chro_state = STATE_STOP;
	private Date startTime; // 
	private long startElapsedTime; //
	
	public String getEventName() {
		return eventName;
	}
	public void setEventName(String eventName) {
		this.eventName = eventName;
	}
	public int getOrder() {
		return order;
	}
	public void setOrder(int order) {
		this.order = order;
	}
	public Date getStartTime() {
		return startTime;
	}
	public String getStartTime (String pattern) {
		if (pattern == null || pattern.length() <= 0)
			return DateUtils.dateFormat(getStartTime());
		else 
			return DateUtils.dateFormat(getStartTime(), pattern);
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public long getStartElapsedTime() {
		return startElapsedTime;
	}
	public void setStartElapsedTime(long startElapsedTime) {
		this.startElapsedTime = startElapsedTime;
	}
	public int getEventId() {
		return eventId;
	}
	public void setEventId(int eventId) {
		this.eventId = eventId;
	}
	public String getEventCategoryName() {
		return eventCategoryName;
	}
	public void setEventCategoryName(String eventCategoryName) {
		this.eventCategoryName = eventCategoryName;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getChro_state() {
		return chro_state;
	}
	public void setChro_state(String chro_state) {
		this.chro_state = chro_state;
	}
	
	@Override
	public String toString() {
		return "eventName :" + eventName + ", eventCategoryName :" + eventCategoryName + ", chro_state :" + chro_state
				+ ", startElapsedTime :" + startElapsedTime + ", startTime :" + startTime;
	}

}
