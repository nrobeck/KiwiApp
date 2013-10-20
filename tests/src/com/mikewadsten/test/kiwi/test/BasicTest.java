package com.mikewadsten.test.kiwi.test;

import android.provider.OpenableColumns;
import android.test.ActivityInstrumentationTestCase2;

import com.google.android.apps.common.testing.ui.espresso.Espresso;
import com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers;
import com.mikewadsten.test.kiwi.MainActivity;
import com.mikewadsten.test.kiwi.R;

import static com.google.android.apps.common.testing.ui.espresso.Espresso.onView;
import static com.google.android.apps.common.testing.ui.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static com.google.android.apps.common.testing.ui.espresso.action.ViewActions.click;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.withId;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.withText;

/**
 * Created by Mike on 10/18/13.
 */
public class BasicTest extends ActivityInstrumentationTestCase2<MainActivity> {
    @SuppressWarnings("deprecation")
    public BasicTest() {
        super("com.mikewadsten.test.kiwi", MainActivity.class);
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
