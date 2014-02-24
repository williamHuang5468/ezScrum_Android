package ntut.mobile.ezscrum.controller.story;

import ntut.mobile.ezscrum.controller.BaseThread;
import ntut.mobile.ezscrum.model.StoryObject;

public class StoryGetThread extends BaseThread {
	private String mProjectID, mStoryID;

	public StoryGetThread(String projectID, String storyID) {
		mProjectID = projectID;
		mStoryID = storyID;
	}
	
	@Override
	protected void handleWebServiceUrl() {

	}

	@Override
	protected void handleOperation() {

	}

	public StoryObject getStory() {
		return null;
	}
}
