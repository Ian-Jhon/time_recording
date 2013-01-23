package com.yvelabs.timerecording;

import java.util.Date;

import android.os.Parcel;
import android.os.Parcelable;

import com.yvelabs.timerecording.utils.DateUtils;

public class EventModel implements Parcelable {
	
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
	
	
	@Override
	public int describeContents() {
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(eventId);
		dest.writeString(eventName);
		dest.writeString(eventCategoryName);
		dest.writeInt(order);
		dest.writeString(status);
		dest.writeString(chro_state);
		dest.writeLong(startTime == null ? 0 : startTime.getTime());
		dest.writeLong(startElapsedTime);
	}
	
	public static final Parcelable.Creator<EventModel> CREATOR = new Parcelable.Creator<EventModel>() {

		@Override
		public EventModel createFromParcel(Parcel source) {
			EventModel eventModel = new EventModel();
			eventModel.setEventId(source.readInt());
			eventModel.setEventName(source.readString());
			eventModel.setEventCategoryName(source.readString());
			eventModel.setOrder(source.readInt());
			eventModel.setStatus(source.readString());
			eventModel.setChro_state(source.readString());
			eventModel.setStartTime(new Date(source.readLong()));
			eventModel.setStartElapsedTime(source.readLong());
			
			return eventModel;
		}

		@Override
		public EventModel[] newArray(int size) {
			return new EventModel[size];
		} 
	};
}
