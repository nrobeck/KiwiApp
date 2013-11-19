package umn.cs5115.kiwi;

import umn.cs5115.kiwi.activity.KiwiDoneCancelActivity;
import android.util.Log;
import android.widget.Toast;

public class EditCourseActivity extends KiwiDoneCancelActivity {
    @Override
    public void onCancel() {
        Log.d("AddCourseActivity", "Cancelling adding a course.");
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
