package com.yvelabs.timerecording;

import java.util.List;
import java.util.Map;

import com.yvelabs.timerecording.ConfigCategoryFragment.DeleteListener;
import com.yvelabs.timerecording.dao.EventCategoryDAO;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

public class ConfigEventFragment extends Fragment {
	
	private ImageButton addBut;
	
	public static ConfigEventFragment newInstance(Map<String, Object> map) {
		ConfigEventFragment configEventFragment = new ConfigEventFragment();
		Bundle args = new Bundle();
		args.putInt( "position", map.get("POSICTION") == null ? -1 : Integer.parseInt(map.get("POSICTION").toString()));
		configEventFragment.setArguments(args);
		return configEventFragment;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.config_event_fragment, null);
		
		addBut = (ImageButton) view.findViewById(R.id.config_event_add_but);
		
		addBut.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//是否有可用的大类
				EventCategoryModel para = new EventCategoryModel();
				para.setStatus("1");
				List<EventCategoryModel> categoryList = new EventCategoryDAO(getActivity()).query(para);
				if (categoryList.size() > 0) {
					//add dialog
					FragmentTransaction ft = getFragmentManager().beginTransaction();
					ConfigEventFraAddDialog addDialog = ConfigEventFraAddDialog.newInstance();
					addDialog.show(ft, "config_event_add_dialog");
				} else {
					//alert dialog
					StringBuilder msg = new StringBuilder();
					msg.append(getString(R.string.please_add_categories) + " ");
					msg.append(getString(R.string.or) + "\r\n");
					msg.append(getString(R.string.modify_the_categories_for_the_enable) + ". ");
					DialogFragment newFragment = MyAlertDialogFragment.newInstance(
				            R.string.attention, R.drawable.ic_my_alert, msg.toString(), null, null);
				    newFragment.show(getFragmentManager(), "config_event_not_categroy_attention_dialog");
				}
				
				
			}
		});
		
		return view;
	}
	
	public void refreshEventList() {
		
	}

}
