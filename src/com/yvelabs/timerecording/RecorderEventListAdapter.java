package com.yvelabs.timerecording;

import java.util.List;

import com.yvelabs.timerecording.utils.DateUtils;
import com.yvelabs.timerecording.utils.LogUtils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class RecorderEventListAdapter extends BaseAdapter {
	
	private List<EventModel> eventList;
	private LayoutInflater mInflater;
	
	static class ViewHolder {
	    public TextView listEventName;
	    public TextView listEventCagegory;
	    private TextView listEventstartTime;
	    private ImageView listEventStateImg;
	  }
	
	public RecorderEventListAdapter (Context context, List<EventModel> eventList) {
		this.mInflater = LayoutInflater.from(context);
		this.eventList = eventList;
	}

	@Override
	public int getCount() {
		return eventList == null ? 0 : eventList.size();
	}

	@Override
	public Object getItem(int position) {
		return eventList == null ? null : eventList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View rowView = convertView;
		
		if (eventList == null || eventList.size() <= 0) return null;
		EventModel eventModel = eventList.get(position);
		LogUtils.d(this.getClass(), eventModel.toString());
		
		if (rowView == null) {
			rowView = mInflater.inflate(R.layout.recorder_event_list_item, null);
			ViewHolder viewHolder = new ViewHolder();
			viewHolder.listEventName = (TextView) rowView.findViewById(R.id.list_event_name);
			viewHolder.listEventCagegory = (TextView) rowView.findViewById(R.id.list_event_category);
			viewHolder.listEventstartTime = (TextView) rowView.findViewById(R.id.list_event_start_time);
			viewHolder.listEventStateImg = (ImageView) rowView.findViewById(R.id.list_event_state_img);
			rowView.setTag(viewHolder);
		}
		
		ViewHolder holder = (ViewHolder) rowView.getTag();
		holder.listEventName.setText(eventModel.getEventName());
		holder.listEventCagegory.setText(eventModel.getEventCategoryName());
		
		if (eventModel.STATE_START.equals(eventModel.getChro_state())) {
			holder.listEventstartTime.setVisibility(View.VISIBLE);
			holder.listEventStateImg.setVisibility(View.VISIBLE);
			
			holder.listEventstartTime.setText(eventModel.getStartTime(eventModel.getStartTime(DateUtils.DEFAULT_TIME_PATTERN)));
			holder.listEventStateImg.setImageResource(R.drawable.start_state);
		} else if (eventModel.STATE_PAUSE.equals(eventModel.getChro_state())) {
			holder.listEventstartTime.setVisibility(View.VISIBLE);
			holder.listEventStateImg.setVisibility(View.VISIBLE);
			
			holder.listEventstartTime.setText(eventModel.getStartTime(eventModel.getStartTime(DateUtils.DEFAULT_TIME_PATTERN)));
			holder.listEventStateImg.setImageResource(R.drawable.pause_state);
		} else {
			holder.listEventstartTime.setVisibility(View.GONE);
			holder.listEventStateImg.setVisibility(View.GONE);
		}
		
		return rowView;
	}

}
