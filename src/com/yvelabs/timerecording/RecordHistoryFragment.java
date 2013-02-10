package com.yvelabs.timerecording;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.yvelabs.timerecording.dao.EventRecordsDAO;
import com.yvelabs.timerecording.utils.LogUtils;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnCreateContextMenuListener;
import android.widget.ImageButton;
import android.widget.ListView;

public class RecordHistoryFragment extends Fragment {
	
	private ImageButton searchBut;
	private ListView historyList;
	private ImageButton initBut;
	
	private RecordHistoryListAdapter recordHistoryListAdapter;
	private EventRecordModel parameter = new EventRecordModel();
	private List<EventRecordModel> eventRecordModels = new ArrayList<EventRecordModel>();

	public static RecordHistoryFragment newInstance(Map<String, Object> map) {
		RecordHistoryFragment recordHistoryFragment = new RecordHistoryFragment();
		Bundle args = new Bundle();
		args.putInt( "position", map.get("POSICTION") == null ? -1 : Integer.parseInt(map.get(
						"POSICTION").toString()));
		recordHistoryFragment.setArguments(args);

		return recordHistoryFragment;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.record_histroy_fragment, null);
		searchBut = (ImageButton) view.findViewById(R.id.record_history_search_but);
		initBut = (ImageButton) view.findViewById(R.id.record_history_init_but);
		historyList = (ListView) view.findViewById(R.id.record_history_list);
		
		searchBut.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				FragmentTransaction ft = getFragmentManager().beginTransaction();
				RecordHistorySearchDialog searchDialog = RecordHistorySearchDialog.newInstance();
				searchDialog.show(ft, "record_history_search_dialog");
			}
		});
		
		initBut.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				refreshHistoryList (new EventRecordModel());
			}
		});
		
		refreshHistoryList ();
		historyList.setAdapter(recordHistoryListAdapter);
		
		historyList.setOnCreateContextMenuListener(new OnCreateContextMenuListener() {
			@Override
			public void onCreateContextMenu(ContextMenu menu, View v,
					ContextMenuInfo menuInfo) {
				menu.add(0, 301, 1, R.string.delete);

			}
		});
		return view;
	}
	
	public void deleteSelectedRecord (int selectedPosition) {
		EventRecordModel selectedModel = eventRecordModels.get(selectedPosition);
		EventRecordModel paraModel = new EventRecordModel();
		paraModel.setRecordId(selectedModel.getRecordId());
		new EventRecordsDAO(getActivity()).delete(paraModel);
		
		refreshHistoryList ();
	}
	
	@Override
	public void onResume() {
		refreshHistoryList ();
		super.onResume();
	}
	
	public void refreshHistoryList () {
		refreshHistoryList(parameter);
	}
	
	public void refreshHistoryList (EventRecordModel parameter) {
		this.parameter = parameter;
		eventRecordModels.removeAll(eventRecordModels);
		eventRecordModels.addAll(new EventRecordsDAO(getActivity()).query(parameter));
		
		LogUtils.d(this.getClass(), "eventRecordModels:" + eventRecordModels);
		
		if (recordHistoryListAdapter == null)
			recordHistoryListAdapter = new RecordHistoryListAdapter(getActivity(), eventRecordModels);
		
		recordHistoryListAdapter.notifyDataSetChanged();
	}
	
	
}
