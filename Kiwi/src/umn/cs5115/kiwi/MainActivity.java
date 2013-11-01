package umn.cs5115.kiwi;

import java.util.Calendar;

import umn.cs5115.kiwi.CalendarUtils.EventCursorWrapper;
import umn.cs5115.kiwi.fragments.FilterDialogFragment;
import umn.cs5115.kiwi.fragments.FilterDialogFragment.FilterListener;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ListFragment;
import android.app.PendingIntent;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Checkable;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.espian.showcaseview.ShowcaseView;

public class MainActivity extends Activity implements ShowcaseView.OnShowcaseEventListener, FilterListener {
    private Cursor mCursor = null;
    private ShowcaseView showcaseView;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        getActionBar().setDisplayHomeAsUpEnabled(true);
        
        return;
        
//        ActionBar b = getActionBar();
//        if (b != null) {
//            Utils.makeActionBarDoneButton(b, new DoneBar.DoneButtonHandler() {
//                @Override
//                public void onDone(View view) {
//                    Toast.makeText(getBaseContext(), "Clicked Done!", Toast.LENGTH_SHORT).show();
//                    notifyInThreeSeconds();
//                    if (showcaseView.isShown()) {
////                        showcaseView.hide();
//                    }
//                }
//            });
//        }
        
//        ShowcaseView.ConfigOptions co = new ShowcaseView.ConfigOptions();
//        co.hideOnClickOutside = true;
        
//        showcaseView = ShowcaseView.insertShowcaseView(R.id.actionbar_done,
//                this, "Something...", "Something else...", co);
/*
        ListFragment lf = (ListFragment) getFragmentManager().findFragmentById(R.id.main_list_fragment);
        ListView lv = (ListView) lf.getListView();
        
        // If left out, list will appear with loading indicator
        lf.setListShown(true);
        
        // Set the text to show if we found nothing in the calendar API
        lf.setEmptyText(getApplicationContext().
        				getResources().getString(
        						R.string.import_courses_nothing_to_import));
        
        mCursor = CalendarUtils.getCourseEventCursor(this);
        
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

        // Get calendars as well
        Cursor calCursor = getContentResolver().query(
                CalendarContract.Calendars.CONTENT_URI, new String[]{
                CalendarContract.Calendars._ID,
                CalendarContract.Calendars.NAME,
                CalendarContract.Calendars.ACCOUNT_NAME,
                CalendarContract.Calendars.CALENDAR_DISPLAY_NAME
        }, null, null, null
        );
        if (calCursor == null) {
            return;
        }
        Log.d("CalendarTest", "Got calendars, I guess");
        while (calCursor.moveToNext()) {
            Log.i("CalendarTest",
                    String.format("Found calendar: %s: %s (%s - %s)",
                            calCursor.getString(0), calCursor.getString(1),
                            calCursor.getString(2), calCursor.getString(3)));
        }*/
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    private void showFilterDialog() {
    	FragmentManager fm = getFragmentManager();
    	FragmentTransaction ft = fm.beginTransaction();
    	Fragment prev = fm.findFragmentByTag("dialog_filter");
    	if (prev != null) ft.remove(prev);
    	ft.addToBackStack(null);
    	
    	FilterDialogFragment.newInstance(20).show(ft, "dialog_about");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
	        case android.R.id.home:
	        	showFilterDialog();
	        	return true;
	        case R.id.add_assignment:
	        	Utils.goToAddAssignment(this);
	        	return true;
        	case R.id.add_course:
        		Utils.goToAddCourse(this);
        		return true;
            case R.id.cancel:
                Toast.makeText(getBaseContext(), "Clicked Cancel!", Toast.LENGTH_SHORT).show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    private void notifyInThreeSeconds() {
        Intent i = new Intent(this, NotificationReceiver.class);
        PendingIntent alarm = PendingIntent.getBroadcast(this, 0, i, PendingIntent.FLAG_CANCEL_CURRENT);
        ((AlarmManager) getSystemService(ALARM_SERVICE)).set(AlarmManager.RTC_WAKEUP, Calendar.getInstance().getTimeInMillis() + 3000, alarm);
    }

    @Override
    public void onShowcaseViewHide(ShowcaseView showcaseView) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void onShowcaseViewShow(ShowcaseView showcaseView) {
        // TODO Auto-generated method stub
        
    }

	@Override
	public void onNewFilter(FilterDefinition newfilter) {
		Toast.makeText(this, "Got filter!", Toast.LENGTH_SHORT).show();
	}
}
