package umn.cs5115.kiwi;

import umn.cs5115.kiwi.DoneBar.DoneButtonHandler;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.cocosw.undobar.UndoBarController;
import com.cocosw.undobar.UndoBarController.UndoListener;

public class AddCourseActivity extends KiwiActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_add_course);
		
		Utils.makeActionBarDoneButton(getActionBar(), new DoneButtonHandler() {
            
            @Override
            public void onDone(View view) {
//                UndoBarController.show(activity, "New assignment created.", new UndoListener() {
//                    @Override
//                    public void onUndo(Parcelable token) {
//                        Toast.makeText(activity, "Undone.", Toast.LENGTH_SHORT).show();
//                    }
//                });
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
