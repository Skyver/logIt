package com.matra.logit;

import java.util.ArrayList;

import com.matra.logit.ExerciseDetailFragment.ViewHolder;
import com.matra.logit.interopServices.PersonalInfoManager;
import com.matra.logit.storage.DataItem;
import com.matra.logit.storage.Metric;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
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
		metricsListview = (ListView) result.findViewById(R.id.personalMetricList);
		metricsListview.setLongClickable(true);
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
			metricsListview.setAdapter(new PersonalMetricListAdapter(this, displayedItem.getMetricList()));
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
			metricsListview.setAdapter(new PersonalMetricListAdapter(this, item.getMetricList()));
		}
		displayedItem = item;
	}
	
	public void onNewMetricClick()
	{
		Intent intent = new Intent(this.getActivity(), NewMetric.class);
		startActivityForResult(intent, NEW_METRIC_REQUESTCODE);
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		infoManager.signalResume();
		if(requestCode == NEW_METRIC_REQUESTCODE && resultCode == Activity.RESULT_OK)
		{
			String name = data.getStringExtra(NewMetric.RETURN_NAME);
			int initValue = data.getIntExtra(NewMetric.RETURN_VALUE, 0);
			infoManager.addMetric(name, initValue, displayedItem.getId());
		}
		else if(requestCode == UPDATE_METRIC_REQUESTCODE && resultCode == Activity.RESULT_OK)
		{
			int value = data.getIntExtra(UpdateMetric.MDATA_VALUE, 1);
			long id = data.getLongExtra(UpdateMetric.MDATA_ID, -1);
			infoManager.updateMetric(id, value);
			displayedItem.updateMetric(id, value);
		}
		metricsListview.setAdapter(new PersonalMetricListAdapter(this, displayedItem.getMetricList()));
	}
	
	public void setInfoManager(PersonalInfoManager mgmt)
	{
		infoManager = mgmt;
	}
	
	public void callbackDeleteMetric(long metricID)
	{
		for(Metric mt : displayedItem.getMetricList())
		{
			if(mt.getId() == metricID)
			{
				displayedItem.removeMetric(mt);
				infoManager.removeMetric(mt);
			}
		}
		metricsListview.setAdapter(new PersonalMetricListAdapter(this, displayedItem.getMetricList()));
	}
	
	public void callbackUpgradeMetric(long metricID)
	{
		int value = 0;
		for(Metric mt : displayedItem.getMetricList())
		{
			if(mt.getId() == metricID)
			{
				value = mt.getValue();
				break;
			}
		}
		Intent intent = new Intent(this.getActivity(), UpdateMetric.class);
		intent.putExtra(UpdateMetric.MDATA_ID, metricID);
		intent.putExtra(UpdateMetric.MDATA_VALUE, value);
		startActivityForResult(intent, UPDATE_METRIC_REQUESTCODE);
	}
	
	private class PersonalMetricListAdapter extends BaseAdapter
	{
		private PersonalDetailFragment fParent;
		private ArrayList<Metric> metricList;
		
		public PersonalMetricListAdapter(PersonalDetailFragment parent, ArrayList<Metric> metrics)
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
