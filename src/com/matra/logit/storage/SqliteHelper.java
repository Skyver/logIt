package com.matra.logit.storage;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SqliteHelper extends SQLiteOpenHelper
{
	public static final String TABLE_EXERCISES_NAME = "exercises";
	public static final String EXX_COLUMN_ID = "_id";
	public static final String EXX_COLUMN_EXNAME = "_name";
	public static final String EXX_COLUMN_EXDESC = "_desc";
	//MORE : (damn, send somewhere? read on it)
	
	public static final String DATABASE_NAME = "logit.db";
	public static final int DATABASE_VERSION = 1;
	
	public static final String CREATE_EXERCISES_TABLE = "create table " + TABLE_EXERCISES_NAME 
			+ "(" + EXX_COLUMN_ID + " integer primary key autoincrement, " 
			+ EXX_COLUMN_EXNAME + " text not null, " 
			+ EXX_COLUMN_EXDESC + " text not null"
			+");";
	
	public SqliteHelper(Context context)
	{
		super(context,DATABASE_NAME,null,DATABASE_VERSION);
	}
	
	public void onCreate(SQLiteDatabase database)
	{
		database.execSQL(CREATE_EXERCISES_TABLE);
		//database.execSQL(here!);
	}
	
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
		Log.w(SqliteHelper.class.getName(),"Upgrading database, data will be erased");
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXERCISES_NAME);
		//db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXERCISES_NAME);
		onCreate(db);
	}
	

}

//TODO : Same for more tables, but this is not hard
