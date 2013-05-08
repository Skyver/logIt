package com.matra.logit;

import com.fima.cardsui.objects.CardStack;
import com.fima.cardsui.views.CardUI;
import com.matra.logit.storage.Exercise;
import com.matra.utils.NoteCard;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class NotesFragment extends Fragment {
	
	private Exercise owner;
	
	private TextView title;
	private CardUI cardHolder;
	
	private String[] mocktitles = {"Note1", "Note2", "Note3"};
	private String[] mockContent = {"Lorem ipsum contentus interesantus", "Lorem ipsum contentus interesantus", "Lorem ipsum contentus interesantus"};
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
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
			CardStack stack = new CardStack();
			stack.setTitle(/*owner.getName() + " " +*/ getActivity().getString(R.string.title_notes));
			cardHolder.addStack(stack);
			for(int i = 0; i < mocktitles.length; i++)
			{
				NoteCard c = new NoteCard(mocktitles[i], mockContent[i], NoteCard.HOLO_DARK_RED);
				cardHolder.addCardToLastStack(c);
			}
			cardHolder.refresh();
		}
	}
	
	

}
