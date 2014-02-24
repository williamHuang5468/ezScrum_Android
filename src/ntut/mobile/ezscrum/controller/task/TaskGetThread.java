package ntut.mobile.ezscrum.controller.task;

import ntut.mobile.ezscrum.controller.BaseThread;
import ntut.mobile.ezscrum.model.TaskObject;

public class TaskGetThread extends BaseThread {
	private String mProjectID, mTaskID;

	public TaskGetThread(String projectID, String taskID) {
		mProjectID = projectID;
		mTaskID = taskID;
	}
	
	@Override
	protected void handleWebServiceUrl() {

	}

	@Override
	protected void handleOperation() {

	}

	public TaskObject getTask() {
		return null;
	}
}
