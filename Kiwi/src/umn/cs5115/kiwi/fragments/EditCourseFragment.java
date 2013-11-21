package umn.cs5115.kiwi.fragments;

import umn.cs5115.kiwi.Course;
import umn.cs5115.kiwi.DatabaseHandler;
import umn.cs5115.kiwi.EditAssignmentActivity;
import umn.cs5115.kiwi.EditCourseActivity;
import umn.cs5115.kiwi.R;
import umn.cs5115.kiwi.ui.DoneBar.DoneBarListenable;
import umn.cs5115.kiwi.ui.DoneBar.DoneBarListener;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

public class EditCourseFragment extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.edit_course_fragment, container);
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
