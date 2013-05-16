package com.matra.logit;

import java.util.ArrayList;

import com.fima.cardsui.objects.CardStack;
import com.fima.cardsui.views.CardUI;
import com.matra.logit.interopServices.NotesManager;
import com.matra.logit.storage.Exercise;
import com.matra.logit.storage.ExerciseNote;
import com.matra.utils.NoteCard;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.PopupMenu.OnMenuItemClickListener;
import android.widget.TextView;
import android.widget.Toast;

public class NotesFragment extends Fragment implements OnMenuItemClickListener {
	
	private Exercise owner;
	
	private TextView title;
	private CardUI cardHolder;
	private NotesManager notesMgmt;
	CardStack stack; 
	
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		notesMgmt = new NotesManager();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View result = inflater.inflate(R.layout.fragment_notes, container, false);
		title = (TextView) result.findViewById(R.id.notesTitle);
		cardHolder = (CardUI) result.findViewById(R.id.notecards);
		return result;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		if(owner != null)
		{
			updateFragment();
		}
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
	{
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.notes_menu, menu);
	}
	
	public void setNotesOwner(Exercise exercise)
	{
		owner = exercise;
		updateFragment();
	}
	
	private void updateFragment()
	{
		if(title != null)
		{
			title.setText(owner.getName() + " " + getActivity().getString(R.string.title_notes));
			stack = new CardStack();
			stack.setTitle(/*owner.getName() + " " +*/ getActivity().getString(R.string.title_notes));
			cardHolder.addStack(stack);
			ArrayList<ExerciseNote> notes = notesMgmt.getNotesByOwner(owner.getId());
			for(int i = 0; i < notes.size(); i++)
			{
				NoteCard c = new NoteCard(notes.get(i).getId() ,notes.get(i).getTitle(), notes.get(i).getNoteContent(), 
																						notes.get(i).getStripeColor());
				c.setOnClickListener( new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						showCardPopupMenu(v);
						
					}
				} );
				cardHolder.addCardToLastStack(c);
			}
			cardHolder.refresh();
		}
	}
	
	public void showCardPopupMenu(View v)
	{
		PopupMenu overflow = new PopupMenu(getActivity(), v);
		MenuInflater inflater = overflow.getMenuInflater();
		inflater.inflate(R.menu.note_overflow_menu, overflow.getMenu());
		overflow.setOnMenuItemClickListener(this);
		overflow.show();
	}
	
	@Override
	public boolean onMenuItemClick(MenuItem item)
	{
		switch(item.getItemId())
		{
			case(R.id.card_edit):
				Toast.makeText(getActivity(), "EDIT", Toast.LENGTH_SHORT).show();
				return true;
			case(R.id.card_delete):		
				boolean b = notesMgmt.deleteNoteByID( ((NoteCard) stack.getTop()).getNoteId() );
				stack.popStack();
				cardHolder.refresh();
				//TODO
				Toast.makeText(getActivity(), String.valueOf(b) + " "+ notesMgmt.getAllNotes().size(), Toast.LENGTH_SHORT).show();
				return true;
			default:
				return false;
		}	
	}
	
	 @Override
	 public boolean onOptionsItemSelected(MenuItem item)
	 {
		 switch(item.getItemId())
		 {
		 	case R.id.menu_notes_add:
		 		Toast.makeText(getActivity(), "NEW NOTE", Toast.LENGTH_SHORT).show();
		 		//TODO
		 		return true;
		 	default:
		 		return false;
		 }
	 }
	
	

}
