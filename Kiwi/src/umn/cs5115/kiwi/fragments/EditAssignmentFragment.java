package umn.cs5115.kiwi.fragments;

import umn.cs5115.kiwi.EditAssignmentActivity;
import umn.cs5115.kiwi.Assignment;
import umn.cs5115.kiwi.DatabaseHandler;
import umn.cs5115.kiwi.R;
import umn.cs5115.kiwi.assignment.AssignmentUtils;
import umn.cs5115.kiwi.ui.DoneBar.DoneBarListenable;
import umn.cs5115.kiwi.ui.DoneBar.DoneBarListener;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class EditAssignmentFragment extends Fragment {
    private EditText assignmentName;
    
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
		
		//TODO: Need to put in a textbook spinner in the edit assignment fragment
		//TODO: Need to change the textbook spinner values to the current selected course's textbooks
		//http://stackoverflow.com/questions/1337424/android-spinner-get-the-selected-item-change-event
		
/*		Spinner textbookSpinner = (Spinner) getActivity().findViewById(R.id.textbook_spinner);	
		Spinner courseNameSpinner = (Spinner) getActivity().findViewById(R.id.spinner2); //Course Name Spinner
		courseNameSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parentView, View selectedItemView,
					int position, long id) {
				// TODO Change the textbook spinner contents
				Log.i("EditAssignmentFragment", "Course Name Changed");
				
				//TODO: Put the dummy first item in the spinner to (Textbook Name) even if there are no textbooks
				//textbookSpinner.add
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
        
        final Activity activity = getActivity();
        final EditAssignmentActivity eActivity = (EditAssignmentActivity) activity;
        
        final boolean isEdit = eActivity.isEdit();
        
        //TODO: Remove all course name from Course Name Spinner
        
        //TODO: Pull all the course names from the Database to fill the Course Name Spinner
        
        if (activity instanceof DoneBarListenable) {
            final DoneBarListenable listenable = (DoneBarListenable) getActivity();
            Log.i("EditAssignmentFragment", "Registering listener.");
            listenable.addDoneBarListener(new DoneBarListener() {
                @Override
                public boolean onDone() {
                    Log.i("EditAssignmentFragment", "DoneBarListener.onDone()");
                    boolean isValidInput = false;
                    String courseName = "";
                    String assignmentType = "";
                    String assignmentName = "";
                    String textbook = "";
                    String notes = "";
                    
                    //Store the assignment
                    DatabaseHandler dbHandler = new DatabaseHandler(activity);
                    
                    //Pulling the information out of the activity's fields
                    Spinner courseNameSpinner = (Spinner) activity.findViewById(R.id.spinner1); //Course Name
                    if(courseNameSpinner.getSelectedItemPosition() > 0) {
                    	courseName = courseNameSpinner.getSelectedItem().toString();
                    }
                    else {
                    	courseName = "";
                    }
                    
                    Spinner assignmentTypeSpinner = (Spinner) activity.findViewById(R.id.spinner2); //Assignment Type
                    if(assignmentTypeSpinner.getSelectedItemPosition() > 0) {
                    	assignmentType = assignmentTypeSpinner.getSelectedItem().toString();
                    	Log.e("ERROR: EditAssignmentFragment", ""+assignmentType);
                    }
                    else {
                    	assignmentType = "";
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
                    Assignment assignment = new Assignment(0, assignmentName, 0, assignmentType, "", 0, 0, 0, "", notes, textbook); //TODO: the information into the assignment object 
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
        
        findView(R.id.spinner3).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                AssignmentUtils.showTimeEditDialog(getFragmentManager(), null, null);
            }
        });
    }
}
