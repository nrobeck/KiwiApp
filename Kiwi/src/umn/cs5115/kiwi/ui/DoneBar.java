package umn.cs5115.kiwi.ui;


/**
 * Created by Mike on 10/13/13.
 */
public class DoneBar {
    /**
     * Interface for UI components that may wish to know when the user
     * clicks "Done" or "Cancel" in activities that use that feature,
     * so that they can handle that event as needed (e.g. the "edit course"
     * fragment knows it needs to save the course), and stop the default
     * action as necessary (e.g. the user input is invalid, so we don't want
     * the user to be able to click "Done").
     * 
     * @author Mike
     *
     */
    public static interface DoneBarListener {
        public boolean onDone();
        public void onCancel();
    }
    
    public static interface DoneBarListenable {
        public void addDoneBarListener(DoneBarListener listener);
    }
}
