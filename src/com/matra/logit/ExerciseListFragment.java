package com.matra.logit;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockListFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.matra.logit.interopServices.ExercisesManager;
import com.matra.logit.storage.Exercise;

public class ExerciseListFragment extends SherlockListFragment{
	
	public static final String PREFS_NAME = "SETTINGS";
	public static final String FIRST_TIME = "FIRST_TIME";
	
	public static final int ADD_REQUEST_CODE = 1;
	
	
	private ExercisesManager exerciseManager;
	private int activeIndex;
	
	@Override
	public void onCreate (Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		exerciseManager = new ExercisesManager(this.getSherlockActivity());
		activeIndex = -1;
		checkFirstTime();
		setHasOptionsMenu(true);
		
		setListAdapter(new ArrayAdapter<Exercise>(this.getSherlockActivity(),
				android.R.layout.simple_list_item_activated_1, 
				exerciseManager.getExercises()));
	}
	

	
    @Override
    public void onListItemClick(ListView l, View v, int position, long id)
    {
    	activeIndex = position;
    	Exercise exercise = (Exercise) l.getItemAtPosition(position);
    	if(getSherlockActivity().getSupportFragmentManager().findFragmentByTag(LogIt.ID_EXERCISEDETAIL) == null)
    	{
    		ExerciseDetailFragment fragment = new ExerciseDetailFragment();
    		getSherlockActivity().getSupportFragmentManager().beginTransaction()
    					.add(R.id.containerDetails,fragment,LogIt.ID_EXERCISEDETAIL).show(fragment).commit();
    		fragment.setDisplayedExercise(exercise);
    	}
    	else
    	{
    		ExerciseDetailFragment fragment = (ExerciseDetailFragment) getSherlockActivity().getSupportFragmentManager()
    																.findFragmentByTag(LogIt.ID_EXERCISEDETAIL);
    		fragment.setDisplayedExercise(exercise);
    		getSherlockActivity().getSupportFragmentManager().beginTransaction().show(fragment).commit();
    	}
    	
    }
    
    private void checkFirstTime()
    {
    	SharedPreferences settings = this.getSherlockActivity().getSharedPreferences(PREFS_NAME, 0);
    	boolean first_time = settings.getBoolean(FIRST_TIME, true);
    	if(first_time)
    	{
    		//Initialize all things needed at first run
    		exerciseManager.generateInitExercies();
    		//TODO : Other functions
    		//Mark first time settings as ran
    		SharedPreferences.Editor editor = settings.edit();
    		editor.putBoolean(FIRST_TIME, false);
    		editor.commit();
    	}
    	

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
    	switch(item.getItemId())
    	{
	    	case R.id.menu_add :
	    		//Toast.makeText(this.getActivity(), "ADD", Toast.LENGTH_SHORT).show();
	    		Intent intent = new Intent(getSherlockActivity(), NewExercise.class);
	    		//getSherlockActivity().startActivity(intent);
	    		startActivityForResult(intent, ADD_REQUEST_CODE);
	    		return true;
	    	case R.id.menu_delete :
	    		//Toast.makeText(this.getActivity(), "DEL at " + activeIndex, Toast.LENGTH_SHORT).show();
	    		Exercise target = (Exercise) getListAdapter().getItem(activeIndex);
	    		exerciseManager.removeExercise(target);
	    		//Remove from UI list
	    		ArrayAdapter<Exercise> adapter = (ArrayAdapter<Exercise>) this.getListAdapter();
	    		adapter.remove(target);
	    		//Hide fragment for cleanliness and better UX
	    		ExerciseDetailFragment fragment = (ExerciseDetailFragment) getSherlockActivity().getSupportFragmentManager()
						.findFragmentByTag(LogIt.ID_EXERCISEDETAIL);
	    		getSherlockActivity().getSupportFragmentManager().beginTransaction().hide(fragment).commit();
	    		
	    		
	    		return true;
	    	default :
	    		return super.onOptionsItemSelected(item);
    	}
    }
    
    @Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
    	super.onCreateOptionsMenu(menu, inflater);
    	inflater.inflate(R.menu.exercise_list_menu, menu);
    }
    
    @Override
	public void onResume()
    {
    	exerciseManager.signalResume();
    	super.onResume();
    }
    
    @Override
	public void onPause()
    {
    	exerciseManager.signalPause();
    	super.onPause();
    }
	
    @Override
    public void onActivityResult (int requestCode, int resultCode, Intent data)
    {
    	//Before anything, this could be ran before onResume, so signal the database to open
    	exerciseManager.signalResume();
    	if(requestCode == ADD_REQUEST_CODE && resultCode == Activity.RESULT_OK)
    	{
    		//1. Get the exercise data from intent. As it expands, more will follow.
    		String exName = data.getStringExtra(NewExercise.RETURN_NAME);
    		String exDesc = data.getStringExtra(NewExercise.RETURN_DESC);
    		//2. Add to database
    		//System.out.println(exName + "  " + exDesc);
    		Exercise newEx = exerciseManager.addNewExercise(exName, exDesc);
    		//3. Update adapter with name is ran on the OnCreate Method


    	}

    	super.onActivityResult(requestCode, resultCode, data);
    }
	
}
