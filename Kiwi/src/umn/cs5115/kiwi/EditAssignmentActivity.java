package umn.cs5115.kiwi;

import umn.cs5115.kiwi.activity.KiwiDoneCancelActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

public class EditAssignmentActivity extends KiwiDoneCancelActivity {
	public static final String EXTRA_IS_EDIT = "i_am_edit";
	public static final String EXTRA_ASSIGNMENT = "assignment";

	private boolean isEdit;
	private int assignmentId;

	@Override
	public void onDone() {
		finish();
	}

	@Override
	public void onCancel() {
		finish();
	}

	@Override
	protected void onCreated(Bundle savedInstanceState) {
		super.onCreated(savedInstanceState);
		
		Intent incomingIntent = getIntent();
		if (incomingIntent != null) {
			if (incomingIntent.getBooleanExtra(EXTRA_IS_EDIT, false)) {
				Toast.makeText(this, "Editing the assignment woohoo", Toast.LENGTH_SHORT).show();
				isEdit = true;
			} else {
				isEdit = false;
			}
			
			assignmentId = incomingIntent.getIntExtra(EXTRA_ASSIGNMENT, -1);
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
