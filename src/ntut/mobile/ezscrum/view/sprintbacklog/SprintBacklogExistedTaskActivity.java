package ntut.mobile.ezscrum.view.sprintbacklog;

import java.util.List;

import ntut.mobile.ezscrum.controller.story.StoryManager;
import ntut.mobile.ezscrum.controller.task.TaskManager;
import ntut.mobile.ezscrum.model.TaskObject;
import ntut.mobile.ezscrum.view.sprintbacklog.SprintBacklogExistedTaskList.OnCancelClickListener;
import ntut.mobile.ezscrum.view.sprintbacklog.SprintBacklogExistedTaskList.OnOKClickListener;
import android.app.Activity;
import android.os.Bundle;

public class SprintBacklogExistedTaskActivity extends Activity {
	SprintBacklogExistedTaskList mExistedTaskList;
	private String mProjectID, mStoryID;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle bundle = getIntent().getExtras();
		mProjectID = bundle.getString("projectID");
		mStoryID = bundle.getString("storyID");
		
		mExistedTaskList = new SprintBacklogExistedTaskList(this, getExistedTask());
		mExistedTaskList.setOnOkClickListener(new OnOKClickListener() {
			@Override
			public void onClick(List<String> selectedTaskIDList) {
				StoryManager.addExistedTask(mProjectID, mStoryID, selectedTaskIDList);
				finish();
			}
		});
		mExistedTaskList.setOnCancelClickListener(new OnCancelClickListener() {
			@Override
			public void onClick() {
				finish();
			}
		});
		setContentView(mExistedTaskList.getView());
	}
	
	private List<TaskObject> getExistedTask() {
		return TaskManager.getExistedTask(mProjectID);
	}
}
