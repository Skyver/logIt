package com.matra.logit.storage;

public class Metric {
	
	public static final String TREND_DOWN = "down";
	public static final String TREND_UP = "up";
	public static final String TREND_EQUAL = "eq";
	
	
	private long id;
	private long ownerId;
	private String name;
	private int value;
	private String trend;
	private String formattedDate;
	
	public Metric(long ownerId, String name, int value)
	{
		this.ownerId = ownerId;
		this.name = name;
		this.value = value;
		this.setId(-1);
		this.setTrend(TREND_EQUAL);
		this.setFormattedDate(ExerciseDataSource.getTodayDate());
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
		return name;
	}

	public String getTrend() {
		return trend;
	}

	public void setTrend(String trend) {
		this.trend = trend;
	}

	public String getFormattedDate() {
		return formattedDate;
	}

	public void setFormattedDate(String formattedDate) {
		this.formattedDate = formattedDate;
		System.out.println(formattedDate);
	}

}
