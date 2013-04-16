package com.matra.logit.storage;

import java.util.ArrayList;

import com.matra.logit.interopServices.ExercisesManager;

public class DataItem {

	private long id;
	private String name;
	private String description;
	private ArrayList<Metric> metrics;
	
	public DataItem(String name, String description)
	{
		this.name = name;
		this.description = description;
		this.setId(-1);
		metrics = new ArrayList<Metric>();
	}
	
	public void updateMetric(long id, int value)
	{
		for(Metric m:metrics)
		{
			if(m.getId() == id)
			{
				m.setTrend(ExercisesManager.getTrend(m.getValue(), value));
				m.setValue(value);
				m.setFormattedDate(ExerciseDataSource.getTodayDate());
			}
		}
	}
	
	public ArrayList<Metric> getMetricList()
	{
		return metrics;
	}
	
	
	public void addMetric(Metric metric)
	{
		metrics.add(metric);
	}
	
	public void removeMetric(Metric metric)
	{
		metrics.remove(metric);
	}
	
	public void addAllMetrics(ArrayList<Metric> metrics)
	{
		this.metrics = metrics;
	}
	
	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}
	
	@Override
	public String toString()
	{
		return name;
	}
	
}
