package com.matra.logit.storage;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SqliteHelper extends SQLiteOpenHelper
{
	public static final String DATABASE_NAME = "logit.db";
	public static final int DATABASE_VERSION = 2; //
	
	public static final String TABLE_EXERCISES_NAME = "exercises";
	public static final String EXX_COLUMN_ID = "_id";
	public static final String EXX_COLUMN_EXNAME = "_name";
	public static final String EXX_COLUMN_EXDESC = "_desc";
	//MORE : (damn, send somewhere? read on it)
	
	public static final String CREATE_EXERCISES_TABLE = "create table " + TABLE_EXERCISES_NAME 
			+ "(" + EXX_COLUMN_ID + " integer primary key autoincrement, " 
			+ EXX_COLUMN_EXNAME + " text not null, " 
			+ EXX_COLUMN_EXDESC + " text not null"
			+ ");" ;
	
	public static final String TABLE_METRICS_NAME = "metrics";
	public static final String MXX_COLUMN_ID = "_id";
	public static final String MXX_COLUMN_OWNER_ID = "_fkID";
	public static final String MXX_COLUMN_NAME = "_name";
	public static final String MXX_COLUMN_VALUE = "_value";
	
	public static final String CREATE_METRICS_TABLE = "create table " + TABLE_METRICS_NAME
			+ "(" + MXX_COLUMN_ID + " integer primary key autoincrement, "
			+ MXX_COLUMN_OWNER_ID + " integer not null, "
			+ MXX_COLUMN_NAME + " text not null, "
			+ MXX_COLUMN_VALUE + " integer not null"
			+ ");" ;
	
	public SqliteHelper(Context context)
	{
		super(context,DATABASE_NAME,null,DATABASE_VERSION);
	}
	
	public void onCreate(SQLiteDatabase database)
	{
		database.execSQL(CREATE_EXERCISES_TABLE);
		database.execSQL(CREATE_METRICS_TABLE);
	}
	
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
		Log.w(SqliteHelper.class.getName(),"Upgrading database, data will be erased");
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXERCISES_NAME);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_METRICS_NAME);
		onCreate(db);
	}
	

}

