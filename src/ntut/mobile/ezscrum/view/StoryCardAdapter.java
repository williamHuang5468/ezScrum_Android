package ntut.mobile.ezscrum.view;

import java.util.ArrayList;
import java.util.List;

import ntut.mobile.ezscrum.controller.productbacklog.ProductBacklogItemManager;
import ntut.mobile.ezscrum.model.StoryObject;
import ntut.mobile.ezscrum.model.TagObject;
import ntut.mobile.ezscrum.view.productbacklog.ProductBacklogGridViewActivity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class StoryCardAdapter extends BaseAdapter {

	private Context mContext;
	private List<StoryObject> mStoryList;
	private String mProjectID;
	private ProductBacklogItemManager mManager;
	
	public StoryCardAdapter(Context context, List<StoryObject> storyList, String projectID) {
		this.mContext = context;
		this.mStoryList = storyList;
		this.mProjectID = projectID;
		mManager = new ProductBacklogItemManager();
	}

	@Override
	public int getCount() {
		return mStoryList.size();
	}

	@Override
	public Object getItem(int position) {
		return mStoryList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final int STORY_CARD_WIDTH = 280;
		final int STORY_CARD_HEIGHT = 225;
		
		if (convertView == null) {
			LayoutInflater inflater = LayoutInflater.from(mContext);
			convertView = (LinearLayout) inflater.inflate(
					R.layout.storyitem_thumbnail, null);
			convertView.setLayoutParams(new GridView.LayoutParams(STORY_CARD_WIDTH, STORY_CARD_HEIGHT));
		}
		StoryObject story = mStoryList.get(position);
		setCardOnClickListener(convertView, getCardOnClickListener(story));
		setOnLongClickListener(convertView, getOnLongClickListener(story));
		setThumbnailInfo(convertView, story);
		return convertView;
	}
	
	public void addItem(StoryObject story) {
		mStoryList.add(story);
	}
	
	/**
	 * 取得Story Card縮圖的OnClickListener
	 * @param position
	 * @return
	 */
	private OnClickListener getCardOnClickListener(final StoryObject story) {
		OnClickListener listener = new OnClickListener() {
			@Override
			public void onClick(final View v) {
				LayoutInflater inflater = LayoutInflater.from(mContext);
				View storyCardView = inflater.inflate(R.layout.storyitem,
						null);

				setStoryCardInfo(storyCardView, story);
				AlertDialog.Builder builder = new AlertDialog.Builder(
						mContext);
				builder.setTitle("#" + story.get_id() + " " + story.get_name());
				builder.setView(storyCardView);
				builder.setPositiveButton("Save", getOnSaveBtnClickListener(storyCardView, story.get_id()));
				builder.setNegativeButton("Cancel", null);
				builder.create().show();
				
			}
		};
		return listener;
	}
	
	
	/**
	 * 取得Story card縮圖長按的事件
	 * @param position
	 * @return
	 */
	private View.OnLongClickListener getOnLongClickListener(final StoryObject story) {
		return new OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				showStoryOperationDialog(story);
				return true;
			}
		};
	}
	
	/**
	 * 設定Story card上各欄位的值
	 * @param view
	 * @param story
	 */
	private void setStoryCardInfo(View view, StoryObject story) {
		((EditText) view.findViewById(R.id.storyName)).setText(story.get_name());
		((EditText) view.findViewById(R.id.storyImportance)).setText(story.get_importance());
		((EditText) view.findViewById(R.id.storyEstimation)).setText(story.get_estimation());
		((EditText) view.findViewById(R.id.storyValue)).setText(story.get_value());
		((EditText) view.findViewById(R.id.storyNote)).setText(story.get_notes());
		((EditText) view.findViewById(R.id.storyHowToDemo)).setText(story.get_howToDemo());
	}
	
	/**
	 * 設定Story card縮圖上個欄位的值
	 * @param view
	 * @param story
	 */
	private void setThumbnailInfo(View view, StoryObject story) {
		((TextView) view.findViewById(R.id.thumbStoryName)).setText(story.get_name());
		((TextView) view.findViewById(R.id.thumbStoryImportance)).setText(story.get_importance());
		((TextView) view.findViewById(R.id.thumbStoryValue)).setText(story.get_value());
		((TextView) view.findViewById(R.id.thumbStoryEstimation)).setText(story.get_estimation());
		((TextView) view.findViewById(R.id.thumbStoryNote)).setText(story.get_notes());
		((TextView) view.findViewById(R.id.thumbStoryHowToDemo)).setText(story.get_howToDemo());
	}
	
	/**
	 * 設定Story Card縮圖的OnClickListener
	 * @param view
	 * @param listener
	 */
	private void setCardOnClickListener(View view, OnClickListener listener) {
		view.findViewById(R.id.thumbStoryName).setOnClickListener(
				listener);
		view.findViewById(R.id.thumbStoryNote).setOnClickListener(
				listener);
		view.findViewById(R.id.thumbStoryHowToDemo)
				.setOnClickListener(listener);
		view.findViewById(R.id.thumbStoryImportance)
				.setOnClickListener(listener);
		view.findViewById(R.id.thumbStoryValue).setOnClickListener(
				listener);
		view.findViewById(R.id.thumbStoryEstimation)
				.setOnClickListener(listener);
		view.setOnClickListener(listener);
	}
	
	/**
	 * 設定Story Card縮圖的長按事件
	 * @param view
	 * @param listener
	 */
	private void setOnLongClickListener(View view, OnLongClickListener listener) {
		view.findViewById(R.id.thumbStoryName).setOnLongClickListener(
				listener);
		view.findViewById(R.id.thumbStoryNote).setOnLongClickListener(
				listener);
		view.findViewById(R.id.thumbStoryHowToDemo)
		.setOnLongClickListener(listener);
		view.findViewById(R.id.thumbStoryImportance)
		.setOnLongClickListener(listener);
		view.findViewById(R.id.thumbStoryValue).setOnLongClickListener(
				listener);
		view.findViewById(R.id.thumbStoryEstimation)
		.setOnLongClickListener(listener);
		view.setOnLongClickListener(listener);
	}
	
	/**
	 * 從StoryCard取出資料
	 * @param view
	 * @return StoryObject
	 */
	private StoryObject getStoryObjectFromCardView(View view) {
		StoryObject story = new StoryObject();
		String name = ((EditText) view.findViewById(R.id.storyName)).getText().toString();
		String note = ((EditText) view.findViewById(R.id.storyNote)).getText().toString();
		String howToDemo = ((EditText) view.findViewById(R.id.storyHowToDemo)).getText().toString();
		String value = ((EditText) view.findViewById(R.id.storyValue)).getText().toString();
		String estimation = ((EditText) view.findViewById(R.id.storyEstimation)).getText().toString();
		String importance = ((EditText) view.findViewById(R.id.storyImportance)).getText().toString();
		List<TagObject> tagList = new ArrayList<TagObject>(); // TODO: 從view中取得tag
		
		story.set_name(name);
		story.set_notes(note);
		story.set_howToDemo(howToDemo);
		story.set_value(value);
		story.set_estimation(estimation);
		story.set_importance(importance);
		story.set_tagList(tagList);
		
		return story;
	}
	
	/**
	 * 取得點擊Story card儲存的事件
	 * @return
	 */
	private DialogInterface.OnClickListener getOnSaveBtnClickListener(final View storyCardView, final String storyID) {
		return new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// 從card中取得Story資訊
				StoryObject storyObject = getStoryObjectFromCardView(storyCardView);
				storyObject.set_id(storyID);
				
				mManager.updateProductBacklogItem(mProjectID, storyObject);
				// 通知activity更新頁面
				((ProductBacklogGridViewActivity) mContext).refreshAdapter();
			}
		};
	}
	
	/**
	 * 顯示story 功能列表
	 * @param position
	 */
	private void showStoryOperationDialog(final StoryObject story) {
		//	initial view component
		LayoutInflater inflater = LayoutInflater.from(mContext);
		final View storyOperationView = inflater.inflate(R.layout.productbacklog_story_operation, null);
		Button deleteStoryButton = (Button) storyOperationView.findViewById(R.id.productbacklog_deleteStoryButton);
//		Button editStoryButton = (Button) storyOperationView.findViewById(R.id.editStoryButton);
//		Button addTagButton = (Button) storyOperationView.findViewById( R.id.addTagButton );
		
		//	set alertDialog
		AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
		builder.setTitle(story.get_name());
		builder.setView(storyOperationView);
		builder.setNegativeButton("Cancel", null);
		final AlertDialog storyOperationDialog = builder.create();
		
		//	button event
		deleteStoryButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//	delete story button
				AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
				builder.setTitle("Delete Story");
				builder.setMessage("Make sure you want to delete the story!\n" +
								   "Caution:This will make the tasks, belong to the deleted story, related to no story." +
								   "But you can relate the tasks, belonged to the deleted story, to other story by adding the tasks as existed task.");
				builder.setNegativeButton("Cancel", null);
				builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						mManager.deleteProductBacklogItem(mProjectID, story.get_id());
						storyOperationDialog.cancel();
						((ProductBacklogGridViewActivity) mContext).refreshAdapter();
					}
				});
				builder.create().show();
			}
		});
/*		editStoryButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO: edit story button
			}
		});*/
		storyOperationDialog.show();
	}
	
}