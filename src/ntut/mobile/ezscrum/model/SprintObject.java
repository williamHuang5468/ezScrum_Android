package ntut.mobile.ezscrum.model;

import java.util.ArrayList;
import java.util.List;

public class SprintObject {
	private String id = "";
	private String sprintGoal = "";
	private String startDate = "";
	private String demoDate = "";
	private String endDate = "";
	private String interval = "";
	private String focusFactor = "100"; // 預設值為 100 
	private String members = "";
	private String hoursCanCommit = "";
	private String notes = "";
	private String demoPlace = "";
	private String actualCost = "";
	private List<StoryObject> storyList = new ArrayList<StoryObject>();	
	
	public void setSprintID(String sprintID) {
		this.id = sprintID;
	}
	public String getSprintID() {
		return id;
	}
	public void setSprintGoal(String sprintGoal) {
		this.sprintGoal = sprintGoal;
	}
	public String getSprintGoal() {
		return sprintGoal;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setDemoDate(String demoDate) {
		this.demoDate = demoDate;
	}
	public String getDemoDate() {
		return demoDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public void setInterval(String interval) {
		this.interval = interval;
	}
	public String getInterval() {
		return interval;
	}
	public void setHoursCanCommit(String hoursCanCommit) {
		this.hoursCanCommit = hoursCanCommit;
	}
	public String getHoursCanCommit() {
		return hoursCanCommit;
	}
	public void setMembers(String members) {
		this.members = members;
	}
	public String getMembers() {
		return members;
	}
	public void setDemoPlace(String demoPlace) {
		this.demoPlace = demoPlace;
	}
	public String getDemoPlace() {
		return demoPlace;
	}
	public String getFocusFactor() {
		return focusFactor;
	}
	public void setFocusFactor(String focusFactor) {
		this.focusFactor = focusFactor;
	}
	public List<StoryObject> getStoryList() {
		return storyList;
	}
	public void setStoryList(List<StoryObject> storyList) {
		this.storyList = storyList;
	}
	public void addStory(StoryObject story) {
		this.storyList.add(story);
	}
	public String getNotes() {
		return notes;
	}
	public void setNotes(String notes) {
		this.notes = notes;
	}
	public String getActualCost() {
		return actualCost;
	}
	public void setActualCost(String actualCost) {
		this.actualCost = actualCost;
	}
	public String toString() {
		String sprint = "id :" + id + 
				", startDate :" + startDate +
				", interval :" + interval +
				", members :" + members +
				", focusFactor :" + focusFactor +
				", hoursCanCommit :" + hoursCanCommit +
				", goal :" + sprintGoal +
				", demoDate :" + demoDate +
				", notes :" + notes +
				", demoPlace :" + demoPlace +
				", endDate :" + endDate +
				", actualCost :" + actualCost;
		for (StoryObject story : storyList)
			sprint += "\n" + story.toString();
		return sprint;
	}
}
