package ntut.mobile.ezscrum.controller.story;

import ntut.mobile.ezscrum.controller.BaseThread;

public class StoryChangeStatusThread extends BaseThread {
	private String mProjectID, mStoryID, mStatus;

	public StoryChangeStatusThread(String projectID, String storyID, String status) {
		mProjectID = projectID;
		mStoryID = storyID;
		mStatus = status;
	}
	
	@Override
	protected void handleWebServiceUrl() {

	}

	@Override
	protected void handleOperation() {

	}
	
	public void changeStoryStatus() {
		
	}
}
