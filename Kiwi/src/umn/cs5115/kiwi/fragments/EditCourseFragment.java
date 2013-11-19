package umn.cs5115.kiwi.fragments;

import umn.cs5115.kiwi.R;
import umn.cs5115.kiwi.ui.DoneBar.DoneBarListenable;
import umn.cs5115.kiwi.ui.DoneBar.DoneBarListener;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

public class EditCourseFragment extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.edit_course_fragment, container);
	}

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        
        final Activity activity = getActivity();
        if (activity instanceof DoneBarListenable) {
            final DoneBarListenable listenable = (DoneBarListenable) getActivity();
            Log.i("EditCourseFragment", "Registering listener.");
            listenable.addDoneBarListener(new DoneBarListener() {
                @Override
                public boolean onDone() {
                    Log.i("EditCourseFragment", "DoneBarListener.onDone()");
                    //TODO: Validate input...
                    Toast.makeText(getActivity(), "Invalid input!", Toast.LENGTH_SHORT).show();
                    
                    
                    
                    return false;
                }
                
                @Override
                public void onCancel() {
                    Log.i("EditCourseFragment", "DoneBarListener.onCancel()");
                }
            });
        } else {
            // TODO: Decide if we actually want to be able to use this fragment
            // in a non-DoneCancel activity.
            Log.e("EditCourseFragment", "Parent activity does not implement DoneBarListenable, FYI.");
        }
    }
}
