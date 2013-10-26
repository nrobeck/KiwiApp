package com.mikewadsten.test.kiwi;

import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;

public class AddCourseActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_add_course);
		
		getActionBar().setDisplayHomeAsUpEnabled(true);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			return true;
		}
		// TODO Auto-generated method stub
		return super.onOptionsItemSelected(item);
	}
	
	

}
