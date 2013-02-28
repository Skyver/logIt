package com.matra.logit.interopServices;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;

import com.matra.logit.storage.Exercise;
import com.matra.logit.storage.ExerciseDataSource;
import com.matra.logit.storage.MockStorageService;

public class ExercisesManager {
	
	private ExerciseDataSource exerciseDatasource;
	private ArrayList<Exercise> cacheStorage;
	
	public ExercisesManager(Context context)
	{
		exerciseDatasource = new ExerciseDataSource(context);
		exerciseDatasource.open();
		cacheStorage = exerciseDatasource.getAllExercises();
		//---
	}
	
	public ArrayList<Exercise> getExercises()
	{
		return cacheStorage;
	}
	
	public Exercise getExerciseDetail(String exerciseID)
	{
		for(Exercise ex:cacheStorage)
		{
			if(ex.getName().equals(exerciseID))
			{
				return ex;
			}
		}
		
		return null;
	}
	
	public void generateInitExercies()
	{
		//Default exercises to be stored in database. This exercises can be deleted by the user 
		Exercise defaultPushup = new Exercise("Pushup", "A push-up is a common calisthenics exercise performed in a prone position by raising and lowering the body using the arms");
		Exercise defaultRun = new Exercise("Jogging", "Jogging is a form of trotting or running at a slow or leisurely pace. The main intention is to increase physical fitness with less stress on the body than from faster running.");
		Exercise defaultDeadlift = new Exercise("Deadlift", "The deadlift is a weight training exercise where a loaded barbell is lifted off the ground from a stabilized, bent over position.");
		Exercise defaultBenchpress = new Exercise("Bench Press", "The bench press is an exercise of the upper body. While on their back, the person performing the bench press lowers a weight to the level of the chest, then pushes it back up until the arm is straight.");
		//---
		exerciseDatasource.addExercise(defaultPushup);
		exerciseDatasource.addExercise(defaultRun);
		exerciseDatasource.addExercise(defaultDeadlift);
		exerciseDatasource.addExercise(defaultBenchpress);
		
		//First run likely found an empty DB, update the list on control
		cacheStorage = exerciseDatasource.getAllExercises();
		//TODO
	}
	
	public Exercise addNewExercise(String name, String desc)
	{
		Exercise newExercise = new Exercise(name, desc);
		long id = exerciseDatasource.addExercise(newExercise);
		newExercise.setId(id);
		cacheStorage.add(newExercise);
		return newExercise;
	}
	
	public void removeExercise(Exercise exercise)
	{
		cacheStorage.remove(exercise);
		exerciseDatasource.deleteExercise(exercise);
	}
	
	//signals the bottom layer that the app is on resumed state
	public void signalResume()
	{
		exerciseDatasource.open();
	}
	
	//signals the bottom layer that the app is on paused state
	public void signalPause()
	{
		exerciseDatasource.close();
	}

}
