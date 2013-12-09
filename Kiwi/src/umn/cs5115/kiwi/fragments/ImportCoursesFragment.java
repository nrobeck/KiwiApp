package umn.cs5115.kiwi.fragments;

import umn.cs5115.kiwi.CalendarUtils;
import umn.cs5115.kiwi.CalendarUtils.EventCursorWrapper;
import umn.cs5115.kiwi.EditCourseActivity;
import umn.cs5115.kiwi.R;
import umn.cs5115.kiwi.Utils;
import umn.cs5115.kiwi.ui.DoneBar.DoneBarListenable;
import umn.cs5115.kiwi.ui.DoneBar.DoneBarListener;
import android.app.Activity;
import android.app.ListFragment;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.CalendarContract.Events;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class ImportCoursesFragment extends ListFragment {
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
        setListShown(true);
//        setEmptyText(getResources().getString(R.string.import_courses_nothing_to_import));
        
        final ListView lv = getListView();
        
//        setListShownNoAnimation(true);
        
        // Set up the calendar cursor
        Cursor mCursor = CalendarUtils.getCourseEventCursor(getActivity());
        
        // http://stackoverflow.com/a/6156962
        getActivity().startManagingCursor(mCursor);
        
        final EventCursorWrapper wrapper = new EventCursorWrapper(mCursor);
        final SimpleCursorAdapter adapter = new SimpleCursorAdapter(
                getActivity(), R.layout.import_course_item, wrapper,
                CalendarUtils.EVENT_QUERY_COLS, new int[] {R.id.title, R.id.content});

        // Set the adapter
        lv.setAdapter(adapter);
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
                
                Cursor c = adapter.getCursor();
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
                .putExtra(EditCourseActivity.EXTRA_EVENT_NAME, c.getString(c.getColumnIndex(Events.TITLE)));
                
                startActivity(makeNewIntent);
            }
        });
        
        if (getActivity() instanceof DoneBarListenable) {
            final Activity activity = getActivity();
            final DoneBarListenable listenable = (DoneBarListenable) getActivity();
            Log.i("EditAssignmentFragment", "Registering listener.");
            listenable.addDoneBarListener(new DoneBarListener() {
                @Override
                public boolean onDone() {
                    Log.i("EditAssignmentFragment", "DoneBarListener.onDone()");
                    boolean returnValue = onActionBarDone(activity);
                    if (!returnValue) {
                        /*
                         * One or more input fields are in error.
                         */
                        Toast.makeText(activity, "You must select a course to import.", Toast.LENGTH_SHORT).show();
                    }
                    return returnValue;
                }

                @Override
                public void onCancel() {
                    Log.i("EditAssignmentFragment", "DoneBarListener.onCancel()");
                }
            });
        }
    }

    protected boolean onActionBarDone(Activity activity) {
        // TODO Auto-generated method stub
        return false;
    }
    
}
