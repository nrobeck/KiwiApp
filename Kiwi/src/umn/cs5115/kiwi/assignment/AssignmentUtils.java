package umn.cs5115.kiwi.assignment;

import umn.cs5115.kiwi.Assignment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;

import com.android.datetimepicker.date.DatePickerDialog;
import com.android.datetimepicker.time.TimePickerDialog;

public class AssignmentUtils {
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
}
