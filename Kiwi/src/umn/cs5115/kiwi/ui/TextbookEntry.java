package umn.cs5115.kiwi.ui;

import umn.cs5115.kiwi.R;
import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

public class TextbookEntry extends LinearLayout {
    /*
     * Static final variables pointing to layouts and view IDs.
     */
    private static final int LAYOUT_ID = R.layout.textbook_entry;
    private static final int TEXT_FIELD_ID = R.id.textbook_title;
    private static final int REMOVE_ICON = R.id.remove_textbook_icon;
    
    private EditText mInputField;
    private ImageButton mRemoveIcon;
    
    public TextbookEntry(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView();
    }

    public TextbookEntry(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public TextbookEntry(Context context) {
        super(context);
        initView();
    }
    
    private void initView() {
        View.inflate(getContext(), LAYOUT_ID, this);
        
        mInputField = (EditText) findViewById(TEXT_FIELD_ID);
        mRemoveIcon = (ImageButton) findViewById(REMOVE_ICON);
        
        mRemoveIcon.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                 * This is probably utterly terrible to do.
                 */
                TextbookEntry entry = TextbookEntry.this;
                ViewGroup vg = (ViewGroup)entry.getParent();
                // Remove this TextbookEntry from its parent. Ew...
                vg.removeView(entry);
            }
        });
    }
    
    /**
     * Find out if the input field on this textbook entry is empty or not.
     * @return true if this textbook entry's input field is empty
     */
    public boolean isEmpty() {
        return TextUtils.isEmpty(mInputField.getText().toString());
    }
    
    /**
     * Get the text input that has been entered into this field.
     * @return the input that has been entered into this textbook entry.
     */
    public String getInput() {
        Editable input = mInputField.getText();
        // Just to be safe, check whether this is null.
        if (input != null) {
            return input.toString();
        } else {
            return null;
        }
    }
}
