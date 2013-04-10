package com.matra.logit.storage;

public class DataItem {

	private long id;
	private String name;
	private String description;
	
	public DataItem(String name, String description)
	{
		this.name = name;
		this.description = description;
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
