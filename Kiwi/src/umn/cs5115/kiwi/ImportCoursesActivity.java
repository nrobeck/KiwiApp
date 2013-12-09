package umn.cs5115.kiwi;

import umn.cs5115.kiwi.app.KiwiActivity;
import android.os.Bundle;
import android.view.MenuItem;

public class ImportCoursesActivity extends KiwiActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.activity_import_courses);
        
        getActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    
}
