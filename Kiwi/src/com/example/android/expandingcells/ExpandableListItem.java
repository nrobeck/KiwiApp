/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.expandingcells;

import java.util.Date;

/**
 * This custom object is used to populate the list adapter. It contains a reference
 * to an image, title, and the extra text to be displayed. Furthermore, it keeps track
 * of the current state (collapsed/expanded) of the corresponding item in the list,
 * as well as store the height of the cell in its collapsed state.
 */
public class ExpandableListItem implements OnSizeChangedListener {

    private String mTitle;
    private String mText;
    private boolean mIsExpanded;
    private int mImgResource;
    private int mCollapsedHeight;
    private int mExpandedHeight;
    
    //CSCI 5115 Added
    private String courseName;
    private Date assignmentDate;
    private String assignmentName;
    private String assignmentType;
    private String[] assignmentTextbooks;
    private String assignmentNotes;

    public ExpandableListItem(String title, int imgResource, int collapsedHeight, String text) {
        mTitle = title;
        mImgResource = imgResource;
        mCollapsedHeight = collapsedHeight;
        mIsExpanded = false;
        mText = text;
        mExpandedHeight = -1;
    }
    
    //CSCI 5115 Assignment Tile Constructor
    public ExpandableListItem(String course, Date date, String name, String type, String[] textbooks, String notes, int collapsedHeight) {
    	this.courseName = course;
    	this.assignmentDate = date;
    	this.assignmentName = name;
    	this.assignmentType = type;
    	this.assignmentTextbooks = textbooks;
    	this.assignmentNotes = notes;
    	
    	this.mIsExpanded = false;
    	this.mExpandedHeight = -1;
    	this.mCollapsedHeight = collapsedHeight;
    }

    public boolean isExpanded() {
        return mIsExpanded;
    }

    public void setExpanded(boolean isExpanded) {
        mIsExpanded = isExpanded;
    }

    public String getTitle() {
        return mTitle;
    }

    public int getImgResource() {
        return mImgResource;
    }

    public int getCollapsedHeight() {
        return mCollapsedHeight;
    }

    public void setCollapsedHeight(int collapsedHeight) {
        mCollapsedHeight = collapsedHeight;
    }

    public String getText() {
        return mText;
    }

    public void setText(String text) {
        mText = text;
    }

    public int getExpandedHeight() {
        return mExpandedHeight;
    }

    public void setExpandedHeight(int expandedHeight) {
        mExpandedHeight = expandedHeight;
    }

    @Override
    public void onSizeChanged(int newHeight) {
        setExpandedHeight(newHeight);
    }

	public String getCourseName() {
		return courseName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	public Date getAssignmentDate() {
		return assignmentDate;
	}

	public void setAssignmentDate(Date assignmentDate) {
		this.assignmentDate = assignmentDate;
	}

	public String getAssignmentName() {
		return assignmentName;
	}

	public void setAssignmentName(String assignmentName) {
		this.assignmentName = assignmentName;
	}

	public String getAssignmentType() {
		return assignmentType;
	}

	public void setAssignmentType(String assignmentType) {
		this.assignmentType = assignmentType;
	}

	public String[] getAssignmentTextbooks() {
		return assignmentTextbooks;
	}

	public void setAssignmentTextbooks(String[] assignmentTextbooks) {
		this.assignmentTextbooks = assignmentTextbooks;
	}

	public String getAssignmentNotes() {
		return assignmentNotes;
	}

	public void setAssignmentNotes(String assignmentNotes) {
		this.assignmentNotes = assignmentNotes;
	}
}
