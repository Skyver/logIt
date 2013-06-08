package com.matra.logit.interopServices;

import java.util.ArrayList;
import java.util.ListIterator;

import com.matra.logit.storage.ExerciseNote;

public class NotesManager {

	private ArrayList<ExerciseNote> notesList;
	
	public NotesManager()
	{
		//TODO Lower layer fetch
		notesList = new ArrayList<ExerciseNote>();
		notesList.add(new ExerciseNote(1, "Note about Dogs", "Lorez Ipzum Dolorrz et sun sit amet valamint", ExerciseNote.HOLO_DARK_BLUE, 12));
		notesList.add(new ExerciseNote(2, "Note about Cats", "Lorez Ipzum Dolorrz et sun sit amet valamint", ExerciseNote.HOLO_DARK_GREEN, 12));
		notesList.add(new ExerciseNote(3, "Note about Ferrets", "Lorez Ipzum Dolorrz et sun sit amet valamint", ExerciseNote.HOLO_DARK_ORANGE, 1));
		notesList.add(new ExerciseNote(4, "Note about Parrots", "Lorez Ipzum Dolorrz et sun sit amet valamint", ExerciseNote.HOLO_PURPLE, 6));
	}
	
	public ArrayList<ExerciseNote> getAllNotes()
	{
		return notesList;
	}
	
	public ArrayList<ExerciseNote> getNotesByOwner(long ownerID)
	{
		ArrayList<ExerciseNote> selected = new ArrayList<ExerciseNote>();
		for (ExerciseNote note : notesList)
		{
			if(note.getOwnerId() == ownerID)
			{
				selected.add(note);
			}
		}
		return selected;
	}
	
	//Returns false if not found. deletes first found
	public boolean deleteNoteByID(long id)
	{
		ListIterator<ExerciseNote> it = notesList.listIterator();
		ExerciseNote note;
		while(it.hasNext())
		{
			note = it.next();
			if(note.getId() == id)
			{
				it.remove();
				//TODO Lower layer delete
				return true;
			}
		}
		return false;
	}
	
	public void addNote(String title, String desc, String color,long owner)
	{
		long id = 0;
		if(!notesList.isEmpty())
		{
			id = notesList.get(notesList.size()-1).getId() + 1;
		}
		
		notesList.add(new ExerciseNote(id, title, desc, color, owner));
		//TODO Lower layer add
	
	}
}
