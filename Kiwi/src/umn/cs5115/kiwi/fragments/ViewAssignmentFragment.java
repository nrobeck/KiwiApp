package umn.cs5115.kiwi.fragments;

import java.text.SimpleDateFormat;
import java.util.Locale;

import umn.cs5115.kiwi.EditAssignmentActivity;
import umn.cs5115.kiwi.R;
import umn.cs5115.kiwi.Utils;
import umn.cs5115.kiwi.assignment.AssignmentUtils.DueDateBuilder;
import umn.cs5115.kiwi.model.Assignment;
import umn.cs5115.kiwi.ui.DateButton;
import umn.cs5115.kiwi.ui.TimeButton;
import android.app.ActionBar;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.b50.gesticulate.SwipeDetector;

public class ViewAssignmentFragment extends Fragment {
    public static final String ASSIGNMENT_ARG = "assignment";
    private Assignment mAssignment;
    
    public static ViewAssignmentFragment newInstance(Assignment assignment) {
        ViewAssignmentFragment fragment = new ViewAssignmentFragment();
        Bundle args = new Bundle();
        args.putParcelable(ASSIGNMENT_ARG, assignment);
        fragment.setArguments(args);
        
        return fragment;
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setHasOptionsMenu(true);
        
        Bundle args = getArguments();
        if (args != null) {
            mAssignment = (Assignment)args.getParcelable(ASSIGNMENT_ARG);
        }
        
        
    }
    
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.view_assignment_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch(item.getItemId()) {
    	case 1:
    		
    		return true;
    	case 2:
    		
    		return true;
    	}
    	return super.onOptionsItemSelected(item);
    }
    
    @Override
    public void onResume() {
        super.onResume();
        if (isAdded()) {
            ActionBar ab = getActivity().getActionBar();
            // Enable the 'Up' button on the activity.
            ab.setDisplayHomeAsUpEnabled(true);
            if (mAssignment == null) {
                ab.setTitle("Viewing assignment");
            } else {
                ab.setTitle(mAssignment.getName());
            }
        }
    }

    // Note: onCreateView is called after onCreate, so mAssignment will be
    // available here.
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.view_assignment_fragment, null);
        
        if (mAssignment != null) {
            //assignment name
        	TextView assignmentNameTextView = (TextView) view.findViewById(R.id.assignmentName);
        	assignmentNameTextView.setText(mAssignment.getName());
        	
        	//assignment course
        	TextView assignmentCourseTextView = (TextView) view.findViewById(R.id.assignmentCourse);
        	assignmentCourseTextView.setText("Course: " + mAssignment.getCourseDesignation());
        	
        	//assignment type
        	TextView assignmentTypeTextView = (TextView) view.findViewById(R.id.assignmentType);
        	assignmentTypeTextView.setText("Type: " + mAssignment.getType());
        	
        	//assignment book
        	TextView assignmentBookTextView = (TextView) view.findViewById(R.id.assignmentBook);
        	assignmentBookTextView.setText("TextBook: " + mAssignment.getTextbook());
        	
        	//assignment due date
        	TextView assignmentDueTextView = (TextView) view.findViewById(R.id.assignmentDue);
        	assignmentDueTextView.setText(new SimpleDateFormat("h:mm aaa, EEE, MMM dd, ''yy", Locale.US).format(new DueDateBuilder(mAssignment.getDueMillis()).toDate()));
        	
        	//assignment notes
        	TextView assignmentNotesTextView = (TextView) view.findViewById(R.id.assignmentNotes);
        	assignmentNotesTextView.setText(mAssignment.getNotes());
        	
        } else {
            // Hopefully we aren't stupid enough to not pass an assignment in.
            // But if we are, log it.
            Log.e("ViewAssignmentFragment", "Assignment is null.");
        }
        
        final GestureDetector gestureDetector = new GestureDetector(getActivity(), new SimpleOnGestureListener() {
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2,
                    float velocityX, float velocityY) {
                try {
                    final SwipeDetector detector = new SwipeDetector(e1, e2, velocityX, velocityY);
                    if (detector.isRightSwipe()) {
                        // Swipe to right -> go back to the assignment list.
                        getActivity().onBackPressed();
                    } else if (detector.isLeftSwipe()) {
                        // Swipe to left -> launch edit assignment activity.
                        Intent editIntent = Utils.goToAddAssignment(getActivity().getBaseContext());
                        editIntent
                            .putExtra(EditAssignmentActivity.EXTRA_IS_EDIT, true)
                            .putExtra(EditAssignmentActivity.EXTRA_ASSIGNMENT, mAssignment.getId());
                        // Start the edit activity from the main activity, so the
                        // launch animation is used (sliding views)
                        getActivity().startActivity(editIntent);
                    } else {
                        return false;
                    }
                } catch (Exception e) {
                    Log.e("ViewAssignmentFragment", "Exception caught in gesture detection", e);
                }
                return false;
            }
        });
        
        view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // Do nothing, according to Gesticulate.
            }
        });
        
        view.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return gestureDetector.onTouchEvent(event);
            }
        });
        return view;
    }
}
