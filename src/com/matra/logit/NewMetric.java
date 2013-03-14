package com.matra.logit;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

public class NewMetric extends Activity {

	public static final String RETURN_NAME = "return_name";
	public static final String RETURN_VALUE = "return value";
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_metric);
    }

    public void onSubmitClick(View view)
    {
    	EditText name = (EditText) findViewById(R.id.newmetric_name);
    	EditText value = (EditText) findViewById(R.id.newmetric_value);
    	
    	Intent data = new Intent();
    	data.putExtra(RETURN_NAME, name.getText().toString());
    	data.putExtra(RETURN_VALUE, Integer.parseInt(value.getText().toString()));
    	
    	this.setResult(RESULT_OK,data);
    	this.finish();
    }
}
