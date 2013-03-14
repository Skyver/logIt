package com.matra.logit;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ListView;



public class LogIt extends Activity{

	public static final String ID_EXERCISELIST = "EXERCISELIST";
	public static final String ID_EXERCISEDETAIL = "EXERCISEDETAIL";
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_it);
       
        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_log_it, menu);
        return true;
    }
    
}
