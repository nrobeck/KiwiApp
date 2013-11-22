package umn.cs5115.kiwi;

import umn.cs5115.kiwi.activity.KiwiActivity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class AdvancedOptionsActivity extends KiwiActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// TODO real layout for this...
		setContentView(R.layout.delete_courses_activity);
	}

}
