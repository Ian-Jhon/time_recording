package com.yvelabs.timerecording;

import android.os.Parcel;
import android.os.Parcelable;

public class EventCategoryModel implements Parcelable {
	
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
	
	@Override
	public int describeContents() {
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(eventCategoryId);
		dest.writeString(eventCategoryName);
		dest.writeString(status);
	}
	
	public static final Parcelable.Creator<EventCategoryModel> CREATOR = new Parcelable.Creator<EventCategoryModel>() {

		@Override
		public EventCategoryModel createFromParcel(Parcel source) {
			EventCategoryModel model = new EventCategoryModel();
			model.setEventCategoryId(source.readInt());
			model.setEventCategoryName(source.readString());
			model.setStatus(source.readString());
			return model;
		}

		@Override
		public EventCategoryModel[] newArray(int size) {
			return new EventCategoryModel[size];
		}
		
	};
	
}
