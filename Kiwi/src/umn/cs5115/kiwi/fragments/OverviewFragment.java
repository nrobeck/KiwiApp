package umn.cs5115.kiwi.fragments;

import umn.cs5115.kiwi.DatabaseHandler;
import umn.cs5115.kiwi.R;
import umn.cs5115.kiwi.adapter.OverviewListCursorAdapter;
import android.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class OverviewFragment extends ListFragment {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// Don't kill this fragment when screen is rotated, etc.
		setRetainInstance(true);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		setListShown(true);
		setEmptyText("No assignments!");
		getListView().setDivider(null);
		
		getListView().setAdapter(new OverviewListCursorAdapter(getActivity(), new DatabaseHandler(getActivity()).getRawAssignmentCursor(null), 0));
	}
}
