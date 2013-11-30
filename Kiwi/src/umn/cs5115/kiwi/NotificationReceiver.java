package umn.cs5115.kiwi;

import java.util.ArrayList;
import java.util.Calendar;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationCompat.InboxStyle;
import android.support.v4.app.NotificationCompat.Style;
import android.util.Log;

public class NotificationReceiver extends BroadcastReceiver {
	private static final int NOTIF_ID = 0x12435671;
	public static final String EXTRA_REMINDERS = "reminders";
	public static void removeNotifications(Context context) {
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.cancel(NOTIF_ID);
	}

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("NotificationReceiver", "Got notification intent!");
        
        ArrayList<String> reminders = intent.getStringArrayListExtra(EXTRA_REMINDERS);
        int reminderCount;
        if (reminders == null || reminders.size() < 1) {
        	reminderCount = 0;
        } else {
        	reminderCount = reminders.size();
        }
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        
        String title = "Kiwi: Scheduled reminders";
        Style style;
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
        notif.setSmallIcon(android.R.drawable.ic_media_play)
        .setContentTitle(title)
        .setContentText("You have upcoming assignments")
        .setTicker("Upcoming assignments: " + (reminderCount > 0 ? reminderCount : "None"))
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
        					.putStringArrayListExtra(EXTRA_REMINDERS, reminders),
        				0));
        manager.notify(NOTIF_ID, notif.build());
    }

}