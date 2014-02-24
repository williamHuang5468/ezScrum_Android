package ntut.mobile.ezscrum.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Task Object
 */
public class TaskObject implements Serializable {
	private static final long serialVersionUID = 1L;
	private String id = "";
	private String name = "";
	private String handler = "";
	private List<String> parterners = new ArrayList<String>();
	private String estimation = "";
	private String status = "";
	private String remainHour = "";
	private String actualHour = "";
	private String notes = "";
	private String storyID = "";
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getEstimation() {
		return estimation;
	}
	
	public void setEstimation(String estimation) {
		this.estimation = estimation;
	}
	
	public String getHandler() {
		return handler;
	}
	
	public void setHandler(String handler) {
		this.handler = handler;
	}
	
	public List<String> getParterners() {
		return parterners;
	}
	
	public void setParterners(List<String> parterners) {
		this.parterners = parterners;
	}
	
	public String getStatus() {
		return status;
	}
	
	public void setStatus(String status) {
		this.status = status;
	}
	
	public String getRemainHour() {
		return remainHour;
	}
	
	public void setRemainHour(String remainHour) {
		this.remainHour = remainHour;
	}
	
	public String getActualHour() {
		return actualHour;
	}
	
	public void setActualHour(String actualHour) {
		this.actualHour = actualHour;
	}
	
	public String getNotes() {
		return notes;
	}
	
	public void setNotes(String notes) {
		this.notes = notes;
	}
	
	public String getStoryID() {
		return storyID;
	}

	public void setStoryID(String storyID) {
		this.storyID = storyID;
	}

	public String toString() {
		String str = "id: " +  id + 
				", name: " + name + 
				", eatimation: " + estimation + 
				", handler: " + handler + 
				", parterners: " + parterners.toString() + 
				", status: " + status + 
				", remainHours: " + remainHour + 
				", actualHour: " + actualHour + 
				", storyID: " + storyID + 
				", notes: " + notes;
		return str;
	}
}
