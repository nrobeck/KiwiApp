package umn.cs5115.kiwi;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import net.fortuna.ical4j.model.Recur;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.CursorLoader;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.net.Uri;
import android.provider.CalendarContract;
import android.provider.CalendarContract.Events;

public class CalendarUtils {
    public static final String[] EVENT_QUERY_COLS = new String[] {
        CalendarContract.Events.TITLE,
        CalendarContract.Events.RRULE,
        CalendarContract.Events.EVENT_LOCATION,
        CalendarContract.Events.DTSTART,
        CalendarContract.Events.DTEND,
        CalendarContract.Events.DURATION,
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
    	
        String query = Events.RRULE + "<> ''";
        query += " AND " + Events.ALL_DAY + "=0";

        mCursor = context.getContentResolver().query(
                Events.CONTENT_URI, EVENT_QUERY_COLS,
                query, null, null
        );
        if (mCursor == null) {
            return null;
        }
        mCursor.moveToFirst();
        
        return mCursor;
    }
    
    public static CursorLoader getCourseEventCursorLoader(Context context) {
    	Uri uri = CalendarContract.Events.CONTENT_URI;
    	
    	String query = String.format("%s <> '' AND %s = 0", Events.RRULE, Events.ALL_DAY);
    	
    	return new CursorLoader(context, uri, EVENT_QUERY_COLS, query, null, null);
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
                Recur recur;
				try {
					recur = new Recur(this.getString(1));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					continue;
				}
                Date until = recur.getUntil();
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
