package umn.cs5115.kiwi.assignment;

import java.util.Calendar;
import java.util.Date;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextUtils;
import android.widget.EditText;

import com.android.datetimepicker.date.DatePickerDialog;
import com.android.datetimepicker.time.TimePickerDialog;

public class AssignmentUtils {
	public static final Date END_OF_SEMESTER;
	static {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, 2013);
		cal.set(Calendar.MONTH, 11); // december. it's stupid
		cal.set(Calendar.DAY_OF_MONTH, 31);
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE, 59);
		END_OF_SEMESTER = cal.getTime();
	}
	
	public static class DueDateBuilder {
		private int year, month, day, hour, minute;
		
		public DueDateBuilder() {}
		
		/**
		 * Constructor. Takes in a time in milliseconds (i.e. how we
		 * will represent due date/time in the database) and parses that
		 * into the relevant fields here.
		 * 
		 * @param fromMillis time in milliseconds to build up a builder
		 */
		public DueDateBuilder(long fromMillis) {
			Calendar cal = Calendar.getInstance();
			cal.setTimeInMillis(fromMillis);
			this.year = cal.get(Calendar.YEAR);
			this.month = cal.get(Calendar.MONTH);
			this.day = cal.get(Calendar.DAY_OF_MONTH);
			this.hour = cal.get(Calendar.HOUR_OF_DAY);
			this.minute = cal.get(Calendar.MINUTE);
		}
		
		/**
		 * Set this due date/time's year
		 * @param year the year
		 * @return this DueDateBuilder object, to chain calls
		 */
		public DueDateBuilder setYear(int year) {
			this.year = year;
			return this;
		}
		
		/**
		 * Get the year on this builder
		 * @return the year
		 */
		public int getYear() {
			return year;
		}
		
		/**
		 * Set this due date/time's month
		 * @param month the month
		 * @return this DueDateBuilder object, to chain calls
		 */
		public DueDateBuilder setMonth(int month) {
			this.month = month;
			return this;
		}
		
		/**
		 * Get the month on this builder
		 * @return the month
		 */
		public int getMonth() {
			return month;
		}
		
		public DueDateBuilder setDayOfMonth(int day) {
			this.day = day;
			return this;
		}
		public int getDayOfMonth() {
			return day;
		}
		
		public DueDateBuilder setHour(int hour) {
			this.hour = hour;
			return this;
		}
		public int getHour() {
			return hour;
		}
		
		public DueDateBuilder setMinute(int minute) {
			this.minute = minute;
			return this;
		}
		public int getMinute() {
			return minute;
		}
		
		private Calendar toCalendar() {
			Calendar cal = Calendar.getInstance();
			cal.set(Calendar.YEAR, year);
			cal.set(Calendar.MONTH, month);
			cal.set(Calendar.DAY_OF_MONTH, day);
			cal.set(Calendar.HOUR_OF_DAY, hour);
			cal.set(Calendar.MINUTE, minute);
			return cal;
		}
		
		/**
		 * "Build" the due date out to a Date object (for comparisons, etc.)
		 * @return a Date object representing this due date builder's values
		 */
		public Date toDate() {
			return toCalendar().getTime();
		}
		
		/**
		 * "Build" the due date out to a millisecond value.
		 * @return the milliseconds value represented by this builder
		 */
		public long toMilliseconds() {
			return toCalendar().getTimeInMillis();
		}
	}
	
	
	public static void showDateEditDialog(FragmentManager manager, int year, int month, int day, DatePickerDialog.OnDateSetListener listener) {
		DatePickerDialog dialog = DatePickerDialog.newInstance(listener, year, month, day);
		
		manager.executePendingTransactions();
		final FragmentTransaction ft = manager.beginTransaction();
		final Fragment prev = manager.findFragmentByTag("DATEPICK");
		if (prev != null) {
			ft.remove(prev);
		}
		ft.commit();
		
		if (dialog != null && !dialog.isAdded()) {
			dialog.show(manager, "DATEPICK");
		}
	}
	
	// Implementation based on AlarmUtils.showTimeEditDialog from the AOSP DeskClock app
	public static void showTimeEditDialog(FragmentManager manager,
			int hour, int minutes, TimePickerDialog.OnTimeSetListener listener) {
		TimePickerDialog dialog = TimePickerDialog.newInstance(listener, hour, minutes, false);
		dialog.setThemeDark(false);
		
		manager.executePendingTransactions();
		final FragmentTransaction ft = manager.beginTransaction();
		final Fragment prev = manager.findFragmentByTag("TIMEPICK");
		if (prev != null) {
			ft.remove(prev);
		}
		ft.commit();
		
		if (dialog != null && !dialog.isAdded()) {
			dialog.show(manager, "TIMEPICK");
		}
	}
	
	public static class EmptyErrorTextWatcher implements android.text.TextWatcher {
		private final EditText edit;
		private final CharSequence error;
		public EmptyErrorTextWatcher(EditText edit, CharSequence errorMessage) {
			this.edit = edit;
			error = errorMessage;
		}

		@Override
		public void afterTextChanged(Editable s) {
			// pass
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// pass
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			/*
			 * Call setError with null or the error message, depending on the
			 * contents of edit. (Otherwise, the error state does not
			 * clear itself out automatically until space is pressed...)
			 */
			if (TextUtils.isEmpty(s)) {
				edit.setError(error);
			} else {
				edit.setError(null);
			}
		}
		
	}
}
