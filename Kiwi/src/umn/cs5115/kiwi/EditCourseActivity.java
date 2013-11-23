package umn.cs5115.kiwi;

import umn.cs5115.kiwi.app.KiwiDoneCancelActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class EditCourseActivity extends KiwiDoneCancelActivity {
	public static final String EXTRA_IS_EDIT = "i_am_edit";
	public static final String EXTRA_COURSE = "course";

	private boolean isEdit;
	private int courseId;
	
    @Override
	protected void onCreated(Bundle savedInstanceState) {
		super.onCreated(savedInstanceState);
		
		Intent incomingIntent = getIntent();
		if (incomingIntent != null) {
			if (incomingIntent.getBooleanExtra(EXTRA_IS_EDIT, false)) {
				Toast.makeText(this, "Editing the course", Toast.LENGTH_SHORT).show();
				isEdit = true;
			} else {
				isEdit = false;
			}
			
			courseId = incomingIntent.getIntExtra(EXTRA_COURSE, -1);
		}
	}

    public boolean isEdit() {
    	return isEdit;
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
        Toast.makeText(EditCourseActivity.this, "New course added.", Toast.LENGTH_SHORT).show();
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
