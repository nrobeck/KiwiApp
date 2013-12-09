package umn.cs5115.kiwi;

import umn.cs5115.kiwi.app.KiwiActivity;
import umn.cs5115.kiwi.app.KiwiDoneCancelActivity;
import android.os.Bundle;
import android.widget.Toast;

public class ImportCoursesActivity extends KiwiActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.activity_import_courses);
    }
    
}
