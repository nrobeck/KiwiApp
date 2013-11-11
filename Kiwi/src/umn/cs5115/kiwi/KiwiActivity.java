package umn.cs5115.kiwi;

import android.app.Activity;
import android.content.Intent;

/**
 * Subclass of {@link Activity} which overrides
 * {@link Activity#finish()} and {@link Activity#startActivity(Intent)}
 * in order to override the transitions to be what we want them to be.
 * 
 * @author Mike Wadsten
 */
public class KiwiActivity extends Activity {
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.hold, R.anim.slide_out_to_right);
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        overridePendingTransition(R.anim.slide_in_from_right, R.anim.hold);
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
