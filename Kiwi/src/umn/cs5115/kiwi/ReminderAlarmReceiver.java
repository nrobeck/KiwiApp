package umn.cs5115.kiwi;

import java.util.ArrayList;
import java.util.Calendar;

import umn.cs5115.kiwi.DatabaseHandler.DbAndCursor;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationCompat.InboxStyle;
import android.support.v4.app.NotificationCompat.Style;
import android.text.TextUtils;
import android.util.Log;

public class ReminderAlarmReceiver extends BroadcastReceiver {
	private static final int NOTIFICATION_ID = 0x54946683; // KIWINOTF
	public static final String EXTRA_REMINDER_TIME = "when";
	
	public static void removeNotifications(Context context) {
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.cancel(NOTIFICATION_ID);
	}
	
	private ArrayList<String> getCurrentReminders(Context context, long time) {
        DbAndCursor dbc = new DatabaseHandler(context).queryReminders(time);
        ArrayList<String> reminders = new ArrayList<String>();
        Cursor c = dbc.cursor;
        if (c.getCount() > 0) {
        	c.moveToFirst();
        	while (!c.isAfterLast()) {
        		String cdes = c.getString(c.getColumnIndex(DatabaseHandler.REMINDER_CDES));
        		String aname = c.getString(c.getColumnIndex(DatabaseHandler.REMINDER_ANAME));
        		String atype = c.getString(c.getColumnIndex(DatabaseHandler.REMINDER_ATYPE));
        		long remindermillis = c.getLong(c.getColumnIndex(DatabaseHandler.REMINDER_MILLIS));
//        		Log.d("MainActivity", String.format("%s %s %s %d", cdes, aname, atype, remindermillis));
        		if (remindermillis > time) {
        			// Somehow a reminder got through the query WHERE filter...
        			c.moveToNext();
        			continue;
        		}
        		
        		if (TextUtils.isEmpty(cdes)) {
        			cdes = "";
        		} else {
        			cdes = String.format("%s: ", cdes);
        		}
        		if (TextUtils.isEmpty(atype)) {
        			atype = "";
        		} else {
        			atype = String.format(" (%s)", atype);
        		}
        		
        		reminders.add(cdes + aname + atype);
        		c.moveToNext();
        	}
        }
        dbc.close();
        
        return reminders;
	}

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("NotificationReceiver", "Got notification intent!");
        
        ArrayList<String> reminders = getCurrentReminders(
        		context,
        		intent.getLongExtra(EXTRA_REMINDER_TIME,
        							Calendar.getInstance().getTimeInMillis()));
        
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        
        String title = "Upcoming assignments";
        Style style;
        int reminderCount = reminders.size();
        if (reminderCount > 0) {
        	style = new InboxStyle();
        	InboxStyle istyle = (InboxStyle)style;
        	for (String reminder : reminders) {
        		istyle.addLine(reminder);
        	}

            istyle.setBigContentTitle(title);
        	istyle.setSummaryText("Kiwi - Assignment and Exam Manager");
        } else {
        	style = null;
        }
        
        NotificationCompat.Builder notif = new NotificationCompat.Builder(context);
        notif.setSmallIcon(R.drawable.ic_kiwi_notif)
        //.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.icon_kiwi))
        .setContentTitle(title)
        .setContentText(String.format("You have %d upcoming assignment(s)", reminderCount))
        .setTicker(reminderCount < 1 ? "No upcoming assignments" : reminderCount + " upcoming assignment(s)")
        .setWhen(Calendar.getInstance().getTimeInMillis())
        .setStyle(style)
        .setAutoCancel(true);
        
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
        	// Add text on non-expanded notification to make sure the user knows
        	// how to access the reminders.
        	notif.setContentText("Expand this notification to see reminders");
        }
        
        if (reminderCount > 0) {
        	notif.setNumber(reminderCount);
        } else {
        	notif.setContentText("You have no upcoming assignments");
        }
        
        // When the notification is clicked, bring up the MainActivity.
        // TODO: Should we maybe add a new activity to show only assignments from reminders?
        notif.setContentIntent(
        		PendingIntent.getActivity(
        				context, 101,
        				// Got this off StackOverflow. Launch activity just like opening from launcher.
        				// This is so that clicking the notification when already looking at the MainActivity
        				// doesn't launch a new instance of the activity (and making it so clicking the Back button
        				// just cycles back to the old instance)
        				new Intent(context, MainActivity.class)
        					.setAction(Intent.ACTION_MAIN)
        					.addCategory(Intent.CATEGORY_LAUNCHER)
        					.putStringArrayListExtra("reminder_texts", reminders),
        				0));
        manager.notify(NOTIFICATION_ID, notif.build());
        
        // Schedule the next one
        ReminderUtils.autoScheduleReminders(context);
    }

}