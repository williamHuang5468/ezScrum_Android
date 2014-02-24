package ntut.mobile.ezscrum.view.sprintbacklog;

import ntut.mobile.ezscrum.controller.task.TaskManager;
import ntut.mobile.ezscrum.model.TaskObject;
import ntut.mobile.ezscrum.view.R;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class SprintBacklogTaskDetailDialog extends SprintBacklogDialog {
	public static final int MODE_CREATE = 1;
	public static final int MODE_UPDATE = 2;
	private int mCurrentMode = MODE_UPDATE;
	private TaskObject mTask;
	private View mView;
	private EditText mTaskName, mTaskEstimation, mTaskNotes;
	private Button mSubmitButton;

	OnClickListener mOnSaveClickListener = new OnClickListener() {
		@Override
		public void onClick(DialogInterface dialog, int which) {
			updateTask();
			TaskManager.updateTask(mProjectID, mTask);
			update();
			cancel();
		}
	};
	
	OnClickListener mOnCreateClickListener = new OnClickListener() {
		@Override
		public void onClick(DialogInterface dialog, int which) {
			updateTask();
			TaskManager.createTask(mProjectID, mTask.getStoryID(), mTask);
			update();
			cancel();
		}
	};
	
	TextWatcher mTextWatcher = new TextWatcher() {
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {}
		
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
		
		@Override
		public void afterTextChanged(Editable s) {
			if (mSubmitButton != null)
				mSubmitButton.setEnabled(textChecker());
		}
	};
	
	private OnShowListener mOnShowListener = new OnShowListener() {
		@Override
		public void onShow(DialogInterface dialogInterface) {
			mSubmitButton = getButton(BUTTON_POSITIVE);
			mSubmitButton.setEnabled(textChecker());
		}
	};
	
	public SprintBacklogTaskDetailDialog(Context context, TaskObject task, String projecID, int mode) {
		super(context, projecID);
		// Content view 的初始化
		LayoutInflater inflater = LayoutInflater.from(mContext);
		mView = inflater.inflate(R.layout.taskitem, null);
		mTaskName = (EditText) mView.findViewById(R.id.taskNameEditText);
		mTaskName.addTextChangedListener(mTextWatcher);
		mTaskEstimation = (EditText) mView.findViewById(R.id.taskEstimetionEditText);
		mTaskEstimation.addTextChangedListener(mTextWatcher);
		mTaskNotes = (EditText) mView.findViewById(R.id.taskNotesEditText);
		
		setView(mView);
		setMode(mode);
		setTask(task);
		setOnShowListener(mOnShowListener);
	}
	
	/**
	 * 新增或修改 task 的模式
	 * @param mode
	 */
	public void setMode(int mode) {
		mCurrentMode = mode;
		if (mCurrentMode == MODE_CREATE)
			setButton(AlertDialog.BUTTON_POSITIVE, "Create", mOnCreateClickListener);
		else
			setButton(AlertDialog.BUTTON_POSITIVE, "Save", mOnSaveClickListener);
	}
	
	public void setTask(TaskObject task) {
		mTask = task;
		mTaskName.setText(task.getName());
		mTaskEstimation.setText(task.getEstimation());
		mTaskNotes.setText(task.getNotes());
		setTitle(mTask.getName());
	}
	
	/**
	 * 將畫面上的文字更新到 mTask
	 */
	private void updateTask() {
		mTask.setName(mTaskName.getText().toString());
		mTask.setEstimation(mTaskEstimation.getText().toString());
		mTask.setNotes(mTaskNotes.getText().toString());
	}
	
	/**
	 * 判斷必要的屬性有沒有被填入資料
	 * @return
	 */
	private boolean textChecker() {
		if (mTaskName.getText().toString().isEmpty() ||
			mTaskEstimation.getText().toString().isEmpty())
			return false;
		return true;
	}
}
