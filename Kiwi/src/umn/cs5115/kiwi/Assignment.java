package umn.cs5115.kiwi;

public class Assignment {
	// private variables
	private int id;
	private String name;
	private int course;
	private String type;
	private String due_date;
	private int hours;
	private int minutes;
	private int reminder;
	private String reminder_time;
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
			String due_date, int h, int m, int reminder, String reminder_time,
			String n, String tb) {
		this.id = id;
		this.name = name;
		this.course = course;
		this.type = type;
		this.due_date = due_date;
		this.hours = h;
		this.minutes = m;
		this.reminder = reminder;
		this.reminder_time = reminder_time;
		this.notes = n;
		this.textbook = tb;
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

	public String getDueDate() {
		return this.due_date;
	}

	public int getReminder() {
		return this.reminder;
	}

	public String getReminderTime() {
		return this.reminder_time;
	}

	public int getId() {
		return this.id;
	}

	public String getNotes() {
		return this.notes;
	}

	public int getHours() {
		return this.hours;
	}

	public int getMinutes() {
		return this.minutes;
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

	public void setDueDate(String d) {
		this.due_date = d;
	}

	public void setHours(int h) {
		this.hours = h;
	}

	public void setMinutes(int m) {

	}

	public void setReminder(int r) {
		this.reminder = r;
	}

	public void setReminderTime(String t) {
		this.reminder_time = t;
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
}