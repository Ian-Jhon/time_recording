package com.yvelabs.timerecording;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.yvelabs.timerecording.dao.EventCategoryDAO;
import com.yvelabs.timerecording.utils.LogUtils;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;

public class ConfigCategoryFragment extends Fragment {
	
	private ImageButton addBut;
	private ListView categoryLv;
	
	private List<EventCategoryModel> categoryModelList = new ArrayList<EventCategoryModel>();
	private ConfigCategoryListAdapter configCategoryListAdapter;
	
	public static ConfigCategoryFragment newInstance(Map<String, Object> map) {
		ConfigCategoryFragment configCategoryFragment = new ConfigCategoryFragment();
		Bundle args = new Bundle();
		args.putInt( "position", map.get("POSICTION") == null ? -1 : Integer.parseInt(map.get(
						"POSICTION").toString()));
		configCategoryFragment.setArguments(args);

		return configCategoryFragment;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.config_category_fragment, null);
		addBut = (ImageButton) view.findViewById(R.id.config_category_add_but);
		categoryLv = (ListView) view.findViewById(R.id.config_category_list);
		
		addBut.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				FragmentTransaction ft = getFragmentManager().beginTransaction();
				ConfigCategoryFraAddDialog addDialog = ConfigCategoryFraAddDialog.newInstance();
				addDialog.show(ft, "config_category_add_dialog");
			}
		});
		
		refreshList ();
		categoryLv.setAdapter(configCategoryListAdapter);
		
		return view;
	}
	
	public void refreshList () {
		categoryModelList.removeAll(categoryModelList);
		categoryModelList.addAll(new EventCategoryDAO(getActivity()).query(new EventCategoryModel()));
		
		if (configCategoryListAdapter == null)
			configCategoryListAdapter = new ConfigCategoryListAdapter(getActivity(), categoryModelList);
		
		configCategoryListAdapter.notifyDataSetChanged();
	}

}
