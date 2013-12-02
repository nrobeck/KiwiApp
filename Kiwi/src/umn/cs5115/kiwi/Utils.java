package umn.cs5115.kiwi;

import umn.cs5115.kiwi.model.Assignment;
import umn.cs5115.kiwi.model.Course;
import umn.cs5115.kiwi.ui.DoneBar.DoneBarListener;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Mike on 10/13/13.
 */
public class Utils {
//    https://android.googlesource.com/platform/developers/samples/android/+/master/ui/actionbar/DoneBar/src/com/example/android/donebar/DoneBarActivity.java
    public static void makeActionBarDoneCancel(ActionBar bar, final DoneBarListener listener) {
        final Context context = bar.getThemedContext();
        if (context != null) {
            final LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View customActionBarView = inflater.inflate(R.layout.actionbar_custom_view_done_cancel, null);
            if (customActionBarView == null) {
                Log.d("Kiwi Test Utils", "Null custom actionbar view!");
                return;
            }

            customActionBarView.findViewById(R.id.actionbar_done).setClickable(true);
            if (listener != null) {
                customActionBarView.findViewById(R.id.actionbar_done)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        listener.onDone();
                    }
                });
                customActionBarView.findViewById(R.id.actionbar_cancel)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        listener.onCancel();
                    }
                });
            }

            bar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM,
                    ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_SHOW_TITLE);

            bar.setCustomView(customActionBarView,
                    new ActionBar.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT));
        }
    }

