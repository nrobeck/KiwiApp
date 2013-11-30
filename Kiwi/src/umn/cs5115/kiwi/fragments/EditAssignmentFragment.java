package umn.cs5115.kiwi.fragments;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import umn.cs5115.kiwi.DatabaseHandler;
import umn.cs5115.kiwi.EditAssignmentActivity;
import umn.cs5115.kiwi.R;
import umn.cs5115.kiwi.adapter.DummyItemAdapter;
import umn.cs5115.kiwi.assignment.AssignmentUtils;
import umn.cs5115.kiwi.assignment.AssignmentUtils.DueDateBuilder;
import umn.cs5115.kiwi.assignment.AssignmentUtils.EmptyErrorTextWatcher;
import umn.cs5115.kiwi.model.Assignment;
import umn.cs5115.kiwi.model.Course;
import umn.cs5115.kiwi.ui.DateButton;
import umn.cs5115.kiwi.ui.DoneBar.DoneBarListenable;
import umn.cs5115.kiwi.ui.DoneBar.DoneBarListener;
import umn.cs5115.kiwi.ui.TimeButton;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.datetimepicker.date.DatePickerDialog;
import com.android.datetimepicker.date.DatePickerDialog.OnDateSetListener;
import com.android.datetimepicker.time.RadialPickerLayout;
import com.android.datetimepicker.time.TimePickerDialog.OnTimeSetListener;

public class EditAssignmentFragment extends Fragment {
	private EditText assignmentName;
	private String[] typesArray;
	private Spinner typesSpinner;

	private View findView(int id) {
		return getView().findViewById(id);
	}

	private void loadLocals(View layout) {
		try {
			assignmentName = (EditText) layout.findViewById(R.id.editText1);
		} catch (Exception e) {
			Log.e("EditAssignmentFragment", "loadLocals failed", e);
		}
	}

	/**
	 * Go over the inputs in the layout, calling setError where necessary to
	 * inform the user what they need to do.
	 */
	@SuppressWarnings("unused")
	private void setErrorsAsNeeded() {
		if (TextUtils.isEmpty(assignmentName.getText().toString())) {
			assignmentName.setError("Cannot be empty!");
		}
	}

