package umn.cs5115.kiwi;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.provider.CalendarContract;

public class CalendarUtils {
    public static final String[] EVENT_QUERY_COLS = new String[] {
        CalendarContract.Events.TITLE,
        CalendarContract.Events.RRULE,
        CalendarContract.Events._ID
    };
    
    /**
     * Get a Cursor over all events in the Android calendar, where the repeat rule (RRULE)
     * is non-empty and the events are NOT all day events.
     * 
     * @param context The context to use to get a content resolver for querying
     * @return the Cursor over events in the Calendar, or null if something went wrong
     */
    public static Cursor getCourseEventCursor(Context context) {
    	Cursor mCursor;
    	
        String query = CalendarContract.Events.RRULE + "<> ''";
        query += " AND " + CalendarContract.Events.ALL_DAY + "=0";

        mCursor = context.getContentResolver().query(
                CalendarContract.Events.CONTENT_URI, EVENT_QUERY_COLS,
                query, null, null
        );
        if (mCursor == null) {
            return null;
        }
        mCursor.moveToFirst();
        
        return mCursor;
    }
    
    // https://gist.github.com/ramzes642/5400792
    /**
     * CursorWrapper used to wrap the cursor over calendar events, filtering out
     * any repeating event where the end time is BEFORE the time the cursor wrapper
     * was created (i.e. filters out any events that have ended)
     *
     * @author Mike Wadsten
     *
     */
    public static class EventCursorWrapper extends CursorWrapper {
        private int[] index;
        private int count = 0;
        private int pos = 0;

        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd'T'hhmmss'Z'", Locale.US);

        @SuppressLint("SimpleDateFormat")
        public EventCursorWrapper(Cursor cursor) {
            super(cursor);
            this.count = super.getCount();
            this.index = new int[this.count];
            
            final Date date = new Date();
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
				if (until == null || until.after(date)) {
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

}
