package umn.cs5115.kiwi;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHandler extends SQLiteOpenHelper {
	public static final class DbAndCursor {
		public final SQLiteDatabase db;
		public final Cursor cursor;
		
		public DbAndCursor(SQLiteDatabase db, Cursor cursor) {
			this.db = db;
			this.cursor = cursor;
		}
		
		public void close() {
			db.close();
		}
	}

    // Database Version
    private static final int DATABASE_VERSION = 1;

    //Database States
    private static boolean DATABASE_EMPTY = true;

    // Database Name
    private static final String DATABASE_NAME = "assignmentsManager";

    // Assignments table name
    private static final String TABLE_ASSIGNMENTS = "assignments";
    private static final String TABLE_COURSES = "courses";
    
    // ID fields in both assignments and courses
    private static final String KEY_ID = "_id";

    // Assignments table column names
    public static final String NAME = "name";
    public static final String COURSE = "course";
    public static final String TYPE = "type";
    public static final String DUE_DATE = "due_date";
    public static final String DUE_HOURS = "due_hours";
    public static final String DUE_MINUTES = "due_minutes";
    public static final String REMINDER = "reminder";
    public static final String REMINDER_TIME = "reminder_time";
    public static final String NOTES = "notes";
    public static final String COMPLETED = "completed";

    // Course table column names
    public static final String DESIGNATION = "designation";
    public static final String START_TIME = "start_time";
    public static final String END_TIME = "end_time";
    public static final String LOCATION = "location";
    public static final String START_DATE = "start_date";
    public static final String END_DATE = "end_date";
    public static final String RRULE = "recurrence_rule";
    public static final String TEXTBOOKS = "textbooks";
    
    public static final String ASSIGNMENTS_QUERY;
    static {
    	ASSIGNMENTS_QUERY = String.format("SELECT a.*, c.%s as cname from %s AS a LEFT OUTER JOIN %s AS c ON a.%s = c.%s",
    										DESIGNATION, TABLE_ASSIGNMENTS, TABLE_COURSES, COURSE, KEY_ID);
    }

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
                + COURSE + " INTEGER,"
                + TYPE + " TEXT,"
                + DUE_DATE + " TEXT,"
                + DUE_HOURS + " TEXT,"
                + DUE_MINUTES + " TEXT,"
                + REMINDER + " INTEGER,"
                + REMINDER_TIME + " TEXT,"
                + NOTES + " TEXT,"
                + TEXTBOOKS + " TEXT,"
                + COMPLETED + " INTEGER"
                + ")";
        db.execSQL(CREATE_ASSIGNMENTS_TABLE);

        String CREATE_COURSES_TABLE = "CREATE TABLE " + TABLE_COURSES
                + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + NAME + " TEXT,"
                + DESIGNATION + " TEXT,"
                + START_TIME + " TEXT,"
                + END_TIME + " TEXT,"
                + LOCATION + " TEXT,"
                + START_DATE + " TEXT,"
                + END_DATE + " TEXT,"
                + RRULE + " TEXT,"
                + NOTES + " TEXT,"
                + TEXTBOOKS + " TEXT"
                + ")";


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
      //  cv.put(KEY_ID, a.getId());//assignment id
        cv.put(NAME, a.getName());//assignment name
        cv.put(COURSE, a.getCourse());//assignment course
        cv.put(TYPE, a.getType());//assignment type
        cv.put(DUE_DATE, a.getDueDate());//assignment due date
        cv.put(DUE_HOURS, a.getHours());//assignment due hours
        cv.put(DUE_MINUTES, a.getMinutes());//assignment due minutes
        cv.put(REMINDER, a.getReminder());//assignment reminder value
        cv.put(REMINDER_TIME, a.getReminderTime());//assignment reminder time
        cv.put(NOTES, a.getNotes());//assignment notes
        cv.put(TEXTBOOKS, a.getTextbook());//assignment textbook
        cv.put(COMPLETED, a.isCompleted());//assignment done

        //insert the assignment into the assignment table
        db.insert(TABLE_ASSIGNMENTS, null, cv);//insert the assignment
        db.close();//close the database
        DATABASE_EMPTY = false;//sets the database empty variable to be true since data has been added
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
      //  cv.put(KEY_ID, a.getId());//assignment id
        cv.put(NAME, a.getName());//assignment name
        cv.put(COURSE, a.getCourse());//assignment course
        cv.put(TYPE, a.getType());//assignment type
        cv.put(DUE_DATE, a.getDueDate());//assignment due date
        cv.put(DUE_HOURS, a.getHours());//assignment due hours
        cv.put(DUE_MINUTES, a.getMinutes());//assignment due minutes
        cv.put(REMINDER, a.getReminder());//assignment reminder value
        cv.put(REMINDER_TIME, a.getReminderTime());//assignment reminder time
        cv.put(NOTES, a.getNotes());//assignment notes
        cv.put(TEXTBOOKS, a.getTextbook());//assignment textbooks
        cv.put(COMPLETED, a.isCompleted());//assignment done

        //modify the assignment in the assignment table
        db.update(TABLE_ASSIGNMENTS, cv,KEY_ID + " = ?", new String[] {String.valueOf(a.getId())} );//overwrite the assignment from the table with the same id as Assignment a
        db.close();//close the database
    }

    //filter and return assignments as an array of assignment objects, pass null to get all assignments
    public Assignment[] filterAssignments(String filterBy){
        DbAndCursor dbc = getRawAssignmentCursor(filterBy);
        Cursor c = dbc.cursor;

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
        return a;
    }
    
    public DbAndCursor getRawAssignmentCursor(String filterBy) {
    	return getRawAssignmentCursor(filterBy, null);
    }

    public DbAndCursor getRawAssignmentCursor(String filterBy, String orderBy) {
        SQLiteDatabase db = this.getReadableDatabase();//get the database

        //set up and run the query in SQLite
        String select = ASSIGNMENTS_QUERY;
        if (filterBy != null) {
        	select += " WHERE " + filterBy;
        }
        if (orderBy != null) {
        	select += " ORDER BY " + orderBy;
        }
        Cursor c = db.rawQuery(select, null);
        
        return new DbAndCursor(db, c);
    }

    //convert a cursor row into an Assignment object (used in filterAssignments method)
    public static Assignment convertToAssignment(Cursor c){
        Assignment a = new Assignment();//assignment object to return

        //move fields from the cursor row into the assignment object
        a.setId(c.getInt(0));
        a.setName(c.getString(1));
        a.setCourse(c.getInt(2));
        a.setType(c.getString(3));
        a.setDueDate(c.getString(4));
        a.setHours(c.getInt(5));
        a.setMinutes(c.getInt(6));
        a.setReminder(c.getInt(7));
        a.setReminderTime(c.getString(8));
        a.setNotes(c.getString(9));
        a.setTextbook(c.getString(10));
        a.setCompleted(c.getInt(11) != 0);
        a.setCourseDesignation(c.getString(12));

        return a;
    }

    //add a course to the database
    public void addCourse(Course c) {
        SQLiteDatabase db = this.getWritableDatabase();//get the database
        ContentValues cv = new ContentValues();//create new CV object to populate with course

        //store course values into cv
        cv.put(NAME, c.getCourseTitle());
        cv.put(DESIGNATION, c.getCourseDesignation());
        cv.put(START_TIME, c.getStartTime());
        cv.put(END_TIME, c.getEndTime());
        cv.put(LOCATION, c.getLocation());
        cv.put(START_DATE, c.getStartDate());
        cv.put(END_DATE, c.getEndDate());
        cv.put(RRULE, c.getRRule());
        cv.put(NOTES, c.getNotes());
        cv.put(TEXTBOOKS, c.getTextbooksString());

        //insert the course into the courses table
        db.insert(TABLE_COURSES, null, cv);//insert the course
        db.close();//close the database
    }

    //remove a course from the database
    public void removeCourse(Course c){
        SQLiteDatabase db = this.getWritableDatabase();//get the database

        db.delete(TABLE_COURSES, KEY_ID + " = ?", new String[] {String.valueOf(c.getId())} );//remove the course from the table with the same id as course c
        
        /*
         * Also go and delete all the assignments for this course!
         */
        db.delete(TABLE_ASSIGNMENTS, COURSE + " = ?", new String[] {String.valueOf(c.getId()) });
        db.close();//close the database
    }

    //edit a course in the database
    public void editCourse(Course c) {
        SQLiteDatabase db = this.getWritableDatabase();//get the database
        ContentValues cv = new ContentValues();//create new CV object to populate with course

        //store course values into cv
        cv.put(NAME, c.getCourseTitle());
        cv.put(DESIGNATION, c.getCourseDesignation());
        cv.put(START_TIME, c.getStartTime());
        cv.put(END_TIME, c.getEndTime());
        cv.put(LOCATION, c.getLocation());
        cv.put(START_DATE, c.getStartDate());
        cv.put(END_DATE, c.getEndDate());
        cv.put(RRULE, c.getRRule());
        cv.put(NOTES, c.getNotes());
        cv.put(TEXTBOOKS, c.getTextbooksString());

        //modify the assignment in the assignment table
        db.update(TABLE_COURSES, cv,KEY_ID + " = ?", new String[] {String.valueOf(c.getId())} );//overwrite the course from the table with the same id as course c
        db.close();//close the database
    }
    
    public int getCourseCount() {
    	SQLiteDatabase db = this.getReadableDatabase();
    	
    	Cursor c = db.rawQuery(String.format("SELECT count(*) from %s", DatabaseHandler.TABLE_COURSES), null);
    	c.moveToFirst();
    	
    	int count = c.getInt(0);
    	
    	c.close();
    	db.close();
    	
    	return count;
    }

    // return courses in an array of course objects, pass null to get all courses
    public Course[] getCourses(){
        SQLiteDatabase db = this.getReadableDatabase();//get the database

        //set up and run the query in SQLite
        Cursor cursor = db.query(TABLE_COURSES, null, null, null, null, null, null);//query the database for the filtered values

        //set up the course data structures
        Course[] courseArray = new Course[cursor.getCount()];//empty array of course objects

        //convert the results into course objects and insert those objects into the arrays
        int index = 0;
        cursor.moveToFirst();//make sure cursor is set to beginning of results
        while(!cursor.isAfterLast()){//loop while end of results has not been reached
        	Course course = convertToCourse(cursor);
            courseArray[index++] = course;

            cursor.moveToNext();//increment to the next row of cursor result
        }

        cursor.close();//close the cursor
        db.close();//close the database
        return courseArray;

    }

    //convert a cursor row into a course object (used in filterCourses method)
    private Course convertToCourse(Cursor c){
        Course co = new Course();//course object to return

        //move fields from the cursor row into the course object
        co.setId(c.getInt(0));
        co.setCourseTitle(c.getString(1));
        co.setCourseDesignation(c.getString(2));
        co.setStartTime(c.getString(3));
        co.setEndTime(c.getString(4));
        co.setLocation(c.getString(5));
        co.setStartDate(c.getString(6));
        co.setEndDate(c.getString(7));
        co.setRRule(c.getString(8));
        co.setNotes(c.getString(9));
        co.setTextbooks(c.getString(10));

        return co;
    }

    //clear ALL database values
    public void clearDatabase(){
        SQLiteDatabase db = this.getWritableDatabase();//get the database
        db.close();//close the database
        db.delete(TABLE_ASSIGNMENTS, null, null);//delete all data in the assignments table
        db.delete(TABLE_COURSES, null, null);//delete all data in the courses table
        DATABASE_EMPTY = true;//set the test variable for empty database to true
    }

    //check if database is empty
    public boolean isDatabaseEmpty(){
        return DATABASE_EMPTY;
    }

}
