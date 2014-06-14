package ntut.mobile.ezscrum.view.productbacklog;

import java.util.ArrayList;
import java.util.List;

import ntut.mobile.ezscrum.controller.productbacklog.ProductBacklogItemManager;
import ntut.mobile.ezscrum.model.StoryObject;
import ntut.mobile.ezscrum.model.TagObject;
import ntut.mobile.ezscrum.util.EzScrumAppUtil;
import ntut.mobile.ezscrum.view.BaseActivity;
import ntut.mobile.ezscrum.view.R;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SearchView;

public class ProductBacklogListViewActivity extends BaseActivity implements Runnable {
	private String mProjectID;
	private ListView mProductBacklogListView;
	private List<StoryObject> mStoryList;
	private List<TagObject> mTagList;
	private ProductBacklogItemManager mProductBacklogItemManager;
	private Context mContext;
	private ProductBacklogListViewAdapter mProductBacklogListViewAdapter;
	private ProgressDialog mProgressDialog;
	private MenuItem mRefreshMenuItem;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.productbacklog_listview);
		setTitle(R.string.productBacklog);
		mContext = this;
		mProductBacklogListView = (ListView) findViewById(R.id.productbacklog_listview);
		mProductBacklogListView.setSelector(R.drawable.projectitem_selector);

		mProductBacklogItemManager = new ProductBacklogItemManager();

		// 從前一個activity取得資料
		Bundle bundle = this.getIntent().getExtras();
		mProjectID = bundle.getString("projectID");
		refreshAdapter();
	}

	@Override
	public void onBackPressed() {
		finish();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();

		inflater.inflate(R.menu.filter, menu);
		inflater.inflate(R.menu.common, menu);
		inflater.inflate(R.menu.productbacklog, menu);
		inflater.inflate(R.menu.search, menu);
		MenuItem changeViewProductBacklog = menu
				.findItem(R.id.changeViewProductBacklog);
		mRefreshMenuItem = (MenuItem) menu.findItem(R.id.refreshProductBacklog);
		mRefreshMenuItem.setTitle(EzScrumAppUtil.getCurrentSystemTime());

		// 搜尋功能
		SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
		SearchView searchView = (SearchView) menu.findItem(R.id.search)
				.getActionView();
		searchView.setSearchableInfo(searchManager
				.getSearchableInfo(getComponentName()));
		// Listener的設定
		SearchView.OnQueryTextListener queryListener = new SearchView.OnQueryTextListener() {
			@Override
			public boolean onQueryTextChange(String searchKeyword) {
				mProductBacklogListViewAdapter.getFilter().filter(searchKeyword);
				return true;
			}

			@Override
			public boolean onQueryTextSubmit(String query) {
				return true;
			}
		};
		searchView.setOnQueryTextListener(queryListener);

		changeViewProductBacklog.setIcon(R.drawable.grid_view);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.manageTagProductBacklog:
			onManageTagClick(item);
			break;
		case R.id.addBackloggedItem:
			onAddStoryClick(item);
			break;
		case R.id.refreshProductBacklog:
			onRefreshClick(item);
			break;
		case R.id.changeViewProductBacklog:
			onChangeViewClick(item);
			break;
		case R.id.filterStory:
			onFilterStory(item);
			break;
		case R.id.quickEdit:
			onQuickEdit(item);
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * 點擊 Filter Story 事件
	 * 
	 * @param item
	 */
	public void onFilterStory(MenuItem item) {
		LayoutInflater inflater = LayoutInflater.from(mContext);
		final View filterAlertView = inflater.inflate(R.layout.productbacklog_filter, null);

		AlertDialog.Builder builder = new AlertDialog.Builder(mContext); //
		builder.setTitle("Filter Story");
		builder.setView(filterAlertView);

		builder.setPositiveButton("Descending",	new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				RadioGroup group = (RadioGroup) filterAlertView.findViewById(R.id.filterGroup);
				RadioButton filterItem = (RadioButton) filterAlertView.findViewById(group.getCheckedRadioButtonId());
				String filterText = (String) filterItem.getText();
				mProductBacklogListViewAdapter.setComparator("Des", filterText);
				mProductBacklogListViewAdapter.sort(mStoryList);
			}
		});
		builder.setNegativeButton("Ascending", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				RadioGroup group = (RadioGroup) filterAlertView.findViewById(R.id.filterGroup);
				RadioButton filterItem = (RadioButton) filterAlertView.findViewById(group.getCheckedRadioButtonId());
				String filterText = (String) filterItem.getText();
				mProductBacklogListViewAdapter.setComparator("Asc", filterText);
				mProductBacklogListViewAdapter.sort(mStoryList);
			}
		});
		AlertDialog dialog = builder.create();
		dialog.show();
		dialog.setCanceledOnTouchOutside(false);
	}

	/**
	 * 點擊 Quick Edit 事件
	 * 
	 * @param item
	 */
	public void onQuickEdit(MenuItem item) {
		Intent intent = new Intent();
		Bundle bundle = new Bundle();
		bundle.putString("projectID", mProjectID);
		intent.putExtras(bundle);
		intent.setClass(this, ProductBacklogQuickEditViewActivity.class);
		startActivity(intent);
	}

	/**
	 * 點擊 Add Story 事件
	 * 
	 * @param item
	 */
	public void onAddStoryClick(MenuItem item) {
		LayoutInflater inflater = LayoutInflater.from(mContext);
		final View storyCardView = inflater.inflate(R.layout.storyitem, null);

		// 建立 New Story 的 AlertDialog
		AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
		builder.setTitle("New Story");
		builder.setView(storyCardView);
		builder.setPositiveButton("Save",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						StoryObject newStory = getStoryObjectFromCardView(storyCardView);
						mProductBacklogItemManager.addProductBacklogItem(
								mProjectID, newStory);
						refreshAdapter();
					}
				});
		builder.setNegativeButton("Cancel", null);
		AlertDialog dialog = builder.create();
		dialog.show();
		dialog.setCanceledOnTouchOutside(false);
	}

	/**
	 * 點擊 Manage Tag 事件
	 * 
	 * @param item
	 */
	public void onManageTagClick(MenuItem item) {
		refreshAdapter();
	}

	/**
	 * 點擊 Refresh 事件
	 * 
	 * @param item
	 */
	public void onRefreshClick(MenuItem item) {
		refreshAdapter();
	}

	/**
	 * 點擊 change view 事件
	 * 
	 * @param item
	 */
	public void onChangeViewClick(MenuItem item) {
		Bundle bundle = new Bundle();
		bundle.putString("projectID", mProjectID);
		Intent intent = new Intent();
		intent.putExtras(bundle);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.setClass(this, ProductBacklogGridViewActivity.class);
		startActivity(intent);
	}

	/**
	 * 從StoryCard取出資料
	 * 
	 * @param view
	 * @return StoryObject
	 */
	private StoryObject getStoryObjectFromCardView(View view) {
		StoryObject story = new StoryObject();
		String name = ((EditText) view.findViewById(R.id.storyName)).getText()
				.toString();
		String note = ((EditText) view.findViewById(R.id.storyNote)).getText()
				.toString();
		String howToDemo = ((EditText) view.findViewById(R.id.storyHowToDemo))
				.getText().toString();
		String value = ((EditText) view.findViewById(R.id.storyValue))
				.getText().toString();
		String estimation = ((EditText) view.findViewById(R.id.storyEstimation))
				.getText().toString();
		String importance = ((EditText) view.findViewById(R.id.storyImportance))
				.getText().toString();
		List<TagObject> tagList = new ArrayList<TagObject>(); // 從view中取得tag

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

	private void initialProductBacklogListViewAdpater() {
		mStoryList = mProductBacklogItemManager
				.retrieveProductBacklogAllItems(mProjectID);
		mTagList = mProductBacklogItemManager
				.readProductBacklogTagList(mProjectID);
		mProductBacklogListViewAdapter = new ProductBacklogListViewAdapter(
				ProductBacklogListViewActivity.this, mStoryList, mTagList);
		mProductBacklogListViewAdapter.setProjectInformation(mProjectID);
		mProductBacklogListViewAdapter.sort(mStoryList);
	}

	/**
	 * 更新Adapter, 重新於Server取出, 並更新畫面
	 */
	public void refreshAdapter() {
		// 顯示Progress對話方塊
		mProgressDialog = ProgressDialog.show(this, "", "Loading...", true);

		Thread thread = new Thread(this);
		thread.start();
	}

	public void run() {
		// 設定 product backlog list view adapter並顯示
		initialProductBacklogListViewAdpater(); // 透過web service取得product
												// backlog所需的資料
		handler.sendEmptyMessage(0);
	}

	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			mProductBacklogListView.setAdapter(mProductBacklogListViewAdapter);
			mRefreshMenuItem.setTitle(EzScrumAppUtil.getCurrentSystemTime());
			mProgressDialog.dismiss(); // 將Progress關閉
		}
	};
}