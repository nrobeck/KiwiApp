package umn.cs5115.kiwi.fragments;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import umn.cs5115.kiwi.Assignment;
import umn.cs5115.kiwi.Course;
import umn.cs5115.kiwi.DatabaseHandler;
import umn.cs5115.kiwi.EditAssignmentActivity;
import umn.cs5115.kiwi.R;
import umn.cs5115.kiwi.adapter.DummyItemAdapter;
import umn.cs5115.kiwi.assignment.AssignmentUtils;
import umn.cs5115.kiwi.ui.DateButton;
import umn.cs5115.kiwi.ui.DoneBar.DoneBarListenable;
import umn.cs5115.kiwi.ui.DoneBar.DoneBarListener;
import umn.cs5115.kiwi.ui.TimeButton;
import android.app.Activity;
import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.datetimepicker.date.DatePickerDialog;
import com.android.datetimepicker.date.DatePickerDialog.OnDateSetListener;
import com.android.datetimepicker.time.RadialPickerLayout;
import com.android.datetimepicker.time.TimePickerDialog.OnTimeSetListener;

public class EditAssignmentFragment extends Fragment {
    private EditText assignmentName;
    private String[] typesArray;
    private Spinner typesSpinner;
    
    private View findView(int id) {
        return getView().findViewById(id);
    }
    
    private void loadLocals(View layout) {
        try {
            assignmentName = (EditText) layout.findViewById(R.id.editText1);
        } catch (Exception e) {
            Log.e("EditAssignmentFragment", "loadLocals failed", e);
        }
    }
    
    /**
     * Go over the inputs in the layout, calling setError where necessary to
     * inform the user what they need to do.
     */
    @SuppressWarnings("unused")
    private void setErrorsAsNeeded() {
        if (TextUtils.isEmpty(assignmentName.getText().toString())) {
            assignmentName.setError("Cannot be empty!");
        }
    }

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View layout = inflater.inflate(R.layout.edit_assignment_fragment, container);
		
		loadLocals(layout);
		
		typesSpinner  = (Spinner)layout.findViewById(R.id.assignment_types_spinner);
		typesArray = getResources().getStringArray(R.array.assignment_types);
		DummyItemAdapter<String> typesAdapter = new DummyItemAdapter<String>(getActivity(),
				android.R.layout.simple_spinner_dropdown_item, typesArray, getResources().getString(R.string.default_assignment_type)) {
					@Override
					public Object getViewTag(String obj) {
						return obj;
					}

					@Override
					public String getViewText(int position, String obj, boolean isDummy) {
						return obj;
					}
		};

		typesSpinner.setAdapter(typesAdapter);
		
		final DateButton db = (DateButton)layout.findViewById(R.id.date_button);
		final TimeButton tb = (TimeButton)layout.findViewById(R.id.time_button);

		Calendar current = Calendar.getInstance(Locale.US);
		final int year = current.get(Calendar.YEAR),
			month = current.get(Calendar.MONTH),
			day = current.get(Calendar.DAY_OF_MONTH),
			hour = current.get(Calendar.HOUR_OF_DAY),
			minute = current.get(Calendar.MINUTE);

		db.setDate(year, month, day);
		tb.setTime(hour, minute);
		
        db.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                AssignmentUtils.showDateEditDialog(getFragmentManager(), year, month, day, new OnDateSetListener() {
					@Override
					public void onDateSet(DatePickerDialog dialog, int year, int monthOfYear,
							int dayOfMonth) {
						db.setDate(year, monthOfYear, dayOfMonth);
					}
				});
            }
        });
        
        tb.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				AssignmentUtils.showTimeEditDialog(getFragmentManager(), hour, minute, new OnTimeSetListener() {
					
					@Override
					public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {
						tb.setTime(hourOfDay, minute);
					}
				});
			}
		});
		
		//TODO: Need to put in a textbook spinner in the edit assignment fragment
		//TODO: Need to change the textbook spinner values to the current selected course's textbooks
		//http://stackoverflow.com/questions/1337424/android-spinner-get-the-selected-item-change-event
		
