package umn.cs5115.kiwi.fragments;

import umn.cs5115.kiwi.Assignment;
import umn.cs5115.kiwi.DatabaseHandler;
import umn.cs5115.kiwi.DatabaseHandler.DbAndCursor;
import umn.cs5115.kiwi.FilterDefinition;
import umn.cs5115.kiwi.MainActivity;
import umn.cs5115.kiwi.R;
import umn.cs5115.kiwi.adapter.OverviewListCursorAdapter;
import umn.cs5115.kiwi.adapter.OverviewListCursorAdapter.TileInteractionListener;
import umn.cs5115.kiwi.app.CustomOverviewListFragment;
import umn.cs5115.kiwi.ui.OverviewEmptyView.CustomEmptyViewButtonListener;
import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

public class OverviewFragment extends CustomOverviewListFragment {
    private static final String TAG = "OverviewFragment";
	private TileInteractionListener mListenerActivity;
	private CustomEmptyViewButtonListener mEmptyViewListenerActivity;
	private DbAndCursor assignments;
	private boolean showFilterButton;
	
	private boolean shouldRefreshFilterUponResume;
	
	/**
	 * Generate a String to use as an exception message, specifying that this
	 * fragment can only be added to activities implementing the given interface
	 * class. We don't use hardcoded strings for these class names because if we
	 * did, refactoring would be painful (maybe).
	 * 
	 * @param intfclass the interface needed to be implemented
	 * @return a string
	 */
	private final String makeInterfaceErrorString(Class<?> intfclass) {
	    String fmt = "%s can only be attached to activities implementing %s";
	    return String.format(fmt, getClass().getCanonicalName(), intfclass.getCanonicalName());
	}
	
	@Override
	public void onAttach(Activity activity) {
	    Log.d(TAG, "onAttach");
	    
		if (activity instanceof TileInteractionListener) {
			mListenerActivity = (TileInteractionListener) activity;
		} else {
			throw new ClassCastException(makeInterfaceErrorString(TileInteractionListener.class));
		}
		
		if (activity instanceof CustomEmptyViewButtonListener) {
		    mEmptyViewListenerActivity = (CustomEmptyViewButtonListener) activity;
		} else {
		    throw new ClassCastException(makeInterfaceErrorString(CustomEmptyViewButtonListener.class));
		}

        super.onAttach(activity);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		// Make sure to close the database, otherwise we're just leaking the connection.
		if (assignments != null) {
			assignments.db.close();
			assignments = null;
		}
	}

	@Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.overview_fragment_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        /*
         * Hide or show the filter button, depending on the state of the
         * showFilterButton variable.
         */
        menu.findItem(R.id.filter).setVisible(showFilterButton);
        
        super.onPrepareOptionsMenu(menu);
    }

    @Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// Don't kill this fragment when screen is rotated, etc.
		setRetainInstance(true);
		
		setHasOptionsMenu(true);
	}
    
	@Override
    public void onResume() {
		Log.d(TAG, "onResume OverviewFragment");
        super.onResume();
        if (isAdded()) {
            ActionBar ab = getActivity().getActionBar();
            // Disable the Up button on the activity.
            ab.setDisplayHomeAsUpEnabled(false);
//            ab.setTitle(getActivity().getTitle());
            ab.setTitle("Assignments");
            ab.setSubtitle(null);
        }
        
        if (shouldRefreshFilterUponResume) {
        	Log.d(TAG, "Refreshing filter because we were told we should...");
        	shouldRefreshFilterUponResume = false;
        	refreshFilter();
        }
    }

    private FilterDefinition getFilter() {
		MainActivity activity = (MainActivity)getActivity();
		return activity.getFilter();
	}
    
    public void setRefreshUponResume(boolean should) {
    	this.shouldRefreshFilterUponResume = should;
    }
	
	public void refreshFilter() {
		Log.d(TAG, "refreshFilter OverviewFragment");
		OverviewListCursorAdapter adapter = (OverviewListCursorAdapter) getListView().getAdapter();
		FilterDefinition defn = getFilter();
		DatabaseHandler dbh = new DatabaseHandler(getActivity());
		assignments = dbh.getRawAssignmentCursor(defn.toQueryString(), defn.getOrderString());
		adapter.changeCursor(assignments.cursor);
		
		/*
		 * Only show the Filter button if there are not zero assignments
		 * in the database altogether.
		 */
		boolean thereAreAssignments = dbh.getAssignmentCount() > 0;
		showFilterButton = thereAreAssignments;
		getEmptyView().setFilterButtonShown(showFilterButton);
		
		/*
		 * Cause the options menu to be re-upped, therefore allowing us to
		 * change the visibility of the filter button as necessary. See
		 * onPrepareOptionsMenu above.
		 */
		getActivity().invalidateOptionsMenu();
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		setListShown(true);
		
		getListView().setDivider(null);
		
		String filter = getFilter().toQueryString();
		
		final DatabaseHandler database = new DatabaseHandler(getActivity());
		
		OverviewListCursorAdapter adapter = (OverviewListCursorAdapter) getListAdapter();
		if (assignments == null && getListView().getAdapter() == null) {
			assignments = database.getRawAssignmentCursor(filter);
			adapter = new OverviewListCursorAdapter(getActivity(), assignments.cursor, mListenerActivity);
		}
		
		setListAdapter(adapter);
		
		getListView().setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapter, View clickedView, int position, long arg3) {
				Object tag = clickedView.getTag();
				if (tag instanceof Assignment) {
					mListenerActivity.viewAssignment((Assignment)tag);
				} else {
					Log.e("OverviewFragment", "clicked view no assignment tag?" + tag + clickedView);
				}
			}
		});
		getListView().setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> adapter, View longClickedView, int position, long id) {
				Object tag = longClickedView.getTag();
				if (tag instanceof Assignment) {
					Assignment a = (Assignment)tag;
					boolean destCompletion = !a.isCompleted();
					mListenerActivity.onChangeCompletion(a, destCompletion);
				}
				return true;
			}
		});
		
		setEmptyViewButtonClickListener(mEmptyViewListenerActivity);
	}
}
