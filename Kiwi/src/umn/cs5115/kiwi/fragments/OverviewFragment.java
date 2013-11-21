package umn.cs5115.kiwi.fragments;

import umn.cs5115.kiwi.Assignment;
import umn.cs5115.kiwi.DatabaseHandler;
import umn.cs5115.kiwi.FilterDefinition;
import umn.cs5115.kiwi.MainActivity;
import umn.cs5115.kiwi.R;
import umn.cs5115.kiwi.adapter.OverviewListCursorAdapter;
import umn.cs5115.kiwi.adapter.OverviewListCursorAdapter.TileInteractionListener;
import android.app.Activity;
import android.app.ListFragment;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

public class OverviewFragment extends ListFragment {
	private TileInteractionListener mListenerActivity;
	
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
		Cursor newCursor = new DatabaseHandler(getActivity()).getRawAssignmentCursor(getFilter().toQueryString());
		adapter.changeCursor(newCursor);
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
		
		Cursor assignmentsCursor = database.getRawAssignmentCursor(filter);
		final OverviewListCursorAdapter adapter = new OverviewListCursorAdapter(getActivity(), assignmentsCursor, mListenerActivity);
		
		getListView().setAdapter(adapter);
		
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
	}
}
