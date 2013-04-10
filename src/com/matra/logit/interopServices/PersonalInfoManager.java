package com.matra.logit.interopServices;

import java.sql.SQLException;
import java.util.ArrayList;

import com.matra.logit.storage.DataItem;
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
		cacheStorage = new ArrayList<DataItem>();
	}
	
	//TODO change string to class
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
	
	// TODO DELETE
	
	// TODO UPDATE
	
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
