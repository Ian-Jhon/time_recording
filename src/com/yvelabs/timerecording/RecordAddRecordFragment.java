package com.yvelabs.timerecording;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.TimePicker.OnTimeChangedListener;

import com.yvelabs.timerecording.dao.EventRecordsDAO;
import com.yvelabs.timerecording.utils.DateUtils;
import com.yvelabs.timerecording.utils.MyKeyValuePair;
import com.yvelabs.timerecording.utils.SpinnerUtils;
import com.yvelabs.timerecording.utils.TypefaceUtils;

public class RecordAddRecordFragment extends Fragment {

	private TextView categoryLabelTv;
	private Spinner categorySp;
	private TextView eventLabelTv;
	private Spinner eventSp;
	private TextView eventDateLabelTv;
	private DatePicker eventDateDp;
	private TextView usingTimeLabelTv;
	private TimePicker usingTimeTp;
	private EditText summaryEv;
	private ImageButton saveIb;
	
	private ArrayAdapter<MyKeyValuePair> eventSppinerAdapter;
	private List<MyKeyValuePair> eventSpinnerList;
	
	private EventRecordModel saveModel;
	private long usingTime;
	private Date eventDate;

	public static RecordAddRecordFragment newInstance(Map<String, Object> map) {
		RecordAddRecordFragment recordAddRecordFragment = new RecordAddRecordFragment();
		Bundle args = new Bundle();
		args.putInt(
				"position",
				map.get("POSICTION") == null ? -1 : Integer.parseInt(map.get(
						"POSICTION").toString()));
		recordAddRecordFragment.setArguments(args);

		return recordAddRecordFragment;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Calendar c = Calendar.getInstance();
		eventDate = new Date(c.get(Calendar.YEAR) - 1900, c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.record_add_record_fragment, null);
		categoryLabelTv = (TextView) view.findViewById(R.id.record_add_record_event_category_label);
		categorySp = (Spinner) view.findViewById(R.id.record_add_record_event_category);
		eventLabelTv = (TextView) view.findViewById(R.id.record_add_record_event_label);
		eventSp = (Spinner) view.findViewById(R.id.record_add_record_event);
		eventDateLabelTv = (TextView) view.findViewById(R.id.record_add_record_event_date_label);
		eventDateDp = (DatePicker) view.findViewById(R.id.record_add_record_event_date);
		usingTimeLabelTv = (TextView) view.findViewById(R.id.record_add_record_using_time_label);
		usingTimeTp = (TimePicker) view.findViewById(R.id.record_add_record_using_time);
		summaryEv = (EditText) view.findViewById(R.id.record_add_record_summary);
		saveIb = (ImageButton) view.findViewById(R.id.record_add_record_save);
		
		usingTimeTp.setIs24HourView(true);
		
		// set typeface
		TypefaceUtils.setTypeface(categoryLabelTv, TypefaceUtils.RBNO2_LIGHT_A);
		TypefaceUtils.setTypeface(eventLabelTv, TypefaceUtils.RBNO2_LIGHT_A);
		TypefaceUtils.setTypeface(eventDateLabelTv, TypefaceUtils.RBNO2_LIGHT_A);
		TypefaceUtils.setTypeface(usingTimeLabelTv, TypefaceUtils.RBNO2_LIGHT_A);
		
		// init event and category spinner
		List<MyKeyValuePair> categoryList = new SpinnerUtils().categorySpinner(getActivity());
		ArrayAdapter<MyKeyValuePair> categorySppinerAdapter = new SpinnerUtils().new MySpinnerAdapter(getActivity(), categoryList); 
		categorySp.setAdapter(categorySppinerAdapter);
		MyKeyValuePair categoryPair = (MyKeyValuePair) categorySp.getSelectedItem();
		
		eventSpinnerList = new SpinnerUtils().eventSpinner(getActivity(), categoryPair == null ? null : categoryPair.getKey().toString());
		eventSppinerAdapter =  new SpinnerUtils().new MySpinnerAdapter(getActivity(), eventSpinnerList);
		eventSp.setAdapter(eventSppinerAdapter);
		
		categorySp.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				MyKeyValuePair categoryPair = (MyKeyValuePair)categorySp.getSelectedItem();
				
				eventSpinnerList.removeAll(eventSpinnerList);
				eventSpinnerList.addAll(new SpinnerUtils().eventSpinner(getActivity(), categoryPair == null ? null : categoryPair.getKey().toString())); 
				eventSppinerAdapter.notifyDataSetChanged();
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {}
		});
		
		saveIb.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				MyKeyValuePair categoryPair = (MyKeyValuePair) categorySp.getSelectedItem();
				MyKeyValuePair eventPair = (MyKeyValuePair) eventSp.getSelectedItem();
				
				saveModel = new EventRecordModel();
				saveModel.setEventName(eventPair.getKey().toString());
				saveModel.setEventCategoryName(categoryPair.getKey().toString());
				saveModel.setEventDate(eventDate);
				saveModel.setUseingTime(usingTime);
				saveModel.setSummary(summaryEv.getText().toString());
				
				DialogFragment newFragment = MyAlertDialogFragment.newInstance(
			            R.string.save, R.drawable.ic_save_normal, getEventMsg(saveModel), new SaveButListener(saveModel), null);
			    newFragment.show(getFragmentManager(), "record_add_record_save_dialog");
			}
		});
		
		usingTimeTp.setCurrentHour(0);
		usingTimeTp.setCurrentMinute(0);
		
		usingTimeTp.setOnTimeChangedListener(new OnTimeChangedListener() {
			@Override
			public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
				usingTime = hourOfDay * 60 * 60 * 1000 + minute * 60 * 1000;
			}
		});
		
		Calendar c = Calendar.getInstance();
		eventDateDp.init(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH), new OnDateChangedListener() {
			@Override
			public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
				eventDate = DateUtils.getDateByYMD(year, monthOfYear, dayOfMonth);
			}
		});

		return view;
	}

	@Override
	public void onResume() {
		super.onResume();
		// refresh event and category
		
	}
	
	private String getEventMsg (EventRecordModel eventRecordModel) {
		StringBuilder result = new StringBuilder();
		result.append(getString(R.string.recorder_page_stop)).append("\r\n");
		result.append(getActivity().getResources().getString(R.string.event_name) + " : ").append(eventRecordModel.getEventName()).append("\r\n");
		result.append(getActivity().getResources().getString(R.string.event_category) + " : ").append(eventRecordModel.getEventCategoryName()).append("\r\n");
		result.append(getActivity().getResources().getString(R.string.event_date) + " : ").append(DateUtils.format(eventDate, DateUtils.DEFAULT_DATE_PATTERN)).append("\r\n");
		result.append(getActivity().getResources().getString(R.string.using_time) + " : ").append(DateUtils.formatAdjust(usingTime)).append("\r\n");
		result.append(getActivity().getResources().getString(R.string.summary) + " : ").append(summaryEv.getText().toString()).append("\r\n");
		return result.toString();
	}
	
	class SaveButListener implements MyAlertDialogFragment.OKOnClickListener {
		private EventRecordModel saveModel;
		
		public SaveButListener (EventRecordModel saveModel) {
			this.saveModel = saveModel; 
		}

		@Override
		public void onClick(View view) {
			new EventRecordsDAO(getActivity()).insert(saveModel);
		}
		
	}
}
