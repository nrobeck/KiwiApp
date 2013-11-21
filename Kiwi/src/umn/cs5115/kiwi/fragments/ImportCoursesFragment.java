package umn.cs5115.kiwi.fragments;

import umn.cs5115.kiwi.CalendarUtils;
import umn.cs5115.kiwi.CalendarUtils.EventCursorWrapper;
import umn.cs5115.kiwi.R;
import android.app.ListFragment;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Checkable;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

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
        
        final EventCursorWrapper wrapper = new EventCursorWrapper(mCursor);
        final SimpleCursorAdapter adapter = new SimpleCursorAdapter(
                getActivity(), R.layout.import_course_item, wrapper,
                CalendarUtils.EVENT_QUERY_COLS, new int[] {R.id.title, R.id.content});

        // Set the adapter
        lv.setAdapter(adapter);
        lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Checkable child = (Checkable) adapterView.getChildAt(i);
                if (child == null) {
                    return;
                }
                child.toggle();
//                view.setActivated(!child.isChecked());
                
                adapter.notifyDataSetChanged();
            }
        });
    }
    
}
