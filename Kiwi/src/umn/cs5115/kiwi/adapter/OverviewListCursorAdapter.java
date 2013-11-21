package umn.cs5115.kiwi.adapter;

import umn.cs5115.kiwi.Assignment;
import umn.cs5115.kiwi.DatabaseHandler;
import umn.cs5115.kiwi.R;
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CursorAdapter;
import android.widget.TextView;

public class OverviewListCursorAdapter extends CursorAdapter {
	/** Constructor. */
	public OverviewListCursorAdapter(Context context, Cursor c, int flags) {
		super(context, c, flags);
	}
	
	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		Assignment assignment = DatabaseHandler.convertToAssignment(cursor);
		
		((TextView)view.findViewById(R.id.assignment_name)).setText(assignment.getName() + "--" + assignment.getCourseName());
		
		CheckBox completedCheckbox = (CheckBox)view.findViewById(R.id.completed_check_box);
		completedCheckbox.setChecked(assignment.isCompleted());
		
		// Fade out completed assignments
		view.setAlpha(assignment.isCompleted() ? 0.5f : 1);
	}

	/*
	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		Assignment object = DatabaseHandler.convertToAssignment(cursor);
		
		View convertView = view;
        LinearLayout linearLayout = (LinearLayout)(convertView.findViewById(
                R.id.item_linear_layout));
        LinearLayout.LayoutParams linearLayoutParams = new LinearLayout.LayoutParams
                (AbsListView.LayoutParams.MATCH_PARENT, ExpandableListItem.);
        linearLayout.setLayoutParams(linearLayoutParams);

    //    TextView titleView = (TextView)convertView.findViewById(R.id.title_view);
    //    titleView.setText(object.getTitle());
        
        //Setting the Course Name on the Assignment Tile
        TextView courseNameView = (TextView) convertView.findViewById(R.id.course_name);
        courseNameView.setText(object.getCourse());
        
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

        //Expanded portion of the Assignment Tile
        ExpandingLayout expandingLayout =
                (ExpandingLayout) convertView.findViewById(R.id.expanding_layout);
        expandingLayout.setExpandedHeight(object.getExpandedHeight());
        expandingLayout.setSizeChangedListener(object);
        
        //Setting the Notes on the Assignment Tile
        TextView assignmentNotesView = (TextView) convertView.findViewById(R.id.assignment_notes);
        assignmentNotesView.setText(object.getAssignmentNotes());
        
        //Setting the Textbooks on the Assignment Tile
        int numTextbooks = object.getAssignmentTextbooks().length;
        String textbookString = "";
        for(int i = 0; i < numTextbooks; i++) {
        	if(i == (numTextbooks - 1)){
        		textbookString += object.getAssignmentTextbooks()[i];
        	}
        	else {
        		textbookString += object.getAssignmentTextbooks()[i] + "\n";
        	}
        }
        TextView assignmentTextbooksView = (TextView) convertView.findViewById(R.id.assignment_textbooks);
        assignmentTextbooksView.setText(textbookString);

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
*/
	@Override
	public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
		return LayoutInflater.from(context).inflate(R.layout.assignment_tile, null);
	}

}
