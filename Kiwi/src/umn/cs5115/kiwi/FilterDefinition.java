package umn.cs5115.kiwi;

import java.util.ArrayList;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

/**
 * Represents the state of the filtering/sorting displayed in a fragment.
 *
 * @author mike
 *
 */
public class FilterDefinition implements Parcelable {
    public static enum SortBy {
    	DUE_DATE(0), COURSE(1), TYPE(2);

    	private final int value;
    	private SortBy(int value) {
    		this.value = value;
    	}
    	public final int toInt() {
    		return value;
    	}
    	public static final SortBy fromInt(int v) {
    		switch (v) {
    		case 0:
    			return DUE_DATE;
    		case 1:
    			return COURSE;
    		default:
    			return TYPE;
    		}
    	}
    }

    public int[] courses;
    public String[] types;
    public SortBy sorter;
    public boolean showCompleted;

    private FilterDefinition(Parcel in) {
    	courses = in.createIntArray();
    	types = in.createStringArray();
    	sorter = SortBy.fromInt(in.readInt());
    	showCompleted = (in.readByte() != 0);
    }

    public FilterDefinition(SortBy sorter, boolean showCompleted, int[] courses, String[] types) {
    	this.sorter = sorter;
    	this.showCompleted = showCompleted;
    	this.courses = courses;
    	this.types = types;
    }
    
    public String getOrderString() {
    	switch (sorter) {
    	case COURSE:
    		return "cname ASC";
    	case DUE_DATE:
    		return DatabaseHandler.DUE_DATE + " ASC";
    	default:
    		return DatabaseHandler.TYPE + " ASC";
    	}
    }
    
    public String toQueryString() {
    	// Filter on completion status
    	String completed = String.format("(%s %s 0)", DatabaseHandler.COMPLETED, (showCompleted ? ">=" : "="));
    	
    	// Make a piece of the query string to filter on courses
    	ArrayList<String> coursechecks = new ArrayList<String>();
    	if (courses == null) {
    	    /*
    	     * No course filtering specified? No course filtering will be done!
    	     */
    	} else if (courses.length == 0) {
    	    /*
    	     * The user seems to have deliberately chosen to hide every course.
    	     * We know that (without crazy external intervention) course IDs
    	     * will all be 1+. We also have a 'None' option in the course
    	     * checkboxes, so that must be unchecked. Guess the user wants to
    	     * see absolutely no assignments!
    	     */
    	    coursechecks.add(String.format("%s = -10389", DatabaseHandler.COURSE));
    	} else {
	    	for (int course : courses) {
	    		coursechecks.add(String.format("%s = %d", DatabaseHandler.COURSE, course));
	    	}
    	}
    	
    	String course = null;
    	if (!coursechecks.isEmpty()) {
    		course = "(" + TextUtils.join(" OR ", coursechecks) + ")";
    	}
    	
    	// Make a piece of the query string to filter on types
    	ArrayList<String> typechecks = new ArrayList<String>();
    	if (types == null) {
    	    /*
    	     * No type filtering given? No type filtering will be done!
    	     */
    	}
    	else if (types.length == 0) {
    	    /*
    	     * The user seems to have deliberately chosen to hide every type.
    	     * This includes the 'no type selected' option.
    	     */
    	    typechecks.add(String.format("%s = '_NotAType!!_'", DatabaseHandler.COURSE));
    	} else {
        	for (String type : types) {
        	    /*
        	     * If assignment type is inserted as null, we need to check
        	     * for it against `null`, not ''.
        	     */
        	    if (type == null) {
        	        typechecks.add(String.format("%s is null", DatabaseHandler.TYPE));
        	    } else {
                    typechecks.add(String.format("%s = '%s'", DatabaseHandler.TYPE, type));
        	    }
        	}
    	}
    	
    	/*
    	 * If typechecks is empty, we do no filtering on assignment type.
    	 * (Remember, if the types array is EMPTY but not null, this means
    	 * they want to hide every type...)
    	 */
    	String type;
    	if (typechecks.isEmpty()) {
    	    type = null;
    	} else {
    	    type = "(" + TextUtils.join(" OR ", typechecks) + ")";
    	}
    	
    	// Construct the query string.
    	ArrayList<String> pieces = new ArrayList<String>();
    	pieces.add(completed);
    	if (type != null) {
    		pieces.add(type);
    	}
    	if (course != null) {
    		pieces.add(course);
    	}
    	
    	String query = TextUtils.join(" AND ", pieces);
    	
    	return query;
    }

    @Override
    public int describeContents() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    	dest.writeStringArray(types);
    	dest.writeIntArray(courses);
    	dest.writeInt(sorter.toInt());
    	dest.writeByte((byte) (showCompleted ? 1: 0));
    }
    // set of courses to display, etc etc

    public static final Parcelable.Creator<FilterDefinition> CREATOR = new Parcelable.Creator<FilterDefinition>() {
        public FilterDefinition createFromParcel(Parcel in) {
            return new FilterDefinition(in);
        }

        public FilterDefinition[] newArray(int size) {
            return new FilterDefinition[size];
        }
    };
}
