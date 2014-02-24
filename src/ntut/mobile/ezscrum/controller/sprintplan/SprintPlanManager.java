package ntut.mobile.ezscrum.controller.sprintplan;

import ntut.mobile.ezscrum.controller.sprint.SprintManager;
import ntut.mobile.ezscrum.model.SprintObject;

public class SprintPlanManager {
	private String mResponseString;
	
	public SprintPlanManager() {}

	public void creatSprint(String projectID, SprintObject sprint) {
		SprintManager sprintManager = new SprintManager();
		sprintManager.creatSprint(projectID, sprint);
		mResponseString = sprintManager.getResponseString();
	}
	
	public void updateSprint(String projectID, SprintObject sprint) {
		SprintManager sprintManager = new SprintManager();
		sprintManager.updateSprint(projectID, sprint);
		mResponseString = sprintManager.getResponseString();
	}
	
	public void deleteSprint(String projectID, String sprintID) {
		SprintManager sprintManager = new SprintManager();
		sprintManager.deleteSprint(projectID, sprintID);
		mResponseString = sprintManager.getResponseString();
	}

	public String getResponseString() {
		return mResponseString;
	}
}
