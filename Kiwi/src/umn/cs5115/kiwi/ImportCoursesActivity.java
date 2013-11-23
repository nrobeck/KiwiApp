package umn.cs5115.kiwi;

import umn.cs5115.kiwi.app.KiwiDoneCancelActivity;
import android.widget.Toast;

public class ImportCoursesActivity extends KiwiDoneCancelActivity {
	@Override
	protected int getLayoutResource() {
		return R.layout.activity_import_courses;
	}

    @Override
    public void onDone() {
        Toast.makeText(getApplicationContext(), "Done!", Toast.LENGTH_SHORT).show();
        finish();
    }
    
    @Override
    public void onCancel() {
        finish();
    }
}
