package ntut.mobile.ezscrum.controller.story;

import java.util.List;

import ntut.mobile.ezscrum.controller.BaseThread;
import ntut.mobile.ezscrum.model.StoryObject;

public class StoryGetAllInSprintThread extends BaseThread {
	private String mProjectID, mSprintID;

	public StoryGetAllInSprintThread(String projectID, String sprintID) {
		mProjectID = projectID;
		mSprintID = sprintID;
	}
	
	@Override
	protected void handleWebServiceUrl() {

	}

	@Override
	protected void handleOperation() {

	}

	public List<StoryObject> getAllStoryInSprint() {
		return null;
	}
}
