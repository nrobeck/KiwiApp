package umn.cs5115.kiwi;

import umn.cs5115.kiwi.FilterDefinition.SortBy;
import umn.cs5115.kiwi.activity.KiwiActivity;
import umn.cs5115.kiwi.adapter.OverviewListCursorAdapter.TileInteractionListener;
import umn.cs5115.kiwi.fragments.FilterDialogFragment;
import umn.cs5115.kiwi.fragments.FilterDialogFragment.FilterListener;
import umn.cs5115.kiwi.fragments.OverviewFragment;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.Vibrator;
import android.support.v4.view.ViewCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.cocosw.undobar.UndoBarController;
import com.cocosw.undobar.UndoBarController.UndoListener;
import com.espian.showcaseview.ShowcaseView;

public class MainActivity extends KiwiActivity implements ShowcaseView.OnShowcaseEventListener, FilterListener, TileInteractionListener {
	private FilterDefinition filter;
	private DatabaseHandler database;
	
	public FilterDefinition getFilter() {
		return this.filter;
	}
	
	/**
	 * Fundamental body of the onChangeCompletion method. We can't let the UndoBar
	 * launched by that method just call onChangeCompletion... If we did, you get
	 * a fun continuous series of undo bars allowing you to toggle the completion
	 * status over and over!
	 * @param assignment Assignment whose completion status to change
	 * @param isCompleted the completion status to change to
	 */
	private void changeCompletion(Assignment assignment, boolean isCompleted) {
    	assignment.setCompleted(isCompleted);
    	database.editAssignment(assignment);
    	refreshOverviewFragment();
	}

	// Assignment tile interaction callbacks
    @Override
	public void onChangeCompletion(final Assignment assignment, final boolean isCompleted) {
    	changeCompletion(assignment, isCompleted);
    	
    	/*
    	 * Give a little bit of haptic feedback.
    	 */
		Vibrator vib = (Vibrator) getSystemService(VIBRATOR_SERVICE);
		// But we may not be able to call vibrate if hasVibrator is false.
		// TODO: Check if this is the case...
		if (vib.hasVibrator())
			vib.vibrate(15);
    	
		String toast = String.format("Marked '%s' as%s completed.", assignment.getName(), (isCompleted ? "" : " not"));
    	
		Log.d("MainActivity", "onChangeCompletion " + assignment + " " + isCompleted);
    	UndoBarController.show(this, toast, new UndoListener() {
			@Override
			public void onUndo(Parcelable token) {
				changeCompletion(assignment, !isCompleted);
			}
		});
	}

	@Override
	public void viewAssignment(Assignment assignment) {
		// TODO: Switch view to show ViewAssignmentFragment.
		Log.d("MainActivity", "Viewing assignment " + assignment);
	}

	@Override
	public void editAssignment(Assignment assignment) {
		// TODO: Launch EditAssignmentActivity with this assignment...
		Log.d("MainActivity", "Editing assignment " + assignment);
		Utils.goToAddAssignment(this);
	}

	@Override
	public void deleteAssignment(final View deletingView, final Assignment assignment) {
		new AlertDialog.Builder(this)
		.setTitle("Delete assignment")
		.setMessage(String.format("Are you sure you want to delete '%s'?", assignment.getName()))
		.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				Log.d("MainActivity", "Deleting assignment " + assignment);
				final OverviewFragment overview = (OverviewFragment)getFragmentManager().findFragmentById(R.id.main_list_fragment);
				overview.getListView().setEnabled(false);
				overview.getListView().setClickable(false);
				database.removeAssignment(assignment);
				Toast.makeText(MainActivity.this, "Deleted assignment " + assignment.getName(), Toast.LENGTH_SHORT).show();
				ViewCompat.setHasTransientState(deletingView, true);
				Animation anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.assignment_tile_remove);
				anim.setAnimationListener(new AnimationListener() {
					@Override
					public void onAnimationEnd(Animation arg0) {
						ViewCompat.setHasTransientState(deletingView, false);
						overview.getListView().setEnabled(true);
						overview.getListView().setClickable(true);
						refreshOverviewFragment();
					}

					@Override
					public void onAnimationRepeat(Animation animation) {
						// ignore
					}

					@Override
					public void onAnimationStart(Animation animation) {
						// ignore
					}
				});
				deletingView.startAnimation(anim);
			}
		})
		.setNegativeButton("No", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				Toast.makeText(MainActivity.this, "Did not delete assignment.", Toast.LENGTH_SHORT).show();
			}
		})
		.show();
	}

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        DatabaseHandler dbh = new DatabaseHandler(this);
        this.database = dbh;

        if (savedInstanceState != null) {
        	filter = (FilterDefinition) savedInstanceState.getParcelable("filter");
        	if (filter != null) {
        		Log.d("MainActivity", "Found filter: " + filter.toQueryString());
        	}
        }
        
        if (filter == null) {
        	Log.d("MainActivity", "Filter is null! Making fresh filter.");
        	filter = new FilterDefinition(SortBy.DUE_DATE, true, null, null);
        	Log.d("MainActivity", "New filter: " + filter.toQueryString());
        }
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
//	        	notifyInThreeSeconds();
	        	return true;
	        case R.id.filter:
	            showFilterDialog();
	            return true;
        	case R.id.add_course:
        		Utils.goToAddCourse(this);
        		return true;
        	case R.id.action_settings:
        		startActivity(new Intent(this, AdvancedOptionsActivity.class));
        		return true;
        }
        return super.onOptionsItemSelected(item);
    }

//    private void notifyInThreeSeconds() {
//        Intent i = new Intent(this, NotificationReceiver.class);
//        PendingIntent alarm = PendingIntent.getBroadcast(this, 0, i, PendingIntent.FLAG_CANCEL_CURRENT);
//        ((AlarmManager) getSystemService(ALARM_SERVICE)).set(AlarmManager.RTC_WAKEUP, Calendar.getInstance().getTimeInMillis() + 3000, alarm);
//    }

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
