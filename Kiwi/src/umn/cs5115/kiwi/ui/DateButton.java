package umn.cs5115.kiwi.ui;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;

public class DateButton extends Button {
	private final Calendar mCal = Calendar.getInstance(Locale.US);
	
	public CharSequence prefix = "";

	public DateButton(Context context) {
		super(context);
	}

	public DateButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public DateButton(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	private void refreshView() {
		setText(prefix + new SimpleDateFormat("EEE, MMM dd, ''yy", Locale.US).format(getDateObject()));
	}
	
	/**
	 * Set the date represented by this button
	 * @param year the year
	 * @param month the month (note that months are indexed from 0)
	 * @param day the day of the month
	 */
	public void setDate(int year, int month, int day) {
		mCal.set(Calendar.YEAR, year);
		mCal.set(Calendar.MONTH, month);
		mCal.set(Calendar.DAY_OF_MONTH, day);
		
		refreshView();
	}
	
	/**
	 * @return a {@link Date} object corresponding to this button's date
	 */
	public Date getDateObject() {
		return mCal.getTime();
	}
	
	/**
	 * @return the year value on this button
	 */
	public int getYear() {
		return mCal.get(Calendar.YEAR);
	}
	
	/**
	 * @return the month value on this button
	 */
	public int getMonth() {
		return mCal.get(Calendar.MONTH);
	}
	
	/**
	 * @return the day value on this button
	 */
	public int getDay() {
		return mCal.get(Calendar.DAY_OF_MONTH);
	}
}