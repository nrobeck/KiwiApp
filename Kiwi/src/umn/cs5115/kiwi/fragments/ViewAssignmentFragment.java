package umn.cs5115.kiwi.fragments;

import java.text.SimpleDateFormat;
import java.util.Locale;

import umn.cs5115.kiwi.DatabaseHandler;
import umn.cs5115.kiwi.EditAssignmentActivity;
import umn.cs5115.kiwi.R;
import umn.cs5115.kiwi.Utils;
import umn.cs5115.kiwi.assignment.AssignmentUtils.DueDateBuilder;
import umn.cs5115.kiwi.model.Assignment;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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
    public static final int EDIT_ASSIGNMENT_REQCODE = 0x1453;
    
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
    
    private void startEditing() {
		Intent editIntent = Utils.goToAddAssignment(getActivity().getBaseContext());
    	editIntent
        	.putExtra(EditAssignmentActivity.EXTRA_IS_EDIT, true)
        	.putExtra(EditAssignmentActivity.EXTRA_ASSIGNMENT, mAssignment.getId());
    	getActivity().startActivityForResult(editIntent, EDIT_ASSIGNMENT_REQCODE);
    }
    
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.view_assignment_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch(item.getItemId()) {
    	case R.id.assignment_view_menu_edit:
    		startEditing();
    		return true;
    		
    		case R.id.assignment_view_menu_delete:
    			
    			new AlertDialog.Builder(getActivity())
    			.setTitle("Delete assignment")
    			.setMessage(String.format("Are you sure you want to delete '%s'?", mAssignment.getName()))
    			.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
    				public void onClick(DialogInterface dialog, int which) {
    					new DatabaseHandler(getActivity()).removeAssignment(mAssignment);
    					getActivity().onBackPressed();
    					Utils.shortToast(getActivity(), "Deleted assignment.");
    				}
    			})
    			.setNegativeButton("No", new DialogInterface.OnClickListener() {
    				public void onClick(DialogInterface dialog, int which) {
    					Utils.shortToast(getActivity(), "Did not delete assignment.");
    				}
    			})
    			.show();
    			
    		return true;
    	}
    	return super.onOptionsItemSelected(item);
    }
    
    @Override
    public void onResume() {
        super.onResume();
        if (isAdded()) {
            ActionBar ab = getActivity().getActionBar();
            
            if (mAssignment == null) {
                ab.setTitle("Viewing assignment");
            } else {
                ab.setTitle(mAssignment.getName());
            }
        }
    }
    
    public void updateAssignment(Assignment assignment) {
    	mAssignment = assignment;
    	fillInAssignment(null);
    	ActionBar ab = getActivity().getActionBar();
        if (mAssignment == null) {
            ab.setTitle("Viewing assignment");
        } else {
            ab.setTitle(mAssignment.getName());
        }
    }
    
    private void fillInAssignment(View view) {
    	if (view == null) {
    		view = getView();
    	}
        if (mAssignment != null) {
            //assignment name
        	TextView assignmentNameTextView = (TextView) view.findViewById(R.id.assignmentName);
        	assignmentNameTextView.setText(mAssignment.getName());
        	
        	//assignment course
        	TextView assignmentCourseTextView = (TextView) view.findViewById(R.id.assignmentCourse);
        	assignmentCourseTextView.setText("Course: " + mAssignment.getCourseDesignation());
        	assignmentCourseTextView.setVisibility(mAssignment.getCourseDesignation() == null ? View.GONE : View.VISIBLE);
        	
        	//assignment type
        	TextView assignmentTypeTextView = (TextView) view.findViewById(R.id.assignmentType);
        	assignmentTypeTextView.setText("Type: " + mAssignment.getType());
        	assignmentTypeTextView.setVisibility(mAssignment.getType() == null ? View.GONE : View.VISIBLE);
        	
        	//assignment book
        	TextView assignmentBookTextView = (TextView) view.findViewById(R.id.assignmentBook);
        	assignmentBookTextView.setText("TextBook: " + mAssignment.getTextbook());
        	assignmentBookTextView.setVisibility(TextUtils.isEmpty(mAssignment.getTextbook()) ? View.GONE : View.VISIBLE);
        	
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
    }

    // Note: onCreateView is called after onCreate, so mAssignment will be
    // available here.
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.view_assignment_fragment, null);

        fillInAssignment(view);
        
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
                        startEditing();
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
