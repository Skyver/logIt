package com.matra.logit.storage;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class ExerciseDataSource 
{
	private SQLiteDatabase database;
	private SqliteHelper dbHelper;
	private String[] allEXXTableColumns = 
		{SqliteHelper.EXX_COLUMN_ID
			, SqliteHelper.EXX_COLUMN_EXNAME 
			, SqliteHelper.EXX_COLUMN_EXDESC};
	
	private String[] allMXXTableColumns = 
		{SqliteHelper.MXX_COLUMN_ID
			, SqliteHelper.MXX_COLUMN_OWNER_ID
			, SqliteHelper.MXX_COLUMN_NAME
			, SqliteHelper.MXX_COLUMN_VALUE
			, SqliteHelper.MXX_COLUMN_TREND
			, SqliteHelper.MXX_COLUMN_DATE
		};
	
	public ExerciseDataSource(Context context)
	{
		dbHelper = new SqliteHelper(context);
	}
	
	public void open() throws SQLException
	{
		database = dbHelper.getWritableDatabase();
	}
	
	public void close()
	{
		database.close();
	}
	
	public long addExercise(Exercise exercise)
	{
		ContentValues values = new ContentValues();
		values.put(SqliteHelper.EXX_COLUMN_EXNAME, exercise.getName());
		values.put(SqliteHelper.EXX_COLUMN_EXDESC, exercise.getDescription());
		long id = database.insert(SqliteHelper.TABLE_EXERCISES_NAME, null, values);
		return id;
	}
	
	public void deleteExercise(Exercise exercise)
	{
		long id = exercise.getId();
		System.out.println("Comment deleted with id: " + id);
		database.delete(SqliteHelper.TABLE_EXERCISES_NAME, SqliteHelper.EXX_COLUMN_ID + " = " + id, null);
		database.delete(SqliteHelper.TABLE_METRICS_NAME, SqliteHelper.MXX_COLUMN_OWNER_ID + " = " + id, null); 
		//Metrics belonging to the deleted exercise should not remain
	}
	
	public ArrayList<Exercise> getAllExercises()
	{
		ArrayList<Exercise> exercises = new ArrayList<Exercise>();
		Cursor cursor = database.query(SqliteHelper.TABLE_EXERCISES_NAME, allEXXTableColumns, null, null, null, null, null);
		cursor.moveToFirst();
		while(!cursor.isAfterLast())
		{
			Exercise ex = cursorToExercise(cursor);
			ex.addAllMetrics(getAllOwnedMetrics(ex.getId()));
			exercises.add(ex);
			cursor.moveToNext();
		}
		cursor.close();
		return exercises;
	} 
	
	public ArrayList<Metric> getAllOwnedMetrics(long ownerID)
	{
		ArrayList<Metric> metrics = new ArrayList<Metric>();
		String whereClause = SqliteHelper.MXX_COLUMN_OWNER_ID + " = " + ownerID;
		Cursor cursor = database.query(SqliteHelper.TABLE_METRICS_NAME, allMXXTableColumns, whereClause, null, null, null, null);
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

	public long addMetricToExercise(Metric metric)
	{
		ContentValues values = new ContentValues();
		values.put(SqliteHelper.MXX_COLUMN_OWNER_ID, metric.getOwnerId());
		values.put(SqliteHelper.MXX_COLUMN_NAME, metric.getName());
		values.put(SqliteHelper.MXX_COLUMN_VALUE, metric.getValue());
		values.put(SqliteHelper.MXX_COLUMN_TREND, metric.getTrend());
		values.put(SqliteHelper.MXX_COLUMN_DATE, getTodayDate());
		//
		long id = database.insert(SqliteHelper.TABLE_METRICS_NAME, null, values);
		return id;
	}

	
	public void removeMetricFromExercise(Metric metric)
	{
		System.out.println("Metric " + metric.getId()+ " deleted from " + metric.getOwnerId());
		database.delete(SqliteHelper.TABLE_METRICS_NAME, SqliteHelper.MXX_COLUMN_ID + " = " + metric.getId(), null);
	}
	
	public void updateMetricValue(long metricID, int metricValue, String trend)
	{
		ContentValues values = new ContentValues();
		values.put(SqliteHelper.MXX_COLUMN_VALUE, metricValue);
		values.put(SqliteHelper.MXX_COLUMN_TREND, trend);
		values.put(SqliteHelper.MXX_COLUMN_DATE, getTodayDate());
		int clx = database.update(SqliteHelper.TABLE_METRICS_NAME, values, SqliteHelper.MXX_COLUMN_ID + " = " + metricID, null);
		System.out.println(clx + " rows were updated for the metric " + metricID);
	}
	
	private Exercise cursorToExercise(Cursor cursor)
	{
		String name = cursor.getString(1);
		String description = cursor.getString(2);
		long id = cursor.getLong(0);
		Exercise ex = new Exercise(name, description);
		ex.setId(id);
		return ex;
		
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
	
	public static String getTodayDate()
	{
		Calendar date = Calendar.getInstance();
		String formatted = date.get(Calendar.YEAR) + "-" + (date.get(Calendar.MONTH) + 1 )+ "-" + date.get(Calendar.DAY_OF_MONTH);
		return formatted;
	}
	
}
