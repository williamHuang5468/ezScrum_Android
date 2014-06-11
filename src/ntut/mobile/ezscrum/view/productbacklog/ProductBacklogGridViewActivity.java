package ntut.mobile.ezscrum.view.productbacklog;

import java.util.ArrayList;
import java.util.List;

import ntut.mobile.ezscrum.controller.productbacklog.ProductBacklogItemManager;
import ntut.mobile.ezscrum.model.StoryObject;
import ntut.mobile.ezscrum.model.TagObject;
import ntut.mobile.ezscrum.util.EzScrumAppUtil;
import ntut.mobile.ezscrum.view.BaseActivity;
import ntut.mobile.ezscrum.view.R;
import ntut.mobile.ezscrum.view.StoryCardAdapter;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.ProgressDialog;
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
import android.widget.GridView;

public class ProductBacklogGridViewActivity extends BaseActivity implements Runnable {
	private String mProjectID;
	private StoryCardAdapter mStoryCardAdapter;
	private GridView mGridView;
	private ProgressDialog mProgressDialog;
	private ProductBacklogItemManager mProductBacklogItemManager;
	private MenuItem mRefreshMenuItem;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.productbacklog_gridview);
		setTitle(R.string.productBacklog);

		// 從前一個activity取得資料
		Bundle bundle = this.getIntent().getExtras();
		mProjectID = bundle.getString("projectID");
		mProductBacklogItemManager = new ProductBacklogItemManager();

		// 設定左上角返回主頁面按鈕
		ActionBar bar = getActionBar();
		bar.setDisplayHomeAsUpEnabled(true);

		// 設定gridview
		mGridView = (GridView) findViewById(R.id.productBacklogGridView);
		refreshAdapter();

	}

	@Override
	public void onBackPressed() {
		finish();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.common, menu);
		inflater.inflate(R.menu.productbacklog, menu);
		MenuItem changeViewProductBacklog = menu.findItem(R.id.changeViewProductBacklog);
		mRefreshMenuItem = (MenuItem) menu.findItem(R.id.refreshProductBacklog);
		mRefreshMenuItem.setTitle(EzScrumAppUtil.getCurrentSystemTime());

		changeViewProductBacklog.setIcon(R.drawable.list_view);
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
		case android.R.id.home:
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * 點擊Add Story事件
	 * 
	 * @param item
	 */
	public void onAddStoryClick(MenuItem item) {
		LayoutInflater inflater = LayoutInflater.from(this);
		final View storyCardView = inflater.inflate(R.layout.storyitem, null);
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("New Story");
		builder.setView(storyCardView);
		DialogInterface.OnClickListener listener = getCardSaveBtnListener(storyCardView);
		// Button reference = new Button(context);
		builder.setPositiveButton("save", listener);
		builder.setNegativeButton("cancel", null);
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
		// refreshAdapter();
	}

	/**
	 * 點擊Refresh事件
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
		intent.setClass(this, ProductBacklogListViewActivity.class);
		startActivity(intent);
	}

	/**
	 * 建立Story card儲存事件
	 */
	private DialogInterface.OnClickListener getCardSaveBtnListener(
			final View view) {
		DialogInterface.OnClickListener saveBtnlistener = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				StoryObject newStory = getStoryObjectFromCardView(view);
				mProductBacklogItemManager.addProductBacklogItem(mProjectID,
						newStory);
				refreshAdapter();
			}
		};
		return saveBtnlistener;
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
		List<TagObject> tagList = new ArrayList<TagObject>(); // TODO:
																// 從view中取得tag

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

	/**
	 * 取得所有Story
	 * 
	 * @return
	 */
	private List<StoryObject> getProductBacklogItemList() {
		List<StoryObject> storyList = mProductBacklogItemManager
				.retrieveProductBacklogAllItems(mProjectID);
		return storyList;
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
		mStoryCardAdapter = new StoryCardAdapter(this,
				getProductBacklogItemList(), mProjectID);
		handler.sendEmptyMessage(0);
	}

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			mGridView.setAdapter(mStoryCardAdapter);
			mRefreshMenuItem.setTitle(EzScrumAppUtil.getCurrentSystemTime());
			mProgressDialog.dismiss(); // 將Progress關閉
		}
	};
}
