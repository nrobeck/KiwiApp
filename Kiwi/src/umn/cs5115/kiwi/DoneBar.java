package umn.cs5115.kiwi;

import android.view.View;

/**
 * Created by Mike on 10/13/13.
 */
public class DoneBar {
    public static interface DoneCancelBarHandler {
        public void onDone(View view);
        public void onCancel(View view);
    }

    public static interface DoneButtonHandler {
        public void onDone(View view);
    }
}
