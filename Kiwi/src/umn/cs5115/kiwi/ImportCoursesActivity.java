package umn.cs5115.kiwi;

import umn.cs5115.kiwi.CalendarUtils.EventCursorWrapper;

import com.mikewadsten.test.kiwi.R;

import android.app.Activity;
import android.app.ListFragment;
import android.database.Cursor;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Checkable;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class ImportCoursesActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_import_courses);
		
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
        ListFragment lf = (ListFragment) getFragmentManager().findFragmentById(R.id.list_fragment);
        ListView lv = (ListView) lf.getListView();
        
        // If left out, list will appear with loading indicator
        lf.setListShown(true);
        
        // Set the text to show if we found nothing in the calendar API
        lf.setEmptyText(getApplicationContext().
        				getResources().getString(
        						R.string.import_courses_nothing_to_import));
        
        Cursor mCursor = CalendarUtils.getCourseEventCursor(this);
        
        final EventCursorWrapper wrapper = new EventCursorWrapper(mCursor);
        final SimpleCursorAdapter adapter = new SimpleCursorAdapter(
                this, R.layout.import_course_item, wrapper,
                CalendarUtils.EVENT_QUERY_COLS, new int[] {R.id.title, R.id.content});

        View headerView = View.inflate(this, R.layout.import_courses_header, null);
        headerView.setClickable(false);
        headerView.setFocusable(false);
        headerView.setFocusableInTouchMode(false);
        lf.getListView().addHeaderView(headerView, null, false);
        lv.setAdapter(adapter);
        lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Checkable child = (Checkable) adapterView.getChildAt(i);
                if (child == null) {
                    return;
                }
                child.toggle();
                adapter.notifyDataSetChanged();
            }
        });
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
