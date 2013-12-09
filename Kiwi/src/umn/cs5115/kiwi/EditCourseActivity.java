package umn.cs5115.kiwi;

import umn.cs5115.kiwi.app.KiwiDoneCancelActivity;
import umn.cs5115.kiwi.fragments.ImportCoursesFragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class EditCourseActivity extends KiwiDoneCancelActivity {
	public static final String EXTRA_IS_EDIT = "i_am_edit";
	public static final String EXTRA_IS_IMPORT = "is_import";
	public static final String EXTRA_COURSE_ID = "course";
	public static final String EXTRA_EVENT_NAME = "event_name",
	                             EXTRA_EVENT_LOC = "event_location",
	                             EXTRA_EVENT_RRULE = "rrule",
	                             EXTRA_EVENT_START = "event_start",
	                             EXTRA_EVENT_END = "event_end",
	                             EXTRA_EVENT_DURATION = "event_duration";

	private boolean isEdit;
	private boolean isImport;
	private int courseId;
	
    @Override
	protected void onCreated(Bundle savedInstanceState) {
		super.onCreated(savedInstanceState);
		
		Intent incomingIntent = getIntent();
		if (incomingIntent != null) {
		    isImport = incomingIntent.getBooleanExtra(EXTRA_IS_IMPORT, false);
			if (incomingIntent.getBooleanExtra(EXTRA_IS_EDIT, false)) {
				Toast.makeText(this, "Editing the course", Toast.LENGTH_SHORT).show();
				isEdit = true;
			} else {
				isEdit = false;
			}
			
			courseId = incomingIntent.getIntExtra(EXTRA_COURSE_ID, -1);
		}
	}

    public boolean isEdit() {
    	return isEdit;
    }
    
    public boolean isImport() {
        return isImport;
    }
    
    public int getCourseId() {
    	return courseId;
    }
    
	@Override
    public void onCancel() {
        Log.d("AddCourseActivity", "Cancelling adding a course.");
        String msg = isEdit ? "Course not saved." : "Course not created.";
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        finish();
    }
    
    @Override
    public void onDone() {
        finish();
    }

    @Override
    protected boolean hasCancelButton() {
        return false;
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_add_course;
    }
}
