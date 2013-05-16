package com.matra.utils;

import android.R.anim;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AnalogClock;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fima.cardsui.objects.Card;
import com.matra.logit.R;
import com.matra.logit.storage.ExerciseNote;

public class NoteCard extends Card {
	
	private long noteId;
	
	public NoteCard(long id, String title, String content, String color)
	{
		super(title, content, color, ExerciseNote.HOLO_DARK_RED, true, false);
		setNoteId(id);
	}
	
	@Override
	public View getCardContent(Context context)
	{
		View v = LayoutInflater.from(context).inflate(R.layout.note_card, null);
		((TextView)v.findViewById(R.id.noteTitle)).setText(titlePlay);
		((TextView)v.findViewById(R.id.noteTitle))
							.setTextColor(context.getResources().getColor(android.R.color.holo_red_dark));
		((TextView)v.findViewById(R.id.noteContent)).setText(description);

		((ImageView)v.findViewById(R.id.noteColor))
							.setBackgroundColor(Color.parseColor(color));
		if (isClickable == true)
			((LinearLayout) v.findViewById(R.id.cardContent))
						.setBackgroundResource(R.drawable.selectable_background_cardbank);
		
		if (hasOverflow == true)
			((ImageView) v.findViewById(R.id.overflow))
					.setVisibility(View.VISIBLE);
		else
			((ImageView) v.findViewById(R.id.overflow))
					.setVisibility(View.GONE);
		
		return v;
	}

	public long getNoteId() {
		return noteId;
	}

	public void setNoteId(long noteId) {
		this.noteId = noteId;
	}
}
