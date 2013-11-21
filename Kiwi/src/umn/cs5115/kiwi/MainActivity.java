package umn.cs5115.kiwi;

import java.util.Calendar;

import umn.cs5115.kiwi.FilterDefinition.SortBy;
import umn.cs5115.kiwi.activity.KiwiActivity;
import umn.cs5115.kiwi.fragments.FilterDialogFragment;
import umn.cs5115.kiwi.fragments.FilterDialogFragment.FilterListener;
import umn.cs5115.kiwi.fragments.OverviewFragment;
import android.app.AlarmManager;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.espian.showcaseview.ShowcaseView;

public class MainActivity extends KiwiActivity implements ShowcaseView.OnShowcaseEventListener, FilterListener {
	private FilterDefinition filter;
	
	public FilterDefinition getFilter() {
		return this.filter;
	}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        DatabaseHandler dbh = new DatabaseHandler(this);

        if (savedInstanceState != null) {
        	filter = (FilterDefinition) savedInstanceState.getParcelable("filter");
        	Log.d("MainActivity", "Found filter: " + filter.toQueryString());
        } else {
        	// New activity; add assignment because of reasons.
            Assignment a = new Assignment(-1, "New completed!!!", 0, null, null, 0, 0, 0, null, null, null);
            a.setCompleted(true);
            dbh.addAssignment(a);
        }
        
        if (filter == null) {
        	Log.d("MainActivity", "Filter is null! Making fresh filter.");
        	filter = new FilterDefinition(SortBy.DUE_DATE, false, null, null);
        	Log.d("MainActivity", "New filter: " + filter.toQueryString());
        }
        
        if (dbh.getCourseCount() < 1) {
        	dbh.addCourse(new Course(0, "My course", "CS5115", null, null, null, null, null, null, null, null));
        } else {
        	// pass.
        }
        //DEBUGGING CODE
        DatabaseHandler dbHandler = new DatabaseHandler(this);
        dbHandler.addAssignment(new Assignment(0, "Hello world!", 0, null, null, 0, 0, 0, null, null, null));
        dbHandler.addCourse(new Course(0, 
        							   "User Interface Design", 
        							   "CSCI 5115", 
        							   "9:45", 
        							   "11:00", 
        							   "212 MechE", 
        							   "September 1, 2013", 
        							   "December 9, 2013", 
        							   "", 
        							   "LOTS OF NOTES", 
        							   "Design of Everyday Things\nDesign for Use"));
        dbHandler.addCourse(new Course(0, 
									   "Data Mining", 
									   "CSCI 5523", 
									   "16:00", 
									   "17:15", 
									   "3-230 Keller", 
									   "September 1, 2013", 
									   "December 9, 2013", 
									   "", 
									   "LOTS OF NOTES", 
									   "Introduction to Data Mining"));
        
    }
    
    private void refreshOverviewFragment() {
		OverviewFragment overview = (OverviewFragment)getFragmentManager().findFragmentById(R.id.main_list_fragment);
		overview.refreshFilter();
    }

    @Override
	protected void onResume() {
    	Log.d("MainActivity", "onResume");
		super.onResume();
		refreshOverviewFragment();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		if (filter != null) {
			outState.putParcelable("filter", filter);
		}
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

    	FilterDialogFragment.newInstance(this.filter).show(ft, "dialog_about");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
	        case R.id.add_assignment:
	        	Utils.goToAddAssignment(this);
	        	notifyInThreeSeconds();
	        	return true;
	        case R.id.filter:
	            showFilterDialog();
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

    @SuppressWarnings("unused")
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
//		Toast.makeText(this, "Got filter!", Toast.LENGTH_SHORT).show();
		this.filter = newfilter;
		refreshOverviewFragment();
	}
}
