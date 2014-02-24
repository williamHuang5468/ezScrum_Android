package ntut.mobile.ezscrum.controller.sprint;

import ntut.mobile.ezscrum.controller.BaseThread;
import ntut.mobile.ezscrum.model.SprintObject;

public class SprintGetThread extends BaseThread {
	private String mProjectID, mSprintID;

	public SprintGetThread(String projectID, String sprintID) {
		mProjectID = projectID;
		mSprintID = sprintID;
	}
	
	@Override
	protected void handleWebServiceUrl() {

	}

	@Override
	protected void handleOperation() {

	}

	public SprintObject getSprint() {
		return null;
	}
}
