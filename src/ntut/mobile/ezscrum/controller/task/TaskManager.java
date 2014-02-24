package ntut.mobile.ezscrum.controller.task;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;
import ntut.mobile.ezscrum.model.TaskObject;

public class TaskManager {

	public TaskManager(){}
	
	public TaskObject getTask(String projectID, String taskID) {
		TaskGetThread taskGetThread = new TaskGetThread(projectID, taskID);
		return taskGetThread.getTask();
	}
	
	public List<TaskObject> getAllTaskInSprint(String projectID, String sprintID) {
		TaskGetAllInSprintThread taksGetAllInStoryThread = new TaskGetAllInSprintThread();
		return taksGetAllInStoryThread.getAllTaskInSprint();
	}
	
	static public List<TaskObject> getExistedTask(String projectID) {
		List<TaskObject> existedTaskList = new ArrayList<TaskObject>();
		TaskGetExistedListThread thread = new TaskGetExistedListThread(projectID);
		thread.start();
		try {
			thread.join();
			existedTaskList = thread.getExistedTaskList();
		} catch (Exception e) {
			Log.e("InterruptedException", "class: TaskManager, method: getExistedTask");
			e.printStackTrace();
		}
		return existedTaskList;
	}
	
	static public String createTask(String projectID, String storyID, TaskObject task) {
		String newTaskID = "";
		TaskCreateThread taskCreateThread = new TaskCreateThread(projectID, storyID, task);
		taskCreateThread.start();
		try {
			taskCreateThread.join();
			newTaskID = taskCreateThread.getNewTaskID(); 
		} catch (Exception e) {
			Log.e("InterruptedException", "class: TaskManager, method: createTask");
			e.printStackTrace();
		}
		return newTaskID;
	}
	
	public void deleteTask(String projectID, String taskID, String storyID) {
		TaskDeleteThread taskDeleteThread = new TaskDeleteThread(projectID, taskID, storyID);
		taskDeleteThread.start();
		try {
			taskDeleteThread.join();
		} catch (Exception e) {
			Log.e("InterruptedException", "class: TaskManager, method: deleteTask");
			e.printStackTrace();
		}
	}
	
	public void dropTask(String projectID, String taskID, String storyID) {
		TaskDropThread taskDropThread = new TaskDropThread(projectID, taskID, storyID);
		taskDropThread.start();
		try {
			taskDropThread.join();
		} catch (Exception e) {
			Log.e("InterruptedException", "class: TaskManager, method: dropTask");
			e.printStackTrace();
		}
	}
	
	static public boolean updateTask(String projectID, TaskObject task) {
		TaskUpdateThread thread = new TaskUpdateThread(projectID, task);
		thread.start();
		try {
			thread.join();
		} catch (Exception e) {
			Log.e("InterruptedException", "class: TaskManager, method: updateTask");
			e.printStackTrace();
		}
		return thread.updateTask();
	}

	public void changeTaskStatus(String projectID, String taskID, String status) {
		TaskChangeStatusThread taskChangeStatusThread = new TaskChangeStatusThread(projectID, taskID, status);
		taskChangeStatusThread.changeTaskStatus();
	}
}
