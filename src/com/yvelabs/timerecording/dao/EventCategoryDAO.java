package com.yvelabs.timerecording.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.yvelabs.timerecording.EventCategoryModel;
import com.yvelabs.timerecording.EventModel;
import com.yvelabs.timerecording.EventRecordModel;
import com.yvelabs.timerecording.utils.MyDBHelper;

public class EventCategoryDAO {
	
	private static final String EVENT_CAGETORY_INSERT = " insert into t_event_category (event_category_name, status) values (?, ?) ";
	private static final String EVENT_CATEGORY_SELECT_ID = " select last_insert_rowid() from t_event_category ";
	private static final String EVENT_CATEGORY_DELETE = " delete from t_event_category where 1 = 1 ";
	private static final String EVENT_CATEGORY_SELECT = " select _id, event_category_name, status from t_event_category where 1 = 1 ";
	private static final String EVENT_CATEGORY_UPDATE_BY_CATEGORYNAME = "update t_event_category set event_category_name = ? where lower(event_category_name) = ?";
	private static final String UPDATE_STATUS_BY_ID = "update t_event_category set status = ? where _id = ?";

	
	private Context context;
	
	public EventCategoryDAO (Context context) {
		this.context = context;
	}

	/**
	 * 插入单条， 返回id
	 * @param categoryModel
	 * @return
	 */
	public Integer insertReturnId(EventCategoryModel categoryModel) {
		List<EventCategoryModel> insertModels = new ArrayList<EventCategoryModel>();
		insertModels.add(categoryModel);
		
		List<Integer> resultId = insertReturnId(insertModels);
		return resultId.get(0);
	}
	
	/**
	 * 插入单条
	 * @param categoryModel
	 */
	public void insert (EventCategoryModel categoryModel) {
		List<EventCategoryModel> insertModels = new ArrayList<EventCategoryModel>();
		insertModels.add(categoryModel);
		
		insert(insertModels);
	}
	
	/**
	 * 插入多条, 返回ID List
	 * @param categoryModels
	 * @return
	 */
	public List<Integer> insertReturnId(List<EventCategoryModel> categoryModels) {
		List<Integer> resultIds = new ArrayList<Integer>();
		SQLiteDatabase db = new MyDBHelper(context).getWritableDatabase();
		try {
			db.beginTransaction();
			for (EventCategoryModel categoryModel : categoryModels) {
				db.execSQL(EVENT_CAGETORY_INSERT, new Object[]{categoryModel.getEventCategoryName(), categoryModel.getStatus()});
				Cursor cursor = db.rawQuery(EVENT_CATEGORY_SELECT_ID,null);
				if(cursor.moveToFirst())
					resultIds.add(cursor.getInt(0));
			}
			db.setTransactionSuccessful();
		} finally {
			if (db != null){
				db.endTransaction();
				db.close();
			}
		}
		
		return resultIds;
	}
	
	/**
	 * 插入多条
	 * @param categoryModels
	 */
	public void insert (List<EventCategoryModel> categoryModels) {
		SQLiteDatabase db = new MyDBHelper(context).getWritableDatabase();
		try {
			db.beginTransaction();
			for (EventCategoryModel categoryModel : categoryModels) {
				db.execSQL(EVENT_CAGETORY_INSERT, new Object[]{categoryModel.getEventCategoryName(), categoryModel.getStatus()});
			}
			db.setTransactionSuccessful();
		} finally {
			if (db != null) {
				db.endTransaction();
				db.close();
			}
		}
	}
	
	public void delete (EventCategoryModel categoryModel) {
		List<EventCategoryModel> categoryModels = new ArrayList<EventCategoryModel>();
		categoryModels.add(categoryModel);
		delete(categoryModels);
	}
	
