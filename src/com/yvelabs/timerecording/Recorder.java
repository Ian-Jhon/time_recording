package com.yvelabs.timerecording;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnCreateContextMenuListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.yvelabs.chronometer.Chronometer;
import com.yvelabs.chronometer.utils.FontUtils;
import com.yvelabs.timerecording.utils.LogUtils;

public class Recorder extends Fragment {

	private int position;
	private View view;
	private ListView eventList;
	private TextView eventName;
	private TextView eventCategory;
	private Chronometer eventChro;
	
	private ImageButton resetBut;
	private ImageButton startBut;
	private ImageButton pauseBut;
	private ImageButton stopBut;
	
	private ControlPanelHandler controlPanelHandler;
	private RecorderEventListAdapter recorderEventListAdapter;
	
	private List<EventModel> eventModels;
	private EventModel currentEvent;

	public static Recorder newInstance(Map<String, Object> map) {
		Recorder record = new Recorder();
		Bundle args = new Bundle();
		args.putInt( "position", map.get("POSICTION") == null ? -1 : Integer.parseInt(map.get(
						"POSICTION").toString()));
		record.setArguments(args);

		return record;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		position = getArguments() != null ? getArguments().getInt("position") : -1;
		if (savedInstanceState != null) {
			// TODO 恢复页面
		}
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		controlPanelHandler = new ControlPanelHandler();
		
		View view = inflater.inflate(R.layout.recorder, null);
		this.view = view;
		eventList = (ListView) view.findViewById(R.id.event_list);
		eventName = (TextView) view.findViewById(R.id.event_name);
		eventCategory = (TextView) view.findViewById(R.id.event_category);
		eventChro = (Chronometer) view.findViewById(R.id.event_chro);
		resetBut = (ImageButton) view.findViewById(R.id.reset_but);
		startBut = (ImageButton) view.findViewById(R.id.start_but);
		pauseBut = (ImageButton) view.findViewById(R.id.pause_but);
		stopBut = (ImageButton) view.findViewById(R.id.stop_but);
		
		eventChro.setPlayPauseAlphaAnimation(true);
		eventChro.setFont(FontUtils.FONT_DUPLEX);
		eventChro.reset();
		
		resetBut.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startBut.setVisibility(View.VISIBLE);
				pauseBut.setVisibility(View.GONE);
				eventChro.reset();
				
				//初始化 event model
				currentEvent.setStartElapsedTime(0);
				currentEvent.setChro_state(EventModel.STATE_STOP);
				currentEvent.setStartTime(null);
				
				//刷新列表
				recorderEventListAdapter.notifyDataSetChanged();
				
			}
		});
		
		startBut.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startBut.setVisibility(View.GONE);
				pauseBut.setVisibility(View.VISIBLE);
				eventChro.start();
				
				currentEvent.setStartElapsedTime(SystemClock.elapsedRealtime() - eventChro.duringTime());
				currentEvent.setChro_state(EventModel.STATE_START);
				currentEvent.setStartTime(new Date());
				
				//刷新列表
				recorderEventListAdapter.notifyDataSetChanged();
			}
		});
		
		pauseBut.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startBut.setVisibility(View.VISIBLE);
				pauseBut.setVisibility(View.GONE);
				eventChro.pause();
				
				currentEvent.setStartElapsedTime(eventChro.duringTime());
				currentEvent.setChro_state(EventModel.STATE_PAUSE);
				
				//刷新列表
				recorderEventListAdapter.notifyDataSetChanged();
			}
		});
		
		stopBut.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				eventChro.stop();
				startBut.setVisibility(View.VISIBLE);
				pauseBut.setVisibility(View.GONE);
				
				//TODO 保存数据进 t_event_reords
				
				//初始化 该事件 event model
				currentEvent.setStartElapsedTime(0);
				currentEvent.setChro_state(EventModel.STATE_STOP);
				currentEvent.setStartTime(null);
				
				//刷新列表
				recorderEventListAdapter.notifyDataSetChanged();
			}
		});
		
		//列表单击事件
		eventList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				new Thread(new ControlPanleRunnable(eventModels.get(arg2))).start();
				
				currentEvent = eventModels.get(arg2);
			}
		});
		
		//列表长按菜单
		eventList.setOnCreateContextMenuListener(new OnCreateContextMenuListener() {
			@Override
			public void onCreateContextMenu(ContextMenu menu, View v,
					ContextMenuInfo menuInfo) {
				menu.setHeaderTitle("");
                menu.setHeaderIcon(R.drawable. ic_launcher);
//				menu.add(0, 1, 5, R.string.delete_all);
//				menu.add(0, 2, 5, R.string.delete_this);

			}
		});
		
		return view;
	}
	
	@Override
	public void onResume() {
		super.onResume();
		
		eventModels = new ArrayList<EventModel>();
		EventModel eventModel = new EventModel();
		eventModel.setEventName("Eat");
		eventModel.setEventCategoryName("rest");
		eventModels.add(eventModel);
		
		eventModel = new EventModel();
		eventModel.setEventName("sleep");
		eventModel.setEventCategoryName("rest");
		eventModels.add(eventModel);
		
		eventModel = new EventModel();
		eventModel.setEventName("go to work");
		eventModel.setEventCategoryName("work");
		eventModels.add(eventModel);
		
		eventModel = new EventModel();
		eventModel.setEventName("study");
		eventModel.setEventCategoryName("work");
		eventModels.add(eventModel);
		
		eventModel = new EventModel();
		eventModel.setEventName("work");
		eventModel.setEventCategoryName("work");
		eventModels.add(eventModel);
		
		currentEvent = eventModels.get(0);
		
		if (recorderEventListAdapter == null)
			recorderEventListAdapter = new RecorderEventListAdapter(this.getActivity(), eventModels);
		eventList.setAdapter(recorderEventListAdapter);
	}
	
	class ControlPanleRunnable implements Runnable {
		private EventModel newModel;
		
		public ControlPanleRunnable (EventModel newModel) {
			this.newModel = newModel;
		}
		
		@Override
		public void run() {
			
			//载入新数据
			Message msg = controlPanelHandler.obtainMessage();
			Bundle date=new Bundle();
			date.putSerializable("EVENT_MODEL", newModel);
			msg.setData(date);
			msg.sendToTarget();
		}
	}
	
	class ControlPanelHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			Bundle bundle = msg.getData();
			EventModel eventModel = (EventModel) bundle.get("EVENT_MODEL");
			
			//更新 控制面板控件
			Recorder.this.eventName.setText(eventModel.getEventName());
			Recorder.this.eventCategory.setText(eventModel.getEventCategoryName());
			Recorder.this.eventChro.reset();
			
			if (EventModel.STATE_START.equals(eventModel.getChro_state())) {
				Recorder.this.pauseBut.setVisibility(View.VISIBLE);
				Recorder.this.startBut.setVisibility(View.GONE);

				Recorder.this.eventChro.setStartingTime(SystemClock.elapsedRealtime() - eventModel.getStartElapsedTime());
				Recorder.this.eventChro.start();
			} else if (EventModel.STATE_PAUSE.equals(eventModel.getChro_state())) {
				Recorder.this.pauseBut.setVisibility(View.GONE);
				Recorder.this.startBut.setVisibility(View.VISIBLE);
				
				Recorder.this.eventChro.setStartingTime(eventModel.getStartElapsedTime());
				Recorder.this.eventChro.start();
				Recorder.this.eventChro.pause();
			} else {
				Recorder.this.pauseBut.setVisibility(View.GONE);
				Recorder.this.startBut.setVisibility(View.VISIBLE);
			}
		}
	}
}



