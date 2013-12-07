package umn.cs5115.kiwi.fragments;

import umn.cs5115.kiwi.DatabaseHandler;
import umn.cs5115.kiwi.R;
import umn.cs5115.kiwi.model.Course;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Toast;


public class DeleteCoursesFragment extends Fragment {
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.delete_courses, container);
	}
	
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        LinearLayout layout = (LinearLayout) getView().findViewById(R.id.delete_courses_cb_layout);
        
        Context ctx = getActivity();
		final Course[] courses = new DatabaseHandler(ctx).getCourses();
		final CheckBox[] courseCheckboxes = new CheckBox[courses.length];
		for (int i = 0; i < courses.length; i++) {
			Course course = courses[i];
            CheckBox cb = new CheckBox(ctx);
            cb.setText(course.getCourseDesignation());
            // Keep track of the course id
            cb.setTag(course.getId());
            layout.addView(cb);
            
            courseCheckboxes[i] = cb;
		}

		final CheckBox noCourseCheckbox = (CheckBox)getView().findViewById(R.id.delete_assignments_no_course_cb);
		
		final Button selectAllButton = (Button) getView().findViewById(R.id.select_all_courses);
		selectAllButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				noCourseCheckbox.setChecked(true);
				for(CheckBox cb : courseCheckboxes){
					cb.setChecked(true);
				}
			}
		});
		
		final Button button = (Button) getView().findViewById(R.id.delete_courses);
		button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				int count = 0;
				for(CheckBox cb : courseCheckboxes){
					if(cb.isChecked()){
						count += 1;
						break;
					}
				}
				if((count == 0) && !(noCourseCheckbox.isChecked())){
					Toast.makeText(getActivity(), "No Courses Selected", Toast.LENGTH_SHORT).show();
				}
				else{
					new AlertDialog.Builder(getActivity())
					.setTitle("Delete Courses")
					.setMessage("Are you sure you want to delete? This option cannot be undone!")
					.setIcon(android.R.drawable.ic_dialog_alert)
					.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int whichButton) {
							int i = 0;
							LinearLayout l = (LinearLayout)getView().findViewById(R.id.delete_courses_cb_layout);
							DatabaseHandler dbh = new DatabaseHandler(getActivity());
						
							for(CheckBox cb : courseCheckboxes){
								if(cb.isChecked()){
									dbh.removeCourse(courses[i]);
									l.removeView(cb);
								}
								i++;
							}
							
							if (noCourseCheckbox.isChecked()) {
								dbh.removeAssignmentsWithNoCourse();
								l.removeView(noCourseCheckbox);
							}
						
							if (l.getChildCount() == 0) {
								getActivity().finish();
							}
							//new DatabaseHandler(DeleteCoursesActivity.this).clearDatabase();
							Toast.makeText(getActivity(), "Courses Deleted", Toast.LENGTH_SHORT).show();
						}})
					.setNegativeButton(android.R.string.no, null).show();
				}	
			}
		});
    }
    
}