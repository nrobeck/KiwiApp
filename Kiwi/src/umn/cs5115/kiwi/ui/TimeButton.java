package umn.cs5115.kiwi.ui;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;

public class TimeButton extends Button {
	private int hour, minute;

	public TimeButton(Context context) {
		super(context);
	}

	public TimeButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public TimeButton(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	private void refreshView() {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, hour);
		cal.set(Calendar.MINUTE, minute);
		setText(new SimpleDateFormat("h:mm aaa", Locale.US).format(cal.getTime()));
	}
	
	/**
	 * Set the time shown on the button.
	 * @param hour the hour
	 * @param minute the minute
	 */
	public void setTime(int hour, int minute) {
		this.hour = hour;
		this.minute = minute;
		
		refreshView();
	}
	
	/**
	 * Retrieve the hour displayed on this button
	 * @return the hour value
	 */
	public int getHour() {
		return this.hour;
	}
	
	/**
	 * Retrieve the minute displayed on this button
	 * @return the minute value
	 */
	public int getMinute() {
		return this.minute;
	}
}
