package umn.cs5115.kiwi.fragments;

import umn.cs5115.kiwi.Assignment;
import umn.cs5115.kiwi.DatabaseHandler;
import umn.cs5115.kiwi.DatabaseHandler.DbAndCursor;
import umn.cs5115.kiwi.FilterDefinition;
import umn.cs5115.kiwi.MainActivity;
import umn.cs5115.kiwi.R;
import umn.cs5115.kiwi.adapter.OverviewListCursorAdapter;
import umn.cs5115.kiwi.adapter.OverviewListCursorAdapter.TileInteractionListener;
import android.app.Activity;
import android.app.ListFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.TextView;

public class OverviewFragment extends ListFragment {
	private TileInteractionListener mListenerActivity;
	private DbAndCursor assignments;
	
	@Override
	public void onAttach(Activity activity) {
		if (activity instanceof TileInteractionListener) {
			mListenerActivity = (TileInteractionListener) activity;
			super.onAttach(activity);
		} else {
			throw new ClassCastException("OverviewFragment can only be attached to Activities implementing TileInteractionListener!");
		}
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
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// Don't kill this fragment when screen is rotated, etc.
		setRetainInstance(true);
	}
	
	private FilterDefinition getFilter() {
		MainActivity activity = (MainActivity)getActivity();
		return activity.getFilter();
	}
	
	public void refreshFilter() {
		OverviewListCursorAdapter adapter = (OverviewListCursorAdapter) getListView().getAdapter();
		FilterDefinition defn = getFilter();
		assignments = new DatabaseHandler(getActivity()).getRawAssignmentCursor(defn.toQueryString(), defn.getOrderString());
		adapter.changeCursor(assignments.cursor);
		// need this?
		//adapter.notifyDataSetChanged();
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		setListShown(true);
		setEmptyText(null);

		TextView emptyView = (TextView)getListView().getEmptyView();
		emptyView.setText(getResources().getString(R.string.overview_no_assignments));
		emptyView.setTextSize(18f);
		
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
				return false;
			}
		});
	}
}
