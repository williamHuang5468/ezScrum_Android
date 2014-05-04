package ntut.mobile.ezscrum.view.sprintplan;

import ntut.mobile.ezscrum.model.SprintObject;
import ntut.mobile.ezscrum.view.R;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

public class SprintPlanSprintDialog extends AlertDialog {
	public static final int CREATE_MODE = 0;
	public static final int UPDATE_MODE = 1;
	
	private String sprintID;

	private View mView;
	private DatePicker mStartDatePicker;
	private EditText mStartDateEditText;
	private DatePicker mDemoDatePicker;
	private EditText mDemoDateEditText;
	private EditText mSprintGoalEditText;
	private EditText mIntervalEditText;
	private EditText mMembersEditText;
	private EditText mHoursToCommitEditText;
	private Button mSubmitButton;
	
	protected SprintPlanSprintDialog(Context context, SprintObject sprint, int mode) {
		super(context);
		LayoutInflater inflater = LayoutInflater.from(context);
		mView = inflater.inflate(R.layout.sprintitem, null);
		initialize();
		setWidgetsListener();
		setWidgetsInfo(sprint, mode);
		setOnShowListener(mOnShowListener);
		setView(mView);
	}
	
	/**
	 * 此 Dialog 純粹處理 User 跟 View 之間互動的部分
	 * 不希望去使用到 Manager(Thread) 故此使用這方法
	 * 在外部的 Activity 監聽此 Dialog
	 * 當 Dialog 消失交給外部 Activity 去判斷是否要送交
	 */
	private OnClickListener mOnSubmitClickListener = new OnClickListener() {
		@Override
		public void onClick(DialogInterface dialog, int which) {
		}
	};
	
	private OnClickListener mOnCancelClickListener = new OnClickListener() {
		@Override
		public void onClick(DialogInterface dialog, int which) {
			cleanAllWidgetsInfo();
		}
	};

	private void initialize() {
		mStartDatePicker = (DatePicker) mView.findViewById(R.id.sprintStartDatePicker);
		mStartDateEditText = (EditText) mView.findViewById(R.id.sprintStartDate);
		mDemoDatePicker = (DatePicker) mView.findViewById(R.id.sprintDemoDatePicker);
		mDemoDateEditText = (EditText) mView.findViewById(R.id.sprintDemoDate);
		mSprintGoalEditText = (EditText) mView.findViewById(R.id.sprintGoal);
		mIntervalEditText = (EditText) mView.findViewById(R.id.sprintInterval);
		mMembersEditText = (EditText) mView.findViewById(R.id.sprintMembers);
		mHoursToCommitEditText = (EditText) mView.findViewById(R.id.sprintHourToCommit);
	}

	private void setWidgetsInfo(SprintObject sprint, int mode) {
		sprintID = sprint.getSprintID();
		if (mode == CREATE_MODE) {
			setTitle("Create Sprint #" + sprintID);
			setButton(AlertDialog.BUTTON_POSITIVE, "Create", mOnSubmitClickListener);
			setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel", mOnCancelClickListener);
		} else {
			setTitle("Edit Sprint #" + sprintID);
			setButton(AlertDialog.BUTTON_POSITIVE, "Save", mOnSubmitClickListener);
			setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel", mOnCancelClickListener);
		}
		mStartDateEditText.setText(sprint.getStartDate());
		mDemoDateEditText.setText(sprint.getDemoDate());
		mSprintGoalEditText.setText(sprint.getSprintGoal());
		mIntervalEditText.setText(sprint.getInterval());
		mMembersEditText.setText(sprint.getMembers());
		mHoursToCommitEditText.setText(sprint.getHoursCanCommit());
	}

