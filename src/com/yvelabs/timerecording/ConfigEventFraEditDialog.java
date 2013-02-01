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


public class ConfigEventFraEditDialog extends DialogFragment {
	
	private EventModel selectedModel;
	
	private TextView titleText;
	private TextView warning;
	private EditText eventName;
	private Spinner categorySp;
	private Spinner orderSp;
	private Spinner statusSp;
	private ImageButton saveBut;
	
	static ConfigEventFraEditDialog newInstance(EventModel selectedModel) {
		ConfigEventFraEditDialog f = new ConfigEventFraEditDialog();
		Bundle args = new Bundle();
		args.putParcelable("SELECTED_MODEL", selectedModel);
		f.setArguments(args);
		return f;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setStyle(STYLE_NO_TITLE, 0);
		
		selectedModel = getArguments().getParcelable("SELECTED_MODEL");
	}
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.config_event_fra_edit_dialog, container, false);
		
		titleText = (TextView) view.findViewById(R.id.config_event_edit_dialog_edit_title_text);
		warning = (TextView) view.findViewById(R.id.config_event_edit_dialog_warning);
		eventName = (EditText) view.findViewById(R.id.config_event_edit_dialog_event_name);
		categorySp = (Spinner) view.findViewById(R.id.config_event_edit_dialog_category_sp);
		orderSp = (Spinner) view.findViewById(R.id.config_event_edit_dialog_order_sp);
		statusSp = (Spinner) view.findViewById(R.id.config_event_edit_dialog_status_sp);
		saveBut = (ImageButton) view.findViewById(R.id.config_event_edit_dialog_save_but);
		
		new TypefaceUtils().setTypeface(titleText, TypefaceUtils.MOBY_MONOSPACE);
		
		eventName.setText(selectedModel.getEventName());
		
		List<MyKeyValuePair> categoryList = new SpinnerUtils().categorySpinner(getActivity());
		ArrayAdapter<MyKeyValuePair> categorySppinerAdapter = new ArrayAdapter<MyKeyValuePair>(getActivity(), android.R.layout.simple_spinner_item, categoryList); 
		categorySp.setAdapter(categorySppinerAdapter);
		MyKeyValuePair categorySelectedPair = new MyKeyValuePair();
		categorySelectedPair.setKey(selectedModel.getEventCategoryName());
		categorySelectedPair.setValue(selectedModel.getEventCategoryName());
		categorySp.setSelection(new SpinnerUtils().getSpinnerPosition(categorySp, categorySelectedPair));
		
		List<MyKeyValuePair> orderList = new SpinnerUtils().orderSpinner();
		ArrayAdapter<MyKeyValuePair> orderSppinerAdapter = new ArrayAdapter<MyKeyValuePair>(getActivity(), android.R.layout.simple_spinner_item, orderList); 
		orderSp.setAdapter(orderSppinerAdapter);
		MyKeyValuePair orderSelectedPair = new MyKeyValuePair();
		orderSelectedPair.setKey(selectedModel.getOrder());
		orderSelectedPair.setValue(selectedModel.getOrder() + "");
		orderSp.setSelection(new SpinnerUtils().getSpinnerPosition(orderSp, orderSelectedPair));
		
		List<MyKeyValuePair> statusList = new SpinnerUtils().statusSpinner(getActivity());
		ArrayAdapter<MyKeyValuePair> statusSppinerAdapter = new ArrayAdapter<MyKeyValuePair>(getActivity(), android.R.layout.simple_spinner_item, statusList); 
		statusSp.setAdapter(statusSppinerAdapter);
		MyKeyValuePair statusSelectedPair = new SpinnerUtils().generateStatusSpinner(getActivity(), selectedModel.getStatus());
		statusSp.setSelection(new SpinnerUtils().getSpinnerPosition(statusSp, statusSelectedPair));
		
		
		saveBut.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				MyKeyValuePair categoryPair = (MyKeyValuePair) categorySp.getSelectedItem();
				MyKeyValuePair orderPair = (MyKeyValuePair) orderSp.getSelectedItem();
				MyKeyValuePair statusPair = (MyKeyValuePair) statusSp.getSelectedItem();
				int order = orderPair.getKey() == null ? 0 : Integer.parseInt(orderPair.getKey().toString());
				
				if (eventName.getText().toString().length() <= 0) {
					warning.setText(getString(R.string.please_enter_the_event_name));
					return;
				}
				
				if (selectedModel.getEventCategoryName().toLowerCase().equals(categoryPair.getKey().toString().toLowerCase())
						&& selectedModel.getEventName().toLowerCase().equals(eventName.getText().toString().toLowerCase())
						&& selectedModel.getStatus().equals(statusPair.getKey().toString())
						&& selectedModel.getOrder() == order) {
					warning.setText(getString(R.string.no_changes));
					return;
				} else {
					//update
					EventModel newModel = new EventModel();
					newModel.setEventName(eventName.getText().toString());
					newModel.setEventCategoryName(categoryPair.getKey().toString());
					newModel.setOrder(order);
					newModel.setStatus(statusPair.getKey().toString());
					new EventDAO(getActivity()).updateAllTable(selectedModel, newModel);
				}
				
				((ConfigActivity)getActivity()).refreshEventList();
				getDialog().dismiss();
			}
		});
		
		return view;
	}

}
