package ntut.mobile.ezscrum.view.sprintbacklog;

import ntut.mobile.ezscrum.controller.productbacklog.ProductBacklogItemManager;
import ntut.mobile.ezscrum.model.StoryObject;
import ntut.mobile.ezscrum.view.R;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

public class SprintBacklogStoryDetailDialog extends SprintBacklogDialog {
	public static final int MODE_CREATE = 1;
	public static final int MODE_UPDATE = 2;
	private int mCurrentMode = MODE_UPDATE;
	private StoryObject mStory;
	private View mView;
	private EditText mStoryName, mStoryImportance, mStoryEstimation, mStoryValue, mStoryNotes, mStoryHowToDemo;
	private ProductBacklogItemManager mProductBacklogItemManager;

	OnClickListener mOnCreateClickListener = new OnClickListener() {
		@Override
		public void onClick(DialogInterface dialog, int which) {
			updateStory();
			mProductBacklogItemManager.addProductBacklogItem(mProjectID, mStory);
			update();
			cancel();
		}
	};
	
	OnClickListener mOnSaveClickListener = new OnClickListener() {
		@Override
		public void onClick(DialogInterface dialog, int which) {
			updateStory();
			mProductBacklogItemManager.updateProductBacklogItem(mProjectID, mStory);
			update();
			cancel();
		}
	};
	
	protected SprintBacklogStoryDetailDialog(Context context, StoryObject story, String projecID, int mode) {
		super(context, projecID);
		mProductBacklogItemManager = new ProductBacklogItemManager();
		
		// Content view 的初始化
		LayoutInflater inflater = LayoutInflater.from(mContext);
		mView = inflater.inflate(R.layout.storyitem, null);
		mStoryName = (EditText) mView.findViewById(R.id.storyName);
		mStoryImportance = (EditText) mView.findViewById(R.id.storyImportance);
		mStoryEstimation = (EditText) mView.findViewById(R.id.storyEstimation);
		mStoryValue = (EditText) mView.findViewById(R.id.storyValue);
		mStoryNotes = (EditText) mView.findViewById(R.id.storyNote);
		mStoryHowToDemo = (EditText) mView.findViewById(R.id.storyHowToDemo);

		setView(mView);
		setMode(mode);
		setStory(story);
	}
	
	public void setStory(StoryObject story) {
		mStory = story;
		mStoryName.setText(story.get_name());
		mStoryImportance.setText(story.get_importance());
		mStoryEstimation.setText(story.get_estimation());
		mStoryValue.setText(story.get_value());
		mStoryNotes.setText(story.get_notes());
		mStoryHowToDemo.setText(story.get_howToDemo());
		setTitle(mStory.get_name());
	}
	
	/**
	 * 更新 story 的資料
	 */
	public void updateStory() {
		mStory.set_name(mStoryName.getText().toString());
		mStory.set_importance(mStoryImportance.getText().toString());
		mStory.set_estimation(mStoryEstimation.getText().toString());
		mStory.set_value(mStoryValue.getText().toString());
		mStory.set_notes(mStoryNotes.getText().toString());
		mStory.set_howToDemo(mStoryHowToDemo.getText().toString());
	}
	
	/**
	 * 新增或修改 story 的模式
	 * @param mode
	 */
	public void setMode(int mode) {
		mCurrentMode = mode;
		if (mCurrentMode == MODE_CREATE)
			setButton(AlertDialog.BUTTON_POSITIVE, "Create", mOnCreateClickListener);
		else
			setButton(AlertDialog.BUTTON_POSITIVE, "Save", mOnSaveClickListener);
	}
}