	private void setWidgetsListener() {
		mStartDateEditText.addTextChangedListener(mTextWatcher);
		mStartDateEditText.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					mStartDatePicker.setVisibility(View.VISIBLE);
					mDemoDatePicker.setVisibility(View.GONE);
				} else {
					mStartDateEditText.setText(String.valueOf(mStartDatePicker.getYear())
						+ "/" + String.valueOf(mStartDatePicker.getMonth() + 1)
						+ "/" + String.valueOf(mStartDatePicker.getDayOfMonth()));
				}
			}
		});
		mDemoDateEditText.addTextChangedListener(mTextWatcher);
		mDemoDateEditText.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					mDemoDatePicker.setVisibility(View.VISIBLE);
					mStartDatePicker.setVisibility(View.GONE);
				} else {
					mDemoDateEditText.setText(String.valueOf(mDemoDatePicker.getYear())
						+ "/" + String.valueOf(mDemoDatePicker.getMonth() + 1)
						+ "/" + String.valueOf(mDemoDatePicker.getDayOfMonth()));
				}
			}
		});
		mSprintGoalEditText.addTextChangedListener(mTextWatcher);
		mSprintGoalEditText.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					mStartDateEditText.setVisibility(View.VISIBLE);
					mDemoDateEditText.setVisibility(View.VISIBLE);
					mStartDatePicker.setVisibility(View.GONE);
					mDemoDatePicker.setVisibility(View.GONE);
				}
			}
		});
		mIntervalEditText.addTextChangedListener(mTextWatcher);
		mIntervalEditText.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					mStartDateEditText.setVisibility(View.VISIBLE);
					mDemoDateEditText.setVisibility(View.VISIBLE);
					mStartDatePicker.setVisibility(View.GONE);
					mDemoDatePicker.setVisibility(View.GONE);
				}
			}
		});
		mMembersEditText.addTextChangedListener(mTextWatcher);
		mMembersEditText.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					mStartDateEditText.setVisibility(View.VISIBLE);
					mDemoDateEditText.setVisibility(View.VISIBLE);
					mStartDatePicker.setVisibility(View.GONE);
					mDemoDatePicker.setVisibility(View.GONE);
				}
			}
		});
		mHoursToCommitEditText.addTextChangedListener(mTextWatcher);
		mHoursToCommitEditText.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					mStartDateEditText.setVisibility(View.VISIBLE);
					mDemoDateEditText.setVisibility(View.VISIBLE);
					mStartDatePicker.setVisibility(View.GONE);
					mDemoDatePicker.setVisibility(View.GONE);
				}
			}
		});
	}

	public SprintObject getSprint() {
		SprintObject sprint = new SprintObject();
		sprint.setSprintID(sprintID);
		sprint.setStartDate(mStartDateEditText.getText().toString());
		sprint.setDemoDate(mDemoDateEditText.getText().toString());
		sprint.setHoursCanCommit(mHoursToCommitEditText.getText().toString());
		sprint.setInterval(mIntervalEditText.getText().toString());
		sprint.setSprintGoal(mSprintGoalEditText.getText().toString());
		sprint.setMembers(mMembersEditText.getText().toString());
		return sprint;
	}
	
	public boolean isExistValueForAll() {
		if(mStartDateEditText.getText().toString().isEmpty()
				|| mDemoDateEditText.getText().toString().isEmpty()
				|| mSprintGoalEditText.getText().toString().isEmpty()
				|| mIntervalEditText.getText().toString().isEmpty()
				|| mMembersEditText.getText().toString().isEmpty()
				|| mHoursToCommitEditText.getText().toString().isEmpty())
			return false;
		return true;
	}
	
	TextWatcher mTextWatcher = new TextWatcher() {
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {}
		
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
		
		@Override
		public void afterTextChanged(Editable s) {
			if (mSubmitButton != null)
				mSubmitButton.setEnabled(isExistValueForAll());
		}
	};
	
	private OnShowListener mOnShowListener = new OnShowListener() {
		@Override
		public void onShow(DialogInterface dialogInterface) {
			mSubmitButton = getButton(BUTTON_POSITIVE);
			mSubmitButton.setEnabled(isExistValueForAll());
		}
	};
	
	private void cleanAllWidgetsInfo() {
		mStartDateEditText.setText("");
		mDemoDateEditText.setText("");
		mSprintGoalEditText.setText("");
		mIntervalEditText.setText("");
		mMembersEditText.setText("");
		mHoursToCommitEditText.setText("");
	}
}
