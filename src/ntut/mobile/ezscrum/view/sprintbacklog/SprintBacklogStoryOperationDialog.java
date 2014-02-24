package ntut.mobile.ezscrum.view.sprintbacklog;

import ntut.mobile.ezscrum.controller.productbacklog.ProductBacklogItemManager;
import ntut.mobile.ezscrum.model.StoryObject;
import ntut.mobile.ezscrum.model.TaskObject;
import ntut.mobile.ezscrum.view.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

public class SprintBacklogStoryOperationDialog extends SprintBacklogDialog {
	private StoryObject mStory;
	private View mView;
	private Button mDropStoryButton, mCreateNewTaskButton, mAddExistedTaskButton;
	private ProductBacklogItemManager mProductBacklogItemManager;
	private SprintBacklogTaskDetailDialog mSprintBacklogTaskDetailDialog;
	
	View.OnClickListener mDropStoryConfirmListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
			builder.setTitle("Drop Story " + mStory.get_id());
			builder.setMessage(	"確定要把 Story " + mStory.get_id() + " 從 Sprint " + mStory.get_sprint() + " 中移除嗎？");
			builder.setNegativeButton("Cancel", null);
			builder.setPositiveButton("Drop Story", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					mProductBacklogItemManager.dropProductBacklogItem(mProjectID, mStory);
					cancel();
					update();
				}
			});
			builder.create().show();
		}
	};
	
	View.OnClickListener mCreateNewTaskListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			TaskObject task = new TaskObject();
			task.setStoryID(mStory.get_id());
			if (mSprintBacklogTaskDetailDialog == null) {
				int mode = SprintBacklogTaskDetailDialog.MODE_CREATE;
				mSprintBacklogTaskDetailDialog = new SprintBacklogTaskDetailDialog(mContext, task, mProjectID, mode);
				mSprintBacklogTaskDetailDialog.setUpdateListener(mUpdateListener);
			} else {
				mSprintBacklogTaskDetailDialog.setTask(task);
			}
			mSprintBacklogTaskDetailDialog.show();
			cancel();
		}
	};
	
	View.OnClickListener mAddExistedTaskListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent intent = new Intent();
			Bundle bundle = new Bundle();
			bundle.putString("projectID", mProjectID);
			bundle.putString("storyID", mStory.get_id());
			intent.putExtras(bundle);
			intent.setClass(mContext, SprintBacklogExistedTaskActivity.class);
			((Activity) mContext).startActivityForResult(intent, 0);
			cancel();
		}
	};
	
	protected SprintBacklogStoryOperationDialog(Context context, StoryObject story, String projecID) {
		super(context, projecID);
		mProductBacklogItemManager = new ProductBacklogItemManager();
		// Content view 的初始化
		LayoutInflater inflater = LayoutInflater.from(mContext);
		mView = inflater.inflate(R.layout.sprintbacklog_story_operation, null);
		mDropStoryButton = (Button) mView.findViewById(R.id.sprintbacklog_dropStoryButton);
		mCreateNewTaskButton = (Button) mView.findViewById(R.id.sprintbacklog_createNewTaskButton);
		mAddExistedTaskButton = (Button) mView.findViewById(R.id.sprintbacklog_addExistedTaskButton);
		// 設定針對 Story 操作的按鈕事件
		mDropStoryButton.setOnClickListener(mDropStoryConfirmListener);
		mCreateNewTaskButton.setOnClickListener(mCreateNewTaskListener);
		mAddExistedTaskButton.setOnClickListener(mAddExistedTaskListener);

		setView(mView);
		setStory(story);
	}
	
	public void setStory(StoryObject story) {
		mStory = story;
		mDropStoryButton.setText("Drop Story " + mStory.get_id());
		mCreateNewTaskButton.setText("Create a new task into story " + mStory.get_id());
		mAddExistedTaskButton.setText("Add an existed task into story " + mStory.get_id());
		setTitle(mStory.get_name());
	}
}
