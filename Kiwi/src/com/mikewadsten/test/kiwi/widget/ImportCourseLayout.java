package com.mikewadsten.test.kiwi.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Checkable;
import android.widget.LinearLayout;

public class ImportCourseLayout extends LinearLayout implements Checkable {
    public ImportCourseLayout(Context context) {
        super(context);
    }

    public ImportCourseLayout(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
    }

    public ImportCourseLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    private boolean checked;

    @Override
    public void setChecked(boolean checked) {
        if (this.checked != checked) {
            this.checked = checked;

            for (int i = 0; i < getChildCount(); i++) {
                View child = getChildAt(i);
                if (child instanceof Checkable) {
                    ((Checkable) child).setChecked(checked);
                }
            }
        }
    }

    @Override
    public boolean isChecked() {
        return checked;
    }

    @Override
    public void toggle() {
        setChecked(!checked);
    }

    @Override
    protected int[] onCreateDrawableState(int extraSpace) {
        final int[] drawableState = super.onCreateDrawableState(extraSpace + 1);
        if (isChecked()) {
            mergeDrawableStates(drawableState, new int[]{android.R.attr.state_checked});
        }
        return drawableState;
    }
}
