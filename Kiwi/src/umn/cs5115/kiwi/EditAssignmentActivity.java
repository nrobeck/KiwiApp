package umn.cs5115.kiwi;

import umn.cs5115.kiwi.app.KiwiDoneCancelActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class EditAssignmentActivity extends KiwiDoneCancelActivity {
	public static final String EXTRA_IS_EDIT = "i_am_edit";
	public static final String EXTRA_ASSIGNMENT = "assignment";

	private boolean isEdit;
	private int assignmentId;
	
	private void giveFeedback(String text) {
	    Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onDone() {
	    giveFeedback("Assignment saved.");
		finish();
	}

	@Override
	public void onCancel() {
	    giveFeedback(isEdit ? "Assignment not changed." : "Assignment not created.");
		finish();
	}

	@Override
	protected void onCreated(Bundle savedInstanceState) {
		super.onCreated(savedInstanceState);
		
		Intent incomingIntent = getIntent();
		if (incomingIntent != null) {
			if (incomingIntent.getBooleanExtra(EXTRA_IS_EDIT, false)) {
				isEdit = true;
			} else {
				isEdit = false;
			}
			
			assignmentId = incomingIntent.getIntExtra(EXTRA_ASSIGNMENT, -1);
			
			if (isEdit) {
			    Log.i("EditAssignmentActivity", "Editing assignment " + assignmentId);
			} else {
			    Log.i("EditAssignmentActivity", "Making new assignment.");
			}
		}
	}
	
	public boolean isEdit() {
		return isEdit;
	}
	
	public int getAssignmentId() {
		return assignmentId;
	}

	@Override
	protected int getLayoutResource() {
		return R.layout.activity_add_assignment;
	}

	@Override
	protected boolean hasCancelButton() {
		return false;
	}
}
