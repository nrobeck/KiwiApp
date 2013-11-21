package umn.cs5115.kiwi.ui;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;

public class DateButton extends Button {
	private int year, month, day;

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
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.MONTH, month);
		cal.set(Calendar.DAY_OF_MONTH, day);
		setText(new SimpleDateFormat("EEE, MMM dd, ''yy", Locale.US).format(cal.getTime()));
	}
	
	public void setDate(int year, int month, int day) {
		this.year = year;
		this.month = month;
		this.day = day;
		
		refreshView();
	}
}
