package ntut.mobile.ezscrum.controller.story;

import ntut.mobile.ezscrum.controller.BaseThread;
import ntut.mobile.ezscrum.model.StoryObject;

public class StoryUpdateThread extends BaseThread {
	private String mProjectID;
	private StoryObject mStory;

	public StoryUpdateThread(String projectID, StoryObject story) {
		mProjectID = projectID;
		mStory = story;
	}
	
	@Override
	protected void handleWebServiceUrl() {

	}

	@Override
	protected void handleOperation() {

	}
	
	public void updateStory() {
		
	}
}
