package umn.cs5115.kiwi.fragments;

import java.util.ArrayList;

import umn.cs5115.kiwi.Course;
import umn.cs5115.kiwi.DatabaseHandler;
import umn.cs5115.kiwi.FilterDefinition;
import umn.cs5115.kiwi.FilterDefinition.SortBy;
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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class FilterDialogFragment extends DialogFragment {
	public static interface FilterListener {
		public void onNewFilter(FilterDefinition newfilter);
	}
	
	public FilterDialogFragment() {}
	
	
	public static FilterDialogFragment newInstance(FilterDefinition definition) {
        FilterDialogFragment f = new FilterDialogFragment();
        
        Bundle args = new Bundle();
        args.putParcelable("definition", definition);
        f.setArguments(args);
        
        return f;
	}
	
	private int getSortNumber(View rootView, RadioGroup radios) {
		return Integer.parseInt((String) ((RadioButton)rootView.findViewById(radios.getCheckedRadioButtonId())).getTag());
	}
	
	private boolean getShowCompleted(View rootView) {
		return ((CheckBox)rootView.findViewById(R.id.show_completed)).isChecked();
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		LayoutInflater inflater = getActivity().getLayoutInflater();
		
		final View rootView = inflater.inflate(R.layout.filter_dialog_layout, null);
		
		FilterDefinition defn = (FilterDefinition)getArguments().getParcelable("definition");
		if (defn == null) {
			defn = new FilterDefinition(SortBy.DUE_DATE, true, null, null);
		}
		// Make sure these arrays are things we can iterate over.
		if (defn.courses == null) {
			defn.courses = new int[0];
		}
		if (defn.types == null) {
			defn.types = new String[0];
		}
		
//		TextView tv = (TextView)rootView.findViewById(R.id.textView1);
//		tv.setText(String.format("Hello! %d", getArguments().getInt("whatever")));
		
		Context ctx = getActivity();
		
		Course[] courses = new DatabaseHandler(ctx).getCourses();
		String[] types = getResources().getStringArray(R.array.assignment_types);
		
		LinearLayout coursesLayout = (LinearLayout) rootView.findViewById(R.id.courses_linearlayout);
		LinearLayout typesLayout = (LinearLayout) rootView.findViewById(R.id.assignment_types_linearlayout);
		
		final RadioGroup sortRadios = (RadioGroup)rootView.findViewById(R.id.radioGroup1);
		
		// Select the correct radio button.
		RadioButton initialSortRadio;
		initialSortRadio = (RadioButton)sortRadios.findViewWithTag(Integer.toString(defn.sorter.toInt()));
		if (initialSortRadio == null) {
			initialSortRadio = (RadioButton)sortRadios.findViewById(R.id.sort_by_due);
		}
		initialSortRadio.setChecked(true);
		
		// Check off the 'show completed' box if needed
		((CheckBox)rootView.findViewById(R.id.show_completed)).setChecked(defn.showCompleted);
		
		final CheckBox[] courseCheckboxes = new CheckBox[courses.length];
		final CheckBox[] typeCheckboxes = new CheckBox[types.length];
		
		if (courses.length == 0) {
		    ((TextView)rootView.findViewById(R.id.no_courses_textview)).setVisibility(View.VISIBLE);
		    ((TextView)rootView.findViewById(R.id.no_courses_textview)).setText("Hi Dom!");
		} else {
			for (int i = 0; i < courses.length; i++) {
				Course course = courses[i];
	            CheckBox cb = new CheckBox(ctx);
	            cb.setText(course.getCourseDesignation());
	            // Keep track of the course id
	            cb.setTag(course.getId());
	            coursesLayout.addView(cb);
	            
	            courseCheckboxes[i] = cb;
	            
	            for (int id : defn.courses) {
	            	if (id == course.getId()) {
	            		cb.setChecked(true);
	            		break;
	            	}
	            }
			}
		}
		
		for (int i = 0, nameindex = 0; i < typeCheckboxes.length; i++, nameindex++) {
			String typeName = types[nameindex];
			// We need to skip the item that reads
			// "(Assignment Type)" or something like that
			if (typeName.equals(
					getResources()
					.getString(R.string.default_assignment_type))) {
				// Keep indexing correctly.
				i--;
				continue;
			}
		    CheckBox cb = new CheckBox(ctx);
		    cb.setText(typeName);
	        // Keep track of the type name
	        cb.setTag(typeName);
	        
		    typesLayout.addView(cb);
		    
		    typeCheckboxes[i] = cb;
		    
		    for (String type : defn.types) {
		    	if (typeName.equals(type)) {
		    		cb.setChecked(true);
		    		break;
		    	}
		    }
		}
		
		final FilterListener listener = (FilterListener)getActivity();
		
		return new AlertDialog.Builder(getActivity())
			.setView(rootView)
			.setTitle("Hello")
			.setPositiveButton(android.R.string.ok, new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					Toast.makeText(getActivity(), "Filtered!", Toast.LENGTH_LONG).show();
					
					int courseCount = 0;
					for (CheckBox cb : courseCheckboxes) {
						if (cb != null && cb.isChecked()) {
							courseCount++;
						}
					}
					// Turn the selectedCourses list into the int[] for FilterDefinition
					final int[] courseIds = new int[courseCount];
					int index = 0;
					for (CheckBox cb : courseCheckboxes) {
						if (index >= courseCount) {
							// Shouldn't happen, unless you managed to check off a box between these two loops...
							// But if this does happen, then break out of the loop.
							break;
						}
						if (cb != null && cb.isChecked()) {
							courseIds[index] = (Integer)cb.getTag();
							index++;
						}
					}
					
					ArrayList<String> selectedTypes = new ArrayList<String>();
					for (CheckBox cb : typeCheckboxes) {
						if (cb.isChecked()) {
							selectedTypes.add((String) cb.getTag());
						}
					}
					final String[] types = new String[selectedTypes.size()];
					for (int i = 0; i < types.length; i++) {
						types[i] = selectedTypes.get(i);
					}
					
					SortBy sorter = SortBy.fromInt(getSortNumber(rootView, sortRadios));
					
					boolean showCompleted = getShowCompleted(rootView);
					
					FilterDefinition defn = new FilterDefinition(sorter, showCompleted, courseIds, types);
					
					listener.onNewFilter(defn);
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
