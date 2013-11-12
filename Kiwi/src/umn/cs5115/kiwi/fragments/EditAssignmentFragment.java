package umn.cs5115.kiwi.fragments;

import umn.cs5115.kiwi.R;
import umn.cs5115.kiwi.assignment.AssignmentUtils;
import android.app.Fragment;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;

public class EditAssignmentFragment extends Fragment {
    private EditText assignmentName;
    
    private View findView(int id) {
        return getView().findViewById(id);
    }
    
    private void loadLocals(View layout) {
        try {
            assignmentName = (EditText) layout.findViewById(R.id.editText1);
        } catch (Exception e) {
            Log.e("EditAssignmentFragment", "loadLocals failed", e);
        }
    }
    
    /**
     * Go over the inputs in the layout, calling setError where necessary to
     * inform the user what they need to do.
     */
    @SuppressWarnings("unused")
    private void setErrorsAsNeeded() {
        if (TextUtils.isEmpty(assignmentName.getText().toString())) {
            assignmentName.setError("Cannot be empty!");
        }
    }

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View layout = inflater.inflate(R.layout.edit_assignment_fragment, container);
		
		loadLocals(layout);
		
		return layout;
	}

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        
        findView(R.id.spinner3).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                AssignmentUtils.showTimeEditDialog(getFragmentManager(), null, null);
            }
        });
    }
}
