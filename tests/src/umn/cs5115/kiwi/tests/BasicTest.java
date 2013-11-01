package umn.cs5115.kiwi.tests;

import static com.google.android.apps.common.testing.ui.espresso.Espresso.onView;
import static com.google.android.apps.common.testing.ui.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static com.google.android.apps.common.testing.ui.espresso.action.ViewActions.click;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.withId;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.withText;
import umn.cs5115.kiwi.MainActivity;
import android.test.ActivityInstrumentationTestCase2;

import umn.cs5115.kiwi.R;

/**
 * Created by Mike on 10/18/13.
 */
public class BasicTest extends ActivityInstrumentationTestCase2<MainActivity> {
    @SuppressWarnings("deprecation")
    public BasicTest() {
        super("umn.cs5115.kiwi", MainActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        // Espresso will not launch activity; we must do so
        getActivity();
    }

    // These tests are in here simply to test Espresso interactions...
    public void testBlah() {
        onView(withId(R.id.actionbar_done)).perform(click());
    }
    
    public void testClickCancel() {
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        
        onView(withText("Cancel")).perform(click());
    }
}
