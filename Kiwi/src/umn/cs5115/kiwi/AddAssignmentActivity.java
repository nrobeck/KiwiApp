package umn.cs5115.kiwi;

import umn.cs5115.kiwi.activity.KiwiDoneCancelActivity;

public class AddAssignmentActivity extends KiwiDoneCancelActivity {

	@Override
	public void onDone() {
		finish();
	}

	@Override
	public void onCancel() {
		finish();
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
