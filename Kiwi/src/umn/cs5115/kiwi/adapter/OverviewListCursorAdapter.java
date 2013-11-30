package umn.cs5115.kiwi.adapter;

import java.text.SimpleDateFormat;
import java.util.Locale;

import umn.cs5115.kiwi.DatabaseHandler;
import umn.cs5115.kiwi.R;
import umn.cs5115.kiwi.assignment.AssignmentUtils.DueDateBuilder;
import umn.cs5115.kiwi.model.Assignment;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.CursorAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

public class OverviewListCursorAdapter extends CursorAdapter {
	public static interface TileInteractionListener {
		/**
		 * Callback when the user has selected to change whether an assignment is
		 * completed or not, from the assignment tile.
		 * @param assignment the assignment in question
		 * @param isCompleted whether they want the assignment to be marked as completed
		 */
		public void onChangeCompletion(Assignment assignment, boolean isCompleted);

		/**
		 * Callback when the user intends to view an assignment (i.e. clicked on tile, or in menu)
		 * @param assignment the assignment to view
		 */
		public void viewAssignment(Assignment assignment);

		/**
		 * Callback when the user intends to edit an assignment.
		 * @param assignment the assignment to edit
		 */
		public void editAssignment(Assignment assignment);

		/**
		 * Callback when the user intends to delete an assignment.
		 * @param assignment the assignment to delete
		 */
		public void deleteAssignment(View removingView, Assignment assignment);
	}

	private final TileInteractionListener mListener;

	private static final float COMPLETED_ALPHA = 0.5f;

	/** Constructor. */
	public OverviewListCursorAdapter(Context context, Cursor c, TileInteractionListener listener) {
		super(context, c, 0);
		mListener = listener;
	}

	private void showAsCompleted(View itemView, boolean isCompleted) {
		itemView.setAlpha(isCompleted ? COMPLETED_ALPHA : 1);
	}

