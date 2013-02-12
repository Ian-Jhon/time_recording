package com.yvelabs.timerecording;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageButton;

import com.yvelabs.timerecording.dao.EventRecordsDAO;
import com.yvelabs.timerecording.utils.DateUtils;
import com.yvelabs.timerecording.utils.LogUtils;

public class DiaryListFragment extends ListFragment {

	private DiaryListAdapter diaryListAdapter;
	private List<List<EventRecordModel>> diaryList = new ArrayList<List<EventRecordModel>>();
	private ImageButton searchIb;
	private Date searchDate = new Date();

	public static DiaryListFragment newInstance(Map<String, Object> map) {
		DiaryListFragment diaryListFragment = new DiaryListFragment();
		Bundle args = new Bundle();
		args.putInt(
				"position",
				map.get("POSICTION") == null ? -1 : Integer.parseInt(map.get(
						"POSICTION").toString()));
		diaryListFragment.setArguments(args);
		return diaryListFragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.diary_list, container, false);
		searchIb = (ImageButton) view.findViewById(R.id.diary_list_search_but);
		
		searchIb.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Calendar c = Calendar.getInstance();
				
				DatePickerDialog dialog = new DatePickerDialog(getActivity(),
						new DatePickerDialog.OnDateSetListener() {
							public void onDateSet(DatePicker dp, int year, int month, int dayOfMonth) {
								searchDate = DateUtils.getDateByYMD(year, month, dayOfMonth);
							}
						}, 
						c.get(Calendar.YEAR), // 传入年份
						c.get(Calendar.MONTH), // 传入月份
						c.get(Calendar.DAY_OF_MONTH) // 传入天数
				);
				
				dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
					@Override
					public void onDismiss(DialogInterface dialog) {
						searchDate(searchDate);
					}
				});
				
				dialog.show();
			}
		});
		
		refreshDiaryList ();
		setListAdapter(diaryListAdapter);
		
		return view;
	}
	
	@Override
	public void onResume() {
		super.onResume();
		refreshDiaryList ();
	}
	
	private void searchDate (Date searchDate) {
		LogUtils.d(getClass(), "searchDate:" + searchDate);
		for (int i = 0 ; i < diaryList.size() ; i ++) {
			List<EventRecordModel> subList = diaryList.get(i);
			if (subList != null && subList.size() > 0) {
				if (subList.get(0).getEventDate().compareTo(searchDate) <= 0) {
					setSelection(i);
					LogUtils.d(getClass(), "i:" + i);
					break;
				}
			}
		}
		
		setSelection(diaryList.size() - 1);
	}

	public void refreshDiaryList () {
		
		diaryList.removeAll(diaryList);
		diaryList.addAll(new EventRecordsDAO(getActivity()).getDiaryList());
		
		if (diaryListAdapter == null) 
			diaryListAdapter = new DiaryListAdapter(getActivity(), diaryList);
		
		diaryListAdapter.notifyDataSetChanged();
	}
}
