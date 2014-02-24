package ntut.mobile.ezscrum.view.sprintbacklog;

import ntut.mobile.ezscrum.model.StoryObject;
import ntut.mobile.ezscrum.model.TaskObject;
import ntut.mobile.ezscrum.view.R;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.TextView;

interface ItemClickListener {
	public void onItemClick(int storyIndex, int taskIndex);
}

interface ItemLongClickListener {
	public void onItemLongClick(int storyIndex, int taskIndex);
}

public class SprintBacklogItem {
	private static final String TYPE_STORY = "S";
	private static final String TYPE_TASK = "T";
	private ItemClickListener mItemClickListener = null;
	private ItemLongClickListener mItemLongClickListener = null;
	private Context mContext;
	private int mStoryIndex = -1, mTaskIndex = -1;
	private Drawable mBackground;
	private View mView;
	private String mType, mID, mName, mImportance, mValue, mEstimation;

	public SprintBacklogItem(Context context, StoryObject story, int storyIndex) {
		mContext = context;
		mStoryIndex = storyIndex;
		mType = SprintBacklogItem.TYPE_STORY;
		mID = story.get_id();
		mName = story.get_name();
		mImportance = story.get_importance();
		mValue = story.get_value();
		mEstimation = story.get_estimation();
		mBackground = mContext.getResources().getDrawable(R.drawable.sprintbacklog_item_story);
		init();
	}

	public SprintBacklogItem(Context context, TaskObject task, int storyIndex, int taskIndex) {
		mContext = context;
		mStoryIndex = storyIndex;
		mTaskIndex = taskIndex;
		mType = SprintBacklogItem.TYPE_TASK;
		mID = task.getId();
		mName = task.getName();
		mImportance = "";
		mValue = "";
		mEstimation = task.getEstimation();
		mBackground = mContext.getResources().getDrawable(R.drawable.sprintbacklog_item_task);
		init();
	}
	
	private void init() {
		mView = View.inflate(mContext, R.layout.sprintbacklog_item, null);
		mView.setBackgroundDrawable(mBackground);
		((TextView) mView.findViewById(R.id.sprintBacklog_type)).setText(mType);
		((TextView) mView.findViewById(R.id.sprintBacklog_storyID)).setText(mID);
		((TextView) mView.findViewById(R.id.sprintBacklog_name)).setText(mName);
		((TextView) mView.findViewById(R.id.sprintBacklog_importance)).setText(mImportance);
		((TextView) mView.findViewById(R.id.sprintBacklog_value)).setText(mValue);
		((TextView) mView.findViewById(R.id.sprintBacklog_estimate)).setText(mEstimation);
		// 如果不是 story 則 value, importamce 不用顯示
		if (isTask()) {
			((TextView) mView.findViewById(R.id.sprintBacklog_importance)).setVisibility(View.INVISIBLE);
			((TextView) mView.findViewById(R.id.sprintBacklog_value)).setVisibility(View.INVISIBLE);
		}
		mView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mItemClickListener != null)
					mItemClickListener.onItemClick(mStoryIndex, mTaskIndex);
			}
		});
		mView.setOnLongClickListener(new OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				if (mItemLongClickListener == null)
					return false;
				else 
					mItemLongClickListener.onItemLongClick(mStoryIndex, mTaskIndex);
				return true;
			}
		});
	}
	
	public void setOnItemClickListener(ItemClickListener listener) {
		mItemClickListener = listener;
	}
	
	public void setOnItemLonglickListener(ItemLongClickListener listener) {
		mItemLongClickListener = listener;
	}

	public View getView() {
		return mView;
	}
	
	public boolean isStory() {
		if (mType.equals(SprintBacklogItem.TYPE_STORY))
			return true;
		return false;
	}
	
	public boolean isTask() {
		if (mType.equals(SprintBacklogItem.TYPE_TASK))
			return true;
		return false;
	}
}