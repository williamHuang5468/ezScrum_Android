package ntut.mobile.ezscrum.view.sprintbacklog;

import java.util.ArrayList;
import java.util.List;

import ntut.mobile.ezscrum.model.TaskObject;
import ntut.mobile.ezscrum.view.R;
import ntut.mobile.ezscrum.view.sprintbacklog.SprintBacklogExistedTaskItem.OnItemClickListener;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.ScrollView;


public class SprintBacklogExistedTaskList {
	private Context mContext;
	private RelativeLayout mView;
	private List<TaskObject> mTaskList;
	private List<String> mSelectedTaskIDList;
	private Button mOKButton;
	private OnOKClickListener mOnOkClickListener;
	private OnCancelClickListener mOnCancelClickListener;

	interface OnOKClickListener {
		public void onClick(List<String> selectedTaskIDList);
	}
	
	interface OnCancelClickListener {
		public void onClick();
	}
	
	private OnItemClickListener mOnItemClickListener = new OnItemClickListener() {
		@Override
		public void onClick(String taskID) {
			if (mSelectedTaskIDList.contains(taskID)) {
				mSelectedTaskIDList.remove(taskID);
			} else {
				mSelectedTaskIDList.add(taskID);
			}
			// OK 按鈕上的選取數量計算
			int count = mSelectedTaskIDList.size();
			if (count <= 0)
				mOKButton.setText("OK");
			else
				mOKButton.setText("OK  ( " + count + " )");
		}
	};
	
	private OnClickListener mOnOKClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			mOnOkClickListener.onClick(mSelectedTaskIDList);
		}
	};
	
	private OnClickListener mOnCancelClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			mOnCancelClickListener.onClick();
		}
	};
	
	public SprintBacklogExistedTaskList(Context context, List<TaskObject> taskList) {
		mContext = context;
		mTaskList = taskList;
		mSelectedTaskIDList = new ArrayList<String>();
		initView();
	}
	
	private void initView() {
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		mView = new RelativeLayout(mContext);
		mView.setLayoutParams(params);

		// 下方的確定按鈕
		View buttonList = View.inflate(mContext, R.layout.button_list, null);
		mOKButton = (Button) buttonList.findViewById(R.id.buttonOK);
		mOKButton.setOnClickListener(mOnOKClick);
		((Button) buttonList.findViewById(R.id.buttonCancel)).setOnClickListener(mOnCancelClick);
		RelativeLayout.LayoutParams buttonListParams = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		buttonListParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
		mView.addView(buttonList, buttonListParams);
		
		// task list content
		LinearLayout taskListLayout = new LinearLayout(mContext);
		taskListLayout.setLayoutParams(params);
		taskListLayout.setOrientation(LinearLayout.VERTICAL);
		LayoutParams taskParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		for (TaskObject task : mTaskList) {
			SprintBacklogExistedTaskItem taskItem = new SprintBacklogExistedTaskItem(mContext, task);
			taskItem.setOnItemClickListener(mOnItemClickListener);
			taskListLayout.addView(taskItem.getView(), taskParams);
		}

		// task list scroll view
		ScrollView scroll = new ScrollView(mContext);
		scroll.addView(taskListLayout);
		RelativeLayout.LayoutParams scrollParams = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		scrollParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
		scrollParams.addRule(RelativeLayout.ABOVE, buttonList.getId());
		mView.addView(scroll, scrollParams);
	}
	
	public View getView() {
		return mView;
	}
	
	public void setOnOkClickListener(OnOKClickListener listener) {
		mOnOkClickListener = listener;
	}
	
	public void setOnCancelClickListener(OnCancelClickListener listener) {
		mOnCancelClickListener = listener;
	}
}
