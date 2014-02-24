package ntut.mobile.ezscrum.controller.story;

import java.util.List;

import ntut.mobile.ezscrum.controller.BaseThread;
import ntut.mobile.ezscrum.model.StoryObject;

public class StoryGetAllInProjectThread extends BaseThread {
	private String mProjectID;

	public StoryGetAllInProjectThread(String projectID) {
		mProjectID = projectID;
	}
	
	@Override
	protected void handleWebServiceUrl() {

	}

	@Override
	protected void handleOperation() {

	}

	public List<StoryObject> getAllStoryInProject() {
		return null;
	}
}
