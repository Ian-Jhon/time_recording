package com.yvelabs.timerecording;

import java.util.List;

import com.yvelabs.timerecording.ConfigEventListAdapter.ViewHolder;
import com.yvelabs.timerecording.utils.DateUtils;
import com.yvelabs.timerecording.utils.LogUtils;
import com.yvelabs.timerecording.utils.TypefaceUtils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class RecordHistoryListAdapter extends BaseAdapter {
	
	private List<EventRecordModel> recordModels;
	private LayoutInflater mInflater;
	
	static class ViewHolder {
		private TextView eventName;
		private TextView eventCategoryName;
		private TextView eventDate;
		private TextView usingTime;
		private TextView summary;
	}

	public RecordHistoryListAdapter (Context context, List<EventRecordModel> recordModels) {
		this.recordModels = recordModels;
		this.mInflater = LayoutInflater.from(context);
	}
	
	@Override
	public int getCount() {
		return recordModels == null ? 0 : recordModels.size();
	}

	@Override
	public Object getItem(int position) {
		return recordModels == null ? null : recordModels.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		EventRecordModel recordModel = recordModels.get(position);
		View rowView = convertView;
		
		if (rowView == null) {
			rowView = mInflater.inflate(R.layout.record_history_list_item, null);
			ViewHolder viewHolder = new ViewHolder();
			viewHolder.eventName = (TextView) rowView.findViewById(R.id.record_history_list_event_name);
			viewHolder.eventCategoryName = (TextView) rowView.findViewById(R.id.record_history_list_event_category_name);
			viewHolder.eventDate = (TextView) rowView.findViewById(R.id.record_history_list_event_date);
			viewHolder.usingTime = (TextView) rowView.findViewById(R.id.record_history_list_using_time);
			viewHolder.summary = (TextView) rowView.findViewById(R.id.record_history_list_summary);
			
			rowView.setTag(viewHolder);
		}
		
		ViewHolder viewHolder = (ViewHolder) rowView.getTag();
		viewHolder.eventName.setText(recordModel.getEventName());
		viewHolder.eventCategoryName.setText(recordModel.getEventCategoryName());
		viewHolder.eventDate.setText(" : " + DateUtils.format(recordModel.getEventDate(), DateUtils.DEFAULT_DATE_PATTERN));
		viewHolder.usingTime.setText(" : " + DateUtils.formatAdjust(recordModel.getUseingTime(), DateUtils.DEFAULT_TIME_PATTERN));
		if (recordModel.getSummary() != null && recordModel.getSummary().length() > 0) {
			viewHolder.summary.setVisibility(View.VISIBLE);
			viewHolder.summary.setText(recordModel.getSummary());
		}
		
		TypefaceUtils.setTypeface(viewHolder.eventName, TypefaceUtils.RBNO2_LIGHT_A);
		TypefaceUtils.setTypeface(viewHolder.eventCategoryName, TypefaceUtils.RBNO2_LIGHT_A);
		return rowView;
	}

}
