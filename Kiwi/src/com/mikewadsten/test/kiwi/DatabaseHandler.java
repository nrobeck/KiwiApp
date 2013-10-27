package com.mikewadsten.test.kiwi;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHandler extends SQLiteOpenHelper {
	 
    // Database Version
    private static final int DATABASE_VERSION = 1;
 
    // Database Name
    private static final String DATABASE_NAME = "assignmentsManager";
 
    // Assignments table name
    private static final String TABLE_ASSIGNMENTS = "assignments";
    private static final String TABLE_COURSES = "courses";
 
    // Assignments table column names
    private static final String KEY_ID = "id";
    private static final String NAME = "name";
    private static final String COURSE = "course";
    private static final String TYPE = "type";
    private static final String DUE_DATE = "due_date";
    private static final String DUE_TIME = "due_time";
    private static final String REMINDER = "reminder";
    private static final String REMINDER_TIME = "reminder_time";
    
    // Course table column names
    private static final String START_TIME = "start_time";
    private static final String END_TIME = "end_time";
    private static final String LOCATION = "location";
    private static final String START_DATE = "start_date";
    private static final String END_DATE = "end_date";
    private static final String RRULE = "recurrence_rule";
 
    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
 
    // Create Assignments Table
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_ASSIGNMENTS_TABLE = "CREATE TABLE " + TABLE_ASSIGNMENTS 
        		+ "("
                + KEY_ID + " INTEGER PRIMARY KEY," 
        		+ NAME + " TEXT,"
                + COURSE + " TEXT," 
        		+ TYPE + " TEXT," 
                + DUE_DATE + " TEXT," 
                + DUE_TIME + " TEXT," 
                + REMINDER + " INTEGER," 
                + REMINDER_TIME + " TEXT" 
                + ")";
        db.execSQL(CREATE_ASSIGNMENTS_TABLE);
        
        String CREATE_COURSES_TABLE = "CREATE TABLE" + TABLE_COURSES 
        		+ "(" 
        		+ NAME + "TEXT," 
        		+ KEY_ID + "TEXT," 
        		+ START_TIME + "TEXT," 
        		+ END_TIME + "TEXT," 
        		+ LOCATION + "TEXT," 
        		+ START_DATE + "TEXT," 
        		+ END_DATE + "TEXT," 
        		+ RRULE + "TEXT" + ")";
        
        
        db.execSQL(CREATE_COURSES_TABLE);
    }
 
    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older tables if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ASSIGNMENTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COURSES);
 
        // Create tables again
        onCreate(db);
    }
    
    //database operations
    
    //add new assignment to the database
    public void addAssignment(Assignment a) {
    	SQLiteDatabase db = this.getWritableDatabase();//get the database
    	ContentValues cv = new ContentValues();//create new CV object to populate with assignment
    	
    	//store assignment values from Assignment object a in the ContentValues object cv
    	cv.put(KEY_ID, a.getId());//assignment id
    	cv.put(NAME, a.getName());//assignment name
    	cv.put(COURSE, a.getCourse());//assignment course
    	cv.put(TYPE, a.getType());//assignment type
    	cv.put(DUE_DATE, a.getDueDate());//assignment due date
    	cv.put(DUE_TIME, a.getDueTime());//assignment due time
    	cv.put(REMINDER, a.getReminder());//assignment reminder value
    	cv.put(REMINDER_TIME, a.getReminderTime());//assignment reminder time
    	
    	//insert the assignment into the assignment table
    	db.insert(TABLE_ASSIGNMENTS, null, cv);//insert the assignment
    	db.close();//close the database
    }
    
    //remove assignment
    public void removeAssignment(Assignment a){
    	SQLiteDatabase db = this.getWritableDatabase();//get the database
    	
    	db.delete(TABLE_ASSIGNMENTS, KEY_ID + " = ?", new String[] {String.valueOf(a.getId())} );//remove the assignment from the table with the same id as Assignment A
    	db.close();//close the database
    }
    
    //edit assignment
    public void editAssignment(Assignment a) {
    	SQLiteDatabase db = this.getWritableDatabase();//get the database
    	ContentValues cv = new ContentValues();//create new CV object to populate with assignment database
    	
    	//store assignment values from Assignment object a in the ContentValues object cv
    	cv.put(KEY_ID, a.getId());//assignment id
    	cv.put(NAME, a.getName());//assignment name
    	cv.put(COURSE, a.getCourse());//assignment course
    	cv.put(TYPE, a.getType());//assignment type
    	cv.put(DUE_DATE, a.getDueDate());//assignment due date
    	cv.put(DUE_TIME, a.getDueTime());//assignment due time
    	cv.put(REMINDER, a.getReminder());//assignment reminder value
    	cv.put(REMINDER_TIME, a.getReminderTime());//assignment reminder time
    	
    	//modify the assignment in the assignment table
    	db.update(TABLE_ASSIGNMENTS, cv,KEY_ID + " = ?", new String[] {String.valueOf(a.getId())} );//overwrite the assignment from the table with the same id as Assignment a
    	db.close();//close the database
    }
    
    //filter and return assignments as an array of assignment objects, pass null to get all assignments
    public Assignment[] filterAssignments(String filterBy){
    	SQLiteDatabase db = this.getReadableDatabase();//get the database
    	
    	//set up and run the query in SQLite
    	Cursor c = db.query(TABLE_ASSIGNMENTS, null, filterBy, null, null, null, null);//query the database for the filtered values

    	//set up the assignment data structures
    	Assignment[] a = new Assignment[c.getCount()];//empty array of assignment objects
    	Assignment assign = new Assignment();//empty assignment object
    	
    	//convert the results into assignment objects and insert those objects into the arrays
    	int assignmentArrayLocation = 0;
    	c.moveToFirst();//make sure cursor is set to beginning of results
    	while(!c.isAfterLast()){//loop while end of results has not been reached
    		assign = convertToAssignment(c);//convert cursor row to assignment object
    		a[assignmentArrayLocation] = assign;//place assignment object into array
    		c.moveToNext();//increment to the next row of cursor result
    		assignmentArrayLocation++;//increment array location
    	}
    	
    	c.close();//close the cursor
    	db.close();//close the database
    	return a;
    }

    //convert a cursor row into an Assignment object (used in filterAssignments method)
    private Assignment convertToAssignment(Cursor c){
    	Assignment a = new Assignment();//assignment object to return
    	
    	//move fields from the cursor row into the assignment object
    	a.setId(c.getInt(0));
    	a.setName(c.getString(1));
    	a.setCourse(c.getString(2));
    	a.setType(c.getString(3));
    	a.setDueDate(c.getString(4));
    	a.setDueTime(c.getString(5));
    	a.setReminder(c.getInt(6));
    	a.setReminderTime(c.getString(7));
    	
    	return a;
    }
    
    //add a course to the database
    public void addCourse(Course c) {
    	SQLiteDatabase db = this.getWritableDatabase();//get the database
    	ContentValues cv = new ContentValues();//create new CV object to populate with course
    	
    	//store course values into cv
    	cv.put(NAME, c.getCourseTitle());
    	cv.put(KEY_ID, c.getCourseDesignation());
    	cv.put(START_TIME, c.getStartTime());
    	cv.put(END_TIME, c.getEndTime());
    	cv.put(LOCATION, c.getLocation());
    	cv.put(START_DATE, c.getStartDate());
    	cv.put(END_DATE, c.getEndDate());
    	cv.put(RRULE, c.getRRule());
    	
    	//insert the course into the courses table
    	db.insert(TABLE_COURSES, null, cv);//insert the course
    	db.close();//close the database
    }
    
    //remove a course from the database
    public void removeCourse(Course c){
    	SQLiteDatabase db = this.getWritableDatabase();//get the database
    	
    	db.delete(TABLE_COURSES, KEY_ID + " = ?", new String[] {String.valueOf(c.getCourseDesignation())} );//remove the course from the table with the same id as course c
    	db.close();//close the database
    }
    
    //edit a course in the database
    public void editCourse(Course c) {
    	SQLiteDatabase db = this.getWritableDatabase();//get the database
    	ContentValues cv = new ContentValues();//create new CV object to populate with course
    	
    	//store course values into cv
    	cv.put(NAME, c.getCourseTitle());
    	cv.put(KEY_ID, c.getCourseDesignation());
    	cv.put(START_TIME, c.getStartTime());
    	cv.put(END_TIME, c.getEndTime());
    	cv.put(LOCATION, c.getLocation());
    	cv.put(START_DATE, c.getStartDate());
    	cv.put(END_DATE, c.getEndDate());
    	cv.put(RRULE, c.getRRule());
    	
    	//modify the assignment in the assignment table
    	db.update(TABLE_COURSES, cv,KEY_ID + " = ?", new String[] {String.valueOf(c.getCourseDesignation())} );//overwrite the course from the table with the same id as course c
    	db.close();//close the database
    }
    
    //filter and return courses in an array of course objects, pass null to get all courses
    public Course[] filterCourses(String filterBy){
    	SQLiteDatabase db = this.getReadableDatabase();//get the database
    	
    	//set up and run the query in SQLite
    	Cursor c = db.query(TABLE_COURSES, null, filterBy, null, null, null, null);//query the database for the filtered values

    	//set up the course data structures
    	Course[] co = new Course[c.getCount()];//empty array of course objects
    	Course course = new Course();//empty course object
    	
    	//convert the results into course objects and insert those objects into the arrays
    	int courseArrayLocation = 0;
    	c.moveToFirst();//make sure cursor is set to beginning of results
    	while(!c.isAfterLast()){//loop while end of results has not been reached
    		course = convertToCourse(c);//convert cursor row to assignment object
    		co[courseArrayLocation] = course;//place assignment object into array
    		c.moveToNext();//increment to the next row of cursor result
    		courseArrayLocation++;//increment array location
    	}
    	
    	c.close();//close the cursor
    	db.close();//close the database
    	return co;
    	
    }	
    	
    //convert a cursor row into a course object (used in filterCourses method)
    private Course convertToCourse(Cursor c){
    	Course co = new Course();//course object to return
    	
    	//move fields from the cursor row into the course object
    	co.setCourseTitle(c.getString(0));
    	co.setCourseDesignation(c.getString(1));
    	co.setStartTime(c.getString(2));
    	co.setEndTime(c.getString(3));
    	co.setLocation(c.getString(4));
    	co.setStartDate(c.getString(5));
    	co.setEndDate(c.getString(6));
    	co.setRRule(c.getString(7));
    	
    	return co;
    }

}