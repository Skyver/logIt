package com.matra.logit;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.NumberPicker;

public class UpdateMetric extends Activity {

	public static final String MDATA_ID = "metric-id";
	public static final String MDATA_VALUE = "metric-val";
	
	NumberPicker picker;
	int currentValue;
	long metricID;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_metric);
        Intent intent = getIntent();
        currentValue = intent.getIntExtra(MDATA_VALUE, 0);
        metricID = intent.getLongExtra(MDATA_ID, -1);
        
        picker = (NumberPicker) findViewById(R.id.updatePicker);
        picker.setMinValue(currentValue / 2);
        picker.setMaxValue(currentValue * 2 + 10);
        picker.setValue(currentValue);
        picker.setWrapSelectorWheel(false);
    }
    
    public void onUpdateClick(View view)
    {
    	Intent data = new Intent();
    	data.putExtra(MDATA_ID, metricID);
    	data.putExtra(MDATA_VALUE, picker.getValue());
    	this.setResult(Activity.RESULT_OK, data);
    	this.finish();
    }


}
