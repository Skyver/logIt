package com.matra.logit;

import com.matra.logit.interopServices.PersonalInfoManager;
import com.matra.logit.storage.DataItem;

import android.app.Activity;
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

public class PersonalListFragment extends ListFragment {
	
	
	public static final String FIRST_TIME_PERSONAL = "FIRST_TIME_PER";
	public static final int ADD_REQUEST_CODE = 1;
	
	private int activeIndex;
	private PersonalInfoManager infoManager;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		infoManager = new PersonalInfoManager(this.getActivity());
		activeIndex = -1;
		checkFirstTime();
		setHasOptionsMenu(true);
		
		setListAdapter(new ArrayAdapter<DataItem>(this.getActivity(), 
				android.R.layout.simple_list_item_activated_1, 
				infoManager.getData()));
		System.out.println(infoManager.getData().size()); 
	}
	
	@Override
    public void onListItemClick(ListView l, View v, int position, long id)
	{
		activeIndex = position;
		DataItem item = (DataItem) l.getItemAtPosition(position);
		if(getActivity().getFragmentManager().findFragmentByTag(LogIt.ID_PERSONALDETAIL) == null)
		{
			PersonalDetailFragment fragment = new PersonalDetailFragment();
			getActivity().getFragmentManager().beginTransaction()
				.add(R.id.containerDetails, fragment, LogIt.ID_PERSONALDETAIL).show(fragment).commit();
			fragment.setDisplayedData(item);
			fragment.setInfoManager(infoManager);
		}
		else
		{
			PersonalDetailFragment fragment = 
					(PersonalDetailFragment) getActivity().getFragmentManager().findFragmentByTag(LogIt.ID_PERSONALDETAIL);
			fragment.setDisplayedData(item);
			fragment.setInfoManager(infoManager);
			getActivity().getFragmentManager().beginTransaction().show(fragment).commit();
		}
	}
	
	@Override
    public boolean onOptionsItemSelected(MenuItem item)
	{
		switch(item.getItemId())
		{
		case R.id.menu_personal_add:
			Intent intent = new Intent(getActivity(), NewDetail.class);
			startActivityForResult(intent, ADD_REQUEST_CODE);
			return true;
		case R.id.menu_personal_delete:
			DataItem target = (DataItem) getListAdapter().getItem(activeIndex);
			infoManager.removeDataItem(target);
			ArrayAdapter<DataItem> adapter = (ArrayAdapter<DataItem>) this.getListAdapter();
			adapter.remove(target);
			PersonalDetailFragment fragment = (PersonalDetailFragment) 
					getActivity().getFragmentManager().findFragmentByTag(LogIt.ID_PERSONALDETAIL);
			getActivity().getFragmentManager().beginTransaction().hide(fragment).commit();
			return true;
		default:
			return super.onOptionsItemSelected(item);		
		}
		//TODO implement delete case
		
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.personal_list_menu, menu);
    }
	
	public void checkFirstTime()
	{
		SharedPreferences settings = this.getActivity().getSharedPreferences(ExerciseListFragment.PREFS_NAME, 0);
		boolean first_time = settings.getBoolean(FIRST_TIME_PERSONAL, true);
		if(first_time)
		{
			infoManager.generateInitData();
			SharedPreferences.Editor editor = settings.edit();
			editor.putBoolean(FIRST_TIME_PERSONAL, false);
			editor.commit();
			
		}
	}
	
    @Override
	public void onResume()
    {
    	infoManager.signalResume(); 
    	super.onResume();
    }
    
    @Override
	public void onPause()
    {
    	infoManager.signalPause();
    	super.onPause();
    }
    
    @Override
    public void onActivityResult (int requestCode, int resultCode, Intent data)
    {
    	infoManager.signalResume();
    	if(resultCode == Activity.RESULT_OK && requestCode == ADD_REQUEST_CODE)
    	{
    		String dtName = data.getStringExtra(NewDetail.RETURN_NAME);
    		String dtDesc = data.getStringExtra(NewDetail.RETURN_DESC);
    		infoManager.addNewDataItem(dtName, dtDesc);
    	}
    	super.onActivityResult(requestCode, resultCode, data);
    }
    
    
	
}


