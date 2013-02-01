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
	private static final String INSERT = " insert into t_event_status (event_id, event_name, event_category_name, chro_state, start_time, start_elapsed_time, summary, create_on) values (?,?,?,?,?,?,?,?) ";
	private static final String DELETE = " delete from t_event_status where 1 = 1 ";
	private static final String SELECT_ALL = " select _id, event_id, event_name, event_category_name, chro_state, start_time, start_elapsed_time, summary, create_on from t_event_status order by create_on";
	private static final String UPDATE_BY_CATEGORYNAME = "update t_event_status set event_category_name = ? where lower(event_category_name) = ?";
	
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
		deleteSql.append(" and lower(event_name) = ? ");
		deleteSql.append(" and lower(event_category_name) = ? ");
		db.execSQL(deleteSql.toString(), new Object[] { model.getEventName().toLowerCase(), model.getEventCategoryName().toLowerCase() });
	}
	
	public void updateByCategoryName (SQLiteDatabase db, EventModel oldModel, EventModel newModel) {
		db.execSQL(UPDATE_BY_CATEGORYNAME, new Object[]{newModel.getEventCategoryName(), oldModel.getEventCategoryName().toLowerCase()});
	}
	
	public void update (SQLiteDatabase db, EventModel parameterModel, EventModel newModel) {
		StringBuilder sql = new StringBuilder(" update t_event_status set _id = _id ");
		List<String> paraList = new ArrayList<String>();
		
		if (newModel.getEventName() != null && newModel.getEventName().length() > 0) {
			sql.append(" , event_name = ? ");
			paraList.add( newModel.getEventName());
		}
		if (newModel.getEventCategoryName() != null && newModel.getEventCategoryName().length() > 0) {
			sql.append(" , event_category_name = ? ");
			paraList.add(newModel.getEventCategoryName());
		}
		sql.append(" where 1 = 1 ");
		
		if (parameterModel.getEventName() != null && parameterModel.getEventName().length() > 0) {
			sql.append(" and lower(event_name) = ? ");
			paraList.add(parameterModel.getEventName().toLowerCase());
		}
		if (parameterModel.getEventCategoryName() != null && parameterModel.getEventName().length() > 0) {
			sql.append(" and lower(event_category_name) = ? ");
			paraList.add(parameterModel.getEventCategoryName().toLowerCase());
		}
		
		if (paraList.size() <= 0) {
			db.execSQL(sql.toString(), null);
		} else {
			String [] paraArray = new String[paraList.size()];
			for (int i = 0 ; i < paraArray.length ; i ++) {
				paraArray[i] = paraList.get(i);
			}
			db.execSQL(sql.toString(), paraArray);
		}
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
			eventModel.setEventCategoryName(c.getString(c.getColumnIndex("event_category_name")));
			eventModel.setChro_state(c.getString(c.getColumnIndex("chro_state")));
			eventModel.setStartTime(new Date(c.getLong(c.getColumnIndex("start_time"))));
			eventModel.setStartElapsedTime(c.getLong(c.getColumnIndex("start_elapsed_time")));
			eventModel.setSummary(c.getString(c.getColumnIndex("summary")));
			
			resultList.add(eventModel);
		}
		
		return resultList;
	}

}
