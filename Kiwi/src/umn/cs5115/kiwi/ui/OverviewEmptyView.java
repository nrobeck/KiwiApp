package umn.cs5115.kiwi.ui;

import umn.cs5115.kiwi.R;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class OverviewEmptyView extends LinearLayout {
//    private static final String TAG = OverviewEmptyView.class.getCanonicalName();
    private static final int EMPTY_LAYOUT_RES_ID = R.layout.overview_empty_layout,
                                EMPTY_ICON_VIEW = R.id.overview_empty_icon,
                                EMPTY_TEXT_VIEW = R.id.overview_empty_text,
                                BUTTON_ADD_COURSES = R.id.overview_empty_add_courses,
                                BUTTON_ADD_ASSIGN = R.id.overview_empty_add_assignment,
                                BUTTON_FILTER = R.id.overview_empty_filter;
    
    /**
     * Interface to deal with user interactions with this empty view.
     * @author Mike
     *
     */
    public static interface CustomEmptyViewButtonListener {
        /**
         * Callback executed when the user clicks on the button to
         * add courses.
         * @param buttonView the View of that button (so that you might do
         * something like bring up a popup menu on that button?)
         */
        public void onClickAddCourses(final View buttonView);
        
        /**
         * Callback executed when the user clicks on the button to add
         * an assignment.
         */
        public void onClickAddAssignment();
        
        /**
         * Callback executed when the user clicks on the button to bring
         * up the filtering dialog.
         */
        public void onClickFilter();
    }
    
    private ImageView mEmptyIcon;
    private TextView mEmptyTextView;
    private Button mButtonAddCourses;
    private Button mButtonAddAssignment;
    private Button mButtonFilter;
    private CustomEmptyViewButtonListener mListener;
    
    private Drawable mEmptyDrawable;
    private CharSequence mEmptyText;
    private CharSequence mAddCourseText;
    private CharSequence mAddAssignmentText;
    private CharSequence mFilterText;
 
    /**
     * Constructor.
     * @param context a Context to use
     * @param attrs set of attributes (see XML attributes for this view)
     * @param defStyle ???
     */
    public OverviewEmptyView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView(getAttrs(attrs));
    }

    /**
     * Constructor.
     * @param context a Context to use
     * @param attrs set of attributes (see XML attributes for this view)
     */
    public OverviewEmptyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(getAttrs(attrs));
    }

    /**
     * Constructor.
     * 
     * <p>If you are using this constructor, you'll need to set the
     * text values and icon yourself. Just an FYI.</p>
     * @param context a context to use
     */
    public OverviewEmptyView(Context context) {
        super(context);
        initView(null);
    }
    
    /**
     * Set an {@link CustomEmptyViewButtonListener} listener object for this view,
     * so as to provide callbacks for when the user interacts with this view.
     * @param listener the {@link CustomEmptyViewButtonListener} to attach to this view
     */
    public void setOnButtonClickListener(CustomEmptyViewButtonListener listener) {
        mListener = listener;
    }
    
    /**
     * Inflate the layout, get references to the necessary views and save
     * them in their local variables, bind click listeners to those views,
     * and use attributes that may have been passed in from the layout XML
     * to set up the views (e.g. set the icon, empty text, etc.).
     * 
     * @param attrs a TypedArray with this view's attributes
     */
    private void initView(TypedArray attrs) {
        View.inflate(getContext(), EMPTY_LAYOUT_RES_ID, this);

        mEmptyIcon = (ImageView) findViewById(EMPTY_ICON_VIEW);
        mEmptyTextView = (TextView) findViewById(EMPTY_TEXT_VIEW);
        mButtonAddCourses = (Button) findViewById(BUTTON_ADD_COURSES);
        mButtonAddAssignment = (Button) findViewById(BUTTON_ADD_ASSIGN);
        mButtonFilter = (Button) findViewById(BUTTON_FILTER);
        
        /*
         * Check that we got all those views.
         */
        if (mEmptyIcon == null) {
            throw new NullPointerException("Could not find the empty icon view. Did you change the layout?");
        }
        if (mEmptyTextView == null) {
            throw new NullPointerException("Could not find the empty text view. Did you change the layout?");
        }
        if (mButtonAddCourses == null) {
            throw new NullPointerException("Could not find the Add course(s) button. Did you change the layout?");
        }
        if (mButtonAddAssignment == null) {
            throw new NullPointerException("Could not find the Add assignment button. Did you change the layout?");
        }
        if (mButtonFilter == null) {
            throw new NullPointerException("Could not find the Filter button. Did you change the layout?");
        }
        
        /*
         * Set an OnClickListener on the Add course(s) button.
         */
        mButtonAddCourses.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onClickAddCourses(v);
                }
            }
        });
        
        /*
         * Set an OnClickListener on the Add assignment button.
         */
        mButtonAddAssignment.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onClickAddAssignment();
                }
            }
        });
        
        /*
         * Set an OnClickListener on the Filter button.
         */
        mButtonFilter.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onClickFilter();
                }
            }
        });
        
        /*
         * If no attributes were set, we should return now.
         */
        if (attrs == null) {
            return;
        }
        
        /* ***********************************
         * Use the attributes TypedArray to set stuff up.
         */
        
        mEmptyDrawable = attrs.getDrawable(R.styleable.OverviewEmptyView_icon);
        mEmptyText = attrs.getString(R.styleable.OverviewEmptyView_text);
        mAddCourseText = attrs.getString(R.styleable.OverviewEmptyView_courseButtonText);
        mAddAssignmentText = attrs.getString(R.styleable.OverviewEmptyView_assignmentButtonText);
        mFilterText = attrs.getString(R.styleable.OverviewEmptyView_filterButtonText);
        attrs.recycle();

        updateIcon(false);
        updateEmptyText(false);
        updateCourseButton(false);
        updateAssignmentButton(false);
        updateFilterButton(false);
        relayout();
    }
    
    private TypedArray getAttrs(AttributeSet attrs) {
        return getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.OverviewEmptyView, 0, 0);
    }

    
    /* ************************************************************************
     * Public methods to update resources, text, etc. on this empty view.
     */
    
    /**
     * Change the image displayed in this empty view.
     * @param iconDrawable a Drawable to set as the image on this empty view
     */
    public void setImage(Drawable imageDrawable) {
        mEmptyDrawable = imageDrawable;
        updateIcon(true);
    }
    
    /**
     * Change the image displayed in this empty view.
     * @param drawableResId a resource ID for the Drawable to set as the image
     * on this empty view.
     */
    public void setImage(int drawableResId) {
        setImage(getContext().getResources().getDrawable(drawableResId));
    }
    
    /**
     * Change the text displayed in this empty view.
     * @param text the text to display
     */
    public void setText(CharSequence text) {
        mEmptyText = text;
        updateEmptyText(true);
    }
    
    /**
     * Change the text displayed in this empty view.
     * @param resId the resource ID for the text to display
     */
    public void setText(int resId) {
        setText(getContext().getResources().getText(resId));
    }
    
    /**
     * Change the text on the Add courses button.
     * @param text the text to display on that button
     */
    public void setAddCourseText(CharSequence text) {
        mAddCourseText = text;
        updateCourseButton(true);
    }
    
    /**
     * Change the text on the Add courses button.
     * @param text the resource ID for the text to display on that button
     */
    public void setAddCourseText(int resId) {
        setAddCourseText(getContext().getResources().getText(resId));
    }
    
    /**
     * Change the text on the Add assignment button.
     * @param text the text to display on that button
     */
    public void setAddAssignmentText(CharSequence text) {
        mAddAssignmentText = text;
        updateAssignmentButton(true);
    }
    
    /**
     * Change the text on the Add assignment button.
     * @param text the resource ID for the text to display on that button
     */
    public void setAddAssignmentText(int resId) {
        setAddAssignmentText(getContext().getResources().getText(resId));
    }
    
    public void setFilterButtonShown(boolean shown) {
        mButtonFilter.setVisibility(shown ? View.VISIBLE : View.GONE);
    }
    
    /* ************************************************************************
     * Private methods to update views.
     */
    
    /**
     * Updates the empty icon.
     * @param relayout if true, the view is invalidated and redrawn automatically
     */
    @SuppressWarnings("deprecation")
    private void updateIcon(boolean relayout) {
        /*
         * setBackgroundDrawable was deprecated in API 16, but we support
         * APIs 15 and up. We suppress the deprecation warning because
         * nobody likes deprecation warnings.
         */
        mEmptyIcon.setBackgroundDrawable(mEmptyDrawable);
        if (relayout) relayout();
    }
    
    /**
     * Updates the text on the empty text view.
     * @param relayout if true, the view is invalidated and redrawn automatically
     */
    private void updateEmptyText(boolean relayout) {
        mEmptyTextView.setText(mEmptyText);
        if (relayout) relayout();
    }

    /**
     * Updates the text on the add-courses button.
     * @param relayout if true, the view is invalidated and redrawn automatically
     */
    private void updateCourseButton(boolean relayout) {
        mButtonAddCourses.setText(mAddCourseText);
        if (relayout) relayout();
    }
    
    /**
     * Updates the text on the add assignment button.
     * @param relayout if true, the view is invalidated and redrawn automatically
     */
    private void updateAssignmentButton(boolean relayout) {
        mButtonAddAssignment.setText(mAddAssignmentText);
        if (relayout) relayout();
    }
    
    /**
     * Updates the text on the filter button.
     * @param relayout if true, the view is invalidated and redrawn automatically
     */
    private void updateFilterButton(boolean relayout) {
        mButtonFilter.setText(mFilterText);
        if (relayout) relayout();
    }
    
    /**
     * Update the view.
     */
    private void relayout() {
        invalidate();
        requestLayout();
    }
}
