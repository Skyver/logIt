package com.matra.logit.storage;

public class Metric {
	
	private long id;
	private long ownerId;
	private String name;
	private int value;
	
	public Metric(long ownerId, String name, int value)
	{
		this.ownerId = ownerId;
		this.name = name;
		this.value = value;
		this.setId(-1);
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getOwnerId() {
		return ownerId;
	}

	public String getName() {
		return name;
	}

	public int getValue() {
		return value;
	}
	
	public void setValue(int value)
	{
		this.value = value;
	}
	
	@Override
	public String toString()
	{
		return name + "      " + value; //TODO : Once redesign comes, return to default and change layout
	}

}
