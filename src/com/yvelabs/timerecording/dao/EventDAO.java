package com.yvelabs.timerecording.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.yvelabs.timerecording.EventModel;
import com.yvelabs.timerecording.utils.LogUtils;
import com.yvelabs.timerecording.utils.MyDBHelper;

public class EventDAO {
	private static final String INSERT = " insert into t_event (event_name, event_category_name, status, event_order) values (?, ?, ?, ?) ";
	private static final String DELETE = " delete from t_event where 1 = 1 ";
	private static final String SELECT = " select _id, event_name, event_category_name, status, event_order from t_event where 1 = 1 ";
	private static final String UPDATE_BY_CATEGORYNAME = "update t_event set event_category_name = ? where lower(event_category_name) = ?";
	
	private Context context;
	
	public EventDAO (Context context) {
		this.context = context;
	}
	
	public void insert (EventModel eventModel) {
		List<EventModel> eventModels = new ArrayList<EventModel>();
		eventModels.add(eventModel);
		insert(eventModels);
	}
	
	public void insert (List<EventModel> eventModels) {
		SQLiteDatabase db = new MyDBHelper(context).getWritableDatabase();
		try {
			db.beginTransaction();
			for (EventModel eventModel : eventModels) {
				db.execSQL(INSERT, new Object[]{eventModel.getEventName(), eventModel.getEventCategoryName(), eventModel.getStatus(), eventModel.getOrder()});
			}
			db.setTransactionSuccessful();
		} finally {
			if (db != null) {
				db.endTransaction();
				db.close();
			}
		}
	}
	
	public void delete (EventModel eventModel) {
		List<EventModel> eventModels = new ArrayList<EventModel>();
		eventModels.add(eventModel);
		delete(eventModels);
	}
	
