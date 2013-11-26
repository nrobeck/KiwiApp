package umn.cs5115.kiwi.assignment;

import umn.cs5115.kiwi.R;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextUtils;
import android.widget.EditText;

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