	private boolean onActionBarDone(final EditAssignmentActivity activity) {
		Course selectedCourse = null;
		String assignmentType = "";
		String assignmentName = "";
		String textbook = "";
		String notes = "";

		//Pulling the information out of the activity's fields
		Spinner courseNameSpinner = (Spinner) activity.findViewById(R.id.courses_spinner); //Course Name
		if(courseNameSpinner.getSelectedItemPosition() > 0) {
			Object tag = courseNameSpinner.getSelectedView().getTag();
			if (tag instanceof Course) {
				selectedCourse = (Course)tag;
			} else {
				Log.e("EditAssignmentFragment", "selected course view tag is not Course object!");
			}
		}
		else {
			selectedCourse = null;
		}

		// Assignment type
		int typePos = typesSpinner.getSelectedItemPosition();
		if (typePos > 0) {
			Object tag = typesSpinner.getSelectedView().getTag();
			if (tag instanceof String) {
				assignmentType = (String) tag;
			} else {
				Log.e("EditAssignmentFragment", "Selected type view has non-String tag...");
			}
			//Log.e("ERROR: EditAssignmentFragment", ""+assignmentType);
		}
		else {
			assignmentType = null;
			Log.d("EditAssignmentFragment", "type pos: " + typePos);
		}

		EditText assignmentNameEditText = (EditText) findView(R.id.editText1); //Assignment Name   
		assignmentName = assignmentNameEditText.getText().toString();
		//Put this on each of the required fields and change the Toast message to say what is wrong
		if (assignmentName.isEmpty()) {
			assignmentNameEditText.setError(getResources().getString(R.string.assignment_title_empty_error));
			/*
			 * Scroll back to the top to make sure the user sees the error message.
			 */
			ScrollView sv = (ScrollView)getView();
			sv.scrollTo(0, 0);
			return false;
		} else {
			assignmentNameEditText.setError(null);
		}

		Spinner textbookSpinner = (Spinner) activity.findViewById(R.id.textbook_spinner); //Textbook Name
		if(textbookSpinner.getSelectedItemPosition() > 0) {
			textbook = textbookSpinner.getSelectedItem().toString();
		}
		else {
			textbook = "";
		}
		
		Spinner reminderSpinner = (Spinner) findView(R.id.reminder_spinner);
		int reminderHoursPosition = reminderSpinner.getSelectedItemPosition();
		int reminderHours = -1;
		int[] hoursArray = getResources().getIntArray(R.array.reminder_entry_values);
		if (reminderHoursPosition >= hoursArray.length || reminderHoursPosition < 0) {
			// Something's wrong with resources, or the spinner...
			reminderHours = -1;
		} else {
			reminderHours = hoursArray[reminderHoursPosition];
		}

		//TODO: Due Date
		//TODO: Reminder and number picker for this field
		//TODO: Field to specify how many times to repeat, etc.

		EditText notesEditText = (EditText) activity.findViewById(R.id.assignment_notes_field);
		notes = notesEditText.getText().toString();

		// TODO: Validate input...
		//       if (!isValidInput) {
		//           Toast.makeText(getActivity(), "Invalid input", Toast.LENGTH_SHORT).show();
		//           return false;
		//       }

		int assignmentId = activity.getAssignmentId();

		final DateButton db = (DateButton)findView(R.id.date_button);
		final TimeButton tb = (TimeButton)findView(R.id.time_button);
		
		DueDateBuilder ddb = new DueDateBuilder();
		ddb.setYear(db.getYear())
			.setMonth(db.getMonth())
			.setDayOfMonth(db.getDay())
			.setHour(tb.getHour())
			.setMinute(tb.getMinute());
		long dueMillis = ddb.toMilliseconds();
		
		DatabaseHandler dbHandler = new DatabaseHandler(activity);
		
		int selectedCourseId = (selectedCourse == null) ? 0 : selectedCourse.getId();
		Assignment assignment = new Assignment(assignmentId, assignmentName, selectedCourseId, assignmentType, dueMillis, reminderHours, notes, textbook);

        //Store the assignment
        
		if (activity.isEdit()) {
			//TODO: Need to pass in the assignment ID
			dbHandler.editAssignment(assignment);
		} else {
			dbHandler.addAssignment(assignment);
		}
		
		return true;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
        setRetainInstance(true);

		View layout = inflater.inflate(R.layout.edit_assignment_fragment, container);

		loadLocals(layout);

		final EditText assignmentNameEditText = (EditText) layout.findViewById(R.id.editText1); //Assignment Name   
		assignmentNameEditText.addTextChangedListener(
				new EmptyErrorTextWatcher(
						assignmentNameEditText,
						getResources().getString(R.string.assignment_title_empty_error)));

		typesSpinner  = (Spinner)layout.findViewById(R.id.assignment_types_spinner);
		typesArray = getResources().getStringArray(R.array.assignment_types);
		DummyItemAdapter<String> typesAdapter = new DummyItemAdapter<String>(getActivity(),
				android.R.layout.simple_spinner_dropdown_item, typesArray, getResources().getString(R.string.default_assignment_type)) {
			@Override
			public Object getViewTag(String obj) {
				return obj;
			}

			@Override
			public String getViewText(int position, String obj, boolean isDummy) {
				return obj;
			}
		};

		typesSpinner.setAdapter(typesAdapter);
		
		final DateButton db = (DateButton)layout.findViewById(R.id.date_button);
		final TimeButton tb = (TimeButton)layout.findViewById(R.id.time_button);

		db.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				AssignmentUtils.showDateEditDialog(getFragmentManager(), db.getYear(), db.getMonth(), db.getDay(), new OnDateSetListener() {
					@Override
					public void onDateSet(DatePickerDialog dialog, int year, int monthOfYear,
							int dayOfMonth) {
						db.setDate(year, monthOfYear, dayOfMonth);
					}
				});
			}
		});

		tb.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				AssignmentUtils.showTimeEditDialog(getFragmentManager(), tb.getHour(), tb.getMinute(), new OnTimeSetListener() {

					@Override
					public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {
						tb.setTime(hourOfDay, minute);
					}
				});
			}
		});

		return layout;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		final Activity activity = getActivity();
		final EditAssignmentActivity eActivity = (EditAssignmentActivity) activity;

		//TODO: Pull all the course names from the Database to fill the Course Name Spinner
		final DatabaseHandler dbHandler = new DatabaseHandler(getActivity());
		List<String> courseSpinnerArray =  new ArrayList<String>();
		courseSpinnerArray.add("(Course)");
		Course[] coursesArray = dbHandler.getCourses(); //TODO: This is NOT returning any courses when there have been courses add?
		int selectedCourseIndex = 0;
		for	(int i = 0; i < coursesArray.length; i++) {
			Course course = coursesArray[i];
			String text;
			if (TextUtils.isEmpty(course.getCourseDesignation())) {
				text = course.getCourseTitle();
			} else {
				text = String.format("%s: %s", course.getCourseDesignation(), course.getCourseTitle());
			}
			courseSpinnerArray.add(text); //This will require the course designation though?

			// TODO: Grab assignment from activity?
			if (course.getId() == activity.getTaskId()) {
				selectedCourseIndex = i + 1;
			}
		}

		DummyItemAdapter<Course> coursesAdapter = new DummyItemAdapter<Course>(
				getActivity(), android.R.layout.simple_spinner_dropdown_item, coursesArray, new Course()) {
			@Override
			public Object getViewTag(Course obj) {
				return obj;
			}

			@Override
			public String getViewText(int position, Course course, boolean isDummy) {
				if (isDummy) {
					return getResources().getString(R.string.default_course);
				} else if (TextUtils.isEmpty(course.getCourseDesignation())) {
					return course.getCourseTitle();
				} else {
					return String.format("%s: %s", course.getCourseDesignation(), course.getCourseTitle());
				}
			}
		};
		Spinner courseSpinner = (Spinner) findView(R.id.courses_spinner);
		courseSpinner.setAdapter(coursesAdapter);
		courseSpinner.setSelection(selectedCourseIndex, false);
		
		final ArrayList<String> textbooksList = new ArrayList<String>();
		final DummyItemAdapter<String> textbooksAdapter =
		        new DummyItemAdapter<String>(getActivity(),
		                android.R.layout.simple_spinner_dropdown_item,
		                textbooksList, "Textbook (optional)") {

            @Override
            public Object getViewTag(String obj) {
                if (obj.equals(getDummy())) {
                    return null;
                } else {
                    return obj;
                }
            }
            
            @Override
            public String getViewText(int position, String obj,
                    boolean isDummy) {
                return obj;
            }
        };
        final Spinner textbooks = (Spinner) findView(R.id.textbook_spinner);
        textbooks.setAdapter(textbooksAdapter);
        textbooks.setSelection(0, false);
        
        courseSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> spinner, View selection,
                    int pos, long id) {
                Log.d("EditAssignmentFragment", "on course selected " + pos);
                try {
                    Object tag = selection.getTag();
                    Course course = (Course)tag;
                    
                    DummyItemAdapter<?> textbooksAdapter = (DummyItemAdapter<?>)textbooks.getAdapter();
                    String currentTextbook = (String) textbooks.getSelectedItem();
                    Log.d("EditAssignmentFragment", "Current textbook: " + currentTextbook);

                    textbooksList.clear();
                    if (course == null || course.getId() == 0 || course.getTextbooksArray().length < 1) {
                        // Hide the textbook spinner, since it's pointless
                        // without the course or any textbooks to pick from.
                        textbooks.setVisibility(View.GONE);
                    } else {
                        textbooks.setVisibility(View.VISIBLE);
                        // Add the course's textbooks to the list
                        String[] textbooksArray = course.getTextbooksArray();
                        for (String tb : textbooksArray) {
                            textbooksList.add(tb);
                        }
                    }
                    
                    // This is a bit of a terrible hack. Select the textbook
                    // that is the same as the old selected textbook...
                    int index = textbooksList.indexOf(currentTextbook);
                    if (index < 0) {
                        index = 0;
                    } else {
                        index++;
                    }
                    Log.d("EditAssignmentFragment", "selecting textbook index " + index);

                    textbooksAdapter.notifyDataSetChanged();
                    textbooks.setSelection(index, true);
                } catch (ClassCastException exc) {
                    // tag on the views should be Course objects...
                    Log.e("EditAssignmentFragment", "Caught ClassCastException", exc);
                } catch (Exception e) {
                    Log.e("EditAssignmentFragment", "Caught exception", e);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

		if (activity instanceof DoneBarListenable) {
			final DoneBarListenable listenable = (DoneBarListenable) getActivity();
			Log.i("EditAssignmentFragment", "Registering listener.");
			listenable.addDoneBarListener(new DoneBarListener() {
				@Override
				public boolean onDone() {
					Log.i("EditAssignmentFragment", "DoneBarListener.onDone()");
					boolean returnValue = onActionBarDone(eActivity);
					if (!returnValue) {
						/*
						 * One or more input fields are in error.
						 */
						Toast.makeText(eActivity, "Cannot save assignment until errors are fixed.", Toast.LENGTH_SHORT).show();
					}
					return returnValue;
				}

				@Override
				public void onCancel() {
					Log.i("EditAssignmentFragment", "DoneBarListener.onCancel()");
				}
			});
		}

        /*
         * Fragments will by default save quite a bit of state (like entry in
         * EditTexts, checkbox state, etc.) so leverage that here.
         */
        super.onActivityCreated(savedInstanceState);
        

		final DateButton db = (DateButton)findView(R.id.date_button);
		final TimeButton tb = (TimeButton)findView(R.id.time_button);
		
		// Set due date/time to right now (TODO change this to something sane. Or add a null option...)
		Calendar cal = new GregorianCalendar();
		db.setDate(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
		tb.setTime(cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE));
        
		final boolean isEdit = eActivity.isEdit();
		if (isEdit) {
		    int id = eActivity.getAssignmentId();
		    Assignment as = dbHandler.getAssignment(id);
		    if (as == null) {
		        /*
		         * Something weird is probably happening with the database, if
		         * we're editing an assignment whose ID does not belong to any
		         * assignment in the database.
		         */
		        Log.e("EditAssignmentFragment", "No assignment by that id: " + id);
		        Toast.makeText(eActivity, "Assignment not found.", Toast.LENGTH_SHORT).show();
		        
		        /*
		         * Guess we'll have to forcibly finish out...
		         */
		        eActivity.finish();
		    }
		    
		    DueDateBuilder ddb = new DueDateBuilder(as.getDueMillis());
		    // TODO: Pick a sane "no assignment will be due before this time" time
		    long noDueBefore = 1000;
		    if (ddb.toMilliseconds() < noDueBefore) {
		    	// Assignment can't be due before this time. Don't mess with DateButton/TimeButton
		    } else {
		    	db.setDate(ddb.getYear(), ddb.getMonth(), ddb.getDayOfMonth());
		    	tb.setTime(ddb.getHour(), ddb.getMinute());
		    }
		    
		    //Filling in the information for the assignment
		    ((EditText)findView(R.id.editText1)).setText(as.getName());
		    
		    String[] assignmentTypes = getResources().getStringArray(R.array.assignment_types);
		    int assignmentTypePos = 0;
		    for(int i = 0; i < assignmentTypes.length; i++) {
		    	if(assignmentTypes[i].equals(as.getType())) {
		    		assignmentTypePos = i;
		    		assignmentTypePos++; //This is because of the hint dummy item in the spinner
		    	}
		    }
		    typesSpinner.setSelection(assignmentTypePos);
		    
		    int assignmentCoursePos = 0;
		    for(int i = 0; i < coursesArray.length; i++) {
		    	if(coursesArray[i].getCourseDesignation().equals(as.getCourseDesignation())) { //This is assuming all are unique
		    		assignmentCoursePos = i;
		    		assignmentCoursePos++; //This is because of the hint dummy item in the spinner
		    	}
		    }
		    
		    int courseCount = coursesAdapter.getCount();
		    int courseSelectionIndex = 0;
		    Course selectedCourse = null;
		    for (int i = 0; i < courseCount; i++) {
		        Course c = coursesAdapter.getItem(i);
		        if (c.getId() == as.getCourse()) {
		            courseSelectionIndex = i;
		            selectedCourse = c;
		            break;
		        }
		    }
		    courseSpinner.setSelection(courseSelectionIndex, false);
		    
		    /*
		     * Pick the relevant item in the reminders spinner.
		     */
		    Spinner reminderSpinner = (Spinner)findView(R.id.reminder_spinner);
		    int[] originalReminderEntries = getResources().getIntArray(R.array.reminder_entry_values);
		    Integer[] reminderEntries = new Integer[originalReminderEntries.length];
		    for (int i = 0; i < reminderEntries.length; i++) {
		    	reminderEntries[i] = originalReminderEntries[i];
		    }
		    int reminderIndex = Arrays.asList(reminderEntries).indexOf(as.getReminder());
		    if (reminderIndex < 0) {
		    	// No matching reminder hour value in entries. Just reset to "Don't remind me"...
		    	// Might be nicer to try to pick the closest value, but do we really want to write
		    	// the code to do that? No. No we don't.
		    	reminderIndex = 0;
		    }
		    reminderSpinner.setSelection(reminderIndex, false);
		    
		    /*
		     * Now that the course has been selected, and presumably the
		     * textbook adapter has been similarly updated, we must
		     * select the textbook.
		     */

		    final Assignment assignment = as;
            int textbookIndex = 0;
            if (assignment.getTextbook() == null) {
                /*
                 * Just select the first (dummy) textbook...
                 */
                textbookIndex = 0;
            } else if (selectedCourse == null) {
                /*
                 * Do nothing (no textbooks to choose from)
                 */
            } else {
                String[] textbooksArray = selectedCourse.getTextbooksArray();
                for (int i = 0; i < textbooksArray.length; i++) {
                    String t = textbooksArray[i];
                    if (assignment.getTextbook().equals(t)) {
                        textbookIndex = i;
                        break;
                    }
                }
            }
            
            textbooksList.clear();
            if (selectedCourse == null || selectedCourse.getId() == 0) {
                // do nothing specific. We already cleared out the list
            } else {
                // Add the course's textbooks to the list
                String[] textbooksArray = selectedCourse.getTextbooksArray();
                for (String textbook : textbooksArray) {
                    textbooksList.add(textbook);
                }
            }

            ((DummyItemAdapter)textbooks.getAdapter()).notifyDataSetChanged();
            textbooks.setSelection(textbookIndex + 1, false);
		    
		    ((EditText)findView(R.id.assignment_notes_field)).setText(as.getNotes());
		}
	}
}
