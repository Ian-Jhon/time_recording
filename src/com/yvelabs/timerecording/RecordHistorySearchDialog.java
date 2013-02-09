package com.yvelabs.timerecording;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
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
	private TextView startEventDateTv;
	private TextView endeventDateTv;
	private ImageButton searchBut;
	
	private ArrayAdapter<MyKeyValuePair> eventSppinerAdapter;
	private List<MyKeyValuePair> eventSpinnerList;
	private EventRecordModel parameter = new EventRecordModel();
	private Date startEventDate;
	private Date endEventDate;
	
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
		startEventDateTv = (TextView) view.findViewById(R.id.record_history_search_dialog_event_date_from);
		endeventDateTv = (TextView) view.findViewById(R.id.record_history_search_dialog_event_date_to);
		
		startEventDateTv.setKeyListener(null);
		startEventDateTv.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Calendar c = Calendar.getInstance();
				
				Dialog dialog = new DatePickerDialog(getActivity(),
						new DatePickerDialog.OnDateSetListener() {
							public void onDateSet(DatePicker dp, int year, int month, int dayOfMonth) {
								startEventDateTv.setText(year + "-" + (month + 1) + "-" + dayOfMonth);
								
								Calendar resultCalender = Calendar.getInstance();
								resultCalender.set(year, month + 1, dayOfMonth);
								startEventDate = resultCalender.getTime();
							}
						}, 
						c.get(Calendar.YEAR), // 传入年份
						c.get(Calendar.MONTH), // 传入月份
						c.get(Calendar.DAY_OF_MONTH) // 传入天数
				);
				
				dialog.show();
			}
		});
		
		endeventDateTv.setKeyListener(null);
		endeventDateTv.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Calendar c = Calendar.getInstance();
				Dialog dialog = new DatePickerDialog(getActivity(),
						new DatePickerDialog.OnDateSetListener() {
							public void onDateSet(DatePicker dp, int year, int month, int dayOfMonth) {
								endeventDateTv.setText(year + "-" + (month + 1) + "-" + dayOfMonth);
								Calendar resultCalender = Calendar.getInstance();
								resultCalender.set(year, month + 1, dayOfMonth);
								endEventDate = resultCalender.getTime();
							}
						}, 
						c.get(Calendar.YEAR), // 传入年份
						c.get(Calendar.MONTH), // 传入月份
						c.get(Calendar.DAY_OF_MONTH) // 传入天数
						
				);
				
				dialog.show();
			}
		});
		
		new TypefaceUtils().setTypeface(titleTextTv, TypefaceUtils.MOBY_MONOSPACE);
		
		List<MyKeyValuePair> categoryList = new SpinnerUtils().categorySpinner(getActivity());
		ArrayAdapter<MyKeyValuePair> categorySppinerAdapter = new ArrayAdapter<MyKeyValuePair>(getActivity(), android.R.layout.simple_spinner_item, categoryList); 
		eventCategoryNameSp.setAdapter(categorySppinerAdapter);
		MyKeyValuePair categoryPair = (MyKeyValuePair) eventCategoryNameSp.getSelectedItem();
		
		eventSpinnerList = new SpinnerUtils().eventSpinner(getActivity(), categoryPair == null ? null : categoryPair.getKey().toString());
		eventSppinerAdapter = new ArrayAdapter<MyKeyValuePair>(getActivity(), android.R.layout.simple_spinner_item, eventSpinnerList);
		eventNameSp.setAdapter(eventSppinerAdapter);
		
		eventCategoryNameSp.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				MyKeyValuePair categoryPair = (MyKeyValuePair)eventCategoryNameSp.getSelectedItem();
				
				eventSpinnerList.removeAll(eventSpinnerList);
				eventSpinnerList.addAll(new SpinnerUtils().eventSpinner(getActivity(), categoryPair == null ? null : categoryPair.getKey().toString())); 
				eventSppinerAdapter.notifyDataSetChanged();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				
			}
		});
		
		searchBut.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				
				MyKeyValuePair categoryPair = (MyKeyValuePair) eventCategoryNameSp.getSelectedItem();
				MyKeyValuePair eventPair = (MyKeyValuePair) eventNameSp.getSelectedItem();
				parameter.setEventName(eventPair.getKey().toString());
				parameter.setEventCategoryName(categoryPair.getKey().toString());
				
				if (endEventDate != null) {
					parameter.setStartEventDate(startEventDate);
				}
				if (endEventDate != null) {
					parameter.setEndEventDate(endEventDate);
				}
				
				((RecordActivity)getActivity()).refreshHistoryList(parameter);
				getDialog().dismiss();
			}
		});
		
		return view;
	}

}
