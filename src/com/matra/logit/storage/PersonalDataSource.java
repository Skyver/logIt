package com.matra.logit.storage;

import java.sql.SQLException;
import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class PersonalDataSource 
{
	private SQLiteDatabase database;
	private SqliteHelper helper;
	private String[] allPXXTableColumns = 
		{SqliteHelper.PXX_COLUMN_ID
			, SqliteHelper.PXX_COLUMN_NAME 
			, SqliteHelper.PXX_COLUMN_DESC};
	
	public PersonalDataSource(Context context)
	{
		helper = new SqliteHelper(context);
	}
	
	public void open() throws SQLException
	{
		database = helper.getWritableDatabase();
	}
	
	public void close()
	{
		database.close();
	}
	
	public long addDataItem(DataItem item)
	{
		ContentValues values = new ContentValues();
		values.put(SqliteHelper.PXX_COLUMN_NAME, item.getName());
		values.put(SqliteHelper.PXX_COLUMN_DESC, item.getDescription());
		long id  = database.insert(SqliteHelper.TABLE_PERSONAL_NAME, null, values);
		return id;
	}
	
	public ArrayList<DataItem> getAllDataItems()
	{
		ArrayList<DataItem> itemList = new ArrayList<DataItem>();
		Cursor cursor = database.query(SqliteHelper.TABLE_PERSONAL_NAME, allPXXTableColumns, null, null, null, null, null);
		cursor.moveToFirst();
		while(!cursor.isAfterLast())
		{
			DataItem item = cursorToDataItem(cursor);
			itemList.add(item);
			cursor.moveToNext();
		}
		cursor.close();
		return itemList;
	}
	
	//public void deleteDataItem() TODO
	
	

	public DataItem cursorToDataItem(Cursor cursor)
	{
		long id = cursor.getLong(0);
		String name = cursor.getString(1);
		String desc = cursor.getString(2);
		DataItem item = new DataItem(name, desc);
		item.setId(id);
		return item;
	}
}
