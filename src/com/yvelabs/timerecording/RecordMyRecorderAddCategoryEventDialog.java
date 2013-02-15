package com.yvelabs.timerecording;

import java.util.ArrayList;
import java.util.List;

import com.yvelabs.timerecording.dao.EventCategoryDAO;
import com.yvelabs.timerecording.dao.EventDAO;
import com.yvelabs.timerecording.utils.MyKeyValuePair;
import com.yvelabs.timerecording.utils.SpinnerUtils;
import com.yvelabs.timerecording.utils.TypefaceUtils;
import com.yvelabs.timerecording.utils.SpinnerUtils.MySpinnerAdapter2;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

public class RecordMyRecorderAddCategoryEventDialog extends DialogFragment {
	
	private TextView titleTextTV;
	private TextView addCateogryLabel;
	private EditText categoryNameET;
	private ImageButton addCategoryBut;
	private TextView addEventLabel;
	private Spinner categorySP;
	private EditText eventNameET;
	private ImageButton addEventBut;
	private TextView categoryErrorTV;
	private TextView eventErrorTV;
	
	private List<MyKeyValuePair> categoryList = new ArrayList<MyKeyValuePair>();
	private ArrayAdapter<MyKeyValuePair> categorySppinerAdapter;

	static RecordMyRecorderAddCategoryEventDialog newInstance() {
		RecordMyRecorderAddCategoryEventDialog f = new RecordMyRecorderAddCategoryEventDialog();
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
		View view = inflater.inflate(R.layout.record_myrecorder_add_category_event_dialog, null);
		titleTextTV = (TextView) view.findViewById(R.id.record_my_record_add_category_event_dialog_title_text);
		addCateogryLabel = (TextView) view.findViewById(R.id.record_my_recorder_add_category_event_dialog_add_cate_label);
		categoryNameET = (EditText) view.findViewById(R.id.  record_my_record_add_category_event_dialog_category_name_et);
		addCategoryBut = (ImageButton) view.findViewById(R.id.record_my_record_add_category_event_dialog_add_category_but);
		addEventLabel = (TextView) view.findViewById(R.id.record_my_record_add_category_event_dialog_add_event_label);
		categorySP = (Spinner) view.findViewById(R.id.record_my_record_add_category_event_dialog_category_sp);
		eventNameET = (EditText) view.findViewById(R.id.record_my_record_add_category_event_dialog_event_name_et);
		addEventBut = (ImageButton) view.findViewById(R.id.record_my_record_add_category_event_dialog_add_event_but);
		categoryErrorTV = (TextView) view.findViewById(R.id.record_my_record_add_category_event_dialog_category_error_tv);
		eventErrorTV = (TextView) view.findViewById(R.id.record_my_record_add_category_event_dialog_event_error_tv);
		
		new TypefaceUtils().setTypeface(titleTextTV, TypefaceUtils.MOBY_MONOSPACE);
		new TypefaceUtils().setTypeface(addCateogryLabel, TypefaceUtils.MOBY_MONOSPACE);
		new TypefaceUtils().setTypeface(addEventLabel, TypefaceUtils.MOBY_MONOSPACE);
		addCateogryLabel.getPaint().setFakeBoldText(true);
		addEventLabel.getPaint().setFakeBoldText(true);
		
		refreshCategorySP ();
		categorySP.setAdapter(categorySppinerAdapter);
		
		addCategoryBut.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				categoryErrorTV.setText("");
				//validate
				if (categoryNameET.getText().toString().trim().length() <= 0) {
					categoryErrorTV.setText(getString(R.string.please_enter_the_category_name));
					return;
				}
				
				EventCategoryModel categoryModel = new EventCategoryModel();
				categoryModel.setEventCategoryName(categoryNameET.getText().toString().trim());
				List<EventCategoryModel> repeatList = new EventCategoryDAO(getActivity()).query(categoryModel);
				if (repeatList.size() > 0) {
					categoryErrorTV.setText(categoryNameET.getText().toString().trim() + " " + getString(R.string.already_exists));
					return;
				}
				
				//insert into t_event_category
				categoryModel.setStatus("1");
				new EventCategoryDAO(getActivity()).insert(categoryModel);
					
				refreshCategorySP ();
				
				categoryNameET.setText("");
			}
		});
		
		addEventBut.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				eventErrorTV.setText("");
				//validate
				if (eventNameET.getText().toString().trim().length() <= 0) {
					eventErrorTV.setText(getString(R.string.please_enter_the_event));
					return;
				}
				
				EventModel parameter = new EventModel();
				parameter.setEventName(eventNameET.getText().toString().trim());
				MyKeyValuePair categoryPair = (MyKeyValuePair) categorySP.getSelectedItem();
				parameter.setEventCategoryName(categoryPair.getKey().toString());
				List<EventModel> repeatList = new EventDAO(getActivity()).query(parameter);
				if (repeatList.size() > 0) {
					eventErrorTV.setText(getString(R.string.this_event_already_exists_in_the_category) + "(" + categoryPair.getKey().toString() + ")");
					return;
				}
				
				//insert
				parameter.setOrder(3);
				parameter.setStatus("1");
				new EventDAO(getActivity()).insert(parameter);
				
				//refresh
				((RecordActivity)getActivity()).refreshAddRecordCategoryNEvent();
				((RecordActivity)getActivity()).refreshMyRecordEventList();
				
				eventNameET.setText("");
			}
		});
		
		return view;
	}
	
	public void refreshCategorySP () {
		categoryList.removeAll(categoryList);
		categoryList.addAll(new SpinnerUtils().categorySpinner(getActivity()));
		
		if (categorySppinerAdapter == null)
			categorySppinerAdapter = new SpinnerUtils().new MySpinnerAdapter2(getActivity(), categoryList); 
		
		categorySppinerAdapter.notifyDataSetChanged();
	}
}
