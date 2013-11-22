package umn.cs5115.kiwi.adapter;

import umn.cs5115.kiwi.Assignment;
import umn.cs5115.kiwi.DatabaseHandler;
import umn.cs5115.kiwi.R;
import android.content.Context;
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
		final Assignment assignment = DatabaseHandler.convertToAssignment(cursor);
		
		// Fade out completed assignments
		showAsCompleted(view, assignment.isCompleted());

		view.setTag(assignment);
		
		// Populate text and state of the tile.

		TextView aName, aDate, aCourse, aType;
		CheckBox completedCheckbox;
		
		// Get TextViews
		aName 	= (TextView) view.findViewById(R.id.assignment_name);
		aDate 	= (TextView) view.findViewById(R.id.assignment_date);
		aCourse = (TextView) view.findViewById(R.id.course_name);
		aType 	= (TextView) view.findViewById(R.id.assignment_type);
		
		// Set their text values
		aName.setText(assignment.getName());
		aDate.setText(assignment.getDueDate());
		aCourse.setText(assignment.getCourseDesignation());
		aType.setText(assignment.getType());

		// Get the CheckBox
		completedCheckbox = (CheckBox)view.findViewById(R.id.completed_check_box);
		
		/*
		 * Remove any pre-existing listeners that might be on this view.
		 * If we don't do this, then when this view is recycled (e.g. re-bound)
		 * then any toggling of the checkbox's state will trigger that click
		 * handler, which we don't want to have happen.
		 */
		completedCheckbox.setOnCheckedChangeListener(null);
		completedCheckbox.setChecked(assignment.isCompleted());
		/*
		 * Bake up a new listener for the checkbox, and add it.
		 */
		OnCheckedChangeListener listener = new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				mListener.onChangeCompletion(assignment, isChecked);
			}
		};
		completedCheckbox.setOnCheckedChangeListener(listener);

		
		//Setting if the assignment tiles need to have the notes and textbook identifier turned on
		ImageView notesIdentifier = (ImageView) view.findViewById(R.id.assignment_tile_notes_identifier);
		if (assignment.getNotes() == null || assignment.getNotes().isEmpty()) {
			notesIdentifier.setVisibility(View.INVISIBLE);
		}
		else {
			notesIdentifier.setVisibility(View.VISIBLE);
		}

		ImageView textbookIdentifier = (ImageView) view.findViewById(R.id.assignment_tile_textbook_identifier);
		if (assignment.getTextbook() == null || assignment.getTextbook().isEmpty()) {
			textbookIdentifier.setVisibility(View.INVISIBLE);
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
