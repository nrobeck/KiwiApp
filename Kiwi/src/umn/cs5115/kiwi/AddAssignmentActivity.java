package umn.cs5115.kiwi;

import umn.cs5115.kiwi.DoneBar.DoneButtonHandler;
import umn.cs5115.kiwi.assignment.AssignmentUtils;

import com.cocosw.undobar.UndoBarController;
import com.cocosw.undobar.UndoBarController.UndoListener;
import umn.cs5115.kiwi.R;

import android.app.Activity;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class AddAssignmentActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_add_course);
		
		final Activity activity = this;
		
		Utils.makeActionBarDoneButton(getActionBar(), new DoneButtonHandler() {
            
            @Override
            public void onDone(View view) {
                AssignmentUtils.showTimeEditDialog(getFragmentManager(), null, null);
                UndoBarController.show(activity, "New assignment created.", new UndoListener() {
                    @Override
                    public void onUndo(Parcelable token) {
                        Toast.makeText(activity, "Undone.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
	}

	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
	    getMenuInflater().inflate(R.menu.cancel, menu);
	    return true;
    }

    @Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			return true;
		case R.id.cancel:
		    UndoBarController.show(this, "Assignment deleted.", new UndoListener() {
		        @Override
		        public void onUndo(Parcelable token) {
		            Toast.makeText(getApplication(), "Assignment not deleted.", Toast.LENGTH_SHORT).show();
		        }
		    });
		}
		// TODO Auto-generated method stub
		return super.onOptionsItemSelected(item);
	}
	
	

}
