package umn.cs5115.kiwi.ui;

import android.content.Context;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.WindowManager;
import android.widget.FrameLayout;

/**
 * Slightly-customized FrameLayout subclass which enables animations of
 * X-position percentages.
 * 
 * @see <a href="http://stackoverflow.com/a/11015834">Stack Overflow</a>
 * @author Mike
 */
public class SlidingFrameLayout extends FrameLayout {
    public SlidingFrameLayout(Context context) {
        super(context);
    }

    public SlidingFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    
    private int getWindowWidth() {
        WindowManager manager = (WindowManager)getContext().getSystemService(Context.WINDOW_SERVICE);
        Point size = new Point();
        manager.getDefaultDisplay().getSize(size);
        return size.x;
    }

    public float getXFraction() {
        int width = getWindowWidth();
        return (width == 0) ? 0 : getX() / (float) width;
    }
    
    public void setXFraction(float xFraction) {
        int width = getWindowWidth();
        setX((width > 0) ? (xFraction * width) : 0);
    }
}
