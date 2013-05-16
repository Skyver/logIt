package com.matra.logit.storage;

public class ExerciseNote 
{
	private String title;
	private String noteContent;
	private String stripeColor; //On alpha hex format
	private long ownerId;
	private long id;
	
	public static final String HOLO_DARK_RED = "#FFCC0000";
	public static final String HOLO_DARK_BLUE = "#ff0099cc";
	public static final String HOLO_DARK_GREEN = "#ff669900";
	public static final String HOLO_DARK_ORANGE = "#ffff8800";
	public static final String HOLO_PURPLE = "#ffaa66cc";
	
	public ExerciseNote(long id, String noteTitle, String content, String color, long owner)
	{
		setTitle(noteTitle);
		setNoteContent(content);
		setStripeColor(color);
		setId(id);
		ownerId = owner;
	}

	public String getTitle() {
		return title;
	}

	private void setTitle(String title) {
		this.title = title;
	}

	public String getNoteContent() {
		return noteContent;
	}

	public void setNoteContent(String noteContent) {
		this.noteContent = noteContent;
	}

	public String getStripeColor() {
		return stripeColor;
	}

	private void setStripeColor(String stripeColor) {
		this.stripeColor = stripeColor;
	}

	public long getOwnerId() {
		return ownerId;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	
	
	
	
}
