package umn.cs5115.kiwi.fragments;

import umn.cs5115.kiwi.R;
import umn.cs5115.kiwi.assignment.AssignmentUtils;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

public class EditAssignmentFragment extends Fragment {
    private View findView(int id) {
        return getView().findViewById(id);
    }

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.edit_assignment_fragment, container);
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
