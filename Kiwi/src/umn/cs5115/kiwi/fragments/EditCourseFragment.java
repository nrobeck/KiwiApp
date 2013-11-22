package umn.cs5115.kiwi.fragments;

import java.util.Calendar;
import java.util.Locale;

import com.android.datetimepicker.date.DatePickerDialog;
import com.android.datetimepicker.date.DatePickerDialog.OnDateSetListener;
import com.android.datetimepicker.time.RadialPickerLayout;
import com.android.datetimepicker.time.TimePickerDialog.OnTimeSetListener;

import umn.cs5115.kiwi.Course;
import umn.cs5115.kiwi.DatabaseHandler;
import umn.cs5115.kiwi.EditAssignmentActivity;
import umn.cs5115.kiwi.EditCourseActivity;
import umn.cs5115.kiwi.R;
import umn.cs5115.kiwi.assignment.AssignmentUtils;
import umn.cs5115.kiwi.ui.DateButton;
import umn.cs5115.kiwi.ui.TimeButton;
import umn.cs5115.kiwi.ui.DoneBar.DoneBarListenable;
import umn.cs5115.kiwi.ui.DoneBar.DoneBarListener;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;

public class EditCourseFragment extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View layout = inflater.inflate(R.layout.edit_course_fragment, container);
		
		Calendar current = Calendar.getInstance(Locale.US);
		final int year = current.get(Calendar.YEAR),
				month = current.get(Calendar.MONTH),
				day = current.get(Calendar.DAY_OF_MONTH),
				hour = current.get(Calendar.HOUR_OF_DAY),
				minute = current.get(Calendar.MINUTE);
		final int incrementedMonth = month + 2;
		final int incrementedHour = hour + 1;
		
		final DateButton startDB = (DateButton)layout.findViewById(R.id.start_date_button);
		final DateButton endDB = (DateButton)layout.findViewById(R.id.end_date_button);
		
		//Start Course Date
		startDB.setDate(year, month, day);
		
		startDB.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				AssignmentUtils.showDateEditDialog(getFragmentManager(), year, month, day, new OnDateSetListener() {
					@Override
					public void onDateSet(DatePickerDialog dialog, int year, int monthOfYear,
							int dayOfMonth) {
						startDB.setDate(year, monthOfYear, dayOfMonth);
					}
				});
			}
		});
		
		//End Course Date
		//Increment the end course dialog by 2 months by default
		endDB.setDate(year, incrementedMonth, day); //This is why there is 2 months difference between pickers
		
		endDB.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				AssignmentUtils.showDateEditDialog(getFragmentManager(), year, incrementedMonth, day, new OnDateSetListener() {
					@Override
					public void onDateSet(DatePickerDialog dialog, int year, int monthOfYear,
							int dayOfMonth) {
						endDB.setDate(year, monthOfYear, dayOfMonth);
					}
				});
			}
		});		
		
		final TimeButton startTB = (TimeButton)layout.findViewById(R.id.start_time_button);
		final TimeButton endTB = (TimeButton)layout.findViewById(R.id.end_time_button);
		
		//Start Course Time
		startTB.setTime(hour, minute);

		startTB.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				AssignmentUtils.showTimeEditDialog(getFragmentManager(), hour, minute, new OnTimeSetListener() {

					@Override
					public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {
						startTB.setTime(hourOfDay, minute);
					}
				});
			}
		});
		
		//End Course Time
		//Increment the end course time by 1 hour by default
		endTB.setTime(incrementedHour, minute); //This is why there is 1 hour difference between pickers 

		endTB.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				AssignmentUtils.showTimeEditDialog(getFragmentManager(), incrementedHour, minute, new OnTimeSetListener() {

					@Override
					public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {
						endTB.setTime(hourOfDay, minute);
					}
				});
			}
		});
		
		return layout;
	}

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        
        final Activity activity = getActivity();
        final EditCourseActivity eActivity = (EditCourseActivity) activity;
        
        final boolean isEdit = eActivity.isEdit();
        
        if (activity instanceof DoneBarListenable) {
            final DoneBarListenable listenable = (DoneBarListenable) getActivity();
            Log.i("EditCourseFragment", "Registering listener.");
            listenable.addDoneBarListener(new DoneBarListener() {
                @Override
                public boolean onDone() {
                    Log.i("EditCourseFragment", "DoneBarListener.onDone()");
                    
                    boolean isValidInput = false;
                    String courseName;
                	String courseDesignation;
                	String startTime = "";
                	String endTime = "";
                	String location = "";
                	String startDate = "";
                	String endDate = "";
                	String rRule = "";
                	String notes = "";
                	String textbooks = "";
                    
                	//TODO: Pull the information out of the activity's fields
                	EditText courseNameEditText = (EditText) activity.findViewById(R.id.editText1); //Course Name   
                	courseName = courseNameEditText.getText().toString();
                    //Put this on each of the required fields and change the Toast message to say what is wrong
                    if (courseName.isEmpty()) {
                        Toast.makeText(getActivity(), "No Course Name Specified", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                	
                    EditText courseDesignationEditText = (EditText) activity.findViewById(R.id.editText2); //Course Designation   
                    courseDesignation = courseDesignationEditText.getText().toString();
                    //Put this on each of the required fields and change the Toast message to say what is wrong
                    if (courseDesignation.isEmpty()) {
                        Toast.makeText(getActivity(), "No Course Designation Specified", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                    
                    //TODO Start Time
                    
                    //TODO End Time
                    
                    //TODO Start Date
                    
                    //TODO End Date
                    
                    //TODO rRule
                    //Right now there are 7 check boxes for the days of the week
                    
                    EditText courseLocationEditText = (EditText) activity.findViewById(R.id.editText3); //Location
                    location = courseLocationEditText.getText().toString();
                    if (location == null) {
                    	location = "";
                    }
                    
                    EditText textbookEditText = (EditText) activity.findViewById(R.id.textbooks); //Textbooks
                    textbooks = textbookEditText.getText().toString();
                    if (textbooks == null) {
                    	textbooks = "";
                    }
                    
                    EditText notesEditText = (EditText) activity.findViewById(R.id.notes); //Notes
                    notes = notesEditText.getText().toString();
                    if (notes == null) {
                    	notes = "";
                    }
                    
                	//TODO: Checking to make sure the data if valid
                	
                	//TODO: Put the information into a Course object and store it
                    Course course = new Course(0, courseName, courseDesignation, startTime, endTime, location, startDate, endDate, rRule, notes, textbooks);
                	DatabaseHandler dbHandler = new DatabaseHandler(activity);
                	dbHandler.addCourse(course);
                	return true;
                    
                    //TODO: Validate input...
                    //Toast.makeText(getActivity(), "Invalid input!", Toast.LENGTH_SHORT).show();
                    
                    //return false;
                }
                
                @Override
                public void onCancel() {
                    Log.i("EditCourseFragment", "DoneBarListener.onCancel()");
                }
            });
        } else {
            // TODO: Decide if we actually want to be able to use this fragment
            // in a non-DoneCancel activity.
            Log.e("EditCourseFragment", "Parent activity does not implement DoneBarListenable, FYI.");
        }
    }
}
