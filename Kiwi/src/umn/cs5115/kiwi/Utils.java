package umn.cs5115.kiwi;

import umn.cs5115.kiwi.DoneBar.*;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mikewadsten.test.kiwi.R;

/**
 * Created by Mike on 10/13/13.
 */
public class Utils {
//    https://android.googlesource.com/platform/developers/samples/android/+/master/ui/actionbar/DoneBar/src/com/example/android/donebar/DoneBarActivity.java
    public static void makeActionBarDoneCancel(ActionBar bar, final DoneCancelBarHandler handler) {
        final Context context = bar.getThemedContext();
        if (context != null) {
            final LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View customActionBarView = inflater.inflate(R.layout.actionbar_custom_view_done_cancel, null);
            if (customActionBarView == null) {
                Log.d("Kiwi Test Utils", "Null custom actionbar view!");
                return;
            }
            customActionBarView.findViewById(R.id.actionbar_done).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    handler.onDone(view);
                }
            });
            customActionBarView.findViewById(R.id.actionbar_cancel).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    handler.onCancel(view);
                }
            });

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
    public static void makeActionBarDoneButton(ActionBar bar, final DoneButtonHandler handler) {
        final Context context = bar.getThemedContext();
        if (context != null) {
            final LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View customActionBarView = inflater.inflate(R.layout.actionbar_custom_view_done, null);
            if (customActionBarView == null) {
                Log.d("Kiwi Test Utils", "Null custom actionbar view!");
                return;
            }
            customActionBarView.findViewById(R.id.actionbar_done).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    handler.onDone(view);
                }
            });

            bar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM,
                    ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_SHOW_TITLE);

            bar.setCustomView(customActionBarView);
        }
    }
    
    // Exist to make getting around the app simpler, for us.
    
    public static Intent goToAddCourse(Context context) {
    	return new Intent(context, AddCourseActivity.class);
    }
    
    public static void goToAddCourse(Activity activity) {
    	activity.startActivity(new Intent(activity, AddCourseActivity.class));
    }
    
    public static Intent goToAddAssignment(Context context) {
    	return new Intent(context, AddAssignmentActivity.class);
    }
    
    public static void goToAddAssignment(Activity activity) {
    	activity.startActivity(new Intent(activity, AddAssignmentActivity.class));
    }
}
