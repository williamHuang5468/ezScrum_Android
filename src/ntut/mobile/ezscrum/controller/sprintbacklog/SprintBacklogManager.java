package ntut.mobile.ezscrum.controller.sprintbacklog;

import java.util.ArrayList;
import java.util.List;

import ntut.mobile.ezscrum.controller.sprint.SprintManager;
import ntut.mobile.ezscrum.model.SprintObject;
import ntut.mobile.ezscrum.model.StoryObject;
import android.util.Log;

public class SprintBacklogManager {
	/****
	 * 取得所有sprint plan information
	 * @param userName
	 * @param password
	 * @param projectID
	 * @return
	 */
	public List<SprintObject> getAllSprintListInformation(String projectID) {
		SprintManager sprintManager = new SprintManager();
		return sprintManager.getSprintAll(projectID);
	}
	
	/**
	 * 取得專案正在進行的sprint information
	 * @param userName
	 * @param password
	 * @param projectID
	 * @return
	 */
	public SprintObject getTheCurrentSprintInformation(String projectID) {
		SprintBacklogGetCurrentSprintInformationThread getCurrentSprintInformationThread = new SprintBacklogGetCurrentSprintInformationThread(projectID);
		SprintObject currentSprint = null;
		getCurrentSprintInformationThread.start();
		try {
			getCurrentSprintInformationThread.join();
			currentSprint = getCurrentSprintInformationThread.getCurrentSprint();
		} catch (InterruptedException e) {
			Log.e("InterruptedException", "class: SprintBacklogManager, " + "method: getTheCurrentSprintInformation, " + e.toString());
			e.printStackTrace();
		}
		return currentSprint;
	}
	
	public List<StoryObject> getStoryList(String projectID, String sprintID) {
		SprintBacklogGetStoryIDListThread getStoryIDListThread = new SprintBacklogGetStoryIDListThread(projectID, sprintID);
		List<String> storyIDList = new ArrayList<String>();
		getStoryIDListThread.start();
		try {
			getStoryIDListThread.join();
			storyIDList = getStoryIDListThread.getSprintStoryIDList();
		} catch (InterruptedException e) {
			Log.e("InterruptedException", "class: SprintBacklogManager, " + "method: getStoryIDList, " + e.toString());
			e.printStackTrace();
		}
		return null;
	}
	
	/***
	 * 取得特定 Sprint 內的 story id list
	 * @param projectID
	 * @param userName
	 * @param password
	 * @param sprintID
	 */
	public List<String> getStoryIDList(String projectID, String sprintID) {
		SprintBacklogGetStoryIDListThread getStoryIDListThread = new SprintBacklogGetStoryIDListThread(projectID, sprintID);
		List<String> storyIDList = new ArrayList<String>();
		getStoryIDListThread.start();
		try {
			getStoryIDListThread.join();
			storyIDList = getStoryIDListThread.getSprintStoryIDList();
		} catch (InterruptedException e) {
			Log.e("InterruptedException", "class: SprintBacklogManager, " + "method: getStoryIDList, " + e.toString());
			e.printStackTrace();
		}
		return storyIDList;
	}
	
	/***
	 * 取得特定 Sprint 某 Story 的 task id list
	 * @param projectID
	 * @param userName
	 * @param password
	 * @param sprintID
	 * @param storyID
	 */
	public List<String> getTaskIDList(String projectID, String sprintID, String storyID) {
		SprintBacklogGetTaskIDListInStoryThread getTaskIDListInStoryThread = new SprintBacklogGetTaskIDListInStoryThread(projectID, sprintID, storyID);
		List<String> taskIDList = new ArrayList<String>();
		getTaskIDListInStoryThread.start();
		try {
			getTaskIDListInStoryThread.join();
			taskIDList = getTaskIDListInStoryThread.getTaskIDList();
		} catch (InterruptedException e) {
			Log.e("InterruptedException", "class: SprintBacklogManager, " + "method: getTaskIDList, " + e.toString());
			e.printStackTrace();
		}
		return taskIDList;
	}
	
	/***
	 * 取得最新Sprint中story id list
	 * @param userName
	 * @param password
	 * @param projectID
	 */
	public String getTheLatestSprintInformation(String projectID) {
		String responseString = null;
		SprintBacklogGetTheLatestThread getTheLatestThread = new SprintBacklogGetTheLatestThread(projectID);
		getTheLatestThread.start();
		try {
			getTheLatestThread.join();
			responseString = getTheLatestThread.getResponseString();
			Log.d("get the latest thread", responseString);
		} catch (InterruptedException e) {
			Log.e("InterruptedException", "class: SprintBacklogManager, " + "method: getTheLatestSprintInformation, " + e.toString());
			e.printStackTrace();
		}
		return responseString;
	}
}
