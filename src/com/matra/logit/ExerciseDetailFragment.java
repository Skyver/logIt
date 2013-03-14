package com.matra.logit;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.matra.logit.interopServices.ExercisesManager;
import com.matra.logit.storage.Exercise;
import com.matra.logit.storage.Metric;

public class ExerciseDetailFragment extends Fragment 
{
	public static final int NEW_METRIC_REQUESTCODE = 2;
	public static final int UPDATE_METRIC_REQUESTCODE = 3;
	
	TextView title;
	TextView description;
	ListView metricsListView;
	Exercise displayedExercise; 
	ExercisesManager exerciseManager;
	
	@Override
	public void onCreate (Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View result = inflater.inflate(R.layout.fragment_exercise_detail, container, false);
		title = (TextView) result.findViewById(R.id.detailTitle);
		description = (TextView) result.findViewById(R.id.detailDescription);
		metricsListView = (ListView) result.findViewById(R.id.detailMetricList);
		
		metricsListView.setLongClickable(true);

		
		Button button = (Button) result.findViewById(R.id.detailButtonMetric);
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
		if(displayedExercise != null) //this is on rotating display, state loss?
		{
			title.setText(displayedExercise.getName());
			description.setText(displayedExercise.getDescription());
			//ArrayAdapter<Metric> adapter = new ArrayAdapter<Metric>(this.getActivity(), 
			//		android.R.layout.simple_list_item_1, displayedExercise.getMetricList());
			metricsListView.setAdapter(new MetricListAdapter(this, displayedExercise.getMetricList()));
			
		}
		
	}
	

