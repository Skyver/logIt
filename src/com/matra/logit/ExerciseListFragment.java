package com.matra.logit;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ListFragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;


import com.matra.logit.interopServices.ExercisesManager;
import com.matra.logit.storage.Exercise;

public class ExerciseListFragment extends ListFragment{
	
	public static final String PREFS_NAME = "SETTINGS";
	public static final String FIRST_TIME = "FIRST_TIME";
	
	public static final int ADD_REQUEST_CODE = 1;
	
	
	private ExercisesManager exerciseManager;
	private int activeIndex;
	
	@Override
	public void onCreate (Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		exerciseManager = new ExercisesManager(this.getActivity());
		activeIndex = -1;
		checkFirstTime();
		setHasOptionsMenu(true);
		
		setListAdapter(new ArrayAdapter<Exercise>(this.getActivity(),
				android.R.layout.simple_list_item_activated_1, 
				exerciseManager.getExercises()));
	}
	

	
    @Override
    public void onListItemClick(ListView l, View v, int position, long id)
    {
    	activeIndex = position;
    	Exercise exercise = (Exercise) l.getItemAtPosition(position);
    	Fragment frg = getActivity().getFragmentManager().findFragmentByTag(LogIt.ID_EXERCISENOTES);
    	if( frg != null)
    	{
    		getActivity().getFragmentManager().beginTransaction().setCustomAnimations(R.animator.slide_in_left, R.animator.slide_out_right).remove(frg).commit();
    	}    	
    	if(getActivity().getFragmentManager().findFragmentByTag(LogIt.ID_EXERCISEDETAIL) == null)
    	{
    		ExerciseDetailFragment fragment = new ExerciseDetailFragment();
    		getActivity().getFragmentManager().beginTransaction()
    					.add(R.id.containerDetails,fragment,LogIt.ID_EXERCISEDETAIL).show(fragment).commit();
    		fragment.setDisplayedExercise(exercise);
    		fragment.setExerciseManager(exerciseManager);
    	}
    	else
    	{
    		ExerciseDetailFragment fragment = (ExerciseDetailFragment) getActivity().getFragmentManager()
    																.findFragmentByTag(LogIt.ID_EXERCISEDETAIL);
    		fragment.setDisplayedExercise(exercise);
    		fragment.setExerciseManager(exerciseManager);
    		getActivity().getFragmentManager().popBackStackImmediate();
    		FragmentTransaction transaction = getActivity().getFragmentManager().beginTransaction();
    		transaction.setCustomAnimations(R.animator.slide_in_left, R.animator.slide_out_right);
    		if(fragment.isRemoving())
    		{
    			transaction.add(fragment, LogIt.ID_EXERCISEDETAIL).show(fragment).commit();
    		}
    		else
    		{
    			transaction.show(fragment).commit();
    		}
    		
    		
    		
    		
    	}
    	
    }
    
    private void checkFirstTime()
    {
    	SharedPreferences settings = this.getActivity().getSharedPreferences(PREFS_NAME, 0);
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
	    		Intent intent = new Intent(getActivity(), NewExercise.class);
	    		//getSherlockActivity().startActivity(intent);
	    		startActivityForResult(intent, ADD_REQUEST_CODE);
	    		return true;
	    	case R.id.menu_delete :
	    		Exercise target = (Exercise) getListAdapter().getItem(activeIndex);
	    		exerciseManager.removeExercise(target);
	    		//Remove from UI list
	    		ArrayAdapter<Exercise> adapter = (ArrayAdapter<Exercise>) this.getListAdapter();
	    		adapter.remove(target);
	    		//Hide fragment for cleanliness and better UX
	    		ExerciseDetailFragment fragment = (ExerciseDetailFragment) getActivity().getFragmentManager()
						.findFragmentByTag(LogIt.ID_EXERCISEDETAIL);
	    		getActivity().getFragmentManager().beginTransaction().hide(fragment).commit();
	    	case R.id.menu_personal_notes:
	    		Exercise active = (Exercise) getListAdapter().getItem(activeIndex);
	    		//Toast.makeText(getActivity(), String.valueOf(active.getId()), Toast.LENGTH_LONG).show();
	    		NotesFragment notesFragment = new NotesFragment();
	    		notesFragment.setNotesOwner(active);
	    		FragmentTransaction transaction = getFragmentManager().beginTransaction();
	    		transaction.setCustomAnimations(R.animator.slide_in_left, R.animator.slide_out_right);
	    		transaction.replace(R.id.containerDetails, notesFragment, LogIt.ID_EXERCISENOTES);
	    		transaction.addToBackStack(null);
	    		transaction.commit();
	    		
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

    	}

    	super.onActivityResult(requestCode, resultCode, data);
    }
	
}
