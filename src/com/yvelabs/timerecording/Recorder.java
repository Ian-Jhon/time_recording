package com.yvelabs.timerecording;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnCreateContextMenuListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.yvelabs.chronometer.Chronometer;
import com.yvelabs.chronometer.utils.FontUtils;
import com.yvelabs.timerecording.dao.EventRecordsDAO;
import com.yvelabs.timerecording.utils.DateUtils;
import com.yvelabs.timerecording.utils.LogUtils;
import com.yvelabs.timerecording.utils.NotificationUtils;
import com.yvelabs.timerecording.utils.TypefaceUtils;

public class Recorder extends Fragment {

	private int position;
	private View view;
	private ListView eventList;
	private TextView eventName;
	private TextView eventCategory;
	private Chronometer eventChro;
	private EditText eventSummary;
	
	private ImageButton resetBut;
	private ImageButton startBut;
	private ImageButton pauseBut;
	private ImageButton stopBut;
	
	private ControlPanelHandler controlPanelHandler;
	private RecorderEventListAdapter recorderEventListAdapter;
	
	private ArrayList<EventModel> eventModels;
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
			this.eventModels = savedInstanceState.getParcelableArrayList("EVENT_MODELS");
			this.currentEvent = savedInstanceState.getParcelable("CURRENT_EVENT");
		}
		
		if (getActivity().getIntent() != null && getActivity().getIntent().getExtras() != null) {
			Bundle bundle = getActivity().getIntent().getExtras();
			currentEvent = bundle.getParcelable("CURRENT_EVENT");
			eventModels = bundle.getParcelableArrayList("EVENT_MODELS");
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
		eventSummary = (EditText) view.findViewById(R.id.event_summary);
		resetBut = (ImageButton) view.findViewById(R.id.reset_but);
		startBut = (ImageButton) view.findViewById(R.id.start_but);
		pauseBut = (ImageButton) view.findViewById(R.id.pause_but);
		stopBut = (ImageButton) view.findViewById(R.id.stop_but);
		
		TypefaceUtils.setTypeface(eventName, TypefaceUtils.RBNO2_LIGHT_A);
		TypefaceUtils.setTypeface(eventCategory, TypefaceUtils.RBNO2_LIGHT_A);
		TypefaceUtils.setTypeface(eventSummary, TypefaceUtils.RBNO2_LIGHT_A);
		
		eventName.getPaint().setFakeBoldText(true);
		
		eventName.setFocusable(true);
		eventName.setFocusableInTouchMode(true);
		eventName.requestFocus();

		//init chronometer
		eventChro.setPlayPauseAlphaAnimation(true);
		eventChro.setFont(FontUtils.FONT_DUPLEX);
		eventChro.reset();
		
		
		//chronometer reset
		resetBut.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				DialogFragment newFragment = MyAlertDialogFragment.newInstance(
			            R.string.my_recorder_rest_alert, R.drawable.ic_my_alert, null, new ResetOkListener(), new NormalCancelListener());
			    newFragment.show(getFragmentManager(), "dialog");
			}
		});
		
		//chronometer start
		startBut.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startBut.setVisibility(View.GONE);
				pauseBut.setVisibility(View.VISIBLE);
				eventChro.start();
				
				currentEvent.setStartElapsedTime(SystemClock.elapsedRealtime() - eventChro.duringTime());
				currentEvent.setChro_state(EventModel.STATE_START);
				currentEvent.setStartTime(new Date());
				
				//ˢ���б�
				recorderEventListAdapter.notifyDataSetChanged();
			}
		});
		
		//chronometer pause
		pauseBut.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startBut.setVisibility(View.VISIBLE);
				pauseBut.setVisibility(View.GONE);
				eventChro.pause();
				
				currentEvent.setStartElapsedTime(eventChro.duringTime());
				currentEvent.setChro_state(EventModel.STATE_PAUSE);
				
				//ˢ���б�
				recorderEventListAdapter.notifyDataSetChanged();
			}
		});
		
		//chronometer stop
		stopBut.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (eventChro.duringTime() > 0) {
					DialogFragment newFragment = MyAlertDialogFragment.newInstance(
				            R.string.recorder_page_stop, R.drawable.ic_my_alert, getEventMsg(currentEvent), new StopOkListener(), new NormalCancelListener());
				    newFragment.show(getFragmentManager(), "dialog");
				}
			}
		});
		
		//�б����¼�
		eventList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				currentEvent.setSummary(eventSummary.getText().toString());
				
				new Thread(new ControlPanleRunnable(eventModels.get(arg2))).start();
				currentEvent = eventModels.get(arg2);
			}
		});
		
		//�б����˵�
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
		
		if (eventModels == null) {
			eventModels = new ArrayList<EventModel>();
			EventModel eventModel = new EventModel();
			
			eventModel = new EventModel();
			eventModel.setEventName("Eat abs sdf jiewijfd");
			eventModel.setEventCategoryName("rest");
			eventModels.add(eventModel);
			
			eventModel = new EventModel();
			eventModel.setEventName("Eatf dfewfweesdf");
			eventModel.setEventCategoryName("rest");
			eventModels.add(eventModel);
			
			eventModel = new EventModel();
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
			
		} 
		
		//init component
		new Thread(new ControlPanleRunnable(currentEvent)).start();
		
		//init listView
		if (recorderEventListAdapter == null)
			recorderEventListAdapter = new RecorderEventListAdapter(this.getActivity(), eventModels);
		eventList.setAdapter(recorderEventListAdapter);
		
		
	}
	
	public Notification getMyRecorderNotification () {
		int startTotle = getEventsStatus(eventModels).get(EventModel.STATE_START);
		int pauseTotle = getEventsStatus(eventModels).get(EventModel.STATE_PAUSE);
		
		if (startTotle + pauseTotle > 0) {
			Map<String, Object> extras = new HashMap<String, Object>();
			extras.put("EVENT_MODELS", eventModels);
			extras.put("CURRENT_EVENT", currentEvent);
			
			StringBuilder content = new StringBuilder();
			if (startTotle > 0) {
				if (startTotle == 1) {
					content.append(startTotle + " ").append(getActivity().getResources().getString(R.string.event_is_timed)).append("\r\n");
				} else {
					content.append(startTotle + " ").append(getActivity().getResources().getString(R.string.events_are_timed)).append("\r\n");
				}
				
			}
			if (pauseTotle > 0) {
				if (pauseTotle == 1) {
					content.append(pauseTotle + " ").append(getActivity().getResources().getString(R.string.event_has_been_suspended)).append("\r\n");
				} else {
					content.append(pauseTotle + " ").append(getActivity().getResources().getString(R.string.events_have_been_suspended)).append("\r\n");
				}
				
			}
			
			return new NotificationUtils().buildNotification(getActivity(),
					RecordActivity.class, extras, R.drawable.ic_launcher,
					getActivity().getResources().getString(R.string.app_name),
					content.toString());
		}
		
		return null;
		
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putParcelableArrayList("EVENT_MODELS", eventModels);
		outState.putParcelable("CURRENT_EVENT", currentEvent);
	}
	
	public HashMap<String, Integer> getEventsStatus (ArrayList<EventModel> eventModels) {
		HashMap<String, Integer> resultMap = new HashMap<String, Integer>();
		resultMap.put(EventModel.STATE_START, -1);
		resultMap.put(EventModel.STATE_PAUSE, -1);
		resultMap.put(EventModel.STATE_STOP, -1);
		if (eventModels == null || eventModels.size() <= 0) return resultMap;
		
		for (EventModel eventModel : eventModels) {
			if (EventModel.STATE_START.equals(eventModel.getChro_state())) {
				resultMap.put(EventModel.STATE_START, resultMap.get(EventModel.STATE_START) + 1);
			} else if (EventModel.STATE_PAUSE.equals(eventModel.getChro_state())) {
				resultMap.put(EventModel.STATE_PAUSE, resultMap.get(EventModel.STATE_PAUSE) + 1);
			} else if (EventModel.STATE_STOP.equals(eventModel.getChro_state())) {
				resultMap.put(EventModel.STATE_STOP, resultMap.get(EventModel.STATE_STOP) + 1);
			}
		}
		
		return resultMap;
	}
	
	public String getEventMsg (EventModel eventModel) {
		StringBuilder result = new StringBuilder();
		result.append(getActivity().getResources().getString(R.string.event_name) + " : ").append(eventModel.getEventName()).append("\r\n");
		result.append(getActivity().getResources().getString(R.string.event_category) + " : ").append(eventModel.getEventCategoryName()).append("\r\n");
		result.append(getActivity().getResources().getString(R.string.event_date) + " : ").append(DateUtils.format(new Date(), DateUtils.DEFAULT_DATE_PATTERN)).append("\r\n");
		result.append(getActivity().getResources().getString(R.string.using_time) + " : ").append(DateUtils.formatAdjust(eventChro.duringTime())).append("\r\n");
		result.append(getActivity().getResources().getString(R.string.summary) + " : ").append(eventSummary.getText().toString()).append("\r\n");
		return result.toString();
	}
	
	
	class ControlPanleRunnable implements Runnable {
		private EventModel newModel;
		
		public ControlPanleRunnable (EventModel newModel) {
			this.newModel = newModel;
		}
		
		@Override
		public void run() {
			//����������
			Message msg = controlPanelHandler.obtainMessage();
			Bundle date=new Bundle();
			date.putParcelable("EVENT_MODEL", newModel);
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
			
			//���� �������ؼ�
			Recorder.this.eventName.setText(eventModel.getEventName());
			Recorder.this.eventCategory.setText(eventModel.getEventCategoryName());
			Recorder.this.eventSummary.setText(eventModel.getSummary());
			
			//init chro
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
	
	class ResetOkListener implements DialogInterface.OnClickListener {
		@Override
		public void onClick(DialogInterface dialog, int which) {
			//
			startBut.setVisibility(View.VISIBLE);
			pauseBut.setVisibility(View.GONE);
			eventChro.reset();
			
			//��ʼ�� event model
			currentEvent.setStartElapsedTime(0);
			currentEvent.setChro_state(EventModel.STATE_STOP);
			currentEvent.setStartTime(null);
			
			//ˢ���б�
			recorderEventListAdapter.notifyDataSetChanged();
		}
	}
	
	class StopOkListener implements DialogInterface.OnClickListener {
		@Override
		public void onClick(DialogInterface dialog, int which) {
			//
			eventChro.stop();
			startBut.setVisibility(View.VISIBLE);
			pauseBut.setVisibility(View.GONE);
			
			//�������ݽ� t_event_reords
			EventRecordModel eventRecordModel = new EventRecordModel();
			eventRecordModel.setEventName(currentEvent.getEventName());
			eventRecordModel.setEventCategoryName(currentEvent.getEventCategoryName());
			eventRecordModel.setUseingTime(eventChro.duringTime());
			eventRecordModel.setSummary(eventSummary.getText().toString());
			try {
				eventRecordModel.setEventDate(DateUtils.getDateWithoutTime(new Date()));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			
			new EventRecordsDAO(Recorder.this.getActivity()).insert(eventRecordModel);
			
			//��ʼ�� ���¼� event model
			currentEvent.setStartElapsedTime(0);
			currentEvent.setChro_state(EventModel.STATE_STOP);
			currentEvent.setStartTime(null);
			
			eventChro.reset();
			
			//ˢ���б�
			recorderEventListAdapter.notifyDataSetChanged();
		}
	}
	
	class NormalCancelListener implements DialogInterface.OnClickListener {
		@Override
		public void onClick(DialogInterface dialog, int which) {
			
		}
	}
}
