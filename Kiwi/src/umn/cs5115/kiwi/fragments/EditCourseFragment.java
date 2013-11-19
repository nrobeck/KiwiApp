package umn.cs5115.kiwi.fragments;

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
                    String courseTitle;
                	String courseDesignation;
                	String startTime;
                	String endTime;
                	String location = "";
                	String startDate;
                	String endDate;
                	String rRule;
                	String notes = "";
                	String textbooks = "";
                    
                	//TODO: Pull the information out of the activity's fields

                	//TODO: Checking to make sure the data if valid
                	
                	//TODO: Put the information into a Course object and store it
                	
                    //TODO: Validate input...
                    Toast.makeText(getActivity(), "Invalid input!", Toast.LENGTH_SHORT).show();
                    
                    return false;
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
