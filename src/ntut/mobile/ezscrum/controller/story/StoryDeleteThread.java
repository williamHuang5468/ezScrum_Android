package ntut.mobile.ezscrum.controller.story;

import ntut.mobile.ezscrum.controller.BaseThread;

public class StoryDeleteThread extends BaseThread {
	private String mProjectID, mStoryID;

	public StoryDeleteThread(String projectID, String storyID) {
		mProjectID = projectID;
		mStoryID = storyID;
	}
	
	@Override
	protected void handleWebServiceUrl() {

	}

	@Override
	protected void handleOperation() {

	}

	public void deleteStory() {
		
	}
}
