package com.matra.logit;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ListView;



public class LogIt extends Activity{

	public static final String ID_EXERCISELIST = "EXERCISELIST";
	public static final String ID_EXERCISEDETAIL = "EXERCISEDETAIL";
	public static final String ID_PERSONALDETAIL = "PERSONALDETAIL";
	public static final String ID_EXERCISENOTES = "NOTESFRAG";
	
	public static final String TAG_EXERCISES = "EXERCISES";
	public static final String TAG_PERSONAL = "PERSONAL";
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_it);
       
        ActionBar actionBar = this.getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        actionBar.setDisplayShowTitleEnabled(false);
        
        Tab navTab = actionBar.newTab();
        navTab.setText("Exercises").setTabListener(new TabListener<ExerciseListFragment>(this, TAG_EXERCISES, ExerciseListFragment.class));
        //TODO change it to string resource
        actionBar.addTab(navTab);
        
        navTab = actionBar.newTab();
        navTab.setText("Personal").setTabListener(new TabListener<PersonalListFragment>(this, TAG_PERSONAL, PersonalListFragment.class));
        actionBar.addTab(navTab);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_log_it, menu);
        return true;
    }
    

    
}
