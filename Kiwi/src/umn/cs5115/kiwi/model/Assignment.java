package umn.cs5115.kiwi.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Assignment implements Parcelable {
	// private variables
	private int id;
	private String name;
	private int course;
	private String type;
	private long due_millis;
	private int reminder;
	private String notes;
	private String textbook;
	private boolean completed;
	private String color;
	
	private String courseDesignation;

	// empty constructor
	public Assignment() {
	}

	// constructor
	public Assignment(int id, String name, int course, String type,
			long due_millis, int reminder, String notes, String textbook) {
		this.id = id;
		this.name = name;
		this.course = course;
		this.type = type;
		this.due_millis = due_millis;
		this.reminder = reminder;
		this.notes = notes;
		this.textbook = textbook;
	}

	// getters for each of the Assignment elements

	public String getName() {
		return this.name;
	}
	
	public boolean isCompleted() {
		return this.completed;
	}

	public int getCourse() {
		return this.course;
	}

	public String getType() {
		return this.type;
	}

	public long getDueMillis() {
		return this.due_millis;
	}

	public int getReminder() {
		return this.reminder;
	}

	public int getId() {
		return this.id;
	}

	public String getNotes() {
		return this.notes;
	}
	
	public String getTextbook() {
		return this.textbook;
	}
	
	public String getCourseDesignation() {
		return this.courseDesignation;
	}
	

	// setters for each of the Assignment elements
	public void setTextbook(String tb) {
		this.textbook = tb;
	}
	
	public void setCompleted(boolean completed) {
		this.completed = completed;
	}
	
	public void setName(String n) {
		this.name = n;
	}

	public void setCourse(int c) {
		this.course = c;
	}

	public void setType(String t) {
		this.type = t;
	}

	public void setDueMillis(long d) {
		this.due_millis = d;
	}

	public void setReminder(int r) {
		this.reminder = r;
	}

	public void setId(int i) {
		this.id = i;
	}

	public void setNotes(String n) {
		this.notes = n;
	}
	
	public void setCourseDesignation(String cd) {
		this.courseDesignation = cd;
	}

	@Override
	public String toString() {
		String obj = super.toString();
		obj += " " + name;
		return obj;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}
	
	//==========================================================================
	// Methods required by Parcelable interface.

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeInt(course);
        dest.writeString(type);
        dest.writeLong(due_millis);
        dest.writeInt(reminder);
        dest.writeString(notes);
        dest.writeString(textbook);
        dest.writeByte((byte) (completed ? 1 : 0));
        dest.writeString(color);
        dest.writeString(courseDesignation);
    }
    
    /**
     * Constructor for the CREATOR inner class.
     * @param in the Parcel to read from to create this assignment object
     */
    private Assignment(Parcel in) {
        id = in.readInt();
        name = in.readString();
        course = in.readInt();
        type = in.readString();
        due_millis = in.readLong();
        reminder = in.readInt();
        notes = in.readString();
        textbook = in.readString();
        completed = in.readByte() != 0;
        color = in.readString();
        courseDesignation = in.readString();
    }
    
    public static final Parcelable.Creator<Assignment> CREATOR = new Parcelable.Creator<Assignment>() {
        public Assignment createFromParcel(Parcel in) {
            return new Assignment(in);
        }

        public Assignment[] newArray(int size) {
            return new Assignment[size];
        }
    };
}