package com.yvelabs.timerecording;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnCreateContextMenuListener;
import android.view.ViewGroup;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.yvelabs.timerecording.Recorder.ResetOkListener;
import com.yvelabs.timerecording.dao.EventCategoryDAO;
import com.yvelabs.timerecording.dao.EventDAO;

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
		
		categoryLv.setOnCreateContextMenuListener(new OnCreateContextMenuListener() {
			@Override
			public void onCreateContextMenu(ContextMenu menu, View v,
					ContextMenuInfo menuInfo) {
//				menu.setHeaderTitle("");
                menu.setHeaderIcon(R.drawable. ic_launcher);
				menu.add(0, 1, 1, R.string.edit);
				menu.add(0, 2, 2, R.string.delete);

			}
		});
		
		return view;
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		int selectedPosition = ((AdapterContextMenuInfo) item.getMenuInfo()).position;// 获取点击了第几行
		EventCategoryModel selectedModel = (EventCategoryModel) categoryLv.getItemAtPosition(selectedPosition);
		
		if (item.getItemId() == 1) {
			//edit
			FragmentTransaction ft = getFragmentManager().beginTransaction();
			ConfigCategoryFraEditDialog editDialog = ConfigCategoryFraEditDialog.newInstance(selectedModel);
			editDialog.show(ft, "config_category_edit_dialog");
			
		} else if (item.getItemId() == 2) {
			//delete confirm dialog
			String deleteMsg = getString(R.string.do_you_want_to_delete) + " " + selectedModel.getEventCategoryName();
			DialogFragment newFragment = MyAlertDialogFragment.newInstance(
		            R.string.delete, R.drawable.ic_delete_normal, deleteMsg, new DeleteListener(selectedModel), null);
		    newFragment.show(getFragmentManager(), "config_category_delete_dialog");
		    
		}
		
		return super.onContextItemSelected(item);
	}
	
	public void refreshList () {
		categoryModelList.removeAll(categoryModelList);
		categoryModelList.addAll(new EventCategoryDAO(getActivity()).query(new EventCategoryModel()));
		
		if (configCategoryListAdapter == null)
			configCategoryListAdapter = new ConfigCategoryListAdapter(getActivity(), categoryModelList);
		
		configCategoryListAdapter.notifyDataSetChanged();
	}
	
	
	class DeleteListener implements MyAlertDialogFragment.OKOnClickListener {
		
		private EventCategoryModel selectedModel;
		
		public DeleteListener (EventCategoryModel selectedModel) {
			this.selectedModel = selectedModel;
		}
		@Override
		public void onClick(View view) {
			//select
			EventModel parameter = new EventModel();
			parameter.setEventCategoryName(selectedModel.getEventCategoryName());
			List<EventModel> resultList = new EventDAO(getActivity()).query(parameter);
			
			if (resultList.size() <= 0) {
				//delete
				EventCategoryModel para = new EventCategoryModel();
				para.setEventCategoryId(selectedModel.getEventCategoryId());
				new EventCategoryDAO(getActivity()).delete(para);
				
				refreshList ();
			} else {
				Toast.makeText(getActivity(), selectedModel.getEventCategoryName() + getString(R.string.has_been_used_in_the_event), Toast.LENGTH_SHORT ).show();
			}
		}
	}

}
