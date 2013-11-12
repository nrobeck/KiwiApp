package umn.cs5115.kiwi.activity;

import java.util.ArrayList;
import java.util.List;

import umn.cs5115.kiwi.R;
import umn.cs5115.kiwi.Utils;
import umn.cs5115.kiwi.ui.DoneBar.CancelFromMenuHandler;
import umn.cs5115.kiwi.ui.DoneBar.DoneBarListenable;
import umn.cs5115.kiwi.ui.DoneBar.DoneBarListener;
import umn.cs5115.kiwi.ui.DoneBar.DoneButtonHandler;
import umn.cs5115.kiwi.ui.DoneBar.DoneCancelBarHandler;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.LayoutParams;

/**
 * A version of KiwiActivity which automatically sets up the Done and Cancel
 * buttons in the action bar (or just Done with Cancel in the menu, if you
 * override hasCancelButton to return false) and helps handle interactions
 * with those elements.
 * 
 * @author Mike Wadsten
 *
 */
public abstract class KiwiDoneCancelActivity extends KiwiActivity implements DoneBarListenable {
    private List<DoneBarListener> doneListeners;
    private CancelFromMenuHandler cancelFromMenu;
    private boolean contentViewSet;
    
    /**
     * Get the {@link DoneCancelBarHandler} that will be used when interacting
     * with the action bar items for Done and Cancel. Returns null by default.
     * 
     * <p>This method need only be overridden if you are not
     * also overriding {@link #hasCancelButton()} to return false -- that is,
     * you only need to provide a DoneCancelBarHandler if you are using the
     * Done-Cancel action bar functionality.</p>
     * @return an instance of the {@link DoneCancelBarHandler} interface
     */
    protected DoneCancelBarHandler getDoneCancelHandler() {
        return null;
    }
    
    /**
     * Get the {@link DoneButtonHandler} that will be used when interacting with
     * the single Done button in the action bar. Returns null by default.
     * 
     * <p>This method need only be overridden if you are also overriding
     * {@link #hasCancelButton()} to return false -- that is, you only need to
     * provide a DoneButtonHandler if you are making this activity hide Cancel
     * in the menu (in which case you must also provide a CancelFromMenuHandler).
     * 
     * @return an instance of the {@link DoneButtonHandler} interface
     */
    protected DoneButtonHandler getDoneButtonHandler() {
        return null;
    }
    
    protected CancelFromMenuHandler getCancelFromMenuHandler() {
        return null;
    }
    
    /**
     * Get the layout resource to be used in the activity. The return value
     * will be passed as an argument to {@link #setContentView(int)} in the
     * {@link android.app.Activity#onCreate(Bundle)} method.
     * 
     * @return the layout resource ID to be used
     */
    protected abstract int getLayoutResource();
    
    protected void onCreated(Bundle savedInstanceState) {}
    
    protected boolean hasCancelButton() {
        return true;
    }
    
    /*
     * Final private methods.
     */

    // Override setContentView - I don't think Android lets you call it twice
    // in the lifetime of an activity, and we call it for you already, so let's
    // just forbid ourselves from getting into a pickle.
    
    @Override
    final public void setContentView(int layoutResID) {
        if (contentViewSet) {
            throw new IllegalStateException("Cannot call setContentView - the overridden onCreate does that for you.");
        }
        contentViewSet = true;
        super.setContentView(layoutResID);
    }
    
    @Override
    public void setContentView(View view, LayoutParams params) {
        if (contentViewSet) {
            throw new IllegalStateException("Cannot call setContentView - the overridden onCreate does that for you.");
        }
        contentViewSet = true;
        super.setContentView(view, params);
    }

    @Override
    public void setContentView(View view) {
        if (contentViewSet) {
            throw new IllegalStateException("Cannot call setContentView - the overridden onCreate does that for you.");
        }
        contentViewSet = true;
        super.setContentView(view);
    }

    final private boolean usesCancelMenu() {
        return !hasCancelButton();
    }

