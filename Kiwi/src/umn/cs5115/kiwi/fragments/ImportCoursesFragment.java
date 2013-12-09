package umn.cs5115.kiwi.fragments;

import umn.cs5115.kiwi.CalendarUtils;
import umn.cs5115.kiwi.CalendarUtils.EventCursorWrapper;
import umn.cs5115.kiwi.EditCourseActivity;
import umn.cs5115.kiwi.R;
import umn.cs5115.kiwi.Utils;
import android.app.ListFragment;
import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.CalendarContract.Events;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class ImportCoursesFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor> {
	SimpleCursorAdapter mAdapter;
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View layout = super.onCreateView(inflater, container, savedInstanceState);
        layout.setBackgroundResource(R.drawable.card_background);
        
        return layout;
    }

	@Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setListShown(false);
        setEmptyText(getResources().getString(R.string.import_courses_nothing_to_import));
        
        final ListView lv = getListView();
        
//        setListShownNoAnimation(true);
        
        mAdapter = new SimpleCursorAdapter(
                getActivity(), R.layout.import_course_item, null,
                CalendarUtils.EVENT_QUERY_COLS, new int[] {R.id.title, R.id.content}, 0);

        // Set the adapter
        setListAdapter(mAdapter);
        
        getLoaderManager().initLoader(0, null, this);
        
//        lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Checkable child = (Checkable) adapterView.getChildAt(i);
//                if (child == null) {
//                    return;
//                }
//                child.toggle();
//                view.setActivated(!child.isChecked());
                
//                adapter.notifyDataSetChanged();
                Intent makeNewIntent = Utils.goToAddCourse((Context)getActivity());
                
                Cursor c = mAdapter.getCursor();
                c.moveToPosition(i);
                
                String location = c.getString(c.getColumnIndex(Events.EVENT_LOCATION));
                long start = c.getLong(c.getColumnIndex(Events.DTSTART));
                long end = c.getLong(c.getColumnIndex(Events.DTEND));
                String rrule = c.getString(c.getColumnIndex(Events.RRULE));
                // Necessary because we aren't certain that calendar events are
                // being represented as start-time/end-time vs. start-time + duration
                String duration = c.getString(c.getColumnIndex(Events.DURATION));
                
                makeNewIntent.putExtra(EditCourseActivity.EXTRA_EVENT_LOC, location)
                .putExtra(EditCourseActivity.EXTRA_EVENT_START, start)
                .putExtra(EditCourseActivity.EXTRA_EVENT_END, end)
                .putExtra(EditCourseActivity.EXTRA_EVENT_DURATION, duration)
                .putExtra(EditCourseActivity.EXTRA_EVENT_RRULE, rrule)
                .putExtra(EditCourseActivity.EXTRA_EVENT_NAME, c.getString(c.getColumnIndex(Events.TITLE)))
                .putExtra(EditCourseActivity.EXTRA_IS_IMPORT, true);
                
                startActivity(makeNewIntent);
            }
        });
    }

	@Override
	public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {
		return CalendarUtils.getCourseEventCursorLoader(getActivity());
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		data.moveToFirst();
		
		EventCursorWrapper wrapper = new EventCursorWrapper(data);
		
		mAdapter.swapCursor(wrapper);
		
		setListShownNoAnimation(true);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {
		// This is called when the last Cursor provided to onLoadFinished()
        // above is about to be closed.  We need to make sure we are no
        // longer using it.
        mAdapter.swapCursor(null);
	}
    
}
