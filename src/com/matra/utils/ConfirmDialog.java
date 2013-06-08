package com.matra.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

public class ConfirmDialog extends DialogFragment{

	public interface ConfirmDialogListener
	{
		public void onDialogPositive(DialogFragment dialog);
		public void onDialogNegative(DialogFragment dialog);
	}
	
	ConfirmDialogListener listener;
	
	@Override
	public void onAttach(Activity activity)
	{
		super.onAttach(activity);
		try
		{
			listener = (ConfirmDialogListener) getTargetFragment();
		}
		catch(ClassCastException e)
		{
			throw new ClassCastException(activity.toString()
                    + " must implement NoticeDialogListener");
		}
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState)
	{
		AlertDialog.Builder builder = new Builder(getActivity());
		builder.setMessage("Would you like to delete the exercise?");//TODO to r.string
		builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				listener.onDialogPositive(ConfirmDialog.this);
				
			}
		});
		builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				listener.onDialogNegative(ConfirmDialog.this);
				
			}
		});
		
		return builder.create();
	}
}
