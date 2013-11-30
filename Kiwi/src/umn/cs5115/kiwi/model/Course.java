package umn.cs5115.kiwi.model;

import android.text.TextUtils;

public class Course {

	// private variables
	private int id;
	private String courseTitle;
	private String courseDesignation;
	private String startTime;
	private String endTime;
	private String location;
	private String startDate;
	private String endDate;
	private String rRule;
	private String notes;
	public static final String TEXTBOOK_DELIMITER = "\n";
	private String textbooks;
	private String color;

	// empty constructor
	public Course() {
	}

	// constructor
	public Course(int id, String title, String courseDesignation, String startTime, String endTime,
			String cLocation, String sDate, String eDate, String cRRule,
			String n, String tb, String color) {
		this.id = id;
		this.courseTitle = title;
		this.courseDesignation = courseDesignation;
		this.startTime = startTime;
		this.endTime = endTime;
		this.location = cLocation;
		this.startDate = sDate;
		this.endDate = eDate;
		this.rRule = cRRule;
		this.notes = n;
		this.textbooks = tb;
		this.setColor(color);
	}

	// getters for each of the Course elements
	public int getId() {
		return this.id;
	}
	
	public String getCourseTitle() {
		return this.courseTitle;
	}

	public String getCourseDesignation() {
		return this.courseDesignation;
	}

	public String getStartTime() {
		return this.startTime;
	}

	public String getEndTime() {
		return this.endTime;
	}

	public String getLocation() {
		return this.location;
	}

	public String getStartDate() {
		return this.startDate;
	}

	public String getEndDate() {
		return this.endDate;
	}

	public String getRRule() {
		return this.rRule;
	}

	public String getNotes() {
		return this.notes;
	}
	
	//read textbooks from the course as a string
	public String getTextbooksString() {
		return this.textbooks;
	}
	
	//read textbooks from the course as an array
	public String[] getTextbooksArray() {
	    if (TextUtils.isEmpty(this.textbooks)) {
	    	// 'textbooks' could be null, or just an empty string.
	        return new String[0];
	    }
		String deliminator = "[" + TEXTBOOK_DELIMITER + "]+";//sets the DELIMITER character as a deliminator and treats multiple DELIMITER characters in a row as one deliminator
		String[] textbookArray = this.textbooks.split(deliminator);//split the textbooks  string into an array of the individual books
		return textbookArray;
	}
	
	// setters for each of the Course elements	
	public void setId(int id) {
		this.id = id;
	}
	
	public void setCourseTitle(String t) {
		this.courseTitle = t;
	}

	public void setCourseDesignation(String cd) {
		this.courseDesignation = cd;
	}

	public void setStartTime(String st) {
		this.startTime = st;
	}

	public void setEndTime(String et) {
		this.endTime = et;
	}

	public void setLocation(String l) {
		this.location = l;
	}

	public void setStartDate(String sd) {
		this.startDate = sd;
	}

	public void setEndDate(String ed) {
		this.endDate = ed;
	}

	public void setRRule(String rr) {
		this.rRule = rr;
	}

	public void setNotes(String n) {
		this.notes = n;
	}
	
	public void setTextbooks(String t) {
		this.textbooks = t;
	}
	
	
	//add a textbook to the course
	public void addTextbook(String textbook){
		if (this.textbooks == null) {
			this.textbooks = "";
		}
		this.textbooks += textbook + TEXTBOOK_DELIMITER;//add textbook to the end of the string
	}
	
	//remove textbook from the course
	public void removeTextbook(String textbook){
		String[] tb = getTextbooksArray();//get the array format of textbooks for the course
		String newTextbooks = TEXTBOOK_DELIMITER;//new string to set textbooks to after removal of the textbook
	
		//loop through and add textbooks other than textbook to be removed to the new string
		int i;
		for(i = 0; i < tb.length; i++){
			if(!tb[i].equals(textbook)){
				newTextbooks = newTextbooks + tb[i] + TEXTBOOK_DELIMITER;
			}
		}
	
		this.textbooks = newTextbooks;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}
	
	@Override
	public String toString() {
	    return String.format("<Course@%d %d %s>", hashCode(), this.id, this.courseTitle);
	}
}
