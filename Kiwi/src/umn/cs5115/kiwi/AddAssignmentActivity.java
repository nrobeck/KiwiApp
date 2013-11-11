package umn.cs5115.kiwi;

import umn.cs5115.kiwi.DoneBar.DoneButtonHandler;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class AddAssignmentActivity extends KiwiActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_add_assignment);
		
		Utils.makeActionBarDoneButton(getActionBar(), new DoneButtonHandler() {
            @Override
            public void onDone(View view) {
                finish();
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
		case R.id.cancel:
/*		    UndoBarController.show(this, "Assignment deleted.", new UndoListener() {
		        @Override
		        public void onUndo(Parcelable token) {
		            Toast.makeText(getApplication(), "Assignment not deleted.", Toast.LENGTH_SHORT).show();
		        }
		    });*/
		    finish();
		    return true;
		}
		// TODO Auto-generated method stub
		return super.onOptionsItemSelected(item);
	}
}
