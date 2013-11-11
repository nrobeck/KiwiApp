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
	private String DELIMITER = "\n";
	private String textbooks;

	// empty constructor
	public Course() {
	}

	// constructor
	public Course(String title, String cd, String sTime, String eTime,
			String cLocation, String sDate, String eDate, String cRRule,
			String n, String tb) {
		this.courseTitle = title;
		this.courseDesignation = cd;
		this.startTime = sTime;
		this.endTime = eTime;
		this.location = cLocation;
		this.startDate = sDate;
		this.endDate = eDate;
		this.rRule = cRRule;
		this.notes = n;
		this.textbooks = tb;
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
	
	//read textbooks from the course as a string
	public String getTextbooksString() {
		return this.textbooks;
	}
	
	//read textbooks from the course as an array
	public String[] getTextbooksArray(){
		String deliminator = "[" + DELIMITER + "]+";//sets the DELIMITER character as a deliminator and treats multiple DELIMITER characters in a row as one deliminator
		String[] textbookArray = this.textbooks.split(deliminator);//split the textbooks  string into an array of the individual books
		return textbookArray;
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
	
	public void setTextbooks(String t) {
		this.textbooks = t;
	}
	
	
	//add a textbook to the course
	public void addTextbook(String textbook){
		this.textbooks = this.textbooks + textbook + DELIMITER;//add textbook to the end of the string
	}
	
	//remove textbook from the course
	public void removeTextbook(String textbook){
		String[] tb = getTextbooksArray();//get the array format of textbooks for the course
		String newTextbooks = DELIMITER;//new string to set textbooks to after removal of the textbook
	
		//loop through and add textbooks other than textbook to be removed to the new string
		int i;
		for(i = 0; i < tb.length; i++){
			if(!tb[i].equals(textbook)){
				newTextbooks = newTextbooks + tb[i] + DELIMITER;
			}
		}
	
		this.textbooks = newTextbooks;
	}
}
