package com.matra.logit.interopServices;

import java.security.acl.Owner;
import java.sql.SQLException;
import java.util.ArrayList;

import com.matra.logit.storage.DataItem;
import com.matra.logit.storage.Metric;
import com.matra.logit.storage.PersonalDataSource;
import android.content.Context;

public class PersonalInfoManager {

	private PersonalDataSource personalDatasource;
	private ArrayList<DataItem> cacheStorage;
	
	public PersonalInfoManager(Context context)
	{
		personalDatasource = new PersonalDataSource(context);
		try {
			personalDatasource.open();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		cacheStorage = personalDatasource.getAllDataItems();
	}
	
	public ArrayList<DataItem> getData()
	{
		return cacheStorage;
	}
	
	public void generateInitData()
	{
		DataItem weightItem = new DataItem("Body Weight", "Refers to a person's mass or weight. Strictly speaking, the body weight is the weight of the person without any items on, but practically body weight is taken with clothes on but often without the shoes and heavy accessories like mobile phones and wallets.");
		DataItem bmiItem = new DataItem("BMI", "The body mass index (BMI), or Quetelet index, is a measure for human body shape based on an individual's weight and height.");
		
		long wID = personalDatasource.addDataItem(weightItem);
		long bmiID = personalDatasource.addDataItem(bmiItem);
		//--Add metrics here
		Metric kg = new Metric(wID, "Kilograms", 60);
		weightItem.addMetric(kg);
		personalDatasource.addMetricToDataItem(kg);
		Metric x10BMI = new Metric(bmiID, "BMI X10", 200);
		bmiItem.addMetric(x10BMI);
		personalDatasource.addMetricToDataItem(x10BMI);
		//------
		cacheStorage = personalDatasource.getAllDataItems();		
	}
	
	public DataItem addNewDataItem(String name, String desc)
	{
		DataItem item = new DataItem(name, desc);
		long id = personalDatasource.addDataItem(item);
		item.setId(id);
		cacheStorage.add(item); 
		return item;
	}
	
	public void removeDataItem(DataItem item)
	{
		cacheStorage.remove(item);
		personalDatasource.deleteDataItem(item);
	}
	
	public Metric addMetric(String name, int init, long ownerID)
	{
		Metric metric = new Metric(ownerID, name, init);
		long id = personalDatasource.addMetricToDataItem(metric);
		metric.setId(id);
		for(DataItem item : cacheStorage)
		{
			if(item.getId() == ownerID)
			{
				item.addMetric(metric);
			}
		}
		return metric;
	}
	
	public void removeMetric(Metric metric)
	{
		personalDatasource.removeMetricFromDataItem(metric);
		for(DataItem item : cacheStorage)
		{
			if(item.getId() == metric.getOwnerId())
			{
				item.removeMetric(metric);
			}
		}
	}
	
	public void updateMetric(long metricID, int newValue)
	{
		if(metricID != -1)
		{
			int oldValue = 0;
			outloop:
			for(DataItem item: cacheStorage)
			{
				for(Metric mt : item.getMetricList())
				{
					if(mt.getId() == metricID)
					{
						oldValue = mt.getValue();
						break outloop;
					}
				}
			}
			personalDatasource.updateMetricValue(metricID, newValue, ExercisesManager.getTrend(oldValue, newValue));
			cacheStorage = personalDatasource.getAllDataItems();
		}
	}
	
	//signals the bottom layer that the app is on resumed state
	public void signalResume()
	{
		try {
			personalDatasource.open();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	//signals the bottom layer that the app is on paused state
	public void signalPause()
	{
		personalDatasource.close();
	}
	
}
