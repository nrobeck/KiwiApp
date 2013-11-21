package umn.cs5115.kiwi.fragments;

import umn.cs5115.kiwi.DatabaseHandler;
import umn.cs5115.kiwi.FilterDefinition;
import umn.cs5115.kiwi.MainActivity;
import umn.cs5115.kiwi.adapter.OverviewListCursorAdapter;
import android.app.ListFragment;
import android.database.Cursor;
import android.os.Bundle;

public class OverviewFragment extends ListFragment {
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
		setEmptyText("No assignments!");
		getListView().setDivider(null);
		
		String filter = getFilter().toQueryString();
		
		getListView().setAdapter(new OverviewListCursorAdapter(getActivity(), new DatabaseHandler(getActivity()).getRawAssignmentCursor(filter), 0));
	}
}
