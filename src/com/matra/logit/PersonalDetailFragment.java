package com.matra.logit;

import com.matra.logit.interopServices.PersonalInfoManager;
import com.matra.logit.storage.DataItem;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class PersonalDetailFragment extends Fragment {
	public static final int NEW_METRIC_REQUESTCODE = 2;
	public static final int UPDATE_METRIC_REQUESTCODE = 3;
	
	TextView title;
	TextView description;
	ListView metricsListview;
	DataItem displayedItem;
	PersonalInfoManager infoManager;
	
	@Override
	public void onCreate (Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View result = inflater.inflate(R.layout.fragment_personal_detail, container, false);
		title = (TextView) result.findViewById(R.id.personalDetailTitle);
		description = (TextView) result.findViewById(R.id.personalDetailDesc);
		//TODO listView thing
		Button button = (Button) result.findViewById(R.id.personalDetailButtonMetric);
		button.setOnClickListener( new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				onNewMetricClick();
				
			}
		});
		return result;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		if(displayedItem != null)
		{
			title.setText(displayedItem.getName());
			description.setText(displayedItem.getDescription());
			//TODO adapter
		}
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
    	super.onCreateOptionsMenu(menu, inflater);
    	inflater.inflate(R.menu.personal_detail_menu, menu);
    }
	
	public void setDisplayedData(DataItem item)
	{
		if(title != null)
		{
			title.setText(item.getName());
			description.setText(item.getDescription());
			// TODO Adapter
		}
		displayedItem = item;
	}
	
	public void onNewMetricClick()
	{
		Toast.makeText(getActivity(), "New MEtric", Toast.LENGTH_SHORT).show();
	}
	
	//@Override
	//public void onActivityResult(int requestCode, int resultCode, Intent data) {} TODO
	
	public void setInfoManager(PersonalInfoManager mgmt)
	{
		infoManager = mgmt;
	}
	
	// public void callbackDeleteMetric(long metricID) TODO
	
	// public void callbackUpgradeMetric(long metricID) TODO
	
	
}
