package umn.cs5115.kiwi;

import umn.cs5115.kiwi.FilterDefinition.SortBy;
import umn.cs5115.kiwi.adapter.OverviewListCursorAdapter.TileInteractionListener;
import umn.cs5115.kiwi.app.KiwiActivity;
import umn.cs5115.kiwi.fragments.FilterDialogFragment;
import umn.cs5115.kiwi.fragments.FilterDialogFragment.FilterListener;
import umn.cs5115.kiwi.fragments.OverviewFragment;
import umn.cs5115.kiwi.fragments.ViewAssignmentFragment;
import umn.cs5115.kiwi.ui.OverviewEmptyView.CustomEmptyViewButtonListener;
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
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.cocosw.undobar.UndoBarController;
import com.cocosw.undobar.UndoBarController.UndoListener;
import com.espian.showcaseview.ShowcaseView;

public class MainActivity extends KiwiActivity
            implements ShowcaseView.OnShowcaseEventListener,
                        FilterListener, TileInteractionListener,
                        CustomEmptyViewButtonListener {
	private FilterDefinition filter;
	private DatabaseHandler database;
	
	private static final String OVERVIEW_TAG = "overview_frag";
	private static final String VIEW_ASSIGN_TAG = "view_frag";
	
	/**
	 * Get a reference to a fragment to show, in order to view that assignment's
	 * information. Pretty much you're going to want to instantiate a new
	 * ViewAssignmentFragment, and give it identifying information about the
	 * assignment that's being viewed.
	 * @param viewingAssignment the assignment that's being viewed
	 * @return a fragment that will "view" the given assignment
	 */
	private Fragment getViewAssignmentFragment(Assignment viewingAssignment) {
	    return ViewAssignmentFragment.newInstance(viewingAssignment);
	}
	
	/**
	 * Takes in a FragmentTransaction and calls setCustomAnimations on that
	 * transaction to ensure consistent animations.
	 * @param ft the fragment transaction to set animations on
	 */
	private void setSwapAnimations(FragmentTransaction ft) {
        ft.setCustomAnimations(
            R.anim.slide_in_from_right_objanim, 
            R.anim.slide_out_to_left_objanim,
            R.anim.slide_in_from_left_objanim,
            R.anim.slide_out_to_right_objanim);
	}
	
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
			vib.vibrate(20);
    	
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
		
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		setSwapAnimations(ft);
		ft.replace(R.id.base, getViewAssignmentFragment(assignment), VIEW_ASSIGN_TAG);
		ft.addToBackStack(null);
		ft.commit();
	}

	@Override
	public void editAssignment(Assignment assignment) {
		// TODO: Launch EditAssignmentActivity with this assignment...
		Log.d("MainActivity", "Editing assignment " + assignment);
		Intent editIntent = Utils.goToAddAssignment(getBaseContext());
		editIntent
		    .putExtra(EditAssignmentActivity.EXTRA_IS_EDIT, true)
		    .putExtra(EditAssignmentActivity.EXTRA_ASSIGNMENT, assignment.getId());
		startActivity(editIntent);
	}

	@Override
	public void deleteAssignment(final View deletingView, final Assignment assignment) {
		new AlertDialog.Builder(this)
		.setTitle("Delete assignment")
		.setMessage(String.format("Are you sure you want to delete '%s'?", assignment.getName()))
		.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				Log.d("MainActivity", "Deleting assignment " + assignment);
				final OverviewFragment overview = (OverviewFragment)getFragmentManager().findFragmentByTag(OVERVIEW_TAG);
                Toast.makeText(MainActivity.this, "Deleted assignment " + assignment.getName(), Toast.LENGTH_SHORT).show();
                database.removeAssignment(assignment);
                
                if (overview == null) {
                    return;
                }
				overview.getListView().setEnabled(false);
				overview.getListView().setClickable(false);
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
        } else {
            OverviewFragment overview = new OverviewFragment();
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.add(R.id.base, overview, OVERVIEW_TAG);
            ft.commit();
        }
        
        if (filter == null) {
        	Log.d("MainActivity", "Filter is null! Making fresh filter.");
        	filter = new FilterDefinition(SortBy.DUE_DATE, true, null, null);
        	Log.d("MainActivity", "New filter: " + filter.toQueryString());
        }
    }
    
    private void refreshOverviewFragment() {
		OverviewFragment overview = (OverviewFragment)getFragmentManager().findFragmentByTag(OVERVIEW_TAG);
		if (overview == null) {
			Log.d("MainActivity", "refreshOverviewFragment - overview frag is null!");
		    return;
		}
		if (overview.isResumed()) {
			Log.d("MainActivity", "calling overview.refreshFilter");
			overview.refreshFilter();
		} else {
			Log.d("MainActivity", "overview not resumed");
		}
    }

    /*
     * We override onPostResume here because onResume is too early
     * to execute refreshOverviewFragment (because at that point,
     * the OverviewFragment still hasn't been completely started -
     * the fragment's onResume doesn't get called until a tiny bit
     * later).
     */
	@Override
	protected void onPostResume() {
		super.onPostResume();
		/*
		 * Update the overview fragment's filter.
		 */
		refreshOverviewFragment();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		if (filter != null) {
			outState.putParcelable("filter", filter);
		}
	}

    private void showFilterDialog() {
    	FragmentManager fm = getFragmentManager();
    	FragmentTransaction ft = fm.beginTransaction();
    	Fragment prev = fm.findFragmentByTag("dialog_filter");
    	if (prev != null) ft.remove(prev);
    	ft.addToBackStack(null);

    	FilterDialogFragment.newInstance(this.filter).show(ft, "dialog_about");
    }

    /**
     * Handle clicking options menu items. These items will be created
     * in any fragments living inside this activity.
     * 
     * @param item The MenuItem that was selected.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
	        case R.id.add_assignment:
	            /*
	             * We may as well reuse the interface method for the empty
	             * view button click!
	             */
	            onClickAddAssignment();
	        	return true;
	        case R.id.filter:
	            /*
	             * We may as well reuse the interface method for requesting the
	             * filtering dialog.
	             */
	            onClickFilter();
	            return true;
        	case R.id.add_course:
        		Utils.goToAddCourse(this);
        		return true;
        	case R.id.action_settings:
        		startActivity(new Intent(this, AdvancedOptionsActivity.class));
        		return true;
        	case android.R.id.home:
        	    onBackPressed();
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

    @Override
    public void onClickAddCourses(View buttonView) {
//        PopupMenu popup = new PopupMenu(this, buttonView);
//        popup.getMenuInflater().inflate(R.menu.overview_add_course_popup_menu, popup.getMenu());
//
//        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
//            public boolean onMenuItemClick(final MenuItem item) {
//                switch (item.getItemId()) {
//                case R.id.add_course_menu_add:
//                    Utils.goToAddCourse(MainActivity.this);
//                    return true;
//                case R.id.add_menu_course_import:
//                    startActivity(new Intent(MainActivity.this, ImportCoursesActivity.class));
//                    return true;
//                }
//                return true;
//            }
//        });
//        popup.show();
        
        /*
         * Until we have importing from calendar working, just go directly
         * to the Add course activity.
         */
        Utils.goToAddCourse(this);
    }

    @Override
    public void onClickAddAssignment() {
        Utils.goToAddAssignment(this);
    }

    @Override
    public void onClickFilter() {
        showFilterDialog();
    }

    @Override
    public void onBackPressed() {
        FragmentManager fm = getFragmentManager();
        if (fm.getBackStackEntryCount() > 0) {
            FragmentTransaction transaction = fm.beginTransaction();
            fm.popBackStack();
            transaction.commit();
        } else {
            super.onBackPressed();
        }
    }
}
