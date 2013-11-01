package umn.cs5115.kiwi;

public class Course {

	// private variables
	private String courseTitle;
	private String courseDesignation;
	private String startTime;
	private String endTime;
	private String location;
	private String startDate;
	private String endDate;
	private String rRule;
	private String notes;

	// empty constructor
	public Course() {
	}

	// constructor
	public Course(String title, String cd, String sTime, String eTime,
			String cLocation, String sDate, String eDate, String cRRule,
			String n) {
		this.courseTitle = title;
		this.courseDesignation = cd;
		this.startTime = sTime;
		this.endTime = eTime;
		this.location = cLocation;
		this.startDate = sDate;
		this.endDate = eDate;
		this.rRule = cRRule;
		this.notes = n;
	}

	// getters for each of the Course elements
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

	// setters for each of the Course elements
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
}
