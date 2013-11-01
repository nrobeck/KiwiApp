package umn.cs5115.kiwi.assignment;

import umn.cs5115.kiwi.Assignment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;

import com.android.datetimepicker.time.TimePickerDialog;

public class AssignmentUtils {
	// Implementation based on AlarmUtils.showTimeEditDialog from the AOSP DeskClock app
	public static void showTimeEditDialog(FragmentManager manager,
			final Assignment assignment, TimePickerDialog.OnTimeSetListener listener) {
		int hour, minutes;
		
		if (assignment == null) {
			hour = 0;
			minutes = 0;
		} else {
			hour = assignment.getId();
			minutes = assignment.getId();
		}
		
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