	public int delete (List<EventModel> eventModels) {
		SQLiteDatabase db = new MyDBHelper(context).getWritableDatabase();
		int count = 0;
		try {
			db.beginTransaction();
			
			for (EventModel eventModel : eventModels) {
				StringBuilder sql = new StringBuilder(DELETE);
				List<String> paraList = new ArrayList<String>();
				
				if (eventModel.getEventId() >= 0) {
					sql.append(" and _id = ? ");
					paraList.add(String.valueOf(eventModel.getEventId()));
				}
				if (eventModel.getEventName() != null && eventModel.getEventName().length() > 0) {
					sql.append(" and lower(event_name) = ? ");
					paraList.add(eventModel.getEventName().toLowerCase());
				}
				if (eventModel.getEventCategoryName() != null && eventModel.getEventCategoryName().length() > 0) {
					sql.append(" and lower(event_category_name) = ? ");
					paraList.add(eventModel.getEventCategoryName().toLowerCase());
				}
				if (eventModel.getStatus() != null && eventModel.getStatus().length() > 0) {
					sql.append(" and status = ? ");
					paraList.add(eventModel.getStatus());
				}
				if (eventModel.getOrder() > 0) {
					sql.append(" and event_order = ? ");
					paraList.add(String.valueOf(eventModel.getOrder()));
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
				
				count++;
			}
			
			db.setTransactionSuccessful();
			
			return count;
		} finally {
			if (db != null) {
				db.endTransaction();
				db.close();
			}
		}
	}
	
	public void updateByCategoryName (SQLiteDatabase db, EventModel parameterModel, EventModel newModel) {
		db.execSQL(UPDATE_BY_CATEGORYNAME, new Object[]{newModel.getEventCategoryName(), parameterModel.getEventCategoryName().toLowerCase()});
	}
	
	public void updateAllTable(EventModel parameterModel, EventModel newModel) {
		SQLiteDatabase db = new MyDBHelper(context).getWritableDatabase();
		try {
			db.beginTransaction();
			update(db, parameterModel, newModel);
			
			db.setTransactionSuccessful();
		} finally {
			if (db != null) {
				db.endTransaction();
				db.close();
			}
		}
	}
	
	public void update (SQLiteDatabase db, EventModel parameterModel, EventModel newModel) {
		StringBuilder sql = new StringBuilder(" update t_event set _id = _id ");
		List<String> paraList = new ArrayList<String>();
		
		if (newModel.getEventName() != null && newModel.getEventName().length() > 0) {
			sql.append(", event_name = ? ");
			paraList.add(newModel.getEventName());
		}
		if (newModel.getEventCategoryName() != null && newModel.getEventCategoryName().length() > 0) {
			sql.append(", event_category_name = ? ");
			paraList.add(newModel.getEventCategoryName());
		}
		if (newModel.getStatus() != null && newModel.getStatus().length() > 0) {
			sql.append(", status = ? ");
			paraList.add(newModel.getStatus());
		}
		if (newModel.getOrder() > 0) {
			sql.append(", event_order = ? ");
			paraList.add(newModel.getOrder() + "");
		}
		
		sql.append(" where 1 = 1 ");
		
		if (parameterModel.getEventName() != null && parameterModel.getEventName().length() > 0) {
			sql.append(" and lower(event_name) = ? ");
			paraList.add(parameterModel.getEventName().toLowerCase());
		}
		if (parameterModel.getEventCategoryName() != null && parameterModel.getEventCategoryName().length() > 0) {
			sql.append(" and lower(event_category_name) = ? ");
			paraList.add(parameterModel.getEventCategoryName().toLowerCase());
		}
		if (parameterModel.getStatus() != null && parameterModel.getStatus().length() > 0) {
			sql.append(" and status = ? ");
			paraList.add(parameterModel.getStatus());
		}
		if (parameterModel.getOrder() > 0) {
			sql.append(" and event_order = ? ");
			paraList.add(parameterModel.getOrder() + "");
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
	
	public List<EventModel> query (EventModel parameter) {
		List<EventModel> resultList = new ArrayList<EventModel>();
		Cursor c = null;
		SQLiteDatabase db = new MyDBHelper(context).getWritableDatabase();
		try {
			StringBuilder sql = new StringBuilder(SELECT);
			List<String> paraList = new ArrayList<String>();
			
			if (parameter.getEventId() >= 0) {
				sql.append(" and _id = ? ");
				paraList.add(String.valueOf(parameter.getEventId()));
			}
			if (parameter.getEventName() != null && parameter.getEventName().length() > 0) {
				sql.append(" and lower(event_name) = ? ");
				paraList.add(parameter.getEventName().toLowerCase());
			}
			if (parameter.getEventCategoryName() != null && parameter.getEventCategoryName().length() > 0) {
				sql.append(" and lower(event_category_name) = ? ");
				paraList.add(parameter.getEventCategoryName().toLowerCase());
			}
			if (parameter.getStatus() != null && parameter.getStatus().length() > 0) {
				sql.append(" and status = ? ");
				paraList.add(parameter.getStatus());
			}
			if (parameter.getOrder() > 0) {
				sql.append(" and event_order = ? ");
				paraList.add(String.valueOf(parameter.getOrder()));
			}
			
			if (paraList.size() <= 0) {
				c = db.rawQuery(sql.toString(), null);
			} else {
				String [] paraArray = new String[paraList.size()];
				for (int i = 0 ; i < paraArray.length ; i ++) {
					paraArray[i] = paraList.get(i);
				}
				c = db.rawQuery(sql.toString(), paraArray);
			}
			
			//封装结果集
			while (c.moveToNext()) {
				EventModel eventModel = new EventModel();
				eventModel.setEventId(c.getInt(c.getColumnIndex("_id")));
				eventModel.setEventName(c.getString(c.getColumnIndex("event_name")));
				eventModel.setEventCategoryName(c.getString(c.getColumnIndex("event_category_name")));
				eventModel.setStatus(c.getString(c.getColumnIndex("status")));
				eventModel.setOrder(c.getInt(c.getColumnIndex("event_order")));
				
				resultList.add(eventModel);
			}
		} finally {
			if (c != null) c.close();
			if (db != null) db.close();
		}
		
		return resultList;
	}

}
