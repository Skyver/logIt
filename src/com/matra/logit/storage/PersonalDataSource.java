package com.matra.logit.storage;

import java.security.acl.Owner;
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
	
	private String[] allPMXXTableColumns = 
		{SqliteHelper.PMXX_COLUMN_ID
			, SqliteHelper.PMXX_COLUMN_OWNER_ID
			, SqliteHelper.PMXX_COLUMN_NAME
			, SqliteHelper.PMXX_COLUMN_VALUE
			, SqliteHelper.PMXX_COLUMN_TREND
			, SqliteHelper.PMXX_COLUMN_DATE
		};
	
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
			item.addAllMetrics(getAllOwnedMetrics(item.getId()));
			itemList.add(item);
			cursor.moveToNext();
		}
		cursor.close();
		return itemList;
	}
	
	public ArrayList<Metric> getAllOwnedMetrics(long ownerID)
	{
		ArrayList<Metric> metrics = new ArrayList<Metric>();
		String whereClause = SqliteHelper.PMXX_COLUMN_OWNER_ID + " = " + ownerID;
		Cursor cursor = database.query(SqliteHelper.TABLE_PMETRICS_NAME, allPMXXTableColumns, whereClause, null, null, null, null);
		cursor.moveToFirst();
		while(!cursor.isAfterLast())
		{
			Metric mt = cursorToMetric(cursor);
			metrics.add(mt);
			cursor.moveToNext();
		}
		cursor.close();
		return metrics;
	}
	
	public long addMetricToDataItem(Metric metric)
	{
		ContentValues values = new ContentValues();
		values.put(SqliteHelper.PMXX_COLUMN_OWNER_ID, metric.getOwnerId());
		values.put(SqliteHelper.PMXX_COLUMN_NAME, metric.getName());
		values.put(SqliteHelper.MXX_COLUMN_VALUE, metric.getValue());
		values.put(SqliteHelper.PMXX_COLUMN_TREND, metric.getTrend());
		values.put(SqliteHelper.PMXX_COLUMN_DATE, ExerciseDataSource.getTodayDate());
		long id = database.insert(SqliteHelper.TABLE_PMETRICS_NAME, null, values);
		return id;
	}
	
	public void removeMetricFromDataItem(Metric metric)
	{
		database.delete(SqliteHelper.TABLE_PMETRICS_NAME, SqliteHelper.PMXX_COLUMN_ID + " = " + metric.getId(), null);
	}
	
	public void updateMetricValue(long metricId, int metricValue, String trend)
	{
		ContentValues values = new ContentValues();
		values.put(SqliteHelper.PMXX_COLUMN_VALUE, metricValue);
		values.put(SqliteHelper.PMXX_COLUMN_TREND, trend);
		values.put(SqliteHelper.PMXX_COLUMN_DATE, ExerciseDataSource.getTodayDate());
		database.update(SqliteHelper.TABLE_PMETRICS_NAME, values, SqliteHelper.PMXX_COLUMN_ID + " = " + metricId, null);
	}
	public void deleteDataItem(DataItem item)
	{
		long id = item.getId();
		database.delete(SqliteHelper.TABLE_PERSONAL_NAME, SqliteHelper.PXX_COLUMN_ID + " = " + id, null);
		database.delete(SqliteHelper.TABLE_PMETRICS_NAME, SqliteHelper.PMXX_COLUMN_OWNER_ID + " = " + id, null);
	}
	
	private Metric cursorToMetric(Cursor cursor)
	{
		long id = cursor.getLong(0);
		long ownerID = cursor.getLong(1);
		String name = cursor.getString(2);
		int value = cursor.getInt(3);
		String trend = cursor.getString(4);
		String date = cursor.getString(5);
		Metric mt = new Metric(ownerID, name, value);
		mt.setId(id);
		mt.setTrend(trend);
		mt.setFormattedDate(date);
		return mt;
	}

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
