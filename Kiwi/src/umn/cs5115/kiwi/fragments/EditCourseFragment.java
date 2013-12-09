package umn.cs5115.kiwi.fragments;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.fortuna.ical4j.model.Date;
import net.fortuna.ical4j.model.Dur;
import net.fortuna.ical4j.model.Recur;
import net.fortuna.ical4j.model.WeekDay;
import umn.cs5115.kiwi.DatabaseHandler;
import umn.cs5115.kiwi.EditCourseActivity;
import umn.cs5115.kiwi.R;
import umn.cs5115.kiwi.assignment.AssignmentUtils;
import umn.cs5115.kiwi.assignment.AssignmentUtils.EmptyErrorTextWatcher;
import umn.cs5115.kiwi.model.Course;
import umn.cs5115.kiwi.ui.DateButton;
import umn.cs5115.kiwi.ui.DoneBar.DoneBarListenable;
import umn.cs5115.kiwi.ui.DoneBar.DoneBarListener;
import umn.cs5115.kiwi.ui.TextbookEntry;
import umn.cs5115.kiwi.ui.TimeButton;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.android.datetimepicker.date.DatePickerDialog;
import com.android.datetimepicker.date.DatePickerDialog.OnDateSetListener;
import com.android.datetimepicker.time.RadialPickerLayout;
import com.android.datetimepicker.time.TimePickerDialog.OnTimeSetListener;

public class EditCourseFragment extends Fragment {

