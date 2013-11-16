package umn.cs5115.kiwi.adapter;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;

import umn.cs5115.kiwi.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.android.expandingcells.ExpandableListItem;
import com.example.android.expandingcells.ExpandingLayout;

public class OverviewListAdapter extends ArrayAdapter<ExpandableListItem> {
    public static interface ItemInteractionListener {
        public void onEdit(ExpandableListItem object);
        public void onDelete(ExpandableListItem object);
    }
    
    private List<ExpandableListItem> mData;
    private int mLayoutViewResourceId;
    private final ItemInteractionListener mListener;
    
    public OverviewListAdapter(Context context, int layoutViewResourceId,
                                List<ExpandableListItem> data,
                                ItemInteractionListener listener) {
        super(context, layoutViewResourceId, data);
        mData = data;
        mLayoutViewResourceId = layoutViewResourceId;
        mListener = listener;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ExpandableListItem object = mData.get(position);
        
        if (convertView == null) {
            // Need to inflate the layout
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(mLayoutViewResourceId, parent, false);
        }
        
        LinearLayout linearLayout = (LinearLayout)(convertView.findViewById(
                R.id.item_linear_layout));
        LinearLayout.LayoutParams linearLayoutParams = new LinearLayout.LayoutParams
                (AbsListView.LayoutParams.MATCH_PARENT, object.getCollapsedHeight());
        linearLayout.setLayoutParams(linearLayoutParams);

    //    TextView titleView = (TextView)convertView.findViewById(R.id.title_view);
    //    titleView.setText(object.getTitle());
        
        //Setting the Course Name on the Assignment Tile
        TextView courseNameView = (TextView) convertView.findViewById(R.id.course_name);
        courseNameView.setText(object.getCourseName());
        
        //This needs to be formatted to look better
        //Setting the Date on the Assignment Tile
        TextView assignmentDateView = (TextView) convertView.findViewById(R.id.assignment_date);
        DateFormat dateFormat = DateFormat.getDateTimeInstance();
        assignmentDateView.setText(dateFormat.format(object.getAssignmentDate()));		//For some reason this causes the rest of the information to display
        
        //Setting the Assignment Name on the Assignment Tile
        TextView assignmentNameView = (TextView) convertView.findViewById(R.id.assignment_name);
        assignmentNameView.setText(object.getAssignmentName());
        
        //Setting the Assignment Type on the Assignment Tile
        TextView assignmentTypeView = (TextView) convertView.findViewById(R.id.assignment_type);
        assignmentTypeView.setText(object.getAssignmentType());

        convertView.setLayoutParams(
                new ListView.LayoutParams(
                        AbsListView.LayoutParams.MATCH_PARENT,
                        AbsListView.LayoutParams.WRAP_CONTENT));

        ExpandingLayout expandingLayout =
                (ExpandingLayout) convertView.findViewById(R.id.expanding_layout);
        expandingLayout.setExpandedHeight(object.getExpandedHeight());
        expandingLayout.setSizeChangedListener(object);
        
        TextView assignmentNotesView = (TextView) convertView.findViewById(R.id.assignment_notes);
        assignmentNotesView.setText(object.getAssignmentNotes());

        if (!object.isExpanded()) {
            expandingLayout.setVisibility(View.GONE);
        } else {
            expandingLayout.setVisibility(View.VISIBLE);
        }
        
        if (mListener != null) {
            convertView.findViewById(R.id.editButton).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onEdit(object);
                }
            });
            convertView.findViewById(R.id.deleteButton).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onDelete(object);
                }
            });
        }
        
        return convertView;
    }
}
