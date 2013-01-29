package com.yvelabs.timerecording;

import java.util.ArrayList;
import java.util.List;

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

import com.yvelabs.timerecording.dao.EventCategoryDAO;
import com.yvelabs.timerecording.utils.LogUtils;
import com.yvelabs.timerecording.utils.MyKeyValuePair;
import com.yvelabs.timerecording.utils.TypefaceUtils;

public class ConfigCategoryFraAddDialog extends DialogFragment {
	
	private ImageButton saveBut;
	private EditText categoryNameEt;
	private Spinner stateSp;
	private TextView titleTv;
	private TextView warnTv;
	
	static ConfigCategoryFraAddDialog newInstance() {
		ConfigCategoryFraAddDialog f = new ConfigCategoryFraAddDialog();
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
		// getDialog().setTitle(getString(R.string.app_name));

		View view = inflater.inflate(R.layout.config_category_fra_add_dialog, container, false);
		saveBut = (ImageButton) view.findViewById(R.id.config_category_add_dialog_save_but);
		categoryNameEt = (EditText) view.findViewById(R.id.config_category_add_dialog_category_name);
		stateSp = (Spinner) view.findViewById(R.id.config_category_add_dialog_state_sp);
		titleTv = (TextView) view.findViewById(R.id.config_category_add_dialog_add_title_text);
		warnTv = (TextView) view.findViewById(R.id.config_category_add_dialog_warn);
		
		new TypefaceUtils().setTypeface(titleTv, TypefaceUtils.MOBY_MONOSPACE);
		
		//init state spinner
		List<MyKeyValuePair> stateList = new ArrayList<MyKeyValuePair>();
		MyKeyValuePair pair = new MyKeyValuePair();
		pair.setKey(1);
		pair.setValue(getString(R.string.enable));
		stateList.add(pair);
		
		pair = new MyKeyValuePair();
		pair.setKey(2);
		pair.setValue(getString(R.string.disable));
		stateList.add(pair);
		
		ArrayAdapter<MyKeyValuePair> stateSppinerAdapter = new ArrayAdapter<MyKeyValuePair>(getActivity(), android.R.layout.simple_spinner_item, stateList); 
		stateSp.setAdapter(stateSppinerAdapter);

		saveBut.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				warnTv.setText("¡¡");
				//validate
				if (categoryNameEt.getText().toString().trim().length() <= 0) {
					warnTv.setText(getString(R.string.please_enter_the_category_name));
					return;
				}
				EventCategoryModel categoryModel = new EventCategoryModel();
				categoryModel.setEventCategoryName(categoryNameEt.getText().toString().trim());
				List<EventCategoryModel> repeatList = new EventCategoryDAO(getActivity()).query(categoryModel);
				if (repeatList.size() > 0) {
					warnTv.setText(categoryNameEt.getText().toString().trim() + " " + getString(R.string.already_exists));
					return;
				}
				
				//insert into t_event_category
				MyKeyValuePair statePair = (MyKeyValuePair) stateSp.getSelectedItem();
				categoryModel.setStatus(statePair.getKey().toString());
				new EventCategoryDAO(getActivity()).insert(categoryModel);
			
				((ConfigActivity)getActivity()).refreshCategoryList();
				
				getDialog().dismiss();
				
			}
		});

		return view;
	}
	
}
