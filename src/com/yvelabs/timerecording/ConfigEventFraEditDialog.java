package com.yvelabs.timerecording;
import java.util.List;

import com.yvelabs.timerecording.R;
import com.yvelabs.timerecording.utils.MyKeyValuePair;
import com.yvelabs.timerecording.utils.SpinnerUtils;
import com.yvelabs.timerecording.utils.TypefaceUtils;

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
		
		List<MyKeyValuePair> categoryList = new SpinnerUtils().categorySpinner(getActivity());
		ArrayAdapter<MyKeyValuePair> categorySppinerAdapter = new ArrayAdapter<MyKeyValuePair>(getActivity(), android.R.layout.simple_spinner_item, categoryList); 
		categorySp.setAdapter(categorySppinerAdapter);
		
		List<MyKeyValuePair> orderList = new SpinnerUtils().orderSpinner();
		ArrayAdapter<MyKeyValuePair> orderSppinerAdapter = new ArrayAdapter<MyKeyValuePair>(getActivity(), android.R.layout.simple_spinner_item, orderList); 
		orderSp.setAdapter(orderSppinerAdapter);
		orderSp.setSelection(2);
		
		List<MyKeyValuePair> statusList = new SpinnerUtils().statusSpinner(getActivity());
		ArrayAdapter<MyKeyValuePair> statusSppinerAdapter = new ArrayAdapter<MyKeyValuePair>(getActivity(), android.R.layout.simple_spinner_item, statusList); 
		statusSp.setAdapter(statusSppinerAdapter);
		
		saveBut.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				
			}
		});
		
		return view;
	}

}
