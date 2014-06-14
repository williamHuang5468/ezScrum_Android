package ntut.mobile.ezscrum.view.productbacklog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ntut.mobile.ezscrum.controller.productbacklog.ProductBacklogItemManager;
import ntut.mobile.ezscrum.model.StoryObject;
import ntut.mobile.ezscrum.model.TagObject;
import ntut.mobile.ezscrum.sort.ComparatorFactory;
import ntut.mobile.ezscrum.view.R;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

public class ProductBacklogListViewAdapter extends BaseAdapter implements Filterable{
	private Context context;
	private List<StoryObject> storyList;
	private List<TagObject> tagList;
	private LayoutInflater inflater;
	private String projectID;
	private Map<String, Integer> tagImageResouce, pairTagAndImage;
	private ProductBacklogItemManager productBacklogItemManager;
	private final int TAG_SIZE_LIMIT = 7;
	private Filter storyFilter;
	private List<StoryObject> sourceStoryList;
	private Comparator<StoryObject> mComparator;
	private ComparatorFactory mFactory;
	
	public ProductBacklogListViewAdapter(Context context ,List<StoryObject> storyList, List<TagObject> tagList){
		this.context = context;
		this.storyList = storyList;
		this.tagList = tagList;
		this.inflater = LayoutInflater.from(this.context);
		
		this.sourceStoryList = storyList;
		mFactory = new ComparatorFactory();
		mComparator = mFactory.createComparator("Des", "Importance");
		
		productBacklogItemManager = new ProductBacklogItemManager();
		pairTagAndImage = new HashMap<String, Integer>();
		mapImageResouce();
		pairTagAndImage();
	}
	
	public void setProjectInformation(String projectID) {
		this.projectID = projectID;
	}
	
	@Override
	public int getCount() {
		return storyList.size();
	}

	@Override
	public Object getItem(int position) {
		return storyList.get(position);
	}

