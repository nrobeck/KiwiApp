package umn.cs5115.kiwi.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import umn.cs5115.kiwi.R;

public class EditCourseFragment extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Toast.makeText(getActivity(), "Edit course fragment!", Toast.LENGTH_SHORT).show();
		return inflater.inflate(R.layout.edit_course_fragment, container);
	}

}
