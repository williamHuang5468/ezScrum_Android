package ntut.mobile.ezscrum.controller.taskboard;

import ntut.mobile.ezscrum.controller.story.StoryManager;
import ntut.mobile.ezscrum.controller.task.TaskManager;
import ntut.mobile.ezscrum.model.StoryObject;
import ntut.mobile.ezscrum.model.TaskObject;

public class TaskBoardManager {
	
	public TaskBoardManager() {}

	public void updateStory(String projectID, StoryObject story) {
		StoryManager storyManager = new StoryManager();
		storyManager.updateStory(projectID, story);
	}
	
	public void updateTask(String projectID, TaskObject task) {
	}
	
	public void changeStoryStatus(String projectID, String storyID, String status) {
		StoryManager storyManager = new StoryManager();
		storyManager.changeStoryStatus(projectID, storyID, status);
	}
	
	public void changeTaskStatus(String projectID, String taskID, String status) {
		TaskManager taskManager = new TaskManager();
		taskManager.changeTaskStatus(projectID, taskID, status);
	}
}
