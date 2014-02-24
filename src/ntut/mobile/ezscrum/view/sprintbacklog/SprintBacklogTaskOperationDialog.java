package ntut.mobile.ezscrum.view.sprintbacklog;

import ntut.mobile.ezscrum.controller.task.TaskManager;
import ntut.mobile.ezscrum.model.TaskObject;
import ntut.mobile.ezscrum.view.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

public class SprintBacklogTaskOperationDialog extends SprintBacklogDialog {
	TaskObject mTask;
	View mView;
	Button mDropTaskButton, mDeleteTaskButton;
	TaskManager mTaskManager;

	View.OnClickListener mDropTaskListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			mTaskManager.dropTask(mProjectID, mTask.getId(), mTask.getStoryID());
			cancel();
			update();
		}
	};
	
	View.OnClickListener mDeleteTaskListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			mTaskManager.deleteTask(mProjectID, mTask.getId(), mTask.getStoryID());
			cancel();
			update();
		}
	};
	
	public SprintBacklogTaskOperationDialog(Context context, TaskObject task, String projecID) {
		super(context, projecID);
		// Content view 的初始化
		LayoutInflater inflater = LayoutInflater.from(mContext);
		mView = inflater.inflate(R.layout.sprintbacklog_task_operation, null);
		mDropTaskButton = (Button) mView.findViewById(R.id.sprintbacklog_dropTaskButton);
		mDeleteTaskButton = (Button) mView.findViewById(R.id.sprintbacklog_deleteTaskButton);
		mDropTaskButton.setOnClickListener(mDropTaskListener);
		mDeleteTaskButton.setOnClickListener(mDeleteTaskListener);
		mTaskManager = new TaskManager();
		
		setView(mView);
		setTask(task);
	}
	
	public void setTask(TaskObject task) {
		mTask = task;
		setTitle(mTask.getName());
		mDropTaskButton.setText("Drop task " + mTask.getId());
		mDeleteTaskButton.setText("Delete task " + mTask.getId());
	}
}
