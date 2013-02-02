package com.yvelabs.timerecording;

import java.util.List;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.yvelabs.timerecording.utils.MyKeyValuePair;
import com.yvelabs.timerecording.utils.SpinnerUtils;
import com.yvelabs.timerecording.utils.TypefaceUtils;

public class RecordHistorySearchDialog extends DialogFragment {
	
	private TextView titleTextTv;
	private Spinner eventNameSp;
	private Spinner eventCategoryNameSp;
	private ImageButton searchBut;
	
	private ArrayAdapter<MyKeyValuePair> eventSppinerAdapter;
	private List<MyKeyValuePair> eventSpinnerList;
	
	static RecordHistorySearchDialog newInstance() {
		RecordHistorySearchDialog f = new RecordHistorySearchDialog();
		Bundle args = new Bundle();
		f.setArguments(args);
		return f;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setStyle(STYLE_NO_TITLE, 0);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.record_history_search_dialog, null);
		titleTextTv = (TextView) view.findViewById(R.id.record_history_search_dialog_title_text);
		eventNameSp = (Spinner) view.findViewById(R.id.record_history_search_dialog_event_name);
		eventCategoryNameSp = (Spinner) view.findViewById(R.id.record_history_search_dialog_event_category_name);
		searchBut = (ImageButton) view.findViewById(R.id.record_history_search_dialog_search_but);
		
		new TypefaceUtils().setTypeface(titleTextTv, TypefaceUtils.MOBY_MONOSPACE);
		
		List<MyKeyValuePair> categoryList = new SpinnerUtils().categorySpinner(getActivity());
		ArrayAdapter<MyKeyValuePair> categorySppinerAdapter = new ArrayAdapter<MyKeyValuePair>(getActivity(), android.R.layout.simple_spinner_item, categoryList); 
		eventCategoryNameSp.setAdapter(categorySppinerAdapter);
		MyKeyValuePair categoryPair = (MyKeyValuePair) eventCategoryNameSp.getSelectedItem();
		
		eventSpinnerList = new SpinnerUtils().eventSpinner(getActivity(), categoryPair.getKey().toString());
		eventSppinerAdapter = new ArrayAdapter<MyKeyValuePair>(getActivity(), android.R.layout.simple_spinner_item, eventSpinnerList);
		eventNameSp.setAdapter(eventSppinerAdapter);
		
		eventCategoryNameSp.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				MyKeyValuePair categoryPair = (MyKeyValuePair)eventCategoryNameSp.getSelectedItem();
				
				eventSpinnerList.removeAll(eventSpinnerList);
				eventSpinnerList.addAll(new SpinnerUtils().eventSpinner(getActivity(), categoryPair.getKey().toString())); 
				eventSppinerAdapter.notifyDataSetChanged();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				
			}
		});
		
		searchBut.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				EventRecordModel parameter = new EventRecordModel();
				MyKeyValuePair categoryPair = (MyKeyValuePair) eventCategoryNameSp.getSelectedItem();
				MyKeyValuePair eventPair = (MyKeyValuePair) eventNameSp.getSelectedItem();
				parameter.setEventName(eventPair.getKey().toString());
				parameter.setEventCategoryName(categoryPair.getKey().toString());
				
				((RecordActivity)getActivity()).refreshHistoryList(parameter);
				getDialog().dismiss();
			}
		});
		
		return view;
	}

}
