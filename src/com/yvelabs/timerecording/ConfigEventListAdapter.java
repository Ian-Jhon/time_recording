package com.yvelabs.timerecording;

import java.util.List;

import com.yvelabs.timerecording.utils.TypefaceUtils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ConfigEventListAdapter extends BaseAdapter {
	
	private List<EventModel> eventModels;
	private LayoutInflater mInflater;
	private Context context;
	
	static class ViewHolder {
		private TextView eventName;
		private TextView eventCategory;
		private TextView orderLabel;
		private TextView order;
		private TextView status;
	}
	
	public ConfigEventListAdapter (Context context, List<EventModel> eventModels) {
		this.eventModels = eventModels;
		this.context = context;
		this.mInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return eventModels == null ? 0 : eventModels.size();
	}

	@Override
	public Object getItem(int position) {
		return eventModels == null ? null : eventModels.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View rowView = convertView;
		EventModel eventModel = eventModels.get(position);
		
		if (rowView == null) {
			rowView = mInflater.inflate(R.layout.config_event_list_item, null);
			ViewHolder holder = new ViewHolder();
			holder.eventName = (TextView) rowView.findViewById(R.id.config_event_list_event_name);
			holder.eventCategory = (TextView) rowView.findViewById(R.id.config_event_list_category_name);
			holder.order = (TextView) rowView.findViewById(R.id.config_event_list_order);
			holder.orderLabel = (TextView) rowView.findViewById(R.id.config_event_list_order_label);
			holder.status = (TextView) rowView.findViewById(R.id.config_event_list_status);
			
			rowView.setTag(holder);
		}
		
		ViewHolder viewHolder = (ViewHolder) rowView.getTag();
		viewHolder.eventCategory.setText(eventModel.getEventCategoryName());
		viewHolder.eventName.setText(eventModel.getEventName());
		viewHolder.order.setText(eventModel.getOrder() == 0 ? "" :  " : " + eventModel.getOrder());
		if ("1".equals(eventModel.getStatus())) {
			viewHolder.status.setText(context.getString(R.string.enable));
		} else if ("2".equals(eventModel.getStatus())) {
			viewHolder.status.setText(context.getString(R.string.disable));
		}
		
		new TypefaceUtils().setTypeface(viewHolder.eventCategory, TypefaceUtils.RBNO2_LIGHT_A);
		new TypefaceUtils().setTypeface(viewHolder.eventName, TypefaceUtils.RBNO2_LIGHT_A);
		
		
		return rowView;
	}

}