//    https://android.googlesource.com/platform/developers/samples/android/+/master/ui/actionbar/DoneBar/src/com/example/android/donebar/DoneBarActivity.java

    /**
     * Changes the ActionBar passed in to the custom DoneButton view.
     *
     * Note: If you use this method, you must make sure to override onCreateOptionsMenu and
     * inflate the cancel menu there, as well as overriding onOptionsItemSelected to handle
     * clicking the cancel menu item. If you do not do these things, you are leaving the user
     * with only the option to click "Done"
     *
     * @param bar ActionBar to modify
     * @param handler DoneButtonHandler to provide the onDone callback.
     */
    public static void makeActionBarDoneButton(ActionBar bar, final DoneBarListener listener) {
        final Context context = bar.getThemedContext();
        if (context != null) {
            final LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View customActionBarView = inflater.inflate(R.layout.actionbar_custom_view_done, null);
            if (customActionBarView == null) {
                Log.d("Kiwi Test Utils", "Null custom actionbar view!");
                return;
            }

            customActionBarView.findViewById(R.id.actionbar_done).setClickable(true);
            if (listener != null) {
                customActionBarView.findViewById(R.id.actionbar_done)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        listener.onDone();
                    }
                });
            }

            bar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM,
                    ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_SHOW_TITLE);

            bar.setCustomView(customActionBarView);
        }
    }

    // Exist to make getting around the app simpler, for us.

    public static Intent goToAddCourse(Context context) {
        return new Intent(context, ImportCoursesActivity.class);
    }

    public static void goToAddCourse(Activity activity) {
        activity.startActivity(new Intent(activity, EditCourseActivity.class));
    }

    public static Intent goToAddAssignment(Context context) {
        return new Intent(context, EditAssignmentActivity.class);
    }

    public static void goToAddAssignment(Activity activity) {
        activity.startActivity(new Intent(activity, EditAssignmentActivity.class));
    }
    
    public static void presentationDatabase(Context context) {
    	DatabaseHandler dbHandler = new DatabaseHandler(context);
    	
    	//Deleting all the information from the database to make the database the way we want it
    	Course[] courses = dbHandler.getCourses();
    	for(int i = 0; i < courses.length; i++) {
    		dbHandler.removeCourse(courses[i]);
    	}
    	
    	//Adding courses
    	dbHandler.addCourse(new Course(0, //ID
    								   "Intro to User Interface Design", //Course Title
    								   "CSCI 5115", //Course Designation
    								   "9:45", //Start Time
    								   "11:00", //End Time
    								   "MechE 212", //Location
    								   "", //Start Date 
    								   "", //End Date
    								   "", //Recurrence Rule 
    								   "", //Notes
    								   "Design of Everday Things\nDesign for Use", //Textbooks 
    								   "orange")); //Color
    	dbHandler.addCourse(new Course(0, //ID
									   "Intro to Data Mining", //Course Title
									   "CSCI 5523", //Course Designation
									   "16:00", //Start Time
									   "17:15", //End Time
									   "Keller 3-230", //Location
									   "", //Start Date 
									   "", //End Date
									   "", //Recurrence Rule 
									   "", //Notes
									   "Intro to Data Mining", //Textbooks 
									   "green")); //Color
    	dbHandler.addCourse(new Course(0, //ID
									   "Intro to Chemistry", //Course Title
									   "CHEM 2013", //Course Designation
									   "8:00", //Start Time
									   "9:00", //End Time
									   "Smith 130", //Location
									   "", //Start Date 
									   "", //End Date
									   "", //Recurrence Rule 
									   "", //Notes
									   "Intro to Chemistry", //Textbooks 
									   "blue")); //Color
    	dbHandler.addCourse(new Course(0, //ID
									   "Intro to Physics", //Course Title
									   "PHYS 2013", //Course Designation
									   "10:00", //Start Time
									   "11:00", //End Time
									   "Tate 170", //Location
									   "", //Start Date 
									   "", //End Date
									   "", //Recurrence Rule 
									   "", //Notes
									   "Intro to Physics", //Textbooks 
									   "red")); //Color
    	
    	//TODO: Need to put the assignments
    	//Adding assignments
    	dbHandler.addAssignment(new Assignment(0, //ID
    										   "Final Presentation", //Assignment Name
    										   1, //Course as an int
    										   "Presentation", //Assignment Type
    										   1386655140000l, //Due in Milliseconds
    										   72, //Reminder
    										   "Remember to submit and print out all the stuff for the presentation!", //Notes
    										   "")); //Textbook
    	dbHandler.addAssignment(new Assignment(0, //ID
											   "Evaluations", //Assignment Name
											   1, //Course as an int
											   "Assignment", //Assignment Type
											   1387000740000l, //Due in Milliseconds
											   72, //Reminder
											   "", //Notes
											   "")); //Textbook
    	dbHandler.addAssignment(new Assignment(0, //ID
											   "Homework #5", //Assignment Name
											   2, //Course as an int
											   "Assignment", //Assignment Type
											   1386741540000l, //Due in Milliseconds
											   24, //Reminder
											   "Remember to print it out!", //Notes
											   "Intro to Data Mining")); //Textbook
    	dbHandler.addAssignment(new Assignment(0, //ID
											   "Final", //Assignment Name
											   2, //Course as an int
											   "Exam", //Assignment Type
											   1387087140000l, //Due in Milliseconds
											   0, //Reminder
											   "", //Notes
											   "Intro to Data Mining")); //Textbook
    	dbHandler.addAssignment(new Assignment(0, //ID
											   "Experiment #6", //Assignment Name
											   3, //Course as an int
											   "Lab", //Assignment Type
											   1386827940000l, //Due in Milliseconds
											   0, //Reminder
											   "", //Notes
											   "")); //Textbook
    	dbHandler.addAssignment(new Assignment(0, //ID
											   "Lab Report #6", //Assignment Name
											   3, //Course as an int
											   "Paper", //Assignment Type
											   1387173540000l, //Due in Milliseconds
											   12, //Reminder
											   "", //Notes
											   "Intro to Chemistry")); //Textbook
    	dbHandler.addAssignment(new Assignment(0, //ID
											   "WebAssign #10", //Assignment Name
											   4, //Course as an int
											   "Assignment", //Assignment Type
											   1386914340000l, //Due in Milliseconds
											   0, //Reminder
											   "", //Notes
											   "Intro to Physics")); //Textbook
    	dbHandler.addAssignment(new Assignment(0, //ID
											   "Final", //Assignment Name
											   4, //Course as an int
											   "Exam", //Assignment Type
											   1387259940000l, //Due in Milliseconds
											   12, //Reminder
											   "STUDY!!!", //Notes
											   "Intro to Physics")); //Textbook
    }
}
