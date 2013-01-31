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
import android.view.View;
import android.view.View.OnCreateContextMenuListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.yvelabs.timerecording.dao.EventCategoryDAO;
import com.yvelabs.timerecording.dao.EventDAO;
import com.yvelabs.timerecording.dao.EventRecordsDAO;

public class ConfigEventFragment extends Fragment {
	
	private ImageButton addBut;
	private ListView eventList;

	private List<EventModel> eventModels = new ArrayList<EventModel>();
	private ConfigEventListAdapter configEventListAdapter;
	
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
		eventList = (ListView) view.findViewById(R.id.config_event_list);
		
		refreshEventList();
		eventList.setAdapter(configEventListAdapter);
		
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
		
		eventList.setOnCreateContextMenuListener(new OnCreateContextMenuListener() {
			@Override
			public void onCreateContextMenu(ContextMenu menu, View v,
					ContextMenuInfo menuInfo) {
//				menu.setHeaderTitle("");
//              menu.setHeaderIcon(R.drawable. ic_launcher);
				menu.add(0, 1, 1, R.string.edit);
				menu.add(0, 2, 2, R.string.delete);

			}
		});
		
		return view;
	}
	
	
	public void refreshEventList() {
		eventModels.removeAll(eventModels);
		eventModels.addAll(new EventDAO(getActivity()).query(new EventModel()));
		
		if (configEventListAdapter == null) 
			configEventListAdapter = new ConfigEventListAdapter(getActivity(), eventModels);
		
		configEventListAdapter.notifyDataSetChanged();
		
	}
	
	public void deleteEvent (int selectedPosition) {
		EventModel selectedModel = (EventModel) eventList.getItemAtPosition(selectedPosition);
		
		StringBuilder deleteMsg = new StringBuilder();
		deleteMsg.append(getString(R.string.do_you_want_to_delete)).append(" ");
		deleteMsg.append(selectedModel.getEventName()).append(" (").append(selectedModel.getEventCategoryName()).append(") ");
		DialogFragment newFragment = MyAlertDialogFragment.newInstance(
	            R.string.delete, R.drawable.ic_delete_normal, deleteMsg.toString(), new DeleteListener(selectedModel), null);
	    newFragment.show(getFragmentManager(), "config_edit_delete_dialog");
	}
	
	public void editEdit (int selectedPosition) {
		EventModel selectedModel = (EventModel) eventList.getItemAtPosition(selectedPosition);
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		ConfigEventFraEditDialog configEventFraEditDialog = ConfigEventFraEditDialog.newInstance(selectedModel);
		configEventFraEditDialog.show(ft, "config_event_edit_dialog");
	}
	
	class DeleteListener implements MyAlertDialogFragment.OKOnClickListener {

		private EventModel selectedModel;

		public DeleteListener(EventModel selectedModel) {
			this.selectedModel = selectedModel;
		}

		@Override
		public void onClick(View view) {
			//select t_event_records
			EventRecordModel parameter = new EventRecordModel();
			parameter.setEventCategoryName(selectedModel.getEventCategoryName());
			parameter.setEventName(selectedModel.getEventName());
			List<EventRecordModel> existList = new EventRecordsDAO(getActivity()).query(parameter);
			
			if (existList.size() > 0) {
				StringBuilder msg = new StringBuilder();
				msg.append(selectedModel.getEventName()).append(" (").append(selectedModel.getEventCategoryName()).append(") ");
				msg.append(" ").append(getString(R.string.has_been_used_in_the_record));
				Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT ).show();
			} else {
				//delete
				EventModel deletePara = new EventModel();
				deletePara.setEventId(selectedModel.getEventId());
				new EventDAO(getActivity()).delete(deletePara);
				
				refreshEventList ();
			}
		}
	}
}
