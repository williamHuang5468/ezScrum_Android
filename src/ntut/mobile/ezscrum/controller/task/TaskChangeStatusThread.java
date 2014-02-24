package ntut.mobile.ezscrum.controller.task;

import ntut.mobile.ezscrum.controller.BaseThread;

public class TaskChangeStatusThread extends BaseThread {
	private String mProjectID, mTaskID, mStatus;

	public TaskChangeStatusThread(String projectID, String taskID, String status) {
		mProjectID = projectID;
		mTaskID = taskID;
		mStatus = status;
	}
	
	@Override
	protected void handleWebServiceUrl() {

	}

	@Override
	protected void handleOperation() {

	}
	
	public void changeTaskStatus() {
		
	}
}
