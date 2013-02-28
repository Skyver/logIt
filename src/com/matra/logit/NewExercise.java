package com.matra.logit;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


public class NewExercise extends Activity {

	public static final String RETURN_NAME = "RETURN_NAME";
	public static final String RETURN_DESC = "RETURN_DESC";
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_exercise);
        
    }
    
    public void onSubmitClick(View view)
    {
    	EditText name = (EditText) this.findViewById(R.id.textfield_name);
    	EditText desc = (EditText) this.findViewById(R.id.textfield_description);
    	
    	//TODO : input control
    	
    	Intent data = new Intent();
    	data.putExtra(RETURN_NAME, name.getText().toString());
    	data.putExtra(RETURN_DESC, desc.getText().toString());
    	
    	this.setResult(RESULT_OK, data);
    	this.finish();
    }

    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_new_exercise, menu);
        return true;
    }
    */
}
