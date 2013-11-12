package umn.cs5115.kiwi;

import umn.cs5115.kiwi.activity.KiwiDoneCancelActivity;
import umn.cs5115.kiwi.ui.DoneBar.CancelFromMenuHandler;
import umn.cs5115.kiwi.ui.DoneBar.DoneButtonHandler;

public class AddAssignmentActivity extends KiwiDoneCancelActivity {

	@Override
    protected DoneButtonHandler getDoneButtonHandler() {
	    return new DoneButtonHandler() {
            @Override
            public void onDone() {
                finish();
            }
        };
    }

    @Override
    protected CancelFromMenuHandler getCancelFromMenuHandler() {
        return new CancelFromMenuHandler() {
            @Override
            public void onCancel() {
                finish();
            }
        };
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_add_assignment;
    }

    @Override
    protected boolean hasCancelButton() {
        return false;
    }
}
