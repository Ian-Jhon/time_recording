package com.yvelabs.timerecording;

public class EventCategoryModel {
	
	private int eventCategoryId = -1;
	private String eventCategoryName;
	private String status;
	
	public int getEventCategoryId() {
		return eventCategoryId;
	}
	public void setEventCategoryId(int eventCategoryId) {
		this.eventCategoryId = eventCategoryId;
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
	
	@Override
	public String toString() {
		StringBuilder str = new StringBuilder();
		str.append("eventCategoryId : " + eventCategoryId);
		str.append(", eventCategoryName : " + eventCategoryName);
		str.append(", status" + status);
		return str.toString();
	}
}
