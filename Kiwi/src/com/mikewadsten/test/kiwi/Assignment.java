package com.mikewadsten.test.kiwi;

public class Assignment {
	//private variables
	private int id;
	private String name;
	private String course;
	private String type;
	private String due_date;
	private String due_time;
	private int reminder;
	private String reminder_time;
	private String notes;
	
	//empty constructor
	Assignment(){
	}
	
	//constructor
	Assignment(int id, String name, String course, String type, String due_date,
			String due_time, int reminder, String reminder_time, String n){
		this.id=id;
		this.name=name;
		this.course=course;
		this.type=type;
		this.due_date=due_date;
		this.due_time=due_time;
		this.reminder=reminder;
		this.reminder_time=reminder_time;
		this.notes = n;
	}
	
	//getters for each of the Assignment elements
	
	public String getName(){
		return this.name;
	}
	
	public String getCourse(){
		return this.course;
	}
	
	public String getType(){
		return this.type;
	}
	
	public String getDueDate(){
		return this.due_date;
	}
	
	public String getDueTime(){
		return this.due_time;
	}
	
	public int getReminder(){
		return this.reminder;
	}
	
	public String getReminderTime(){
		return this.reminder_time;
	}
	
	public int getId(){
		return this.id;
	}
	
	public String getNotes() {
		return this.notes;
	}
	
	//setters for each of the Assignment elements
	public void setName(String n){
		this.name=n;
	}
	
	public void setCourse(String c){
		this.course=c;
	}
	
	public void setType(String t){
		this.type=t;
	}
	
	public void setDueDate(String d){
		this.due_date=d;
	}
	
	public void setDueTime(String t){
		this.due_time=t;
	}
	
	public void setReminder(int r){ 
		this.reminder = r;
	}
	
	public void setReminderTime(String t){
		this.reminder_time=t;
	}
	
	public void setId(int i){
		this.id=i;
	}
	
	public void setNotes(String n) {
		this.notes = n;
	}
	
}