	@Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        setRetainInstance(true);
    }
	
	private boolean onDone(Activity activity) {
        Log.i("EditCourseFragment", "DoneBarListener.onDone()");
        
        String courseName;
        String courseDesignation;
        String startTime = "";
        String endTime = "";
        String location = "";
        String startDate = "";
        String endDate = "";
        String rRule = "";
        String notes = "";
        String textbooks = null;
        
        //TODO: Pull the information out of the activity's fields
        EditText courseNameEditText = (EditText) getView().findViewById(R.id.editText1); //Course Name   
        courseName = courseNameEditText.getText().toString();
        //Put this on each of the required fields and change the Toast message to say what is wrong
        if (courseName.isEmpty()) {
            courseNameEditText.requestFocus(); // jump there
            courseNameEditText.setError("Must give a course name.");
            Toast.makeText(getActivity(), "No Course Name Specified", Toast.LENGTH_SHORT).show();
            return false;
        }
        
        EditText courseDesignationEditText = (EditText) activity.findViewById(R.id.editText2); //Course Designation   
        courseDesignation = courseDesignationEditText.getText().toString();
        //Put this on each of the required fields and change the Toast message to say what is wrong
        if (courseDesignation.isEmpty()) {
            courseDesignationEditText.requestFocus(); // jump there
            courseDesignationEditText.setError("Must give a course designation.");
            Toast.makeText(getActivity(), "No Course Designation Specified", Toast.LENGTH_SHORT).show();
            return false;
        }
        
        //TODO Start Time
        
        //TODO End Time
        
        //TODO Start Date
        
        //TODO End Date
        
        //TODO rRule
        //Right now there are 7 check boxes for the days of the week
        
        EditText courseLocationEditText = (EditText) activity.findViewById(R.id.course_location); //Location
        location = courseLocationEditText.getText().toString();
        if (location == null) {
            location = "";
        }
        
        ArrayList<String> textbooksList = new ArrayList<String>();
        ViewGroup textbookEntries = (ViewGroup)getView().findViewById(R.id.course_textbooks_layout);
        for (int i = 0; i < textbookEntries.getChildCount(); i++) {
            try {
                TextbookEntry entry = (TextbookEntry)textbookEntries.getChildAt(i);
                if (!entry.isEmpty()) {
                    textbooksList.add(entry.getInput());
                }
            } catch (Exception e) {
                // Most likely a ClassCastException, if for some reason
                // we added a child view that isn't TextbookEntry.
                Log.e("EditCourseFragment", "Exception caught pulling out textbooks", e);
            }
        }
        if (textbooksList.size() > 0) {
            textbooks = TextUtils.join(Course.TEXTBOOK_DELIMITER, textbooksList);
        } else {
            textbooks = null;
        }
        
        EditText notesEditText = (EditText) activity.findViewById(R.id.notes); //Notes
        notes = notesEditText.getText().toString();
        
        //TODO: Checking to make sure the data if valid
        
        DatabaseHandler dbHandler = new DatabaseHandler(activity);
        //Getting the course color
        //modulus
        String[] colorsForCourses = getResources().getStringArray(R.array.course_color_strings);
        String courseColor = colorsForCourses[dbHandler.getCourseCount() % colorsForCourses.length];
        
        //TODO: Put the information into a Course object and store it
        Course course = new Course(0, 
                                   courseName, 
                                   courseDesignation, 
                                   startTime, 
                                   endTime, 
                                   location, 
                                   startDate, 
                                   endDate, 
                                   rRule, 
                                   notes, 
                                   textbooks, 
                                   courseColor);
        dbHandler.addCourse(course);
        return true;
	}

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
		
		EditText courseName = (EditText)layout.findViewById(R.id.editText1);
		courseName.addTextChangedListener(new EmptyErrorTextWatcher(courseName, "Must give a course name."));
		EditText courseDes = (EditText)layout.findViewById(R.id.editText2);
		courseDes.addTextChangedListener(new EmptyErrorTextWatcher(courseDes, "Must give a course designation."));
		
		Button addTextbookButton = (Button) layout.findViewById(R.id.course_add_textbook_btn);
		final ViewGroup textbookEntries = (ViewGroup) layout.findViewById(R.id.course_textbooks_layout);
		addTextbookButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                TextbookEntry entry = new TextbookEntry(getActivity());
                textbookEntries.addView(entry);
            }
        });
		
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
        
        final boolean isImport = eActivity.isImport();
        
        EditText courseName = (EditText)getView().findViewById(R.id.editText1);
        courseName.addTextChangedListener(new EmptyErrorTextWatcher(courseName, "Must give a course name."));
        EditText courseDes = (EditText)getView().findViewById(R.id.editText2);
        courseDes.addTextChangedListener(new EmptyErrorTextWatcher(courseDes, "Must give a course designation."));
        
        if (isImport) {
            String eventName = eActivity.getIntent().getStringExtra(EditCourseActivity.EXTRA_EVENT_NAME);
            
            // Matches the following pattern:
            // <course designation><space?><colon or dash><space?><course name>
            String namePattern = "(.*?)\\s?[-:]\\s?(.*)";
            Matcher matcher = Pattern.compile(namePattern).matcher(eventName);
            if (matcher.find()) {
                courseName.setText(matcher.group(2).trim());
                courseDes.setText(matcher.group(1).trim());
            } else {
                courseName.setText(eventName);
            }
            
            String eventLoc = eActivity.getIntent().getStringExtra(EditCourseActivity.EXTRA_EVENT_LOC);
            EditText location = (EditText)getView().findViewById(R.id.course_location);
            location.setText(eventLoc);
            
            final DateButton startDB = (DateButton)getView().findViewById(R.id.start_date_button);
            final DateButton endDB = (DateButton)getView().findViewById(R.id.end_date_button);
            final TimeButton startTB = (TimeButton)getView().findViewById(R.id.start_time_button);
            final TimeButton endTB = (TimeButton)getView().findViewById(R.id.end_time_button);
            
            long startTime = eActivity.getIntent().getLongExtra(EditCourseActivity.EXTRA_EVENT_START, System.currentTimeMillis());
            long endTime = eActivity.getIntent().getLongExtra(EditCourseActivity.EXTRA_EVENT_END, System.currentTimeMillis());
            
            Log.d("EditCourseFragment", "Start: " + new AssignmentUtils.DueDateBuilder(startTime).toString());
            Log.d("EditCourseFragment", "End: " + new AssignmentUtils.DueDateBuilder(endTime).toString());
            
            String duration = eActivity.getIntent().getStringExtra(EditCourseActivity.EXTRA_EVENT_DURATION);
            Log.d("EditCourseFragment", "duration: " + duration);
            
            // If the end time is quite a long time ago, then calculate the end time
            // from the start time + duration
            if (endTime < 5000000 && duration != null) {
                // This assumes the duration is being specified in seconds...
                Calendar cal = Calendar.getInstance();
                cal.setTimeInMillis(startTime);
                cal.add(Calendar.SECOND, new Dur(duration).getSeconds());
                endTime = cal.getTimeInMillis();
            }
            Log.d("EditCourseFragment", "End: " + new AssignmentUtils.DueDateBuilder(endTime).toString());

            
            Recur r;
            try {
                r = new Recur(eActivity.getIntent().getStringExtra(EditCourseActivity.EXTRA_EVENT_RRULE));
            } catch (Exception e) {
                r = new Recur();
                r.setUntil(new Date(AssignmentUtils.END_OF_SEMESTER));
            }
            
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(startTime);
            startDB.setDate(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
            startTB.setTime(cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE));
            
            cal.setTimeInMillis(r.getUntil().getTime());
            endDB.setDate(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
            
            cal.setTimeInMillis(endTime);
            endTB.setTime(cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE));
            
            int[] dayCbIds = new int[] {
                    R.id.check_box_sunday, R.id.check_box_monday, R.id.check_box_tuesday,
                    R.id.check_box_wednesday, R.id.check_box_thursday, R.id.check_box_friday,
                    R.id.check_box_saturday
            };
            WeekDay[] dayList = new WeekDay[] {
                    WeekDay.SU, WeekDay.MO, WeekDay.TU, WeekDay.WE, WeekDay.TH,
                    WeekDay.FR, WeekDay.SA
            };
            
            for (int i = 0; i < 7; i++) {
                if (r.getDayList().contains(dayList[i])) {
                    CheckBox cb = (CheckBox) getView().findViewById(dayCbIds[i]);
                    cb.setChecked(true);
                }
            }
        }
        
        if (activity instanceof DoneBarListenable) {
            final DoneBarListenable listenable = (DoneBarListenable) getActivity();
            Log.i("EditCourseFragment", "Registering listener.");
            listenable.addDoneBarListener(new DoneBarListener() {
                @Override
                public boolean onDone() {
                    return EditCourseFragment.this.onDone(activity);
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
