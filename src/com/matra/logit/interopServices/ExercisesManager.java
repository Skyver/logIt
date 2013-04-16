package com.matra.logit.interopServices;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;

import com.matra.logit.storage.Exercise;
import com.matra.logit.storage.ExerciseDataSource;
import com.matra.logit.storage.Metric;
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
		long dpID = exerciseDatasource.addExercise(defaultPushup);
		long rID = exerciseDatasource.addExercise(defaultRun);
		long ddID = exerciseDatasource.addExercise(defaultDeadlift);
		long bpID = exerciseDatasource.addExercise(defaultBenchpress);
		//---
		Metric dpReps = new Metric(dpID, "Repetitions", 15);
		defaultPushup.addMetric(dpReps);
		exerciseDatasource.addMetricToExercise(dpReps);
		Metric rMeters = new Metric(rID, "Meters ran", 500);
		defaultRun.addMetric(rMeters);
		exerciseDatasource.addMetricToExercise(rMeters);
		Metric ddKilos = new Metric(ddID, "Kilograms", 100);
		defaultDeadlift.addMetric(ddKilos);
		exerciseDatasource.addMetricToExercise(ddKilos);
		Metric bpKilos = new Metric(bpID, "Kilograms", 100);
		defaultBenchpress.addMetric(bpKilos);
		exerciseDatasource.addMetricToExercise(bpKilos);
		
		//First run likely found an empty DB, update the list on control
		cacheStorage = exerciseDatasource.getAllExercises();

	}

	public Metric addMetric(String name, int initialValue, long ownerID)
	{
		Metric newMetric = new Metric(ownerID, name, initialValue);
		long id = exerciseDatasource.addMetricToExercise(newMetric);
		newMetric.setId(id);
		
		for (Exercise ex : cacheStorage) {
			if(ex.getId() == ownerID)
			{
				ex.addMetric(newMetric);
			}
		}
		
		return newMetric;
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
	
	public void removeMetric(Metric metric)
	{
		exerciseDatasource.removeMetricFromExercise(metric);
		for(Exercise ex: cacheStorage)
		{
			if(ex.getId() == metric.getOwnerId())
			{
				ex.removeMetric(metric);
			}
		}
		
	}
	
	public void updateMetric(long metricID, int newValue)
	{
		if(metricID != -1)
		{
			//First, fetch the old value
			int oldValue = 0;
			outloop: //Labeling the outer loop
			for(Exercise ex: cacheStorage)
			{
				for(Metric mt: ex.getMetricList())
				{
					if(mt.getId() == metricID)
					{
						oldValue = mt.getValue();
						break outloop;
					}
				}
			}
			
			exerciseDatasource.updateMetricValue(metricID, newValue, getTrend(oldValue, newValue));
			cacheStorage = exerciseDatasource.getAllExercises();
		}
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

	public static String getTrend(int old, int newValue)
	{
		if(old > newValue)
		{
			return Metric.TREND_DOWN;
		}
		else if (old < newValue)
		{
			return Metric.TREND_UP;
		}
		else
		{
			return Metric.TREND_EQUAL;
		}
	}
	
}
