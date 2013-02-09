package com.yvelabs.timerecording.utils;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.yvelabs.timerecording.EventCategoryModel;
import com.yvelabs.timerecording.EventModel;
import com.yvelabs.timerecording.R;
import com.yvelabs.timerecording.dao.EventCategoryDAO;
import com.yvelabs.timerecording.dao.EventDAO;

public class SpinnerUtils {
	
	public List<MyKeyValuePair> eventSpinner (Context context, String categoryName) {
		EventModel parameter = new EventModel();
		parameter.setStatus("1");
		if (categoryName != null && categoryName.length() > 0) {
			parameter.setEventCategoryName(categoryName);
		}
		List<EventModel> eventList = new EventDAO(context).query(parameter);
		
		List<MyKeyValuePair> list = new ArrayList<MyKeyValuePair>();
		MyKeyValuePair pair = null;
		
		for (EventModel eventModel : eventList) {
			pair = new MyKeyValuePair();
			pair.setKey(eventModel.getEventName());
			pair.setValue(eventModel.getEventName());
			list.add(pair);
		}
		return list;
	}
	
	public List<MyKeyValuePair> categorySpinner (Context context) {
		EventCategoryModel para = new EventCategoryModel();
		para.setStatus("1");
		List<EventCategoryModel> categoryList = new EventCategoryDAO(context).query(para);
		
		List<MyKeyValuePair> list = new ArrayList<MyKeyValuePair>();
		MyKeyValuePair pair = null;
		
		for (EventCategoryModel model : categoryList) {
			pair = new MyKeyValuePair();
			pair.setKey(model.getEventCategoryName());
			pair.setValue(model.getEventCategoryName());
			list.add(pair);
		}
		
		return list;
	}
	
	
	
	public int getSpinnerPosition (Spinner spinner, MyKeyValuePair pair) {
		for (int i = 0 ; i <spinner.getCount() ; i ++ ) {
			MyKeyValuePair tempPair = (MyKeyValuePair) spinner.getItemAtPosition(i);
			if (tempPair.getValue().toString().equals(pair.getValue().toString())) {
				return i;
			}
		}
		return -1;
	}

	public List<MyKeyValuePair> orderSpinner () {
		List<MyKeyValuePair> list = new ArrayList<MyKeyValuePair>();
		MyKeyValuePair pair = null;
		
		pair = new MyKeyValuePair();
		pair.setKey(1);
		pair.setValue(String.valueOf(1));
		list.add(pair);
		
		pair = new MyKeyValuePair();
		pair.setKey(2);
		pair.setValue(String.valueOf(2));
		list.add(pair);
		
		pair = new MyKeyValuePair();
		pair.setKey(3);
		pair.setValue(String.valueOf(3));
		list.add(pair);
		
		pair = new MyKeyValuePair();
		pair.setKey(4);
		pair.setValue(String.valueOf(4));
		list.add(pair);
		
		pair = new MyKeyValuePair();
		pair.setKey(5);
		pair.setValue(String.valueOf(5));
		list.add(pair);
		
		return list;
	}
	
	public List<MyKeyValuePair> statusSpinner (Context context) {
		List<MyKeyValuePair> stateList = new ArrayList<MyKeyValuePair>();
		MyKeyValuePair pair = new MyKeyValuePair();
		pair.setKey(1);
		pair.setValue(context.getString(R.string.enable));
		stateList.add(pair);
		
		pair = new MyKeyValuePair();
		pair.setKey(2);
		pair.setValue(context.getString(R.string.disable));
		stateList.add(pair);
		
		return stateList;
	}
	
	public MyKeyValuePair generateStatusSpinner (Context context, String status) {
		MyKeyValuePair pair = new MyKeyValuePair();
		pair.setKey(status);
		
		if ("1".equals(status)) {
			pair.setValue(context.getString(R.string.enable));
		} else if ("2".equals(status)) {
			pair.setValue(context.getString(R.string.disable));
		}
		
		return pair;
		
	}
	
	public class MySpinnerAdapter2 extends ArrayAdapter<MyKeyValuePair> {
		private Context context;
		private List<MyKeyValuePair> pairList;
		private LayoutInflater inflater;
		
		public MySpinnerAdapter2 (Context context, List<MyKeyValuePair> pairList) {
			super(context, android.R.layout.simple_spinner_item, pairList);
			
			this.context = context;
			this.pairList = pairList;
			this.inflater = LayoutInflater.from(context);
		}
		
		@Override
		public View getDropDownView(int position, View convertView,
				ViewGroup parent) {
			if (convertView == null) {  
	            convertView = inflater.inflate(android.R.layout.simple_spinner_item, parent, false);  
	        }
			
			TextView tv = (TextView) convertView.findViewById(android.R.id.text1);
            tv.setText(pairList.get(position).getValue());
            tv.setTextSize(15);
            tv.setPadding(5, 7, 5, 7);
            tv.setBackground(context.getResources().getDrawable(R.drawable.my_recorder_main_bg));
            
			return convertView;
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {  
	            convertView = inflater.inflate(android.R.layout.simple_spinner_item, parent, false);  
	        }
			
			TextView tv = (TextView) convertView.findViewById(android.R.id.text1);
			tv.setText(pairList.get(position).getValue());
			tv.setTextSize(17);
	        
			return convertView;
		}
		
	}
	
	public class MySpinnerAdapter extends ArrayAdapter<MyKeyValuePair> {
		
		private Context context;
		private List<MyKeyValuePair> pairList;
		private LayoutInflater inflater;
		
		public MySpinnerAdapter (Context context, List<MyKeyValuePair> pairList) {
			super(context, android.R.layout.simple_spinner_item, pairList);
			
			this.context = context;
			this.pairList = pairList;
			this.inflater = LayoutInflater.from(context);
		}
		
		@Override
		public View getDropDownView(int position, View convertView,
				ViewGroup parent) {
			if (convertView == null) {  
	            convertView = inflater.inflate(android.R.layout.simple_spinner_item, parent, false);  
	        }
			
			TextView tv = (TextView) convertView.findViewById(android.R.id.text1);
            tv.setText(pairList.get(position).getValue());
            tv.setTextSize(20);
            tv.setPadding(10, 13, 10, 13);
            tv.setBackground(context.getResources().getDrawable(R.drawable.my_recorder_main_bg));
            TypefaceUtils.setTypeface(tv, TypefaceUtils.RBNO2_LIGHT_A);
            
			return convertView;
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {  
	            convertView = inflater.inflate(android.R.layout.simple_spinner_item, parent, false);  
	        }
			
			TextView tv = (TextView) convertView.findViewById(android.R.id.text1);
			tv.setText(pairList.get(position).getValue());
			tv.setTextSize(25);
			TypefaceUtils.setTypeface(tv, TypefaceUtils.RBNO2_LIGHT_A);
	        
			return convertView;
		}
		
	}
}
