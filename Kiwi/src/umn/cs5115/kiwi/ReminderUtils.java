package umn.cs5115.kiwi;

import java.util.Calendar;

import net.fortuna.ical4j.model.Recur;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.util.Pair;

/**
 * Utility functions for scheduling reminder notifications and
 * rendering those notifications at the appropriate time.
 * @author mike
 *
 */
public class ReminderUtils {
	private static final int ALARM_REQUEST_CODE = 0x54942676; //KIWIALRM
	
	/**
	 * Get a Pair of Intent and PendingIntent, just in case we want to put
	 * extra values on the intent.
	 * 
	 * (Most likely will be dropped in favor of just a getPendingIntent method)
	 * @param context
	 * @return
	 */
	private static Pair<Intent, PendingIntent> getIntentPair(Context context) {
		Intent intent = new Intent(context, ReminderAlarmReceiver.class);
		PendingIntent pend = PendingIntent.getBroadcast(
				context, ALARM_REQUEST_CODE, intent, 0);
		return new Pair<Intent, PendingIntent>(intent, pend);
	}
	
	/**
	 * Allow for scheduling reminder notifications outside of the built-in
	 * pre-scheduled reminder system. (Generally will be used for app
	 * demonstrations)
	 * @param context context to use
	 * @param manager the AlarmManager instance, or null to get it automatically
	 * @param when the time to schedule a reminder for
	 */
	public static void scheduleReminders(Context context, AlarmManager manager, long when) {
		Log.d("ReminderUtils", "scheduleReminders...");
		if (context == null) {
			throw new NullPointerException("context must not be null!");
		}
		
		Pair<Intent, PendingIntent> pair = getIntentPair(context);
		
		if (manager == null) {
			manager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
		}
		
		// Cancel any pre-scheduled notifications
//		manager.cancel(pair.second);
		
		// Schedule this next one.
		manager.set(AlarmManager.RTC_WAKEUP, when, pair.second);
	}
	
	/**
	 * Use the {@link AlarmManager} system service to schedule the next
	 * reminder notification. Needs to use (TBD: preferences?) to find
	 * the time for the notification, and then use iCal4j to find the next
	 * actual instance.
	 * @param context context for making intents and such
	 */
	public static void autoScheduleReminders(Context context) {
		// TODO: Implement this.
		SharedPreferences prefs = context.getSharedPreferences(null, Context.MODE_PRIVATE);
		if (!prefs.getBoolean("give_reminders", false)) {
			// TODO: This could be done better. Perhaps PreferenceUtils?
			
			// This means the user has decided they don't want reminders.
			return;
		}
		
		Recur recur = new Recur();
		recur.setFrequency(Recur.DAILY);
		// TODO: Pull user's specified reminder time(s) from preferences.
		recur.getHourList().add(18);
		recur.getMinuteList().add(30);
		// TODO: Use iCal4j to find the next Date to make the notification
		
		long nextReminder;
		nextReminder = Calendar.getInstance().getTimeInMillis();
		
		scheduleReminders(context, null, nextReminder);
	}
}
