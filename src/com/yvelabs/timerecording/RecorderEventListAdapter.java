package com.yvelabs.timerecording;

import java.util.List;

import com.yvelabs.timerecording.utils.DateUtils;
import com.yvelabs.timerecording.utils.LogUtils;
import com.yvelabs.timerecording.utils.TypefaceUtils;

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
		private TextView listEventName;
	    private TextView listEventCagegory;
	    private TextView listEventStartTimeLabel;
	    private TextView listEventStartTime;
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
		
		if (rowView == null) {
			rowView = mInflater.inflate(R.layout.recorder_event_list_item, null);
			ViewHolder viewHolder = new ViewHolder();
			viewHolder.listEventName = (TextView) rowView.findViewById(R.id.list_event_name);
			viewHolder.listEventCagegory = (TextView) rowView.findViewById(R.id.list_event_category);
			viewHolder.listEventStartTimeLabel = (TextView) rowView.findViewById(R.id.start_time_lab);
			viewHolder.listEventStartTime = (TextView) rowView.findViewById(R.id.list_event_start_time);
			viewHolder.listEventStateImg = (ImageView) rowView.findViewById(R.id.list_event_state_img);
			
			rowView.setTag(viewHolder);
		}
		
		ViewHolder holder = (ViewHolder) rowView.getTag();
		holder.listEventName.setText(eventModel.getEventName());
		TypefaceUtils.setTypeface(holder.listEventName, TypefaceUtils.RBNO2_LIGHT_A);
		holder.listEventCagegory.setText(eventModel.getEventCategoryName());
		TypefaceUtils.setTypeface(holder.listEventCagegory, TypefaceUtils.RBNO2_LIGHT_A);
		
		if (eventModel.STATE_START.equals(eventModel.getChro_state())) {
			holder.listEventStartTimeLabel.setVisibility(View.VISIBLE);
			holder.listEventStartTime.setVisibility(View.VISIBLE);
			holder.listEventStateImg.setVisibility(View.VISIBLE);
			
			holder.listEventStartTime.setText(eventModel.getStartTime(eventModel.getStartTime(DateUtils.DEFAULT_DATETIME_PATTERN)));
			holder.listEventStateImg.setImageResource(R.drawable.start_state);
		} else if (eventModel.STATE_PAUSE.equals(eventModel.getChro_state())) {
			holder.listEventStartTimeLabel.setVisibility(View.VISIBLE);
			holder.listEventStartTime.setVisibility(View.VISIBLE);
			holder.listEventStateImg.setVisibility(View.VISIBLE);
			
			holder.listEventStartTime.setText(eventModel.getStartTime(eventModel.getStartTime(DateUtils.DEFAULT_DATETIME_PATTERN)));
			holder.listEventStateImg.setImageResource(R.drawable.pause_state);
		} else {
			holder.listEventStartTimeLabel.setVisibility(View.GONE);
			holder.listEventStartTime.setVisibility(View.GONE);
			holder.listEventStateImg.setVisibility(View.GONE);
		}
		
		return rowView;
	}

}
