package com.mikewadsten.test.kiwi;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import com.espian.showcaseview.ShowcaseView;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Checkable;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class MainActivity extends Activity implements ShowcaseView.OnShowcaseEventListener {
    private Cursor mCursor = null;
    private ShowcaseView showcaseView;
    
    private static final String[] COLS = new String[] {
            CalendarContract.Events.TITLE,
            CalendarContract.Events.RRULE,
            CalendarContract.Events._ID
    };

    // https://gist.github.com/ramzes642/5400792
    private class EventCursorWrapper extends CursorWrapper {
        private int[] index;
        private int count = 0;
        private int pos = 0;

        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd'T'hhmmss'Z'", Locale.US);

        @SuppressLint("SimpleDateFormat")
        public EventCursorWrapper(Cursor cursor) {
            super(cursor);
            this.count = super.getCount();
            this.index = new int[this.count];
            for (int i = 0; i < this.count; i++) {
                super.moveToPosition(i);
                EventRecurrence recur = new EventRecurrence();
                recur.parse(this.getString(1));
                Date until;
                try {
                    until = format.parse(recur.until);
                } catch (ParseException e) {
                    e.printStackTrace();
                    continue;
                } catch (NullPointerException e) {
                    // recur.until is null. We're fine with this
                    try {
                        // Fake an end datetime. Yay!
                        until = format.parse("55551231T235959Z");
                    } catch (ParseException e1) {
                        e1.printStackTrace();
                        continue;
                    }
                }
                // Only track this item if its UNTIL is after now
                if (until == null || until.after(new Date())) {
                    this.index[this.pos++] = i;
                } else {
                    //Log.i("CalendarTest", "Ignoring: " + this.getString(0) + " (" + recur.until + ")");
                }
            }
            this.count = this.pos;
            this.pos = 0;
            super.moveToFirst();
        }

        @Override
        public boolean move(int offset) {
            return this.moveToPosition(this.pos+offset);
        }

        @Override
        public boolean moveToNext() {
            return this.moveToPosition(this.pos+1);
        }

        @Override
        public boolean moveToPrevious() {
            return this.moveToPosition(this.pos-1);
        }

        @Override
        public boolean moveToFirst() {
            return this.moveToPosition(0);
        }

        @Override
        public boolean moveToLast() {
            return this.moveToPosition(this.count-1);
        }

        @Override
        public boolean moveToPosition(int position) {
            if (position >= this.count || position < 0)
                return false;
            return super.moveToPosition(this.index[position]);
        }

        @Override
        public int getCount() {
            return this.count;
        }

        @Override
        public int getPosition() {
            return this.pos;
        }

        public void remove(int position) {
            if (position < 0 || position > this.count) {
                return;
            }
            int i = position;
            while (i < this.count) {
                this.index[i] = this.index[i+1];
                i++;
            }
            this.count--;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_import_courses);
        
        ActionBar b = getActionBar();
        if (b != null) {
            Utils.makeActionBarDoneButton(b, new DoneBar.DoneButtonHandler() {
                @Override
                public void onDone(View view) {
                    Toast.makeText(getBaseContext(), "Clicked Done!", Toast.LENGTH_SHORT).show();
                    notifyInThreeSeconds();
                    if (showcaseView.isShown()) {
//                        showcaseView.hide();
                    }
                }
            });
        }
        
        ShowcaseView.ConfigOptions co = new ShowcaseView.ConfigOptions();
        co.hideOnClickOutside = true;
        
        showcaseView = ShowcaseView.insertShowcaseView(R.id.actionbar_done,
                this, "Something...", "Something else...", co);

        String query = CalendarContract.Events.RRULE + "<> ''";
        query += " AND " + CalendarContract.Events.ALL_DAY + "=0";

        mCursor = getContentResolver().query(
                CalendarContract.Events.CONTENT_URI, COLS,
                query, null, null
        );
        if (mCursor == null) {
            return;
        }
        mCursor.moveToFirst();

        ListView lv = (ListView) findViewById(R.id.listview);
        final EventCursorWrapper wrapper = new EventCursorWrapper(mCursor);
        final SimpleCursorAdapter adapter = new SimpleCursorAdapter(
                this, R.layout.import_course_item, wrapper,
                COLS, new int[] {R.id.title, R.id.content});

        View headerView = View.inflate(this, R.layout.import_courses_header, null);
        headerView.setClickable(false);
        headerView.setFocusable(false);
        headerView.setFocusableInTouchMode(false);
        lv.addHeaderView(headerView, null, false);
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
                adapter.notifyDataSetChanged();
            }
        });

        // Get calendars as well
        Cursor calCursor = getContentResolver().query(
                CalendarContract.Calendars.CONTENT_URI, new String[]{
                CalendarContract.Calendars._ID,
                CalendarContract.Calendars.NAME,
                CalendarContract.Calendars.ACCOUNT_NAME,
                CalendarContract.Calendars.CALENDAR_DISPLAY_NAME
        }, null, null, null
        );
        if (calCursor == null) {
            return;
        }
        Log.d("CalendarTest", "Got calendars, I guess");
        while (calCursor.moveToNext()) {
            Log.i("CalendarTest",
                    String.format("Found calendar: %s: %s (%s - %s)",
                            calCursor.getString(0), calCursor.getString(1),
                            calCursor.getString(2), calCursor.getString(3)));
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.cancel, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.cancel:
                Toast.makeText(getBaseContext(), "Clicked Cancel!", Toast.LENGTH_SHORT).show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    private void notifyInThreeSeconds() {
        Intent i = new Intent(this, NotificationReceiver.class);
        PendingIntent alarm = PendingIntent.getBroadcast(this, 0, i, PendingIntent.FLAG_CANCEL_CURRENT);
        ((AlarmManager) getSystemService(ALARM_SERVICE)).set(AlarmManager.RTC_WAKEUP, Calendar.getInstance().getTimeInMillis() + 3000, alarm);
    }

    @Override
    public void onShowcaseViewHide(ShowcaseView showcaseView) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void onShowcaseViewShow(ShowcaseView showcaseView) {
        // TODO Auto-generated method stub
        
    }
}
