package umn.cs5115.kiwi.fragments;

import umn.cs5115.kiwi.Assignment;
import umn.cs5115.kiwi.R;
import android.app.ActionBar;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;

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
        // TODO: Make the actions menu for this view, and point to it here.
        // inflater.inflate(R.menu.view_assignment_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
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
            // TODO: Fill in text views, etc. in the view with information
            // stored on mAssignment here.
            
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
                        getActivity().onBackPressed();
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