	/**
	 * 删除
	 * @param categoryModels
	 */
	public void delete (List<EventCategoryModel> categoryModels) {
		SQLiteDatabase db = new MyDBHelper(context).getWritableDatabase();
		try {
			db.beginTransaction();
			for (EventCategoryModel categoryModel : categoryModels) {
				StringBuilder sql = new StringBuilder(EVENT_CATEGORY_DELETE);
				List<String> paraList = new ArrayList<String>();
				
				if (categoryModel.getEventCategoryId() >= 0) {
					sql.append(" and _id = ? ");
					paraList.add(String.valueOf(categoryModel.getEventCategoryId()));
				}
				if (categoryModel.getEventCategoryName() != null && categoryModel.getEventCategoryName().length() > 0) {
					sql.append(" and lower(event_category_name) = ? ");
					paraList.add(categoryModel.getEventCategoryName().toLowerCase());
				}
				if (categoryModel.getStatus() != null && categoryModel.getStatus().length() > 0) {
					sql.append(" and status = ? ");
					paraList.add(categoryModel.getStatus());
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
			
			db.setTransactionSuccessful();
		} finally {
			if (db != null) {
				db.endTransaction();
				db.close();
			}
		}
	}
	
	public void updateAllTableByCategoryName (EventCategoryModel oldModel, EventCategoryModel newModel) {
		SQLiteDatabase db = new MyDBHelper(context).getWritableDatabase();
		try {
			db.beginTransaction();
			//t_event_category
			updateByCategoryName(db, oldModel, newModel);
			
			//t_event
			EventModel oldEventModel = new EventModel();
			oldEventModel.setEventCategoryName(oldModel.getEventCategoryName());
			EventModel newEventModle = new EventModel();
			newEventModle.setEventCategoryName(newModel.getEventCategoryName());
			new EventDAO(context).updateByCategoryName(db, oldEventModel, newEventModle);
			
			//t_event_records
			EventRecordModel oldRecordModel = new EventRecordModel();
			oldRecordModel.setEventCategoryName(oldModel.getEventCategoryName());
			EventRecordModel newRecordModel = new EventRecordModel();
			newRecordModel.setEventCategoryName(newModel.getEventCategoryName());
			new EventRecordsDAO(context).updateByCategoryName(db, oldRecordModel, newRecordModel);
			
			//t_event_status
			new EventStatusDAO(context).updateByCategoryName(db, oldEventModel, newEventModle);
			
			db.setTransactionSuccessful();
		} finally {
			if (db != null) {
				db.endTransaction();
				db.close();
			}
		}
	}
	
	public void updateByCategoryName (SQLiteDatabase db, EventCategoryModel oldModel, EventCategoryModel newModel) {
		db.execSQL(EVENT_CATEGORY_UPDATE_BY_CATEGORYNAME, new Object[]{newModel.getEventCategoryName(), oldModel.getEventCategoryName().toLowerCase()});
	}
	
	public void updateStatusById (EventCategoryModel parameter) {
		SQLiteDatabase db = new MyDBHelper(context).getWritableDatabase();
		try {
			db.beginTransaction();
			db.execSQL(UPDATE_STATUS_BY_ID, new Object[] {parameter.getStatus(), parameter.getEventCategoryId()});
			db.setTransactionSuccessful();
		} finally {
			if (db != null) {
				db.endTransaction();
				db.close();
			}
		}
	}
	
	/**
	 * 查询
	 * @param parameter
	 * @return
	 */
	public List<EventCategoryModel> query (EventCategoryModel parameter) {
		List<EventCategoryModel> eventCategorys = new ArrayList<EventCategoryModel>();
		Cursor c = null;
		SQLiteDatabase db = new MyDBHelper(context).getWritableDatabase();
		
		try {
			//构造查询sql
			StringBuilder sql = new StringBuilder(EVENT_CATEGORY_SELECT);
			List<String> paraList = new ArrayList<String>();
			
			if (parameter.getEventCategoryId() >= 0) {
				sql.append(" and _id = ? ");
				paraList.add(String.valueOf(parameter.getEventCategoryId()));
			}
			if (parameter.getEventCategoryName() != null && parameter.getEventCategoryName().length() > 0) {
				sql.append(" and lower(event_category_name) = ? ");
				paraList.add(parameter.getEventCategoryName().toLowerCase());
			}
			if (parameter.getStatus() != null && parameter.getStatus().length() > 0) {
				sql.append(" and status = ? ");
				paraList.add(parameter.getStatus());
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
			EventCategoryModel eventCategory = null;
			while (c.moveToNext()) {
				eventCategory = new EventCategoryModel();
				eventCategory.setEventCategoryId(c.getInt(c.getColumnIndex("_id")));
				eventCategory.setEventCategoryName(c.getString(c.getColumnIndex("event_category_name")));
				eventCategory.setStatus(c.getString(c.getColumnIndex("status")));
				
				eventCategorys.add(eventCategory);
			}
		} finally {
			if (c != null) c.close();
			if (db != null) {
				db.close();
			}
		}
		
		return eventCategorys;
	}
	

}
