package com.yvelabs.timerecording;

import java.util.List;

import com.yvelabs.timerecording.dao.EventCategoryDAO;
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

public class ConfigCategoryFraEditDialog extends DialogFragment {
	
	private ImageButton saveBut;
	private EditText categoryNameEt;
	private Spinner stateSp;
	private TextView titleTv;
	private TextView warnTv;
	
	private EventCategoryModel selectedModel;
	
	static ConfigCategoryFraEditDialog newInstance(EventCategoryModel selectedModle) {
		ConfigCategoryFraEditDialog f = new ConfigCategoryFraEditDialog();
		Bundle args = new Bundle();
		args.putParcelable("SELECTED_MDOLE", selectedModle);
		f.setArguments(args);
		
		return f;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setStyle(STYLE_NO_TITLE, 0);
		
		selectedModel = getArguments().getParcelable("SELECTED_MDOLE");
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.config_category_fra_edit_dialog, container, false);
		saveBut = (ImageButton) view.findViewById(R.id.config_category_edit_dialog_save_but);
		categoryNameEt = (EditText) view.findViewById(R.id.config_category_edit_dialog_category_name);
		stateSp = (Spinner) view.findViewById(R.id.config_category_edit_dialog_state_sp);
		titleTv = (TextView) view.findViewById(R.id.config_category_edit_dialog_edit_title_text);
		warnTv = (TextView) view.findViewById(R.id.config_category_edit_dialog_warn);
		
		new TypefaceUtils().setTypeface(titleTv, TypefaceUtils.MOBY_MONOSPACE);
		
		categoryNameEt.setText(selectedModel.getEventCategoryName());
		
		//init state spinner
		List<MyKeyValuePair> stateList = new SpinnerUtils().statusSpinner(getActivity());
		
		ArrayAdapter<MyKeyValuePair> stateSppinerAdapter = new ArrayAdapter<MyKeyValuePair>(getActivity(), android.R.layout.simple_spinner_item, stateList); 
		stateSp.setAdapter(stateSppinerAdapter);
		
		if ("1".equals(selectedModel.getStatus())) {
			stateSp.setSelection(0);
		} else if ("2".equals(selectedModel.getStatus())) {
			stateSp.setSelection(1);
		}
		
		
		saveBut.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				boolean isChanged = false;
				
				if (categoryNameEt.getText().toString().trim().length() <= 0) {
					warnTv.setText(getString(R.string.please_enter_the_category_name));
					return;
				}
				
				//update category name
				if (!selectedModel.getEventCategoryName().equals(categoryNameEt.getText().toString().trim())) {
					EventCategoryModel newModel = new EventCategoryModel();
					newModel.setEventCategoryName(categoryNameEt.getText().toString().trim());
					new EventCategoryDAO(getActivity()).updateAllTableByCategoryName(selectedModel, newModel);
					
					isChanged = true;
				}
				
				//update status
				MyKeyValuePair statePair = (MyKeyValuePair) stateSp.getSelectedItem();
				if (!selectedModel.getStatus().equals(statePair.getKey().toString())) {
					EventCategoryModel parameter = new EventCategoryModel();
					parameter.setEventCategoryId(selectedModel.getEventCategoryId());
					parameter.setStatus(statePair.getKey().toString());
					new EventCategoryDAO(getActivity()).updateStatusById(parameter);
					
					isChanged = true;
				}
				
				if (isChanged == false) {
					warnTv.setText(getString(R.string.no_changes));
					return;
				}
				
				((ConfigActivity)getActivity()).refreshCategoryList();
				((ConfigActivity)getActivity()).refreshEventList();
				getDialog().dismiss();
			}
			
		});
		
		return view;
	}
}
