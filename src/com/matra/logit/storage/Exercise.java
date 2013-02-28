package com.matra.logit.storage;

public class Exercise {
	

	private String name;
	private String description;
	private long id;
	
	public Exercise(String name, String description)
	{
		this.name = name;
		this.description = description;
		this.setId(-1);
	}
	
	public String getName()
	{
		return name;
	}
	
	public String getDescription()
	{
		return description;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@Override
	public String toString()
	{
		return name;
	}

}