    final private DoneCancelBarHandler getWrappedHandler(final DoneCancelBarHandler handler) {
        return new DoneCancelBarHandler() {
            @Override
            public void onDone() {
                boolean allGood = true;
                
                for (DoneBarListener listener : doneListeners) {
                    // Call onDone in each listener, and keep track of the
                    // overall return value.
                    allGood = listener.onDone() && allGood;
                }
                
                if (allGood && handler != null) {
                    // No listener returned false - we're clear to continue
                    handler.onDone();
                } else {
                    // At least one listener returned false. Abort!
                    Log.d("KiwiDoneCancelActivity", "A listener returned false. Not calling handler.onDone");
                    return;
                }
            }
            
            @Override
            public void onCancel() {
                // The onCancel method has no way of communicating (and has no
                // need to communicate) that the Cancel action needs to be
                // aborted, so just chug through the listeners and then call
                // handler.onCancel
                for (DoneBarListener listener : doneListeners) {
                    listener.onCancel();
                }
                handler.onCancel();
            }
        };
    }
    
    final private DoneButtonHandler getWrappedHandler(final DoneButtonHandler handler) {
        return new DoneButtonHandler() {
            public void onDone() {
                boolean thisGood, allGood = true;
                
                for (DoneBarListener listener : doneListeners) {
                    // Call onDone in each listener, and keep track of the
                    // overall return value.
                    thisGood = listener.onDone();
                    // TODO: Add some sort of logging? (Would have to add
                    // a method to the interface like 'getDescription' so we
                    // can log what listener returned false)
                    allGood = thisGood && allGood;
                }
                
                if (allGood && handler != null) {
                    // No listener returned false - we're clear to continue
                    handler.onDone();
                } else {
                    // At least one listener returned false. Abort!
                    Log.d("KiwiDoneCancelActivity", "A listener returned false. Not calling handler.onDone");
                    return;
                }
            }
        };
    }
    
    final private CancelFromMenuHandler getWrappedHandler(final CancelFromMenuHandler handler) {
        return new CancelFromMenuHandler() {
            @Override
            public void onCancel() {
                // The onCancel method has no way of communicating (and has no
                // need to communicate) that the Cancel action needs to be
                // aborted, so just chug through the listeners and then call
                // handler.onCancel
                for (DoneBarListener listener : doneListeners) {
                    listener.onCancel();
                }
                
                if (handler == null) {
                    Log.i("KiwiDoneCancelActivity", "Clicked 'Cancel' but the handler is null");
                } else {
                    handler.onCancel();
                }
            }
        };
    }
    
    /**
     * Register an instance of {@link DoneBarListener} with the activity, so
     * as to be notified when the user clicks on either the Done or Cancel
     * button. The main use case for DoneBarListener is to halt further
     * processing when the user clicks "Done" if there is an error they must
     * sort out first (like invalid input).
     * 
     * <p>
     * Keep in mind that the listeners you register here will not survive any
     * destructive activity lifecycle changes (like rotating the screen, for
     * instance), so you will want to ensure that if, for instance, you are
     * registering a DoneBarListener from a fragment, that you do so from the
     * {@link android.app.Fragment#onActivityCreated(Bundle) onActivityCreated(Bundle)}
     * method.
     * </p>
     * 
     * @param listener the {@link DoneBarListener} instance to register
     * @throws NullPointerException if <code>listener</code> is null
     */
    final public void addDoneBarListener(final DoneBarListener listener) {
        if (listener == null) {
            throw new NullPointerException("Why would you register null as a listener?");
        }
        doneListeners.add(listener);
    }
    
    /*
     * Activity class methods that we modify to be final so that they cannot
     * be overridden in sub-classes.
     */
    
    @Override
    final protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(getLayoutResource());
        
        if (hasCancelButton()) {
            Utils.makeActionBarDoneCancel(getActionBar(), getWrappedHandler(getDoneCancelHandler()));
        } else {
            Utils.makeActionBarDoneButton(getActionBar(), getWrappedHandler(getDoneButtonHandler()));
        }
        
        doneListeners = new ArrayList<DoneBarListener>();
        cancelFromMenu = getWrappedHandler(getCancelFromMenuHandler());
        
        this.onCreated(savedInstanceState);
    }

    @Override
    final public boolean onCreateOptionsMenu(Menu menu) {
        if (!hasCancelButton()) {
            // Need to inflate the menu with "Cancel" in it
            getMenuInflater().inflate(R.menu.cancel, menu);
        }
        return true;
    }

    @Override
    final public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        
        if (usesCancelMenu() && id == R.id.cancel) {
            // User clicked the Cancel menu button. Trigger the handler.
            cancelFromMenu.onCancel();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}