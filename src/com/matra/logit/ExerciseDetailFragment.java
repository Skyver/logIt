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
import android.widget.ImageView;
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
			
		}
		metricsListView.setAdapter(new MetricListAdapter(this, displayedExercise.getMetricList()));
		
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
		private ExerciseDetailFragment fParent;
		private ArrayList<Metric> metricList;
		
		public MetricListAdapter(ExerciseDetailFragment parent, ArrayList<Metric> metrics)
		{
			this.fParent = parent;
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
			final Metric metric = metricList.get(position);
			ViewHolder holder;
			
			if(convertView == null)
			{
				LayoutInflater li = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = li.inflate(R.layout.metric_view_normal, parent, false);
				holder = new ViewHolder();
				holder.tvName = (TextView) convertView.findViewById(R.id.metricTitle);
				holder.tvValue = (TextView) convertView.findViewById(R.id.metricValue);
				holder.tvTrend = (ImageView) convertView.findViewById(R.id.metricTrend);
				holder.bUpdate = (Button) convertView.findViewById(R.id.buttonUpdateMetric);
				holder.bDelete = (Button) convertView.findViewById(R.id.buttonDeleteMetric);
				convertView.setTag(holder);
			}
			else
			{
				holder = (ViewHolder) convertView.getTag();
			}
			holder.tvName.setText(metric.getName());
			holder.tvValue.setText(String.valueOf(metric.getValue()));
			//holder.tvTrend.setText(); 
			if(metric.getTrend().equals(Metric.TREND_UP))
			{
				holder.tvTrend.setImageResource(R.drawable.up_icon);
			}
			else if(metric.getTrend().equals(Metric.TREND_DOWN))
			{
				holder.tvTrend.setImageResource(R.drawable.down_icon);
			}
			
			
			
			holder.bUpdate.setOnClickListener( new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					fParent.callbackUpgradeMetric(metric.getId());
					
				}
			});
			
			holder.bDelete.setOnClickListener(new View.OnClickListener() {				
				@Override
				public void onClick(View v) {
					fParent.callbackDeleteMetric(metric.getId());
					
				}
			});

			return convertView;
		}
		
		
	}
	
	static class ViewHolder
	{
		TextView tvName;
		TextView tvValue;
		ImageView tvTrend;
		Button bUpdate;
		Button bDelete;
		
	}
		
		
		
	
	
	
}
