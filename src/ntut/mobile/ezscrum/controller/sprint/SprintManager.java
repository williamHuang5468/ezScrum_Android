package ntut.mobile.ezscrum.controller.sprint;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import ntut.mobile.ezscrum.model.SprintObject;

public class SprintManager {
	private String mResponseString;

	public SprintManager() {

	}

	public List<SprintObject> getSprintAll(String projectID) {
		SprintGetAllThread sprintGetInProjectThread = new SprintGetAllThread(projectID);
		sprintGetInProjectThread.start();
		List<SprintObject> sprintList = new ArrayList<SprintObject>();
		try {
			sprintGetInProjectThread.join();
			sprintList = sprintGetInProjectThread.getSprintList();
		} catch (InterruptedException e) {
			Log.e("InterruptedException", "class: SprintManager, " + "method: getSprintAll");
			e.printStackTrace();
		}
		return sprintList;
	}

	public void creatSprint(String projectID, SprintObject sprint) {
		SprintCreateThread sprintCreateThread = new SprintCreateThread(projectID, sprint);
		sprintCreateThread.start();
		try {
			sprintCreateThread.join();
			mResponseString = sprintCreateThread.getResponseString();
		} catch (InterruptedException e) {
			Log.e("InterruptedException", "class: SprintManager, " + "method: creatSprint");
			e.printStackTrace();
		}
	}

	public void updateSprint(String projectID, SprintObject sprint) {
		SprintUpdateThread sprintUpdateThread = new SprintUpdateThread(projectID, sprint);
		sprintUpdateThread.start();
		try {
			sprintUpdateThread.join();
			mResponseString = sprintUpdateThread.getResponseString();
			Log.d("update sprint", mResponseString);
		} catch (InterruptedException e) {
			Log.e("InterruptedException", "class: SprintManager, " + "method: updateSprint");
			e.printStackTrace();
		}
	}

	public void deleteSprint(String projectID, String sprintID) {
		SprintDeleteThread sprintDeleteThread = new SprintDeleteThread(projectID, sprintID);
		sprintDeleteThread.start();
		try {
			sprintDeleteThread.join();
			mResponseString = sprintDeleteThread.getResponseString();
			Log.d("delete sprint", mResponseString);
		} catch (InterruptedException e) {
			Log.e("InterruptedException", "class: SprintManager, " + "method: deleteSprint");
			e.printStackTrace();
		}
	}
	
	static public SprintObject getSprintWithItem(String projectID, String sprintID) {
		SprintGetWithItemThread thread = new SprintGetWithItemThread(projectID, sprintID);
		thread.start();
		try {
			thread.join();
		} catch (InterruptedException e) {
			Log.e("InterruptedException", "class: SprintManager, " + "method: getSprintWithItem");
			e.printStackTrace();
		}
		return thread.getSprint();
	}

	public String getResponseString() {
		return mResponseString;
	}
}
