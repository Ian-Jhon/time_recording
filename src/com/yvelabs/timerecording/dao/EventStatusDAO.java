package com.yvelabs.timerecording.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.yvelabs.timerecording.EventModel;
import com.yvelabs.timerecording.utils.LogUtils;
import com.yvelabs.timerecording.utils.MyDBHelper;

public class EventStatusDAO {
	private static final String INSERT = " insert into t_event_status (event_id, event_name, even_category_name, chro_state, start_time, start_elapsed_time, summary, create_on) values (?,?,?,?,?,?,?,?) ";
	private static final String DELETE = " delete from t_event_status where 1 = 1 ";
	private static final String SELECT_ALL = " select _id, event_id, event_name, even_category_name, chro_state, start_time, start_elapsed_time, summary, create_on from t_event_status order by create_on";
	
	private Context context;

	public EventStatusDAO(Context context) {
		this.context = context;
	}
	
	public void deleteNInert (EventModel model ) {
		SQLiteDatabase db = new MyDBHelper(context).getWritableDatabase();
		
		try {
			db.beginTransaction();
			deleteByEvent(model, db);
			insert(model, db);
			db.setTransactionSuccessful();
		} finally {
			if (db != null) {
				db.endTransaction();
				db.close();
			}
		}
	}
	
	public void deleteNInert (List<EventModel> modelList) {
		SQLiteDatabase db = new MyDBHelper(context).getWritableDatabase();
		
		try {
			db.beginTransaction();
			
			for (EventModel model : modelList) {
				deleteByEvent(model, db);
				insert(model, db);
			}
			db.setTransactionSuccessful();
		} finally {
			if (db != null) {
				db.endTransaction();
				db.close();
			}
		}
	}
	
	public void insert (EventModel model, SQLiteDatabase db) {
		Long startTime = model.getStartTime() == null ? null : model.getStartTime().getTime();
		Long startETime = model.getStartElapsedTime() == 0 ? null : model.getStartElapsedTime(); 
		Long current = new Date().getTime();
		
		db.execSQL(INSERT, new Object[] {model.getEventId(), model.getEventName(), model.getEventCategoryName(), model.getChro_state(), startTime, startETime, model.getSummary(), current});
	}
	
	public void deleteByEvent (EventModel model) {
		SQLiteDatabase db = new MyDBHelper(context).getWritableDatabase();
		try {
			db.beginTransaction();
			deleteByEvent(model, db);
			db.setTransactionSuccessful();
		} finally {
			if (db != null) {
				db.endTransaction();
				db.close();
			}
		}
	}
	
	public void deleteByEvent (EventModel model, SQLiteDatabase db) {
		StringBuilder deleteSql = new StringBuilder(DELETE);
		deleteSql.append(" and event_name = ? ");
		deleteSql.append(" and even_category_name = ? ");
		db.execSQL(deleteSql.toString(), new Object[] { model.getEventName(), model.getEventCategoryName() });
	}
	
	public List<EventModel> selectAll () {
		List<EventModel> resultList = new ArrayList<EventModel>();
		EventModel eventModel = null;
		SQLiteDatabase db = new MyDBHelper(context).getWritableDatabase();
		Cursor c = db.rawQuery(SELECT_ALL, null);
		
		while (c.moveToNext()) {
			eventModel = new EventModel();
			eventModel.setEventId(c.getInt(c.getColumnIndex("event_id")));
			eventModel.setEventName(c.getString(c.getColumnIndex("event_name")));
			eventModel.setEventCategoryName(c.getString(c.getColumnIndex("even_category_name")));
			eventModel.setChro_state(c.getString(c.getColumnIndex("chro_state")));
			eventModel.setStartTime(new Date(c.getLong(c.getColumnIndex("start_time"))));
			eventModel.setStartElapsedTime(c.getLong(c.getColumnIndex("start_elapsed_time")));
			eventModel.setSummary(c.getString(c.getColumnIndex("summary")));
			
			resultList.add(eventModel);
		}
		
		return resultList;
	}

}
