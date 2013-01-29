package com.yvelabs.timerecording;

import java.util.List;

import com.yvelabs.timerecording.RecorderEventListAdapter.ViewHolder;
import com.yvelabs.timerecording.utils.TypefaceUtils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ConfigCategoryListAdapter extends BaseAdapter {
	private List<EventCategoryModel> eventCategoryList;
	private LayoutInflater mInflater;
	private Context context;
	
	static class ViewHolder {
		private TextView categoryName;
		private TextView status;
	}
	
	public ConfigCategoryListAdapter(Context context, List<EventCategoryModel> eventCategoryList) {
		this.mInflater = LayoutInflater.from(context);
		this.eventCategoryList = eventCategoryList;
		this.context = context;
	}

	@Override
	public int getCount() {
		return eventCategoryList == null ? 0 : eventCategoryList.size();
	}

	@Override
	public Object getItem(int position) {
		return eventCategoryList == null ? null : eventCategoryList.get(position) ;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View rowView = convertView;
		
		EventCategoryModel categoryModel = eventCategoryList.get(position);
		
		if (rowView == null) {
			rowView = mInflater.inflate(R.layout.config_category_list_item, null);
			ViewHolder viewHolder = new ViewHolder();
			viewHolder.categoryName = (TextView) rowView.findViewById(R.id.config_category_list_category_name);
			viewHolder.status = (TextView) rowView.findViewById(R.id.config_category_list_status);
			
			rowView.setTag(viewHolder);
		}
		
		ViewHolder holder = (ViewHolder) rowView.getTag();
		holder.categoryName.setText(categoryModel.getEventCategoryName());
		new TypefaceUtils().setTypeface(holder.categoryName, TypefaceUtils.RBNO2_LIGHT_A);
		
		if ("1".equals(categoryModel.getStatus())) {
			holder.status.setText(context.getString(R.string.enable));
		} else if ("2".equals(categoryModel.getStatus())) {
			holder.status.setText(context.getString(R.string.disable));
		}
		
		return rowView;
	}

}
