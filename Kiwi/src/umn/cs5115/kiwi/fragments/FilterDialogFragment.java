package umn.cs5115.kiwi.fragments;

import java.util.ArrayList;

import umn.cs5115.kiwi.DatabaseHandler;
import umn.cs5115.kiwi.model.Course;
import umn.cs5115.kiwi.model.FilterDefinition;
import umn.cs5115.kiwi.model.FilterDefinition.SortBy;
import umn.cs5115.kiwi.R;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class FilterDialogFragment extends DialogFragment {
    public static interface FilterListener {
        public void onNewFilter(FilterDefinition newfilter);
    }

    public FilterDialogFragment() {}


    public static FilterDialogFragment newInstance(FilterDefinition definition) {
        FilterDialogFragment f = new FilterDialogFragment();

        Bundle args = new Bundle();
        args.putParcelable("definition", definition);
        f.setArguments(args);

        return f;
    }

    private int getSortNumber(View rootView, RadioGroup radios) {
        return Integer.parseInt((String) ((RadioButton)rootView.findViewById(radios.getCheckedRadioButtonId())).getTag());
    }

    private boolean getShowCompleted(View rootView) {
        return ((CheckBox)rootView.findViewById(R.id.show_completed)).isChecked();
    }
    
    private void makeCheckboxWide(CheckBox cb) {
        cb.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();

        final View rootView = inflater.inflate(R.layout.filter_dialog_layout, null);

        FilterDefinition defn = (FilterDefinition)getArguments().getParcelable("definition");
        if (defn == null) {
            defn = new FilterDefinition(SortBy.DUE_DATE, true, null, null);
        }

        Context ctx = getActivity();

        Course[] courses = new DatabaseHandler(ctx).getCourses();
        String[] types = getResources().getStringArray(R.array.assignment_types);

        // Make sure these arrays are things we can iterate over.
        if (defn.courses == null) {
            /*
             * courses was null, so we should default to selecting everything.
             */
            defn.courses = new int[courses.length + 1];
            defn.courses[0] = 0;
            for (int i = 0; i < courses.length; i++) {
                defn.courses[i + 1] = courses[i].getId();
            }
        }
        if (defn.types == null) {
            /*
             * types was null, so we should default to selecting everything.
             */
            defn.types = new String[types.length + 1];
            defn.types[0] = null;
            for (int i = 0; i < types.length; i++) {
                defn.types[i + 1] = types[i];
            }
        }

        //		TextView tv = (TextView)rootView.findViewById(R.id.textView1);
        //		tv.setText(String.format("Hello! %d", getArguments().getInt("whatever")));

        LinearLayout coursesLayout = (LinearLayout) rootView.findViewById(R.id.courses_linearlayout);
        LinearLayout typesLayout = (LinearLayout) rootView.findViewById(R.id.assignment_types_linearlayout);

        final RadioGroup sortRadios = (RadioGroup)rootView.findViewById(R.id.radioGroup1);

        // Select the correct radio button.
        RadioButton initialSortRadio;
        initialSortRadio = (RadioButton)sortRadios.findViewWithTag(Integer.toString(defn.sorter.toInt()));
        if (initialSortRadio == null) {
            initialSortRadio = (RadioButton)sortRadios.findViewById(R.id.sort_by_due);
        }
        initialSortRadio.setChecked(true);

        // Check off the 'show completed' box if needed
        ((CheckBox)rootView.findViewById(R.id.show_completed)).setChecked(defn.showCompleted);

        final CheckBox[] courseCheckboxes = new CheckBox[courses.length + 1];
        final CheckBox[] typeCheckboxes = new CheckBox[types.length + 1];

        CheckBox noCourseCb = new CheckBox(ctx);
        CheckBox noTypeCb = new CheckBox(ctx);

        /*
         * Set up all the necessary filter-by-course checkboxes.
         */
        // Add a checkbox to add no-course assignments to the filter.
        noCourseCb.setText("(None selected)");
        noCourseCb.setTag(0);
        makeCheckboxWide(noCourseCb);
        coursesLayout.addView(noCourseCb);
        courseCheckboxes[0] = noCourseCb;
        for (int id : defn.courses) {
            if (id == 0) {
                noCourseCb.setChecked(true);
                break;
            }
        }
        for (int i = 0; i < courses.length; i++) {
            // Add a checkbox for each of the courses in the DB.
            Course course = courses[i];
            CheckBox cb = new CheckBox(ctx);
            cb.setText(course.getCourseDesignation());
            // Keep track of the course id
            cb.setTag(course.getId());
            makeCheckboxWide(cb);
            coursesLayout.addView(cb);

            courseCheckboxes[i + 1] = cb;

            for (int id : defn.courses) {
                if (id == course.getId()) {
                    cb.setChecked(true);
                    break;
                }
            }
        }
        
        /*
         * Set up all the necessary filter-by-type checkboxes.
         */
        // Add a checkbox to add no-type courses to the filter.
        noTypeCb.setText("(No type selected)");
        noTypeCb.setTag(null);
        makeCheckboxWide(noTypeCb);
        typesLayout.addView(noTypeCb);
        typeCheckboxes[0] = noTypeCb;
        for (String t : defn.types) {
            if (t == null) {
                noTypeCb.setChecked(true);
                break;
            }
        }
        for (int i = 0; i < types.length; i++) {
            // Add a checkbox for each type.
            String typeName = types[i];
            CheckBox cb = new CheckBox(ctx);
            cb.setText(typeName);
            // Keep track of the type name
            cb.setTag(typeName);
            makeCheckboxWide(cb);

            typesLayout.addView(cb);

            typeCheckboxes[i + 1] = cb;

            for (String type : defn.types) {
                if (typeName.equals(type)) {
                    cb.setChecked(true);
                    break;
                }
            }
        }

        final FilterListener listener = (FilterListener)getActivity();

        return new AlertDialog.Builder(getActivity())
        .setView(rootView)
        .setTitle("Filter and sort assignments")
        .setPositiveButton(android.R.string.ok, new OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                int courseCount = 0;
                for (CheckBox cb : courseCheckboxes) {
                    if (cb != null && cb.isChecked()) {
                        courseCount++;
                    }
                }
                // Turn the selectedCourses list into the int[] for FilterDefinition
                final int[] courseIds = new int[courseCount];
                int index = 0;
                for (CheckBox cb : courseCheckboxes) {
                    if (index >= courseCount) {
                        // Shouldn't happen, unless you managed to check off a box between these two loops...
                        // But if this does happen, then break out of the loop.
                        break;
                    }
                    if (cb != null && cb.isChecked()) {
                        courseIds[index] = (Integer)cb.getTag();
                        index++;
                    }
                }

                ArrayList<String> selectedTypes = new ArrayList<String>();
                for (CheckBox cb : typeCheckboxes) {
                    if (cb.isChecked()) {
                        selectedTypes.add((String) cb.getTag());
                    }
                }
                final String[] types = new String[selectedTypes.size()];
                for (int i = 0; i < types.length; i++) {
                    types[i] = selectedTypes.get(i);
                }

                SortBy sorter = SortBy.fromInt(getSortNumber(rootView, sortRadios));

                boolean showCompleted = getShowCompleted(rootView);

                FilterDefinition defn = new FilterDefinition(sorter, showCompleted, courseIds, types);

                listener.onNewFilter(defn);
            }
        })
        .setNegativeButton(android.R.string.cancel, new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        }).create();
    }
}
