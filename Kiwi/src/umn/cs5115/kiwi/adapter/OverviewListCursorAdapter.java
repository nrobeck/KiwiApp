package umn.cs5115.kiwi.adapter;

import umn.cs5115.kiwi.Assignment;
import umn.cs5115.kiwi.DatabaseHandler;
import umn.cs5115.kiwi.R;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CursorAdapter;
import android.view.View.OnClickListener;
import android.webkit.WebView.FindListener;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

public class OverviewListCursorAdapter extends CursorAdapter {
	/** Constructor. */
	public OverviewListCursorAdapter(Context context, Cursor c, int flags) {
		super(context, c, flags);
	}

	@Override
	public void bindView(final View view, final Context context, Cursor cursor) {
		Assignment assignment = DatabaseHandler.convertToAssignment(cursor);

		((TextView)view.findViewById(R.id.assignment_name)).setText(assignment.getName() + "--" + assignment.getCourseName());

		CheckBox completedCheckbox = (CheckBox)view.findViewById(R.id.completed_check_box);
		completedCheckbox.setChecked(assignment.isCompleted());

		// Fade out completed assignments
		view.setAlpha(assignment.isCompleted() ? 0.5f : 1);

		((TextView)view.findViewById(R.id.assignment_name)).setText(assignment.getName());
		((TextView)view.findViewById(R.id.assignment_date)).setText(assignment.getDueDate());
		//TODO:		((TextView)view.findViewById(R.id.course_name)).setText(assignment.getCourse()); //Need to get the course name
		((TextView)view.findViewById(R.id.assignment_type)).setText(assignment.getType());

		final Button popupButton = (Button) view.findViewById(R.id.assignment_tile_popup_button);

		popupButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				PopupMenu popup = new PopupMenu(context, popupButton);
				popup.getMenuInflater().inflate(R.menu.assignment_tile_popup, popup.getMenu());

				popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
					public boolean onMenuItemClick(final MenuItem item) {
						//Notes
						if (item.getTitle().equals(context.getString(R.string.notes))) {
							Toast.makeText(context,
									"You Clicked : " + item.getTitle(),
									Toast.LENGTH_SHORT).show();
						}
						//Textbooks
						if (item.getTitle().equals(context.getString(R.string.textbook))) {
							Toast.makeText(context,
									"You Clicked : " + item.getTitle(),
									Toast.LENGTH_SHORT).show();
						}
						//Edit
						if (item.getTitle().equals(context.getString(R.string.edit))) {
							Toast.makeText(context,
									"You Clicked : " + item.getTitle(),
									Toast.LENGTH_SHORT).show();
						}
						//Delete
						if (item.getTitle().equals(context.getString(R.string.delete))) {
							new AlertDialog.Builder(context)
							.setTitle("Delete assignment")
							.setMessage("Are you sure you want to delete this assignment?")
							.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int which) { 
									// continue with delete
									Toast.makeText(context,
											"You Clicked : " + item.getTitle(),
											Toast.LENGTH_SHORT).show();
								}
							})
							.setNegativeButton("No", new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int which) { 
									// do nothing
								}
							})
							.show();
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
