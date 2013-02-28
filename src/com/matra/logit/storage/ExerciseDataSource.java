package com.matra.logit.storage;

import java.sql.SQLXML;
import java.util.ArrayList;
import java.util.List;

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
	}
	
	public ArrayList<Exercise> getAllExercises()
	{
		ArrayList<Exercise> exercises = new ArrayList<Exercise>();
		Cursor cursor = database.query(SqliteHelper.TABLE_EXERCISES_NAME, allEXXTableColumns, null, null, null, null, null);
		cursor.moveToFirst();
		while(!cursor.isAfterLast())
		{
			Exercise ex = cursorToExercise(cursor);
			exercises.add(ex);
			cursor.moveToNext();
		}
		cursor.close();
		return exercises;
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
	
}
