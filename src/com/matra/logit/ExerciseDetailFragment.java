package com.matra.logit;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.matra.logit.storage.Exercise;

public class ExerciseDetailFragment extends SherlockFragment 
{
	TextView title;
	TextView description;
	Exercise displayedExercise; 
	
	@Override
	public void onCreate (Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View result = inflater.inflate(R.layout.fragment_exercise_detail, container, false);
		title = (TextView) result.findViewById(R.id.detailTitle);
		description = (TextView) result.findViewById(R.id.detailDescription);
		return result;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		if(displayedExercise != null) //this is on rotating display, state loss?
		{
			title.setText(displayedExercise.getName());
			description.setText(displayedExercise.getDescription());
		}
	}
	

    @Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
    	super.onCreateOptionsMenu(menu, inflater);
    	inflater.inflate(R.menu.exercise_detail_menu, menu);
    }
	
	
	//TODO : Single refresh method to take care of displaying
	public void setDisplayedExercise(Exercise exercise) //
	{
		
		if(title != null) //If title is null, so is any other View
		{
			title.setText(exercise.getName());
			description.setText(exercise.getDescription());
		}
		displayedExercise = exercise;
	}
	
	
}