    @Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
    	super.onCreateOptionsMenu(menu, inflater);
    	inflater.inflate(R.menu.exercise_detail_menu, menu);
    }
	
	
	//TODO : Single refresh method to take care of displaying
	public void setDisplayedExercise(Exercise exercise) //
	{
		
		if(title != null) //If title is null, so is any other View
		{
			title.setText(exercise.getName());
			description.setText(exercise.getDescription());
			//ArrayAdapter<Metric> adapter = new ArrayAdapter<Metric>(this.getActivity(), 
			//		android.R.layout.simple_list_item_1, exercise.getMetricList());
			metricsListView.setAdapter(new MetricListAdapter(this, exercise.getMetricList()));
			
		}
		displayedExercise = exercise;
		
	}
	
	public void onNewMetricClick()
	{
		
		Intent intent = new Intent(this.getActivity(),NewMetric.class);
		startActivityForResult(intent, NEW_METRIC_REQUESTCODE);
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		exerciseManager.signalResume();
		if(requestCode == NEW_METRIC_REQUESTCODE && resultCode == Activity.RESULT_OK)
		{
			String name = data.getStringExtra(NewMetric.RETURN_NAME);
			int initValue = data.getIntExtra(NewMetric.RETURN_VALUE, 0);
			
			Metric newMetric = exerciseManager.addMetric(name, initValue, displayedExercise.getId());
			//displayedExercise.addMetric(newMetric);
			
		}
		if(requestCode == UPDATE_METRIC_REQUESTCODE && resultCode == Activity.RESULT_OK)
		{
			int value = data.getIntExtra(UpdateMetric.MDATA_VALUE, 0);
			long id = data.getLongExtra(UpdateMetric.MDATA_ID, -1);
			exerciseManager.updateMetric(id, value);
			displayedExercise.updateMetric(id, value);
			metricsListView.setAdapter(new MetricListAdapter(this, displayedExercise.getMetricList()));
		}
		
		
		super.onActivityResult(requestCode, resultCode, data);
		
	}
	
	public void setExerciseManager(ExercisesManager manager)
	{
		this.exerciseManager = manager;
	}
	
	public void callbackDeleteMetric(long metricID)
	{
		for(Metric m:displayedExercise.getMetricList())
		{
			if(m.getId() == metricID)
			{
				displayedExercise.removeMetric(m);
				exerciseManager.removeMetric(m);
				break;
			}
		}
		metricsListView.setAdapter(new MetricListAdapter(this, displayedExercise.getMetricList()));
		
	}
	
	public void callbackUpgradeMetric(long metricID)
	{
		int value = 0;
		//find the current value, to show on picker
		for(Metric m:displayedExercise.getMetricList())
		{
			if(m.getId() == metricID)
			{
				value = m.getValue();
				break;
			}
		}
		
		Intent intent = new Intent(this.getActivity(), UpdateMetric.class);
		intent.putExtra(UpdateMetric.MDATA_ID, metricID);
		intent.putExtra(UpdateMetric.MDATA_VALUE, value);
		startActivityForResult(intent, UPDATE_METRIC_REQUESTCODE);
		
	}
	
	private class MetricListAdapter extends BaseAdapter
	{
		private ExerciseDetailFragment parent;
		private ArrayList<Metric> metricList;
		
		public MetricListAdapter(ExerciseDetailFragment parent, ArrayList<Metric> metrics)
		{
			this.parent = parent;
			this.metricList = metrics;
		}
		
		public int getCount()
		{
			return metricList.size();
		}
		
		public Object getItem(int position)
		{
			return position;
		}
		
		public long getItemId(int position) {
            return position;
        }

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			MetricView mv;
			if(convertView == null)
			{
				mv = new MetricView(this.parent, metricList.get(position).getName(), metricList.get(position).getValue());
				mv.setMetricID(metricList.get(position).getId());
			}
			else
			{
				mv = (MetricView) convertView;
				mv.setName(metricList.get(position).getName());
				mv.setValue(metricList.get(position).getValue());
				mv.setMetricID(metricList.get(position).getId());
			}
			return mv;
		}
		
		
	}
	
	private class MetricView extends LinearLayout
	{
		TextView title;
		TextView number;
		TextView backButton;
		TextView updateButton;
		TextView deleteButton;
		ExerciseDetailFragment parent;
		String name;
		int value;
		long metricID;
		
		public MetricView(ExerciseDetailFragment parent, String name, int value)
		{
			super(parent.getActivity());
			this.parent = parent;
			this.setOrientation(HORIZONTAL);
			this.name = name;
			this.value = value;
			
			this.setDefaultView();
			
			
			this.setOnLongClickListener(new View.OnLongClickListener() {
				
				@Override
				public boolean onLongClick(View v) {
					setAlternateView();
					return true;
				}
			});
		}
		
		public void setName(String name)
		{
			title.setText(name);
		}
		
		public void setValue(int value)
		{
			number.setText(String.valueOf(value));
		}
		
		public void setMetricID(long ID)
		{
			this.metricID = ID;
		}
		
		private void setDefaultView()
		{
			removeAllViews();
			title = new TextView(parent.getActivity());
			title.setText(name);
			LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, 50);
			addView(title, params);
			
			number = new TextView(parent.getActivity());
			number.setText(String.valueOf(value));
			LayoutParams paramsNum = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, 50);
			number.setGravity(Gravity.RIGHT);
			
			addView(number, paramsNum);
			
			
		}
		
		private void setAlternateView()
		{
			removeAllViews();
			backButton = new TextView(parent.getActivity());
			backButton.setText("Back");
			backButton.setGravity(Gravity.CENTER);		
			backButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					setDefaultView();
					
				}
			});
			LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, 33);
			addView(backButton,params);
			
			updateButton = new TextView(parent.getActivity());
			updateButton.setText("Update");
			updateButton.setGravity(Gravity.CENTER);
			updateButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					setDefaultView();
					parent.callbackUpgradeMetric(metricID);
				}
			});
			addView(updateButton, params);
			
			deleteButton = new TextView(parent.getActivity());
			deleteButton.setText("Delete");
			deleteButton.setGravity(Gravity.CENTER);
			deleteButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					setDefaultView();
					parent.callbackDeleteMetric(metricID);
				}
			});
			addView(deleteButton, params);
		}
		
		
		
	
	}
	
}
