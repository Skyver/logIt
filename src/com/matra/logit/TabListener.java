package com.matra.logit;

import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.Fragment;
import android.app.ActionBar;
import android.app.FragmentTransaction;


public class TabListener<T extends Fragment> implements ActionBar.TabListener
{

    private Fragment currentFragment;
    private final Activity activity;
    private final String fragTag;
    private final Class<T> fragClass;
	
	public TabListener(Activity activity, String tag, Class<T> clz) {
		this.activity = activity;
		fragTag = tag;
		fragClass = clz;
  	}
	
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
	   
    	currentFragment = activity.getFragmentManager().findFragmentByTag(fragTag);		
		if (currentFragment == null) {
		   currentFragment = Fragment.instantiate(activity, fragClass.getName());
	       ft.add(R.id.listContainer, currentFragment, fragTag);
		} else {
	       ft.attach(currentFragment);	       
		}
		//Clear the second one
	    Fragment rightFragment = activity.getFragmentManager().findFragmentById(R.id.containerDetails);
	    if(rightFragment != null)
	    {
	    	ft.remove(rightFragment);
	    }
		
	}
   
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		if (currentFragment != null) {
			ft.detach(currentFragment);
		}
	}
	
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
	 // User selected the already selected tab. Usually do nothing.
	}
}