	@Override
	public long getItemId(int position) {
		long id = Long.valueOf(storyList.get(position).get_id());
		return id;
	}
 
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final StoryObject story = storyList.get(position);
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.productbacklog_item, null);
		}
		
		final LinearLayout productbacklog_tag_linaerLayout = (LinearLayout) convertView.findViewById(R.id.productbacklog_tag_linaerLayout);
		TextView storyIdTextView = (TextView) convertView.findViewById(R.id.storyIdTextView);
		TextView storyNameTextView = (TextView) convertView.findViewById(R.id.storyNameTextView);
		
		TextView storyValueTextView = (TextView) convertView.findViewById(R.id.story_value_TextView);
		TextView storyEstimationTextView = (TextView) convertView.findViewById(R.id.story_estimataion_TextView);
		TextView storyImportanceTextView = (TextView) convertView.findViewById(R.id.story_importance_TextView);

		storyIdTextView.setText(story.get_id());
		storyNameTextView.setText(story.get_name());
		
		storyValueTextView.setText(story.get_value());
		storyEstimationTextView.setText(story.get_estimation());
		storyImportanceTextView.setText(story.get_importance());
		
		//	顯示tag image button
		productbacklog_tag_linaerLayout.removeAllViews();
		controlDisplayTagImageButton(story, productbacklog_tag_linaerLayout);
		
		convertView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				showStoryDetailInfoDialog(story);
			}
		});
		convertView.setOnLongClickListener(new View.OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				showStoryOperationDialog(story);
				return true;
			}
		});
		return convertView;
	}

	private void controlDisplayTagImageButton(final StoryObject story,
		final LinearLayout productbacklog_tag_linaerLayout) {
		int storyTagSize = story.get_tagList().size();
		if(storyTagSize < TAG_SIZE_LIMIT){
			productbacklog_tag_linaerLayout.addView(newEmptyButton(story, productbacklog_tag_linaerLayout));
		}
		for(TagObject tag : story.get_tagList()){
			productbacklog_tag_linaerLayout.addView(newImageButton(story, tag, productbacklog_tag_linaerLayout));
		}
	}
	
	/**
	 * 新增empty image button.
	 * 在story顯示的tag數量未達7個時，表示還可以新增tag
	 * @param story
	 * @param productbacklog_tag_linaerLayout
	 * @return
	 */
	public ImageButton newEmptyButton( final StoryObject story, final LinearLayout productbacklog_tag_linaerLayout){
		final ImageButton newTagEmptyImageButton = new ImageButton(context);
		newTagEmptyImageButton.setBackgroundColor(this.context.getResources().getColor(R.color.transparent));
		newTagEmptyImageButton.setBackgroundResource(R.drawable.tag_empty);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
		params.leftMargin = 5;
		params.rightMargin = 5;
		params.bottomMargin = 5;
		newTagEmptyImageButton.setLayoutParams(params);
		newTagEmptyImageButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//	show total tag list dialog
				final ProductBacklogTagDialog productBacklogTagDialog = new ProductBacklogTagDialog(context, productBacklogItemManager, story, tagList, pairTagAndImage);
				productBacklogTagDialog.setOnDismissListener(new OnDismissListener() {
					@Override
					public void onDismiss(DialogInterface dialog) {
						updateStory(productBacklogTagDialog.getStoryObject());
					}
				});
			}
		});
		return newTagEmptyImageButton;
	}
	
	private void mapImageResouce(){
		tagImageResouce = new HashMap<String, Integer>();
		tagImageResouce.put("0", R.drawable.tag_empty);
		tagImageResouce.put("1", R.drawable.tag_red);
		tagImageResouce.put("2", R.drawable.tag_orange);
		tagImageResouce.put("3", R.drawable.tag_yellow);
		tagImageResouce.put("4", R.drawable.tag_green);
		tagImageResouce.put("5", R.drawable.tag_blue);
		tagImageResouce.put("6", R.drawable.tag_gray);
		tagImageResouce.put("7", R.drawable.tag_purple);
	}
	
	private void pairTagAndImage(){
		for(int i = 0; i < tagList.size(); i++) {
			pairTagAndImage.put(tagList.get(i).getTagName(), tagImageResouce.get(String.valueOf(i+1)));
		}
	}
	
	public ImageButton newImageButton(final StoryObject story, final TagObject tag, final LinearLayout productbacklog_tag_linaerLayout){
//		int tagImageResource = pairTagAndImage.get(tag.getTagName());
		int tagImageResource = 0;
		final ImageButton newTagImageButton = new ImageButton(context);
		newTagImageButton.setBackgroundResource(tagImageResource);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
		params.leftMargin = 5;
		params.rightMargin = 5;
		params.bottomMargin = 5;
		newTagImageButton.setLayoutParams(params);
		newTagImageButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//	show total tag list dialog
				final ProductBacklogTagDialog productBacklogTagDialog = new ProductBacklogTagDialog(context, productBacklogItemManager, story, tagList, pairTagAndImage);
				productBacklogTagDialog.setOnDismissListener(new OnDismissListener() {
					@Override
					public void onDismiss(DialogInterface dialog) {
						updateStory(productBacklogTagDialog.getStoryObject());
					}
				});
			}
		});
		return newTagImageButton;
	}
	
	/**
	 * edit story
	 * 顯示所選中的story information
	 * @param story
	 */
	private void showStoryDetailInfoDialog(final StoryObject story) {
		LayoutInflater inflater = LayoutInflater.from(context);
		final View storyCardView = inflater.inflate(R.layout.storyitem, null);
		setStoryCardInfo(storyCardView, story);
		
		//	
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle("#" + story.get_id() + " Information");
		builder.setView(storyCardView);
		builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// 從card中取得Story資訊
				StoryObject storyObject = getStoryObjectFromCardView(storyCardView);
				storyObject.set_id(story.get_id());
				storyObject.set_sprint(story.get_sprint());
				updateStory(storyObject);
			}
		});
		builder.setNegativeButton("Cancel", null);
		builder.create().show();
	}
	
	/**
	 * 刪除story
	 */
	private void deleteStory(StoryObject story) {
		productBacklogItemManager.deleteProductBacklogItem(projectID, story.get_id());
		((ProductBacklogListViewActivity) context).refreshAdapter();
	}
	
	/**
	 * 更新story
	 */
	private void updateStory(StoryObject story) {
		productBacklogItemManager.updateProductBacklogItem(projectID, story);
		((ProductBacklogListViewActivity) context).refreshAdapter();
	}
	
	/**
	 * 顯示 story 功能列表
	 * @param story
	 */
	private void showStoryOperationDialog(final StoryObject story) {
		//	initial view component
		LayoutInflater inflater = LayoutInflater.from(context);
		final View storyOperationView = inflater.inflate(R.layout.productbacklog_story_operation, null);
		Button deleteStoryButton = (Button) storyOperationView.findViewById(R.id.productbacklog_deleteStoryButton);
		
		//	set alertDialog
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(story.get_name());
		builder.setView(storyOperationView);
		builder.setNegativeButton("Cancel", null);
		final AlertDialog storyOperationDialog =  builder.create();
		
		//	button event
		deleteStoryButton.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				//	delete story button
				AlertDialog.Builder builder = new AlertDialog.Builder(context);
				builder.setTitle("Delete Story");
				builder.setMessage(	"Make sure you want to delete the story!\n" +
									"Caution:This will make the tasks, belong to the deleted story, related to no story." +
									"But you can relate the tasks, belonged to the deleted story, to other story by adding the tasks as existed task.");
				builder.setNegativeButton("Cancel", null);
				builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						deleteStory(story);
						storyOperationDialog.cancel();
					}
				});
				builder.create().show();
			}
		});
		storyOperationDialog.show();
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
		story.set_sprint("0");
		story.set_notes(note);
		story.set_howToDemo(howToDemo);
		story.set_value(value);
		story.set_estimation(estimation);
		story.set_importance(importance);
		story.set_tagList(tagList);
		
		return story;
	}

	@Override
	public Filter getFilter() {
		if (storyFilter == null)
			storyFilter = new StoryFilter();
		return storyFilter;
	}
	
	private class StoryFilter extends Filter {
		@SuppressLint("DefaultLocale")
		@Override
		protected FilterResults performFiltering(CharSequence constraint) {
			FilterResults results = new FilterResults();
			if (constraint == null || constraint.length() == 0) {
				// 沒有輸入任何字，傳回List
				results.values = sourceStoryList;
				results.count = sourceStoryList.size();
			}
			else {
				// 搜尋符合輸入的Keyword，符合的存入mStoryList
				List<StoryObject> searchResult = new ArrayList<StoryObject>();
				for (StoryObject story : storyList) {
					if (story.get_name().contains(constraint.toString())){
						searchResult.add(story);
					}
				}
				results.values = searchResult;
				results.count = searchResult.size();
			}
			return results;
		}

		@Override
		protected void publishResults(CharSequence constraint,
				FilterResults results) {
			// 更新ListView
			if (results.count == 0)
				notifyDataSetInvalidated();
			else {
				storyList = (List<StoryObject>) results.values;
				notifyDataSetChanged();
			}
		}
	}
	
	/**
	 * 設定 Story 升冪排序, 降冪排序
	 * @param getFilterItem
	 */
	public void setComparator(String ascOrDes, String getFilterItem){
		mComparator = mFactory.createComparator(ascOrDes, getFilterItem);
	}
	
	public void sort(List<StoryObject> storyList){
		if(!(mComparator == null)){
			Collections.sort(storyList, mComparator);
			notifyDataSetChanged();
		}
	}
}