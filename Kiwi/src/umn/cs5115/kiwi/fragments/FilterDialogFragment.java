package umn.cs5115.kiwi.fragments;

import umn.cs5115.kiwi.Course;
import umn.cs5115.kiwi.DatabaseHandler;
import umn.cs5115.kiwi.FilterDefinition;
import umn.cs5115.kiwi.R;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
	
	public static FilterDialogFragment newInstance(FilterDefinition definition) {
        FilterDialogFragment f = new FilterDialogFragment();
        
        Bundle args = new Bundle();
        args.putParcelable("definition", definition);
        f.setArguments(args);
        
        return f;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		
		LayoutInflater inflater = getActivity().getLayoutInflater();
		
		View rootView = inflater.inflate(R.layout.filter_dialog_layout, null);
		
		FilterDefinition defn = (FilterDefinition)getArguments().getParcelable("definition");
		Toast.makeText(getActivity(), "" + defn.i + defn.b, Toast.LENGTH_SHORT).show();
		
//		TextView tv = (TextView)rootView.findViewById(R.id.textView1);
//		tv.setText(String.format("Hello! %d", getArguments().getInt("whatever")));
		
		Context ctx = getActivity();
		
		String[] courses = new String[] { "Course 1", "Course 2", "Course 3" };
		String[] types = new String[] { "Reading", "Essay", "Exam", "Something else" };
		
		Course[] courses2 = new DatabaseHandler(ctx).filterCourses(null);
		
		LinearLayout coursesLayout = (LinearLayout) rootView.findViewById(R.id.courses_linearlayout);
		LinearLayout typesLayout = (LinearLayout) rootView.findViewById(R.id.assignment_types_linearlayout);
		
		if (courses2.length == 0) {
		    ((TextView)rootView.findViewById(R.id.no_courses_textview)).setVisibility(View.VISIBLE);
		    ((TextView)rootView.findViewById(R.id.no_courses_textview)).setText("Hi Dom!");
		} else {
	        for (Course c : courses2) {
	            CheckBox cb = new CheckBox(ctx);
	            cb.setText(c.getCourseDesignation());
	            cb.setChecked(true);
	            coursesLayout.addView(cb);
	        }
		}

		for (String t : types) {
		    CheckBox cb = new CheckBox(ctx);
		    cb.setText(t);
	        cb.setChecked(true);
		    typesLayout.addView(cb);
		}
	        
		
		final FilterListener listener = (FilterListener)getActivity();
		
		return new AlertDialog.Builder(getActivity())
			.setView(rootView)
			.setTitle("Hello")
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