	@Override
	public void bindView(final View view, final Context context, Cursor cursor) {
	    view.setHapticFeedbackEnabled(false);
		final Assignment assignment = DatabaseHandler.convertToAssignment(cursor);
		
		// Fade out completed assignments
		showAsCompleted(view, assignment.isCompleted());

		view.setTag(assignment);
		
		// Populate text and state of the tile.

		TextView aName, aDate, aCourse, aType;
		CheckBox completedCB;
		
		// Get TextViews
		aName 	= (TextView) view.findViewById(R.id.assignment_name);
		aDate 	= (TextView) view.findViewById(R.id.assignment_date);
		aCourse = (TextView) view.findViewById(R.id.course_name);
		aType 	= (TextView) view.findViewById(R.id.assignment_type);
		
		// Get the checkbox
		completedCB = (CheckBox) view.findViewById(R.id.completed_check_box);
		
		// Set the color of the TextViews
		//DatabaseHandler dbHandler = new DatabaseHandler(context);
		
	//	dbHandler.
		Resources res = context.getResources();
		if(assignment.getColor() == null) {
			aName.setTextColor(res.getColor(R.color.black));
			aDate.setTextColor(res.getColor(R.color.black));
			aCourse.setTextColor(res.getColor(R.color.black));
			aType.setTextColor(res.getColor(R.color.black));
			completedCB.setTextColor(res.getColor(R.color.black));
			//Skip the rest of this code
		}
		else if(assignment.getColor().equals("orange")) {
			aName.setTextColor(res.getColor(R.color.orange));
			aDate.setTextColor(res.getColor(R.color.orange));
			aCourse.setTextColor(res.getColor(R.color.orange));
			aType.setTextColor(res.getColor(R.color.orange));
			completedCB.setTextColor(res.getColor(R.color.orange));
		}
		else if(assignment.getColor().equals("green")) {
			aName.setTextColor(res.getColor(R.color.green));
			aDate.setTextColor(res.getColor(R.color.green));
			aCourse.setTextColor(res.getColor(R.color.green));
			aType.setTextColor(res.getColor(R.color.green));
			completedCB.setTextColor(res.getColor(R.color.green));
		}
		else if(assignment.getColor().equals("blue")) {
			aName.setTextColor(res.getColor(R.color.blue));
			aDate.setTextColor(res.getColor(R.color.blue));
			aCourse.setTextColor(res.getColor(R.color.blue));
			aType.setTextColor(res.getColor(R.color.blue));
			completedCB.setTextColor(res.getColor(R.color.blue));
		}
		else if(assignment.getColor().equals("red")) {
			aName.setTextColor(res.getColor(R.color.red));
			aDate.setTextColor(res.getColor(R.color.red));
			aCourse.setTextColor(res.getColor(R.color.red));
			aType.setTextColor(res.getColor(R.color.red));
			completedCB.setTextColor(res.getColor(R.color.red));
		}
		else if(assignment.getColor().equals("purple")){
			aName.setTextColor(res.getColor(R.color.purple));
			aDate.setTextColor(res.getColor(R.color.purple));
			aCourse.setTextColor(res.getColor(R.color.purple));
			aType.setTextColor(res.getColor(R.color.purple));
			completedCB.setTextColor(res.getColor(R.color.purple));
		}
		else if(assignment.getColor().equals("yellow")) {
			aName.setTextColor(res.getColor(R.color.yellow));
			aDate.setTextColor(res.getColor(R.color.yellow));
			aCourse.setTextColor(res.getColor(R.color.yellow));
			aType.setTextColor(res.getColor(R.color.yellow));
			completedCB.setTextColor(res.getColor(R.color.yellow));
		}
		
		// Set their text values
		aName.setText(assignment.getName());
		if (assignment.getDueMillis() > 10000) {
			aDate.setText(new SimpleDateFormat("h:mm aaa, EEE, MMM dd, ''yy", Locale.US).format(new DueDateBuilder(assignment.getDueMillis()).toDate()));
		} else {
			aDate.setText(null);
		}
		aCourse.setText(assignment.getCourseDesignation()); //I do not know how this value was being stored but it broke when I changed to put in the course colors
		aType.setText(assignment.getType());
		
		/*
		 * Remove any pre-existing listeners that might be on this view.
		 * If we don't do this, then when this view is recycled (e.g. re-bound)
		 * then any toggling of the checkbox's state will trigger that click
		 * handler, which we don't want to have happen.
		 */
		completedCB.setOnCheckedChangeListener(null);
		completedCB.setChecked(assignment.isCompleted());
		/*
		 * Bake up a new listener for the checkbox, and add it.
		 */
		OnCheckedChangeListener listener = new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				mListener.onChangeCompletion(assignment, isChecked);
			}
		};
		completedCB.setOnCheckedChangeListener(listener);

		
		//Setting if the assignment tiles need to have the notes and textbook identifier turned on
		ImageView notesIdentifier = (ImageView) view.findViewById(R.id.assignment_tile_notes_identifier);
		if (assignment.getNotes() == null || assignment.getNotes().isEmpty()) {
			notesIdentifier.setVisibility(View.GONE);
		}
		else {
			notesIdentifier.setVisibility(View.VISIBLE);
		}

		ImageView textbookIdentifier = (ImageView) view.findViewById(R.id.assignment_tile_textbook_identifier);
		if (assignment.getTextbook() == null || assignment.getTextbook().isEmpty()) {
			textbookIdentifier.setVisibility(View.GONE);
		}
		else {
			textbookIdentifier.setVisibility(View.VISIBLE);
		}

		final ImageButton popupButton = (ImageButton) view.findViewById(R.id.assignment_tile_popup_button);
		popupButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				PopupMenu popup = new PopupMenu(context, popupButton);
				popup.getMenuInflater().inflate(R.menu.assignment_tile_popup, popup.getMenu());

				popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
					public boolean onMenuItemClick(final MenuItem item) {
						switch (item.getItemId()) {
						case R.id.assignment_tile_popup_edit:
							mListener.editAssignment(assignment);
							break;
						case R.id.assignment_tile_popup_delete:
							mListener.deleteAssignment(view, assignment);
							break;
						case R.id.assignment_tile_popup_view:
							mListener.viewAssignment(assignment);
							break;
						}
						return true;
					}
				});

				popup.show();
			}
		});
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
		return LayoutInflater.from(context).inflate(R.layout.assignment_tile, null);
	}

}
