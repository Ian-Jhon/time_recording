package com.yvelabs.timerecording.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.yvelabs.timerecording.EventCategoryModel;
import com.yvelabs.timerecording.utils.LogUtils;
import com.yvelabs.timerecording.utils.MyDBHelper;

public class EventCategoryDAO {
	
	private static final String EVENT_CAGETORY_INSERT = " insert into t_event_category (event_category, status) values (?, ?) ";
	private static final String EVENT_CATEGORY_SELECT_ID = " select last_insert_rowid() from t_event_category ";
	private static final String EVENT_CATEGORY_DELETE = " delete from t_event_category where 1 = 1 ";
	private static final String EVENT_CATEGORY_SELECT = " select _id, event_category, status from t_event_category where 1 = 1 ";
	//	private static final String EVENT_RECORDS_SELECT = "select _id, event_name, event_category, event_date, useing_time, summary, create_time from t_event_records where 1 = 1";

	
	private SQLiteDatabase db;
	
	public EventCategoryDAO (Context context) {
		db = new MyDBHelper(context).getWritableDatabase();
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
		
		LogUtils.d(this.getClass(), categoryModel + "");
		
		insert(insertModels);
	}
	
	/**
	 * 插入多条, 返回ID List
	 * @param categoryModels
	 * @return
	 */
	public List<Integer> insertReturnId(List<EventCategoryModel> categoryModels) {
		List<Integer> resultIds = new ArrayList<Integer>();

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
	
	/**
	 * 删除
	 * @param categoryModels
	 */
	public void delete (List<EventCategoryModel> categoryModels) {
		
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
					sql.append(" and event_category = ? ");
					paraList.add(categoryModel.getEventCategoryName());
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
	
	/**
	 * 查询
	 * @param parameter
	 * @return
	 */
	public List<EventCategoryModel> query (EventCategoryModel parameter) {
		List<EventCategoryModel> eventCategorys = new ArrayList<EventCategoryModel>();
		Cursor c = null;
		try {
			//构造查询sql
			StringBuilder sql = new StringBuilder(EVENT_CATEGORY_SELECT);
			List<String> paraList = new ArrayList<String>();
			
			if (parameter.getEventCategoryId() >= 0) {
				sql.append(" and _id = ? ");
				paraList.add(String.valueOf(parameter.getEventCategoryId()));
			}
			if (parameter.getEventCategoryName() != null && parameter.getEventCategoryName().length() > 0) {
				sql.append(" and event_category = ? ");
				paraList.add(parameter.getEventCategoryName());
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
				eventCategory.setEventCategoryName(c.getString(c.getColumnIndex("event_category")));
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
