package ntut.mobile.ezscrum.controller.story;

import java.util.List;

import ntut.mobile.ezscrum.model.StoryObject;
import android.util.Log;

public class StoryManager {

	public StoryManager(){}
	
	public StoryObject getStory(String projectID, String storyID) {
		StoryGetThread storyGetThread = new StoryGetThread(projectID, storyID);
		return storyGetThread.getStory();
	}
	
	public List<StoryObject> getAllStoryInSprint(String projectID, String sprintID) {
		StoryGetAllInSprintThread storyGetAllInSprintThread = new StoryGetAllInSprintThread(projectID, sprintID);
		return storyGetAllInSprintThread.getAllStoryInSprint();
	}
	
	public List<StoryObject> getAllStoryInProject(String projectID) {
		StoryGetAllInProjectThread storyGetAllInProjectThread = new StoryGetAllInProjectThread(projectID);
		return storyGetAllInProjectThread.getAllStoryInProject();
	}
	
	public int createStory(String projectID, StoryObject story) {
		StoryCreateThread storyCreateThread = new StoryCreateThread(projectID, story);
		return storyCreateThread.getCreateID();
	}
	
	public void deleteStory(String projectID, String storyID) {
		StoryDeleteThread storyDeleteThread = new StoryDeleteThread(projectID, storyID);
		storyDeleteThread.deleteStory();
	}
	
	public void updateStory(String projectID, StoryObject story) {
		StoryUpdateThread storyUpdateThread = new StoryUpdateThread(projectID, story);
		storyUpdateThread.updateStory();
	}
	
	public void changeStoryStatus(String projectID, String storyID, String status) {
		StoryChangeStatusThread storyChangeStatusThread = new StoryChangeStatusThread(projectID, storyID, status);
		storyChangeStatusThread.changeStoryStatus();
	}
	
	static public void addExistedTask(String projectID, String storyID, List<String> taskIDs) {
		StoryAddExistedTaskThread thread = new StoryAddExistedTaskThread(projectID, storyID, taskIDs);
		thread.start();
		try {
			thread.join();
		} catch (InterruptedException e) {
			Log.e("InterruptedException", "class: TaskManager, method: addExistedTask");
			e.printStackTrace();
		}
	}
}
