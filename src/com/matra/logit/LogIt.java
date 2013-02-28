package com.matra.logit;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;



public class LogIt extends SherlockFragmentActivity{

	public static final String ID_EXERCISELIST = "EXERCISELIST";
	public static final String ID_EXERCISEDETAIL = "EXERCISEDETAIL";
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_it);
       
        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getSupportMenuInflater().inflate(R.menu.activity_log_it, menu);
        return true;
    }
    
}
