package umn.cs5115.kiwi;

public class Assignment {
	// private variables
	private int id;
	private String name;
	private String course;
	private String type;
	private String due_date;
	private int hours;
	private int minutes;
	private int reminder;
	private String reminder_time;
	private String notes;
	private String textbook;
	private int done = 0;

	// empty constructor
	Assignment() {
	}

	// constructor
	Assignment(int id, String name, String course, String type,
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
	
	public int getDone() {
		return this.done;
	}

	public String getCourse() {
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
	

	// setters for each of the Assignment elements
	public void setTextbook(String tb) {
		this.textbook = tb;
	}
	
	public void setDone(){
		if(this.done == 0){
			this.done = 1;
		}
		else{
			this.done = 1;
		}
	}
	
	public void setDone(int d) {
		this.done = d;
	}
	
	public void setName(String n) {
		this.name = n;
	}

	public void setCourse(String c) {
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

}