package com.matra.logit;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

public class NewDetail extends Activity {

	public static final String RETURN_NAME = "RETURN_NAME";
	public static final String RETURN_DESC = "RETURN_DESC";
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_detail);
    }

    public void onSubmitClick(View view)
    {
    	EditText name = (EditText) findViewById(R.id.detail_textfield_name);
    	EditText desc = (EditText) findViewById(R.id.detail_textfield_desc);
    	//TODO input control?
    	Intent data = new Intent();
    	data.putExtra(RETURN_NAME, name.getText().toString());
    	data.putExtra(RETURN_DESC, desc.getText().toString());
    	setResult(RESULT_OK, data);
    	finish();
    	
    }
    
}