/*		final Spinner textbookSpinner = (Spinner) getActivity().findViewById(R.id.textbook_spinner);	
		Spinner courseNameSpinner = (Spinner) getActivity().findViewById(R.id.spinner2); //Course Name Spinner
		courseNameSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parentView, View selectedItemView,
					int position, long id) {
				// TODO Change the textbook spinner contents
				Log.i("EditAssignmentFragment", "Course Name Changed");
				
				//TODO: Put the dummy first item in the spinner to (Textbook Name) even if there are no textbooks
				List<String> textbookSpinnerArray =  new ArrayList<String>();
				textbookSpinnerArray.add("ALL THE TEXTBOOKS");
				
				ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, textbookSpinnerArray);
			    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			    Spinner Items = (Spinner) getActivity().findViewById(R.id.textbook_spinner);
			    Items.setAdapter(adapter);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parentView) {
				// TODO Auto-generated method stub
				
			}
		});
*/
		
		return layout;
	}

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        
        setRetainInstance(true);
        
        final Activity activity = getActivity();
        final EditAssignmentActivity eActivity = (EditAssignmentActivity) activity;
        
        final boolean isEdit = eActivity.isEdit();
        
        if (isEdit) {
        	// Pick the assignment type in the spinner.
    		int selectedTypeIndex = 0;
    		for (int i = 0; i < typesArray.length; i++) {
    			if (typesArray[i].equals("Selected Type In Assignment!")) {
    				selectedTypeIndex = i;
    			}
    		}
    		typesSpinner.setSelection(selectedTypeIndex, false);
        }
        
        //TODO: Pull all the course names from the Database to fill the Course Name Spinner
        DatabaseHandler dbHandler = new DatabaseHandler(getActivity());
        List<String> courseSpinnerArray =  new ArrayList<String>();
        courseSpinnerArray.add("(Course)");
        Course[] coursesArray = dbHandler.getCourses(); //TODO: This is NOT returning any courses when there have been courses add?
        int selectedCourseIndex = 0;
        for	(int i = 0; i < coursesArray.length; i++) {
        	Course course = coursesArray[i];
        	String text;
        	if (TextUtils.isEmpty(course.getCourseDesignation())) {
        		text = course.getCourseTitle();
        	} else {
        		text = String.format("%s: %s", course.getCourseDesignation(), course.getCourseTitle());
        	}
        	courseSpinnerArray.add(text); //This will require the course designation though?
        	
        	// TODO: Grab assignment from activity?
        	if (course.getId() == activity.getTaskId()) {
        		selectedCourseIndex = i + 1;
        	}
        }
        
        DummyItemAdapter<Course> adapter = new DummyItemAdapter<Course>(
        		getActivity(), android.R.layout.simple_spinner_dropdown_item, coursesArray, new Course()) {
			@Override
			public Object getViewTag(Course obj) {
				return Integer.valueOf(obj.getId());
			}

			@Override
			public String getViewText(int position, Course course, boolean isDummy) {
				Log.d("EditAssignmentFragment", String.format("Courses getViewText: %d %s %s", position, course.getCourseTitle(), isDummy));
				if (isDummy) {
					return getResources().getString(R.string.default_course);
				} else if (TextUtils.isEmpty(course.getCourseDesignation())) {
					return course.getCourseTitle();
				} else {
					return String.format("%s: %s", course.getCourseDesignation(), course.getCourseTitle());
				}
			}
		};
        Spinner Items = (Spinner) findView(R.id.courses_spinner);
        Items.setAdapter(adapter);
        Items.setSelection(selectedCourseIndex, false);
        
        if (activity instanceof DoneBarListenable) {
            final DoneBarListenable listenable = (DoneBarListenable) getActivity();
            Log.i("EditAssignmentFragment", "Registering listener.");
            listenable.addDoneBarListener(new DoneBarListener() {
                @Override
                public boolean onDone() {
                    Log.i("EditAssignmentFragment", "DoneBarListener.onDone()");
                    boolean isValidInput = false;
                    int selectedCourse = 0;
                    String assignmentType = "";
                    String assignmentName = "";
                    String textbook = "";
                    String notes = "";
                    
                    //Store the assignment
                    DatabaseHandler dbHandler = new DatabaseHandler(activity);
                    
                    //Pulling the information out of the activity's fields
                    Spinner courseNameSpinner = (Spinner) activity.findViewById(R.id.courses_spinner); //Course Name
                    if(courseNameSpinner.getSelectedItemPosition() > 0) {
                    	Object tag = courseNameSpinner.getSelectedView().getTag();
                    	if (tag instanceof Integer) {
                    		selectedCourse = (Integer)tag;
                    	} else {
                    		Log.e("EditAssignmentFragment", "selected course view tag is not Integer!");
                    	}
                    }
                    else {
                    	selectedCourse = 0;
                    }
                    
                    // Assignment type
                    int typePos = typesSpinner.getSelectedItemPosition();
                    if (typePos > 0) {
                    	Object tag = typesSpinner.getSelectedView().getTag();
                    	if (tag instanceof String) {
                    		assignmentType = (String) tag;
                    	} else {
                    		Log.e("EditAssignmentFragment", "Selected type view has non-String tag...");
                    	}
                    	//Log.e("ERROR: EditAssignmentFragment", ""+assignmentType);
                    }
                    else {
                    	assignmentType = null;
                    	Log.d("EditAssignmentFragment", "type pos: " + typePos);
                    }
                    
                    EditText assignmentNameEditText = (EditText) activity.findViewById(R.id.editText1); //Assignment Name   
                    assignmentName = assignmentNameEditText.getText().toString();
                    //Put this on each of the required fields and change the Toast message to say what is wrong
                    if (assignmentName.isEmpty()) {
                        Toast.makeText(getActivity(), "No Assignment Name Specified", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                    
                    Spinner textbookSpinner = (Spinner) activity.findViewById(R.id.textbook_spinner); //Textbook Name
                    if(textbookSpinner.getSelectedItemPosition() > 0) {
                    	textbook = textbookSpinner.getSelectedItem().toString();
                    }
                    else {
                    	textbook = "";
                    }
                    	
                    //TODO: Due Date
                    //TODO: Reminder and number picker for this field
                    //TODO: Field to specify how many times to repeat, etc.
                    
                    EditText notesEditText = (EditText) activity.findViewById(R.id.editText2);
                    notes = notesEditText.getText().toString();
                    
                    // TODO: Validate input...
             //       if (!isValidInput) {
             //           Toast.makeText(getActivity(), "Invalid input", Toast.LENGTH_SHORT).show();
             //           return false;
             //       }
                    
                    int assignmentId = eActivity.getAssignmentId();
                    Assignment assignment = new Assignment(0, assignmentName, selectedCourse, assignmentType, "", 0, 0, 0, "", notes, textbook); //TODO: the information into the assignment object 
                    assignment.setId(assignmentId);
                    // fill in assignment
                    
                    if (isEdit) {
                    	//TODO: Need to pass in the assignment ID
                    	dbHandler.editAssignment(assignment);
                    } else {
                    	dbHandler.addAssignment(assignment);
                    }
                    
                    return true;
                }
                
                @Override
                public void onCancel() {
                    Log.i("EditAssignmentFragment", "DoneBarListener.onCancel()");
                }
            });
        }
    }
}
