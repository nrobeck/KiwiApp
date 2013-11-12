package umn.cs5115.kiwi;

import umn.cs5115.kiwi.ui.DoneBar.DoneCancelBarHandler;
import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

public class ImportCoursesActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_import_courses);
		
		Utils.makeActionBarDoneCancel(getActionBar(), new DoneCancelBarHandler() {
            
            @Override
            public void onDone() {
                Toast.makeText(getApplicationContext(), "Done!", Toast.LENGTH_SHORT).show();
                finish();
            }
            
            @Override
            public void onCancel() {
                finish();
            }
        });
	}
}
