package com.yvelabs.timerecording;

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

import com.yvelabs.timerecording.dao.EventDAO;
import com.yvelabs.timerecording.utils.MyKeyValuePair;
import com.yvelabs.timerecording.utils.SpinnerUtils;
import com.yvelabs.timerecording.utils.TypefaceUtils;
import com.yvelabs.timerecording.utils.SpinnerUtils.MySpinnerAdapter2;

public class ConfigEventFraAddDialog extends DialogFragment {
	
	private TextView titleText;
	private TextView warning;
	private EditText eventName;
	private Spinner categorySp;
	private Spinner orderSp;
	private Spinner statusSp;
	private ImageButton addBut;
	
	static ConfigEventFraAddDialog newInstance() {
		ConfigEventFraAddDialog f = new ConfigEventFraAddDialog();
		Bundle args = new Bundle();
		f.setArguments(args);
		return f;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setStyle(STYLE_NO_TITLE, 0);
	}
	
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.config_event_fra_add_dialog, container, false);
		
		titleText = (TextView) view.findViewById(R.id.config_event_add_dialog_add_title_text);
		warning = (TextView) view.findViewById(R.id.config_event_add_dialog_warning);
		eventName = (EditText) view.findViewById(R.id.config_event_add_dialog_event_name);
		categorySp = (Spinner) view.findViewById(R.id.config_event_add_dialog_category_sp);
		orderSp = (Spinner) view.findViewById(R.id.config_event_add_dialog_order_sp);
		statusSp = (Spinner) view.findViewById(R.id.config_event_add_dialog_status_sp);
		addBut = (ImageButton) view.findViewById(R.id.config_event_add_dialog_save_but);
		
		new TypefaceUtils().setTypeface(titleText, TypefaceUtils.MOBY_MONOSPACE);
		
		List<MyKeyValuePair> categoryList = new SpinnerUtils().categorySpinner(getActivity());
		ArrayAdapter<MyKeyValuePair> categorySppinerAdapter = new SpinnerUtils().new MySpinnerAdapter2(getActivity(), categoryList); 
		categorySp.setAdapter(categorySppinerAdapter);
		
		List<MyKeyValuePair> orderList = new SpinnerUtils().orderSpinner();
		ArrayAdapter<MyKeyValuePair> orderSppinerAdapter = new SpinnerUtils().new MySpinnerAdapter2(getActivity(), orderList); 
		orderSp.setAdapter(orderSppinerAdapter);
		orderSp.setSelection(2);
		
		List<MyKeyValuePair> statusList = new SpinnerUtils().statusSpinner(getActivity());
		ArrayAdapter<MyKeyValuePair> statusSppinerAdapter = new SpinnerUtils().new MySpinnerAdapter2(getActivity(), statusList); 
		statusSp.setAdapter(statusSppinerAdapter);
		
		addBut.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				warning.setText("");
				//validate
				if (eventName.getText().toString().trim().length() <= 0) {
					warning.setText(getString(R.string.please_enter_the_event));
					return;
				}
				
				
				EventModel parameter = new EventModel();
				parameter.setEventName(eventName.getText().toString().trim());
				MyKeyValuePair categoryPair = (MyKeyValuePair) categorySp.getSelectedItem();
				parameter.setEventCategoryName(categoryPair.getKey().toString());
				
				List<EventModel> repeatList = new EventDAO(getActivity()).query(parameter);
				if (repeatList.size() > 0) {
					warning.setText(getString(R.string.this_event_already_exists_in_the_category) + "(" + categoryPair.getKey().toString() + ")");
					return;
				}
					
				//insert t_event
				MyKeyValuePair orderPair = (MyKeyValuePair) orderSp.getSelectedItem();
				parameter.setOrder(Integer.parseInt(orderPair.getKey().toString()));
				MyKeyValuePair statusPair = (MyKeyValuePair) statusSp.getSelectedItem();
				parameter.setStatus(statusPair.getKey().toString());
				
				new EventDAO(getActivity()).insert(parameter);
				
				((ConfigActivity)getActivity()).refreshEventList();
				
				getDialog().dismiss();
			}
		});
		
		return view;
		
	}

}
