package umn.cs5115.kiwi;

import umn.cs5115.kiwi.activity.KiwiDoneCancelActivity;
import umn.cs5115.kiwi.ui.DoneBar.CancelFromMenuHandler;
import umn.cs5115.kiwi.ui.DoneBar.DoneButtonHandler;
import android.util.Log;
import android.widget.Toast;

public class AddCourseActivity extends KiwiDoneCancelActivity {
    @Override
    protected CancelFromMenuHandler getCancelFromMenuHandler() {
        return new CancelFromMenuHandler() {
            @Override
            public void onCancel() {
                Log.d("AddCourseActivity", "Cancelling adding a course.");
                finish();
            }
        };
    }

    @Override
    protected DoneButtonHandler getDoneButtonHandler() {
        return new DoneButtonHandler() {
            @Override
            public void onDone() {
                Toast.makeText(AddCourseActivity.this, "New course added.", Toast.LENGTH_SHORT).show();
                finish();
            }
        };
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
