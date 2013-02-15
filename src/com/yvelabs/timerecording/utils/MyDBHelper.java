package com.yvelabs.timerecording.utils;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDBHelper extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "time_recording.db";
	private static final int DATABASE_VERSION = 1;

	public MyDBHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	public MyDBHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
	}

	public MyDBHelper(Context context, String name, CursorFactory factory,
			int version, DatabaseErrorHandler errorHandler) {
		super(context, name, factory, version, errorHandler);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		//t_event_records
		StringBuilder eventRecordsTableSql = new StringBuilder();
		eventRecordsTableSql.append("CREATE TABLE IF NOT EXISTS t_event_records ");
		eventRecordsTableSql.append("(_id INTEGER PRIMARY KEY AUTOINCREMENT, ");
		eventRecordsTableSql.append("event_name VARCHAR(50), ");
		eventRecordsTableSql.append("event_category_name VARCHAR(30), ");
		eventRecordsTableSql.append("event_date NUMERIC,");
		eventRecordsTableSql.append("useing_time NUMERIC,");
		eventRecordsTableSql.append("summary VARCHAR(500),");
		eventRecordsTableSql.append("create_time NUMERIC");
		eventRecordsTableSql.append(")");
		db.execSQL(eventRecordsTableSql.toString());
		
		//t_event
		StringBuilder eventTableSql = new StringBuilder();
		eventTableSql.append("CREATE TABLE IF NOT EXISTS t_event");
		eventTableSql.append("(_id INTEGER PRIMARY KEY AUTOINCREMENT, ");
		eventTableSql.append("event_name VARCHAR(50), ");
		eventTableSql.append("event_category_name VARCHAR(30), ");
		eventTableSql.append("status VARCHAR(5),");
		eventTableSql.append("event_order INTEGER");
		eventTableSql.append(")");
		db.execSQL(eventTableSql.toString());
		
		//t_event_category
		StringBuilder eventCategoryTableSql = new StringBuilder();
		eventCategoryTableSql.append("CREATE TABLE IF NOT EXISTS t_event_category");
		eventCategoryTableSql.append("(_id INTEGER PRIMARY KEY AUTOINCREMENT, ");
		eventCategoryTableSql.append("event_category_name VARCHAR(30), ");
		eventCategoryTableSql.append("status VARCHAR(5)");
		eventCategoryTableSql.append(")");
		db.execSQL(eventCategoryTableSql.toString());
		
		//t_event_status
		StringBuilder eventStatusTableSql = new StringBuilder();
		eventStatusTableSql.append(" CREATE TABLE IF NOT EXISTS t_event_status ( ");
		eventStatusTableSql.append("   _id INTEGER PRIMARY KEY AUTOINCREMENT , ");
		eventStatusTableSql.append("   event_id INTEGER , ");
		eventStatusTableSql.append("   event_name VARCHAR , ");
		eventStatusTableSql.append("   event_category_name VARCHAR , ");
		eventStatusTableSql.append("   chro_state VARCHAR , ");
		eventStatusTableSql.append("   start_time INTEGER , ");
		eventStatusTableSql.append("   start_elapsed_time INTEGER , ");
		eventStatusTableSql.append("   summary VARCHAR,  ");
		eventStatusTableSql.append("   create_on INTEGER ");
		eventStatusTableSql.append(" ) ");
		db.execSQL(eventStatusTableSql.toString());
		
		initData(db);
	}
	
	private void initData (SQLiteDatabase db) {
		String sql = "insert into t_event_category (event_category_name, status) values ('Work', '1')";
		db.execSQL(sql);
		
		sql = "insert into t_event_category (event_category_name, status) values ('Other', '1')";
		db.execSQL(sql);
		
		sql = "insert into t_event (event_name, event_category_name, status, event_order) values ('Project 1', 'Work', '1', 3)";
		db.execSQL(sql);
		
		sql = "insert into t_event (event_name, event_category_name, status, event_order) values ('Project 2', 'Work', '1', 3)";
		db.execSQL(sql);
		
		sql = "insert into t_event (event_name, event_category_name, status, event_order) values ('Watch TV', 'Other', '1', 3)";
		db.execSQL(sql);
		
		sql = "insert into t_event (event_name, event_category_name, status, event_order) values ('Movies', 'Other', '1', 3)";
		db.execSQL(sql);
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		
	}

}
