package umn.cs5115.kiwi.fragments;

import umn.cs5115.kiwi.FilterDefinition;
import umn.cs5115.kiwi.R;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class FilterDialogFragment extends DialogFragment {
	public static interface FilterListener {
		public void onNewFilter(FilterDefinition newfilter);
	}
	
	public FilterDialogFragment() {}
	
	public static FilterDialogFragment newInstance(int whatever) {
		FilterDialogFragment f = new FilterDialogFragment();
		
		Bundle args = new Bundle();
		args.putInt("whatever", whatever);
		f.setArguments(args);
		
		return f;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		
		LayoutInflater inflater = getActivity().getLayoutInflater();
		
		View rootView = inflater.inflate(R.layout.filter_dialog_layout, null);
		
		TextView tv = (TextView)rootView.findViewById(R.id.textView1);
		tv.setText(String.format("Hello! %d", getArguments().getInt("whatever")));
		
		final FilterListener listener = (FilterListener)getActivity();
		
		return new AlertDialog.Builder(getActivity())
			.setView(rootView)
			.setTitle("Filter/Sort")
			.setPositiveButton(android.R.string.ok, new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					Toast.makeText(getActivity(), "Filtered!", Toast.LENGTH_LONG).show();
					
					listener.onNewFilter(null);
				}
			})
			.setNegativeButton(android.R.string.cancel, new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					Toast.makeText(getActivity(), "Not filtered...", Toast.LENGTH_SHORT).show();
				}
			}).create();
	}
}
