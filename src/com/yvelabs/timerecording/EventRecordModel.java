package com.yvelabs.timerecording;

import java.util.Date;

public class EventRecordModel {
	
	private int recordId = -1;
	private String eventCategoryName;
	private String eventName;
	private long useingTime;
	private String summary;
	private Date eventDate; // yyyy/mm/dd
	private Date createTime;
	
	private Date startEventDate;
	private Date endEventDate;
	private Date startCreateTime;
	private Date endCreateTime;
	
	
	public long getUseingTime() {
		return useingTime;
	}
	public void setUseingTime(long useingTime) {
		this.useingTime = useingTime;
	}
	public String getSummary() {
		return summary;
	}
	public void setSummary(String summary) {
		this.summary = summary;
	}
	public Date getEventDate() {
		return eventDate;
	}
	public void setEventDate(Date eventDate) {
		this.eventDate = eventDate;
	}
	public int getRecordId() {
		return recordId;
	}
	public void setRecordId(int recordId) {
		this.recordId = recordId;
	}
	public Date getCreateTime() {
		if (createTime == null) {
			createTime = new Date();
		}
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getEventName() {
		return eventName;
	}
	public void setEventName(String eventName) {
		this.eventName = eventName;
	}
	public String getEventCategoryName() {
		return eventCategoryName;
	}
	public void setEventCategoryName(String eventCategoryName) {
		this.eventCategoryName = eventCategoryName;
	}
	public Date getStartEventDate() {
		return startEventDate;
	}
	public void setStartEventDate(Date startEventDate) {
		this.startEventDate = startEventDate;
	}
	public Date getEndEventDate() {
		return endEventDate;
	}
	public void setEndEventDate(Date endEventDate) {
		this.endEventDate = endEventDate;
	}
	public Date getStartCreateTime() {
		return startCreateTime;
	}
	public void setStartCreateTime(Date startCreateTime) {
		this.startCreateTime = startCreateTime;
	}
	public Date getEndCreateTime() {
		return endCreateTime;
	}
	public void setEndCreateTime(Date endCreateTime) {
		this.endCreateTime = endCreateTime;
	}
	
}
