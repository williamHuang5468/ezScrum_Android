package ntut.mobile.ezscrum.view.sprintbacklog;

import ntut.mobile.ezscrum.model.TaskObject;
import ntut.mobile.ezscrum.view.R;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class SprintBacklogExistedTaskItem {
	Context mContext;
	TaskObject mTask;
	View mView;
	boolean mIsCheck = false;
	ImageView mCheckImageView;
	OnItemClickListener mOnItemClickListener = null;
	
	interface OnItemClickListener {
		public void onClick(String taskID);
	}
	
	/**
	 * 當 view 被點到，要改變 item 的有沒有被選取的屬性
	 */
	private OnClickListener mOnClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			mIsCheck = !mIsCheck;
			setCheck(mIsCheck);
			if (mOnItemClickListener != null)
				mOnItemClickListener.onClick(mTask.getId());
		}
	};

	public SprintBacklogExistedTaskItem(Context context, TaskObject task) {
		mContext = context;
		mTask = task;
		initView();
		setCheck(false);
	}
	
	private void initView() {
		mView = View.inflate(mContext, R.layout.sprintbacklog_item_checkable, null);
		mCheckImageView = (ImageView) mView.findViewById(R.id.sprintBacklog_item_check);
		((TextView) mView.findViewById(R.id.sprintBacklog_storyID)).setText("Task " + mTask.getId());
		((TextView) mView.findViewById(R.id.sprintBacklog_name)).setText(mTask.getName());
		((TextView) mView.findViewById(R.id.sprintBacklog_importance)).setText("");
		((TextView) mView.findViewById(R.id.sprintBacklog_value)).setText("");
		((TextView) mView.findViewById(R.id.sprintBacklog_estimate)).setText(mTask.getEstimation());
		mView.setOnClickListener(mOnClickListener);
	}
	
	/**
	 * 被點到時要不要顯示加號的圖片
	 * @param check
	 */
	private void setCheck(boolean check) {
		mIsCheck = check;
		if (mIsCheck) {
			mCheckImageView.setVisibility(View.VISIBLE);
			mView.setBackgroundColor(mContext.getResources().getColor(R.color.item_task_background));
		} else {
			mCheckImageView.setVisibility(View.INVISIBLE);
			mView.setBackgroundColor(Color.argb(0, 0, 0, 0));
		}
	}
	
	public View getView() {
		return mView;
	}
	
	public void setOnItemClickListener(OnItemClickListener listener) {
		mOnItemClickListener = listener;
	}
}
