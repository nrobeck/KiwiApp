package umn.cs5115.kiwi.fragments;

import java.util.ArrayList;
import java.util.List;

import com.example.android.expandingcells.CustomArrayAdapter;
import com.example.android.expandingcells.ExpandableListItem;
import com.example.android.expandingcells.ExpandingListView;

import umn.cs5115.kiwi.R;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class OverviewFragment extends Fragment {
    private final int CELL_DEFAULT_HEIGHT = 250;
    private final int NUM_OF_CELLS = 30;
    
    private ExpandingListView mListView;
    
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.overview_fragment, null);
        mListView = (ExpandingListView) view.findViewById(R.id.main_list_view);
        
		return view;
	}

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        
        ExpandableListItem[] values = new ExpandableListItem[] {
                new ExpandableListItem("Chameleon", R.drawable.chameleon, CELL_DEFAULT_HEIGHT,
                        getResources().getString(R.string.short_lorem_ipsum)),
                new ExpandableListItem("Rock", R.drawable.rock, CELL_DEFAULT_HEIGHT,
                        getResources().getString(R.string.medium_lorem_ipsum)),
                new ExpandableListItem("Flower", R.drawable.flower, CELL_DEFAULT_HEIGHT,
                        getResources().getString(R.string.long_lorem_ipsum)),
        };

        List<ExpandableListItem> mData = new ArrayList<ExpandableListItem>();

        for (int i = 0; i < NUM_OF_CELLS; i++) {
            ExpandableListItem obj = values[i % values.length];
            mData.add(new ExpandableListItem(obj.getTitle(), obj.getImgResource(),
                    obj.getCollapsedHeight(), obj.getText()));
        }

        CustomArrayAdapter adapter = new CustomArrayAdapter(getActivity(), R.layout.list_view_item, mData);

        mListView.setAdapter(adapter);
        mListView.setDivider(null);
        mListView.setDividerHeight(8);
    }


}
